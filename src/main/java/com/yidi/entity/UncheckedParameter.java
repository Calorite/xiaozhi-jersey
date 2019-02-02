package com.yidi.entity;

public class UncheckedParameter {
	Parameter parameter;
	int count;
	public UncheckedParameter(Parameter para,int sum){
		this.parameter=para;
		this.count=sum;
	}
	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
