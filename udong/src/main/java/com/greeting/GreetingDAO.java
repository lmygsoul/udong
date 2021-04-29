package com.greeting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class GreetingDAO {
	private Connection conn = DBConn.getConnection();
	//DB에 INSERT 
	public int insertGreeting(GreetingDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO greeting(num, userId, subject, content, created, hitCount)"
				+ " VALUES(greeting_seq.NEXTVAL, ?, ?, ?, SYSDATE, 0)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			
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
	
	//전체데이터수
	public int dataCount() { 
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM greeting";
			pstmt = conn.prepareStatement(sql);
			
			rs= pstmt.executeQuery(); //rs=실행결과
			if(rs.next()) { 
				result = rs.getInt(1); //위 SELECT 문에 컬럼명 없어서 첫번째라는 의미의 1
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
	
	//게시물 리스트 //greeting과 member1 EQUI JOIN (member1에서 nickName을 가져와야 하기 때문)
		public List<GreetingDTO> listGreeting(int offset, int rows) {
			List<GreetingDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT num, g.userId, nickName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created "
						+ " FROM greeting g "
						+ " JOIN member1 m ON g.userId = m.userId "
						+ " ORDER BY num DESC "
						+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, offset);
				pstmt.setInt(2, rows);
				
				rs = pstmt.executeQuery();
				while(rs.next()) {
					GreetingDTO dto = new GreetingDTO();
					dto.setNum(rs.getInt("num"));
					dto.setNickName(rs.getString("nickName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
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
					}catch (Exception e2) {					
					}
				}
			}
			return list;
		}

		//검색 전체 데이터 수 //greeting과 member1 EQUI JOIN
			public int dataCount(String condition, String keyword) {
				int result = 0;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT COUNT(*) FROM greeting g JOIN member1 m ON g.userId = m.userId ";
					if(condition.equals("all")) { //제목과 내용 둘 다 검색 가능
						sql  += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
					}else if(condition.equals("created")) {
						keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
						sql += " WHERE TO_CHAR(created, YYYYMMDD') = ? ";
					}else {
						sql += " WHERE INSTR(" + condition + ", ?) >= 1";
					}
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, keyword);
					if(condition.equals("all")) {
						pstmt.setString(2, keyword);
					}
					
					rs = pstmt.executeQuery();
					if(rs.next() ) {
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
			
			//검색일때 리스트 //greeting과 member1 EQUI JOIN
			public List<GreetingDTO> listBoard(int offset, int rows, String condition, String keyword) {
				List<GreetingDTO> list = new ArrayList<GreetingDTO>();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT num, g.userId, nickName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created "
							+ " FROM greeting g"
							+ " JOIN member1 m ON g.userId = m.userId ";
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
						GreetingDTO dto = new GreetingDTO();
						dto.setNum(rs.getInt("num"));
						dto.setUserId(rs.getString("userId"));
						dto.setNickName(rs.getString("nickName"));
						dto.setSubject(rs.getString("subject"));
						dto.setHitCount(rs.getInt("hitCount"));
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
			

			//글 조회수 증가
			public int updateHitCount(int num) throws SQLException {
				int result = 0;
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					sql = "UPDATE greeting SET hitCount = hitCount + 1 WHERE num = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, num);  //해당되는 num일 경우 hitCount+1을 줘야함
					result = pstmt.executeUpdate();
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(pstmt !=null) {
						try {
							pstmt.close();
						} catch (Exception e2) {
						}
					}
				}
				return result;
			}
			
			//글보기 
			public GreetingDTO readGreeting(int num) {
				GreetingDTO dto = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT num, g.userId, nickName, subject, content, hitCount, created FROM greeting g " //userId 가져와야 게시글 올린사람만 수정 및 삭제 가능
							+ " JOIN member1 m ON g.userId = m.userId "
							+ " WHERE num = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1,num);
					
					rs= pstmt.executeQuery();
					if(rs.next()) {
						dto = new GreetingDTO();
						dto.setNum(rs.getInt("num"));
						dto.setUserId(rs.getString("userId"));
						dto.setNickName(rs.getString("nickName"));
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
			
			//이전글
			public GreetingDTO preReadGreeting(int num, String condition, String keyword) {
				GreetingDTO dto = null;
				
				PreparedStatement pstmt =null;
				ResultSet rs = null;
				StringBuilder sb = new StringBuilder();
				
				try {
					if(keyword.length() != 0) {//검색
		                sb.append("SELECT num, subject FROM greeting g JOIN member1 m ON g.userId = m.userId "); //게시물번호, 제목
		                
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
		                sb.append("SELECT num, subject FROM greeting ");
		                sb.append(" WHERE num > ? ");
		                sb.append(" ORDER BY num ASC ");
		                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

		                pstmt=conn.prepareStatement(sb.toString());
		                pstmt.setInt(1, num);
		            }
		        	
		            rs=pstmt.executeQuery();

		            if(rs.next()) {
		                dto=new GreetingDTO();
		                dto.setNum(rs.getInt("num"));
		                dto.setSubject(rs.getString("subject"));
		            }

		            if(rs.next()) {
		                dto=new GreetingDTO();
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
		public GreetingDTO nextReadGreeting(int num, String condition, String keyword) {
	      GreetingDTO dto=null;

		  PreparedStatement pstmt=null;
		  ResultSet rs=null;
		  StringBuilder sb = new StringBuilder();
		       
		  try {
			  if(keyword.length() != 0) {
				  sb.append("SELECT num, subject FROM greeting g ");
				  sb.append(" JOIN member1 m ON  g.userId = m.userId ");
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
				  sb.append("SELECT num, subject FROM greeting ");
				  sb.append(" WHERE num < ? ");
				  sb.append(" ORDER BY num DESC ");
				  sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

				  pstmt=conn.prepareStatement(sb.toString());
				  pstmt.setInt(1, num);
			  }

			  rs=pstmt.executeQuery();

			  if(rs.next()) {
				  dto=new GreetingDTO();
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
		public int updateGreeting(GreetingDTO dto) throws SQLException {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
				
			try {
				sql= "UPDATE greeting SET subject=?, content=? WHERE num=? AND userId=?";//and조건->작성한 사람만 수정 가능
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getSubject());
				pstmt.setString(2, dto.getContent());
				pstmt.setInt(3, dto.getNum());
				pstmt.setString(4, dto.getUserId());
					
				result = pstmt.executeUpdate();//쿼리실행
									
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
			
		//삭제 //게시글 올린사람 or 관리자만 삭제 가능 
		public int deleteGreeting(int num, String userId) throws SQLException {
			int result = 0;
			PreparedStatement pstmt =null;
			String sql;
				
			try {
				sql="DELETE FROM greeting WHERE num = ?";
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
				if(pstmt !=null) {
					try {
						pstmt.close();
					} catch (Exception e2) {
					}
				}
			}
			return result;
		}
		public int dataCount_gt(String userId) { 
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) FROM greeting WHERE userId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				rs= pstmt.executeQuery(); //rs=실행결과
				if(rs.next()) { 
					result = rs.getInt(1); //위 SELECT 문에 컬럼명 없어서 첫번째라는 의미의 1
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
		public int dataCount_gt(String condition, String keyword,String userId) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) FROM greeting g JOIN member1 m ON g.userId = m.userId ";
				if(condition.equals("all")) { //제목과 내용 둘 다 검색 가능
					sql  += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND g.userId = ? ";
				}else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sql += " WHERE TO_CHAR(created, YYYYMMDD') = ? AND g.userId = ?";
				}else {
					sql += " WHERE INSTR(" + condition + ", ?) >= 1 AND g.userId = ?";
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
				if(rs.next() ) {
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
		public List<GreetingDTO> listBoard_gt(int offset, int rows,String userId) {
			List<GreetingDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT num, g.userId, nickName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created "
						+ " FROM greeting g "
						+ " JOIN member1 m ON g.userId = m.userId "
						+ " WHERE g.userId = ?"
						+ " ORDER BY num DESC "
						+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, rows);
				
				rs = pstmt.executeQuery();
				while(rs.next()) {
					GreetingDTO dto = new GreetingDTO();
					dto.setNum(rs.getInt("num"));
					dto.setNickName(rs.getString("nickName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
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
					}catch (Exception e2) {					
					}
				}
			}
			return list;
		}	
		public List<GreetingDTO> listBoard_gt(int offset, int rows, String condition, String keyword,String userId) {
			List<GreetingDTO> list = new ArrayList<GreetingDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT num, g.userId, nickName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created "
						+ " FROM greeting g"
						+ " JOIN member1 m ON g.userId = m.userId ";
				if(condition.equals("all")) {
					sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND g.userId = ? ";
				}else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND g.userId = ?";
				}else {
					sql += " WHERE INSTR(" + condition + ", ?) >=1 AND g.userId = ?";
				}
					sql	+= " ORDER BY num DESC "
						+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
					
					pstmt = conn.prepareStatement(sql);
					
				if(condition.equals("all")) {					
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setString(3, userId);
					pstmt.setInt(5, offset);
					pstmt.setInt(5, rows);
				} else {
					pstmt.setString(1, keyword);
					pstmt.setString(2, userId);
					pstmt.setInt(3, offset);
					pstmt.setInt(4, rows);
				}
				rs = pstmt.executeQuery();
				while(rs.next()) {
					GreetingDTO dto = new GreetingDTO();
					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setNickName(rs.getString("nickName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
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
		public int updateHitCount_gt(int num,String userId) throws SQLException {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "UPDATE greeting SET hitCount = hitCount + 1 WHERE num = ? AND userId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);  //해당되는 num일 경우 hitCount+1을 줘야함
				pstmt.setString(2, userId);
				result = pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(pstmt !=null) {
					try {
						pstmt.close();
					} catch (Exception e2) {
					}
				}
			}
			return result;
		}
		//이전글
		public GreetingDTO preReadGreeting_gt(int num, String condition, String keyword,String userId) {
			GreetingDTO dto = null;
			
			PreparedStatement pstmt =null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				if(keyword.length() != 0) {//검색
	                sb.append("SELECT num, subject FROM greeting g JOIN member1 m ON g.userId = m.userId "); //게시물번호, 제목
	                
	                if(condition.equals("all")) {//제목또는 내용으로 검색
	                    sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >=1 AND g.userId = ?");
	                } else if(condition.equals("created")) { //등록일자로 검색
	                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
	                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)AND g.userId = ? ");
	                } else {//username, subject, contet 중 하나일 때 
	                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) AND g.userId = ?");//제목,내용,이름 다 검색 가능
	                }
	                sb.append("            AND (num > ? ) "); //num이 커야함
	                sb.append(" ORDER BY num ASC "); //큰것 중에 오름차순 정렬
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY "); //정렬한 것 중에 첫번째 데이터(제일 작은거)만 가져온다

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
	                sb.append("SELECT num, subject FROM greeting ");
	                sb.append(" WHERE num > ? AND g.userId = ?");
	                sb.append(" ORDER BY num ASC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, num);
	                pstmt.setString(2, userId);
	            }
	        	
	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new GreetingDTO();
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
	public GreetingDTO nextReadGreeting_gt(int num, String condition, String keyword,String userId) {
      GreetingDTO dto=null;

	  PreparedStatement pstmt=null;
	  ResultSet rs=null;
	  StringBuilder sb = new StringBuilder();
	       
	  try {
		  if(keyword.length() != 0) {
			  sb.append("SELECT num, subject FROM greeting g ");
			  sb.append(" JOIN member1 m ON  g.userId = m.userId ");
			  if(condition.equals("all")) {
				  sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) AND g.userId = ? ");
			  } else if(condition.equals("created")) {
				  keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				  sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) AND g.userId = ?");
			  } else {
				  sb.append(" WHERE ( INSTR("+condition+", ?) > 0) AND g.userId = ? ");
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
			  sb.append("SELECT num, subject FROM greeting ");
			  sb.append(" WHERE num < ? AND g.userId = ? ");
			  sb.append(" ORDER BY num DESC ");
			  sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

			  pstmt=conn.prepareStatement(sb.toString());
			  pstmt.setInt(1, num);
			  pstmt.setString(2, userId);
		  }

		  rs=pstmt.executeQuery();

		  if(rs.next()) {
			  dto=new GreetingDTO();
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
	public GreetingDTO readGreeting_gt(int num,String userId) {
		GreetingDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, g.userId, nickName, subject, content, hitCount, created FROM greeting g " //userId 가져와야 게시글 올린사람만 수정 및 삭제 가능
					+ " JOIN member1 m ON g.userId = m.userId "
					+ " WHERE num = ? AND g.userId = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,num);
			pstmt.setString(2, userId);
			
			rs= pstmt.executeQuery();
			if(rs.next()) {
				dto = new GreetingDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNickName(rs.getString("nickName"));
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
	}
