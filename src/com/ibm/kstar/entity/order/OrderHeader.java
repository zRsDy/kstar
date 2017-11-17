package com.ibm.kstar.entity.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 
 * ClassName: 订单头表(CRM_T_ORDER_HEADER) <br/>
 * date: 2017年1月6日 下午4:53:29 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_order_header")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_ORDER)
public class OrderHeader extends BaseEntity implements java.io.Serializable  {
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@Column(name = "c_pid", unique = false)
	@GeneratedValue(generator = "order_h_id_generator")
	@GenericGenerator(name = "order_h_id_generator", strategy = "uuid")
	@PermissionBusinessId
	private String id;

	/** 订单编号 */
	@Column(name = "c_order_code")
	private String orderCode;

	/** 客户ID */
	@Column(name = "c_customer_id")
	private String customerId;

	/** 客户编号 */
	@Column(name = "c_customer_code")
	private String customerCode;
	
	/** 客户ERP编号 */
	@Column(name = "c_customer_erp_code")
	private String customerErpCode;

	/** 客户名称 */
	@Column(name = "c_customer_name")
	private String customerName;

	/** 客户联系人编号 */
	@Column(name = "c_cust_attn_code")
	private String custAttnCode;

	/** 客户联系人名称 */
	@Column(name = "c_cust_attn_name")
	private String custAttnName;
	
	/** 客户联系人电话 */
	@Column(name = "c_cust_attn_tel")
	private String custAttnTel;

	/** 客户PO */
	@Column(name = "c_customer_po")
	private String customerPo;

	/** 收货地点ID */
	@Column(name = "c_delivery_address_id")
	private String deliveryAddressId;
	
	/** 收货地点 */
	@Column(name = "c_delivery_address")
	private String deliveryAddress;
	
	/** 收货地点邮编 */
	@Column(name = "c_zip_code")
	private String zipCode;
	
	/** 收单地点ID */
	@Column(name = "c_bill_address_id")
	private String billAddressId;

	/** 收单地点 */
	@Column(name = "c_bill_address")
	private String billAddress;

	/** 业务实体 */
	@Column(name = "c_business_entity")
	private String businessEntity;
	
	/** 发货组织 */
	@Column(name = "c_ship_org")
	private String shipOrg;
	
	/** 来源类型 */
	@Column(name = "c_source_type")
	private String sourceType;
	
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_id")
	private String sourceId;
	
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_code")
	private String sourceCode;
	
	/** 来源名称,当来源为合同时为合同名称，当来源是渠道是为商机名称*/
	@Column(name = "c_source_name")
	private String sourceName;

	/** 订单类型 */
	@Column(name = "c_order_type")
	private String orderType;
	
	/** 订购日期 */
	@Column(name = "dt_order_date")
	private Date orderDate;

	/** 请求日期 */
	@Column(name = "dt_request_date")
	private Date requestDate;

	/** 货币 */
	@Column(name = "c_currency")
	private String currency;

	/** 折换类型 */
	@Column(name = "c_convert_type")
	private String convertType;

	/** 折换日期 */
	@Column(name = "dt_convert_date")
	private Date convertDate;

	/** 价目表ID */
	@Column(name = "c_price_table_id")
	private String priceTableId;
	
	/** 价目表 名称*/
	@Column(name = "c_price_table_name")
	private String priceTableName;
	
	/** 合计 */
	@Column(name = "n_amount")
	private BigDecimal amount;

	/** 题头备注 */
	@Column(name = "c_remark")
	private String remark;

	/** 销售人员 ID*/
	@Column(name = "c_salesman_id")
	private String salesmanId;
	
	/** 销售人员 编号*/
	@Column(name = "c_salesman_code")
	private String salesmanCode;
	
	/** 销售人员名称*/
	@Column(name = "c_salesman_name")
	private String salesmanName;

	/** 销售人员所属中心 */
	@Column(name = "c_salesman_center")
	private String salesmanCenter;

	/** 销售人员所属部门 */
	@Column(name = "c_salesman_dep")
	private String salesmanDep;
	
	/** 销售人员岗位*/
	@Column(name = "c_salesman_position")
	private String salesmanPos;

	/** 销售区域 */
	@Column(name = "c_sales_territory")
	private String salesTerritory;

	/** 执行状态 */
	@Column(name = "c_execute_status")
	private String executeStatus;

	/** 控制状态 */
	@Column(name = "c_control_status")
	private String controlStatus;

	/** 是否价格异常 */
	@Column(name = "n_is_abnormal_price")
	private Integer isAbnormalPrice;

	/** 是否信用异常 */
	@Column(name = "n_is_abnormal_credit")
	private Integer isAbnormalCredit;

	/** 付款条件 */
	@Column(name = "c_term_payment")
	private String termPayment;

	/** 详情付款条款 */
	@Column(name = "c_term_payment_detail")
	private String termPaymentDetail;

	/** 付款客户ID */
	@Column(name = "c_payment_customer_id")
	private String paymentCustId;
	
	/** 付款客户ID */
	@Column(name = "c_payment_customer_name")
	private String paymentCustName;

	/** 最终用户ID */
	@Column(name = "c_final_cust_id")
	private String finalCustId;
	
	/** 最终用户名称 */
	@Column(name = "c_final_cust_name")
	private String finalCustName;

	/** 最终用户行业大类 */
	@Column(name = "c_final_cust_trade_b")
	private String finalCustTradeB;

