package com.sinosure.exchange.edi.po;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

/**
 *买家代码申请信息
*/
public class BuyerCodeApplyInfo {
	private String corpSerialNo;	//企业内部客户唯一标识
	private String clientNo;		//企业标识
	private String shtName;			//买方简称
	private String chnName;			//买方中文名称
	private String engName;			//买方英文名称
	private String countryCode;		//买方国家代码
	private String engAddress;		//买方英文地址
	private String chnAddress;		//买方中文地址
	private String regAddress;		//买方注册地址
	private String regNo;			//买方注册号
	private String tel;				//买方电话
	private String fax;				//买方传真
	private String webAddress;		//网站地址
	private String eMail;			//电子邮件
	private Timestamp setDate;		//成立日期
	private Long regyear;			//注册年份
	private String corporation;		//法人代表
	private Double equity;			//资产净值
	private Double yearSale;		//年销售额
	private String remark;			//备注
	private String applicant;		//申请人
	private Timestamp applyTime;	//申请时间
	private String employees;    //职员姓名
 	private String item1;			//备用字段1
	private String item2;			//备用字段2
	private String item3;			//备用字段3
	private String item4;			//备用字段4
	private String item5;			//备用字段5
	private String policyNo;		//保单编号
	private Long areano;    		//20130807  国内买家代码申请 必填字段  注册区域
	
	public String getEmployees() {
		return employees;
	}
	public void setEmployees(String employees) {
		this.employees = employees;
	}
	public Long getAreano() {
		return areano;
	}
	public void setAreano(Long areano) {
		this.areano = areano;
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
	public String getShtName() {
		return shtName;
	}
	public void setShtName(String shtName) {
		this.shtName = shtName;
	}
	public String getChnName() {
		return chnName;
	}
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getEngAddress() {
		return engAddress;
	}
	public void setEngAddress(String engAddress) {
		this.engAddress = engAddress;
	}
	public String getChnAddress() {
		return chnAddress;
	}
	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}
	public String getRegAddress() {
		return regAddress;
	}
	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public Timestamp getSetDate() {
		return setDate;
	}
	public void setSetDate(Timestamp setDate) {
		this.setDate = setDate;
	}
	public Long getRegyear() {
		return regyear;
	}
	public void setRegyear(Long regyear) {
		this.regyear = regyear;
	}
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	public Double getEquity() {
		return equity;
	}
	public void setEquity(Double equity) {
		this.equity = equity;
	}
	public Double getYearSale() {
		return yearSale;
	}
	public void setYearSale(Double yearSale) {
		this.yearSale = yearSale;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	@Override 
	public String toString(){
		return this.printBean();
	}
	
	private String printBean(){
		StringBuffer sbf = new StringBuffer();
		Method[] mds = BuyerCodeApplyInfo.class.getMethods();
		for(Method m:mds){
			String sMethod = m.getName();
			if(sMethod.startsWith("get")){
				try {
					sbf.append("  ");
					sbf.append(sMethod.substring(3));
					sbf.append("-->");
					sbf.append(m.invoke(this));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return sbf.toString();
	}
}
