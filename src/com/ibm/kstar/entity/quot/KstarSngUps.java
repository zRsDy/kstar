      
package com.ibm.kstar.entity.quot;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
 
/** 
 * ClassName:KstarSngUps <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 29, 2016 3:50:21 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_SNG_UPS")
public class KstarSngUps implements Serializable {

	/**
	 * serialVersionUID:TODO
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(generator = "sngups_id_generator")
	@GenericGenerator(name="sngups_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 配电系统图纸
	@Column(name = "C_ELEC_DRAFT")
	private String elecDrf;
	
	// 配电系统图纸名称
	@Column(name = "C_ELEC_DRAFTNM")
	private String elecDrfnm;
	
	// 项目技术要求
	@Column(name = "C_PRJTCH_RQ")
	private String prjtchRq;
	
	// 项目技术要求名称
	@Column(name = "C_PRJTCH_RQNM")
	private String prjtchRqnm;
	
	// 进出线方式
	@Column(name = "C_INOTLN_TYP")
	private String intlnTyp;
	
	// UPS系统组成
	@Column(name = "C_UPS_CNST")
	private String upsCnst;
	
	// 后备时间
	@Column(name = "N_BCKUP_TM")
	private String bckupTm;
	
	// 监控要求、协议要求
	@Column(name = "C_MNT_RQ")
	private String mntRq;
	
	// IP等级
	@Column(name = "C_IP_LVL")
	private String ipLvl;
	
	// IP等级其它
	@Column(name = "C_IP_LVL_RQ")
	private String ipLvlRq;
	
	// 其他特殊需求
	@Column(name = "C_OTHERS_RQ")
	private String otrsRq;

	
	public KstarSngUps() {
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

	public String getElecDrf() {
		return elecDrf;
	}

	public void setElecDrf(String elecDrf) {
		this.elecDrf = elecDrf;
	}

	public String getPrjtchRq() {
		return prjtchRq;
	}

	public void setPrjtchRq(String prjtchRq) {
		this.prjtchRq = prjtchRq;
	}

	public String getIntlnTyp() {
		return intlnTyp;
	}

	public void setIntlnTyp(String intlnTyp) {
		this.intlnTyp = intlnTyp;
	}

	public String getUpsCnst() {
		return upsCnst;
	}

	public void setUpsCnst(String upsCnst) {
		this.upsCnst = upsCnst;
	}

	public String getBckupTm() {
		return bckupTm;
	}

	public void setBckupTm(String bckupTm) {
		this.bckupTm = bckupTm;
	}

	public String getMntRq() {
		return mntRq;
	}

	public void setMntRq(String mntRq) {
		this.mntRq = mntRq;
	}

	public String getIpLvl() {
		return ipLvl;
	}

	public void setIpLvl(String ipLvl) {
		this.ipLvl = ipLvl;
	}

	public String getIpLvlRq() {
		return ipLvlRq;
	}

	public void setIpLvlRq(String ipLvlRq) {
		this.ipLvlRq = ipLvlRq;
	}

	public String getOtrsRq() {
		return otrsRq;
	}

	public void setOtrsRq(String otrsRq) {
		this.otrsRq = otrsRq;
	}

	public String getElecDrfnm() {
		return elecDrfnm;
	}

	public void setElecDrfnm(String elecDrfnm) {
		this.elecDrfnm = elecDrfnm;
	}

	public String getPrjtchRqnm() {
		return prjtchRqnm;
	}

	public void setPrjtchRqnm(String prjtchRqnm) {
		this.prjtchRqnm = prjtchRqnm;
	}

}
  
