package com.ibm.kstar.entity.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 对账单头表(CRM_T_STATEMENT_MASTER)
 * 
 * @author bianj
 * @version 1.0.0 2016-12-27
 */
@Entity
@Table(name = "crm_t_statement_master")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_STATEMENT)
public class StatementMaster extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "statement_m_pid_generator")
   	@GenericGenerator(name="statement_m_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 对账申请编号 */
    @Column(name = "c_statement_code")
    private String statementCode;
    
    /** 申请人 */
    @Column(name = "c_proposer_id")
    private String proposerId;
    
    /** 申请人名称 */
    @Column(name = "c_proposer_name")
    private String proposerName;
    
    /** 期初余额 */
    @Column(name = "n_init_balance")
    private BigDecimal initBalance;
    
    /** 对账期间 */
    @Column(name = "c_period")
    private String period;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    /** 是否发布 1 是， 0 否*/
   	@Column(name = "c_is_publish")
   	private String isPublish;
       
    /** 申请日期 */
    @Column(name = "dt_apply_date")
    private Date applyDate;
    
    /** 本期累计 */
    @Column(name = "n_current_amount")
    private BigDecimal currentAmount;

    /** 客户ID */
	@Column(name = "c_customer_id")
	private String customerId;

	/** 客户编号 */
	@Column(name = "c_customer_code")
	private String customerCode;

	/** 客户名称 */
	@Column(name = "c_customer_name")
	private String customerName;
	
	/** 对账开始日期 */
	@Column(name = "dt_statement_date_begin" )
	private Date statementDateBegin;
	
	/** 对账结束时间 */
	@Column(name = "dt_statement_date_end")
	private Date statementDateEnd;
	
	/** 仓库已是否过账 */
	@Column(name = "c_is_post_account")
	private String isPostAccount;
	
	/** 状态显示名称 */
	@Transient
	private String statusLable;

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getStatementCode() {
		return statementCode;
	}

	public void setStatementCode(String statementCode) {
		this.statementCode = statementCode;
	}

	public String getProposerId() {
		return proposerId;
	}

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public BigDecimal getInitBalance() {
		return initBalance;
	}

	public void setInitBalance(BigDecimal initBalance) {
		this.initBalance = initBalance;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getStatementDateBegin() {
		return statementDateBegin;
	}

	public void setStatementDateBegin(Date statementDateBegin) {
		this.statementDateBegin = statementDateBegin;
	}

	public Date getStatementDateEnd() {
		return statementDateEnd;
	}

	public void setStatementDateEnd(Date statementDateEnd) {
		this.statementDateEnd = statementDateEnd;
	}

	public String getIsPostAccount() {
		return isPostAccount;
	}

	public void setIsPostAccount(String isPostAccount) {
		this.isPostAccount = isPostAccount;
	}

	public String getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDER_CONTROL_STATUS", status);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
	}
    
    
}