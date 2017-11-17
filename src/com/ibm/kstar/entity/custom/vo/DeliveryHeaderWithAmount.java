package com.ibm.kstar.entity.custom.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;

public class DeliveryHeaderWithAmount extends BaseEntity implements
		java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	private String id;

	/** 出货申请头编号 */
	private String deliveryCode;

	/** 申请日期 */
	private Date applyDate;

	/** 状态 */
	private String status;

	/** 状态显示名称 */
	private Object statusLable;

	/** 业务实体 */
	private String businessEntity;

	/** 业务实体显示名称 */
	private String businessEntityLable;

	/** 申请人ID */
	private String proposerId;

	/** 申请人ID */
	private String proposerCode;

	/** 申请人名称 */
	private String proposerName;

	/** 收货客户 */
	private String receCustomerId;

	/** 收货客户 */
	private String receCustomerCode;

	/** 收货客户 */
	private String receCustomerName;

	/** 收货地点 ID */
	private String deliveryAddressId;

	/** 收货地点 */
	private String deliveryAddress;

	/** 收货人 */
	private String consignee;

	/** 收货人电话 */
	private String consigneeTel;

	/** 收单客户 */
	private String billCustomerId;

	/** 收单客户 */
	private String billCustomerName;

	/** 备注 */
	private String remarks;
	
	/** 发货金额 */
	private BigDecimal deliveryAmount;
	
	/** 已收款金额 */
	private BigDecimal receiveAmount;
	
	/** 收款余额 */
	private BigDecimal balance;

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
		Object obj = CacheData.getInstance().getMember("ORDER_CONTROL_STATUS",
				status);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
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
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
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

	public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



}