package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
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
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * @author Wutw  2016-12-28
 *
 */
@Entity
@Table(name = "CRM_T_BID")
@Permission(businessType = "bid")
public class Bid extends ComEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "crm_t_bid_id_generator")
	@GenericGenerator(name="crm_t_bid_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	@PermissionBusinessId
	private String id;

	/** 授权名称 */
	@Column(name = "c_authorization_name")
	private String authorizationName;

	/** 授权名称 */
	@Column(name = "c_authorization_disc")
	private String authorizationDisc;

	/** 招标单位 */
	@Column(name = "c_bid_enterprise")
	private String bidEnterprise;

	/** 招标编号 */
	@Column(name = "c_bid_number")
	private String bidNumber;

	/** 商机ID */
	@Column(name = "c_business_opp_id")
	private String bizOppId;

	/** 商机名称 */
	@Column(name = "c_business_opp_name")
	private String bizOppName;

	/** 招标授权编号 */
	@Column(name = "c_bid_auth_number")
	private String bidAuthNumber;

	/** 代理商 */
	@Column(name = "c_agency")
	private String agency;

	@Column(name = "c_agency_name")
	private String agencyName;

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	/** 开标时间 */
	@Column(name = "dt_bid_start_date")
	private Date bidStartDate;

	/** 状态 */
	@Column(name = "c_status")
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
	 * 终端客户
	 */
	@Column(name = "c_terminal_client")
	private String terminalClient;

	/** 项目所在地 */
	@Column(name = "c_project_address")
	private String projectAddress;

	/**
	 * 项目名称
	 */
	@Column(name = "project_Name")
	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/** 行业 */
	@Column(name = "c_industry")
	private String industry;

	/** 是否集成 */
	@Column(name = "c_is_integrated")
	private String isIntegrated;

	/** 是否跨区 */
	@Column(name = "c_is_cross_district")
	private String isCrossDistrict;

	/** 跨区部门 */
	@Column(name = "c_cross_department")
	private String crossDepartment;

	/** 涉及行业部 */
	@Column(name = "c_involve_industry")
	private String involveIndustry;

	/** 负责人 */
	@Column(name = "c_person_in_charge")
	private String personInCharge;

	/** 联系方式 */
	@Column(name = "c_connect_method")
	private String connectMethod;

	/** 负责部门 */
	@Column(name = "c_responsible_dep")
	private String responsibleDep;

	/** 已授权未投标说明 */
	@Column(name = "c_auth_nobid_disc")
	private String authNobidDisc;

	/** 开始时间 */
	@Column(name = "dt_start_time")
	private Date startTime;

	/** 结束时间 */
	@Column(name = "dt_end_time")
	private Date endTime;

	/** 备注 */
	@Column(name = "c_remark")
	private String remark;

	/** 投标结果 */
	@Column(name = "c_bid_result")
	private String bidResult;

	/** 反馈情况 */
	@Column(name = "c_feedback")
	private String feedback;

	/** 投标结果备注 */
	@Column(name = "c_bid_remark")
	private String bidRemark;

	/**
	 * 打印样式
	 */
	@Column(name = "print_Style")
	private String printStyle;

	public String getPrintStyle() {
		return printStyle;
	}

	public void setPrintStyle(String printStyle) {
		this.printStyle = printStyle;
	}

	/**
	 * 分项报价
	 */
	@Column(name = "item_Quote")
	private String itemQuote;

	// 主要竞争对手
	@Column(name = "C_BIDCMPR")
	private String bidCmpr;

	// 中标品牌
	@Column(name = "C_BIDBRD")
	private String bidBrd;

	/**
	 * 总报价
	 */
	@Column(name = "all_Quote")
	private String allQuote;

	/**
	 * 用章形式
	 */
	@Column(name = "C_PSIGN_TYPE")
	private String psignType;


	/**
	 * 公章类型
	 */
	@Column(name = "C_GSIGN_TYPE")
	private String gsignType;

	/**
	 * 用章法人
	 */
	@Column(name = "C_SIGN_USR")
	private String signUsr;

	@Transient
	private String signUsrName;

	/**
	 * 客户名称
	 */
	@Column(name = "C_CUST_NAME")
	private String custName;

	/**
	 * 项目名称
	 */
	@Column(name = "C_PRJ_NAME")
	private String prjName;

	/**
	 * 收件信息
	 */
	@Column(name = "C_RCR_INF")
	private String rcrInf;

	/**
	 * 备注
	 */
	@Column(name = "C_NOTES")
	private String notes;

	/**
	 * 选择类型
	 */
	@Column(name = "C_SLT_TYPE")
	private String sltType;

	public String getSignUsrName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(signUsr));
		return lov==null? null : lov.getName();
	}

	public String getSltType() {
		return sltType;
	}

	public void setSltType(String sltType) {
		this.sltType = sltType;
	}

	public String getPsignType() {
		return psignType;
	}

	public void setPsignType(String psignType) {
		this.psignType = psignType;
	}

	public String getGsignType() {
		return gsignType;
	}

	public void setGsignType(String gsignType) {
		this.gsignType = gsignType;
	}

	public String getSignUsr() {
		return signUsr;
	}

	public void setSignUsr(String signUsr) {
		this.signUsr = signUsr;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getRcrInf() {
		return rcrInf;
	}

	public void setRcrInf(String rcrInf) {
		this.rcrInf = rcrInf;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getItemQuote() {
		return itemQuote;
	}

	public void setItemQuote(String itemQuote) {
		this.itemQuote = itemQuote;
	}

	public String getAllQuote() {
		return allQuote;
	}

	public void setAllQuote(String allQuote) {
		this.allQuote = allQuote;
	}

	/**
	 * 附件文件说明
	 */
	@Column(name = "doc_desc")
	private String docDesc;

	@Column(name = "layer1")
	private String layer1;

	@Column(name = "layer2")
	private String layer2;

	@Column(name = "layer3")
	private String layer3;

	@Column(name = "layer4")
	private String layer4;


	/**
	 * 申请类别
	 */
	@Column(name = "c_apply_type")
	private String applyType;


	/** 投标结果附件 */
	@Column(name = "c_has_attm", nullable = true, length = 32)
	private String hasAttm;
	
	/**业务实体*/
	@Column(name = "BID_ENTITY")
	private String bidEntity;
	
	public String getBidEntity() {
		return bidEntity;
	}

	public void setBidEntity(String bidEntity) {
		this.bidEntity = bidEntity;
	}

	public String getLayer1() {
		return layer1;
	}

	public void setLayer1(String layer1) {
		this.layer1 = layer1;
	}

	public String getLayer2() {
		return layer2;
	}

	public void setLayer2(String layer2) {
		this.layer2 = layer2;
	}

	public String getLayer3() {
		return layer3;
	}

	public void setLayer3(String layer3) {
		this.layer3 = layer3;
	}

	public String getLayer4() {
		return layer4;
	}

	public void setLayer4(String layer4) {
		this.layer4 = layer4;
	}

	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorizationName() {
		return authorizationName;
	}

	public void setAuthorizationName(String authorizationName) {
		this.authorizationName = authorizationName;
	}

	public String getAuthorizationDisc() {
		return authorizationDisc;
	}

	public void setAuthorizationDisc(String authorizationDisc) {
		this.authorizationDisc = authorizationDisc;
	}

	public String getBidEnterprise() {
		return bidEnterprise;
	}

	public void setBidEnterprise(String bidEnterprise) {
		this.bidEnterprise = bidEnterprise;
	}

	public String getBidNumber() {
		return bidNumber;
	}

	public void setBidNumber(String bidNumber) {
		this.bidNumber = bidNumber;
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

	public String getBidAuthNumber() {
		return bidAuthNumber;
	}

	public void setBidAuthNumber(String bidAuthNumber) {
		this.bidAuthNumber = bidAuthNumber;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public Date getBidStartDate() {
		return bidStartDate;
	}

	public void setBidStartDate(Date bidStartDate) {
		this.bidStartDate = bidStartDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminalClient() {
		return terminalClient;
	}

	public void setTerminalClient(String terminalClient) {
		this.terminalClient = terminalClient;
	}

	public String getProjectAddress() {
		return projectAddress;
	}

	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIsIntegrated() {
		return isIntegrated;
	}

	public void setIsIntegrated(String isIntegrated) {
		this.isIntegrated = isIntegrated;
	}

	public String getIsCrossDistrict() {
		return isCrossDistrict;
	}

	public void setIsCrossDistrict(String isCrossDistrict) {
		this.isCrossDistrict = isCrossDistrict;
	}

	public String getCrossDepartment() {
		return crossDepartment;
	}

	public void setCrossDepartment(String crossDepartment) {
		this.crossDepartment = crossDepartment;
	}

	public String getInvolveIndustry() {
		return involveIndustry;
	}

	public void setInvolveIndustry(String involveIndustry) {
		this.involveIndustry = involveIndustry;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getConnectMethod() {
		return connectMethod;
	}

	public void setConnectMethod(String connectMethod) {
		this.connectMethod = connectMethod;
	}

	public String getResponsibleDep() {
		return responsibleDep;
	}

	public void setResponsibleDep(String responsibleDep) {
		this.responsibleDep = responsibleDep;
	}

	public String getAuthNobidDisc() {
		return authNobidDisc;
	}

	public void setAuthNobidDisc(String authNobidDisc) {
		this.authNobidDisc = authNobidDisc;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBidResult() {
		return bidResult;
	}

	public void setBidResult(String bidResult) {
		this.bidResult = bidResult;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getBidRemark() {
		return bidRemark;
	}

	public void setBidRemark(String bidRemark) {
		this.bidRemark = bidRemark;
	}



	public String getProjectAddressName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(projectAddress);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public String getTerminalClientName(){
		CustomInfo customInfo = (CustomInfo)CacheData.getInstance().get(terminalClient);
		if(customInfo != null){
			return customInfo.getCustomFullName() + " | " + customInfo.getCustomCode();
		}
		return "";
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
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

	public String getHasAttm() {
		return hasAttm;
	}

	public void setHasAttm(String hasAttm) {
		this.hasAttm = hasAttm;
	}
}
