package com.ibm.kstar.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;
@Entity
@Table(name = "CRM_T_PRODUCT_CLIENT")
public class KstarProductClient extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 客户ID
	@Id
	@GeneratedValue(generator = "kstarproclient_id_generator")
	@GenericGenerator(name="kstarproclient_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 客户名称(CRM客户名称)
	@Column(name="C_CLIENT_NAME")
	private String clientName;
	
	// 客户编码(CRM客户编码)
	@Column(name="C_CLIENT_CODE")
	private String clientCode;
	
	// 客户型号(客户产品型号)
	@Column(name="C_CLIENT_MODEL")
	private String clientModel;
	
	// 客户品牌
	@Column(name="C_CLIENT_BRAND")
	private String clientBrand;
	
	// 备注说明
	@Column(name="C_CLIENT_REMARK")
	private String clientRemark;
	
	// 产品ID
	@Column(name="C_PRO_ID")
	private String productID;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientModel() {
		return clientModel;
	}

	public void setClientModel(String clientModel) {
		this.clientModel = clientModel;
	}

	public String getClientBrand() {
		return clientBrand;
	}

	public void setClientBrand(String clientBrand) {
		this.clientBrand = clientBrand;
	}

	public String getClientRemark() {
		return clientRemark;
	}

	public void setClientRemark(String clientRemark) {
		this.clientRemark = clientRemark;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}
}
