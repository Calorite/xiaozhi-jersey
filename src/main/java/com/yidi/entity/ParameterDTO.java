package com.yidi.entity;

public class ParameterDTO {
	private String id;
	private String rank;
	public ParameterDTO(String id,String rank){
		this.id=id;
		this.rank=rank;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
}
