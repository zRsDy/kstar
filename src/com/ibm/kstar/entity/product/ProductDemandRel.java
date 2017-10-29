package com.ibm.kstar.entity.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 需求单与产品关联表
 * 
 * @author lhm
 * @version 1.0.0 2017-05-10
 */
@Entity
@Table(name = "CRM_T_PRODUCT_DEMAND_REL")
public class ProductDemandRel extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -1627065857046096404L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "pro_demand_rel_id_generator")
   	@GenericGenerator(name="pro_demand_rel_id_generator", strategy="uuid")
    private String id;
    
    /** 产品id */
    @Column(name = "c_product_id")
    private String productId;
    
    /** 需求单id */
    @Column(name = "c_demand_id")
    private String demandId;
    
    /** 需求数量 */
 	@Column(name="N_DEMANT_NUMBER")
 	private Long demandNumber;
 	
	/** 是否提前备料 */
	@Column(name="C_PREPARE_BEFORE")
	private String prepareBefore;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public Long getDemandNumber() {
		return demandNumber;
	}

	public void setDemandNumber(Long demandNumber) {
		this.demandNumber = demandNumber;
	}

	public String getPrepareBefore() {
		return prepareBefore;
	}

	public void setPrepareBefore(String prepareBefore) {
		this.prepareBefore = prepareBefore;
	}

}