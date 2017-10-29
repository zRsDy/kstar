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
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

@Entity
@Table(name = "crm_t_custom_competitor_report")
@Permission(businessType = "CUSTOM_COMPETITOR_PROC")
public class CustomCompetitorReport implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @GeneratedValue(generator = "custom_competitor_report_generator")
	@GenericGenerator(name="custom_competitor_report_generator", strategy="uuid")
    @PermissionBusinessId
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    private String id;
    
    /** 报告编号 */
    @Column(name = "c_report_code", nullable = false, length = 32)
    private String reportCode;
    
    /** 客户编号 */
    @Column(name = "c_custom_code_team_in", nullable = false, length = 32)
    private String customCode;
    
    /** 内部公司 */
    @Column(name = "c_custom_name_team_in", nullable = true, length = 32)
    private String customName;
    
    /** 报告状态 */
    @Column(name = "c_status", nullable = false, length = 32)
    private String status;
    
    @Transient
	private String statusName;
    
    public String getStatusName() {
		
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(status));
		return lov==null? null : lov.getName();
	}
    
    public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
    
    /** 报告日期 */
    @Column(name = "c_reoprt_date", nullable = false)
    private String reoprtDate;
    
    /** 申请人 */
    @Column(name = "APPLIER_ID", nullable = true, length = 32)
    private String applier;
    
    @Transient
	private String applierName;
    
    public String getApplierName() {
    	Employee lov =  ((Employee)CacheData.getInstance().get(applier));
		return lov==null? null : lov.getName().concat("/").concat(lov.getNo());
	}
    
    /** 申请人岗位 */
    @Column(name = "APPLIER_POS_ID", nullable = true, length = 32)
    private String applierPos;
    
    /** 申请人组织 */
    @Column(name = "APPLIER_ORG_ID", nullable = true, length = 32)
    private String applierOrg;
    
    @Transient
	private String applierOrgName;
    
    public String getApplierOrgName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(applierOrg));
		return lov==null? null : lov.getNamePath();
	}
    
    /** 备注 */
    @Column(name = "c_comment", nullable = true, length = 32)
    private String comment;
    
    /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    @PermissionUserId
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    @PermissionPositionId
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    @PermissionOrgId
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

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReoprtDate() {
		return reoprtDate;
	}

	public void setReoprtDate(String reoprtDate) {
		this.reoprtDate = reoprtDate;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierPos() {
		return applierPos;
	}

	public void setApplierPos(String applierPos) {
		this.applierPos = applierPos;
	}

	public String getApplierOrg() {
		return applierOrg;
	}

	public void setApplierOrg(String applierOrg) {
		this.applierOrg = applierOrg;
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

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public void setApplierOrgName(String applierOrgName) {
		this.applierOrgName = applierOrgName;
	}


}