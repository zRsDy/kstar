package com.ibm.kstar.entity.report;

import java.util.Date;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 
 * ClassName: 出货详细报表
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年4月26日 下午1:49:36 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */

public class ReportDelivelyDetail {
    
    /** 主键自增 */
	private String id;
	
	/**销售员工号**/
	private String salesManNo;
	
	/**销售员ID**/
	private String salesManId;
	
	/**销售员名称**/
	private String salesManName;
	
	/**客户编号**/
	private String customerCode;
	
	/**客户名称**/
	private String customerName;
	
	/**客户类别**/
	private String customClass;
	
	/**行业大类**/
	private String customType;
	
	/**行业小类**/
	private String customTypeSub;
	
	/**销售员岗位ID**/
	private String salesmanPosId;
	
	/**销售员岗位名称**/
	private String salesmanPosName;
	
	/**所属部门ID**/
	private String salesmanOrgId;
	
	/**所属部门**/
	private String salesmanOrgName;
	
	/**所属部门Path**/
	private String salesmanOrgPath;
	
	/**营销中心**/
	private String salesmanCenter;
	
	/**打印日期**/
	private Date printTime;
	
	/**开票数量**/
	private String invoiceQuantity;
	
	/**发货数量**/
	private String deliveryQuantity;
	
	/**省份**/
	private String provinceName;
	
	/**城市**/
	private String cityName;
	
	/**区/县**/
	private String countyName;
	
	/**币种**/
	private String currency;
	
	/**物料编码**/
	private String materielCode;
	
	/**产品类别**/
	private String proCategory;
	
	/**产品型号**/
	private String proSeries;
	
	/**人名币金额**/
	private String rmbAmount;
	
	/**美元金额**/
	private String usdAmount;

	/**ERP订单号**/
	private String erpOrderCode;
	
	/**ERP外部出货单号**/
	private String externalNo;
	
	/**CRM订单号**/
	private String orderCode;

	/**CRM出货申请单号**/
	private String deliveryCode;
	
	/**营销中心ID**/
	private String parentOrgId;
	
	public ReportDelivelyDetail() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalesManNo() {
		return salesManNo;
	}

	public void setSalesManNo(String salesManNo) {
		this.salesManNo = salesManNo;
	}

	public String getSalesManId() {
		return salesManId;
	}

	public void setSalesManId(String salesManId) {
		this.salesManId = salesManId;
	}

	public String getSalesManName() {
		return salesManName;
	}

	public void setSalesManName(String salesManName) {
		this.salesManName = salesManName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomClass() {
		return customClass;
	}

	public void setCustomClass(String customClass) {
		this.customClass = customClass;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public String getCustomTypeSub() {
		return customTypeSub;
	}

	public void setCustomTypeSub(String customTypeSub) {
		this.customTypeSub = customTypeSub;
	}

	public String getSalesmanPosId() {
		return salesmanPosId;
	}

	public void setSalesmanPosId(String salesmanPosId) {
		this.salesmanPosId = salesmanPosId;
	}

	public String getSalesmanPosName() {
		return salesmanPosName;
	}

	public void setSalesmanPosName(String salesmanPosName) {
		this.salesmanPosName = salesmanPosName;
	}

	public String getSalesmanOrgId() {
		return salesmanOrgId;
	}

	public void setSalesmanOrgId(String salesmanOrgId) {
		this.salesmanOrgId = salesmanOrgId;
	}

	public String getSalesmanOrgName() {
		return salesmanOrgName;
	}

	public void setSalesmanOrgName(String salesmanOrgName) {
		this.salesmanOrgName = salesmanOrgName;
	}

	public String getSalesmanOrgPath() {
		return salesmanOrgPath;
	}

	public void setSalesmanOrgPath(String salesmanOrgPath) {
		this.salesmanOrgPath = salesmanOrgPath;
	}

	public String getSalesmanCenter() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(parentOrgId);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}

	public void setSalesmanCenter(String salesmanCenter) {
		this.salesmanCenter = salesmanCenter;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getInvoiceQuantity() {
		return invoiceQuantity;
	}

	public void setInvoiceQuantity(String invoiceQuantity) {
		this.invoiceQuantity = invoiceQuantity;
	}

	public String getDeliveryQuantity() {
		return deliveryQuantity;
	}

	public void setDeliveryQuantity(String deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCurrency() {
		String sCurrency = "";
		if("USD".equals(currency)){
			sCurrency = "美元";
		}else {
			sCurrency = "人民币";
		}
		return sCurrency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getProCategory() {
		return proCategory;
	}

	public void setProCategory(String proCategory) {
		this.proCategory = proCategory;
	}

	public String getProSeries() {
		return proSeries;
	}

	public void setProSeries(String proSeries) {
		this.proSeries = proSeries;
	}

	public String getRmbAmount() {
		return rmbAmount;
	}

	public void setRmbAmount(String rmbAmount) {
		this.rmbAmount = rmbAmount;
	}

	public String getUsdAmount() {
		return usdAmount;
	}

	public void setUsdAmount(String usdAmount) {
		this.usdAmount = usdAmount;
	}

	public String getErpOrderCode() {
		return erpOrderCode;
	}

	public void setErpOrderCode(String erpOrderCode) {
		this.erpOrderCode = erpOrderCode;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	
	
	
}
