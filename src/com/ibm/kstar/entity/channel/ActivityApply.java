package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 市场活动申请表(crm_t_activity_apply)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-06
 */
@Entity
@Table(name = "crm_t_activity_apply")
public class ActivityApply extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 5653583009226165110L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "activity_apply_c_pid_generator")
   	@GenericGenerator(name="activity_apply_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_code")
    private String applyCode;
    
    /** 业务类型 */
    @Column(name = "c_business_type")
    private String businessType;
    
    /** 申请单位 */
    @Column(name = "c_apply_unit")
    private String applyUnit;

    /** 是否经销商*/
    @Column(name = "c_dealer")
    private String dealer;

	/** 活动类型 */
    @Column(name = "c_activity_type")
    private String activityType;
    
    /** 行业类型 */
    @Column(name = "c_industry_type")
    private String industryType;
    
    /** 活动目的 */
    @Column(name = "c_activity_purpose")
    private String activityPurpose;
    
    /** 状态*/
    @Column(name = "c_status")
    private String status;

	/** 货币 */
	@Column(name = "c_currency")
	private String currency;
	
    /** 预估费用 */
    @Column(name = "c_estimated_expense")
    private BigDecimal estimatedExpense;
	
    /** 应收费用 */
    @Transient
    private BigDecimal receivableExpense;
    
    /** 申请日期 */
    @Column(name = "c_apply_date")
    private Date applyDate;
    
    /** 预计开始日期 */
    @Column(name = "c_estimated_start_date")
    private Date estimatedStartDate;
    
    /** 关闭日期 */
    @Column(name = "c_close_date")
    private Date closeDate;
    
    /** 组织 */
    @Column(name = "c_organization")
    private String organization;

	/** 申请人*/
    @Column(name = "c_applier")
    private String applier;
    
    @Transient
    private String applierName;
    
    /** 申请人电话 */
    @Column(name = "c_applier_phone")
    private String applierPhone;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}

	public String getApplyUnitName() {
		return this.getLovName(applyUnit);
	}

	public String getDealer() {
		return dealer;
	}
	
	public String getDealerCode() {
		return this.getLovCode(dealer);
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getDealerName() {
		return this.getLovName(dealer);
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityTypeName() {
		return this.getLovName(activityType);
	}
	
	public String getIndustryType() {
		return industryType;
	}
	
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	
	public String getIndustryTypeName() {
		return this.getLovName(industryType);
	}
	
	public String getActivityPurpose() {
		return activityPurpose;
	}

	public void setActivityPurpose(String activityPurpose) {
		this.activityPurpose = activityPurpose;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusCode() {
		return this.getLovCode(status);
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
	
	public BigDecimal getEstimatedExpense() {
		return estimatedExpense;
	}

	public void setEstimatedExpense(BigDecimal estimatedExpense) {
		this.estimatedExpense = estimatedExpense;
	}

	public BigDecimal getReceivableExpense() {
		return receivableExpense;
	}

	public void setReceivableExpense(BigDecimal receivableExpense) {
		this.receivableExpense = receivableExpense;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getEstimatedStartDate() {
		return estimatedStartDate;
	}

	public void setEstimatedStartDate(Date estimatedStartDate) {
		this.estimatedStartDate = estimatedStartDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
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
	
	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApplierPhone() {
		return applierPhone;
	}

	public void setApplierPhone(String applierPhone) {
		this.applierPhone = applierPhone;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}