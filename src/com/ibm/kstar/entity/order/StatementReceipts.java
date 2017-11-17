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
 * 对账单发货表(CRM_T_STATEMENT_RECEIPTS)
 * 
 * @author bianj
 * @version 1.0.0 2016-12-27
 */
@Entity
@Table(name = "crm_t_statement_receipts")
public class StatementReceipts implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "statement_r_pid_generator")
   	@GenericGenerator(name="statement_r_pid_generator", strategy="uuid")
    private String id;
    
    /** 对账申请编号 */
    @Column(name = "c_statement_code")
    private String statementCode;
    
    /** 收款单编号 */
    @Column(name = "c_receipts_code")
    private String receiptsCode;
    
    /** 收款日期 */
    @Column(name = "dt_receipts_date")
    private Date receiptsDate;
    
    /** 收款银行 */
    @Column(name = "c_receipts_bank")
    private String receiptsBank;
    
    /** 币种 */
    @Column(name = "c_currency")
    private String currency;
    
    /** 收款金额 */
    @Column(name = "n_receipts_amount")
    private BigDecimal receiptsAmount;
    
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;
    
    
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

	public String getReceiptsCode() {
		return receiptsCode;
	}

	public void setReceiptsCode(String receiptsCode) {
		this.receiptsCode = receiptsCode;
	}

	public Date getReceiptsDate() {
		return receiptsDate;
	}

	public void setReceiptsDate(Date receiptsDate) {
		this.receiptsDate = receiptsDate;
	}

	public String getReceiptsBank() {
		return receiptsBank;
	}

	public void setReceiptsBank(String receiptsBank) {
		this.receiptsBank = receiptsBank;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getReceiptsAmount() {
		return receiptsAmount;
	}

	public void setReceiptsAmount(BigDecimal receiptsAmount) {
		this.receiptsAmount = receiptsAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
    
}