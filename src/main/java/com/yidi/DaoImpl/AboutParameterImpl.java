package com.yidi.DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes.Name;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.service.ConvertImpl;
import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterSolution;


public class AboutParameterImpl implements AboutParametersDAO {
	private static Logger logger = Logger.getLogger(AboutParameterImpl.class);

	@Override
	public Map<Integer, Parameter> getparams() throws SQLException {
		Map<Integer,Parameter> allparamenters=new HashMap<Integer, Parameter>();
		ResultSet rs;
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.parameter_tb;";
		rs=helper.executeQueryRS(sql, null);
		while(rs.next()) {
			Parameter p=new Parameter(rs.getInt(1),rs.getInt(2),rs.getString(4),rs.getInt(6),rs.getString(7));
			allparamenters.put(rs.getInt(1), p);
		}
		return allparamenters;
	}

	@Override
	public Map<Integer, Parameter> getparams(String id1) throws SQLException {
		Map<Integer,Parameter> allparamenters=new HashMap<Integer, Parameter>();
		ResultSet rs;
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.parameter_tb where first="+id1+";";
		rs=helper.executeQueryRS(sql, null);
		while(rs.next()) {
			Parameter p=new Parameter(rs.getInt(1),rs.getInt(2),rs.getString(4),rs.getInt(6),rs.getString(7));
			allparamenters.put(rs.getInt(1), p);
		}
		return allparamenters;
	}

	@Override
	public Map<Integer, Parameter> getparams2(String id2) throws SQLException {
		Map<Integer,Parameter> allparamenters=new HashMap<Integer, Parameter>();
		ResultSet rs;
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.parameter_tb where second="+id2+";";
		rs=helper.executeQueryRS(sql, null);
		while(rs.next()) {
			Parameter p=new Parameter(rs.getInt(1),rs.getInt(2),rs.getString(4),rs.getInt(6),rs.getString(7));
			allparamenters.put(rs.getInt(1), p);
		}
		return allparamenters;
	}

