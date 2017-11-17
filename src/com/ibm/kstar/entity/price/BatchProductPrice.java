package com.ibm.kstar.entity.price;

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
import com.ibm.kstar.entity.product.KstarProductLine;
import com.ibm.kstar.entity.product.ProductPriceHead;

/**
 * 产品批量调价表(crm_t_batch_product_price)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-16
 */

@Entity
@Table(name = "crm_t_batch_product_price")
public class BatchProductPrice extends BaseEntity implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = -1114857657979610534L;

	@Id
	@Column(name = "c_pid", unique = true, nullable = false)
	@GeneratedValue(generator = "batch_product_price_id_generator")
	@GenericGenerator(name="batch_product_price_id_generator", strategy="uuid")
	private String id;
	
	/** 价格头表ID */
	@Column(name = "c_price_head_id")
	private String priceHeadId;
	
	@Transient
	private ProductPriceHead priceHead;
	
	/** 产品类型 */
	@Column(name = "c_product_type")
	private String productType;
	
	/** 产品分类 */
	@Column(name = "c_product_sort_id")
	private String productSortId;
	
	@Transient
	private KstarProductLine productSort;
	
	/** 调整类型 */
	@Column(name = "c_adjust_type")
	private String adjustType;
	
	/** 调整值 */
	@Column(name = "c_adjust_value")
	private BigDecimal adjustValue;
	
	/** 调整状态 */
	@Column(name = "c_adjust_status")
	private String adjustStatus;
	
	/** 调整日期 */
	@Column(name = "c_adjust_date")
	private Date adjustDate;

	//创建组织
	@Column(name = "c_create_org")
	private String createOrg;
	
	//创建人
	@Column(name = "c_creater")
	private String creater;
	
	@Transient
	private String createrName;
	
	//创建时间
	@Column(name = "c_create_date")
	private Date createDate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPriceHeadId() {
		return priceHeadId;
	}

	public void setPriceHeadId(String priceHeadId) {
		this.priceHeadId = priceHeadId;
	}

	public ProductPriceHead getPriceHead() {
		return priceHead;
	}

	public void setPriceHead(ProductPriceHead priceHead) {
		this.priceHead = priceHead;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductTypeName() {
		return this.getLovName(productType);
	}

	public String getProductSortId() {
		return productSortId;
	}

	public void setProductSortId(String productSortId) {
		this.productSortId = productSortId;
	}

	public KstarProductLine getProductSort() {
		return productSort;
	}

	public void setProductSort(KstarProductLine productSort) {
		this.productSort = productSort;
	}

	public String getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}

	public String getAdjustTypeName() {
		return this.getLovName(adjustType);
	}
	
	public BigDecimal getAdjustValue() {
		return adjustValue;
	}

	public void setAdjustValue(BigDecimal adjustValue) {
		this.adjustValue = adjustValue;
	}

	public String getAdjustStatus() {
		return adjustStatus;
	}

	public void setAdjustStatus(String adjustStatus) {
		this.adjustStatus = adjustStatus;
	}
	
	public Date getAdjustDate() {
		return adjustDate;
	}

	public void setAdjustDate(Date adjustDate) {
		this.adjustDate = adjustDate;
	}

	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String getCreateOrgName() {
		return this.getLovName(createOrg);
	}
	
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
