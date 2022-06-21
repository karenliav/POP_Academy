package servlet.chat;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import database.board.BoardDAO;
import database.chat.ChatRoomDAO;

/**
 * Servlet implementation class OpenChatCreateServlet
 */
@WebServlet("/OpenChatCreateServlet")
public class OpenChatCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 1024 * 1024;
		String savePath = request.getRealPath("/upload/openchatfile").replace("\\\\", "/");
		try {
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (Exception e) {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "파일 크기는 10MB를 넘을 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		String boardName = (String) session.getAttribute("boardName");
		if(!userID.equals((String) session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		String roomTitle = multi.getParameter("roomTitle");
		
		if(roomTitle == null || roomTitle.equals("")) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "내용을 모두 채워주세요.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		ChatRoomDAO chatRoomDAO = new ChatRoomDAO();
		chatRoomDAO.create(roomTitle, boardName);
		session.setAttribute("messageType", "성공 메시지");
		session.setAttribute("messageContent", "성공적으로 게시물이 작성되었습니다.");
		response.sendRedirect("main.jsp");
		return;
	}

}
