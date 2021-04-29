package com.neighbor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class NeighborDAO {
private Connection conn = DBConn.getConnection();
	
	public int insertNeighbor(NeighborDTO dto) throws SQLException{
		int result = 0;
		String sql;
		PreparedStatement pstmt = null;
		
		try {
			sql = "INSERT INTO neighbor_bbs(num, userid, subject, content, created, imageFileName, HitCount)"
					+ " VALUSE (neighbor_seq.NEXTVAL, ?, ?, ?, SYSDATE, ?, 0)";
			
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

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM neighbor_bbs";
			pstmt=conn.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM neighbor_bbs b LEFT OUTER JOIN member1 m  ON b.userid=m.userid";
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql = " WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			} else {
				sql += " WHERE INSTR(" +condition+", ?) >=1";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
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

	public List<NeighborDTO> listBoard(int offset, int rows) {
		List<NeighborDTO> list = new ArrayList<NeighborDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql=" SELECT b.num, nickName, subject, TO_CHAR(created,'YYYY-MM-DD') created, hitCount, rec, notRec  "
					+ " FROM neighbor_bbs b"
					+ " LEFT OUTER JOIN member1 m ON b.userId = m.userId"
					+ " LEFT OUTER JOIN (SELECT num, COUNT(CASE WHEN type=1 then 1 end) rec, COUNT(CASE WHEN type=2 then 1 end) notRec"
					+ " FROM neighbor_rec GROUP BY num) r ON b.num = r.num"
					+ " ORDER BY num DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROW ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				NeighborDTO dto = new NeighborDTO();
				dto.setNum(rs.getInt("num"));
				dto.setNickName(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setRec(rs.getInt("rec"));
				dto.setNotRec(rs.getInt("notRec"));
				list.add(dto);
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
		return list;
	}

	public List<NeighborDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<NeighborDTO> list = new ArrayList<NeighborDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql=" SELECT b.num, nickName, subject, TO_CHAR(created,'YYYY-MM-DD') created, hitCount, rec, notRec  "
					+ " FROM neighbor_bbs b"
					+ " LEFT OUTER JOIN member1 m ON b.userId = m.userId"
					+ " LEFT OUTER JOIN (SELECT num, COUNT(CASE WHEN type=1 then 1 end) rec, COUNT(CASE WHEN type=2 then 1 end) notRec"
					+ " FROM neighbor_rec GROUP BY num) r ON b.num = r.num";
				
			if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			} else if(condition.equals("all")) {
				sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else {
				sql += " WHERE INSTR("+condition+", ?) >= 1 ";
			}
					
			sql	+= " ORDER BY num DESC;"
				+ " OFFSET ? ROWS FETCH FIRST ? ROW ONLY";
			pstmt = conn.prepareStatement(sql);
            if(condition.equals("all")) {
    			pstmt.setString(1, keyword);
    			pstmt.setString(2, keyword);
    			pstmt.setInt(3, offset);
    			pstmt.setInt(4, rows);
            } else {
    			pstmt.setString(1, keyword);
    			pstmt.setInt(2, offset);
    			pstmt.setInt(3, rows);
            }
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				NeighborDTO dto = new NeighborDTO();
				dto.setNum(rs.getInt("num"));
				dto.setNickName(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setRec(rs.getInt("rec"));
				dto.setNotRec(rs.getInt("notRec"));
				list.add(dto);
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
		return list;
	}
	public int dataCount_nb(String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM neighbor_bbs WHERE userId = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
	
	public int dataCount_nb(String condition, String keyword,String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM neighbor_bbs b LEFT OUTER JOIN member1 m  ON b.userid=m.userid";
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND b.userId = ? ";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql = " WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND b.userId = ? ";
			} else {
				sql += " WHERE INSTR(" +condition+", ?) >=1 AND b.userId = ? ";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
				pstmt.setString(3, userId);
			} else {
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

	public List<NeighborDTO> listBoard_nb(int offset, int rows,String userId) {
		List<NeighborDTO> list = new ArrayList<NeighborDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql=" SELECT b.num, nickName, subject, TO_CHAR(created,'YYYY-MM-DD') created, hitCount, rec, notRec  "
					+ " FROM neighbor_bbs b"
					+ " LEFT OUTER JOIN member1 m ON b.userId = m.userId"
					+ " LEFT OUTER JOIN (SELECT num, COUNT(CASE WHEN type=1 then 1 end) rec, COUNT(CASE WHEN type=2 then 1 end) notRec"
					+ " FROM neighbor_rec GROUP BY num) r ON b.num = r.num"
					+ " WHERE b.userId = ?"
					+ " ORDER BY num DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROW ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				NeighborDTO dto = new NeighborDTO();
				dto.setNum(rs.getInt("num"));
				dto.setNickName(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setRec(rs.getInt("rec"));
				dto.setNotRec(rs.getInt("notRec"));
				list.add(dto);
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
		return list;
	}

	public List<NeighborDTO> listBoard_nb(int offset, int rows, String condition, String keyword,String userId) {
		List<NeighborDTO> list = new ArrayList<NeighborDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql=" SELECT b.num, nickName, subject, TO_CHAR(created,'YYYY-MM-DD') created, hitCount, rec, notRec  "
					+ " FROM neighbor_bbs b"
					+ " LEFT OUTER JOIN member1 m ON b.userId = m.userId"
					+ " LEFT OUTER JOIN (SELECT num, COUNT(CASE WHEN type=1 then 1 end) rec, COUNT(CASE WHEN type=2 then 1 end) notRec"
					+ " FROM neighbor_rec GROUP BY num) r ON b.num = r.num";
				
			if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND b.userId = ? ";
			} else if(condition.equals("all")) {
				sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND b.userId = ? ";
			} else {
				sql += " WHERE INSTR("+condition+", ?) >= 1 AND b.userId = ? ";
			}
					
			sql	+= " ORDER BY num DESC;"
				+ " OFFSET ? ROWS FETCH FIRST ? ROW ONLY";
			pstmt = conn.prepareStatement(sql);
            if(condition.equals("all")) {
    			pstmt.setString(1, keyword);
    			pstmt.setString(2, keyword);
    			pstmt.setString(3, userId);
    			pstmt.setInt(4, offset);
    			pstmt.setInt(5, rows);
            } else {
    			pstmt.setString(1, keyword);
    			pstmt.setString(2, userId);
    			pstmt.setInt(3, offset);
    			pstmt.setInt(4, rows);
            }
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				NeighborDTO dto = new NeighborDTO();
				dto.setNum(rs.getInt("num"));
				dto.setNickName(rs.getString("nickname"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setRec(rs.getInt("rec"));
				dto.setNotRec(rs.getInt("notRec"));
				list.add(dto);
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
		return list;
	}
}
