package com.yidi.DaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yidi.entity.PSranklist;
import com.yidi.entity.ParameterSolution;
import com.yidi.interfactoty.AboutSolutionDAO;

public class AboutSolutionImpl implements AboutSolutionDAO {

	@Override
	public String getSolutinStr(String id) {
		String sql="SELECT solution FROM ai_qanda.solution_tb where id=?";
		String[] params= {id};
		DBService helper=new DBService();
		Object returnlist=helper.executeQuerySingle(sql, params);
		return returnlist.toString();
	}

	@Override
	public Map<Set<Integer>, ParameterSolution> getsolutionlist() throws SQLException {
		Map<Set<Integer>,ParameterSolution> list1=new HashMap<Set<Integer>,ParameterSolution>();
		ResultSet rs;

		DBService helper=new DBService();
		String sql="SELECT * FROM ai_qanda.parameter_solution where solutionid;";
		rs=helper.executeQueryRS(sql, null);
		while(rs.next()) {
			Set<Integer> set1=new HashSet<Integer>();
			String paramets=rs.getString(1);
			List<PSranklist> psranklist = new LinkedList<PSranklist>();
			try {
				String[] pararray=paramets.split(",");
				String[] parankarrsy=rs.getString(4).split(",");
				if(pararray.length>1){
					for(int i=0;i<pararray.length;i++){
						set1.add(Integer.valueOf(pararray[i]));
						psranklist.add(new PSranklist(Integer.valueOf(pararray[i]), Integer.valueOf(parankarrsy[i])));
					}
				}
				Integer solutionrank=rs.getInt(3);
				Integer returnid=rs.getInt(2);
				list1.put(set1, new ParameterSolution(psranklist, returnid,solutionrank));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("fail");
			}
		}
		return list1;
	}

}
