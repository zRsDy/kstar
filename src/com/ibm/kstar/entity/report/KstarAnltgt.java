/** 
 * Project Name:crm 
 * File Name:KstarAnltgt.java 
 * Package Name:com.ibm.kstar.entity.report 
 * Date:Mar 15, 20173:14:51 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.entity.report;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * ClassName:KstarAnltgt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 15, 2017 3:14:51 PM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Entity
@Table(name = "CRM_T_ANL_SALTRT")
public class KstarAnltgt implements Serializable {

	/**
	 * serialVersionUID:TODO
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "kstaranltgt_id_generator")
	@GenericGenerator(name="kstaranltgt_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 类型
	@Column(name = "C_TYP")
	private String ctype;
	
	// 部门
	@Column(name = "C_DEP")
	private String dep;
	
	// 部门ID
	@Column(name = "C_DEP_ID")
	private String depid;
	
	// 销售员
	@Column(name = "C_EMP")
	private String emp;
	
	// 销售员ID
	@Column(name = "C_EMP_ID")
	private String empId;
	
	// 销售员岗位ID
	@Column(name = "C_POS_ID")
	private String posId;
	
	// 销售员岗位名称
	@Transient
	private String posNm;
	
	// 工号
	@Column(name = "C_EMP_NM")
	private String empNm;
	
	// 币种
	@Column(name = "C_CURRENCY")
	private String currency;
	
	// 年度
	@Column(name = "C_ANNUAL")
	private String annual;
	
	// 年度目标
	@Column(name = "N_ANL_TRG")
	private Double anlTrg;
	
	// 1月
	@Column(name = "N_JAN_TRG")
	private Double janTrg;
	
	// 2月
	@Column(name = "N_FEB_TRG")
	private Double febTrg;
	
	// 3月
	@Column(name = "N_MRC_TRG")
	private Double mrcTrg;
	
	// 4月
	@Column(name = "N_APR_TRG")
	private Double aprTrg;
	
	// 5月
	@Column(name = "N_MAY_TRG")
	private Double mayTrg;
	
	// 6月
	@Column(name = "N_JUN_TRG")
	private Double junTrg;
	
	// 7月
	@Column(name = "N_JUL_TRG")
	private Double julTrg;
	
	// 8月
	@Column(name = "N_AGT_TRG")
	private Double agtTrg;
	
	// 9月
	@Column(name = "N_SEP_TRG")
	private Double sepTrg;
	
	// 10月
	@Column(name = "N_OCT_TRG")
	private Double octTrg;
	
	// 11月
	@Column(name = "N_NOV_TRG")
	private Double novTrg;
	
	// 12月
	@Column(name = "N_DEC_TRG")
	private Double decTrg;

	
	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getPosNm() {
		LovMember lov = new LovMember();
        Object obj = CacheData.getInstance().get(posId);
        if(obj != null ){
            BeanUtils.copyPropertiesIgnoreNull(obj, lov);
        }
		return lov.getName();
	}

	public void setPosNm(String posNm) {
		this.posNm = posNm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getCtypeName() {
		return this.getLovName(ctype);
	}
	
	
	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public String getEmp() {
		return emp;
	}

	public void setEmp(String emp) {
		this.emp = emp;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpNm() {
		return empNm;
	}

	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyName() {
		return this.getLovName(currency);
	}
	
	
	public String getAnnual() {
		return annual;
	}

	public void setAnnual(String annual) {
		this.annual = annual;
	}

	public Double getAnlTrg() {
		return anlTrg;
	}

	public void setAnlTrg(Double anlTrg) {
		this.anlTrg = anlTrg;
	}

	public Double getJanTrg() {
		return janTrg;
	}

	public void setJanTrg(Double janTrg) {
		this.janTrg = janTrg;
	}

	public Double getFebTrg() {
		return febTrg;
	}

	public void setFebTrg(Double febTrg) {
		this.febTrg = febTrg;
	}

	public Double getMrcTrg() {
		return mrcTrg;
	}

	public void setMrcTrg(Double mrcTrg) {
		this.mrcTrg = mrcTrg;
	}

	public Double getAprTrg() {
		return aprTrg;
	}

	public void setAprTrg(Double aprTrg) {
		this.aprTrg = aprTrg;
	}

	public Double getMayTrg() {
		return mayTrg;
	}

	public void setMayTrg(Double mayTrg) {
		this.mayTrg = mayTrg;
	}

	public Double getJunTrg() {
		return junTrg;
	}

	public void setJunTrg(Double junTrg) {
		this.junTrg = junTrg;
	}

	public Double getJulTrg() {
		return julTrg;
	}

	public void setJulTrg(Double julTrg) {
		this.julTrg = julTrg;
	}

	public Double getAgtTrg() {
		return agtTrg;
	}

	public void setAgtTrg(Double agtTrg) {
		this.agtTrg = agtTrg;
	}

	public Double getSepTrg() {
		return sepTrg;
	}

	public void setSepTrg(Double sepTrg) {
		this.sepTrg = sepTrg;
	}

	public Double getOctTrg() {
		return octTrg;
	}

	public void setOctTrg(Double octTrg) {
		this.octTrg = octTrg;
	}

	public Double getNovTrg() {
		return novTrg;
	}

	public void setNovTrg(Double novTrg) {
		this.novTrg = novTrg;
	}

	public Double getDecTrg() {
		return decTrg;
	}

	public void setDecTrg(Double decTrg) {
		this.decTrg = decTrg;
	}

	
	public String getLovName(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
	
	
	
}
