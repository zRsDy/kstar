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
@Table(name = "crm_t_custom_erp_info")
public class CustomErpInfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_erp_pid_generator")
	@GenericGenerator(name="custom_erp_pid_generator", strategy="uuid")
    private String id;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_pid", nullable = false, length = 32)
    private String customId;
    
    /** 业务实体 */
    @Column(name = "c_corp_erp_unit", nullable = false, length = 100)
    private String corpErpUnit;
    
    @Transient
	private String corpErpUnitGrid;
    
    public Object getCorpErpUnitGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(corpErpUnit);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setCorpErpUnitGrid(String corpErpUnit) {
		this.corpErpUnit = corpErpUnit;
	}
    
    /** 引入地址 */
    @Column(name = "c_corp_leaded_address", nullable = false, length = 32)
    private String corpLeadedAddress;
    
    /** 引入说明 */
    @Column(name = "c_corp_leaded_comment", nullable = true, length = 100)
    private String corpLeadedComment;
    
    /** 状态 */
    @Column(name = "c_corp_erp_status", nullable = true, length = 32)
    private String corpErpStatus;
    
    @Transient
	private String corpErpStatusName;
    
    public String getCorpErpStatusName() {
		
    	LovMember lov = (LovMember)CacheData.getInstance().get(corpErpStatus);
		return  lov==null? null:lov.getName();
	}
    
    public void setCorpErpStatusName(String corpErpStatusName) {
		this.corpErpStatusName = corpErpStatusName;
	}
    
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

	public String getCorpErpUnit() {
		return corpErpUnit;
	}

	public void setCorpErpUnit(String corpErpUnit) {
		this.corpErpUnit = corpErpUnit;
	}

	public String getCorpLeadedAddress() {
		return corpLeadedAddress;
	}

	public void setCorpLeadedAddress(String corpLeadedAddress) {
		this.corpLeadedAddress = corpLeadedAddress;
	}

	public String getCorpLeadedComment() {
		return corpLeadedComment;
	}

	public void setCorpLeadedComment(String corpLeadedComment) {
		this.corpLeadedComment = corpLeadedComment;
	}

	public String getCorpErpStatus() {
		return corpErpStatus;
	}

	public void setCorpErpStatus(String corpErpStatus) {
		this.corpErpStatus = corpErpStatus;
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