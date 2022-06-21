package database.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	DataSource dataSource;
	
	Connection conn= null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public BoardDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/mariadb");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<BoardDTO> resultSet(ResultSet rs) {
		ArrayList<BoardDTO> boardList = new ArrayList<BoardDTO>();
		try {
			while (rs.next()) {
				BoardDTO board = new BoardDTO();
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardContent(rs.getString("userID").replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardDate(rs.getString("boardDate").substring(0,11));
				board.setBoardHit(rs.getInt("boardHit"));
				board.setBoardFile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardGroup(rs.getInt("boardGroup"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				board.setBoardAvailable(rs.getInt("boardAvailable"));
				boardList.add(board);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return boardList;
	}
	
	public String checkBoardName(String boardName) {
		String SQL = "";
		switch (boardName) {
		case "board1" :
			SQL += "board";
			break;
		case "board2" :
			SQL += "board2";
			break;
		}
		return SQL;
	}
	
	public int write(String userID, String boardTitle,
			String boardContent, String boardFile, String boardRealFile, String boardName) {
		String SQL = "insert into ";
		SQL += checkBoardName(boardName);
		SQL += " select ?, ifnull((select max(boardID) + 1 from ";
		SQL += checkBoardName(boardName);
		SQL += "),1),?,?,now(),0,?,?,ifnull((select max(boardGroup) + 1 from ";
		SQL += checkBoardName(boardName);
		SQL += "),0), 0, 0, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);	
			pstmt.setString(2, boardTitle);	
			pstmt.setString(3, boardContent);	
			pstmt.setString(4, boardFile);	
			pstmt.setString(5, boardRealFile);	

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
	
	public BoardDTO getBoard(String boardID, String boardName) {
		ArrayList<BoardDTO> boardList = new ArrayList<BoardDTO>();
		BoardDTO board = new BoardDTO();
		String SQL = "select * from ";
		SQL += checkBoardName(boardName);
		SQL += " where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);			
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
	
	public ArrayList<BoardDTO> getList(String pageNumber, String boardName) {
		
		return findList("",pageNumber,boardName);
		
	}
	
	public ArrayList<BoardDTO> findList (String searchTitle,String pageNumber, String boardName) {
		ArrayList<BoardDTO> boardList = new ArrayList<BoardDTO>();
		BoardDTO board = new BoardDTO();
		int i = 0;
		ArrayList<BoardDTO> boardListPage = new ArrayList<BoardDTO>();
		String title = "%" + searchTitle + "%";
		String SQL = "select * from ";
		SQL += checkBoardName(boardName);
		SQL += " where boardTitle like ?";
		if (!searchTitle.equals("")) {
			SQL += " and boardAvailable = 1";			
		}
		SQL += " order by boardGroup desc, boardSequence asc";
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
	
	public int hit(String boardID, String boardName) {
		String SQL = "update ";
		SQL += checkBoardName(boardName);
		SQL += " set boardHit = boardHit + 1 where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);

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
	
	public boolean nextPage(String pageNumber, String boardName) {
		String SQL = "select * from ";
		SQL += checkBoardName(boardName);
		SQL += " where boardGroup >= ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);			
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
		String SQL = "select count(boardGroup) from ";
		SQL += checkBoardName(boardName);
		SQL += " where boardTitle like ?";
		if (!searchTitle.equals("")) {
			SQL += " and boardAvailable = 1";			
		}
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
	
	public String getFile(String boardID, String boardName) {
		String SQL = "select boardFile from ";
		SQL += checkBoardName(boardName);
		SQL += " where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString("boardFile");
			}
			return "";
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
		return "";
	}
	
	public String getRealFile(String boardID, String boardName) {
		String SQL = "select boardRealFile from ";
		SQL += checkBoardName(boardName);
		SQL += " where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString("boardRealFile");
			}
			return "";
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
		return "";
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
	
	public int delete(String boardID, String boardName) {
		String SQL = "update ";
		SQL += checkBoardName(boardName);
		SQL += " set boardAvailable = 0 where boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));	
			
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
	
	public int reply(String userID, String boardTitle, String boardContent,
			String boardFile, String boardRealFile, BoardDTO parent, String boardName) {
		String SQL = "insert into ";
		SQL += checkBoardName(boardName);
		SQL += " select ?, ifnull((select max(boardID) + 1 from ";
		SQL += checkBoardName(boardName);
		SQL += "),1),?,?,now(),0,?,?, ?, ?, ?, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);	
			pstmt.setString(2, boardTitle);	
			pstmt.setString(3, boardContent);	
			pstmt.setString(4, boardFile);	
			pstmt.setString(5, boardRealFile);
			pstmt.setInt(6, parent.getBoardGroup());
			pstmt.setInt(7, parent.getBoardSequence() + 1);
			pstmt.setInt(8, parent.getBoardLevel() + 1);

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
	
	public int replyUpdate(BoardDTO parent, String boardName) {
		String SQL = "update ";
		SQL += checkBoardName(boardName);
		SQL += " set boardSequence = boardSequence + 1 where boardGroup = ? and boardSequence > ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, parent.getBoardGroup());	
			pstmt.setInt(2, parent.getBoardSequence());	
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
}
