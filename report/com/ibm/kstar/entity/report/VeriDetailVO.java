package com.ibm.kstar.entity.report;

import java.util.Date;


import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 回款金额详细VO
 * @author zhangjunxin
 *
 */
@SuppressWarnings("unused")
public class VeriDetailVO {
	/** 回款详细id**/
	private String id;
	/** 业务实体**/
	private String businessName;
	 /** 收款单号 */
    private String receiptsCode;
    /** 收款日期 */
    private Date receiptsDate;
    /** 收款金额*/
    private String receiptAmount;
    /** 付款客户 名称*/
    private String paymentCustomerName;
    /** 是否ERP客户 */
    private String erpCust;
    /** 更正客户名称*/
    private String correctCustName;
    /** 营销中心**/
    private String salesCenter;
    /** 合同编号 */
    private String contractCode;
    /** 合同收款计划行 */
    private String receiptsPlan;
    private String receiptsPlanLable;
    /** 收款阶段 */
    private String receiptsStage;
    private String receiptsStageLable;
    /** 发货单编号 */
    private String deliveryCode;
    /** 计划金额 */
    private String planAmount;
    /** 核销金额 */
    private String veriAmount;
    /** 业务部门 */
    private String bizDept;
    private String bizDeptLable;
    /** 销售人员名称 */
    private String salesmanName;
    /** 核销日期 */
    private Date veriDate;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getReceiptsCode() {
		return receiptsCode;
	}
	public void setReceiptsCode(String receiptsCode) {
		this.receiptsCode = receiptsCode;
	}
	public Date getReceiptsDate() {
		return receiptsDate;
	}
	public void setReceiptsDate(Date receiptsDate) {
		this.receiptsDate = receiptsDate;
	}
	public String getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(String receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getPaymentCustomerName() {
		return paymentCustomerName;
	}
	public void setPaymentCustomerName(String paymentCustomerName) {
		this.paymentCustomerName = paymentCustomerName;
	}
	public String getErpCust() {
		return erpCust;
	}
	public void setErpCust(String erpCust) {
		this.erpCust = erpCust;
	}
	public String getCorrectCustName() {
		return correctCustName;
	}
	public void setCorrectCustName(String correctCustName) {
		this.correctCustName = correctCustName;
	}
	public String getSalesCenter() {
		return salesCenter;
	}
	public void setSalesCenter(String salesCenter) {
		this.salesCenter = salesCenter;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getReceiptsPlan() {
		return receiptsPlan;
	}
	public void setReceiptsPlan(String receiptsPlan) {
		this.receiptsPlan = receiptsPlan;
	}
	public String getReceiptsPlanLable() {
		return this.getLovName(receiptsPlan);
	}
	public void setReceiptsPlanLable(String receiptsPlanLable) {
		this.receiptsPlanLable = receiptsPlanLable;
	}
	public String getReceiptsStage() {
		return receiptsStage;
	}
	public void setReceiptsStage(String receiptsStage) {
		this.receiptsStage = receiptsStage;
	}
	public String getReceiptsStageLable() {
		return  this.getLovName(receiptsStage);
	}
	public void setReceiptsStageLable(String receiptsStageLable) {
		this.receiptsStageLable = receiptsStageLable;
	}
	public String getDeliveryCode() {
		return deliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	public String getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(String planAmount) {
		this.planAmount = planAmount;
	}
	public String getVeriAmount() {
		return veriAmount;
	}
	public void setVeriAmount(String veriAmount) {
		this.veriAmount = veriAmount;
	}
	public String getBizDept() {
		return bizDept;
	}
	public void setBizDept(String bizDept) {
		this.bizDept = bizDept;
	}
	public String getBizDeptLable() {
		return this.getLovName(bizDept);
	}
	public void setBizDeptLable(String bizDeptLable) {
		this.bizDeptLable = bizDeptLable;
	}
	public String getSalesmanName() {
		return salesmanName;
	}
	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	public Date getVeriDate() {
		return veriDate;
	}
	public void setVeriDate(Date veriDate) {
		this.veriDate = veriDate;
	}
    
	private String getLovName(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
}
