/** 
 * Project Name:crmp 
 * File Name:KstarIdu.java 
 * Package Name:com.kstar.crm.quot 
 * Date:Dec 28, 201610:18:44 AM 
 * Copyright (c) 2016, ZW All Rights Reserved. 
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
 * ClassName:KstarIdu <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 28, 2016 10:18:44 AM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_IDU")
public class KstarIdu implements Serializable {
	
	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(generator = "kstaridu_id_generator")
	@GenericGenerator(name="kstaridu_id_generator", strategy="uuid")
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
	
	// 项目技术要求文件名称
	@Column(name = "C_PRJTCH_RQNM")
	private String prjtchRqnm;
	
	// 每个模块机柜数量
	@Column(name = "N_RCKMDL_NM")
	private String rckmdlNm;
	
	// 机柜数量
	@Column(name = "N_RCK_NM")
	private String rckNm;
	
	// 后期扩容要求
	@Column(name = "C_EDEXT_RQ")
	private String edextRq;
	
	// 市电输入
	@Column(name = "C_ELEC_IN")
	private String elecIn;
	
	// UPS额定容量
	@Column(name = "N_UPS_CPC")
	private String upsCpc;
	
	// UPS是否需要并机冗余
	@Column(name = "C_IF_UPSPRRL")
	private String ifUpsprrl;
	
	// 电池后备时间
	@Column(name = "N_BCKBTT_TM")
	private String bckbttTm;
	
	// 输入配电单元是否需要预留
	@Column(name = "C_IF_ELECIN")
	private String ifElecin;
	
	// 输入配电单元预留路
	@Column(name = "C_ELECIN_NM")
	private String elecinNm;
	
	// 输出配电单元是否需要预留
	@Column(name = "C_IF_ELECOUT")
	private String ifElecout;
	
	// 输出配电单元预留路
	@Column(name = "C_ELECOUT_NM")
	private String elecoutNm;
	
	// 机柜供电回路
	@Column(name = "C_RCKELEC_CLC")
	private String rckelecClc;
	
	// 机房布线
	@Column(name = "C_RM_DSLLN")
	private String rmDslln;
	
	// 电池安装形式
	@Column(name = "C_BTTINS_TYP")
	private String bttinsTyp;
	
	// PDU输出插孔
	@Column(name = "C_PDUOT_TYP")
	private String pduotTyp;
	
	// PDU输出路数
	@Column(name = "N_PDUOT_NM")
	private String pduotNm;
	
	// PDU选配功能
	@Column(name = "C_PDU_OPT")
	private String pduOpt;
	
	// 是否需要空调制冷
	@Column(name = "C_IF_CLR")
	private String ifClr;
	
	// 空调制冷方式
	@Column(name = "C_CLR_TYP")
	private String clrTyp;
	
	// 机型选择
	@Column(name = "C_MCH_TYP")
	private String mchTyp;
	
	// 室外机位置
	@Column(name = "C_MCH_PST")
	private String mchPst;
	
	// 冗余需求
	@Column(name = "C_RDN_RQ")
	private String rdnRq;
	
	// 室外机与室内机之间的高度差
	@Column(name = "N_MCH_HGH")
	private String mchHgh;
	
	// 室内机与室外机的管程长度
	@Column(name = "N_MCH_LNG")
	private String mchLng;
	
	// 是否有给排水
	@Column(name = "C_IF_WTR")
	private String ifWtr;
	
	// 是否需要应急风扇
	@Column(name = "C_IF_FAN")
	private String ifFan;
	
	// 是否需要消防
	@Column(name = "C_IF_FP")
	private String ifFp;
	
	// 监控为默认配置有无特殊要求
	@Column(name = "C_SPC_RQ")
	private String spcRq;

	
	public KstarIdu() {
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

	public String getPrjtchRqnm() {
		return prjtchRqnm;
	}

	public void setPrjtchRqnm(String prjtchRqnm) {
		this.prjtchRqnm = prjtchRqnm;
	}

	public String getRckmdlNm() {
		return rckmdlNm;
	}

	public void setRckmdlNm(String rckmdlNm) {
		this.rckmdlNm = rckmdlNm;
	}

	public String getRckNm() {
		return rckNm;
	}

	public void setRckNm(String rckNm) {
		this.rckNm = rckNm;
	}
	
	public String getEdextRq() {
		return edextRq;
	}

	public void setEdextRq(String edextRq) {
		this.edextRq = edextRq;
	}

	public String getElecIn() {
		return elecIn;
	}

	public void setElecIn(String elecIn) {
		this.elecIn = elecIn;
	}

	public String getUpsCpc() {
		return upsCpc;
	}

	public void setUpsCpc(String upsCpc) {
		this.upsCpc = upsCpc;
	}

	public String getIfUpsprrl() {
		return ifUpsprrl;
	}

	public void setIfUpsprrl(String ifUpsprrl) {
		this.ifUpsprrl = ifUpsprrl;
	}

	public String getBckbttTm() {
		return bckbttTm;
	}

	public void setBckbttTm(String bckbttTm) {
		this.bckbttTm = bckbttTm;
	}

	public String getIfElecin() {
		return ifElecin;
	}

	public void setIfElecin(String ifElecin) {
		this.ifElecin = ifElecin;
	}

	public String getIfElecout() {
		return ifElecout;
	}

	public void setIfElecout(String ifElecout) {
		this.ifElecout = ifElecout;
	}

	public String getElecinNm() {
		return elecinNm;
	}

	public void setElecinNm(String elecinNm) {
		this.elecinNm = elecinNm;
	}

	public String getElecoutNm() {
		return elecoutNm;
	}

	public void setElecoutNm(String elecoutNm) {
		this.elecoutNm = elecoutNm;
	}

	public String getRckelecClc() {
		return rckelecClc;
	}

	public void setRckelecClc(String rckelecClc) {
		this.rckelecClc = rckelecClc;
	}

	public String getRmDslln() {
		return rmDslln;
	}

	public void setRmDslln(String rmDslln) {
		this.rmDslln = rmDslln;
	}

	public String getBttinsTyp() {
		return bttinsTyp;
	}

	public void setBttinsTyp(String bttinsTyp) {
		this.bttinsTyp = bttinsTyp;
	}

	public String getPduotTyp() {
		return pduotTyp;
	}

	public void setPduotTyp(String pduotTyp) {
		this.pduotTyp = pduotTyp;
	}

	public String getPduotNm() {
		return pduotNm;
	}

	public void setPduotNm(String pduotNm) {
		this.pduotNm = pduotNm;
	}

	public String getPduOpt() {
		return pduOpt;
	}

	public void setPduOpt(String pduOpt) {
		this.pduOpt = pduOpt;
	}

	public String getIfClr() {
		return ifClr;
	}

	public void setIfClr(String ifClr) {
		this.ifClr = ifClr;
	}

	public String getClrTyp() {
		return clrTyp;
	}

	public void setClrTyp(String clrTyp) {
		this.clrTyp = clrTyp;
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

	public String getRdnRq() {
		return rdnRq;
	}

	public void setRdnRq(String rdnRq) {
		this.rdnRq = rdnRq;
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

	public String getIfWtr() {
		return ifWtr;
	}

	public void setIfWtr(String ifWtr) {
		this.ifWtr = ifWtr;
	}

	public String getIfFan() {
		return ifFan;
	}

	public void setIfFan(String ifFan) {
		this.ifFan = ifFan;
	}

	public String getIfFp() {
		return ifFp;
	}

	public void setIfFp(String ifFp) {
		this.ifFp = ifFp;
	}

	public String getSpcRq() {
		return spcRq;
	}

	public void setSpcRq(String spcRq) {
		this.spcRq = spcRq;
	}
	
}
  
