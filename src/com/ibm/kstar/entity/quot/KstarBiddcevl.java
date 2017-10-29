/** 
 * Project Name:crm 
 * File Name:KstarBiddcevl.java 
 * Package Name:com.ibm.kstar.entity.quot 
 * Date:Jan 13, 20179:25:21 AM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.entity.quot;
 
 import java.io.Serializable;

 import javax.persistence.Column;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 import javax.persistence.Table;

 import org.hibernate.annotations.GenericGenerator;
 
/** 
 * ClassName:KstarBiddcevl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Jan 13, 2017 9:25:21 AM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_BIDDC_EVL")
public class KstarBiddcevl implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "kstarbiddcevl_id_generator")
	@GenericGenerator(name = "kstarbiddcevl_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 当前处理人
	@Column(name = "C_CUR_PRSR")
	private String curPrsr;
	
	// 当前状态
	@Column(name = "C_CUR_STTS")
	private String curStts;
	
	// 审核意见
	@Column(name = "C_EVL_SGT")
	private String evlSgt;
	
	// 审批流Processid
	@Column(name = "C_SN_PNT")
	private String snPnt;
	
	// 标书URL
	@Column(name = "C_DOC_URL")
	private String docUrl;

	public KstarBiddcevl() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public String getCurPrsr() {
		return curPrsr;
	}

	public void setCurPrsr(String curPrsr) {
		this.curPrsr = curPrsr;
	}

	public String getCurStts() {
		return curStts;
	}

	public void setCurStts(String curStts) {
		this.curStts = curStts;
	}

	public String getEvlSgt() {
		return evlSgt;
	}

	public void setEvlSgt(String evlSgt) {
		this.evlSgt = evlSgt;
	}

	public String getSnPnt() {
		return snPnt;
	}

	public void setSnPnt(String snPnt) {
		this.snPnt = snPnt;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	
}
  
