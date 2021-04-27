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
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
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
		
		sql = "UPDATE qaBoard SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo > ?";
		
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
			sb.append("SELECT boardNum, b.userId, nickName,");
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
			sb.append("SELECT boardNum, b.userId, nickName, ");
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
			sb.append("SELECT boardNum, b.userId, nickName, subject, ");
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
			sql="DELETE FROM qaBoard WHERE boardNum IN (SELECT boardNum FROM board START WITH  boardNum = ? CONNECT BY PRIOR boardNum = parent)";
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
}