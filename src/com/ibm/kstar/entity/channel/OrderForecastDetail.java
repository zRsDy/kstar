package com.ibm.kstar.entity.channel;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 下单预测明细表(crm_t_order_forecast_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-09
 */
@Entity
@Table(name = "crm_t_order_forecast_detail")
public class OrderForecastDetail extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 8338108605270625747L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "order_forecast_detail_c_pid_generator")
   	@GenericGenerator(name="order_forecast_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 预测单号 */
    @Column(name = "c_forecast_id")
    private String forecastId;
    
    /** 产品系列 */
    @Column(name = "c_product_series")
    private String productSeries;
    
    /** 产品型号 */
    @Column(name = "c_product_kind")
    private String productKind;

    /** 物料号 */
    @Column(name = "c_materiel_code")
    private String materielCode;
    
    /** 客户 */
    @Column(name = "c_customer")
    private String customer;
    
    @Transient
    private String customerName;
    
    /** 第一周数量 */
	@Column(name = "c_first_week_quantity")
	private Integer firstWeekQuantity;
	
	/** 第二周数量 */
	@Column(name = "c_second_week_quantity")
	private Integer secondWeekQuantity;
	
	/** 第三周数量 */
	@Column(name = "c_third_week_quantity")
	private Integer thirdWeekQuantity;
	
	/** 第四周数量 */
	@Column(name = "c_fourth_week_quantity")
	private Integer fourthWeekQuantity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForecastId() {
		return forecastId;
	}

	public void setForecastId(String forecastId) {
		this.forecastId = forecastId;
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

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getFirstWeekQuantity() {
		return firstWeekQuantity;
	}

	public void setFirstWeekQuantity(Integer firstWeekQuantity) {
		this.firstWeekQuantity = firstWeekQuantity;
	}

	public Integer getSecondWeekQuantity() {
		return secondWeekQuantity;
	}

	public void setSecondWeekQuantity(Integer secondWeekQuantity) {
		this.secondWeekQuantity = secondWeekQuantity;
	}

	public Integer getThirdWeekQuantity() {
		return thirdWeekQuantity;
	}

	public void setThirdWeekQuantity(Integer thirdWeekQuantity) {
		this.thirdWeekQuantity = thirdWeekQuantity;
	}

	public Integer getFourthWeekQuantity() {
		return fourthWeekQuantity;
	}

	public void setFourthWeekQuantity(Integer fourthWeekQuantity) {
		this.fourthWeekQuantity = fourthWeekQuantity;
	}

}