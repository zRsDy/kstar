package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

@Entity
@Table(name = "crm_t_custom_credit_adjustment")
@Permission(businessType = "CUSTOM_ADJUST_PROC")
public class CustomCreditAdjustment implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @GeneratedValue(generator = "custom_credit_adjustment_generator")
	@GenericGenerator(name="custom_credit_adjustment_generator", strategy="uuid")
    @PermissionBusinessId
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    private String id;
    
    /** 申请编号 */
    @Column(name = "c_credit_adjustment_code", nullable = false, length = 32)
    private String creditAdjustmentCode;
    
    /** 客户编号 */
    @Column(name = "c_custom_code", nullable = false, length = 32)
    private String customCode;
    
    /** 客户名称 */
    @Column(name = "c_custom_full_name", nullable = false, length = 300)
    private String customName;
    
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
    @Column(name = "c_create_contact", nullable = false, length = 32)
    private String createContact;
    
    /** 状态 */
    @Column(name = "c_status", nullable = false, length = 32)
    private String status;
    
    @Transient
	private String statusName;
    
    public String getStatusName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(status));
		return lov==null? null : lov.getName();
	}
    
    public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
    
    /** 创建日期 */
    @Column(name = "c_create_date", nullable = true)
    private String createDate;
    
    /** 信用额度-常规-币种 */
    @Column(name = "c_limit_currency", nullable = true, length = 32)
    private String limitCurrency;
    
	@Transient
	private String limitCurrencyName;

	public String getLimitCurrencyName() {

		LovMember lov = (LovMember) CacheData.getInstance().get(limitCurrency);
		return lov == null ? null : lov.getName();
	}
    
    /** 信用额度-常规-额度 */
    @Column(name = "c_limit_amount", nullable = true, length = 32)
    private String limitAmount;
    
    /** 信用额度-常规-汇率 */
    @Column(name = "c_limit_rate", nullable = true, length = 32)
    private String limitRate;
    
    /** 信用额度-常规-业务实体 */
    @Column(name = "c_limit_unit", nullable = true, length = 32)
    private String limitUnit;
    
    /** 信用额度-常规-说明 */
    @Column(name = "c_limit_comment", nullable = true, length = 32)
    private String limitComment;
    
    /** 信用额度-常规-有效期至 */
    @Column(name = "c_limit_valid_to")
    private Date limitValidTo;
    
    /** 信用额度-临时-币种 */
    @Column(name = "c_temp_currency", nullable = true, length = 32)
    private String tempCurrency;
    
	@Transient
	private String tempCurrencyName;

	public String getTempCurrencyName() {

		LovMember lov = (LovMember) CacheData.getInstance().get(tempCurrency);
		return lov == null ? null : lov.getName();
	}
    
    /** 信用额度-临时-额度 */
    @Column(name = "c_temp_amount", nullable = true, length = 32)
    private String tempAmount;
    
    /** 信用额度-临时-汇率 */
    @Column(name = "c_temp_rate", nullable = true, length = 32)
    private String tempRate;
    
    /** 信用额度-临时-业务实体 */
    @Column(name = "c_temp_unit", nullable = true, length = 32)
    private String tempUnit;
    
    /** 信用额度-临时-说明 */
    @Column(name = "c_temp_comment", nullable = true, length = 32)
    private String tempComment;
    
    /** 信用额度-临时-有效期至 */
    @Column(name = "c_temp_valid_to")
    private Date tempValidTo;
    
    /** 客户类别 */
    @Column(name = "c_custom_class", nullable = true, length = 32)
    private String customClass;
    
    @Transient
	private String customClassName;
    
    public String getCustomClassName() {
		
		LovMember lov = (LovMember)CacheData.getInstance().get(customClass);
		return  lov==null? null:lov.getName();
	}
    
    public void setCustomClassName(String customClassName) {
   		this.customClassName = customClassName;
   	}
    
    
    /** 客户等级 */
    @Column(name = "c_custom_grade", nullable = true, length = 32)
    private String customGrade;
    
    @Transient
  	private String customGradeName;
      
    public String getCustomGradeName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customGrade));
  		return lov==null? null : lov.getName();
  	}
    
    /** 账期 */
    @Column(name = "c_corp_pay_days", nullable = true, length = 10)
    private String corpPayDays;
    
    /** 等级调整至 */
    @Column(name = "c_grade_changeto", nullable = true, length = 10)
    private String gradeChangeto;
    
    /** 账期调整至 */
    @Column(name = "c_pay_days_changeto", nullable = true, length = 10)
    private String payDaysChangeto;
    
    /** 渠道类别 */
    @Column(name = "c_pipe_class", nullable = true, length = 32)
    private String pipeClass;
    
    @Transient
  	private String pipeClassName;
      
    public String getPipeClassName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(pipeClass));
  		return lov==null? null : lov.getName();
  	}
    
    /** 渠道属性 */
    @Column(name = "c_pipe_attr", nullable = true, length = 100)
    private String pipeAttr;
    
    @Transient
  	private String pipeAttrName;
      
    public String getPipeAttrName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(pipeAttr));
  		return lov==null? null : lov.getName();
  	}
    
    /** 签约经销商等级 */
    @Column(name = "c_pipe_rank", nullable = true, length = 100)
    private String pipeRank;
    
    @Transient
  	private String pipeRankName;
      
    public String getPipeRankName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(pipeRank));
  		return lov==null? null : lov.getName();
  	}
    
    /** 去年营业额 */
    @Column(name = "c_year_before_turnover", nullable = true, length = 32)
    private String yearBeforeTurnover;
    
    /** 今年营业额 */
    @Column(name = "c_year_current_turnover", nullable = true, length = 32)
    private String yearCurrentTurnover;
    
    /** 原信用额度-常规-币种 */
    @Column(name = "c_limit_currency_old", nullable = true, length = 32)
    private String limitCurrencyOld;
    
    @Transient
	private String limitCurrencyOldName;
    
    public String getLimitCurrencyOldName() {
		
		LovMember lov = (LovMember)CacheData.getInstance().get(limitCurrencyOld);
		return  lov==null? null:lov.getName();
	}
    
    /** 原信用额度-常规-额度 */
    @Column(name = "c_limit_amount_old", nullable = true, length = 32)
    private String limitAmountOld;
    
    /** 原信用额度-常规-汇率 */
    @Column(name = "c_limit_rate_old", nullable = true, length = 32)
    private String limitRateOld;
    
    /** 原信用额度-临时-币种 */
    @Column(name = "c_temp_currency_old", nullable = true, length = 32)
    private String tempCurrencyOld;
    
    @Transient
	private String tempCurrencyOldName;
    
    public String getTempCurrencyOldName() {
		
		LovMember lov = (LovMember)CacheData.getInstance().get(tempCurrencyOld);
		return  lov==null? null:lov.getName();
	}
    
    /** 原信用额度-临时-额度 */
    @Column(name = "c_temp_amount_old", nullable = true, length = 32)
    private String tempAmountOld;
    
    /** 原信用额度-临时-汇率 */
    @Column(name = "c_temp_rate_old", nullable = true, length = 32)
    private String tempRateOld;
    
    /** 抵押物 */
    @Column(name = "c_pledge", nullable = true, length = 32)
    private String pledge;
    
    /** 备注 */
    @Column(name = "c_comment", nullable = true, length = 32)
    private String comment;
    
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

	public void setId(String id) {
		this.id = id;
	}

	public String getCreditAdjustmentCode() {
		return creditAdjustmentCode;
	}

	public void setCreditAdjustmentCode(String creditAdjustmentCode) {
		this.creditAdjustmentCode = creditAdjustmentCode;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierPos() {
		return applierPos;
	}

	public void setApplierPos(String applierPos) {
		this.applierPos = applierPos;
	}

	public String getApplierOrg() {
		return applierOrg;
	}

	public void setApplierOrg(String applierOrg) {
		this.applierOrg = applierOrg;
	}

	public String getCreateContact() {
		return createContact;
	}

	public void setCreateContact(String createContact) {
		this.createContact = createContact;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLimitCurrency() {
		return limitCurrency;
	}

	public void setLimitCurrency(String limitCurrency) {
		this.limitCurrency = limitCurrency;
	}

	public String getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(String limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getLimitRate() {
		return limitRate;
	}

	public void setLimitRate(String limitRate) {
		this.limitRate = limitRate;
	}

	public String getLimitUnit() {
		return limitUnit;
	}

	public void setLimitUnit(String limitUnit) {
		this.limitUnit = limitUnit;
	}

	public String getLimitComment() {
		return limitComment;
	}

	public void setLimitComment(String limitComment) {
		this.limitComment = limitComment;
	}

	public Date getLimitValidTo() {
		return limitValidTo;
	}

	public void setLimitValidTo(Date limitValidTo) {
		this.limitValidTo = limitValidTo;
	}

	public String getTempCurrency() {
		return tempCurrency;
	}

	public void setTempCurrency(String tempCurrency) {
		this.tempCurrency = tempCurrency;
	}

	public String getTempAmount() {
		return tempAmount;
	}

	public void setTempAmount(String tempAmount) {
		this.tempAmount = tempAmount;
	}

	public String getTempRate() {
		return tempRate;
	}

	public void setTempRate(String tempRate) {
		this.tempRate = tempRate;
	}

	public String getTempUnit() {
		return tempUnit;
	}

	public void setTempUnit(String tempUnit) {
		this.tempUnit = tempUnit;
	}

	public String getTempComment() {
		return tempComment;
	}

	public void setTempComment(String tempComment) {
		this.tempComment = tempComment;
	}

	public Date getTempValidTo() {
		return tempValidTo;
	}

	public void setTempValidTo(Date tempValidTo) {
		this.tempValidTo = tempValidTo;
	}

	public String getCustomClass() {
		return customClass;
	}

	public void setCustomClass(String customClass) {
		this.customClass = customClass;
	}

	public String getCustomGrade() {
		return customGrade;
	}

	public void setCustomGrade(String customGrade) {
		this.customGrade = customGrade;
	}

	public String getCorpPayDays() {
		return corpPayDays;
	}

	public void setCorpPayDays(String corpPayDays) {
		this.corpPayDays = corpPayDays;
	}

	public String getGradeChangeto() {
		return gradeChangeto;
	}

	public void setGradeChangeto(String gradeChangeto) {
		this.gradeChangeto = gradeChangeto;
	}

	public String getPayDaysChangeto() {
		return payDaysChangeto;
	}

	public void setPayDaysChangeto(String payDaysChangeto) {
		this.payDaysChangeto = payDaysChangeto;
	}

	public String getPipeClass() {
		return pipeClass;
	}

	public void setPipeClass(String pipeClass) {
		this.pipeClass = pipeClass;
	}

	public String getPipeAttr() {
		return pipeAttr;
	}

	public void setPipeAttr(String pipeAttr) {
		this.pipeAttr = pipeAttr;
	}

	public String getPipeRank() {
		return pipeRank;
	}

	public void setPipeRank(String pipeRank) {
		this.pipeRank = pipeRank;
	}

	public String getYearBeforeTurnover() {
		return yearBeforeTurnover;
	}

	public void setYearBeforeTurnover(String yearBeforeTurnover) {
		this.yearBeforeTurnover = yearBeforeTurnover;
	}

	public String getYearCurrentTurnover() {
		return yearCurrentTurnover;
	}

	public void setYearCurrentTurnover(String yearCurrentTurnover) {
		this.yearCurrentTurnover = yearCurrentTurnover;
	}

	public String getLimitCurrencyOld() {
		return limitCurrencyOld;
	}

	public void setLimitCurrencyOld(String limitCurrencyOld) {
		this.limitCurrencyOld = limitCurrencyOld;
	}

	public String getLimitAmountOld() {
		return limitAmountOld;
	}

	public void setLimitAmountOld(String limitAmountOld) {
		this.limitAmountOld = limitAmountOld;
	}

	public String getLimitRateOld() {
		return limitRateOld;
	}

	public void setLimitRateOld(String limitRateOld) {
		this.limitRateOld = limitRateOld;
	}

	public String getTempCurrencyOld() {
		return tempCurrencyOld;
	}

	public void setTempCurrencyOld(String tempCurrencyOld) {
		this.tempCurrencyOld = tempCurrencyOld;
	}

	public String getTempAmountOld() {
		return tempAmountOld;
	}

	public void setTempAmountOld(String tempAmountOld) {
		this.tempAmountOld = tempAmountOld;
	}

	public String getTempRateOld() {
		return tempRateOld;
	}

	public void setTempRateOld(String tempRateOld) {
		this.tempRateOld = tempRateOld;
	}

	public String getPledge() {
		return pledge;
	}

	public void setPledge(String pledge) {
		this.pledge = pledge;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public void setApplierOrgName(String applierOrgName) {
		this.applierOrgName = applierOrgName;
	}

	public void setLimitCurrencyName(String limitCurrencyName) {
		this.limitCurrencyName = limitCurrencyName;
	}

	public void setTempCurrencyName(String tempCurrencyName) {
		this.tempCurrencyName = tempCurrencyName;
	}

	public void setCustomGradeName(String customGradeName) {
		this.customGradeName = customGradeName;
	}

	public void setLimitCurrencyOldName(String limitCurrencyOldName) {
		this.limitCurrencyOldName = limitCurrencyOldName;
	}

	public void setTempCurrencyOldName(String tempCurrencyOldName) {
		this.tempCurrencyOldName = tempCurrencyOldName;
	}

	public void setPipeClassName(String pipeClassName) {
		this.pipeClassName = pipeClassName;
	}

	public void setPipeAttrName(String pipeAttrName) {
		this.pipeAttrName = pipeAttrName;
	}

	public void setPipeRankName(String pipeRankName) {
		this.pipeRankName = pipeRankName;
	}

}