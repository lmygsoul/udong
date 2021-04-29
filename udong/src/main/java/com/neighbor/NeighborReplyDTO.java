package com.neighbor;

public class neighborReplyDTO {
	private int num;
	private int articlenum;
	private String userId;
	private String userName;
	private String content;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public int getArticlenum() {
		return articlenum;
	}
	public void setArticlenum(int articlenum) {
		this.articlenum = articlenum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
