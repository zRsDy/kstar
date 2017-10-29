package com.ibm.kstar.entity.support.deposit;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;

/**
 * (CRM_T_DEPOSIT_APPLY)
 * 
 * @author bianj
 * @version 1.0.0 2017-02-13
 */
@Entity
@Table(name = "crm_t_deposit_apply")
public class DepositApply implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -1146650820694275877L;
    
    /** ID */
    @Id
    @GeneratedValue(generator = "deposit_apply_id_generator")
	@GenericGenerator(name="deposit_apply_id_generator", strategy="uuid")
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    private String id;
    
    /** 申请人 */
    @Column(name = "APPLIER_ID", nullable = true, length = 32)
    private String applier;
    
    @Transient
	private String applierNo;
    
    public String getApplierNo() {
    	Employee lov =  ((Employee)CacheData.getInstance().get(applier));
		return lov==null? null : lov.getNo();
	}
    
    @Transient
	private String applierName;
    
    public String getApplierName() {
    	Employee lov =  ((Employee)CacheData.getInstance().get(applier));
		return lov==null? null : lov.getName();
	}
    
    /** 申请人岗位 */
    @Column(name = "APPLIER_POS_ID", nullable = true, length = 32)
    private String applierPos;
    
    /** 申请人组织 */
    @Column(name = "APPLIER_ORG_ID", nullable = true, length = 32)
    private String applierOrg;
    
    @Transient
	private String applierOrgName;
    
    public String getApplierOrgName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(applierOrg));
		return lov==null? null : lov.getNamePath();
	}
    
    /** 金额 */
    @Column(name = "c_amount", nullable = true, length = 20)
    private String amount;
    
    /** 申请类型 */
    @Column(name = "c_apply_type", nullable = true, length = 32)
    private String applyType;
    
    /** 资金性质 */
    @Column(name = "c_nature", nullable = true, length = 32)
    private String nature;
    
    
    /** 收款单位名称 */
    @Column(name = "c_payment_company", nullable = true, length = 32)
    private String paymentCompany;
    
    /** 商机名称 */
    @Column(name = "c_opportunity", nullable = true, length = 32)
    private String opportunity;
    
    /** 申请编号 */
    @Column(name = "c_apply_code", nullable = true, length = 32)
    private String applyCode;
    
    /** 收款单位银行名称 */
    @Column(name = "c_payment_bank", nullable = true, length = 32)
    private String paymentBank;
    
    /** 报价单 */
    @Column(name = "c_quotation", nullable = true, length = 32)
    private String quotation;
    
    /** 申请时间 */
    @Column(name = "c_apply_date", nullable = true)
    private String applyDate;
    
    /** 收款单位银行账号 */
    @Column(name = "c_payment_account", nullable = true, length = 32)
    private String paymentAccount;
    
    /** 合同 */
    @Column(name = "c_contract", nullable = true, length = 32)
    private String contract;
    
    /** 拟回款日期 */
    @Column(name = "dt_payment_date_return", nullable = true)
    private Date paymentDateReturn;
    
    /** 币种 */
    @Column(name = "c_currency", nullable = true, length = 32)
    private String currency;
    
    @Transient
	private String currencyName;

	public String getCurrencyName() {

		LovMember lov = (LovMember) CacheData.getInstance().get(currency);
		return lov == null ? null : lov.getName();
	}
    
    /** 状态 */
    @Column(name = "c_status", nullable = true, length = 32)
    private String status;
    
    @Transient
	private String statusGrid;
    
    public Object getStatusGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(status);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setStatusGrid(String statusGrid) {
		this.statusGrid = statusGrid;
	}
    
    /** 拟付款日期 */
    @Column(name = "dt_payment_date", nullable = true)
    private Date paymentDate;
    
    /** 备注 */
    @Column(name = "c_comment", nullable = true, length = 200)
    private String comment;
    
    /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierPos() {
		return applierPos;
	}

	public void setApplierPos(String applierPos) {
		this.applierPos = applierPos;
	}

	public String getApplierOrg() {
		return applierOrg;
	}

	public void setApplierOrg(String applierOrg) {
		this.applierOrg = applierOrg;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getPaymentCompany() {
		return paymentCompany;
	}

	public void setPaymentCompany(String paymentCompany) {
		this.paymentCompany = paymentCompany;
	}

	public String getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(String opportunity) {
		this.opportunity = opportunity;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getQuotation() {
		return quotation;
	}

	public void setQuotation(String quotation) {
		this.quotation = quotation;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public Date getPaymentDateReturn() {
		return paymentDateReturn;
	}

	public void setPaymentDateReturn(Date paymentDateReturn) {
		this.paymentDateReturn = paymentDateReturn;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedPosId() {
		return createdPosId;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public void setApplierOrgName(String applierOrgName) {
		this.applierOrgName = applierOrgName;
	}

	public void setApplierNo(String applierNo) {
		this.applierNo = applierNo;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

}