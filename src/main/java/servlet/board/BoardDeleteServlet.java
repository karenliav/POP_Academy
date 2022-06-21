package servlet.board;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.board.BoardDAO;
import database.board.BoardDTO;


public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();
		String userID = (String) session.getAttribute("userID");
		String boardID = request.getParameter("boardID");
		String boardName = (String) session.getAttribute("boardName");
		if(boardID == null || boardID.equals("")){
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		BoardDAO boardDAO = new BoardDAO();
		BoardDTO board = boardDAO.getBoard(boardID,boardName);
		if(!userID.equals(board.getUserID())) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		String savePath = request.getRealPath("/upload/boardfile").replace("\\\\", "/");
		String prev = boardDAO.getRealFile(boardID,boardName);
		int result = boardDAO.delete(boardID,boardName);
		if(result == -1) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		} else {
			File prevFile = new File(savePath + "/" + prev);
			if(prevFile.exists()) {
				prevFile.delete();
			}
			session.setAttribute("messageType", "성공 메시지");
			session.setAttribute("messageContent", "삭제에 성공했습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
	}
}
