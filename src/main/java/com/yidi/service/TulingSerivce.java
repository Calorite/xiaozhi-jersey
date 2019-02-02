package com.yidi.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yidi.entity.TulingPostEntity;

public class TulingSerivce {
	private static String entityToString(HttpEntity entity) throws IOException {
		String result = null;
		if(entity != null)
		{
			long lenth = entity.getContentLength();
			if(lenth != -1 && lenth < 2048)
			{
				result = EntityUtils.toString(entity,"UTF-8");
			}else {
				InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
				CharArrayBuffer buffer = new CharArrayBuffer(2048);
				char[] tmp = new char[1024];
				int l;
				while((l = reader1.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				result = buffer.toString();
			}
		}
		return result;
	}
	
	public static String postJson(String url,String jsonString){
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new ByteArrayEntity(jsonString.getBytes("UTF-8")));
			response = httpClient.execute(post);
			if(response != null && response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
				JSONObject responseBody = new JSONObject(result);
				String responseresult=responseBody.get("results").toString();
				String newresponse=responseresult.substring(1, responseresult.length()-1);
				JSONObject resresults = new JSONObject(newresponse);
				String valuesstr=resresults.get("values").toString();
				JSONObject values = new JSONObject(valuesstr);
				String text=values.get("text").toString();
				return text;
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();
				if(response != null)
				{
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static final String key="1a68aec9e32a4b5e86af755f052f4d35";
	public static String getresult(String content){
		Gson gson=new Gson();
		String posturl="http://openapi.tuling123.com/openapi/api/v2";
		TulingPostEntity postEntity=new TulingPostEntity(content);
		String postString=gson.toJson(postEntity);
//		System.out.println(postString);
//		System.out.println(postJson(posturl, postString));
		return postJson(posturl, postString);
	}
	
	public static void main(String[] args) {
		TulingSerivce service=new TulingSerivce();
		System.out.println(service.getresult("你好啊"));
	}
}
