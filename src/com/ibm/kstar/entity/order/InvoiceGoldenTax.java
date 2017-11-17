package com.ibm.kstar.entity.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * ClassName: InvoiceGoldenTax <br/> 
 * Function: 开票-金税明细表. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年4月28日 下午3:25:08 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_invoice_golden_tax")
public class InvoiceGoldenTax implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "invoice_g_id_generator")
   	@GenericGenerator(name="invoice_g_id_generator", strategy="uuid")
    private String id;
    
    /** 开票申请主表ID */
    @Column(name = "c_invoice_id")
    private String invoiceId;
    
    /** 开票申请编号 */
    @Column(name = "c_invoice_code")
    private String invoiceCode;
    
    /** 物料说明/品名  */
    @Column(name = "c_materiel_desc")
    private String materielDesc;
    
    /** 规格型号  */
    @Column(name = "c_pro_model")
    private String proModel;
    
    /** 单位 */
	@Column(name = "c_unit")
	private String unit;
    
    /** 开票数量 */
    @Column(name = "n_invoice_qty")
    private double invoiceQty;
    
    /** 开票单价 */
    @Column(name = "n_invoice_price")
    private BigDecimal invoicePrice;
    
    /** 开票金额 */
    @Column(name = "n_invoice_amount")
    private BigDecimal invoiceAmount;
    
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;

    /** 发票单号 */
    @Column(name = "c_invoice_no")
    private String invoiceNo;
    
    /** 客户名称 */
    @Column(name = "c_cust_name")
    private String custName;
    
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getMaterielDesc() {
		return materielDesc;
	}

	public void setMaterielDesc(String materielDesc) {
		this.materielDesc = materielDesc;
	}

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getInvoiceQty() {
		return invoiceQty;
	}

	public void setInvoiceQty(double invoiceQty) {
		this.invoiceQty = invoiceQty;
	}

	public BigDecimal getInvoicePrice() {
		return invoicePrice;
	}

	public void setInvoicePrice(BigDecimal invoicePrice) {
		this.invoicePrice = invoicePrice;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
  
}