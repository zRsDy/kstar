package com.ibm.kstar.entity.order.vo;


import java.math.BigDecimal;
import java.util.Date;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderLines;

/**
 * 
 * ClassName: 订单VO<br/>
 * date: 2017年1月5日 下午4:53:29 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 */
public class OrderVO{
	/**行ID */
	private String id ;
	/**产品ID */
	private String proId ;
	/**业务实体 */
	private String businessEntity;
	/**订单CODE */
	private String orderCode;
	/**订单类型 */
	private String orderType;
	/**客户/下单客户ID */
	private String customerId;
	/**客户/下单客户编码 */
	private String customerCode;
	/**客户/下单客户名称 */
	private String customerName ;
	/**客户PO */
	private String customerPO ;
	/**客户地址ID */
	private String deliveryAddressId ;
	/**客户地址 */
	private String deliveryAddress ;
	/**订单来源 */
	private String sourceType ;
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	private String sourceCode ;
	/**订单来源名称 */
	private String sourceName ;
	/**产品价格 */
	private BigDecimal price ;
	/**产品型号 */
	private String proModel;
	/**物料编号 */
	private String materielCode;
	/**产品名称 */
	private String itemDescription;
	/**产品说明 */
	private String proDesc;
	/**单位 */
	private String unit;
	/**订单产品数量 */
	private double proQty;
	/**发货数量 */
	private double deliveryQty;
	/**未发货数量 */
    private double nonDeliveryQty ;
    /**开票数量 */
    private double billingQty ;
    /**取消数量 */
    private double cancelQty ;
    
    /**下单日期 */
	private Date orderDate;
	/**请求日期 */
	private Date requestDate;
	/**承诺日期 */
	private Date promiseDate;
	/**订单行状态 */
	private String status;
	/**订单行号*/
	private String lineNo;
	/**订单行金额 */
	private BigDecimal amount;
	/**发货组织 */
	private String shipOrg;
	/**ERP订单编号 */
	private String erpOrderCode;

	/** 销售人员名称*/
	private String salesmanName;
	/** 销售人员所属中心 */
	private String salesmanCenter;
	/** 销售人员所属部门 */
	private String salesmanDep;
	/** 商务专员名称 */
	private String businessManagerName;
	/** 是否提前开票 */
	private String isAdvanceBilling;
	/** 是否需要启动交期确认 */
	private String confirmDeliveryDate;
	/** 特价编号 */
    private String spCode;
    /** 是否特价 */
    private String isSp;
    /** ERP状态 */
    private String erpStatus;
    /** ERP是否已发货 */
    private String isErpDelivery;
    /** 备注 */
	private String remark;
	 /** 备注 */
	private String erpLineNo;
	
	/** 订单类型显示名称   */
	private String orderTypeLable;
	/** 订单来源显示名称   */
	private String sourceTypeLable ;
	/** 发货组织 显示名称*/
	private String shipOrgLable;
	/**订单行状态显示名称*/
	private String statusLable;
	/**订单行状态显示名称*/
	private String businessEntityLable;
	/** 单位 显示名称*/
	private String unitLable;
	
	/** 销售人员所属中心*/
	private String salesmanCenterLable;
	/** 销售人员所属部门 */
	private String salesmanDepLable;
	 /** ERP状态 */
    private String erpStatusLable;
	
	
	public OrderVO(){}

