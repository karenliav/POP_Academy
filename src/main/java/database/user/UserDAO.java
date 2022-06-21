package database.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	DataSource dataSource;
	
	Connection conn= null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public UserDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/mariadb");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int findMember(String str1, String str2, String str3) {
		String SQL = null;
		if (str3.equals("findID")) {
			SQL = "select * from user where userName = ? and userEmail = ?";						
		} else {
			SQL = "select * from user where userName = ? and userEmail = ? and userID = ?";									
		}
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, str1);				
			pstmt.setString(2, str2);				
			if (!str3.equals("findID")) {
				pstmt.setString(3, str3);					
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 1;
			} else {
				return 0;
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

	public int login(String userID, String userPassword) {
		String SQL = "select * from user where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString("userPassword").equals(userPassword)) {
					return 1;
				}
				return 2;
			} else {
				return 0;
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
	
	public int registerCheck(String userID) {
		String SQL = "select * from user where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 0;
			} else if(userID.equals("")) {
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

	public int register(String userID, String userPassword, String userName,
			String userAge, String userGender, String userEmail, String userProfile, String userFriends) {
		String SQL = "insert into user values (null,?, ?, ?, ?, ?, ?, ?,?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);	
			pstmt.setString(2, userPassword);	
			pstmt.setString(3, userName);	
			pstmt.setInt(4, Integer.parseInt(userAge));	
			pstmt.setString(5, userGender);	
			pstmt.setString(6, userEmail);
			pstmt.setString(7, userProfile);
			pstmt.setString(8, userFriends);
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
	public UserDTO getUser(String userID) {
		UserDTO user = new UserDTO();
		String SQL = "select * from user where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user.setNum(rs.getLong("num"));
				user.setUserID(userID);
				user.setUserPassword(rs.getString("userPassword"));
				user.setUserName(rs.getString("userName"));
				user.setUserAge(rs.getInt("userAge"));
				user.setUserGender(rs.getString("userGender"));
				user.setUserEmail(rs.getString("userEmail"));
				user.setUserProfile(rs.getString("userProfile"));
				user.setUserFriends(rs.getString("userFriends"));
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
		return user;
	}
	
	public int makeFriend(String friends, String userID) {
		String SQL = "update user set userFriends = ? where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, friends);	
			pstmt.setString(2, userID);
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
	
	public int update(String userID, String userPassword, String userName,
			String userAge, String userGender, String userEmail) {
		String SQL = "update user set userPassword = ?, userName = ?, userAge = ?, userGender = ?, userEmail = ? where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPassword);	
			pstmt.setString(2, userName);	
			pstmt.setInt(3, Integer.parseInt(userAge));	
			pstmt.setString(4, userGender);	
			pstmt.setString(5, userEmail);
			pstmt.setString(6, userID);	
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
	
	public int profile(String userID, String userProfile) {
		String SQL = "update user set userProfile = ? where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userProfile);	
			pstmt.setString(2, userID);	
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
	public String getProfile(String userID) {
		String SQL = "select userProfile from user where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString("userProfile").equals("")) {
					return "http://localhost:8081/Academy_JSP_Project/images/chat-image1.png";
				}
				return ("http://localhost:8081/Academy_JSP_Project/upload/userprofile/" + rs.getString("userProfile"));
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
		return "http://localhost:8081/Academy_JSP_Project/images/chat-image1.png";
	}
	public String getFrineds(String userID) {
		String SQL = "select userFriends from user where userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString("userFriends").equals("")) {
					return "";
				}
				return (rs.getString("userFriends"));
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
		return "";
	}
}
