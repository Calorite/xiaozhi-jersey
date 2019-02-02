package com.yidi.entity;


public class TulingPerception {
	TulingText inputText;
	TulingSelfInfo selfInfo;
	public TulingPerception (TulingText text,TulingSelfInfo selfInfo){
		this.selfInfo=selfInfo;
		this.inputText=text;
	}
}
