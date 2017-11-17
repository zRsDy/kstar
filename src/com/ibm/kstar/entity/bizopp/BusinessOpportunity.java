/**
 * 
 */
package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商机
 * @author Gaoyuliang 2017-2-21
 * 
 */
@Entity
@Table(name = "crm_t_business_opportunity")
@Permission(businessType = "BusinessOpportunity")
public class BusinessOpportunity extends ComEntity implements Serializable {

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 6130872110687264604L;

	/** primary key */
	@Id
	@GeneratedValue(generator = "crm_t_business_opportunity_id_generator")
	@GenericGenerator(name = "crm_t_business_opportunity_id_generator", strategy = "uuid")
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	@PermissionBusinessId
	private String id;

	/** 销售阶段 */
	@Column(name = "c_sale_stage", nullable = true, length = 32)
	private String saleStage;

	@Transient
	private String saleStageName;

	public String getSaleStageName() {
		return CacheData.getInstance().getLovMemberName(this.saleStage);
	}

	public void setSaleStageName(String saleStageName) {
		this.saleStageName = saleStageName;
	}

	/** 业务流程 */
	@Column(name = "c_business_process", nullable = true, length = 32)
	private String businessProcess;

	/** 报备单位 */
	@Column(name = "c_enterprise", nullable = true, length = 32)
	private String enterprise;

	@Transient
	private String enterpriseName;

	public String getEnterpriseName() {
		LovMember lovMember = (LovMember) CacheData.getInstance().get(enterprise);
		if (lovMember != null) {
			return lovMember.getName();
		}
		return "";
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	/** 商机编号 */
	@Column(name = "c_number", nullable = true, length = 32)
	private String number;

	/** 状态 */
	@Column(name = "c_status", nullable = true, length = 32)
	private String status;

	@Transient
	private String statusName;

	public String getStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("OPPORTUNITY_STATUS_" + status);
		return lov == null ? "" : lov.getName();
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 销售员
	 */
	@Column(name = "c_saler_id", nullable = true, length = 32)
	private String salerId;

	/** 销售员 */
	@Column(name = "c_saler_name", nullable = true, length = 32)
	private String salerName;

	/** 商机状态 */
	@Column(name = "c_conflict_status", nullable = true, length = 32)
	private String conflictStatus;

	@Transient
	private String conflictStatusName;

	public String getConflictStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("CONFLICT_STATUS_" + conflictStatus);
		return lov == null ? "" : lov.getName();
	}

	public void setConflictStatusName(String conflictStatusName) {
		this.conflictStatusName = conflictStatusName;
	}

	/**
	 * 销售员电话
	 */
	@Column(name = "c_saler_phone", nullable = true, length = 20)
	private String salerPhone;

	/** 冲突管理单 */
	@Column(name = "c_conflict_number", nullable = true, length = 32)
	private String conflictNumber;

	/** 客户ID */
	@Column(name = "c_client_id", nullable = true, length = 32)
	private String clientId;

	/** 客户名称 */
	@Column(name = "c_client_name", nullable = true, length = 80)
	private String clientName;

	/** 行业 */
	@Column(name = "c_industry", nullable = true, length = 80)
	private String industry;

	@Column(name = "c_industry_code", nullable = true, length = 80)
	private String industryCode;

	public String getIndustryName() {
		return CacheData.getInstance().getLovMemberName(industry);
	}

	/**
	 * 行业
	 */
	@Column(name = "c_industry_sub", nullable = true, length = 80)
	private String industrySub;

	@Column(name = "c_industry_sub_code", nullable = true, length = 80)
	private String industrySubCode;

	public String getIndustrySubName() {
		return CacheData.getInstance().getLovMemberName(industrySub);
	}

	public String getIndustrySub() {
		return industrySub;
	}

	public void setIndustrySub(String industrySub) {
		this.industrySub = industrySub;
	}

	/** 招标单位 */
	@Column(name = "c_bid_unit", nullable = true, length = 80)
	private String bidUnit;

	@Column(name = "C_BID_NO")
	private String bidNo;

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	/** 主要联系人 */
	@Column(name = "c_key_contact", nullable = true, length = 80)
	private String keyContact;

	/** 联系人部门 */
	@Column(name = "c_contact_dept", nullable = true, length = 80)
	private String contactDept;

	/** 联系人电话 */
	@Column(name = "c_contact_phone", nullable = true, length = 20)
	private String contactPhone;

	/** 商机名称 */
	@Column(name = "c_opportunity_name", nullable = true, length = 80)
	private String opportunityName;

