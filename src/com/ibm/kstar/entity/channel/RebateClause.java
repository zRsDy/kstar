package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 返利条款表(crm_t_rebate_clause)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-14
 */
@Entity
@Table(name = "crm_t_rebate_clause")
public class RebateClause extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -1471256746216927139L;

	
	public RebateClause() {
	}

	public RebateClause(RebateClause c,RebateProduct p) {
		this.id = c.id;
		this.policyId = c.policyId;
		this.productGroup = c.productGroup;
		this.productGroupName = c.productGroupName;
		this.referenceQuantity = c.referenceQuantity;
		this.leastReturnRate = c.leastReturnRate;
		this.finishRate = c.finishRate;
		this.rebateRate = c.rebateRate;
		this.finishRate2 = c.finishRate2;
		this.rebateRate2 = c.rebateRate2;
		this.rebateRatio = c.rebateRatio;
		this.yearNoGrowth = c.yearNoGrowth;
		this.excessRebate = c.excessRebate;
		this.remark = c.remark;
		this.productGroupName = p.getGroupName();
	}

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "rebate_clause_c_pid_generator")
   	@GenericGenerator(name="rebate_clause_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 政策id */
    @Column(name = "c_policy_id")
    private String policyId;

    /** 产品组 */
    @Column(name = "c_product_group")
    private String productGroup;
    
    @Transient
    private String productGroupName;
    
    /** 基准量 */
    @Column(name = "c_reference_quantity")
    private Long referenceQuantity;

    /** 至少回款率*/
    @Column(name = "c_least_return_rate")
    private BigDecimal leastReturnRate;
    
    /** 完成率*/
    @Column(name = "c_finish_rate")
    private BigDecimal finishRate;
    
    /** 返利比例（%） */
	@Column(name = "c_rebate_rate")
	private BigDecimal rebateRate;
    
    /** 完成率2*/
    @Column(name = "c_finish_rate2")
    private BigDecimal finishRate2;
    
    /** 返利比例2（%） */
	@Column(name = "c_rebate_rate2")
	private BigDecimal rebateRate2;
    
    /** 返利系数（%） */
	@Column(name = "c_rebate_retio")
	private BigDecimal rebateRatio;
    
    /** 同比增长率（%） */
	@Column(name = "c_year_no_growth")
	private BigDecimal yearNoGrowth;
    
    /** 超额返利 （%） */
	@Column(name = "c_excess_rebate")
	private BigDecimal excessRebate;
    
    /** 备注*/
    @Column(name = "c_remark")
    private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public Long getReferenceQuantity() {
		return referenceQuantity;
	}

	public void setReferenceQuantity(Long referenceQuantity) {
		this.referenceQuantity = referenceQuantity;
	}

	public BigDecimal getLeastReturnRate() {
		return leastReturnRate;
	}

	public void setLeastReturnRate(BigDecimal leastReturnRate) {
		this.leastReturnRate = leastReturnRate;
	}

	public BigDecimal getFinishRate() {
		return finishRate;
	}

	public void setFinishRate(BigDecimal finishRate) {
		this.finishRate = finishRate;
	}

	public BigDecimal getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(BigDecimal rebateRate) {
		this.rebateRate = rebateRate;
	}

	public BigDecimal getFinishRate2() {
		return finishRate2;
	}

	public void setFinishRate2(BigDecimal finishRate2) {
		this.finishRate2 = finishRate2;
	}

	public BigDecimal getRebateRate2() {
		return rebateRate2;
	}

	public void setRebateRate2(BigDecimal rebateRate2) {
		this.rebateRate2 = rebateRate2;
	}

	public BigDecimal getRebateRatio() {
		return rebateRatio;
	}

	public void setRebateRatio(BigDecimal rebateRatio) {
		this.rebateRatio = rebateRatio;
	}

	public BigDecimal getYearNoGrowth() {
		return yearNoGrowth;
	}

	public void setYearNoGrowth(BigDecimal yearNoGrowth) {
		this.yearNoGrowth = yearNoGrowth;
	}

	public BigDecimal getExcessRebate() {
		return excessRebate;
	}

	public void setExcessRebate(BigDecimal excessRebate) {
		this.excessRebate = excessRebate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}