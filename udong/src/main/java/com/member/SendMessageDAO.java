package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DBConn;

public class SendMessageDAO {
	private Connection conn = DBConn.getConnection();
	
	public MessageDTO readMember(String userId) throws SQLException {
		MessageDTO mdto =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT sm.sendUser, sm.receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType FROM sendMessage sm" );
			sb.append(" LETF OUTER JOIN member1 m1 ON sm.sendUser = m1.userId WHERE sm.sendUser = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				mdto = new MessageDTO();
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setContent(rs.getString("content"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setMessageType(rs.getString("messageType"));
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
		
		return mdto;
	}
	
	public int insertMessage(MemberDTO dto,MessageDTO mdto) throws SQLException{
		int result =0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="INSERT INTO sendMessage(sendUser, receiveUser, subject, content, sendTime, messageType) VALUES(?,?,?,?,SYSDATE,?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, mdto.getSendUser());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, mdto.getSubject());
			pstmt.setString(4, mdto.getContent());
			mdto.setMessageType("1");
			pstmt.setString(5, mdto.getMessageType());
			
			result=pstmt.executeUpdate();
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
}
