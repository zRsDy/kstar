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
 * 对账单发票表(CRM_T_STATEMENT_INVOICE)
 * 
 * @author bianj
 * @version 1.0.0 2016-12-27
 */
@Entity
@Table(name = "crm_t_statement_invoice")
public class StatementInvoice implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "statement_i_pid_generator")
   	@GenericGenerator(name="statement_i_pid_generator", strategy="uuid")
    private String id;
    
    /** 对账申请编号 */
    @Column(name = "c_statement_code")
    private String statementCode;
    
    /** 开票编号 */
    @Column(name = "c_invoice_code")
    private String invoiceCode;
    
    /** 开票类型 */
    @Column(name = "c_invoice_type")
    private String invoiceType;
    
    /** 金税发票号 */
    @Column(name = "c_invoice_code_gt")
    private String invoiceCodeGt;
    
    /** 币种 */
    @Column(name = "c_currency")
    private String currency;
    
    /** 开票金额 */
    @Column(name = "n_invoice_amount")
    private BigDecimal invoiceAmount;
    
    /** 应付金额 */
    @Column(name = "n_payable_amount")
    private BigDecimal payableAmount;
    
    /** 开票日期 */
    @Column(name = "dt_invoice_date")
    private Date invoiceDate;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;
    

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

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getStatementCode() {
		return statementCode;
	}

	public void setStatementCode(String statementCode) {
		this.statementCode = statementCode;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceCodeGt() {
		return invoiceCodeGt;
	}

	public void setInvoiceCodeGt(String invoiceCodeGt) {
		this.invoiceCodeGt = invoiceCodeGt;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
    
    
}