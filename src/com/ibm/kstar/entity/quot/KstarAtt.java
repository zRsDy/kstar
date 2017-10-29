
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
 * ClassName:KstarAtt <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Dec 23, 2016 4:18:15 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Entity
 @Table(name = "CRM_T_ATT") 
public class KstarAtt implements Serializable {
	
	/** 
	 * serialVersionUID:TODO 
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstaratt_id_generator")
	@GenericGenerator(name = "kstaratt_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;
	
	// 报价单编号
	@Column(name = "C_QID")
	private String quotCode;
	
	// 类型
	@Column(name = "C_TYPE")
	private String CType;
	
	// 文档名称
	@Column(name = "C_DOC_NM")
	private String docNm;

	// 文档类型
	@Column(name = "C_DOC_TP")
	private String docTp;
	

	public String getDocTpDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(docTp));
		return lov==null? null : lov.getName();
	}
	
	@Transient
	private String docTpDesc;
	
	public String getDocTp() {
		return docTp;
	}

	public void setDocTp(String docTp) {
		this.docTp = docTp;
	}

	// 附件说明
	@Column(name = "C_DOC_CMT")
	private String docCmt;
	
	// 上传人
	@Column(name = "C_UPDR")
	private String updr;
	
	// 上传时间
	@Column(name = "DT_UPD_DT")
	private Date updDt;
	
	// 备注
	@Column(name = "C_NOTES")
	private String cNotes;
	
	

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

	public String getDocNm() {
		return docNm;
	}

	public void setDocNm(String docNm) {
		this.docNm = docNm;
	}
	
	public String getDocCmt() {
		return docCmt;
	}

	public void setDocCmt(String docCmt) {
		this.docCmt = docCmt;
	}

	public String getUpdr() {
		return updr;
	}

	public void setUpdr(String updr) {
		this.updr = updr;
	}

	public String getUpdDt() {
		if (updDt!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(updDt);
		}
		return null;
	}

	public void setUpdDt(Date updDt) {
		this.updDt = updDt;
	}

	public String getcNotes() {
		return cNotes;
	}

	public void setcNotes(String cNotes) {
		this.cNotes = cNotes;
	}
	
	
}
  
