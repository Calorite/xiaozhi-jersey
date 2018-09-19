package com.yidi.service;

import com.yidi.DaoImpl.DBService;
import com.yidi.DaoImpl.DefaultDaoFactory;
import com.yidi.Impl.AnswerQuestionImpl;
import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.interfactoty.AboutSolutionDAO;
import com.yidi.interfactoty.AnswerQuestion;
import com.yidi.interfactoty.ConvertAdapter;
import com.yidi.interfactoty.DaoFactory;
import com.yidi.interfactoty.ParameterService;
import com.yidi.interfactoty.ProcessFactory;
import com.yidi.interfactoty.ServiceFactory;
import com.yidi.interfactoty.SpecialprocessFactory;

public class DefaultServiceFactory implements ServiceFactory {
	private DaoFactory daoFactory;

	public DefaultServiceFactory() {
		this(new DefaultDaoFactory());
	}

	public DefaultServiceFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public AboutParametersDAO getparameterDao(DBService helper) {
		// TODO Auto-generated method stub
		return daoFactory.getparametersdao(helper);
	}

	@Override
	public DBService getDBhelper() {
		// TODO Auto-generated method stub
		return new DBService();
	}

	@Override
	public AboutQuestionDAO getquestionDao(DBService helper) {
		// TODO Auto-generated method stub
		return daoFactory.getquestiondao(helper);
	}

	@Override
	public AboutSolutionDAO getsolutionDao(DBService helper) {
		// TODO Auto-generated method stub
		return daoFactory.getsolution(helper);
	}

	@Override
	public ProcessFactory getprocessService() {
		// TODO Auto-generated method stub
		return daoFactory.getprocess();
	}

	@Override
	public ParameterService getparameterService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpecialprocessFactory getspecialprocess() {
		return new SpecialProcess();
	}

	@Override
	public ConvertAdapter getconverter() {
		return new ConvertImpl();
	}

	@Override
	public AnswerQuestion getanswer() {
		// TODO Auto-generated method stub
		return new AnswerQuestionImpl();
	}


}
