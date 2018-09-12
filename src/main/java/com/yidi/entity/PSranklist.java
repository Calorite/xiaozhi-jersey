package com.yidi.entity;

public class PSranklist {
	private int id;
	private int rank;
	public  PSranklist(int i,int rank) {
		this.id=i;
		this.rank=rank;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
}
