package com.jacky.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	/**
	 * 发送Get请求
	 * @param httpClient httpClient4
	 * @param url http地址
	 * @param encoding 编码
	 * @return
	 */
	public static String methodGet(HttpClient httpClient, String url) {
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送 Post请求
	 * @param httpClient httpClient4
	 * @param url http网址
	 * @param paramsMap	form参数
	 * @return
	 */
	public static String methodPost(HttpClient httpClient, String url, Map<String, String> paramsMap) {
		String body = null;
		try {
			HttpPost httppost = new HttpPost(url);
			// 设置参数
			if(paramsMap != null){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				 for (String key : paramsMap.keySet()) {
					NameValuePair valuePair = new BasicNameValuePair(key, paramsMap.get(key));
					params.add(valuePair);
				}
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity, "UTF-8");
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return body;
	}
}
