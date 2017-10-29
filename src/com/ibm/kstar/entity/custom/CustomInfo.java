package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.log.KeyFiled;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

@Entity
@Table(name = "crm_t_custom_info")
@Permission(businessType = "CUSTOM_REPORT_PROC")
public class CustomInfo extends BaseEntity implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	@Id
	@GeneratedValue(generator = "crm_t_custom_info_generator")
	@GenericGenerator(name = "crm_t_custom_info_generator", strategy = "uuid")
	@Column(name = "c_pid")
	@PermissionBusinessId
	private String id;

	/** 客户信息主键 */
	@Column(name = "c_custom_code")
	private String customCode;

	/** CRM/PRM flag */
	@Column(name = "C_CUSTOM_PRM_FLG")
	private String prmFlg;

	/** ERP主键 */
	@Column(name = "c_custom_erp_code")
	@KeyFiled(comment = "客户ERP编码")
	private String erpCode;

	/** ERP状态 */
	@Column(name = "c_custom_erp_status")
	private String erpStatus;

	@Transient
	private String erpStatusName;

	
	public String getErpStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get(erpStatus);
		return lov == null ? null : lov.getName();
	}

	/** 客户全称 */
	@Column(name = "c_custom_full_name")
	private String customFullName;

	/** 客户别名 */
	@Column(name = "c_custom_alias_name")
	private String customAliasName;

	/** 公司网址 */
	@Column(name = "c_custom_web_address")
	private String customWebAddress;

	/** 客户种类 0:内部客户 1:外部客户 */
	@Column(name = "c_custom_type")
	private String customType;

	/** 客户类别 */
	@Column(name = "c_custom_class")
	private String customClass;

	@Transient
	private String customClassName;

	public String getCustomClassName() {
		LovMember lov = (LovMember) CacheData.getInstance().get(customClass);
		return lov == null ? null : lov.getName();
	}
	
	@Transient
	private String customClassGrid;

	public Object getCustomClassGrid() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(customClass);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public void setCustomClassGrid(String customClassGrid) {
		this.customClassGrid = customClassGrid;
	}

	/** 客户来源 */
	@Column(name = "c_custom_source")
	private String customSource;

	@Transient
	private String customSourceName;

	public String getCustomSourceName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customSource));
		return lov == null ? null : lov.getName();
	}

	/** 客户状态 */
	@Column(name = "c_custom_status")
	private String customStatus;

	@Transient
	private String customStatusName;

	public String getCustomStatusName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customStatus));
		return lov == null ? null : lov.getName();
	}

	/** OEM 品牌 */
	@Column(name = "c_custom_oem")
	private String customOem;

	/** 所属区域 国家 */
	@Column(name = "c_custom_area")
	private String customArea;

	@Transient
	private String customAreaName;

	public String getCustomAreaName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customArea));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 省 */
	@Column(name = "c_custom_area_sub1")
	private String customAreaSub1;

	@Transient
	private String customAreaSub1Name;

	public String getCustomAreaSub1Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customAreaSub1));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 市 */
	@Column(name = "c_custom_area_sub2")
	private String customAreaSub2;

	@Transient
	private String customAreaSub2Name;

	public String getCustomAreaSub2Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customAreaSub2));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 区县 */
	@Column(name = "c_custom_area_sub3")
	private String customAreaSub3;

	@Transient
	private String customAreaSub3Name;

	public String getCustomAreaSub3Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customAreaSub3));
		return lov == null ? null : lov.getName();
	}

	/** 业务状态 */
	@Column(name = "c_custom_control_status")
	private String customControlStatus;

	@Transient
	private String customControlStatusName;

	public String getCustomControlStatusName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customControlStatus));
		return lov == null ? null : lov.getName();
	}

	/** 客户等级 */
	@Column(name = "c_custom_grade")
	private String customGrade;

	@Transient
	private String customGradeName;

	public String getCustomGradeName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customGrade));
		return lov == null ? null : lov.getName();
	}

	/** 收入规模 */
	@Column(name = "c_custom_income_scale")
	private String customIncomeScale;

	@Transient
	private String customIncomeScaleName;

	public String getCustomIncomeScaleName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customIncomeScale));
		return lov == null ? null : lov.getName();
	}

	/** 已报备 */
	@Column(name = "c_custom_reported_flg")
	private String customReportedFlg;

	@Transient
	private String customReportedFlgName;

	public String getCustomReportedFlgName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(customReportedFlg));
		return lov == null ? null : lov.getName();
	}

	public void setCustomReportedFlgName(String customReportedFlgName) {
		this.customReportedFlgName = customReportedFlgName;
	}

	/** 客户属性 */
	@Column(name = "c_custom_oroperty")
	private String customOroperty;

	@Transient
	private String customOropertyName;

	public String getCustomOropertyName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customOroperty));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 大类 */
	@Column(name = "c_custom_category")
	private String customCategory;

	@Transient
	private String customCategoryName;

	public String getCustomCategoryName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customCategory));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 小类 */
	@Column(name = "c_custom_category_sub")
	private String customCategorySub;

	@Transient
	private String customCategorySubName;

	public String getCustomCategorySubName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customCategorySub));
		return lov == null ? null : lov.getName();
	}

	/** 行业备注 */
	@Column(name = "c_custom_category_comment")
	private String customCategoryComment;

	/** 客户概况 */
	@Column(name = "c_custom_profile")
	private String customProfile;

	/** 法定代表人 */
	@Column(name = "c_corp_representative")
	private String corpRepresentative;

	/** 纳税登记号 */
	@Column(name = "c_corp_trn")
	private String corpTrn;

	/** 默认付款方式 */
	@Column(name = "c_corp_payment_default")
	private String corpPaymentDefault;

	@Transient
	private String corpPaymentDefaultName;

	public String getCorpPaymentDefaultName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpPaymentDefault));
		return lov == null ? null : lov.getName();
	}

	/** 注册地址 */
	@Column(name = "c_corp_reg_address")
	private String corpRegAddress;

	/** 名称 */
	@Column(name = "c_contact_name")
	private String contactName;

	/** 部门 */
	@Column(name = "c_contact_dept")
	private String contactDept;

	/** 职务 */
	@Column(name = "c_contact_business")
	private String contactBusiness;

	/** 角色 */
	@Column(name = "c_contact_role")
	private String contactRole;

	@Transient
	private Object contactRoleGrid;

	public Object getContactRoleGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(contactRole);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public void setContactRoleGrid(Object contactRoleGrid) {
		this.contactRoleGrid = contactRoleGrid;
	}

	/** 电话 */
	@Column(name = "c_contact_tel")
	private String contactTel;

	/** 一般纳税人资格 */
	@Column(name = "c_corp_ordinary_flg")
	private String corpOrdinaryFlg;

	@Transient
	private String corpOrdinaryFlgName;

	public String getCorpOrdinaryFlgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpOrdinaryFlg));
		return lov == null ? null : lov.getName();
	}

	/** 默认付款条件 */
	@Column(name = "c_corp_term_default")
	private String corpTermDefault;

	@Transient
	private String corpTermDefaultName;

	public String getCorpTermDefaultName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpTermDefault));
		return lov == null ? null : lov.getName();
	}

	/** 注册资本 */
	@Column(name = "c_corp_reg_captial")
	private String corpRegCaptial;

	/** 账期 */
	@Column(name = "c_corp_pay_days")
	private String corpPayDays;

	/** 中信保额度 */
	@Column(name = "c_corp_citic_amount")
	private String corpCiticAmount;

	/** 特价异常 */
	@Column(name = "c_corp_onsale_price")
	private String corpOnsalePrice;

	/** 对账单显示收款明细 */
	@Column(name = "c_corp_detail_flg")
	private String corpDetailFlg;

	@Transient
	private String corpDetailFlgName;

	public String getCorpDetailFlgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpDetailFlg));
		return lov == null ? null : lov.getName();
	}

	/** 人保额度 */
	@Column(name = "c_corp_picc_amount")
	private String corpPiccAmount;

	/** 开票名称 */
	@Column(name = "c_corp_invoice_name")
	private String corpInvoiceName;
	
	/** 开票地址 */
	@Column(name = "c_account_address")
	private String accountAddress;

	/** 开户行 */
	@Column(name = "c_account_bank")
	private String accountBank;

	/** 开票电话 */
	@Column(name = "c_account_tel")
	private String accountTel;

	/** 开户账号 */
	@Column(name = "c_account_no")
	private String accountNo;

	/** 开票注意事项 */
	@Column(name = "c_corp_invoice_comment")
	private String corpInvoiceComment;

	/** 信用调整主键 */
	@Column(name = "c_credit_id")
	private String creditId;

	/** 信用额度-常规-币种 */
	@Column(name = "c_limit_currency")
	private String limitCurrency;

	@Transient
	private String limitCurrencyName;

	public String getLimitCurrencyName() {

		LovMember lov = (LovMember) CacheData.getInstance().get(limitCurrency);

		return lov == null ? null : lov.getName();
	}

	public void setLimitCurrencyName(String limitCurrencyName) {
		this.limitCurrencyName = limitCurrencyName;
	}

	/** 信用额度-常规-额度 */
	@Column(name = "c_limit_amount")
	private String limitAmount;

	/** 信用额度-常规-汇率 */
	@Column(name = "c_limit_rate")
	private String limitRate;

	/** 信用额度-常规-业务实体 */
	@Column(name = "c_limit_unit")
	private String limitUnit;

	@Transient
	private String limitUnitName;

	public String getLimitUnitName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(limitUnit));
		return lov == null ? null : lov.getName();
	}

	/** 信用额度-常规-说明 */
	@Column(name = "c_limit_comment")
	private String limitComment;

	/** 信用额度-常规-有效期至 */
	@Column(name = "c_limit_valid_to")
	private Date limitValidTo;

	/** 信用额度-临时-说明 */
	@Column(name = "c_temp_comment")
	private String tempComment;

	/** 信用额度-临时-币种 */
	@Column(name = "c_temp_currency")
	private String tempCurrency;

	@Transient
	private String tempCurrencyName;

	public String getTempCurrencyName() {

		LovMember lov = (LovMember) CacheData.getInstance().get(tempCurrency);

		return lov == null ? null : lov.getName();
	}

	public void setTempCurrencyName(String tempCurrencyName) {
		this.tempCurrencyName = tempCurrencyName;
	}

	/** 信用额度-临时-额度 */
	@Column(name = "c_temp_amount")
	private String tempAmount;

	/** 信用额度-临时-汇率 */
	@Column(name = "c_temp_rate")
	private String tempRate;

	/** 信用额度-临时-业务实体 */
	@Column(name = "c_temp_unit")
	private String tempUnit;

	@Transient
	private String tempUnitName;

	public String getTempUnitName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempUnit));
		return lov == null ? null : lov.getName();
	}

	/** 信用额度-临时-有效期至 */
	@Column(name = "c_temp_valid_to", nullable = true)
	private Date tempValidTo;

	/** 渠道类别 */
	@Column(name = "c_pipe_class")
	private String pipeClass;

	@Transient
	private String pipeClassName;

	public String getPipeClassName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(pipeClass));
		return lov == null ? null : lov.getName();
	}

	/** 渠道属性 */
	@Column(name = "c_pipe_attr")
	private String pipeAttr;

	@Transient
	private String pipeAttrName;

	public String getPipeAttrName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(pipeAttr));
		return lov == null ? null : lov.getName();
	}

	/** 签约经销商等级 */
	@Column(name = "c_pipe_rank")
	private String pipeRank;

	@Transient
	private String pipeRankName;

	public String getPipeRankName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(pipeRank));
		return lov == null ? null : lov.getName();
	}

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

	public String getCustomCategorySub() {
		return customCategorySub;
	}

	public void setCustomCategorySub(String customCategorySub) {
		this.customCategorySub = customCategorySub;
	}

	public void setCustomCategorySubName(String customCategorySubName) {
		this.customCategorySubName = customCategorySubName;
	}

	public String getCustomAreaSub1() {
		return customAreaSub1;
	}

	public void setCustomAreaSub1(String customAreaSub1) {
		this.customAreaSub1 = customAreaSub1;
	}

	public String getCustomAreaSub2() {
		return customAreaSub2;
	}

	public void setCustomAreaSub2(String customAreaSub2) {
		this.customAreaSub2 = customAreaSub2;
	}

	public String getCustomAreaSub3() {
		return customAreaSub3;
	}

	public void setCustomAreaSub3(String customAreaSub3) {
		this.customAreaSub3 = customAreaSub3;
	}

	public void setCustomAreaSub1Name(String customAreaSub1Name) {
		this.customAreaSub1Name = customAreaSub1Name;
	}

	public void setCustomAreaSub2Name(String customAreaSub2Name) {
		this.customAreaSub2Name = customAreaSub2Name;
	}

	public void setCustomAreaSub3Name(String customAreaSub3Name) {
		this.customAreaSub3Name = customAreaSub3Name;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public String getPrmFlg() {
		return prmFlg;
	}

	public void setPrmFlg(String prmFlg) {
		this.prmFlg = prmFlg;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactDept() {
		return contactDept;
	}

	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}

	public String getContactBusiness() {
		return contactBusiness;
	}

	public void setContactBusiness(String contactBusiness) {
		this.contactBusiness = contactBusiness;
	}

	public String getContactRole() {
		return contactRole;
	}

	public void setContactRole(String contactRole) {
		this.contactRole = contactRole;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getAccountAddress() {
		return accountAddress;
	}

	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getAccountTel() {
		return accountTel;
	}

	public void setAccountTel(String accountTel) {
		this.accountTel = accountTel;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
}