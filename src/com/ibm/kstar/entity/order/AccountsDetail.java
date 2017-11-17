package com.ibm.kstar.entity.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 
 * ClassName: CrmTAccountsDetailEntity 账期申请明细表 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年1月4日 上午9:21:02 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_accounts_detail")
public class AccountsDetail implements java.io.Serializable {
    
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
	@GeneratedValue(generator = "accounts_d_id_generator")
	@GenericGenerator(name="accounts_d_id_generator", strategy="uuid")
    private String id;
    
    /** 账期申请主表ID */
    @Column(name = "c_accounts_id")
    private String accountsId;
    /** 账期申请主表编号 */
    @Column(name = "c_accounts_code")
    private String accountsCode;
    
    /** 商机编号 */
    @Column(name = "c_bo_code")
    private String boCode;
    
    /** 商机名称 */
    @Column(name = "c_bo_name")
    private String boName;
    
    /** 订单编号 */
    @Column(name = "c_order_code")
    private String orderCode;
    
    /** 发货日期 */
    @Column(name = "dt_delivery_date")
    private Date deliveryDate;
    
    /** 物料编码 */
    @Column(name = "c_materiel_code")
    private String materielCode;
    
    /** 物料名称 */
    @Column(name = "c_materiel_name")
    private String materielName;
    
    /** 单价 */
    @Column(name = "n_price")
    private BigDecimal price;
    
    /** 金额 */
    @Column(name = "n_amount")
    private BigDecimal amount;
    
    /** 币种 */
    @Column(name = "c_currency")
    private String currency;
    
    /** 发货数量 */
    @Column(name = "n_qty")
    private double qty;
    
    /** 是否发货 */
    @Column(name = "c_is_delivery")
    private String isDelivery;
    
    /** 是否开票 */
    @Column(name = "c_is_invoice")
    private String isInvoice;
    
    /** 是否逾期 */
    @Column(name = "c_is_delay")
    private String isDelay;
    
    /** 应付日期 */
    @Column(name = "n_payable_date")
    private Date payableDate;
    
    /** 延期至 */
    @Column(name = "dt_delay_date")
    private Date delayDate;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;
    
    @Transient
    /** 是否逾期显示名称 */
    private String isDelayLable;
    

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

	public String getAccountsId() {
		return accountsId;
	}

	public void setAccountsId(String accountsId) {
		this.accountsId = accountsId;
	}

	public String getAccountsCode() {
		return accountsCode;
	}

	public void setAccountsCode(String accountsCode) {
		this.accountsCode = accountsCode;
	}

	public String getBoCode() {
		return boCode;
	}

	public void setBoCode(String boCode) {
		this.boCode = boCode;
	}

	public String getBoName() {
		return boName;
	}

	public void setBoName(String boName) {
		this.boName = boName;
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

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(String isDelivery) {
		this.isDelivery = isDelivery;
	}

	public String getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getIsDelay() {
		return isDelay;
	}

	public void setIsDelay(String isDelay) {
		this.isDelay = isDelay;
	}

	public Date getPayableDate() {
		return payableDate;
	}

	public void setPayableDate(Date payableDate) {
		this.payableDate = payableDate;
	}

	public Date getDelayDate() {
		return delayDate;
	}

	public void setDelayDate(Date delayDate) {
		this.delayDate = delayDate;
	}

	public String getIsDelayLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("NY", "0");
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setIsDelayLable(String isDelayLable) {
		this.isDelayLable = isDelayLable;
	}
    
   
}