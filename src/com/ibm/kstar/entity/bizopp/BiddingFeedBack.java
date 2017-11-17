package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 投标反馈
 * @author Gaoyuliang 2017-2-21
 * 
 */
@Entity
@Table(name = "crm_t_bidding_feedback")
@Permission(businessType = "BiddingFeedBack")
public class BiddingFeedBack implements Serializable{
	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "kstarquot_id_generator")
	@GenericGenerator(name="kstarquot_id_generator", strategy="uuid")
	@Column(name = "id")
	@PermissionBusinessId
	private String id;
	
	/** 商机ID，类外键关联ID **/
	@Column(name = "bidoppid")
	private String bidId;
	
	/** 投标反馈审批状态 **/
	@Column(name = "status")
	private String status ;
	
	/** 投标结果 **/
	@Column(name = "C_BID_RESULTS")
	private String bidResults ;
	
	/** 丢标/赢标原因 **/
	@Column(name = "bid_res_reason")
	private String bidResReason ;
	
	/** 反馈投标结果附件 **/
	@Column(name = "C_BIDRSTATT")
	private String bidRstatt ;
	
	/** 主要竞争对手 **/
	@Column(name = "C_BIDCMPR")
	private String bidCmpr ;
	
	/** 中标品牌 **/
	@Column(name = "C_BIDBRD")
	private String bidBrd ;
	
	/** 中标价格 **/
	@Column(name = "N_BIDAMOUNT")
	private BigDecimal bidAmount ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBidResults() {
		return bidResults;
	}

	public void setBidResults(String bidResults) {
		this.bidResults = bidResults;
	}

	public String getBidResReason() {
		return bidResReason;
	}

	public void setBidResReason(String bidResReason) {
		this.bidResReason = bidResReason;
	}

	public String getBidRstatt() {
		return bidRstatt;
	}

	public void setBidRstatt(String bidRstatt) {
		this.bidRstatt = bidRstatt;
	}

	public String getBidCmpr() {
		return bidCmpr;
	}

	public void setBidCmpr(String bidCmpr) {
		this.bidCmpr = bidCmpr;
	}

	public String getBidBrd() {
		return bidBrd;
	}

	public void setBidBrd(String bidBrd) {
		this.bidBrd = bidBrd;
	}

	public BigDecimal getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(BigDecimal bidAmount) {
		this.bidAmount = bidAmount;
	}
	
	
}
