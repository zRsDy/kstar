package com.ibm.kstar.entity.custom;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "crm_t_ho_custom_info")
public class HandoverCustomInfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @GeneratedValue(generator = "crm_t_ho_custom_info_generator")
	@GenericGenerator(name="crm_t_ho_custom_info_generator", strategy="uuid")
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    private String id;
    
    /** 交接单据ID */
    @Column(name = "C_BUSINESS_ID", nullable = true, length = 32)
    private String businessId;
    
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
    
    /** 交接单据状态 */
    @Column(name = "C_BUSINESS_STATUS", nullable = true, length = 32)
    private String businessStatus;
    
	@Transient
	private String businessStatusName;

	public String getBusinessStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(businessStatus));
		return lov == null ? null : lov.getName();
	}

	public void setBusinessStatusName(String businessStatusName) {
		this.businessStatusName = businessStatusName;
	}
    
    /** 交接单据原id */
    @Column(name = "C_BASE_ID", nullable = true, length = 32)
    private String baseId;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_code", nullable = true, length = 32)
    private String customCode;
    
    /** ERP主键 */
    @Column(name = "c_custom_erp_code")
    private String erpCode;
    
    /** ERP状态 */
    @Column(name = "c_custom_erp_status")
    private String erpStatus;
    
    @Transient
	private String erpStatusName;
    
    public String getErpStatusName() {
		
		LovMember lov = (LovMember)CacheData.getInstance().get(erpStatus);
		return  lov==null? null:lov.getName();
	}
    
    /** 客户全称 */
    @Column(name = "c_custom_full_name", nullable = true, length = 300)
    private String customFullName;
    
    /** 客户别名 */
    @Column(name = "c_custom_alias_name", nullable = true, length = 300)
    private String customAliasName;
    
    /** 公司网址 */
    @Column(name = "c_custom_web_address", nullable = true, length = 300)
    private String customWebAddress;
    
    /** 客户种类 0:内部客户 1:外部客户 */
    @Column(name = "c_custom_type", nullable = true, length = 1)
    private String customType;
    
    /** 客户类别 */
    @Column(name = "c_custom_class", nullable = true, length = 32)
    private String customClass;
    
    @Transient
	private String customClassName;
    
    public String getCustomClassName() {
		
		LovMember lov = (LovMember)CacheData.getInstance().get(customClass);
		return  lov==null? null:lov.getName();
	}
    
    
    
    @Transient
	private String customClassGrid;
    
    public Object getCustomClassGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(customClass);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setCustomClassGrid(String customClassGrid) {
		this.customClassGrid = customClassGrid;
	}
    
    /** 客户来源 */
    @Column(name = "c_custom_source", nullable = true, length = 32)
    private String customSource;
    
    @Transient
	private String customSourceName;
    
    public String getCustomSourceName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(customSource));
		return lov==null? null : lov.getName();
	}
    
    /** 客户状态 */
    @Column(name = "c_custom_status", nullable = true, length = 32)
    private String customStatus;
    
    @Transient
  	private String customStatusName;
      
    public String getCustomStatusName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customStatus));
  		return lov==null? null : lov.getName();
  	}
    
    /** OEM 品牌 */
    @Column(name = "c_custom_oem", nullable = true, length = 300)
    private String customOem;
    
    /** 所属区域 */
    @Column(name = "c_custom_area", nullable = true, length = 32)
    private String customArea;
    
    @Transient
  	private String customAreaName;
      
    public String getCustomAreaName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customArea));
  		if (lov==null ) {
  			return null;
  		}
  		
  		String[] names = lov.getNamePath().split("/");
  		StringBuffer name = new StringBuffer();
  		for (int i = 0; i < names.length; i++) {
			if (i== 0 || i==1 || i==2) {
				continue;
			}
			name.append("/").append(names[i]);
		}
  		return name.toString();
  	}
    
    /** 业务状态 */
    @Column(name = "c_custom_control_status", length = 32)
    private String customControlStatus;
    
    @Transient
  	private String customControlStatusName;
      
    public String getCustomControlStatusName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customControlStatus));
  		return lov==null? null : lov.getName();
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
    
    /** 收入规模 */
    @Column(name = "c_custom_income_scale", nullable = true, length = 32)
    private String customIncomeScale;
    
    @Transient
  	private String customIncomeScaleName;
      
    public String getCustomIncomeScaleName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customIncomeScale));
  		return lov==null? null : lov.getName();
  	}
    
    /** 已报备 */
    @Column(name = "c_custom_reported_flg", nullable = true, length = 32)
    private String customReportedFlg;
    
    @Transient
	private String customReportedFlgName;
    
    public String getCustomReportedFlgName() {
		
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(customReportedFlg));
		return lov==null? null : lov.getName();
	}
    
    public void setCustomReportedFlgName(String customReportedFlgName) {
		this.customReportedFlgName = customReportedFlgName;
	}
    
    /** 客户属性 */
    @Column(name = "c_custom_oroperty", nullable = true, length = 32)
    private String customOroperty;
    
    @Transient
  	private String customOropertyName;
      
    public String getCustomOropertyName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customOroperty));
  		return lov==null? null : lov.getName();
  	}
    
    /** 客户行业 */
    @Column(name = "c_custom_category", nullable = true, length = 32)
    private String customCategory;
    
    @Transient
  	private String customCategoryName;
      
    public String getCustomCategoryName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(customCategory));
  		return lov==null? null : lov.getNamePath();
  	}
    
    /** 行业备注 */
    @Column(name = "c_custom_category_comment", nullable = true, length = 300)
    private String customCategoryComment;
    
    /** 客户概况 */
    @Column(name = "c_custom_profile", nullable = true, length = 300)
    private String customProfile;
    
    /** 法定代表人 */
    @Column(name = "c_corp_representative", nullable = true, length = 100)
    private String corpRepresentative;
    
    /** 纳税登记号 */
    @Column(name = "c_corp_trn", nullable = true, length = 20)
    private String corpTrn;
    
    /** 默认付款方式 */
    @Column(name = "c_corp_payment_default", nullable = true, length = 32)
    private String corpPaymentDefault;
    
    @Transient
  	private String corpPaymentDefaultName;
      
    public String getCorpPaymentDefaultName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(corpPaymentDefault));
  		return lov==null? null : lov.getName();
  	}
    
    /** 注册地址 */
    @Column(name = "c_corp_reg_address", nullable = true, length = 300)
    private String corpRegAddress;
    
    /** 一般纳税人资格 */
    @Column(name = "c_corp_ordinary_flg", nullable = true, length = 32)
    private String corpOrdinaryFlg;
    
    @Transient
  	private String corpOrdinaryFlgName;
      
    public String getCorpOrdinaryFlgName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(corpOrdinaryFlg));
  		return lov==null? null : lov.getName();
  	}
    
    /** 默认付款条件 */
    @Column(name = "c_corp_term_default", nullable = true, length = 32)
    private String corpTermDefault;
    
    @Transient
  	private String corpTermDefaultName;
      
    public String getCorpTermDefaultName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(corpTermDefault));
  		return lov==null? null : lov.getName();
  	}
    
    /** 注册资本 */
    @Column(name = "c_corp_reg_captial", nullable = true, length = 20)
    private String corpRegCaptial;
    
    /** 账期 */
    @Column(name = "c_corp_pay_days", nullable = true, length = 10)
    private String corpPayDays;
    
    /** 中信保额度 */
    @Column(name = "c_corp_citic_amount", nullable = true, length = 20)
    private String corpCiticAmount;
    
    /** 特价异常 */
    @Column(name = "c_corp_onsale_price", nullable = true, length = 20)
    private String corpOnsalePrice;
    
    /** 对账单显示收款明细 */
    @Column(name = "c_corp_detail_flg", nullable = true, length = 32)
    private String corpDetailFlg;
    
    @Transient
  	private String corpDetailFlgName;
      
    public String getCorpDetailFlgName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(corpDetailFlg));
  		return lov==null? null : lov.getName();
  	}
    
    /** 人保额度 */
    @Column(name = "c_corp_picc_amount", nullable = true, length = 20)
    private String corpPiccAmount;
    
    /** 开票名称 */
    @Column(name = "c_corp_invoice_name", nullable = true, length = 100)
    private String corpInvoiceName;
    
    /** 开票注意事项 */
    @Column(name = "c_corp_invoice_comment", nullable = true, length = 100)
    private String corpInvoiceComment;
    
    /** 信用额度-常规-币种 */
    @Column(name = "c_limit_currency", nullable = true, length = 32)
    private String limitCurrency;
    
    @Transient
	private String limitCurrencyName;
    
    public String getLimitCurrencyName() {
		
		LovMember lov  = (LovMember)CacheData.getInstance().get(limitCurrency);
		
		return  lov == null ? null : lov.getName();
	}
    
    public void setLimitCurrencyName(String limitCurrencyName) {
		this.limitCurrencyName = limitCurrencyName;
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
    
    @Transient
  	private String limitUnitName;
      
    public String getLimitUnitName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(limitUnit));
  		return lov==null? null : lov.getName();
  	}
    
    /** 信用额度-常规-说明 */
    @Column(name = "c_limit_comment", nullable = true, length = 32)
    private String limitComment;
    
    /** 信用额度-常规-有效期至 */
    @Column(name = "c_limit_valid_to")
    private Date limitValidTo;
    
    /** 信用额度-临时-说明 */
    @Column(name = "c_temp_comment", nullable = true, length = 32)
    private String tempComment;
    
    /** 信用额度-临时-币种 */
    @Column(name = "c_temp_currency", nullable = true, length = 32)
    private String tempCurrency;
    
    @Transient
	private String tempCurrencyName;
    
    public String getTempCurrencyName() {
		
		LovMember lov  = (LovMember)CacheData.getInstance().get(tempCurrency);
		
		return  lov == null ? null : lov.getName();
	}
    
    public void setTempCurrencyName(String tempCurrencyName) {
		this.tempCurrencyName = tempCurrencyName;
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
    
    @Transient
  	private String tempUnitName;
      
    public String getTempUnitName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(tempUnit));
  		return lov==null? null : lov.getName();
  	}
    
    /** 信用额度-临时-有效期至 */
    @Column(name = "c_temp_valid_to", nullable = true)
    private Date tempValidTo;
    
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
    @Column(name = "c_pipe_attr", nullable = true, length = 32)
    private String pipeAttr;
    
    @Transient
  	private String pipeAttrName;
      
    public String getPipeAttrName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(pipeAttr));
  		return lov==null? null : lov.getName();
  	}
    
    /** 签约经销商等级 */
    @Column(name = "c_pipe_rank", nullable = true, length = 32)
    private String pipeRank;
    
    @Transient
  	private String pipeRankName;
      
    public String getPipeRankName() {
  		LovMember lov =  ((LovMember)CacheData.getInstance().get(pipeRank));
  		return lov==null? null : lov.getName();
  	}
    
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCustomFullName() {
		return customFullName;
	}

	public void setCustomFullName(String customFullName) {
		this.customFullName = customFullName;
	}

	public String getCustomAliasName() {
		return customAliasName;
	}

	public void setCustomAliasName(String customAliasName) {
		this.customAliasName = customAliasName;
	}

	public String getCustomWebAddress() {
		return customWebAddress;
	}

	public void setCustomWebAddress(String customWebAddress) {
		this.customWebAddress = customWebAddress;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public String getCustomClass() {
		return customClass;
	}

	public void setCustomClass(String customClass) {
		this.customClass = customClass;
	}

	public String getCustomSource() {
		return customSource;
	}

	public void setCustomSource(String customSource) {
		this.customSource = customSource;
	}

	public String getCustomStatus() {
		return customStatus;
	}

	public void setCustomStatus(String customStatus) {
		this.customStatus = customStatus;
	}

	public String getCustomOem() {
		return customOem;
	}

	public void setCustomOem(String customOem) {
		this.customOem = customOem;
	}

	public String getCustomArea() {
		return customArea;
	}

	public void setCustomArea(String customArea) {
		this.customArea = customArea;
	}

	public String getCustomControlStatus() {
		return customControlStatus;
	}

	public void setCustomControlStatus(String customControlStatus) {
		this.customControlStatus = customControlStatus;
	}

	public String getCustomGrade() {
		return customGrade;
	}

	public void setCustomGrade(String customGrade) {
		this.customGrade = customGrade;
	}

	public String getCustomIncomeScale() {
		return customIncomeScale;
	}

	public void setCustomIncomeScale(String customIncomeScale) {
		this.customIncomeScale = customIncomeScale;
	}

	public String getCustomReportedFlg() {
		return customReportedFlg;
	}

	public void setCustomReportedFlg(String customReportedFlg) {
		this.customReportedFlg = customReportedFlg;
	}

	public String getCustomOroperty() {
		return customOroperty;
	}

	public void setCustomOroperty(String customOroperty) {
		this.customOroperty = customOroperty;
	}

	public String getCustomCategory() {
		return customCategory;
	}

	public void setCustomCategory(String customCategory) {
		this.customCategory = customCategory;
	}

	public String getCustomCategoryComment() {
		return customCategoryComment;
	}

	public void setCustomCategoryComment(String customCategoryComment) {
		this.customCategoryComment = customCategoryComment;
	}

	public String getCustomProfile() {
		return customProfile;
	}

	public void setCustomProfile(String customProfile) {
		this.customProfile = customProfile;
	}

	public String getCorpRepresentative() {
		return corpRepresentative;
	}

	public void setCorpRepresentative(String corpRepresentative) {
		this.corpRepresentative = corpRepresentative;
	}

	public String getCorpTrn() {
		return corpTrn;
	}

	public void setCorpTrn(String corpTrn) {
		this.corpTrn = corpTrn;
	}

	public String getCorpPaymentDefault() {
		return corpPaymentDefault;
	}

	public void setCorpPaymentDefault(String corpPaymentDefault) {
		this.corpPaymentDefault = corpPaymentDefault;
	}

	public String getCorpRegAddress() {
		return corpRegAddress;
	}

	public void setCorpRegAddress(String corpRegAddress) {
		this.corpRegAddress = corpRegAddress;
	}

	public String getCorpOrdinaryFlg() {
		return corpOrdinaryFlg;
	}

	public void setCorpOrdinaryFlg(String corpOrdinaryFlg) {
		this.corpOrdinaryFlg = corpOrdinaryFlg;
	}

	public String getCorpTermDefault() {
		return corpTermDefault;
	}

	public void setCorpTermDefault(String corpTermDefault) {
		this.corpTermDefault = corpTermDefault;
	}

	public String getCorpRegCaptial() {
		return corpRegCaptial;
	}

	public void setCorpRegCaptial(String corpRegCaptial) {
		this.corpRegCaptial = corpRegCaptial;
	}

	public String getCorpPayDays() {
		return corpPayDays;
	}

	public void setCorpPayDays(String corpPayDays) {
		this.corpPayDays = corpPayDays;
	}

	public String getCorpCiticAmount() {
		return corpCiticAmount;
	}

	public void setCorpCiticAmount(String corpCiticAmount) {
		this.corpCiticAmount = corpCiticAmount;
	}

	public String getCorpOnsalePrice() {
		return corpOnsalePrice;
	}

	public void setCorpOnsalePrice(String corpOnsalePrice) {
		this.corpOnsalePrice = corpOnsalePrice;
	}

	public String getCorpDetailFlg() {
		return corpDetailFlg;
	}

	public void setCorpDetailFlg(String corpDetailFlg) {
		this.corpDetailFlg = corpDetailFlg;
	}

	public String getCorpPiccAmount() {
		return corpPiccAmount;
	}

	public void setCorpPiccAmount(String corpPiccAmount) {
		this.corpPiccAmount = corpPiccAmount;
	}

	public String getCorpInvoiceName() {
		return corpInvoiceName;
	}

	public void setCorpInvoiceName(String corpInvoiceName) {
		this.corpInvoiceName = corpInvoiceName;
	}

	public String getCorpInvoiceComment() {
		return corpInvoiceComment;
	}

	public void setCorpInvoiceComment(String corpInvoiceComment) {
		this.corpInvoiceComment = corpInvoiceComment;
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

	public String getTempComment() {
		return tempComment;
	}

	public void setTempComment(String tempComment) {
		this.tempComment = tempComment;
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

	public Date getTempValidTo() {
		return tempValidTo;
	}

	public void setTempValidTo(Date tempValidTo) {
		this.tempValidTo = tempValidTo;
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

	public void setCustomClassName(String customClassName) {
		this.customClassName = customClassName;
	}

	public void setCustomSourceName(String customSourceName) {
		this.customSourceName = customSourceName;
	}

	public void setCustomStatusName(String customStatusName) {
		this.customStatusName = customStatusName;
	}

	public void setCustomAreaName(String customAreaName) {
		this.customAreaName = customAreaName;
	}

	public void setCustomControlStatusName(String customControlStatusName) {
		this.customControlStatusName = customControlStatusName;
	}

	public void setCustomGradeName(String customGradeName) {
		this.customGradeName = customGradeName;
	}

	public void setCustomIncomeScaleName(String customIncomeScaleName) {
		this.customIncomeScaleName = customIncomeScaleName;
	}

	public void setCustomOropertyName(String customOropertyName) {
		this.customOropertyName = customOropertyName;
	}

	public void setCustomCategoryName(String customCategoryName) {
		this.customCategoryName = customCategoryName;
	}

	public void setCorpPaymentDefaultName(String corpPaymentDefaultName) {
		this.corpPaymentDefaultName = corpPaymentDefaultName;
	}

	public void setCorpOrdinaryFlgName(String corpOrdinaryFlgName) {
		this.corpOrdinaryFlgName = corpOrdinaryFlgName;
	}

	public void setCorpTermDefaultName(String corpTermDefaultName) {
		this.corpTermDefaultName = corpTermDefaultName;
	}

	public void setCorpDetailFlgName(String corpDetailFlgName) {
		this.corpDetailFlgName = corpDetailFlgName;
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

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public void setLimitUnitName(String limitUnitName) {
		this.limitUnitName = limitUnitName;
	}

	public void setTempUnitName(String tempUnitName) {
		this.tempUnitName = tempUnitName;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
	}

	public void setErpStatusName(String erpStatusName) {
		this.erpStatusName = erpStatusName;
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