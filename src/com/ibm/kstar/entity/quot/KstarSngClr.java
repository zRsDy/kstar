/** 
 * Project Name:crm 
 * File Name:KstarSngClr.java 
 * Package Name:com.ibm.kstar.entity.quot 
 * Date:Feb 8, 20174:07:42 PM 
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
 * ClassName:KstarSngClr <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Feb 8, 2017 4:07:42 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
@Entity
@Table(name = "CRM_T_SNG_COOLER") 
public class KstarSngClr implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "kstarsngclr_id_generator")
	@GenericGenerator(name="kstarsngclr_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 项目技术要求
	@Column(name = "C_PRJTCH_RQ")
	private String prjtchRq;
	
	// 项目技术要求名称
	@Column(name = "C_PRJTCH_RQNM")
	private String prjtchRqnm;
	
	// 空调制冷方式
	@Column(name = "C_CLR_TYP")
	private String clrTyp;
	
	// 空调送风方式
	@Column(name = "C_WND_TYP")
	private String wndTyp;
	
	// 机型选择
	@Column(name = "C_MCH_TYP")
	private String mchTyp;
	
	// 室外机位置
	@Column(name = "C_MCH_PST")
	private String mchPst;
	
	// 室外机与室内机之间的高度差
	@Column(name = "C_MCH_HGH")
	private String mchHgh;
	
	// 室内机与室外机的管程长度
	@Column(name = "C_MCH_LNG")
	private String mchLng;
	
	// 其他情况说明
	@Column(name = "C_OTHERS_NTS")
	private String othersNts;
	
	// 是否需要监控
	@Column(name = "C_IF_MNT")
	private String ifMnt;
	
	// 监控方式
	@Column(name = "C_MNT_TYP")
	private String mntTyp;
	
	// 是否需要漏水检测
	@Column(name = "C_IF_DRP")
	private String ifDrp;
	
	// 漏水检测方式
	@Column(name = "C_DRP_TYP")
	private String drpTyp;
	
	// 水冷空调进水温度
	@Column(name = "C_CLR_IN_TMP")
	private String clrIntmp;
	
	// 水冷空调出水温度
	@Column(name = "C_CLR_OT_TMP")
	private String clrOttmp;

	
	public KstarSngClr() {
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

	public String getPrjtchRq() {
		return prjtchRq;
	}

	public void setPrjtchRq(String prjtchRq) {
		this.prjtchRq = prjtchRq;
	}

	public String getClrTyp() {
		return clrTyp;
	}

	public void setClrTyp(String clrTyp) {
		this.clrTyp = clrTyp;
	}

	public String getWndTyp() {
		return wndTyp;
	}

	public void setWndTyp(String wndTyp) {
		this.wndTyp = wndTyp;
	}

	public String getMchTyp() {
		return mchTyp;
	}

	public void setMchTyp(String mchTyp) {
		this.mchTyp = mchTyp;
	}

	public String getMchPst() {
		return mchPst;
	}

	public void setMchPst(String mchPst) {
		this.mchPst = mchPst;
	}

	public String getMchHgh() {
		return mchHgh;
	}

	public void setMchHgh(String mchHgh) {
		this.mchHgh = mchHgh;
	}

	public String getMchLng() {
		return mchLng;
	}

	public void setMchLng(String mchLng) {
		this.mchLng = mchLng;
	}

	public String getOthersNts() {
		return othersNts;
	}

	public void setOthersNts(String othersNts) {
		this.othersNts = othersNts;
	}

	public String getIfMnt() {
		return ifMnt;
	}

	public void setIfMnt(String ifMnt) {
		this.ifMnt = ifMnt;
	}

	public String getMntTyp() {
		return mntTyp;
	}

	public void setMntTyp(String mntTyp) {
		this.mntTyp = mntTyp;
	}

	public String getIfDrp() {
		return ifDrp;
	}

	public void setIfDrp(String ifDrp) {
		this.ifDrp = ifDrp;
	}

	public String getDrpTyp() {
		return drpTyp;
	}

	public void setDrpTyp(String drpTyp) {
		this.drpTyp = drpTyp;
	}

	public String getClrIntmp() {
		return clrIntmp;
	}

	public void setClrIntmp(String clrIntmp) {
		this.clrIntmp = clrIntmp;
	}

	public String getClrOttmp() {
		return clrOttmp;
	}

	public void setClrOttmp(String clrOttmp) {
		this.clrOttmp = clrOttmp;
	}

	public String getPrjtchRqnm() {
		return prjtchRqnm;
	}

	public void setPrjtchRqnm(String prjtchRqnm) {
		this.prjtchRqnm = prjtchRqnm;
	}
	
}
  
