package com.yidi.application;

import java.sql.SQLException;

import com.yidi.service.MainService;

public class Application {
	public static void main(String[] args) throws SQLException {
		//String sender=new String(args[0].getBytes(),"UTF-8");
		//String touser=new String(args[1].getBytes(),"UTF-8");
		//String text=new String(args[2].getBytes(),"UTF-8");
		MainService main=new MainService("test", "test","狗狗吞咽困难");
		System.out.println(main.getReply());



	}
}