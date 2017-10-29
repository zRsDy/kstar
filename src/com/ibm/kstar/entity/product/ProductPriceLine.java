package com.ibm.kstar.entity.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 价格表产品 
 * @author chao.chen
 *
 */
@Entity
@Table(name = "CRM_T_PRICE_LINE")
public class ProductPriceLine extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 价格行表ID
	@Id
	@GeneratedValue(generator = "kstarpricel_id_generator")
	@GenericGenerator(name="kstarpricel_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 产品ID
	@Column(name = "C_PRO_ID")
	private String productID;
	
	// 产品编码
	@Transient
	private String productCode;
	
	// 产品名称
	@Transient
	private String productName;
	
	// 物料号 -- 否
	@Transient
	private String materCode;
	
	// 预定义物料号
	@Transient
	private String vmaterCode;
	
	// 产品描述
	@Transient
	private String proDesc;
	
	// 单位
	@Transient
	private String unit;
	
	// 产品型号
	@Transient
	private String proModel;
	
	//产品线
	@Transient
	private String proLine;
	
	// 品牌
	@Transient
	private String proBrand;
	
	// 功率或容量
	@Transient
	private String cproPowcap;
	
	// 产品转销状态
	@Transient
	private String saleStatus;
	
	// 产品成本   -- 否
	@Column(name = "N_PRO_COST")
	private BigDecimal cost;
	
	// 目录价格   -- 是
	@Column(name = "N_CATALOG_PRICE")
	private BigDecimal catalogPrice;
	
	// 毛利率   -- 否
	@Column(name = "N_CATALOG_RATE")
	private BigDecimal crossMargin;
	
	// 有效期结束时间
	@Column(name = "DT_START_DATE")
	private Date startDate;
	
	// 有效期结束时间
	@Column(name = "DT_END_DATE")
	private Date endDate;

	// 	价格头表ID
	@Column(name = "C_PRICE_HEAD_ID")
	private String headId;
	
	
	//层一价格
	@Column(name = "N_LAYER1_PRICE")
	private BigDecimal layer1Price;
	
	//层一价折扣率
	@Column(name = "N_LAYER1_DISCOUNT")
	private BigDecimal layer1Discount;
	
	//层一数量级
	@Column(name = "N_LAYER1_NUMBER")
	private Long layer1Number;
	
	//层二价格
	@Column(name = "N_LAYER2_PRICE")
	private BigDecimal layer2Price;
	
	//层二价折扣率
	@Column(name = "N_LAYER2_DISCOUNT")
	private BigDecimal layer2Discount;
	
	//层二数量级
	@Column(name = "N_LAYER2_NUMBER")
	private Long layer2Number;
	
	//层三价格
	@Column(name = "N_LAYER3_PRICE")
	private BigDecimal layer3Price;
	
	//层三价折扣率
	@Column(name = "N_LAYER3_DISCOUNT")
	private BigDecimal layer3Discount;
	
	//层三数量级
	@Column(name = "N_LAYER3_NUMBER")
	private Long layer3Number;
	
	//层四价格
	@Column(name = "N_LAYER4_PRICE")
	private BigDecimal layer4Price;
	
	//层四价折扣率
	@Column(name = "N_LAYER4_DISCOUNT")
	private BigDecimal layer4Discount;
	
	//层四数量级
	@Column(name = "N_LAYER4_NUMBER")
	private Long layer4Number;

	//层五价格
	@Column(name = "N_LAYER5_PRICE")
	private BigDecimal layer5Price;
	
	//层五价折扣率
	@Column(name = "N_LAYER5_DISCOUNT")
	private BigDecimal layer5Discount;
	
	//层五数量级
	@Column(name = "N_LAYER5_NUMBER")
	private Long layer5Number;
	
	//层六价格
	@Column(name = "N_LAYER6_PRICE")
	private BigDecimal layer6Price;
	
	//层六价折扣率
	@Column(name = "N_LAYER6_DISCOUNT")
	private BigDecimal layer6Discount;
	
	//层六数量级
	@Column(name = "N_LAYER6_NUMBER")
	private Long layer6Number;

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

	public String getMaterCode() {
		return materCode;
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
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


	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public String getProLine() {
		return proLine;
	}

	public void setProLine(String proLine) {
		this.proLine = proLine;
	}

	public String getProBrand() {
		return proBrand;
	}

	public void setProBrand(String proBrand) {
		this.proBrand = proBrand;
	}

	public String getCproPowcap() {
		return cproPowcap;
	}

	public void setCproPowcap(String cproPowcap) {
		this.cproPowcap = cproPowcap;
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
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getCatalogPrice() {
		return catalogPrice;
	}

	public void setCatalogPrice(BigDecimal catalogPrice) {
		this.catalogPrice = catalogPrice;
	}

	public BigDecimal getCrossMargin() {
		return crossMargin;
	}

	public void setCrossMargin(BigDecimal crossMargin) {
		this.crossMargin = crossMargin;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}


	public BigDecimal getLayer1Price() {
		return layer1Price;
	}

	public void setLayer1Price(BigDecimal layer1Price) {
		this.layer1Price = layer1Price;
	}

	public BigDecimal getLayer1Discount() {
		return layer1Discount;
	}

	public void setLayer1Discount(BigDecimal layer1Discount) {
		this.layer1Discount = layer1Discount;
	}

	public Long getLayer1Number() {
		return layer1Number;
	}

	public void setLayer1Number(Long layer1Number) {
		this.layer1Number = layer1Number;
	}

	public BigDecimal getLayer2Price() {
		return layer2Price;
	}

	public void setLayer2Price(BigDecimal layer2Price) {
		this.layer2Price = layer2Price;
	}

	public BigDecimal getLayer2Discount() {
		return layer2Discount;
	}

	public void setLayer2Discount(BigDecimal layer2Discount) {
		this.layer2Discount = layer2Discount;
	}

	public Long getLayer2Number() {
		return layer2Number;
	}

	public void setLayer2Number(Long layer2Number) {
		this.layer2Number = layer2Number;
	}

	public BigDecimal getLayer3Price() {
		return layer3Price;
	}

	public void setLayer3Price(BigDecimal layer3Price) {
		this.layer3Price = layer3Price;
	}

	public BigDecimal getLayer3Discount() {
		return layer3Discount;
	}

	public void setLayer3Discount(BigDecimal layer3Discount) {
		this.layer3Discount = layer3Discount;
	}

	public Long getLayer3Number() {
		return layer3Number;
	}

	public void setLayer3Number(Long layer3Number) {
		this.layer3Number = layer3Number;
	}

	public BigDecimal getLayer4Price() {
		return layer4Price;
	}

	public void setLayer4Price(BigDecimal layer4Price) {
		this.layer4Price = layer4Price;
	}

	public BigDecimal getLayer4Discount() {
		return layer4Discount;
	}

	public void setLayer4Discount(BigDecimal layer4Discount) {
		this.layer4Discount = layer4Discount;
	}

	public Long getLayer4Number() {
		return layer4Number;
	}

	public void setLayer4Number(Long layer4Number) {
		this.layer4Number = layer4Number;
	}

	public BigDecimal getLayer5Price() {
		return layer5Price;
	}

	public void setLayer5Price(BigDecimal layer5Price) {
		this.layer5Price = layer5Price;
	}

	public BigDecimal getLayer5Discount() {
		return layer5Discount;
	}

	public void setLayer5Discount(BigDecimal layer5Discount) {
		this.layer5Discount = layer5Discount;
	}

	public Long getLayer5Number() {
		return layer5Number;
	}

	public void setLayer5Number(Long layer5Number) {
		this.layer5Number = layer5Number;
	}

	public BigDecimal getLayer6Price() {
		return layer6Price;
	}

	public void setLayer6Price(BigDecimal layer6Price) {
		this.layer6Price = layer6Price;
	}

	public BigDecimal getLayer6Discount() {
		return layer6Discount;
	}

	public void setLayer6Discount(BigDecimal layer6Discount) {
		this.layer6Discount = layer6Discount;
	}

	public Long getLayer6Number() {
		return layer6Number;
	}

	public void setLayer6Number(Long layer6Number) {
		this.layer6Number = layer6Number;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getVmaterCode() {
		return vmaterCode;
	}

	public void setVmaterCode(String vmaterCode) {
		this.vmaterCode = vmaterCode;
	}
}
