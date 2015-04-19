package com.jacky.ganji.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.jacky.ganji.bean.ItemBean;
import com.jacky.ganji.bean.ItemDetailBean;
import com.jacky.util.HttpClientUtils;
import com.jacky.util.PropertiesUtil;

public class GanjiService {
	private static HttpClient httpClient = new DefaultHttpClient();
	
	static{		
//		HttpHost proxy = new HttpHost("117.146.116.2", 80);
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy); 
		 //让服务器知道访问源为浏览器 
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
		
	}
	
	public static void main(String[] args) {
		System.out.println(getMsgGanjiList().size());
	}
	
	// 获取列表
	public static List<ItemBean> getMsgGanjiList() {
		List<ItemBean> list = new ArrayList<ItemBean>();
		
		try {
			String urlStr = PropertiesUtil.ganjiSearchUrl;
			String htmlStr = HttpClientUtils.methodGet(httpClient, urlStr);
			Document doc = Jsoup.parse(htmlStr);
			//System.out.println(doc.text());
			
			Elements elements = doc.getElementsByClass("list-style1");
			//System.out.println(elements.text());
			Elements childs = elements.get(0).children();
			
			for(int i=0; i<childs.size(); i++){
				ItemBean b = new ItemBean();
				b.setPuid(childs.get(i).attr("id"));
				b.setMsg(childs.get(0).text());
				list.add(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
//	// 获取详细 短信里直接查看链接，所以不作为发送短信的内容了
//	public static ItemDetailBean getDetailMsgGanji(String puid) {
//		ItemDetailBean itemDetailBean = new ItemDetailBean();
//		try {
//			String urlStr = "http://sz.ganji.com/fang1/"+puid.split("-")[1]+"x.htm";
//			String htmlStr = HttpClientUtils.methodGet(httpClient, urlStr);
//			Document doc = Jsoup.parse(htmlStr);
//			
//			Elements elements = doc.getElementsByClass("talk-btn");
//			String phone = elements.get(0).attr("data-phone");
//			String name = elements.get(0).attr("data-username");
//			String basicInfo = doc.getElementsByClass("basic-info-ul").get(0).text();
//			String msg = doc.getElementsByClass("summary-cont").get(0).text();
//			
//			itemDetailBean.setBasicInfo(basicInfo);
//			itemDetailBean.setMsg(msg);
//			itemDetailBean.setName(name);
//			itemDetailBean.setPhone(phone);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return itemDetailBean;
//	}
}
