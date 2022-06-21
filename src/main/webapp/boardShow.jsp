<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="database.board.BoardDAO" %>
<%@ page import="database.board.BoardDTO" %>
<!DOCTYPE html>
<html>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if(userID == null) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "현재 로그인이 되어 있지 않은 상태 입니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		String boardID = null;
		if(request.getParameter("boardID") != null) {
			boardID = (String) request.getParameter("boardID");
		}
		if(boardID == null || boardID.equals("")) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "게시물을 선택해주세요.");
			response.sendRedirect("main.jsp");
			return;
		}
		String boardName = null;
		if(session.getAttribute("boardName") != null) {
			boardName = (String) session.getAttribute("boardName");
		}
		BoardDAO boardDAO = new BoardDAO();
		BoardDTO board = boardDAO.getBoard(boardID, boardName);
		if(board.getBoardAvailable() == 0) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "삭제된 게시물입니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		boardDAO.hit(boardID, boardName);
	%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css?v=3" type="text/css">
	<link rel="stylesheet" href="css/custom.css?v=3" type="text/css">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
	
	</script>
<title>Partner Of Project</title>
</head>
<body>
	<jsp:include page="layout/navbarTop.jsp" flush="false">
    	<jsp:param value="boardShow" name="pageName"/>
    </jsp:include>

	<div class="container">
		<table class="table table-boardered table-hover" style="text-align: center; border: 1px solid #dddddd;">
			<thead>
				<tr>
					<th colspan="4"><h4>게시물</h4></th>
				</tr>
				<tr>
					<td style="background-color: #fafafa; color: #000000; width: 80px;"><h5>제목</h5></td>
					<td colspan="3"><h5><%= board.getBoardTitle() %></h5></td>
				</tr>
				<tr>
					<td style="background-color: #fafafa; color: #000000; width: 80px;"><h5>작성자</h5></td>
					<td colspan="3"><h5><%= board.getUserID() %></h5></td>
				</tr>
				<tr>
					<td style="background-color: #fafafa; color: #000000; width: 80px;"><h5>작성날짜</h5></td>
					<td><h5><%= board.getBoardDate() %></h5></td>
					<td style="background-color: #fafafa; color: #000000; width: 80px;"><h5>조회수</h5></td>
					<td><h5><%= board.getBoardHit() %></h5></td>
				</tr>
				<tr>
					<td style="vertical-align: middle; min-height: 150px; background-color: #fafafa; color: #000000; width: 80px;"><h5>글 내용</h5></td>
					<td colspan="3" style="text-align: left;"><h5><%= board.getBoardContent() %></h5></td>
				</tr>
				<tr>
					<td style="background-color: #fafafa; color: #000000; width: 80px;"><h5>첨부파일</h5></td>
					<td colspan="3"><h5><a href="boardDownload.jsp?boardID=<%= board.getBoardID() %>"><%= board.getBoardFile() %></a></h5></td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="5" style="text-align: right;">
						<a href="main.jsp" class="btn btn-primary">목록</a>
						<a href="boardReply.jsp?boardID=<%= board.getBoardID() %>" class="btn btn-primary">답변</a>
						<%
							if(userID.equals(board.getUserID())) {
						%>
						<a href="boardUpdate.jsp?boardID=<%= board.getBoardID() %>" class="btn btn-primary">수정</a>
						<a href="boardDelete?boardID=<%= board.getBoardID() %>" class="btn btn-primary" onclick="return confirm('정말로 삭제하시겠습니까?')">삭제</a>
						<%	
							}
						%>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	
	<%
		if(userID != null) {
	%>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
	<%
		}
	%>
</body>
</html>