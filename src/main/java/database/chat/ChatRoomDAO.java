package database.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ChatRoomDAO {
	DataSource dataSource;
	
	Connection conn= null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ChatRoomDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/mariadb");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ChatRoomDTO> resultSet(ResultSet rs) {
		ArrayList<ChatRoomDTO> chatRoomList = new ArrayList<ChatRoomDTO>();
		try {
			while (rs.next()) {
				ChatRoomDTO chatRoom = new ChatRoomDTO();
				chatRoom.setRoomID(rs.getInt("roomID"));
				chatRoom.setUserID1(rs.getString("userID1").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chatRoom.setUserID2(rs.getString("userID2").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chatRoom.setUserID3(rs.getString("userID3").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chatRoom.setUserID4(rs.getString("userID4").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chatRoom.setUserID5(rs.getString("userID5").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chatRoom.setRoomTitle(rs.getString("roomTitle").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				
				chatRoomList.add(chatRoom);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return chatRoomList;
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
	public int registerCheck(String roomID) {
		String SQL = "select * from chatroom where roomID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 0;
			} else if(roomID.equals("")) {
				return 2;
			} else {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	public ArrayList<ChatRoomDTO> getRoomInfo(String roomID, String chatTable) {
		ArrayList<ChatRoomDTO> chatRoomList = new ArrayList<ChatRoomDTO>();
		String SQL = "select * from ";				
		SQL += chatTable;
		SQL	+= " where roomID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomID);
			rs = pstmt.executeQuery();
			chatRoomList = resultSet(rs);
			
		}  catch (Exception e) {
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
		return chatRoomList;
		
	}
	public int create(String roomTitle, String boardName) {
		String SQL = "insert into ";
		SQL += checkBoardName(boardName);
		SQL += " select null, '','','','','',?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomTitle);

			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public ChatRoomDTO getBoard(String roomID, String boardName) {
		ArrayList<ChatRoomDTO> boardList = new ArrayList<ChatRoomDTO>();
		ChatRoomDTO board = new ChatRoomDTO();
		String SQL = "select * from ";
		SQL += checkBoardName(boardName);
		SQL += " where roomID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, roomID);			
			rs = pstmt.executeQuery();
			boardList = resultSet(rs);
			board = boardList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return board;
	}
	
	public ArrayList<ChatRoomDTO> getList(String pageNumber, String boardName) {
		
		return findList("",pageNumber,boardName);
		
	}
	
	public ArrayList<ChatRoomDTO> findList (String searchTitle,String pageNumber, String boardName) {
		ArrayList<ChatRoomDTO> boardList = new ArrayList<ChatRoomDTO>();
		ChatRoomDTO board = new ChatRoomDTO();
		int i = 0;
		ArrayList<ChatRoomDTO> boardListPage = new ArrayList<ChatRoomDTO>();
		String title = "%" + searchTitle + "%";
		String SQL = "select * from ";
		SQL += checkBoardName(boardName);
		SQL += " where roomTitle like ?";
		SQL += " order by roomID desc";
		int max = ((Integer.parseInt(pageNumber)) * 10)-1;
		int min = ((Integer.parseInt(pageNumber)-1) * 10);
		if(min < 0) min = 0;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, title);
			rs = pstmt.executeQuery();
			boardList = resultSet(rs);
			if(max >= boardList.size()) {
				max = boardList.size()-1;
			}
			for(i = min; i <= max; i++) {
				board = boardList.get(i);
				boardListPage.add(board);
				board = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return boardListPage;
	}
	
	public boolean nextPage(String pageNumber, String boardName) {
		String SQL = "select * from ";
		SQL += checkBoardName(boardName);
		SQL += " where roomID >= ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, (Integer.parseInt(pageNumber) * 10) -1);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int targetFindPage(String searchTitle, String pageNumber, String boardName) {
		String title = "%" + searchTitle + "%";
		String SQL = "select count(roomID) from ";
		SQL += checkBoardName(boardName);
		SQL += " where roomTitle like ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, title);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) % 10 == 0) {
					return ((rs.getInt(1) / 10)-(Integer.parseInt(pageNumber)));
				} else {
					return ((rs.getInt(1) / 10)-(Integer.parseInt(pageNumber))+1);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	public int targetPage(String pageNumber, String boardName) {
		return targetFindPage("", pageNumber, boardName);
	}
	
//	public String getFile(String boardID, String boardName) {
//		String SQL = "select boardFile from ";
//		SQL += checkBoardName(boardName);
//		SQL += " where boardID = ?";
//		try {
//			conn = dataSource.getConnection();
//			pstmt = conn.prepareStatement(SQL);
//			pstmt.setString(1, boardID);			
//			rs = pstmt.executeQuery();
//			if(rs.next()) {
//				return rs.getString("boardFile");
//			}
//			return "";
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(rs != null) rs.close();
//				if(pstmt != null) pstmt.close();
//				if(conn != null) conn.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return "";
//	}
//	
//	public String getRealFile(String boardID, String boardName) {
//		String SQL = "select boardRealFile from ";
//		SQL += checkBoardName(boardName);
//		SQL += " where boardID = ?";
//		try {
//			conn = dataSource.getConnection();
//			pstmt = conn.prepareStatement(SQL);
//			pstmt.setString(1, boardID);			
//			rs = pstmt.executeQuery();
//			if(rs.next()) {
//				return rs.getString("boardRealFile");
//			}
//			return "";
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(rs != null) rs.close();
//				if(pstmt != null) pstmt.close();
//				if(conn != null) conn.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return "";
//	}
	
	public int userUpdate(String roomID, String boardName,String userID, int number ) {
		String SQL = "update ";
		SQL += checkBoardName(boardName);
		switch (number) {
		case 1:
			SQL += " set userID1 = ?";
			break;
		case 2:
			SQL += " set userID2 = ?";			
			break;
		case 3:			
			SQL += " set userID3 = ?";			
			break;
		case 4:
			SQL += " set userID4 = ?";						
			break;
		case 5:
			SQL += " set userID5 = ?";			
			break;
		}
		SQL += " where roomID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);	
			pstmt.setInt(2, Integer.parseInt(roomID));	
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	public int update(String boardID, String boardTitle,
			String boardContent, String boardFile, String boardRealFile, String boardName) {
		String SQL = "update ";
		SQL += checkBoardName(boardName);
		SQL += " set boardTitle = ?, boardContent = ?,"
				+ " boardFile = ?, boardRealFile = ? where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardTitle);	
			pstmt.setString(2, boardContent);	
			pstmt.setString(3, boardFile);	
			pstmt.setString(4, boardRealFile);	
			pstmt.setInt(5, Integer.parseInt(boardID));	

			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
//	public int delete(String boardID, String boardName) {
//		String SQL = "update ";
//		SQL += checkBoardName(boardName);
//		SQL += " set boardAvailable = 0 where boardID = ?";
//		try {
//			conn = dataSource.getConnection();
//			pstmt = conn.prepareStatement(SQL);
//			pstmt.setInt(1, Integer.parseInt(boardID));	
//			
//			return pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(conn != null) conn.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return -1;
//	}

}
