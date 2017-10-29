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

@Entity
@Table(name = "CRM_T_PRICE_LAYER_COMHEADER")
public class PriceLayCompareHeader extends BaseEntity implements  Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarpricelayercomph_id_generator")
	@GenericGenerator(name="kstarpricelayercomph_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 层级对照表名称
	@Column(name = "C_LAYER_COMPARE_NAME")
	private String layCompName;

	// 有效期开始时间
	@Column(name = "DT_START_DATE")
	private Date startDate;
	
	// 有效期结束时间
	@Column(name = "DT_END_DATE")
	private Date endDate;
	
	// 组织
	@Column(name = "C_ORGANIZATION")
	private String organization;
	
	// 说明
	@Column(name = "C_PRICE_COMP_DESC")
	private String priceCompDesc;
	
	//创建组织
	@Column(name = "c_create_org")
	private String createOrg;
	
	//创建人
	@Column(name = "c_creater")
	private String creater;
	
	@Transient
	private String createrName;
	
	//创建时间
	@Column(name = "c_create_date")
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayCompName() {
		return layCompName;
	}

	public void setLayCompName(String layCompName) {
		this.layCompName = layCompName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationName() {
		return this.getLovName(organization);
	}

	public String getPriceCompDesc() {
		return priceCompDesc;
	}

	public void setPriceCompDesc(String priceCompDesc) {
		this.priceCompDesc = priceCompDesc;
	}

	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String getCreateOrgName() {
		return this.getLovName(createOrg);
	}
	
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
