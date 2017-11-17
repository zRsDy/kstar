
package com.ibm.kstar.entity.quot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
 * ClassName:KstarPrjEvl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 22, 2016 7:40:08 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_PRJ_EVL")
public class KstarPrjEvl implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator = "kstarprjevl_id_generator")
	@GenericGenerator(name = "kstarprjevl_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 序号
	@Column(name = "C_SEQ_NO")
	private int seqno;
	
	// 评审类别
	@Column(name = "C_EVL_TYP")
	private String evlTyp;
	

	// 评审类别
	@Transient
	private String evlTypDesc;
	
	// 评审人
	@Column(name = "C_EVL_MM")
	private String evlMm;
	
	// 评审状态
	@Column(name = "C_EVL_ST")
	private String evlSt;

	// 评审状态
	@Transient
	private String evlStDesc;
	
	// 提交时间
	@Column(name = "DT_SBM_DT")
	private Date sbmDt;
	
	// 审批流程processid
	@Column(name = "C_EVL_RS")
	private String evlRs;
	
	// 评审人意见
	@Column(name = "C_EVL_SG")
	private String evlSg;
	
	// 评审完成时间
	@Column(name = "DT_FIN_DT")
	private Date finDt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuotCode() {
		return quotCode;
	}

	public void setQuotCode(String quotCode) {
		this.quotCode = quotCode;
	}

	public String getCType() {
		return CType;
	}

	public void setCType(String cType) {
		CType = cType;
	}



	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public String getEvlTyp() {
		return evlTyp;
	}

	public void setEvlTyp(String evlTyp) {
		this.evlTyp = evlTyp;
	}

	public String getEvlMm() {
		return evlMm;
	}

	public void setEvlMm(String evlMm) {
		this.evlMm = evlMm;
	}

	public String getEvlSt() {
		return evlSt;
	}

	public void setEvlSt(String evlSt) {
		this.evlSt = evlSt;
	}


	public Date getSbmDt() {
		return sbmDt;
	}

	public void setSbmDt(Date sbmDt) {
		this.sbmDt = sbmDt;
	}

	public String getEvlRs() {
		return evlRs;
	}

	public void setEvlRs(String evlRs) {
		this.evlRs = evlRs;
	}

	public String getEvlSg() {
		return evlSg;
	}

	public void setEvlSg(String evlSg) {
		this.evlSg = evlSg;
	}


	public Date getFinDt() {
		return finDt;
	}

	public void setFinDt(Date finDt) {
		this.finDt = finDt;
	}
	

	 public String getEvlTypDesc() { 
	 LovMember lov = ((LovMember)CacheData.getInstance().get(evlTyp));
	 return lov==null? null : lov.getName();
	 }


	 public String getEvlStDesc() { 
	 LovMember lov = ((LovMember)CacheData.getInstance().get(evlSt));
	 return lov==null? null : lov.getName();
	 }
}
  
