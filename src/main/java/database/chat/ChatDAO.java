package database.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import database.board.BoardDTO;

public class ChatDAO {
	DataSource dataSource;
	
	Connection conn= null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ChatDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/mariadb");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<ChatDTO> resultSet(ResultSet rs) {
		ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
		try {
			while (rs.next()) {
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11,13));
				String timeType = "오전";
				if (chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0,11) + " " + timeType + " "
						+ chatTime + ":" + rs.getString("chatTime").substring(14,16) + "");
				chatList.add(chat);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return chatList;
	}
	
	public ArrayList<ChatDTO> getChatListByID(String fromID, String toID, String chatID) {
		ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
		String SQL = "select * from chat where ((fromID = ? and toID = ?) or (fromID = ? and toID =?)) and chatID > ? order by chatTime";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);			
			pstmt.setInt(5, Integer.parseInt(chatID));
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

	public ArrayList<ChatDTO> getChatListByRecent(String fromID, String toID, int number) {
		ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
		String SQL = "select * from chat where ((fromID = ? and toID = ?) or (fromID = ? and toID = ?)) order by chatID desc limit ?";
		try {
			conn = dataSource.getConnection();
			
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, number);
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
	
	public ArrayList<ChatDTO> getBox(String userID) {
		ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
		String SQL = "select * from chat where chatID in (select max(chatID) from chat where toID = ? or fromID = ? group by fromID, toID)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userID);
			
			rs = pstmt.executeQuery();
			chatList = resultSet(rs);
			
			for(int i = 0; i < chatList.size(); i++ ) {
				ChatDTO x = chatList.get(i);
				for(int j = 0; j < chatList.size(); j++) {
					ChatDTO y = chatList.get(j);
					if(x.getFromID().equals(y.getToID()) && x.getToID().equals(y.getFromID())) {
						if(x.getChatID() < y.getChatID()) {
							chatList.remove(x);
							i--;
							break;
						} else {
							chatList.remove(y);
							j--;							
						}
					}
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

	public int submit(String fromID, String toID, String chatContent) {
		String SQL = "insert into chat values (null, ?, ?, ?, now(), 0)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, chatContent);
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
	
	public int readChat(String fromID, String toID) {
		String SQL = "update chat set chatRead = 1 where (fromID = ? and toID = ?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, toID);
			pstmt.setString(2, fromID);
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
