package com.yidi.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.yidi.entity.PSranklist;
import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.ReturnInfo;
import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.interfactoty.AboutSolutionDAO;
import com.yidi.interfactoty.AnswerQuestion;
import com.yidi.interfactoty.ConvertAdapter;
import com.yidi.interfactoty.ProcessFactory;
import com.yidi.service.DefaultServiceFactory;

public class AnswerQuestionImpl implements AnswerQuestion {

	@Override
	public ReturnInfo answerNormalQuestion(ReturnInfo lastinfo,AboutQuestionDAO questiondao,ConvertAdapter converter,Map<Integer,Parameter> allparameters,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,ProcessFactory process,AboutSolutionDAO solutiondao,AboutParametersDAO parameterdao) {
		Map<Integer,Parameter> relateParametemap=questiondao.gettargetparamete(lastinfo.getId());
		String targetparamters2="";
		if(relateParametemap.size()==1) {
			String preparamter=lastinfo.getParameter();
			Entry<Integer, Parameter> entry = relateParametemap.entrySet().iterator().next();
			String newparamter=preparamter+","+String.valueOf(entry.getKey());
			Map<Integer,Parameter> initalparameters=converter.Set2map(converter.String2intSet(newparamter), allparameters);
			ReturnInfo newinfotag=process.getReturnMSG(parameter_solutionlist, initalparameters, allparameters, process,solutiondao, parameterdao);
			if (newinfotag!=null) {
				newinfotag.setParameter(newparamter);
				newinfotag.setUncheckparameter(process.updatebyChecked(lastinfo.getUncheckparameter(), newparamter));
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
			ReturnInfo infotag2=process.getReturnMSG(parameter_solutionlist, vaildparameters, allparameters, process, solutiondao, parameterdao);
			infotag2.setParameter(targetparamters2);
			infotag2.setUncheckparameter(process.updatebyChecked(lastinfo.getUncheckparameter(), targetparamters2));
			return infotag2;
		}//else{}  问题内参数有1个以上
		return null;
	}

	@Override
	public ReturnInfo NegativeAnswer(ReturnInfo lastinfo, Map<Integer,Parameter> allparamenter,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,AboutQuestionDAO questiondao, ConvertAdapter converter,
			ProcessFactory process, AboutSolutionDAO solutiondao, AboutParametersDAO parameterdao) {
		Set<Integer> parameteridset= converter.String2intSet(lastinfo.getParameter());
		String newgetedparameter="";
		Set<Integer> upcheckparameterid=new HashSet<Integer>();
		Map<Set<Integer>, Integer> parametersolutionnewlist=new HashMap<Set<Integer>, Integer>();
		for(Set<Integer> key: parameter_solutionlist.keySet()){
			ParameterSolution thisPS=parameter_solutionlist.get(key);
			if(key.containsAll(parameteridset)){
				parametersolutionnewlist.put(key, thisPS.getSolutionrank());
			}
		}
		parametersolutionnewlist=sortByValueDesc(parametersolutionnewlist);
		Entry<Set<Integer>, Integer> entry = parametersolutionnewlist.entrySet().iterator().next();
		ParameterSolution firstPS=parameter_solutionlist.get(entry.getKey());
		parametersolutionnewlist.remove(entry.getKey());
		List<PSranklist> nowpsranklist=sortByrank(firstPS.getParameterset());
		int index=0;
		int questionid=0;
		String question="";
		for(PSranklist thisp:nowpsranklist){
			if(parameteridset.contains(thisp.getId())){

			}else {
				index++;
				if(index==2) {
					questionid=process.getquestionidbyparameterid(thisp.getId());
					question=process.getquestionbyid(String.valueOf(questionid));
				}else {
					//uncheckupperquestion.add(allparamenter.get(thisp.getId()).getUpperquestion());
					Parameter param=allparamenter.get(thisp.getId());
					if(param!=null){
						upcheckparameterid.add(param.getParameterid());
					}
					
				}
			}
		}
		for(Set<Integer> key:parametersolutionnewlist.keySet()) {
			for (Integer id:key) {
				if (parameteridset.contains(id)) {

				}else {
					Parameter parame=allparamenter.get(id);
					if (parame!=null) {
						//uncheckupperquestion.add(parame.getUpperquestion());
						upcheckparameterid.add(parame.getParameterid());
					}
				}
			}
		}
		ReturnInfo infotag=null;
		infotag=new ReturnInfo(String.valueOf(questionid), 0, question);
		infotag.setUncheckparameter(upcheckparameterid);
		if (upcheckparameterid.isEmpty()) {//问完了
			if (infotag.getStatus()==0) {//没有出solution
				if (parameterdao.checkRemindParameter(infotag.getParameter())) {
					infotag.setId("remind");
					infotag.setInfo("推荐就医");
				}else {
					infotag.setId("remind");
					infotag.setInfo("宠友小智！您身边的养宠专家！");
				}
			}
			
		}
		infotag.setParameter(newgetedparameter);
		return infotag;
	}
	
	//降序
		public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {

			List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
			Collections.sort(sortedEntries,
					new Comparator<Entry<K,V>>() {
				@Override
				public int compare(Entry<K,V> e1, Entry<K,V> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
					);
			Map<K, V> result = new LinkedHashMap<K, V>();
			for (Entry<K, V> entry : sortedEntries) {
				result.put(entry.getKey(), entry.getValue());
			}
			return result;
		}

		public static List<PSranklist> sortByrank(List<PSranklist> list1) {
			List<PSranklist> newlist=new LinkedList<PSranklist>();
			newlist=list1.stream().sorted((u1, u2) -> u2.getRank()-(u1.getRank())).collect(Collectors.toList());
			return newlist;
		}
}
