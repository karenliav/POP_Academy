<%@page import="database.user.UserDAO"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		String toID = null;
		if(request.getParameter("toID") != null) {
			UserDAO userDAO = new UserDAO();
			int checkToID = userDAO.registerCheck(URLDecoder.decode((String) request.getParameter("toID"), "UTF-8"));
			if(checkToID == 0){
				toID = (String) request.getParameter("toID");
			}
		}
		if(userID == null) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "현재 로그인이 되어 있지 않은 상태 입니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		if(toID == null || URLDecoder.decode(toID, "UTF-8").equals("")) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "대화 상대가 지정되지 않았습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		if(userID.equals(URLDecoder.decode(toID, "UTF-8"))) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "자기 자신에게는 메세지를 보낼수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		String fromProfile = URLDecoder.decode(new UserDAO().getProfile(URLDecoder.decode(userID, "UTF-8")), "UTF-8");
		String toProfile = URLDecoder.decode(new UserDAO().getProfile(URLDecoder.decode(toID, "UTF-8")), "UTF-8");
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css?v=3" type="text/css">
	<link rel="stylesheet" href="css/custom.css?v=3" type="text/css">
	<title>Partner Of Project</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		function autoClosingAlert(selector, delay) {
			var alert = $(selector).alert();
			alert.show();
			window.setTimeout(function() { alert.hide() }, delay);
		}
		function submitFunction() {
			var fromID = '<%= userID %>';
			var toID = '<%= toID %>';
			var chatContent = $('#chatContent').val();
			$.ajax({
				type: 'POST',
				url: './chatSubmitServlet',
				data: {
					fromID: encodeURIComponent(fromID),
					toID: encodeURIComponent(toID),
					chatContent: encodeURIComponent(chatContent)
				},
				success: function(result) {
					if(result == 1) {
						autoClosingAlert('#successMessage', 2000);
					} else if (result == 0){
						autoClosingAlert('#dangerMessage', 2000);
					} else {
						autoClosingAlert('#warningMessage', 2000);
					}
				}
			});
			$('#chatContent').val('');
			chatListFunction(lastID);
		}
		var lastID = 0;
		function chatListFunction(type) {
			var fromID = '<%= userID %>';
			var toID = '<%= toID %>';
			$.ajax({
				type: "POST",
				url: "./chatListServlet",
				data: {
					fromID: encodeURIComponent(fromID),
					toID: encodeURIComponent(toID),
					listType: type
				},
				success: function(data){
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					for(var i = 0; i < result.length; i++) {
						if(result[i][0].value == fromID){
							result[i][0].value = '나';
						}
						addChat(result[i][0].value, result[i][2].value, result[i][3].value);
					}
					lastID = Number(parsed.last);
				}
			});
		}
		function addChat(chatName, chatContent, chatTime) {
			if(chatName == '나') {
				$('#chatList').append('<div class="row">' +
						'<div class="col-lg-12">' +
						'<div class="media">' +
						'<a class="pull-left" href="#">' +
						'<img class="media-object img-circle" style="width: 30px; height: 30px;" src="<%= fromProfile %>" alt="">' +
						'</a>' +
						'<div class="media-body">' +
						'<h4 class="media-heading">' +
						chatName +
						'<span class="small pull-right">' +
						chatTime +
						'</span>' +
						'</h4>' +
						'<p>' +
						chatContent +
						'</p>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'<hr>');
			} else {
				$('#chatList').append('<div class="row">' +
						'<div class="col-lg-12">' +
						'<div class="media">' +
						'<a class="pull-left" href="#">' +
						'<img class="media-object img-circle" style="width: 30px; height: 30px;" src="<%= toProfile %>" alt="">' +
						'</a>' +
						'<div class="media-body">' +
						'<h4 class="media-heading">' +
						chatName +
						'<span class="small pull-right">' +
						chatTime +
						'</span>' +
						'</h4>' +
						'<p>' +
						chatContent +
						'</p>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'<hr>');
			}
			$('#chatList').scrollTop($('#chatList')[0].scrollHeight);
		}
		function getInfiniteChat() {
			setInterval(function() {
				chatListFunction(lastID);
			}, 3000);
		}
	</script>
</head>
<body>
	<div class="container bootstrap snippet">
		<div class="row">
			<div class="col-xs-12">
				<div class="portlet portlet-default">
					<div class="portlet-heading">
						<div class="portlet-title">
							<h4><i class="fa fa-circle text-green"></i>채팅방</h4>
						</div>
						<div class="clearfix"></div>
					</div>
					<div id="chat" class="panel-collapse collapse in">
						<div id="chatList" class="portlet-body chat-widget" style="overflow-y:auto; width:auto; height:600px;">
						
						</div>
		                <div class="portlet-footer">
							<div class="row" style="height:90px;">
								<div class="form-group col-xs-10">
									<textarea style="height:80px;" id="chatContent" class="form-control" placeholder="메세지를 입력하세요." maxlength="100"></textarea>
								</div>
								<div class="form-group col-xs-2">
									<button type="button" class="btn btn-default pull-right" onclick="submitFunction();">전송</button>
									<div class="clearfix"></div>
								</div>
							</div>
								<!-- </form> -->
		                </div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="alert alert-success" id="successMessage" style="display: none;">
		<strong>메시지 전송에 성공하였습니다.</strong>
	</div>
	<div class="alert alert-danger" id="dangerMessage" style="display: none;">
		<strong>이름과 내용을 모두 입력해주세요.</strong>
	</div>
	<div class="alert alert-warning" id="warningMessage" style="display: none;">
		<strong>데이터베이스 오류가 발생했습니다.</strong>
	</div>
	
	<jsp:include page="layout/modalMiddle.jsp" flush="false"/>
	
	<script type="text/javascript">
		$(document).ready(function() {
//			chatListFunction('ten');
			chatListFunction(0);
			getInfiniteChat();
		});
	</script>
</body>
</html>