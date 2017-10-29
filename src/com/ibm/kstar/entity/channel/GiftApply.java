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
 * 资料与礼品申请表(crm_t_gift_apply)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-04
 */
@Entity
@Table(name = "crm_t_gift_apply")
public class GiftApply extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 7266198772747377964L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "gift_apply_c_pid_generator")
   	@GenericGenerator(name="gift_apply_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_code", nullable = false)
    private String applyCode;
    
    /** 申请单位 */
    @Column(name = "c_apply_unit")
    private String applyUnit;
    
    /** 客户 */
    @Column(name = "c_custom")
    private String custom;
    
    /** 是否经销商*/
    @Column(name = "c_dealer")
    private String dealer;
    
    /** 行业类型 */
    @Column(name = "c_industry_type")
    private String industryType;
    
    /** 用途 */
    @Column(name = "c_purpose")
    private String purpose;
    
    /** 状态*/
    @Column(name = "c_status")
    private String status;
    
    /** 货币 */
	@Column(name = "c_currency")
	private String currency;
	
	/** 可用额度 */
    @Column(name = "c_available_limit")
    private BigDecimal availableLimit;
    
    /** 申请金额 */
    @Transient
    private BigDecimal applyAmount;
    
    /** 申请日期 */
    @Column(name = "c_apply_date")
    private Date applyDate;
    
    /** 预计需求日期 */
    @Column(name = "c_estimated_demand_date")
    private Date estimatedDemandDate;
    
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

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}

	public String getApplyUnitName() {
		return this.getLovName(applyUnit);
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
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
	
	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getIndustryTypeName() {
		return this.getLovName(industryType);
	}
	
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
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
	
	public BigDecimal getAvailableLimit() {
		return availableLimit;
	}

	public void setAvailableLimit(BigDecimal availableLimit) {
		this.availableLimit = availableLimit;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getEstimatedDemandDate() {
		return estimatedDemandDate;
	}

	public void setEstimatedDemandDate(Date estimatedDemandDate) {
		this.estimatedDemandDate = estimatedDemandDate;
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