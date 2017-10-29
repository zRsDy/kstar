package com.ibm.kstar.entity.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 价格表头
 * @author chao.chen
 *
 */
@Entity
@Table(name = "CRM_T_PRICE_HEAD")
@Permission(businessType = ProcessConstants.PRODUCT_PRICE_HEAD)
public class ProductPriceHead extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 价格头表ID
	@Id
	@GeneratedValue(generator = "kstarpriceh_id_generator")
	@GenericGenerator(name="kstarpriceh_id_generator", strategy="uuid")
	@Column(name="C_PID")
	@PermissionBusinessId
	private String id;
	
	// 价格表名称
	@Column(name = "C_PRICE_HEAD_NAME")
	private String priceTableName;
	
	// 是否客户价格
	@Column(name = "C_CLIENT_PRICE")
	private String clientPrice;
	
	// 是否统一标准价格
	@Column(name = "C_COMMON_PRICE")
	private String commonPrice;	//存字典code
	
	// 是否批发价 1=是，0=否
	@Column(name = "C_is_Wholesale")
	private String isWholesale;
	
	// 是否优惠折扣  1=是，0=否
	@Column(name = "C_is_discount")
	private String isDiscount;
	
	// 客户
	@Column(name = "C_CLIENT_ID")
	private String clientId;
	
	@Transient
	private String clientName;
	
	// 货币
	@Column(name = "C_CURRENCY")
	private String currency;
	
	// 有效期结束时间
	@Column(name = "DT_START_DATE")
	private Date startDate;
	
	// 有效期结束时间
	@Column(name = "DT_END_DATE")
	private Date endDate;
	
	// 组织
	@Column(name = "C_ORGANIZATION")
	private String organization;
	
	// 目录价格   -- 是
	@Transient
	private BigDecimal catalogPrice;
	
	// 说明
	@Column(name = "C_DESC")
	private String comments;
	
	//创建组织
	@Column(name = "c_create_org")
	private String createOrg;
	
	//共享组织名称
	@Column(name = "c_share_create_org_name")
	private String shareCreateOrgName;
	
	//共享组织ID
	@Column(name = "c_share_create_org_id")
	private String shareCreateOrgId;
	
	//创建人
	@Column(name = "c_creater")
	private String creater;
	
	@Transient
	private String createrName;
	
	//行业大类
	@Transient
	private String customCategory;
	@Transient
	private String customCategoryName;
	//行业小类
	@Transient
	private String customCategorySub;
	@Transient
	private String customCategorySubName;
	
	//创建时间
	@Column(name = "c_create_date")
	private Date createDate;

	@Transient
	private List<ProductPriceDiscount> discounts = new ArrayList<>();
	
	public List<ProductPriceDiscount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<ProductPriceDiscount> discounts) {
		this.discounts = discounts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPriceTableName() {
		return priceTableName;
	}

	public void setPriceTableName(String priceTableName) {
		this.priceTableName = priceTableName;
	}

	public String getClientPrice() {
		return clientPrice;
	}

	public void setClientPrice(String clientPrice) {
		this.clientPrice = clientPrice;
	}

	public String getCommonPrice() {
		return commonPrice;
	}

	public void setCommonPrice(String commonPrice) {
		this.commonPrice = commonPrice;
	}

	public String getCommonPriceName() {
		return this.getLovMember("NY", commonPrice).getName();
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyName() {
		return this.getLovName(currency);
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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationName() {
		return this.getLovName(organization);
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public BigDecimal getCatalogPrice() {
		return catalogPrice;
	}

	public void setCatalogPrice(BigDecimal catalogPrice) {
		this.catalogPrice = catalogPrice;
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

	public String getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(String isDiscount) {
		this.isDiscount = isDiscount;
	}

	public String getIsWholesale() {
		return isWholesale;
	}

	public void setIsWholesale(String isWholesale) {
		this.isWholesale = isWholesale;
	}

	public String getCustomCategory() {
		return customCategory;
	}

	public void setCustomCategory(String customCategory) {
		this.customCategory = customCategory;
	}

	public String getCustomCategorySub() {
		return customCategorySub;
	}

	public void setCustomCategorySub(String customCategorySub) {
		this.customCategorySub = customCategorySub;
	}

	public String getCustomCategoryName() {
		return customCategoryName;
	}

	public void setCustomCategoryName(String customCategoryName) {
		this.customCategoryName = customCategoryName;
	}

	public String getCustomCategorySubName() {
		return customCategorySubName;
	}

	public void setCustomCategorySubName(String customCategorySubName) {
		this.customCategorySubName = customCategorySubName;
	}

	public String getShareCreateOrgName() {
		return shareCreateOrgName;
	}

	public void setShareCreateOrgName(String shareCreateOrgName) {
		this.shareCreateOrgName = shareCreateOrgName;
	}

	public String getShareCreateOrgId() {
		return shareCreateOrgId;
	}

	public void setShareCreateOrgId(String shareCreateOrgId) {
		this.shareCreateOrgId = shareCreateOrgId;
	}


}
