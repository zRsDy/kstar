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
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
/**
 * 
 * ClassName: InvoiceMaster <br/> 
 * Function: 开票主表. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年1月10日 上午11:16:50 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_invoice_master")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_INVOICE)
public class InvoiceMaster extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "invoice_m_pid_generator")
   	@GenericGenerator(name="invoice_m_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 开票申请编号 */
    @Column(name = "c_invoice_code")
    private String invoiceCode;
    
    /** 申请人ID */
    @Column(name = "c_proposer_id")
    private String proposerId;
    
    /** 申请人名称 */
    @Column(name = "c_proposer_name")
    private String proposerName;
    
    /** 客户ID */
	@Column(name = "c_customer_id")
	private String customerId;

	/** 客户编号 */
	@Column(name = "c_customer_code")
	private String customerCode;

	/** 客户名称 */
	@Column(name = "c_customer_name")
	private String customerName;
    
    /** 申请日期 */
    @Column(name = "dt_apply_date")
    private Date applyDate;
    
    /** 邮寄地址 */
    @Column(name = "c_postal_address")
    private String postalAddress;
    
    /** 快递单号 */
    @Column(name = "c_express_no")
    private String expressNo;
    
    /** 收票人 */
    @Column(name = "c_consignee")
    private String consignee;
    
    /** 收票人电话 */
    @Column(name = "c_consignee_tel")
    private String consigneeTel;
    
    /** 审批日期 */
    @Column(name = "dt_invoice_date")
    private Date invoiceDate;
    
    /** 开票金额 */
    @Column(name = "n_invoice_amount")
    private BigDecimal invoiceAmount;
    
    /** 税号 */
    @Column(name = "c_tax_no")
    private String taxNo;
    
    /** 开票类型 */
    @Column(name = "c_invoice_type")
    private String invoiceType;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
	
    /** 开票说明 */
    @Column(name = "c_remarks")
    private String remarks;
   
    /** 合同编号 */
    @Column(name = "c_contract_code")
    private String contractCode;
    
    /** 客户地址 */
    @Column(name = "c_address")
    private String address;
    
    /** 电话 */
    @Column(name = "c_tel_no")
    private String telno;
    
    /** 开户行 */
    @Column(name = "c_bank_act")
    private String bankact;
    
    /** 账号 */
    @Column(name = "c_act_no")
    private String actno;
    
    /** 业务实体 */
    @Column(name = "c_business_entity")
    private String businessEntity;
    
    /** 货币 */
	@Column(name = "c_currency")
	private String currency;
    
	/** 商务专员ID */
	@Column(name = "c_business_manager_id")
	private String businessManagerId;
	
	/** 商务专员编号 */
	@Column(name = "c_business_manager_code")
	private String businessManagerCode;
	
	/** 商务专员名称 */
	@Column(name = "c_business_manager_name")
	private String businessManagerName;
	
	@Column(name = "c_golden_tax_no")
	private String goldenTaxNo;
	
    @Transient
    private List<Map<Object, Object>> detailList = new ArrayList<Map<Object,Object>>();
    
    @Transient
    private List<Map<Object, Object>> goldenTaxList = new ArrayList<Map<Object,Object>>();
    
    @Transient
    private String statusLable;
    
    @Transient
    private String invoiceTypeLable;

    /** 业务实体显示名称*/
    @Transient
    private String businessEntityLable;
    
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
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

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Map<Object, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<Object, Object>> detailList) {
		this.detailList = detailList;
	}

	public List<Map<Object, Object>> getGoldenTaxList() {
		return goldenTaxList;
	}

	public void setGoldenTaxList(List<Map<Object, Object>> goldenTaxList) {
		this.goldenTaxList = goldenTaxList;
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

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
    
	public String getInvoiceTypeLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("INVOICE_TYPE", invoiceType);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setInvoiceTypeLable(String invoiceTypeLable) {
		this.invoiceTypeLable = invoiceTypeLable;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getBankact() {
		return bankact;
	}

	public void setBankact(String bankact) {
		this.bankact = bankact;
	}

	public String getActno() {
		return actno;
	}

	public void setActno(String actno) {
		this.actno = actno;
	}

	public String getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBusinessEntityLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(businessEntity);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public String getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(String businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public String getBusinessManagerCode() {
		return businessManagerCode;
	}

	public void setBusinessManagerCode(String businessManagerCode) {
		this.businessManagerCode = businessManagerCode;
	}

	public String getBusinessManagerName() {
		return businessManagerName;
	}

	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}

	public String getGoldenTaxNo() {
		return goldenTaxNo;
	}

	public void setGoldenTaxNo(String goldenTaxNo) {
		this.goldenTaxNo = goldenTaxNo;
	}
	
}