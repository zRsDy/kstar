package com.ibm.kstar.entity.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * ClassName:InvoiceLines<br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * 
 * Date: 2016年12月16日 上午10:22:17 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 * @see
 */
@Entity
@Table(name = "CRM_T_DELIVERY_LINES")
public class DeliveryLines implements java.io.Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	@Id
	@Column(name = "c_pid", unique = true)
	@GeneratedValue(generator = "delivery_l_id_generator")
	@GenericGenerator(name="delivery_l_id_generator", strategy="uuid")
	private String id;
	
	/** 发货申请单头ID*/
	@Column(name = "c_delivery_id")
	private String deliveryId;

	/** 发货申请单头编号 */
	@Column(name = "c_delivery_code")
	private String deliveryCode;
	
	/** 订单行行ID */
	@Column(name = "c_order_id")
	private String orderId;
	
	/** 订单行号 */
	@Column(name = "c_order_line_no")
	private String orderLineNo;
	
	/** 订单编号 */
	@Column(name = "c_order_code")
	private String orderCode;

	/** 订单类型 */
	@Column(name = "c_order_type")
	private String orderType;
    
    /** 下单客户ID */
	@Column(name = "c_single_cust_id")
	private String singleCustId;

	/** 下单客户编号 */
	@Column(name = "c_single_cust_code")
	private String singleCustCode;

	/** 下单客户编号 */
	@Column(name = "c_single_cust_name")
	private String singleCustName;

	/** 下单客户PO */
	@Column(name = "c_single_cust_po")
	private String singleCustPO;
	
	/** 单价 */
	@Column(name = "n_price")
	private BigDecimal price;

	/** 产品型号 */
	@Column(name = "c_product_model")
	private String proModel;
	
	/** 产品说明 */
	@Column(name = "c_pro_desc")
	private String proDesc;

	/** 物料编码 */
	@Column(name = "c_materiel_code")
	private String materielCode;

	/** 物料名称/产品名称 */
	@Column(name = "c_materiel_name")
	private String materielName;

	/** 单位 */
	@Column(name = "c_unit")
	private String unit;

	/** 出货单数量 */
	@Column(name = "n_delivery_quantity")
	private double deliveryQty;
	
	/** 发货产品金额 */
	@Column(name = "n_delivery_amount")
	private BigDecimal deliveryAmount;

	/** 签收数量 */
	@Column(name = "n_receipt_quantity")
	private double receiptQty;

	/** 剩余数量 */
	@Column(name = "n_residual_quantity")
	private double residualQty;
	
	/**开票数量 */
	@Column(name = "n_invoice_quantity")
	private double invoiceQty;

	/** 下单日期 */
	@Column(name = "dt_order_date")
	private Date orderDate;

	/** 客户要货日期 */
	@Column(name = "dt_arrival_date")
	private Date arrivalDate;

	/** 工厂承诺日期 */
	@Column(name = "dt_promise_date")
	private Date promiseDate;

	/** 计划状态 */
	@Column(name = "c_plan_status")
	private String planStatus;

	/** 行号 */
	@Column(name = "c_line_num")
	private String lineNum;

	/** 出货单编号(外部) */
	@Column(name = "c_external_no")
	private String externalNo;

	/** 出货单打印日期（外部） */
	@Column(name = "dt_print_time")
	private Date printTime;

	/** 是否已过账 */
	@Column(name = "c_is_post_account")
	private String isPostAccount;
	
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_code")
	private String sourceCode;
	/** ERP订单编号 */
	@Column(name = "c_erp_order_code")
	private String erpOrderCode;
	
	
	/** 备注 */
	@Column(name = "c_remarks")
	private String remarks;
	
	/** 创建日期 */
	@Column(name = "c_create_time")
	private Date createTime;

	/** 创建人 */
	@Column(name = "c_creator")
	private String creator;
	/**
	 * 发货日期
	 */
	@Column(name = "c_delivery_date")
	private Date deliveryDate;
	
	/** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;
    
    /** 删除标志, 1.已删除，0 未删除 */ 
    @Column(name="c_delete_flag")
	private String deleteFlag = "0";
    
	/** 发货单行ERP状态 */
	@Column(name = "c_erp_status")
	private String erpStatus;
	
	/** ERP导入标志 */
	@Column(name = "c_erp_import_flag")
	private String erpImportFlag = "No";
	    
    @Transient
    private String orderTypeLable;
    
    /** 单位 显示名称*/
	@Transient
	private String unitLable;
	
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

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderLineNo() {
		return orderLineNo;
	}

	public void setOrderLineNo(String orderLineNo) {
		this.orderLineNo = orderLineNo;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getSingleCustId() {
		return singleCustId;
	}

	public void setSingleCustId(String singleCustId) {
		this.singleCustId = singleCustId;
	}

	public String getSingleCustCode() {
		return singleCustCode;
	}

	public void setSingleCustCode(String singleCustCode) {
		this.singleCustCode = singleCustCode;
	}

	public String getSingleCustName() {
		return singleCustName;
	}

	public void setSingleCustName(String singleCustName) {
		this.singleCustName = singleCustName;
	}
	
	public String getSingleCustPO() {
		return singleCustPO;
	}

	public void setSingleCustPO(String singleCustPO) {
		this.singleCustPO = singleCustPO;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}
	
	public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public double getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(double receiptQty) {
		this.receiptQty = receiptQty;
	}

	public double getResidualQty() {
		return residualQty;
	}

	public void setResidualQty(double residualQty) {
		this.residualQty = residualQty;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getPromiseDate() {
		return promiseDate;
	}

	public void setPromiseDate(Date promiseDate) {
		this.promiseDate = promiseDate;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
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

	public String getOrderTypeLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("OPERATION_UNIT", orderType);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setOrderTypeLable(String orderTypeLable) {
		this.orderTypeLable = orderTypeLable;
	}

	public String getIsPostAccount() {
		return isPostAccount;
	}

	public void setIsPostAccount(String isPostAccount) {
		this.isPostAccount = isPostAccount;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getErpOrderCode() {
		return erpOrderCode;
	}

	public void setErpOrderCode(String erpOrderCode) {
		this.erpOrderCode = erpOrderCode;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
	}

	public String getErpImportFlag() {
		return erpImportFlag;
	}

	public void setErpImportFlag(String erpImportFlag) {
		this.erpImportFlag = erpImportFlag;
	}
	

}