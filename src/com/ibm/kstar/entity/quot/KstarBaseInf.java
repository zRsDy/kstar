
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
 * ClassName:KstarBaseInf <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Dec 27, 2016 11:52:02 AM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Entity
@Table(name = "CRM_T_BASE_INF")
public class KstarBaseInf implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator = "kstarbsif_id_generator")
	@GenericGenerator(name = "kstarbsif_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;

	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;

	// 类型
	@Column(name = "C_TYPE")
	private String CType;

	// 机房图纸
	@Column(name = "C_DRAFT")
	private String draft;

	// 机房图纸名称
	@Column(name = "C_DRAFTNM")
	private String draftNm;
	
	// 项目名称
	@Column(name = "C_PROJ_NAME")
	private String prjNm;

	// 当地海拔
	@Column(name = "N_HEIGHT")
	private String height;

	// 建设等级
	@Column(name = "C_CNST_LEVEL")
	private String cnLvl;

	// 项目地址
	@Column(name = "C_PROJ_ADDR")
	private String prjAddr;

	// 项目预算
	@Column(name = "C_PROJ_BUDGET")
	private String prjBgt;

	// 机房性质
	@Column(name = "C_RM_TYPE")
	private String rmTyp;

	// 集成产品
	@Column(name = "C_INT_PRD_TYPE")
	private String intPRDTyp;

	// 相关产品
	@Column(name = "C_REL_PRD")
	private String relPrd;

	// 机房面积的长度
	@Column(name = "N_RM_LENGTH")
	private String rmLng;

	// 机房面积的宽度
	@Column(name = "N_RM_WIDTH")
	private String rmWdt;

	// 机房楼层
	@Column(name = "N_RM_LEVEL")
	private String rmLvl;

	// 机房楼层的说明
	@Column(name = "C_RM_LEVEL_NOTES")
	private String rmLvlNts;

	// 机房梁下净高
	@Column(name = "N_RM_LEVEL_HGHT")
	private String rmLvlHgh;

	// 机房梁下净高的说明
	@Column(name = "C_RM_LEVEL_HGHT_NTS")
	private String rmLvlHghNts;

	// 楼层承重
	@Column(name = "N_LEVEL_WGHT")
	private String lvlWgh;

	// 楼层承重的说明
	@Column(name = "C_LEVEL_WGHT_NTS")
	private String lvlWghNts;

	// 是否为高架地板
	@Column(name = "C_IF_HGH_FLR")
	private String ifHghFlr;

	// 高架地板长度
	@Column(name = "N_HGH_FLR")
	private String hghFlr;

	// 高架地板的说明
	@Column(name = "C_HGH_FLR_NTS")
	private String hghFlrNts;

	// 项目其他情况说明
	@Column(name = "C_PRJ_OTHER_NTS")
	private String prjThrNts;

	// 多通道或机柜功率不均提供设备布局图
	@Column(name = "C_MH_LAYOUT")
	private String mhLyt;
	
	// 多通道或机柜功率不均提供设备布局图名称
	@Column(name = "C_MH_LAYOUTNM")
	private String mhLytNm;

	// 负载类型
	@Column(name = "C_LOAD_TYPE")
	private String ldTyp;
	
	// 负载类型说明
	@Column(name = "C_LOAD_TYP_NTS")
	private String ldTypnts;

	// 服务器机柜（若有）
	@Column(name = "N_SERVER_NUM")
	private String srvNm;

	// 单机柜功率
	@Column(name = "N_SERVER_PWR")
	private String srvPwr;

	// 机柜尺寸要求(宽×高×长)
	@Column(name = "C_SERVER_SIZE")
	private String srvSz;

	// 后期扩容要求
	@Column(name = "C_EXTENSION")
	private String extn;

	// 负载其他情况说明
	@Column(name = "C_LD_OTHER_NTS")
	private String ldThrNts;

	// 外购件技术要求
	@Column(name = "C_TECH_REQ_PRHS")
	private String tchRqPrs;

	// 外购件安装服务要求
	@Column(name = "C_INT_REQ_PRHS")
	private String intRqPrs;

	// 外购件物流要求
	@Column(name = "C_LGST_REQ_PRHS")
	private String lgstRqPrs;

	// 只负责开机
	@Column(name = "C_TURN_ON")
	private String trnOn;

	// 只负责开机的相关产品
	@Column(name = "C_TURN_ON_PRD")
	private String trnOnPrd;

	// 安装督导
	@Column(name = "C_INS_MNTR")
	private String insMntr;

	// 安装督导的相关产品
	@Column(name = "C_INS_MNTR_PRD")
	private String insMntrPrd;

	// 安装（不含施工材料）
	@Column(name = "C_INS")
	private String ins;

	// 安装（不含施工材料）的相关产品
	@Column(name = "C_INS_PRD")
	private String insPrd;

	// 交钥匙工程
	@Column(name = "C_KEY")
	private String cKey;

	// 交钥匙工程的相关产品
	@Column(name = "C_KEY_PRD")
	private String cKeyPrd;

	// 要求到货时间
	@Column(name = "DT_DEL_READY")
	private Date dtDelRdy;

	// 要求详细的到货地点（具体到楼层）
	@Column(name = "C_DEL_ADDR")
	private String delAddr;

	// 分批发货说明（若有）
	@Column(name = "C_BATCH_DEL")
	private String btchDl;

	// 包装标签需求
	@Column(name = "C_PCKG_REQ")
	private String pkgRq;

	// 商务部分的其他特殊需求
	@Column(name = "C_BSS_OTHER_NTS")
	private String bssThrNts;

	// 要货申请
	@Column(name = "C_ORD_REQ")
	private String ordRq;
	
	// 要货申请名称
	@Column(name = "C_ORD_REQNM")
	private String ordRqnm;

	// 相关产品UPS
	@Column(name = "C_IF_UPS")
	private String ifups;

	// 相关产品电池
	@Column(name = "C_IF_BATTRY")
	private String ifbattry;
	
	// 相关产品精密空调
	@Column(name = "C_IF_CLR")
	private String ifclr;
	
	// 相关产品配电
	@Column(name = "C_IF_ELEC")
	private String ifelec;
	
	// 相关产品机柜
	@Column(name = "C_IF_RCK")
	private String ifrck;
	
	// 相关产品监控
	@Column(name = "C_IF_MNT")
	private String ifmnt;
	
	// 集成产品IDM
	@Column(name = "C_IF_IDM")
	private String ifidm;
	
	// 集成产品IDU
	@Column(name = "C_IF_IDU")
	private String ifidu;
	
	// 集成产品IDR
	@Column(name = "C_IF_IDR")
	private String ifidr;
	
	
	
	public KstarBaseInf() {
		super();
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

	public String getDraft() {
		return draft;
	}

	public void setDraft(String draft) {
		this.draft = draft;
	}

	public String getPrjNm() {
		return prjNm;
	}

	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getCnLvl() {
		return cnLvl;
	}

	public void setCnLvl(String cnLvl) {
		this.cnLvl = cnLvl;
	}

	public String getPrjAddr() {
		return prjAddr;
	}

	public void setPrjAddr(String prjAddr) {
		this.prjAddr = prjAddr;
	}

	public String getPrjBgt() {
		return prjBgt;
	}

	public void setPrjBgt(String prjBgt) {
		this.prjBgt = prjBgt;
	}

	public String getRmTyp() {
		return rmTyp;
	}

	public void setRmTyp(String rmTyp) {
		this.rmTyp = rmTyp;
	}

	public String getIntPRDTyp() {
		return intPRDTyp;
	}

	public void setIntPRDTyp(String intPRDTyp) {
		this.intPRDTyp = intPRDTyp;
	}

	public String getRelPrd() {
		return relPrd;
	}

	public void setRelPrd(String relPrd) {
		this.relPrd = relPrd;
	}

	public String getRmLng() {
		return rmLng;
	}

	public void setRmLng(String rmLng) {
		this.rmLng = rmLng;
	}

	public String getRmWdt() {
		return rmWdt;
	}

	public void setRmWdt(String rmWdt) {
		this.rmWdt = rmWdt;
	}

	public String getRmLvl() {
		return rmLvl;
	}

	public void setRmLvl(String rmLvl) {
		this.rmLvl = rmLvl;
	}

	public String getRmLvlNts() {
		return rmLvlNts;
	}

	public void setRmLvlNts(String rmLvlNts) {
		this.rmLvlNts = rmLvlNts;
	}

	public String getRmLvlHgh() {
		return rmLvlHgh;
	}

	public void setRmLvlHgh(String rmLvlHgh) {
		this.rmLvlHgh = rmLvlHgh;
	}

	public String getRmLvlHghNts() {
		return rmLvlHghNts;
	}

	public void setRmLvlHghNts(String rmLvlHghNts) {
		this.rmLvlHghNts = rmLvlHghNts;
	}

	public String getLvlWgh() {
		return lvlWgh;
	}

	public void setLvlWgh(String lvlWgh) {
		this.lvlWgh = lvlWgh;
	}

	public String getLvlWghNts() {
		return lvlWghNts;
	}

	public void setLvlWghNts(String lvlWghNts) {
		this.lvlWghNts = lvlWghNts;
	}

	public String getIfHghFlr() {
		return ifHghFlr;
	}

	public void setIfHghFlr(String ifHghFlr) {
		this.ifHghFlr = ifHghFlr;
	}

	public String getHghFlr() {
		return hghFlr;
	}

	public void setHghFlr(String hghFlr) {
		this.hghFlr = hghFlr;
	}

	public String getHghFlrNts() {
		return hghFlrNts;
	}

	public void setHghFlrNts(String hghFlrNts) {
		this.hghFlrNts = hghFlrNts;
	}

	public String getPrjThrNts() {
		return prjThrNts;
	}

	public void setPrjThrNts(String prjThrNts) {
		this.prjThrNts = prjThrNts;
	}

	public String getMhLyt() {
		return mhLyt;
	}

	public void setMhLyt(String mhLyt) {
		this.mhLyt = mhLyt;
	}

	public String getLdTyp() {
		return ldTyp;
	}

	public void setLdTyp(String ldTyp) {
		this.ldTyp = ldTyp;
	}
	
	public String getLdTypnts() {
		return ldTypnts;
	}

	public void setLdTypnts(String ldTypnts) {
		this.ldTypnts = ldTypnts;
	}

	public String getSrvNm() {
		return srvNm;
	}

	public void setSrvNm(String srvNm) {
		this.srvNm = srvNm;
	}

	public String getSrvPwr() {
		return srvPwr;
	}

	public void setSrvPwr(String srvPwr) {
		this.srvPwr = srvPwr;
	}

	public String getSrvSz() {
		return srvSz;
	}

	public void setSrvSz(String srvSz) {
		this.srvSz = srvSz;
	}

	public String getExtn() {
		return extn;
	}

	public void setExtn(String extn) {
		this.extn = extn;
	}

	public String getLdThrNts() {
		return ldThrNts;
	}

	public void setLdThrNts(String ldThrNts) {
		this.ldThrNts = ldThrNts;
	}

	public String getTchRqPrs() {
		return tchRqPrs;
	}

	public void setTchRqPrs(String tchRqPrs) {
		this.tchRqPrs = tchRqPrs;
	}

	public String getIntRqPrs() {
		return intRqPrs;
	}

	public void setIntRqPrs(String intRqPrs) {
		this.intRqPrs = intRqPrs;
	}

	public String getLgstRqPrs() {
		return lgstRqPrs;
	}

	public void setLgstRqPrs(String lgstRqPrs) {
		this.lgstRqPrs = lgstRqPrs;
	}

	public String getTrnOn() {
		return trnOn;
	}

	public void setTrnOn(String trnOn) {
		this.trnOn = trnOn;
	}

	public String getTrnOnPrd() {
		return trnOnPrd;
	}

	public void setTrnOnPrd(String trnOnPrd) {
		this.trnOnPrd = trnOnPrd;
	}

	public String getInsMntr() {
		return insMntr;
	}

	public void setInsMntr(String insMntr) {
		this.insMntr = insMntr;
	}

	public String getInsMntrPrd() {
		return insMntrPrd;
	}

	public void setInsMntrPrd(String insMntrPrd) {
		this.insMntrPrd = insMntrPrd;
	}

	public String getIns() {
		return ins;
	}

	public void setIns(String ins) {
		this.ins = ins;
	}

	public String getInsPrd() {
		return insPrd;
	}

	public void setInsPrd(String insPrd) {
		this.insPrd = insPrd;
	}

	public String getcKey() {
		return cKey;
	}

	public void setcKey(String cKey) {
		this.cKey = cKey;
	}

	public String getcKeyPrd() {
		return cKeyPrd;
	}

	public void setcKeyPrd(String cKeyPrd) {
		this.cKeyPrd = cKeyPrd;
	}

	public String getDtDelRdy() {
		if (dtDelRdy != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(dtDelRdy);
		}
		return null;
	}

	public void setDtDelRdy(Date dtDelRdy) {
		this.dtDelRdy = dtDelRdy;
	}

	public String getDelAddr() {
		return delAddr;
	}

	public void setDelAddr(String delAddr) {
		this.delAddr = delAddr;
	}

	public String getBtchDl() {
		return btchDl;
	}

	public void setBtchDl(String btchDl) {
		this.btchDl = btchDl;
	}

	public String getPkgRq() {
		return pkgRq;
	}

	public void setPkgRq(String pkgRq) {
		this.pkgRq = pkgRq;
	}

	public String getBssThrNts() {
		return bssThrNts;
	}

	public void setBssThrNts(String bssThrNts) {
		this.bssThrNts = bssThrNts;
	}

	public String getOrdRq() {
		return ordRq;
	}

	public void setOrdRq(String ordRq) {
		this.ordRq = ordRq;
	}

	public String getIfups() {
		return ifups;
	}

	public void setIfups(String ifups) {
		this.ifups = ifups;
	}

	public String getIfbattry() {
		return ifbattry;
	}

	public void setIfbattry(String ifbattry) {
		this.ifbattry = ifbattry;
	}

	public String getIfclr() {
		return ifclr;
	}

	public void setIfclr(String ifclr) {
		this.ifclr = ifclr;
	}

	public String getIfelec() {
		return ifelec;
	}

	public void setIfelec(String ifelec) {
		this.ifelec = ifelec;
	}

	public String getIfrck() {
		return ifrck;
	}

	public void setIfrck(String ifrck) {
		this.ifrck = ifrck;
	}

	public String getIfmnt() {
		return ifmnt;
	}

	public void setIfmnt(String ifmnt) {
		this.ifmnt = ifmnt;
	}

	public String getIfidm() {
		return ifidm;
	}

	public void setIfidm(String ifidm) {
		this.ifidm = ifidm;
	}

	public String getIfidu() {
		return ifidu;
	}

	public void setIfidu(String ifidu) {
		this.ifidu = ifidu;
	}

	public String getIfidr() {
		return ifidr;
	}

	public void setIfidr(String ifidr) {
		this.ifidr = ifidr;
	}

	public String getDraftNm() {
		return draftNm;
	}

	public void setDraftNm(String draftNm) {
		this.draftNm = draftNm;
	}

	public String getMhLytNm() {
		return mhLytNm;
	}

	public void setMhLytNm(String mhLytNm) {
		this.mhLytNm = mhLytNm;
	}

	public String getOrdRqnm() {
		return ordRqnm;
	}

	public void setOrdRqnm(String ordRqnm) {
		this.ordRqnm = ordRqnm;
	}

}
