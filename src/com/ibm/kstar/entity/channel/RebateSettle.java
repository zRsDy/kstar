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
 * 返利结算表(crm_t_rebate_settle)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-15
 */
@Entity
@Table(name = "crm_t_rebate_settle")
public class RebateSettle extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -3857737179617518709L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "rebate_settle_c_pid_generator")
   	@GenericGenerator(name="rebate_settle_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 抵扣单号 */
    @Column(name = "c_deduction_code")
    private String deductionCode;
    
    /** 客户 */
    @Column(name = "c_custom_id")
    private String customId;
    
    @Transient
    private String customName;
    
    /** 抵扣金额 */
    @Column(name = "c_deduction_money")
    private BigDecimal deductionMoney;
    
    /** 返利方式*/
    @Column(name = "c_rebate_mode")
    private String rebateMode;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    /** 货币 */
	@Column(name = "c_currency")
	private String currency;
    
    /** 创建日期 */
    @Column(name = "c_create_date")
    private Date createDate;
    
    /** 确认日期 */
    @Column(name = "c_check_date")
    private Date checkDate;
    
    /** 组织 */
    @Column(name = "c_organization")
    private String organization;
  
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeductionCode() {
		return deductionCode;
	}

	public void setDeductionCode(String deductionCode) {
		this.deductionCode = deductionCode;
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

	public BigDecimal getDeductionMoney() {
		return deductionMoney;
	}

	public void setDeductionMoney(BigDecimal deductionMoney) {
		this.deductionMoney = deductionMoney;
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
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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
	
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}