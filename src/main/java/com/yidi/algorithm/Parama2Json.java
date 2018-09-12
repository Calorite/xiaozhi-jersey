package com.yidi.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.yidi.entity.Parameter;
import com.yidi.entity.parameInQuestion;


public class Parama2Json {

	public static String GsonListStr2(List<parameInQuestion> parametes) {
		Gson gson = new Gson();
		return gson.toJson(parametes);
	}


	public static String listToJSON(List<Parameter> parameterlist){
		Gson gson = new Gson();
		return gson.toJson(parameterlist);
	}
}
