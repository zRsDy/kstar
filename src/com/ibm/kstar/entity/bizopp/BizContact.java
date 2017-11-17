package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * @author Wutw  2017-2-26
 * 
 */
/**
 * @author gaoyuliang
 *
 */
@Entity
@Table(name = "CRM_T_BIZ_CHAIN_CONTACT")
public class BizContact implements Serializable {
	
	/** 版本号 */
    private static final long serialVersionUID = -2264559523760712020L;
	
	@Id
	@GeneratedValue(generator = "bizChainContact_generator")
	@GenericGenerator(name="bizChainContact_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
    
    /** 角色 */
    @Column(name = "c_role", nullable = true, length = 10)
    private String role;
    
    /** 姓名 */
    @Column(name = "c_name", nullable = true, length = 20)
    private String name;
    
    /** 职务 */
    @Column(name = "c_duty", nullable = true, length = 20)
    private String duty;
    
    /** 联系电话 */
    @Column(name = "c_phonenumber", nullable = true, length = 20)
    private String phonenumber;
    
    /** 决策权重 */
    @Column(name = "c_decision_weight", nullable = true, length = 20)
    private String decisionWeight;
    
    /** 对我司支持程度 */
    @Column(name = "c_support_level", nullable = true, length = 20)
    private String supportLevel;
    
    /** 商机组织ID */
    @Column(name = "c_biz_org_id", nullable = true, length = 32)
    private String bizOrgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getDecisionWeight() {
		return decisionWeight;
	}

	public void setDecisionWeight(String decisionWeight) {
		this.decisionWeight = decisionWeight;
	}

	public String getSupportLevel() {
		return supportLevel;
	}

	public void setSupportLevel(String supportLevel) {
		this.supportLevel = supportLevel;
	}

	public String getBizOrgId() {
		return bizOrgId;
	}

	public void setBizOrgId(String bizOrgId) {
		this.bizOrgId = bizOrgId;
	}
}
