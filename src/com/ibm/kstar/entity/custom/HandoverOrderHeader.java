package com.ibm.kstar.entity.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

@Entity
@Table(name = "crm_t_ho_order_header")
public class HandoverOrderHeader implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "ho_order_h_id_generator")
	@GenericGenerator(name = "ho_order_h_id_generator", strategy = "uuid")
	private String id;
	
	/** 交接单据ID */
    @Column(name = "C_BUSINESS_ID", nullable = true, length = 32)
    private String businessId;
    
    /** 交接单据状态 */
    @Column(name = "C_BUSINESS_STATUS", nullable = true, length = 32)
    private String businessStatus;
    
    @Transient
	private String businessStatusName;

	public String getBusinessStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(businessStatus));
		return lov == null ? null : lov.getName();
	}

	public void setBusinessStatusName(String businessStatusName) {
		this.businessStatusName = businessStatusName;
	}
    
    /** 交接单据状态 */
    @Column(name = "C_CHECK_STATUS", nullable = true, length = 32)
    private String checkStatus;
    
    @Transient
	private String checkStatusName;
    
    public String getCheckStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(checkStatus));
		
		return lov == null ? null : lov.getName();
		
	}

	public void setCheckStatusName(String checkStatusName) {
		this.checkStatusName = checkStatusName;
	}
    
    /** 交接单据原id */
    @Column(name = "C_BASE_ID", nullable = true, length = 32)
    private String baseId;

	/** 订单编号 */
	@Column(name = "c_order_code", nullable = false, length = 32)
	private String orderCode;

	/** 客户ID */
	@Column(name = "c_customer_id", nullable = false, length = 32)
	private String customerId;

	/** 客户编号 */
	@Column(name = "c_customer_code", nullable = false, length = 32)
	private String customerCode;

	/** 客户名称 */
	@Column(name = "c_customer_name", nullable = true, length = 300)
	private String customerName;

	/** 客户联系人编号 */
	@Column(name = "c_cust_attn_code", nullable = true, length = 300)
	private String custAttnCode;

	/** 客户联系人名称 */
	@Column(name = "c_cust_attn_name", nullable = true, length = 300)
	private String custAttnName;

	/** 客户PO */
	@Column(name = "c_customer_po", nullable = true, length = 300)
	private String customerPo;

	/** 收货地点 */
	@Column(name = "c_delivery_address_id", nullable = true, length = 300)
	private String deliveryAddressId;
	
	/** 收货地点 */
	@Column(name = "c_delivery_address", nullable = true, length = 300)
	private String deliveryAddress;
	
	/** 收单地点ID */
	@Column(name = "c_bill_address_id", nullable = true, length = 32)
	private String billAddressId;

	/** 收单地点 */
	@Column(name = "c_bill_address", nullable = true, length = 300)
	private String billAddress;

	/** 业务实体 */
	@Column(name = "c_business_entity", nullable = true, length = 300)
	private String businessEntity;
	
	/** 业务实体显示名称 */
	@Transient
	private String businessEntityLable;

	/** 发货组织 */
	@Column(name = "c_ship_org", nullable = true, length = 300)
	private String shipOrg;
	
	/** 发货组织 显示名称*/
	@Transient
	private Object shipOrgLable;
	
	
	/** 来源类型 */
	@Column(name = "c_source_type", nullable = true, length = 300)
	private String sourceType;
	
	/** 来源类型显示名称 */
	@Transient
	private Object sourceTypeLable;
	
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_id", nullable = true, length = 32)
	private String sourceId;
	
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_code", nullable = true, length = 300)
	private String sourceCode;
	
	/** 来源名称,当来源为合同时为合同名称，当来源是渠道是为商机名称*/
	@Column(name = "c_source_name", nullable = true, length = 300)
	private String sourceName;

	/** 订单类型 */
	@Column(name = "c_order_type", nullable = true, length = 300)
	private String orderType;
	/** 订单类型显示名称 */
	@Transient
	private Object orderTypeLable;
	
	/** 订购日期 */
	@Column(name = "dt_order_date", nullable = true)
	private Date orderDate;

	/** 请求日期 */
	@Column(name = "dt_request_date", nullable = true)
	private Date requestDate;

	/** 货币 */
	@Column(name = "c_currency", nullable = true, length = 32)
	private String currency;

	/** 折换类型 */
	@Column(name = "c_convert_type", nullable = true, length = 32)
	private String convertType;

	/** 折换日期 */
	@Column(name = "dt_convert_date", nullable = true)
	private Date convertDate;

	/** 价目表ID */
	@Column(name = "c_price_table_id", nullable = true, length = 32)
	private String priceTableId;
	
	/** 价目表 名称*/
	@Column(name = "c_price_table_name", nullable = true, length = 32)
	private String priceTableName;
	
	/** 合计 */
	@Column(name = "n_amount", nullable = true)
	private BigDecimal amount;

	/** 题头备注 */
	@Column(name = "c_remark", nullable = true, length = 300)
	private String remark;

	/** 销售人员 ID*/
	@Column(name = "c_salesman_id", nullable = true, length = 32)
	private String salesmanId;
	
	/** 销售人员 编号*/
	@Column(name = "c_salesman_code", nullable = true, length = 32)
	private String salesmanCode;
	
	/** 销售人员名称*/
	@Column(name = "c_salesman_name", nullable = true, length = 300)
	private String salesmanName;

	/** 销售人员所属中心 */
	@Column(name = "c_salesman_center", nullable = true, length = 32)
	private String salesmanCenter;

	/** 销售人员所属部门 */
	@Column(name = "c_salesman_dep", nullable = true, length = 300)
	private String salesmanDep;

	/** 销售区域 */
	@Column(name = "c_sales_territory", nullable = true, length = 32)
	private String salesTerritory;

	/** 执行状态 */
	@Column(name = "c_execute_status", nullable = true, length = 32)
	private String executeStatus;

	/** 控制状态 */
	@Column(name = "c_control_status", nullable = true, length = 32)
	private String controlStatus;

	/** 是否价格异常 */
	@Column(name = "n_is_abnormal_price", nullable = true, length = 1)
	private Integer isAbnormalPrice;

	/** 是否信用异常 */
	@Column(name = "n_is_abnormal_credit", nullable = true, length = 1)
	private Integer isAbnormalCredit;

	/** 付款条件 */
	@Column(name = "c_term_payment", nullable = true, length = 32)
	private String termPayment;

	/** 详情付款条款 */
	@Column(name = "c_term_payment_detail", nullable = true, length = 300)
	private String termPaymentDetail;

	/** 付款客户ID */
	@Column(name = "c_payment_customer_id", nullable = true, length = 32)
	private String paymentCustId;
	
	/** 付款客户ID */
	@Column(name = "c_payment_customer_name", nullable = true, length = 32)
	private String paymentCustName;

	/** 最终用户ID */
	@Column(name = "c_final_cust_id", nullable = true, length = 32)
	private String finalCustId;
	
	/** 最终用户ID */
	@Column(name = "c_final_cust_name", nullable = true, length = 32)
	private String finalCustName;

	/** 最终用户行业大类 */
	@Column(name = "c_final_cust_trade_b", nullable = true, length = 32)
	private String finalCustTradeB;

	/** 最终用户行业小类 */
	@Column(name = "c_final_cust_trade_s", nullable = true, length = 32)
	private String finalCustTradeS;

	/** 是否上门安装 */
	@Column(name = "c_is_install", nullable = true, length = 32)
	private String isInstall;

	/** 是否所需辅材 */
	@Column(name = "c_is_am", nullable = true, length = 32)
	private String isAm;

	/** 巡检周期 */
	@Column(name = "c_inspection_cycle", nullable = true, length = 32)
	private String inspectionCycle;

	/** 保修年限 */
	@Column(name = "c_warranty_period", nullable = true, length = 32)
	private String warrantyPeriod;

	/** 运输方式 */
	@Column(name = "c_ship_type", nullable = true, length = 32)
	private String shipType;

	/** 是否送货上门 */
	@Column(name = "c_is_home_delivery", nullable = true, length = 1)
	private String isHomeDelivery;

	/** 是否卸货到客户指定安装地点 */
	@Column(name = "c_is_destination_delivery", nullable = true, length = 1)
	private String isDestinationDelivery;

	/** 售后服务条款 */
	@Column(name = "c_service_clause", nullable = true, length = 300)
	private String serviceClause;

	/** 代收运费 */
	@Column(name = "n_collection_freight", nullable = true)
	private BigDecimal collectionFreight;

	/** 模具费 */
	@Column(name = "n_die_cost", nullable = true)
	private BigDecimal dieCost;

	/** 开发费用 */
	@Column(name = "n_development_costs", nullable = true)
	private BigDecimal developmentCosts;

	/** 客户品牌分类 */
	@Column(name = "c_cust_brand_class", nullable = true, length = 32)
	private String custBrandClass;

	/** 是否免费赠送备件 */
	@Column(name = "c_is_free_spare", nullable = true, length = 32)
	private String isFreeSpare;

	/** 是否随机发赠备件 */
	@Column(name = "c_is_random_spare", nullable = true, length = 1)
	private String isRandomSpare;

	/** 备件额度 */
	@Column(name = "n_spare_amount", nullable = true)
	private BigDecimal spareAmount;

	/** 备件剩余额度 */
	@Column(name = "n_spare_balance", nullable = true)
	private BigDecimal spareBalance;

	/** 备件额度使用情况 */
	@Column(name = "c_spare_amount_situation", nullable = true, length = 32)
	private String spareAmountSituation;

	/** 备件订单编号 */
	@Column(name = "c_spare_order_no", nullable = true, length = 32)
	private String spareOrderNo;
	
	/** ERP订单编号 */
	
	@Column(name = "c_erp_order_code", nullable = true, length = 32)
	private String erpOrderCode;
	

	@Transient
	private List<Map<Object, Object>> linesList;
	
	 /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedPosId() {
		return createdPosId;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
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
	
	public List<Map<Object, Object>> getLinesList() {
		return linesList;
	}

	public void setLinesList(List<Map<Object, Object>> linesList) {
		this.linesList = linesList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getCustAttnCode() {
		return custAttnCode;
	}

	public void setCustAttnCode(String custAttnCode) {
		this.custAttnCode = custAttnCode;
	}

	public String getCustAttnName() {
		return custAttnName;
	}

	public void setCustAttnName(String custAttnName) {
		this.custAttnName = custAttnName;
	}

	public String getCustomerPo() {
		return customerPo;
	}

	public void setCustomerPo(String customerPo) {
		this.customerPo = customerPo;
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
	
	public String getBillAddressId() {
		return billAddressId;
	}

	public void setBillAddressId(String billAddressId) {
		this.billAddressId = billAddressId;
	}

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}
	
	public Object getBusinessEntityLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(businessEntity);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setBusinessEntityLable(String businessEntityLable) {
		this.businessEntityLable = businessEntityLable;
	}

	public String getShipOrg() {
		return shipOrg;
	}

	public void setShipOrg(String shipOrg) {
		this.shipOrg = shipOrg;
	}
	
	public Object getShipOrgLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(shipOrg);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setShipOrgLable(Object shipOrgLable) {
		this.shipOrgLable = shipOrgLable;
	}

	public String getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public LovMember getSourceTypeLable() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDERSOURCE", sourceType);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setSourceTypeLable(Object sourceTypeLable) {
		this.sourceTypeLable = sourceTypeLable;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Object getOrderTypeLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDERTYPE", orderType);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setOrderTypeLable(Object orderTypeLable) {
		this.orderTypeLable = orderTypeLable;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getConvertType() {
		return convertType;
	}

	public void setConvertType(String convertType) {
		this.convertType = convertType;
	}

	public Date getConvertDate() {
		return convertDate;
	}

	public void setConvertDate(Date convertDate) {
		this.convertDate = convertDate;
	}

	public String getPriceTableId() {
		return priceTableId;
	}

	public void setPriceTableId(String priceTableId) {
		this.priceTableId = priceTableId;
	}

	public String getPriceTableName() {
		return priceTableName;
	}

	public void setPriceTableName(String priceTableName) {
		this.priceTableName = priceTableName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(String salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public String getSalesmanCode() {
		return salesmanCode;
	}

	public void setSalesmanCode(String salesmanCode) {
		this.salesmanCode = salesmanCode;
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

	public String getSalesTerritory() {
		return salesTerritory;
	}

	public void setSalesTerritory(String salesTerritory) {
		this.salesTerritory = salesTerritory;
	}

	public String getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getControlStatus() {
		return controlStatus;
	}

	public void setControlStatus(String controlStatus) {
		this.controlStatus = controlStatus;
	}

	public Integer getIsAbnormalPrice() {
		return isAbnormalPrice;
	}

	public void setIsAbnormalPrice(Integer isAbnormalPrice) {
		this.isAbnormalPrice = isAbnormalPrice;
	}

	public Integer getIsAbnormalCredit() {
		return isAbnormalCredit;
	}

	public void setIsAbnormalCredit(Integer isAbnormalCredit) {
		this.isAbnormalCredit = isAbnormalCredit;
	}

	public String getTermPayment() {
		return termPayment;
	}

	public void setTermPayment(String termPayment) {
		this.termPayment = termPayment;
	}

	public String getTermPaymentDetail() {
		return termPaymentDetail;
	}

	public void setTermPaymentDetail(String termPaymentDetail) {
		this.termPaymentDetail = termPaymentDetail;
	}

	public String getPaymentCustId() {
		return paymentCustId;
	}

	public void setPaymentCustId(String paymentCustId) {
		this.paymentCustId = paymentCustId;
	}

	public String getPaymentCustName() {
		return paymentCustName;
	}

	public void setPaymentCustName(String paymentCustName) {
		this.paymentCustName = paymentCustName;
	}
	
	public String getFinalCustId() {
		return finalCustId;
	}

	public void setFinalCustId(String finalCustId) {
		this.finalCustId = finalCustId;
	}

	public String getFinalCustName() {
		return finalCustName;
	}

	public void setFinalCustName(String finalCustName) {
		this.finalCustName = finalCustName;
	}

	public String getFinalCustTradeB() {
		return finalCustTradeB;
	}

	public void setFinalCustTradeB(String finalCustTradeB) {
		this.finalCustTradeB = finalCustTradeB;
	}

	public String getFinalCustTradeS() {
		return finalCustTradeS;
	}

	public void setFinalCustTradeS(String finalCustTradeS) {
		this.finalCustTradeS = finalCustTradeS;
	}

	public String getIsInstall() {
		return isInstall;
	}

	public void setIsInstall(String isInstall) {
		this.isInstall = isInstall;
	}

	public String getIsAm() {
		return isAm;
	}

	public void setIsAm(String isAm) {
		this.isAm = isAm;
	}

	public String getInspectionCycle() {
		return inspectionCycle;
	}

	public void setInspectionCycle(String inspectionCycle) {
		this.inspectionCycle = inspectionCycle;
	}

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String getServiceClause() {
		return serviceClause;
	}

	public void setServiceClause(String serviceClause) {
		this.serviceClause = serviceClause;
	}

	public BigDecimal getCollectionFreight() {
		return collectionFreight;
	}

	public void setCollectionFreight(BigDecimal collectionFreight) {
		this.collectionFreight = collectionFreight;
	}

	public BigDecimal getDieCost() {
		return dieCost;
	}

	public void setDieCost(BigDecimal dieCost) {
		this.dieCost = dieCost;
	}

	public BigDecimal getDevelopmentCosts() {
		return developmentCosts;
	}

	public void setDevelopmentCosts(BigDecimal developmentCosts) {
		this.developmentCosts = developmentCosts;
	}

	public String getCustBrandClass() {
		return custBrandClass;
	}

	public void setCustBrandClass(String custBrandClass) {
		this.custBrandClass = custBrandClass;
	}

	public String getIsFreeSpare() {
		return isFreeSpare;
	}

	public void setIsFreeSpare(String isFreeSpare) {
		this.isFreeSpare = isFreeSpare;
	}
	
	public String getIsHomeDelivery() {
		return isHomeDelivery;
	}

	public void setIsHomeDelivery(String isHomeDelivery) {
		this.isHomeDelivery = isHomeDelivery;
	}

	public String getIsDestinationDelivery() {
		return isDestinationDelivery;
	}

	public void setIsDestinationDelivery(String isDestinationDelivery) {
		this.isDestinationDelivery = isDestinationDelivery;
	}

	public String getIsRandomSpare() {
		return isRandomSpare;
	}

	public void setIsRandomSpare(String isRandomSpare) {
		this.isRandomSpare = isRandomSpare;
	}

	public BigDecimal getSpareAmount() {
		return spareAmount;
	}

	public void setSpareAmount(BigDecimal spareAmount) {
		this.spareAmount = spareAmount;
	}

	public BigDecimal getSpareBalance() {
		return spareBalance;
	}

	public void setSpareBalance(BigDecimal spareBalance) {
		this.spareBalance = spareBalance;
	}

	public String getSpareAmountSituation() {
		return spareAmountSituation;
	}

	public void setSpareAmountSituation(String spareAmountSituation) {
		this.spareAmountSituation = spareAmountSituation;
	}

	public String getSpareOrderNo() {
		return spareOrderNo;
	}

	public void setSpareOrderNo(String spareOrderNo) {
		this.spareOrderNo = spareOrderNo;
	}

	public String getErpOrderCode() {
		return erpOrderCode;
	}

	public void setErpOrderCode(String erpOrderCode) {
		this.erpOrderCode = erpOrderCode;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	

}