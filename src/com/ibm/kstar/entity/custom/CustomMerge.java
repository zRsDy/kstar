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
@Table(name = "crm_t_custom_merge")
@Permission(businessType = "CUSTOM_MERGE_PROC")
public class CustomMerge implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	@Id
	@GeneratedValue(generator = "crm_t_custom_merge_generator")
	@GenericGenerator(name = "crm_t_custom_merge_generator", strategy = "uuid")
	@PermissionBusinessId
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	private String id;

	/** 合并编号 */
	@Column(name = "c_merge_code", nullable = false, length = 32)
	private String mergeCode;

	/** 重复客户编号 */
	@Column(name = "c_custom_code_frm")
	private String customCodeFrm;

	/** 重复客户名称 */
	@Column(name = "c_custom_name_frm")
	private String customNameFrm;

	/** 合并至客户编号 */
	@Column(name = "c_custom_code_to")
	private String customCodeTo;

	/** 合并至客户名称 */
	@Column(name = "c_custom_name_to")
	private String customNameTo;

	/** 合并原因 */
	@Column(name = "c_merge_reason")
	private String mergeReason;

	@Transient
	private String mergeReasonGrid;

	public Object getMergeReasonGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(mergeReason);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public void setMergeReasonGrid(String mergeReasonGrid) {
		this.mergeReasonGrid = mergeReasonGrid;
	}

	/** 状态 */
	@Column(name = "c_merge_status")
	private String mergeStatus;

	@Transient
	private String mergeStatusName;

	public String getMergeStatusName() {

		LovMember lov = ((LovMember) CacheData.getInstance().get(mergeStatus));
		return lov == null ? null : lov.getName();
	}

	public void setMergeStatusName(String mergeStatusName) {
		this.mergeStatusName = mergeStatusName;
	}

	/** 合并日期 */
	@Column(name = "c_merge_date")
	private String mergeDate;

	/** ERP已合并 */
	@Column(name = "c_merge_erp_flg")
	private String mergeErpFlg;

	@Transient
	private String mergeErpFlgGrid;

	public Object getMergeErpFlgGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(mergeErpFlg);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public void setMergeErpFlgGrid(String mergeErpFlgGrid) {
		this.mergeErpFlgGrid = mergeErpFlgGrid;
	}

	/** 申请人 */
	@Column(name = "APPLIER_ID")
	private String applier;

	@Transient
	private String applierName;

	public String getApplierName() {
		Employee lov = ((Employee) CacheData.getInstance().get(applier));
		return lov == null ? null : lov.getName().concat("/").concat(lov.getNo());
	}

	/** 申请人岗位 */
	@Column(name = "APPLIER_POS_ID")
	private String applierPos;

	/** 申请人组织 */
	@Column(name = "APPLIER_ORG_ID")
	private String applierOrg;

	@Transient
	private String applierOrgName;

	public String getApplierOrgName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(applierOrg));
		return lov == null ? null : lov.getNamePath();
	}

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

	public String getMergeCode() {
		return mergeCode;
	}

	public void setMergeCode(String mergeCode) {
		this.mergeCode = mergeCode;
	}

	public String getCustomCodeFrm() {
		return customCodeFrm;
	}

	public void setCustomCodeFrm(String customCodeFrm) {
		this.customCodeFrm = customCodeFrm;
	}

	public String getCustomNameFrm() {
		return customNameFrm;
	}

	public void setCustomNameFrm(String customNameFrm) {
		this.customNameFrm = customNameFrm;
	}

	public String getCustomCodeTo() {
		return customCodeTo;
	}

	public void setCustomCodeTo(String customCodeTo) {
		this.customCodeTo = customCodeTo;
	}

	public String getCustomNameTo() {
		return customNameTo;
	}

	public void setCustomNameTo(String customNameTo) {
		this.customNameTo = customNameTo;
	}

	public String getMergeReason() {
		return mergeReason;
	}

	public void setMergeReason(String mergeReason) {
		this.mergeReason = mergeReason;
	}

	public String getMergeStatus() {
		return mergeStatus;
	}

	public void setMergeStatus(String mergeStatus) {
		this.mergeStatus = mergeStatus;
	}

	public String getMergeDate() {
		return mergeDate;
	}

	public void setMergeDate(String mergeDate) {
		this.mergeDate = mergeDate;
	}

	public String getMergeErpFlg() {
		return mergeErpFlg;
	}

	public void setMergeErpFlg(String mergeErpFlg) {
		this.mergeErpFlg = mergeErpFlg;
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