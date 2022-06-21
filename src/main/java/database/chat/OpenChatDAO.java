package database.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class OpenChatDAO {
	DataSource dataSource;
	
	Connection conn= null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public OpenChatDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/mariadb");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String checkBoardName(String boardName) {
		String SQL = "";
		switch (boardName) {
		case "openchat1" :
			SQL += "chatroom";
			break;
		case "openchat2" :
			SQL += "chatroom2";
			break;
		}
		return SQL;
	}
	
	public ArrayList<OpenChatDTO> resultSet(ResultSet rs) {
		ArrayList<OpenChatDTO> openChatList = new ArrayList<OpenChatDTO>();
		try {
			while (rs.next()) {
				OpenChatDTO openChat = new OpenChatDTO();				
				openChat.setRoomID(rs.getInt("roomID"));
				openChat.setChatID(rs.getInt("chatID"));
				openChat.setFromID(rs.getString("fromID").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				openChat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11,13));
				String timeType = "오전";
				if (chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;
				}
				openChat.setChatTime(rs.getString("chatTime").substring(0,11) + " " + timeType + " "
						+ chatTime + ":" + rs.getString("chatTime").substring(14,16) + "");
				openChat.setRead1(rs.getInt("read1"));
				openChat.setRead2(rs.getInt("read2"));
				openChat.setRead3(rs.getInt("read3"));
				openChat.setRead4(rs.getInt("read4"));
				openChat.setRead5(rs.getInt("read5"));
				openChat.setChatTable(rs.getString("chatTable"));
				openChatList.add(openChat);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return openChatList;
	}
	public ArrayList<OpenChatDTO> getChatListByID(String fromID, String roomID, String chatTable, String chatID) {
		ArrayList<OpenChatDTO> chatList = new ArrayList<OpenChatDTO>();
		String SQL = "select * from openchat where roomID = ? and chatTable = ? and chatID > ? order by chatTime";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomID);
			pstmt.setString(2, chatTable);	
			pstmt.setInt(3, Integer.parseInt(chatID));
			rs = pstmt.executeQuery();
			chatList = resultSet(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return chatList;
	}

//	public ArrayList<OpenChatDTO> getChatListByRecent(String fromID, String toID, int number) {
//		ArrayList<OpenChatDTO> chatList = new ArrayList<OpenChatDTO>();
//		String SQL = "select * from chat where ((fromID = ? and toID = ?) or (fromID = ? and toID = ?)) order by chatID desc limit ?";
//		try {
//			conn = dataSource.getConnection();
//			
//			pstmt = conn.prepareStatement(SQL);
//			pstmt.setString(1, fromID);
//			pstmt.setString(2, toID);
//			pstmt.setString(3, toID);
//			pstmt.setString(4, fromID);
//			pstmt.setInt(5, number);
//			rs = pstmt.executeQuery();
//			chatList = resultSet(rs);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(rs != null) rs.close();
//				if(pstmt != null) pstmt.close();
//				if(conn != null) conn.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return chatList;
//	}
	
	public ArrayList<OpenChatDTO> getBox(String userID) {
		ArrayList<OpenChatDTO> chatList = new ArrayList<OpenChatDTO>();
		String SQL = "select * from chat where chatID in (select max(chatID)"
				+ " from chat where toID = ? or fromID = ? group by fromID, toID)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userID);
			
			rs = pstmt.executeQuery();
			chatList = resultSet(rs);
			
			for(int i = 0; i < chatList.size(); i++ ) {
				OpenChatDTO x = chatList.get(i);
				for(int j = 0; j < chatList.size(); j++) {
					OpenChatDTO y = chatList.get(j);
//					if(x.getFromID().equals(y.getToID()) && x.getToID().equals(y.getFromID())) {
//						if(x.getChatID() < y.getChatID()) {
//							chatList.remove(x);
//							i--;
//							break;
//						} else {
//							chatList.remove(y);
//							j--;							
//						}
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return chatList;
	}

	public int submit(String fromID, String roomID, String chatContent,String chatTable) {
		String SQL = "insert into openchat values (?, null, ?, ?, now(), 0,0,0,0,0,?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomID);
			pstmt.setString(2, fromID);
			pstmt.setString(3, chatContent);
			pstmt.setString(4, chatTable);
			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;
	}
	public int readChat(String userID, String roomID, String chatTable, int number) {
		String SQL = "update openchat set ";
		switch (number) {
		case 1:
			SQL += "read1";
			break;
		case 2:
			SQL += "read2";
			break;
		case 3:
			SQL += "read3";
			break;
		case 4:
			SQL += "read4";
			break;
		case 5:
			SQL += "read5";
			break;
		default:
			return -1;
		}
		SQL += " = 1 where (roomID = ? and chatTable = ?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomID);
			pstmt.setString(2, chatTable);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;
	}
	
	public int getAllUnreadChat(String userID) {
		String SQL = "select count(chatID) from chat where toID = ? and chatRead = 0";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("count(chatID)");
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;
	}
	
	public int getUnreadChat(String fromID, String toID) {
		String SQL = "select count(chatID) from chat where fromID = ? and toID = ? and chatRead = 0";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("count(chatID)");
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;
	}
}
