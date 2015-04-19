package com.jacky.feixin.bean;

import java.util.Map;
/**
 * 微信通过电话或昵称查询结果
 * @author Jacky
 *
 */
public class SearchResultBean {
	// 记录数量
	private int total;
	// 每一个map封装着一条记录的json信息
	private Map<String, Object>[] contacts;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Map<String, Object>[] getContacts() {
		return contacts;
	}
	public void setContacts(Map<String, Object>[] contacts) {
		this.contacts = contacts;
	}
}
