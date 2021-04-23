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
				dto.setType(rs.getInt("type"));
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
}
