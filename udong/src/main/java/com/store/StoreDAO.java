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
			sql = "INSERT INTO store_bbs(num, userId, subject, content, imageFileName, Created, addr)"
					+ " VALUES(store_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getImageFileName());
			pstmt.setString(5, dto.getAddr());
			
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
	
	public int dataCount(String keyword){
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			if(keyword.equals("기타")) {
				sql = "SELECT NVL(COUNT(*), 0) FROM store_bbs"
					+ " WHERE INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1";
			} else {
			sql = "SELECT NVL(COUNT(*), 0) FROM store_bbs WHERE INSTR(addr, ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			if(keyword.equals("기타")) {
				pstmt.setString(1, "서울");
				pstmt.setString(2, "인천");
				pstmt.setString(3, "경기");	
			} else {
				pstmt.setString(1, keyword);
			}
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
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score, addr, recnum");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, ROUND(AVG(score),2) score, COUNT(*) recnum FROM store_rec GROUP BY num) r ON s.num=r.num");
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
				dto.setAddr(rs.getString("addr"));
				dto.setRecnum(rs.getInt("recnum"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<StoreDTO> listStore(int offset, int rows, String keyword) {
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score, addr, recnum ");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, ROUND(AVG(score),2) score, COUNT(*) recnum FROM store_rec GROUP BY num) r ON s.num=r.num");
			if(keyword.equals("기타")) {
				sb.append(" WHERE INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 ");
				sb.append(" ORDER BY num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROW ONLY");	
			}else {
				sb.append(" WHERE INSTR(addr, ?) >= 1 ");
				sb.append(" ORDER BY num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROW ONLY");
			}
			pstmt = conn.prepareStatement(sb.toString());
			if(keyword.equals("기타")) {
				pstmt.setString(1, "서울");
				pstmt.setString(2, "경기");
				pstmt.setString(3, "인천");
				pstmt.setInt(4, offset);
				pstmt.setInt(5, rows);	
			} else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, rows);
			}
			rs=pstmt.executeQuery();
			while(rs.next()) {
				StoreDTO dto = new StoreDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickname(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setAddr(rs.getString("addr"));
				dto.setCreated(rs.getString("created"));
				dto.setScore(rs.getDouble("score"));
				dto.setRecnum(rs.getInt("recnum"));
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
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score, addr ");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, ROUND(AVG(score),2) score FROM store_rec GROUP BY num) r ON s.num=r.num");
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
				dto.setAddr(rs.getString("addr"));
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

	public int updateStore(StoreDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "UPDATE store_bbs SET subject = ?, content = ?, imageFileName = ?, addr = ? WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getImageFileName());
			pstmt.setString(4, dto.getAddr());
			pstmt.setInt(5, dto.getNum());
			
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
	
	public int updateScore(StoreDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO store_rec(num, rec_id, score) VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setDouble(3, dto.getScore());
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
	
	public int deleteStore(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "DELETE FROM store_rec WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			pstmt.close();
			
				
			sql = "DELETE FROM store_bbs WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
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
		boolean is_rec=false;
		try {
			sql = "SELECT score FROM store_rec WHERE num=? AND rec_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				is_rec=true;
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
		return is_rec;
	}
	public int dataCount_st(String userId){
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM store_bbs WHERE userid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
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
	
	public int dataCount_st(String keyword,String userId){
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			if(keyword.equals("기타")) {
				sql = "SELECT NVL(COUNT(*), 0) FROM store_bbs"
					+ " WHERE INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 AND userid = ?";
			} else {
			sql = "SELECT NVL(COUNT(*), 0) FROM store_bbs WHERE INSTR(addr, ?) >= 1 AND userid = ?";
			}
			
			pstmt = conn.prepareStatement(sql);
			if(keyword.equals("기타")) {
				pstmt.setString(1, "서울");
				pstmt.setString(2, "인천");
				pstmt.setString(3, "경기");	
				pstmt.setString(4, userId);
			} else {
				pstmt.setString(1, keyword);
				pstmt.setString(2, userId);
			}
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
	public List<StoreDTO> listStore_st(int offset, int rows,String userId) {
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score, addr, recnum");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, ROUND(AVG(score),2) score, COUNT(*) recnum FROM store_rec GROUP BY num) r ON s.num=r.num");
			sb.append(" WHERE s.userid = ? ");
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROW ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
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
				dto.setAddr(rs.getString("addr"));
				dto.setRecnum(rs.getInt("recnum"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<StoreDTO> listStore_st(int offset, int rows, String keyword,String userId) {
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT s.num, s.userid, nickname, subject, content, imageFileName, created, score, addr, recnum ");
			sb.append(" FROM store_bbs s");
			sb.append(" LEFT OUTER JOIN member1 m ON s.userid=m.userid");
			sb.append(" LEFT OUTER JOIN (SELECT num, ROUND(AVG(score),2) score, COUNT(*) recnum FROM store_rec GROUP BY num) r ON s.num=r.num");
			if(keyword.equals("기타")) {
				sb.append(" WHERE INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 AND INSTR(addr, ?) < 1 AND s.userid = ?");
				sb.append(" ORDER BY num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROW ONLY");	
			}else {
				sb.append(" WHERE INSTR(addr, ?) >= 1 AND s.userid = ? ");
				sb.append(" ORDER BY num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROW ONLY");
			}
			pstmt = conn.prepareStatement(sb.toString());
			if(keyword.equals("기타")) {
				pstmt.setString(1, "서울");
				pstmt.setString(2, "경기");
				pstmt.setString(3, "인천");
				pstmt.setString(4, userId);
				pstmt.setInt(5, offset);
				pstmt.setInt(6, rows);	
			} else {
				pstmt.setString(1, keyword);
				pstmt.setString(2, userId);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			}
			rs=pstmt.executeQuery();
			while(rs.next()) {
				StoreDTO dto = new StoreDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickname(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setAddr(rs.getString("addr"));
				dto.setCreated(rs.getString("created"));
				dto.setScore(rs.getDouble("score"));
				dto.setRecnum(rs.getInt("recnum"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public StoreDTO preReadStore_st(int num,String userId) {
		StoreDTO dto =null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		ResultSet rs = null;
		try {
			sb.append(" SELECT num, subject");
			sb.append(" FROM store_bbs");
			sb.append(" WHERE num<? AND userid =? ");
			sb.append(" ORDER BY num DESC");
			sb.append(" FETCH FIRST 1 ROWS ONLY");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			
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

	public StoreDTO nextReadStore_st(int num,String userId) {
		StoreDTO dto =null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		ResultSet rs = null;
		try {
			sb.append(" SELECT num, subject");
			sb.append(" FROM store_bbs");
			sb.append(" WHERE num>? AND userid = ?");
			sb.append(" ORDER BY num ASC");
			sb.append(" FETCH FIRST 1 ROWS ONLY");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
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
}
