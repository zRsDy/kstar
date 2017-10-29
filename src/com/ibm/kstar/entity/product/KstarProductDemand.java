package com.ibm.kstar.entity.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "CRM_T_PRODUCT_DEMAND")
public class KstarProductDemand extends BaseEntity implements Serializable {
	
	// 需求单ID
	@Id
	@GeneratedValue(generator = "kstarprocdemand_id_generator")
	@GenericGenerator(name="kstarprocdemand_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 需求单编号
	@Column(name="C_DEMAND_CODE")
	private String demandCode;
	
	// 需求单名称
	@Column(name="C_DEMAND_NAME")
	private String demandName;
	
	// 客户id
	@Column(name="C_CLIENT_CODE")
	private String clientCode;
	
	@Transient
	private String clientName;
	
	// 客户所在地  地址PID
	@Column(name="C_CLIENT_ADDRESS")
	private String clientAddress;
	
	@Transient
	private String clientAddressName;
	
	// 客户PO/合同
	@Column(name="C_CLIENT_CONTRACT")
	private String clientContract;
	
	// 期望交货日期
	@Column(name="DT_HOPEDELIVER_DATE")
	private Date hopeDeliverDate;
	
	// 需求种类
	@Column(name="C_DEMANT_CATEGORY")
	private String demantCategory;
	
	// 需求数量
	@Column(name="N_DEMANT_NUMBER")
	private Long demandNumber;
	
	// 是否提前备料
	@Column(name="C_PREPARE_BEFORE")
	private String prepareBefore;
	
	// 需求部门
	@Column(name="C_DEMAND_DEPARTMENT")
	private String demandDepartment;
	
	
	// 需求人员
	@Column(name="C_DEMAND_PERSON")
	private String demandPerson;
	
	@Transient
	private String demandPersonName;
	
	// 联系电话
	@Column(name="C_CONTRACT_TEL")
	private String contractNumber;
	
	// 需求状态
	@Column(name="C_DEMAND_STATUS")
	private String demandStatus;
	
	@Transient
	private String relProductID;
	// 产品ID
	@Column(name="C_PRO_ID")
	private String productID;
	
	
	// 特殊产品规格需求清单
	@Column(name="C_SPEC_REQUIRE_LIST")
	private String specRequireList;
	
	// 客户资料
	@Column(name="C_CLIENT_DOC")
	private String clientDoc;
	
	//驳回原因
	@Column(name="C_BACK_REASON")
	private String backReason;
	
	//营销部门
	@Column(name="C_BUSINESS_UNIT")
	private String businessUnit;
	
	//产品系列/型号
	@Column(name="C_PRO_SERIES_OR_MODEL")
	private String proSeriesOrModel;
	
	// 客户行业 小类
	@Column(name = "c_custom_category_sub")
	private String customCategorySub;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDemandCode() {
		return demandCode;
	}

	public void setDemandCode(String demandCode) {
		this.demandCode = demandCode;
	}

	public String getDemandName() {
		return demandName;
	}

	public void setDemandName(String demandName) {
		this.demandName = demandName;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getClientAddressName() {
		return clientAddressName;
	}

	public void setClientAddressName(String clientAddressName) {
		this.clientAddressName = clientAddressName;
	}

	public String getClientContract() {
		return clientContract;
	}

	public void setClientContract(String clientContract) {
		this.clientContract = clientContract;
	}

	public Date getHopeDeliverDate() {
		return hopeDeliverDate;
	}

	public void setHopeDeliverDate(Date hopeDeliverDate) {
		this.hopeDeliverDate = hopeDeliverDate;
	}

	public String getDemantCategory() {
		return demantCategory;
	}

	public void setDemantCategory(String demantCategory) {
		this.demantCategory = demantCategory;
	}

	public String getDemantCategoryName() {
		return this.getLovName(demantCategory);
	}
	
	public Long getDemandNumber() {
		return demandNumber;
	}

	public void setDemandNumber(Long demandNumber) {
		this.demandNumber = demandNumber;
	}

	public String getPrepareBefore() {
		return prepareBefore;
	}

	public void setPrepareBefore(String prepareBefore) {
		this.prepareBefore = prepareBefore;
	}

	public String getDemandDepartment() {
		return demandDepartment;
	}

	public void setDemandDepartment(String demandDepartment) {
		this.demandDepartment = demandDepartment;
	}

	public String getDemandDepartmentName() {
		return this.getLovName(demandDepartment);
	}
	
	public String getDemandPerson() {
		return demandPerson;
	}

	public void setDemandPerson(String demandPerson) {
		this.demandPerson = demandPerson;
	}

	public String getDemandPersonName() {
		return demandPersonName;
	}

	public void setDemandPersonName(String demandPersonName) {
		this.demandPersonName = demandPersonName;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getDemandStatus() {
		return demandStatus;
	}

	public void setDemandStatus(String demandStatus) {
		this.demandStatus = demandStatus;
	}

	public String getDemandStatusName() {
		return this.getLovName(demandStatus);
	}
	
	public String getRelProductID() {
		return relProductID;
	}

	public void setRelProductID(String relProductID) {
		this.relProductID = relProductID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getSpecRequireList() {
		return specRequireList;
	}

	public void setSpecRequireList(String specRequireList) {
		this.specRequireList = specRequireList;
	}

	public String getClientDoc() {
		return clientDoc;
	}

	public void setClientDoc(String clientDoc) {
		this.clientDoc = clientDoc;
	}

	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
	public String getBusinessUnitCode() {
		return this.getLovCode(businessUnit);
	}
	
	public String getBusinessUnitName() {
		return this.getLovName(businessUnit);
	}

	public String getProSeriesOrModel() {
		return proSeriesOrModel;
	}

	public void setProSeriesOrModel(String proSeriesOrModel) {
		this.proSeriesOrModel = proSeriesOrModel;
	}

	public String getCustomCategorySub() {
		return customCategorySub;
	}

	public void setCustomCategorySub(String customCategorySub) {
		this.customCategorySub = customCategorySub;
	}

	public String getCustomCategorySubName() {
		return this.getLovName(customCategorySub);
	}
	
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}
