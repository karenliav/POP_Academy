<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html>
<%
String pageName = URLDecoder.decode((String) request.getParameter("pageName"), "UTF-8");
String boardName = URLDecoder.decode((String) request.getParameter("boardName"), "UTF-8");
%>
<head>

<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="item item13">
        <div class="container-fluid">
            <div class="btn-group">
                <div class="btn-group">
                    <button type="button" style="width: 200px;height: 50px;" class="btn btn-light dropdown-toggle" data-toggle="dropdown">게시판 <span class="caret"></span></button>
                    <ul class="dropdown-menu" role="menu">
                        <li <% if(boardName.equals("board1")) out.print(" class=\"active\"");%>><a href="./boardShow?boardID=main&boardName=board1">공지 사항</a></li>
                        <li <% if(boardName.equals("board2")) out.print(" class=\"active\"");%>><a href="./boardShow?boardID=main&boardName=board2">자유 게시판</a></li>
                        
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" style="width: 200px;height: 50px;" class="btn btn-light dropdown-toggle" data-toggle="dropdown">채팅방 <span class="caret"></span></button>
                    <ul class="dropdown-menu" role="menu">
                        <li <% if(boardName.equals("openchat1")) out.print(" class=\"active\"");%>><a href="./boardShow?boardID=main&boardName=openchat1">오픈 채팅1</a></li>
                        <li <% if(boardName.equals("openchat2")) out.print(" class=\"active\"");%>><a href="./boardShow?boardID=main&boardName=openchat2">오픈 채팅2</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</body>
</html>