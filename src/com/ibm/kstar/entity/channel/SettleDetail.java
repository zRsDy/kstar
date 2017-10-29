package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 返利结算明细表(crm_t_settle_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-15
 */
@Entity
@Table(name = "crm_t_settle_detail")
public class SettleDetail extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 1970146068378174281L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "settle_detail_c_pid_generator")
   	@GenericGenerator(name="settle_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 返利结算id */
    @Column(name = "c_rebate_settle_id")
    private String rebateSettleId;
    
    /** 返利明细id */
    @Column(name = "c_reabate_detail_id")
    private String rebateDetailId;
    
    /** 结算金额 */
    @Column(name = "c_settle_money")
    private BigDecimal settleMoney;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRebateSettleId() {
		return rebateSettleId;
	}

	public void setRebateSettleId(String rebateSettleId) {
		this.rebateSettleId = rebateSettleId;
	}

	public String getRebateDetailId() {
		return rebateDetailId;
	}

	public void setRebateDetailId(String rebateDetailId) {
		this.rebateDetailId = rebateDetailId;
	}

	public BigDecimal getSettleMoney() {
		return settleMoney;
	}

	public void setSettleMoney(BigDecimal settleMoney) {
		this.settleMoney = settleMoney;
	}

}