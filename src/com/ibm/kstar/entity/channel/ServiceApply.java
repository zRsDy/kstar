package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 服务申请表(crm_t_service_apply)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-13
 */
@Entity
@Table(name = "crm_t_service_apply")
public class ServiceApply extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -1471256746216927139L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "service_apply_c_pid_generator")
   	@GenericGenerator(name="service_apply_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 服务单号 */
    @Column(name = "c_apply_code")
    private String applyCode;

    /** 申请单位 */
    @Column(name = "c_apply_unit")
    private String applyUnit;
    
    /** 服务类型 */
    @Column(name = "c_service_type")
    private String serviceType;

    /** 是否经销商*/
    @Column(name = "c_dealer")
    private String dealer;
    
    /** 状态*/
    @Column(name = "c_status")
    private String status;
    
    /** 货币 */
	@Column(name = "c_currency")
	private String currency;
	
    /** 服务费用 */
    @Column(name = "c_service_expense")
    private BigDecimal serviceExpense;
    
    /** 申请日期 */
    @Column(name = "c_apply_date")
    private Date applyDate;
    
    /** 需求完成日期 */
    @Column(name = "c_demand_finish_date")
    private Date demandFinishDate;

    /** 组织 */
    @Column(name = "c_organization")
    private String organization;
    
    /** 申请人*/
    @Column(name = "c_applier")
    private String applier;
    
    @Transient
    private String applierName;
    
    /** 申请人电话 */
    @Column(name = "c_applier_phone")
    private String applierPhone;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;
    
    /** 商机编号*/
    @Column(name = "c_busi_chance_code")
    private String busiChanceCode;
    
    /** 商机名称*/
    @Column(name = "c_busi_chance_name")
    private String busiChanceName;
    
    /** 客户所在地*/
    @Column(name = "c_custom_place_id")
    private String customPlaceId;
    
    /** 客户所在地*/
    @Column(name = "c_custom_place_name")
    private String customPlaceName;
    
    /** 联系地址*/
    @Column(name = "c_contact_address")
    private String contractAddress;
    
    /** 合同编号*/
    @Column(name = "c_contact_code")
    private String contractCode;
    
    /** 终端客户名称*/
    @Column(name = "c_end_custom_name")
    private String endCustomName;
    
    /** 客户联系人*/
    @Column(name = "c_custom_contact")
    private String customContact;
    
    /** 客户联系人电话*/
    @Column(name = "c_contact_phone")
    private String contactPhone;
    
    /** 服务内容*/
    @Column(name = "c_service_content")
    private String serviceContent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}

	public String getApplyUnitName() {
		return this.getLovName(applyUnit);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getServiceTypeCode() {
		return this.getLovCode(serviceType);
	}

	public String getServiceTypeName() {
		return this.getLovName(serviceType);
	}
	
	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	
	public String getDealerCode() {
		return this.getLovCode(dealer);
	}
	
	public String getDealerName() {
		return this.getLovName(dealer);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusName() {
		return this.getLovName(status);
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrencyName() {
		return this.getLovName(currency);
	}
	
	public BigDecimal getServiceExpense() {
		return serviceExpense;
	}

	public void setServiceExpense(BigDecimal serviceExpense) {
		this.serviceExpense = serviceExpense;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getDemandFinishDate() {
		return demandFinishDate;
	}

	public void setDemandFinishDate(Date demandFinishDate) {
		this.demandFinishDate = demandFinishDate;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApplierPhone() {
		return applierPhone;
	}

	public void setApplierPhone(String applierPhone) {
		this.applierPhone = applierPhone;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getBusiChanceCode() {
		return busiChanceCode;
	}

	public void setBusiChanceCode(String busiChanceCode) {
		this.busiChanceCode = busiChanceCode;
	}

	public String getBusiChanceName() {
		return busiChanceName;
	}

	public void setBusiChanceName(String busiChanceName) {
		this.busiChanceName = busiChanceName;
	}

	public String getCustomPlaceId() {
		return customPlaceId;
	}

	public void setCustomPlaceId(String customPlaceId) {
		this.customPlaceId = customPlaceId;
	}

	public String getCustomPlaceName() {
		return customPlaceName;
	}

	public void setCustomPlaceName(String customPlaceName) {
		this.customPlaceName = customPlaceName;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getEndCustomName() {
		return endCustomName;
	}

	public void setEndCustomName(String endCustomName) {
		this.endCustomName = endCustomName;
	}

	public String getCustomContact() {
		return customContact;
	}

	public void setCustomContact(String customContact) {
		this.customContact = customContact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getServiceContent() {
		return serviceContent;
	}

	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
    
}