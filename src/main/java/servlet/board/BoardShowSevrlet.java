package servlet.board;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class BoardShowSevrlet
 */
@WebServlet("/BoardShowSevrlet")
public class BoardShowSevrlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			doPost(req,resp);
		}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String boardID = request.getParameter("boardID");
		String boardName = request.getParameter("boardName");
		request.getSession().setAttribute("boardName", boardName);
		switch (boardID) {
		case "main":
			response.sendRedirect("main.jsp");		
			break;
		case "write":
			response.sendRedirect("boardWrite.jsp");	
			break;
		default:
			if(boardName.equals("openchat1") || boardName.equals("openchat2")) {
				response.sendRedirect("./openChatEnter?roomID="+boardID+"&boardName="+boardName);
			}else {
				response.sendRedirect("boardShow.jsp?boardID="+boardID+"&boardName="+boardName);
				break;
			}
		}
		
//		return;
	}

}
