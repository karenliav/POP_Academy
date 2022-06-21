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
	%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css?v=3" type="text/css">
	<link rel="stylesheet" href="css/custom.css?v=3" type="text/css">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		function findFunction() {
			var userID = $('#findID').val();
			$.ajax({
				type: "POST",
				url: './UserFindServlet',
				data: {userID: userID},
				success: function(result) {
					if(result == -1) {
						$('#checkMessage').html('친구를 찾을 수 없습니다.');
						$('#checkType').attr('class', 'modal-content panel-warning');
						failFriend();
					} else {
						$('#checkMessage').html('친구 찾기에 성공했습니다.');
						$('#checkType').attr('class', 'modal-content panel-success');
						var data = JSON.parse(result);
						var profile = data.userProfile;
						getFriend(userID, profile);
					}
					$('#checkModal').modal("show");
				}
			});
		}
		function getFriend(findID, userProfile) {
			$('#friendResult').html('<thead>' +
					'<tr>' +
					'<th><h4>검색 결과</h4></th>' +
					'</tr>' +
					'</thead>' +
					'<tbody>' +
					'<tr>' +
					'<td style="text-align: center;">' + 
					'<img class="media-object img-circle" style="max-width: 300px; margin: 0 auto;" src="' + userProfile + '">' +
					'<h3>' + findID + '</h3><a href="./makeFriend?findID=' + encodeURIComponent(findID) + '" class="btn btn-primary pull-right">' + '친구 등록</a></td>' +
					'</tr>' +
					'</tbody>');
		}
		function failFriend() {
			$('#friendResult').html('');
		}
	</script>
<title>Partner Of Project</title>
</head>
<body>
	<jsp:include page="layout/navbarTop.jsp" flush="false">
    	<jsp:param value="find" name="pageName"/>	
    </jsp:include>
    
	<div class="container">
		<table class="table table-bordered table-hover" style="text-align:center; border: 1px solid #dddddd;">
			<thead>
                <tr>
                    <th colspan="2"><h4>친구 찾기</h4></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 110px;"><h5>친구 아이디</h5></td>
                    <td><input class="form-control" type="text" id="findID" name="findID" maxlength="20" placeholder="아이디를 입력하세요."></td> 
                </tr>
                <tr>
                	<td colspan="2"><button class="btn btn-primary pull-right" onclick="findFunction();">검색</button></td>
                </tr>
            </tbody>
		</table>
	</div>
	<div class="container">
		<table id="friendResult" class="table table-bordered table-hover" style="text-align:center; border: 1px solid #dddddd">
		</table>
	</div>
	
	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	<jsp:include page="layout/modalEnd.jsp" flush="false"/>
	
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</body>
</html>