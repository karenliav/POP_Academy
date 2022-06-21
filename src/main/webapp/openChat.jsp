<%@page import="database.chat.ChatRoomDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="database.user.UserDAO"%>
<%@page import="database.chat.OpenChatDAO"%>
<%@page import="database.chat.ChatRoomDAO"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		String chatTable = null;
		if(request.getParameter("chatTable") != null){
			chatTable = (String) request.getParameter("chatTable");
		}
		String roomID = null;
		ArrayList<ChatRoomDTO> chatRoomDTO = new ArrayList<ChatRoomDTO>();
		ChatRoomDTO chatRoomInfo = new ChatRoomDTO();
		if(request.getParameter("roomID") != null){
			ChatRoomDAO chatRoomDAO = new ChatRoomDAO();
			int checkRoomID = chatRoomDAO.registerCheck(URLDecoder.decode((String) request.getParameter("roomID"), "UTF-8"));
			if(checkRoomID == 0){
				roomID = (String) request.getParameter("roomID");
				chatRoomDTO = chatRoomDAO.getRoomInfo(roomID,chatTable);
				chatRoomInfo = chatRoomDTO.get(0);
			}
		}
		if(userID == null) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "현재 로그인이 되어 있지 않은 상태 입니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		if(roomID == null || URLDecoder.decode(roomID, "UTF-8").equals("")) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "대화방이 지정되지 않았습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		String userID1 = chatRoomInfo.getUserID1(); 
		String userID2 = chatRoomInfo.getUserID2(); 
		String userID3 = chatRoomInfo.getUserID3(); 
		String userID4 = chatRoomInfo.getUserID4(); 
		String userID5 = chatRoomInfo.getUserID5();
		UserDAO userDAO = new UserDAO();
		String user1Profile = "";
		String user2Profile = "";
		String user3Profile = "";
		String user4Profile = "";
		String user5Profile = "";
		if(!userID1.equals("")){
			user1Profile = URLDecoder.decode(userDAO.getProfile(URLDecoder.decode(userID1, "UTF-8")), "UTF-8");		
		}
		if(!userID2.equals("")){
			user2Profile = URLDecoder.decode(userDAO.getProfile(URLDecoder.decode(userID2, "UTF-8")), "UTF-8");		
		}
		if(!userID3.equals("")){
			user3Profile = URLDecoder.decode(userDAO.getProfile(URLDecoder.decode(userID3, "UTF-8")), "UTF-8");		
		}
		if(!userID4.equals("")){
			user4Profile = URLDecoder.decode(userDAO.getProfile(URLDecoder.decode(userID4, "UTF-8")), "UTF-8");		
		}
		if(!userID5.equals("")){
			user5Profile = URLDecoder.decode(userDAO.getProfile(URLDecoder.decode(userID5, "UTF-8")), "UTF-8");		
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
		function autoClosingAlert(selector, delay) {
			var alert = $(selector).alert();
			alert.show();
			window.setTimeout(function() { alert.hide() }, delay);
		}
		function submitFunction() {
			var fromID = '<%= userID %>';
			var roomID = '<%= roomID %>';
			var chatContent = $('#chatContent').val();
			var chatTable = '<%= chatTable %>';
			$.ajax({
				type: 'POST',
				url: './openChatSubmit',
				data: {
					fromID: encodeURIComponent(fromID),
					roomID: encodeURIComponent(roomID),
					chatContent: encodeURIComponent(chatContent),
					chatTable: encodeURIComponent(chatTable)
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
			var userID = '<%= userID %>';
			var roomID = '<%= roomID %>';
			var chatTable = '<%= chatTable %>';
			$.ajax({
				type: "POST",
				url: "./openChatList",
				data: {
					userID: encodeURIComponent(userID),
					roomID: encodeURIComponent(roomID),
					chatTable: encodeURIComponent(chatTable),
					listType: type
				},
				success: function(data){
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					for(var i = 0; i < result.length; i++) {
						if(result[i][0].value == userID){
							result[i][0].value = '나';
						}
						addChat(result[i][0].value, result[i][1].value, result[i][2].value, result[i][3].value);
					}
					lastID = Number(parsed.last);
				}
			});
		}
		function addChat(chatName, chatContent, chatTime, num) {
			var profile = null;
			
			switch(num) {
			case '1':
				profile = '<%= user1Profile %>';
				break;
			case '2':
				profile = '<%= user2Profile %>';
				break;
			case '3':
				profile = '<%= user3Profile %>';
				break;
			case '4':
				profile = '<%= user4Profile %>';
				break;
			case '5':
				profile = '<%= user5Profile %>';
				break;
			}
			$('#chatList').append('<div class="row">' +
						'<div class="col-lg-12">' +
						'<div class="media">' +
						'<a class="pull-left" href="#">' +
						'<img class="media-object img-circle" style="width: 30px; height: 30px;" src="' + profile + '" alt="">' +
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
			$('#chatList').scrollTop($('#chatList')[0].scrollHeight);
		}
		function getInfiniteChat() {
			setInterval(function() {
				chatListFunction(lastID);
			}, 3000);
		}
	</script>
<title>Partner Of Project</title>
</head>
<body>
	<div class="container bootstrap snippet">
		<div class="row">
			<div class="col-xs-12">
				<div class="portlet portlet-default">
					<div class="portlet-heading">
						<div class="portlet-title">
							<h4><i class="fa fa-circle text-green"></i>오픈 채팅방</h4>
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
			chatListFunction(0);
			getInfiniteChat();
		});
	</script>
</body>
</html>