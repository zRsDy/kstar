package com.ibm.kstar.entity.order;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:OrderLines <br/>
 * Function: 订单行表和ERP对接视图 <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年12月19日 上午10:22:17 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 * @see
 */
@Entity
@Table(name = "CRM_V_ORDER_LINES")
public class OrderLinesView {
	
	/** 主键 */
	@Id
	@Column(name = "c_pid", unique = true)
	@GeneratedValue(generator = "order_l_id_generator")
	@GenericGenerator(name = "order_l_id_generator", strategy = "uuid")
	private String id;
	
	/** 订单头号 */
	@Column(name = "c_order_code")
	private String orderCode;
	
	/** 产品型号 */
	@Column(name = "c_pro_model")
	private String proModel;
	
	/** 物料编号 */
	@Column(name = "c_materiel_code")
	private String materielCode;
	
	/** 产品描述 */
	@Column(name = "c_item_description")
	private String itemDescription;
	
	/** 产品数量 */
	@Column(name = "n_product_quantity")
	private double proQty;
	
	/** 单位 */
	@Column(name = "c_unit")
	private String unit;
	
	/** 销售单价 */
	@Column(name = "n_price")
	private BigDecimal price;
	
	/** 金额 */
	@Column(name = "n_amount")
	private BigDecimal amount;
	
	/** 请求日期 */
	@Column(name = "dt_request_date")
	private Date requestDate;
	
	/** 承诺日期 */
	@Column(name = "DT_PROMISE_DATE")
	private Date promiseDate;
	
	/** CRM状态 */
	@Column(name = "c_status")
	private String status;
	
	/** 是否需要启动交期确认 */
	@Column(name = "c_confirm_delivery_date")
	private String confirmDeliveryDate;
	
	/** 发货组织 */
	@Column(name = "c_ship_org")
	private String shipOrg;

	/** 是否提前开票 */
	@Column(name = "c_is_advance_billing")
	private String isAdvanceBilling;
	
	/** 退货原因 */
	@Column(name = "c_return_reason")
	private String returnReason;
	
	/** 退货参考 */
	@Column(name = "c_return_reference")
	private String returnReference;
	
	/** 是否暂挂 */
	@Column(name = "c_is_pending")
	private String isPending;
	
	/** 发货数量 */
	@Column(name = "n_delivery_quantity")
	private double deliveryQty;
	
	/** 开票数量 */
	@Column(name = "n_billing_quantity")
	private double billingQty;
	
	/** 收货客户*/
	@Column(name = "C_RECEIVING_CUSTOMER")
	private String receiveCustomer;
	
	/** 收货方 */
	@Column(name = "C_CONSIGNEE")
	private String consignee;
	
	/** 行号 */
	@Column(name = "c_line_no")
	private String lineNo;
	
	/** 订单头ID */
	@Column(name = "c_order_id")
	private String orderId;
	
	/** 产品ID */
	@Column(name = "c_pro_id")
	private String proId;
	
	/** 创建日期 */
	@Column(name = "c_create_time")
	private Date createTime;

	/** 创建人 */
	@Column(name = "c_creator")
	private String creator;
	
	/** 物料名称 */
	@Column(name = "c_materiel_name")
	private String materielName;
	
	/** 取消数量 */
	@Column(name = "n_cancel_quantity")
	private double cancelQty;
	
	/** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;
    
    /** 原订单行ID */
	@Column(name = "c_original_line_id")
	private String originalLineId;
	
	/** 来源行：合同为工程清单行行号，商机为商机ID */
	@Column(name = "c_source_id")
	private String sourceId;
	
	/** 备注 */
	@Column(name = "c_remark")
	private String remark;
	
	 /** ERP计划状态 */
    @Column(name = "c_erp_plan_status")
    private String erpPlanStatus;
    
    /** ERP是否已发货 */
    @Column(name = "c_is_erp_delivery")
    private String isErpDelivery;
    
    /** ERP结算单价 */
	@Column(name = "n_erp_settlement_price")
	private BigDecimal erpSettPrice;
	
	 /** ERP状态 */
    @Column(name = "c_erp_status")
    private String erpStatus;
    
    /** 特价编号 */
    @Column(name = "c_special_price_code")
    private String spCode;
    
    /** 是否特价  */
    @Column(name = "c_is_special_price")
    private String isSp;
    
    /** 品牌   */
	@Column(name = "c_pro_brand")
	private String proBrand;
	
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_code")
	private String sourceCode;
	
	/** 特价申请行ID */
    @Column(name = "c_special_price_line_id")
    private String spLineId;
    
    /** 拆行上级行号 */
    @Column(name = "PARENT_LINE_NUM")
	private String parentLineNum;
    
    /** 拆行根行号 */
    @Column(name = "ROOT_LINE_NUM")
	private String rootLineNum;
    
    /** 产品说明 */
	@Column(name = "c_pro_desc")
	private String proDesc;
	
	 /** ERP订单行号  */
    @Column(name = "c_erp_line_no")
    private String erpLineNo;
    
    /** DB层修改记录的标识  */
    @Column(name = "C_UPDATE_TAG")
    private String updateTag;
    
    /** ERP状态 描述  */
    @Column(name = "C_ERP_STATUS_DESC")
    private String erpStatusDes;
    
    /** ERP备货完指令  */
    @Column(name = "BE_READY_FLAG")
    private String readyFlag;
    
    /** ERP可开票数量 */
	@Column(name = "REQUESTED_QUANTITY")
	private Double requestedQty;
	
	/** 外部出货单打印日期 */
	@Column(name = "DT_PRINT_TIME")
	private Date dtprintTime;

	/** 外部出货单号 */
	@Column(name = "EXTERNAL_SHIPMENT_NUMBER")
	private String externalShipmentNumber;
	
