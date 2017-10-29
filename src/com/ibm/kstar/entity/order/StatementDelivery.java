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
 * 对账单发货表(CRM_T_STATEMENT_DELIVERY)
 * 
 * @author bianj
 * @version 1.0.0 2016-12-27
 */
@Entity
@Table(name = "crm_t_statement_delivery")
public class StatementDelivery implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "statement_d_pid_generator")
   	@GenericGenerator(name="statement_d_pid_generator", strategy="uuid")
    private String id;
    
    /** 对账申请编号 */
    @Column(name = "c_statement_code")
    private String statementCode;
    
    /** 发货申请编号 */
    @Column(name = "c_delivery_code")
    private String deliveryCode;
    
    /** 订单编号 */
    @Column(name = "c_order_code")
    private String orderCode;
    
    /** 发货日期 */
    @Column(name = "dt_delivery_date")
    private Date deliveryDate;
    
    /** 物料名称 */
    @Column(name = "c_materiel_name")
    private String materielName;
    
    /** 币种 */
    @Column(name = "c_currency")
    private String currency;
    
    /** 单价 */
    @Column(name = "n_price")
    private BigDecimal price;
    
    /** 发货数量 */
    @Column(name = "n_delivery_qty")
    private BigDecimal deliveryQty;
    
    /** 应付金额 */
    @Column(name = "n_payable_amount")
    private BigDecimal payableAmount;
    
    
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

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(BigDecimal deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public BigDecimal getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}
    
  
}