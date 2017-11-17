package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 返利明细表(crm_t_rebate_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-15
 */
@Entity
@Table(name = "crm_t_rebate_detail")
public class RebateDetail extends BaseEntity implements java.io.Serializable {
	
    /** 版本号 */
	private static final long serialVersionUID = 4583467942645081293L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "rebate_detail_c_pid_generator")
   	@GenericGenerator(name="rebate_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 政策编号 */
    @Column(name = "c_policy_id")
    private String policyId;
    
    /** 应计金额*/
    @Column(name = "c_accrued_money")
    private BigDecimal accruedMoney;
    
    /** 确认金额*/
    @Column(name = "c_check_money")
    private BigDecimal checkMoney;
    
    /** 金额差异原因*/
    @Column(name = "c_difference_reason")
    private String differenceReason;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    /** 创建日期 */
    @Column(name = "c_create_date")
    private Date createDate;
    
    /** 创建人 */
    @Column(name = "c_creater")
    private String creater;
    
    /** 出货申请行id */
    @Column(name = "c_delivery_line_id")
    private String deliveryLineId;

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

	public BigDecimal getAccruedMoney() {
		return accruedMoney;
	}

	public void setAccruedMoney(BigDecimal accruedMoney) {
		this.accruedMoney = accruedMoney;
	}

	public BigDecimal getCheckMoney() {
		return checkMoney;
	}

	public void setCheckMoney(BigDecimal checkMoney) {
		this.checkMoney = checkMoney;
	}

	public String getDifferenceReason() {
		return differenceReason;
	}

	public void setDifferenceReason(String differenceReason) {
		this.differenceReason = differenceReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return this.getLovName(status);
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getDeliveryLineId() {
		return deliveryLineId;
	}

	public void setDeliveryLineId(String deliveryLineId) {
		this.deliveryLineId = deliveryLineId;
	}

}