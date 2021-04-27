package com.class_bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ClassDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(ClassDTO dto, String mode) throws SQLException {
		int result = 0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		int seq;
		
		try {
			sql="SELECT classBoard_seq.NEXTVAL FROM dual";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			seq=0;
			if(rs.next()) {
				seq=rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			rs=null;
			pstmt=null;
			
			dto.setBoardNum(seq);
			
			sql = "INSERT INTO classBoard(boardNum, userId, subject, content, "
					+ "  curClass, maxClass, created) "
					+ "  VALUES (?, ?, ?, ?, 0, ?, SYSDATE)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBoardNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getMaxClass());
			
			result=pstmt.executeUpdate();
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
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM classBoard";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	// 검색 에서 전체의 개수
    public int dataCount(String condition, String keyword) {
        int result=0;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	sql="SELECT NVL(COUNT(*), 0) FROM classBoard b JOIN member1 m ON b.userId=m.userId ";
        	if(condition.equals("created")) {
        		keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
        		sql+="  WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
        	} else if(condition.equals("all")) {
        		sql+="  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
        	} else {
        		sql+="  WHERE INSTR(" + condition + ", ?) >= 1 ";
        	}
        	
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1, keyword);
            if(condition.equals("all")) {
                pstmt.setString(2, keyword);
            }

            rs=pstmt.executeQuery();

            if(rs.next())
                result=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
        
        return result;
    }
    
	public List<ClassDTO> listBoard(int offset, int rows) {
		List<ClassDTO> list=new ArrayList<ClassDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT boardNum, b.userId, m.nickName,");
			sb.append("       subject, TO_CHAR(created, 'YYYY-MM-DD') created");
			sb.append(" FROM classBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);

			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				ClassDTO dto=new ClassDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return list;
	}
	
	// 검색에서 리스트
    public List<ClassDTO> listBoard(int offset, int rows, String condition, String keyword) {
        List<ClassDTO> list=new ArrayList<ClassDTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT boardNum, b.userId, m.nickName, ");
			sb.append("       subject, TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append(" FROM classBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ");
			} else if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >= 1  ");
			}
			
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
            
			pstmt=conn.prepareStatement(sb.toString());
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
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
                ClassDTO dto=new ClassDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setCurClass(rs.getInt("curClass"));
				dto.setMaxClass(rs.getInt("maxClass"));
				dto.setCreated(rs.getString("created"));
            
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
        
        return list;
    }
    
	public ClassDTO readBoard(int boardNum) {
		ClassDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT boardNum, b.userId, m.nickName, subject, ");
			sb.append("    content, created ");
			sb.append(" FROM classBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			sb.append(" WHERE boardNum=?  ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boardNum);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new ClassDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCurClass(rs.getInt("curClass"));
				dto.setMaxClass(rs.getInt("maxClass"));
				dto.setCreated(rs.getString("created"));
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return dto;
	}
	
	public int updateBoard(ClassDTO dto, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		sql="UPDATE classBoard SET subject=?, content=? WHERE boardNum=? AND userId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getBoardNum());
			pstmt.setString(4, userId);
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
	
	public int deleteBoard(int boardNum) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM classBoard WHERE boardNum=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
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
	
    // 이전글
	  public ClassDTO preReadBoard(int num, String condition, String keyword) {
	        ClassDTO dto=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuilder sb = new StringBuilder();

	        try {
	        	if(keyword.length() != 0) {
	                sb.append("SELECT boardNum, subject FROM classBoard b ");
	                sb.append(" JOIN member1 m ON b.userId = m.userId ");
	                if(condition.equals("all")) {
	                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) ");
	                } else if(condition.equals("created")) {
	                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
	                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
	                } else {
	                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
	                }
	                sb.append("            AND (boardNum > ? ) ");
	                sb.append(" ORDER BY boardNum ASC ");
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
	                sb.append("SELECT boradNum, subject FROM classBoard ");
	                sb.append(" WHERE boardNum < ? ");
	                sb.append(" ORDER BY boardNum ASC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, num);
	            }
	        	
	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new ClassDTO();
	                dto.setBoardNum(rs.getInt("boardNum"));
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
	    public ClassDTO nextReadBoard(int num, String condition, String keyword) {
	        ClassDTO dto=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuilder sb = new StringBuilder();

	        try {
	        	if(keyword.length() != 0) {
	                sb.append("SELECT boardNum, subject FROM classBoard b ");
	                sb.append(" JOIN member1 m ON  b.userId = m.userId ");
	                if(condition.equals("all")) {
	                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  )  ");
	                } else if(condition.equals("created")) {
	                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
	                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
	                } else {
	                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
	                }
	                sb.append("          AND (boardNum < ? ) ");
	                sb.append(" ORDER BY boardNum DESC ");
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
	                sb.append("SELECT boardNum, subject FROM classBoard ");
	                sb.append(" WHERE boardNum < ? ");
	                sb.append(" ORDER BY boardNum DESC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, num);
	            }

	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new ClassDTO();
	                dto.setBoardNum(rs.getInt("boardNum"));
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
	
}