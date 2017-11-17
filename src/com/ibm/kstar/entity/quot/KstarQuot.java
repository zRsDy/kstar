
package com.ibm.kstar.entity.quot;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 
 * ClassName: KstarQuot <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: Dec 15, 2016 7:22:23 PM <br/> 
 * 
 * @author ZW 
 * @version  
 * @since JDK 1.7 
 */ 

@Entity
@Table(name = "CRM_T_QUOTATION_BASIC")
@Permission(businessType = "QUOTINF")
public class KstarQuot extends BaseEntity implements Serializable{
	
	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator = "kstarquot_id_generator")
	@GenericGenerator(name="kstarquot_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	@PermissionBusinessId
	private String id;
	
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
	
	// 销售代表ID
	@Column(name = "C_SALESREPID")
	private String salesRepid;
	
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
	
	// 特价审批状态
	@Column(name = "C_SP_AUDIT_STATUS")
	private String spAuditStatus;
	
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
	
	@Transient
	private String marketDeptName;
	
	// 标书递交日期
	@Column(name = "DT_BIDSBM_DATE")
	private Date bidsbmDt;
	
	// 开标日期
	@Column(name = "DT_BIDST_DATE")
	private Date bidstDt;
	
	// 招标编号
	@Column(name = "C_BIDID")
	private String bidID;
	
	// 招标类型
	@Column(name = "C_BIDTYP")
	private String bidTyp;
	
	// 招标单位
	@Column(name = "C_BIDUNT")
	private String bidUnt;
	
	// 招标单位联系人
	@Column(name = "C_BIDCNCT")
	private String bidCnct;
	
	// 招标单位地址
	@Column(name = "C_BIDADDR")
	private String bidAddr;
	
	// 反馈投标结果附件
	@Column(name = "C_BIDRSTATT")
	private String bidRstatt;
	
	// 主要竞争对手
	@Column(name = "C_BIDCMPR")
	private String bidCmpr;
	
	// 中标品牌
	@Column(name = "C_BIDBRD")
	private String bidBrd;
	
	// 中标价格
	@Column(name = "N_BIDAMOUNT")
	private String bidAmount;
	
	// 价格评审状态
	@Column(name = "C_PRC_ADT_STATUS")
	private String prcAdtstatus;
	
	// 技术评审状态
	@Column(name = "C_TCH_ADT_STATUS")
	private String tchAdtstatus;
	
	// 投标反馈状态
	@Column(name = "C_BID_RSP_STATUS")
	private String bidRspstatus;
	
	// 备料申请状态
	@Column(name = "C_MTR_REQ_STATUS")
	private String mtrReqstatus;
	
	// 特价申请解锁字段状态
	@Column(name = "C_SP_ULK_STATUS")
	private String spUlkstatus;
	

	public KstarQuot() {
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
		Employee e =  (Employee)CacheData.getInstance().get(this.salesRepid);
		if(e != null){
			return e.getName();
		}
		return "";
	}

	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}

	public String getSalesRepid() {
		return salesRepid;
	}

	public void setSalesRepid(String salesRepid) {
		this.salesRepid = salesRepid;
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

	
	public String getSpAuditStatus() {
		return spAuditStatus;
	}

	public void setSpAuditStatus(String spAuditStatus) {
		this.spAuditStatus = spAuditStatus;
	}

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
	
	public String getMarketDeptName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(marketDept));
		return lov==null? null : lov.getName();
	}

	public Date getBidsbmDt() {
		return bidsbmDt;
	}

	public void setBidsbmDt(Date bidsbmDt) {
		this.bidsbmDt = bidsbmDt;
	}

	public Date getBidstDt() {
		return bidstDt;
	}

	public void setBidstDt(Date bidstDt) {
		this.bidstDt = bidstDt;
	}

	public String getBidID() {
		return bidID;
	}

	public void setBidID(String bidID) {
		this.bidID = bidID;
	}

	public String getBidTyp() {
		return bidTyp;
	}

	public void setBidTyp(String bidTyp) {
		this.bidTyp = bidTyp;
	}

	public String getBidUnt() {
		return bidUnt;
	}

	public void setBidUnt(String bidUnt) {
		this.bidUnt = bidUnt;
	}

	public String getBidCnct() {
		return bidCnct;
	}

	public void setBidCnct(String bidCnct) {
		this.bidCnct = bidCnct;
	}

	public String getBidAddr() {
		return bidAddr;
	}

	public void setBidAddr(String bidAddr) {
		this.bidAddr = bidAddr;
	}

	public String getBidRstatt() {
		return bidRstatt;
	}

	public void setBidRstatt(String bidRstatt) {
		this.bidRstatt = bidRstatt;
	}

	public String getBidCmpr() {
		return bidCmpr;
	}

	public void setBidCmpr(String bidCmpr) {
		this.bidCmpr = bidCmpr;
	}

	public String getBidBrd() {
		return bidBrd;
	}

	public void setBidBrd(String bidBrd) {
		this.bidBrd = bidBrd;
	}

	public String getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(String bidAmount) {
		this.bidAmount = bidAmount;
	}

	public String getPrcAdtstatus() {
		return prcAdtstatus;
	}

	public void setPrcAdtstatus(String prcAdtstatus) {
		this.prcAdtstatus = prcAdtstatus;
	}

	public String getTchAdtstatus() {
		return tchAdtstatus;
	}

	public void setTchAdtstatus(String tchAdtstatus) {
		this.tchAdtstatus = tchAdtstatus;
	}

	public String getBidRspstatus() {
		return bidRspstatus;
	}

	public void setBidRspstatus(String bidRspstatus) {
		this.bidRspstatus = bidRspstatus;
	}

	public String getMtrReqstatus() {
		return mtrReqstatus;
	}

	public void setMtrReqstatus(String mtrReqstatus) {
		this.mtrReqstatus = mtrReqstatus;
	}

	public String getSpUlkstatus() {
		return spUlkstatus;
	}

	public void setSpUlkstatus(String spUlkstatus) {
		this.spUlkstatus = spUlkstatus;
	}
	
	
	
}


