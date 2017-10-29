package com.ibm.kstar.entity.order.vo;  

import java.math.BigDecimal;
import java.util.Date;

import com.ibm.kstar.entity.order.DeliveryHeader;
import com.ibm.kstar.entity.order.DeliveryLines;
import com.ibm.kstar.entity.order.OrderLines;

/** 
 * ClassName:DeliveryVo <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年3月30日 下午7:24:35 <br/> 
 * @author   liming 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public class DeliveryVO {
	
	//主表字段
	/** 收货地点 ID*/
    private String deliveryAddressId;
    /** 收货地点 */
    private String deliveryAddress;
    /** 发货申请单头ID*/
	private String deliveryId;
	/** 发货申请单头编号 */
	private String deliveryCode;
	
	//行表字段
	/** 发货申请行ID */
	private String id;
	/** 订单行ID */
	private String orderId;
	/** 订单行号 */
	private String orderLineNo;
	/** 订单编号 */
	private String orderCode;
	/** 订单类型 */
	private String orderType;
    /** 下单客户ID */
	private String singleCustId;
	/** 下单客户编号 */
	private String singleCustCode;
	/** 下单客户编号 */
	private String singleCustName;
	/** 下单客户PO */
	private String singleCustPO;
	/** 单价 */
	private BigDecimal price;
	/** 产品型号 */
	private String proModel;
	/** 物料编码 */
	private String materielCode;
	/** 物料名称/产品名称 */
	private String materielName;
	/** 单位 */
	private String unit;
	/** 出货单数量 */
	private double deliveryQty;
	/** 发货产品金额 */
	private BigDecimal deliveryAmount;
	/** 签收数量 */
	private double receiptQty;
	/** 剩余数量 */
	private double residualQty;
	/**开票数量 */
	private double invoiceQty;
	/** 下单日期 */
	private Date orderDate;
	/** 客户要货日期 */
	private Date arrivalDate;
	/** 工厂承诺日期 */
	private Date promiseDate;
	/** 计划状态 */
	private String planStatus;
	/** 行号 */
	private String lineNum;
	/** 出货单编号(外部) */
	private String externalNo;
	/** 出货单打印日期（外部） */
	private Date printTime;
	/** 备注 */
	private String remarks;
	/** 创建日期 */
	private Date createTime;
	/** 创建人 */
	private String creator;
	/** 发货日期 */
	private Date deliveryDate;
	/** 更新者 */
    private String updatedById;
    /** 更新时间 */
    private Date updatedAt;
    
    private String orderTypeLable;
    /** 单位 显示名称*/
	private String unitLable;
    /** ERP是否已发货 */
	private String isErpDelivery;
	
    private DeliveryHeader header;
    private DeliveryLines lines;
    public DeliveryVO(DeliveryHeader header,DeliveryLines lines) {
    	init(header,lines);
	}
    public DeliveryVO(DeliveryHeader header,DeliveryLines lines,OrderLines orderLines) {
    	init(header,lines);
    	this.isErpDelivery = orderLines.getIsErpDelivery();
	}
    private void init(DeliveryHeader header,DeliveryLines lines){
    	this.deliveryId = header.getId();
		this.deliveryCode = header.getDeliveryCode();
		this.deliveryAddressId = header.getDeliveryAddressId();
		this.deliveryAddress = header.getDeliveryAddress();
		
		this.id = lines.getId();
		this.orderId = lines.getOrderId();
		this.orderLineNo = lines.getOrderLineNo();
		this.orderCode = lines.getOrderCode();
		this.orderType = lines.getOrderType();
		this.singleCustId = lines.getSingleCustId();
		this.singleCustCode = lines.getSingleCustCode();
		this.singleCustName = lines.getSingleCustName();
		this.singleCustPO = lines.getSingleCustPO();
		this.price = lines.getPrice();
		this.proModel = lines.getProModel();
		this.materielCode = lines.getMaterielCode();
		this.materielName = lines.getMaterielName();
		this.unit = lines.getUnit();
		this.deliveryQty = lines.getDeliveryQty();
		this.deliveryAmount = lines.getDeliveryAmount();
		this.receiptQty = lines.getReceiptQty();
		this.residualQty = lines.getResidualQty();
		this.invoiceQty = lines.getInvoiceQty();
		this.orderDate = lines.getOrderDate();
		this.arrivalDate = lines.getArrivalDate();
		this.promiseDate = lines.getPromiseDate();
		this.planStatus = lines.getPlanStatus();
		this.lineNum = lines.getLineNum();
		this.externalNo = lines.getExternalNo();
		this.printTime = lines.getPrintTime();
		this.remarks = lines.getRemarks();
		this.createTime = lines.getCreateTime();
		this.creator = lines.getCreator();
		this.deliveryDate = lines.getDeliveryDate();
		this.updatedById = lines.getUpdatedById();
		this.updatedAt = lines.getUpdatedAt();
		this.orderTypeLable = lines.getOrderTypeLable();
		this.unitLable = lines.getUnitLable();
    }
	public String getDeliveryAddressId() {
		return deliveryAddressId;
	}
	public void setDeliveryAddressId(String deliveryAddressId) {
		this.deliveryAddressId = deliveryAddressId;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public double getInvoiceQty() {
		return invoiceQty;
	}
	public void setInvoiceQty(double invoiceQty) {
		this.invoiceQty = invoiceQty;
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
	public String getOrderTypeLable() {
		return orderTypeLable;
	}
	public void setOrderTypeLable(String orderTypeLable) {
		this.orderTypeLable = orderTypeLable;
	}
	public DeliveryHeader getHeader() {
		return header;
	}
	public void setHeader(DeliveryHeader header) {
		this.header = header;
	}
	public DeliveryLines getLines() {
		return lines;
	}
	public void setLines(DeliveryLines lines) {
		this.lines = lines;
	}
	public String getUnitLable() {
		return unitLable;
	}
	public void setUnitLable(String unitLable) {
		this.unitLable = unitLable;
	}
	public String getIsErpDelivery() {
		return isErpDelivery;
	}
	public void setIsErpDelivery(String isErpDelivery) {
		this.isErpDelivery = isErpDelivery;
	}

}
  