	/** 详细地址 */
	@Column(name = "c_address", nullable = true, length = 80)
	private String address;

	/** 预计成交时间 */
	@Column(name = "dt_plan_fin_date", nullable = true, length = 11)
	private Date planFinDate;

	/** 销售方法 */
	@Column(name = "c_sale_method", nullable = true, length = 32)
	private String saleMethod;

	@Transient
	private String saleMethod_;

	/** 成功率 */
	@Column(name = "n_success_rate", nullable = true)
	private String successRate;

	/** 项目概况 */
	@Column(name = "c_project_situation", nullable = true, length = 300)
	private String projectSituation;

	/** 项目推动情况 */
	@Column(name = "c_project_progress", nullable = true, length = 300)
	private String projectProgress;

	/** 是否集成包 */
	@Column(name = "n_is_integreated", nullable = true, length = 22)
	private Integer isIntegreated;

	/** 商机所在地 */
	@Column(name = "c_biz_opp_address", nullable = true, length = 32)
	private String bizOppAddress;

	/** 商机所在地名称 */
	@Column(name = "c_biz_opp_address_name", nullable = true, length = 20)
	private String bizOppAddressName;

	/** 来源 */
	@Column(name = "c_source", nullable = true, length = 10)
	private String source;

	/** 币种 */
	@Column(name = "c_currency", nullable = true, length = 10)
	private String currency;

	/** 预计金额 */
	@Column(name = "n_estimated_amount", nullable = true)
	private BigDecimal estimatedAmount;

	/** 竞争品牌 */
	@Column(name = "c_competitive_brands", nullable = true, length = 32)
	private String competitiveBrands;

	/** 立项类型 */
	@Column(name = "c_project_type", nullable = true, length = 30)
	private String projectType;

	@Column(name = "C_QOUT_NAME") // 报价单名称
	private String qoutName;
	@Column(name = "C_IS_BID_PROJECT") // 是否投标类型
	private String isBidProject;
	@Column(name = "C_IS_PRO_REVIEW") // 是否工程评审
	private String isProReview;
	@Column(name = "C_CONTRNAME") // 合同名称
	private String contrname;

	@Transient
	private String customClassName;

	@Column(name = "C_PRODUCT_PROJECT")
	private String productProject;

	@Column(name = "C_IS_INTEGRATION")
	private String isIntegration;

	@Column(name = "IS_UNION_BID")
	private String isUnionBid;

	public String getIsUnionBid() {
		return isUnionBid;
	}

	public void setIsUnionBid(String isUnionBid) {
		this.isUnionBid = isUnionBid;
	}

	@Column(name = "UNION_CONTACT") // 业务联系人
	private String unionContact;

	@Column(name = "UNION_POSITION") // 职务
	private String unionPosition;

	@Column(name = "UNION_NAME") // 联合单位名称
	private String unionName;

	@Column(name = "UNION_ADDRESS") // 地址
	private String unionAddress;
	@Column(name = "UNION_PHONE") // 电话
	private String unionPhone;
	@Column(name = "UNION_LEGAL") // 法人代表
	private String unionLegal;
	@Column(name = "UNION_EMAIL") // email
	private String unionEmail;

	@Column(name = "sbu_flag")
	private String sbu_flag;

	@Column(name = "layer4")
	private String layer4;

	@Column(name = "layer3")
	private String layer3;

	@Column(name = "layer2")
	private String layer2;

	@Column(name = "layer1")
	private String layer1;
	// 商务联系人
	@Column(name = "COM_CONTACT")
	private String comContact;
	// 技术联系人
	@Column(name = "TEC_CONTACT")
	private String tecContact;
	// 商务部门职务
	@Column(name = "COM_DEPT")
	private String comDept;
	// 技术部门职务
	@Column(name = "TEC_DEPT")
	private String tecDept;

	@Column(name = "brand1")
	private String brand1;

	@Column(name = "brand2")
	private String brand2;

	@Column(name = "brand3")
	private String brand3;

	@Column(name = "brand4")
	private String brand4;

	@Column(name = "brand5")
	private String brand5;
	@Column(name = "OTHER_DESC")
	private String otherDesc;

	public String getOtherDesc() {
		return otherDesc;
	}

	@Column(name = "end_Date")
	private Date endDate;

	@Column(name = "success_Date")
	private Date successDate;

	@Column(name = "ALTITUDE") // 海拔
	private String altitude;
	@Column(name = "ELC_TYPE") // 电站性质
	private String elcType;
	@Column(name = "INTEGRATE_PROD") // 集成产品
	private String integrateProd;
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


