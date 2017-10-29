package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.team.Orgs;
import com.ibm.kstar.permission.utils.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Wutw  2016-12-26
 * 
 */
@Entity
@Table(name = "crm_t_prototype_apply_return")
@Permission(businessType = "PrototypeApplyReturn")
public class PrototypeApplyReturn extends ComEntity implements Serializable {
	
	/** 版本号 */
    private static final long serialVersionUID = 4364751444874356891L;
	
	/** primary key */
	@Id
	@GeneratedValue(generator = "prototype_apply_return_generator")
	@GenericGenerator(name="prototype_apply_return_generator", strategy="uuid")
	@Column(name = "C_PID")
	@PermissionBusinessId
	private String id;
	
    /** 申请人 */
    @Column(name = "c_applicant_id", nullable = true, length = 32)
    private String applicantId;
    
    /** 部门 */
    @Column(name = "c_department")
    private String department;
    
    /** 编号 */
    @Column(name = "c_number", nullable = true, length = 30)
    private String number;
    
    /** 期望到货时间 */
    @Column(name = "dt_plan_recieve_time", nullable = true)
    private Date planRecieveTime;
    
    /** 预计归还时间 */
    @Column(name = "dt_plan_return_time", nullable = true)
    private Date planReturnTime;
    
    /** 客户ID */
    @Column(name = "c_client_id", nullable = true, length = 32)
    private String clientId;
    
    /** 客户名称 */
    @Column(name = "c_client_name", nullable = true, length = 200)
    private String clientName;
    
    /** 商机ID */
    @Column(name = "c_bizopp_id", nullable = true, length = 32)
    private String bizOppId;
    
    /** 商机名称 */
    @Column(name = "c_bizopp_name", nullable = true, length = 200)
    private String bizoppName;
    
    /** 状态 */
    @Column(name = "c_status", nullable = true, length = 10)
    private String status;

	@Transient
	private String statusName;

	public String getStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("PROCESS_STATUS_" + status);
		return lov == null ? "" : lov.getName();
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 样机型号及描述
	 */
	@Column(name = "c_prototype_type_disc", nullable = true, length = 200)
    private String prototypeTypeDisc;
    
    /** 是否免费赠送 */
    @Column(name = "c_is_for_free", nullable = true, length = 32)
    private String isForFree;
    
    /** 免费赠送原因 */
    @Column(name = "c_free_reason", nullable = true, length = 1000)
    private String freeReason;
    
    /** 备注 */
    @Column(name = "c_remark", nullable = true, length = 1000)
    private String remark;
    
    /** 样机销售报价 */
    @Column(name = "c_prototype_price", nullable = true, length = 32)
    private String prototypePrice;
    
    /** 销售方式 */
    @Column(name = "c_sale_method", nullable = true, length = 10)
    private String saleMethod;
    
    /** 订单编号 */
    @Column(name = "c_order_number", nullable = true, length = 30)
    private String orderNumber;
    
    /** 确认已领取 */
    @Column(name = "c_get_confirm", nullable = true, length = 32)
    private String receiveConfirm;
    
    /** 样机领取备注 */
    @Column(name = "c_get_remark", nullable = true, length = 1000)
    private String receiveRemark;
    
    /** 收货人地址 */
    @Column(name = "c_reciever_address", nullable = true, length = 80)
    private String recieverAddress;
    
    /** 收货人 */
    @Column(name = "c_reciever", nullable = true, length = 10)
    private String reciever;
    
    /** 收货电话 */
    @Column(name = "c_reciever_phone", nullable = true, length = 20)
    private String recieverPhone;
    
    /** 申请人确认退还接收 */
    @Column(name = "c_applicant_r_confirm", nullable = true, length = 32)
    private String applicantRConfirm;
    
    /** 样机状况备注 */
    @Column(name = "c_prototype_condition", nullable = true, length = 200)
    private String prototypeCondition;
    
    /** 接收人确认退还接收 */
    @Column(name = "c_reciever_confirm", nullable = true, length = 32)
    private String recieverConfirm;
    
