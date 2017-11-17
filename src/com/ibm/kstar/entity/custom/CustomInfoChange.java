package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "crm_t_custom_info_update")
public class CustomInfoChange implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	@Id
	@GeneratedValue(generator = "crm_t_custom_info_update_generator")
	@GenericGenerator(name = "crm_t_custom_info_update_generator", strategy = "uuid")
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	private String id;

	/** 客户ID */
	@Column(name = "c_custom_id", nullable = false, length = 32)
	private String customId;

	/** 地址ID */
	@Column(name = "c_address_id")
	private String addressId;

	/** 申请人 */
	@Column(name = "APPLIER_ID", nullable = true, length = 32)
	private String applier;

	@Transient
	private String applierName;

	public String getApplierName() {
		Employee lov = ((Employee) CacheData.getInstance().get(applier));
		return lov == null ? null : lov.getName().concat("/")
				.concat(lov.getNo());
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
		LovMember lov = ((LovMember) CacheData.getInstance().get(applierOrg));
		return lov == null ? null : lov.getNamePath();
	}

	/**  */
	@Column(name = "c_create_date", nullable = true, length = 32)
	private String createDate;

	/**  */
	@Column(name = "c_status", nullable = true, length = 32)
	private String status;

	@Transient
	private String statusName;

	public String getStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get(status);
		return lov == null ? null : lov.getName();
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

	/** OEM 品牌 */
	@Column(name = "c_custom_oem", nullable = true, length = 300)
	private String customOem;

	/** 所属区域 国家 */
	@Column(name = "c_custom_area", nullable = true, length = 32)
	private String customArea;

	@Transient
	private String customAreaName;

	public String getCustomAreaName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(customArea));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 省 */
	@Column(name = "c_custom_area_sub1", nullable = true, length = 32)
	private String customAreaSub1;

	@Transient
	private String customAreaSub1Name;

	public String getCustomAreaSub1Name() {
		LovMember lov = ((LovMember) CacheData.getInstance()
				.get(customAreaSub1));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 市 */
	@Column(name = "c_custom_area_sub2", nullable = true, length = 32)
	private String customAreaSub2;

	@Transient
	private String customAreaSub2Name;

	public String getCustomAreaSub2Name() {
		LovMember lov = ((LovMember) CacheData.getInstance()
				.get(customAreaSub2));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 区县 */
	@Column(name = "c_custom_area_sub3", nullable = true, length = 32)
	private String customAreaSub3;

	@Transient
	private String customAreaSub3Name;

	public String getCustomAreaSub3Name() {
		LovMember lov = ((LovMember) CacheData.getInstance()
				.get(customAreaSub3));
		return lov == null ? null : lov.getName();
	}

	/** 收入规模 */
	@Column(name = "c_custom_income_scale", nullable = true, length = 32)
	private String customIncomeScale;

	@Transient
	private String customIncomeScaleName;

	public String getCustomIncomeScaleName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(
				customIncomeScale));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 大类 */
	@Column(name = "c_custom_category", nullable = true, length = 32)
	private String customCategory;

	@Transient
	private String customCategoryName;

	public String getCustomCategoryName() {
		LovMember lov = ((LovMember) CacheData.getInstance()
				.get(customCategory));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 小类 */
	@Column(name = "c_custom_category_sub", nullable = true, length = 32)
	private String customCategorySub;

	@Transient
	private String customCategorySubName;

	public String getCustomCategorySubName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(
				customCategorySub));
		return lov == null ? null : lov.getName();
	}

	/** 客户概况 */
	@Column(name = "c_custom_profile", nullable = true, length = 300)
	private String customProfile;

	/** 法定代表人 */
	@Column(name = "c_corp_representative", nullable = true, length = 100)
	private String corpRepresentative;

	/** 纳税登记号 */
	@Column(name = "c_corp_trn", nullable = true, length = 20)
	private String corpTrn;

	/** 开票名称 */
	@Column(name = "c_corp_invoice_name", nullable = true, length = 100)
	private String corpInvoiceName;
	
	/** 一般纳税人资格 */
	@Column(name = "c_corp_ordinary_flg")
	private String corpOrdinaryFlg;

	@Transient
	private String corpOrdinaryFlgName;

	public String getCorpOrdinaryFlgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(corpOrdinaryFlg));
		return lov == null ? null : lov.getName();
	}
	
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
	
	/** 一般纳税人资格 */
	@Column(name = "c_temp_ordinary_flg")
	private String tempOrdinaryFlg;

	@Transient
	private String tempOrdinaryFlgName;

	public String getTempOrdinaryFlgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempOrdinaryFlg));
		return lov == null ? null : lov.getName();
	}
	
	/** 开票地址 */
	@Column(name = "c_temp_account_address")
	private String tempAccountAddress;

	/** 开户行 */
	@Column(name = "c_temp_account_bank")
	private String tempAccountBank;

	/** 开票电话 */
	@Column(name = "c_temp_account_tel")
	private String tempAccountTel;

	/** 开户账号 */
	@Column(name = "c_temp_account_no")
	private String tempAccountNo;

	/** 开票注意事项 */
	@Column(name = "c_corp_invoice_comment", nullable = true, length = 100)
	private String corpInvoiceComment;

	/** 注册地址 */
	@Column(name = "c_reg_address", nullable = true, length = 300)
	private String corpRegAddress;

	/** 客户全称 */
	@Column(name = "c_temp_full_name", nullable = true, length = 300)
	private String tempFullName;

	/** 客户别名 */
	@Column(name = "c_temp_alias_name", nullable = true, length = 300)
	private String tempAliasName;

	/** 公司网址 */
	@Column(name = "c_temp_web_address", nullable = true, length = 300)
	private String tempWebAddress;

	/** OEM 品牌 */
	@Column(name = "c_temp_oem", nullable = true, length = 300)
	private String tempOem;

	/** 所属区域 国家 */
	@Column(name = "c_temp_area", nullable = true, length = 32)
	private String tempArea;

	@Transient
	private String tempAreaName;

	public String getTempAreaName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempArea));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 省 */
	@Column(name = "c_temp_area_sub1", nullable = true, length = 32)
	private String tempAreaSub1;

	@Transient
	private String tempAreaSub1Name;

	public String getTempAreaSub1Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempAreaSub1));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 市 */
	@Column(name = "c_temp_area_sub2", nullable = true, length = 32)
	private String tempAreaSub2;

	@Transient
	private String tempAreaSub2Name;

	public String getTempAreaSub2Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempAreaSub2));
		return lov == null ? null : lov.getName();
	}

	/** 所属区域 区县 */
	@Column(name = "c_temp_area_sub3", nullable = true, length = 32)
	private String tempAreaSub3;

	@Transient
	private String tempAreaSub3Name;

	public String getTempAreaSub3Name() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempAreaSub3));
		return lov == null ? null : lov.getName();
	}

	/** 收入规模 */
	@Column(name = "c_temp_income_scale", nullable = true, length = 32)
	private String tempIncomeScale;

	/** 客户行业 */
	@Column(name = "c_temp_category", nullable = true, length = 32)
	private String tempCategory;

	@Transient
	private String tempCategoryName;

	public String getTempCategoryName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(tempCategory));
		return lov == null ? null : lov.getName();
	}

	/** 客户行业 小类 */
	@Column(name = "c_temp_category_sub", nullable = true, length = 32)
	private String tempCategorySub;

	@Transient
	private String tempCategorySubName;

	public String getTempCategorySubName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(
				tempCategorySub));
		return lov == null ? null : lov.getName();
	}

	/** 客户概况 */
	@Column(name = "c_temp_profile", nullable = true, length = 300)
	private String tempProfile;

	/** 法定代表人 */
	@Column(name = "c_temp_representative", nullable = true, length = 100)
	private String tempRepresentative;

	/** 纳税登记号 */
	@Column(name = "c_temp_trn", nullable = true, length = 20)
	private String tempTrn;

	/** 开票名称 */
	@Column(name = "c_temp_invoice_name", nullable = true, length = 100)
	private String tempInvoiceName;

	/** 开票注意事项 */
	@Column(name = "c_temp_invoice_comment", nullable = true, length = 100)
	private String tempInvoiceComment;

	/** 注册地址 */
	@Column(name = "c_temp_reg_address", nullable = true, length = 300)
	private String tempRegAddress;

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

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCustomIncomeScale() {
		return customIncomeScale;
	}

	public void setCustomIncomeScale(String customIncomeScale) {
		this.customIncomeScale = customIncomeScale;
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

	public String getCorpRegAddress() {
		return corpRegAddress;
	}

	public void setCorpRegAddress(String corpRegAddress) {
		this.corpRegAddress = corpRegAddress;
	}

	public String getTempFullName() {
		return tempFullName;
	}

	public void setTempFullName(String tempFullName) {
		this.tempFullName = tempFullName;
	}

	public String getTempAliasName() {
		return tempAliasName;
	}

	public void setTempAliasName(String tempAliasName) {
		this.tempAliasName = tempAliasName;
	}

	public String getTempWebAddress() {
		return tempWebAddress;
	}

	public void setTempWebAddress(String tempWebAddress) {
		this.tempWebAddress = tempWebAddress;
	}

	public String getTempOem() {
		return tempOem;
	}

	public void setTempOem(String tempOem) {
		this.tempOem = tempOem;
	}

	public String getTempArea() {
		return tempArea;
	}

	public void setTempArea(String tempArea) {
		this.tempArea = tempArea;
	}

	public String getTempAreaSub1() {
		return tempAreaSub1;
	}

	public void setTempAreaSub1(String tempAreaSub1) {
		this.tempAreaSub1 = tempAreaSub1;
	}

	public String getTempAreaSub2() {
		return tempAreaSub2;
	}

	public void setTempAreaSub2(String tempAreaSub2) {
		this.tempAreaSub2 = tempAreaSub2;
	}

	public String getTempAreaSub3() {
		return tempAreaSub3;
	}

	public void setTempAreaSub3(String tempAreaSub3) {
		this.tempAreaSub3 = tempAreaSub3;
	}

	public String getTempIncomeScale() {
		return tempIncomeScale;
	}

	public void setTempIncomeScale(String tempIncomeScale) {
		this.tempIncomeScale = tempIncomeScale;
	}

	public String getTempCategory() {
		return tempCategory;
	}

	public void setTempCategory(String tempCategory) {
		this.tempCategory = tempCategory;
	}

	public String getTempCategorySub() {
		return tempCategorySub;
	}

	public void setTempCategorySub(String tempCategorySub) {
		this.tempCategorySub = tempCategorySub;
	}

	public String getTempProfile() {
		return tempProfile;
	}

	public void setTempProfile(String tempProfile) {
		this.tempProfile = tempProfile;
	}

	public String getTempRepresentative() {
		return tempRepresentative;
	}

	public void setTempRepresentative(String tempRepresentative) {
		this.tempRepresentative = tempRepresentative;
	}

	public String getTempTrn() {
		return tempTrn;
	}

	public void setTempTrn(String tempTrn) {
		this.tempTrn = tempTrn;
	}

	public String getTempInvoiceName() {
		return tempInvoiceName;
	}

	public void setTempInvoiceName(String tempInvoiceName) {
		this.tempInvoiceName = tempInvoiceName;
	}

	public String getTempInvoiceComment() {
		return tempInvoiceComment;
	}

	public void setTempInvoiceComment(String tempInvoiceComment) {
		this.tempInvoiceComment = tempInvoiceComment;
	}

	public String getTempRegAddress() {
		return tempRegAddress;
	}

	public void setTempRegAddress(String tempRegAddress) {
		this.tempRegAddress = tempRegAddress;
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

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setCustomAreaName(String customAreaName) {
		this.customAreaName = customAreaName;
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

	public void setCustomIncomeScaleName(String customIncomeScaleName) {
		this.customIncomeScaleName = customIncomeScaleName;
	}

	public void setCustomCategoryName(String customCategoryName) {
		this.customCategoryName = customCategoryName;
	}

	public void setCustomCategorySubName(String customCategorySubName) {
		this.customCategorySubName = customCategorySubName;
	}

	public void setTempAreaName(String tempAreaName) {
		this.tempAreaName = tempAreaName;
	}

	public void setTempAreaSub1Name(String tempAreaSub1Name) {
		this.tempAreaSub1Name = tempAreaSub1Name;
	}

	public void setTempAreaSub2Name(String tempAreaSub2Name) {
		this.tempAreaSub2Name = tempAreaSub2Name;
	}

	public void setTempAreaSub3Name(String tempAreaSub3Name) {
		this.tempAreaSub3Name = tempAreaSub3Name;
	}

	public void setTempCategoryName(String tempCategoryName) {
		this.tempCategoryName = tempCategoryName;
	}

	public void setTempCategorySubName(String tempCategorySubName) {
		this.tempCategorySubName = tempCategorySubName;
	}

	public String getCorpOrdinaryFlg() {
		return corpOrdinaryFlg;
	}

	public void setCorpOrdinaryFlg(String corpOrdinaryFlg) {
		this.corpOrdinaryFlg = corpOrdinaryFlg;
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

	public String getTempOrdinaryFlg() {
		return tempOrdinaryFlg;
	}

	public void setTempOrdinaryFlg(String tempOrdinaryFlg) {
		this.tempOrdinaryFlg = tempOrdinaryFlg;
	}

	public String getTempAccountAddress() {
		return tempAccountAddress;
	}

	public void setTempAccountAddress(String tempAccountAddress) {
		this.tempAccountAddress = tempAccountAddress;
	}

	public String getTempAccountBank() {
		return tempAccountBank;
	}

	public void setTempAccountBank(String tempAccountBank) {
		this.tempAccountBank = tempAccountBank;
	}

	public String getTempAccountTel() {
		return tempAccountTel;
	}

	public void setTempAccountTel(String tempAccountTel) {
		this.tempAccountTel = tempAccountTel;
	}

	public String getTempAccountNo() {
		return tempAccountNo;
	}

	public void setTempAccountNo(String tempAccountNo) {
		this.tempAccountNo = tempAccountNo;
	}

	public void setCorpOrdinaryFlgName(String corpOrdinaryFlgName) {
		this.corpOrdinaryFlgName = corpOrdinaryFlgName;
	}

	public void setTempOrdinaryFlgName(String tempOrdinaryFlgName) {
		this.tempOrdinaryFlgName = tempOrdinaryFlgName;
	}

}