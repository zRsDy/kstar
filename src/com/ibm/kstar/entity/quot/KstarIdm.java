/** 
 * Project Name:crmp 
 * File Name:KstarIdm.java 
 * Package Name:com.kstar.crm.quot 
 * Date:Dec 29, 20166:54:33 PM 
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
 * ClassName:KstarIdm <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 29, 2016 6:54:33 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_IDM") 
public class KstarIdm implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator = "kstaridm_id_generator")
	@GenericGenerator(name="kstaridm_id_generator", strategy="uuid")
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
	
	// 每个模块机柜数量
	@Column(name = "C_RCK_NUM")
	private String rckNm;
	
	// 机柜尺寸要求
	@Column(name = "C_RCK_SIZE")
	private String rckSz;
	
	// 后期扩容要求
	@Column(name = "C_ENEXT_RQ")
	private String enextRq;
	
	// 市电输入
	@Column(name = "C_ELEC_INT")
	private String elecInt;
	
	// 电池后备时间
	@Column(name = "C_BTTR_BKTM")
	private String bttrBktm;
	
	// 配电柜需求
	@Column(name = "C_ELCRCK_RQ")
	private String elcrckRq;
	
	// 机柜供电回路
	@Column(name = "C_RCK_CRC")
	private String rckCrc;
	
	// 机房布线
	@Column(name = "C_RM_LNDP")
	private String rmLndp;
	
	// 电池安装位置
	@Column(name = "C_BTR_INSPS")
	private String btrInsps;
	
	// PDU输出插孔
	@Column(name = "C_PDU_OUT")
	private String pduOut;
	
	// PDU输出路数
	@Column(name = "C_PDU_OTNM")
	private String pduOtnm;
	
	// PDU选配功能
	@Column(name = "C_PDU_EXFN")
	private String pduExfn;
	
	// 空调制冷方式
	@Column(name = "C_CLR_CLDTYP")
	private String clrCldtyp;
	
	// 空调制冷方式其它
	@Column(name = "C_CLR_CLDNTS")
	private String clrCldnts;
	
	// 空调送风方式
	@Column(name = "C_CLR_WNDTYP")
	private String clrCldwndtyp;
	
	// 空调送风方式其它
	@Column(name = "C_CLR_WNDNTS")
	private String clrCldwndnts;
	
	// 机型选择
	@Column(name = "C_MCH_TYP")
	private String mchTyp;
	
	// 室外机位置
	@Column(name = "C_MCH_PST")
	private String mchPst;
	
	// 冗余需求
	@Column(name = "C_RDM_REQ")
	private String rdmRq;
	
	// 室外机与室内机之间的高度差
	@Column(name = "C_MCH_HGT")
	private String mchHgt;
	
	// 室内机与室外机的管程长度
	@Column(name = "C_MCHPP_LNG")
	private String mchppLng;
	
	// 是否有给排水
	@Column(name = "C_IF_WTR")
	private String ifWtr;
	
	// 是否封闭通道
	@Column(name = "C_IF_CLSPP")
	private String ifClspp;
	
	// 封闭通道形式
	@Column(name = "C_CLSPP_TYP")
	private String clsppTyp;
	
	// 机柜品牌
	@Column(name = "C_RCK_BRD")
	private String rckBrd;
	
	// 封闭通道门
	@Column(name = "C_CLS_PP")
	private String clsPp;
	
	// 是否需要监控
	@Column(name = "C_IF_MNTR")
	private String ifMntr;
	
	// 告警需求
	@Column(name = "C_ALM_RQ")
	private String almRq;
	
	// 烟感数量
	@Column(name = "N_SMK_NM")
	private String smkNm;
	
	// 温湿度数量
	@Column(name = "N_TMPWT_NM")
	private String tmpwtNm;
	
	// 红外探测数量
	@Column(name = "N_INF_NM")
	private String infNm;
	
	// 漏水监测数量
	@Column(name = "N_DRP_NM")
	private String drpNm;
	
	// 门禁门的数量
	@Column(name = "N_DR_NM")
	private String drNm;
	
	// 漏水检测方式
	@Column(name = "C_DRP_TYP")
	private String drpTyp;
	
	// 开门方式
	@Column(name = "C_DR_TYP")
	private String drTyp;
	
	// 门锁方式
	@Column(name = "C_DRLCK_TYP")
	private String drlckTyp;
	
	// 视频监控数量
	@Column(name = "N_CCTV_NM")
	private String cctvNm;
	
	// 视频像素要求
	@Column(name = "C_VD_RQ")
	private String vdRq;
	
	// 视频存储时间
	@Column(name = "C_VD_TM")
	private String vdTm;
	
	// 原厂安装调试
	@Column(name = "C_INS_DBG")
	private String insDbg;
	
	// 电池监控
	@Column(name = "C_BTT_MNT")
	private String bttMnt;
	
	// 第三方设备情况
	@Column(name = "C_TRD_DVC")
	private String trdDvc;

	
	public KstarIdm() {
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

	public String getRckNm() {
		return rckNm;
	}

	public void setRckNm(String rckNm) {
		this.rckNm = rckNm;
	}

	public String getRckSz() {
		return rckSz;
	}

	public void setRckSz(String rckSz) {
		this.rckSz = rckSz;
	}

	public String getEnextRq() {
		return enextRq;
	}

	public void setEnextRq(String enextRq) {
		this.enextRq = enextRq;
	}

	public String getElecInt() {
		return elecInt;
	}

	public void setElecInt(String elecInt) {
		this.elecInt = elecInt;
	}

	public String getBttrBktm() {
		return bttrBktm;
	}

	public void setBttrBktm(String bttrBktm) {
		this.bttrBktm = bttrBktm;
	}

	public String getElcrckRq() {
		return elcrckRq;
	}

	public void setElcrckRq(String elcrckRq) {
		this.elcrckRq = elcrckRq;
	}

	public String getRckCrc() {
		return rckCrc;
	}

	public void setRckCrc(String rckCrc) {
		this.rckCrc = rckCrc;
	}

	public String getRmLndp() {
		return rmLndp;
	}

	public void setRmLndp(String rmLndp) {
		this.rmLndp = rmLndp;
	}

	public String getBtrInsps() {
		return btrInsps;
	}

	public void setBtrInsps(String btrInsps) {
		this.btrInsps = btrInsps;
	}

	public String getPduOut() {
		return pduOut;
	}

	public void setPduOut(String pduOut) {
		this.pduOut = pduOut;
	}

	public String getPduOtnm() {
		return pduOtnm;
	}

	public void setPduOtnm(String pduOtnm) {
		this.pduOtnm = pduOtnm;
	}

	public String getPduExfn() {
		return pduExfn;
	}

	public void setPduExfn(String pduExfn) {
		this.pduExfn = pduExfn;
	}

	public String getClrCldtyp() {
		return clrCldtyp;
	}

	public void setClrCldtyp(String clrCldtyp) {
		this.clrCldtyp = clrCldtyp;
	}

	public String getClrCldwndtyp() {
		return clrCldwndtyp;
	}

	public void setClrCldwndtyp(String clrCldwndtyp) {
		this.clrCldwndtyp = clrCldwndtyp;
	}
	
	public String getClrCldnts() {
		return clrCldnts;
	}

	public void setClrCldnts(String clrCldnts) {
		this.clrCldnts = clrCldnts;
	}

	public String getClrCldwndnts() {
		return clrCldwndnts;
	}

	public void setClrCldwndnts(String clrCldwndnts) {
		this.clrCldwndnts = clrCldwndnts;
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

	public String getRdmRq() {
		return rdmRq;
	}

	public void setRdmRq(String rdmRq) {
		this.rdmRq = rdmRq;
	}

	public String getMchHgt() {
		return mchHgt;
	}

	public void setMchHgt(String mchHgt) {
		this.mchHgt = mchHgt;
	}

	public String getMchppLng() {
		return mchppLng;
	}

	public void setMchppLng(String mchppLng) {
		this.mchppLng = mchppLng;
	}

	public String getIfWtr() {
		return ifWtr;
	}

	public void setIfWtr(String ifWtr) {
		this.ifWtr = ifWtr;
	}

	public String getIfClspp() {
		return ifClspp;
	}

	public void setIfClspp(String ifClspp) {
		this.ifClspp = ifClspp;
	}

	public String getClsppTyp() {
		return clsppTyp;
	}

	public void setClsppTyp(String clsppTyp) {
		this.clsppTyp = clsppTyp;
	}

	public String getRckBrd() {
		return rckBrd;
	}

	public void setRckBrd(String rckBrd) {
		this.rckBrd = rckBrd;
	}

	public String getClsPp() {
		return clsPp;
	}

	public void setClsPp(String clsPp) {
		this.clsPp = clsPp;
	}

	public String getIfMntr() {
		return ifMntr;
	}

	public void setIfMntr(String ifMntr) {
		this.ifMntr = ifMntr;
	}

	public String getAlmRq() {
		return almRq;
	}

	public void setAlmRq(String almRq) {
		this.almRq = almRq;
	}

	public String getSmkNm() {
		return smkNm;
	}

	public void setSmkNm(String smkNm) {
		this.smkNm = smkNm;
	}

	public String getTmpwtNm() {
		return tmpwtNm;
	}

	public void setTmpwtNm(String tmpwtNm) {
		this.tmpwtNm = tmpwtNm;
	}

	public String getInfNm() {
		return infNm;
	}

	public void setInfNm(String infNm) {
		this.infNm = infNm;
	}

	public String getDrpNm() {
		return drpNm;
	}

	public void setDrpNm(String drpNm) {
		this.drpNm = drpNm;
	}

	public String getDrNm() {
		return drNm;
	}

	public void setDrNm(String drNm) {
		this.drNm = drNm;
	}

	public String getDrpTyp() {
		return drpTyp;
	}

	public void setDrpTyp(String drpTyp) {
		this.drpTyp = drpTyp;
	}

	public String getDrTyp() {
		return drTyp;
	}

	public void setDrTyp(String drTyp) {
		this.drTyp = drTyp;
	}

	public String getDrlckTyp() {
		return drlckTyp;
	}

	public void setDrlckTyp(String drlckTyp) {
		this.drlckTyp = drlckTyp;
	}

	public String getCctvNm() {
		return cctvNm;
	}

	public void setCctvNm(String cctvNm) {
		this.cctvNm = cctvNm;
	}

	public String getVdRq() {
		return vdRq;
	}

	public void setVdRq(String vdRq) {
		this.vdRq = vdRq;
	}

	public String getVdTm() {
		return vdTm;
	}

	public void setVdTm(String vdTm) {
		this.vdTm = vdTm;
	}

	public String getInsDbg() {
		return insDbg;
	}

	public void setInsDbg(String insDbg) {
		this.insDbg = insDbg;
	}

	public String getBttMnt() {
		return bttMnt;
	}

	public void setBttMnt(String bttMnt) {
		this.bttMnt = bttMnt;
	}

	public String getTrdDvc() {
		return trdDvc;
	}

	public void setTrdDvc(String trdDvc) {
		this.trdDvc = trdDvc;
	}

	public String getPrjtchRqnm() {
		return prjtchRqnm;
	}

	public void setPrjtchRqnm(String prjtchRqnm) {
		this.prjtchRqnm = prjtchRqnm;
	}
	
}
  
