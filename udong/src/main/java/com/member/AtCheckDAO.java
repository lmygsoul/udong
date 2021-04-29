package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class AtCheckDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(AtCheckDTO ato) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="INSERT INTO at_check(num, userId ,content, created,checkType)"
					+" VALUES(check_seq.NEXTVAL, ?,?, SYSDATE,2)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, ato.getUserId());
			pstmt.setString(2, ato.getContent());
			
			result=pstmt.executeUpdate();
			
		} catch (SQLException e) {
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
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM at_check";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				result=rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	public List<AtCheckDTO> listBoard(int offset, int rows){
		List<AtCheckDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String sql;
		//mem1, bbs
		try {
			sql="SELECT num, a.userId, userName ,content, TO_CHAR(created,'YYYY-MM-DD') created,checkType FROM at_check a"
					+ " JOIN member1 m ON a.userId = m.userId"
					+ " ORDER BY num DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				AtCheckDTO ato = new AtCheckDTO();
				ato.setNum(rs.getInt("num"));
				ato.setContent(rs.getString("content"));
				ato.setCreated(rs.getString("created"));
				ato.setUserId(rs.getString("userId"));
				ato.setUserName(rs.getString("userName"));
				ato.setCheckType(rs.getInt("checkType"));
				
				list.add(ato);
				//ArrayList에 저
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	public AtCheckDTO readBoard(String userId) {
		AtCheckDTO ato = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT num, a.userId, userName ,content, TO_CHAR(created,'YYYY-MM-DD') created,checkType FROM at_check a"
					+ " JOIN member1 m ON a.userId = m.userId WHERE a.userId = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				ato = new AtCheckDTO();
				ato.setNum(rs.getInt("num"));
				ato.setContent(rs.getString("content"));
				ato.setUserId(rs.getString("userId"));
				ato.setCreated(rs.getString("created"));
				ato.setUserName(rs.getString("userName"));
				ato.setCheckType(rs.getInt("checkType"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return ato;
	}
	public int updateBoard(AtCheckDTO ato)throws SQLException{
    	int result=0;
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
			sql="UPDATE at_check SET checkType = 2 WHERE userid=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, ato.getUserId());
			
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.addSuppressed(e);
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
	 public int deleteBoard(String userId) throws SQLException{
	    	int result=0;
	    	PreparedStatement pstmt = null;
	    	String sql;
	    	
	    	try {
				sql="DELETE FROM at_check";
				pstmt= conn.prepareStatement(sql);
				
				result=pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
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
