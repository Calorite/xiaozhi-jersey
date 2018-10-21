package com.yidi.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.yidi.DaoImpl.AboutParameterImpl;
import com.yidi.DaoImpl.DBService;
import com.yidi.entity.PSranklist;
import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.PetInfo;
import com.yidi.entity.ReturnInfo;
import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.interfactoty.AboutSolutionDAO;
import com.yidi.interfactoty.AnswerQuestion;
import com.yidi.interfactoty.ProcessFactory;

public class ProcessFactoryImpl implements ProcessFactory {
	private static Logger logger = Logger.getLogger(AboutParameterImpl.class);
	@Override
	public List<ReturnInfo> returnpassedrecord(int rows,String usrname,DBService helper) {
		List<ReturnInfo> list1=new LinkedList<ReturnInfo>();
		String sql="SELECT * FROM ai_qanda.user_dialogue_tb where username=? order by datetime desc limit "+String.valueOf(rows)+";";
		String[] params={usrname};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				ReturnInfo returninstance=new ReturnInfo(rs.getString(5), rs.getInt(7), rs.getString(3));
				returninstance.setDatetime(rs.getDate(4));
				returninstance.setParameter(rs.getString(6));
				returninstance.setRecieved(rs.getString(2));
				returninstance.setSpecial(rs.getInt(9));
				String uncheck=rs.getString(8);
				Set<Integer> set1=new HashSet<Integer>();
				if(uncheck.equals("null")){
					returninstance.setUncheckparameter(set1);
				}else {
					if (uncheck.contains(",")) {
						String[] uncheckarr=uncheck.split(",");
						for(String thisone:uncheckarr){
							set1.add(Integer.valueOf(thisone));
						}
					}else {
						set1.add(Integer.valueOf(uncheck));
					}
					returninstance.setUncheckparameter(set1);
				}

				list1.add(returninstance);
			}
			return list1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public boolean insertReturnInfo(ReturnInfo infoinstance) {
		DBService helper=new DBService();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime=df.format(new Date());
		Gson gson=new Gson();
		String sql="INSERT INTO ai_qanda.user_dialogue_tb (username,recevied,reply,datetime,replyid,parameters,type,uncheck,specialprocess) values(?,?,?,?,?,?,?,?,?);";
		String upcheck=gson.toJson(infoinstance.getUncheckparameter());
		String uncheck="";
		try {
			uncheck=upcheck.replace("[", "").replace("]", "");
		} catch (Exception e) {
			// TODO: handle exception
		}
		int rows=0;
		if (infoinstance.getUncheckparameter().size()>0) {
			String[] params={infoinstance.getUsername(),infoinstance.getRecieved(),infoinstance.getInfo(),datetime,String.valueOf(infoinstance.getId()),infoinstance.getParameter(),String.valueOf(infoinstance.getStatus()),uncheck,String.valueOf(infoinstance.getSpecial())};
			rows=helper.executeUpdate(sql, params);
		}else {
			String[] params={infoinstance.getUsername(),infoinstance.getRecieved(),infoinstance.getInfo(),datetime,String.valueOf(infoinstance.getId()),infoinstance.getParameter(),String.valueOf(infoinstance.getStatus()),"",String.valueOf(infoinstance.getSpecial())};
			rows=helper.executeUpdate(sql, params);
		}

		if (rows>0) {
			return true;
		}
		return false;
	}


