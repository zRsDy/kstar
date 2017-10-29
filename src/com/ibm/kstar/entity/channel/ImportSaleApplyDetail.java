package com.ibm.kstar.entity.channel;


import com.ibm.kstar.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 引入销量明细表(crm_t_import_sale_apply_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-10
 */
@Entity
@Table(name = "crm_t_import_sale_apply_detail")
public class ImportSaleApplyDetail extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -8408995569323796737L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "import_apply_detail_c_pid_generator")
   	@GenericGenerator(name="import_apply_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_id")
    private String applyId;
    
    /** 订单单号 */
    @Column(name = "c_order_code")
    private String orderCode;

	/** 订单数量  */
	@Column(name = "I_Order_Quantity")
	private Integer orderQuantity;

	/** 订单行号 */
	@Column(name = "I_Order_line_no")
	private String orderLineNo;

	/** 产品系列 */
    @Column(name = "c_product_series")
    private String productSeries;
    
    /** 产品型号 */
    @Column(name = "c_product_kind")
    private String productKind;

    /** 物料号 */
    @Column(name = "c_materiel_code")
    private String materielCode;
	
	/** 引入日期 */
    @Column(name = "c_import_date")
    private Date importDate;

    /** 引入数量 */
	@Column(name = "c_import_quantity")
	private BigDecimal importQuantity;
	
	/** 单价 */
    @Column(name = "c_import_price")
    private BigDecimal importPrice;
    
	/** 金额 */
    @Column(name = "c_import_amount")
    private BigDecimal importAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String purchaseCode) {
		this.orderCode = purchaseCode;
	}

	public String getProductSeries() {
		return productSeries;
	}

	public void setProductSeries(String productSeries) {
		this.productSeries = productSeries;
	}

	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public String getProductKindName() {
		return this.getLovName(productKind);
	}
	
	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public BigDecimal getImportQuantity() {
		return importQuantity;
	}

	public void setImportQuantity(BigDecimal importQuantity) {
		this.importQuantity = importQuantity;
	}

	public BigDecimal getImportPrice() {
		return importPrice;
	}

	public void setImportPrice(BigDecimal importPrice) {
		this.importPrice = importPrice;
	}

	public BigDecimal getImportAmount() {
		return importAmount;
	}

	public void setImportAmount(BigDecimal importAmount) {
		this.importAmount = importAmount;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getOrderLineNo() {
		return orderLineNo;
	}

	public void setOrderLineNo(String orderLineNo) {
		this.orderLineNo = orderLineNo;
	}
}