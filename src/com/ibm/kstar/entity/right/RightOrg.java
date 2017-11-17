package com.ibm.kstar.entity.right;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

//@Entity
//@Table(name = "view_right_org")
public class RightOrg implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "view_right_org_id_generator")
	@GenericGenerator(name = "view_right_org_id_generator", strategy = "uuid")
	@Column(name = "org_id")
	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
