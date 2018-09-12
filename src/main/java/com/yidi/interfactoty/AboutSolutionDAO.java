package com.yidi.interfactoty;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.yidi.entity.PSranklist;
import com.yidi.entity.ParameterSolution;

public interface AboutSolutionDAO {
	String getSolutinStr(String id);
	Map<Set<Integer>,ParameterSolution> getsolutionlist() throws SQLException;
}