	@Override
	public boolean checkandpara(String para,String text) {
		if(para.contains("&")) {
			String[] pararray=para.split("&");
			int count=0;
			for(String parameter:pararray) {
				if(parameter.contains("!")){
					String afterparameter=parameter.replace("!", "");
					if(text.contains(afterparameter)) {

					}else {
						count++;
					}
				}
				else if(text.contains(parameter)) {
					count++;
				}
			}
			if(count==pararray.length) {
				return true;
			}
		}else {
			if(text.contains(para)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String checkParameterLine(String parameline,String text) {
		if (parameline.contains("|")) {
			String[] items=parameline.split("\\|");
			int count=0;
			for(String item:items){
				if (item.contains("#")) {
					int incount=0;
					String[] paramearray=item.split("#");
					for(int i=0;i+1<paramearray.length;i++){
						if(checkAllItems(paramearray[i],paramearray[i+1],text)){
							incount++;
						}				
					}
					if (incount==paramearray.length-1) {
						count++;
					}
					if(count>0){
						return parameline;
					}
				}else if(item.contains("/")) {//有多个参
					String[] paramelist=item.split("/");
					for(String parameter:paramelist) {
						if(checkandpara(parameter,text)) {
							return parameline;
						}
					}
				}else {//单参数
					if (text.contains(item)) {
						return parameline;
					}
				}
			}
		}else if(parameline.contains("(")&&parameline.contains(")")){
			if(parameline.contains("#")){
				String[] paramearray=parameline.split("#");
				for(int i=0;i+1<paramearray.length;i++){
					if(!checkAllItems(paramearray[i],paramearray[i+1],text)){
						return null;
					}				
				}
				return parameline;
			}
		}else {
			String[] items=parameline.split("/");
			for(String item:items){
				if (checkandpara(item,text)) {
					return parameline;
				}
			}
		}
		return null;
	}

	public boolean checkAllItems(String para1,String para2,String text){
		if (para1.contains("/")&&para2.contains("/")) {
			String newpara1="";
			String newpara2="";
			if(para1.contains("(")&&para1.contains(")")&&para2.contains("(")&&para2.contains(")")){
				newpara1=para1.substring(1,para1.length()-1);
				newpara2=para2.substring(1,para2.length()-1);
			}			
			String[] array1=newpara1.split("/");
			String[] array2=newpara2.split("/");
			int count=0;
			for (int i = 0; i < array1.length; i++) {
				for (int j = 0; j < array2.length; j++) {
					if (checkIsYesOrNo(array1[i], text)&&checkIsYesOrNo(array2[j], text)) {
						count++;
					}
				}
			}
			if (count>0) {
				return true;
			}
		}else if(para1.contains("/")&&para2.contains("&")){
			String newpara1="";
			String newpara2="";
			if(para1.contains("(")&&para1.contains(")")&&para2.contains("(")&&para2.contains(")")){
				newpara1=para1.substring(1,para1.length()-1);
				newpara2=para2.substring(1,para2.length()-1);
			}			
			String[] array1=newpara1.split("/");
			String[] array2=newpara2.split("&");
			int count=0;
			for (int i = 0; i < array1.length; i++) {
				for (int j = 0; j < array2.length; j++) {
					if (checkIsYesOrNo(array1[i], text)&&checkIsYesOrNo(array2[j], text)) {
						count++;
					}
					if (count==array2.length) {
						return true;
					}
				}
			}
		}else if(para1.contains("&")&&para2.contains("/")){
			String newpara1="";
			String newpara2="";
			if(para1.contains("(")&&para1.contains(")")&&para2.contains("(")&&para2.contains(")")){
				newpara1=para1.substring(1,para1.length()-1);
				newpara2=para2.substring(1,para2.length()-1);
			}			
			String[] array1=newpara1.split("&");
			String[] array2=newpara2.split("/");
			int count=0;
			for (int i = 0; i < array2.length; i++) {
				for (int j = 0; j < array1.length; j++) {
					if (checkIsYesOrNo(array2[i], text)&&checkIsYesOrNo(array1[j], text)) {
						count++;
					}
					if (count==array1.length) {
						return true;
					}
				}
			}
		}else if (para1.contains("&")&&para2.contains("&")) {
			String newpara1="";
			String newpara2="";
			if(para1.contains("(")&&para1.contains(")")&&para2.contains("(")&&para2.contains(")")){
				newpara1=para1.substring(1,para1.length()-1);
				newpara2=para2.substring(1,para2.length()-1);
			}			
			String[] array1=newpara1.split("&");
			String[] array2=newpara2.split("&");
			int allcount=0;
			int count=0;
			for (int i = 0; i < array1.length; i++) {
				for (int j = 0; j < array2.length; j++) {
					allcount++;
					if (checkIsYesOrNo(array1[i], text)&&checkIsYesOrNo(array2[j], text)) {
						count++;
					}
				}
			}
			if (allcount==count) {
				return true;
			}
		}else {
			String newpara1="";
			String newpara2="";
			if(para1.contains("(")&&para1.contains(")")&&para2.contains("(")&&para2.contains(")")){
				newpara1=para1.substring(1,para1.length()-1);
				newpara2=para2.substring(1,para2.length()-1);
			}			
			String[] array1=newpara1.split("/");
			String[] array2=newpara2.split("/");
			int no1count=0;
			int no2count=0;
			if(newpara1.contains("&")){
				String[] itemarray1=newpara2.split("/");
				String[] itemarray2=newpara1.split("&");
				for (int i = 0; i < itemarray1.length; i++) {
					int count=0;
					for (int j = 0; j < itemarray2.length; j++) {
						if (checkIsYesOrNo(itemarray1[i], text)&&checkIsYesOrNo(itemarray2[j], text)) {
							count++;
						}
					}
					if (count==itemarray2.length) {
						return true;
					}
				}
			}
			else if (!newpara1.contains("/")) {
				for (int i = 0; i < array2.length; i++) {
					if (checkIsYesOrNo(newpara1, text)&&checkIsYesOrNo(array2[i], text)) {
						no1count++;
					}
				}
				if (no1count>0) {
					return true;
				}
			}else if (newpara2.contains("&")) {
				String[] itemarray1=newpara1.split("/");
				String[] itemarray2=newpara2.split("&");
				for (int i = 0; i < itemarray1.length; i++) {
					int count=0;
					for (int j = 0; j < itemarray2.length; j++) {
						if (checkIsYesOrNo(itemarray1[i], text)&&checkIsYesOrNo(itemarray2[j], text)) {
							count++;
						}
					}
					if (count==itemarray2.length) {
						return true;
					}
				}
			}else if (!newpara2.contains("/")) {
				for (int i = 0; i < array1.length; i++) {
					if (checkIsYesOrNo(newpara2, text)&&checkIsYesOrNo(array1[i], text)) {
						no2count++;
					}
				}
				if (no2count>0) {
					return true;
				}
			}
			int allcount=0;
			int count=0;
			for (int i = 0; i < array1.length; i++) {
				for (int j = 0; j < array2.length; j++) {
					allcount++;
					if (checkIsYesOrNo(array1[i], text)&&checkIsYesOrNo(array2[j], text)) {
						count++;
					}
				}
			}
			if (allcount==count) {
				return true;
			}
		}
		return false;	
	}

	public boolean checkIsYesOrNo(String parameter,String text) {
		if (parameter.contains("&")) {
			String[] paray=parameter.split("&");
			int count=0;
			for (String paraitem:paray) {
				if (text.contains(paraitem)) {
					count++;
				}
			}
			if (count==paray.length) {
				return true;
			}else {
				return false;
			}
		}else if (parameter.contains("!")) {
			String newparameter=parameter.replace("!", "");
			if (text.contains(newparameter)) {
				return false;
			}else {
				return true;
			}
		}else {
			if (text.contains(parameter)) {
				return true;
			}else {
				return false;
			}
		}
	}



	public Parameter insertParametergetID(String paramenter,String first,String second) throws SQLException{
		DBService helper=new DBService();
		String sql1="INSERT INTO ai_qanda.parameter_tb (paramenter, rank, first, second) VALUES (?, '0', ?, ?);";

		Connection conn=helper.getConnection();
		PreparedStatement statement=conn.prepareStatement(sql1);
		statement.setString(1, paramenter);
		statement.setString(2, first);
		statement.setString(3, second);
		int rows=statement.executeUpdate();
		if(rows>0){
			String sql2="SELECT LAST_INSERT_ID();";
			ResultSet rs=statement.executeQuery(sql2);
			try {
				if (rs.next()) {
					int id=rs.getInt(1);
					Parameter newparameter=new Parameter(id, 0, paramenter, 0,first);
					return newparameter;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	public int insertQuestionID(String question,String parameterid,String parameterstr) throws SQLException{
		DBService helper=new DBService();
		String sql1="INSERT INTO ai_qanda.paramenterques_tb (question,chioces,answer,returnparameter) VALUES (?,1,?,?);";
		Connection conn=helper.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql1);
		statement.setString(1, question);
		statement.setString(2, parameterstr);
		statement.setString(3, parameterid);
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

	public boolean updateParameterquestionid(int parameterid,int questionid) {
		DBService helper=new DBService();
		String sql="UPDATE ai_qanda.parameter_tb SET quesid=? WHERE id=?;";
		Integer[] parame={questionid,parameterid};
		int rows=helper.executeUpdate(sql, parame);
		if (rows>0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<Integer, Parameter> targetparametersbyquestion(String questionid) {
		String sql="SELECT * FROM ai_qanda.parameter_tb where quesid=?;";
		String[] params={questionid};
		DBService helper=new DBService();
		Map<Integer,Parameter> Inparamenter=new HashMap<Integer, Parameter>();
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				Inparamenter.put(rs.getInt(1), new Parameter(rs.getInt(1), rs.getInt(2), rs.getString(4), rs.getInt(6), rs.getString(7)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		helper.closeAll();
		return Inparamenter;
	}

	@Override
	public boolean checkRemindParameter(String parameters) {
		String sql="SELECT * FROM ai_qanda.parameter_tb where id in (?);";
		DBService helper=new DBService();
		String[] params={parameters};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				int remindflag=rs.getInt(10);
				if(remindflag==1){
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
	public Set<Integer> getParametersByquestionid(String questionid) {
		Set<Integer> set1=new HashSet<>();
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.parameter_tb where quesid=?;";
		String[] params={questionid};
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				set1.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		helper.closeAll();
		return set1;
	}

	@Override
	public void updateStatus(String username) {
		String sql="update ai_qanda.user_dialogue_tb set type=1 where username=?;";
		DBService helper=new DBService();
		String[] params={username};
		helper.executeUpdate(sql, params);	
	}

	@Override
	public Set<Integer> updateUncheckParameter(Set<Integer> set1, String usrname) {
		DBService helper=new DBService();
		String[] params={usrname};
		String sql="SELECT parameter_tb.id FROM ai_qanda.parameter_tb right join ai_qanda.user_dialogue_tb on ai_qanda.parameter_tb.quesid=ai_qanda.user_dialogue_tb.replyid where user_dialogue_tb.type=0 and user_dialogue_tb.username=?;";
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while (rs.next()) {
				if (set1.contains(rs.getInt(1))) {
					set1.remove(rs.getInt(1));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set1;
	}

	@Override
	public Set<Integer> getrelatedparameters(Map<Set<Integer>, ParameterSolution> parameter_solutionlist,Set<Integer> parameters,Map<Integer,Parameter> allparamenter) {
		Set<Integer> set1=new HashSet<Integer>();
		for(Set<Integer> parameteridset:parameter_solutionlist.keySet()){
			if (parameteridset.containsAll(parameters)) {
				parameteridset.removeAll(parameters);
				set1.addAll(parameteridset);
			}
		}
		return set1;
	}
}
