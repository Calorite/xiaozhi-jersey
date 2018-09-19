package com.yidi.service;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Map.Entry;

import com.yidi.entity.MaxUpperQuestion;
import com.yidi.entity.PSranklist;
import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.PetInfo;
import com.yidi.entity.ReturnInfo;
import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.interfactoty.AboutSolutionDAO;
import com.yidi.interfactoty.AnswerQuestion;
import com.yidi.interfactoty.ConvertAdapter;
import com.yidi.interfactoty.ProcessFactory;
import com.yidi.interfactoty.SpecialprocessFactory;
import com.yidi.interfactoty.TextInfoBytypeFactory;

public class MainService implements TextInfoBytypeFactory {
	private String senderid;
	private String tousr;
	private String text;
	private String reply;
	private ProcessFactory process;
	private AboutParametersDAO parametersdao;
	private AboutSolutionDAO solutiondao;
	private AboutQuestionDAO questiondao;
	private SpecialprocessFactory specialcess;
	private ConvertAdapter converter;
	private AnswerQuestion answer;
	private Map<Integer,Parameter> allparamenter;
	private List<PetInfo> userpets;
	private Map<Set<Integer>, ParameterSolution> parameter_solutionlist;
	Map<Integer, Parameter> initalparameters=new HashMap<Integer, Parameter>();
	Set<Integer> uncheckparamerters=new HashSet<Integer>();

