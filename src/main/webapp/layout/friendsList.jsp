<%@page import="database.user.UserDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="database.board.BoardDAO" %>
<%@ page import="database.board.BoardDTO" %>
<%@ page import="java.util.ArrayList" %>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html>
<%
	String userID = null;
	userID = request.getParameter("userID");
	if(userID == null) {
		session.setAttribute("messageType", "오류 메시지");
		session.setAttribute("messageContent", "현재 로그인이 되어 있지 않은 상태 입니다.");
		response.sendRedirect("index.jsp");
		return;
	}
%>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="col-xs-4" style="border: 1px solid white; width: 30%">
    	<div class="item item3 bg-grey2 text-light nav justify-content-center">
	    	<button class="btn bg-grey1"  onclick="changeFriendList('friend');" type="button">친구목록</button>
	    	<button class="btn bg-grey1" onclick="changeFriendList('chat');" type="button">메시지</button>
    	</div>
        <div class="row" style="padding-left: 20px;padding-top: 10px;">
            <div class="col-xs-12">
				<input class="form-control me-2" style="width: 200px; height: 30px;" id="searchWord" name="searchWord"
					onkeyup="changeFriendList('search');" type="text" placeholder="검색 내용을 입력하세요.">    
            </div>
        </div>
        <div class="item item5 text-light pt-3" style="padding-top:10px;">
        	<table class="table" style="text-align:center; margin: 0 auto; color:white;">
				<tbody id="boxTable">
						
				</tbody>
			</table>
        </div>
    </div>
</body>
</html>