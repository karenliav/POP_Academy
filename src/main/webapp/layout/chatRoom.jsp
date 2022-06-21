<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="database.chat.ChatRoomDTO" %>
<%@ page import="database.chat.ChatRoomDAO" %>
   <%@ page import="java.util.ArrayList" %>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html>
<%
	String pageNumber = "1";
	if(request.getParameter("pageNumber") != null){
		pageNumber = URLDecoder.decode((String) request.getParameter("pageNumber"), "UTF-8");
	}
	try {
		Integer.parseInt(pageNumber);
	} catch (Exception e) {
		session.setAttribute("messageType", "오류 메시지");
		session.setAttribute("messageContent", "페이지 번호가 잘못되었습니다.");
		response.sendRedirect("main.jsp");
		return;
	}
	
	String boardName = null;
	boardName = request.getParameter("boardName");
	String searchTitle = null;
	searchTitle = request.getParameter("searchTitle");
	ChatRoomDAO chatRoomDAO = new ChatRoomDAO();
	ArrayList<ChatRoomDTO> boardList = new ArrayList<ChatRoomDTO>();
	if(!searchTitle.equals("")){
		boardList = chatRoomDAO.findList(searchTitle, pageNumber,boardName);
	} else {
		boardList = chatRoomDAO.getList(pageNumber,boardName);
	}
	ChatRoomDTO board = new ChatRoomDTO();
%>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="col-xs-8 bg-grey1" style="width: 70%;">
                <div class="item item3 bg-grey3 text-light nav justify-content-center" style="width: 100%;">
                	<h4>
                	<%
                		if(boardName.equals("openchat1")) {
                	%>
                 	오픈채팅1
                 	<%
                 		} else if (boardName.equals("openchat2")) {
                 	%>
                 	오픈채팅2
                 	<%
                 		}
                 	%>
                	</h4>
                </div>
                    <div class="row" style="padding-left: 20px;">
                        <div class="col-xs-12">
                            <form method="post" action="main.jsp">
                                <table style="text-align:center;">
                                    <thead></thead>
                                    <tbody>
                                        <tr>
                                            <td style="padding-top: 10px; padding-bottom: 10px;">
                                                <input class="form-control me-2" style="width: 500px; height: 30px;"
                                                	name="searchTitle" type="text" placeholder="검색 내용을 입력하세요."
                                                	<% if(!searchTitle.equals("")){ out.print("value=\""+searchTitle+"\"");} %>>
                                            </td>
                                            <td style="padding-left: 10px; padding-top: 10px;padding-bottom: 10px;">
                                                <button class="btn btn-primary pull-right" style="width: 80px;" type="submit">검색</button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
                    <div class="item item4 text-light pt-3">
			        	<table class="table" style="color: white;">
				            <thead>
				              <tr>
				                <th class="text-center jb-th-1">번호</th>
				                <th class="text-center">제목</th>
				              </tr>
				            </thead>
				            <tbody id="showlist">
								<%
									for(int i =0; i< boardList.size(); i++) {
										board = boardList.get(i);
								%>
								<tr>
									<td><%= board.getRoomID() %></td>
									<td style="text-align: left;">
									<a style="color: white;" href="./openChatEnter?roomID=<%= board.getRoomID() %>&boardName=<%= boardName %>">
									
									
									<%= board.getRoomTitle() %>
									
									</a></td>				
								</tr>
								<%
									}
								%>
								<tr>
									<td colspan="2">
										<a href="./openChatEnter?roomID=write&boardName=<%= boardName %>" class="btn btn-primary pull-right" type="submit">만들기</a>
										<ul class="pagination" style="margin: 0 auto;">
											<%
												int startPage = (Integer.parseInt(pageNumber) / 10) * 10 + 1;
												if(Integer.parseInt(pageNumber) % 10 == 0) startPage -= 10;
												int targetPage = 0;
												if(!searchTitle.equals("")) {
													targetPage = new ChatRoomDAO().targetFindPage(searchTitle,pageNumber,boardName);									
												} else {
													targetPage = new ChatRoomDAO().targetPage(pageNumber,boardName);
												}
												if(startPage != 1) {
											%>
											<li><a href="main.jsp?pageNumber=<%= startPage - 1 %>
												<% if(!searchTitle.equals("")){ out.print("&searchTitle="+searchTitle);} %>">
												<span class="glyphicon glyphicon-chevron-left"></span></a></li>
											<%
												} else {
											%>
											<li><span class="glyphicon glyphicon-chevron-left" style="color: gray;"></span></li>
											<%
												}
												for(int i = startPage; i < Integer.parseInt(pageNumber); i++) {
											%>
											<li><a href="main.jsp?pageNumber=<%= i %>
												<% if(!searchTitle.equals("")){ out.print("&searchTitle="+searchTitle);} %>">
												<%= i %></a></li>
											<%
												}
											%>
											<li class="active"><a href="main.jsp?pageNumber=<%= pageNumber %>"><%= pageNumber %></a></li>
											<%
												for(int i = Integer.parseInt(pageNumber) + 1; i <= targetPage + Integer.parseInt(pageNumber); i++) {
													if(i < startPage + 10) {
											%>
											<li><a href="main.jsp?pageNumber=<%= i %>
												<% if(!searchTitle.equals("")){ out.print("&searchTitle="+searchTitle);} %>">
												<%= i %></a></li>
											<%
													}
												}
												if(targetPage + Integer.parseInt(pageNumber) > startPage + 9) {
											%>
											<li><a href="main.jsp?pageNumber=<%= startPage + 10 %>
												<% if(!searchTitle.equals("")){ out.print("&searchTitle="+searchTitle);} %>">
												<span class="glyphicon glyphicon-chevron-right"></span></a></li>
											<%
												} else {
											%>
											<li><span class="glyphicon glyphicon-chevron-right" style="color: gray;"></span></li>
											<%
												}
											%>
										</ul>
									</td>
								</tr>
				            </tbody>
				        </table>
                    </div>
                </div>
</body>
</html>