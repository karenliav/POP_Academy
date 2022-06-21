package servlet.chat;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.chat.ChatRoomDAO;
import database.chat.ChatRoomDTO;

/**
 * Servlet implementation class OpenChatEnterServlet
 */
@WebServlet("/OpenChatEnterServlet")
public class OpenChatEnterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String roomID = request.getParameter("roomID");
		String boardName = request.getParameter("boardName");
//		String roomPw = "";
//		if(!request.getParameter("roomPw").equals("")||request.getParameter("roomPw") != null)
//		{
//			roomPw = request.getParameter("boardPw");
//		}
		String chatTable = null;
		switch (boardName) {
		case "openchat1" :
			chatTable = "chatroom";
			break;
		case "openchat2" :
			chatTable = "chatroom2";
			break;
		}
		HttpSession session = request.getSession();
		request.getSession().setAttribute("boardName", boardName);
		switch (roomID) {
		case "main":
			response.sendRedirect("main.jsp");		
			break;
		case "write":
			response.sendRedirect("roomCreate.jsp");	
			break;
		default:
			ChatRoomDAO chatRoomDAO = new ChatRoomDAO();
			ArrayList<ChatRoomDTO> chatRoomDTO = new ArrayList<ChatRoomDTO>();
			chatRoomDTO = chatRoomDAO.getRoomInfo(roomID, chatTable);
			boolean roomFull = true;
			String userID = (String) session.getAttribute("userID");
			String userID1 = chatRoomDTO.get(0).getUserID1(); 
			String userID2 = chatRoomDTO.get(0).getUserID2(); 
			String userID3 = chatRoomDTO.get(0).getUserID3(); 
			String userID4 = chatRoomDTO.get(0).getUserID4(); 
			String userID5 = chatRoomDTO.get(0).getUserID5();
			String checkUserID[] = {userID1,userID2,userID3,userID4,userID5};
//			String roomPw = chatRoomDTO.get(0).getRoomPw(); 
			
			// 새로 작성한 부분
//			int personnel = chatRoomDTO.get(0).getPersonnel();
//			String roomUsers = chatRoomDTO.get(0).getRoomUser();
//			String roomUserList[];
//			if(!roomUsers.equals("")) {
//				roomUserList = roomUsers.split(",");
//				for(int i =0; i< roomUserList.length;i++) {
//					if(roomUserList[i].equals(userID)) {
//						roomFull = false;
//						break;
//					}
//				}
//				if(roomUserList.length < personnel && roomFull == true) {
//					addUser(userID, session, response);
//					roomFull = false;
//				}
//			} else if(roomUsers.equals("")) { 
//				addUser(userID, session, response);
//				roomFull = false;
//			}
			
			for(int i =0; i< 5;i++) {
				if(checkUserID[i].equals(userID)) {
					roomFull = false;
					break;
				}
				if(checkUserID[i].equals("") && roomFull == true) {
					int dbCheck = chatRoomDAO.userUpdate(roomID, boardName, userID, i+1);
					if(dbCheck == -1) {
						session.setAttribute("messageType", "오류 메시지");
						session.setAttribute("messageContent", "데이터베이스 오류");
						response.sendRedirect("main.jsp");
						return;
					}
					roomFull = false;
					break;
				}
			}
			if(roomFull) {
				session.setAttribute("messageType", "오류 메시지");
				session.setAttribute("messageContent", "채팅방에 자리가 없습니다.");
				response.sendRedirect("main.jsp");
				return;
			} else {
				response.sendRedirect("openChat.jsp?roomID="+roomID+"&chatTable="+chatTable);				
			}
		}
	}
	
//	public void addUser(String userID, HttpSession session, HttpServletResponse response) {
//		ChatRoomDAO chatRoomDAO = new ChatRoomDAO();
//		int addUser = chatRoomDAO.addRoomUser(userID);
//		if(addUser == -1) {
//			session.setAttribute("messageType", "오류 메시지");
//			session.setAttribute("messageContent", "데이터베이스 오류");
//			response.sendRedirect("main.jsp");
//			return;
//		}
//	}

}