    @Column(name = "Conflict_Appeal", columnDefinition="varchar(1) default 'N'", length = 1)
    private String conflictAppeal;
	@Column(name = "conflict_appeal_remark", columnDefinition="varchar(600)",length = 200)
	private String conflictAppealRemark;

	public BusinessOpportunity(){}
	
	
	public BusinessOpportunity(String xuni){
		this.setNumber("KSTAR-SJ-XN01");
		this.setOpportunityName("虚拟商机");
		this.setClientId("KSTAR-SJ-XN01");
		this.setClientName("虚拟商机");
		this.setId("KSTAR-SJ-XN01");
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

	public String getIntegrateProd() {
		return integrateProd;
	}

	public void setIntegrateProd(String integrateProd) {
		this.integrateProd = integrateProd;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}

	public void setOtherDesc(String otherDesc) {
		this.otherDesc = otherDesc;
	}

	public String getBrand1() {
		return brand1;
	}

	public void setBrand1(String brand1) {
		this.brand1 = brand1;
	}

	public String getBrand2() {
		return brand2;
	}

	public void setBrand2(String brand2) {
		this.brand2 = brand2;
	}

	public String getBrand3() {
		return brand3;
	}

	public void setBrand3(String brand3) {
		this.brand3 = brand3;
	}

	public String getBrand4() {
		return brand4;
	}

	public void setBrand4(String brand4) {
		this.brand4 = brand4;
	}

	public String getBrand5() {
		return brand5;
	}

	public void setBrand5(String brand5) {
		this.brand5 = brand5;
	}

	public String getComContact() {
		return comContact;
	}

	public void setComContact(String comContact) {
		this.comContact = comContact;
	}

	public String getTecContact() {
		return tecContact;
	}

	public void setTecContact(String tecContact) {
		this.tecContact = tecContact;
	}

	public String getComDept() {
		return comDept;
	}

	public void setComDept(String comDept) {
		this.comDept = comDept;
	}

	public String getTecDept() {
		return tecDept;
	}

	public void setTecDept(String tecDept) {
		this.tecDept = tecDept;
	}

	public String getLayer4() {
		return layer4;
	}

	public void setLayer4(String layer4) {
		this.layer4 = layer4;
	}

	public String getLayer3() {
		return layer3;
	}

	public void setLayer3(String layer3) {
		this.layer3 = layer3;
	}

	public String getLayer2() {
		return layer2;
	}

	public void setLayer2(String layer2) {
		this.layer2 = layer2;
	}

	public String getLayer1() {
		return layer1;
	}

	public void setLayer1(String layer1) {
		this.layer1 = layer1;
	}

	public String getSbu_flag() {
		return sbu_flag;
	}

	public void setSbu_flag(String sbu_flag) {
		this.sbu_flag = sbu_flag;
	}

	public String getUnionContact() {
		return unionContact;
	}

	public void setUnionContact(String unionContact) {
		this.unionContact = unionContact;
	}

	public String getUnionPosition() {
		return unionPosition;
	}

	public void setUnionPosition(String unionPosition) {
		this.unionPosition = unionPosition;
	}

	public String getUnionName() {
		return unionName;
	}

	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}

	public String getUnionAddress() {
		return unionAddress;
	}

	public void setUnionAddress(String unionAddress) {
		this.unionAddress = unionAddress;
	}

	public String getUnionPhone() {
		return unionPhone;
	}

	public void setUnionPhone(String unionPhone) {
		this.unionPhone = unionPhone;
	}

	public String getUnionLegal() {
		return unionLegal;
	}

	public void setUnionLegal(String unionLegal) {
		this.unionLegal = unionLegal;
	}

	public String getUnionEmail() {
		return unionEmail;
	}

	public void setUnionEmail(String unionEmail) {
		this.unionEmail = unionEmail;
	}

	public String getIsIntegration() {
		return isIntegration;
	}

	public void setIsIntegration(String isIntegration) {
		this.isIntegration = isIntegration;
	}

	public String getProductProject() {
		return productProject;
	}

	public void setProductProject(String productProject) {
		this.productProject = productProject;
	}

	public String getCustomClassName() {
		return customClassName;
	}

	public void setCustomClassName(String customClassName) {
		this.customClassName = customClassName;
	}

	public String getSaleMethod_() {
		return saleMethod;
	}

	public void setSaleMethod_(String saleMethod_) {
		this.saleMethod_ = saleMethod_;
	}

	public String getQoutName() {
		return qoutName;
	}

	public void setQoutName(String qoutName) {
		this.qoutName = qoutName;
	}

