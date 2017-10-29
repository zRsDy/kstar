package com.ibm.kstar.action.common;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

public class KstarProductVO {

	private String id; 
	//产品ID
	private String proId;
	// 产品编码
	private String productCode;
	
	// 产品名称
	private String productName;
	
	// 英文名
	private String proEName;
	
	// 产品描述
	private String proDesc;
	
	// 需求描述
	private String proRequestDesc;
	
	// 物料号
	private String materielCode;
	// 预定义物料号
	private String vmaterCode;
	// 单位
	private String unit;
	
	// 毛重
	private Double crossWeight;
	
	// 净重
	private Double netWeight;
	
	// 品牌
	private String proBrand;
	
	// 销售类型
	private String saleType;
	
	//产品型号
	private String proModel;
	
	// 本身目录价格
	private Double catalogPriceShow1;
		
	// 标准目录价格
	private Double catalogPrice1;

	// 本身金牌价格
	private Double catalogPriceShow;
		
	// 标准品金牌价格
	private Double catalogPrice;

	//CRM产品类别
	private String	crmCategory;
	
	// 转销状态
	private String saleStatus;
	
	//ERP产品
	private String erpCategory;
	
	// 产品线id
	private String proLineId;
	
	// 产品线
	private String proLine;
	// 产品组
	private String proGroup;
	// 产品分类
	private String cproCategory;
	// 产品类别
	private String cproType;
	
	// 客户型号
	private String clientCategory;
	
	// 产品系列
	private String proSeries;
	// 功率容量
	private String cproPowcap;
	// 转销状态Grid
	private String saleStatusLable;
	// 单位名称
	private String unitLable;
	//CRM产品类别显示名称
	private Object crmCategoryLable;
	//ERP产品类别显示名称
	private Object erpCategoryLable;
	
	private String procatalogNamePath;
	
	//报备
	private String optTxt2; 
	
	public KstarProductVO(){};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
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

	public String getProRequestDesc() {
		return proRequestDesc;
	}

	public void setProRequestDesc(String proRequestDesc) {
		this.proRequestDesc = proRequestDesc;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProLineId() {
		return proLineId;
	}

	public void setProLineId(String proLineId) {
		this.proLineId = proLineId;
	}

	public String getProLine() {
		return proLine;
	}

	public void setProLine(String proLine) {
		this.proLine = proLine;
	}

	public String getCproPowcap() {
		return cproPowcap;
	}

	public void setCproPowcap(String cproPowcap) {
		this.cproPowcap = cproPowcap;
	}

	public Double getCrossWeight() {
		return crossWeight;
	}

	public void setCrossWeight(Double crossWeight) {
		this.crossWeight = crossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
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

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public Double getCatalogPrice() {
		return catalogPrice;
	}

	public void setCatalogPrice(Double catalogPrice) {
		this.catalogPrice = catalogPrice;
	}

	public String getErpCategory() {
		return erpCategory;
	}

	public void setErpCategory(String erpCategory) {
		this.erpCategory = erpCategory;
	}

	
	public Object getCrmCategoryLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("crmCategory", crmCategory);
		if( null != obj){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}		
		return  lov.getName();
	}

	public void setCrmCategoryLable(Object crmCategoryLable) {
		this.crmCategoryLable = crmCategoryLable;
	}

	public Object getErpCategoryLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("erpCategory", erpCategory);
		if(obj !=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setErpCategoryLable(Object erpCategoryLable) {
		this.erpCategoryLable = erpCategoryLable;
	}

	public String getVmaterCode() {
		return vmaterCode;
	}

	public void setVmaterCode(String vmaterCode) {
		this.vmaterCode = vmaterCode;
	}

	public Double getCatalogPriceShow() {
		return catalogPriceShow;
	}

	public void setCatalogPriceShow(Double catalogPriceShow) {
		this.catalogPriceShow = catalogPriceShow;
	}

	public String getCrmCategory() {
		return crmCategory;
	}

	public void setCrmCategory(String crmCategory) {
		this.crmCategory = crmCategory;
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public String getProGroup() {
		return proGroup;
	}

	public void setProGroup(String proGroup) {
		this.proGroup = proGroup;
	}

	public String getCproCategory() {
		return cproCategory;
	}

	public void setCproCategory(String cproCategory) {
		this.cproCategory = cproCategory;
	}

	public String getCproType() {
		return cproType;
	}

	public void setCproType(String cproType) {
		this.cproType = cproType;
	}

	public String getClientCategory() {
		return clientCategory;
	}

	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}

	public String getProSeries() {
		return proSeries;
	}

	public void setProSeries(String proSeries) {
		this.proSeries = proSeries;
	}

	public String getSaleStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(saleStatus);
		if(obj != null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setSaleStatusLable(String saleStatusLable) {
		this.saleStatusLable = saleStatusLable;
	}

	public String getUnitLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(unit);
		if(obj != null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setUnitLable(String unitLable) {
		this.unitLable = unitLable;
	}

	public Double getCatalogPriceShow1() {
		return catalogPriceShow1;
	}

	public void setCatalogPriceShow1(Double catalogPriceShow1) {
		this.catalogPriceShow1 = catalogPriceShow1;
	}

	public Double getCatalogPrice1() {
		return catalogPrice1;
	}

	public void setCatalogPrice1(Double catalogPrice1) {
		this.catalogPrice1 = catalogPrice1;
	}

	public String getProcatalogNamePath() {
		return procatalogNamePath;
	}
	public void setProcatalogNamePath(String procatalogNamePath) {
		this.procatalogNamePath = procatalogNamePath;
	}
	public String getDisplayCatalogName(){
		if(procatalogNamePath== null){
			return null;
		}
		String[] names = procatalogNamePath.split("/");
		if(names.length > 3){
			return names[2];
		}
		return procatalogNamePath;
	}

	public String getOptTxt2() {
		return optTxt2;
	}

	public void setOptTxt2(String optTxt2) {
		this.optTxt2 = optTxt2;
	}
	
}
