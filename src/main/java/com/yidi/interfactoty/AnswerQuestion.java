package com.yidi.interfactoty;

import java.util.Map;
import java.util.Set;

import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.ReturnInfo;

public interface AnswerQuestion {
	ReturnInfo answerNormalQuestion(ReturnInfo lastinfo,AboutQuestionDAO questiondao,ConvertAdapter converter,Map<Integer,Parameter> allparameters,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,ProcessFactory process,AboutSolutionDAO solutiondao,AboutParametersDAO parameterdao,String sender);
	public ReturnInfo NegativeAnswer(ReturnInfo lastinfo, Map<Integer,Parameter> allparameters,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,AboutQuestionDAO questiondao, ConvertAdapter converter,
			ProcessFactory process, AboutSolutionDAO solutiondao, AboutParametersDAO parameterdao,String username);
}