	public String getIsBidProject() {
		return isBidProject;
	}

	public void setIsBidProject(String isBidProject) {
		this.isBidProject = isBidProject;
	}

	public String getIsProReview() {
		return isProReview;
	}

	public void setIsProReview(String isProReview) {
		this.isProReview = isProReview;
	}

	public String getContrname() {
		return contrname;
	}

	public void setContrname(String contrname) {
		this.contrname = contrname;
	}

	// @Transient
	// private String statusLable;
	//
	// @Transient
	// private String conflictStatusLable;
	//
	// @Transient
	// private String saleStageLable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSaleStage() {
		return saleStage;
	}

	public void setSaleStage(String saleStage) {
		this.saleStage = saleStage;
	}

	public String getBusinessProcess() {
		return businessProcess;
	}

	public void setBusinessProcess(String businessProcess) {
		this.businessProcess = businessProcess;
	}

	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSalerId() {
		return salerId;
	}

	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}

	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	public String getConflictStatus() {
		return conflictStatus;
	}

	public void setConflictStatus(String conflictStatus) {
		this.conflictStatus = conflictStatus;
	}

	public String getSalerPhone() {
		return salerPhone;
	}

	public void setSalerPhone(String salerPhone) {
		this.salerPhone = salerPhone;
	}

	public String getConflictNumber() {
		return conflictNumber;
	}

	public void setConflictNumber(String conflictNumber) {
		this.conflictNumber = conflictNumber;
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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getKeyContact() {
		return keyContact;
	}

	public void setKeyContact(String keyContact) {
		this.keyContact = keyContact;
	}

	public String getContactDept() {
		return contactDept;
	}

	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getOpportunityName() {
		return opportunityName;
	}

	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getPlanFinDate() {
		return planFinDate;
	}

	public void setPlanFinDate(Date planFinDate) {
		this.planFinDate = planFinDate;
	}

	public String getSaleMethod() {
		return saleMethod;
	}

	public void setSaleMethod(String saleMethod) {
		this.saleMethod = saleMethod;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}

	public String getProjectSituation() {
		return projectSituation;
	}

	public void setProjectSituation(String projectSituation) {
		this.projectSituation = projectSituation;
	}

	public String getProjectProgress() {
		return projectProgress;
	}

	public void setProjectProgress(String projectProgress) {
		this.projectProgress = projectProgress;
	}

	public Integer getIsIntegreated() {
		return isIntegreated;
	}

	public void setIsIntegreated(Integer isIntegreated) {
		this.isIntegreated = isIntegreated;
	}

	public String getBizOppAddress() {
		return bizOppAddress;
	}

	public void setBizOppAddress(String bizOppAddress) {
		this.bizOppAddress = bizOppAddress;
	}

	public String getBizOppAddressName() {
		return bizOppAddressName;
	}

	public void setBizOppAddressName(String bizOppAddressName) {
		this.bizOppAddressName = bizOppAddressName;
	}

	public String getBidUnit() {
		return bidUnit;
	}

	public void setBidUnit(String bidUnit) {
		this.bidUnit = bidUnit;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getEstimatedAmount() {
		return estimatedAmount;
	}

	public void setEstimatedAmount(BigDecimal estimatedAmount) {
		this.estimatedAmount = estimatedAmount;
	}

	public String getCompetitiveBrands() {
		return competitiveBrands;
	}

	public void setCompetitiveBrands(String competitiveBrands) {
		this.competitiveBrands = competitiveBrands;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Object getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(status);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public Object getConflictStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(conflictStatus);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public Object getSaleStageLable() {
		LovMember lovMember = (LovMember) CacheData.getInstance().get(saleStage);
		if (lovMember != null) {
			return lovMember.getName();
		}
		return "";
	}

	public String getAgentId() {
		LovMember org = (LovMember) CacheData.getInstance().get(getCreatedOrgId());
		if (org != null) {
			if (org.getOptTxt4() != null) {
				return org.getOptTxt4();
			}
		}
		return null;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getIndustrySubCode() {
		return industrySubCode;
	}

	public void setIndustrySubCode(String industrySubCode) {
		this.industrySubCode = industrySubCode;
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

	public String getConflictAppeal() {
		return conflictAppeal;
	}

	public void setConflictAppeal(String conflictAppeal) {
		this.conflictAppeal = conflictAppeal;
	}

	public String getConflictAppealRemark() {
		return conflictAppealRemark;
	}

	public void setConflictAppealRemark(String conflictAppealRemark) {
		this.conflictAppealRemark = conflictAppealRemark;
	}
}
