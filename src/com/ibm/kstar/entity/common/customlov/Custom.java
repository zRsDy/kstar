package com.ibm.kstar.entity.common.customlov;

import java.util.Date;
import java.util.List;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.team.Orgs;
import com.ibm.kstar.entity.team.Team;

public class Custom implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private CustomInfo customInfo;
	private Team team;
	private Orgs org;

	public Custom(CustomInfo customInfo, Team team, Orgs org) {
		this.customInfo = customInfo;
		this.team = team;
		this.org = org;
		this.id = customInfo.getId();
	}

	public Custom(List<Object> list) {
		this.customInfo = (CustomInfo) list.get(0);
		this.team = (Team) list.get(1);
		this.org = (Orgs) list.get(2);
		this.id = customInfo.getId();
	}

	public String getId() {
		return id;
	}


	public String getCustomClassName() {
		return customInfo.getCustomClassName();
	}

	public String getCustomSourceName() {
		return customInfo.getCustomSourceName();
	}

	public String getCustomStatusName() {
		return customInfo.getCustomStatusName();
	}




	public String getCustomAreaName() {
		return customInfo.getCustomAreaName();
	}

	public String getCustomAreaSub1Name() {
		return customInfo.getCustomAreaSub1Name();
	}

	public String getCustomAreaSub2Name() {
		return customInfo.getCustomAreaSub2Name();
	}

	public String getCustomAreaSub3Name() {
		return customInfo.getCustomAreaSub3Name();
	}

	public String getCustomControlStatusName() {
		return customInfo.getCustomControlStatusName();
	}

	public String getCustomIncomeScaleName() {
		return customInfo.getCustomIncomeScaleName();
	}

	public String getCustomReportedFlgName() {
		return customInfo.getCustomReportedFlgName();
	}

	public String getCustomOropertyName() {
		return customInfo.getCustomOropertyName();
	}

	public String getCorpPaymentDefaultName() {
		return customInfo.getCorpPaymentDefaultName();
	}

	public String getCorpOrdinaryFlgName() {
		return customInfo.getCorpOrdinaryFlgName();
	}

	public String getCorpTermDefaultName() {
		return customInfo.getCorpTermDefaultName();
	}
	
	public String getCorpDetailFlgName() {
		return customInfo.getCorpDetailFlgName();
	}
	
	 public String getLimitCurrencyName() {
		 return customInfo.getLimitCurrencyName();
	 }
	 
	 public String getLimitUnitName() {
		 return customInfo.getLimitUnitName();
	 }

	 public String getTempCurrencyName() {
		 return customInfo.getTempCurrencyName();
		}
	 
	 public String getTempUnitName() {
		 return customInfo.getTempUnitName();
	 }
	 

	public String getCustomGradeName() {
		return customInfo.getCustomGradeName();
	}


	public String getCustomCategoryName() {

		return customInfo.getCustomCategoryName();
	}

	public String getCustomCategorySubName() {

		return customInfo.getCustomCategorySubName();
	}

	public String getPipeClassName() {
		return customInfo.getPipeClassName();
	}


	public String getPipeAttrName() {
		return customInfo.getPipeAttrName();
	}

	public String getPipeRankName() {
		return customInfo.getPipeRankName();
	}

	public String getMasterEmployeeId() {
		return team.getMasterEmployeeId();
	}

	public String getMasterEmployeeName() {

		Employee lov = new Employee();
		Object obj = CacheData.getInstance().get(team.getMasterEmployeeId());
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}

	public CustomInfo getCustomInfo() {
		return customInfo;
	}

	public Team getTeam() {
		return team;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCustomInfo(CustomInfo customInfo) {
		this.customInfo = customInfo;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Orgs getOrg() {
		return org;
	}

	public void setOrg(Orgs org) {
		this.org = org;
	}

	public String getBusinessId() {
		return org.getBusinessId();
	}

	public String getBusinessType() {
		return org.getBusinessType();
	}

	public String getOrgId() {
		return org.getOrgId();
	}

	public String getPrimaryOrgId() {
		return org.getPrimaryOrgId();
	}
	
	public String getPrimaryOrgName() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(org.getPrimaryOrgId());
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}

	public String getPrimaryOrgFlag() {
		return org.getPrimaryOrgFlag();
	}

	public String getErpStatusName() {
		return this.customInfo.getErpStatusName();
	}
	
	public String getCustomCode() {
		return customInfo.getCustomCode();
	}

	public String getErpCode() {
		return customInfo.getErpCode();
	}

	public String getErpStatus() {
		return customInfo.getErpStatus();
	}

	public String getCustomFullName() {
		return customInfo.getCustomFullName();
	}

	public String getCustomAliasName() {
		return customInfo.getCustomAliasName();
	}

	public String getCustomWebAddress() {
		return customInfo.getCustomWebAddress();
	}

	public String getCustomType() {
		return customInfo.getCustomType();
	}

	public String getCustomClass() {
		return customInfo.getCustomClass();
	}

	public String getCustomSource() {
		return customInfo.getCustomSource();
	}

	public String getCustomStatus() {
		return customInfo.getCustomStatus();
	}

	public String getCustomOem() {
		return customInfo.getCustomOem();
	}

	public String getCustomArea() {
		return customInfo.getCustomArea();
	}

	public String getCustomAreaSub1() {
		return customInfo.getCustomAreaSub1();
	}

	public String getCustomAreaSub2() {
		return customInfo.getCustomAreaSub2();
	}

	public String getCustomAreaSub3() {
		return customInfo.getCustomAreaSub3();
	}

	public String getCustomControlStatus() {
		return customInfo.getCustomControlStatus();
	}

	public String getCustomGrade() {
		return customInfo.getCustomGrade();
	}

	public String getCustomIncomeScale() {
		return customInfo.getCustomIncomeScale();
	}

	public String getCustomReportedFlg() {
		return customInfo.getCustomReportedFlg();
	}

	public String getCustomOroperty() {
		return customInfo.getCustomOroperty();
	}

	public String getCustomCategory() {
		return customInfo.getCustomCategory();
	}

	public String getCustomCategorySub() {
		return customInfo.getCustomCategorySub();
	}

	public String getCustomCategoryComment() {
		return customInfo.getCustomCategoryComment();
	}

	public String getCustomProfile() {
		return customInfo.getCustomProfile();
	}

	public String getCorpRepresentative() {
		return customInfo.getCorpRepresentative();
	}

	public String getCorpTrn() {
		return customInfo.getCorpTrn();
	}

	public String getCorpPaymentDefault() {
		return customInfo.getCorpPaymentDefault();
	}

	public String getCorpRegAddress() {
		return customInfo.getCorpRegAddress();
	}

	public String getCorpOrdinaryFlg() {
		return customInfo.getCorpOrdinaryFlg();
	}

	public String getCorpTermDefault() {
		return customInfo.getCorpTermDefault();
	}

	public String getCorpRegCaptial() {
		return customInfo.getCorpRegCaptial();
	}

	public String getCorpPayDays() {
		return customInfo.getCorpPayDays();
	}

	public String getCorpCiticAmount() {
		return customInfo.getCorpCiticAmount();
	}

	public String getCorpOnsalePrice() {
		return customInfo.getCorpOnsalePrice();
	}

	public String getCorpDetailFlg() {
		return customInfo.getCorpDetailFlg();
	}

	public String getCorpPiccAmount() {
		return customInfo.getCorpPiccAmount();
	}

	public String getCorpInvoiceName() {
		return customInfo.getCorpInvoiceName();
	}

	public String getCorpInvoiceComment() {
		return customInfo.getCorpInvoiceComment();
	}

	public String getCreditId() {
		return customInfo.getCreditId();
	}

	public String getLimitCurrency() {
		return customInfo.getLimitCurrency();
	}

	public String getLimitAmount() {
		return customInfo.getLimitAmount();
	}

	public String getLimitRate() {
		return customInfo.getLimitRate();
	}

	public String getLimitUnit() {
		return customInfo.getLimitUnit();
	}

	public String getLimitComment() {
		return customInfo.getLimitComment();
	}

	public Date getLimitValidTo() {
		return customInfo.getLimitValidTo();
	}

	public String getTempComment() {
		return customInfo.getTempComment();
	}

	public String getTempCurrency() {
		return customInfo.getTempCurrency();
	}

	public String getTempAmount() {
		return customInfo.getTempAmount();
	}

	public String getTempRate() {
		return customInfo.getTempRate();
	}

	public String getTempUnit() {
		return customInfo.getTempUnit();
	}

	public Date getTempValidTo() {
		return customInfo.getTempValidTo();
	}

	public String getPipeClass() {
		return customInfo.getPipeClass();
	}

	public String getPipeAttr() {
		return customInfo.getPipeAttr();
	}

	public String getPipeRank() {
		return customInfo.getPipeRank();
	}

	public String getCreatedById() {
		return customInfo.getCreatedById();
	}

	public Date getCreatedAt() {
		return customInfo.getCreatedAt();
	}

	public String getCreatedPosId() {
		return customInfo.getCreatedPosId();
	}

	public String getCreatedOrgId() {
		return customInfo.getCreatedOrgId();
	}

	public String getUpdatedById() {
		return customInfo.getUpdatedById();
	}

	public Date getUpdatedAt() {
		return customInfo.getUpdatedAt();
	}
}