	public MainService(String senderid,String tousr,String text) throws SQLException {
		DefaultServiceFactory factory=new DefaultServiceFactory();
		this.process=factory.getprocessService();
		this.parametersdao=factory.getparameterDao(factory.getDBhelper());
		this.solutiondao=factory.getsolutionDao(factory.getDBhelper());
		this.questiondao=factory.getquestionDao(factory.getDBhelper());
		this.specialcess=factory.getspecialprocess();
		this.converter=factory.getconverter();
		this.answer=factory.getanswer();
		this.senderid=senderid;
		this.tousr=tousr;
		this.text=text;
		this.userpets=specialcess.getuserpetType(senderid);
		this.allparamenter=parametersdao.getparams();
		this.parameter_solutionlist=solutiondao.getsolutionlist();
		initalparameters=process.getInitialParameters(allparamenter, text,parametersdao);

		//查询历史纪录   宠物类型之类的处理
		List<ReturnInfo> lastRecord=process.returnpassedrecord(1, senderid,factory.getDBhelper());
		factory.getDBhelper().closeAll();
		if(userpets.size()>0) {//有登录宠物信息
			if(userpets.size()==1) {//只有一只宠物
				if(text.contains("猫")||userpets.get(0).getType()==1) {

				}else {//狗
					if (lastRecord.isEmpty()||lastRecord==null) {//新对话...
						if(initalparameters.size()==0){//没有参数
							//API
						}else {
							ReturnInfo answeredinfo=newconversation(text);
							this.reply=answeredinfo.getInfo();
							process.insertReturnInfo(answeredinfo);
						}
					}else if (lastRecord.get(0).getStatus()==0) {//话题中...
						if(lastRecord.get(0).getSpecial()==1) {//回答的是姓名
							//specialcess.insertpetName(text, senderid);
							ReturnInfo answeredinfo=answerName(lastRecord.get(0));
							process.insertReturnInfo(answeredinfo);
						}else {//正常回答
							ReturnInfo answeredinfo=answerTypesQuestion(lastRecord);
							answeredinfo.setRecieved(text);
							this.reply=answeredinfo.getInfo();
							process.insertReturnInfo(answeredinfo);
						}
					}else{//新话题...
						if(initalparameters.size()==0){//没有参数
							//API
						}else {
							ReturnInfo answeredinfo=newconversation(text);
							this.reply=answeredinfo.getInfo();
							process.insertReturnInfo(answeredinfo);
						}
					}
				}
			}else {//多只宠物

			}
		}else {//请告诉我您家宠物的信息
			ReturnInfo repeatreturn=newconversation(text);
			repeatreturn.setInfo("请告诉我您家宠物的昵称");
			repeatreturn.setSpecial(1);
			if(process.insertReturnInfo(repeatreturn)){
				this.reply=repeatreturn.getInfo();
			}
		}
	}

	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}

	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}



	/**
	 * @return the senderid
	 */
	public String getSenderid() {
		return senderid;
	}

	/**
	 * @param senderid the senderid to set
	 */
	public void setSenderid(String senderid) {
		this.senderid = senderid;
	}

	@Override
	public ReturnInfo dogprocess(String recivedtext,Map<Integer, Parameter> thisinitalparameters) {
		try {
			String targetparamters="";
			String targetparamters2="";
			ReturnInfo infotag=process.getReturnMSG(parameter_solutionlist, thisinitalparameters);
			Set<Parameter> initalparameterset=new HashSet<Parameter>();
			for (int id:thisinitalparameters.keySet()) {
				if(targetparamters.equals("")){
					targetparamters=String.valueOf(id);
				}else {
					targetparamters=targetparamters+","+String.valueOf(id);
				}
				initalparameterset.add(thisinitalparameters.get(id));
			}
			if (infotag!=null) {
				infotag.setParameter(targetparamters);
				return infotag;
			}
			Map<Integer, Parameter> vaildparameters=process.getValidparameters(parameter_solutionlist, initalparameterset);
			for (int id:vaildparameters.keySet()) {
				if (targetparamters2.equals("")) {
					targetparamters2=String.valueOf(id);
				}else {
					targetparamters2=targetparamters2+","+String.valueOf(id);
				}
			}
			ReturnInfo infotag2=process.getReturnMSG(parameter_solutionlist, vaildparameters);
			infotag2.setParameter(targetparamters2);
			return infotag2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ReturnInfo catprocess(String recivedtext,Map<Integer, Parameter> thisinitalparameters) {
		// TODO Auto-generated method stub
		return null;
	}


	public ReturnInfo answerUpperquestion(ReturnInfo infoinstance,String text,Map<Integer,Parameter> parameterin) {
		Map<Integer,Parameter> preparametersets=new HashMap<Integer, Parameter>();
		String[] preparamStrarry=infoinstance.getParameter().split(",");
		for(String id:preparamStrarry) {
			Integer parameid=Integer.valueOf(id);
			preparametersets.put(parameid, allparamenter.get(parameid));
		}
		for(Integer key:parameterin.keySet()) {
			preparametersets.put(key, parameterin.get(key));
		}
		ReturnInfo infotag=dogprocess(text, preparametersets);
		infotag.setUsername(senderid);
		return infotag;
	}

	public ReturnInfo answerQuestion(ReturnInfo infoinstance,String text,Map<Integer,Parameter> parameterin) {
		Map<Integer,Parameter> preparametersets=new HashMap<Integer, Parameter>();
		String[] preparamStrarry=infoinstance.getParameter().split(",");
		for(String id:preparamStrarry) {
			Integer parameid=Integer.valueOf(id);
			preparametersets.put(parameid, allparamenter.get(parameid));
		}
		return null;
	}




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


	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		Collections.sort(sortedEntries,
				new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e1.getValue().compareTo(e2.getValue());
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

	public MaxUpperQuestion getMaxString(List<String> list1,List<ReturnInfo> inconversation) {
		String regex;
		Pattern p;
		Matcher m;
		String tmp = "";
		String tot_str = list1.toString();
		int max_cnt = 0;
		String max_str = "";
		Set<String> askedupper=new HashSet<String>();
		for(ReturnInfo tag:inconversation) {
			askedupper.add(tag.getId());
		}

		for(String str : list1) {
			if (tmp.equals(str)) continue;
			tmp = str;
			regex = str;
			p = Pattern.compile(regex);
			m = p.matcher(tot_str);
			int cnt = 0;
			while(m.find()) {
				cnt++;
			}
			if (cnt > max_cnt) {
				if(askedupper.contains(str)) {//提问过的一级问不再计算

				}else {
					max_cnt = cnt;
					max_str = str;
				}
			}
		}
		return new MaxUpperQuestion(max_str,max_cnt);
	}

	public ReturnInfo newconversation(String receviedtext) {
		ReturnInfo infotag;
		try {
			infotag = dogprocess(receviedtext,process.getInitialParameters(allparamenter, receviedtext,parametersdao));
			infotag.setRecieved(receviedtext);
			infotag.setUsername(senderid);
			return infotag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public Set<Integer> updateParameteset(ReturnInfo lastrecord,String checkedparameter){
		Set<Integer> unchecked=lastrecord.getUncheckparameter();
		if(checkedparameter.contains(",")) {
			String[] checkedarray=checkedparameter.split(",");
			for(String item:checkedarray) {
				if(unchecked.contains(Integer.valueOf(item))) {
					unchecked.remove(Integer.valueOf(item));
				}
			}
		}else{
			unchecked.remove(Integer.valueOf(checkedparameter));
		}
		return unchecked;
	}


	public ReturnInfo answerTypesQuestion(List<ReturnInfo> lastRecord) throws SQLException{
//		if (lastRecord.get(0).getId().contains("A")) {//回答一级问题
//			Map<Integer, Parameter> targetparameters=process.parameterInupperquestion(lastRecord.get(0).getId());
//			Map<Integer, Parameter> parameterin=process.getInitialParameters(targetparameters, text, parametersdao);
//			if(parameterin.size()>0) {//所回内容包括一级问涵括的三级参数
//				ReturnInfo newinfotag=answerUpperquestion(lastRecord.get(0), text, parameterin);
//				newinfotag.setUncheckparameter(updateParameteset(lastRecord.get(0),newinfotag.getParameter()));
//				return newinfotag;
//			}else{//根据uncheckquestion换问题提问
//				List<String> uncheckupperquestion=new LinkedList<String>();
//				Set<Integer> upcheckparame=lastRecord.get(0).getUncheckparameter();
//				for(Integer id:upcheckparame) {
//					uncheckupperquestion.add(allparamenter.get(id).getUpperquestion());
//				}
//				MaxUpperQuestion maxtimesquestion=getMaxString(uncheckupperquestion,process.inconversationrecord(senderid));
//				ReturnInfo infotag=lastRecord.get(0);
//				if(maxtimesquestion.getCount()>1) {//还有一级问
//					String id=maxtimesquestion.getQuestionid();
//					infotag.setId(id);
//					infotag.setInfo(questiondao.getUpperquestionbyid(id));
//				}else {//没有一级问了
//					Map<Integer, Parameter> recoredparameter=new HashMap<>();
//					Set<Integer> set1=converter.String2intSet(lastRecord.get(0).getParameter());
//					for(Integer i:set1) {
//						recoredparameter.put(i, allparamenter.get(i));
//					}
//					infotag=getParameterQuestion(parameter_solutionlist, recoredparameter);
//					infotag.setUncheckparameter(lastRecord.get(0).getUncheckparameter());
//
//
//				}
//				return infotag;
//			}
		//}else {//回答普通三级问（全局参数）
			ReturnInfo newinfotag=null;
			Map<Integer, Parameter> targetparameters1=questiondao.gettargetparamete(lastRecord.get(0).getId());
			Map<Integer, Parameter> parameterin1=process.getInitialParameters(targetparameters1, text, parametersdao);
			if(parameterin1.isEmpty()) {//回答的内容没有包含三级参数（全局参数）
				Map<Integer, Parameter> targetparameters=parametersdao.targetparametersbyquestion(lastRecord.get(0).getId());
				Map<Integer, Parameter> parameterin=process.getInitialParameters(targetparameters, text, parametersdao);
				if(parameterin.isEmpty()) {//回答的内容不是问题内参数，positive和negative判断
					BaiduInstance aicheck=new BaiduInstance();
					if(aicheck.sentimentClassify(text).equals("positive")) {//肯定
						newinfotag=answer.answerNormalQuestion(lastRecord.get(0), questiondao, converter, allparamenter, parameter_solutionlist);
					}else {//否定

					}
				}else {
					newinfotag=answerUpperquestion(lastRecord.get(0), text, parameterin);
					newinfotag.setUncheckparameter(updateParameteset(lastRecord.get(0),newinfotag.getParameter()));
				}

			}else {
				newinfotag=answerUpperquestion(lastRecord.get(0), text, parameterin1);
				newinfotag.setUncheckparameter(updateParameteset(lastRecord.get(0),newinfotag.getParameter()));
			}
			return newinfotag;
		//}
	}

	public ReturnInfo answerName(ReturnInfo lastRecord) throws SQLException {
		return newconversation(lastRecord.getRecieved());
	}

	public ReturnInfo getParameterQuestion(Map<Set<Integer>, ParameterSolution> parameter_solutionlist,Map<Integer, Parameter> parameters){
		Set<Integer> parameteridset=new HashSet<>();
		for (Integer integer : parameters.keySet()) {
			parameteridset.add(integer);
		}
		Map<Set<Integer>, Integer> parametersolutionnewlist=new HashMap<Set<Integer>, Integer>();
		for(Set<Integer> key: parameter_solutionlist.keySet()){
			ParameterSolution thisPS=parameter_solutionlist.get(key);
			if(key.containsAll(parameteridset)){
				parametersolutionnewlist.put(key, thisPS.getSolutionrank());
			}
		}
		if(parametersolutionnewlist.size()==1) {//目标parametersolution只有一个了return一个solution
			Entry<Set<Integer>, Integer> entry = parametersolutionnewlist.entrySet().iterator().next();
			ParameterSolution firstPS=parameter_solutionlist.get(entry.getKey());
			return new ReturnInfo(String.valueOf(firstPS.getSolution()), 1, solutiondao.getSolutinStr(String.valueOf(firstPS.getSolution())));
		}
		parametersolutionnewlist=sortByValueDesc(parametersolutionnewlist);
		Entry<Set<Integer>, Integer> entry = parametersolutionnewlist.entrySet().iterator().next();
		ParameterSolution firstPS=parameter_solutionlist.get(entry.getKey());
		parametersolutionnewlist.remove(entry.getKey());
		List<PSranklist> nowpsranklist=sortByrank(firstPS.getParameterset());
		for(PSranklist thisp:nowpsranklist){
			if(parameteridset.contains(thisp.getId())){

			}else {//没有提问的参数
				int questionid=process.getquestionidbyparameterid(thisp.getId());
				String question=process.getquestionbyid(String.valueOf(questionid));
				ReturnInfo infotag=new ReturnInfo(String.valueOf(questionid), 0, question);
				return infotag;
			}
		}
		return null;
	}

}
