package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.*;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wutw  2017-1-10
 * 
 */
@Entity
@Table(name = "crm_t_biz_support_apply")
@Permission(businessType = "SupportApply")
public class SupportApply extends ComEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "crm_t_biz_support_apply_id_generator")
	@GenericGenerator(name="crm_t_biz_support_apply_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	@PermissionBusinessId
	private String id;
	
	/** 申请人ID */
    @Column(name = "c_applicant_id", nullable = true, length = 32)
    private String applicantId;
    
    /** 申请人 */
	@Column(name = "C_applicant")
	private String applicant;
	
	 /** 客户ID */
    @Column(name = "c_client_id", nullable = true, length = 32)
    private String clientId;
    
    /** 客户名称 */
    @Column(name = "c_client_name", nullable = true, length = 300)
    private String clientName;
	
	@Column(name = "C_number")    
	private String number;  
	@Column(name = "C_apply_enterprise")        
	private String applyEnterprise;
	/** 商机ID */
    @Column(name = "c_biz_opp_id", nullable = true, length = 32)
    private String bizOppId;
    
    /** 商机名称 */
	@Column(name = "C_biz_opp_name")
	private String bizOppName;

	@Column(name = "C_department")
	private String department;
	/** 商机进展 */
	@Column(name = "C_biz_opp_progress")    
	private String bizOppProgress;

	@Transient
	private String bizOppProgressName;

	public void setBizOppProgressName(String bizOppProgressName) {
		this.bizOppProgressName = bizOppProgressName;
	}

	@Column(name = "C_status")
	private String status;

	@Column(name = "remark")
	private String remark;

	@Transient
	private String statusName;
	
	/** 要求开始日期 */
    @Column(name = "dt_request_start")
    private Date requestStart;
    
    /** 机房面积 */
    @Column(name = "c_room_area")
    private String roomArea;
    
	@Column(name = "n_report_price")           
	private double reportPrice;    
	@Column(name = "C_competitor")
	private String competitor; 
	@Column(name = "n_is_1")     
	private Integer is_1;    
	@Column(name = "n_is_2")        
	private Integer is_2;   
	@Column(name = "n_is_3")         
	private Integer is_3;   
	@Column(name = "n_is_4")         
	private Integer is_4;   
	@Column(name = "n_is_5")         
	private Integer is_5;  
	@Column(name = "n_is_6")          
	private Integer is_6; 
	@Column(name = "n_is_7")           
	private Integer is_7;    
	@Column(name = "n_is_8")        
	private Integer is_8;    
	@Column(name = "n_is_9")        
	private Integer is_9;  
	@Column(name = "n_is_10")          
	private Integer is_10;     
	@Column(name = "n_is_11")
	private Integer is_11;
	@Column(name = "n_is_12")
	private Integer is_12;
    @Column(name = "n_is_13")
	private Integer is_13;
	@Column(name = "PROCESS_ID")
	private String processId;
	
	@Column(name = "ALTITUDE") // 海拔
	private String altitude;
	
	@Column(name = "ELC_TYPE") // 电站性质
	private String elcType;
	
	@Column(name = "BID_AMT") // 投标保证金
	private String bidAmt;
	
	@Column(name = "WARRANTY") // 质保期
	private String warranty;
	
	@Column(name = "DELIVERY") // 交货期
	private String delivery;
	
	@Column(name = "BID_METHOD") // 标书交付方式
	private String bidMethod;
	
	@Column(name = "PAY_TYPE") // 付款方式
	private String payType;
	
	@Column(name = "ARREARS") // 欠款情况
	private String arrears;
	
	@Column(name = "POST_ADDRESS") //邮寄地址
	private String postAddress;
	
	@Column(name = "PERSON")       //联系人
	private String person;
	
	@Column(name = "TEL")          //电话
	private String tel;

	// 投标结果
	@Column(name = "C_BID_RESULTS")
	private String bidResults;
	
	// 丢标 赢标原因
	@Column(name = "C_BID_RESULTS_Reason")
	private String bidResReason;
	
	// 反馈投标结果附件
	@Column(name = "C_BIDRSTATT")
	private String bidRstatt;

	// 主要竞争对手
	@Column(name = "C_BIDCMPR")
	private String bidCmpr;
	
	// 中标品牌
	@Column(name = "C_BIDBRD")
	private String bidBrd;
		
	// 中标价格
	@Column(name = "N_BIDAMOUNT")
	private String bidAmount;
	
	//项目情况
	@Column(name = "PROJECT_DETAIL")      
	private String projectDetail;
	
	// 客户参加人员/职位
	@Column(name = "C_CUSTOM_JOIN_PERSON")
	private String customJoinPerson;
	
	//客户参加主要领导联系电话
	@Column(name = "C_CUSTOM_PHONE")
	private String customPhone;
	
	//邮箱
	@Column(name = "C_CUSTOM_EMAIL")
	private String customEmail;
	
	//需要提前准备的资料
	@Column(name = "C_CUSTOM_DATA")
	private String customData;
	
	public String getStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("PROCESS_STATUS_" + status);
		return lov == null ? "" : lov.getName();

	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getApplyEnterprise() {
		return applyEnterprise;
	}

	public void setApplyEnterprise(String applyEnterprise) {
		this.applyEnterprise = applyEnterprise;
	}

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

	public String getBizOppName() {
		return bizOppName;
	}

	public void setBizOppName(String bizOppName) {
		this.bizOppName = bizOppName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBizOppProgress() {
		return bizOppProgress;
	}

	public void setBizOppProgress(String bizOppProgress) {
		this.bizOppProgress = bizOppProgress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getReportPrice() {
		return reportPrice;
	}

	public void setReportPrice(double reportPrice) {
		this.reportPrice = reportPrice;
	}

	public String getCompetitor() {
		return competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	public Integer getIs_1() {
		return is_1;
	}

	public void setIs_1(Integer is_1) {
		this.is_1 = is_1;
	}

	public Integer getIs_2() {
		return is_2;
	}

	public void setIs_2(Integer is_2) {
		this.is_2 = is_2;
	}

	public Integer getIs_3() {
		return is_3;
	}

	public void setIs_3(Integer is_3) {
		this.is_3 = is_3;
	}

	public Integer getIs_4() {
		return is_4;
	}

	public void setIs_4(Integer is_4) {
		this.is_4 = is_4;
	}

	public Integer getIs_5() {
		return is_5;
	}

	public void setIs_5(Integer is_5) {
		this.is_5 = is_5;
	}

	public Integer getIs_6() {
		return is_6;
	}

	public void setIs_6(Integer is_6) {
		this.is_6 = is_6;
	}

	public Integer getIs_7() {
		return is_7;
	}

	public void setIs_7(Integer is_7) {
		this.is_7 = is_7;
	}

	public Integer getIs_8() {
		return is_8;
	}

	public void setIs_8(Integer is_8) {
		this.is_8 = is_8;
	}

	public Integer getIs_9() {
		return is_9;
	}

	public void setIs_9(Integer is_9) {
		this.is_9 = is_9;
	}

	public Integer getIs_10() {
		return is_10;
	}

	public void setIs_10(Integer is_10) {
		this.is_10 = is_10;
	}

	public Integer getIs_11() {
		return is_11;
	}

	public void setIs_11(Integer is_11) {
		this.is_11 = is_11;
	}

	public String getBizOppProgressName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(bizOppProgress);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();	
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getRequestStart() {
		return requestStart;
	}

	public void setRequestStart(Date requestStart) {
		this.requestStart = requestStart;
	}

	public String getRoomArea() {
		return roomArea;
	}

	public void setRoomArea(String roomArea) {
		this.roomArea = roomArea;
	}

	public Integer getIs_12() {
		return is_12;
	}

	public void setIs_12(Integer is_12) {
		this.is_12 = is_12;
	}

	public Integer getIs_13() {
		return is_13;
	}

	public void setIs_13(Integer is_13) {
		this.is_13 = is_13;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getElcType() {
		return elcType;
	}

	public void setElcType(String elcType) {
		this.elcType = elcType;
	}

	public String getBidAmt() {
		return bidAmt;
	}

	public void setBidAmt(String bidAmt) {
		this.bidAmt = bidAmt;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getBidMethod() {
		return bidMethod;
	}

	public void setBidMethod(String bidMethod) {
		this.bidMethod = bidMethod;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBidResults() {
		return bidResults;
	}

	public void setBidResults(String bidResults) {
		this.bidResults = bidResults;
	}

	public String getBidResReason() {
		return bidResReason;
	}

	public void setBidResReason(String bidResReason) {
		this.bidResReason = bidResReason;
	}

	public String getBidRstatt() {
		return bidRstatt;
	}

	public void setBidRstatt(String bidRstatt) {
		this.bidRstatt = bidRstatt;
	}

	public String getBidCmpr() {
		return bidCmpr;
	}

	public void setBidCmpr(String bidCmpr) {
		this.bidCmpr = bidCmpr;
	}

	public String getBidBrd() {
		return bidBrd;
	}

	public void setBidBrd(String bidBrd) {
		this.bidBrd = bidBrd;
	}

	public String getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(String bidAmount) {
		this.bidAmount = bidAmount;
	}

	public String getProjectDetail() {
		return projectDetail;
	}

	public void setProjectDetail(String projectDetail) {
		this.projectDetail = projectDetail;
	}

	public String getCustomJoinPerson() {
		return customJoinPerson;
	}

	public void setCustomJoinPerson(String customJoinPerson) {
		this.customJoinPerson = customJoinPerson;
	}

	public String getCustomPhone() {
		return customPhone;
	}

	public void setCustomPhone(String customPhone) {
		this.customPhone = customPhone;
	}

	public String getCustomEmail() {
		return customEmail;
	}

	public void setCustomEmail(String customEmail) {
		this.customEmail = customEmail;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}
	

}