	public OrderVO(String id, String proId, String businessEntity,
			String orderCode, String orderType, String customerId,
			String customerCode, String customerName, String customerPO,
			String deliveryAddressId, String deliveryAddress,
			String sourceType, String sourceCode, String sourceName,
			BigDecimal price, String proModel, String materielCode,
			String itemDescription, String proDesc , String unit, double proQty, double deliveryQty,
			double nonDeliveryQty, double billingQty , Date orderDate, Date requestDate,
			Date promiseDate, String status, String lineNo, BigDecimal amount,
			String shipOrg,String erpOrderCode) {
		this.id = id;
		this.proId = proId;
		this.businessEntity = businessEntity;
		this.orderCode = orderCode;
		this.orderType = orderType;
		this.customerId = customerId;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.customerPO = customerPO;
		this.deliveryAddressId = deliveryAddressId;
		this.deliveryAddress = deliveryAddress;
		this.sourceType = sourceType;
		this.sourceCode = sourceCode;
		this.sourceName = sourceName;
		this.price = price;
		this.proModel = proModel;
		this.materielCode = materielCode;
		this.itemDescription = itemDescription;
		this.proDesc = proDesc;
		this.unit = unit;
		this.proQty = proQty;
		this.deliveryQty = deliveryQty;
		this.nonDeliveryQty = nonDeliveryQty;
		this.billingQty = billingQty;
		this.orderDate = orderDate;
		this.requestDate = requestDate;
		this.promiseDate = promiseDate;
		this.status = status;
		this.lineNo = lineNo;
		this.amount = amount;
		this.shipOrg = shipOrg;
		this.erpOrderCode = erpOrderCode;
		
	}
	
	private OrderHeader orderHeader;
	private OrderLines orderLines;
	
