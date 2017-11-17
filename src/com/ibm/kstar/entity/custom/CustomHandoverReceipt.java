package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "crm_t_custom_handover_receipt")
public class CustomHandoverReceipt implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 3311863893178949940L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "custom_handover_receipt_pid_generator")
	@GenericGenerator(name="custom_handover_receipt_pid_generator", strategy="uuid")
    private String id;
    
    /** 交接ID */
    @Column(name = "c_system_id", nullable = false, length = 32)
    private String systemId;
    
    /** 交接人ID */
    @Column(name = "c_user_id", nullable = false, length = 32)
    private String userId;
    
    /** 客户编码 */
    @Column(name = "c_custom_code", nullable = false, length = 32)
    private String customCode;
    
    /** 客户名称 */
    @Column(name = "c_custom_full_name", nullable = true, length = 200)
    private String customFullName;
    
    /** 商机名称 */
    @Column(name = "c_opportunity_name", nullable = true, length = 32)
    private String opportunityName;
    
    /** 商机状态 */
    @Column(name = "c_opportunity_status", nullable = true, length = 32)
    private String opportunityStatus;
    
    /** 报价单编号 */
    @Column(name = "c_quot_id", nullable = true, length = 32)
    private String quotId;
    
    /** 报价单状态 */
    @Column(name = "c_quot_status", nullable = true, length = 32)
    private String quotStatus;
    
    /** 合同编号 */
    @Column(name = "c_contr_no", nullable = true, length = 32)
    private String contrNo;
    
    /** 合同状态 */
    @Column(name = "c_contr_stat", nullable = true, length = 32)
    private String contrStat;
    
    /** 订单编号 */
    @Column(name = "c_order_code", nullable = true, length = 32)
    private String orderCode;
    
    /** 订单状态 */
    @Column(name = "c_order_status", nullable = true, length = 32)
    private String orderStatus;
    
    /** 发货金额 */
    @Column(name = "c_delivery_amount", nullable = true, length = 32)
    private String deliveryAmount;
    
    /** 开票余额 */
    @Column(name = "c_receipt_balance", nullable = true, length = 32)
    private String receiptBalance;
    
    /** 收款余额 */
    @Column(name = "c_payment_balance", nullable = true, length = 32)
    private String paymentBalance;
    
    /** 备注 */
    @Column(name = "c_comment", nullable = true, length = 100)
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

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCustomFullName() {
		return customFullName;
	}

	public void setCustomFullName(String customFullName) {
		this.customFullName = customFullName;
	}

	public String getOpportunityName() {
		return opportunityName;
	}

	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}

	public String getOpportunityStatus() {
		return opportunityStatus;
	}

	public void setOpportunityStatus(String opportunityStatus) {
		this.opportunityStatus = opportunityStatus;
	}

	public String getQuotId() {
		return quotId;
	}

	public void setQuotId(String quotId) {
		this.quotId = quotId;
	}

	public String getQuotStatus() {
		return quotStatus;
	}

	public void setQuotStatus(String quotStatus) {
		this.quotStatus = quotStatus;
	}

	public String getContrNo() {
		return contrNo;
	}

	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}

	public String getContrStat() {
		return contrStat;
	}

	public void setContrStat(String contrStat) {
		this.contrStat = contrStat;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(String deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public String getReceiptBalance() {
		return receiptBalance;
	}

	public void setReceiptBalance(String receiptBalance) {
		this.receiptBalance = receiptBalance;
	}

	public String getPaymentBalance() {
		return paymentBalance;
	}

	public void setPaymentBalance(String paymentBalance) {
		this.paymentBalance = paymentBalance;
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
    
}