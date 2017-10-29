package com.ibm.kstar.entity.order;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
/**
 * 
 * ClassName: 收款单表
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年4月26日 下午1:49:36 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_t_receipts")
public class Receipts extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true)
    @GeneratedValue(generator = "receipts_pid_generator")
   	@GenericGenerator(name="receipts_pid_generator", strategy="uuid")
    private String id;
    
    /** 收款单号 */
    @Column(name = "c_receipts_code")
    private String receiptsCode;
    
    /** 收款日期 */
    @Column(name = "dt_receipts_date")
    private Date receiptsDate;
    
    /** 业务实体编号 */
    @Column(name = "c_business_id")
    private String businessId;
    
    /** 业务实体编号 */
    @Column(name = "c_business_code")
    private String businessCode;
    
    /** 业务实体名称 */
    @Column(name = "c_business_name")
    private String businessName;
    
    /** 收款银行 */
    @Column(name = "c_receipts_bank")
    private String receiptsBank;
    
    /** 银行账号 */
    @Column(name = "c_bank_account")
    private String bankAccount;
    
    /** 币种 */
    @Column(name = "c_currency")
    private String currency;
    
	/** 到账金额 */
	@Column(name = "n_arrival_amount")
	private BigDecimal arrivalAmount;
    
    /** 手续费 */
    @Column(name = "n_poundage")
    private BigDecimal poundage;
    
    /** 收款金额 合计*/
    @Column(name = "n_amount")
    private BigDecimal amount;
    
    /** 代垫运费收款  */
    @Column(name = "n_freight")
    private BigDecimal freight;
    
    /** 已核销金额*/
    @Column(name = "n_veri_amount")
    private BigDecimal veriAmount;
    
    /** 付款客户 ID*/
    @Column(name = "c_payment_customer_id")
    private String paymentCustomerId;
    
    /** 付款客户 名称*/
    @Column(name = "c_payment_customer_name")
    private String paymentCustomerName;
    
    /** 是否ERP客户 */
    @Column(name = "c_erp_cust")
    private String erpCust;
    
    /** 更正客户ID*/
    @Column(name = "c_correct_cust_id")
    private String correctCustId;
    
    /** 更正客户名称*/
    @Column(name = "c_correct_cust_name")
    private String correctCustName;
    
    /** 销售人员记录更正客户时间 */
    @Column(name = "c_change_cust_date")
    private Date changeCustDate;
    
    /** 销售人员记录更正客户字段 */
    @Column(name = "c_change_cust_member")
    private String changeCustMember;
    
    /** 销售人员记录更正客户字段ID */
    @Column(name = "c_change_cust_memberid")
    private String changeCustMemberId;
    
    /** 销售中心 */
    @Column(name = "c_sales_center")
    private String salesCenter;
    
    /** 业务部门 */
    @Column(name = "c_biz_dept")
    private String bizDept;
    
    /** 销售人员ID */
    @Column(name = "c_salesman_id")
    private String salesmanId;
    
    /** 销售人员编码 */
    @Column(name = "c_salesman_code")
    private String salesmanCode;
    
    /** 销售人员名称 */
    @Column(name = "c_salesman_name")
    private String salesmanName;

	/** 销售人员职位 */
	@Column(name = "c_salesman_post")
	private String salesmanPost;
    
    /** 负责人工号 */
    @Column(name = "c_pic_no")
    private String picNo;
    
    /** 负责人名称 */
    @Column(name = "c_pic")
    private String pic;
    
    /** 负责部门ID */
    @Column(name = "c_resp_dept")
    private String respDept;
    
    /** 负责人所属营销 ID*/
    @Column(name = "c_pic_salecenter")
    private String picSaleCenter;
    
    /** 地区（国家/省） */
    @Column(name = "c_region")
    private String region;
    
    /** 状态 */
    @Column(name = "c_status")
    private String status;
    
    /** 备注 */
    @Column(name = "c_remarks")
    private String remarks;
    
    /** 是否是预收款 */
    @Column(name = "c_is_advances_received")
    private String isAdvancesReceived;
    
    /** 合同编号 */
    @Column(name = "c_contract_code")
    private String contractCode;
    
    /** 收款类型 */
    @Column(name = "c_receipts_type")
    private String receiptsType;
    
    /** ERP是否导入 */
    @Column(name = "c_erp_imp")
    private String erpImp;

    @Column(name = "C_OA_Waybill_No")
	private String oaWaybillNo;

    /** 状态显示名称 */
    @Transient
    private String statusLable;
    
    @Transient
    private String currencyLable;
    
    /** 营销中心*/
    @Transient
    private String salesCenterLable;
    
    /** 业务部门 */
    @Transient
    private String bizDeptLable;
    
    /** 负责人所属显示名称 */
    @Transient
    private String picSaleCenterLable;

	/** 负责部门 显示名称*/
	@Transient
	private String respDeptLable;

    /** 销售人员职位显示名称*/
    @Transient
    private String salesmanPostLabel;
    
    /** 未核销金额  */
    @Transient
    private  BigDecimal notVeriAmount;
    
    /** 收款类型显示  */
    @Transient
    private String receiptsTypeLable;
    /**创建人名称 */
    @Transient
    private String creator;
    
    /** ERP是否导入显示名称 */
    @Transient
    private String erpImpLable;
    
    
    public Date getChangeCustDate() {
		return changeCustDate;
	}

	public void setChangeCustDate(Date changeCustDate) {
		this.changeCustDate = changeCustDate;
	}

	public String getChangeCustMember() {
		return changeCustMember;
	}

	public void setChangeCustMember(String changeCustMember) {
		this.changeCustMember = changeCustMember;
	}

	public String getChangeCustMemberId() {
		return changeCustMemberId;
	}

	public void setChangeCustMemberId(String changeCustMemberId) {
		this.changeCustMemberId = changeCustMemberId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceiptsCode() {
		return receiptsCode;
	}

	public void setReceiptsCode(String receiptsCode) {
		this.receiptsCode = receiptsCode;
	}

	public Date getReceiptsDate() {
		return receiptsDate;
	}

	public void setReceiptsDate(Date receiptsDate) {
		this.receiptsDate = receiptsDate;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getReceiptsBank() {
		return receiptsBank;
	}

	public void setReceiptsBank(String receiptsBank) {
		this.receiptsBank = receiptsBank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getVeriAmount() {
		return veriAmount;
	}

	public void setVeriAmount(BigDecimal veriAmount) {
		this.veriAmount = veriAmount;
	}

	public String getPaymentCustomerId() {
		return paymentCustomerId;
	}

	public void setPaymentCustomerId(String paymentCustomerId) {
		this.paymentCustomerId = paymentCustomerId;
	}

	public String getPaymentCustomerName() {
		return paymentCustomerName;
	}

	public void setPaymentCustomerName(String paymentCustomerName) {
		this.paymentCustomerName = paymentCustomerName;
	}

	public String getErpCust() {
		return erpCust;
	}

	public void setErpCust(String erpCust) {
		this.erpCust = erpCust;
	}
	
	public String getCorrectCustId() {
		return correctCustId;
	}
	
	public void setCorrectCustId(String correctCustId) {
		this.correctCustId = correctCustId;
	}

	public String getCorrectCustName() {
		return correctCustName;
	}

	public void setCorrectCustName(String correctCustName) {
		this.correctCustName = correctCustName;
	}

	public String getSalesCenter() {
		return salesCenter;
	}

	public void setSalesCenter(String salesCenter) {
		this.salesCenter = salesCenter;
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
	
	public String getPicNo() {
		return picNo;
	}

	public void setPicNo(String picNo) {
		this.picNo = picNo;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getRespDept() {
		return respDept;
	}

	public void setRespDept(String respDept) {
		this.respDept = respDept;
	}

	public String getPicSaleCenter() {
		return picSaleCenter;
	}

	public void setPicSaleCenter(String picSaleCenter) {
		this.picSaleCenter = picSaleCenter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getErpImp() {
		return erpImp;
	}

	public void setErpImp(String erpImp) {
		this.erpImp = erpImp;
	}


    public String getSalesmanPost() {
        return salesmanPost;
    }

    public void setSalesmanPost(String salesmanPost) {
        this.salesmanPost = salesmanPost;
    }

    public String getStatusLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("RECEIPT_STATUS", status);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setStatusLable(String statusLable) {
		this.statusLable = statusLable;
	}

	public String getCurrencyLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("CURRENCY", currency);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setCurrencyLable(String currencyLable) {
		this.currencyLable = currencyLable;
	}

	public String getSalesCenterLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(salesCenter);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

	public void setSalesCenterLable(String salesCenterLable) {
		this.salesCenterLable = salesCenterLable;
	}

	public String getBizDeptLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(bizDept);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov.getName();
	}

    public String getSalesmanPostLabel() {
        LovMember lov = new LovMember();
        Object obj = CacheData.getInstance().get(salesmanPost);
        if(obj != null ){
            BeanUtils.copyPropertiesIgnoreNull(obj, lov);
        }
        return  lov.getName();
    }

	public void setBizDeptLable(String bizDeptLable) {
		this.bizDeptLable = bizDeptLable;
	}

	public BigDecimal getNotVeriAmount() {
		BigDecimal va = veriAmount == null ? new BigDecimal(0): veriAmount;
		return amount.subtract(va) ;
	}

	public void setNotVeriAmount(BigDecimal notVeriAmount) {
		this.notVeriAmount = notVeriAmount;
	}

	public BigDecimal getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(BigDecimal arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public String getIsAdvancesReceived() {
		return isAdvancesReceived;
	}

	public void setIsAdvancesReceived(String isAdvancesReceived) {
		this.isAdvancesReceived = isAdvancesReceived;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getReceiptsType() {
		return receiptsType;
	}

	public void setReceiptsType(String receiptsType) {
		this.receiptsType = receiptsType;
	}

	public String getReceiptsTypeLable() {
		return this.getLovMember("RECEIPTSTYPE", receiptsType).getName();
	}

	public String getSalesmanCode() {
		return salesmanCode;
	}

	public void setSalesmanCode(String salesmanCode) {
		this.salesmanCode = salesmanCode;
	}

	public String getPicSaleCenterLable() {
		return this.getLovName(picSaleCenter);
	}

	public String getRespDeptLable() {
		return this.getLovName(respDept);
	}

	public String getCreator() {
		return this.getCreatorName();
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getErpImpLable() {
		return this.getLovMember("RECEIPT_IMPORT_FLAG", erpImp).getName();
	}

	public void setErpImpLable(String erpImpLable) {
		this.erpImpLable = erpImpLable;
	}


	public String getOaWaybillNo() {
		return oaWaybillNo;
	}

	public void setOaWaybillNo(String oaWaybillNo) {
		this.oaWaybillNo = oaWaybillNo;
	}
}