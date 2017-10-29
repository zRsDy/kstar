package com.ibm.kstar.action.task.schedule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TEMP_FOR_SYNC")
public class TempForDataSync {
	
	@Id
	@GenericGenerator(name = "temp_for_sync_id_generator", strategy = "uuid")
	@GeneratedValue(generator = "temp_for_sync_id_generator")
	@Column(name = "ID")
	String id;
	
	@Column(name = "FD_ID")
	private String 	fdId;
	
	@Column(name = "FD_PERSON")
	private String 	fdPerson;		//员工id
	
	@Column(name = "FD_DEPUT")
	private String 	fdDeput;    	//部门id
	
	@Column(name = "FD_EMP_NO")
	private String 	fdEmpNo;   		//员工工号
	
	@Column(name = "FD_REQUEST_DATE")
	private Date 	fdRequestDate; 	//申请日期
	
	@Column(name = "FD_PROJECT")
	private String 	fdProject;     	//项目名称
	
	@Column(name = "FD_TYPE")
	private String 	fdType;        	//项目类型 1.投标保证金 2.质量保证金 3.履约保证金 4.其他
	
	@Column(name = "FD_PAY_DATE")
	private Date 	fdPayDate;   	//拟款日期
	
	@Column(name = "FD_BACK_DATE")
	private Date 	fdBackDate;  	//拟回款日期
	
	@Column(name = "FD_RECEIPT_NAME")
	private String 	fdReceiptName; 	//收款单位名称 
	
	@Column(name = "FD_BANK_NAME")
	private String 	fdBankName;  	//收款单位银行名称 
	
	@Column(name = "FD_BANK_NO")
	private String 	fdBankNo; 		//收款单位银行账号 
	
	@Column(name = "FD_AMOUNT")
	private Double 	fdAmount;  		//金额（元）
	
	@Column(name = "FD_MEMO")
	private String 	fdMemo;  		//备注
	
	@Column(name = "FD_NUMBER")
	private String 	fdNumber;  		//流程编号
	
	@Column(name = "FD_LAST_MODIFIED_TIME")
	private Date 	fdLastModifiedTime; //最后更新时间
	
	@Column(name = "DOC_SUBJECT")
	private String 	docSubject; 		//表单标题
	
	@Column(name = "STATUS")
	private String 	status;
	
	public String getFdId() {
		return fdId;
	}
	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	public String getFdPerson() {
		return fdPerson;
	}
	public void setFdPerson(String fdPerson) {
		this.fdPerson = fdPerson;
	}
	public String getFdDeput() {
		return fdDeput;
	}
	public void setFdDeput(String fdDeput) {
		this.fdDeput = fdDeput;
	}
	public String getFdEmpNo() {
		return fdEmpNo;
	}
	public void setFdEmpNo(String fdEmpNo) {
		this.fdEmpNo = fdEmpNo;
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
	public String getFdType() {
		return fdType;
	}
	public void setFdType(String fdType) {
		this.fdType = fdType;
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
	public Double getFdAmount() {
		return fdAmount;
	}
	public void setFdAmount(Double fdAmount) {
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
	public Date getFdLastModifiedTime() {
		return fdLastModifiedTime;
	}
	public void setFdLastModifiedTime(Date fdLastModifiedTime) {
		this.fdLastModifiedTime = fdLastModifiedTime;
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
}
