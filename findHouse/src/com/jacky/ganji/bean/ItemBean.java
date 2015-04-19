package com.jacky.ganji.bean;

/**
 * 赶集查询列表每一项
 * @author Jacky
 */
public class ItemBean {
	// 唯一区分每一条租房信息的ID
	private String puid;
	// 简要租房信息
	private String msg;
	public String getPuid() {
		return puid;
	}
	public void setPuid(String puid) {
		this.puid = puid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
