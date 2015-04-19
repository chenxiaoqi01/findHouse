package com.jacky.util;

import java.util.Properties;

public class PropertiesUtil {
	// 赶集监测的url
	public static String ganjiSearchUrl;
	// 监测间隔时间
	public static String jianceTime;
	
	// 登陆这飞信登陆的账号
	public static String feixinPhone;
	// 登陆这飞信登陆短信验证密码
	public static String feixinPwd;
	
	// 接收方号码或昵称
	public static String receivers;
	
	
	static{
		try {
			Properties p = new Properties();
			p.load(PropertiesUtil.class.getResourceAsStream("/config.properties"));
			ganjiSearchUrl = p.getProperty("ganjiSearchUrl");
			jianceTime = p.getProperty("jianceTime");
			feixinPhone = p.getProperty("feixinPhone");
			feixinPwd = p.getProperty("feixinPwd");
			receivers = p.getProperty("receivers");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
