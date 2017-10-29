package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

@Entity
@Table(name = "crm_t_custom_event_info")
@Permission(businessType = "CUSTOM_EVENT_PROC")
public class CustomEventInfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_event_info_pid_generator")
	@GenericGenerator(name="custom_event_info_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 活动编号 */
    @Column(name = "c_event_code")
    private String eventCode;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_code")
    private String customCode;
    
    /** 客户名称(已报备用户) */
    @Column(name = "C_CUSTOM_FULL_NAME")
    private String customName;
    
    /** 客户名称(未报备用户) */
    @Column(name = "C_CUSTOM_NAME_NO_RPT")
    private String customNameNoRpt;
    
    /** 客户名称 */
    @Transient
    private String customNameDisp;
    
    public String getCustomNameDisp() {
    	if (StringUtils.isEmpty(customCode)) {
    		return customNameNoRpt;
    	} else {
    		return customName;
    	}
	}
    
    /** 项目编码 */
    @Column(name = "c_bizopp_code")
    private String bizoppCode;
    
    /** 项目名称 */
    @Column(name = "c_bizopp_name")
    private String bizoppName;
    
    /** 创建日期 */
    @Column(name = "c_event_create_date", nullable = true)
    private String eventCreateDate;
    
    /** 活动状态 */
    @Column(name = "c_event_status", nullable = true, length = 32)
    private String eventStatus;
    
    @Transient
	private String eventStatusName;
    
    public String getEventStatusName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(eventStatus));
		return lov==null? null : lov.getName();
	}
    
    public void setEventStatusName(String eventStatusName) {
		this.eventStatusName = eventStatusName;
	}
    
    /** 客户来访时间 */
    @Column(name = "c_event_visit_term", nullable = true)
    private Date eventVisitTerm;
    
    /** 逗留天数 */
    @Column(name = "c_event_stay_days", nullable = true, length = 100)
    private String eventStayDays;
    
    /** 活动目的 */
    @Column(name = "c_event_goal", nullable = true, length = 100)
    private String eventGoal;
    
    /** 申请人 */
    @Column(name = "APPLIER_ID", nullable = true, length = 32)
    private String applier;
    
    @Transient
	private String applierName;
    
    public String getApplierName() {
    	Employee lov =  ((Employee)CacheData.getInstance().get(applier));
		return lov==null? null : lov.getName().concat("/").concat(lov.getNo());
	}
    
    /** 申请人岗位 */
    @Column(name = "APPLIER_POS_ID", nullable = true, length = 32)
    private String applierPos;
    
    /** 申请人组织 */
    @Column(name = "APPLIER_ORG_ID", nullable = true, length = 32)
    private String applierOrg;
    
    @Transient
	private String applierOrgName;
    
    public String getApplierOrgName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(applierOrg));
		return lov==null? null : lov.getNamePath();
	}
    
    /** 联系方式 */
    @Column(name = "c_create_contact", nullable = true, length = 32)
    private String createContact;
    
    /** 来访人数 */
    @Column(name = "c_person_cnt", nullable = true, length = 32)
    private String personCnt;
    
    /** 客户等级 */
    @Column(name = "c_custom_grade", nullable = true, length = 32)
    private String customGrade;
    
    @Transient
   	private String customGradeName;

	public String getCustomGradeName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customGrade));
		return lov == null ? null : lov.getName();
	}
    
    /** 客户重要程度 */
    @Column(name = "c_event_lel", nullable = true, length = 32)
    private String eventLev;
    
    
    /** 欢迎辞 */
    @Column(name = "c_event_profile", nullable = true, length = 100)
    private String eventProfile;
    
    /** 客户启程日期 */
    @Column(name = "c_event_departure_date", nullable = true)
    private Date eventDepartureDate;
    
    /** 客户启程时间 */
    @Column(name = "c_event_departure_time", nullable = true)
    private String eventDepartureTime;
    
    /** 预计到达日期 */
    @Column(name = "c_event_arrival_date", nullable = true)
    private Date eventArrivalDate;
    
    /** 预计到达时间 */
    @Column(name = "c_event_arrival_time", nullable = true)
    private String eventArrivalTime;
    
    /** 到达地点 */
    @Column(name = "c_event_arrival_location", nullable = true, length = 100)
    private String eventArrivalLocation;
    
    /** 车(船)次航班 */
    @Column(name = "c_event_flight", nullable = true, length = 100)
    private String eventFlight;
    
    /** 住宿标准 */
    @Column(name = "c_event_acco_label", nullable = true, length = 100)
    private String eventAccoLabel;
    
    /** 餐饮标准 */
    @Column(name = "c_event_food_label", nullable = true, length = 100)
    private String eventFoodLabel;
    
    /** 高层领导参加 */
    @Column(name = "c_event_lead_in", nullable = true, length = 100)
    private String eventLeadIn;
    
    /** 讲解PPT及技术 */
    @Column(name = "c_event_tect", nullable = true, length = 100)
    private String eventTect;
    
    /** 承担部门 */
    @Column(name = "c_event_bear_deaprtment", nullable = true, length = 100)
    private String eventBearDeaprtment;
    
	@Transient
	private String eventBearDeaprtmentName;

	public String getEventBearDeaprtmentName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(
				eventBearDeaprtment));
		return lov == null ? null : lov.getName();
	}
    
    /** 承担方案 */
    @Column(name = "c_event_bear_plan", nullable = true, length = 32)
    private String eventBearPlan;
    
    @Transient
	private String eventBearPlanName;
    
    public String getEventBearPlanName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(eventBearPlan));
		return lov==null? null : lov.getName();
	}
    
    /** 客户关注点 */
    @Column(name = "c_event_aim", nullable = true, length = 100)
    private String eventAim;
    
	/** 客户关注点 技术 */
	@Column(name = "n_is_1")
	private String is1;
	/** 客户关注点 规模 */
	@Column(name = "n_is_2")
	private String is2;
	/** 客户关注点 生产 */
	@Column(name = "n_is_3")
	private String is3;
	/** 客户关注点 体系 */
	@Column(name = "n_is_4")
	private String is4;
	/** 客户关注点 价格 */
	@Column(name = "n_is_5")
	private String is5;
	/** 客户关注点 服务 */
	@Column(name = "n_is_6")
	private String is6;
	/** 客户关注点 其他 */
	@Column(name = "n_is_7")
	private String is7;
	
	/** 协助事项 会议室准备 */
	@Column(name = "n_is_11")
	private String is11;
	/** 协助事项 展厅准备 */
	@Column(name = "n_is_12")
	private String is12;
	/** 协助事项 公司介绍 */
	@Column(name = "n_is_13")
	private String is13;
	/** 协助事项 技术交流 */
	@Column(name = "n_is_14")
	private String is14;
	/** 协助事项 生产参观 */
	@Column(name = "n_is_15")
	private String is15;
	/** 协助事项 高层接待 */
	@Column(name = "n_is_16")
	private String is16;
	
	/** 协助事项 制作欢迎牌 */
	@Column(name = "n_is_17")
	private String is17;
	
	/** 协助事项 样板项目参观 */
	@Column(name = "n_is_18")
	private String is18;
    
    /** 是否赠送礼品及规格 */
    @Column(name = "c_event_gift", nullable = true, length = 100)
    private String eventGift;
    
    /** 行程安排 */
    @Column(name = "c_event_plan", nullable = true, length = 100)
    private String eventPlan;
    
    /** 其他要求 */
    @Column(name = "c_event_other", nullable = true, length = 100)
    private String eventOther;
    
    /** 实际逗留天数 */
    @Column(name = "c_event_actual_days", nullable = true, length = 100)
    private String eventActualDays;
    
    /** 效果评估 */
    @Column(name = "c_event_evaluation", nullable = true, length = 100)
    private String eventEvaluation;
    
    /** 效果概述 */
    @Column(name = "c_event_result", nullable = true, length = 100)
    private String eventResult;
    
    /** 手机 */
    @Column(name = "c_mobile", nullable = true, length = 100)
    private String mobile;
    
    /** 负责人 */
    @Column(name = "c_charge_person", nullable = true, length = 32)
    private String chargePerson;
    
    @Transient
	private String chargePersonNo;
    
    public String getChargePersonNo() {
    	
    	Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(chargePerson);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getNo();
	}
    
    @Transient
	private String chargePersonName;
    
    public String getChargePersonName() {
    	Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(chargePerson);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getName();
	}
    
    
    
    /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    @PermissionUserId
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    @PermissionPositionId
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    @PermissionOrgId
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getId() {
		return id;
	}

	public String getEventCode() {
		return eventCode;
	}

	public String getCustomCode() {
		return customCode;
	}

	public String getCustomName() {
		return customName;
	}

	public String getBizoppCode() {
		return bizoppCode;
	}

	public String getBizoppName() {
		return bizoppName;
	}

	public String getEventCreateDate() {
		return eventCreateDate;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public Date getEventVisitTerm() {
		return eventVisitTerm;
	}

	public String getEventStayDays() {
		return eventStayDays;
	}

	public String getEventGoal() {
		return eventGoal;
	}

	public String getApplier() {
		return applier;
	}

	public String getApplierPos() {
		return applierPos;
	}

	public String getApplierOrg() {
		return applierOrg;
	}

	public String getCreateContact() {
		return createContact;
	}

	public String getPersonCnt() {
		return personCnt;
	}

	public String getCustomGrade() {
		return customGrade;
	}

	public String getEventLev() {
		return eventLev;
	}

	public String getEventProfile() {
		return eventProfile;
	}

	public Date getEventDepartureDate() {
		return eventDepartureDate;
	}

	public String getEventDepartureTime() {
		return eventDepartureTime;
	}

	public Date getEventArrivalDate() {
		return eventArrivalDate;
	}

	public String getEventArrivalTime() {
		return eventArrivalTime;
	}

	public String getEventArrivalLocation() {
		return eventArrivalLocation;
	}

	public String getEventFlight() {
		return eventFlight;
	}

	public String getEventAccoLabel() {
		return eventAccoLabel;
	}

	public String getEventFoodLabel() {
		return eventFoodLabel;
	}

	public String getEventLeadIn() {
		return eventLeadIn;
	}

	public String getEventTect() {
		return eventTect;
	}

	public String getEventBearDeaprtment() {
		return eventBearDeaprtment;
	}

	public String getEventBearPlan() {
		return eventBearPlan;
	}

	public String getEventAim() {
		return eventAim;
	}

	public String getIs1() {
		return is1;
	}

	public String getIs2() {
		return is2;
	}

	public String getIs3() {
		return is3;
	}

	public String getIs4() {
		return is4;
	}

	public String getIs5() {
		return is5;
	}

	public String getIs6() {
		return is6;
	}

	public String getIs7() {
		return is7;
	}

	public String getEventGift() {
		return eventGift;
	}

	public String getEventPlan() {
		return eventPlan;
	}

	public String getEventOther() {
		return eventOther;
	}

	public String getEventActualDays() {
		return eventActualDays;
	}

	public String getEventEvaluation() {
		return eventEvaluation;
	}

	public String getEventResult() {
		return eventResult;
	}

	public String getMobile() {
		return mobile;
	}

	public String getChargePerson() {
		return chargePerson;
	}

	public String getCreatedById() {
		return createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getCreatedPosId() {
		return createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public void setBizoppCode(String bizoppCode) {
		this.bizoppCode = bizoppCode;
	}

	public void setBizoppName(String bizoppName) {
		this.bizoppName = bizoppName;
	}

	public void setEventCreateDate(String eventCreateDate) {
		this.eventCreateDate = eventCreateDate;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public void setEventVisitTerm(Date eventVisitTerm) {
		this.eventVisitTerm = eventVisitTerm;
	}

	public void setEventStayDays(String eventStayDays) {
		this.eventStayDays = eventStayDays;
	}

	public void setEventGoal(String eventGoal) {
		this.eventGoal = eventGoal;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public void setApplierPos(String applierPos) {
		this.applierPos = applierPos;
	}

	public void setApplierOrg(String applierOrg) {
		this.applierOrg = applierOrg;
	}

	public void setApplierOrgName(String applierOrgName) {
		this.applierOrgName = applierOrgName;
	}

	public void setCreateContact(String createContact) {
		this.createContact = createContact;
	}

	public void setPersonCnt(String personCnt) {
		this.personCnt = personCnt;
	}

	public void setCustomGrade(String customGrade) {
		this.customGrade = customGrade;
	}

	public void setCustomGradeName(String customGradeName) {
		this.customGradeName = customGradeName;
	}

	public void setEventLev(String eventLev) {
		this.eventLev = eventLev;
	}

	public void setEventProfile(String eventProfile) {
		this.eventProfile = eventProfile;
	}

	public void setEventDepartureDate(Date eventDepartureDate) {
		this.eventDepartureDate = eventDepartureDate;
	}

	public void setEventDepartureTime(String eventDepartureTime) {
		this.eventDepartureTime = eventDepartureTime;
	}

	public void setEventArrivalDate(Date eventArrivalDate) {
		this.eventArrivalDate = eventArrivalDate;
	}

	public void setEventArrivalTime(String eventArrivalTime) {
		this.eventArrivalTime = eventArrivalTime;
	}

	public void setEventArrivalLocation(String eventArrivalLocation) {
		this.eventArrivalLocation = eventArrivalLocation;
	}

	public void setEventFlight(String eventFlight) {
		this.eventFlight = eventFlight;
	}

	public void setEventAccoLabel(String eventAccoLabel) {
		this.eventAccoLabel = eventAccoLabel;
	}

	public void setEventFoodLabel(String eventFoodLabel) {
		this.eventFoodLabel = eventFoodLabel;
	}

	public void setEventLeadIn(String eventLeadIn) {
		this.eventLeadIn = eventLeadIn;
	}

	public void setEventTect(String eventTect) {
		this.eventTect = eventTect;
	}

	public void setEventBearDeaprtment(String eventBearDeaprtment) {
		this.eventBearDeaprtment = eventBearDeaprtment;
	}

	public void setEventBearDeaprtmentName(String eventBearDeaprtmentName) {
		this.eventBearDeaprtmentName = eventBearDeaprtmentName;
	}

	public void setEventBearPlan(String eventBearPlan) {
		this.eventBearPlan = eventBearPlan;
	}

	public void setEventBearPlanName(String eventBearPlanName) {
		this.eventBearPlanName = eventBearPlanName;
	}

	public void setEventAim(String eventAim) {
		this.eventAim = eventAim;
	}

	public void setIs1(String is1) {
		this.is1 = is1;
	}

	public void setIs2(String is2) {
		this.is2 = is2;
	}

	public void setIs3(String is3) {
		this.is3 = is3;
	}

	public void setIs4(String is4) {
		this.is4 = is4;
	}

	public void setIs5(String is5) {
		this.is5 = is5;
	}

	public void setIs6(String is6) {
		this.is6 = is6;
	}

	public void setIs7(String is7) {
		this.is7 = is7;
	}

	public void setEventGift(String eventGift) {
		this.eventGift = eventGift;
	}

	public void setEventPlan(String eventPlan) {
		this.eventPlan = eventPlan;
	}

	public void setEventOther(String eventOther) {
		this.eventOther = eventOther;
	}

	public void setEventActualDays(String eventActualDays) {
		this.eventActualDays = eventActualDays;
	}

	public void setEventEvaluation(String eventEvaluation) {
		this.eventEvaluation = eventEvaluation;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public void setChargePersonNo(String chargePersonNo) {
		this.chargePersonNo = chargePersonNo;
	}

	public void setChargePersonName(String chargePersonName) {
		this.chargePersonName = chargePersonName;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getIs11() {
		return is11;
	}

	public void setIs11(String is11) {
		this.is11 = is11;
	}

	public String getIs12() {
		return is12;
	}

	public void setIs12(String is12) {
		this.is12 = is12;
	}

	public String getIs13() {
		return is13;
	}

	public void setIs13(String is13) {
		this.is13 = is13;
	}

	public String getIs14() {
		return is14;
	}

	public void setIs14(String is14) {
		this.is14 = is14;
	}

	public String getIs15() {
		return is15;
	}

	public void setIs15(String is15) {
		this.is15 = is15;
	}

	public String getIs16() {
		return is16;
	}

	public void setIs16(String is16) {
		this.is16 = is16;
	}

	public String getIs17() {
		return is17;
	}

	public void setIs17(String is17) {
		this.is17 = is17;
	}

	public String getIs18() {
		return is18;
	}

	public void setIs18(String is18) {
		this.is18 = is18;
	}

	public String getCustomNameNoRpt() {
		return customNameNoRpt;
	}

	public void setCustomNameNoRpt(String customNameNoRpt) {
		this.customNameNoRpt = customNameNoRpt;
	}


}