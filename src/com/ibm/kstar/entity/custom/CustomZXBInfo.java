package com.ibm.kstar.entity.custom;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import com.ibm.kstar.entity.BaseEntity;

@Entity
@Table(name = "CRM_INT_ZXB_BUYER_INFOR")
public class CustomZXBInfo extends BaseEntity {

	/** 主键 **/
	@Id
	@GeneratedValue(generator = "CRM_INT_ZXB_BUYER_INFOR_generator")
	@GenericGenerator(name = "CRM_INT_ZXB_BUYER_INFOR_generator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;

	/** 流水号 **/
	@Column(name = "CORP_SERIAL_NUM", nullable = false, length = 50)
	private String corpSerialNo;

	/** CRM系统客户 **/
	@Column(name = "C_CUSTOMER_ID", nullable = false, length = 32)
	private String c_customer_id;

	/** 企业标识 **/
	@Column(name = "CLIENT_NUM", nullable = false, length = 30)
	private String clientNo;

	/** 保险单号 **/
	@Column(name = "POLICY_NUM", nullable = false, length = 30)
	private String policyNo;

	/** 买方简称 **/
	@Column(name = "SHT_NAME", nullable = true, length = 300)
	private String shtName;

	/** 买方中文名称 **/
	@Column(name = "CHN_NAME", nullable = true, length = 500)
	private String chnName;

	/** 买方英文名称 **/
	@Column(name = "ENG_NAME", nullable = true, length = 500)
	private String engName;

	/** 买方国家代码 **/
	@Column(name = "COUNTRY_CODE", nullable = false, length = 3)
	private String countryCode;

	/** 买方英文地址 **/
	@Column(name = "ENG_ADDRESS", nullable = true, length = 300)
	private String engAddress;

	/** 注册区域代码 **/
	@Column(name = "AREA_NUM", nullable = true, length = 4)
	private Long areano;
	
	/** 买方中文地址 **/
	@Column(name = "CHN_ADDRESS", nullable = true, length = 255)
	private String chnAddress;

	/** 买方注册地址 **/
	@Column(name = "REG_ADDRESS", nullable = true, length = 300)
	private String regAddress;

	/** 买方注册号 **/
	@Column(name = "REG_NUM", nullable = true, length = 100)
	private String regNo;

	/** 买方电话 **/
	@Column(name = "TEL", nullable = true, length = 200)
	private String tel;

	/** 买方传真 **/
	@Column(name = "FAX", nullable = true, length = 100)
	private String fax;

	/** 网站地址 **/
	@Column(name = "WEB_ADDRESS", nullable = true, length = 200)
	private String webAddress;

	/** 电子邮件 **/
	@Column(name = "EMAIL", nullable = true, length = 100)
	private String eMail;

	/** 成立日期 **/
	@Column(name = "SET_DATE", nullable = true)
	private Date setDate;

	/** 注册年份 **/
	@Column(name = "REG_YEAR", nullable = true, length = 4)
	private Long regyear;

	/** 法人代表 **/
	@Column(name = "CORPORATION", nullable = true, length = 50)
	private String corporation;

	/** 资产净值 **/
	@Column(name = "EQUITY", nullable = true, length = 20)
	private Double equity;

	/** 年销售额 **/
	@Column(name = "YEAR_SALE", nullable = true, length = 20)
	private Double yearSale;

	/** 备注 **/
	@Column(name = "REMARK", nullable = true, length = 4000)
	private String remark;

	/** 职员名称 **/
	@Column(name = "EMPLOYEES", nullable = true, length = 50)
	private String employees;

	/** 申请人 **/
	@Column(name = "APPLICANT", nullable = true, length = 50)
	private String applicant;

	/** 申请时间 **/
	@Column(name = "APPLY_TIME", nullable = false)
	private Date applyTime;

	/** 备用字段1 **/
	@Column(name = "ITEM1", nullable = true, length = 500)
	private String item1;

	/** 备用字段2 **/
	@Column(name = "ITEM2", nullable = true, length = 500)
	private String item2;

	/** 备用字段3 **/
	@Column(name = "ITEM3", nullable = true, length = 500)
	private String item3;

	/** 备用字段4 **/
	@Column(name = "ITEM4", nullable = true, length = 500)
	private String item4;

	/** 备用字段5 **/
	@Column(name = "ITEM5", nullable = true, length = 500)
	private String item5;

	/** 申请状态 **/
	@Column(name = "APPLY_STATUS", nullable = false, length = 12)
	private String apply_status;

	/** 批复状态 **/
	@Column(name = "APPR_STATUS", nullable = true, length = 1)
	private String appr_status;

	/** 审批不通过原因 **/
	@Column(name = "APPR_COMMENTS", nullable = true, length = 512)
	private String appr_comments;

	/** 批复时间 **/
	@Column(name = "APPR_DATE", nullable = true)
	private Date appr_date;

	/** 中信保买家代码 **/
	@Column(name = "ZXB_BUYER_CODE", nullable = true)
	private String zxb_buyer_code;
	
	@Transient
	private String apl_status_desc;		//申请状态说明状态说明(新建Drfat / 已提交Sumited / 已批复Responded)
	
	@Transient
	private String app_status_desc;		//审批状态说明(不通过0 / 代码发布1)
	
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

	public String getC_customer_id() {
		return c_customer_id;
	}

	public void setC_customer_id(String c_customer_id) {
		this.c_customer_id = c_customer_id;
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

	public String getCorporation() {
		return corporation;
	}

	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmployees() {
		return employees;
	}

	public void setEmployees(String employees) {
		this.employees = employees;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
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

	public String getApply_status() {
		return apply_status;
	}

	public void setApply_status(String apply_status) {
		this.apply_status = apply_status;
	}

	public String getAppr_status() {
		return appr_status;
	}

	public void setAppr_status(String appr_status) {
		this.appr_status = appr_status;
	}

	public String getAppr_comments() {
		return appr_comments;
	}

	public void setAppr_comments(String appr_comments) {
		this.appr_comments = appr_comments;
	}
	
	public Date getSetDate() {
		return setDate;
	}

	public void setSetDate(Date setDate) {
		this.setDate = setDate;
	}

	public Long getRegyear() {
		return regyear;
	}

	public void setRegyear(Long regyear) {
		this.regyear = regyear;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getAppr_date() {
		return appr_date;
	}

	public void setAppr_date(Date appr_date) {
		this.appr_date = appr_date;
	}

	public String getZxb_buyer_code() {
		return zxb_buyer_code;
	}

	public void setZxb_buyer_code(String zxb_buyer_code) {
		this.zxb_buyer_code = zxb_buyer_code;
	}	

	public Long getAreano() {
		return areano;
	}

	public void setAreano(Long areano) {
		this.areano = areano;
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

	public void setStatusDesc(){
		//(不通过0 / 代码发布1)
		String appVal = this.getAppr_status();
		if(null != appVal){
			if("0".equalsIgnoreCase(appVal)){
				this.setApp_status_desc("不通过");
			} else if ("1".equalsIgnoreCase(appVal)){
				this.setApp_status_desc("代码发布");
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
