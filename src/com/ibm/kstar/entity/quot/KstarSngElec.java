/** 
 * Project Name:crm 
 * File Name:KstarSngElec.java 
 * Package Name:com.ibm.kstar.entity.quot 
 * Date:Feb 7, 20177:20:01 PM 
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
 * ClassName:KstarSngElec <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Feb 7, 2017 7:20:01 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */

@Entity
@Table(name = "CRM_T_SNG_ELEC") 
public class KstarSngElec implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "kstarsngelec_id_generator")
	@GenericGenerator(name="kstarsngelec_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 进出线方式
	@Column(name = "C_LN_INOT")
	private String lnInot;
	
	// 维护方式
	@Column(name = "C_MNT_TYP")
	private String mntTyp;
	
	// 维护方式其它
	@Column(name = "C_MNT_NTS")
	private String mntNts;
	
	
	// 开门方式（前门）
	@Column(name = "C_FRDR_TYP")
	private String frdrYyp;
	
	// 开门方式其它（前门）
	@Column(name = "C_FRDR_NTS")
	private String frdrNts;
	
	
	// 开门方式（后门）
	@Column(name = "C_RRDR_TYP")
	private String rrdrTyp;
	
	// 开门方式其它（后门）
	@Column(name = "C_RRDR_NTS")
	private String rrdrNts;
	
	
	// 前门型式
	@Column(name = "C_FRDR")
	private String frdr;
	
	// 进出线电缆线径
	@Column(name = "N_INOT_LN")
	private String inotLn;
	
	// 柜体尺寸
	@Column(name = "C_RCK_SZ")
	private String rckSz;
	
	// 柜体尺寸其它
	@Column(name = "C_RCK_SZ_NTS")
	private String rckSznts;
	
	// 柜体防护等级
	@Column(name = "C_RCKPR_LVL")
	private String rckprLvl;
	
	// 柜体防护等级其它
	@Column(name = "C_RCKPR_NTS")
	private String rckprNts;
	
	// 开关品牌
	@Column(name = "C_SWT_BRD")
	private String swtBrd;
	
	// 开关品牌其它
	@Column(name = "C_SWT_NTS")
	private String swtNts;
	
	// 消防分励脱扣
	@Column(name = "C_FP_TK")
	private String fpTk;
	
	// 微型断路器热插拔功能
	@Column(name = "C_CLC_PLGIN")
	private String clcPlgin;
	
	// 提供配电系统图
	@Column(name = "C_ELEC_DRF")
	private String elecDrf;
	
	// 单电源总输入开关电流
	@Column(name = "C_TTLIN_SWT_CRT")
	private String ttlinSwtcrt;
	
	// 单电源总输入开关极数
	@Column(name = "C_TTLIN_SWT_PLE")
	private String ttlinSwtple;
	
	// 单电源总输入开关数量
	@Column(name = "C_TTLIN_SWT_NM")
	private String ttlinSwtnm;
	
	// 双电源总输入ATS开关电流
	@Column(name = "C_ATS_SWT_CRT")
	private String atsSwtcrt;
	
	// 双电源总输入ATS开关极数
	@Column(name = "C_ATS_SWT_PLE")
	private String atsSwtple;
	
	// 双电源总输入ATS开关数量
	@Column(name = "C_ATS_SWT_NM")
	private String atsSwtnm;
	
	// 市电输出开关电流
	@Column(name = "C_ELECOT_SWT_CRT")
	private String elecotSwtcrt;
	
	// 市电输出开关极数
	@Column(name = "C_ELECOT_SWT_PLE")
	private String elecotSwtple;
	
	// 市电输出开关数量
	@Column(name = "C_ELECOT_SWT_NM")
	private String elecotSwtnm;
	
	// UPS输入开关电流
	@Column(name = "C_UPSIN_SWT_CRT")
	private String upsinSwtcrt;
	
	// UPS输入开关极数
	@Column(name = "C_UPSIN_SWT_PLE")
	private String upsinSwtple;
	
	// UPS输入开关数量
	@Column(name = "C_UPSIN_SWT_NM")
	private String upsinSwtnm;
	
	// UPS输出开关电流
	@Column(name = "C_UPSOT_SWT_CRT")
	private String upsotSwtcrt;
	
	// UPS输出开关极数
	@Column(name = "C_UPSOT_SWT_PLE")
	private String upsotSwtple;
	
	// UPS输出开关数量
	@Column(name = "C_UPSOT_SWT_NM")
	private String upsotSwtnm;
	
	// UPS旁路开关电流
	@Column(name = "C_UPSBP_SWT_CRT")
	private String upsbpSwtcrt;
	
	// UPS旁路开关极数
	@Column(name = "C_UPSBP_SWT_PLE")
	private String upsbpSwtple;
	
	// UPS旁路开关数量
	@Column(name = "C_UPSBP_SWT_NM")
	private String upsbpSwtnm;
	
	// 1UPS输出开关电流
	@Column(name = "C_FRT_UPS_CRT")
	private String frtUpscrt;
	
	// 1UPS输出开关极数
	@Column(name = "C_FRT_UPS_PLE")
	private String frtUpsple;
	
	// 1UPS输出开关数量
	@Column(name = "C_FRT_UPS_NM")
	private String frtUpsnm;
	
	// 2UPS输出开关电流
	@Column(name = "C_SCD_UPS_CRT")
	private String scdUpscrt;
	
	// 2UPS输出开关极数
	@Column(name = "C_SCD_UPS_PLE")
	private String scdUpsple;
	
	// 2UPS输出开关数量
	@Column(name = "C_SCD_UPS_NM")
	private String scdUpsnm;
	
	// 其他特殊需求
	@Column(name = "C_OTHERS_RQ")
	private String othersRq;

	
	public KstarSngElec() {
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

	public String getLnInot() {
		return lnInot;
	}

	public void setLnInot(String lnInot) {
		this.lnInot = lnInot;
	}

	public String getMntTyp() {
		return mntTyp;
	}

	public void setMntTyp(String mntTyp) {
		this.mntTyp = mntTyp;
	}

	public String getFrdrYyp() {
		return frdrYyp;
	}

	public void setFrdrYyp(String frdrYyp) {
		this.frdrYyp = frdrYyp;
	}

	public String getRrdrTyp() {
		return rrdrTyp;
	}

	public void setRrdrTyp(String rrdrTyp) {
		this.rrdrTyp = rrdrTyp;
	}

	public String getFrdr() {
		return frdr;
	}

	public void setFrdr(String frdr) {
		this.frdr = frdr;
	}

	public String getInotLn() {
		return inotLn;
	}

	public void setInotLn(String inotLn) {
		this.inotLn = inotLn;
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

	public String getSwtNts() {
		return swtNts;
	}

	public void setSwtNts(String swtNts) {
		this.swtNts = swtNts;
	}

	public String getSwtBrd() {
		return swtBrd;
	}

	public void setSwtBrd(String swtBrd) {
		this.swtBrd = swtBrd;
	}

	public String getFpTk() {
		return fpTk;
	}

	public void setFpTk(String fpTk) {
		this.fpTk = fpTk;
	}

	public String getClcPlgin() {
		return clcPlgin;
	}

	public void setClcPlgin(String clcPlgin) {
		this.clcPlgin = clcPlgin;
	}

	public String getElecDrf() {
		return elecDrf;
	}

	public void setElecDrf(String elecDrf) {
		this.elecDrf = elecDrf;
	}

	public String getMntNts() {
		return mntNts;
	}

	public void setMntNts(String mntNts) {
		this.mntNts = mntNts;
	}

	public String getFrdrNts() {
		return frdrNts;
	}

	public void setFrdrNts(String frdrNts) {
		this.frdrNts = frdrNts;
	}

	public String getRrdrNts() {
		return rrdrNts;
	}

	public void setRrdrNts(String rrdrNts) {
		this.rrdrNts = rrdrNts;
	}

	public String getTtlinSwtcrt() {
		return ttlinSwtcrt;
	}

	public void setTtlinSwtcrt(String ttlinSwtcrt) {
		this.ttlinSwtcrt = ttlinSwtcrt;
	}

	public String getTtlinSwtple() {
		return ttlinSwtple;
	}

	public void setTtlinSwtple(String ttlinSwtple) {
		this.ttlinSwtple = ttlinSwtple;
	}

	public String getTtlinSwtnm() {
		return ttlinSwtnm;
	}

	public void setTtlinSwtnm(String ttlinSwtnm) {
		this.ttlinSwtnm = ttlinSwtnm;
	}

	public String getAtsSwtcrt() {
		return atsSwtcrt;
	}

	public void setAtsSwtcrt(String atsSwtcrt) {
		this.atsSwtcrt = atsSwtcrt;
	}

	public String getAtsSwtple() {
		return atsSwtple;
	}

	public void setAtsSwtple(String atsSwtple) {
		this.atsSwtple = atsSwtple;
	}

	public String getAtsSwtnm() {
		return atsSwtnm;
	}

	public void setAtsSwtnm(String atsSwtnm) {
		this.atsSwtnm = atsSwtnm;
	}

	public String getElecotSwtcrt() {
		return elecotSwtcrt;
	}

	public void setElecotSwtcrt(String elecotSwtcrt) {
		this.elecotSwtcrt = elecotSwtcrt;
	}

	public String getElecotSwtple() {
		return elecotSwtple;
	}

	public void setElecotSwtple(String elecotSwtple) {
		this.elecotSwtple = elecotSwtple;
	}

	public String getElecotSwtnm() {
		return elecotSwtnm;
	}

	public void setElecotSwtnm(String elecotSwtnm) {
		this.elecotSwtnm = elecotSwtnm;
	}

	public String getUpsinSwtcrt() {
		return upsinSwtcrt;
	}

	public void setUpsinSwtcrt(String upsinSwtcrt) {
		this.upsinSwtcrt = upsinSwtcrt;
	}

	public String getUpsinSwtple() {
		return upsinSwtple;
	}

	public void setUpsinSwtple(String upsinSwtple) {
		this.upsinSwtple = upsinSwtple;
	}

	public String getUpsinSwtnm() {
		return upsinSwtnm;
	}

	public void setUpsinSwtnm(String upsinSwtnm) {
		this.upsinSwtnm = upsinSwtnm;
	}

	public String getUpsotSwtcrt() {
		return upsotSwtcrt;
	}

	public void setUpsotSwtcrt(String upsotSwtcrt) {
		this.upsotSwtcrt = upsotSwtcrt;
	}

	public String getUpsotSwtple() {
		return upsotSwtple;
	}

	public void setUpsotSwtple(String upsotSwtple) {
		this.upsotSwtple = upsotSwtple;
	}

	public String getUpsotSwtnm() {
		return upsotSwtnm;
	}

	public void setUpsotSwtnm(String upsotSwtnm) {
		this.upsotSwtnm = upsotSwtnm;
	}

	public String getUpsbpSwtcrt() {
		return upsbpSwtcrt;
	}

	public void setUpsbpSwtcrt(String upsbpSwtcrt) {
		this.upsbpSwtcrt = upsbpSwtcrt;
	}

	public String getUpsbpSwtple() {
		return upsbpSwtple;
	}

	public void setUpsbpSwtple(String upsbpSwtple) {
		this.upsbpSwtple = upsbpSwtple;
	}

	public String getUpsbpSwtnm() {
		return upsbpSwtnm;
	}

	public void setUpsbpSwtnm(String upsbpSwtnm) {
		this.upsbpSwtnm = upsbpSwtnm;
	}

	public String getFrtUpscrt() {
		return frtUpscrt;
	}

	public void setFrtUpscrt(String frtUpscrt) {
		this.frtUpscrt = frtUpscrt;
	}

	public String getFrtUpsple() {
		return frtUpsple;
	}

	public void setFrtUpsple(String frtUpsple) {
		this.frtUpsple = frtUpsple;
	}

	public String getFrtUpsnm() {
		return frtUpsnm;
	}

	public void setFrtUpsnm(String frtUpsnm) {
		this.frtUpsnm = frtUpsnm;
	}

	public String getScdUpscrt() {
		return scdUpscrt;
	}

	public void setScdUpscrt(String scdUpscrt) {
		this.scdUpscrt = scdUpscrt;
	}

	public String getScdUpsple() {
		return scdUpsple;
	}

	public void setScdUpsple(String scdUpsple) {
		this.scdUpsple = scdUpsple;
	}

	public String getScdUpsnm() {
		return scdUpsnm;
	}

	public void setScdUpsnm(String scdUpsnm) {
		this.scdUpsnm = scdUpsnm;
	}

	public String getOthersRq() {
		return othersRq;
	}

	public void setOthersRq(String othersRq) {
		this.othersRq = othersRq;
	}
	
}
  
