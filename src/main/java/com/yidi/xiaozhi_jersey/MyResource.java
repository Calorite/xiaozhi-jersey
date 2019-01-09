package com.yidi.xiaozhi_jersey;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidi.DaoImpl.AboutParameterImpl;
import com.yidi.DaoImpl.AboutQuestionImpl;
import com.yidi.DaoImpl.DBService;
import com.yidi.Impl.DBupdate;
import com.yidi.algorithm.Parama2Json;
import com.yidi.entity.Parameter;
import com.yidi.entity.ParameterDTO;
import com.yidi.entity.Question;
import com.yidi.entity.UpperQuestion;
import com.yidi.entity.parameInQuestion;
import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.interfactoty.ProcessFactory;
import com.yidi.service.DefaultServiceFactory;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/dbtool")
public class MyResource {
	DBService helper=new DBService();
	Gson gson=new Gson();
	DefaultServiceFactory factory=new DefaultServiceFactory();
	AboutQuestionDAO questiondao=factory.getquestionDao(helper);
	AboutParametersDAO parameterdao=factory.getparameterDao(helper);
	ProcessFactory process=factory.getprocessService();
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET
    @Produces("text/plain")
    public void getIt(@Context HttpServletRequest request,@Context HttpServletResponse response) {
    	try {

			List<UpperQuestion> alllist=AboutQuestionImpl.getAllupperQuesiton();
			List<UpperQuestion> list1=new ArrayList<UpperQuestion>();
			List<UpperQuestion> list2=new ArrayList<UpperQuestion>();
			for(UpperQuestion up:alllist){
				if(up.getId().contains("A")){
					list1.add(up);
				}else {
					list2.add(up);
				}
			}
			request.setAttribute("firstlist", list1);
			request.setAttribute("secondlist", list2);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Path("getquestionlist")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String getQuestions() throws IOException {
		List<Question> questionlist=new LinkedList<>();
		try{
			questionlist=questiondao.getQuestionlist(helper);
			String questionStr=gson.toJson(questionlist);
			return questionStr;
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}


	@Path("getparametes")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String getParametes(@FormParam("description") String text) throws IOException {
		Set<Parameter> parametes;
		if(text!=null){
			try {
				Gson gson=new Gson();
				Map<Integer,Parameter> parametermap=process.getInitialParameters(parameterdao.getparams(),text,factory.getparameterDao(helper));
				Set<Parameter> initalparameterset=new HashSet<Parameter>();
				for(Parameter p:parametermap.values()){
					initalparameterset.add(p);
				}
				return (gson.toJson(initalparameterset));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return "";
	}

	@Path("question")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	public String getQuestion(@FormParam("quesid") String questionid) {
		String question=DBupdate.getQustionStr(questionid);
		return question;
	}

	@Path("getparamaterbyquestionid")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String getQuestionParameters(@FormParam("quesid") String questionid) throws SQLException {
		return Parama2Json.GsonListStr2(DBupdate.getParameterByqueid(questionid));
	}

	@Path("updateparamaters")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String getUpdateParameters(@FormParam("parametersJSON") String parametersjson) throws SQLException {

		List<parameInQuestion> parameters=gson.fromJson(parametersjson, new TypeToken<List<parameInQuestion>>(){}.getType());
		int count=0;
		String inparameter="";
		String inparameid="";
		String questionid="";
		for(parameInQuestion pip:parameters){
			questionid=DBupdate.getQuestionbyparameterid(pip.getId());
			if(inparameter.equals("")){
				inparameid=pip.getId();
				inparameter=pip.getParameterinquestion();
			}else {
				inparameter=inparameter+","+pip.getParameterinquestion();
				inparameid=inparameid+","+pip.getId();
			}
			if(DBupdate.updateParameter(pip)){
				count++;
			}
		}
		try {
			DBupdate.updateInparamters(String.valueOf(parameters.size()), inparameter, inparameid,questionid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (count==parameters.size()) {
			return "true";
		}
		return "false";
	}

	@Path("insertnewparameter")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String getNewParameterID(@FormParam("parame") String parameter) throws SQLException {
		try {
			String id=String.valueOf(DBupdate.Insertnewparameter(parameter));
			return id;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("fail");
		}
		return "0";
	}


	@Path("insertsolution")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String getsolutionID(@FormParam("solutiontext") String parameter) throws SQLException {
		try {
			String id=String.valueOf(DBupdate.Insertsolution(parameter));
			System.out.println(id);
			return id;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("fail");
		}
		return "0";
	}

	@Path("putInparametersolution")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String parametersolution(@FormParam("parameters") String parameters,@FormParam("solutionid") String solutionid,@FormParam("solutionrank") String solutionrank,@FormParam("minset") String minparaset,@FormParam("sexvalue") String sex,@FormParam("agemin") String age1,@FormParam("agemax") String age2) throws SQLException {
		try {
			int insolutionrank=0;
			if (!solutionrank.equals("")) {
				insolutionrank=Integer.valueOf(solutionrank);
			}
			Gson gs=new Gson();
			String parametersetstr="";
			String ranksetstr="";
			String soluid=String.valueOf(DBupdate.getSolutionid(solutionid));
			List<ParameterDTO> jsonObjectlist = gs.fromJson(parameters, new TypeToken<List<ParameterDTO>>(){}.getType());
			if (minparaset!=null) {
				String minparametrstr=minparaset.replace("]", "").replace("[", "").replace("\"", "");
				String[] minparameterArr=minparametrstr.split(",");
				if (minparameterArr.length>1) {
					String minparametersetstr="";
					String minranksetstr="";
					for(String item:minparameterArr){
						for(int i=0;i<jsonObjectlist.size();i++){
							ParameterDTO pdto=jsonObjectlist.get(i);
							if(item.equals(pdto.getId())){
								if(minparametersetstr.equals("")||minranksetstr.equals("")){
									minparametersetstr=pdto.getId();
									minranksetstr=pdto.getRank();
									break;
								}else {
									minparametersetstr=minparametersetstr+","+pdto.getId();
									minranksetstr=minranksetstr+","+pdto.getRank();
									break;
								}
							}
						}
					}
					DBupdate.InsertSolution(minparametersetstr,soluid,String.valueOf(insolutionrank),minranksetstr, sex, age1, age2);
				}
			}
			for(int i=0;i<jsonObjectlist.size();i++){
				ParameterDTO pdto=jsonObjectlist.get(i);
				if(i==0){
					parametersetstr=pdto.getId();
					ranksetstr=pdto.getRank();
				}else {
					parametersetstr=parametersetstr+","+pdto.getId();
					ranksetstr=ranksetstr+","+pdto.getRank();
				}
			}
			DBupdate.InsertSolution(parametersetstr,soluid,String.valueOf(insolutionrank),ranksetstr, sex, age1, age2);
			return "1";
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("fail");
		}
		return "0";
	}


	@Path("putNewparameter")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String insertnewParameterANDQuestion(@FormParam("newparameter") String parameters,@FormParam("question") String solutionid,@FormParam("first") String firstid,@FormParam("second") String secondid) throws SQLException {
		try {
			Map<String, String> returnmap=new HashMap<>();
			AboutParameterImpl paImpl=new AboutParameterImpl();
			Parameter parater=paImpl.insertParametergetID(parameters,firstid,secondid);
			int questionid=paImpl.insertQuestionID(solutionid, String.valueOf(parater.getParameterid()),parameters);
			paImpl.updateParameterquestionid(parater.getParameterid(), questionid);
			parater.setQuestionid(questionid);
			List<Parameter> pmeters=new ArrayList<>();
			Map<Integer, Parameter> allparameters=paImpl.getparams();
			for(Parameter p: allparameters.values()){
				pmeters.add(p);
			}
			String returnstr=Parama2Json.listToJSON(pmeters);
			String returnparama=String.valueOf(parater.getParameterid())+';'+parater.getParameter();
			returnmap.put("parameterIN", returnparama);
			returnmap.put("parameterlist", returnstr);
			returnmap.put("questionid", String.valueOf(questionid));
			return gson.toJson(returnmap);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("fail");
		}
		return "0";
	}

	@Path("bundparametertoQuestion")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String AddparameterToquestion(@FormParam("newparameter") String parameter,@FormParam("questionid") String id,@FormParam("first") String firstid,@FormParam("second") String secondid) throws SQLException {
		AboutParameterImpl paImpl=new AboutParameterImpl();
		Parameter parater=paImpl.insertParametergetID(parameter,firstid,secondid);
		if(questiondao.updateQuestionparametr(id, String.valueOf(parater.getParameterid()), parameter)){
			paImpl.updateParameterquestionid(parater.getParameterid(), Integer.valueOf(id));
			return "true";
		}
		return "false";
	}


	@Path("bundnewparameter")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  //接受的参数类型为表单信息
	@Produces({MediaType.APPLICATION_JSON})
	public String updateParameterForquestion(@FormParam("parameter") String parameters,@FormParam("questionid") String id) throws SQLException {
		try {
			String parameliststr=parameters.replace("[", "").replace("]", "").replace("\"","");
			if(AboutQuestionImpl.updateBundparameter(parameliststr, id)){
				return "1";
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "0";
		}

}
