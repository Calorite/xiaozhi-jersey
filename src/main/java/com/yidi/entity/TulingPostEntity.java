package com.yidi.entity;

public class TulingPostEntity {
	int reqType;
	TulingPerception perception;
	TulingUser userInfo;
	public TulingPostEntity(String text){
		TulingText input=new TulingText(text);
		TulingSelfInfo selfInfo=new TulingSelfInfo(new TulingLocation());
		this.perception=new TulingPerception(input, selfInfo);
		this.userInfo=new TulingUser();
	}
}
