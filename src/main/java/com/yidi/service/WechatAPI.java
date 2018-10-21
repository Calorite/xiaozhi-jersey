package com.yidi.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;



import org.apache.http.HttpEntity;
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
import com.yidi.entity.AccessToken;
import com.yidi.entity.WechatPostContent;
import com.yidi.entity.WechatPostEntity;

public class WechatAPI {

	private static final String geturl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx39172f9a64862437&secret=316ed666268225a35685ae1c2165a4c7";
	
	private String entityToString(HttpEntity entity) throws IOException {
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
	/**
	 * get请求，参数拼接在地址上
	 * @param url 请求地址加参数
	 * @return 响应
	 */
	public String get(String url)
	{
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(get);
			if(response != null && response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			}
			return result;
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


	public String postJson(String url,String jsonString){
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

	public boolean SendMSG(String touser,String text) {
		WechatPostContent content=new WechatPostContent(text);
		Gson gson=new Gson();
		WechatPostEntity wechatpost=new WechatPostEntity(touser, "text", gson.toJson(content));	
		AccessToken token=gson.fromJson(get(geturl),AccessToken.class);
		String sendurl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		sendurl=sendurl.replace("ACCESS_TOKEN", token.getAccess_token());
		String test=gson.toJson(wechatpost);
		System.out.println(postJson(sendurl, gson.toJson(wechatpost)));
		return false;
	}
	
	
	public static void main(String[] args) {
		WechatAPI wechatAPI=new WechatAPI(); 
		String url="https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=14_IwQXvIehJTVu-CJHmSUci3RbZyLF6Bbn0S-3OwJBKJijE7-VE-3CQptP7TzkPFAVQ-oLredOLLPsMyQKUZkBzesQapJAVY67-CRz0sEwsGTYKvvwzsRLwipVm4wKLFiAHAWXR";		
		String getalluser="https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=14_IwQXvIehJTVu-CJHmSUci3RbZyLF6Bbn0S-3OwJBKJijE7-VE-3CQptP7TzkPFAVQ-oLredOLLPsMyQKUZkBzesQapJAVY67-CRz0sEwsGTYKvvwzsRLwipVm4wKLFiAHAWXR";
		wechatAPI.SendMSG("oYsXB0XuweW-P7TvTwu8RYXdkrTM", "你好啊");
	}
}
