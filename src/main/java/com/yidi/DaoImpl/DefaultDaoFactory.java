package com.yidi.DaoImpl;

import com.yidi.interfactoty.AboutParametersDAO;
import com.yidi.interfactoty.AboutQuestionDAO;
import com.yidi.interfactoty.AboutSolutionDAO;
import com.yidi.interfactoty.DaoFactory;
import com.yidi.interfactoty.ParameterService;
import com.yidi.interfactoty.ProcessFactory;
import com.yidi.service.ProcessFactoryImpl;

public class DefaultDaoFactory implements DaoFactory {

	@Override
	public ProcessFactory getprocess() {
		// TODO Auto-generated method stub
		return new ProcessFactoryImpl();
	}

	@Override
	public AboutParametersDAO getparametersdao(DBService helper) {
		// TODO Auto-generated method stub
		return new AboutParameterImpl();
	}

	@Override
	public AboutQuestionDAO getquestiondao(DBService helper) {
		// TODO Auto-generated method stub
		return new AboutQuestionImpl();
	}

	@Override
	public AboutSolutionDAO getsolution(DBService helper) {
		// TODO Auto-generated method stub
		return new AboutSolutionImpl();
	}


}
