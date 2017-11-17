package com.ibm.kstar.entity.common.outteam;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * (CRM_T_CUSTOM_SALES_TEAM_OUT)
 * 
 * @author bianj
 * @version 1.0.0 2016-12-29
 */
@Entity
@Table(name = "crm_t_sales_team_out")
public class SalesTeamOut implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "sales_team_out_pid_generator")
	@GenericGenerator(name="sales_team_out_pid_generator", strategy="uuid")
    private String id;
    
    /** 业务主键 */
    @Column(name = "c_system_pid", nullable = false, length = 32)
    private String systemId;
    
    /** 业务CODE */
    @Column(name = "c_system_code", nullable = false, length = 32)
    private String systemCode;
    
    /** 公司名称 */
    @Column(name = "c_mark_comp", nullable = true, length = 32)
    private String markComp;
    
    /** 关系 */
    @Column(name = "c_mark_rela", nullable = false, length = 100)
    private String markRela;
    
    /** 人员姓名 */
    @Column(name = "c_mark_name", nullable = true, length = 100)
    private String markName;
    
    /** 主要联系人 */
    @Column(name = "c_mark_contact_person", nullable = true, length = 100)
    private String markContactPerson;
    
    /** 角色 */
    @Column(name = "c_mark_role", nullable = true, length = 100)
    private String markRole;
    
    /** 权限 */
    @Column(name = "c_mark_auth", nullable = true, length = 100)
    private String markAuth;
    
    /** 有效日期从 */
    @Column(name = "c_mark_valid_from", nullable = true, length = 100)
    private String markValidFrom;
    
    /** 有效日期至 */
    @Column(name = "c_mark_valid_to", nullable = true, length = 100)
    private String markValidTo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getMarkComp() {
		return markComp;
	}

	public void setMarkComp(String markComp) {
		this.markComp = markComp;
	}

	public String getMarkRela() {
		return markRela;
	}

	public void setMarkRela(String markRela) {
		this.markRela = markRela;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public String getMarkContactPerson() {
		return markContactPerson;
	}

	public void setMarkContactPerson(String markContactPerson) {
		this.markContactPerson = markContactPerson;
	}

	public String getMarkRole() {
		return markRole;
	}

	public void setMarkRole(String markRole) {
		this.markRole = markRole;
	}

	public String getMarkAuth() {
		return markAuth;
	}

	public void setMarkAuth(String markAuth) {
		this.markAuth = markAuth;
	}

	public String getMarkValidFrom() {
		return markValidFrom;
	}

	public void setMarkValidFrom(String markValidFrom) {
		this.markValidFrom = markValidFrom;
	}

	public String getMarkValidTo() {
		return markValidTo;
	}

	public void setMarkValidTo(String markValidTo) {
		this.markValidTo = markValidTo;
	}

}