      
package com.ibm.kstar.entity.quot;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.cache.CacheUtils;


 
/** 
 * 合同工程清单
 * ClassName:KstarPrjLst <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 26, 2016 1:45:41 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_PRJ_LST")
public class KstarPrjLst implements Serializable {

	/**
	 * serialVersionUID:TODO
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarprjlst_id_generator")
	@GenericGenerator(name = "kstarprjlst_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// lovId
	@Column(name = "C_LVID")
	private String lvId;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 产品包
	@Column(name = "C_PRD_PKG")
	private String prdPkg;
	
	// 产品名称
	@Column(name = "C_PRD_NAME")
	private String prdNm;
	
	// 产品分类
	@Column(name = "C_PRD_CTG")
	private String prdCtg;
	
	// 产品分类ID
	@Column(name = "C_PRD_CTGID")
	private String prdCtgid;
	
	// 产品型号
	@Column(name = "C_PRD_TYP")
	private String prdTyp;
	
	// 产品编码
	@Column(name = "C_PRD_CD")
	private String prdcd;
	
	// 物料号
	@Column(name = "C_MATER_CODE")
	private String materCode;
	
	// 产品描述
	@Column(name = "C_PRO_DESC")
	private String proDesc;

	// 客户产品描述
	@Column(name = "C_CUS_PRO_DESC")
	private String cusProDesc;
	
	// 单位
	@Column(name = "C_PRD_UNT")
	private String prdUnt;
	
	// 定价
	@Column(name = "N_PRD_SPRC")
	private Double prdSprc;
	
	// 报价
	@Column(name = "N_PRD_PRC")
	private Double prdPrc;
	
	// 数量
	@Column(name = "N_AMOUNT")
	private Double amt;
	
	// 单品总金额
	@Column(name = "N_TTL_UNT")
	private Double ttlUnt;
	
	// 行汇总单价
	@Column(name = "N_HH_TTL")
	private Double hhTtl;
	
	// 平均总单价
	@Column(name = "N_AVG_TTL")
	private Double avgTtl;
	
	// 非标需求
	@Column(name = "C_NSTD_RQ")
	private String nstRq;
	
	// 备注
	@Column(name = "C_NOTES")
	private String notes;

	// 已订购数量
	@Column(name = "N_ORD_NUM")
	private Double ordNum;

	// 订单编号
	@Column(name = "C_ORD_NO")
	private String ordNo;

	// 产品ID
	@Column(name = "C_PRO_ID")
	private String proId;

	// 已核销数量
	@Column(name = "N_VERIED_NUM")
	private Double veriedNum;
	
	// 借货无合同核销-本次核销数量
	@Column(name = "C_VERIED_NUM")
	private Double contractVeriedNum;

	// 借货无合同核销-ID
	@Column(name = "C_ELIMINATE_ID")
	private String contractEliminateId;

	
	// 未核销数量
	@Column(name = "N_NOT_VERI_NUM")
	private Double notVeriNum;

	// 业务号（合同行中的产品时即为合同编号）
	@Column(name = "C_BUSINEES_CODE")
	private String buzCode;

	// 新报价
	@Column(name = "N_NEW_PRD_PRC")
	private Double newPrdPrc;
	
	// 新数量
	@Column(name = "N_NEW_AMOUNT")
	private Double newAmt;
	
	// 新平均总单价
	@Column(name = "N_NEW_AVG_TTL")
	private Double newAvgTtl;

	// 单品总金额
	@Column(name = "N_NEW_TTL_UNT")
	private Double newTtlUnt;
	
	// 更新标志
	@Column(name = "C_UPDT_FLAG")
	private String updtFlag;

	// 更新类型
	@Column(name = "C_UPDT_TYPE")
	private String updatType;
	
	// 特价审批标志
	@Column(name = "C_SPRV_MRK")
	private String sprvMrk;

	// 新行汇总单价
	@Column(name = "N_NEW_HH_TTL")
	private Double newHhTtl;
	

	// 需要特价审批的层次
	@Column(name = "C_SPRV_LVL")
	private String sprvLvl;
	

	// 当前层级
	@Column(name = "C_CUR_LVL")
	private String curLvl;
	
	// 工程清单产品行号
	@Column(name = "C_LINE_NUM")
	private String lineNum;
	
	// 销售产品分类
	@Column(name = "C_SAL_CTG")
	private String salCtg;
	
	

	// 金牌价格
	@Column(name = "N_GOLD_PRC")
	private Double goldPrc;
	
	// 申请折扣
	@Column(name = "N_APPLY_DISCOUNT")
	private Double applyDiscount;
	
	// 批复价格
	@Column(name = "N_APPROVE_PRC")
	private Double approvePrc;
	
	// 批复折扣
	@Column(name = "N_APPROVE_DISCOUNT")
	private Double approveDiscount;

	// 临时价格(用于合同变更临时价格)
	@Column(name = "N_PRD_TMP_PRC")
	private Double prdTmpPrc;

	// 申请价格
	@Column(name = "N_APPLY_PRC")
	private Double applyPrc;

	// 所属层级的默认折扣率
	@Column(name = "N_DF_DISRT")
	private Double dfDisrt;

	//1报备，0非报备
	@Column(name = "OPT_TXT2")
	private String optTxt2;
	
	
	@Transient
	private String itemId;
	
	@Transient
	private BigDecimal quantity;
	
	@Transient
	private BigDecimal quantityB;
	
	public KstarPrjLst(){}
	
	public KstarPrjLst(Double veriedNum,Double notVeriNum){
		this.veriedNum = veriedNum;
		this.notVeriNum = notVeriNum;
	}
	
	public BigDecimal getQuantityB() {
		return quantityB;
	}

	public void setQuantityB(BigDecimal quantityB) {
		this.quantityB = quantityB;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLvId() {
		return lvId;
	}

	public void setLvId(String lvId) {
		this.lvId = lvId;
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

	public String getPrdPkg() {
		return prdPkg;
	}

	public void setPrdPkg(String prdPkg) {
		this.prdPkg = prdPkg;
	}

	public String getPrdNm() {
		//return prdNm;
		return CacheUtils.getProductName(this.getProId());
	}

	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}

	public String getPrdCtg() {
		return prdCtg;
	}

	public void setPrdCtg(String prdCtg) {
		this.prdCtg = prdCtg;
	}
	
	public String getPrdCtgid() {
		return prdCtgid;
	}

	public void setPrdCtgid(String prdCtgid) {
		this.prdCtgid = prdCtgid;
	}

	public String getPrdTyp() {
		//return prdTyp;
		return CacheUtils.getProductModel(this.getProId());
	}

	public void setPrdTyp(String prdTyp) {
		this.prdTyp = prdTyp;
	}

	public String getPrdUnt() {
		return prdUnt;
	}

	public void setPrdUnt(String prdUnt) {
		this.prdUnt = prdUnt;
	}

	public Double getPrdSprc() {
		return prdSprc;
	}

	public void setPrdSprc(Double prdSprc) {
		this.prdSprc = prdSprc;
	}

	public Double getPrdPrc() {
		return prdPrc;
	}

	public void setPrdPrc(Double prdPrc) {
		this.prdPrc = prdPrc;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Double getTtlUnt() {
		return ttlUnt;
	}

	public void setTtlUnt(Double ttlUnt) {
		this.ttlUnt = ttlUnt;
	}

	public Double getHhTtl() {
		return hhTtl;
	}

	public void setHhTtl(Double hhTtl) {
		this.hhTtl = hhTtl;
	}

	public Double getAvgTtl() {
		return avgTtl;
	}

	public void setAvgTtl(Double avgTtl) {
		this.avgTtl = avgTtl;
	}

	public String getNstRq() {
		return nstRq;
	}

	public void setNstRq(String nstRq) {
		this.nstRq = nstRq;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getProDesc() {
		//return proDesc;
		return CacheUtils.getProduct(this.getProId()).getProDesc()==null?"":
			CacheUtils.getProduct(this.getProId()).getProDesc();
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getCusProDesc() {
		return cusProDesc;
	}

	public void setCusProDesc(String cusProDesc) {
		this.cusProDesc = cusProDesc;
	}

	public Double getOrdNum() {
		return ordNum;
	}

	public void setOrdNum(Double ordNum) {
		this.ordNum = ordNum;
	}

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	
	public String getMaterCode() {
		//return materCode;
		return CacheUtils.getProduct(this.getProId()).getMaterCode()==null?"":
			CacheUtils.getProduct(this.getProId()).getMaterCode();
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Double getVeriedNum() {
		return veriedNum;
	}

	public void setVeriedNum(Double veriedNum) {
		this.veriedNum = veriedNum;
	}

	public Double getNotVeriNum() {
		return notVeriNum;
	}

	public void setNotVeriNum(Double notVeriNum) {
		this.notVeriNum = notVeriNum;
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public String getPrdcd() {
		return prdcd;
	}

	public void setPrdcd(String prdcd) {
		this.prdcd = prdcd;
	}

	public String getBuzCode() {
		return buzCode;
	}

	public void setBuzCode(String buzCode) {
		this.buzCode = buzCode;
	}

	public Double getNewPrdPrc() {
		return newPrdPrc;
	}

	public void setNewPrdPrc(Double newPrdPrc) {
		this.newPrdPrc = newPrdPrc;
	}

	public Double getNewAmt() {
		return newAmt;
	}

	public void setNewAmt(Double newAmt) {
		this.newAmt = newAmt;
	}

	public Double getNewTtlUnt() {
		return newTtlUnt;
	}

	public void setNewTtlUnt(Double newTtlUnt) {
		this.newTtlUnt = newTtlUnt;
	}

	public Double getNewAvgTtl() {
		return newAvgTtl;
	}

	public void setNewAvgTtl(Double newAvgTtl) {
		this.newAvgTtl = newAvgTtl;
	}
	

	public String getUpdtFlag() {
		return updtFlag;
	}

	public void setUpdtFlag(String updtFlag) {
		this.updtFlag = updtFlag;
	}

	public String getUpdatType() {
		return updatType;
	}

	public void setUpdatType(String updatType) {
		this.updatType = updatType;
	}

	public String getSprvMrk() {
		return sprvMrk;
	}

	public void setSprvMrk(String sprvMrk) {
		this.sprvMrk = sprvMrk;
	}

	public Double getNewHhTtl() {
		return newHhTtl;
	}

	public void setNewHhTtl(Double newHhTtl) {
		this.newHhTtl = newHhTtl;
	}

	public String getSprvLvl() {
		return sprvLvl;
	}

	public void setSprvLvl(String sprvLvl) {
		this.sprvLvl = sprvLvl;
	}

	public String getCurLvl() {
		return curLvl;
	}

	public void setCurLvl(String curLvl) {
		this.curLvl = curLvl;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public Double getGoldPrc() {
		return goldPrc;
	}

	public void setGoldPrc(Double goldPrc) {
		this.goldPrc = goldPrc;
	}

	public Double getApplyDiscount() {
		return applyDiscount;
	}

	public void setApplyDiscount(Double applyDiscount) {
		this.applyDiscount = applyDiscount;
	}

	public Double getApprovePrc() {
		return approvePrc;
	}

	public void setApprovePrc(Double approvePrc) {
		this.approvePrc = approvePrc;
	}

	public Double getApproveDiscount() {
		return approveDiscount;
	}

	public void setApproveDiscount(Double approveDiscount) {
		this.approveDiscount = approveDiscount;
	}

	public Double getPrdTmpPrc() {
		return prdTmpPrc;
	}

	public void setPrdTmpPrc(Double prdTmpPrc) {
		this.prdTmpPrc = prdTmpPrc;
	}

	public Double getDfDisrt() {
		return dfDisrt;
	}

	public void setDfDisrt(Double dfDisrt) {
		this.dfDisrt = dfDisrt;
	}

	public Double getApplyPrc() {
		return applyPrc;
	}

	public void setApplyPrc(Double applyPrc) {
		this.applyPrc = applyPrc;
	}

	public String getSalCtg() {
		return salCtg;
	}

	public void setSalCtg(String salCtg) {
		this.salCtg = salCtg;
	}

	public String getOptTxt2() {
		return optTxt2;
	}

	public void setOptTxt2(String optTxt2) {
		this.optTxt2 = optTxt2;
	}

	public Double getContractVeriedNum() {
		return contractVeriedNum;
	}

	public void setContractVeriedNum(Double contractVeriedNum) {
		this.contractVeriedNum = contractVeriedNum;
	}

	public String getContractEliminateId() {
		return contractEliminateId;
	}

	public void setContractEliminateId(String contractEliminateId) {
		this.contractEliminateId = contractEliminateId;
	}
	
	
}
  
