/** 
 * Project Name:crm 
 * File Name:KstarSngRck.java 
 * Package Name:com.ibm.kstar.entity.quot 
 * Date:Feb 8, 20177:26:23 PM 
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
 * ClassName:KstarSngRck <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Feb 8, 2017 7:26:23 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */

 @Entity
 @Table(name = "CRM_T_SNG_RACK") 
 public class KstarSngRck implements Serializable {

	/**
	 * serialVersionUID:TODO
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarsngrck_id_generator")
	@GenericGenerator(name = "kstarsngrck_id_generator", strategy = "uuid")
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

	// 进出线方式
	@Column(name = "C_LN_INOT")
	private String lnInot;
	
	// 柜体尺寸
	@Column(name = "C_RCK_SZ")
	private String rckSz;
	
	// 柜体尺寸其他
	@Column(name = "C_RCK_SZ_NTS")
	private String rckSznts;
	
	// 开门方式（前门）
	@Column(name = "C_FRDR_TYP")
	private String frdrTyp;
	
	// 开门方式（前门）其他
	@Column(name = "C_FRDR_NTS")
	private String frdrNts;
	
	// 开门方式（后门）
	@Column(name = "C_RRDR_TYP")
	private String rrdrTyp;
	
	// 开门方式（后门）其他
	@Column(name = "C_RRDR_NTS")
	private String rrdrNts;
	
	// 前后门型式
	@Column(name = "C_FRRR_DR")
	private String frrrDr;
	
	// 前后门型式其他
	@Column(name = "C_FRRR_NTS")
	private String frrrNts;
	
	// 侧门
	@Column(name = "C_SD_DR")
	private String sdDr;
	
	// 侧门其他
	@Column(name = "C_SD_NTS")
	private String sdNts;
	
	// 柜体防护等级
	@Column(name = "C_RCKPR_LVL")
	private String rckprLvl;
	
	// 柜体防护等级其他
	@Column(name = "C_RCKPR_NTS")
	private String rckprNts;
	
	// 理线槽
	@Column(name = "C_LN_SLOT")
	private String lnSlot;
	
	// 理线槽规格数量
	@Column(name = "C_LN_SLOT_NM")
	private String lnSlotnm;
	
	// 理线槽规格其他
	@Column(name = "C_LN_SLOT_NTS")
	private String lnSlotnts;
	
	// 底板
	@Column(name = "C_BTTM")
	private String bttm;
	
	// 底板数量
	@Column(name = "C_BTTM_NM")
	private String bttmNm;
	
	// 底板其他
	@Column(name = "C_BTTM_NTS")
	private String bttmNts;
	
	// 层板
	@Column(name = "C_LVL")
	private String lvl;
	
	// 层板数量
	@Column(name = "C_LVL_NM")
	private String lvlNm;
	
	// 层板其他
	@Column(name = "C_LVL_NTS")
	private String lvlNts;
	
	// 盲板
	@Column(name = "C_BLD")
	private String bld;
	
	// 盲板数量
	@Column(name = "C_BLD_NM")
	private String bldNm;
	
	// 盲板其他
	@Column(name = "C_BLD_NTS")
	private String bldNts;
	
	// 散热单元
	@Column(name = "C_CLR_UT")
	private String clrUt;
	
	// 散热单元数量
	@Column(name = "C_CLR_UT_NM")
	private String clrUtnm;
	
	// 散热单元其他
	@Column(name = "C_CLR_UT_NTS")
	private String clrUtnts;
	
	// 机柜密闭组件
	@Column(name = "C_RCK_CMT")
	private String rckCmt;
	
	// 机柜密闭组件数量
	@Column(name = "C_RCK_CMT_NM")
	private String rckCmtnm;
	
	// 机柜密闭组件其他
	@Column(name = "C_RCK_CMT_NTS")
	private String rckCmtnts;
	
	// 其他特殊需求
	@Column(name = "C_OTHERS_RQ")
	private String othersRq;

	public KstarSngRck() {
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

	public String getLnInot() {
		return lnInot;
	}

	public void setLnInot(String lnInot) {
		this.lnInot = lnInot;
	}

	public String getRckSz() {
		return rckSz;
	}

	public void setRckSz(String rckSz) {
		this.rckSz = rckSz;
	}

	public String getRckSznts() {
		return rckSznts;
	}

	public void setRckSznts(String rckSznts) {
		this.rckSznts = rckSznts;
	}

	public String getFrdrTyp() {
		return frdrTyp;
	}

	public void setFrdrTyp(String frdrTyp) {
		this.frdrTyp = frdrTyp;
	}

	public String getFrdrNts() {
		return frdrNts;
	}

	public void setFrdrNts(String frdrNts) {
		this.frdrNts = frdrNts;
	}

	public String getRrdrTyp() {
		return rrdrTyp;
	}

	public void setRrdrTyp(String rrdrTyp) {
		this.rrdrTyp = rrdrTyp;
	}

	public String getRrdrNts() {
		return rrdrNts;
	}

	public void setRrdrNts(String rrdrNts) {
		this.rrdrNts = rrdrNts;
	}

	public String getFrrrDr() {
		return frrrDr;
	}

	public void setFrrrDr(String frrrDr) {
		this.frrrDr = frrrDr;
	}

	public String getFrrrNts() {
		return frrrNts;
	}

	public void setFrrrNts(String frrrNts) {
		this.frrrNts = frrrNts;
	}

	public String getSdDr() {
		return sdDr;
	}

	public void setSdDr(String sdDr) {
		this.sdDr = sdDr;
	}

	public String getSdNts() {
		return sdNts;
	}

	public void setSdNts(String sdNts) {
		this.sdNts = sdNts;
	}

	public String getRckprLvl() {
		return rckprLvl;
	}

	public void setRckprLvl(String rckprLvl) {
		this.rckprLvl = rckprLvl;
	}

	public String getRckprNts() {
		return rckprNts;
	}

	public void setRckprNts(String rckprNts) {
		this.rckprNts = rckprNts;
	}

	public String getLnSlot() {
		return lnSlot;
	}

	public void setLnSlot(String lnSlot) {
		this.lnSlot = lnSlot;
	}

	public String getLnSlotnm() {
		return lnSlotnm;
	}

	public void setLnSlotnm(String lnSlotnm) {
		this.lnSlotnm = lnSlotnm;
	}

	public String getLnSlotnts() {
		return lnSlotnts;
	}

	public void setLnSlotnts(String lnSlotnts) {
		this.lnSlotnts = lnSlotnts;
	}

	public String getBttm() {
		return bttm;
	}

	public void setBttm(String bttm) {
		this.bttm = bttm;
	}

	public String getBttmNm() {
		return bttmNm;
	}

	public void setBttmNm(String bttmNm) {
		this.bttmNm = bttmNm;
	}

	public String getBttmNts() {
		return bttmNts;
	}

	public void setBttmNts(String bttmNts) {
		this.bttmNts = bttmNts;
	}

	public String getLvl() {
		return lvl;
	}

	public void setLvl(String lvl) {
		this.lvl = lvl;
	}

	public String getLvlNm() {
		return lvlNm;
	}

	public void setLvlNm(String lvlNm) {
		this.lvlNm = lvlNm;
	}

	public String getLvlNts() {
		return lvlNts;
	}

	public void setLvlNts(String lvlNts) {
		this.lvlNts = lvlNts;
	}

	public String getBld() {
		return bld;
	}

	public void setBld(String bld) {
		this.bld = bld;
	}

	public String getBldNm() {
		return bldNm;
	}

	public void setBldNm(String bldNm) {
		this.bldNm = bldNm;
	}

	public String getBldNts() {
		return bldNts;
	}

	public void setBldNts(String bldNts) {
		this.bldNts = bldNts;
	}

	public String getClrUt() {
		return clrUt;
	}

	public void setClrUt(String clrUt) {
		this.clrUt = clrUt;
	}

	public String getClrUtnm() {
		return clrUtnm;
	}

	public void setClrUtnm(String clrUtnm) {
		this.clrUtnm = clrUtnm;
	}

	public String getClrUtnts() {
		return clrUtnts;
	}

	public void setClrUtnts(String clrUtnts) {
		this.clrUtnts = clrUtnts;
	}

	public String getRckCmt() {
		return rckCmt;
	}

	public void setRckCmt(String rckCmt) {
		this.rckCmt = rckCmt;
	}

	public String getRckCmtnm() {
		return rckCmtnm;
	}

	public void setRckCmtnm(String rckCmtnm) {
		this.rckCmtnm = rckCmtnm;
	}

	public String getRckCmtnts() {
		return rckCmtnts;
	}

	public void setRckCmtnts(String rckCmtnts) {
		this.rckCmtnts = rckCmtnts;
	}

	public String getOthersRq() {
		return othersRq;
	}

	public void setOthersRq(String othersRq) {
		this.othersRq = othersRq;
	}

	public String getPrjtchRqnm() {
		return prjtchRqnm;
	}

	public void setPrjtchRqnm(String prjtchRqnm) {
		this.prjtchRqnm = prjtchRqnm;
	}

}
  
