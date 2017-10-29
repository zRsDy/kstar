package com.ibm.kstar.entity.channel;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.custom.CustomInfo;

/**
 * 返利政策表(crm_t_rebate_policy)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-14
 */
@Entity
@Table(name = "crm_t_rebate_policy")
public class RebatePolicy extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 7901375195740696449L;
	
	public RebatePolicy(){
		
	}

	public RebatePolicy(RebatePolicy policy,CustomInfo custom) {
		super();
		this.id = policy.id;
		this.policyCode = policy.policyCode;
		this.policyName = policy.policyName;
		this.contact = policy.contact;
		this.contactPhone = policy.contactPhone;
		this.rebateType = policy.rebateType;
		this.status = policy.status;
		this.currency = policy.currency;
		this.startDate = policy.startDate;
		this.endDate = policy.endDate;
		this.organization = policy.organization;
		this.agreeCode = policy.agreeCode;
		this.contractCode = policy.contractCode;
		this.salesman = policy.salesman;
		this.authorityZone = policy.authorityZone;
		this.authorityProduct = policy.authorityProduct;
		this.agentLevel = policy.agentLevel;
		this.annualTask = policy.annualTask;
		this.referenceType = policy.referenceType;
		this.referenceDate = policy.referenceDate;
		this.rebateMode = policy.rebateMode;
		this.accountMode = policy.accountMode;
		this.eliminateSpecial = policy.eliminateSpecial;
		this.eliminateNonStandard = policy.eliminateNonStandard;
		this.explain = policy.explain;
		this.customId = custom.getId();
		this.customName = custom.getCustomFullName();
	}

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "rebate_policy_c_pid_generator")
   	@GenericGenerator(name="rebate_policy_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 政策编号 */
    @Column(name = "c_policy_code")
    private String policyCode;

    /** 政策名称 */
    @Column(name = "c_policy_name")
    private String policyName;

    /** 客户id */
    @Column(name = "c_custom_id")
    private String customId;
    
    @Transient
    private String customName;
    
    /** 联系人*/
    @Column(name = "c_contact")
    private String contact;
    
    /** 联系电话 */
    @Column(name = "c_contact_phone")
    private String contactPhone;
    
    /** 返利类型 */
    @Column(name = "c_rebate_type")
    private String rebateType;
    
    /** 状态*/
    @Column(name = "c_status")
    private String status;
    
    /** 货币 */
	@Column(name = "c_currency")
	private String currency;
    
    /** 起始日期 */
    @Column(name = "c_start_date")
    private Date startDate;
    
    /** 结束日期 */
    @Column(name = "c_end_date")
    private Date endDate;

    /** 组织 */
    @Column(name = "c_organization")
    private String organization;
    
    /** 协议编号 */
	@Column(name = "c_agree_code")
	private String agreeCode;
	
    /** 合同编号 */
	@Column(name = "c_contract_code")
	private String contractCode;
    
    /** 销售员 */
	@Column(name = "c_salesman")
	private String salesman;
	
    /** 授权区域 */
	@Column(name = "c_authority_zone")
	private String authorityZone;
    
    /** 授权产品*/
	@Column(name = "c_authority_product")
	private String authorityProduct;
    
    /** 代理商级别*/
	@Column(name = "c_agent_level")
	private String agentLevel;
    
    /** 年任务量*/
	@Column(name = "c_annual_task")
	private Double annualTask;
	
    /** 基准类型*/
    @Column(name = "c_reference_type")
    private String referenceType;
	
    /** 基准日期*/
    @Column(name = "c_reference_Date")
    private String referenceDate;
	
    /** 返利方式*/
    @Column(name = "c_rebate_mode")
    private String rebateMode;
	
    /** 账期方式*/
    @Column(name = "c_account_mode")
    private String accountMode;
    
    /** 剔除特价销售*/
    @Column(name = "c_eliminate_special")
    private String eliminateSpecial;
    
    /** 剔除非标品销售*/
    @Column(name = "c_eliminate_non_standard")
    private String eliminateNonStandard;

    /** 说明*/
    @Column(name = "c_explain")
    private String explain;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPolicyCode() {
		return policyCode;
	}

	public void setPolicyCode(String policyCode) {
		this.policyCode = policyCode;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getRebateType() {
		return rebateType;
	}

	public void setRebateType(String rebateType) {
		this.rebateType = rebateType;
	}
	
	public String getRebateTypeCode() {
		return this.getLovCode(rebateType);
	}

	public String getRebateTypeName() {
		return this.getLovName(rebateType);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusName() {
		return this.getLovName(status);
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrencyName() {
		return this.getLovName(currency);
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationName() {
		return this.getLovName(organization);
	}
	
	public String getAgreeCode() {
		return agreeCode;
	}

	public void setAgreeCode(String agreeCode) {
		this.agreeCode = agreeCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getAuthorityZone() {
		return authorityZone;
	}

	public void setAuthorityZone(String authorityZone) {
		this.authorityZone = authorityZone;
	}

	public String getAuthorityProduct() {
		return authorityProduct;
	}

	public void setAuthorityProduct(String authorityProduct) {
		this.authorityProduct = authorityProduct;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getAgentLevelName() {
		return this.getLovName(agentLevel);
	}
	
	public Double getAnnualTask() {
		return annualTask;
	}

	public void setAnnualTask(Double annualTask) {
		this.annualTask = annualTask;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	
	public String getReferenceTypeCode() {
		return this.getLovCode(referenceType);
	}

	public String getReferenceTypeName() {
		return this.getLovName(referenceType);
	}
	
	public String getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
	}
	
	public String getReferenceDateName() {
		return this.getLovName(referenceDate);
	}
	
	public String getRebateMode() {
		return rebateMode;
	}

	public void setRebateMode(String rebateMode) {
		this.rebateMode = rebateMode;
	}
	
	public String getRebateModeName() {
		return this.getLovName(rebateMode);
	}
	
	public String getAccountMode() {
		return accountMode;
	}

	public void setAccountMode(String accountMode) {
		this.accountMode = accountMode;
	}

	public String getEliminateSpecial() {
		return eliminateSpecial;
	}

	public void setEliminateSpecial(String eliminateSpecial) {
		this.eliminateSpecial = eliminateSpecial;
	}

	public String getEliminateNonStandard() {
		return eliminateNonStandard;
	}

	public void setEliminateNonStandard(String eliminateNonStandard) {
		this.eliminateNonStandard = eliminateNonStandard;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
    
}