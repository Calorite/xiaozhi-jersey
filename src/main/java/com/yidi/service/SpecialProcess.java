package com.yidi.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.yidi.DaoImpl.DBService;
import com.yidi.entity.DialogueRecord;
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
				set1.add(new PetInfo(rs.getString(5), username));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set1;
	}

	@Override
	public boolean ageinfocheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int ageProcess(String text, DialogueRecord lr) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int sexProcess(String text, DialogueRecord lr) {
		// TODO Auto-generated method stub
		return 0;
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

}
