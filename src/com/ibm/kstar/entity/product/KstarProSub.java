package com.ibm.kstar.entity.product;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

@Entity
@Table(name = "CRM_T_PRODUCT_SUB")
public class KstarProSub extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarprosub_id_generator")
	@GenericGenerator(name="kstarprosub_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 主产品ID
	@Column(name = "C_PRO_ID")
	private String productID;
	
	// 选配产品ID
	@Column(name = "C_PRO_SUB_ID")
	private String proSubID;

	// 创建日期
	@Column(name = "DT_CREATE_DATE")
	private String createDate;
	
	// 产品主辅关系说明
	@Column(name = "C_PRO_SUB_DESC")
	private String proSubDesc;

	@Transient
	private KstarProduct productBean = new KstarProduct();
	
	public KstarProSub() {
	}
	
	public KstarProSub(KstarProSub ps, KstarProduct p) {
		this.id = ps.getId();
		this.proSubDesc = ps.getProSubDesc();
		this.productBean = p;
	}


	public KstarProduct getProductBean() {
		return productBean;
	}

	public void setProductBean(KstarProduct productBean) {
		this.productBean = productBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProSubID() {
		return proSubID;
	}

	public void setProSubID(String proSubID) {
		this.proSubID = proSubID;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getProSubDesc() {
		return proSubDesc;
	}

	public void setProSubDesc(String proSubDesc) {
		this.proSubDesc = proSubDesc;
	}
}
