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

import database.chat.ChatRoomDAO;
import database.chat.ChatRoomDTO;
import database.chat.OpenChatDAO;
import database.chat.OpenChatDTO;

/**
 * Servlet implementation class OpenChatListServlet
 */
@WebServlet("/OpenChatListServlet")
public class OpenChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		String roomID = request.getParameter("roomID");
		String chatTable = request.getParameter("chatTable");
		String listType = request.getParameter("listType");
		if(userID == null || userID.equals("") || roomID == null || roomID.equals("")
				|| chatTable == null || chatTable.equals("") || listType == null || listType.equals("")) {
			response.getWriter().write("");
		} else {
			try {
				HttpSession session = request.getSession();
				if(!URLDecoder.decode(userID, "UTF-8").equals((String) session.getAttribute("userID"))) {
					response.getWriter().write("");
					return;
				}
				response.getWriter().write(
						getID(URLDecoder.decode(userID, "UTF-8"),URLDecoder.decode(roomID, "UTF-8"),
								URLDecoder.decode(chatTable, "UTF-8"),listType));
			} catch (Exception e) {
				response.getWriter().write("");
			}
			
		}
	}
	public String getID(String userID, String roomID, String chatTable, String listType) {
		
		ChatRoomDAO chatRoomDAO = new ChatRoomDAO();
		ArrayList<ChatRoomDTO> chatRoomDTO = new ArrayList<ChatRoomDTO>();
		chatRoomDTO = chatRoomDAO.getRoomInfo(roomID, chatTable);
		int userNumber = 0;
		String userID1 = chatRoomDTO.get(0).getUserID1(); 
		String userID2 = chatRoomDTO.get(0).getUserID2(); 
		String userID3 = chatRoomDTO.get(0).getUserID3(); 
		String userID4 = chatRoomDTO.get(0).getUserID4(); 
		String userID5 = chatRoomDTO.get(0).getUserID5();
		String checkUserID[] = {userID1,userID2,userID3,userID4,userID5};
		for(int i =0; i< 5;i++) {
			if(checkUserID[i].equals(userID)) {
				userNumber = i+1;
				break;
			}
		}
		String userNum = Integer.toString(userNumber);
		String num = "";
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		OpenChatDAO openChatDAO = new OpenChatDAO();
		ArrayList<OpenChatDTO> chatList = openChatDAO.getChatListByID(userID, roomID, chatTable, listType);
		for (int i = 0;i < chatList.size();i++) {
			for(int j =0; j< 5;j++) {
				if(checkUserID[j].equals(chatList.get(i).getFromID())) {
					num = Integer.toString(j+1);
					break;
				}
			}
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"},");
			result.append("{\"value\": \"" + num + "\"}]");
			if(i != chatList.size() - 1) result.append(",");
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		openChatDAO.readChat(userID, roomID, chatTable, userNumber);
		return result.toString();
	}

}