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
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

@Entity
@Table(name = "crm_t_custom_share_info")
@Permission(businessType = "CUSTOM_SHARE_PROC")
public class CustomShareInfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_share_info_pid_generator")
	@GenericGenerator(name="custom_share_info_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 分享编号 */
    @Column(name = "c_share_code", nullable = false, length = 32)
    private String shareCode;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_pid", nullable = false, length = 32)
    private String customCode;
    
    /** 客户全称 */
    @Column(name = "c_custom_full_name", nullable = false, length = 300)
    private String customName;
    
    /** 申请状态 */
    @Column(name = "c_share_status", nullable = false, length = 100)
    private String shareStatus;
    
    @Transient
	private String shareStatusName;
    
    public String getShareStatusName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(shareStatus));
		return lov==null? null : lov.getName();
	}
    
    public void setShareStatusName(String shareStatusName) {
		this.shareStatusName = shareStatusName;
	}
    
    /** 申请时间 */
    @Column(name = "c_share_create_time", nullable = true, length = 100)
    private String shareCreateTime;
    
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
    
    /** 申请原因 */
    @Column(name = "c_share_create_reason", nullable = true, length = 32)
    private String shareCreateReason;
    
    @Transient
	private Object shareCreateReasonGrid;
    
    public Object getShareCreateReasonGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(shareCreateReason);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setShareCreateReasonGrid(Object shareCreateReasonGrid) {
		this.shareCreateReasonGrid = shareCreateReasonGrid;
	}
    
    /** 归属部门 */
    @Column(name = "c_share_dept", nullable = true, length = 100)
    private String shareDept;
    
    @Transient
	private String shareDeptName;
    
    public String getShareDeptName() {
		
		LovMember lov = (LovMember) CacheData.getInstance().get(shareDept);
		if (lov != null) {
			return lov.getName();
		}
		return  null;
	}
    
    public void setShareDeptName(String shareDeptName) {
		this.shareDeptName = shareDeptName;
	}
    
    /** 归属销售员 */
    @Column(name = "c_share_sale", nullable = true, length = 100)
    private String shareSale;
    
    @Transient
	private String shareSaleName;
    
    public String getShareSaleName() {
    	Employee lov = (Employee) CacheData.getInstance().get(shareSale);
		if (lov != null) {
			return lov.getName();
		}
		return  null;
	}
    
    public void setShareSaleName(String shareSaleName) {
		this.shareSaleName = shareSaleName;
	}
    
    /** 原因描述 */
    @Column(name = "c_share_comment", nullable = true, length = 100)
    private String shareComment;
    
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

	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
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

	public String getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
	}

	public String getShareCreateTime() {
		return shareCreateTime;
	}

	public void setShareCreateTime(String shareCreateTime) {
		this.shareCreateTime = shareCreateTime;
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

	public String getShareCreateReason() {
		return shareCreateReason;
	}

	public void setShareCreateReason(String shareCreateReason) {
		this.shareCreateReason = shareCreateReason;
	}

	public String getShareComment() {
		return shareComment;
	}

	public void setShareComment(String shareComment) {
		this.shareComment = shareComment;
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

	public String getShareDept() {
		return shareDept;
	}

	public void setShareDept(String shareDept) {
		this.shareDept = shareDept;
	}

	public String getShareSale() {
		return shareSale;
	}

	public void setShareSale(String shareSale) {
		this.shareSale = shareSale;
	}

}