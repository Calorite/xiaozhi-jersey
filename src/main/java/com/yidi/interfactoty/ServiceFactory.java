package com.yidi.interfactoty;

import com.yidi.DaoImpl.DBService;
import com.yidi.service.ProcessFactoryImpl;

public interface ServiceFactory {
	ParameterService getparameterService();
	ProcessFactory getprocessService();
	AboutParametersDAO getparameterDao(DBService helper);
	DBService getDBhelper();
	AboutQuestionDAO getquestionDao(DBService helper);
	AboutSolutionDAO getsolutionDao(DBService helper);
	SpecialprocessFactory getspecialprocess();
	ConvertAdapter getconverter();
}
