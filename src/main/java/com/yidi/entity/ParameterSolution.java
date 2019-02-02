package com.yidi.entity;

import java.util.List;
import java.util.Set;

public class ParameterSolution {
	private Set<Integer> parameterset;
	private int solution;
	private int solutionrank;
	private String ageperiod;
	private String sex;
	public ParameterSolution(int solution){
		this.solution=solution;
	}
	public Set<Integer> getParameterset() {
		return parameterset;
	}
	public void setParameterset(Set<Integer> parameterset) {
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
