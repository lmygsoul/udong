package com.used;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class UsedDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertUsed(UsedDTO dto ) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "INSERT INTO used(num, userId, subject, content, category, area, price, imageFilename, created)"
				+ " VALUES(used_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getArea());
			pstmt.setString(6, dto.getPrice());
			pstmt.setString(7, dto.getImageFilename());
			
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
		
		
	//데이터 개수
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM used";
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
	
	// 검색에서의 데이터 개수
		public int dataCount(String condition, String keyword) {
			int result=0;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql;
			
			try {
				sql="SELECT NVL(COUNT(*), 0)  FROM used u JOIN member1 m ON u.userId=m.userId ";
				if(condition.equals("all")) {
					sql+="  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
				} else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sql += "  WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
				} else {
					sql += "  WHERE INSTR(" + condition+ ", ?) >= 1 ";
				}
				
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, keyword);
				if(condition.equals("all")) {
					pstmt.setString(2, keyword);
				}
				
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
	
	//글리스트
	public List<UsedDTO> listUsed(int offset, int rows) {
		List<UsedDTO> list=new ArrayList<UsedDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, u.userId, category, nickName, subject, TO_CHAR(created, 'YYYY-MM-DD') created");
			sb.append(" FROM used u JOIN member1 m ON u.userId=m.userId  ");
			sb.append(" ORDER BY num DESC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				UsedDTO dto=new UsedDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setCategory(rs.getString("category"));
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
	
	//검색글리스트
	public List<UsedDTO> listUsed(int offset, int rows, String condition, String keyword) {
		List<UsedDTO> list=new ArrayList<UsedDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql= "SELECT num, u.userId, nickName, subject, category, TO_CHAR(created, 'YYYY-MM-DD') created "
					+ " FROM used u  "
					+ " JOIN member1 m ON u.userId = m.userId  ";
			if(condition.equals("all")) {
				sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			}else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			}else {
				sql += " WHERE INSTR(" + condition + ", ?) >=1";
			}
				sql	+= " ORDER BY num DESC "
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
				
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
				UsedDTO dto=new UsedDTO();			
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setCategory(rs.getString("category"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return list;
	}
	
	
	//글보기
	public UsedDTO readUsed(int num) {
		UsedDTO dto=null;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, u.userId, nickName, subject, content, price, area, imageFilename, created");
			sb.append(" FROM used u JOIN member1 m ON u.userId=m.userId");
			sb.append(" WHERE num=?");						
				
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new UsedDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setPrice(rs.getString("price"));
				dto.setArea(rs.getString("area"));
				dto.setImageFilename(rs.getString("imageFilename"));
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
	
	//이전글
	public UsedDTO preReadUsed(int num, String condition, String keyword) {
		UsedDTO dto = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword.length() != 0) {//검색
                sb.append("SELECT num, subject FROM used u JOIN member1 m ON u.userId = m.userId "); //게시물번호, 제목
                
                if(condition.equals("all")) {//제목또는 내용으로 검색
                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >=1 ");
                } else if(condition.equals("created")) { //등록일자로 검색
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
                } else {//username, subject, contet 중 하나일 때 
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");//제목,내용,이름 다 검색 가능
                }
                sb.append("            AND (num > ? ) "); //num이 커야함
                sb.append(" ORDER BY num ASC "); //큰것 중에 오름차순 정렬
                sb.append(" FETCH  FIRST  1  ROWS  ONLY "); //정렬한 것 중에 첫번째 데이터(제일 작은거)만 가져온다

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
                sb.append("SELECT num, subject FROM used ");
                sb.append(" WHERE num > ? ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new UsedDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }

            if(rs.next()) {
                dto=new UsedDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}
		return dto;
		
}	
		
		
//다음글 
public UsedDTO nextReadUsed(int num, String condition, String keyword) {
  UsedDTO dto=null;

  PreparedStatement pstmt=null;
  ResultSet rs=null;
  StringBuilder sb = new StringBuilder();
       
  try {
	  if(keyword.length() != 0) {
		  sb.append("SELECT num, subject FROM used u ");
		  sb.append(" JOIN member1 m ON  u.userId = m.userId ");
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
		  sb.append("SELECT num, subject FROM used ");
		  sb.append(" WHERE num < ? ");
		  sb.append(" ORDER BY num DESC ");
		  sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

		  pstmt=conn.prepareStatement(sb.toString());
		  pstmt.setInt(1, num);
	  }

	  rs=pstmt.executeQuery();

	  if(rs.next()) {
		  dto=new UsedDTO();
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
	
	
	//수정
	public int updateUsed(UsedDTO dto) throws SQLException{
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "UPDATE used SET subject=?, content=?, price=?, area=?, imageFilename=? WHERE num=? AND userId=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getPrice());
			pstmt.setString(4, dto.getArea());
			pstmt.setString(5, dto.getImageFilename());
			pstmt.setInt(6, dto.getNum());
			pstmt.setString(7, dto.getUserId());
			
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
	//삭제
	public int deleteUsed(int num, String userId) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "DELETE FROM used WHERE num=?";
			if( ! userId.equals("admin")) { //관리자가 아닌경우 
				sql += " AND userId = ?"; //작성한 사람만 삭제 가능 
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			if(! userId.equals("admin")) {
				pstmt.setString(2, userId);
			}
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}
		
		return result;
	}
	
	
}

