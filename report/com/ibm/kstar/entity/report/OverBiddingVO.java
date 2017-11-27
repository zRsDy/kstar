package com.ibm.kstar.entity.report;

import java.math.BigDecimal;
import java.util.Date;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;

/**
 * 逾期投标保证金
 * @author 1
 *
 */
@SuppressWarnings("unused")
public class OverBiddingVO {
	/**ID**/
	private String id;
	/** 销售员工号**/
	private String fdEmpNo;
	/** CRM岗位 */
	private String crmPositionName;
	/** CRM部门 */
	private String crmOrgName;
	/** 申请日期 */
	private Date fdRequestDate;
	/** 项目名称 */
	private String fdProject;
	/** 支付日期 */
    private Date fdPayDate;
    /** 退还日期 */
    private Date fdBackDate;
    /** 收款方 */
    private String fdReceiptName;
    /** 银行 */
	private String fdBankName;
	/** 银行账号 */
	private String fdBankNo;
	/** 金额 */
	private BigDecimal fdAmount;
	/** 备注 */
	private String fdMemo;
	/** 申请编号 */
	private String fdNumber;
	/** 申请内容 */
	private String docSubject;
	/** 状态*/
	private String status;
	/** CRM员工姓名*/
	private String crmPersonName;
	/** 最后更新时间 */
    private Date fdLastModifiedTime;
	
	public String getFdEmpNo() {
		return fdEmpNo;
	}
	public void setFdEmpNo(String fdEmpNo) {
		this.fdEmpNo = fdEmpNo;
	}
	public String getCrmPositionName() {
		return crmPositionName;
	}
	public void setCrmPositionName(String crmPositionName) {
		this.crmPositionName = crmPositionName;
	}
	public String getCrmOrgName() {
		return crmOrgName;
	}
	public void setCrmOrgName(String crmOrgName) {
		this.crmOrgName = crmOrgName;
	}
	public Date getFdRequestDate() {
		return fdRequestDate;
	}
	public void setFdRequestDate(Date fdRequestDate) {
		this.fdRequestDate = fdRequestDate;
	}
	public String getFdProject() {
		return fdProject;
	}
	public void setFdProject(String fdProject) {
		this.fdProject = fdProject;
	}
	public Date getFdPayDate() {
		return fdPayDate;
	}
	public void setFdPayDate(Date fdPayDate) {
		this.fdPayDate = fdPayDate;
	}
	public Date getFdBackDate() {
		return fdBackDate;
	}
	public void setFdBackDate(Date fdBackDate) {
		this.fdBackDate = fdBackDate;
	}
	public String getFdReceiptName() {
		return fdReceiptName;
	}
	public void setFdReceiptName(String fdReceiptName) {
		this.fdReceiptName = fdReceiptName;
	}
	public String getFdBankName() {
		return fdBankName;
	}
	public void setFdBankName(String fdBankName) {
		this.fdBankName = fdBankName;
	}
	public String getFdBankNo() {
		return fdBankNo;
	}
	public void setFdBankNo(String fdBankNo) {
		this.fdBankNo = fdBankNo;
	}
	public BigDecimal getFdAmount() {
		return fdAmount;
	}
	public void setFdAmount(BigDecimal fdAmount) {
		this.fdAmount = fdAmount;
	}
	public String getFdMemo() {
		return fdMemo;
	}
	public void setFdMemo(String fdMemo) {
		this.fdMemo = fdMemo;
	}
	public String getFdNumber() {
		return fdNumber;
	}
	public void setFdNumber(String fdNumber) {
		this.fdNumber = fdNumber;
	}
	public String getDocSubject() {
		return docSubject;
	}
	public void setDocSubject(String docSubject) {
		this.docSubject = docSubject;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getFdLastModifiedTime() {
		return fdLastModifiedTime;
	}
	public void setFdLastModifiedTime(Date fdLastModifiedTime) {
		this.fdLastModifiedTime = fdLastModifiedTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCrmPersonName() {
		return crmPersonName;
	}
	public void setCrmPersonName(String crmPersonName) {
		this.crmPersonName = crmPersonName;
	}
}
