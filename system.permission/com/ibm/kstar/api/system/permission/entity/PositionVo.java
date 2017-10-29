package com.ibm.kstar.api.system.permission.entity;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public class PositionVo extends LovMember{
	
	private static final long serialVersionUID = 1L;

	private String parentOrgId;

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	
}
