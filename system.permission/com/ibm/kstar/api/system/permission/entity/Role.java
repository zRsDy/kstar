package com.ibm.kstar.api.system.permission.entity;

import java.io.Serializable;

import com.ibm.kstar.api.system.lov.entity.LovMember;

//转义类,将LOV值转义为角色
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private LovMember role;

	public Role(LovMember role){
		this.role = role;
	}
	
	public String getId(){
		return role.getId();
	}
	
	public String getName(){
		return role.getName();
	}
	
	public String getCode(){
		return role.getCode();
	}
	
	public String getNamePath(){
		return role.getNamePath();
	}
}
