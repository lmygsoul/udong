package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.util.DBConn;

public class receiveUserDAO {
	
	private Connection conn = DBConn.getConnection();
	
	public int insertUser(receiveUserDTO rdto) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql="INSERT INTO receiveUser_sm(userId) VALUES(?)";
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, rdto.getReceiveUser());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
}
