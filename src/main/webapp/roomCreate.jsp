<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		String boardName = (String) session.getAttribute("boardName");
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
    	<jsp:param value="boardWirte" name="pageName"/>	
    </jsp:include>
    
	<div class="container">
		<form method="post" action="./openChatCreate" enctype="multipart/form-data">
	        <table class="table table-bordered table-hover" style="text-align:center; border: 1px solid #dddddd">
	            <thead>
	                <tr>
	                    <th colspan="2"><h4>오픈채팅방 만들기</h4></th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>아이디</h5></td>
	                    <td><h5><%= userID %></h5>
	                    <input type="hidden" id="userID" name="userID" value="<%= userID %>"></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>채팅방 제목</h5></td>
	                    <td><input class="form-control" type="text" maxlength="50" name="roomTitle" placeholder="글 제목을 입력하세요."></td>
	                </tr>
	                
	                <tr>
	                    <td style="text-align: center;" colspan="2"><h5 style="color: red;" id="passwordCheckMessage"></h5><input class="btn btn-primary pull-right" type="submit" value="만들기"></td>
	                </tr>
	            </tbody>
	        </table>
	    </form>
	</div>
	
	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	<jsp:include page="layout/modalEnd.jsp" flush="false"/>

	<script type="text/javascript">
		$(document).ready(function() {
			
		}
		});
		$(document).on('click', '.browse', function() {
			var file = $(this).parent().parent().parent().find('.file');
			file.trigger('click');
		});
		$(document).on('change', '.file', function() {
			$(this).parent().find('.form-control').val($(this).val().replace(/c:\\fakepath\\/i, ''));
		});
	</script>
</body>
</html>