package com.ibm.kstar.entity.order;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * ClassName: DeliveryLogistics <br/> 
 * Function: 发货单物流表. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年1月9日 下午6:45:30 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_delivery_logistics")
public class DeliveryLogistics implements java.io.Serializable {
	
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	@Id
	@Column(name = "c_pid", unique = true)
	@GeneratedValue(generator = "delivery_log_id_generator")
	@GenericGenerator(name="delivery_log_id_generator", strategy="uuid")
	private String id;

	/** 物流编号 */
	@Column(name = "c_logistics_code")
	private String logisticsCode;

	/** 发货申请编号 */
	@Column(name = "c_delivery_code")
	private String deliveryCode;

	/** 物流公司编号 */
	@Column(name = "c_logts_co_code")
	private String logtsCoCode;

	/** 物流公司名称 */
	@Column(name = "c_logts_co_name")
	private String logtsCoName;

	/** 运输方式 */
	@Column(name = "c_transport_mode")
	private String transportMode;

	/** 运单号 */
	@Column(name = "c_logistics_no")
	private String logisticsNo;

	/** 船名 */
	@Column(name = "c_ship_name")
	private String shipName;

	/** 船期 */
	@Column(name = "dt_shipment")
	private Date shipment;

	/** 重量 */
	@Column(name = "c_weight")
	private String weight;

	/** 体积 */
	@Column(name = "c_volume")
	private String volume;

	/** 国内运费 */
	@Column(name = "n_freight_internal")
	private BigDecimal freightInternal;

	/** 国际运费 */
	@Column(name = "n_freight_external")
	private BigDecimal freightExternal;

	/** 计划到港日期 */
	@Column(name = "dt_arrival")
	private Date dtArrival;

	/** 保险公司 */
	@Column(name = "c_insurer")
	private String insurer;

	/** 保单号 */
	@Column(name = "c_policy_no")
	private String policyNo;

	/** 保费 */
	@Column(name = "n_policy_fee")
	private BigDecimal policyFee;

	/** 投标有效期 */
	@Column(name = "dt_tender")
	private Date tender;

	/** 保险说明 */
	@Column(name = "c_policy_remarks")
	private String policyRemarks;

	/** 保险状态 */
	@Column(name = "c_policy_status")
	private String policyStatus;

	/** 报关行 */
	@Column(name = "c_customs_broker")
	private String customsBroker;

	/** 报关合同 */
	@Column(name = "c_customs_contract")
	private String customsContract;

	/** 出口发票 */
	@Column(name = "c_export_invoice")
	private String exportInvoice;

	/** 外汇核销单 */
	@Column(name = "c_forex_verification")
	private String forexVerification;

	/** 报关日期 */
	@Column(name = "c_customs_date")
	private Date customsDate;

	/** 报关状态 */
	@Column(name = "c_customs_status")
	private String customsStatus;
	
	/** 截关时间 */
	@Column(name = "dt_closing_time")
	private Date closingTime;
	
	/** 截仓时间 */
	@Column(name = "dt_cut_off_date")
	private Date cutOffDate;
	
	/** 订舱单号 */
	@Column(name = "c_booking_number")
	private String bookingNumber;
	
	/** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
    private Date updatedAt;
    

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

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getLogtsCoCode() {
		return logtsCoCode;
	}

	public void setLogtsCoCode(String logtsCoCode) {
		this.logtsCoCode = logtsCoCode;
	}

	public String getLogtsCoName() {
		return logtsCoName;
	}

	public void setLogtsCoName(String logtsCoName) {
		this.logtsCoName = logtsCoName;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public Date getShipment() {
		return shipment;
	}

	public void setShipment(Date shipment) {
		this.shipment = shipment;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public BigDecimal getFreightInternal() {
		return freightInternal;
	}

	public void setFreightInternal(BigDecimal freightInternal) {
		this.freightInternal = freightInternal;
	}

	public BigDecimal getFreightExternal() {
		return freightExternal;
	}

	public void setFreightExternal(BigDecimal freightExternal) {
		this.freightExternal = freightExternal;
	}

	public Date getDtArrival() {
		return dtArrival;
	}

	public void setDtArrival(Date dtArrival) {
		this.dtArrival = dtArrival;
	}

	public String getInsurer() {
		return insurer;
	}

	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public BigDecimal getPolicyFee() {
		return policyFee;
	}

	public void setPolicyFee(BigDecimal policyFee) {
		this.policyFee = policyFee;
	}

	public Date getTender() {
		return tender;
	}

	public void setTender(Date tender) {
		this.tender = tender;
	}

	public String getPolicyRemarks() {
		return policyRemarks;
	}

	public void setPolicyRemarks(String policyRemarks) {
		this.policyRemarks = policyRemarks;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public String getCustomsBroker() {
		return customsBroker;
	}

	public void setCustomsBroker(String customsBroker) {
		this.customsBroker = customsBroker;
	}

	public String getCustomsContract() {
		return customsContract;
	}

	public void setCustomsContract(String customsContract) {
		this.customsContract = customsContract;
	}

	public String getExportInvoice() {
		return exportInvoice;
	}

	public void setExportInvoice(String exportInvoice) {
		this.exportInvoice = exportInvoice;
	}

	public String getForexVerification() {
		return forexVerification;
	}

	public void setForexVerification(String forexVerification) {
		this.forexVerification = forexVerification;
	}

	public Date getCustomsDate() {
		return customsDate;
	}

	public void setCustomsDate(Date customsDate) {
		this.customsDate = customsDate;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public Date getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}

	public Date getCutOffDate() {
		return cutOffDate;
	}

	public void setCutOffDate(Date cutOffDate) {
		this.cutOffDate = cutOffDate;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}
	
}