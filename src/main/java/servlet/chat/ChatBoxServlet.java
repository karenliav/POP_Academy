package servlet.chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.chat.ChatDAO;
import database.chat.ChatDTO;
import database.user.UserDAO;


/**
 * Servlet implementation class ChatBoxServlet
 */
@WebServlet("/ChatBoxServlet")
public class ChatBoxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		String searchWord = request.getParameter("searchWord");
		if(userID == null || userID.equals("")) {
			response.getWriter().write("");
		} else {
			try {
				userID = URLDecoder.decode(userID, "UTF-8");
				HttpSession session = request.getSession();
				if(!userID.equals((String) session.getAttribute("userID"))) {
					response.getWriter().write("");
					return;
				}
				response.getWriter().write(getBox(userID,searchWord));
			} catch (Exception e) {
				response.getWriter().write("");
			}
		}
	}
	
	public String getBox(String userID, String searchWord) {
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
		ArrayList<ChatDTO> chatList1 = chatDAO.getBox(userID);
		ChatDTO chat = new ChatDTO();
		if(chatList1.size() == 0) return "";
		for (int j = 0; j < chatList1.size(); j++) {
			if(!searchWord.equals("")) {
				int find = 0;
				if(userID.equals(chatList1.get(j).getToID())) {
					find = chatList1.get(j).getFromID().indexOf(searchWord);
				} else {
					find = chatList1.get(j).getToID().indexOf(searchWord);
				}
				if(find != -1) {
					chat = chatList1.get(j);
					chatList.add(chat);
				}
			} else {
				chat = chatList1.get(j);
				chatList.add(chat);
			}
		}
		if(chatList.size() == 0) return "";
		for (int i = chatList.size() - 1;i >=0;i--) {
			String unread = "";
			String userProfile = "";
			if(userID.equals(chatList.get(i).getToID())) {
				unread = chatDAO.getUnreadChat(chatList.get(i).getFromID(), userID)+"";
				if(unread.equals("0")) unread = "";
			}
			if(userID.equals(chatList.get(i).getToID())) {
				userProfile = new UserDAO().getProfile(chatList.get(i).getFromID());
			} else {
				userProfile = new UserDAO().getProfile(chatList.get(i).getToID());				
			}
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"},");
			result.append("{\"value\": \"" + unread + "\"},");
			result.append("{\"value\": \"" + userProfile + "\"}]");
			if(i != 0) result.append(",");
		}
		result.append("], \"last\":\"" + chatList.get(0).getChatID() + "\"}");
		return result.toString();
	}
}
