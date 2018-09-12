package com.yidi.entity;

public class MaxUpperQuestion {
	String questionid;
	int count;
	public MaxUpperQuestion(String id,int count){
		this.questionid=id;
		this.count=count;
	}
	/**
	 * @return the questionid
	 */
	public String getQuestionid() {
		return questionid;
	}
	/**
	 * @param questionid the questionid to set
	 */
	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
