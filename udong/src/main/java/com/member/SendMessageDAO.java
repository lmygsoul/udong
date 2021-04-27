package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class SendMessageDAO {
	private Connection conn = DBConn.getConnection();
	
	public MessageDTO readMember(int num) throws SQLException {
		MessageDTO mdto =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum, nickName, type FROM sendMessage sm" );
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId WHERE pageNum = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				mdto = new MessageDTO();
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setContent(rs.getString("content"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setMessageType(rs.getString("messageType"));
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setNickName(rs.getString("nickName"));
				mdto.setType(rs.getString("type"));
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
	public MessageDTO readMember(String userId) throws SQLException {
		MessageDTO mdto =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum, nickName, type FROM sendMessage sm" );
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId WHERE sm.sendUser = ? ");
			
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
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setNickName(rs.getString("nickName"));
				mdto.setType(rs.getString("type"));
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
	public int insertMessage(MessageDTO mdto) throws SQLException{
		int result =0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="INSERT INTO sendMessage(sendUser, receiveUser, subject, content, sendTime, messageType, pageNum) VALUES(?,?,?,?,SYSDATE,?,send_seq.NEXTVAL)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, mdto.getSendUser());
			pstmt.setString(2, mdto.getReceiveUser());
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
	public int dataCount(String userId) {
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM sendMessage WHERE sendUser = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
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
	public int dataCount(String userId, String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql=" SELECT COUNT(*) FROM sendMessage sm JOIN member1 m ON sm.sendUser = m.userId";
			
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND sendUser = ?";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql+=" WHERE TO_CHAR(created, 'YYYYMMDD') = ? AND sendUser = ?";
			} else {
				sql +=" WHERE INSTR(" + condition + ", ?) >= 1 AND sendUser = ?";
			}
			pstmt =conn.prepareStatement(sql);
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setString(3, userId);
			}else {
				pstmt.setString(1, keyword);
				pstmt.setString(2, userId);
			}
			
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
	public List<MessageDTO> listsm(int offset, int rows){
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum,type FROM sendMessage sm"
					+ " LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ORDER BY pageNum DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MessageDTO mdto = new MessageDTO();
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setMessageType(rs.getString("messageType"));
				mdto.setType(rs.getString("type"));
				
				list.add(mdto);
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
	
	public List<MessageDTO> listsm(int offset, int rows,String condition, String keyword){
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm"
					+ " LETF OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >=1";
			} else if (condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql +=" WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >= 1";
			}
			sql += " ORDER BY pageNum DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
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
				MessageDTO mdto = new MessageDTO();
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setMessageType(rs.getString("messageType"));
				
				list.add(mdto);
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
	public MessageDTO preReadSM(int num, String condition, String keyword) {
		MessageDTO mdto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm");
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ");
			if(condition.equals("all")) {
                sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) ");
            } else if(condition.equals("created")) {
            	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
            } else {
                sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
            }
            sb.append(" AND (pageNum > ? ) ORDER BY pageNum ASC FETCH  FIRST  1  ROWS  ONLY ");
            
            pstmt= conn.prepareStatement(sb.toString());
            if(condition.equals("all")) {
                pstmt.setString(1, keyword);
                pstmt.setString(2, keyword);
               	pstmt.setInt(3, num);
            } else {
                pstmt.setString(1, keyword);
               	pstmt.setInt(2, num);
            }
            rs=pstmt.executeQuery();
            if(rs.next()) {
                mdto=new MessageDTO();
                mdto.setPageNum(rs.getInt("pageNum"));
                mdto.setSubject(rs.getString("subject"));
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
		return mdto;
	}
	public MessageDTO nextReadSM(int num, String condition, String keyword) {
		MessageDTO mdto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm");
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ");
			if(condition.equals("all")) {
                sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) ");
            } else if(condition.equals("created")) {
            	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
            } else {
                sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
            }
            sb.append(" AND (pageNum < ? ) ORDER BY pageNum DESC FETCH  FIRST  1  ROWS  ONLY ");
            
            pstmt= conn.prepareStatement(sb.toString());
            if(condition.equals("all")) {
                pstmt.setString(1, keyword);
                pstmt.setString(2, keyword);
               	pstmt.setInt(3, num);
            } else {
                pstmt.setString(1, keyword);
               	pstmt.setInt(2, num);
            }
            rs=pstmt.executeQuery();
            if(rs.next()) {
                mdto=new MessageDTO();
                mdto.setPageNum(rs.getInt("pageNum"));
                mdto.setSubject(rs.getString("subject"));
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
		return mdto;
	}
}
