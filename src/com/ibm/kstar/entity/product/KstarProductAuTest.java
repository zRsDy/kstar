package com.ibm.kstar.entity.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "CRM_T_PRODUCT_AUTEST")
public class KstarProductAuTest extends BaseEntity implements Serializable {

	
	// 认证测试ID
	@Id
	@GeneratedValue(generator = "kstarproautest_id_generator")
	@GenericGenerator(name="kstarproautest_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 认证测试名称
	@Column(name="C_PRO_TEST_NAME")
	private String auTestName;
	
	// 认证测试事项或需求说明
	@Column(name="C_PRO_TEST_DESC")
	private String auTestDesc;
	
	// 认证类型
	@Column(name="C_PRO_TEST_TYPE")
	private String auTestType;

	// 产品型号
	@Column(name="C_PRO_MODEL")
	private String proModel;
	
	// 提交时间
	@Column(name="DT_SUB_TIME")
	private Date submitTime;
	
	// 需求完成时间
	@Column(name="DT_COMPLETE_TIME")
	private Date completeTime;
	
	// 状态
	@Column(name="C_STATUS")
	private String status;

	// 申请部门
	@Column(name="C_APPLY_DEPARTMENT")
	private String applyDepartment;

	// 申请人
	@Column(name="C_APPLY_PERSON")
	private String applyPersion;

	@Transient
	private Object applyPersionBean = new Employee();
	
	// 认证编号
	@Column(name="C_AUTH_CODE")
	private String authCode;
	
	// 认证结果
	@Column(name="C_AUTH_RESULT")
	private String anthUrl;
	
	// 产品ID
	@Column(name="C_PRO_ID")
	private String productID;
	
	// 是否总经理审批
	@Column(name="C_CEO_APPROVE")
	private String isCeoApprove;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuTestName() {
		return auTestName;
	}

	public void setAuTestName(String auTestName) {
		this.auTestName = auTestName;
	}

	public String getAuTestDesc() {
		return auTestDesc;
	}

	public void setAuTestDesc(String auTestDesc) {
		this.auTestDesc = auTestDesc;
	}

	public String getAuTestType() {
		return auTestType;
	}

	public void setAuTestType(String auTestType) {
		this.auTestType = auTestType;
	}

	public String getAuTestTypeName() {		
		return this.getLovName(auTestType);
	}

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusCode() {
		return this.getLovCode(status);
	}
	
	public String getStatusName() {
		return this.getLovName(status);
	}
	
	public String getApplyDepartment() {
		return applyDepartment;
	}

	public void setApplyDepartment(String applyDepartment) {
		this.applyDepartment = applyDepartment;
	}

	public String getApplyDepartmentName() {
		return this.getLovName(applyDepartment);
	}
	
	public String getApplyPersion() {
		return applyPersion;
	}

	public void setApplyPersion(String applyPersion) {
		this.applyPersion = applyPersion;
	}
	
	public Object getApplyPersionBean() {
		return applyPersionBean;
	}

	public void setApplyPersionBean(Object applyPersionBean) {
		this.applyPersionBean = applyPersionBean;
	}
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAnthUrl() {
		return anthUrl;
	}

	public void setAnthUrl(String anthUrl) {
		this.anthUrl = anthUrl;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getIsCeoApprove() {
		return isCeoApprove;
	}

	public void setIsCeoApprove(String isCeoApprove) {
		this.isCeoApprove = isCeoApprove;
	}
	
}
