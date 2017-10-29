package com.ibm.kstar.entity.custom;


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

@Entity
@Table(name = "crm_t_custom_relation")
public class CustomRelation implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_relation_pid_generator")
	@GenericGenerator(name="custom_relation_pid_generator", strategy="uuid")
    private String id;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_code", nullable = false, length = 32)
    private String customCode;
    
    /** 相关公司编号 */
    @Column(name = "c_custom_comp_code", nullable = false, length = 300)
    private String customCompCode;
    
    
    
    /** 相关公司关系 */
    @Column(name = "c_custom_relation_lvl", nullable = false, length = 300)
    private String customRelationLvl;
    
    @Transient
	private String relationLvl;
    
    public String getRelationLvl() {
		LovMember lov = (LovMember)CacheData.getInstance().get(customRelationLvl);
		return lov== null ? null:lov.getId();
	}
    
    public void setRelationLvl(String relationLvl) {
		this.relationLvl = relationLvl;
	}
    
    /** 相对性相关公司编号 */
    @Transient
    private String customCodeDisp;
    
    /** 相关公司名称 */
    @Transient
    private String customNameDisp;
    
    /** 相对性相关公司关系 */
    @Transient
    private String customRelationLvlDisp;
    
    /** 有效日期从 */
    @Column(name = "c_custom_relate_valid_from", nullable = true)
    private Date customRelateValidFrom;
    
    /** 有效日期至 */
    @Column(name = "c_custom_relate_valid_to", nullable = true)
    private Date customRelateValidTo;
    
    /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCustomCompCode() {
		return customCompCode;
	}

	public void setCustomCompCode(String customCompCode) {
		this.customCompCode = customCompCode;
	}



	public String getCustomRelationLvl() {
		return customRelationLvl;
	}

	public void setCustomRelationLvl(String customRelationLvl) {
		this.customRelationLvl = customRelationLvl;
	}

	public String getCustomCodeDisp() {
		return customCodeDisp;
	}

	public void setCustomCodeDisp(String customCodeDisp) {
		this.customCodeDisp = customCodeDisp;
	}

	public String getCustomRelationLvlDisp() {
		return customRelationLvlDisp;
	}

	public void setCustomRelationLvlDisp(String customRelationLvlDisp) {
		this.customRelationLvlDisp = customRelationLvlDisp;
	}

	public Date getCustomRelateValidFrom() {
		return customRelateValidFrom;
	}

	public void setCustomRelateValidFrom(Date customRelateValidFrom) {
		this.customRelateValidFrom = customRelateValidFrom;
	}

	public Date getCustomRelateValidTo() {
		return customRelateValidTo;
	}

	public void setCustomRelateValidTo(Date customRelateValidTo) {
		this.customRelateValidTo = customRelateValidTo;
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

	public String getCustomNameDisp() {
		return customNameDisp;
	}

	public void setCustomNameDisp(String customNameDisp) {
		this.customNameDisp = customNameDisp;
	}



}