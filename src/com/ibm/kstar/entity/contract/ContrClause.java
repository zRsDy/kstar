package com.ibm.kstar.entity.contract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;



/**
 * KstarContract.java 产品实体类
 * 
 * @author Neil Wan and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "CRM_T_CONTR_CLAUSE")


public class ContrClause implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658308573164369043L;

	// ID

	@Id
	@GeneratedValue(generator = "crm_t_contr_clause_id_generator")
	@GenericGenerator(name = "crm_t_contr_clause_id_generator", strategy = "uuid")
	@Column(name = "C_ID")
	private String id;
	
		
	@Column(name = "C_CONTR_NO")
	private String contrNo;
	
	@Column(name = "C_CONTR_NAME")
	private String contrName;
	
	@Column(name = "C_CONTR_ID")
	private String contrId;
	
	@Column(name = "C_CLAUSE_TYPE_Id")
	private String clauseType;
	
	@Transient
	private String clauseTypeDesc;
	
	
	@Column(name = "C_CLAUSE_DESC")
	private String clauseDesc;	
	
	
	
	@Column(name = "C_REMARK")
	private String remark;

	

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContrNo() {
		return contrNo;
	}

	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}

	public String getContrName() {
		return contrName;
	}

	public void setContrName(String contrName) {
		this.contrName = contrName;
	}

	public String getClauseType() {
		return clauseType;
	}

	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}

	public String getClauseDesc() {
		return clauseDesc;
	}

	public void setClauseDesc(String clauseDesc) {
		this.clauseDesc = clauseDesc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	 public String getClauseTypeDesc() {
	// return paySeqDesc;
	 LovMember lov = ((LovMember)CacheData.getInstance().get(clauseType));
	 return lov==null? null : lov.getName();
	 }

	public String getContrId() {
		return contrId;
	}

	public void setContrId(String contrId) {
		this.contrId = contrId;
	}

	
}
