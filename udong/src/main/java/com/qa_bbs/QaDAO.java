package com.qa_bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class QaDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertBoard(QaDTO dto, String mode)  throws SQLException {
		int result = 0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		int seq;
		
		try {
			sql="SELECT qaBoard_seq.NEXTVAL FROM dual";
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
			if(mode.equals("created")) {
				// 질문일때
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if(mode.equals("reply")) {
				// 답변일때
				int selectreply = selectOrderNo(dto.getGroupNum(), dto.getOrderNo(), dto.getDepth());
				
				if(selectreply == 0) {
					dto.setOrderNo(ifzeroOrderNo(dto.getGroupNum())); 
					dto.setDepth(dto.getDepth() + 1);
				} else {
					updateOrderNo(dto.getGroupNum(), selectreply);
					dto.setOrderNo(selectreply);
					dto.setDepth(dto.getDepth() + 1);
				}

				
			}
			
			sql = "INSERT INTO qaBoard(boardNum, userId, subject, content, "
					+ "  groupNum, depth, orderNo, parent, hitCount, created) "
					+ "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, SYSDATE)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBoardNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());
			
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
	
	// 답변일경우 orderNo +1
	public int updateOrderNo(int groupNum, int orderNo) throws SQLException {
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		sql = "UPDATE qaBoard SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo >= ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
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
	
	public int selectOrderNo(int groupNum, int orderNo, int depth) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(MIN(orderNo),0) FROM qaBoard WHERE groupNum = ? "
				+ " AND orderNo > ? AND depth <= ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
			pstmt.setInt(3, depth);
			
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
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
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int ifzeroOrderNo(int groupNum) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(MAX(orderNo),0)+1 FROM qaBoard WHERE groupNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
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
				} catch (SQLException e) {
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
			sql="SELECT NVL(COUNT(*), 0) FROM qaBoard";
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
        	sql="SELECT NVL(COUNT(*), 0) FROM qaBoard b JOIN member1 m ON b.userId=m.userId ";
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
    
	public List<QaDTO> listBoard(int offset, int rows) {
		List<QaDTO> list=new ArrayList<QaDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT boardNum, b.userId, m.nickName,");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append(" FROM qaBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);

			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				QaDTO dto=new QaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
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
    public List<QaDTO> listBoard(int offset, int rows, String condition, String keyword) {
        List<QaDTO> list=new ArrayList<QaDTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT boardNum, b.userId, m.nickName, ");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,  ");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created  ");
			sb.append(" FROM qaBoard b  ");
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
                QaDTO dto=new QaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
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
    
	public QaDTO readBoard(int boardNum) {
		QaDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT boardNum, b.userId, m.nickName, subject, ");
			sb.append("    content, created, hitCount, groupNum, depth, orderNo, parent  ");
			sb.append(" FROM qaBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			sb.append(" WHERE boardNum=?  ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boardNum);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new QaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				dto.setHitCount(rs.getInt("hitCount"));
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
	
	public int updateHitCount(int boardNum) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		sql = "UPDATE qaBoard SET hitCount=hitCount+1 WHERE boardNum=?";
		
		try {
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
	
	public int updateBoard(QaDTO dto, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		sql="UPDATE qaBoard SET subject=?, content=? WHERE boardNum=? AND userId=?";
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
			sql="DELETE FROM qaBoard WHERE boardNum IN (SELECT boardNum FROM qaBoard START WITH  boardNum = ? CONNECT BY PRIOR boardNum = parent)";
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
    public QaDTO preReadBoard(int groupNum, int orderNo, String condition, String keyword) {
        QaDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT boardNum, subject  ");
    			sb.append(" FROM qaBoard b  ");
    			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND  ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  ");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1 ) AND  ");
            	}
                sb.append("         (( groupNum = ? AND orderNo > ?) OR (groupNum < ? ))  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());

                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setInt(2, groupNum);
                    pstmt.setInt(3, orderNo);
                    pstmt.setInt(4, groupNum);
                }
			} else {
                sb.append("SELECT boardNum, subject FROM qaBoard b JOIN member1 m ON b.userId=m.userId  ");                
                sb.append(" WHERE (groupNum = ? AND orderNo > ?) OR (groupNum < ? )  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QaDTO();
                dto.setBoardNum(rs.getInt("boardNum"));
                dto.setSubject(rs.getString("subject"));
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

    // 다음글
    public QaDTO nextReadBoard(int groupNum, int orderNo, String condition, String keyword) {
        QaDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT boardNum, subject  ");
    			sb.append(" FROM qaBoard b  ");
    			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND  ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  ");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1) AND  ");
    			}
                sb.append("          (( groupNum = ? AND orderNo < ?) OR (groupNum > ? ))  ");
                sb.append(" ORDER BY groupNum ASC, orderNo DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setInt(2, groupNum);
                    pstmt.setInt(3, orderNo);
                    pstmt.setInt(4, groupNum);
                }

			} else {
                sb.append("SELECT boardNum, subject FROM qaBoard b JOIN member1 m ON b.userId=m.userId  ");
                sb.append(" WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ? )  ");
                sb.append(" ORDER BY groupNum ASC, orderNo DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QaDTO();
                dto.setBoardNum(rs.getInt("boardNum"));
                dto.setSubject(rs.getString("subject"));
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
    public int dataCount_qa(String userId) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM qaBoard WHERE userId = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
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
    public int dataCount_qa(String condition, String keyword,String userId) {
        int result=0;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	sql="SELECT NVL(COUNT(*), 0) FROM qaBoard b JOIN member1 m ON b.userId=m.userId ";
        	if(condition.equals("created")) {
        		keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
        		sql+="  WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND b.userId= ? ";
        	} else if(condition.equals("all")) {
        		sql+="  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND b.userId= ? ";
        	} else {
        		sql+="  WHERE INSTR(" + condition + ", ?) >= 1 AND b.userId= ? ";
        	}
        	
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1, keyword);
            if(condition.equals("all")) {
                pstmt.setString(2, keyword);
                pstmt.setString(3, userId);
            } else {
            	pstmt.setString(2, userId);
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
    
	public List<QaDTO> listBoard_qa(int offset, int rows,String userId) {
		List<QaDTO> list=new ArrayList<QaDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT boardNum, b.userId, m.nickName,");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append(" FROM qaBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			sb.append(" WHERE b.userId = ? ");
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);

			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				QaDTO dto=new QaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
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
    public List<QaDTO> listBoard_qa(int offset, int rows, String condition, String keyword,String userId) {
        List<QaDTO> list=new ArrayList<QaDTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT boardNum, b.userId, m.nickName, ");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,  ");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created  ");
			sb.append(" FROM qaBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND b.userId= ? ");
			} else if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND b.userId= ? ");
			} else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >= 1 AND b.userId= ? ");
			}
			
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
            
			pstmt=conn.prepareStatement(sb.toString());
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
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
                QaDTO dto=new QaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
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
    public int updateHitCount_qa(int boardNum,String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		sql = "UPDATE qaBoard SET hitCount=hitCount+1 WHERE boardNum=? AND userId = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, userId);
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
    public QaDTO readBoard_qa(int boardNum,String userId) {
		QaDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT boardNum, b.userId, m.nickName, subject, ");
			sb.append("    content, created, hitCount, groupNum, depth, orderNo, parent  ");
			sb.append(" FROM qaBoard b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			sb.append(" WHERE boardNum=? AND b.userId = ?");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, userId);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new QaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				dto.setHitCount(rs.getInt("hitCount"));
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
    // 이전글
    public QaDTO preReadBoard_qa(int groupNum, int orderNo, String condition, String keyword,String userId) {
        QaDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT boardNum, subject  ");
    			sb.append(" FROM qaBoard b  ");
    			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND b.userId = ? AND  ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND b.userId = ? AND ");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1 ) AND b.userId = ? AND ");
            	}
                sb.append("         (( groupNum = ? AND orderNo > ?) OR (groupNum < ? ))  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());

                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setString(3, userId);
                    pstmt.setInt(4, groupNum);
                    pstmt.setInt(5, orderNo);
                    pstmt.setInt(6, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, userId);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                }
			} else {
                sb.append("SELECT boardNum, subject FROM qaBoard b JOIN member1 m ON b.userId=m.userId  ");                
                sb.append(" WHERE (groupNum = ? AND orderNo > ?) OR (groupNum < ? ) AND b.userId = ? ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
                pstmt.setString(4, userId);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QaDTO();
                dto.setBoardNum(rs.getInt("boardNum"));
                dto.setSubject(rs.getString("subject"));
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

    // 다음글
    public QaDTO nextReadBoard_qa(int groupNum, int orderNo, String condition, String keyword,String userId) {
        QaDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT boardNum, subject  ");
    			sb.append(" FROM qaBoard b  ");
    			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND b.userId = ? AND ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  b.userId = ? AND");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1) AND b.userId = ? AND ");
    			}
                sb.append("          (( groupNum = ? AND orderNo < ?) OR (groupNum > ? ))  ");
                sb.append(" ORDER BY groupNum ASC, orderNo DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setString(3, userId);
                    pstmt.setInt(4, groupNum);
                    pstmt.setInt(5, orderNo);
                    pstmt.setInt(6, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, userId);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                }

			} else {
                sb.append("SELECT boardNum, subject FROM qaBoard b JOIN member1 m ON b.userId=m.userId  ");
                sb.append(" WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ? ) AND b.userId = ? ");
                sb.append(" ORDER BY groupNum ASC, orderNo DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
                pstmt.setString(4, userId);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QaDTO();
                dto.setBoardNum(rs.getInt("boardNum"));
                dto.setSubject(rs.getString("subject"));
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
}
