package com.ibm.kstar.entity.report;

import java.math.BigDecimal;
import java.util.Date;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.product.KstarProduct;

public class ReportRebaeOverOrder {
	
	/**
     * 申请单号
     */
    private String no;
    
    /**
     * 申请名称
     */
    private String name;
    
    /**
     * 审核状态
     */
    private String status;
    
    /**
     * 创建人
     */
    private String createdByIdName;
    
    /**
     * 代理商联系人
     */
    private String agentContact;
    
    /**
     * 代理商联系电话
     */
    private String phone;
    
    /**
     * 商务专员
     */
    private String businessExecutive;
    
    /**
     * 特价类型
     */
    private String specialOff;
    
    /**
     * 特价生效日期
     */
    private Date startDate;
    
    /**
     * 特价失效日期
     */
    private Date endDate;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * CRM产品分类
     */
    private String productSortName;
    
    /**
     * 产品规格
     */
    private String cproPowcap;
    
    /**
     * 产品型号
     */
    private String productModel;
    
    /**
     * 物料号
     */
    private String materCode;
    
    /**
     * 金牌价格
     */
    private BigDecimal catalogPrice;
    
    /**
     * 批复折扣
     */
    private BigDecimal approveRebate;
    
    /**
     * 批复价格
     */
    private BigDecimal approvePrice;
    
    /**
     * 数量
     */
    private BigDecimal applyQty;
    
    /**
     * 金额
     */
    private BigDecimal applyPrice;
    
    /**
     * 商机名称
     */
    private String bizName;
    
    /**
     * 终端用户名称
     */
    private String clientName;
    
    /**
     * 已下单数量
     */
    private BigDecimal orderQty;
    /**
     * 产品
     */
    private KstarProduct product;
    
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		LovMember lov = (LovMember) CacheData.getInstance().get("PROCESS_STATUS_" + status);
        return lov == null ? "" : lov.getName();
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedByIdName() {
		return createdByIdName;
	}

	public void setCreatedByIdName(String createdByIdName) {
		this.createdByIdName = createdByIdName;
	}

	public String getAgentContact() {
		return agentContact;
	}

	public void setAgentContact(String agentContact) {
		this.agentContact = agentContact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBusinessExecutive() {
		return businessExecutive;
	}

	public void setBusinessExecutive(String businessExecutive) {
		this.businessExecutive = businessExecutive;
	}

	public String getSpecialOff() {
		return specialOff;
	}

	public void setSpecialOff(String specialOff) {
		this.specialOff = specialOff;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSortName() {
		return productSortName;
	}

	public void setProductSortName(String productSortName) {
		this.productSortName = productSortName;
	}

	public String getCproPowcap() {
		return cproPowcap;
	}

	public void setCproPowcap(String cproPowcap) {
		this.cproPowcap = cproPowcap;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getMaterCode() {
		this.product = CacheUtils.getProduct(this.materCode);
		if (this.product == null) {
			this.product = new KstarProduct();
		}
		return product.getMaterCode();
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public BigDecimal getApproveRebate() {
		return approveRebate;
	}

	public void setApproveRebate(BigDecimal approveRebate) {
		this.approveRebate = approveRebate;
	}


	public BigDecimal getCatalogPrice() {
		return catalogPrice;
	}

	public void setCatalogPrice(BigDecimal catalogPrice) {
		this.catalogPrice = catalogPrice;
	}

	public BigDecimal getApprovePrice() {
		return approvePrice;
	}

	public void setApprovePrice(BigDecimal approvePrice) {
		this.approvePrice = approvePrice;
	}

	public BigDecimal getApplyPrice() {
		return applyPrice;
	}

	public void setApplyPrice(BigDecimal applyPrice) {
		this.applyPrice = applyPrice;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public BigDecimal getApplyQty() {
		return applyQty;
	}

	public void setApplyQty(BigDecimal applyQty) {
		this.applyQty = applyQty;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}
}
