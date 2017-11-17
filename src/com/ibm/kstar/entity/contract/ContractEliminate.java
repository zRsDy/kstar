package com.ibm.kstar.entity.contract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.order.OrderHeader;



/**
 * KstarContract.java 无合同核销申请实体类
 * 
 * @author Neil Wan and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "CRM_T_Eliminate")
public class ContractEliminate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "C_ID")
	private String id;
	
	

	//申请编号
	@Column(name = "C_CONTRACT_ID")
	private String contractId;

	//申请编号
	@Column(name = "C_ELIMINATE_NUMBER")
	private String eliminateNumber;
	
	//申请人
	@Column(name = "C_ELIMINATE_USER")
	private String eliminateUser;
	
	//申请人ID
	@Column(name = "C_ELIMINATE_USER_ID")
	private String eliminateUserId;
	
	//申请状态
	@Column(name = "C_ELIMINATE_TYPE")
	private String eliminateType;
	
	//申请日期
	@Column(name = "DT_ELIMINATE_DATE")
	private Date eliminateDate;

	//备注
	@Column(name = "C_REMARK")
	private String remark;
	
	//申请状态
	@Transient
	private String eliminateTypeDesc;
	
	@Transient
    private String prjlst;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEliminateNumber() {
		return eliminateNumber;
	}

	public void setEliminateNumber(String eliminateNumber) {
		this.eliminateNumber = eliminateNumber;
	}

	public String getEliminateUser() {
		return eliminateUser;
	}

	public void setEliminateUser(String eliminateUser) {
		this.eliminateUser = eliminateUser;
	}

	public String getEliminateType() {
		return eliminateType;
	}

	public void setEliminateType(String eliminateType) {
		this.eliminateType = eliminateType;
	}

	public Date getEliminateDate() {
		return eliminateDate;
	}

	public void setEliminateDate(Date eliminateDate) {
		this.eliminateDate = eliminateDate;
	}
	
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getEliminateTypeDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(eliminateType));
		return lov==null? null : lov.getName();
	}

	public void setEliminateTypeDesc(String eliminateTypeDesc) {
		this.eliminateTypeDesc = eliminateTypeDesc;
	}

	public String getEliminateUserId() {
		return eliminateUserId;
	}

	public void setEliminateUserId(String eliminateUserId) {
		this.eliminateUserId = eliminateUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPrjlst() {
		return prjlst;
	}

	public void setPrjlst(String prjlst) {
		this.prjlst = prjlst;
	}

}
