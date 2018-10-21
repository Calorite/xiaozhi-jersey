package com.yidi.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TulingSerivce {

	private static final String key="1a68aec9e32a4b5e86af755f052f4d35";
	public static String getresult(String content){
		String apiurl="http://openapi.tuling123.com/openapi/api/v2?key="+key+"&info=";
		try {
			content=URLEncoder.encode(content,"utf-8");
			apiurl=apiurl+content;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpGet request=new HttpGet(apiurl);
		String result="";
		try {
			HttpResponse response=HttpClients.createDefault().execute(request);
			int code=response.getStatusLine().getStatusCode();
			if(code==200){
				result=EntityUtils.toString(response.getEntity());
				
			}else{
				System.out.println("code="+code);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public static void main(String[] args) {
		TulingSerivce service=new TulingSerivce();
		System.out.println(service.getresult("你好啊"));
	}
}
