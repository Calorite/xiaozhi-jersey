package com.yidi.service;

import org.json.JSONArray;
import org.json.JSONException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BsonInstance {

	public static final String SENTIMENT_URL =
			"http://api.bosonnlp.com/sentiment/analysis";
	public static void main(String[] args) throws JSONException, UnirestException,java.io.IOException{
		String body = new JSONArray(new String[]{"不是", "没"}).toString();
		HttpResponse<JsonNode> jsonResponse = Unirest.post(SENTIMENT_URL)
				.header("Accept", "application/json")
				.header("X-Token", "UXEM0gVS.16545.eHCVvdxNoe7O")
				.body(body)
				.asJson();

		System.out.println(jsonResponse.getBody());

		// Unirest starts a background event loop and your Java
		// application won't be able to exit until you manually
		// shutdown all the threads
		Unirest.shutdown();
	}
}
