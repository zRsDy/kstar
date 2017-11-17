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
 * 资料与礼品申请明细表(crm_t_gift_apply_detail)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-04
 */
@Entity
@Table(name = "crm_t_gift_apply_detail")
public class GiftApplyDetail extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -240160473912650851L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "gift_apply_detail_c_pid_generator")
   	@GenericGenerator(name="gift_apply_detail_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_Id")
    private String applyId;
    
    /** 礼品id */
    @Column(name = "c_gift_id")
    private String giftId;
    
    /** 申请数量 */
	@Column(name = "c_apply_quantity")
	private Integer applyQuantity;
	
	/** 实发数量 */
	@Column(name = "c_actual_give_quantity")
	private Integer actualGiveQuantity;
	
	/** 实发日期 */
    @Column(name = "c_actual_give_date")
    private Date actualGiveDate;
    
    /** 实到数量 */
	@Column(name = "c_actual_get_quantity")
	private Integer actualGetQuantity;
	
	/** 实到日期 */
    @Column(name = "c_actual_get_date")
    private Date actualGetDate;
    
    /** 状态 */
	@Column(name = "c_status")
	private String status;

	/** 说明 */
    @Column(name = "c_explain")
    private String explain;
    
    @Transient
    private GiftManage giftManage;
    
    public String getId() {
		return this.id;
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

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public Integer getApplyQuantity() {
		return applyQuantity;
	}

	public void setApplyQuantity(Integer applyQuantity) {
		this.applyQuantity = applyQuantity;
	}

	public Integer getActualGiveQuantity() {
		return actualGiveQuantity;
	}

	public void setActualGiveQuantity(Integer actualGiveQuantity) {
		this.actualGiveQuantity = actualGiveQuantity;
	}

	public Date getActualGiveDate() {
		return actualGiveDate;
	}

	public void setActualGiveDate(Date actualGiveDate) {
		this.actualGiveDate = actualGiveDate;
	}

	public Integer getActualGetQuantity() {
		return actualGetQuantity;
	}

	public void setActualGetQuantity(Integer actualGetQuantity) {
		this.actualGetQuantity = actualGetQuantity;
	}

	public Date getActualGetDate() {
		return actualGetDate;
	}

	public void setActualGetDate(Date actualGetDate) {
		this.actualGetDate = actualGetDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public GiftManage getGiftManage() {
		return giftManage;
	}

	public void setGiftManage(GiftManage giftManage) {
		this.giftManage = giftManage;
	}

}