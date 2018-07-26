package com.example.weipeixian.syydbg.model;

public class ChatListData {

	String receiveContent;
	String chatTime;
	String sendContent;
	/**
	 * 1 发送； 2接收
	 */
	int type;
	/**
	 * 1 发送； 2接收
	 */
	public int getType() {
		return type;
	}
	/**
	 * 1 发送； 2接收
	 */
	public void setType(int type) {
		this.type = type;
	}



	public String getReceiveContent() {
		return receiveContent;
	}

	public void setReceiveContent(String receiveContent) {
		this.receiveContent = receiveContent;
	}

	public String getChatTime() {
		return chatTime;
	}

	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

}
