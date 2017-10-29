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

import com.ibm.kstar.entity.BaseEntity;

/**
 * KstarProductDoc.java
 * 产品文档实体类
 * @author Neil Wan  and rights.userId = :userId
 * 2017年1月9日 下午19:12:35
 */

@Entity
@Table(name = "CRM_T_PRODUCT_DOC")
public class KstarProductDoc extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	// 文档ID
	@Id
	@GeneratedValue(generator = "kstarprodoc_id_generator")
	@GenericGenerator(name="kstarprodoc_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;

	// 文档名称
	@Column(name = "C_DOC_NAME")
	private String docName;
	
	// 文档说明
	@Column(name = "C_DOC_DESC")
	private String docNote;
	
	// 申请人
	@Column(name = "C_APPLY_PERSON")
	private String applyPerson;

	// 申请时间
	@Column(name = "DT_APPLY_DATE")
	private Date applyTime;
	
	// 申请状态
	@Column(name = "C_APPLY_STATUS")
	private String applyStatus;
	
	// 产品ID
	@Column(name = "C_PRO_ID")
	private String productID ;
	
	// submitType
	@Transient
	private String submitType = "submit";
	
	// 创建人
	@Column(name = "C_REATED_BY_ID")
	private String createdBy;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocNote() {
		return docNote;
	}

	public void setDocNote(String docNote) {
		this.docNote = docNote;
	}

	public String getApplyPerson() {
		return applyPerson;
	}

	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}

	public String getApplyPersonName() {		
		return this.getLovName(applyPerson);
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	
	public String getApplyStatusCode() {
		return this.getLovCode(applyStatus);
	}

	public String getApplyStatusName() {
		return this.getLovName(applyStatus);
	}
	
	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	
}
