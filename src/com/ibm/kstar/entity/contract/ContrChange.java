package com.ibm.kstar.entity.contract;

import java.io.Serializable;
//import java.math.Double;
import java.util.Date;
//import java.util.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;



/**
 * KstarContract.java 产品实体类
 * 
 * 
 */
@Entity
@Table(name = "CRM_T_CONTR_CHANGE")
@Permission(businessType = "CONTR_CHANGE")
public class ContrChange  extends BaseEntity implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658308573164369043L;

	// ID

	@Id
	@GeneratedValue(generator = "crm_t_contr_change_id_generator")
	@GenericGenerator(name="crm_t_contr_change_id_generator", strategy="uuid")
	@Column(name = "C_ID")
	@PermissionBusinessId
	private String id;
	
	//变更编号
	@Column(name = "C_CHANGE_NO")
	private String changeNo;
	
	
	//变更类型
	@Column(name = "C_CHANGE_TYPE")
	private String changeType;	

	@Transient
	private String changeTypeDesc;	
	
	public String getChangeTypeDesc() {
		if(changeType==null) return "";
		if (changeType.indexOf(",")==0){
			LovMember lov =  ((LovMember)CacheData.getInstance().get(changeType));
			return lov==null? null : lov.getName();
		}
		String [] chgTd= changeType.split(",");
		String strDescs= ""; 
		for(String chi : chgTd){
		LovMember lov =  ((LovMember)CacheData.getInstance().get(chi));
			strDescs+=lov==null? null : lov.getName()+",";
		} 
		return strDescs.substring(0, strDescs.length()-1);
	}

	@Column(name = "C_CHANGE_STAT")
	private String changeStat;
	

	public String getChangeStat() {
		return changeStat;
	}

	public void setChangeStat(String changeStat) {
		this.changeStat = changeStat;
	}

	@Transient
	private String changeStatDesc;	
	
	public String getChangeStatDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(changeStat));
		return lov==null? null : lov.getName();
	}
	
	
	@Column(name = "C_CHANGE_REASON")
	private String changeReason;

	@Column(name = "C_CHANGE_CONT")
	private String changeCont;

	// 合同ID
	@Column(name = "C_CONTR_ID")
	private String  contrId;
	public String getContrId() {
		return contrId;
	}

	public void setContrId(String contrId) {
		this.contrId = contrId;
	}

	@Transient
	private Contract contract; // 合同信息
	
	// 编码
	@Column(name = "C_CONTR_NO")
	private String  contrNo;

	@Column(name = "N_CONTR_VER")
	private String contrVer;
	
	
	@Column(name = "C_CONTR_NAME")
	private String contrName;
	
	@Column(name = "C_CONTR_TYPE")
	private String contrType;
	
	@Column(name = "C_CONTR_STAT")
	private String contrStat;
	


	@Transient
	private String contrStatName;	
	
	public String getContrStatName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(contrStat));
		return lov==null? null : lov.getName();
	}
	
	@Column(name = "C_PROJ_NAME")
	private String projName;	
	
	@Column(name = "C_PROJ_NO")
	private String projNo;
	
	@Column(name = "C_CUST_CODE")
	private String custCode;
	
	@Column(name = "C_CUST_NAME")
	private String custName;
	
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
	
	@Column(name = "C_FRAME_NO")
	private String frameNo;
	
	@Column(name = "N_TOTAL_AMT")
	private Double totalAmt;

	@Transient
	private Double totalAmtDesc;
	
	@Column(name = "N_CANCEL_NUM")	
	private Double cancelNum;
	
	
	@Column(name = "N_CANCEL_AMT")
	private Double cancelAmt;
	
	@Column(name = "C_BUSS_ENITY")
	private String bussEnity;

	@Transient
	private String bussEnityName;
	
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

	/** 货币 */
	@Column(name = "c_currency")
	private String currency;
	
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
	private Date submiteTime;
	
	@Column(name = "C_PROC_INST_ID")
	private String procInstID;
	
	@Column(name = "C_REMARK")
	private String remark;
	


	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChangeNo() {
		return changeNo;
	}

	public void setChangeNo(String changeNo) {
		this.changeNo = changeNo;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	public String getChangeCont() {
		return changeCont;
	}

	public void setChangeCont(String changeCont) {
		this.changeCont = changeCont;
	}

	public String getContrNo() {
		return contrNo;
	}

	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}

	public String getContrVer() {
		return contrVer;
	}

	public void setContrVer(String contrVer) {
		this.contrVer = contrVer;
	}

	public String getContrName() {
		return contrName;
	}

	public void setContrName(String contrName) {
		this.contrName = contrName;
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

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	

	public Double getCancelNum() {
		return cancelNum;
	}

	public void setCancelNum(Double cancelNum) {
		this.cancelNum = cancelNum;
	}

	public Double getCancelAmt() {
		return cancelAmt;
	}

	public void setCancelAmt(Double cancelAmt) {
		this.cancelAmt = cancelAmt;
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

	public String getIsChange() {
		return isChange;
	}

	public void setIsChange(String isChange) {
		this.isChange = isChange;
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

	public String getIsDelivHome() {
		return isDelivHome;
	}

	public void setIsDelivHome(String isDelivHome) {
		this.isDelivHome = isDelivHome;
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

	public Date getDelivDate() {
		return delivDate;
	}

	public void setDelivDate(Date date) {
		this.delivDate = date;
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

	public Date getSubmiteTime() {
		return submiteTime;
	}

	public void setSubmiteTime(Date submiteTime) {
		this.submiteTime = submiteTime;
	}

	public String getProcInstID() {
		return procInstID;
	}

	public void setProcInstID(String procInstID) {
		this.procInstID = procInstID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCreatorName() {
		Employee lov =  ((Employee)CacheData.getInstance().get(creator));
		return lov==null? null : lov.getName();
	}

	public String getTotalAmtDesc() {
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00"); 
		return (totalAmt==null)?"":df.format(totalAmt);
	}

	public String getOrgName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(org));
		return lov==null? null : lov.getName();
	}
	
	public String getBussEnityName() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(bussEnity));
		return lov==null? null : lov.getName();
	}

	/**
	 * 关联合同
	 * @return
	 */
	public Contract getContract() {
		if (this.contract != null) {
			return this.contract;
		}
		Contract contr = CacheUtils.getContractById(this.contrId);
		if (contr == null) {
			this.contract = new Contract();
		}else{
			this.contract = contr;
		}
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	/**
	 * 关联合同销售人员
	 * @return
	 */
	public String getContrCreatorName() {
		return getContract().getCreatorName();
	}
	
	/**
	 * 关联合同销售部门
	 * @return
	 */
	public String getContrOrgName() {
		return getContract().getOrgName();
	}
	
	/**
	 * 关联合同营销部门
	 * @return
	 */
	public String getMarketDeptName() {
		return getContract().getMarketDeptName();
	}
	
	/**
	 * 关联合同下单商务专员
	 * @return
	 */
	public String getOrdererName() {
		return getContract().getOrdererName();
	}
	
}
