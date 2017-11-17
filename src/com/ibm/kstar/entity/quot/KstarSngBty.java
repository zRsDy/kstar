       
package com.ibm.kstar.entity.quot;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
 
/** 
 * ClassName:KstarSngBty <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 28, 2016 6:58:59 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_SNG_BATTERY")
public class KstarSngBty implements Serializable {
	 
	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarsngbty_id_generator")
	@GenericGenerator(name = "kstarsngbty_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 项目技术要求
	@Column(name = "C_PRJTCH_RQ")
	private String prjtchRq;
	
	// 项目技术要求名称
	@Column(name = "C_PRJTCH_RQNM")
	private String prjtchRqnm;
	
	// 现场布放图纸
	@Column(name = "C_ENVLYT_DRFT")
	private String envlytDrt;
	
	// 现场布放图纸名称
	@Column(name = "C_ENVLYT_DRFTNM")
	private String envlytDrtnm;
	
	// 电池容量
	@Column(name = "C_BTT_CP")
	private String bttCp;
	
	// 电池总数量
	@Column(name = "C_BTT_TNM")
	private String bttTnm;
	
	// 电池组数
	@Column(name = "C_BTT_CNM")
	private String bttCnm;
	
	// 是否需要电池单体监控
	@Column(name = "C_IF_BTTMNT")
	private String ifBttmnt;
	
	// 电池监控方式
	@Column(name = "C_BTTMNT_TYP")
	private String bttmntTyp;
	
	// 电池安装形式
	@Column(name = "C_BTTINS_TYP")
	private String bttTinsTyp;
	
	// 电池摆放方式
	@Column(name = "C_BTTPS_TYP")
	private String bttpsTyp;
	
	// 电池柜架进出线方式
	@Column(name = "C_BTTRCKLN_TYP")
	private String bttrckTyp;
	
	// 电池安装空间梁下净高
	@Column(name = "N_BTTINS_HGH")
	private String bttinsHgh;
	
	// 电池开关安装
	@Column(name = "C_BTTSWT_INS")
	private String bttswtIns;
	
	// 其他特殊要求
	@Column(name = "C_OTHERS_RQ")
	private String othRq;
	
	// 电池开关是否要远程控制
	@Column(name = "C_BTTSWT_RMT")
	private String bttswtRmt;

	public KstarSngBty() {
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

	public String getCType() {
		return CType;
	}

	public void setCType(String cType) {
		CType = cType;
	}

	public String getPrjtchRq() {
		return prjtchRq;
	}

	public void setPrjtchRq(String prjtchRq) {
		this.prjtchRq = prjtchRq;
	}

	public String getEnvlytDrt() {
		return envlytDrt;
	}

	public void setEnvlytDrt(String envlytDrt) {
		this.envlytDrt = envlytDrt;
	}

	public String getBttCp() {
		return bttCp;
	}

	public void setBttCp(String bttCp) {
		this.bttCp = bttCp;
	}

	public String getBttTnm() {
		return bttTnm;
	}

	public void setBttTnm(String bttTnm) {
		this.bttTnm = bttTnm;
	}

	public String getBttCnm() {
		return bttCnm;
	}

	public void setBttCnm(String bttCnm) {
		this.bttCnm = bttCnm;
	}

	public String getIfBttmnt() {
		return ifBttmnt;
	}

	public void setIfBttmnt(String ifBttmnt) {
		this.ifBttmnt = ifBttmnt;
	}

	public String getBttmntTyp() {
		return bttmntTyp;
	}

	public void setBttmntTyp(String bttmntTyp) {
		this.bttmntTyp = bttmntTyp;
	}

	public String getBttTinsTyp() {
		return bttTinsTyp;
	}

	public void setBttTinsTyp(String bttTinsTyp) {
		this.bttTinsTyp = bttTinsTyp;
	}

	public String getBttpsTyp() {
		return bttpsTyp;
	}

	public void setBttpsTyp(String bttpsTyp) {
		this.bttpsTyp = bttpsTyp;
	}

	public String getBttrckTyp() {
		return bttrckTyp;
	}

	public void setBttrckTyp(String bttrckTyp) {
		this.bttrckTyp = bttrckTyp;
	}

	public String getBttinsHgh() {
		return bttinsHgh;
	}

	public void setBttinsHgh(String bttinsHgh) {
		this.bttinsHgh = bttinsHgh;
	}

	public String getBttswtIns() {
		return bttswtIns;
	}

	public void setBttswtIns(String bttswtIns) {
		this.bttswtIns = bttswtIns;
	}

	public String getOthRq() {
		return othRq;
	}

	public void setOthRq(String othRq) {
		this.othRq = othRq;
	}

	public String getBttswtRmt() {
		return bttswtRmt;
	}

	public void setBttswtRmt(String bttswtRmt) {
		this.bttswtRmt = bttswtRmt;
	}

	public String getPrjtchRqnm() {
		return prjtchRqnm;
	}

	public void setPrjtchRqnm(String prjtchRqnm) {
		this.prjtchRqnm = prjtchRqnm;
	}

	public String getEnvlytDrtnm() {
		return envlytDrtnm;
	}

	public void setEnvlytDrtnm(String envlytDrtnm) {
		this.envlytDrtnm = envlytDrtnm;
	}

}
  
