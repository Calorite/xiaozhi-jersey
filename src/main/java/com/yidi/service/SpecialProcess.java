package com.yidi.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.yidi.DaoImpl.DBService;
import com.yidi.entity.DialogueRecord;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.PetInfo;
import com.yidi.interfactoty.SpecialprocessFactory;

public class SpecialProcess implements SpecialprocessFactory {

	@Override
	public List<PetInfo> getuserpetType(String username) {
		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.pet_infomation where user_name=?;";
		String[] params= {username};
		List<PetInfo> set1=new LinkedList<PetInfo>();
		ResultSet rs=helper.executeQueryRS(sql, params);
		try {
			while(rs.next()) {
				PetInfo petInfo=new PetInfo(rs.getString(5), username);
				try {
					petInfo.setName(rs.getString(4));
				} catch (Exception e) {}
				try {
					petInfo.setBirthday(rs.getDate(5));
				} catch (Exception e) {}
				try {				
					petInfo.setSex(rs.getInt(6));//1雄2雌
				} catch (Exception e) {
				}
				
				set1.add(petInfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set1;
	}



	@Override
	public boolean insertpetName(String name,String username) {
		String sql="INSERT INTO ai_qanda.pet_infomation (user_name,name) VALUES (?, ?);";
		DBService helper=new DBService();
		String[] params= {username,name};
		int rows=helper.executeUpdate(sql, params);
		if(rows>0) {
			return true;
		}
		return false;
	}


	@Override
	public int CheckInDB(String name, String usename) {//1:sex   2:age  
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean ageProcess(ParameterSolution ps,PetInfo pet) {
		if(ps.getAgeperiod()!=null){
			String[] periodarray=ps.getAgeperiod().split("~");
			int minday=Integer.valueOf(periodarray[0]);
			int maxday=Integer.valueOf(periodarray[1]);
			Date petbrithday=pet.getBirthday();
			Date nowdate = new Date();  
			int days = (int) ((nowdate.getTime() - petbrithday.getTime()) / (1000*3600*24));
			if(minday<=days&&days>=maxday){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean sexProcess(ParameterSolution ps,PetInfo pet) {
		if(ps.getSex()!=null){
			if (ps.getSex().contains(String.valueOf(pet.getSex()))) {
				return true;
			}		
		}
		return false;
	}



	@Override
	public PetInfo ageinfocheck(String name, String username) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean insertBirthday(String birthday, String username) {
		birthday=birthday.trim();
		String str2="";
		if(birthday != null && !"".equals(birthday)){
			for(int i=0;i<birthday.length();i++){
				if(birthday.charAt(i)>=48 && birthday.charAt(i)<=57){
					str2+=birthday.charAt(i);
				}
			}
		}
		String insertbirthday=str2.substring(0,4)+"-"+str2.substring(4,6)+"-"+str2.substring(6,8);
		DBService helper=new DBService();
		String sql="UPDATE ai_qanda.pet_infomation SET birthday=? WHERE user_name=?;";
		String[] params={insertbirthday,username};
		int rows=helper.executeUpdate(sql, params);
		if(rows>0){
			return true;
		}
		return false;
	}



	@Override
	public boolean insertSex(String sexinfo, String username) {
		String sextag="2";//默认是母的
		if (sexinfo.contains("男")||sexinfo.contains("雄")||sexinfo.contains("公")) {
			sextag="1";
		}
		DBService helper=new DBService();
		String sql="UPDATE ai_qanda.pet_infomation SET sex=? WHERE user_name=?;";
		String[] params={sextag,username};
		int rows=helper.executeUpdate(sql, params);
		if(rows>0){
			return true;
		}
		return false;
	}
}