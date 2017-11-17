package com.ibm.kstar.entity.common.doc;

import java.io.Serializable;
import java.util.Date;

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
import com.ibm.kstar.api.system.permission.entity.Employee;

@Entity
@Table(name = "CRM_T_ATTACHMENT")
public class KstarAttachment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 文档ID
	@Id
	@GeneratedValue(generator = "kstardoc_id_generator")
	@GenericGenerator(name="kstardoc_id_generator", strategy="uuid")
	@Column(name="C_ID")
	private String id;

    /** 业务ID */
    @Column(name = "c_biz_id", nullable = true, length = 32)
    private String bizId;
    
    /** 业务模块 */
    @Column(name = "c_biz_type", nullable = true, length = 32)
    private String bizType;
    
    /** 文档名称 */
    @Column(name = "c_doc_nm", nullable = true, length = 300)
    private String docNm;
    
    /** 文档全名-带后缀 */
    @Column(name = "c_doc_fulnm", nullable = true, length = 300)
    private String docFulnm;
    
    /** 附件说明 */
    @Column(name = "c_doc_desc", nullable = true, length = 300)
    private String docDesc;
    
    /** 文档后缀 */
    @Column(name = "c_doc_suffix", nullable = true, length = 32)
    private String docSuffix;
    
    /** 单据类型--文档类型在字典中的group_code */
    @Column(name = "c_doc_group_code", nullable = true, length = 32)
    private String docGroupCode;
    
    /** 文档类型 */
    @Column(name = "c_doc_tp", nullable = true, length = 32)
    private String docTp;

    /** 文档类型描述 **/
	@Transient
	private String docTpDesc;
	
    /** 文档URL */
    @Column(name = "c_doc_url", nullable = true, length = 200)
    private String docUrl;
    
    /** 文档上传本地路径 */
    @Column(name = "c_doc_path", nullable = true, length = 200)
    private String docPath;
    
    /** 上传人 */
    @Column(name = "c_doc_updr", nullable = true, length = 300)
    private String docUpdr;
    
    /** 上传人 */
    @Transient
    private String docUpdrNm;
    
    /** 上传时间 */
    @Column(name = "dt_upd_dt", nullable = true)
    private Date dtUpdDt;
    
    /** 文档状态 */
    @Column(name = "c_doc_status", nullable = true, length = 32)
    private String docStatus;
    
    /** 备注 */
    @Column(name = "c_notes", nullable = true, length = 300)
    private String notes;
    
    /** 扩展字段1 */
    @Column(name = "c_ext1", nullable = true, length = 50)
    private String ext1;
    
    /** 扩展字段2 */
    @Column(name = "c_ext2", nullable = true, length = 50)
    private String ext2;
    
    /** 扩展字段3 */
    @Column(name = "c_ext3", nullable = true, length = 50)
    private String ext3;
    
    /** 扩展字段4 */
    @Column(name = "c_ext4", nullable = true, length = 50)
    private String ext4;
    
    /** 扩展字段5 */
    @Column(name = "c_ext5", nullable = true, length = 50)
    private String ext5;
    
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

    @Transient
    private String remark;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getDocNm() {
		return docNm;
	}

	public void setDocNm(String docNm) {
		this.docNm = docNm;
	}

	public String getDocFulnm() {
		return docFulnm;
	}

	public void setDocFulnm(String docFulnm) {
		this.docFulnm = docFulnm;
	}

	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getDocSuffix() {
		return docSuffix;
	}

	public void setDocSuffix(String docSuffix) {
		this.docSuffix = docSuffix;
	}

	public String getDocGroupCode() {
		return docGroupCode;
	}

	public void setDocGroupCode(String docGroupCode) {
		this.docGroupCode = docGroupCode;
	}

	public String getDocTp() {
		return docTp;
	}

	public void setDocTp(String docTp) {
		this.docTp = docTp;
	}

	public String getDocTpDesc() {
		LovMember lov =  ((LovMember)CacheData.getInstance().get(docTp));
		return lov==null? null : lov.getName();
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getDocUpdr() {
		return docUpdr;
	}

	public void setDocUpdr(String docUpdr) {
		this.docUpdr = docUpdr;
	}

	public Date getDtUpdDt() {
		return dtUpdDt;
	}

	public void setDtUpdDt(Date dtUpdDt) {
		this.dtUpdDt = dtUpdDt;
	}

	public String getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	
	public String getExt2Name() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("NY", ext2);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
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

	public String getDocUpdrNm() {
		Employee u =  ((Employee)CacheData.getInstance().get(docUpdr));
		return u==null? null : u.getName();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
