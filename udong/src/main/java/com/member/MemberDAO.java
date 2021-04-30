package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public MemberDTO readMember(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.userId, userPwd, userName, nickName, type, TO_CHAR(created_date, 'YYYY-MM-DD') created_date,");
			sb.append(" birth, email, tel, zipcode, addr1, addr2, myComment FROM member1 m1");
			sb.append(" LEFT OUTER JOIN member2 m2 ON m1.userId= m2.userId WHERE m1.userId = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setNickName(rs.getString("nickName"));
				dto.setType(rs.getString("type"));
				dto.setCreated_date(rs.getString("created_date"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail()!=null) {
					String[] ss= dto.getEmail().split("@");
					if(ss.length==2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
				dto.setTel(rs.getString("tel"));
				if(dto.getTel()!=null) {
					String[] ss= dto.getTel().split("@");
					if(ss.length==3) {
						dto.setTel1(ss[0]);
						dto.setTel2(ss[1]);
						dto.setTel3(ss[2]);
					}
				}
				dto.setZipCode(rs.getString("zipCode"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setMyComment(rs.getString("myComment"));
			}
		} catch (Exception e) {
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
	public MemberDTO readMember_nick(String nickName) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.userId, userPwd, userName, nickName, type, TO_CHAR(created_date, 'YYYY-MM-DD') created_date,");
			sb.append(" birth, email, tel, zipcode, addr1, addr2, myComment FROM member1 m1");
			sb.append(" LEFT OUTER JOIN member2 m2 ON m1.userId= m2.userId WHERE nickName = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, nickName);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setNickName(rs.getString("nickName"));
				dto.setType(rs.getString("type"));
				dto.setCreated_date(rs.getString("created_date"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail()!=null) {
					String[] ss= dto.getEmail().split("@");
					if(ss.length==2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
				dto.setTel(rs.getString("tel"));
				if(dto.getTel()!=null) {
					String[] ss= dto.getTel().split("@");
					if(ss.length==3) {
						dto.setTel1(ss[0]);
						dto.setTel2(ss[1]);
						dto.setTel3(ss[2]);
					}
				}
				dto.setZipCode(rs.getString("zipCode"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setMyComment(rs.getString("myComment"));
			}
		} catch (Exception e) {
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
	public int insertMember(MemberDTO dto) throws SQLException{
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO member1(userId, userPwd, userName, nickName, type, created_date) VALUES (?,?,?,?,?,SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			pstmt.setString(4, dto.getNickName());
			if(dto.getType()==null)
				dto.setType("1");
			pstmt.setString(5, dto.getType());
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			sql="INSERT INTO member2(userId, birth, email, tel, zipcode, addr1, addr2, myComment) VALUES (?,TO_DATE(?,'YYYYMMDD'),?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getTel());
			pstmt.setString(5, dto.getZipCode());
			pstmt.setString(6, dto.getAddr1());
			pstmt.setString(7, dto.getAddr2());
			pstmt.setString(8, dto.getMyComment());
			
			result+=pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
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
	public int updateMember(MemberDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "UPDATE member1 SET userPwd=?, nickName= ?, type=?  WHERE userId=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getNickName());
			if(dto.getType()==null)
				dto.setType("1");
			pstmt.setString(3, dto.getType());
			pstmt.setString(4, dto.getUserId());
			
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			sql = "UPDATE member2 SET email=?, tel=?, zipCode=?, addr1=?, addr2=?, myComment = ? WHERE userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getZipCode());
			pstmt.setString(4, dto.getAddr1());
			pstmt.setString(5, dto.getAddr2());
			pstmt.setString(6, dto.getMyComment());
			pstmt.setString(7, dto.getUserId());
			
			result+=pstmt.executeUpdate();

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
