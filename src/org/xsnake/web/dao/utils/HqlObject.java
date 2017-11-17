package org.xsnake.web.dao.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HqlObject implements Serializable{

	private static final long serialVersionUID = 1L;

	String hql;
	
	List<Object> args = new ArrayList<Object>();
	
	public HqlObject(String hql,List<Object> args){
		this.hql = hql;
		this.args=args;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public void addArgs(Object as){
		args.add(as);
	}
	
	public void addArgs(Object[] as){
		for(Object obj : as){
			args.add(obj);
		}
	}
	
	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public Object[] getArgs(){
		if(args == null){
			return null;
		}
		return args.toArray();
	}
	
	
}
