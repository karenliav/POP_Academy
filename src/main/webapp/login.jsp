<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if(userID != null) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "현재 로그인이 되어 있는 상태 입니다.");
			response.sendRedirect("main.jsp");
			return;
		}
	%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css?v=3" type="text/css">
	<link rel="stylesheet" href="css/custom.css?v=3" type="text/css">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
<title>Partner Of Project</title>
</head>
<body>
	<jsp:include page="layout/navbarTop.jsp" flush="false">
    	<jsp:param value="login" name="pageName"/>	
    </jsp:include>
    
	<div class="container">
		<form method="post" action="./userLogin">
			<table class="table table-bordered table-hover" style="text-align:center; border: 1px solid #dddddd">
				<thead>
	                <tr>
	                    <th colspan="2"><h4>로그인</h4></th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td style="width: 110px;"><h5>아이디</h5></td>
	                    <td><input class="form-control" type="text" id="userID" name="userID" maxlength="20" placeholder="아이디를 입력하세요."></td> 
	                </tr>
	                <tr>
	                    <td style="width: 110px;"><h5>비밀번호</h5></td>
	                    <td><input class="form-control" type="password" id="userPassword" name="userPassword" maxlength="20" placeholder="비밀번호를 입력하세요."></td>
	                </tr>
	                <tr>
	                	<td style="text-align: left;" colspan="2"><input class="btn btn-primary pull-right" type="submit" value="로그인"></td>
	                </tr>
	            </tbody>
			</table>
		</form>
	</div>
	
	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	<jsp:include page="layout/modalEnd.jsp" flush="false"/>
</body>
</html>