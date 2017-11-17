package com.ibm.kstar.entity.channel;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 交货预测明细表(crm_t_delivery_forecast_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-09
 */
@Entity
@Table(name = "crm_t_delivery_forecast_detail")
public class DeliveryForecastDetail extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 7403063080577169431L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "delivery_forecast_detail_c_pid_generator")
   	@GenericGenerator(name="delivery_forecast_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 下单预测明细id */
    @Column(name = "c_order_detail_id")
    private String orderDetailId;
    
    /** 交货日期 */
    @Column(name = "c_delivery_date")
    private Date deliveryDate;

    /** 交货数量 */
    @Column(name = "c_delivery_quantity")
    private Integer deliveryQuantity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getDeliveryQuantity() {
		return deliveryQuantity;
	}

	public void setDeliveryQuantity(Integer deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}

}