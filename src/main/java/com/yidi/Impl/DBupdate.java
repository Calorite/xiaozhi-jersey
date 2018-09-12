package com.yidi.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

import com.yidi.DaoImpl.DBService;
import com.yidi.entity.Parameter;
import com.yidi.entity.parameInQuestion;


public class DBupdate {

	public static String getparameteByID(String id){
		DBService helper=new DBService();
		String sql="SELECT paramenter FROM ai_qanda.parameter_tb where id=?";
		Object[] params= {id};
		Object returnlist=helper.executeQuerySingle(sql, params);
		return returnlist.toString();
	}

	public static Map<Integer,Parameter> getparams() throws SQLException{
		Map<Integer,Parameter> allparamenters=new HashMap<Integer,Parameter>();
		ResultSet rs;
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.parameter_tb where id>?;";
		String[] params= {"0"};
		rs=helper.executeQueryRS(sql, params);
		while(rs.next()) {
			Parameter p=new Parameter(rs.getInt(1),rs.getInt(2),rs.getString(4),rs.getInt(6), rs.getString(7));
			allparamenters.put(rs.getInt(1), p);
		}
		return allparamenters;
	}


	public static Map<Set<Integer>,Integer> getsolutionlist() throws SQLException{
		Map<Set<Integer>,Integer> list1=new HashMap<Set<Integer>,Integer>();
		ResultSet rs;
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.paramenter_solution where solutionid>?;";
		String[] params= {"0"};
		rs=helper.executeQueryRS(sql, params);
		while(rs.next()) {
			Set<Integer> set1=new HashSet<Integer>();
			String paramets=rs.getString(1);
			String[] pararray=paramets.split(",");
			Integer returnid=rs.getInt(2);
			for(String paramete:pararray) {
				set1.add(Integer.valueOf(paramete));
			}
			list1.put(set1, returnid);
		}
		return list1;
	}

	public static int getQuestionid(int type,Set<Integer> set1,Map<Integer,Parameter> allparamenter) throws SQLException {
		StringBuffer sql=null;
		DBService helper=new DBService();
		if(type==0) {
			sql=new StringBuffer("SELECT id,rank FROM ai_qanda.parameter_tb where ");
		}else {
			sql=new StringBuffer("SELECT id,rank FROM ai_qanda.parameter_cat_tb where ");
		}
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

	public static String getQustionStr(String id) {
		DBService helper=new DBService();
		String sql="SELECT question FROM ai_qanda.paramenterques_tb where id=?";
		String[] params= {id};
		Object returnlist=helper.executeQuerySingle(sql, params);
		return returnlist.toString();
	}

	public static String getSolutinStr(String id) {
		DBService helper=new DBService();
		String sql="SELECT question FROM ai_qanda.solution_tb where id=?";
		String[] params= {id};
		Object returnlist=helper.executeQuerySingle(sql, params);
		if(returnlist==null){
			return "";
		}else{
			return returnlist.toString();
		}

	}


	public static List<parameInQuestion> getParameterByqueid(String id) throws SQLException {
		List<parameInQuestion> list1=new LinkedList<parameInQuestion>();
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.paramenterques_tb where id=?";
		String[] params= {id};
		ResultSet rs=helper.executeQueryRS(sql, params);
		if (rs.next()) {
			String parameinques=rs.getString(4);
			String parameidinques=rs.getString(5);
			String[] paramearry=parameinques.split(",");
			String[] parameidarry=parameidinques.split(",");
			if (paramearry.length==parameidarry.length) {
				for (int i = 0; i < paramearry.length; i++) {
					parameInQuestion pip=new parameInQuestion(parameidarry[i],paramearry[i],getparameteByID(parameidarry[i]));
					list1.add(pip);
				}
			}else{
				System.out.println("参数与参数id个数不一致！");
			}
			return list1;
		}
		return null;
	}

	public static boolean updateParameter(parameInQuestion pip) {
		DBService helper=new DBService();
		String sql="update ai_qanda.parameter_tb set paramenter=? where id=?";
		String[] params= {pip.getParameter(),pip.getId()};
		int rows=helper.executeUpdate(sql, params);
		if (rows==1) {
			return true;
		}
		return false;
	}

	public static int Insertnewparameter(String parameter){
		DBService helper=new DBService();
		String sql="insert into ai_qanda.parameter_tb  (`paramenter`) VALUES ('"+parameter+"');";
		int rows=helper.insertgetID(sql, null);
		if(rows>0){
			return rows;
		}
		return 0;
	}

	public static int Insertsolution(String text){
		DBService helper=new DBService();
		String sql="INSERT INTO `ai_qanda`.`solution_tb` (`solution`) VALUES ('"+text+"');";
		int rows=helper.insertgetID(sql, null);
		if(rows>0){
			return rows;
		}
		return 0;
	}

	public static boolean InsertSolution(String parameters,String solutionid,String solutionrank,String rankset) {
		DBService helper=new DBService();
		String sql="INSERT INTO ai_qanda.paramenter_solution (parameterid,solutionid,solutionrank,prametersranklist) VALUES (?,?,?,?);";
		String[] paras={parameters,solutionid,solutionrank,rankset};
		int rows=helper.executeUpdate(sql, paras);
		if(rows==1){
			return true;
		}
		return false;
	}

	public static int getSolutionid(String text) throws SQLException {
		DBService helper=new DBService();
		String sql="INSERT INTO ai_qanda.solution_tb (solution) VALUES ('"+text+"');";
		Connection conn=helper.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		int rows=statement.executeUpdate();
		if(rows>0){
			String sql2="SELECT LAST_INSERT_ID();";
			ResultSet rs=statement.executeQuery(sql2);
			try {
				if (rs.next()) {
					int id=rs.getInt(1);
					return id;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
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

}
