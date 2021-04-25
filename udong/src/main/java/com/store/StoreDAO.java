package com.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class StoreDAO {
private Connection conn = DBConn.getConnection();
	
	public int insertStore(StoreDTO dto) throws SQLException{
		int result = 0;
		String sql;
		PreparedStatement pstmt = null;
		
		try {
			sql = "INSERT INTO store_bbs(num, userId, subject, content, imageFileName, Created)"
					+ " VALUES(store_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getImageFileName());
			
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

	public int dataCount(){
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM store_bbs";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();					
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();					
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	public List<StoreDTO> listStore(int offset, int rows) {
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score ");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, AVG(star) score FROM store_rec GROUP BY num) r ON s.num=r.num");
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROW ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				StoreDTO dto = new StoreDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickname(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setCreated(rs.getString("created"));
				dto.setScore(rs.getDouble("score"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public StoreDTO readStore(int num) {
		StoreDTO dto =null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		ResultSet rs = null;
		try {
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score ");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, AVG(star) score FROM store_rec GROUP BY num) r ON s.num=r.num");
			sb.append(" WHERE s.num=?");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new StoreDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userid"));
				dto.setNickname(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setCreated(rs.getString("created"));
				dto.setScore(rs.getDouble("score"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}

	public StoreDTO preReadStore(int num) {
		StoreDTO dto =null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		ResultSet rs = null;
		try {
			sb.append(" SELECT num, subject");
			sb.append(" FROM store_bbs");
			sb.append(" WHERE num<?");
			sb.append(" ORDER BY num DESC");
			sb.append(" FETCH FIRST 1 ROWS ONLY");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new StoreDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}

	public StoreDTO nextReadStore(int num) {
		StoreDTO dto =null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		ResultSet rs = null;
		try {
			sb.append(" SELECT num, subject");
			sb.append(" FROM store_bbs");
			sb.append(" WHERE num>?");
			sb.append(" ORDER BY num ASC");
			sb.append(" FETCH FIRST 1 ROWS ONLY");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new StoreDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}

	public int updateScore(StoreDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO store_rec(num, rec_id, score) VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.num);
			pstmt.setString(2, dto.userId);
			pstmt.setDouble(3, dto.score);
			
			result = pstmt.executeUpdate();
			
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
	
	public boolean RecCheck(StoreDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		boolean rec=false;
		try {
			sql = "SELECT score FROM store_rec WHERE num=? AND userId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			rs = pstmt.executeQuery();
			if(rs.next()==true) {
				rec=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return rec;
	}


}
