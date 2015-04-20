package com.jacky.feixin.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacky.feixin.bean.SearchResultBean;
import com.jacky.util.HttpClientUtils;

public class FeixinService {
	private static HttpClient httpClient = new DefaultHttpClient();
	static{
		//请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
		//读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
		
	}
	
	// 是否登陆的状态 默认是未登陆
	private static boolean loginFlag = false;
	// 我的飞信ID
	private static String myFeixinId = "";
	
	/**
	 * 发送飞信短信
	 * @param sender 发送号码
	 * @param pwd 发送号码密码
	 * @param receiver 接收号码或昵称
	 * @param content 发送内容
	 * @return
	 */
	public static boolean sendMsg(String sender, String pwd, String receiver, String content) throws Exception{
		if(!loginFlag){
			// 登陆
			String retMsg = login(sender, pwd);
			System.out.println(retMsg);
			Map<String, Object> map = new Gson().fromJson(retMsg, new TypeToken<Map<String, Object>>(){}.getType());
			myFeixinId = (String)map.get("idUser");
			System.out.println(myFeixinId);
			if(myFeixinId!=null && myFeixinId.length()>0){
				//飞信登陆成功
				System.out.println("已经成功登陆飞信，自己的飞信ID是："+myFeixinId);
				loginFlag = true;
				// 登录之后隔一分钟访问一下飞信服务器，防止session超时
				Runnable t = new Runnable(){
					@Override
					public void run() {
						while(true){
							try {
								Thread.sleep(60000);
								System.out.println("心跳："+xintiao());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				new Thread(t).start();
			}else{
				System.out.println("登陆失败"+(String)map.get("tip"));
			}
		}
		// 只有已经登陆才可以发送飞信短信
		if(loginFlag){
			// 判断接收号码是不是发送号码，如果是则是发给自己
			if(sender.equals(receiver)){
				sendMy(myFeixinId, content);
			}
			
			// 如果不是则发给他人
			else{
				// 查询他人的飞信id
				String findResult = findFriend(receiver);
				SearchResultBean findBean = new Gson().fromJson(findResult, SearchResultBean.class);
				int count = findBean.getTotal();
				if(count>0){
					DecimalFormat decimalFormat = new DecimalFormat("#");//格式化设置 
					String touserid = decimalFormat.format(findBean.getContacts()[0].get("idContact"));
					System.out.println("接收方ID："+touserid);
					send(touserid, content);
				}else{
					// 失败
					System.out.println("接收方"+receiver+"找不到");
				}
			}
			
		}
		return true;
	}
	
	// 查找
	private static String findFriend(String phone){
		String url = "http://f.10086.cn/im5/index/searchFriendsByQueryKey.action?queryKey="+phone;
		String content = HttpClientUtils.methodGet(httpClient, url);
		return content;
	}
	
	// 登陆
	private static String login(String phone, String pwd){
		Map<String, String> params = new HashMap<String, String>();
		params.put("m", phone);
		params.put("pass", pwd);
		params.put("captchaCode", "");
		params.put("checkCodeKey", "null");
		params.put("t", new Date().getTime()+"");
		String content = HttpClientUtils.methodPost(httpClient, "http://f.10086.cn/im5/login/loginHtml5.action", params);
		return content;
	}
	
	
	
	// 发送信息
	private static String send(String touserid, String msg){
		Map<String, String> params = new HashMap<String, String>();
		params.put("touserid", touserid);
		params.put("msg", msg);
		params.put("csrfToken", "");
		String content = HttpClientUtils.methodPost(httpClient, "http://f.10086.cn/im5/chat/sendNewMsg.action", params);
		return content;
	}
	// 发送自己信息
	private static String sendMy(String myId, String msg){
		Map<String, String> params = new HashMap<String, String>();
		params.put("touserid", myId);
		params.put("msg", msg);
		String content = HttpClientUtils.methodPost(httpClient, "http://f.10086.cn/im5/chat/sendNewGroupShortMsg.action", params);
		return content;
	}
	
	// 心跳
	private static String xintiao(){
		// 号码随便填，不为空就行 主要是为了让session不要过期
		return findFriend("15914489532");
	}
}
