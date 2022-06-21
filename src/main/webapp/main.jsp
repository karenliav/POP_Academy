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
		String boardName = "board1";
		if(session.getAttribute("boardName") != null) {
			boardName = (String) session.getAttribute("boardName");
		}
		String pageNumber = "1";
		if(request.getParameter("pageNumber") != null){
			pageNumber = request.getParameter("pageNumber");
		} 
		try {
			Integer.parseInt(pageNumber);
		} catch (Exception e) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "페이지 번호가 잘못되었습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		String searchTitle = "";
		if(request.getParameter("searchTitle") != null){
			searchTitle = request.getParameter("searchTitle");
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
    	var listName = 'friend';
    	function changeFriendList(type) {
    		if(type != 'search'){
	    		listName = type;
    		}
			if(listName == 'friend'){
				searchFriendFunction();
			} else if(listName == 'chat') {
				chatBoxFunction();
			}
		}
    	function searchFriendFunction() {
    		var userID = '<%= userID %>';
    		var searchWord = $('#searchWord').val();
    		$.ajax({
				type: "POST",
				url: './friendList',
				data: {
					userID: encodeURIComponent(userID),
					searchWord: searchWord
				},
				success: function(data) {
					$('#boxTable').html('');
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					for(var i = 0; i < result.length; i++) {
						if(result[i][0].value == userID){
							result[i][0].value = result[i][1].value;
						} else {
							result[i][1].value = result[i][0].value;
						}
						addBox(result[i][0].value, result[i][1].value, result[i][2].value, result[i][3].value, result[i][4].value, result[i][5].value);
					}
				}
			});
			
		}
    	function chatBoxFunction() {
			var userID = '<%= userID %>';
			var searchWord = $('#searchWord').val();
			$.ajax({
				type: "POST",
				url: './chatBox',
				data: {
					userID: encodeURIComponent(userID),
					searchWord: searchWord
				},
				success: function(data) {
					$('#boxTable').html('');
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					for(var i = 0; i < result.length; i++) {
						if(result[i][0].value == userID){
							result[i][0].value = result[i][1].value;
						} else {
							result[i][1].value = result[i][0].value;
						}
						addBox(result[i][0].value, result[i][1].value, result[i][2].value, result[i][3].value, result[i][4].value, result[i][5].value);
					}
				}
			});
		}
		function addBox(lastID, toID, chatContent, chatTime, unread, profile) {
			$('#boxTable').append('<tr onclick="window.open(\'chat.jsp?toID=' + encodeURIComponent(toID) + '\')">' +
					'<td style="width: 100px;">' +
					'<img class="media-object img-circle" style="margin: 0 auto; max-width: 30px; max-height: 30px;" src="' + profile + '">' +
					'<h5>' + lastID + '</5></td>' +
					'<td>' +
					'<h5>' + chatContent +
					'<span class="label label-info">' + unread + '</span></h5>' +
					'<div class="pull-right">' + chatTime + '</div>' +
					'</td>' +
					'</tr>');
		}
		function getInfiniteBox() {
			setInterval(function() {
				changeFriendList(listName);
			},3000);
		}
		
    </script>
    <title>Partner Of Project</title>
</head>
<body>
    
<jsp:include page="layout/navbarTop.jsp" flush="false">
	<jsp:param value="main" name="pageName"/>
</jsp:include>
    
    <div class="container-fluid bg-main" style="min-height:800px;">
    
<jsp:include page="layout/groupButtonMiddle.jsp" flush="false">
	<jsp:param value="main" name="pageName"/>
	<jsp:param value="<%= boardName %>" name="boardName"/>
</jsp:include>

        <div class="container-fluid bg-black" style="width:100%; height: 100%;">
            <div class="row">
<%
	if(boardName.equals("board1") || boardName.equals("board2")) {
%>
<jsp:include page="layout/board1.jsp" flush="false">
	<jsp:param value="<%= boardName %>" name="boardName"/>
	<jsp:param value="<%= pageNumber %>" name="pageNumber"/>
	<jsp:param value="<%= searchTitle %>" name="searchTitle"/>
</jsp:include>
<%
	} else if (boardName.equals("openchat1") || boardName.equals("openchat2")) {
%>

<jsp:include page="layout/chatRoom.jsp" flush="false">
	<jsp:param value="<%= boardName %>" name="boardName"/>
	<jsp:param value="<%= pageNumber %>" name="pageNumber"/>
	<jsp:param value="<%= searchTitle %>" name="searchTitle"/>
</jsp:include>

<%
	}
%>
<jsp:include page="layout/friendsList.jsp" flush="false">
	<jsp:param value="<%= userID %>" name="userID"/>
</jsp:include>
                
            </div>
        </div>    
    </div>

<jsp:include page="layout/modalMiddle.jsp" flush="false"/>

<jsp:include page="layout/modalEnd.jsp" flush="false"/>

    <script type="text/javascript">
    $(document).ready(function() {
    	searchFriendFunction();
		getInfiniteBox();
    });
	</script>
</body>
</html>