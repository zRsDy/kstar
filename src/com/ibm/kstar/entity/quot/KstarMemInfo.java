      
package com.ibm.kstar.entity.quot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/** 
 * ClassName:KstarMemInfo <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 19, 2016 1:56:04 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_MEMBER_INF")
public class KstarMemInfo implements Serializable {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator = "kstarmem_id_generator")
	@GenericGenerator(name = "kstarmem_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 销售部门
	@Column(name = "C_MARK_DEPART")
	private String markDpt;
	
	// 责任人
	@Column(name = "C_MARK_RESPON")
	private String memRsp;
	
	// 业务实体
	@Column(name = "C_MARK_OP_UNT")
	private String markOpunt;
	
	// 权限
	@Column(name = "C_MARK_AUTH")
	private String markAuth;
	
	// 有效日期从
	@Column(name = "DT_MARK_VLD_FRM")
	private Date mrkvldfrmDt;
	
	// 有效日期至
	@Column(name = "DT_MARK_VLD_TO")
	private Date mrkvldtoDt;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 团队成员名称
	@Column(name = "C_MEM_NAME")
	private String memName;
	
	// 团队成员角色
	@Column(name = "C_ROLE")
	private String role;

	// 团队成员角色
	@Transient
	private String roleDesc;

	// 备注
	@Column(name = "C_NOTES")
	private String notes;

	
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

	public String getMarkDpt() {
		return markDpt;
	}

	public void setMarkDpt(String markDpt) {
		this.markDpt = markDpt;
	}

	public String getMemRsp() {
		return memRsp;
	}

	public void setMemRsp(String memRsp) {
		this.memRsp = memRsp;
	}

	public String getMarkOpunt() {
		return markOpunt;
	}

	public void setMarkOpunt(String markOpunt) {
		this.markOpunt = markOpunt;
	}

	public String getMarkAuth() {
		return markAuth;
	}

	public void setMarkAuth(String markAuth) {
		this.markAuth = markAuth;
	}

	public String getMrkvldfrmDt() {
		if (mrkvldfrmDt != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(mrkvldfrmDt);
		}
		return null;
	}

	public void setMrkvldfrmDt(Date mrkvldfrmDt) {
		this.mrkvldfrmDt = mrkvldfrmDt;
	}

	public String getMrkvldtoDt() {
		if (mrkvldtoDt != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(mrkvldtoDt);
		}
		return null;
	}

	public void setMrkvldtoDt(Date mrkvldtoDt) {
		this.mrkvldtoDt = mrkvldtoDt;
	}

	public String getCType() {
		return CType;
	}

	public void setCType(String cType) {
		CType = cType;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	 public String getRoleDesc() { 
	 LovMember lov = ((LovMember)CacheData.getInstance().get(role));
	 return lov==null? null : lov.getName();
	 }
}
  
