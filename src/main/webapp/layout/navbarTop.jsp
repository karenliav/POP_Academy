<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html>
<%

	String userID = null;
	if(session.getAttribute("userID") != null) {
		userID = (String) session.getAttribute("userID");
	}
	String pageName = URLDecoder.decode((String) request.getParameter("pageName"), "UTF-8");
	
%>
<meta charset="UTF-8">
<head>
    <title>top</title>
</head>
<body>
    <nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" style="padding-left: 40px;" href="./boardShow?boardID=main&boardName=board1">POP</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <div class="row">
                <div class="col-xs-<% if(userID == null) out.print("7"); else out.print("6"); %>">
                    <ul class="nav navbar-nav" style="padding-top: 20px;">
                        <form method="post" action="./userRegister">
                            <table style="text-align:center;">
                                <thead></thead>
                                <tbody>
                                    <tr>
                                        <td><h4 style="width: 100px;color:aliceblue; padding-right: 10px;">채팅방 찾기</h4></td>
                                        <td>
                                            <input class="form-control me-2" style="width: 250px; height: 30px;" type="text" placeholder="방의 번호를 입력하세요">
                                        </td>
                                        <td style="padding-left: 10px;">
                                            <button class="btn btn-primary" style="width: 80px;" type="submit">검색</button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                    </ul>
                </div>
                <%
                	if(userID == null) {
                %>
                <div class="col-xs-3">
                    <ul class="nav navbar-nav navbar-right"style="width:200px;padding-right: 10px;">
                        <li<% if(pageName.equals("login")) out.print(" class=\"active\"");%>><a href="login.jsp">로그인</a></li>
                        <li<% if(pageName.equals("join")) out.print(" class=\"active\"");%>><a href="join.jsp">회원가입</a></li>
                    </ul>
                </div>
                <%
                	} else {
                %>
                <div class="col-xs-3">
                    <ul class="nav navbar-nav navbar-right"style="width:200px;padding-right: 10px;">
                        <li<% if(pageName.equals("main")) out.print(" class=\"active\"");%>><a href="./boardShow?boardID=main&boardName=board1">메인으로</a></li>
                        <li<% if(pageName.equals("find")) out.print(" class=\"active\"");%>><a href="find.jsp">친구찾기</a></li>
                    </ul>
                </div>
                <ul class="nav navbar-nav navbar-right" style="padding-right: 10px;">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle"
                            data-toggle="dropdown" role="button" aria-haspopup="true"
                            aria-expanded="false">회원관리<span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li<% if(pageName.equals("update")) out.print(" class=\"active\"");%>><a href="update.jsp">회원정보수정</a></li>
							<li<% if(pageName.equals("profileUpdate")) out.print(" class=\"active\"");%>><a href="profileUpdate.jsp">프로필 수정</a></li>
                            <li><a href="logoutAction.jsp">로그아웃</a></li>
                        </ul>
                    </li>
                </ul>
                <%
                	}
                %>
            </div>
		</div>
	</nav>
</body>
</html>
