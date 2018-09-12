package com.yidi.entity;

import java.util.Date;
import java.util.Set;

public class ReturnInfo {
	private String id;
	private String username;
	private int status;//1:solution
	private String info;
	private Date datetime;
	private String recieved;
	private String parameter;
	private Set<Integer> uncheckparameter;
	private int special;
	public ReturnInfo(String id,int status,String info) {
		this.id=id;
		this.status=status;
		this.info=info;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getRecieved() {
		return recieved;
	}

	public void setRecieved(String recieved) {
		this.recieved = recieved;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the uncheckparameter
	 */
	public Set<Integer> getUncheckparameter() {
		return uncheckparameter;
	}

	/**
	 * @param uncheckparameter the uncheckparameter to set
	 */
	public void setUncheckparameter(Set<Integer> uncheckparameter) {
		this.uncheckparameter = uncheckparameter;
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
