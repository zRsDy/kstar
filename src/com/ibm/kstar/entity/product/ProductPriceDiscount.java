package com.ibm.kstar.entity.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 价格表批发价
 * @author chao.chen
 *
 */
@Entity
@Table(name = "CRM_T_PRICE_LINE_DISCOUNT")
public class ProductPriceDiscount implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String WHOLESALE_TYPE = "wholesale"; // 批发
	public static final String DISCOUNT_TYPE = "discount"; // 折扣
	
	
	@Id
	@GeneratedValue(generator = "priceDiscount_id_generator")
	@GenericGenerator(name = "priceDiscount_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	
	/**
	 * 价格表ID
	 */
	@Column(name = "C_PRICE_HEAD_ID")
	private String headId;
	
	/**
	 * 类型
	 */
	@Column(name = "C_TYPE")
	private String type;
	
	/**
	 * 批发价格
	 */
	@Column(name = "C_PRICE")
	private BigDecimal prive;
	
	/**
	 * 批发数量
	 */
	@Column(name = "C_QUANTITY")
	private BigDecimal quantity;
	
	/**
	 * 折扣率
	 */
	@Column(name = "C_RATIO")
	private BigDecimal retio;
	
	/**
	 * 折扣金额 -- 废弃
	 */
	@Column(name = "C_SUM")
	private BigDecimal sum; 
	
	/**
	 * 层级排序
	 */
	@Column(name = "C_ORDER_NO")
	private Integer orderNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrive() {
		return prive;
	}

	public void setPrive(BigDecimal prive) {
		this.prive = prive;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getRetio() {
		return retio;
	}

	public void setRetio(BigDecimal retio) {
		this.retio = retio;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
