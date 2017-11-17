/**
 * 
 */
package com.ibm.kstar.entity.custom;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.*;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "crm_t_ho_business_opportunity")
public class HandoverBusinessOpportunity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** primary key */
	@Id
	@GeneratedValue(generator = "crm_t_ho_business_opportunity_id_generator")
	@GenericGenerator(name="crm_t_ho_business_opportunity_id_generator", strategy="uuid")
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	private String id;
	
	/** 交接单据ID */
    @Column(name = "C_BUSINESS_ID", nullable = true, length = 32)
    private String businessId;
    
    @Transient
	private String businessStatusName;

	public String getBusinessStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(businessStatus));
		return lov == null ? null : lov.getName();
	}

	public void setBusinessStatusName(String businessStatusName) {
		this.businessStatusName = businessStatusName;
	}
    
    /** 交接单据状态 */
    @Column(name = "C_BUSINESS_STATUS", nullable = true, length = 32)
    private String businessStatus;
    
    /** 交接单据状态 */
    @Column(name = "C_CHECK_STATUS", nullable = true, length = 32)
    private String checkStatus;
    
    @Transient
	private String checkStatusName;
    
    public String getCheckStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(checkStatus));
		
		return lov == null ? null : lov.getName();
	}

	public void setCheckStatusName(String checkStatusName) {
		this.checkStatusName = checkStatusName;
	}
    
    /** 交接单据原id */
    @Column(name = "C_BASE_ID", nullable = true, length = 32)
    private String baseId;
    
    /** 销售阶段 */
    @Column(name = "c_sale_stage", nullable = true, length = 32)
    private String saleStage;
    
    /** 业务流程 */
    @Column(name = "c_business_process", nullable = true, length = 32)
    private String businessProcess;
    
    /** 报备单位 */
    @Column(name = "c_enterprise", nullable = true, length = 32)
    private String enterprise;
    
    /** 商机编号 */
    @Column(name = "c_number", nullable = true, length = 32)
    private String number;
    
    /** 状态 */
    @Column(name = "c_status", nullable = true, length = 32)
    private String status;

	@Transient
	private String statusName;

	public String getStatusName() {
		LovMember lov = (LovMember)CacheData.getInstance().get("OPPORTUNITY_STATUS_" + status);
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
    
    /** 报备人 */
    @PermissionUserId
    @Column(name = "c_creater", nullable = true, length = 32)
    private String creater;

    @Transient
	private String createrName;

	public String getCreaterName() {
		Employee e = (Employee)CacheData.getInstance().get(creater);
		if(e != null) {
			return e.getName();
		}
		return "";
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	/** 商机状态 */
    @Column(name = "c_conflict_status", nullable = true, length = 32)
    private String conflictStatus;

	@Transient
	private String conflictStatusName;

	public String getConflictStatusName() {
		LovMember lov = (LovMember)CacheData.getInstance().get("CONFLICT_STATUS_" + conflictStatus);
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
    
    /** 创建日期 */
    @Column(name = "dt_create_time", nullable = true, length = 11)
    private Date createTime;
    
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
    
    /** 更新时间 */
    @Column(name = "dt_update_time", nullable = true, length = 11)
    private Date updateTime;
    
    /** 竞争品牌 */
    @Column(name = "c_competitive_brands", nullable = true, length = 32)
    private String competitiveBrands;
    
    /** 立项类型 */
    @Column(name = "c_project_type", nullable = true, length = 30)
    private String projectType;

	@Column(name = "C_QOUT_NAME") 	//报价单名称
	private String qoutName;
	@Column(name = "C_IS_BID_PROJECT") //是否投标类型
	private String isBidProject;
	@Column(name = "C_IS_PRO_REVIEW") 	//是否工程评审
	private String isProReview;
	@Column(name = "C_CONTRNAME") 	//合同名称
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

	@Column(name = "UNION_CONTACT")	//	业务联系人
	private String unionContact;

	@Column(name = "UNION_POSITION")	//	职务
	private String unionPosition;

	@Column(name = "UNION_NAME")	//	联合单位名称
	private String unionName;

	@Column(name = "UNION_ADDRESS")	//	地址
	private String unionAddress;
	@Column(name = "UNION_PHONE")		//	电话
	private String unionPhone;
	@Column(name = "UNION_LEGAL")		//	法人代表
	private String unionLegal;
	@Column(name = "UNION_EMAIL")		//	email
	private String unionEmail;

	@Column(name = "sbu_flag")
	private String sbu_flag;
	
	 /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

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

	public String getCreatedPosId() {
		return createdPosId;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
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

//    @Transient
//	private String statusLable;
//    
//    @Transient
//    private String conflictStatusLable;
//    
//    @Transient
//    private String saleStageLable;

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

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public Object getConflictStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(conflictStatus);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public Object getSaleStageLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(saleStage);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
}
