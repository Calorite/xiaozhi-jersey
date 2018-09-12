package com.yidi.application;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import com.yidi.service.MainService;

public class Application {
	private static String sender;
	private String tousr;
	private static String text;

	public static void main(String[] args) throws UnsupportedEncodingException {
		try {
			String sender=new String(args[0].getBytes(),"UTF-8");
			String touser=new String(args[1].getBytes(),"UTF-8");
			String text=new String(args[2].getBytes(),"UTF-8");
			MainService main=new MainService(sender, touser,text);
			System.out.println(main.getReply());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("false");
		}
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the tousr
	 */
	public String getTousr() {
		return tousr;
	}
	/**
	 * @param tousr the tousr to set
	 */
	public void setTousr(String tousr) {
		this.tousr = tousr;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
