package com.ibm.kstar.entity.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 签收单(CRM_T_DELIVERY_RECEIPT)
 * 
 * @author liming
 * @version 1.0.0 2016-12-27
 */
@Entity
@Table(name = "crm_t_delivery_receipt")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_DELIVERYRECEIPT)
public class DeliveryReceipt extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
	/** 主键自增 */
	@Id
	@Column(name = "c_pid", unique = true)
	@GeneratedValue(generator = "delivery_r_id_generator")
	@GenericGenerator(name="delivery_r_id_generator", strategy="uuid")
	@PermissionBusinessId
    private String id;
    
    /** 签收单编号 */
    @Column(name = "c_receipt_code")
    private String receiptCode;
    
    /** 发货申请编号 */
    @Column(name = "c_delivery_code")
    private String deliveryCode;
    
    /** 发货明细行号 */
    @Column(name = "c_delivery_lines_num")
    private String deliveryLinesNum;
    
    /** 订单编号 */
    @Column(name = "c_order_code")
    private String orderCode;
    
    /** 订单行行ID */
    @Column(name = "c_order_id")
    private String orderId;
    
    /** 订单行行号 */
    @Column(name = "c_order_line_no")
    private String orderLineNo;
    
    /** 下单客户ID */
   	@Column(name = "c_single_cust_id")
   	private String singleCustId;

   	/** 下单客户编号 */
   	@Column(name = "c_single_cust_code")
   	private String singleCustCode;

   	/** 下单客户编号 */
   	@Column(name = "c_single_cust_name")
   	private String singleCustName;
  
    /** 物料编码 */
    @Column(name = "c_materiel_code")
    private String materielCode;
    
    /** 产品说明 */
	@Column(name = "c_pro_desc")
	private String proDesc;
    
    /** 收货地点 */
    @Column(name = "c_receiving_address")
    private String receAddress;
    
    /** 收货人 */
    @Column(name = "c_consignee")
    private String consignee;
    
    /** 收货人电话 */
    @Column(name = "c_consignee_tel")
    private String consigneeTel;
    
    /** 单位 */
    @Column(name = "c_unit")
    private String unit;
    
    /** 发货数量 */
    @Column(name = "n_delivery_quantity")
    private double deliveryQty;
    
    /** 发货产品金额 */
	@Column(name = "n_delivery_amount")
	private BigDecimal deliveryAmount;
	
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;
    
    /** 物料名称 */
    @Column(name = "c_materiel_name")
    private String materielName;
    
    /** 出货单编号(外部) */
    @Column(name = "c_external_no")
    private String externalNo;
    
    /** 签收单打印日期（外部） */
    @Column(name = "dt_invoice_print_time")
    private Date invoicePrintTime;
    
    /** 签收数量 */
    @Column(name = "n_receipt_quantity")
    private Double receiptQty;
    
    /** 物流公司 */
    @Column(name = "c_logistics_company")
    private String logisticsCompany;
    
    /** 物流运单号 */
    @Column(name = "c_logistics_no")
    private String logisticsNo;
    
    /** 预计到达时间 */
    @Column(name = "dt_estimate_arrival_time")
    private Date estimateArrivalTime;
    
    /** 实际到达时间 */
    @Column(name = "dt_actual_arrival_time")
    private Date actualArrivalTime;

    /** 预计时间 */
    @Column(name = "n_estimate_time")
    private Double estimateTime;
    
    /** 实际用时 */
    @Column(name = "n_actual_time")
    private Double actualTime;
     
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    /**
     * 是否是主行
     */
    @Column(name = "c_is_main")
    private String isMain;
    
	/** 来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号 */
	@Column(name = "c_source_code")
	private String sourceCode;
	/** ERP订单编号 */
	@Column(name = "c_erp_order_code")
	private String erpOrderCode;
	
	/** 删除标志, 1.已删除，0 未删除 */ 
    @Column(name="c_delete_flag")
	private String deleteFlag = "0";
    
    /** 当地物流联系人名称  */ 
    @Column(name="c_logistics_contacts_name")
	private String logisticsContactsName;
    
    /** 当地物流联系人电话  */ 
    @Column(name="c_logistics_contacts_tel")
	private String logisticsContactsTel;
    
    /** 当地物流单号  */ 
    @Column(name="c_local_logistics_no")
	private String localLogisticsNo;
    
    /** ERP导入标志 */
	@Column(name = "c_erp_import_flag")
	private String erpImportFlag = "No";
    
    @Transient
   	private List<Map<Object, Object>> lines;
    /**物流公司显示名称*/
    @Transient
   	private String logisticsCompanyLable;
    /**状态显示名称*/
    @Transient
    private String statusLable;
    
    @Transient
   	private String unitLable;
    
    @Transient
   	private String createdByName;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	
	public String getDeliveryLinesNum() {
		return deliveryLinesNum;
	}

	public void setDeliveryLinesNum(String deliveryLinesNum) {
		this.deliveryLinesNum = deliveryLinesNum;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getReceAddress() {
		return receAddress;
	}

	public void setReceAddress(String receAddress) {
		this.receAddress = receAddress;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public Date getInvoicePrintTime() {
		return invoicePrintTime;
	}

	public void setInvoicePrintTime(Date invoicePrintTime) {
		this.invoicePrintTime = invoicePrintTime;
	}

	public Double getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(Double receiptQty) {
		this.receiptQty = receiptQty;
	}

	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public Date getEstimateArrivalTime() {
		return estimateArrivalTime;
	}

	public void setEstimateArrivalTime(Date estimateArrivalTime) {
		this.estimateArrivalTime = estimateArrivalTime;
	}

	public Date getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(Date actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}
	
	public Double getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(Double estimateTime) {
		this.estimateTime = estimateTime;
	}

	public Double getActualTime() {
		return actualTime;
	}

	public void setActualTime(Double actualTime) {
		this.actualTime = actualTime;
	}

	public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public List<Map<Object, Object>> getLines() {
		return lines;
	}

	public void setLines(List<Map<Object, Object>> lines) {
		this.lines = lines;
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

	public String getLogisticsCompanyLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("TRANSPORT_CO", logisticsCompany);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setLogisticsCompanyLable(String logisticsCompanyLable) {
		this.logisticsCompanyLable = logisticsCompanyLable;
	}

	public String getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("LOGISTICS_RECEIPT_STATUS", status);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
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

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getLogisticsContactsName() {
		return logisticsContactsName;
	}

	public void setLogisticsContactsName(String logisticsContactsName) {
		this.logisticsContactsName = logisticsContactsName;
	}

	public String getLogisticsContactsTel() {
		return logisticsContactsTel;
	}

	public void setLogisticsContactsTel(String logisticsContactsTel) {
		this.logisticsContactsTel = logisticsContactsTel;
	}

	public String getLocalLogisticsNo() {
		return localLogisticsNo;
	}

	public void setLocalLogisticsNo(String localLogisticsNo) {
		this.localLogisticsNo = localLogisticsNo;
	}

	public String getCreatedByName() {
		return this.getCreatorName();
	}

	public String getErpImportFlag() {
		return erpImportFlag;
	}

	public void setErpImportFlag(String erpImportFlag) {
		this.erpImportFlag = erpImportFlag;
	}
	
}