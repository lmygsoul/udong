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
		ResultSet rs=null;
		String sql;
		int seq;
		
		
		try {
			sql="SELECT used_seq.NEXTVAL FROM dual";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			seq=0;
			if(rs.next()) {
				seq=rs.getInt(1);
			}
			dto.setNum(seq);
			
			rs.close();
			pstmt.close();
			rs=null;
			pstmt=null;
			
			sql="INSERT INTO used (num, userId, subject, content, area, price, imageFilename, done, created)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getArea());
			pstmt.setString(6, dto.getPrice());
			pstmt.setString(7, dto.getImageFilename());
			pstmt.setString(8, dto.getDone());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles()!=null) {
				sql = "INSERT INTO usedFile(fileNum, num, imageFilename) VALUES (usedFile_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					pstmt.executeUpdate();
				}
			}
			
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt !=null) {
				try {
					pstmt.close();
				}catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	//전체글수
	public int dataCount(String userId) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM used WHERE userId= ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
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
	
	//글리스트
	public List<UsedDTO> listUsed(int offset, int rows, String userId) {
		List<UsedDTO> list =new ArrayList<UsedDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT u.num, u.userId, subject, imageFilename ");
			sb.append(" FROM used u ");
			sb.append(" JOIN member1 m ON u.userId = m.userId ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("    SELECT fileNum, num, imageFilename FROM ( ");
			sb.append("        SELECT fileNum, num, imageFilename, ");
			sb.append("            ROW_NUMBER() OVER(PARTITION BY num ORDER BY fileNum ASC) rank ");
			sb.append("        FROM usedFile");
			sb.append("    ) WHERE rank = 1 ");
			sb.append(" ) i ON u.num = i.num ");
			sb.append(" WHERE u.userId = ? ");
			sb.append(" ORDER BY num DESC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				UsedDTO dto=new UsedDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imageFilename"));
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
	
	//글보기
	public UsedDTO readUsed(int num) {
		UsedDTO dto=null;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, u.userId,  ");
			sb.append("    nickName, subject, content, price, area");
			sb.append("    likeCount, ");
			sb.append("    created, ");
			sb.append("    imageFilename ");
			sb.append(" FROM used u");
			sb.append(" JOIN member1 m ON p.userId=m.userId");
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
				dto.setLikeCount(rs.getInt("likeCount"));
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
	//수정
	public int updateUsed(UsedDTO dto) throws SQLException{
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "UPDATE used SET subject=?, content=?, imageFilename=?, price=?, area=? "
			    + " WHERE num=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getImageFilename());
			pstmt.setInt(4, dto.getNum());
			pstmt.setString(5, dto.getPrice());
			pstmt.setString(6, dto.getArea());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles()!=null) {
				sql = "INSERT INTO usedFile(fileNum, num, imageFilename) VALUES (usedFile_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					pstmt.executeUpdate();
				}
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
	//삭제
	public int deleteUsed(int num) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "DELETE FROM used WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
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
	
	public List<UsedDTO> listPhotoFile(int num) {
		List<UsedDTO> list=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql = "SELECT fileNum, num, imageFilename FROM usedFile WHERE num = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				UsedDTO dto=new UsedDTO();
				
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setNum(rs.getInt("num"));
				dto.setImageFilename(rs.getString("imageFilename"));
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
	
	//사진파일보기
	public UsedDTO readPhotoFile(int fileNum) {
		UsedDTO dto = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql = "SELECT fileNum, num, imageFilename FROM usedFile WHERE fileNum = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, fileNum);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new UsedDTO();
				
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setNum(rs.getInt("num"));
				dto.setImageFilename(rs.getString("imageFilename"));
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
	
	//이전사진
	public UsedDTO preReadUsed(int num, String userId) {
    	UsedDTO dto=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	sql = "SELECT num, subject FROM used  WHERE num > ? AND userId = ? "
        			+ " ORDER BY num ASC "
        			+ " FETCH  FIRST  1  ROWS  ONLY ";

        	pstmt=conn.prepareStatement(sql);
        	pstmt.setInt(1, num);
        	pstmt.setString(2, userId);

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
	
	//다음사진
    public UsedDTO nextReadUsed(int num, String userId) {
    	UsedDTO dto=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	sql = "SELECT num, subject FROM used  WHERE num < ? AND userId = ? "
        			+ " ORDER BY num DESC "
        			+ " FETCH  FIRST  1  ROWS  ONLY ";

        	pstmt=conn.prepareStatement(sql);
        	pstmt.setInt(1, num);
        	pstmt.setString(2, userId);

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
	
    //사진삭제
	public int deletePhotoFile(String mode, int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = "DELETE FROM sphotoFile WHERE num = ?";
			} else {
				sql = "DELETE FROM sphotoFile WHERE fileNum = ?";
			}
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}
	
}

