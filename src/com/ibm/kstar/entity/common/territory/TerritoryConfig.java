package com.ibm.kstar.entity.common.territory;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * (CRM_T_TERRITORY_CONFIG)
 * 
 * @author bianj
 * @version 1.0.0 2017-04-27
 */
@Entity
@Table(name = "crm_t_territory_config")
public class TerritoryConfig implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /**  */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "territory_config_pid_generator")
   	@GenericGenerator(name="territory_config_pid_generator", strategy="uuid")
    private String id;
    
    /**  */
    @Column(name = "c_org_id", nullable = true, length = 32)
    private String orgId;
    
    @Transient
	private String orgIdName;

	public String getOrgIdName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(orgId));
		return lov == null ? null : lov.getNamePath();
	}
    
    /**  */
    @Column(name = "c_territory", nullable = true, length = 32)
    private String territory;
    
    @Transient
	private String territoryName;

	public String getTerritoryName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(territory));
		return lov == null ? null : lov.getName();
	}
    
    /**  */
    @Column(name = "c_comment", nullable = true, length = 300)
    private String comment;
    
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
    
}