package com.yidi.service;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.nlp.AipNlp;
import com.google.gson.Gson;

public class BaiduInstance {
	public static final String APP_ID = "10105799";
	public static final String API_KEY = "tog9F1yLuvnP8xKOf1iyHfsg";
	public static final String SECRET_KEY = "7q508DAbFR0uRxuKpTx7Z66fSYjzhYmN";
	private static Logger log = Logger.getLogger(BaiduInstance.class);
	public static String sentimentClassify(String text) {
		AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

		// 可选：设置网络连接参数
		// client.setConnectionTimeoutInMillis(2000);
		//client.setSocketTimeoutInMillis(60000);

		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		//client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
		// client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		//System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

		// 调用接口
		Gson gson=new Gson();
		HashMap<String, Object> options = new HashMap<String, Object>();
		JSONObject res = client.sentimentClassify(text, options);
		try{
			JSONArray itemsobj=res.getJSONArray("items");
			Object positive=itemsobj.getJSONObject(0).get("positive_prob");
			Object negative=itemsobj.getJSONObject(0).get("negative_prob");
			if(Double.parseDouble(positive.toString())>Double.parseDouble(negative.toString())) {
				return "positive";
			}else {
				return "negative";
			}

		}catch(Exception e) {
			log.error("调用百度API获取返回值失败！");
		}
		return "";
	}
}
