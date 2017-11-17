package com.ibm.kstar.entity.custom;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.entity.common.doc.KstarAttachment;

@Entity
@Table(name = "CRM_INT_ZXB_NOLC_QUOTA_INFOR")
public class NoLcQuotaZXBInfo extends BaseEntity {
	/** 主键  **/
	@Id
	@GeneratedValue(generator = "CRM_INT_ZXB_NOLC_QUOTA_INFOR_generator")
	@GenericGenerator(name = "CRM_INT_ZXB_NOLC_QUOTA_INFOR_generator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	
	@Column(name = "CORP_SERIAL_NUM", nullable = false, length = 50)
	private String corpSerialNo;	//企业内部非LC限额申请唯一标识
	
	@Column(name = "CLIENT_NUM", nullable = false, length = 30)
	private String clientNo;			//企业标识
	
	@Column(name = "POLICY_NUM", nullable = false, length = 30)
	private String policyNo;			//保险单号
	
	@Column(name = "C_CUSTOMER_ID", nullable = false, length = 32)
	private String c_customer_id; 	//CRM系统客户
	
	@Column(name = "SINOSURE_BUYER_NUM", nullable = false, length = 30)
	private String sinosureBuyerNo;	//中国信保买方代码
	
	@Column(name = "CORP_BUYER_NUM", nullable = true, length = 50)
	private String corpBuyerNo;		//企业买方代码
	
	@Column(name = "BUYER_CHN_NAME", nullable = true, length = 500)
	private String buyerChnName;		//买方中文名称
	
	@Column(name = "BUYER_ENG_NAME", nullable = true, length = 500)
	private String buyerEngName;		//买方英文名称
	
	@Column(name = "BUYER_COUNTRY_CODE", nullable = true, length = 3)
	private String buyerCountryCode;	//买方国家代码
	
	@Column(name = "BUYER_ENG_ADDRESS", nullable = true, length = 300)
	private String buyerEngAddress;	//买方英文地址
	
	@Column(name = "BUYER_CHN_ADDRESS", nullable = true, length = 255)
	private String buyerChnAddress;	//买方中文地址
	
	@Column(name = "BUYER_REG_NUM", nullable = true, length = 100)
	private String buyerRegNo;		//买方注册号
	
	@Column(name = "BUYER_REG_ADDRESS", nullable = true, length = 300)
	private String buyer_reg_address;		//买方注地址
	
	@Column(name = "BUYER_TEL", nullable = true, length = 200)
	private String buyerTel;			//买方电话
	
	@Column(name = "BUYER_FAX", nullable = true, length = 100)
	private String buyerFax;			//买方传真
	
	@Column(name = "WARRANTOR_TYPE", nullable = true, length = 1)
	private String warrantorType;	//是否有担保人:0无，1有
	
	@Column(name = "SINOSURE_WARRANTOR_NUM", nullable = true, length = 30)
	private String sinosureWarrantorNo;//中国信保担保人代码
	
	@Column(name = "CORP_WARRANTOR_NUM", nullable = true, length = 50)
	private String corpWarrantorNo;	//企业担保人代码
	
	@Column(name = "WARRANTOR_CHN_NAME", nullable = true, length = 500)
	private String warrantorChnName;	//担保人中文名称
	
	@Column(name = "WARRANTOR_ENG_NAME", nullable = true, length = 500)
	private String warrantorEngName;	//担保人英文名称
	
	@Column(name = "WARRANTOR_COUNTRY_CODE", nullable = true, length = 3)
	private String warrantorCountryCode;//担保人国家代码
	
	@Column(name = "WARRANTOR_ENG_ADDRESS", nullable = true, length = 300)
	private String warrantorEngAddress;//担保人英文地址
	
	@Column(name = "WARRANTOR_CHN_ADDRESS", nullable = true, length = 255)
	private String warrantorChnAddress;//担保人中文地址
	
	@Column(name = "WARRANTOR_REG_NUM", nullable = true, length = 100)
	private String warrantorRegNo;	//担保人注册号
	
	@Column(name = "WARRANTOR_TEL", nullable = true, length = 200)
	private String warrantorTel;		//担保人电话
	
	@Column(name = "WARRANTOR_FAX", nullable = true, length = 100)
	private String warrantorFax;		//担保人传真
	
	@Column(name = "PAY_TERM_APPLY", nullable = false, length = 4)
	private Long payTermApply;		//申请信用期限
	
	@Column(name = "QUOTA_SUM_APPLY", nullable = false, length = 20)
	private Double quotaSumApply;	//申请信用限额
	
	@Column(name = "CONTRACT_PAY_MODE", nullable = false, length = 1)
	private String contractPayMode;	//合同支付方式
	
	@Column(name = "ORDER_SUM", nullable = false, length = 20)
	private Double orderSum;//当前在手订单金额
	
	@Column(name = "ARM_SUM", nullable = true, length = 20)
	private Double armSum; //当前应收账款余额
	
	@Column(name = "TRADE_NAME_CODE", nullable = false, length = 30)
	private String tradeNameCode;	//出口商品名称代码
	
	@Column(name = "TRADE_ELSE_NAME", nullable = true, length = 500)
	private String tradeElseName;	//其他出口商品名称
	
	@Column(name = "TRADE_TERMS", nullable = true, length = 100)
	private String tradeTerms;//贸易术语
	
	@Column(name = "TRADE_ELSE_TERMS", nullable = true, length = 100)
	private String tradeElseTerms;//其他贸易术语名称
	
	@Column(name = "IS_HIST_TRADE", nullable = true, length = 1)
	private String ifHistTrade;		//是否有历史交易：0无，1 有 
	
	@Column(name = "EARLY_TRADE_YEAR", nullable = true, length = 4)
	private String earlyTradeYear;	//最早成交年份
	
	@Column(name = "START_DEBT_YEAR", nullable = true, length = 4)
	private String startDebtYear;	//最早放账年份
	
	@Column(name = "LAST_YEAR_1", nullable = true, length = 4)
	private String lastYear1;//最近三年交易年份1
	
	@Column(name = "LAST_PAY_MODE_1", nullable = true, length = 20)
	private String lastPayMode1;//最近三年交易结算方式1
	
	@Column(name = "LAST_SUM_1", nullable = true, length = 20)
	private Double lastSum1;//最近三年交易额1
	
	@Column(name = "LAST_YEAR_2", nullable = true, length = 4)
	private String lastYear2;//最近三年交易年份2
	
	@Column(name = "LAST_PAY_MODE_2", nullable = true, length = 20)
	private String lastPayMode2;//最近三年交易结算方式2
	
	@Column(name = "LAST_SUM_2", nullable = true, length = 20)
	private Double lastSum2;//最近三年交易额2
	
	@Column(name = "LAST_YEAR_3", nullable = true, length = 4)
	private String lastYear3;//最近三年交易年份3
	
	@Column(name = "LAST_PAY_MODE_3", nullable = true, length = 20)
	private String lastPayMode3;//最近三年交易结算方式3
	
	@Column(name = "LAST_SUM_3", nullable = true, length = 20)
	private Double lastSum3;//最近三年交易额3
	
	@Column(name = "BUYER_PAY_BEHAVE_1", nullable = true, length = 20)
	private  Double buyerPayBehave1;//买方当前付款表现1-应付款   日内（含）USD
	
	@Column(name = "BUYER_PAY_BEHAVE_2", nullable = true, length = 20)
	private  Double buyerPayBehave2;//买方当前付款表现2-应付款   日后30天以内USD
	
	@Column(name = "BUYER_PAY_BEHAVE_3", nullable = true, length = 20)
	private  Double buyerPayBehave3;//买方当前付款表现3-应付款   日后31-60天USD
	
	@Column(name = "BUYER_PAY_BEHAVE_4", nullable = true, length = 20)
	private  Double buyerPayBehave4;//买方当前付款表现4-应付款   日后60天以上USD
	
	@Column(name = "BUYER_OWE_REASON_CODE", nullable = true, length = 20)
	private String buyerOweReasonCode;//逾期原因代码
	
	@Column(name = "BUYER_OWE_ELSE_REASON", nullable = true, length = 2000)
	private String buyerOweElseReason;//其他逾期原因
	
	@Column(name = "REMARK", nullable = true, length = 4000)
	private String remark;			//其他说明/备注
	
	@Column(name = "DECLARATION", nullable = false, length = 1)
	private String declaration;	//被保险人声明
	
	@Column(name = "IF_APPLY_CREDIT", nullable = true, length = 1)
	private String ifapplycredit; //是否随限额申请发起对买家的资信调查申请
	
	@Column(name = "EMPLOYEE_NAME", nullable = true, length = 50)
	private String employeeName;	//业务员名称
	
	@Column(name = "APPLICANT", nullable = true, length = 50)
	private String applicant;		//申请人
	
	@Column(name = "APPLY_TIME", nullable = true)
	private Date applyTime;	//申请时间	
	
	@Column(name = "FILE_NUM", nullable = true, length = 5)
	private Long filenum;  //附件个数
	
	@Column(name = "IF_SAME_WITH_POLICY", nullable = true, length = 1)
	private String ifsamewithpolicy;  //是否与保单出运前约定一致
	
	@Column(name = "PRECREDIT_TERM", nullable = true, length = 3)
	private Long precreditterm;		//出运前信用期限
	
	@Column(name = "AFTER_CREDIT_TERM", nullable = true, length = 5)
	private Long aftercreditterm;	//出运后信用期限
	
	@Column(name = "ITEM1", nullable = true, length = 500)
	private String item1;			//备用字段1
	
	@Column(name = "ITEM2", nullable = true, length = 500)
	private String item2;			//备用字段2
	
	@Column(name = "ITEM3", nullable = true, length = 500)
	private String item3;			//备用字段3
	
	@Column(name = "ITEM4", nullable = true, length = 500)
	private String item4;			//备用字段4
	
	@Column(name = "ITEM5", nullable = true, length = 500)
	private String item5;			//备用字段5
	
	@Column(name = "IF_HAVE_TRADE_FINANCING", nullable = false, length = 1)
	private String ifhavetradefinancing;		//在本信用限额项下是否有贸易融资需求
	
	@Column(name = "IF_HAVE_RELATION", nullable = false, length = 1)
	private String ifhaverelation;		//被保险人及共保人、关联公司、代理人项下是否与买方存在关联关系
	
	@Column(name = "RELATION_DETAIL", nullable = true, length = 4000)
	private String relationdetail;		//具体关联情况
	
	@Column(name = "IS_SAME_WITH_CONTRACT", nullable = false, length = 1)
	private String issamewithcontract;		//被保险人与买方历史交易记录中付款人是否与合同买方一致
	
	@Column(name = "NO_SAME_WITH_CONTRACT_REASON", nullable = true, length = 4000)
	private String nosamewithcontractreason;		//付款人名称及不一致的原因
	
	@Column(name = "APPLY_STATUS", nullable = false, length = 12)
	private String apply_status;		//申请状态
	
	@Column(name = "APPR_STATUS", nullable = true, length = 1)
	private Long approveFlag; 		// 审批标志：0-申请退回/不通过；1-通过/限额发布通知
	
	@Column(name = "APPR_COMMENTS", nullable = true, length = 512)
	private String unAcceptReason; 	// 申请退回/不通过原因
	
	@Column(name = "APPR_DATE", nullable = true)
	private Date notifyTime;	//最新批复时间(通知)时间
	
	//批复信息
	@Column(name = "QUOTA_NO", nullable = true, length = 30)
	private String quotaNo;			//	限额编号
	
	@Column(name = "PAY_TERM", nullable = true, length = 4)
	private Long payTerm;			//	信用期限
	
	@Column(name = "QUOTA_SUM", nullable = true, length = 20)
	private Double quotaSum;		//	信用限额
	
	@Column(name = "EFFECT_DATE", nullable = true)
	private Date effectDate;		//	生效日期
	
	@Column(name = "LAPSE_DATE", nullable = true)
	private Date lapseDate;			//	失效日期
	
	@Column(name = "AUDIT_DATE", nullable = true)
	private Date auditDate;			//	批复日期
	
	@Column(name = "BILL_NOTE", nullable = true, length = 4000)
	private String billNote;		//	批复说明（重要信息）
	
	@Column(name = "APPROVE_TYPE", nullable = true, length = 2)
	private String approveType;		//	批复类型代码
	
	@Column(name = "AD_CONDITION", nullable = true, length = 4000)
	private String adCondition;		//	特别生效条件
	
	@Column(name = "IDLE_SPAN", nullable = true, length = 38)
	private Long idleSpan;          //	闲置期 
	
	@Column(name = "IF_REPEAT", nullable = true, length = 1)
	private String ifRepeat;		//	是否循环使用
	
	@Transient
	private String apl_status_desc;		//申请状态说明状态说明(新建Drfat / 已提交Sumited / 已批复Responded)
	
	@Transient
	private String app_status_desc;		//审批状态说明(不通过0 / 限额发布1)
	
	@Transient
	private List<KstarAttachment> attachs = null;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCorpSerialNo() {
		return corpSerialNo;
	}

	public void setCorpSerialNo(String corpSerialNo) {
		this.corpSerialNo = corpSerialNo;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getSinosureBuyerNo() {
		return sinosureBuyerNo;
	}

	public void setSinosureBuyerNo(String sinosureBuyerNo) {
		this.sinosureBuyerNo = sinosureBuyerNo;
	}

	public String getCorpBuyerNo() {
		return corpBuyerNo;
	}

	public void setCorpBuyerNo(String corpBuyerNo) {
		this.corpBuyerNo = corpBuyerNo;
	}

	public String getBuyerChnName() {
		return buyerChnName;
	}

	public void setBuyerChnName(String buyerChnName) {
		this.buyerChnName = buyerChnName;
	}

	public String getBuyerEngName() {
		return buyerEngName;
	}

	public void setBuyerEngName(String buyerEngName) {
		this.buyerEngName = buyerEngName;
	}

	public String getBuyerCountryCode() {
		return buyerCountryCode;
	}

	public void setBuyerCountryCode(String buyerCountryCode) {
		this.buyerCountryCode = buyerCountryCode;
	}

	public String getBuyerEngAddress() {
		return buyerEngAddress;
	}

	public void setBuyerEngAddress(String buyerEngAddress) {
		this.buyerEngAddress = buyerEngAddress;
	}

	public String getBuyerChnAddress() {
		return buyerChnAddress;
	}

	public void setBuyerChnAddress(String buyerChnAddress) {
		this.buyerChnAddress = buyerChnAddress;
	}

	public String getBuyerRegNo() {
		return buyerRegNo;
	}

	public void setBuyerRegNo(String buyerRegNo) {
		this.buyerRegNo = buyerRegNo;
	}

	public String getBuyerTel() {
		return buyerTel;
	}

	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel;
	}

	public String getBuyerFax() {
		return buyerFax;
	}

	public void setBuyerFax(String buyerFax) {
		this.buyerFax = buyerFax;
	}

	public String getWarrantorType() {
		return warrantorType;
	}

	public void setWarrantorType(String warrantorType) {
		this.warrantorType = warrantorType;
	}

	public String getSinosureWarrantorNo() {
		return sinosureWarrantorNo;
	}

	public void setSinosureWarrantorNo(String sinosureWarrantorNo) {
		this.sinosureWarrantorNo = sinosureWarrantorNo;
	}

	public String getCorpWarrantorNo() {
		return corpWarrantorNo;
	}

	public void setCorpWarrantorNo(String corpWarrantorNo) {
		this.corpWarrantorNo = corpWarrantorNo;
	}

	public String getWarrantorChnName() {
		return warrantorChnName;
	}

	public void setWarrantorChnName(String warrantorChnName) {
		this.warrantorChnName = warrantorChnName;
	}

	public String getWarrantorEngName() {
		return warrantorEngName;
	}

	public void setWarrantorEngName(String warrantorEngName) {
		this.warrantorEngName = warrantorEngName;
	}

	public String getWarrantorCountryCode() {
		return warrantorCountryCode;
	}

	public void setWarrantorCountryCode(String warrantorCountryCode) {
		this.warrantorCountryCode = warrantorCountryCode;
	}

	public String getWarrantorEngAddress() {
		return warrantorEngAddress;
	}

	public void setWarrantorEngAddress(String warrantorEngAddress) {
		this.warrantorEngAddress = warrantorEngAddress;
	}

	public String getWarrantorChnAddress() {
		return warrantorChnAddress;
	}

	public void setWarrantorChnAddress(String warrantorChnAddress) {
		this.warrantorChnAddress = warrantorChnAddress;
	}

	public String getWarrantorRegNo() {
		return warrantorRegNo;
	}

	public void setWarrantorRegNo(String warrantorRegNo) {
		this.warrantorRegNo = warrantorRegNo;
	}

	public String getWarrantorTel() {
		return warrantorTel;
	}

	public void setWarrantorTel(String warrantorTel) {
		this.warrantorTel = warrantorTel;
	}

	public String getWarrantorFax() {
		return warrantorFax;
	}

	public void setWarrantorFax(String warrantorFax) {
		this.warrantorFax = warrantorFax;
	}

	public Long getPayTermApply() {
		return payTermApply;
	}

	public void setPayTermApply(Long payTermApply) {
		this.payTermApply = payTermApply;
	}

	public Double getQuotaSumApply() {
		return quotaSumApply;
	}

	public void setQuotaSumApply(Double quotaSumApply) {
		this.quotaSumApply = quotaSumApply;
	}

	public String getContractPayMode() {
		return contractPayMode;
	}

	public void setContractPayMode(String contractPayMode) {
		this.contractPayMode = contractPayMode;
	}

	public Double getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(Double orderSum) {
		this.orderSum = orderSum;
	}

	public Double getArmSum() {
		return armSum;
	}

	public void setArmSum(Double armSum) {
		this.armSum = armSum;
	}

	public String getTradeNameCode() {
		return tradeNameCode;
	}

	public void setTradeNameCode(String tradeNameCode) {
		this.tradeNameCode = tradeNameCode;
	}

	public String getTradeElseName() {
		return tradeElseName;
	}

	public void setTradeElseName(String tradeElseName) {
		this.tradeElseName = tradeElseName;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
	}

	public String getTradeElseTerms() {
		return tradeElseTerms;
	}

	public void setTradeElseTerms(String tradeElseTerms) {
		this.tradeElseTerms = tradeElseTerms;
	}

	public String getIfHistTrade() {
		return ifHistTrade;
	}

	public void setIfHistTrade(String ifHistTrade) {
		this.ifHistTrade = ifHistTrade;
	}

	public String getEarlyTradeYear() {
		return earlyTradeYear;
	}

	public void setEarlyTradeYear(String earlyTradeYear) {
		this.earlyTradeYear = earlyTradeYear;
	}

	public String getStartDebtYear() {
		return startDebtYear;
	}

	public void setStartDebtYear(String startDebtYear) {
		this.startDebtYear = startDebtYear;
	}

	public String getLastYear1() {
		return lastYear1;
	}

	public void setLastYear1(String lastYear1) {
		this.lastYear1 = lastYear1;
	}

	public String getLastPayMode1() {
		return lastPayMode1;
	}

	public void setLastPayMode1(String lastPayMode1) {
		this.lastPayMode1 = lastPayMode1;
	}

	public Double getLastSum1() {
		return lastSum1;
	}

	public void setLastSum1(Double lastSum1) {
		this.lastSum1 = lastSum1;
	}

	public String getLastYear2() {
		return lastYear2;
	}

	public void setLastYear2(String lastYear2) {
		this.lastYear2 = lastYear2;
	}

	public String getLastPayMode2() {
		return lastPayMode2;
	}

	public void setLastPayMode2(String lastPayMode2) {
		this.lastPayMode2 = lastPayMode2;
	}

	public Double getLastSum2() {
		return lastSum2;
	}

	public void setLastSum2(Double lastSum2) {
		this.lastSum2 = lastSum2;
	}

	public String getLastYear3() {
		return lastYear3;
	}

	public void setLastYear3(String lastYear3) {
		this.lastYear3 = lastYear3;
	}

	public String getLastPayMode3() {
		return lastPayMode3;
	}

	public void setLastPayMode3(String lastPayMode3) {
		this.lastPayMode3 = lastPayMode3;
	}

	public Double getLastSum3() {
		return lastSum3;
	}

	public void setLastSum3(Double lastSum3) {
		this.lastSum3 = lastSum3;
	}

	public Double getBuyerPayBehave1() {
		return buyerPayBehave1;
	}

	public void setBuyerPayBehave1(Double buyerPayBehave1) {
		this.buyerPayBehave1 = buyerPayBehave1;
	}

	public Double getBuyerPayBehave2() {
		return buyerPayBehave2;
	}

	public void setBuyerPayBehave2(Double buyerPayBehave2) {
		this.buyerPayBehave2 = buyerPayBehave2;
	}

	public Double getBuyerPayBehave3() {
		return buyerPayBehave3;
	}

	public void setBuyerPayBehave3(Double buyerPayBehave3) {
		this.buyerPayBehave3 = buyerPayBehave3;
	}

	public Double getBuyerPayBehave4() {
		return buyerPayBehave4;
	}

	public void setBuyerPayBehave4(Double buyerPayBehave4) {
		this.buyerPayBehave4 = buyerPayBehave4;
	}

	public String getBuyerOweReasonCode() {
		return buyerOweReasonCode;
	}

	public void setBuyerOweReasonCode(String buyerOweReasonCode) {
		this.buyerOweReasonCode = buyerOweReasonCode;
	}

	public String getBuyerOweElseReason() {
		return buyerOweElseReason;
	}

	public void setBuyerOweElseReason(String buyerOweElseReason) {
		this.buyerOweElseReason = buyerOweElseReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getIfapplycredit() {
		return ifapplycredit;
	}

	public void setIfapplycredit(String ifapplycredit) {
		this.ifapplycredit = ifapplycredit;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Long getFilenum() {
		return filenum;
	}

	public void setFilenum(Long filenum) {
		this.filenum = filenum;
	}

	public String getIfsamewithpolicy() {
		return ifsamewithpolicy;
	}

	public void setIfsamewithpolicy(String ifsamewithpolicy) {
		this.ifsamewithpolicy = ifsamewithpolicy;
	}

	public Long getPrecreditterm() {
		return precreditterm;
	}

	public void setPrecreditterm(Long precreditterm) {
		this.precreditterm = precreditterm;
	}

	public Long getAftercreditterm() {
		return aftercreditterm;
	}

	public void setAftercreditterm(Long aftercreditterm) {
		this.aftercreditterm = aftercreditterm;
	}

	public String getItem1() {
		return item1;
	}

	public void setItem1(String item1) {
		this.item1 = item1;
	}

	public String getItem2() {
		return item2;
	}

	public void setItem2(String item2) {
		this.item2 = item2;
	}

	public String getItem3() {
		return item3;
	}

	public void setItem3(String item3) {
		this.item3 = item3;
	}

	public String getItem4() {
		return item4;
	}

	public void setItem4(String item4) {
		this.item4 = item4;
	}

	public String getItem5() {
		return item5;
	}

	public void setItem5(String item5) {
		this.item5 = item5;
	}

	public String getIfhavetradefinancing() {
		return ifhavetradefinancing;
	}

	public void setIfhavetradefinancing(String ifhavetradefinancing) {
		this.ifhavetradefinancing = ifhavetradefinancing;
	}

	public String getIfhaverelation() {
		return ifhaverelation;
	}

	public void setIfhaverelation(String ifhaverelation) {
		this.ifhaverelation = ifhaverelation;
	}

	public String getRelationdetail() {
		return relationdetail;
	}

	public void setRelationdetail(String relationdetail) {
		this.relationdetail = relationdetail;
	}

	public String getIssamewithcontract() {
		return issamewithcontract;
	}

	public void setIssamewithcontract(String issamewithcontract) {
		this.issamewithcontract = issamewithcontract;
	}

	public String getNosamewithcontractreason() {
		return nosamewithcontractreason;
	}

	public void setNosamewithcontractreason(String nosamewithcontractreason) {
		this.nosamewithcontractreason = nosamewithcontractreason;
	}

	public String getApply_status() {
		return apply_status;
	}

	public void setApply_status(String apply_status) {
		this.apply_status = apply_status;
	}

	public Long getApproveFlag() {
		return approveFlag;
	}

	public void setApproveFlag(Long approveFlag) {
		this.approveFlag = approveFlag;
	}

	public String getUnAcceptReason() {
		return unAcceptReason;
	}

	public void setUnAcceptReason(String unAcceptReason) {
		this.unAcceptReason = unAcceptReason;
	}

	public Date getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getC_customer_id() {
		return c_customer_id;
	}

	public void setC_customer_id(String c_customer_id) {
		this.c_customer_id = c_customer_id;
	}
	
	public String getApl_status_desc() {
		return apl_status_desc;
	}

	public void setApl_status_desc(String apl_status_desc) {
		this.apl_status_desc = apl_status_desc;
	}

	public String getApp_status_desc() {
		return app_status_desc;
	}

	public void setApp_status_desc(String app_status_desc) {
		this.app_status_desc = app_status_desc;
	}
	
	public String getBuyer_reg_address() {
		return buyer_reg_address;
	}

	public void setBuyer_reg_address(String buyer_reg_address) {
		this.buyer_reg_address = buyer_reg_address;
	}
	
	public List<KstarAttachment> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<KstarAttachment> attachs) {
		this.attachs = attachs;
	}
	
	public String getQuotaNo() {
		return quotaNo;
	}

	public void setQuotaNo(String quotaNo) {
		this.quotaNo = quotaNo;
	}

	public Long getPayTerm() {
		return payTerm;
	}

	public void setPayTerm(Long payTerm) {
		this.payTerm = payTerm;
	}

	public Double getQuotaSum() {
		return quotaSum;
	}

	public void setQuotaSum(Double quotaSum) {
		this.quotaSum = quotaSum;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getLapseDate() {
		return lapseDate;
	}

	public void setLapseDate(Date lapseDate) {
		this.lapseDate = lapseDate;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getBillNote() {
		return billNote;
	}

	public void setBillNote(String billNote) {
		this.billNote = billNote;
	}

	public String getApproveType() {
		return approveType;
	}

	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}

	public String getAdCondition() {
		return adCondition;
	}

	public void setAdCondition(String adCondition) {
		this.adCondition = adCondition;
	}

	public Long getIdleSpan() {
		return idleSpan;
	}

	public void setIdleSpan(Long idleSpan) {
		this.idleSpan = idleSpan;
	}

	public String getIfRepeat() {
		return ifRepeat;
	}

	public void setIfRepeat(String ifRepeat) {
		this.ifRepeat = ifRepeat;
	}

	public void setStatusDesc(){
		//(不通过0 / 限额发布1)
		Long appVal = this.getApproveFlag();
		if(null != appVal){
			if(0 == appVal.intValue()){
				this.setApp_status_desc("不通过");
			} else if (1 == appVal.intValue()){
				this.setApp_status_desc("限额发布");
			}
		}
		
		//(新建Drfat / 已提交Sumited / 已批复Responded)
		String aplVal = this.getApply_status();
		if(null != aplVal){
			switch(aplVal){
			case "Drfat" :
				this.setApl_status_desc("新建");
				break;
			case "Sumited" :
				this.setApl_status_desc("已提交");
				break;
			case "Responded" :
				this.setApl_status_desc("已批复");
				break;
			default :
				break;
			}
		}
	}
}
