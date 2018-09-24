package com.yidi.interfactoty;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yidi.entity.Parameter;


public interface AboutParametersDAO {
	Map<Integer, Parameter> getparams() throws SQLException;

	boolean checkandpara(String para, String text);

	String checkParameterLine(String parameline, String text);

	Map<Integer, Parameter> getparams(String id1) throws SQLException;

	Map<Integer, Parameter> getparams2(String id2) throws SQLException;

	Map<Integer, Parameter> targetparametersbyquestion(String questionid);
	
	boolean checkRemindParameter(String parameters);
	
	Set<Integer> getParametersByquestionid(String questionid);
	
	void updateStatus(String username);
	
	Set<Integer> updateUncheckParameter(Set<Integer> set1,String usrname);
}
