package com.jacky.thread;

import java.util.ArrayList;
import java.util.List;

import com.jacky.feixin.service.FeixinService;
import com.jacky.ganji.bean.ItemBean;
import com.jacky.ganji.service.GanjiService;
import com.jacky.util.PropertiesUtil;

public class Main extends Thread {
	// 放历史puid记录，第一次直接放在这里
	private static List<String> puidList = new ArrayList<String>();
	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}
	
	@Override
	public void run(){

		try {
			// 线程开始发送一条短信到手机
			String receivers = PropertiesUtil.receivers;
			// 接收者
			System.out.println("接收者："+receivers);
			String[] receiverArr = receivers.split(",");
			for(int i=0; i<receiverArr.length; i++){
				System.out.println(i);
				FeixinService.sendMsg(PropertiesUtil.feixinPhone, PropertiesUtil.feixinPwd, receiverArr[i], "实时监测程序已开开启");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 监测查询次数	
		int num = 0;
		while(true){
			try {
				System.out.println("--------------第 "+(++num)+" 次------------");
				ItemBean tempItemBean = null;
				List<ItemBean> list = GanjiService.getMsgGanjiList();
				System.out.println("查询条数："+list.size());
				if(list != null && list.size()>0){
					// 如果是第一次puidList没有值
					if(puidList.size()>0){
						for(int i=0; i<list.size(); i++){
							ItemBean itemBean = list.get(i);
							if(!puidList.contains(itemBean.getPuid())){
								puidList.add(itemBean.getPuid());
								// 不是最后一个   说明不是旧信息
								if(i<list.size()-1){
									tempItemBean = itemBean;
								}
							}
						}
					}else{
						for(ItemBean itemBean: list){
							puidList.add(itemBean.getPuid());
						}
					}
				}
				if(tempItemBean!=null){
					// 发送短信接口
					String content = tempItemBean.getMsg();
					content += "链接: http://sz.ganji.com/fang1/"+tempItemBean.getPuid().split("-")[1]+"x.htm";

					System.out.println("-----------------------------");
					String receivers = PropertiesUtil.receivers;
					String[] receiverArr = receivers.split(",");
					for(int i=0; i<receiverArr.length; i++){
						FeixinService.sendMsg(PropertiesUtil.feixinPhone, PropertiesUtil.feixinPwd, receiverArr[i], content);
					}
					System.out.println("发送短信内容："+content);
					System.out.println("-----------------------------");
					
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----------------------------");
				String content = "程序抛异常："+e.getMessage();
				// 线程开始发送一条短信到手机
				String receivers = PropertiesUtil.receivers;
				String[] receiverArr = receivers.split(",");
				for(int i=0; i<receiverArr.length; i++){
					try {
						FeixinService.sendMsg(PropertiesUtil.feixinPhone, PropertiesUtil.feixinPwd, receiverArr[i], content);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				System.out.println("发送短信内容："+content);
				System.out.println("-----------------------------");
			}
		}
	}
}
