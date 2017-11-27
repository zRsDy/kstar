package com.ibm.kstar.entity.order.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.order.ContractReceiptProduct;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import org.xsnake.web.utils.BeanUtils;

/**
 * 
 * ClassName: 合同收款计划明细 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年2月20日 上午11:23:06 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_contract_receipt_detail_v")
@Permission(businessType = IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL)
public class ContractReceiptDetailVO extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "contract_receipt_pid_generator")
   	@GenericGenerator(name="contract_receipt_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 客户 ID  */
    @Column(name = "c_cust_id")
    private String custId;
    
    /** 客户 编号   */
    @Column(name = "c_cust_code")
    private String custCode;
    
    /** 客户 名称   */
    @Column(name = "c_cust_name")
    private String custName;
    
    /** 合同ID */
    @Column(name = "c_contract_id")
    private String contractId;
    
    /** 合同编号 */
    @Column(name = "c_contract_code")
    private String contractCode;
    
    /** 合同回款规划ID */
    @Column(name = "c_contrpay_id")
    private String contrPayId;
    
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
    
    /** 已核销金额 */
    @Column(name = "n_veri_amount")
    private BigDecimal veriAmount;
    
    /** 币种  */
    @Column(name = "c_receipts_type")
    private String receiptsType;

	/** 业务实体 */
	@Column(name = "c_business_entity")
	private String businessEntity;

	/** 业务实体显示名称*/
	@Transient
	private String businessEntityLable;

    /** 业务部门 */
    @Column(name = "c_biz_dept")
    private String bizDept;
    
    /** 销售人员ID */
    @Column(name = "c_salesman_id")
    private String salesmanId;
    
    /** 销售人员编号 */
    @Column(name = "c_salesman_code")
    private String salesmanCode;
    
    /** 销售人员名称 */
    @Column(name = "c_salesman_name")
    private String salesmanName;
    
    /** 销售人员岗位 */
    @Column(name = "c_salesman_post")
    private String salesmanPos;
    
    /** 销售人员岗位显示名称 */
    @Transient
    private String salesmanPosName;
    
    /**到期日 */
    @Column(name = "dt_payment_date")
    private Date paymentDate;
    
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;

    /** 说明 */
    @Column(name = "c_explain")
    private String explain;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    @Column(name = "C_SALES_SORT")
    private String salesSort;

    /** 本次核销金额 */
    @Transient
    private BigDecimal amount;
    
    /** 余额（未核销金额） */
    @Transient
    private BigDecimal balance;
    
    /** 列表未核销金额 */
    @Transient
    private BigDecimal gridBalance;
    
    /** 状态显示名称 */
    @Transient
    private String statusLable;
    
    /** 合同收款计划行 显示名称*/
    @Transient
    private String receiptsPlanLable;
    
    /** 收款阶段  显示名称*/
    @Transient
    private String receiptsStageLable;
    
    /** 业务部门  显示名称*/
    @Transient
    private String bizDeptLable;
    
    /** 币种 显示名称*/
    @Transient
    private String receiptsTypeLable;
    
    /** 销售中心 */
    @Column(name = "c_sales_center")
    private String salesCenter;

    /** 营销中心名称 */
    @Transient
    private String salesCenterName;

    /** 收款日期 */
    @Transient
    private Date gatheringDate;
    
    /** 核销日期 */
    @Transient
    private Date checkDate;
    
    
    
    @Transient
    private List<ContractReceiptProduct> items;
    
	public List<ContractReceiptProduct> getItems() {
		return items;
	}

	public void setItems(List<ContractReceiptProduct> items) {
		this.items = items;
	}

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

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
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

	public String getSalesmanCode() {
		return salesmanCode;
	}

	public void setSalesmanCode(String salesmanCode) {
		this.salesmanCode = salesmanCode;
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
	
	public BigDecimal getBalance() {
		if(planAmount == null){
			planAmount = new BigDecimal(0);
		}
		return planAmount.subtract(veriAmount == null ? new BigDecimal(0): veriAmount);
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getStatusLable() {
		return  this.getLovMember("RECEIPT_STATUS", status).getName();
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
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
		return this.getLovMember("CURRENCY", receiptsType).getName();
	}

	public void setReceiptsTypeLable(String receiptsTypeLable) {
		this.receiptsTypeLable = receiptsTypeLable;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getContrPayId() {
		return contrPayId;
	}

	public void setContrPayId(String contrPayId) {
		this.contrPayId = contrPayId;
	}

	public String getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}

	
	public String getSalesmanPosName() {
		LovMember lov = new LovMember();
        Object obj = CacheData.getInstance().get(salesmanPos);
        if(obj != null ){
            BeanUtils.copyPropertiesIgnoreNull(obj, lov);
        }
        return  lov.getName();
	}

	public void setSalesmanPosName(String salesmanPosName) {
		this.salesmanPosName = salesmanPosName;
	}

	public String getSalesmanPos() {
		return salesmanPos;
	}

	public void setSalesmanPos(String salesmanPos) {
		this.salesmanPos = salesmanPos;
	}

	public String getSalesCenter() {
		return salesCenter;
	}

	public void setSalesCenter(String salesCenter) {
		this.salesCenter = salesCenter;
	}

	public String getBusinessEntityLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(businessEntity);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setBusinessEntityLable(String businessEntityLable) {
		this.businessEntityLable = businessEntityLable;
	}

	public String getSalesCenterName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(salesCenter);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setSalesCenterName(String salesCenterName) {
		this.salesCenterName = salesCenterName;
	}

	public BigDecimal getGridBalance() {
		if(planAmount == null){//计划金额
			planAmount = new BigDecimal(0);
		}
		if(veriAmount == null){//核销金额
			veriAmount = new BigDecimal(0);
		}
		return planAmount.subtract(veriAmount);
	}

	public void setGridBalance(BigDecimal gridBalance) {
		this.gridBalance = gridBalance;
	}

	public Date getGatheringDate() {
		return gatheringDate;
	}

	public void setGatheringDate(Date gatheringDate) {
		this.gatheringDate = gatheringDate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getSalesSort() {
		return salesSort;
	}

	public void setSalesSort(String salesSort) {
		this.salesSort = salesSort;
	}
	
	
}