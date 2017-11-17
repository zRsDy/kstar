package com.ibm.kstar.entity.common.bizopp;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ibm.kstar.entity.bizopp.BusinessOpportunity;

public class Bizopp implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private BusinessOpportunity bizopp;

	public Bizopp(BusinessOpportunity bizopp){
		this.bizopp = bizopp;
		this.id = bizopp.getId();
	}
	
	public Bizopp(List<Object> list){
		this.bizopp = (BusinessOpportunity)list.get(0);
		this.id = bizopp.getId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public BusinessOpportunity getBizopp() {
		return bizopp;
	}

	public String getSaleStage() {
		return bizopp.getSaleStage();
	}


	public String getBusinessProcess() {
		return bizopp.getBusinessProcess();
	}

	

	public String getEnterprise() {
		return bizopp.getEnterprise();
	}

	

	public String getNumber() {
		return bizopp.getNumber();
	}

	

	public String getStatus() {
		return bizopp.getStatus();
	}

	

	public String getSalerId() {
		return bizopp.getSalerId();
	}

	

	public String getSalerName() {
		return bizopp.getSalerName();
	}

	
	public String getCreater() {
		return bizopp.getCreatedById();
	}

	

	public String getConflictStatus() {
		return bizopp.getConflictStatus();
	}

	

	public String getSalerPhone() {
		return bizopp.getSalerPhone();
	}

	

	public Date getCreateTime() {
		return bizopp.getCreatedAt();
	}

	

	public String getConflictNumber() {
		return bizopp.getConflictNumber();
	}

	

	public String getClientId() {
		return bizopp.getClientId();
	}

	

	public String getOpportunityName() {
		return bizopp.getOpportunityName();
	}

	

	public String getAddress() {
		return bizopp.getAddress();
	}

	

	public Date getPlanFinDate() {
		return bizopp.getPlanFinDate();
	}

	

	public String getSaleMethod() {
		return bizopp.getSaleMethod();
	}

	

	public String getSuccessRate() {
		return bizopp.getSuccessRate();
	}

	

	public String getProjectSituation() {
		return bizopp.getProjectSituation();
	}

	

	public String getProjectProgress() {
		return bizopp.getProjectProgress();
	}

	

	public Integer getIsIntegreated() {
		return bizopp.getIsIntegreated();
	}

	

	public String getBizOppAddress() {
		return bizopp.getBizOppAddress();
	}

	

	public String getBidUnit() {
		return bizopp.getBidUnit();
	}

	

	public String getSource() {
		return bizopp.getSource();
	}

	

	public String getCurrency() {
		return bizopp.getCurrency();
	}

	

	public BigDecimal getEstimatedAmount() {
		return bizopp.getEstimatedAmount();
	}

	

	public Date getUpdateTime() {
		return bizopp.getUpdatedAt();
	}

	public String getCompetitiveBrands() {
		return bizopp.getCompetitiveBrands();
	}

	public void setBizopp(BusinessOpportunity bizopp) {
		this.bizopp = bizopp;
	}
}