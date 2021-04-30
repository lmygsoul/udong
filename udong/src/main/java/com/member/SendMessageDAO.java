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
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum, nickName FROM sendMessage sm" );
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId WHERE pageNum = ?");
			
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
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum, nickName FROM sendMessage sm" );
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
			
			pstmt.close();
			pstmt=null;
			
			sql="INSERT INTO reciveMessage(receiveUser, sendUser, subject, content, sendTime, messageType, pageNum) VALUES(?,?,?,?,SYSDATE,?,send_seq.NEXTVAL)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, mdto.getReceiveUser());
			pstmt.setString(2, mdto.getSendUser());
			pstmt.setString(3, mdto.getSubject());
			pstmt.setString(4, mdto.getContent());
			mdto.setMessageType("1");
			pstmt.setString(5, mdto.getMessageType());
			
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
	public int dataCount_sm(String userId) {
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(userId.equals("admin1")||userId.equals("admin")||userId.equals("admin2")||userId.equals("admin3")||userId.equals("admin4")||userId.equals("admin5")||userId.equals("admin6")) {
				sql="SELECT COUNT(*) FROM sendMessage";
				pstmt = conn.prepareStatement(sql);
				
				rs=pstmt.executeQuery();
				if(rs.next()) {
					result=rs.getInt(1);
				}	
			}
			else {
				sql="SELECT COUNT(*) FROM sendMessage WHERE sendUser = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				rs=pstmt.executeQuery();
				if(rs.next()) {
					result=rs.getInt(1);
				}
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
	public int dataCount_sm(String userId, String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql=" SELECT COUNT(*) FROM sendMessage sm JOIN member1 m ON sm.sendUser = m.userId";
			
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND sendUser = ?";
			} else if(condition.equals("sendTime")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql+=" WHERE TO_CHAR(sendTime, 'YYYYMMDD') = ? AND sendUser = ?";
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
	public List<MessageDTO> listsm(int offset, int rows,String userId){
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm"
					+ " LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId WHERE sm.sendUser = ? ORDER BY pageNum DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MessageDTO mdto = new MessageDTO();
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setContent(rs.getString("content"));
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
	
	public List<MessageDTO> listsm(int offset, int rows,String condition, String keyword,String userId){
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm"
					+ " LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >=1 AND sm.sendUser = ?";
			} else if (condition.equals("sendTime")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql +=" WHERE TO_CHAR(sendTime, 'YYYYMMDD') = ? AND sm.sendUser = ?";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >= 1 AND sm.sendUser = ?";
			}
			sql += " ORDER BY pageNum DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY AND sm.sendUser = ?";
			
			pstmt=conn.prepareStatement(sql);
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setString(3, userId);
				pstmt.setInt(4, offset);
				pstmt.setInt(5, rows);
			}else {
				pstmt.setString(1, keyword);
				pstmt.setString(2, userId);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			}
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MessageDTO mdto = new MessageDTO();
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setContent(rs.getString("content"));
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
	public MessageDTO preReadSM(int num, String condition, String keyword,String userId) {
		MessageDTO mdto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm");
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ");
			if(condition.equals("all")) {
				if(keyword.equals(""))
					sb.append(" WHERE sendUser = ?");
				else
                sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) AND sendUser = ?");
            } else if(condition.equals("sendTime")) {
            	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                sb.append(" WHERE (TO_CHAR(sendTime, 'YYYYMMDD') = ?) AND sendUser = ? ");
            } else {
                sb.append(" WHERE ( INSTR("+condition+", ?) > 0) AND sendUser = ?");
            }
            sb.append(" AND (pageNum > ? ) ORDER BY pageNum ASC FETCH  FIRST  1  ROWS  ONLY ");
            
            pstmt= conn.prepareStatement(sb.toString());
            if(condition.equals("all")) {
            	if(keyword.equals("")) {
            		pstmt.setString(1, userId);
               		pstmt.setInt(2, num);
            	}
            	else {
            		pstmt.setString(1, keyword);
            		pstmt.setString(2, keyword);
            		pstmt.setString(3, userId);
            		pstmt.setInt(4, num);
            	}
            } else {
                pstmt.setString(1, keyword);
                pstmt.setString(2, userId);
               	pstmt.setInt(3, num);
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
	public MessageDTO nextReadSM(int num, String condition, String keyword,String userId) {
		MessageDTO mdto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT sm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM sendMessage sm");
			sb.append(" LEFT OUTER JOIN member1 m1 ON sm.sendUser = m1.userId ");
			if(condition.equals("all")) {
				if(keyword.equals(""))
					sb.append(" WHERE sendUser = ?");
				else
                sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) AND sendUser = ?");
            } else if(condition.equals("sendTime")) {
            	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                sb.append(" WHERE (TO_CHAR(sendTime, 'YYYYMMDD') = ?) AND sendUser = ?");
            } else {
                sb.append(" WHERE ( INSTR("+condition+", ?) > 0) AND sendUser = ? ");
            }
            sb.append(" AND (pageNum < ? ) ORDER BY pageNum DESC FETCH  FIRST  1  ROWS  ONLY ");
            
            pstmt= conn.prepareStatement(sb.toString());
            if(condition.equals("all")) {
            	if(keyword.equals("")) {
            		pstmt.setString(1, userId);
                   	pstmt.setInt(2, num);
            	}
            	else {
            		pstmt.setString(1, keyword);
                	pstmt.setString(2, keyword);
                	pstmt.setString(3, userId);
               		pstmt.setInt(4, num);
            	}
            }  else {
                pstmt.setString(1, keyword);
                pstmt.setString(2, userId);
               	pstmt.setInt(3, num);
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
	public int deletesendMessage(String userId,int pageNum) throws SQLException{
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql="DELETE FROM sendMessage WHERE sendUser = ? AND pageNum= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, pageNum);
			
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
	public List<MessageDTO> listrm(int offset, int rows,String userId){
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT rm.receiveUser, sendUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM reciveMessage rm"
					+ " LEFT OUTER JOIN member1 m1 ON rm.receiveUser = m1.userId WHERE rm.receiveUser = ? ORDER BY pageNum DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MessageDTO mdto = new MessageDTO();
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setContent(rs.getString("content"));
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
	
	public List<MessageDTO> listrm(int offset, int rows,String condition, String keyword, String userId){
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT rm.receiveUser, sendUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum FROM reciveMessage rm"
					+ " LEFT OUTER JOIN member1 m1 ON rm.receiveUser = m1.userId ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >=1 AND rm.receivUser = ?";
			} else if (condition.equals("sendTime")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql +=" WHERE TO_CHAR(sendTime, 'YYYYMMDD') = ? AND rm.receivUser = ? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >= 1 AND rm.receivUser = ?";
			}
			sql += " ORDER BY pageNum DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt=conn.prepareStatement(sql);
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setString(3, userId);
				pstmt.setInt(4, offset);
				pstmt.setInt(5, rows);
			}else {
				pstmt.setString(1, keyword);
				pstmt.setString(2, userId);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			}
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MessageDTO mdto = new MessageDTO();
				mdto.setPageNum(rs.getInt("pageNum"));
				mdto.setSendUser(rs.getString("sendUser"));
				mdto.setReceiveUser(rs.getString("receiveUser"));
				mdto.setSubject(rs.getString("subject"));
				mdto.setSendTime(rs.getString("sendTime"));
				mdto.setContent(rs.getString("content"));
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
	public int updateMessage(String userId,int num) throws SQLException{
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE sendMessage SET messageType = ? WHERE receiveUser = ? AND pageNum = ?-1";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, "2");
			pstmt.setString(2, userId);
			pstmt.setInt(3, num);
			
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			sql="UPDATE reciveMessage SET messageType = ? WHERE receiveUser = ? AND pageNum = ?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, "2");
			pstmt.setString(2, userId);
			pstmt.setInt(3, num);
			
			result +=pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
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
	public int deletereceiveMessage(String userId,int pageNum) throws SQLException{
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql="DELETE FROM reciveMessage WHERE sendUser = ? AND pageNum= ? +1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, pageNum);
			
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
	public MessageDTO readsendUser(String userId,int num) throws SQLException {
		MessageDTO mdto =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT rm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum, nickName FROM reciveMessage rm" );
			sb.append(" LEFT OUTER JOIN member1 m1 ON rm.sendUser = m1.userId WHERE receiveUser = ? AND pageNum = ?+1 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, num);
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
	public int dataCount_rm(String userId) {
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(userId.equals("admin1")||userId.equals("admin")||userId.equals("admin2")||userId.equals("admin3")||userId.equals("admin4")||userId.equals("admin5")||userId.equals("admin6")) {
				sql="SELECT COUNT(*) FROM reciveMessage";
				pstmt = conn.prepareStatement(sql);
				
				rs=pstmt.executeQuery();
				if(rs.next()) {
					result=rs.getInt(1);
				}	
			} else {
				sql="SELECT COUNT(*) FROM reciveMessage WHERE receiveUser = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				rs=pstmt.executeQuery();
				if(rs.next()) {
					result=rs.getInt(1);
				}
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
	public int dataCount_rm(String userId, String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql=" SELECT COUNT(*) FROM reciveMessage rm JOIN member1 m ON rm.receiveUser = m.userId";
			
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 AND receiveUser = ?";
			} else if(condition.equals("sendTime")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql+=" WHERE TO_CHAR(sendTime, 'YYYYMMDD') = ? AND receiveUser = ?";
			} else {
				sql +=" WHERE INSTR(" + condition + ", ?) >= 1 AND receiveUser = ?";
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
	public MessageDTO readMember_rm(int num) throws SQLException {
		MessageDTO mdto =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT rm.sendUser, receiveUser, subject, content, TO_CHAR(sendTime, 'YYYY-MM-DD') sendTime, messageType, pageNum, nickName FROM reciveMessage rm" );
			sb.append(" LEFT OUTER JOIN member1 m1 ON rm.sendUser = m1.userId WHERE pageNum = ? ");
			
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
}
