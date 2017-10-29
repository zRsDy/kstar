       
package com.ibm.kstar.entity.quot;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
 
/** 
 * ClassName:KstarPgInf <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 26, 2016 4:30:51 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_PG_INF")  
public class KstarPgInf implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator = "kstarpginf_id_generator")
	@GenericGenerator(name = "kstarpginf_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 界面类型
	@Column(name = "C_PG_TYP")
	private String pgTyp;
	
	// 相关产品 集成需求
	@Column(name = "C_REL_PRD")
	private String relPrd;
	
	// 表单填写完成度
	@Column(name = "C_COMPLETE")
	private String cComplete;
	
	// 支持请求单号
	@Column(name = "C_REQ_NO")
	private String reqNo;
	
	// 支持人
	@Column(name = "C_SUPPORTER")
	private String cSupporter;
	
	// 受理状态
	@Column(name = "C_STATUS")
	private String cStatus;

	public KstarPgInf() {
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

	public String getPgTyp() {
		return pgTyp;
	}

	public void setPgTyp(String pgTyp) {
		this.pgTyp = pgTyp;
	}

	public String getRelPrd() {
		return relPrd;
	}

	public void setRelPrd(String relPrd) {
		this.relPrd = relPrd;
	}

	public String getcComplete() {
		return cComplete;
	}

	public void setcComplete(String cComplete) {
		this.cComplete = cComplete;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getcSupporter() {
		return cSupporter;
	}

	public void setcSupporter(String cSupporter) {
		this.cSupporter = cSupporter;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String cStatus) {
		this.cStatus = cStatus;
	}
	
}
  
