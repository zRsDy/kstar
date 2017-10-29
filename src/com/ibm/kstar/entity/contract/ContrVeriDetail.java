package com.ibm.kstar.entity.contract;

import java.io.Serializable; 
import java.util.Date;
//import java.util.Time;

import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;


/**
 * 借货合同核销明细表(CRM_T_CONTR_VERI_DETAIL)
 * 
 * @author bianj
 * @version 1.0.0 2017-02-27
 */
@Entity
@Table(name = "crm_t_contr_veri_detail")
public class ContrVeriDetail implements Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -5420947283621117303L;
    
	/** 主键 */
	@Id
	@GeneratedValue(generator = "crm_t_contr_veri_detail_id_generator")
	@GenericGenerator(name = "crm_t_contr_veri_detail_id_generator", strategy = "uuid")
	@Column(name = "C_ID")
    private String id;
    
    /** 核销单编号 */
    @Column(name = "c_veri_no", nullable = true, length = 32)
    private String veriNo;
    
    /** 合同ID */
    @Column(name = "c_contract_id", nullable = true, length = 32)
    private String contrId;
    
    /** 合同编单号 */
    @Column(name = "c_contract_code", nullable = true, length = 32)
    private String contrCode;
    
    /** 工程清单行ID */
    @Column(name = "c_prjlst_id", nullable = true, length = 32)
    private String prjlstId;
    
    /** 产品物料编号 */
    @Column(name = "c_mater_code", nullable = true, length = 32)
    private String materCode;
    
    /** 借货合同ID */
    @Column(name = "c_loan_id", nullable = true, length = 32)
    private String loanId;
    
    /** 借货合同编单号 */
    @Column(name = "c_loan_code", nullable = true, length = 32)
    private String loanCode;
    
    /** 借货工程清单行ID */
    @Column(name = "c_loan_prjlst_id", nullable = true, length = 32)
    private String loanPrjlstId;

    /** 借货数量 */
    @Column(name = "n_loan_num", nullable = true)
    private Double loanNum;
    
    /** 收款分类 */
    @Column(name = "c_veri_type", nullable = true, length = 32)
    private String veriType;
    
    /** 本次核销数量 */
    @Column(name = "n_cur_veri_num", nullable = true)
    private Double curVeriNum;
    
    /** 已核销数量 */
    @Column(name = "n_veried_num", nullable = true)
    private Double veriedNum;
    
    /** 未核销数量 */
    @Column(name = "n_not_veried_num", nullable = true)
    private Double notVeriedNum;
    
    /** 核销时间 */
    @Column(name = "dt_veri_date", nullable = true)
    private Date veriDate;
    
    /** 客户ID */
    @Column(name = "c_cust_id", nullable = true, length = 32)
    private String custId;
    
    /** 客户编号 */
    @Column(name = "c_cust_code", nullable = true, length = 32)
    private String custCode;
    
    /** 创建人 */
    @Column(name = "c_creation_by", nullable = true, length = 32)
    private String creationBy;
    
    /** 创建时间 */
    @Column(name = "dt_creation_date", nullable = true)
    private Date creationDate;
    
    /** 修改人 */
    @Column(name = "c_updated_by", nullable = true, length = 32)
    private String updatedBy;
    
    /** 修改时间 */
    @Column(name = "dt_updated_date", nullable = true)
    private Date updatedDate;
    
    /** 状态 */
    @Column(name = "c_status", nullable = true, length = 32)
    private String status;
    
    /** 备注 */
    @Column(name = "c_remarks", nullable = true, length = 300)
    private String remarks;

    /** 产品ID */
    @Column(name = "c_pro_id", nullable = true, length = 32)
    private String proId;
    
    /** 产品编码 */
    @Column(name = "c_pro_code", nullable = true, length = 12)
    private String proCode;
    
    /** 产品名称 */
    @Column(name = "c_pro_name", nullable = true, length = 20)
    private String proName;
    
    /** 英文名 */
    @Column(name = "c_pro_ename", nullable = true, length = 30)
    private String proEname;
    
    /** 产品描述 */
    @Column(name = "c_pro_desc", nullable = true, length = 300)
    private String proDesc;
    
    /** 产品型号 */
    @Column(name = "c_pro_model", nullable = true, length = 10)
    private String prdTyp;
    
    /** 单位 */
    @Column(name = "c_unit", nullable = true, length = 32)
    private String unit;
    
    /** 品牌 */
    @Column(name = "c_brand", nullable = true, length = 12)
    private String brand;
    
    /** 产品线ID */
    @Column(name = "c_pro_line_id", nullable = true, length = 20)
    private String proLineId;
    
    /** ERP产品类别 */
    @Column(name = "c_pro_erp_category", nullable = true, length = 32)
    private String proErpCategory;
    
    /** CRM产品类别 */
    @Column(name = "c_pro_crm_category", nullable = true, length = 32)
    private String proCrmCategory;
    
	// 报价(单价)
	@Column(name = "N_PRD_PRC")
	private Double prdPrc;
    
    public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProEname() {
		return proEname;
	}

	public void setProEname(String proEname) {
		this.proEname = proEname;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getPrdTyp() {
		return prdTyp;
	}

	public void setPrdTyp(String prdTyp) {
		this.prdTyp = prdTyp;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProLineId() {
		return proLineId;
	}

	public void setProLineId(String proLineId) {
		this.proLineId = proLineId;
	}

	public String getProErpCategory() {
		return proErpCategory;
	}

	public void setProErpCategory(String proErpCategory) {
		this.proErpCategory = proErpCategory;
	}

	public String getProCrmCategory() {
		return proCrmCategory;
	}

	public void setProCrmCategory(String proCrmCategory) {
		this.proCrmCategory = proCrmCategory;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVeriNo() {
		return veriNo;
	}

	public void setVeriNo(String veriNo) {
		this.veriNo = veriNo;
	}


	public String getContrId() {
		return contrId;
	}

	public void setContrId(String contrId) {
		this.contrId = contrId;
	}

	public String getContrCode() {
		return contrCode;
	}

	public void setContrCode(String contrCode) {
		this.contrCode = contrCode;
	}

	public String getPrjlstId() {
		return prjlstId;
	}

	public void setPrjlstId(String prjlstId) {
		this.prjlstId = prjlstId;
	}

	public String getMaterCode() {
		return materCode;
	}

	public void setMaterCode(String materCode) {
		this.materCode = materCode;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getLoanCode() {
		return loanCode;
	}

	public void setLoanCode(String loanCode) {
		this.loanCode = loanCode;
	}

	public String getLoanPrjlstId() {
		return loanPrjlstId;
	}

	public void setLoanPrjlstId(String loanPrjlstId) {
		this.loanPrjlstId = loanPrjlstId;
	}

	public String getVeriType() {
		return veriType;
	}

	public void setVeriType(String veriType) {
		this.veriType = veriType;
	}

	public Double getCurVeriNum() {
		return curVeriNum;
	}

	public void setCurVeriNum(Double curVeriNum) {
		this.curVeriNum = curVeriNum;
	}

	public Double getVeriedNum() {
		return veriedNum;
	}

	public void setVeriedNum(Double veriedNum) {
		this.veriedNum = veriedNum;
	}

	public Double getNotVeriedNum() {
		return notVeriedNum;
	}

	public void setNotVeriedNum(Double notVeriedNum) {
		this.notVeriedNum = notVeriedNum;
	}

	public Date getVeriDate() {
		return veriDate;
	}

	public void setVeriDate(Date veriDate) {
		this.veriDate = veriDate;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCreationBy() {
		return creationBy;
	}

	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getLoanNum() {
		return loanNum;
	}

	public void setLoanNum(Double loanNum) {
		this.loanNum = loanNum;
	}

	public Double getPrdPrc() {
		return prdPrc;
	}

	public void setPrdPrc(Double prdPrc) {
		this.prdPrc = prdPrc;
	}

	
}