    /** 样机状态 */
    @Column(name = "c_prototype_status", nullable = true, length = 10)
    private String prototypeStatus;
    
    /** 样机接收备注 */
    @Column(name = "c_recieved_condition", nullable = true, length = 200)
    private String recievedCondition;
    
    /** 申请单位 */
    @Column(name = "c_apply_unit")
    private String applyUnit;


	@Transient
	private String applicantName;

	@Transient
	private String departmentName;

	public String getApplicantName() {
		return ((Employee)CacheData.getInstance().get(applicantId)).getName();
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getDepartmentName() {
		return ((LovMember)CacheData.getInstance().get(department)).getName();
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getPlanRecieveTime() {
		return planRecieveTime;
	}

	public void setPlanRecieveTime(Date planRecieveTime) {
		this.planRecieveTime = planRecieveTime;
	}

	public Date getPlanReturnTime() {
		return planReturnTime;
	}

	public void setPlanReturnTime(Date planReturnTime) {
		this.planReturnTime = planReturnTime;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

	public String getBizoppName() {
		return bizoppName;
	}

	public void setBizoppName(String bizoppName) {
		this.bizoppName = bizoppName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrototypeTypeDisc() {
		return prototypeTypeDisc;
	}

	public void setPrototypeTypeDisc(String prototypeTypeDisc) {
		this.prototypeTypeDisc = prototypeTypeDisc;
	}

	public String getIsForFree() {
		return isForFree;
	}

	public void setIsForFree(String isForFree) {
		this.isForFree = isForFree;
	}

	public String getFreeReason() {
		return freeReason;
	}

	public void setFreeReason(String freeReason) {
		this.freeReason = freeReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPrototypePrice() {
		return prototypePrice;
	}

	public void setPrototypePrice(String prototypePrice) {
		this.prototypePrice = prototypePrice;
	}

	public String getSaleMethod() {
		return saleMethod;
	}

	public void setSaleMethod(String saleMethod) {
		this.saleMethod = saleMethod;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getReceiveConfirm() {
		return receiveConfirm;
	}

	public void setReceiveConfirm(String receiveConfirm) {
		this.receiveConfirm = receiveConfirm;
	}

	public String getReceiveRemark() {
		return receiveRemark;
	}

	public void setReceiveRemark(String receiveRemark) {
		this.receiveRemark = receiveRemark;
	}

	public String getRecieverAddress() {
		return recieverAddress;
	}

	public void setRecieverAddress(String recieverAddress) {
		this.recieverAddress = recieverAddress;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public String getRecieverPhone() {
		return recieverPhone;
	}

	public void setRecieverPhone(String recieverPhone) {
		this.recieverPhone = recieverPhone;
	}

	public String getApplicantRConfirm() {
		return applicantRConfirm;
	}

	public void setApplicantRConfirm(String applicantRConfirm) {
		this.applicantRConfirm = applicantRConfirm;
	}

	public String getPrototypeCondition() {
		return prototypeCondition;
	}

	public void setPrototypeCondition(String prototypeCondition) {
		this.prototypeCondition = prototypeCondition;
	}

	public String getRecieverConfirm() {
		return recieverConfirm;
	}

	public void setRecieverConfirm(String recieverConfirm) {
		this.recieverConfirm = recieverConfirm;
	}

	public String getPrototypeStatus() {
		return prototypeStatus;
	}

	public void setPrototypeStatus(String prototypeStatus) {
		this.prototypeStatus = prototypeStatus;
	}

	public String getRecievedCondition() {
		return recievedCondition;
	}

	public void setRecievedCondition(String recievedCondition) {
		this.recievedCondition = recievedCondition;
	}

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}

//	public String getApplicanName() {
//		LovMember lov = new LovMember();
//		Object obj = CacheData.getInstance().get(applicanName);
//		if(obj != null ){
//			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
//		}
//		return  applicanName; 
//	}
}
