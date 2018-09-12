package com.yidi.DaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.entity.Parameter;
import com.yidi.entity.Question;
import com.yidi.entity.UpperQuestion;


public class AboutQuestionImpl implements AboutQuestionDAO {

	DBService helper=new DBService();

	@Override
	public String getQustionStr(String id) {
		DBService helper=new DBService();
		String sql="SELECT question FROM ai_qanda.paramenterques_tb where id=?";
		String[] params= {id};
		Object returnlist=helper.executeQuerySingle(sql, params);
		return returnlist.toString();
	}

	@Override
	public int getQuestionid(Set<Integer> set1, Map<Integer, Parameter> allparamenter) throws SQLException {
		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		for(int id:set1) {
			Parameter paramenter=allparamenter.get(id);
			map.put(id,paramenter.getRank());
		}
		Map<Integer,Integer> soredmap=sortByValue(map);
		Map.Entry<Integer,Integer> entry = soredmap.entrySet().iterator().next();
		Integer firstone=entry.getKey();
		if(firstone!=null) {
			Parameter paramenter=allparamenter.get(firstone);
			return paramenter.getQuestionid();
		}
		return 0;
	}

	public static List<UpperQuestion> getAllupperQuesiton() {
		String sql="SELECT * FROM ai_qanda.upperquestion_tb;";
		List<UpperQuestion> quesitonlist=new LinkedList<UpperQuestion>();
		DBService helper=new DBService();
		ResultSet rs;
		rs=helper.executeQueryRS(sql, null);
		try {
			while(rs.next()){
				UpperQuestion newone=new UpperQuestion(rs.getString(1),rs.getString(2));
				quesitonlist.add(newone);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quesitonlist;
	}

	public static boolean updateBundparameter(String parameter,String questionid) {
		DBService helper=new DBService();
		String sql="UPDATE ai_qanda.paramenterques_tb SET returnparameter=? WHERE id=?;";
		String[] paramr={parameter,questionid};
		int rows=helper.executeUpdate(sql, paramr);
		if(rows>0){
			return true;
		}
		return false;
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

	@Override
	public List<Question> getQuestionlist(DBService helper) {
		String sql="SELECT * FROM ai_qanda.paramenterques_tb;";
		List<Question> quesitonlist=new LinkedList<Question>();
		ResultSet rs;
		rs=helper.executeQueryRS(sql, null);
		try {
			while(rs.next()){
				Question newone=new Question(String.valueOf(rs.getInt(1)),rs.getString(2));
				quesitonlist.add(newone);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quesitonlist;
	}

	@Override
	public boolean updateQuestionparametr(String questionid, String parameterid,String parameter) {
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.paramenterques_tb where id=?";
		String[] params= {questionid};
		ResultSet rs=helper.executeQueryRS(sql, params);
		int prechoice=0;
		String preparameter="";
		String prereturnid="";
		try {
			if (rs.next()) {
				prechoice=rs.getInt(3);
				preparameter=rs.getString(4);
				prereturnid=rs.getString(5);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int newchoice=prechoice+1;
		String newparameter=preparameter+","+parameter;
		String newreturnid=prereturnid+","+parameterid;
		try {
			String sql1="UPDATE ai_qanda.paramenterques_tb SET chioces=?, answer=?, returnparameter=? WHERE id=?;";
			String[] params2={String.valueOf(newchoice),newparameter,newreturnid,questionid};
			helper.executeUpdate(sql1, params2);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public String answerQuestion(int id, String text) {
		String sql="";
		return null;
	}

	@Override
	public List<Question> getFirstQuestion() {
		String sql="SELECT * FROM ai_qanda.upperquestion_tb where rank=1;";
		List<Question> list1=new LinkedList<Question>();
		ResultSet rs=helper.executeQueryRS(sql, null);
		try {
			while (rs.next()) {
				list1.add(new Question(rs.getString(1),rs.getString(2)));
			}
			return list1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<Question> getSecendQuestion() {
		String sql="SELECT * FROM ai_qanda.upperquestion_tb where rank=2;";
		List<Question> list1=new LinkedList<Question>();
		ResultSet rs=helper.executeQueryRS(sql, null);
		try {
			while (rs.next()) {
				list1.add(new Question(rs.getString(1),rs.getString(2)));
			}
			return list1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public String getUpperquestionbyid(String id) {
		String sql="SELECT question FROM ai_qanda.upperquestion_tb WHERE id=?;";
		String[] params= {id};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Integer, Parameter> gettargetparamete(String questionid) {
		Map<Integer,Parameter> map1=new HashMap<Integer,Parameter>();
		String sql="SELECT * FROM ai_qanda.paramenterques_tb where id=?;";
		DBService helper=new DBService();
		String[] params= {questionid};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while(rs.next()) {
				String[] parameidarray=rs.getString(4).split(",");
				String[] paramsarrsy=rs.getString(5).split(",");
				if(parameidarray.length<=paramsarrsy.length) {
					for(int i=0;i<parameidarray.length;i++) {
						map1.put(Integer.valueOf(parameidarray[i]), new Parameter(Integer.valueOf(parameidarray[i]),Integer.valueOf(questionid),paramsarrsy[i], 0, ""));
					}
				}else {
					for(int i=0;i<paramsarrsy.length;i++) {
						map1.put(Integer.valueOf(parameidarray[i]), new Parameter(Integer.valueOf(parameidarray[i]),Integer.valueOf(questionid),paramsarrsy[i], 0, ""));
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
