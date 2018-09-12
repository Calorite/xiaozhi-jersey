package com.yidi.entity;

public class parameInQuestion {
	private String id;
	private String parameterinquestion;
	private String parameter;
	public parameInQuestion(String id,String parameterinquestion,String parameter){
		this.id=String.valueOf(id);
		this.parameterinquestion=parameterinquestion;
		this.parameter=parameter;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParameterinquestion() {
		return parameterinquestion;
	}
	public void setParameterinquestion(String parameterinquestion) {
		this.parameterinquestion = parameterinquestion;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
}
