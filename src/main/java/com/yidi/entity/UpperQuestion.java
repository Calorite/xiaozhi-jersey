package com.yidi.entity;

public class UpperQuestion {
	private String id;
	private String question;
	private String returnparameter;
	public UpperQuestion(String id,String question){
		this.id=id;
		this.question=question;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getReturnparameter() {
		return returnparameter;
	}
	public void setReturnparameter(String returnparameter) {
		this.returnparameter = returnparameter;
	}

}
