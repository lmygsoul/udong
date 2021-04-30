package com.udong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.neighbor.NeighborReplyDTO;
import com.util.DBConn;

public class UdongDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(UdongDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO udong(num, subject, content, userId, hitCount, created) "
					+ " VALUES(udong_seq.NEXTVAL, ?, ?, ?, 0, SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getUserId());
			
			result = pstmt.executeUpdate();
			
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
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM udong";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public List<UdongDTO> listBoard(int offset, int rows) {
		List<UdongDTO> list = new ArrayList<UdongDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT num, userName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created"
				+ "  FROM  udong  b"
				+ "  JOIN member1 m ON b.userId = m.userId"
				+ "  ORDER BY num DESC"
				+ "  OFFSET  ?  ROWS  FETCH  FIRST  ?  ROWS  ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				UdongDTO dto = new UdongDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	//  검색일 때 데이터 개수
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM udong b JOIN member1 m ON b.userId = m.userId ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >= 1 ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	// 검색일때 리스트
	public List<UdongDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<UdongDTO> list = new ArrayList<UdongDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT num, userName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created"
				+ "  FROM  udong  b"
				+ "  JOIN member1 m ON b.userId = m.userId";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE  INSTR(" + condition + ", ?) >= 1";
			}
			sql += "  ORDER BY num DESC"
				 + "  OFFSET  ?  ROWS  FETCH  FIRST  ?  ROWS  ONLY";
			
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
				while(rs.next()) {
					UdongDTO dto = new UdongDTO();
					dto.setNum(rs.getInt("num"));
					dto.setUserName(rs.getString("userName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setCreated(rs.getString("created"));
					
					list.add(dto);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}

	// 글 조회수 증가
	public int updateHitCount(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE udong SET hitCount = hitCount + 1 WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
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
	
	public UdongDTO readBoard(int num) {
		UdongDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, b.userId, userName, subject, content, hitCount, created "
				+ "  FROM udong b "
				+ "  JOIN member1 m ON b.userId = m.userId "
				+ "  WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new UdongDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
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
	
    // 이전글
    public UdongDTO preReadBoard(int num, String condition, String keyword) {
        UdongDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM udong  b ");
                sb.append(" JOIN member1 m ON b.userId = m.userId ");
                if(condition.equals("all")) {
                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) ");
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("            AND (num > ? ) ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                } else {
                    pstmt.setString(1, keyword);
                   	pstmt.setInt(2, num);
                }
            } else {
                sb.append("SELECT num, subject FROM udong ");
                sb.append(" WHERE num > ? ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new UdongDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}
    
        return dto;
    }

    // 다음글
    public UdongDTO nextReadBoard(int num, String condition, String keyword) {
        UdongDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM udong b ");
                sb.append(" JOIN member1 m ON  b.userId = m.userId ");
                if(condition.equals("all")) {
                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  )  ");
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("          AND (num < ? ) ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                } else {
                    pstmt.setString(1, keyword);
                   	pstmt.setInt(2, num);
                }
            } else {
                sb.append("SELECT num, subject FROM udong ");
                sb.append(" WHERE num < ? ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new UdongDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}

        return dto;
    }
 // 게시물 수정
 	public int updateBoard(UdongDTO dto) throws SQLException {
 		int result = 0;
 		PreparedStatement pstmt = null;
 		String sql;
 		
 		sql="UPDATE udong SET subject=?, content=? WHERE num=? AND userId=?";
 		try {
 			pstmt = conn.prepareStatement(sql);
 			pstmt.setString(1, dto.getSubject());
 			pstmt.setString(2, dto.getContent());
 			pstmt.setInt(3, dto.getNum());
 			pstmt.setString(4, dto.getUserId());
 			result = pstmt.executeUpdate();
 			
 		} catch (SQLException e) {
 			e.printStackTrace();
 			throw e;
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
 	
 	// 게시물 삭제
 	public int deleteBoard(int num, String userId) throws SQLException {
 		int result = 0;
 		PreparedStatement pstmt = null;
 		String sql;
 		
 		try {
 			if(userId.equals("admin")) {
 				sql="DELETE FROM udong WHERE num=?";
 				pstmt = conn.prepareStatement(sql);
 				pstmt.setInt(1, num);
 				result = pstmt.executeUpdate();
 			}else {
 				sql="DELETE FROM udong WHERE num=? AND userId=?";
 				pstmt = conn.prepareStatement(sql);
 				pstmt.setInt(1, num);
 				pstmt.setString(2, userId);
 				result = pstmt.executeUpdate();
 			}
 			
 		} catch (SQLException e) {
 			e.printStackTrace();
 			throw e;
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
 	public int dataCount_ud(String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM udong WHERE userId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public List<UdongDTO> listBoard_ud(int offset, int rows,String userId) {
		List<UdongDTO> list = new ArrayList<UdongDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT num, userName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created"
				+ "  FROM  udong  b"
				+ "  JOIN member1 m ON b.userId = m.userId"
				+ "	 WHERE b.userId = ? "
				+ "  ORDER BY num DESC"
				+ "  OFFSET  ?  ROWS  FETCH  FIRST  ?  ROWS  ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				UdongDTO dto = new UdongDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	//  검색일 때 데이터 개수
	public int dataCount_ud(String condition, String keyword,String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM udong b JOIN member1 m ON b.userId = m.userId ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND b.userId = ?";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND b.userId = ? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >= 1 AND b.userId = ? ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
				pstmt.setString(3, userId);
			} else {
				pstmt.setString(2, userId);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	// 검색일때 리스트
	public List<UdongDTO> listBoard_ud(int offset, int rows, String condition, String keyword,String userId) {
		List<UdongDTO> list = new ArrayList<UdongDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT num, userName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created"
				+ "  FROM  udong  b"
				+ "  JOIN member1 m ON b.userId = m.userId";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND b.userId = ? ";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND b.userId = ? ";
			} else {
				sql += "  WHERE  INSTR(" + condition + ", ?) >= 1 AND b.userId = ? ";
			}
			sql += "  ORDER BY num DESC"
				 + "  OFFSET  ?  ROWS  FETCH  FIRST  ?  ROWS  ONLY";
			
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
				while(rs.next()) {
					UdongDTO dto = new UdongDTO();
					dto.setNum(rs.getInt("num"));
					dto.setUserName(rs.getString("userName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setCreated(rs.getString("created"));
					
					list.add(dto);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	public int updateHitCount_ud(int num,String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE udong SET hitCount = hitCount + 1 WHERE num = ? AND userId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
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
	// 이전글
    public UdongDTO preReadBoard_ud(int num, String condition, String keyword,String userId) {
        UdongDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM udong  b ");
                sb.append(" JOIN member1 m ON b.userId = m.userId ");
                if(condition.equals("all")) {
                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) AND b.userId =? ");
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  AND b.userId =?");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0)  AND b.userId =? ");
                }
                sb.append("            AND (num > ? ) ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setString(3, userId);
                   	pstmt.setInt(4, num);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, userId);
                   	pstmt.setInt(3, num);
                }
            } else {
                sb.append("SELECT num, subject FROM udong ");
                sb.append(" WHERE num > ? AND userId = ?");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
                pstmt.setString(2, userId);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new UdongDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}
    
        return dto;
    }

    // 다음글
    public UdongDTO nextReadBoard_ud(int num, String condition, String keyword,String userId) {
        UdongDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM udong b ");
                sb.append(" JOIN member1 m ON  b.userId = m.userId ");
                if(condition.equals("all")) {
                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  )   AND b.userId =?");
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) AND b.userId =? ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) AND b.userId =? ");
                }
                sb.append("          AND (num < ? ) ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setString(3, userId);
                   	pstmt.setInt(4, num);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, userId);
                   	pstmt.setInt(3, num);
                }
            } else {
                sb.append("SELECT num, subject FROM udong ");
                sb.append(" WHERE num < ?  AND userId =? ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
                pstmt.setString(2, userId);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new UdongDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}

        return dto;
    }
    public UdongDTO readBoard_ud(int num,String userId) {
		UdongDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, b.userId, userName, subject, content, hitCount, created "
				+ "  FROM udong b "
				+ "  JOIN member1 m ON b.userId = m.userId "
				+ "  WHERE num = ? AND b.userId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new UdongDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
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
    public int insertReply(UdongReplyDTO dto) {
		int result = 0;
		String sql;
		PreparedStatement pstmt = null;
		
		try {
			sql = "INSERT INTO udong_rep(num, articleNum, userid, content, created)"
					+ " VALUES (neighbor_rep_seq.NEXTVAL, ?, ?, ?, SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getArticlenum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getContent());
			
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
    public int replyCount(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM udong_rep WHERE articlenum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
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
	public List<UdongReplyDTO> replyBoard(int num) {
		List<UdongReplyDTO> list = new ArrayList<UdongReplyDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = " SELECT num, m.userId, userName, content, created"
					+ " FROM Udong_rep r"
					+ " LEFT OUTER JOIN member1 m ON r.userId = m.userId"
					+ " WHERE articleNum = ?"
					+ " ORDER BY num DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				UdongReplyDTO dto = new UdongReplyDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setContent(rs.getString("content"));
				dto.setArticlenum(num);
				dto.setUserId(rs.getString("userId"));
				dto.setCreated(rs.getString("created"));
				
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