	/** ERP最后更新日期 */
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;

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

	public String getProModel() {
		return CacheUtils.getProductModel(this.getProId());
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
		return CacheUtils.getProductName(this.getProId());
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Double getProQty() {
		return proQty;
	}

	public void setProQty(Double proQty) {
		this.proQty = proQty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getConfirmDeliveryDate() {
		return confirmDeliveryDate;
	}

	public void setConfirmDeliveryDate(String confirmDeliveryDate) {
		this.confirmDeliveryDate = confirmDeliveryDate;
	}

	public String getShipOrg() {
		return shipOrg;
	}

	public void setShipOrg(String shipOrg) {
		this.shipOrg = shipOrg;
	}

	public String getIsAdvanceBilling() {
		return isAdvanceBilling;
	}

	public void setIsAdvanceBilling(String isAdvanceBilling) {
		this.isAdvanceBilling = isAdvanceBilling;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getReturnReference() {
		return returnReference;
	}

	public void setReturnReference(String returnReference) {
		this.returnReference = returnReference;
	}

	public String getIsPending() {
		return isPending;
	}

	public void setIsPending(String isPending) {
		this.isPending = isPending;
	}

	public double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public double getBillingQty() {
		return billingQty;
	}

	public void setBillingQty(double billingQty) {
		this.billingQty = billingQty;
	}

	public String getReceiveCustomer() {
		return receiveCustomer;
	}

	public void setReceiveCustomer(String receiveCustomer) {
		this.receiveCustomer = receiveCustomer;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
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

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public double getCancelQty() {
		return cancelQty;
	}

	public void setCancelQty(double cancelQty) {
		this.cancelQty = cancelQty;
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

	public String getOriginalLineId() {
		return originalLineId;
	}

	public void setOriginalLineId(String originalLineId) {
		this.originalLineId = originalLineId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getErpPlanStatus() {
		return erpPlanStatus;
	}

	public void setErpPlanStatus(String erpPlanStatus) {
		this.erpPlanStatus = erpPlanStatus;
	}

	public String getIsErpDelivery() {
		return isErpDelivery;
	}

	public void setIsErpDelivery(String isErpDelivery) {
		this.isErpDelivery = isErpDelivery;
	}

	public BigDecimal getErpSettPrice() {
		return erpSettPrice;
	}

	public void setErpSettPrice(BigDecimal erpSettPrice) {
		this.erpSettPrice = erpSettPrice;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getIsSp() {
		return isSp;
	}

	public void setIsSp(String isSp) {
		this.isSp = isSp;
	}

	public String getProBrand() {
		return proBrand;
	}

	public void setProBrand(String proBrand) {
		this.proBrand = proBrand;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSpLineId() {
		return spLineId;
	}

	public void setSpLineId(String spLineId) {
		this.spLineId = spLineId;
	}

	public String getParentLineNum() {
		return parentLineNum;
	}

	public void setParentLineNum(String parentLineNum) {
		this.parentLineNum = parentLineNum;
	}

	public String getRootLineNum() {
		return rootLineNum;
	}

	public void setRootLineNum(String rootLineNum) {
		this.rootLineNum = rootLineNum;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getErpLineNo() {
		return erpLineNo;
	}

	public void setErpLineNo(String erpLineNo) {
		this.erpLineNo = erpLineNo;
	}

	public String getUpdateTag() {
		return updateTag;
	}

	public void setUpdateTag(String updateTag) {
		this.updateTag = updateTag;
	}

	public String getErpStatusDes() {
		return erpStatusDes;
	}

	public void setErpStatusDes(String erpStatusDes) {
		this.erpStatusDes = erpStatusDes;
	}

	public String getReadyFlag() {
		return readyFlag;
	}

	public void setReadyFlag(String readyFlag) {
		this.readyFlag = readyFlag;
	}

	public Double getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(Double requestedQty) {
		this.requestedQty = requestedQty;
	}

	public Date getDtprintTime() {
		return dtprintTime;
	}

	public void setDtprintTime(Date dtprintTime) {
		this.dtprintTime = dtprintTime;
	}

	public String getExternalShipmentNumber() {
		return externalShipmentNumber;
	}

	public void setExternalShipmentNumber(String externalShipmentNumber) {
		this.externalShipmentNumber = externalShipmentNumber;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/** 订单行状态显示名称 */
	@Transient
	private String statusLable;

	/** 发货组织 显示名称*/
	@Transient
	private String shipOrgLable;

	/** 单位 显示名称*/
	@Transient
	private String unitLable;

	/** ERP订单行状态显示名称 */
	@Transient
	private String erpStatusLable;

	/** 来源显示名称  */
	@Transient
	private String sourceCodeLable;

	/** 选择产品lable  */
	@Transient
	private String act;
	/** 特价选择lable  */
	@Transient
	private String spCodeLable;

	/** 退货参考 */
	@Transient
	private String returnReferenceLable;

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

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
	}
    public String getStatusLable() {

        LovMember lov = new LovMember();
        Object obj = CacheData.getInstance().getMember("ORDER_LINE_STATUS", status);
        if(obj != null ){
            BeanUtils.copyPropertiesIgnoreNull(obj, lov);
        }
        return  lov.getName();
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

	public String getErpStatusLable() {
		return  getErpStatusDes();
	}

	public void setErpStatusLable(String erpStatusLable) {
		this.erpStatusLable = erpStatusLable;
	}


	public String getSourceCodeLable() {
		return sourceCode;
	}
	public String getAct() {
		return itemDescription;
	}

	public String getSpCodeLable() {
		return spCode;
	}

	public String getReturnReferenceLable() {
		return returnReference;
	}

}