	/** 最终用户行业小类 */
	@Column(name = "c_final_cust_trade_s")
	private String finalCustTradeS;

	/** 是否上门安装 */
	@Column(name = "c_is_install")
	private String isInstall;

	/** 是否所需辅材 */
	@Column(name = "c_is_am")
	private String isAm;

	/** 巡检周期 */
	@Column(name = "c_inspection_cycle")
	private String inspectionCycle;

	/** 保修年限 */
	@Column(name = "c_warranty_period")
	private String warrantyPeriod;

	/** 运输方式 */
	@Column(name = "c_ship_type")
	private String shipType;

	/** 是否送货上门 */
	@Column(name = "c_is_home_delivery")
	private String isHomeDelivery;

	/** 是否卸货到客户指定安装地点 */
	@Column(name = "c_is_destination_delivery")
	private String isDestinationDelivery;

	/** 售后服务条款 */
	@Column(name = "c_service_clause")
	private String serviceClause;

	/** 代收运费 */
	@Column(name = "n_collection_freight")
	private BigDecimal collectionFreight;

	/** 模具费 */
	@Column(name = "n_die_cost")
	private BigDecimal dieCost;

	/** 开发费用 */
	@Column(name = "n_development_costs")
	private BigDecimal developmentCosts;

	/** 客户品牌分类 */
	@Column(name = "c_cust_brand_class")
	private String custBrandClass;

	/** 是否免费赠送备件 */
	@Column(name = "c_is_free_spare")
	private String isFreeSpare;

	/** 是否随机发赠备件 */
	@Column(name = "c_is_random_spare")
	private String isRandomSpare;

	/** 备件金额*/
	@Column(name = "n_spare_amount")
	private BigDecimal spareAmount;

	/** 备件剩余额度 */
	@Column(name = "n_spare_balance")
	private BigDecimal spareBalance;

	/** 备件额度使用情况 */
	@Column(name = "c_spare_amount_situation")
	private String spareAmountSituation;

	/** 备件订单编号 */
	@Column(name = "c_spare_order_no")
	private String spareOrderNo;
	
	/** ERP订单编号 */
	@Column(name = "c_erp_order_code")
	private String erpOrderCode;
	
	/** 商务专员ID */
	@Column(name = "c_business_manager_id")
	private String businessManagerId;
	
	/** 商务专员编号 */
	@Column(name = "c_business_manager_code")
	private String businessManagerCode;
	
	/** 商务专员名称 */
	@Column(name = "c_business_manager_name")
	private String businessManagerName;
	
	/** 订单版本号 */
	@Column(name = "C_VERSION")
	private Integer version;
	
	/** 订单类型显示名称 */
	@Transient
	private Object orderTypeLable;
	
	/** 业务实体显示名称 */
	@Transient
	private String businessEntityLable;
	
	/** 发货组织 显示名称*/
	@Transient
	private Object shipOrgLable;
	
	/** 来源类型显示名称 */
	@Transient
	private Object sourceTypeLable;
	
	/** 状态显示名称 */
	@Transient
	private Object executeStatusLable;
	/**
	 * 创建人名称
	 */
	@Transient
	private String createdByName;
	
	/** 销售人员所属中心显示名称 */
	@Transient
	private String salesmanCenterLable;

	/** 销售人员所属部门 显示名称 */
	@Transient
	private String salesmanDepLable;
	
	/** 销售人员岗位显示名称 */
	@Transient
	private String salesmanPosName;

	@Transient
	private List<Map<Object, Object>> linesList;
	
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

	public String getCustomerErpCode() {
		return customerErpCode;
	}

	public void setCustomerErpCode(String customerErpCode) {
		this.customerErpCode = customerErpCode;
	}

	public String getCustomerName() {
		//return customerName;
		return CacheUtils.getCustomerName(this.getCustomerId());
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

	public String getCustAttnTel() {
		return custAttnTel;
	}

	public void setCustAttnTel(String custAttnTel) {
		this.custAttnTel = custAttnTel;
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
	
	@Transient
	public String getDeliveryAddressName(){
		CustomAddressInfo addressInfo = CacheUtils.getCustomAddressInfoById(this.deliveryAddressId);
		if (addressInfo != null) {
			return addressInfo.getCustomAddress();
		}else{
			return "";
		}
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
		Object obj = CacheData.getInstance().getMember("OPERATION_UNIT", orderType);
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

	public String getSalesmanPos() {
		return salesmanPos;
	}

	public void setSalesmanPos(String salesmanPos) {
		this.salesmanPos = salesmanPos;
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

	public Object getExecuteStatusLable() {
		return this.getLovMember("ORDERSTATUS", executeStatus).getName();
	}

	public void setExecuteStatusLable(Object executeStatusLable) {
		this.executeStatusLable = executeStatusLable;
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

	public String getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(String businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public String getBusinessManagerCode() {
		return businessManagerCode;
	}

	public void setBusinessManagerCode(String businessManagerCode) {
		this.businessManagerCode = businessManagerCode;
	}

	public String getBusinessManagerName() {
		return businessManagerName;
	}

	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}

	public String getCreatedByName() {
		return this.getCreatorName();
	}

	public String getSalesmanPosName() {
		return this.getLovName(salesmanPos);
	}

	public void setSalesmanPosName(String salesmanPosName) {
		this.salesmanPosName = salesmanPosName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}