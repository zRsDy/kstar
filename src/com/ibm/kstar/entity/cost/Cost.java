package com.ibm.kstar.entity.cost;

import org.xsnake.web.utils.UUIDUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * KstarContract.java 费用查询实体类
 * 
 */
@Entity
@Table(name = "CUX_OA_EXPENSES_CLAIM_DATA")
public class Cost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	@Id
    @GeneratedValue
	@Column(name = "ROW_ID")
	private String id;
	
	/**
	 * 批号
	 */
	@Column(name = "BATCH_NO")
	private String batchNo;
	
	/**
	 * 发票编号
	 */
	@Column(name = "INVOICE_NO")
	private String invoiceNo;
	
	/**
	 * 工号
	 */
	@Column(name = "EMPLOYEE_NO")
	private String employeeNo;
	
	/**
	 * 员工姓名
	 */
	@Column(name = "CLAIM_PERSON_NAME")
	private String claimPersonName;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 发票日期
	 */
	@Column(name = "INVOICE_DATE")
	private Date invoiceDate;

	/**
	 * 发票总金额
	 */
	@Column(name = "INVOICE_AMOUNT")
	private BigDecimal invoiceAmount;	
	
	/**
	 * 费用金额
	 */
	@Column(name = "INVOICE_LINE_AMOUNT")
	private BigDecimal invoiceLineAmount;
	
	/**
	 * 业务实体
	 */
	@Column(name = "BUSINESS_ENTITY")
	private String businessEntity;

	/**
	 * 提交日期
	 */
	@Column(name = "SUBMIT_DATE")
	private Date submitDate;
	
	/**
	 * 科目名称
	 */
	@Column(name = "ACCOUNT_NAME")
	private String accountName;

	/**
	 * 部门段
	 */
	@Column(name = "ACCOUNT_DEPT")
	private String accountDept;
	
	/**
	 * 备注
	 */
	@Column(name = "LINE_DESC")
	private String lineDesc;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	/**
	 * CRM岗位名称
	 */
	@Column(name = "CRM_POSITION_NAME")
	private String crmPositionName;
	
	/**
	 * CRM岗位ID
	 */
	@Column(name = "CRM_POSITION_ID")
	private String crmPositionId;
	
	/**
	 * CRM部门名称
	 */
	@Column(name = "CRM_ORG_NAME")
	private String crmOrgName;
	
	/**
	 * CRM部门ID
	 */
	@Column(name = "CRM_ORG_ID")
	private String crmOrgId;
	
	/**
	 * CRM部门ID路径
	 */
	@Column(name = "CRM_ORG_PATH")
	private String crmOrgPath;
	
	/**
	 * 导入来源（ERP、OA、EXCEL）
	 */
	@Column(name = "IMPORT_FLAG")
	private String importFlag;
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getClaimPersonName() {
		return claimPersonName;
	}

	public void setClaimPersonName(String claimPersonName) {
		this.claimPersonName = claimPersonName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public BigDecimal getInvoiceLineAmount() {
		return invoiceLineAmount;
	}

	public void setInvoiceLineAmount(BigDecimal invoiceLineAmount) {
		this.invoiceLineAmount = invoiceLineAmount;
	}

	public String getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountDept() {
		return accountDept;
	}

	public void setAccountDept(String accountDept) {
		this.accountDept = accountDept;
	}

	public String getLineDesc() {
		return lineDesc;
	}

	public void setLineDesc(String lineDesc) {
		this.lineDesc = lineDesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getImportFlag() {
		return importFlag;
	}

	public void setImportFlag(String importFlag) {
		this.importFlag = importFlag;
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

	public String getCrmPositionId() {
		return crmPositionId;
	}

	public void setCrmPositionId(String crmPositionId) {
		this.crmPositionId = crmPositionId;
	}

	public String getCrmOrgId() {
		return crmOrgId;
	}

	public void setCrmOrgId(String crmOrgId) {
		this.crmOrgId = crmOrgId;
	}

	public String getCrmOrgPath() {
		return crmOrgPath;
	}

	public void setCrmOrgPath(String crmOrgPath) {
		this.crmOrgPath = crmOrgPath;
	}

	
}
