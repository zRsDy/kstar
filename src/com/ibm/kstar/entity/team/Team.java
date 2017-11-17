package com.ibm.kstar.entity.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CRM_T_TEAM")
public class Team implements Serializable {

	private static final long serialVersionUID = 1l;
	
	@Id
    @GeneratedValue(generator = "crm_t_team_id_generator")
	@GenericGenerator(name="crm_t_team_id_generator", strategy="uuid")
    @Column(name = "ROW_ID")
    private String id;
    
	@Column(name = "POSITION_ID")
	private String positionId;
	
	@Column(name = "MASTER_EMPLOYEE_ID")
	private String masterEmployeeId;

	@Column(name = "BUSINESS_TYPE")
	private String businessType;
	
	@Column(name = "BUSINESS_Id")
	private String businessId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getMasterEmployeeId() {
		return masterEmployeeId;
	}

	public void setMasterEmployeeId(String masterEmployeeId) {
		this.masterEmployeeId = masterEmployeeId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	
}
