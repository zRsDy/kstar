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
@Table(name = "CRM_T_SALE_STATUS_MOD_LINE")
public class ProdSaleStatusModLine extends BaseEntity implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "prodstatusmodline_id_generator")
	@GenericGenerator(name="prodstatusmodline_id_generator", strategy="uuid")
	@Column(name="C_ID")
	private String id;
	
	@Column(name = "C_MAINTAIN_ID")
	private String maintainId;
	
	@Column(name = "C_MATER_CODE")
	private String materCode;
	
	@Column(name = "C_CURRENT_SALE_STATUS")
	private String currentSaleStatus;
	
	@Column(name = "C_SALE_STATUS")
	private String saleStatus;
	
	@Column(name = "C_REASON")
	private String reason;
	
	@Column(name = "C_REMARK")
	private String remark;
	
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

	public String getMaterCode() {
		return materCode;
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public String getCurrentSaleStatus() {
		return currentSaleStatus;
	}

	public void setCurrentSaleStatus(String currentSaleStatus) {
		this.currentSaleStatus = currentSaleStatus;
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public String getCurrentSaleStatusName(){
		return this.getLovName(currentSaleStatus);
	}
	
	public String getSaleStatusName(){
		return this.getLovName(saleStatus);
	}
}
