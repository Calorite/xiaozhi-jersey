package com.yidi.entity;

public class WechatPostEntity {
	String touser;
	String msgtype;
	String text;
	public WechatPostEntity(String username,String msg,String conetent){
		this.touser=username;
		this.msgtype=msg;
		this.text=conetent;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
