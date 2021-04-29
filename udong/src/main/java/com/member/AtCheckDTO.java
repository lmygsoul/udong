package com.member;

public class AtCheckDTO {
	private int num,listNum;
	private String userId;
	private String content;
	private String created;
	private String userName;
	private int checkType;
	
	
	
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	
	
}
