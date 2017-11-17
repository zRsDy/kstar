/** 
 * Project Name:crm 
 * File Name:KstarSngMnt.java 
 * Package Name:com.ibm.kstar.entity.quot 
 * Date:Feb 9, 20172:07:12 PM 
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
 * ClassName:KstarSngMnt <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Feb 9, 2017 2:07:12 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */

 @Entity
 @Table(name = "CRM_T_SNG_MNT") 
 public class KstarSngMnt implements Serializable {

	/**
	 * serialVersionUID:TODO
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarsngmnt_id_generator")
	@GenericGenerator(name = "kstarsngmnt_id_generator", strategy = "uuid")
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

	// 告警需求
	@Column(name = "C_ALM_RQ")
	private String almRq;
	
	// 动力监控设备
	@Column(name = "C_PWR_MNT")
	private String pwrMnt;
	
	// UPS
	@Column(name = "C_UPS")
	private String ups;
	
	// UPS规格数量
	@Column(name = "C_UPS_NM")
	private String upsNm;
	
	// 精密空调
	@Column(name = "C_CLR")
	private String clr;
	
	// 精密空调规格数量
	@Column(name = "C_CLR_NM")
	private String clrNm;
	
	// 新风机
	@Column(name = "C_WND")
	private String wnd;
	
	// 新风机规格数量
	@Column(name = "C_WND_NM")
	private String wndNm;
	
	// 精密配电
	@Column(name = "C_JM_ELEC")
	private String jmElec;
	
	// 精密配电规格数量
	@Column(name = "C_JM_ELEC_NM")
	private String jmElecnm;
	
	// 智能配电
	@Column(name = "C_INT_ELEC")
	private String intElec;
	
	// 智能配电规格数量
	@Column(name = "C_INT_ELEC_NM")
	private String intElecnm;
	
	// 定位漏水
	@Column(name = "C_DRP")
	private String drp;
	
	// 定位漏水规格数量
	@Column(name = "C_DRP_NM")
	private String drpNm;
	
	// 智能发电机
	@Column(name = "C_ELEC_GNR")
	private String elecGnr;
	
	// 智能发电机规格数量
	@Column(name = "C_ELEC_GNR_NM")
	private String elecGnrnm;
	
	// 其他(动力监控设备)
	@Column(name = "C_PWR_OTR")
	private String pwrOtr;
	
	// 其他规格数量(动力监控设备)
	@Column(name = "C_PWR_OTR_NM")
	private String pwrOtrnm;
	
	// 环境监控设备
	@Column(name = "C_ENV_MNT")
	private String envMnt;
	
	// 温湿度
	@Column(name = "C_TMP")
	private String tmp;
	
	// 温湿度规格数量
	@Column(name = "C_TMP_NM")
	private String tmpNm;
	
	// 烟感
	@Column(name = "C_SMK")
	private String smk;
	
	// 烟感规格数量
	@Column(name = "C_SMK_NM")
	private String smkNm;
	
	// 红外
	@Column(name = "C_IFR")
	private String ifr;
	
	// 红外规格数量
	@Column(name = "C_IFR_NM")
	private String ifrNm;
	
	// 门磁
	@Column(name = "C_DR_LCK")
	private String drLck;
	
	// 门磁规格数量
	@Column(name = "C_DR_LCK_NM")
	private String drLcknm;
	
	// 新风机(环境监控设备)
	@Column(name = "C_NWWND")
	private String nwwnd;
	
	// 新风机规格数量(环境监控设备)
	@Column(name = "C_NWWND_NM")
	private String nwwndNm;
	
	// 不定位漏水
	@Column(name = "C_NP_DRP")
	private String npDrp;
	
	// 不定位漏水规格数量
	@Column(name = "C_NP_DRP_NM")
	private String npDrpnm;
	
	// 其他(环境监控设备)
	@Column(name = "C_MNT_OTR")
	private String mntOtr;
	
	// 其他规格数量(环境监控设备)
	@Column(name = "C_MNT_OTR_NM")
	private String mntOtrnm;
	
	// 安防系统
	@Column(name = "C_SEC_SYS")
	private String secSys;
	
	// 安防系统描述
	@Column(name = "C_SEC_SYS_NTS")
	private String secSysnts;
	
	// 门禁系统
	@Column(name = "C_DR_CNR")
	private String drCnr;
	
	// 门禁系统描述
	@Column(name = "C_DR_CNR_NTS")
	private String drCnrnts;
	
	// 其他特殊需求
	@Column(name = "C_OTHERS_RQ")
	private String othersRq;

	public KstarSngMnt() {
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

	public String getAlmRq() {
		return almRq;
	}

	public void setAlmRq(String almRq) {
		this.almRq = almRq;
	}

	public String getPwrMnt() {
		return pwrMnt;
	}

	public void setPwrMnt(String pwrMnt) {
		this.pwrMnt = pwrMnt;
	}

	public String getUps() {
		return ups;
	}

	public void setUps(String ups) {
		this.ups = ups;
	}

	public String getUpsNm() {
		return upsNm;
	}

	public void setUpsNm(String upsNm) {
		this.upsNm = upsNm;
	}

	public String getClr() {
		return clr;
	}

	public void setClr(String clr) {
		this.clr = clr;
	}

	public String getClrNm() {
		return clrNm;
	}

	public void setClrNm(String clrNm) {
		this.clrNm = clrNm;
	}

	public String getWnd() {
		return wnd;
	}

	public void setWnd(String wnd) {
		this.wnd = wnd;
	}

	public String getWndNm() {
		return wndNm;
	}

	public void setWndNm(String wndNm) {
		this.wndNm = wndNm;
	}

	public String getJmElec() {
		return jmElec;
	}

	public void setJmElec(String jmElec) {
		this.jmElec = jmElec;
	}

	public String getJmElecnm() {
		return jmElecnm;
	}

	public void setJmElecnm(String jmElecnm) {
		this.jmElecnm = jmElecnm;
	}

	public String getIntElec() {
		return intElec;
	}

	public void setIntElec(String intElec) {
		this.intElec = intElec;
	}

	public String getIntElecnm() {
		return intElecnm;
	}

	public void setIntElecnm(String intElecnm) {
		this.intElecnm = intElecnm;
	}

	public String getDrp() {
		return drp;
	}

	public void setDrp(String drp) {
		this.drp = drp;
	}

	public String getDrpNm() {
		return drpNm;
	}

	public void setDrpNm(String drpNm) {
		this.drpNm = drpNm;
	}

	public String getElecGnr() {
		return elecGnr;
	}

	public void setElecGnr(String elecGnr) {
		this.elecGnr = elecGnr;
	}

	public String getElecGnrnm() {
		return elecGnrnm;
	}

	public void setElecGnrnm(String elecGnrnm) {
		this.elecGnrnm = elecGnrnm;
	}

	public String getPwrOtr() {
		return pwrOtr;
	}

	public void setPwrOtr(String pwrOtr) {
		this.pwrOtr = pwrOtr;
	}

	public String getPwrOtrnm() {
		return pwrOtrnm;
	}

	public void setPwrOtrnm(String pwrOtrnm) {
		this.pwrOtrnm = pwrOtrnm;
	}

	public String getEnvMnt() {
		return envMnt;
	}

	public void setEnvMnt(String envMnt) {
		this.envMnt = envMnt;
	}

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String getTmpNm() {
		return tmpNm;
	}

	public void setTmpNm(String tmpNm) {
		this.tmpNm = tmpNm;
	}

	public String getSmk() {
		return smk;
	}

	public void setSmk(String smk) {
		this.smk = smk;
	}

	public String getSmkNm() {
		return smkNm;
	}

	public void setSmkNm(String smkNm) {
		this.smkNm = smkNm;
	}

	public String getIfr() {
		return ifr;
	}

	public void setIfr(String ifr) {
		this.ifr = ifr;
	}

	public String getIfrNm() {
		return ifrNm;
	}

	public void setIfrNm(String ifrNm) {
		this.ifrNm = ifrNm;
	}

	public String getDrLck() {
		return drLck;
	}

	public void setDrLck(String drLck) {
		this.drLck = drLck;
	}

	public String getDrLcknm() {
		return drLcknm;
	}

	public void setDrLcknm(String drLcknm) {
		this.drLcknm = drLcknm;
	}

	public String getNwwnd() {
		return nwwnd;
	}

	public void setNwwnd(String nwwnd) {
		this.nwwnd = nwwnd;
	}

	public String getNwwndNm() {
		return nwwndNm;
	}

	public void setNwwndNm(String nwwndNm) {
		this.nwwndNm = nwwndNm;
	}

	public String getNpDrp() {
		return npDrp;
	}

	public void setNpDrp(String npDrp) {
		this.npDrp = npDrp;
	}

	public String getNpDrpnm() {
		return npDrpnm;
	}

	public void setNpDrpnm(String npDrpnm) {
		this.npDrpnm = npDrpnm;
	}

	public String getMntOtr() {
		return mntOtr;
	}

	public void setMntOtr(String mntOtr) {
		this.mntOtr = mntOtr;
	}

	public String getMntOtrnm() {
		return mntOtrnm;
	}

	public void setMntOtrnm(String mntOtrnm) {
		this.mntOtrnm = mntOtrnm;
	}

	public String getSecSys() {
		return secSys;
	}

	public void setSecSys(String secSys) {
		this.secSys = secSys;
	}

	public String getSecSysnts() {
		return secSysnts;
	}

	public void setSecSysnts(String secSysnts) {
		this.secSysnts = secSysnts;
	}

	public String getDrCnr() {
		return drCnr;
	}

	public void setDrCnr(String drCnr) {
		this.drCnr = drCnr;
	}

	public String getDrCnrnts() {
		return drCnrnts;
	}

	public void setDrCnrnts(String drCnrnts) {
		this.drCnrnts = drCnrnts;
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
  
