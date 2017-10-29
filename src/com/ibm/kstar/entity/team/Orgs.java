package com.ibm.kstar.entity.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "crm_t_orgs")
public class Orgs implements Serializable {

	private static final long serialVersionUID = 1l;
	
	@Id
    @GeneratedValue(generator = "crm_t_team_id_generator")
	@GenericGenerator(name="crm_t_team_id_generator", strategy="uuid")
    @Column(name = "ROW_ID")
    private String id;
	
	@Column(name = "business_Id")
	private String businessId;
	
	@Column(name = "BUSINESS_TYPE")
	private String businessType;
	
	@Column(name = "ORG_ID")
	private String orgId;
	
	@Column(name = "PRIMARY_ORG_ID")
	private String primaryOrgId;
	
	@Column(name = "PRIMARY_ORG_FLAG")
	private String primaryOrgFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPrimaryOrgId() {
		return primaryOrgId;
	}

	public void setPrimaryOrgId(String primaryOrgId) {
		this.primaryOrgId = primaryOrgId;
	}

	public String getPrimaryOrgFlag() {
		return primaryOrgFlag;
	}

	public void setPrimaryOrgFlag(String primaryOrgFlag) {
		this.primaryOrgFlag = primaryOrgFlag;
	}

}
