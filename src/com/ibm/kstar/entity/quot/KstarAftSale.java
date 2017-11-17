
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
 * ClassName:KstarAftSale <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Dec 29, 2016 1:57:45 PM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Entity
@Table(name = "CRM_T_AFT_SALE")
public class KstarAftSale implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarafts_id_generator")
	@GenericGenerator(name = "kstarafts_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;

	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;

	// 类型
	@Column(name = "C_TYPE")
	private String CType;

	// 设备布局图
	@Column(name = "C_DVC_DRAFT")
	private String dvcDrt;
	
	// 设备布局图名称
	@Column(name = "C_DVC_DRAFTNM")
	private String dvcDrtnm;

	// 等效联机管长
	@Column(name = "N_DXPP_LENGTH")
	private String dxppLnt;

	// 是否需配延长组件
	@Column(name = "C_IF_EXT_PRT")
	private String ifextPrt;

	// 室外机与室内机高度落差
	@Column(name = "N_HEIGHT_DIFF")
	private String hghtDff;

	// 低温型室外机需求
	@Column(name = "C_IF_LWMCH")
	private String ifLwmch;

	// 水冷机组水管接口管长度
	@Column(name = "N_XLPP_LENGTH")
	private String xlppLnt;

	// 送风型式
	@Column(name = "C_WND_TYPE")
	private String wndTyp;

	// 风管尺寸(宽×高×长)
	@Column(name = "C_WNPP_LENGTH")
	private String wnppLnt;

	// 静电地板高度
	@Column(name = "N_ESD_FLHGHT")
	private String esdFlt;

	// 地板通风率
	@Column(name = "N_WND_RATE")
	private String wndRte;

	// 加湿排水管长
	@Column(name = "N_JPPP_LENGTH")
	private String jpppLng;

	// 加湿进水管长
	@Column(name = "N_JJPP_LENGTH")
	private String jjppLng;

	// 冷凝排水管长
	@Column(name = "N_LPPP_LENGTH")
	private String lpppLng;

	// 室内机线缆长
	@Column(name = "N_MCH_LNLEN")
	private String mchLnln;

	// 安装送风组件条件
	@Column(name = "C_INS_WNDREQ")
	private String insWndrq;

	// 是否需要对空调解体搬运
	@Column(name = "C_IF_DIP")
	private String ifDip;

	// 如需外请吊装，情况另外说明
	@Column(name = "C_HNG_NTS")
	private String hngNts;

	// UPS,机柜，配电，电池的设备布局图
	@Column(name = "C_UMB_DRAFT")
	private String umbDrf;
	
	// UPS,机柜，配电，电池的设备布局图名称
	@Column(name = "C_UMB_DRAFTNM")
	private String umbDrfnm;

	// 现场温湿度与通风情况
	@Column(name = "C_TMPWND_NTS")
	private String tmpwndNts;

	// 现场金属粉尘评估
	@Column(name = "C_DUST_EVAL")
	private String dustEvl;

	// 前端是否使用油机
	@Column(name = "C_IF_FRTMCH")
	private String ifFrtmch;

	// 配电系统
	@Column(name = "C_EXT_ELEC")
	private String extElec;

	// 前端配电大小
	@Column(name = "C_FRT_ELEC")
	private String frtElec;

	// 安装空间与梁下净高
	@Column(name = "C_INS_HGHT")
	private String insHgt;

	// 维修环境
	@Column(name = "C_FIX_ENV")
	private String fixEnv;

	// 入场方式与电梯门大小
	@Column(name = "C_ENT_DRSZE")
	private String entDrsze;

	// 安装工具
	@Column(name = "C_INS_TOOL")
	private String insTl;

	// 入场时间
	@Column(name = "C_ENTR_DT")
	private Date entrDt;

	// 进出线方式
	@Column(name = "C_INOUT_TYPE")
	private String inotTyp;

	// 线缆安装方式
	@Column(name = "C_LN_INSTYP")
	private String lnInstyp;

	// UPS负载类型与容量
	@Column(name = "C_UPS_LDTYP")
	private String upsLdtyp;

	// 防护情况
	@Column(name = "C_DFT_ST")
	private String dftSt;

	// 设备间距是否符合维修条件
	@Column(name = "C_IF_DVCDST")
	private String ifDvcdst;

	
	public KstarAftSale() {
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

	public String getDvcDrt() {
		return dvcDrt;
	}

	public void setDvcDrt(String dvcDrt) {
		this.dvcDrt = dvcDrt;
	}

	public String getDxppLnt() {
		return dxppLnt;
	}

	public void setDxppLnt(String dxppLnt) {
		this.dxppLnt = dxppLnt;
	}

	public String getIfextPrt() {
		return ifextPrt;
	}

	public void setIfextPrt(String ifextPrt) {
		this.ifextPrt = ifextPrt;
	}

	public String getHghtDff() {
		return hghtDff;
	}

	public void setHghtDff(String hghtDff) {
		this.hghtDff = hghtDff;
	}

	public String getIfLwmch() {
		return ifLwmch;
	}

	public void setIfLwmch(String ifLwmch) {
		this.ifLwmch = ifLwmch;
	}

	public String getXlppLnt() {
		return xlppLnt;
	}

	public void setXlppLnt(String xlppLnt) {
		this.xlppLnt = xlppLnt;
	}

	public String getWndTyp() {
		return wndTyp;
	}

	public void setWndTyp(String wndTyp) {
		this.wndTyp = wndTyp;
	}

	public String getWnppLnt() {
		return wnppLnt;
	}

	public void setWnppLnt(String wnppLnt) {
		this.wnppLnt = wnppLnt;
	}

	public String getEsdFlt() {
		return esdFlt;
	}

	public void setEsdFlt(String esdFlt) {
		this.esdFlt = esdFlt;
	}

	public String getWndRte() {
		return wndRte;
	}

	public void setWndRte(String wndRte) {
		this.wndRte = wndRte;
	}

	public String getJpppLng() {
		return jpppLng;
	}

	public void setJpppLng(String jpppLng) {
		this.jpppLng = jpppLng;
	}

	public String getJjppLng() {
		return jjppLng;
	}

	public void setJjppLng(String jjppLng) {
		this.jjppLng = jjppLng;
	}

	public String getLpppLng() {
		return lpppLng;
	}

	public void setLpppLng(String lpppLng) {
		this.lpppLng = lpppLng;
	}

	public String getMchLnln() {
		return mchLnln;
	}

	public void setMchLnln(String mchLnln) {
		this.mchLnln = mchLnln;
	}

	public String getInsWndrq() {
		return insWndrq;
	}

	public void setInsWndrq(String insWndrq) {
		this.insWndrq = insWndrq;
	}

	public String getIfDip() {
		return ifDip;
	}

	public void setIfDip(String ifDip) {
		this.ifDip = ifDip;
	}

	public String getHngNts() {
		return hngNts;
	}

	public void setHngNts(String hngNts) {
		this.hngNts = hngNts;
	}

	public String getUmbDrf() {
		return umbDrf;
	}

	public void setUmbDrf(String umbDrf) {
		this.umbDrf = umbDrf;
	}

	public String getTmpwndNts() {
		return tmpwndNts;
	}

	public void setTmpwndNts(String tmpwndNts) {
		this.tmpwndNts = tmpwndNts;
	}

	public String getDustEvl() {
		return dustEvl;
	}

	public void setDustEvl(String dustEvl) {
		this.dustEvl = dustEvl;
	}

	public String getIfFrtmch() {
		return ifFrtmch;
	}

	public void setIfFrtmch(String ifFrtmch) {
		this.ifFrtmch = ifFrtmch;
	}

	public String getExtElec() {
		return extElec;
	}

	public void setExtElec(String extElec) {
		this.extElec = extElec;
	}

	public String getFrtElec() {
		return frtElec;
	}

	public void setFrtElec(String frtElec) {
		this.frtElec = frtElec;
	}

	public String getInsHgt() {
		return insHgt;
	}

	public void setInsHgt(String insHgt) {
		this.insHgt = insHgt;
	}

	public String getFixEnv() {
		return fixEnv;
	}

	public void setFixEnv(String fixEnv) {
		this.fixEnv = fixEnv;
	}

	public String getEntDrsze() {
		return entDrsze;
	}

	public void setEntDrsze(String entDrsze) {
		this.entDrsze = entDrsze;
	}

	public String getInsTl() {
		return insTl;
	}

	public void setInsTl(String insTl) {
		this.insTl = insTl;
	}

	public String getEntrDt() {
		if (entrDt != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(entrDt);
		}
		return null;
	}

	public void setEntrDt(Date entrDt) {
		this.entrDt = entrDt;
	}

	public String getInotTyp() {
		return inotTyp;
	}

	public void setInotTyp(String inotTyp) {
		this.inotTyp = inotTyp;
	}

	public String getLnInstyp() {
		return lnInstyp;
	}

	public void setLnInstyp(String lnInstyp) {
		this.lnInstyp = lnInstyp;
	}

	public String getUpsLdtyp() {
		return upsLdtyp;
	}

	public void setUpsLdtyp(String upsLdtyp) {
		this.upsLdtyp = upsLdtyp;
	}

	public String getDftSt() {
		return dftSt;
	}

	public void setDftSt(String dftSt) {
		this.dftSt = dftSt;
	}

	public String getIfDvcdst() {
		return ifDvcdst;
	}

	public void setIfDvcdst(String ifDvcdst) {
		this.ifDvcdst = ifDvcdst;
	}

	public String getDvcDrtnm() {
		return dvcDrtnm;
	}

	public void setDvcDrtnm(String dvcDrtnm) {
		this.dvcDrtnm = dvcDrtnm;
	}

	public String getUmbDrfnm() {
		return umbDrfnm;
	}

	public void setUmbDrfnm(String umbDrfnm) {
		this.umbDrfnm = umbDrfnm;
	}

}
