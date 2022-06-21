<%@page import="database.user.UserDAO"%>
<%@page import="database.user.UserDTO"%>
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
		UserDTO user = new UserDAO().getUser(userID);
	%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css?v=3" type="text/css">
	<link rel="stylesheet" href="css/custom.css?v=3" type="text/css">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		function passwordCheckFunction() {
			var userPassword1 = $('#userPassword1').val();
			var userPassword2 = $('#userPassword2').val();
			if(userPassword1 != userPassword2) {
				$('#passwordCheckMessage').html('비밀번호가 서로 일치하지 않습니다.');
			} else {
				$('#passwordCheckMessage').html('');
			}
		}
	</script>
<title>Partner Of Project</title>
</head>
<body>
	<jsp:include page="layout/navbarTop.jsp" flush="false">
    	<jsp:param value="update" name="pageName"/>	
    </jsp:include>
    
	<div class="container">
		<form method="post" action="./userUpdate">
	        <table class="table table-bordered table-hover" style="text-align:center; border: 1px solid #dddddd">
	            <thead>
	                <tr>
	                    <th colspan="2"><h4>회원 정보 수정</h4></th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>아이디</h5></td>
	                    <td><h5><%= user.getUserID() %></h5>
	                    <input class="form-control" type="hidden" id="userID" name="userID" value="<%= user.getUserID() %>"></td>
	                    
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>비밀번호</h5></td>
	                    <td colspan="2"><input onkeyup="passwordCheckFunction();" class="form-control" type="password" id="userPassword1" name="userPassword1" maxlength="20" placeholder="비밀번호를 입력하세요."></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>비밀번호확인</h5></td>
	                    <td colspan="2"><input onkeyup="passwordCheckFunction();" class="form-control" type="password" id="userPassword2" name="userPassword2" maxlength="20" placeholder="비밀번호 확인을 입력하세요."></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>이름</h5></td>
	                    <td colspan="2"><input class="form-control" type="text" id="userName" name="userName" maxlength="20" placeholder="이름을 입력하세요." value="<%= user.getUserName() %>"></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>나이</h5></td>
	                    <td colspan="2"><input class="form-control" type="number" id="userAge" name="userAge" maxlength="20" placeholder="나이를 입력하세요." value="<%= user.getUserAge() %>"></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>성별</h5></td>
	                    <td colspan="2">
	                        <div class="form-group" style="text-align: center; margin: 0 auto;">
	                            <div class="btn-group" data-toggle="buttons">
	                                <label class="btn btn-primary <% if(user.getUserGender().equals("남자")) out.print("active"); %>">
	                                    <input type="radio" name="userGender" autocomplete="off" value="남자" <% if(user.getUserGender().equals("남자")) out.print("checked"); %>>남자
	                                </label>
	                                <label class="btn btn-primary <% if(user.getUserGender().equals("여자")) out.print("active"); %>">
	                                    <input type="radio" name="userGender" autocomplete="off" value="여자" <% if(user.getUserGender().equals("여자")) out.print("checked"); %>>여자
	                                </label>
	                            </div>
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>이메일</h5></td>
	                    <td colspan="2"><input class="form-control" type="email" id="userEmail" name="userEmail" maxlength="20" placeholder="이메일을 입력하세요." value="<%= user.getUserEmail() %>"></td>
	                </tr>
	                <tr>
	                    <td style="text-align: center;" colspan="2"><h5 style="color: red;" id="passwordCheckMessage"></h5><input class="btn btn-primary pull-right" type="submit" value="수정"></td>
	                </tr>
	            </tbody>
	        </table>
	    </form>
	</div>
	
	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	<jsp:include page="layout/modalEnd.jsp" flush="false"/>

	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>	
</body>
</html>