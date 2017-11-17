package com.ibm.kstar.entity.contract;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * KstarContract.java 产品实体类
 * 
 * @author Neil Wan and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "CRM_T_CONTR_PAY")
public class ContrPay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658308573164369043L;

	@Id
	@GeneratedValue(generator = "crm_t_contr_pay_id_generator")
	@GenericGenerator(name = "crm_t_contr_pay_id_generator", strategy = "uuid")
	@Column(name = "C_ID")
	private String id;

	@Column(name = "C_PAY_NO")
	private String payNo;

	@Column(name = "C_CONTR_ID")
	private String contrId;
	
	 @Column(name = "C_PAY_SEQ_Id")
	 private String paySeqId;
	 
	 @Transient
	 private String paySeqDesc; 
	 
	 @Column(name = "C_PAY_PLAN")
	 private String payPlan;

	 @Transient
	 private String payPlanDesc;  
	 
	 @Column(name = "C_ORIG_CLAUSE")
	 private String origClause;
	 
	 @Column(name = "C_SALER")
	 private String saler;
	 
	  @Column(name = "C_PLAN_RATIO")
	  private Double planRatio;
	 
	  @Column(name = "C_PLAN_AMT")
	  private Double planAmt;
	  
	 @Column(name = "DT_MEET_TIME")
	 private Date meetTime;
	
	 @Column(name = "N_TOTAL_AMT")
	 private Double totalAmt;
	
	 @Column(name = "N_CANCEL_AMT")
	 private Double cancelAmt;
	
	 @Column(name = "N_NCAN_AMT")
	 private Double ncanAmt;
	//
	 @Column(name = "C_PAY_SEQ")
	 private long paySeq;
		
	 @Column(name = "C_REMARK")
	 private String remark;
	 
	 /*
	  * 收款条件满足时间--期限
	  */
	 @Column(name = "C_PAY_TERM")
	 private String payTerm;
	 

	 @Transient
	 private String payTermDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getContrId() {
		return contrId;
	}

	public void setContrId(String contrId) {
		this.contrId = contrId;
	}

	public String getPaySeqId() {
		return paySeqId;
	}

	public void setPaySeqId(String paySeqId) {
		this.paySeqId = paySeqId;
	}

	public String getPayPlan() {
		return payPlan;
	}

	public void setPayPlan(String payPlan) {
		this.payPlan = payPlan;
	}

	public String getOrigClause() {
		return origClause;
	}

	public void setOrigClause(String origClause) {
		this.origClause = origClause;
	}

	public String getSaler() {
		return saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getMeetTime() {
		return meetTime;
	}

	public void setMeetTime(Date meetTime) {
		this.meetTime = meetTime;
	}

	public Double getPlanRatio() {
		return planRatio;
	}

	public void setPlanRatio(Double planRatio) {
		this.planRatio = planRatio;
	}

	public Double getPlanAmt() {
		return planAmt;
	}

	public void setPlanAmt(Double planAmt) {
		this.planAmt = planAmt;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Double getCancelAmt() {
		return cancelAmt;
	}

	public void setCancelAmt(Double cancelAmt) {
		this.cancelAmt = cancelAmt;
	}

	public Double getNcanAmt() {
		return ncanAmt;
	}

	public void setNcanAmt(Double ncanAmt) {
		this.ncanAmt = ncanAmt;
	}

	public long getPaySeq() {
		return paySeq;
	}

	public void setPaySeq(long paySeq) {
		this.paySeq = paySeq;
	}

	
	 public String getPaySeqDesc() {
	// return paySeqDesc;
	 LovMember lov = ((LovMember)CacheData.getInstance().get(paySeqId));
	 return lov==null? null : lov.getName();
	 }

	 public String getPayPlanDesc() {
	// return paySeqDesc;
	 LovMember lov = ((LovMember)CacheData.getInstance().get(payPlan));
	 return lov==null? null : lov.getName();
	 }

	public String getPayTerm() {
		return payTerm;
	}

	public void setPayTerm(String payTerm) {
		this.payTerm = payTerm;
	}


	 public String getPayTermDesc() {
	// return payTermDesc;
	 LovMember lov = ((LovMember)CacheData.getInstance().get(payTerm));
	 return lov==null? null : lov.getName();
	 }
}
