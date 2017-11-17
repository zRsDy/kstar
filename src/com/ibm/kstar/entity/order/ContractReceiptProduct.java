package com.ibm.kstar.entity.order;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 合同收款计划明细-产品
 * 
 * @author IBM
 *
 */
@Entity
@Table(name = "crm_t_contract_receipt_product")
public class ContractReceiptProduct implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	@Id
	@Column(name = "c_pid", unique = true)
	@GeneratedValue(generator = "contract_receipt_product_generator")
	@GenericGenerator(name = "contract_receipt_product_generator", strategy = "uuid")
	@PermissionBusinessId
	private String id;

	/** 核销ID */
	@Column(name = "C_DETAIL_ID")
	private String detailId;

	/** 合同ID */
	@Column(name = "C_CONTR_ID")
	private String contrId;

	/** 合同行ID */
	@Column(name = "C_CONTR_LINE_ID")
	private String contrIdLineId;

	/** 合同ID */
	@Column(name = "C_PRODUCT_ID")
	private String productId;

	/** 核销数量 */
	@Column(name = "C_QUANTITY")
	private BigDecimal quantity;
	
	/** 核销价格 */
	@Column(name = "C_PRICE")
	private BigDecimal price;
	
	/**
	 * 备注
	 */
	@Column(name = "C_NOTES")
	private String notes;
	
	/**
	 * 本次核销数量
	 */
	@Transient
	private BigDecimal quantityA;
	
	/**
	 * 未核销数量
	 */
	@Transient
	private BigDecimal quantityB;
	
	public BigDecimal getQuantityA() {
		return quantityA;
	}

	public void setQuantityA(BigDecimal quantityA) {
		this.quantityA = quantityA;
	}

	public BigDecimal getQuantityB() {
		return quantityB;
	}

	public void setQuantityB(BigDecimal quantityB) {
		this.quantityB = quantityB;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getContrId() {
		return contrId;
	}

	public void setContrId(String contrId) {
		this.contrId = contrId;
	}

	public String getContrIdLineId() {
		return contrIdLineId;
	}

	public void setContrIdLineId(String contrIdLineId) {
		this.contrIdLineId = contrIdLineId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}