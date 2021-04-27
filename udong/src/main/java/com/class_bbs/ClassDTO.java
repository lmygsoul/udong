package com.class_bbs;

public class ClassDTO {
	int listNum, boardNum;
	String userId;
	String nickName;
	String subject;
	String content;
	int curClass;
	int maxClass;
	String created;
	
	
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCurClass() {
		return curClass;
	}
	public void setCurClass(int curClass) {
		this.curClass = curClass;
	}
	public int getMaxClass() {
		return maxClass;
	}
	public void setMaxClass(int maxClass) {
		this.maxClass = maxClass;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	
	
}
