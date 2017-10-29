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
@Table(name = "crm_t_custom_handover_list")
@Permission(businessType = "CUSTOM_HANDOVER_PROC")
public class CustomHandoverList implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_handover_list_pid_generator")
	@GenericGenerator(name="custom_handover_list_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 交接编号 */
    @Column(name = "c_handover_code", nullable = false, length = 32)
    private String handoverCode;

    /** 申请状态 */
    @Column(name = "c_handover_status", nullable = false, length = 100)
    private String handoverStatus;
    
	@Transient
	private String handoverStatusName;

	public String getHandoverStatusName() {

		LovMember lov =  ((LovMember)CacheData.getInstance().get(handoverStatus));
		return lov==null? null : lov.getName();
	}

	public void setHandoverStatusName(String handoverStatusName) {
		this.handoverStatusName = handoverStatusName;
	}
    
    /** 申请日期 */
    @Column(name = "c_handover_date", nullable = true)
    private String handoverDate;
    
    /** 交接原因 */
    @Column(name = "c_handover_reason", nullable = true, length = 100)
    private String handoverReason;
    
    @Transient
	private String handoverReasonGrid;
    
    public Object getHandoverReasonGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(handoverReason);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setHandoverReasonGrid(String handoverReasonGrid) {
		this.handoverReasonGrid = handoverReasonGrid;
	}
    
    /** 交接人FromOrg */
    @Column(name = "c_handover_from_org", nullable = true, length = 32)
    private String handoverFromOrg;
    
    /** 交接人FromPos */
    @Column(name = "c_handover_from_pos", nullable = true, length = 32)
    private String handoverFromPos;
    
    @Transient
   	private String positionFromName;
    public String getPositionFromName() {
		
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(handoverFromPos));
		return lov==null? null : lov.getName();
	}
    
    @Transient
   	private String positionToName;
    public String getPositionToName() {
		
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(handoverToPos));
		return lov==null? null : lov.getName();
	}
    
    /** 交接人ToOrg */
    @Column(name = "c_handover_to_pos", nullable = true, length = 32)
    private String handoverToPos;
    
    /** 交接人ToOrg */
    @Column(name = "c_handover_to_org", nullable = true, length = 32)
    private String handoverToOrg;
    
    /** 交接人 */
    @Column(name = "c_handover_from", nullable = true, length = 100)
    private String handoverFrom;
    
    @Transient
	private String handoverFromNo;
    
    public String getHandoverFromNo() {
    	
    	Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(handoverFrom);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getNo();
	}
    
    public void setHandoverFromNo(String handoverFromNo) {
		this.handoverFromNo = handoverFromNo;
	}
    
    @Transient
	private String handoverFromName;
    
    public String getHandoverFromName() {
    	Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(handoverFrom);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getName();
	}
    
    public void setHandoverFromName(String handoverFromName) {
		this.handoverFromName = handoverFromName;
	}
    
    /** 交接至 */
    @Column(name = "c_handover_to", nullable = true, length = 100)
    private String handoverTo;
    
    @Transient
	private String handoverToNo;
    
    public String getHandoverToNo() {
    	Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(handoverTo);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getNo();
	}
    
    public void setHandoverToNo(String handoverToNo) {
		this.handoverToNo = handoverToNo;
	}
    
    @Transient
	private String handoverToName;
    
    public String getHandoverToName() {
    	Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(handoverTo);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getName();
	}
    
    public void setHandoverToName(String handoverToName) {
		this.handoverToName = handoverToName;
	}
    
    /** 客户ID */
    @Column(name = "c_custom_id", nullable = true, length = 32)
    private String customId;
    
    /** 备注 */
    @Column(name = "c_handover_comment", nullable = true, length = 100)
    private String handoverComment;
    
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

	public String getHandoverCode() {
		return handoverCode;
	}

	public void setHandoverCode(String handoverCode) {
		this.handoverCode = handoverCode;
	}

	public String getHandoverStatus() {
		return handoverStatus;
	}

	public void setHandoverStatus(String handoverStatus) {
		this.handoverStatus = handoverStatus;
	}



	public String getHandoverReason() {
		return handoverReason;
	}

	public void setHandoverReason(String handoverReason) {
		this.handoverReason = handoverReason;
	}

	public String getHandoverFrom() {
		return handoverFrom;
	}

	public void setHandoverFrom(String handoverFrom) {
		this.handoverFrom = handoverFrom;
	}

	public String getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(String handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getHandoverComment() {
		return handoverComment;
	}

	public void setHandoverComment(String handoverComment) {
		this.handoverComment = handoverComment;
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

	public String getHandoverDate() {
		return handoverDate;
	}

	public void setHandoverDate(String handoverDate) {
		this.handoverDate = handoverDate;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getHandoverFromOrg() {
		return handoverFromOrg;
	}

	public void setHandoverFromOrg(String handoverFromOrg) {
		this.handoverFromOrg = handoverFromOrg;
	}

	public String getHandoverToOrg() {
		return handoverToOrg;
	}

	public void setHandoverToOrg(String handoverToOrg) {
		this.handoverToOrg = handoverToOrg;
	}

	public String getHandoverFromPos() {
		return handoverFromPos;
	}

	public void setHandoverFromPos(String handoverFromPos) {
		this.handoverFromPos = handoverFromPos;
	}

	public void setPositionFromName(String positionFromName) {
		this.positionFromName = positionFromName;
	}

//	public String getPositionToName() {
//		return positionToName;
//	}

	public void setPositionToName(String positionToName) {
		this.positionToName = positionToName;
	}

	public String getHandoverToPos() {
		return handoverToPos;
	}

	public void setHandoverToPos(String handoverToPos) {
		this.handoverToPos = handoverToPos;
	}
	
}