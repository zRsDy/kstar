
package com.ibm.kstar.entity.quot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ClassName:KstarCntr <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Dec 22, 2016 3:55:26 PM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Entity
@Table(name = "CRM_T_GEN_CNT")
public class KstarCntr implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(generator = "kstarcnt_id_generator")
	@GenericGenerator(name = "kstarcnt_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;

	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;

	// 合同编号
	@Column(name = "C_CNT_ID")
	private String cntId;

	// 合同名称
	@Column(name = "C_CNT_NM")
	private String cntNm;

	// 合同类型
	@Column(name = "C_CNT_TYP")
	private String cntTyp;

	// 创建日期
	@Column(name = "DT_CRT_DT")
	private Date cntDt;


	public KstarCntr() {
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

	public String getCntId() {
		return cntId;
	}

	public void setCntId(String cntId) {
		this.cntId = cntId;
	}

	public String getCntNm() {
		return cntNm;
	}

	public void setCntNm(String cntNm) {
		this.cntNm = cntNm;
	}

	public String getCntTyp() {
		return cntTyp;
	}

	public void setCntTyp(String cntTyp) {
		this.cntTyp = cntTyp;
	}

	public Date getCntDt() {
		return cntDt;
	}

	public void setCntDt(Date cntDt) {
		this.cntDt = cntDt;
	}

}
