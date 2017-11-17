package com.ibm.kstar.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "CRM_T_PRODUCT_LINE")
public class KstarProductLine extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "kstarproline_id_generator")
	@GenericGenerator(name="kstarproline_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;

	// 产品分类
	@Column(name = "C_PRO_CATEGORY")
	private String cproCategory;
	
	// 产品类别
	@Column(name="C_PRO_TYPE")
	private String cproType;
	
	// 产品系列
	@Column(name="C_PRO_SERIES")
	private String proSeries;
	
	// 功率或容量
	@Column(name="C_PRO_POWCAP")
	private String cproPowcap;
	
	
	// 产品管理部
	@Column(name="C_PRO_MANAG_DEPART")
	private String proManageDep;
	
	
	// 产品线
	@Column(name="C_PRO_LINE")
	private String proLine;
	
	// 产品组
	@Column(name="C_PRO_GROUP")
	private String proGroup;
	
	// 扩展字段1
	@Column(name="C_PRO_EXTEND1")
	private String proLineExt1;
	
	// 扩展字段2
	@Column(name="C_PRO_EXTEND2")
	private String proLineExt2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProLine() {
		return proLine;
	}

	public void setProLine(String proLine) {
		this.proLine = proLine;
	}

	public String getProSeries() {
		return proSeries;
	}

	public void setProSeries(String proSeries) {
		this.proSeries = proSeries;
	}

	public String getProLineExt1() {
		return proLineExt1;
	}

	public void setProLineExt1(String proLineExt1) {
		this.proLineExt1 = proLineExt1;
	}

	public String getProLineExt2() {
		return proLineExt2;
	}

	public void setProLineExt2(String proLineExt2) {
		this.proLineExt2 = proLineExt2;
	}

	public String getCproCategory() {
		return cproCategory;
	}

	public void setCproCategory(String cproCategory) {
		this.cproCategory = cproCategory;
	}

	public String getCproType() {
		return cproType;
	}

	public void setCproType(String cproType) {
		this.cproType = cproType;
	}

	public String getProManageDep() {
		return proManageDep;
	}

	public void setProManageDep(String proManageDep) {
		this.proManageDep = proManageDep;
	}

	public String getProGroup() {
		return proGroup;
	}

	public void setProGroup(String proGroup) {
		this.proGroup = proGroup;
	}

	public String getCproPowcap() {
		return cproPowcap;
	}

	public void setCproPowcap(String cproPowcap) {
		this.cproPowcap = cproPowcap;
	}
}
