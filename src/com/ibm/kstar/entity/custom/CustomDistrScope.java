package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "crm_t_custom_distr_scope")
public class CustomDistrScope implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_distr_scope_pid_generator")
	@GenericGenerator(name="custom_distr_scope_pid_generator", strategy="uuid")
    private String id;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_pid", nullable = false, length = 32)
    private String customId;
    
    /** 产品范围 */
    @Column(name = "c_distr_scope", nullable = false, length = 32)
    private String distrScope;
    
    @Transient
	private String distrScopeGrid;
    
    public Object getDistrScopeGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(distrScope);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setDistrScopeGrid(String distrScopeGrid) {
		this.distrScopeGrid = distrScopeGrid;
	}
    
    /** 允许经销 */
    @Column(name = "c_distr_allow", nullable = false, length = 32)
    private String distrAllow;
    
    @Transient
	private String distrAllowGrid;
    
    public Object getDistrAllowGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(distrAllow);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setDistrAllowGrid(String distrAllowGrid) {
		this.distrAllowGrid = distrAllowGrid;
	}
    
    /** 备注 */
    @Column(name = "c_distr_comment", nullable = true, length = 100)
    private String distrComment;
    
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

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getDistrScope() {
		return distrScope;
	}

	public void setDistrScope(String distrScope) {
		this.distrScope = distrScope;
	}

	public String getDistrAllow() {
		return distrAllow;
	}

	public void setDistrAllow(String distrAllow) {
		this.distrAllow = distrAllow;
	}

	public String getDistrComment() {
		return distrComment;
	}

	public void setDistrComment(String distrComment) {
		this.distrComment = distrComment;
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