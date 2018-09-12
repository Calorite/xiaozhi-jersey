package com.yidi.entity;

import java.util.Set;

import com.google.gson.Gson;


public class Parama2Json {
	public static String GsonListStr(Set<Parameter> parametes) {
		Gson gson = new Gson();
		return gson.toJson(parametes);
	}
}
