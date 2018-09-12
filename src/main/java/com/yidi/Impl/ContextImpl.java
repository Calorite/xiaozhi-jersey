package com.yidi.Impl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.yidi.DaoImpl.DBService;





public class ContextImpl {

//	Logger log= LoggerFactory.getLogger(ContextImpl.class);


	public static boolean insert(String userid,String receive,String reply,Set<String> paramenters,int replyid,int type) throws IOException {
		// TODO Auto-generated method stub
		DBService helper=new DBService();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql="INSERT INTO `ai_qanda`.`user_dialogue_tb` (`username`, `receive`, `reply`, `datetime`, `replyid`, `parameters`, `type`) VALUES ('？', '？', '？', '？', '？', '？', '？');";
		String parameterstr=StringUtils.join(paramenters.toArray(), ";");
		Object[] params= {userid,receive,reply,df,replyid,parameterstr,type};
		try {
			int returnrows=helper.executeUpdate(sql, params);
			if(returnrows==1) {
				return true;
			}
		}catch(Exception e) {
			//log.trace("insert SQL = {}", sql);
		}
		return false;
	}


	public static boolean updateStatu(String userid) throws IOException {
		// TODO Auto-generated method stub
		DBService helper=new DBService();
		String sql="UPDATE `ai_qanda`.`user_dialogue_tb` SET `type` = '1' WHERE (`username` = '?')";
		Object[] params= {userid};
		try {
			int returnrows=helper.executeUpdate(sql, params);
			if(returnrows==1) {
				return true;
			}
		}catch(Exception e) {
			//log.trace("update SQL = {}", sql);
		}
		return false;
	}

}
