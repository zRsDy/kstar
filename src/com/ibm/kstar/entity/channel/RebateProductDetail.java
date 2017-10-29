package com.ibm.kstar.entity.channel;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 返利产品组明细表(crm_t_rebate_product_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-15
 */
@Entity
@Table(name = "crm_t_rebate_product_detail")
public class RebateProductDetail extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 4079427362412651283L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "rebate_product_detail_c_pid_generator")
   	@GenericGenerator(name="rebate_product_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 产品组id */
    @Column(name = "c_product_group_id")
    private String productGroupId;

    /** 产品系列 */
    @Column(name = "c_product_series")
    private String productSeries;
    
    /** 失效日期*/
    @Column(name = "c_endDate")
    private Date endDate;
    
    @Transient
    private Integer rowNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(String productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getProductSeries() {
		return productSeries;
	}

	public void setProductSeries(String productSeries) {
		this.productSeries = productSeries;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

}