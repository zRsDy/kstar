package com.ibm.kstar.entity.order;

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
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * ClassName:InvoiceHeader <br/>
 * Function: 出货单头表. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年12月16日 上午10:22:17 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 * @see
 */
@Entity
@Table(name = "CRM_T_DELIVERY_HEADER")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY)
public class DeliveryHeader extends BaseEntity implements java.io.Serializable {

    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "delivery_h_id_generator")
 	@GenericGenerator(name="delivery_h_id_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 出货申请头编号 */
    @Column(name = "c_delivery_code")
    private String deliveryCode;
    
    /** 申请日期 */
    @Column(name = "dt_apply_date")
    private Date applyDate;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    /** 状态显示名称*/
    @Transient
    private Object statusLable;
    
    /** 业务实体 */
    @Column(name = "c_business_entity")
    private String businessEntity;
    
    /** 业务实体显示名称*/
    @Transient
    private String businessEntityLable;
    
    /** 申请人ID */
    @Column(name = "c_proposer_id")
    private String proposerId;
    
    /** 申请人编号 */
    @Column(name = "c_proposer_code")
    private String proposerCode;
    
    /** 申请人名称 */
    @Column(name = "c_proposer_name")
    private String proposerName;
    
    /** 收货客户 */
    @Column(name = "c_rece_customer_id")
    private String receCustomerId;
    
    /** 收货客户 */
    @Column(name = "c_rece_customer_code")
    private String receCustomerCode;
    
    /** 收货客户 */
    @Column(name = "c_rece_customer_name")
    private String receCustomerName;
    
    /** 收货地点 ID*/
    @Column(name = "c_delivery_address_id")
    private String deliveryAddressId;
    
    /** 收货地点 */
    @Column(name = "c_delivery_address")
    private String deliveryAddress;
    
    /** 收货人 */
    @Column(name = "c_consignee")
    private String consignee;
    
    /** 收货人电话 */
    @Column(name = "c_consignee_tel")
    private String consigneeTel;
    
    /** 收单客户 */
    @Column(name = "c_bill_customer_id")
    private String billCustomerId;
    
    /** 收单客户 */
    @Column(name = "c_bill_customer_name")
    private String billCustomerName;
    
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;
    
    /** 商务专员ID */
	@Column(name = "c_business_manager_id")
	private String businessManagerId;
	
	/** 商务专员编号 */
	@Column(name = "c_business_manager_code")
	private String businessManagerCode;
	
	/** 商务专员名称 */
	@Column(name = "c_business_manager_name")
	private String businessManagerName;
    
    /** 删除标志, 1.已删除，0 未删除 */ 
    @Column(name="c_delete_flag")
	private String deleteFlag = "0";
    
    /** 同步ERP错误记录*/
    @Column(name = "c_sync_erp_log")
    private String syncErpLog;
    
    @Transient
	private List<Map<Object, Object>> linesList;
    
    @Transient
	private List<Map<Object, Object>> receiptList;
	
    @Transient 
    private DeliveryLogistics logistics ;
	
	public List<Map<Object, Object>> getLinesList() {
		return linesList;
	}

	public void setLinesList(List<Map<Object, Object>> linesList) {
		this.linesList = linesList;
	}

	public List<Map<Object, Object>> getReceiptList() {
		return receiptList;
	}

	public void setReceiptList(List<Map<Object, Object>> receiptList) {
		this.receiptList = receiptList;
	}

	public DeliveryLogistics getLogistics() {
		return logistics;
	}

	public void setLogistics(DeliveryLogistics logistics) {
		this.logistics = logistics;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Object getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("ORDER_CONTROL_STATUS", status);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setStatusLable(Object statusLable) {
		this.statusLable = statusLable;
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

	public String getProposerId() {
		return proposerId;
	}

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public String getBillCustomerId() {
		return billCustomerId;
	}

	public void setBillCustomerId(String billCustomerId) {
		this.billCustomerId = billCustomerId;
	}

	public String getBillCustomerName() {
		return billCustomerName;
	}

	public void setBillCustomerName(String billCustomerName) {
		this.billCustomerName = billCustomerName;
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

	public String getReceCustomerId() {
		return receCustomerId;
	}

	public void setReceCustomerId(String receCustomerId) {
		this.receCustomerId = receCustomerId;
	}

	public String getReceCustomerCode() {
		return receCustomerCode;
	}

	public void setReceCustomerCode(String receCustomerCode) {
		this.receCustomerCode = receCustomerCode;
	}

	public String getReceCustomerName() {
		return receCustomerName;
	}

	public void setReceCustomerName(String receCustomerName) {
		this.receCustomerName = receCustomerName;
	}


	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public String getSyncErpLog() {
		return syncErpLog;
	}

	public void setSyncErpLog(String syncErpLog) {
		this.syncErpLog = syncErpLog;
	}
   
}