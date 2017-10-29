package com.ibm.kstar.entity.common.permission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "crm_t_biz_permission_rel")
public class BusinessPermissionRel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "crm_t_business_permission_rel_id_generator")
	@GenericGenerator(name = "crm_t_business_permission_rel_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;

	@Column(name = "C_BUSINESS_ID")
	private String businessId;

	@Column(name = "C_ORG_ID")
	private String orgId;

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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
