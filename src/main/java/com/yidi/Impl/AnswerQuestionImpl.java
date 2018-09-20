package com.yidi.Impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.ReturnInfo;
import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.interfactoty.AboutSolutionDAO;
import com.yidi.interfactoty.AnswerQuestion;
import com.yidi.interfactoty.ConvertAdapter;
import com.yidi.interfactoty.ProcessFactory;
import com.yidi.service.DefaultServiceFactory;

public class AnswerQuestionImpl implements AnswerQuestion {

	DefaultServiceFactory factory=new DefaultServiceFactory();
	ProcessFactory process=factory.getprocessService();

	@Override
	public ReturnInfo answerNormalQuestion(ReturnInfo lastinfo,AboutQuestionDAO questiondao,ConvertAdapter converter,Map<Integer,Parameter> allparameters,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,ProcessFactory process,AboutSolutionDAO solutiondao) {
		Map<Integer,Parameter> relateParametemap=questiondao.gettargetparamete(lastinfo.getId());
		String targetparamters2="";
		if(relateParametemap.size()==1) {
			String preparamter=lastinfo.getParameter();
			Entry<Integer, Parameter> entry = relateParametemap.entrySet().iterator().next();
			String newparamter=preparamter+","+String.valueOf(entry.getKey());
			Map<Integer,Parameter> initalparameters=converter.Set2map(converter.String2intSet(newparamter), allparameters);
			ReturnInfo newinfotag=process.getReturnMSG(parameter_solutionlist, initalparameters, process,solutiondao);
			if (newinfotag!=null) {
				newinfotag.setParameter(newparamter);
				return newinfotag;
			}
			Map<Integer, Parameter> vaildparameters=process.getValidparameters(parameter_solutionlist, converter.Map2paramterSet(initalparameters));
			for (int id:vaildparameters.keySet()) {
				if (targetparamters2.equals("")) {
					targetparamters2=String.valueOf(id);
				}else {
					targetparamters2=targetparamters2+","+String.valueOf(id);
				}
			}
			ReturnInfo infotag2=process.getReturnMSG(parameter_solutionlist, vaildparameters, process, solutiondao);
			infotag2.setParameter(targetparamters2);
			return infotag2;
		}
		return null;
	}

}
