package servlet.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.user.UserDAO;


public class UserMakeFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();
		String userID = (String) session.getAttribute("userID");
		String findID = request.getParameter("findID");
		if(findID.equals(userID)) {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "자기 자신은 친구로 등록할 수 없습니다.");
			response.sendRedirect("find.jsp");
			return;
		}
		UserDAO userDAO = new UserDAO();
		String friends = userDAO.getFrineds(userID);
		String friendList[];
		if(!friends.equals("")) {
			friendList = friends.split(",");
			for(int i = 0; i < friendList.length; i++) {
				if(friendList[i].equals(findID)) {
					request.getSession().setAttribute("messageType", "오류 메시지");
					request.getSession().setAttribute("messageContent", "이미 등록되어 있는 친구입니다.");
					response.sendRedirect("find.jsp");
					return;
				}
			}
			friends += ",";
		}
		friends += findID; 
		int result =  userDAO.makeFriend(friends, userID);
		if(result == 1) {
			request.getSession().setAttribute("messageType", "성공 메시지");
			request.getSession().setAttribute("messageContent", "친구 등록에 성공했습니다.");
			response.sendRedirect("find.jsp");
			return;
		} else {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "데이터베이스 오류가 발생했습니다.");
			response.sendRedirect("find.jsp");
			return;
			
		}
	}

}