	public OrderVO(OrderHeader orderHeader, OrderLines orderLines){
		this.id = orderLines.getId();
		this.businessEntity = orderHeader.getBusinessEntity();
		this.orderCode = orderHeader.getOrderCode();
		this.orderType = orderHeader.getOrderType();
		this.customerId = orderHeader.getCustomerId();
		this.customerCode = orderHeader.getCustomerCode();
		this.customerName = orderHeader.getCustomerName();
		this.customerPO = orderHeader.getCustomerPo();
		this.deliveryAddressId = orderHeader.getDeliveryAddressId();
		this.deliveryAddress = orderHeader.getDeliveryAddress();
		this.sourceType = orderHeader.getSourceType();
		this.sourceName = orderHeader.getSourceName();
		this.orderDate = orderHeader.getOrderDate();
		this.erpOrderCode = orderHeader.getErpOrderCode();
		this.salesmanCenter = orderHeader.getSalesmanCenter();
		this.salesmanDep = orderHeader.getSalesmanDep();
		this.salesmanName = orderHeader.getSalesmanName();
		this.businessManagerName = orderHeader.getBusinessManagerName();
	
		this.proId =orderLines.getProId();
		this.price = orderLines.getErpSettPrice();
		this.proModel = orderLines.getProModel();
		this.materielCode = orderLines.getMaterielCode();
		this.itemDescription = orderLines.getItemDescription();
		this.proDesc = orderLines.getProDesc();
		this.unit = orderLines.getUnit();
		this.proQty = orderLines.getProQty();
		this.deliveryQty = orderLines.getDeliveryQty();
		this.nonDeliveryQty =  orderLines.getProQty() - orderLines.getDeliveryQty();
		this.billingQty = orderLines.getBillingQty();
		this.requestDate = orderLines.getRequestDate();
		this.promiseDate = orderLines.getPromiseDate();
		this.status = orderLines.getStatus();
		this.lineNo = orderLines.getLineNo();
		this.amount = orderLines.getAmount();
		this.shipOrg = orderLines.getShipOrg();
		this.sourceCode = orderLines.getSourceCode();
		this.isAdvanceBilling = orderLines.getIsAdvanceBilling();
		this.confirmDeliveryDate = orderLines.getConfirmDeliveryDate();
		this.spCode = orderLines.getSpCode();
		this.isSp = orderLines.getIsSp();
		this.erpStatus = orderLines.getErpStatus();
		this.isErpDelivery = orderLines.getIsErpDelivery();
		this.remark = orderLines.getRemark();
		this.erpLineNo = orderLines.getErpLineNo();
		this.cancelQty = orderLines.getCancelQty();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerPO() {
		return customerPO;
	}

	public void setCustomerPO(String customerPO) {
		this.customerPO = customerPO;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
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

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getProQty() {
		return proQty;
	}

	public void setProQty(double proQty) {
		this.proQty = proQty;
	}

	public double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public double getNonDeliveryQty() {
		return nonDeliveryQty;
	}

	public void setNonDeliveryQty(double nonDeliveryQty) {
		this.nonDeliveryQty = nonDeliveryQty;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}


	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getPromiseDate() {
		return promiseDate;
	}

	public void setPromiseDate(Date promiseDate) {
		this.promiseDate = promiseDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getShipOrg() {
		return shipOrg;
	}

	public void setShipOrg(String shipOrg) {
		this.shipOrg = shipOrg;
	}
	
	public String getErpOrderCode() {
		return erpOrderCode;
	}

	public void setErpOrderCode(String erpOrderCode) {
		this.erpOrderCode = erpOrderCode;
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

	public String getSourceTypeLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDERSOURCE", sourceType);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setSourceTypeLable(String sourceTypeLable) {
		this.sourceTypeLable = sourceTypeLable;
	}
	
	public String getShipOrgLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(shipOrg);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}
	public void setShipOrgLable(String shipOrgLable) {
		this.shipOrgLable = shipOrgLable;
	}

	public String getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDER_LINE_STATUS", status);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}
	
	public String getErpStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDER_ERP_STATUS", erpStatus);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setErpStatusLable(String erpStatusLable) {
		this.erpStatusLable = erpStatusLable;
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
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

	public String getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}

	public String getBusinessEntityLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(businessEntity);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setBusinessEntityLable(String businessEntityLable) {
		this.businessEntityLable = businessEntityLable;
	}

	public String getUnitLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(unit);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setUnitLable(String unitLable) {
		this.unitLable = unitLable;
	}

	public double getBillingQty() {
		return billingQty;
	}

	public void setBillingQty(double billingQty) {
		this.billingQty = billingQty;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getSalesmanCenter() {
		return salesmanCenter;
	}

	public void setSalesmanCenter(String salesmanCenter) {
		this.salesmanCenter = salesmanCenter;
	}

	public String getSalesmanDep() {
		return salesmanDep;
	}

	public void setSalesmanDep(String salesmanDep) {
		this.salesmanDep = salesmanDep;
	}

	public String getBusinessManagerName() {
		return businessManagerName;
	}

	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}

	public String getIsAdvanceBilling() {
		return isAdvanceBilling;
	}

	public void setIsAdvanceBilling(String isAdvanceBilling) {
		this.isAdvanceBilling = isAdvanceBilling;
	}

	public String getConfirmDeliveryDate() {
		return confirmDeliveryDate;
	}

	public void setConfirmDeliveryDate(String confirmDeliveryDate) {
		this.confirmDeliveryDate = confirmDeliveryDate;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
	}

	public String getIsErpDelivery() {
		return isErpDelivery;
	}

	public void setIsErpDelivery(String isErpDelivery) {
		this.isErpDelivery = isErpDelivery;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsSp() {
		return isSp;
	}

	public void setIsSp(String isSp) {
		this.isSp = isSp;
	}
	
	public String getSalesmanCenterLable() {
		return this.getLovName(salesmanCenter);
	}

	public void setSalesmanCenterLable(String salesmanCenterLable) {
		this.salesmanCenterLable = salesmanCenterLable;
	}
	
	public String getSalesmanDepLable() {
		return this.getLovName(salesmanDep);
	}

	public void setSalesmanDepLable(String salesmanDepLable) {
		this.salesmanDepLable = salesmanDepLable;
	}
	
	public String getErpLineNo() {
		return erpLineNo;
	}

	public void setErpLineNo(String erpLineNo) {
		this.erpLineNo = erpLineNo;
	}

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public OrderLines getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(OrderLines orderLines) {
		this.orderLines = orderLines;
	}

	public double getCancelQty() {
		return cancelQty;
	}

	public void setCancelQty(double cancelQty) {
		this.cancelQty = cancelQty;
	}

	public String getLovName(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
	
	public LovMember getLovMember(String groupCode,String code){
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember(groupCode, code);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}
}