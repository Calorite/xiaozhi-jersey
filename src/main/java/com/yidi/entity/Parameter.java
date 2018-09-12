package com.yidi.entity;

public class Parameter {
	private int parameterid;
	private int questionid;
	private String parameter;
	private String word;
	private String targetparameitem;
	private String upperquestion;
	private int rank;
	/**
	 * @return the word
	 */
	public Parameter(int parameterid,int questionid,String parameter,int rank,String upper) {
		this.parameterid=parameterid;
		this.questionid=questionid;
		this.parameter=parameter;
		this.rank=rank;
		this.upperquestion=upper;
	}

	public String getWord() {
		return word;
	}
	/**
	 * @return the targetparameitem
	 */
	public String getTargetparameitem() {
		return targetparameitem;
	}
	/**
	 * @param targetparameitem the targetparameitem to set
	 */
	public void setTargetparameitem(String targetparameitem) {
		this.targetparameitem = targetparameitem;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	/**
	 * @return the parameterid
	 */
	public int getParameterid() {
		return parameterid;
	}
	/**
	 * @param parameterid the parameterid to set
	 */
	public void setParameterid(int parameterid) {
		this.parameterid = parameterid;
	}
	/**
	 * @return the questionid
	 */
	public int getQuestionid() {
		return questionid;
	}
	/**
	 * @param questionid the questionid to set
	 */
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the upperquestion
	 */
	public String getUpperquestion() {
		return upperquestion;
	}

	/**
	 * @param upperquestion the upperquestion to set
	 */
	public void setUpperquestion(String upperquestion) {
		this.upperquestion = upperquestion;
	}

}
