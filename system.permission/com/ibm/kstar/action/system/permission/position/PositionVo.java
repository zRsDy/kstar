package com.ibm.kstar.action.system.permission.position;

import java.io.Serializable;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

public class PositionVo implements Serializable{

	private static final long serialVersionUID = 1L;

	String id;
	
	LovMember position;
	
	LovMember org;
	
	LovMember approve;
	
	public PositionVo(){}
	
	public PositionVo(LovMember position,LovMember org,LovMember approve){
		id = position.getId();
		this.position = position;
		this.org = new LovMember();
		BeanUtils.copyProperties(org, this.org); ;
		this.approve = approve;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LovMember getPosition() {
		return position;
	}

	public void setPosition(LovMember position) {
		this.position = position;
	}

	public LovMember getOrg() {
		return org;
	}

	public void setOrg(LovMember org) {
		this.org = org;
	}
	
	public String getPositionType(){
		LovMember lov = (LovMember) CacheData.getInstance().getMember("POSITION_TYPE",position.getOptTxt5());
		if(lov !=null){
			return lov.getName();
		}
		return null;
	}
	
	public String getOrgType(){
		LovMember lov = (LovMember) CacheData.getInstance().getMember("ORG_TYPE",org.getOptTxt3());
		if(lov !=null){
			return lov.getName();
		}
		return null;
	}

	public LovMember getApprove() {
		return approve;
	}

	public void setApprove(LovMember approve) {
		this.approve = approve;
	}
	
	
	
}
