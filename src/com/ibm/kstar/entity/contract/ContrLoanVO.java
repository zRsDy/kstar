package com.ibm.kstar.entity.contract;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ContrLoanVO.java 借货产品明细行表视图实体类
 * 
 * @author 张钧鑫 and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "kstar_export_loan_v")
public class ContrLoanVO implements Serializable{
	private static final long serialVersionUID = -8658308573164369043L;
	
	@Id
	@GeneratedValue(generator = "crm_t_contr_basic_id_generator")
	@GenericGenerator(name="crm_t_contr_basic_id_generator", strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	// 工程清单产品行号
	@Column(name = "C_LINE_NUM")
	private String lineNum;
	
	// 产品型号
	@Column(name = "C_PRD_TYP")
	private String prdTyp;
	
	// 数量
	@Column(name = "N_AMOUNT")
	private Double amt;
	
	// 已核销数量
	@Column(name = "N_VERIED_NUM")
	private Double veriedNum;

	// 未核销数量
	@Column(name = "N_NOT_VERI_NUM")
	private Double notVeriNum;
	
	// 报价
	@Column(name = "N_PRD_PRC")
	private Double prdPrc;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
    
    /** 合同编单号 */
    @Column(name = "c_contract_code")
    private String contrCode;

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public String getPrdTyp() {
		return prdTyp;
	}

	public void setPrdTyp(String prdTyp) {
		this.prdTyp = prdTyp;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
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

	public Double getPrdPrc() {
		return prdPrc;
	}

	public void setPrdPrc(Double prdPrc) {
		this.prdPrc = prdPrc;
	}

	public String getQuotCode() {
		return quotCode;
	}

	public void setQuotCode(String quotCode) {
		this.quotCode = quotCode;
	}

	public String getContrCode() {
		return contrCode;
	}

	public void setContrCode(String contrCode) {
		this.contrCode = contrCode;
	}
    
}
