package org.xsnake.web.dao;

import java.io.Serializable;

import org.xsnake.web.utils.StringUtil;

public class QueryCondition implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		String str = StringUtil.toJson(this).trim(); 
		str = str.replaceAll("\"", "") .replaceAll(":", "") .replaceAll(":", "").replaceAll(" ", "");
		return str;
	}
	
	public QueryCondition(Object... objects){
		this.args = objects;
	}
	
	private Object[] args;

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
