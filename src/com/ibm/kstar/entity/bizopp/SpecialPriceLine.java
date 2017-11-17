package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibm.kstar.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author gaoyuliang  2017-3-7
 * 
 */
@Entity
@Table(name = "crm_t_biz_spe_price_app_line")
public class SpecialPriceLine implements Serializable {

    /** 版本号 */
    private static final long serialVersionUID = 420513581402759777L;
    
    /** primary key */
	@Id
	@GeneratedValue(generator = "crm_t_biz_spe_price_app_line_id_generator")
	@GenericGenerator(name="crm_t_biz_spe_price_app_line_id_generator", strategy="uuid")
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	private String id;
    
    /** 特价表主键 */
    @Column(name = "c_spe_price_id", nullable = true, length = 32)
    private String spePriceId;
    
    /** 产品型号 */
    @Column(name = "c_product_model", nullable = true, length = 32)
    private String productModel;
    
    /** 料号 */
    @Column(name = "c_material_number", nullable = true, length = 32)
    private String materialNumber;
    
    /** 物料名称 */
    @Column(name = "c_material_name", nullable = true, length = 80)
    private String materialName;
    
    /** 目录价格 */
    @Column(name = "n_cataloge_price", nullable = true)
    private BigDecimal catalogePrice;
    
    /** 数量 */
    @Column(name = "n_count", nullable = true, length = 22)
    private Integer count;
    
    /** 申请数量 */
    @Column(name = "n_apply_count", nullable = true, length = 22)
    private Integer applyCount;
    
    /** 申请折扣 */
    @Column(name = "n_apply_discount", nullable = true)
    private BigDecimal applyDiscount;
    
    /** 批复折扣 */
    @Column(name = "n_approved_discount", nullable = true)
    private BigDecimal approvedDiscount;
    
    /** 折扣价 */
    @Column(name = "n_discount_price", nullable = true)
    private BigDecimal discountPrice;
    
    /** 折扣总额 */
    @Column(name = "n_discount_total", nullable = true)
    private BigDecimal discountTotal;
    
    /** 备注 */
    @Column(name = "c_remark", nullable = true, length = 80)
    private String remark;

	@Column(name =  "PRODUCT_ID")
	private String productId;

	@Column(name =  "SOURCE_BIZ_IDS")
	private String sourceBizIds;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSourceBizIds() {
		return sourceBizIds;
	}

	public void setSourceBizIds(String sourceBizIds) {
		this.sourceBizIds = sourceBizIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpePriceId() {
		return spePriceId;
	}

	public void setSpePriceId(String spePriceId) {
		this.spePriceId = spePriceId;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public BigDecimal getCatalogePrice() {
		return catalogePrice;
	}

	public void setCatalogePrice(BigDecimal catalogePrice) {
		this.catalogePrice = catalogePrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	public BigDecimal getApplyDiscount() {
		return applyDiscount;
	}

	public void setApplyDiscount(BigDecimal applyDiscount) {
		this.applyDiscount = applyDiscount;
	}

	public BigDecimal getApprovedDiscount() {
		return approvedDiscount;
	}

	public void setApprovedDiscount(BigDecimal approvedDiscount) {
		this.approvedDiscount = approvedDiscount;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public BigDecimal getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(BigDecimal discountTotal) {
		this.discountTotal = discountTotal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
