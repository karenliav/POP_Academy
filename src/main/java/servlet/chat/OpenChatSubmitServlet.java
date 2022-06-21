package servlet.chat;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.chat.OpenChatDAO;

/**
 * Servlet implementation class OpenChatSubmitServlet
 */
@WebServlet("/OpenChatSubmitServlet")
public class OpenChatSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();
		String fromID = request.getParameter("fromID");
		String roomID = request.getParameter("roomID");
		String chatContent = request.getParameter("chatContent");
		String chatTable = request.getParameter("chatTable");
		if(fromID == null || fromID.equals("") || roomID == null || roomID.equals("")
				|| chatContent == null || chatContent.equals("")) {
			response.getWriter().write("0");
		} else {
			fromID = URLDecoder.decode(fromID, "UTF-8");
			roomID = URLDecoder.decode(roomID, "UTF-8");
			if(!fromID.equals((String) session.getAttribute("userID"))) {
				response.getWriter().write("");
				return;
			}
			chatContent = URLDecoder.decode(chatContent, "UTF-8");
			response.getWriter().write(new OpenChatDAO().submit(fromID,roomID,chatContent,chatTable)+ "");
		}
	}

}
