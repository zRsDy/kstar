package com.ibm.kstar.entity.report;

import java.io.Serializable;
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
 * ReportOverdueVO.java 逾期未发货明细视图实体类
 * 
 * @author 张钧鑫 and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "kstart_order_delivery_report_v")
public class ReportOverdueVO implements Serializable{
	private static final long serialVersionUID = -8658308573164369043L;
	
	@Id
	@GeneratedValue(generator = "crm_t_contr_basic_id_generator")
	@GenericGenerator(name="crm_t_contr_basic_id_generator", strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	/**
	 * 视图订单ID
	 */
	@Column(name = "c_pid")
	private String dueId;

	/** 视图订单编号 */
	@Column(name = "c_order_code")
	private String orderCode;
	
	/** 视图销售人员 ID*/
	@Column(name = "c_salesman_id")
	private String salesmanId;
	
	/** 视图销售人员岗位*/
	@Column(name = "c_salesman_position")
	private String salesmanPos;
	
	/** 销售人员岗位名称*/
	@Transient
	private String salesmanPosName;
	
	/** 视图客户名称 */
	@Column(name = "c_customer_name")
	private String customerName;
	
	/** 视图ERP订单编号 */
	@Column(name = "c_erp_order_code")
	private String erpOrderCode;
	
	/** 视图行号 */
	@Column(name = "c_line_no")
	private String lineNo;
	
	/** 视图物料编号 */
	@Column(name = "c_materiel_code")
	private String materielCode;

	/** 视图产品说明 */
	@Column(name = "c_pro_desc")
	private String proDesc;

	/** 视图产品型号 */
	@Column(name = "c_pro_model")
	private String proModel;

	/** 视图发货组织 */
	@Column(name = "c_ship_org")
	private String shipOrg;
	
	@Transient
	private String shipOrgNm;

	/** 视图请求日期 */
	@Column(name = "dt_request_date")
	private Date requestDate;

	/** 视图数量 */
	@Column(name = "n_product_quantity")
	private double proQty;

	/** 视图单位 */
	@Column(name = "c_unit")
	private String unit;
	
	/** 单位 显示名称*/
	@Transient
	private String unitLable;
	
	/** 视图ERP结算单价 */
	@Column(name = "n_erp_settlement_price")
	private BigDecimal erpSettPrice;

	/** 视图金额 */
	@Transient
	private BigDecimal amount;

	@Column(name = "rmb_order_amount")
	private BigDecimal rmbAmount;
	
	@Column(name = "usd_order_amount")
	private BigDecimal usdAmount;
	
	/** 视图承诺日期 */
	@Column(name = "dt_promise_date")
	private Date promiseDate;

	/** 视图是否需要启动交期确认 */
	@Column(name = "c_confirm_delivery_date")
	private String confirmDeliveryDate;

	/** 视图是否暂挂 */
	@Column(name = "c_is_pending")
	private String isPending;

	/** 视图是否提前开票 */
	@Column(name = "c_is_advance_billing")
	private String isAdvanceBilling;

	/** 视图发货数量 */
	@Column(name = "order_line_del_qty")
	private double deliveryQty;

	/** 视图开票数量 */
	@Column(name = "n_billing_quantity")
	private double billingQty;

	/** 视图状态 */
	@Column(name = "c_status")
	private String status;
	
	/** 订单行状态显示名称 */
	@Transient
	private String statusLable;
	
    /** 视图ERP计划状态 */
    @Column(name = "c_erp_plan_status")
    private String erpPlanStatus;
    
    /** 视图ERP是否已发货 */
    @Column(name = "c_is_erp_delivery")
    private String isErpDelivery;

    /** 销售人员名称*/
	@Column(name = "c_salesman_name")
	private String salesmanName;

	 /** 特价编号 */
    @Column(name = "c_special_price_code")
    private String spCode;
    

	/** 视图出货单表行出货单打印日期（外部） */
	@Column(name = "dt_print_time")
	private Date printTime;
	
	/** 视图出货单表行发货产品金额 */
	@Column(name = "del_amont")
	private BigDecimal deliveryAmount;
	
	/** 视图出货单表行数量 */
	@Column(name = "del_qty")
	private double delQty;
	
	/** 单价 */
	@Transient
	private BigDecimal price;
	
	/** 单价-RMB */
	@Column(name = "rmb_del_price")
	private BigDecimal rmbPrice;
	
	/** 单价 -USD*/
	@Column(name = "usd_del_price")
	private BigDecimal usdPrice;
	
	/** 订购日期 */
	@Column(name = "order_date")
	private Date orderDate;
	
	 /** 订购日期(年) */
    @Column(name = "order_year")
    private String year;
    
    /** 订购日期(月) */
    @Column(name = "order_month")
    private String month;
    
    /** ERP状态 */
    @Column(name = "c_erp_status")
    private String erpStatus;

    /** ERP订单行状态显示名称 */
   	@Transient
   	private String erpStatusLable;
   	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDueId() {
		return dueId;
	}

	public void setDueId(String dueId) {
		this.dueId = dueId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(String salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanPos() {
		return salesmanPos;
	}

	public void setSalesmanPos(String salesmanPos) {
		this.salesmanPos = salesmanPos;
	}
    
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getErpOrderCode() {
		return erpOrderCode;
	}

	public void setErpOrderCode(String erpOrderCode) {
		this.erpOrderCode = erpOrderCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
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

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public String getShipOrg() {
		return shipOrg;
	}

	public void setShipOrg(String shipOrg) {
		this.shipOrg = shipOrg;
	}

	public String getShipOrgNm() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(shipOrg);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setShipOrgNm(String shipOrgNm) {
		this.shipOrgNm = shipOrgNm;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public double getProQty() {
		return proQty;
	}

	public void setProQty(double proQty) {
		this.proQty = proQty;
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

	public BigDecimal getErpSettPrice() {
		return erpSettPrice;
	}

	public void setErpSettPrice(BigDecimal erpSettPrice) {
		this.erpSettPrice = erpSettPrice;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getPromiseDate() {
		return promiseDate;
	}

	public void setPromiseDate(Date promiseDate) {
		this.promiseDate = promiseDate;
	}

	public String getConfirmDeliveryDate() {
		return confirmDeliveryDate;
	}

	public void setConfirmDeliveryDate(String confirmDeliveryDate) {
		this.confirmDeliveryDate = confirmDeliveryDate;
	}

	public String getIsPending() {
		return isPending;
	}

	public void setIsPending(String isPending) {
		this.isPending = isPending;
	}

	public String getIsAdvanceBilling() {
		return isAdvanceBilling;
	}

	public void setIsAdvanceBilling(String isAdvanceBilling) {
		this.isAdvanceBilling = isAdvanceBilling;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDER_LINE_STATUS", status);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
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

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public double getDelQty() {
		return delQty;
	}

	public void setDelQty(double delQty) {
		this.delQty = delQty;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSalesmanPosName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(this.salesmanPos);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}

	public void setSalesmanPosName(String salesmanPosName) {
		this.salesmanPosName = salesmanPosName;
	}

	public BigDecimal getRmbAmount() {
		return rmbAmount;
	}

	public void setRmbAmount(BigDecimal rmbAmount) {
		this.rmbAmount = rmbAmount;
	}

	public BigDecimal getUsdAmount() {
		return usdAmount;
	}

	public void setUsdAmount(BigDecimal usdAmount) {
		this.usdAmount = usdAmount;
	}

	public BigDecimal getRmbPrice() {
		return rmbPrice;
	}

	public void setRmbPrice(BigDecimal rmbPrice) {
		this.rmbPrice = rmbPrice;
	}

	public BigDecimal getUsdPrice() {
		return usdPrice;
	}

	public void setUsdPrice(BigDecimal usdPrice) {
		this.usdPrice = usdPrice;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
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
	
	
}
