package com.faq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class FaqDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertFaq(FaqDTO dto) throws SQLException{
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO faq(num, subject, content, userId, userName, created) "
					+ " VALUES(faq_seq.NEXTVAL,?,?,?,?,SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getUserId());
			pstmt.setString(4, dto.getUserName());
			
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
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT COUNT(*) FROM faq";
			pstmt = conn.prepareStatement(sql);
			

			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public List<FaqDTO> listFaq(int offset, int rows){
		List<FaqDTO> list = new ArrayList<FaqDTO>();
		//faq와 member1 equi join
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, b.userName, subject, "
					+ "TO_CHAR(created, 'YYYY-MM-DD') created "
					+ " FROM faq b "
					+ " JOIN member1 m ON b.userId = m.userId"
					+ " ORDER BY num DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);

			rs=pstmt.executeQuery();
			while(rs.next()) {
				FaqDTO dto = new FaqDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
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
	
	//검색일때 데이터 개수
	public int dataCount(String condition, String keyword) {
		int result=0;
		//faq와 member1 equi join
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM faq b JOIN member1 m ON b.userId = m.userId ";			
			
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql+=" WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			} else {
				sql +=" WHERE INSTR(" + condition + ", ?) >= 1";
			}
			
			pstmt =conn.prepareStatement(sql);
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
			}else {
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
					pstmt.close();
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
	
	//검색일때 리스트
	public List<FaqDTO> listFaq(int offset, int rows, String condition, String keyword){
		List<FaqDTO> list = new ArrayList<FaqDTO>();
		//faq와 member1 equi join
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, userName, subject, "
					+ "TO_CHAR(created, 'YYYY-MM-DD') created "
					+ " FROM faq b "
					+ " JOIN member1 m ON b.userId = m.userId";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >=1";
			} else if (condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql +=" WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >= 1";
			}
			sql += " ORDER BY num DESC "
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt=conn.prepareStatement(sql);
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			}else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, rows);
			}
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				FaqDTO dto = new FaqDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
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
	//글조회수 증가 
	public int updateHitCount(int num) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE faq SET hitCount = hitCount +1 WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			result =pstmt.executeUpdate();
			
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
	
	public FaqDTO readFaq(int num) {
		FaqDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, b.userId, b.userName, subject, content, created "
					+ " FROM faq b "
					+ " JOIN member1 m ON b.userId = m.userId "
					+ " WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs= pstmt.executeQuery();
			if(rs.next()) {
				dto = new FaqDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
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
	  public FaqDTO preReadFaq(int num, String condition, String keyword) {
	        FaqDTO dto=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuilder sb = new StringBuilder();

	        try {
	        	if(keyword.length() != 0) {
	                sb.append("SELECT num, subject FROM faq  b ");
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
	                sb.append("SELECT num, subject FROM faq ");
	                sb.append(" WHERE num > ? ");
	                sb.append(" ORDER BY num ASC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, num);
	            }
	        	
	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new FaqDTO();
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
	    public FaqDTO nextReadFaq(int num, String condition, String keyword) {
	        FaqDTO dto=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuilder sb = new StringBuilder();

	        try {
	        	if(keyword.length() != 0) {
	                sb.append("SELECT num, subject FROM faq b ");
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
	                sb.append("SELECT num, subject FROM faq ");
	                sb.append(" WHERE num < ? ");
	                sb.append(" ORDER BY num DESC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, num);
	            }

	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new FaqDTO();
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
	
	    public int updateFaq(FaqDTO dto) throws SQLException {
	    	int result = 0;
	    	PreparedStatement pstmt = null;
	    	String sql;
	    	
	    	try {
	    		// 게시판 테이블에서 subject, content 2개만 수정
	    		// 게시글을 올린 사람만 수정 가능하도록 > num, userId가 동일
				sql = "UPDATE faq SET subject=?, content=? WHERE num=? AND userId=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getSubject());
				pstmt.setString(2, dto.getContent());
				pstmt.setInt(3, dto.getNum());
				pstmt.setString(4, dto.getUserId());
				
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
	
	    public int deleteFaq(int num, String userId) throws SQLException {
	    	int result = 0;
	    	PreparedStatement pstmt = null;
	    	String sql;
	    	
	    	try {
	    		// 조건 : 게시글 작성자나 관리자만 지울 수 있다.
	    		sql="DELETE FROM faq WHERE num=?";
	    		// 관리자가 아닐 경우, userId 대조
	    		if(! userId.equals("admin")) {
	    			sql += " AND userId = ?";
	    		}
	    		pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				if(! userId.equals("admin")) {
					pstmt.setString(2, userId);
				}
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
}
