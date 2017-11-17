package com.ibm.kstar.entity.report;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.bizopp.ComEntity;
import com.ibm.kstar.entity.team.Orgs;
import com.ibm.kstar.permission.utils.*;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.StringUtil;

import javax.persistence.*;
import java.io.Serializable; 
import java.util.Date;

public class PrototypeApplyReturnReport{
	
	
	private String id;
	
    /** 申请人 */
    private String applicantId;
    
    /** 部门 */
    private String department;
    
    /** 编号 */
    private String number;
    
    /** 期望到货时间 */
    private Date planRecieveTime;
    
    /** 预计归还时间 */
    private Date planReturnTime;
    
    /** 客户ID */
    private String clientId;
    
    /** 客户名称 */
    private String clientName;
    
    /** 商机ID */
    private String bizOppId;
    
    /** 商机名称 */
    private String bizoppName;
    
    /** 状态 */
    private String status;

	private String statusName;

	public String getStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("PROCESS_STATUS_" + status);
		return lov == null ? "" : lov.getName();
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 样机型号及描述
	 */
    private String prototypeTypeDisc;
    
    /** 是否免费赠送 */
    private String isForFree;
    
    /** 免费赠送原因 */
    private String freeReason;
    
    /** 备注 */
    private String remark;
    
    /** 样机销售报价 */
    private String prototypePrice;
    
    /** 销售方式 */
    private String saleMethod;
    
    /** 订单编号 */
    private String orderNumber;
    
    /** 确认已领取 */
    private String receiveConfirm;
    
    /** 样机领取备注 */
    private String receiveRemark;
    
    /** 收货人地址 */
    private String recieverAddress;
    
    /** 收货人 */
    private String reciever;
    
    /** 收货电话 */
    private String recieverPhone;
    
    /** 申请人确认退还接收 */
    private String applicantRConfirm;
    
    /** 样机状况备注 */
    private String prototypeCondition;
    
    /** 接收人确认退还接收 */
    private String recieverConfirm;
    
    /** 样机状态 */
    private String prototypeStatus;
    
    /** 样机接收备注 */
    private String recievedCondition;
    
    /** 申请单位 */
    private String applyUnit;
    
    /** 产品型号 */
    private String productModel;
    
    /** 产品料号 */
    private String materialNumber;
    
    /** 产品描述 */
    private String productDesc;

	/** 标准产品 */
	private String isStandard;

	/** 申请数量 */
    private String applyCount;
    
    /** 类型 */
    private String type;
    
	private String productId;

	private String productPrice;

	private String applyAmount;
    
	private String applicantName;

	private String departmentName;
	
	private String prototypeStatusName;
	
	public void setPrototypeStatusName(String prototypeStatusName) {
		this.prototypeStatusName = prototypeStatusName;
	}

	public String getPrototypeStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("SAMPLE_STATUS_" + prototypeStatus);
		return lov == null ? "" : lov.getName();
	}
	
	public String getApplicantName() {
		Employee employee = (Employee)CacheData.getInstance().get(applicantId);
		String applicantName = "";
		if(!StringUtil.isNullOrEmpty(employee)) {
			applicantName = employee.getName();
		}
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getDepartmentName() {
		LovMember lovMember = ((LovMember)CacheData.getInstance().get(department));
		String name = null; 
		if(StringUtil.isNullOrEmpty(lovMember)) {
			lovMember = new LovMember();
			name = lovMember.getName();
		}else {
			name = lovMember.getName();
		}
		return name;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getPlanRecieveTime() {
		return planRecieveTime;
	}

	public void setPlanRecieveTime(Date planRecieveTime) {
		this.planRecieveTime = planRecieveTime;
	}

	public Date getPlanReturnTime() {
		return planReturnTime;
	}

	public void setPlanReturnTime(Date planReturnTime) {
		this.planReturnTime = planReturnTime;
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

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

	public String getBizoppName() {
		return bizoppName;
	}

	public void setBizoppName(String bizoppName) {
		this.bizoppName = bizoppName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrototypeTypeDisc() {
		return prototypeTypeDisc;
	}

	public void setPrototypeTypeDisc(String prototypeTypeDisc) {
		this.prototypeTypeDisc = prototypeTypeDisc;
	}

	public String getIsForFree() {
		return isForFree;
	}

	public void setIsForFree(String isForFree) {
		this.isForFree = isForFree;
	}

	public String getFreeReason() {
		return freeReason;
	}

	public void setFreeReason(String freeReason) {
		this.freeReason = freeReason;
	}

	public String getPrototypePrice() {
		return prototypePrice;
	}

	public void setPrototypePrice(String prototypePrice) {
		this.prototypePrice = prototypePrice;
	}

	public String getSaleMethod() {
		return saleMethod;
	}

	public void setSaleMethod(String saleMethod) {
		this.saleMethod = saleMethod;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getReceiveConfirm() {
		return receiveConfirm;
	}

	public void setReceiveConfirm(String receiveConfirm) {
		this.receiveConfirm = receiveConfirm;
	}

	public String getReceiveRemark() {
		return receiveRemark;
	}

	public void setReceiveRemark(String receiveRemark) {
		this.receiveRemark = receiveRemark;
	}

	public String getRecieverAddress() {
		return recieverAddress;
	}

	public void setRecieverAddress(String recieverAddress) {
		this.recieverAddress = recieverAddress;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public String getRecieverPhone() {
		return recieverPhone;
	}

	public void setRecieverPhone(String recieverPhone) {
		this.recieverPhone = recieverPhone;
	}

	public String getApplicantRConfirm() {
		return applicantRConfirm;
	}

	public void setApplicantRConfirm(String applicantRConfirm) {
		this.applicantRConfirm = applicantRConfirm;
	}

	public String getPrototypeCondition() {
		return prototypeCondition;
	}

	public void setPrototypeCondition(String prototypeCondition) {
		this.prototypeCondition = prototypeCondition;
	}

	public String getRecieverConfirm() {
		return recieverConfirm;
	}

	public void setRecieverConfirm(String recieverConfirm) {
		this.recieverConfirm = recieverConfirm;
	}

	public String getPrototypeStatus() {
		return prototypeStatus;
	}

	public void setPrototypeStatus(String prototypeStatus) {
		this.prototypeStatus = prototypeStatus;
	}

	public String getRecievedCondition() {
		return recievedCondition;
	}

	public void setRecievedCondition(String recievedCondition) {
		this.recievedCondition = recievedCondition;
	}

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}
	
	public String getApplyAmount() {
		applyAmount = String.valueOf(Double.valueOf(applyCount)*Double.valueOf(productPrice));
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getId() {
		return id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(String applyCount) {
		this.applyCount = applyCount;
	}

	public String getType() {
		return type;
	}

	public String getTypeName() {
		LovMember lov = (LovMember) CacheData.getInstance().getMember("SAMPLETYPE",this.type);
		if(lov == null){
			return "";
		}
		return lov.getName();
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

}
