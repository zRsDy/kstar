package com.ibm.kstar.api.system.permission.entity;

import java.io.Serializable;

import com.ibm.kstar.api.system.lov.entity.LovMember;

//转义类,将LOV值转义为岗位
public class Position implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private LovMember position;

	public Position(LovMember position){
		this.position = position;
	}

	public Position() {
	}

	public String getId(){
		return position.getId();
	}
	
	public String getName(){
		return position.getName();
	}
	
	public String getCode(){
		return position.getCode();
	}
	
	public String getNamePath(){
		return position.getNamePath();
	}
	
	public String getOrgId(){
		return position.getParentId();
	}
	
	public String getPositionInOrgId(){
		return position.getOptTxt1();
	}
	
	public String getOptTxt1(){
		return position.getOptTxt1();
	}
	
	public String getOptTxt2(){
		return position.getOptTxt2();
	}
	
	public String getOptTxt3(){
		return position.getOptTxt3();
	}
	
	public String getOptTxt4(){
		return position.getOptTxt4();
	}
	
	public String getOptTxt5(){
		return position.getOptTxt5();
	}
}
