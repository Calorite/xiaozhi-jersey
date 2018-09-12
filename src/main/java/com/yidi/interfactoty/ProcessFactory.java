package com.yidi.interfactoty;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yidi.DaoImpl.DBService;
import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.ReturnInfo;

public interface ProcessFactory {
	String api(String text);
	Set<Integer> getSecondquestion(String id,String recevedmsg,AboutParametersDAO parametersdao) throws SQLException;
	Set<Integer> getparametesbyUpperquestion(String id,String recevedmsg,AboutParametersDAO parametersdao) throws SQLException;
	List<ReturnInfo> returnpassedrecord(int rows,String usrname,DBService helper);
	int returnstatus(String senderid); //0还在话题中    1话题结束
	String returnsolution(Set<Integer> set);
	ReturnInfo getReturn(Map<Set<Integer>, Integer> parameter_solutionlist,Set<Parameter> initalparameters);//0还在话题中的对话
	Map<Integer, Parameter> getValidparameters(Map<Set<Integer>, ParameterSolution> parameterlist,
			Set<Parameter> initalset);
	int getquestionidbyparameterid(int i);
	String getquestionbyid(String id);
	Map<Integer, Parameter> getInitialParameters(Map<Integer, Parameter> allparamenter, String text,
			AboutParametersDAO parametersdao) throws SQLException;
	Map<Integer, Parameter> parameterInupperquestion(String id);
	boolean insertReturnInfo(ReturnInfo infoinstance);
	List<ReturnInfo> inconversationrecord(String usrname);
}
