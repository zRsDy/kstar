package com.sinosure.exchange.edi.po;
import java.sql.Timestamp;
import java.util.List;
/**
 *非LC限额申请基本信息
 */
public class NoLcQuotaApplyInfoV2{
	private String corpSerialNo;	//企业内部非LC限额申请唯一标识
	private String clientNo;			//企业标识
	private String policyNo;			//保险单号
	private String sinosureBuyerNo;	//中国信保买方代码
	private String corpBuyerNo;		//企业买方代码
	private String buyerChnName;		//买方中文名称
	private String buyerEngName;		//买方英文名称
	private String buyerCountryCode;	//买方国家代码
	private String buyerEngAddress;	//买方英文地址
	private String buyerChnAddress;	//买方中文地址
	private String buyerRegNo;		//买方注册号
	private String buyerTel;			//买方电话
	private String buyerFax;			//买方传真
	private String warrantorType;	//是否有担保人:0无，1有
	private String sinosureWarrantorNo;//中国信保担保人代码
	private String corpWarrantorNo;	//企业担保人代码
	private String warrantorChnName;	//担保人中文名称
	private String warrantorEngName;	//担保人英文名称
	private String warrantorCountryCode;//担保人国家代码
	private String warrantorEngAddress;//担保人英文地址
	private String warrantorChnAddress;//担保人中文地址
	private String warrantorRegNo;	//担保人注册号
	private String warrantorTel;		//担保人电话
	private String warrantorFax;		//担保人传真
	private Long payTermApply;		//申请信用期限
	private Double quotaSumApply;	//申请信用限额
	private String contractPayMode;	//合同支付方式
	private Double orderSum;//当前在手订单金额
	private Double armSum; //当前应收账款余额
	private String tradeNameCode;	//出口商品名称代码
	private String tradeElseName;	//其他出口商品名称
	private String tradeTerms;//贸易术语
	private String TradeElseTerms;//其他贸易术语名称
	private String ifHistTrade;		//是否有历史交易：0无，1 有 
	private String earlyTradeYear;	//最早成交年份
	private String startDebtYear;	//开始放账年份
	private String lastYear1;//最近三年交易年份1
	private String lastPayMode1;//最近三年交易结算方式1
	private Double lastSum1;//最近三年交易额1
	private String lastYear2;//最近三年交易年份2
	private String lastPayMode2;//最近三年交易结算方式2
	private Double lastSum2;//最近三年交易额2
	private String lastYear3;//最近三年交易年份3
	private String lastPayMode3;//最近三年交易结算方式3
	private Double lastSum3;//最近三年交易额3
	private  Double buyerPayBehave1;//买方当前付款表现1-应付款   日内（含）USD
	private  Double buyerPayBehave2;//买方当前付款表现2-应付款   日后30天以内USD
	private  Double buyerPayBehave3;//买方当前付款表现3-应付款   日后31-60天USD
	private  Double buyerPayBehave4;//买方当前付款表现4-应付款   日后60天以上USD
	private String buyerOweReasonCode;//逾期原因代码
	private String buyerOweElseReason;//其他逾期原因
	private String remark;			//其他说明/备注
	private String declaration;	//被保险人声明
	private String employeeName;	//业务员名称
	private String applicant;		//申请人
	private Timestamp applyTime;	//申请时间	
	private String item1;			//备用字段1
	private String item2;			//备用字段2
	private String item3;			//备用字段3
	private String item4;			//备用字段4
	private String item5;			//备用字段5
	private Long filenum = new Long(0);			//附件个数
	private List<Attafile> afs = null;			//附件列表 
	
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
		return TradeElseTerms;
	}
	public void setTradeElseTerms(String tradeElseTerms) {
		TradeElseTerms = tradeElseTerms;
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
	public Timestamp getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
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
	public Long getFilenum() {
		return filenum;
	}
	public void setFilenum(Long filenum) {
		this.filenum = filenum;
	}
	public List<Attafile> getAfs() {
		return afs;
	}
	public void setAfs(List<Attafile> afs) {
		this.afs = afs;
	}
}