	@Override
	public String api(String text) {
		// TODO Auto-generated method stub
		return "";
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

	//升序
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


	@Override
	public int returnstatus(String senderid) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Set<Integer> getSecondquestion(String id, String recevedmsg, AboutParametersDAO parametersdao)
			throws SQLException {
		Set<Integer> set2=new HashSet<Integer>();
		Map<Integer,Parameter> FirstInparamenter=parametersdao.getparams2(id);
		for(Parameter curtparamente:FirstInparamenter.values()) {
			String targetparame=parametersdao.checkParameterLine(curtparamente.getParameter(),recevedmsg);
			if(targetparame!=null) {
				curtparamente.setTargetparameitem(targetparame);
				set2.add(curtparamente.getParameterid());
			}
		}
		return set2;
	}

	@Override
	public Set<Integer> getparametesbyUpperquestion(String id, String recevedmsg, AboutParametersDAO parametersdao)
			throws SQLException {
		Set<Integer> set1=new HashSet<Integer>();
		Map<Integer,Parameter> FirstInparamenter=parametersdao.getparams(id);
		for(Parameter curtparamente:FirstInparamenter.values()) {
			String targetparame=parametersdao.checkParameterLine(curtparamente.getParameter(),recevedmsg);
			if(targetparame!=null) {
				curtparamente.setTargetparameitem(targetparame);
				set1.add(curtparamente.getParameterid());
			}
		}
		return set1;
	}

	@Override
	public int getquestionidbyparameterid(int id) {
		DBService helper=new DBService();
		String sql="SELECT quesid FROM ai_qanda.parameter_tb where id=?";
		String[] params={String.valueOf(id)};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getquestionbyid(String id) {
		DBService helper=new DBService();
		String sql="SELECT question FROM ai_qanda.paramenterques_tb where id=?;";
		String[] params={id};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			if(rs.next()){
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Map<Integer, Parameter> getInitialParameters(Map<Integer, Parameter> allparamenter, String text,
			AboutParametersDAO parametersdao) throws SQLException {
		Map<Integer,Parameter> targetpara=new HashMap<Integer,Parameter>();
		logger.info("InputedInfo:"+text);
		for(Parameter curtparamente:allparamenter.values()) {
			String targetparame=parametersdao.checkParameterLine(curtparamente.getParameter(),text);
			if(targetparame!=null) {
				logger.info("getedParameter:"+targetparame);
				curtparamente.setTargetparameitem(targetparame);
				targetpara.put(curtparamente.getParameterid(),curtparamente);
			}
		}
		return targetpara;
	}

	@Override
	public Map<Integer, Parameter> parameterInupperquestion(String id) {
		String sql="SELECT * FROM ai_qanda.parameter_tb where first=?;";
		String[] params={id};
		DBService helper=new DBService();
		Map<Integer,Parameter> FirstInparamenter=new HashMap<Integer, Parameter>();
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				FirstInparamenter.put(rs.getInt(1), new Parameter(rs.getInt(1), rs.getInt(2), rs.getString(4), rs.getInt(6), rs.getString(7)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FirstInparamenter;
	}

	@Override
	public Map<Integer, Parameter> getValidparameters(Map<Set<Integer>, ParameterSolution> parameterlist,
			Set<Parameter> initalset) {
		Set<Integer> getedpara=new HashSet<Integer>();
		for (Parameter np:initalset) {
			getedpara.add(np.getParameterid());
		}
		Map<Integer,Parameter> validparameterset=new HashMap<Integer,Parameter>();
		Map<Set<Integer>,Integer> retainmap=new HashMap<Set<Integer>,Integer>();//parameter_solution中的parameter集合与Initial参数集合的并集
		for(Set<Integer> set:parameterlist.keySet()) {
			Set<Integer> retainset=new HashSet<Integer>();
			retainset.addAll(set);
			retainset.retainAll(getedpara);
			if(retainset.size()>0) {
				retainmap.put(retainset, retainset.size());
			}
		}
		Map<Set<Integer>,Integer> newretainmap=sortByValue(retainmap);
		Map.Entry<Set<Integer>,Integer> entry = newretainmap.entrySet().iterator().next();
		Set<Integer> newset=entry.getKey();//并集最多的参数集合
		for(int id:newset) {
			for(Parameter thisp:initalset){
				if(id==thisp.getParameterid()) {
					validparameterset.put(thisp.getParameterid(),thisp);
				}
			}
		}
		return validparameterset;
	}

	@Override
	public List<ReturnInfo> inconversationrecord(String usrname) {
		List<ReturnInfo> list1=new LinkedList<ReturnInfo>();
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.user_dialogue_tb where username=? and type=0 order by datetime desc;";
		String[] params={usrname};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				ReturnInfo returninstance=new ReturnInfo(rs.getString(5), rs.getInt(7), rs.getString(3));
				returninstance.setDatetime(rs.getDate(4));
				returninstance.setParameter(rs.getString(6));
				returninstance.setRecieved(rs.getString(2));
				returninstance.setSpecial(rs.getInt(9));
				String uncheck=rs.getString(8);
				Set<Integer> set1=new HashSet<Integer>();
				if (uncheck.contains(",")) {
					String[] uncheckarr=uncheck.split(",");
					for(String thisone:uncheckarr){
						set1.add(Integer.valueOf(thisone));
					}
				}else {
					set1.add(Integer.valueOf(uncheck));
				}
				returninstance.setUncheckparameter(set1);
				list1.add(returninstance);
			}
			return list1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public boolean yesFunction(String text) {
		BaiduInstance baiduapi=new BaiduInstance();
		if(baiduapi.sentimentClassify(text).equals("positive")) {
			return true;
		}
		return false;
	}


	//当前已捕获参数set返回应回复内容
	@Override
	public  ReturnInfo getReturnMSG(Map<Set<Integer>, ParameterSolution> parameter_solutionlist,Map<Integer, Parameter> parameters,Map<Integer,Parameter> allparamenter,ProcessFactory process,AboutSolutionDAO solutiondao,AboutParametersDAO parameterdao,AnswerQuestion answer,String usrname) {
		Set<Integer> parameteridset= new HashSet<Integer>();
		String newgetedparameter="";
		for (int id:parameters.keySet()) {
			parameteridset.add(id);
			if(newgetedparameter.equals("")) {
				newgetedparameter=String.valueOf(id);
			}else {
				newgetedparameter=newgetedparameter+","+String.valueOf(id);
			}
		}
		for (Set<Integer> key : parameter_solutionlist.keySet()) {
			if(key.equals(parameteridset)) {
				ParameterSolution pSolution=parameter_solutionlist.get(key);
				int solutionid=pSolution.getSolution();
				return new ReturnInfo(String.valueOf(solutionid), 1, solutiondao.getSolutinStr(String.valueOf(solutionid)));
			}
		}
		//List<String> uncheckupperquestion=new LinkedList<String>();
		Set<Integer> upcheckparameterid=new HashSet<Integer>();
		Map<Set<Integer>, Integer> parametersolutionnewlist=new HashMap<Set<Integer>, Integer>();
		for(Set<Integer> key: parameter_solutionlist.keySet()){
			ParameterSolution thisPS=parameter_solutionlist.get(key);
			if(key.containsAll(parameteridset)){
				parametersolutionnewlist.put(key, thisPS.getSolutionrank());
			}
		}

		parametersolutionnewlist=sortByValue(parametersolutionnewlist);
		Entry<Set<Integer>, Integer> entry = parametersolutionnewlist.entrySet().iterator().next();
		ParameterSolution firstPS=parameter_solutionlist.get(entry.getKey());
		parametersolutionnewlist.remove(entry.getKey());
		SpecialProcess specialProcess=new SpecialProcess();
		List<PetInfo> petinfolist=specialProcess.getuserpetType(usrname);
		int specialtag=0;
		if (firstPS.getAgeperiod()!=null) {//需要年龄判断			
			 if (petinfolist.size()==1) {
				 if (petinfolist.get(0).getBirthday()!=null) {
					 if (specialProcess.ageProcess(firstPS, petinfolist.get(0))) {
							
						}else {//不符合年龄段
							specialtag=1;
							Entry<Set<Integer>, Integer> newentry = parametersolutionnewlist.entrySet().iterator().next();
							firstPS=parameter_solutionlist.get(newentry.getKey());
						}
				}else{//请告诉我宠物的年龄段
					DBService helper=new DBService();
					List<ReturnInfo> lastRecord=returnpassedrecord(1, usrname,helper);
					if (lastRecord.isEmpty()) {
						ReturnInfo infotag=getInfoByPS(firstPS, parametersolutionnewlist, upcheckparameterid, allparamenter, process, parameterdao, upcheckparameterid, answer, usrname, newgetedparameter);		
						infotag.setInfo("请告诉我"+petinfolist.get(0).getName()+"的出生年月日");
						infotag.setSpecial(2);//2插入年龄
						return infotag;
					}else {
						ReturnInfo infotag=lastRecord.get(0);
						infotag.setInfo("请告诉我"+petinfolist.get(0).getName()+"的出生年月日");
						return infotag;
					}	
				}
			}else {//请告诉是那只宠物
				
			}
			
		}else if (firstPS.getSex()!=null) {//需要性别判断
			if (petinfolist.get(0).getSex()!=0) {
				 if (specialProcess.sexProcess(firstPS, petinfolist.get(0))) {
						
					}else {//不符合性别
						if (specialtag==0) {
							Entry<Set<Integer>, Integer> newentry = parametersolutionnewlist.entrySet().iterator().next();
							firstPS=parameter_solutionlist.get(newentry.getKey());
						}					
					}
			}else{//请告诉我宠物的性别
				DBService helper=new DBService();
				List<ReturnInfo> lastRecord=returnpassedrecord(1, usrname,helper);
				if (lastRecord.isEmpty()) {
					ReturnInfo infotag=getInfoByPS(firstPS, parametersolutionnewlist, upcheckparameterid, allparamenter, process, parameterdao, upcheckparameterid, answer, usrname, newgetedparameter);		
					infotag.setInfo("请告诉我"+petinfolist.get(0).getName()+"的性别");
					infotag.setSpecial(3);//3插入性别
					return infotag;
				}else {
					ReturnInfo infotag=lastRecord.get(0);
					infotag.setInfo("请告诉我"+petinfolist.get(0).getName()+"的性别");
					return infotag;
				}				
			}
		}
		ReturnInfo infotag=getInfoByPS(firstPS, parametersolutionnewlist, upcheckparameterid, allparamenter, process, parameterdao, upcheckparameterid, answer, usrname, newgetedparameter);		
		return infotag;
	}

	public ReturnInfo getInfoByPS(ParameterSolution firstPS,Map<Set<Integer>, Integer> parametersolutionnewlist,Set<Integer> upcheckparameterid,Map<Integer,Parameter> allparamenter,ProcessFactory process,AboutParametersDAO parameterdao,Set<Integer> parameteridset,AnswerQuestion answer,String usrname,String newgetedparameter) {
		List<PSranklist> nowpsranklist=sortByrank(firstPS.getParameterset());
		int index=0;
		int questionid=0;
		String question="";
		for(PSranklist thisp:nowpsranklist){
			if(parameteridset.contains(thisp.getId())){

			}else {
				int curquesid=process.getquestionidbyparameterid(thisp.getId());
				if (answer.IsAskedQuestion(String.valueOf(curquesid), usrname)) {

				}else {
					index++;
					if(index==1) {
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
		//MaxUpperQuestion maxtimesquestion=getMaxString(uncheckupperquestion,process.inconversationrecord(senderid));
		ReturnInfo infotag=null;
		infotag=new ReturnInfo(String.valueOf(questionid), 0, question);
		infotag.setParameter(newgetedparameter);
		infotag.setUncheckparameter(parameterdao.updateUncheckParameter(upcheckparameterid, usrname));
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
		return infotag;
	}
	

	public static List<PSranklist> sortByrank(List<PSranklist> list1) {
		List<PSranklist> newlist=new LinkedList<PSranklist>();
		newlist=list1.stream().sorted((u1, u2) -> u2.getRank()-(u1.getRank())).collect(Collectors.toList());
		return newlist;
	}

	@Override
	public Set<Integer> updatebyChecked(Set<Integer> set1, String checked) {
		if (checked.contains(",")) {
			String[] checkedarray=checked.split(",");
			for (int i = 0; i < checkedarray.length; i++) {
				if (set1.contains(Integer.valueOf(checkedarray[i]))) {
					set1.remove(Integer.valueOf(checkedarray[i]));
				}
			}
		}else {
			if(set1.contains(Integer.valueOf(checked))){
				set1.remove(Integer.valueOf(checked));
			}
		}
		return set1;
	}

	@Override
	public Map<Integer,Parameter> IsUnexpectAnswer(ReturnInfo lastrecord, AboutParametersDAO parameterdao,String text,Map<Integer, Parameter> initalparameters) {
		// TODO Auto-generated method stub
		Map<Integer,Parameter> parametermap=new HashMap<Integer,Parameter>();
		if(!initalparameters.isEmpty()) {
			for(Integer key:initalparameters.keySet()) {
				if(lastrecord.getUncheckparameter().contains(key)) {
					parametermap.put(key, initalparameters.get(key));
				}
			}
		}
		return parametermap;
	}
}
