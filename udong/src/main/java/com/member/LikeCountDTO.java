package com.member;

public class LikeCountDTO {
	private String receiveUser;
	private String giveUser;
	private String likeCount;
	
	public String getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getGiveUser() {
		return giveUser;
	}
	public void setGiveUser(String giveUser) {
		this.giveUser = giveUser;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	
}
