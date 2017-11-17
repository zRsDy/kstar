package com.ibm.kstar.entity.order;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 
 * ClassName: AccountsMaster 账期申请主表 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年1月4日 上午9:25:38 <br/>
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_accounts_master")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_ACCOUNTS)
public class AccountsMaster extends BaseEntity implements java.io.Serializable {
	
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "accounts_m_id_generator")
	@GenericGenerator(name="accounts_m_id_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 账期申请编号 */
    @Column(name = "c_accounts_code")
    private String accountsCode;
    
    /** 申请人ID*/
    @Column(name = "c_proposer_id")
    private String proposerId;
    
    /** 申请人名称*/
    @Column(name = "c_proposer_name")
    private String proposerName;
    
    /** 申请单位ID*/
    @Column(name = "c_company_id")
    private String companyId;
    
    /** 申请单位名称*/
    @Column(name = "c_company_name")
    private String companyName;
    
    /** 协议账期 */
    @Column(name = "c_period_agt")
    private String periodAgt;
    
    /** 账期延期 */
    @Column(name = "c_period_delay")
    private String periodDelay;
    
    /** 申请日期 */
    @Column(name = "dt_apply_date")
    private Date applyDate;
    
    /** 逾期金额 */
    @Column(name = "n_overdue_amount")
    private BigDecimal overdueAmount;
    
    /** 延期金额 */
    @Column(name = "n_delay_amount")
    private BigDecimal delayAmount;
    
    /** 申请原因 */
    @Column(name = "c_apply_reason")
    private String applyReason;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
   
    /** 控制状态 */
	@Column(name = "c_control_status")
	private String controlStatus;
    
    @Transient
	private List<Map<Object, Object>> detailList = new ArrayList<Map<Object,Object>>();
    
    @Transient
    private String statusLable;
    
    @Transient
    private String controlStatusLable;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getAccountsCode() {
		return accountsCode;
	}

	public void setAccountsCode(String accountsCode) {
		this.accountsCode = accountsCode;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPeriodAgt() {
		return periodAgt;
	}

	public void setPeriodAgt(String periodAgt) {
		this.periodAgt = periodAgt;
	}

	public String getPeriodDelay() {
		return periodDelay;
	}

	public void setPeriodDelay(String periodDelay) {
		this.periodDelay = periodDelay;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public BigDecimal getDelayAmount() {
		return delayAmount;
	}

	public void setDelayAmount(BigDecimal delayAmount) {
		this.delayAmount = delayAmount;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Map<Object, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<Object, Object>> detailList) {
		this.detailList = detailList;
	}

	public String getControlStatus() {
		return controlStatus;
	}

	public void setControlStatus(String controlStatus) {
		this.controlStatus = controlStatus;
	}

	public String getStatusLable() {
		return getLovMember("ACCOUNTS_STATUS", status).getName();
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
	}

	public String getControlStatusLable() {
		return getLovMember("ORDER_CONTROL_STATUS", controlStatus).getName();
	}

	public void setControlStatusLable(String controlStatusLable) {
		this.controlStatusLable = controlStatusLable;
	}
   
}