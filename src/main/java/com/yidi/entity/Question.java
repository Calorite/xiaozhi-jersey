package com.yidi.entity;

public class Question {
	private String id;
	private String question;
	private int chioces;
	private String answer;
	private String parameters;
	public Question(String id,String question) {
		this.id=id;
		this.question=question;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the chioces
	 */
	public int getChioces() {
		return chioces;
	}
	/**
	 * @param chioces the chioces to set
	 */
	public void setChioces(int chioces) {
		this.chioces = chioces;
	}
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}
