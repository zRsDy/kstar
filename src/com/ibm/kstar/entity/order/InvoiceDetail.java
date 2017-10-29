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

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 开票明细表(CRM_T_INVOICE_DETAIL)
 * 
 * @author bianj
 * @version 1.0.0 2016-12-27
 */
@Entity
@Table(name = "crm_t_invoice_detail")
public class InvoiceDetail implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "invoice_d_id_generator")
   	@GenericGenerator(name="invoice_d_id_generator", strategy="uuid")
    private String id;
    
    /** 开票申请主表ID */
    @Column(name = "c_invoice_id")
    private String invoiceId;
    
    /** 开票申请编号 */
    @Column(name = "c_invoice_code")
    private String invoiceCode;
    
    /** 开票类型 */
    @Column(name = "c_invoice_type")
    private String invoiceType;
    
    /** 订单编号 */
    @Column(name = "c_order_code")
    private String orderCode;
    
    /** 订单行ID */
    @Column(name = "c_order_line_id")
    private String orderLineId;
    
    /** 发货单编号 */
    @Column(name = "c_delivery_code")
    private String deliveryCode;
    
    /** 发货单行ID */
    @Column(name = "c_delivery_line_id")
    private String deliveryLineId;
    
    /** 客户编号 */
    @Column(name = "c_cust_code")
    private String custCode;
    
    /** 客户名称 */
    @Column(name = "c_cust_name")
    private String custName;
    
    /** 客户地址 */
    @Column(name = "c_cust_addr")
    private String custAddr;
    
    /** 客户PO */
    @Column(name = "c_cust_po")
    private String custPO;
    
    /** 物料编码 */
    @Column(name = "c_materiel_code")
    private String materielCode;
    
    /** 物料说明 */
    @Column(name = "c_materiel_desc")
    private String materielDesc;
    
    /** 规格型号  */
    @Column(name = "c_pro_model")
    private String proModel;
    
    /** 单位 */
	@Column(name = "c_unit")
	private String unit;
    
    /** 发货日期 */
    @Column(name = "dt_delivery_date")
    private Date deliveryDate;
    
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
    
    /** 创建日期 */
    @Column(name = "dt_create_time")
    private Date createTime;
    
    /** 创建人 */
    @Column(name = "c_creator")
    private String creator;

    /** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;
    
    @Transient
    private String invoiceTypeLable;
    
   /** 单位 显示名称*/
	@Transient
	private String unitLable;
    

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

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getDeliveryLineId() {
		return deliveryLineId;
	}

	public void setDeliveryLineId(String deliveryLineId) {
		this.deliveryLineId = deliveryLineId;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAddr() {
		return custAddr;
	}

	public void setCustAddr(String custAddr) {
		this.custAddr = custAddr;
	}

	public String getCustPO() {
		return custPO;
	}

	public void setCustPO(String custPO) {
		this.custPO = custPO;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getMaterielDesc() {
		return materielDesc;
	}

	public void setMaterielDesc(String materielDesc) {
		this.materielDesc = materielDesc;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getInvoiceTypeLable() {
		String lable ="";
		if(IConstants.INVOICE_TYPE_01.equals(invoiceType)){
			lable = "提前开票";
		}else{
			lable = "已出货开票";
		}
		return lable;
	}

	public void setInvoiceTypeLable(String invoiceTypeLable) {
		this.invoiceTypeLable = invoiceTypeLable;
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
	
	public String getUnitLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("UNIT", unit);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setUnitLable(String unitLable) {
		this.unitLable = unitLable;
	}
}