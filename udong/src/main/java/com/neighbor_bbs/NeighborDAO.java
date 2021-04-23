package com.neighbor_bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.store_bbs.StoreDTO;
import com.util.DBConn;

public class NeighborDAO {
private Connection conn = DBConn.getConnection();
	
	public int insertNeighbor(StoreDTO dto) throws SQLException{
		int result = 0;
		String sql;
		PreparedStatement pstmt = null;
		
		try {
			sql = "INSERT INTO neighbor";
			pstmt = conn.prepareStatement(sql);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		return result;
	}
}
