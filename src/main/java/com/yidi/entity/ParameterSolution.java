package com.yidi.entity;

import java.util.List;

public class ParameterSolution {
	private List<PSranklist> parameterset;
	private int solution;
	private int solutionrank;
	private String ageperiod;
	private String sex;
	public ParameterSolution(List<PSranklist> set1,int solution,int solutionrank){
		this.parameterset=set1;
		this.solution=solution;
		this.solutionrank=solutionrank;
	}
	public List<PSranklist> getParameterset() {
		return parameterset;
	}
	public void setParameterset(List<PSranklist> parameterset) {
		this.parameterset = parameterset;
	}
	public int getSolution() {
		return solution;
	}
	public void setSolution(int solution) {
		this.solution = solution;
	}
	public int getSolutionrank() {
		return solutionrank;
	}
	public void setSolutionrank(int solutionrank) {
		this.solutionrank = solutionrank;
	}
	public String getAgeperiod() {
		return ageperiod;
	}
	public void setAgeperiod(String ageperiod) {
		this.ageperiod = ageperiod;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
