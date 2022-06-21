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
			response.sendRedirect("index.jsp");
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
	<script type="text/javascript">
        function registerCheckFunction() {
			var userID = $('#userID').val();
			$.ajax({
				type: 'POST',
				url: './UserRegisterCheckServlet',
				data: {userID: userID},
				success: function(result) {
					if(result == 1) {
						$('#checkMessage').html('사용할 수 있는 아이디입니다.');
						$('#checkType').attr('class', 'modal-content panel-success');
					} else {
						$('#checkMessage').html('사용할 수 없는 아이디입니다.');
						$('#checkType').attr('class', 'modal-content panel-warning');
					}
					$('#checkModal').modal("show");
				}
			});
		}
		
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
    	<jsp:param value="join" name="pageName"/>
    </jsp:include>
    
	<div class="container">
		<form method="post" action="./userRegister">
	        <table class="table table-bordered table-hover" style="text-align:center; border: 1px solid #dddddd;">
	            <thead>
	                <tr>
	                    <th colspan="3"><h4>회원 등록</h4></th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>아이디</h5></td>
	                    <td><input class="form-control" type="text" id="userID" name="userID" maxlength="20" placeholder="아이디를 입력하세요."></td>
	                    <td style="width: 110px;"><button class="btn btn-primary" onclick="registerCheckFunction();" type="button">중복체크</button></td>
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
	                    <td colspan="2"><input class="form-control" type="text" id="userName" name="userName" maxlength="20" placeholder="이름을 입력하세요."></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>나이</h5></td>
	                    <td colspan="2"><input class="form-control" type="number" id="userAge" name="userAge" maxlength="20" placeholder="나이를 입력하세요."></td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>성별</h5></td>
	                    <td colspan="2">
	                        <div class="form-group" style="text-align: center; margin: 0 auto;">
	                            <div class="btn-group" data-toggle="buttons">
	                                <label class="btn btn-primary active">
	                                    <input type="radio" name="userGender" autocomplete="off" value="남자" checked>남자
	                                </label>
	                                <label class="btn btn-primary">
	                                    <input type="radio" name="userGender" autocomplete="off" value="여자">여자
	                                </label>
	                            </div>
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td style="width: 110px; text-align: center;"><h5>이메일</h5></td>
	                    <td colspan="2"><input class="form-control" type="email" id="userEmail" name="userEmail" maxlength="20" placeholder="이메일을 입력하세요."></td>
	                </tr>
	                <tr>
	                    <td style="text-align: center;" colspan="3"><h5 style="color: red;" id="passwordCheckMessage"></h5><input class="btn btn-primary pull-right" type="submit" value="등록"></td>
	                </tr>
	            </tbody>
	        </table>
	    </form>
	</div>
	
	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	<jsp:include page="layout/modalEnd.jsp" flush="false"/>
</body>
</html>