package com.ibm.kstar.entity.bizopp;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.product.KstarProduct;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商机配置
 * @author gaoyuliang  2017-2-22
 * 
 */
@Entity
@Table(name = "crm_t_bizopp_products_detail")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetail implements Serializable {
	
	/** 版本号 */
	private static final long serialVersionUID = -815585995024912962L;
	
	/** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "productdetail_pid_generator")
	@GenericGenerator(name="productdetail_pid_generator", strategy="uuid")
    private String id;
    
    /** 商机ID */
    @Column(name = "c_bizopp_id", nullable = true, length = 32)
    private String bizOppId;

    @Transient
	private String bizOppName;

	public String getBizOppName() {
		return bizOppName;
	}

	public void setBizOppName(String bizOppName) {
		this.bizOppName = bizOppName;
	}

	/** 产品ID */
    @Column(name = "c_product_id", nullable = true, length = 32)
    private String productId;
    
    /** 产品名称 */
    @Column(name = "c_product_name", nullable = true, length = 50)
    private String productName;
    
    /** 产品类别 */
    @Column(name = "c_product_type", nullable = true, length = 30)
    private String productType;
    
    /** 产品型号 */
    @Column(name = "c_product_model", nullable = true, length = 30)
    private String productModel;
    
    /** 预计数量 */
    @Column(name = "n_plan_count", nullable = true, length = 22)
    private Double planCount;
    
    /** 预计价格 */
    @Column(name = "n_plan_price", nullable = true)
    private BigDecimal planPrice;
    
    /** 预计金额 */
    @Column(name = "n_plan_total", nullable = true)
    private BigDecimal planTotal;

    @Column(name = "public_Price")
	private BigDecimal publicPrice;

	public BigDecimal getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(BigDecimal publicPrice) {
		this.publicPrice = publicPrice;
	}

	/** 备注 */
    @Column(name = "c_remark")
    private String remark;
    
    /** 是否标准 */
    @Column(name = "n_is_standard", nullable = true, length = 22)
    private Integer isStandard;

	@Column(name = "crmCategory")
	private String crmCategory;

	@Column(name = "crmCategoryLable")
	private String crmCategoryLable;

	@Column(name = "cproCategory")
	private String cproCategory;

	//销售产品分类名称
	@Column(name = "DISPLAY_CATALOG_NAME")
	private String displayCatalogName;

	@Column(name = "cproPowcap")
	private String cproPowcap;

	@Column(name = "source_id")
	private String sourceId; // 变更前ID 

	/**
	 * 产品
	 */
	@Transient
	private KstarProduct product;
	
	
    public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public String getCproPowcap() {
		return cproPowcap;
	}

	public void setCproPowcap(String cproPowcap) {
		this.cproPowcap = cproPowcap;
	}

	public String getCrmCategory() {
		return crmCategory;
	}

	public void setCrmCategory(String crmCategory) {
		this.crmCategory = crmCategory;
	}

	public String getCrmCategoryLable() {
		return crmCategoryLable;
	}

	public void setCrmCategoryLable(String crmCategoryLable) {
		this.crmCategoryLable = crmCategoryLable;
	}

	public String getCproCategory() {
		return cproCategory;
	}

	public void setCproCategory(String cproCategory) {
		this.cproCategory = cproCategory;
	}

	@Transient
	private String productSeries;

	public String getProductSeries() {
		return productSeries;
	}

	public void setProductSeries(String productSeries) {
		this.productSeries = productSeries;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		//return productName;
		return CacheUtils.getProductName(this.getProductId());
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductModel() {
		//return productModel;
		return CacheUtils.getProductModel(this.getProductId());
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public Double getPlanCount() {
		return planCount;
	}

	public void setPlanCount(Double planCount) {
		this.planCount = planCount;
	}

	public BigDecimal getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(BigDecimal planPrice) {
		this.planPrice = planPrice;
	}
	
	public BigDecimal getPlanTotal() {
		return planTotal;
	}

	public void setPlanTotal(BigDecimal planTotal) {
		this.planTotal = planTotal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(Integer isStandard) {
		this.isStandard = isStandard;
	}

	/**
	 * 获取产品
	 * @return
	 */
	public KstarProduct getProduct() {
		if (this.product != null) {
			return this.product;
		}
		this.product = CacheUtils.getProduct(this.productId);
		if (this.product == null) {
			this.product = new KstarProduct();
		}
		return product;
	}

	public void setProduct(KstarProduct product) {
		this.product = product;
	}
	
	// 产品描述
	public String getProDesc(){
		return getProduct().getProDesc();
	}
	
	// 物料号
	public String getMaterCode(){
		return getProduct().getMaterCode();
	}

	public String getDisplayCatalogName() {
		return displayCatalogName;
	}

	public void setDisplayCatalogName(String displayCatalogName) {
		this.displayCatalogName = displayCatalogName;
	}
	
}
