package com.ibm.kstar.entity.product;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.custom.CustomInfo;

@Entity
@Table(name = "CRM_T_PRODUCT_BASIC")
public class KstarProduct extends BaseEntity implements  Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarproduct_id_generator")
	@GenericGenerator(name="kstarproduct_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;

	// 产品编码
	@Column(name = "C_PRO_CODE")
	private String productCode;

	// 产品名称
	@Column(name = "C_PRO_NAME")
	private String productName;
	
	// 英文名
	@Column(name = "C_PRO_ENAME")
	private String proEName;
	
	// 产品描述
	@Column(name = "C_PRO_DESC")
	private String proDesc;
	
	// 需求描述
	@Column(name = "C_PRO_REQUEST_DESC")
	private String proRequestDesc;
	
	// 物料号
	@Column(name = "C_MATER_CODE")
	private String materCode;
	
	// 预定义物料号
	@Column(name = "C_VMATER_CODE")
	private String vmaterCode;
	
	// 产品型号
	@Column(name = "C_PRO_MODEL")
	private String proModel;
	
	// 父产品
	@Column(name = "C_FATHER_PRO")
	private String fatherProCode;
	
	// 产品发布状态
	@Column(name = "C_PRO_PUBLISH_STATUS")
	private String publishStatus;
	
	// 产品订价状态
	@Column(name = "C_PRO_PRICE_STATUS")
	private String priceStatus;
	
	// 产品转销状态
	@Column(name = "C_PRO_SALE_STATUS")
	private String saleStatus;
	
	// 是否RoHS
	@Column(name = "C_PRO_ROHS_STATUS")
	private String rohStatus;
	
	// 组织ID
	@Column(name = "C_PRO_ORG_ID")
	private String proOrgId;
	
	// 单位
	@Column(name = "C_UNIT")
	private String unit;

	// 毛重
	@Column(name = "N_GROSS_WEIGHT")
	private Double crossWeight;
	
	// 净重
	@Column(name = "N_NET_WEIGHT")
	private Double netWeight;
	
	// 海关料号
	@Column(name = "C_CUSTOMS_CODE")
	private String customsCode;
	
	// 产品长(cm)
	@Column(name = "N_PRO_LENGTH")
	private Double proLength;
	
	// 产品宽(cm)
	@Column(name = "N_PRO_WIDTH")
	private Double proWidth;
	
	// 产品高(cm)
	@Column(name = "N_PRO_HEIGHT")
	private Double proHight;
	
	// 包装长(cm)
	@Column(name = "N_PRO_WRAP_LENGTH")
	private Double proWrapLength;
	
	// 包装宽(cm)
	@Column(name = "N_PRO_WRAP_WIDTH")
	private Double proWrapWidth;
	
	// 包装高(cm)
	@Column(name = "N_PRO_WRAP_HEIGHT")
	private Double proWrapHight;
	
	// 装柜数量(20尺平柜)
	@Column(name = "N_CABINET_20FLAT")
	private Double cabinet20Flat;
	
	// 装柜数量(20尺超重柜)
	@Column(name = "N_CABINET_20WIGHT")
	private Double cabinet20Weight;
	
	// 装柜数量(40尺普通柜)
	@Column(name = "N_CABINET_40FLAT")
	private Double cabinet40Flat;
	
	// 装柜数量(40尺高柜)
	@Column(name = "N_CABINET_40HIGHT")
	private Double cabinet40Hight;
	
	// ERP产品类别
	@Column(name = "C_PRO_ERP_CATEGORY")
	private String erpCategory;
	
	// CRM产品类别
	@Column(name = "C_PRO_CRM_CATEGORY")
	private String crmCategory;

	// 品牌
	@Column(name = "C_BRAND")
	private String proBrand;
	
	// 销售类型
	@Column(name = "C_SALE_TYPE")
	private String saleType;
	
	@Transient
	private KstarProductLine lineBean = new KstarProductLine();
	
	// 产品线ID
	@Column(name="C_PRO_LINE_ID")
	private String proLineID;
	
	// 客户型号
	@Column(name="C_CLIENT_CATEGORY")
	private String clientCategory;
	
	// 供应商CODE
	@Column(name="C_CLIENT_CODE")
	private String clientCode;
	
	// 供应商CODE
	@Column(name="N_CLIENT_ID")
	private String clientId;

	// 客户信息
	@Transient
	private CustomInfo customInfo;

	// 是否专用料
	@Column(name="C_SPECIAL_USE")
	private String isSpecialUse;
	
	// 销售状态
	@Column(name="C_SALE_STATUS")
	private String csaleStatus;
	
	// 更新后的销售状态（流程通过后，将改为的状态）
	@Column(name="C_NEW_SALE_STATUS")
	private String newCsaleStatus;
	
	// 销售流程状态
	@Column(name="C_SALE_PROCESS_STATUS")
	private String csaleProcessStatus;
	
	// 销售原因
	@Column(name="C_SALE_REASON")
	private String csaleReason;
	
	// 需求单编号
	@Column(name="C_DEMAND_CODE")
	private String demandCode;
	
	// 供应链ID (z走接口)
	@Column(name="C_SUPPLY_CHAIN")
	private String supplyChainID;
	
	// 修改硬件
	@Column(name="C_MODIFY_HARDWARE")
	private String modifyHardware;
	
	// 增加功能
	@Column(name="C_ADD_FUNCTION")
	private String addFunction;
	
	// 更改参数
	@Column(name="C_MODIFY_PARAMTER")
	private String modifyParamter;
	
	// 机箱尺寸/外观变更
	@Column(name="C_CHASSIS_SIZE")
	private String chassisSize;
	
	// 商品化资料
	@Column(name="C_COMMERCIAL_DATA")
	private String commercialData;
	
	// 增加随机附件
	@Column(name="C_RANDOM_ATTACH")
	private String randomAttach;
	
	// 其他
	@Column(name="C_OTHER")
	private String other;
		
	// 所在仓库
	@Transient
	private String wareHouse;
	
	// 仓库代码
	@Transient
	private String wareHouseNumber;
	
	// 库存数量
	@Transient
	private Double stockNumber;
	
	// 客户ID
	@Transient
	private Integer clientID;
	
	// 客户名称
	@Transient
	private String clientName;
	
	// 客户型号
	@Transient
	private String clientModel;
	
	@Transient
	private KstarEcrBean ecrBean = new KstarEcrBean();
	
	@Transient
	private Long demandNumber;
	
	@Transient
	private String prepareBefore;
	
	public KstarProduct() {
		super();
	}

	public KstarProduct(KstarProduct kp, ProductDemandRel rel,KstarProductLine line) {
		this.productCode = kp.productCode;
		this.productName = kp.productName;
		this.proEName = kp.proEName;
		this.proDesc = kp.proDesc;
		this.proRequestDesc = kp.proRequestDesc;
		this.materCode = kp.materCode;
		this.vmaterCode = kp.vmaterCode;
		this.proModel = kp.proModel;
		this.fatherProCode = kp.fatherProCode;
		this.publishStatus = kp.publishStatus;
		this.priceStatus = kp.priceStatus;
		this.saleStatus = kp.saleStatus;
		this.rohStatus = kp.rohStatus;
		this.proOrgId = kp.proOrgId;
		this.unit = kp.unit;
		this.crossWeight = kp.crossWeight;
		this.netWeight = kp.netWeight;
		this.customsCode = kp.customsCode;
		this.proLength = kp.proLength;
		this.proWidth = kp.proWidth;
		this.proHight = kp.proHight;
		this.proWrapLength = kp.proWrapLength;
		this.proWrapWidth = kp.proWrapWidth;
		this.proWrapHight = kp.proWrapHight;
		this.cabinet20Flat = kp.cabinet20Flat;
		this.cabinet20Weight = kp.cabinet20Weight;
		this.cabinet40Flat = kp.cabinet40Flat;
		this.cabinet40Hight = kp.cabinet40Hight;
		this.erpCategory = kp.erpCategory;
		this.crmCategory = kp.crmCategory;
		this.proBrand = kp.proBrand;
		this.saleType = kp.saleType;
		this.lineBean = kp.lineBean;
		this.proLineID = kp.proLineID;
		this.clientCategory = kp.clientCategory;
		this.clientCode = kp.clientCode;
		this.clientId = kp.clientId;
		this.customInfo = kp.customInfo;
		this.isSpecialUse = kp.isSpecialUse;
		this.csaleStatus = kp.csaleStatus;
		this.newCsaleStatus = kp.newCsaleStatus;
		this.csaleProcessStatus = kp.csaleProcessStatus;
		this.csaleReason = kp.csaleReason;
		this.demandCode = kp.demandCode;
		this.supplyChainID = kp.supplyChainID;
		this.modifyHardware = kp.modifyHardware;
		this.addFunction = kp.addFunction;
		this.modifyParamter = kp.modifyParamter;
		this.chassisSize = kp.chassisSize;
		this.commercialData = kp.commercialData;
		this.randomAttach = kp.randomAttach;
		this.other = kp.other;
		this.wareHouse = kp.wareHouse;
		this.wareHouseNumber = kp.wareHouseNumber;
		this.stockNumber = kp.stockNumber;
		this.clientName = kp.clientName;
		this.clientModel = kp.clientModel;
		this.demandNumber = rel.getDemandNumber();
		this.prepareBefore = rel.getPrepareBefore();
		this.id = rel.getId();
		this.lineBean = line;
	}

	public CustomInfo getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(CustomInfo customInfo) {
		this.customInfo = customInfo;
	}
	
	public String getCrmCategoryName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("crmCategory", crmCategory);
		if( null != obj){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}		
		return lov.getName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProEName() {
		return proEName;
	}

	public void setProEName(String proEName) {
		this.proEName = proEName;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getMaterCode() {
		return materCode;
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public String getVmaterCode() {
		return vmaterCode;
	}

	public void setVmaterCode(String vmaterCode) {
		this.vmaterCode = vmaterCode;
	}

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public String getFatherProCode() {
		return fatherProCode;
	}

	public void setFatherProCode(String fatherProCode) {
		this.fatherProCode = fatherProCode;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
	

	public String getPublishStatusName() {
		return this.getLovName(publishStatus);
	}

	public String getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}

	public String getPriceStatusName() {
		return  this.getLovName(priceStatus);
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public String getSaleStatusName() {
		return this.getLovName(saleStatus);
	}

	public String getRohStatus() {
		return rohStatus;
	}

	public void setRohStatus(String rohStatus) {
		this.rohStatus = rohStatus;
	}

	public String getProOrgId() {
		return proOrgId;
	}

	public void setProOrgId(String proOrgId) {
		this.proOrgId = proOrgId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitName() {
		return this.getLovName(unit);
	}

	public Double getCrossWeight() {
		return crossWeight;
	}

	public void setCrossWeight(Double crossWeight) {
		this.crossWeight = crossWeight;
	}

	public String getCrmCategory() {
		return crmCategory;
	}

	public void setCrmCategory(String crmCategory) {
		this.crmCategory = crmCategory;
	}

	public String getProBrand() {
		return proBrand;
	}

	public void setProBrand(String proBrand) {
		this.proBrand = proBrand;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	
	public String getSaleTypeName() {
		return this.getLovName(saleType);
	}

	public String getCsaleStatus() {
		return csaleStatus;
	}

	public String getCsaleStatusName() {
		return this.getLovName(csaleStatus);
	}

	public void setCsaleStatus(String csaleStatus) {
		this.csaleStatus = csaleStatus;
	}

	public String getNewCsaleStatus() {
		return newCsaleStatus;
	}
	
	public String getNewCsaleStatusName() {
		return this.getLovName(newCsaleStatus);
	}

	public void setNewCsaleStatus(String newCsaleStatus) {
		this.newCsaleStatus = newCsaleStatus;
	}

	public String getCsaleProcessStatus() {
		return csaleProcessStatus;
	}
	
	public String getCsaleProcessStatusName() {
		return this.getLovName(csaleProcessStatus);
	}

	public void setCsaleProcessStatus(String csaleProcessStatus) {
		this.csaleProcessStatus = csaleProcessStatus;
	}

	public String getCsaleReason() {
		return csaleReason;
	}

	public void setCsaleReason(String csaleReason) {
		this.csaleReason = csaleReason;
	}
	
	public String getCsaleReasonName() {
		return this.getLovName(csaleReason);
	}

	public String getDemandCode() {
		return demandCode;
	}

	public void setDemandCode(String demandCode) {
		this.demandCode = demandCode;
	}

	public String getErpCategory() {
		return erpCategory;
	}

	public void setErpCategory(String erpCategory) {
		this.erpCategory = erpCategory;
	}

	public String getErpCategoryName() {
		return this.getLovMember("erpCategory", erpCategory).getName();
	}

	public String getClientCategory() {
		return clientCategory;
	}

	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getIsSpecialUse() {
		return isSpecialUse;
	}

	public void setIsSpecialUse(String isSpecialUse) {
		this.isSpecialUse = isSpecialUse;
	}

	public String getSupplyChainID() {
		return supplyChainID;
	}

	public void setSupplyChainID(String supplyChainID) {
		this.supplyChainID = supplyChainID;
	}

	public String getModifyHardware() {
		return modifyHardware;
	}

	public void setModifyHardware(String modifyHardware) {
		this.modifyHardware = modifyHardware;
	}

	public String getAddFunction() {
		return addFunction;
	}

	public void setAddFunction(String addFunction) {
		this.addFunction = addFunction;
	}

	public String getModifyParamter() {
		return modifyParamter;
	}

	public void setModifyParamter(String modifyParamter) {
		this.modifyParamter = modifyParamter;
	}

	public String getChassisSize() {
		return chassisSize;
	}

	public void setChassisSize(String chassisSize) {
		this.chassisSize = chassisSize;
	}

	public String getCommercialData() {
		return commercialData;
	}

	public void setCommercialData(String commercialData) {
		this.commercialData = commercialData;
	}

	public String getRandomAttach() {
		return randomAttach;
	}

	public void setRandomAttach(String randomAttach) {
		this.randomAttach = randomAttach;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getCustomsCode() {
		return customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	public Double getProLength() {
		return proLength;
	}

	public void setProLength(Double proLength) {
		this.proLength = proLength;
	}

	public Double getProWidth() {
		return proWidth;
	}

	public void setProWidth(Double proWidth) {
		this.proWidth = proWidth;
	}

	public Double getProHight() {
		return proHight;
	}

	public void setProHight(Double proHight) {
		this.proHight = proHight;
	}

	public Double getProWrapLength() {
		return proWrapLength;
	}

	public void setProWrapLength(Double proWrapLength) {
		this.proWrapLength = proWrapLength;
	}

	public Double getProWrapWidth() {
		return proWrapWidth;
	}

	public void setProWrapWidth(Double proWrapWidth) {
		this.proWrapWidth = proWrapWidth;
	}

	public Double getProWrapHight() {
		return proWrapHight;
	}

	public void setProWrapHight(Double proWrapHight) {
		this.proWrapHight = proWrapHight;
	}

	public Double getCabinet20Flat() {
		return cabinet20Flat;
	}

	public void setCabinet20Flat(Double cabinet20Flat) {
		this.cabinet20Flat = cabinet20Flat;
	}

	public Double getCabinet20Weight() {
		return cabinet20Weight;
	}

	public void setCabinet20Weight(Double cabinet20Weight) {
		this.cabinet20Weight = cabinet20Weight;
	}

	public Double getCabinet40Flat() {
		return cabinet40Flat;
	}

	public void setCabinet40Flat(Double cabinet40Flat) {
		this.cabinet40Flat = cabinet40Flat;
	}

	public Double getCabinet40Hight() {
		return cabinet40Hight;
	}

	public void setCabinet40Hight(Double cabinet40Hight) {
		this.cabinet40Hight = cabinet40Hight;
	}

	public KstarEcrBean getEcrBean() {
		return ecrBean;
	}

	public void setEcrBean(KstarEcrBean ecrBean) {
		this.ecrBean = ecrBean;
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

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

	public String getWareHouseNumber() {
		return wareHouseNumber;
	}

	public void setWareHouseNumber(String wareHouseNumber) {
		this.wareHouseNumber = wareHouseNumber;
	}

	public Double getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(Double stockNumber) {
		this.stockNumber = stockNumber;
	}

	public Integer getClientID() {
		return clientID;
	}

	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientModel() {
		return clientModel;
	}

	public void setClientModel(String clientModel) {
		this.clientModel = clientModel;
	}

	public KstarProductLine getLineBean() {
		return lineBean;
	}

	public void setLineBean(KstarProductLine lineBean) {
		this.lineBean = lineBean;
	}

	public String getProLineID() {
		return proLineID;
	}

	public void setProLineID(String proLineID) {
		this.proLineID = proLineID;
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getProRequestDesc() {
		return proRequestDesc;
	}

	public void setProRequestDesc(String proRequestDesc) {
		this.proRequestDesc = proRequestDesc;
	}
	
	public boolean isStandardProduct(){
		if("0001".equals(crmCategory))
			return true;
		return false;
	}
}
