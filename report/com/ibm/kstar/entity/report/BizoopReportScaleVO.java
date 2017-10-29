package com.ibm.kstar.entity.report;

import java.util.Date;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;

/**
 * 商机报表VO
 * @author 1
 *
 */
@SuppressWarnings("unused")
public class BizoopReportScaleVO {
	/**商机名称**/
	private String opportunityName;
	/** 商机所在地名称 */
	private String bizOppAddressName;
	/** 客户名称 */
	private String clientName;
	/** 报备人id */
	private String createdById;
	/** 报备人 */
	private String createdByIdName;
	/** 报备人组织id */
    private String createdOrgId;
    /** 报备人组织 */
    private String createdOrgIdName;
    /** 报备时间 */
    private Date createdAt;
    /** 处理状态CODE */
	private String status;
	/** 处理状态名 */
	private String statusName;
	/** 商机状态CODE */
	private String conflictStatus;
	/** 商机状态名 */
	private String conflictStatusName;
	/** 商机阶段CODE */
	private String saleStage;
	/** 商机阶段名 */
	private String saleStageName;
	/** 生效日期*/
	private Date successDate;
	/** 失效日期*/
	private Date endDate;
	/** 是否已授权*/
	private String isReport;
	/** 授权是否已反馈*/
	private String isReportFeedback;
	/** 是否已下单*/
	private String isOrder;
	
	public String getOpportunityName() {
		return opportunityName;
	}
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}
	public String getBizOppAddressName() {
		return bizOppAddressName;
	}
	public void setBizOppAddressName(String bizOppAddressName) {
		this.bizOppAddressName = bizOppAddressName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCreatedById() {
		return createdById;
	}
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	public String getCreatedByIdName() {
		Employee employee = (Employee) CacheData.getInstance().get(createdById);
		if(employee != null ){
			return employee.getName() + " | " + employee.getNo();
		}
		return  "";
	}
	public void setCreatedByIdName(String createdByIdName) {
		this.createdByIdName = createdByIdName;
	}
	public String getCreatedOrgId() {
		return createdOrgId;
	}
	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}
	public String getCreatedOrgIdName() {
		LovMember l = (LovMember) CacheData.getInstance().get(this.createdOrgId);
		if (l != null) {
			return l.getName();
		}
		return "";
	}
	public void setCreatedOrgIdName(String createdOrgIdName) {
		this.createdOrgIdName = createdOrgIdName;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("OPPORTUNITY_STATUS_" + status);
		return lov == null ? "" : lov.getName();
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getConflictStatus() {
		return conflictStatus;
	}
	public void setConflictStatus(String conflictStatus) {
		this.conflictStatus = conflictStatus;
	}
	public String getConflictStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("CONFLICT_STATUS_" + conflictStatus);
		return lov == null ? "" : lov.getName();
	}
	public void setConflictStatusName(String conflictStatusName) {
		this.conflictStatusName = conflictStatusName;
	}
	public String getSaleStage() {
		return saleStage;
	}
	public void setSaleStage(String saleStage) {
		this.saleStage = saleStage;
	}
	public String getSaleStageName() {
		LovMember lovMember = (LovMember) CacheData.getInstance().get(saleStage);
		if (lovMember != null) {
			return lovMember.getName();
		}
		return "";
	}
	public void setSaleStageName(String saleStageName) {
		this.saleStageName = saleStageName;
	}
	public Date getSuccessDate() {
		return successDate;
	}
	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getIsReport() {
		return isReport;
	}
	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}
	public String getIsReportFeedback() {
		return isReportFeedback;
	}
	public void setIsReportFeedback(String isReportFeedback) {
		this.isReportFeedback = isReportFeedback;
	}
	public String getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}
	
	
}
