package servlet.user;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.user.UserDAO;

/**
 * Servlet implementation class UserFriendListServlet
 */
@WebServlet("/UserFriendListServlet")
public class UserFriendListServlet extends HttpServlet {
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
				response.getWriter().write(friendList(userID,searchWord));
			} catch (Exception e) {
				response.getWriter().write("");
			}
		}
	}
	
	public String friendList(String userID, String searchWord) {
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		UserDAO userDAO = new UserDAO();
		String friends = userDAO.getFrineds(userID);
		String friendList[];
		ArrayList<String> searchList = new ArrayList<String>();
		if(!friends.equals("")) {
			friendList = friends.split(",");
			for(int i = 0; i < friendList.length; i++) {
				if(!searchWord.equals("")) {
					int find = friendList[i].indexOf(searchWord);
					if(find != -1) {
						searchList.add(friendList[i]);
					}
				} else {
					searchList.add(friendList[i]);
				}
			}
			if(searchList.size() != 0) {
				for(int j = 0; j < searchList.size(); j++) {
					String userProfile = "";
					userProfile = userDAO.getProfile(searchList.get(j));				
					result.append("[{\"value\": \"" + userID + "\"},");
					result.append("{\"value\": \"" + searchList.get(j) + "\"},");
					result.append("{\"value\": \"" + "" + "\"},");
					result.append("{\"value\": \"" + "" + "\"},");
					result.append("{\"value\": \"" + "" + "\"},");
					result.append("{\"value\": \"" + userProfile + "\"}]");
					if(j != searchList.size()-1) result.append(",");
				}
				result.append("], \"last\":\"" + "" + "\"}");
				return result.toString();
			} else {
				return "";
			}
			
		} else {
			return "";
		}
		
		
		
	}
}
