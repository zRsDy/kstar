package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.product.KstarProduct;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 特价产品明细
 * Created by wangchao on 2017/5/10.
 */
@Entity
@Table(name = "CRM_T_REBATE_LINE")
public class RebateLine implements Serializable{

    private static final long serialVersionUID = 1L;
    @Column(name = "row_id")
    @Id
    @GeneratedValue(generator = "rebate_line_id_generator")
    @GenericGenerator(name = "rebate_line_id_generator", strategy = "uuid")
    private String id;

    @Column(name = "rebate_id")
    private String rebateId;
    @Column(name = "product_id")
    private String productId;
    
    @Column(name = "apply_qty")
    private Double applyQty; // 申请数量
    
    @Column(name = "source_price")
    private Double sourcePrice;
    @Column(name = "apply_rebate")
    private Double applyRebate;
    @Column(name = "approve_rebate")
    private Double approveRebate;
    @Column(name = "approve_price")
    private Double approvePrice;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "biz_id")
    private String bizId;
    @Column(name = "biz_name")
    private String bizName;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_name")
    private String clientName;
    
    @Column(name = "order_qty")
    private Double orderQty; // 已下单数量
    
    @Column(name = "remark")
    private String remark;
    @Column(name = "product_model")
    private String productModel;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "productName")
    private String productName;
    @Column(name = "cproPowcap")
    private String cproPowcap;
    
    @Column(name = "isBiz")
    private String isBiz; // 商机配置ID

    /**
     * 无效字段
     * 由于报价已修改为按产品型号来进行数量计算的
     * 故同类型的产品第一行为商机中该类型的全部数量,其他的0。
     */
    @Deprecated
    @Column(name = "bizQty")
    private Double bizQty;
    @Column(name = "apply_amount")
    private Double applyAmount;
    
    @Column(name = "old_id")
    private String oldId; // 特价变更，对应变更前数据的ID
   
    @Column(name = "edit_By_Display_Catalog_Name")
    private String editByDisplayCatalogName; // 根据销售类型判断是否可编辑
    
    // 金牌价格
    @Column(name = "catalog_price")
    private Double catalogPrice;

    
    public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	/**
	 * 产品
	 */
	@Transient
	private KstarProduct product;
    
    
    public Double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Double getBizQty() {
        return bizQty;
    }

    public void setBizQty(Double bizQty) {
        this.bizQty = bizQty;
    }

    public String getIsBiz() {
        return isBiz;
    }

    public void setIsBiz(String isBiz) {
        this.isBiz = isBiz;
    }

    public String getProductName() {
        //return productName;
    	return CacheUtils.getProductName(this.getProductId());
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCproPowcap() {
        return cproPowcap;
    }

    public void setCproPowcap(String cproPowcap) {
        this.cproPowcap = cproPowcap;
    }

    /**
     * 代理商申请价格
     * @return
     */
    @Column(name = "apply_price")
    private Double applyPrice;

    @Column(name = "product_type")
    private String productType;
    
    /**
     * CRM销售产品分类ID
     */
    @Column(name = "product_sort_id")
    private String productSortId;

    /**
     * CRM销售产品分类名称
     */
    @Column(name = "product_sort_Name")
    private String productSortName;
    
    //销售产品分类名称(增加行——SQL查询得来的值)
  	@Column(name = "DISPLAY_CATALOG_NAME")
  	private String displayCatalogName;
    
    @Column(name = "apply_yo")
    private Double applyYo;
    @Column(name = "approve_Yo")
    private Double approveYo;

    public Double getApplyYo() {
        return applyYo;
    }

    public void setApplyYo(Double applyYo) {
        this.applyYo = applyYo;
    }

    public Double getApproveYo() {
        return approveYo;
    }

    public void setApproveYo(Double approveYo) {
        this.approveYo = approveYo;
    }

    public Double getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(Double applyPrice) {
        this.applyPrice = applyPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRebateId() {
        return rebateId;
    }

    public void setRebateId(String rebateId) {
        this.rebateId = rebateId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(Double applyQty) {
        this.applyQty = applyQty;
    }

    public Double getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(Double sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public Double getApplyRebate() {
        return applyRebate;
    }

    public void setApplyRebate(Double applyRebate) {
        this.applyRebate = applyRebate;
    }

    public Double getApproveRebate() {
        return approveRebate;
    }

    public void setApproveRebate(Double approveRebate) {
        this.approveRebate = approveRebate;
    }

    public Double getApprovePrice() {
        return approvePrice;
    }

    public void setApprovePrice(Double approvePrice) {
        this.approvePrice = approvePrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
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

    public Double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Double orderQty) {
        this.orderQty = orderQty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProductModel() {
        //return productModel;
    	return CacheUtils.getProductModel(this.getProductId());
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

	public Double getCatalogPrice() {
		return catalogPrice;
	}

	public void setCatalogPrice(Double catalogPrice) {
		this.catalogPrice = catalogPrice;
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

	public String getProductSortId() {
		return productSortId;
	}

	public void setProductSortId(String productSortId) {
		this.productSortId = productSortId;
	}

	public String getProductSortName() {
		return productSortName;
	}

	public void setProductSortName(String productSortName) {
		this.productSortName = productSortName;
	}

	public String getDisplayCatalogName() {
		return displayCatalogName;
	}

	public void setDisplayCatalogName(String displayCatalogName) {
		this.displayCatalogName = displayCatalogName;
	}

	public String getEditByDisplayCatalogName() {
		return editByDisplayCatalogName;
	}

	public void setEditByDisplayCatalogName(String editByDisplayCatalogName) {
		this.editByDisplayCatalogName = editByDisplayCatalogName;
	}

	
	
}
