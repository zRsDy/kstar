package com.ibm.kstar.entity.order;  

import java.math.BigDecimal;
import java.util.Date;


/** 
 * ClassName:合同核销明细VO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年2月16日 下午7:23:25 <br/> 
 * @author   liming 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public class ContractVerificationVO {
	
	  /** 合同编号 */
    private String contractCode;
    
    /** 收款单编号 */
    private String receiptsCode;
    
    /** 合同收款计划行 */
    private String receiptsPlan;
    
    /** 收款阶段 */
    private String receiptsStage;
    
    /** 借货合同 */
    private String contractBorrow;
    
    /** 销售订单编号 */
    private String orderCode;
    
    /** 核销金额 */
    private BigDecimal veriAmount;
    
    /** 收款分类 */
    private String receiptsType;
    
    /** 核销时间 */
    private Date veriDate;
    
    /** 业务部门 */
    private String bizDept;
    
    /** 销售人员 */
    private String salesman;
    
	public ContractVerificationVO(String contractCode, String receiptsCode,
			String receiptsPlan, String receiptsStage, String contractBorrow,
			String orderCode, BigDecimal veriAmount, String receiptsType,
			Date veriDate, String bizDept, String salesman) {
		super();
		this.contractCode = contractCode;
		this.receiptsCode = receiptsCode;
		this.receiptsPlan = receiptsPlan;
		this.receiptsStage = receiptsStage;
		this.contractBorrow = contractBorrow;
		this.orderCode = orderCode;
		this.veriAmount = veriAmount;
		this.receiptsType = receiptsType;
		this.veriDate = veriDate;
		this.bizDept = bizDept;
		this.salesman = salesman;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getReceiptsCode() {
		return receiptsCode;
	}

	public void setReceiptsCode(String receiptsCode) {
		this.receiptsCode = receiptsCode;
	}

	public String getReceiptsPlan() {
		return receiptsPlan;
	}

	public void setReceiptsPlan(String receiptsPlan) {
		this.receiptsPlan = receiptsPlan;
	}

	public String getReceiptsStage() {
		return receiptsStage;
	}

	public void setReceiptsStage(String receiptsStage) {
		this.receiptsStage = receiptsStage;
	}

	public String getContractBorrow() {
		return contractBorrow;
	}

	public void setContractBorrow(String contractBorrow) {
		this.contractBorrow = contractBorrow;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public BigDecimal getVeriAmount() {
		return veriAmount;
	}

	public void setVeriAmount(BigDecimal veriAmount) {
		this.veriAmount = veriAmount;
	}

	public String getReceiptsType() {
		return receiptsType;
	}

	public void setReceiptsType(String receiptsType) {
		this.receiptsType = receiptsType;
	}

	public Date getVeriDate() {
		return veriDate;
	}

	public void setVeriDate(Date veriDate) {
		this.veriDate = veriDate;
	}

	public String getBizDept() {
		return bizDept;
	}

	public void setBizDept(String bizDept) {
		this.bizDept = bizDept;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
    
}
  
