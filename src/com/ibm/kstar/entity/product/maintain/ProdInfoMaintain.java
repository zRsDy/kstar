package com.ibm.kstar.entity.product.maintain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

@Entity
@Table(name = "CRM_T_PROD_INFO_MAINTAIN")
@Permission(businessType = "ProdInfoMaintain")
public class ProdInfoMaintain extends BaseEntity implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "prodinfomaintenance_id_generator")
	@GenericGenerator(name="prodinfomaintenance_id_generator", strategy="uuid")
	@Column(name="C_ID")
	@PermissionBusinessId
	private String id;

	@Column(name = "C_APPLICANT_ID")
	private String applicantId;
	
	@Column(name = "C_APPLICANT_DEPT")
	private String applicantDept;
	
	@Column(name = "C_MAINTAIN_CODE")
	private String maintainCode;
	
	@Column(name = "DT_REQ_COMPLETE_DATE")
	private Date reqCompleteDate;
	
	@Column(name = "C_REMARK")
	private String remark;
	
	@Column(name = "C_PROCESS_STATUS")
	private String processStatus;
	
	@Column(name = "C_STATUS")
	private String status;
	
	@Transient
	private List<Map<Object, Object>> attrModLinesList;
	
	@Transient
	private List<Map<Object, Object>> cataLogModLinesList;
	
	@Transient
	private List<Map<Object, Object>> saleStatusModLinesList;

	public List<Map<Object, Object>> getAttrModLinesList() {
		return attrModLinesList;
	}

	public void setAttrModLinesList(List<Map<Object, Object>> attrModLinesList) {
		this.attrModLinesList = attrModLinesList;
	}

	public List<Map<Object, Object>> getCataLogModLinesList() {
		return cataLogModLinesList;
	}

	public void setCataLogModLinesList(List<Map<Object, Object>> cataLogModLinesList) {
		this.cataLogModLinesList = cataLogModLinesList;
	}

	public List<Map<Object, Object>> getSaleStatusModLinesList() {
		return saleStatusModLinesList;
	}

	public void setSaleStatusModLinesList(List<Map<Object, Object>> saleStatusModLinesList) {
		this.saleStatusModLinesList = saleStatusModLinesList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getApplicantDept() {
		return applicantDept;
	}

	public void setApplicantDept(String applicantDept) {
		this.applicantDept = applicantDept;
	}

	public String getMaintainCode() {
		return maintainCode;
	}

	public void setMaintainCode(String maintainCode) {
		this.maintainCode = maintainCode;
	}

	public Date getReqCompleteDate() {
		return reqCompleteDate;
	}

	public void setReqCompleteDate(Date reqCompleteDate) {
		this.reqCompleteDate = reqCompleteDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getApplicantName(){
		IEmployeeService employeeService = (IEmployeeService)ApplicationContextUtil.getBean("employeeServiceImpl");
		if(employeeService != null){
			Employee employee = employeeService.get(applicantId);
			if(employee != null){
				return employee.getNo()+"|"+employee.getName();
			}
		}
		return "";
	}
	
	public String getApplicantDeptName(){
		return this.getLovMember(applicantDept).getNamePath();
	}
	
	public String getProcessStatusName(){
		return this.getLovMember("PROD_MAINTIAN_PRO_STATUS", processStatus).getName();
	}
	
	public String getStatusName(){
		return this.getLovName(status);
	}
}
