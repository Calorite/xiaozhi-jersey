package com.yidi.entity;

import java.util.Set;

public class DialogueRecord {
	private Set<Integer> lastparameters;
	private int questionid;
	private String receivedmsg;
	private int special;
	public DialogueRecord(int questionid,String receivedmsg,Set<Integer> lastparameters,int special) {
		this.lastparameters=lastparameters;
		this.questionid=questionid;
		this.receivedmsg=receivedmsg;
		this.special=special;
	}
	/**
	 * @return the lastparameters
	 */
	public Set<Integer> getLastparameters() {
		return lastparameters;
	}
	/**
	 * @param lastparameters the lastparameters to set
	 */
	public void setLastparameters(Set<Integer> lastparameters) {
		this.lastparameters = lastparameters;
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
	 * @return the receivedmsg
	 */
	public String getReceivedmsg() {
		return receivedmsg;
	}
	/**
	 * @param receivedmsg the receivedmsg to set
	 */
	public void setReceivedmsg(String receivedmsg) {
		this.receivedmsg = receivedmsg;
	}
	/**
	 * @return the special
	 */
	public int getSpecial() {
		return special;
	}
	/**
	 * @param special the special to set
	 */
	public void setSpecial(int special) {
		this.special = special;
	}


}
