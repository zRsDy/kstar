
package com.ibm.kstar.entity.custom;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "crm_t_ho_quotation_basic")
public class HandoverQuot implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ho_kstarquot_id_generator")
	@GenericGenerator(name="ho_kstarquot_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	/** 交接单据ID */
    @Column(name = "C_BUSINESS_ID", nullable = true, length = 32)
    private String businessId;
    
    /** 交接单据状态 */
    @Column(name = "C_BUSINESS_STATUS", nullable = true, length = 32)
    private String businessStatus;
    
    @Transient
	private String businessStatusName;

	public String getBusinessStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(businessStatus));
		return lov == null ? null : lov.getName();
	}

	public void setBusinessStatusName(String businessStatusName) {
		this.businessStatusName = businessStatusName;
	}
    
    /** 交接单据状态 */
    @Column(name = "C_CHECK_STATUS", nullable = true, length = 32)
    private String checkStatus;
    
    @Transient
	private String checkStatusName;
    
    public String getCheckStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(checkStatus));
		
		return lov == null ? null : lov.getName();
	}

	public void setCheckStatusName(String checkStatusName) {
		this.checkStatusName = checkStatusName;
	}
    
    /** 交接单据原id */
    @Column(name = "C_BASE_ID", nullable = true, length = 32)
    private String baseId;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 报价单版本
	@Column(name = "N_VERSION")
	private String quotVersion;
	
	// 报价单名称
	@Column(name = "C_NAME")
	private String quotName;
	
	// 销售代表
	@Column(name = "C_SALESREP")
	private String salesRep;
	
	// 销售代表部门
	@Column(name = "C_SALESREP_DEP")
	private String salesRepDep;
	
	// 商机编码
	@Column(name = "C_BO_CODE")
	private String boCode;
	
	// 商机名称
	@Column(name = "C_BO_NAME")
	private String boName;
	
	// 组织
	@Column(name = "C_ORG")
	private String org;
	
	// 客户名称
	@Column(name = "C_CUSTOMER_NAME")
	private String customerName;
	
	// 客户编号
	@Column(name = "C_CUSTOMER_CODE")
	private String customerCode;
	
	// 联系人
	@Column(name = "C_CONTACTS")
	private String contacts;
	
	// 状态
	@Column(name = "C_STATUS")
	private String status;
	
	// 关闭原因
	@Column(name = "C_CLOSURE_REASON")
	private String closureReason;
	
	// 工程评审状态
	@Column(name = "C_PRO_REVIEW_STATUS")
	private String proReviewStatus;
	
	// 标书审核状态
	@Column(name = "C_BID_AUDIT_STATUS")
	private String bidAuditStatus;
	
	// 创建日期
	@Column(name = "DT_CREATE_DATE")
	private Date createTime;
	
	// 创建人
	@Column(name = "C_CREATOR")
	private String creator;
	
	// 价格表
	@Column(name = "C_PRICE_LIST")
	private String priceList;
	
	// 价格表ID
	@Column(name = "C_PRICE_LISTID")
	private String priceListid;
	
	// 货币
	@Column(name = "C_CURRENCY")
	private String currency;
	
	// 总额
	@Column(name = "N_AMOUNT")
	private String amount;
	
	// 投标结果
	@Column(name = "C_BID_RESULTS")
	private String bidResults;
	
	// 丢标 赢标原因
	@Column(name = "C_BID_RESULTS_Reason")
	private String bidResReason;
	
	// 是否需工程评审
	@Column(name = "N_IS_PROREVIEW")
	private String isProReview;
	
	// 是否投标项目
	@Column(name = "N_IS_BIDPRO")
	private String isBidPro;
	
	// 投标号
	@Column(name = "N_BID_NO")
	private String bidNo;
	
	// 有效标识
	@Column(name = "N_IS_VALID")
	private String isValid;
	
	// 付款方式
	@Column(name = "C_PAY_TYPE")
	private String payType;
	
	// 销售部门
	@Column(name = "C_SAL_DEP")
	private String salDep;
	
	// 关联标书号
	@Column(name = "C_BIDDOC_NO")
	private String biddocNo;
	
	
	// 备注
	@Column(name = "C_MEMO")
	private String memo;
	
	
	// 提交报价标志
	@Column(name = "C_IF_SUBMITTED")
	private String ifsubmitted;
	
	
	// 营销部门
	@Column(name = "C_MARKET_DEPT")
	private String marketDept;
	
	 /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    @PermissionUserId
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    @PermissionPositionId
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    @PermissionOrgId
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedPosId() {
		return createdPosId;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public HandoverQuot() {
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

	public String getQuotVersion() {
		return quotVersion;
	}

	public void setQuotVersion(String quotVersion) {
		this.quotVersion = quotVersion;
	}

	public String getQuotName() {
		return quotName;
	}

	public void setQuotName(String quotName) {
		this.quotName = quotName;
	}

	public String getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}

	public String getSalesRepDep() {
		return salesRepDep;
	}

	public void setSalesRepDep(String salesRepDep) {
		this.salesRepDep = salesRepDep;
	}

	public String getBoCode() {
		return boCode;
	}

	public void setBoCode(String boCode) {
		this.boCode = boCode;
	}

	public String getBoName() {
		return boName;
	}

	public void setBoName(String boName) {
		this.boName = boName;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusName() {
		LovMember l = (LovMember) CacheData.getInstance().getMember("QUOTSTS", this.status);
		if (l == null) {
			return "";
		}
		return l.getName();
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClosureReason() {
		return closureReason;
	}

	public void setClosureReason(String closureReason) {
		this.closureReason = closureReason;
	}

	public String getProReviewStatus() {
		return proReviewStatus;
	}

	public void setProReviewStatus(String proReviewStatus) {
		this.proReviewStatus = proReviewStatus;
	}

	public String getBidAuditStatus() {
		return bidAuditStatus;
	}

	public void setBidAuditStatus(String bidAuditStatus) {
		this.bidAuditStatus = bidAuditStatus;
	}

//	public String getCreateTime() {
//		if (createTime != null) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//			return sdf.format(createTime);
//		}
//		return null;
//	}

	
	public Date getCreateTime() {
		return createTime;
	}
	
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreatorName() {
		Employee employee = (Employee) CacheData.getInstance().get(this.creator);
		if (employee != null) {
			return employee.getName();
		} else {
			return "";
		}
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	
	public Object getCurrencyGrid() {
		LovMember lov = new LovMember();
		if(currency != null && StringUtils.isNotEmpty(currency)){
			Object obj = CacheData.getInstance().get(currency);
			if(obj != null)
				BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
	
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBidResults() {
		return bidResults;
	}

	public void setBidResults(String bidResults) {
		this.bidResults = bidResults;
	}

	public String getBidResReason() {
		return bidResReason;
	}

	public void setBidResReason(String bidResReason) {
		this.bidResReason = bidResReason;
	}

	public String getIsProReview() {
		return isProReview;
	}

	public void setIsProReview(String isProReview) {
		this.isProReview = isProReview;
	}

	public String getIsBidPro() {
		return isBidPro;
	}

	public void setIsBidPro(String isBidPro) {
		this.isBidPro = isBidPro;
	}

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getSalDep() {
		return salDep;
	}

	public void setSalDep(String salDep) {
		this.salDep = salDep;
	}

	public String getBiddocNo() {
		return biddocNo;
	}

	public void setBiddocNo(String biddocNo) {
		this.biddocNo = biddocNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPriceListid() {
		return priceListid;
	}

	public void setPriceListid(String priceListid) {
		this.priceListid = priceListid;
	}

	public String getIfsubmitted() {
		return ifsubmitted;
	}

	public void setIfsubmitted(String ifsubmitted) {
		this.ifsubmitted = ifsubmitted;
	}

	public String getMarketDept() {
		return marketDept;
	}

	public void setMarketDept(String marketDept) {
		this.marketDept = marketDept;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	
}


