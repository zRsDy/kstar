package com.ibm.kstar.action.common;  

import java.util.Date;

import com.ibm.kstar.entity.bizopp.Rebate;
import com.ibm.kstar.entity.bizopp.RebateLine;

/** 
 * ClassName:特价申请VO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年5月15日 下午7:17:16 <br/> 
 * @author   liming 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public class RebateVO {
	
	/**
	 * ID
	 */
	private String id;
	  
	 /**
     * 申请单号
     */
    private String no;
    /**
     * 申请类型
     */
    private String type;
    /**
     * 申请代理商
     */
    private String applyAgent;
    /**
     * 申请代理商名称
     */
    private String applyAgentName;
    
    /**
     * 特价有效期至
     */
    private Date startDate;
    /**
     * 特价有效期至
     */
    private Date endDate;
    /**
     * 商机名称
     */
    private String bizId;
    /**
     * 商机名称
     */
    private String bizName;
    
    private String productName;
    
    private String productModel;
    
    private String productCode;
    
    private String productId;
    
    private Double applyQty;
    
    private Double sourcePrice;
    
    private Double applyPrice;
    
    private Double applyRebate;
    
    private Double approveRebate;
    
    private Double approvePrice;
    
    private Double amount;
    
    private String clientId;
    
    private String clientName;
    
    private Double orderQty;
    
    private String remark;
    
    public RebateVO(){};
    
	public RebateVO(Rebate rebate,RebateLine rebateLine) {
		this.id = rebateLine.getId();
		this.no = rebate.getNo();
		this.type = rebate.getType();
		this.applyAgent = rebate.getApplyAgent();
		this.applyAgentName = rebate.getApplyAgentName();
		this.startDate = rebate.getStartDate();
		this.endDate = rebate.getEndDate();
		this.bizId = rebateLine.getBizId();
		this.bizName = rebateLine.getBizName();
		this.productName = rebateLine.getProductName();
		this.productModel = rebateLine.getProductModel();
		this.productCode = rebateLine.getProductCode();
		this.productId = rebateLine.getProductId();
		this.applyQty = rebateLine.getApplyQty();
		this.sourcePrice = rebateLine.getSourcePrice();
		this.applyPrice = rebateLine.getApplyPrice();
		this.applyRebate = rebateLine.getApplyRebate();
		this.approveRebate = rebateLine.getApproveRebate();
		this.approvePrice = rebateLine.getApprovePrice();
		this.amount = rebateLine.getAmount();
		this.clientId = rebateLine.getClientId();
		this.clientName = rebateLine.getClientName();
		this.orderQty = rebateLine.getOrderQty();
		this.remark = rebateLine.getRemark();
	}

	

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApplyAgent() {
		return applyAgent;
	}

	public void setApplyAgent(String applyAgent) {
		this.applyAgent = applyAgent;
	}

	public String getApplyAgentName() {
		return applyAgentName;
	}

	public void setApplyAgentName(String applyAgentName) {
		this.applyAgentName = applyAgentName;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Double getApplyQty() {
		return applyQty;
	}

	public void setApplyQty(Double applyQty) {
		this.applyQty = applyQty;
	}

	public Double getSourcePrice() {
		return sourcePrice;
	}

	public void setSourcePrice(Double sourcePrice) {
		this.sourcePrice = sourcePrice;
	}

	public Double getApplyRebate() {
		return applyRebate;
	}

	public void setApplyRebate(Double applyRebate) {
		this.applyRebate = applyRebate;
	}

	public Double getApproveRebate() {
		return approveRebate;
	}

	public void setApproveRebate(Double approveRebate) {
		this.approveRebate = approveRebate;
	}

	public Double getApprovePrice() {
		return approvePrice;
	}

	public void setApprovePrice(Double approvePrice) {
		this.approvePrice = approvePrice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getApplyPrice() {
		return applyPrice;
	}

	public void setApplyPrice(Double applyPrice) {
		this.applyPrice = applyPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
    
}
  
