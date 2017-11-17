package com.ibm.kstar.entity.product.maintain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.product.KstarProduct;

@Entity
@Table(name = "CRM_T_PROD_CATALOG_MOD_LINE")
public class ProdCatalogModLine extends BaseEntity implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "prodaatalogmodline_id_generator")
	@GenericGenerator(name="prodaatalogmodline_id_generator", strategy="uuid")
	@Column(name="C_ID")
	private String id;
	
	@Column(name = "C_MAINTAIN_ID")
	private String maintainId;
	
	@Column(name = "C_OP_TYPE")
	private String opType;
	
	@Column(name = "C_MATER_CODE")
	private String materCode;
	
	@Column(name = "C_CATALOG_ID")
	private String catalogId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getMaintainId() {
		return maintainId;
	}

	public void setMaintainId(String maintainId) {
		this.maintainId = maintainId;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getMaterCode() {
		return materCode;
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getOpTypeName(){
		if("add".equals(opType)){
			return "添加产品到目录";
		}else if("del".equals(opType)){
			return "从目录移除产品";
		}
		return "";
	}
	
	public String getMaterDesc(){
		IProductService productService = (IProductService)ApplicationContextUtil.getBean("productServiceImpl");
		if(productService != null){
			KstarProduct product =  productService.queryByMaterCode(materCode);
			if(product != null){
				return product.getProDesc();
			}
		}
		return "";
	}
	
	public String getProdName(){
		IProductService productService = (IProductService)ApplicationContextUtil.getBean("productServiceImpl");
		if(productService != null){
			KstarProduct product =  productService.queryByMaterCode(materCode);
			if(product != null){
				return product.getProductName();
			}
		}
		return "";
	}
	
	public String getCatalogName(){
		return this.getLovMember(catalogId).getNamePath();
	}
}
