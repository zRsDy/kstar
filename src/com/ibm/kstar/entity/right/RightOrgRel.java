package com.ibm.kstar.entity.right;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

//@Entity
//@Table(name = "view_right_org_rel")
public class RightOrgRel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "view_right_org_id_generator")
	@GenericGenerator(name = "view_right_org_id_generator", strategy = "uuid")
	@Column(name = "row_id")
	private String id;
	
	@Column(name = "org_id")
	private String orgId;
	
	@Column(name = "child_org_id")
	private String childOrgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getChildOrgId() {
		return childOrgId;
	}

	public void setChildOrgId(String childOrgId) {
		this.childOrgId = childOrgId;
	}
	
	
}
