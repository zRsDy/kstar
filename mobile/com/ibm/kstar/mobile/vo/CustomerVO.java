package com.ibm.kstar.mobile.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 客户视图
 * @author jian.xu
 *
 */
public class CustomerVO implements Serializable {

	private static final long serialVersionUID = -509300446686115188L;
	private List<?> erps;
	
	public CustomerVO(){}

	private String id;

	/** 客户信息主键 */
	private String customCode;

	/** CRM/PRM flag */
	private String prmFlg;

	/** ERP主键 */
	private String erpCode;

	/** ERP状态 */
	private String erpStatus;

	public String getErpStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get(erpStatus);
		return lov == null ? null : lov.getName();
	}

	/** 客户全称 */
	private String customFullName;

	/** 客户别名 */
	private String customAliasName;

	/** 公司网址 */
	private String customWebAddress;

	/** 客户种类 0:内部客户 1:外部客户 */
	private String customType;

	/** 客户类别 */
	private String customClass;

	public String getCustomClassName() {
		LovMember lov = (LovMember) CacheData.getInstance().get(customClass);
		return lov == null ? null : lov.getName();
	}

	public Object getCustomClassGrid() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(customClass);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	/** 客户来源 */
	private String customSource;

	public String getCustomSourceName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customSource));
		return lov == null ? null : lov.getName();
	}

	/** 客户状态 */
	private String customStatus;

	public String getCustomStatusName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customStatus));
		return lov == null ? null : lov.getName();
	}

	/** OEM 品牌 */
	private String customOem;

	/** 所属区域 国家 */
	private String customArea;

	public String getCustomAreaName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customArea));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 省 */
	private String customAreaSub1;

	public String getCustomAreaSub1Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customAreaSub1));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 市 */
	private String customAreaSub2;

	public String getCustomAreaSub2Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customAreaSub2));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 区县 */
	private String customAreaSub3;

	public String getCustomAreaSub3Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customAreaSub3));
		return lov == null ? null : lov.getName();
	}

	/** 业务状态 */
	private String customControlStatus;

	public String getCustomControlStatusName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customControlStatus));
		return lov == null ? null : lov.getName();
	}

	/** 客户等级 */
	private String customGrade;

	public String getCustomGradeName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customGrade));
		return lov == null ? null : lov.getName();
	}

	/** 收入规模 */
	private String customIncomeScale;

	public String getCustomIncomeScaleName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customIncomeScale));
		return lov == null ? null : lov.getName();
	}

	/** 已报备 */
	private String customReportedFlg;

	public String getCustomReportedFlgName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(customReportedFlg));
		return lov == null ? null : lov.getName();
	}

	/** 客户属性 */
	private String customOroperty;

	public String getCustomOropertyName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customOroperty));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 大类 */
	private String customCategory;

	public String getCustomCategoryName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customCategory));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 小类 */
	private String customCategorySub;

	public String getCustomCategorySubName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customCategorySub));
		return lov == null ? null : lov.getName();
	}

	/** 行业备注 */
	private String customCategoryComment;

	/** 客户概况 */
	private String customProfile;

	/** 法定代表人 */
	private String corpRepresentative;

	/** 纳税登记号 */
	private String corpTrn;

	/** 默认付款方式 */
	private String corpPaymentDefault;

	public String getCorpPaymentDefaultName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpPaymentDefault));
		return lov == null ? null : lov.getName();
	}

	/** 注册地址 */
	private String corpRegAddress;

	/** 名称 */
	private String contactName;

	/** 部门 */
	private String contactDept;

	/** 职务 */
	private String contactBusiness;

	/** 角色 */
	private String contactRole;


	public Object getContactRoleGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(contactRole);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	/** 电话 */
	private String contactTel;

	/** 一般纳税人资格 */
	private String corpOrdinaryFlg;

	public String getCorpOrdinaryFlgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpOrdinaryFlg));
		return lov == null ? null : lov.getName();
	}

	/** 默认付款条件 */
	private String corpTermDefault;

	public String getCorpTermDefaultName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpTermDefault));
		return lov == null ? null : lov.getName();
	}

	/** 注册资本 */
	private String corpRegCaptial;

	/** 账期 */
	private String corpPayDays;

	/** 中信保额度 */
	private String corpCiticAmount;

	/** 特价异常 */
	private String corpOnsalePrice;

	/** 对账单显示收款明细 */
	private String corpDetailFlg;

	public String getCorpDetailFlgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpDetailFlg));
		return lov == null ? null : lov.getName();
	}

	/** 人保额度 */
	private String corpPiccAmount;

	/** 开票名称 */
	private String corpInvoiceName;
	
	/** 开票地址 */
	private String accountAddress;

	/** 开户行 */
	private String accountBank;

	/** 开票电话 */
	private String accountTel;

	/** 开户账号 */
	private String accountNo;

	/** 开票注意事项 */
	private String corpInvoiceComment;

	/** 信用调整主键 */
	private String creditId;

	/** 信用额度-常规-币种 */
	private String limitCurrency;

	public String getLimitCurrencyName() {

		LovMember lov = (LovMember) CacheData.getInstance().get(limitCurrency);

		return lov == null ? null : lov.getName();
	}

	/** 信用额度-常规-额度 */
	private String limitAmount;

	/** 信用额度-常规-汇率 */
	private String limitRate;

	/** 信用额度-常规-业务实体 */
	private String limitUnit;

	public String getLimitUnitName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(limitUnit));
		return lov == null ? null : lov.getName();
	}

	/** 信用额度-常规-说明 */
	private String limitComment;

	/** 信用额度-常规-有效期至 */
	private Date limitValidTo;

	/** 信用额度-临时-说明 */
	private String tempComment;

	/** 信用额度-临时-币种 */
	private String tempCurrency;

	public String getTempCurrencyName() {
		LovMember lov = (LovMember) CacheData.getInstance().get(tempCurrency);
		return lov == null ? null : lov.getName();
	}

	/** 信用额度-临时-额度 */
	private String tempAmount;

	/** 信用额度-临时-汇率 */
	private String tempRate;

	/** 信用额度-临时-业务实体 */
	private String tempUnit;

	public String getTempUnitName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempUnit));
		return lov == null ? null : lov.getName();
	}

	/** 信用额度-临时-有效期至 */
	private Date tempValidTo;

	/** 渠道类别 */
	private String pipeClass;

	public String getPipeClassName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(pipeClass));
		return lov == null ? null : lov.getName();
	}

	/** 渠道属性 */
	@Column(name = "c_pipe_attr")
	private String pipeAttr;

	public String getPipeAttrName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(pipeAttr));
		return lov == null ? null : lov.getName();
	}

	/** 签约经销商等级 */
	private String pipeRank;

	public String getPipeRankName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(pipeRank));
		return lov == null ? null : lov.getName();
	}

	@SuppressWarnings("rawtypes")
	public List<?> getErps() {
		if(erps==null)
			erps = new ArrayList();
		return erps;
	}

	public void setErps(List<?> erps) {
		this.erps = erps;
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

	public String getPrmFlg() {
		return prmFlg;
	}

	public void setPrmFlg(String prmFlg) {
		this.prmFlg = prmFlg;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public String getErpStatus() {
		return erpStatus;
	}

	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
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

	public String getCustomCategorySub() {
		return customCategorySub;
	}

	public void setCustomCategorySub(String customCategorySub) {
		this.customCategorySub = customCategorySub;
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

	public String getCorpInvoiceComment() {
		return corpInvoiceComment;
	}

	public void setCorpInvoiceComment(String corpInvoiceComment) {
		this.corpInvoiceComment = corpInvoiceComment;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
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
	
	
}
