package com.ibm.kstar.api.system.permission;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public class OrgPositionVo {

	LovMember org;
	
	LovMember position;
	
	String id;
	
	public OrgPositionVo(){}
	
	public OrgPositionVo(LovMember org,LovMember position){
		this.org = org;
		this.position = position;
		id = org.getId();
	}

	public LovMember getOrg() {
		return org;
	}

	public void setOrg(LovMember org) {
		this.org = org;
	}

	public LovMember getPosition() {
		return position;
	}

	public void setPosition(LovMember position) {
		this.position = position;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
