package com.ibm.kstar.entity.contract;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.order.OrderHeader;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * KstarContract.java 合同实体类
 * 
 * @author Neil Wan and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "CRM_T_CONTR_BASIC")
public class Contract implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658308573164369043L;


	@Id
	@GeneratedValue(generator = "crm_t_contr_basic_id_generator")
	@GenericGenerator(name="crm_t_contr_basic_id_generator", strategy="uuid")
	@Column(name = "C_ID")
	private String id;
	
	@Column(name = "C_CONTR_VER")
	private String contrVer;
	
	@Column(name = "C_CONTR_NO")
	private String contrNo;
	
	@Column(name = "C_CONTR_NAME")
	private String contrName;
	
	@Column(name = "C_CONTR_TYPE")
	private String contrType;

	@Transient
	private String contrTypeName;	

	@Column(name = "C_CONTR_STAT")
	private String contrStat;

	@Transient
	private String contrStatName;	

	@Column(name = "C_PROJ_NO")
	private String projNo;

	@Column(name = "C_PROJ_NAME")
	private String projName;	
	
	@Column(name = "C_CUST_CODE")
	private String custCode;
	
	@Column(name = "C_CUST_NAME")
	private String custName;

	@Column(name = "C_CUST_ADDR")
	private String custAddr;
	
	@Column(name = "C_CUST_PHONE")
	private String custPhone;

	@Column(name = "C_CUST_CONTR_NO")
	private String custContrNo;
	
	@Column(name = "C_CUST_LINK")
	private String custLink;
	
	@Column(name = "C_PRIC_NO")
	private String pricNo;
	
	@Column(name = "C_PRIC_TABLE")
	private String pricTable;
	
	@Column(name = "C_REVIEW_STAT")
	private String reviewStat;
	
	@Column(name = "C_TRIAL_STAT")
	private String trialStat;
	
	@Column(name = "C_CANCEL_STAT")
	private String cancelStat;

	@Transient
	private String cancelStatDesc;
	
	@Column(name = "C_FRAME_NO")
	private String frameNo;
	
	@Column(name = "N_TOTAL_AMT")
	private Double totalAmt;

	@Transient
	private String totalAmtDesc;
	
	@Column(name = "N_NCAN_NUM")	
	private Double ncanNum;
	
	@Column(name = "N_NCAN_AMT")
	private Double ncanAmt;
	
	@Column(name = "C_BUSS_ENITY")
	private String bussEnity;
	
	@Column(name = "C_ORG")
	private String org;

	@Transient
	private String orgName;
	
	@Column(name = "C_PAY_WAY")
	private String payWay;
	
	@Transient
	private String payWayDesc;
	
	@Column(name = "C_PAY_COND")
	private String payCond;
	
	@Column(name = "C_IS_VALID")
	private String isValid;
	
	@Column(name = "C_IS_CHANGE")
	private String isChange;
	
	@Column(name = "C_IS_PASS")
	private String isPass;
	
	@Column(name = "C_IS_SEAL_FIRST")
	private String isSealFirst;
	
	@Column(name = "C_IS_WHOLE_SET")
	private String isWholeSet;
	
	@Column(name = "C_IS_CONF_LIST")
	private String isConfList;
	
	@Column(name = "C_IS_DELIV_HOME")
	private String isDelivHome;
	
	@Column(name = "C_IS_UNLOAD")
	private String isUnload;
	
	@Column(name = "C_IS_HOME_INSTALL")
	private String isHomeInstall;
	
	@Column(name = "C_IS_AUX")
	private String isAux;
	
	@Column(name = "C_IS_TRANF_SALE")
	private String isTranfSale;
	
	@Column(name = "C_POST_NO")
	private String postNo;
	
	@Column(name = "DT_POST_DATE")
	private Date postDate;
	
	@Column(name = "DT_DELIV_DATE")
	private Date delivDate;
			
	@Column(name = "DT_SYS_SIGN_DATE")
	private Date sysSignDate;

	@Column(name = "DT_ACT_SIGN_DATE")
	private Date actSignDate;
	
	@Column(name = "C_CREATOR")
	private String creator;

	@Transient
	private String creatorName;
	
	@Column(name = "DT_CREATE_TIME")
	private Date createTime;
	
	@Column(name = "C_CHANGER")
	private String changer;
	
	@Column(name = "DT_CHANGE_TIME")
	private Date changeTime;
	
	@Column(name = "C_SUBMITER")
	private String submiter;
	
	@Column(name = "DT_SUBMIT_TIME")
	private Date submitTime;
	
	@Column(name = "C_PROC_INST_ID")
	private String processInstanceId;
	
	@Column(name = "C_REMARK")
	private String remark;

	@Column(name = "C_FINAL_REVIEW_STAT")
	private String finalReviewStat;
	
	@Transient
	private String finalReviewStatDesc;
	
	@Column(name = "C_IS_ARCHIVE")
	private String isArchive;	
	
	@Column(name = "DT_ARCHIVE_DATE")
	private Date archiveDate;
	
	@Column(name = "C_ARCHIVE_REMARK")
	private String achiveRemark;
	
	@Column(name = "C_PAY_STAT")
	private String payStat;
	
	@Column(name = "C_SHIP_WAY")
	private String shipWay;

	/** 是否高风险合同  */
	@Column(name = "C_IS_HighRisk")
	private String isHighRisk;

	@Transient
	private String payStatDesc;

    /** 客户联系人ID*/
	@Column(name = "C_CUST_LINK_ID")
	private String custLinkId;
	
	  /** 创建人 */
    @Column(name = "created_by_id", nullable = true, length = 32)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "created_pos_id", nullable = true, length = 32)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "created_org_id", nullable = true, length = 32)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "updated_by_id", nullable = true, length = 32)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
    
    /** ERP下单客户ID */
	@Column(name = "C_ERP_ORDER_CUST_ID")
	private String erpOrderCustId;
	
    /** ERP下单客户名称 */
	@Column(name = "C_ERP_ORDER_CUST_NAME")
	private String erpOrderCustName;
	
    /** 下单商务专员*/
	@Column(name = "C_ORDERER")
	private String orderer;

    /** 下单商务专员*/
	@Transient
	private String ordererName;
	
    /** 营销部门 */
	@Column(name = "C_MARKET_DEPT")
	private String marketDept;

    /** 营销部门 */
	@Transient
	private String marketDeptName;
	
	@Column(name = "DT_EXPECT_SIGN_DATE")
	private Date expectSignDate;
	
	/** 合同有效期 */
	@Column(name = "DT_VALID_DATE")
	private Date validDate;

	@Transient
	private String shipWayDesc;

	@Column(name = "C_DIRECT_SELL_NO")
	private String directSellNo;

	//报价单编号
	@Column(name = "C_QUOT_NO")
	private String quotNo;

	//订单编号
	@Column(name = "C_ORDER_NO")
	private String orderNo;
	
	//总已收金额
	@Column(name = "N_TOTAL_RECEIVE_AMT")	
	private Double  tolRecdAmt; 

	//总未收金额
	@Column(name = "N_NOT_TOTAL_RECEIVE_AMT")	
	private Double  notTolRecAmt; 

	//已下单金额
	@Column(name = "N_ORDERED_AMT")	
	private Double  orderedAmt; 

	//已发货金额
	@Column(name = "N_DELIVERIED_AMT")	
	private Double  deliveredAmt; 

	//已开票金额
	@Column(name = "N_INVOICED_AMT")	
	private Double  invoicedAmt; 

	/** 流程结束时间 */
	@Column(name = "DT_PROCESS_CLOSE_DATE")
	private Date processCloseDate;

	//借货用途
	@Column(name = "C_USAGE_ID")
	private String usageId;
	
	@Transient
	private String usageDesc;
	
    /** 下单商务专员岗位*/
	@Column(name = "ORDER_POS_ID")
	private String orderPosId;
	
    /** 下单商务专员组织*/
	@Column(name = "ORDER_ORG_ID")
	private String orderOrgId;
	
	/** 货币 */
	@Column(name = "c_currency", nullable = true, length = 32)
	private String currency;

    /** 报价审批人员*/
	@Column(name = "C_AUDITOR_ID")
	private String auditorId;

    /** 报价审批人员*/
	@Transient
	private String auditorName;

    /** 报价审批人员*/
	@Column(name = "C_AUDITOR_POS_ID")
	private String auditorPosId;

    /** 报价审批人员*/
	@Column(name = "C_AUDITOR_ORG_ID")
	private String auditorOrgId;

    /** 借货核销审批状态*/
	@Column(name = "C_LOAN_CHECK_FLOW_STAT")
	private String loanChkFlowStat; 
	
	/** 订单信息 */
	@Transient
	private OrderHeader orderHeader;
	
	
	public String getContrTypeName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(contrType));
		return lov==null? null : lov.getName();
	}
	
	public String getContrStatName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(contrStat));
		return lov==null? null : lov.getName();
	}
	
	public Date getExpectSignDate() {
		return expectSignDate;
	}

	public void setExpectSignDate(Date expectSignDate) {
		this.expectSignDate = expectSignDate;
	}

	public String getShipWay() {
		return shipWay;
	}

	public void setShipWay(String shipWay) {
		this.shipWay = shipWay;
	}
	
	public String getDirectSellNo() {
		return directSellNo;
	}

	public void setDirectSellNo(String directSellNo) {
		this.directSellNo = directSellNo;
	}
	
	public String getQuotNo() {
		return quotNo;
	}

	public void setQuotNo(String quotNo) {
		this.quotNo = quotNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContrVer() {
		return contrVer;
	}

	public void setContrVer(String contrVer) {
		this.contrVer = contrVer;
	}

	public String getContrNo() {
		return contrNo;
	}

	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}

	public String getContrName() {
		return contrName;
	}

	public void setContrName(String contrName) {
		this.contrName = contrName;
	}
	
	public String getCustAddr() {
		return custAddr;
	}

	public void setCustAddr(String custAddr) {
		this.custAddr = custAddr;
	}

	public String getContrType() {
		return contrType;
	}

	public void setContrType(String contrType) {
		this.contrType = contrType;
	}

	public String getContrStat() {
		return contrStat;
	}

	public void setContrStat(String contrStat) {
		this.contrStat = contrStat;
	}

	public String getProjName() {
		return projName;
	} 
	
	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getProjNo() {
		return projNo;
	}

	public void setProjNo(String projNo) {
		this.projNo = projNo;
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

	public String getCustContrNo() {
		return custContrNo;
	}

	public void setCustContrNo(String custContrNo) {
		this.custContrNo = custContrNo;
	}

	public String getCustLink() {
		return custLink;
	}

	public void setCustLink(String custLink) {
		this.custLink = custLink;
	}

	public String getPricNo() {
		return pricNo;
	}

	public void setPricNo(String pricNo) {
		this.pricNo = pricNo;
	}

	public String getPricTable() {
		return pricTable;
	}

	public void setPricTable(String pricTable) {
		this.pricTable = pricTable;
	}

	public String getReviewStat() {
		return reviewStat;
	}

	public void setReviewStat(String reviewStat) {
		this.reviewStat = reviewStat;
	}

	public String getTrialStat() {
		return trialStat;
	}

	public void setTrialStat(String trialStat) {
		this.trialStat = trialStat;
	}

	public String getCancelStat() {
		return cancelStat;
	}

	public void setCancelStat(String cancelStat) {
		this.cancelStat = cancelStat;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	@Transient
	public String getFrameName(){
		Contract contract = CacheUtils.getContractById(this.frameNo);
		if (contract != null) {
			return contract.getContrNo();
		}else{
			return "";
		}
	}
	

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	

	public Double getNcanNum() {
		return ncanNum;
	}

	public void setNcanNum(Double ncanNum) {
		this.ncanNum = ncanNum;
	}

	public Double getNcanAmt() {
		return ncanAmt;
	}

	public void setNcanAmt(Double ncanAmt) {
		this.ncanAmt = ncanAmt;
	}

	public String getBussEnity() {
		return bussEnity;
	}

	public void setBussEnity(String bussEnity) {
		this.bussEnity = bussEnity;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayWayDesc() {
		if(payWay==null) return "";
		if (payWay.indexOf(",")==0){
			LovMember lov =  ((LovMember)CacheData.getInstance().get(payWay));
			return lov==null? null : lov.getName();
		}
		String [] chgTd= payWay.split(",");
		String strDescs= ""; 
		for(String chi : chgTd){
		LovMember lov =  ((LovMember)CacheData.getInstance().get(chi));
			strDescs+=lov==null? null : lov.getName()+",";
		} 
		return strDescs.substring(0, strDescs.length()-1);
	}
	
	public String getPayCond() {
		return payCond;
	}

	public void setPayCond(String payCond) {
		this.payCond = payCond;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Transient
	public String getIsValidName(){
		if ("1".equals(this.isValid)) {
			return "是";
		}else{
			return "否";
		}
	}
	
	public String getIsChange() {
		return isChange;
	}

	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}
	
	@Transient
	public String getIsChangeName(){
		if ("1".equals(this.isChange)) {
			return "是";
		}else{
			return "否";
		}
	}
	
	public String getIsPass() {
		return isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
	
	@Transient
	public String getIsPassName(){
		if ("1".equals(this.isPass)) {
			return "是";
		}else{
			return "否";
		}
		
	}

	public String getIsSealFirst() {
		return isSealFirst;
	}

	public void setIsSealFirst(String isSealFirst) {
		this.isSealFirst = isSealFirst;
	}

	public String getIsWholeSet() {
		return isWholeSet;
	}

	public void setIsWholeSet(String isWholeSet) {
		this.isWholeSet = isWholeSet;
	}

	public String getIsConfList() {
		return isConfList;
	}

	public void setIsConfList(String isConfList) {
		this.isConfList = isConfList;
	}

	public String getIsUnload() {
		return isUnload;
	}

	public void setIsUnload(String isUnload) {
		this.isUnload = isUnload;
	}

	public String getIsHomeInstall() {
		return isHomeInstall;
	}

	public void setIsHomeInstall(String isHomeInstall) {
		this.isHomeInstall = isHomeInstall;
	}

	public String getIsAux() {
		return isAux;
	}

	public void setIsAux(String isAux) {
		this.isAux = isAux;
	}

	public String getIsTranfSale() {
		return isTranfSale;
	}

	public void setIsTranfSale(String isTranfSale) {
		this.isTranfSale = isTranfSale;
	}

	public String getPostNo() {
		return postNo;
	}

	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getIsDelivHome() {
		return isDelivHome;
	}

	public void setIsDelivHome(String isDelivHome) {
		this.isDelivHome = isDelivHome;
	}

	public Date getDelivDate() {
		return delivDate;
	}

	public void setDelivDate(Date delivDate) {
		this.delivDate = delivDate;
	}

	public Date getSysSignDate() {
		return sysSignDate;
	}

	public void setSysSignDate(Date sysSignDate) {
		this.sysSignDate = sysSignDate;
	}

	public Date getActSignDate() {
		return actSignDate;
	}

	public void setActSignDate(Date actSignDate) {
		this.actSignDate = actSignDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public String getSubmiter() {
		return submiter;
	}

	public void setSubmiter(String submiter) {
		this.submiter = submiter;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	 public String getFinalReviewStat() {
		return finalReviewStat;
	}

	public void setFinalReviewStat(String finalReviewStat) {
		this.finalReviewStat = finalReviewStat;
	}

	public String getFinalReviewStatDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(finalReviewStat));
		return lov==null? null : lov.getName();
	}

	public String getIsArchive() {
		return isArchive;
	}

	public void setIsArchive(String isArchive) {
		this.isArchive = isArchive;
	}

	public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public String getAchiveRemark() {
		return achiveRemark;
	}

	public void setAchiveRemark(String achiveRemark) {
		this.achiveRemark = achiveRemark;
	}

	public String getPayStat() {
		return payStat;
	}

	public void setPayStat(String payStat) {
		this.payStat = payStat;
	}
	
	public String getPayStatDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
		return lov==null? null : lov.getName();
	}

	public String getShipWayDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(shipWay));
		return lov==null? null : lov.getName();
	}

	public String getCustLinkId() {
		return custLinkId;
	}

	public void setCustLinkId(String custLinkId) {
		this.custLinkId = custLinkId;
	}

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

	public String getErpOrderCustId() {
		return erpOrderCustId;
	}

	public void setErpOrderCustId(String erpOrderCustId) {
		this.erpOrderCustId = erpOrderCustId;
	}

	public String getErpOrderCustName() {
		return erpOrderCustName;
	}

	public void setErpOrderCustName(String erpOrderCustName) {
		this.erpOrderCustName = erpOrderCustName;
	}

	public String getOrderer() {
		return orderer;
	}

	public void setOrderer(String orderer) {
		this.orderer = orderer;
	}

	public String getMarketDept() {
		return marketDept;
	}

	public void setMarketDept(String marketDept) {
		this.marketDept = marketDept;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	@Transient
	private String bussEnityName;
	public String getBussEnityName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(bussEnity));
		return lov==null? null : lov.getName();
	}
 
	public Double getTolRecdAmt() {
		return tolRecdAmt;
	}

	public void setTolRecdAmt(Double tolRecdAmt) {
		this.tolRecdAmt = tolRecdAmt;
	}

	public Double getNotTolRecAmt() {
		return notTolRecAmt;
	}

	public void setNotTolRecAmt(Double notTolRecAmt) {
		this.notTolRecAmt = notTolRecAmt;
	}

	public Double getOrderedAmt() {
		return orderedAmt;
	}

	public void setOrderedAmt(Double orderedAmt) {
		this.orderedAmt = orderedAmt;
	}

	public Double getDeliveredAmt() {
		return deliveredAmt;
	}

	public void setDeliveredAmt(Double deliveredAmt) {
		this.deliveredAmt = deliveredAmt;
	}

	public Double getInvoicedAmt() {
		return invoicedAmt;
	}

	public void setInvoicedAmt(Double invoicedAmt) {
		this.invoicedAmt = invoicedAmt;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public String getOrgName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(org));
		return lov==null? null : lov.getName();
	}
	
	public String getMarketDeptName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(marketDept));
		return lov==null? null : lov.getName();
	}

	public String getCreatorName() {
		Employee lov =  ((Employee)CacheData.getInstance().get(creator));
		return lov==null? null : lov.getName();
	}

	public String getCancelStatDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(cancelStat));
		return lov==null? null : lov.getName();
	}

	public Date getProcessCloseDate() {
		return processCloseDate;
	}

	public void setProcessCloseDate(Date processCloseDate) {
		this.processCloseDate = processCloseDate;
	}

	public String getUsageId() {
		return usageId;
	}

	public void setUsageId(String usageId) {
		this.usageId = usageId;
	}
	
	public String getUsageDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(usageId));
		return lov==null? null : lov.getName();
	}

	public String getTotalAmtDesc() {
//		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00"); 
		return (totalAmt==null)?"":String.format("%.2f", totalAmt);
	}

	public String getOrdererName() {
		Employee lov =  ((Employee)CacheData.getInstance().get(orderer));
		return lov==null? null : lov.getName();
	}
	

	public String getAuditorName() {
		Employee lov =  ((Employee)CacheData.getInstance().get(auditorId));
		return lov==null? null : lov.getName();
	}

	public String getOrderPosId() {
		return orderPosId;
	}

	public void setOrderPosId(String orderPosId) {
		this.orderPosId = orderPosId;
	}

	public String getOrderOrgId() {
		return orderOrgId;
	}

	public void setOrderOrgId(String orderOrgId) {
		this.orderOrgId = orderOrgId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorPosId() {
		return auditorPosId;
	}

	public void setAuditorPosId(String auditorPosId) {
		this.auditorPosId = auditorPosId;
	}

	public String getAuditorOrgId() {
		return auditorOrgId;
	}

	public void setAuditorOrgId(String auditorOrgId) {
		this.auditorOrgId = auditorOrgId;
	}

	public String getLoanChkFlowStat() {
		return loanChkFlowStat;
	}

	public void setLoanChkFlowStat(String loanChkFlowStat) {
		this.loanChkFlowStat = loanChkFlowStat;
	}
	

	public OrderHeader getOrderHeader(){
		if (this.orderHeader != null) {
			return this.orderHeader;
		}
		orderHeader = CacheUtils.getOrderHeaderByContrId(this.id);
		if (orderHeader == null) {
			orderHeader = new OrderHeader();
		}
		return orderHeader;
	}
	
	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}
	
	
	/**
	 * ERP 编号
	 * @return
	 */
	public String getErpNo(){
		return getOrderHeader().getErpOrderCode();
	}
	
	/**
	 * 订单编号
	 * @return
	 */
	public String getOrderNum(){
		return getOrderHeader().getOrderCode();
	}
	
	/**
	 * 订单金额
	 * @return
	 */
	public BigDecimal getOrderAmount(){
		return getOrderHeader().getAmount();
	}

	public String getIsHighRisk() {
		return isHighRisk;
	}

	public void setIsHighRisk(String isHighRisk) {
		this.isHighRisk = isHighRisk;
	}
	
	@Transient
	private String isHighRiskName;

	public String getIsHighRiskName() {
		if ("1".equals(this.isHighRisk)) {
			return "是";
		}else{
			return "否";
		}
	}

	public void setIsHighRiskName(String isHighRiskName) {
		this.isHighRiskName = isHighRiskName;
	}
	
	
	
}
