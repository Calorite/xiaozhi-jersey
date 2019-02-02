package com.yidi.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.yidi.DaoImpl.DBService;
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
	public ReturnInfo answerNormalQuestion(ReturnInfo lastinfo,AboutQuestionDAO questiondao,ConvertAdapter converter,Map<Integer,Parameter> allparameters,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,ProcessFactory process,AboutSolutionDAO solutiondao,AboutParametersDAO parameterdao,AnswerQuestion answer,String sender) {
		Map<Integer,Parameter> relateParametemap=questiondao.gettargetparamete(lastinfo.getId());
		String targetparamters2="";
		if(relateParametemap.size()==1) {
			String preparamter=lastinfo.getParameter();
			Entry<Integer, Parameter> entry = relateParametemap.entrySet().iterator().next();
			String newparamter=preparamter+","+String.valueOf(entry.getKey());
			Map<Integer,Parameter> initalparameters=converter.Set2map(converter.String2intSet(newparamter), allparameters);
			ReturnInfo newinfotag=process.getReturnMSG(parameter_solutionlist, initalparameters, allparameters, process,solutiondao, parameterdao, answer, sender);
			if (newinfotag!=null) {
				newinfotag.setParameter(newparamter);
				Set<Integer> testset=parameterdao.updateUncheckParameter(lastinfo.getUncheckparameter(),sender);
				newinfotag.setUncheckparameter(parameterdao.updateUncheckParameter(lastinfo.getUncheckparameter(),sender));
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
			ReturnInfo infotag2=process.getReturnMSG(parameter_solutionlist, vaildparameters, allparameters, process, solutiondao, parameterdao, answer, sender);
			infotag2.setParameter(targetparamters2);
			infotag2.setUncheckparameter(parameterdao.updateUncheckParameter(lastinfo.getUncheckparameter(),sender));
			return infotag2;
		}//else{}  问题内参数有1个以上
		else {
			Set<Integer> oldparameter=converter.String2intSet(lastinfo.getParameter());
			List<Integer> relatedparamete=questiondao.gettargetparametelist(lastinfo.getId());
			oldparameter.add(relatedparamete.get(0));
			Map<Integer, Parameter> newparameter=new HashMap<>();
			for(Integer id:oldparameter){
				newparameter.put(id, allparameters.get(id));
			}
			ReturnInfo infotag=process.getReturnMSG(parameter_solutionlist, newparameter, allparameters, process, solutiondao, parameterdao, answer, sender);
			return infotag;
		}
	}

	@Override
	public ReturnInfo NegativeAnswer(ReturnInfo lastinfo, Map<Integer,Parameter> allparamenter,Map<Set<Integer>, ParameterSolution> parameter_solutionlist,AboutQuestionDAO questiondao, ConvertAdapter converter,
			ProcessFactory process, AboutSolutionDAO solutiondao, AboutParametersDAO parameterdao,AnswerQuestion answer,String username) {
		Set<Integer> parameteridset= converter.String2intSet(lastinfo.getParameter());
		Set<Integer> oldset=new HashSet<Integer>();
		Map<Integer, Parameter> newparameteridmap=new HashMap<Integer, Parameter>();
		List<Integer> list1=questiondao.gettargetparametelist(lastinfo.getId());
		if (list1.size()>1) {
			parameteridset.add(list1.get(1));
		}else {//问题对应的参数只有一个,删除所有跟当前参数有关的unchecked参数		
			try {
				oldset=parameteridset;
				parameteridset.add(list1.get(0));
				Set<Integer> set2=parameterdao.getrelatedparameters(parameter_solutionlist, parameteridset, allparamenter);
				Set<Integer> uncheckparameter=new HashSet<>();
				for(Set<Integer> idset:parameter_solutionlist.keySet()){
					if (idset.containsAll(oldset)) {
						if(idset.containsAll(parameteridset)){}else {
							uncheckparameter.addAll(idset);
						}
					}
				}
				if (!uncheckparameter.isEmpty()) {
					uncheckparameter.removeAll(set2);
					uncheckparameter.remove(list1.get(0));
					uncheckparameter.removeAll(parameteridset);
				}else {
					uncheckparameter=lastinfo.getUncheckparameter();
					uncheckparameter.remove(list1.get(0));
				}				
				int questionid=questiondao.getQuestionid(uncheckparameter, allparamenter);
				String returninfo=questiondao.getQustionStr(String.valueOf(questionid));
				ReturnInfo infotag=new ReturnInfo(String.valueOf(questionid),0,returninfo);
				infotag.setUncheckparameter(uncheckparameter);
				infotag.setParameter(lastinfo.getParameter());
				return infotag;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//ReturnInfo infotag=process.getReturnMSG(parameter_solutionlist, newparameteridmap,allparamenter, process, solutiondao, parameterdao, answer, username);
		return null;
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

		@Override
		public boolean IsAskedQuestion(String replyid,String usrname) {
			String sql="SELECT * from  ai_qanda.user_dialogue_tb where type=0 and username=?;";
			String[] params={usrname};
			DBService helper=new DBService();
			ResultSet rs=helper.executeQueryRS(sql, params);
			try {
				while (rs.next()) {
					String replyedid=rs.getString(5);
					if(replyedid.equals(replyid)){
						return true;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public ReturnInfo answerRelatedQuestion(ReturnInfo lastinfo, Map<Integer, Parameter> allparameters,
				Map<Set<Integer>, ParameterSolution> parameter_solutionlist, Map<Integer, Parameter> newparameters,ProcessFactory process,AboutSolutionDAO solutiondao,AboutParametersDAO parameterdao,AnswerQuestion answer,String sender) {
			if(lastinfo.getParameter().contains(",")) {
				String[] parameterarray=lastinfo.getParameter().split(",");
				for(String keystr:parameterarray) {
					Integer key=Integer.valueOf(keystr);
					newparameters.put(key, allparameters.get(key));
				}
			}else {
				Integer key=Integer.valueOf(lastinfo.getParameter());
				newparameters.put(key, allparameters.get(key));
			}
			ReturnInfo infotag=process.getReturnMSG(parameter_solutionlist, newparameters, allparameters, process, solutiondao, parameterdao, answer, sender);
			return infotag;
		}

		@Override
		public String AnswerByUncheckedparamter(String text, Set<Integer> uncheckedparameter,
				Map<Integer, Parameter> allparameters) {
			// TODO Auto-generated method stub
			return null;
		}
}
