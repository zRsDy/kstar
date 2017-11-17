package com.ibm.kstar.entity.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 
 * ClassName: 收款核销明细 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年2月20日 上午11:23:06 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_verification_detail")
public class VerificationDetail implements  java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "verification_pid_generator")
   	@GenericGenerator(name="verification_pid_generator", strategy="uuid")
    private String id;
    
    /** 客户 ID  */
    @Column(name = "c_cust_id")
    private String custId;
    
    /** 客户 编号   */
    @Column(name = "c_cust_code")
    private String custCode;
    
    /** 合同ID */
    @Column(name = "c_contract_id")
    private String contractId;
    
    /** 合同编号 */
    @Column(name = "c_contract_code")
    private String contractCode;
    
    /** 收款单ID */
    @Column(name = "c_receipts_id")
    private String receiptsId;
    
    /** 收款单编号 */
    @Column(name = "c_receipts_code")
    private String receiptsCode;
    
    /** 合同收款计划明细ID  */
    @Column(name = "c_contr_rece_detail_id")
    private String contrReceDetailId;
    
    /** 合同收款计划行 */
    @Column(name = "c_receipts_plan")
    private String receiptsPlan;
    
    /** 收款阶段 */
    @Column(name = "c_receipts_stage")
    private String receiptsStage;
    
    /** 发货单ID */
    @Column(name = "c_delivery_id")
    private String deliveryId;
    
    /** 发货单编号 */
    @Column(name = "c_delivery_code")
    private String deliveryCode;
    
    /** 合同计划金额 */
    @Column(name = "n_plan_amount")
    private BigDecimal planAmount;
    
    /** 收款分类 */
    @Column(name = "c_receipts_type")
    private String receiptsType;
    
    /** 核销时间 */
    @Column(name = "dt_veri_date")
    private Date veriDate;
    
    /** 业务部门 */
    @Column(name = "c_biz_dept")
    private String bizDept;
    
    /** 销售人员ID */
    @Column(name = "c_salesman_id")
    private String salesmanId;
    
    /** 销售人员名称 */
    @Column(name = "c_salesman_name")
    private String salesmanName;
    
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    /** 本次核销金额 */
    @Column(name = "n_veri_amount")
    private BigDecimal amount;
    
    /** 合同收款计划行 显示名称*/
    @Transient
    private String receiptsPlanLable;
    
    /** 收款阶段  显示名称*/
    @Transient
    private String receiptsStageLable;
    
    /** 业务部门  显示名称*/
    @Transient
    private String bizDeptLable;
    
    /** 收款分类 显示名称*/
    @Transient
    private String receiptsTypeLable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	public String getReceiptsId() {
		return receiptsId;
	}

	public void setReceiptsId(String receiptsId) {
		this.receiptsId = receiptsId;
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

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	
	public BigDecimal getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
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

	public String getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(String salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReceiptsPlanLable() {
		return  this.getLovName(receiptsPlan);
	}

	public void setReceiptsPlanLable(String receiptsPlanLable) {
		this.receiptsPlanLable = receiptsPlanLable;
	}

	public String getReceiptsStageLable() {
		 return  this.getLovName(receiptsStage);
	}

	public void setReceiptsStageLable(String receiptsStageLable) {
		this.receiptsStageLable = receiptsStageLable;
	}

	public String getBizDeptLable() {
		return this.getLovName(bizDept);
	}

	public void setBizDeptLable(String bizDeptLable) {
		this.bizDeptLable = bizDeptLable;
	}

	public String getReceiptsTypeLable() {
		return this.getLovMember("RECEIPT_TYPE", receiptsType).getName();
	}

	public void setReceiptsTypeLable(String receiptsTypeLable) {
		this.receiptsTypeLable = receiptsTypeLable;
	}
	
	private String getLovName(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
	
	private LovMember getLovMember(String groupCode,String code){
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember(groupCode, code);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public String getContrReceDetailId() {
		return contrReceDetailId;
	}

	public void setContrReceDetailId(String contrReceDetailId) {
		this.contrReceDetailId = contrReceDetailId;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
	
	
}