package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/** 
 * ClassName:OrderBaseEntity <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年3月14日 下午4:10:20 <br/> 
 * @author   liming 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
@MappedSuperclass 
public class ComEntity {
	
	 /** 创建人 */
    @Column(name = "created_by_id")
    @PermissionUserId
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "created_at")
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "created_pos_id")
    @PermissionPositionId
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "created_org_id")
    @PermissionOrgId
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "updated_at")
    private Date updatedAt;

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

	public String getCreatedByIdName() {
		Employee employee = (Employee) CacheData.getInstance().get(createdById);
		if(employee != null ){
			return employee.getName() + " | " + employee.getNo();
		}
		return  "";
	}

	public String getCreatedByName() {
		Employee employee = (Employee) CacheData.getInstance().get(createdById);
		if(employee != null ){
			return employee.getName() + " | " + employee.getNo();
		}
		return  "";
	}

	public String getUpdatedByName() {
		Employee employee = (Employee) CacheData.getInstance().get(updatedById);
		if(employee != null ){
			return employee.getName() + " | " + employee.getNo();
		}
		return  "";
	}

	public String getCreatedOrgIdName() {
		LovMember l = (LovMember) CacheData.getInstance().get(this.createdOrgId);
		if (l != null) {
			return l.getName();
		}
		return "";
	}

	public String getCreatedPosIdName() {
		LovMember l = (LovMember) CacheData.getInstance().get(this.createdPosId);
		if (l != null) {
			return l.getName() + " | " + l.getNo();
		}
		return "";
	}

	public void fillInit(UserObject userObject){
		if (this.createdAt == null) {
			this.setCreatedAt(new Date());
		}
		if (this.createdById == null) {
			this.setCreatedById(userObject.getEmployee().getId());
		}
		if ( this.getCreatedOrgId() == null ) {
			this.setCreatedOrgId(userObject.getOrg().getId());
		}
		if (this.getCreatedPosId() == null) {
			this.setCreatedPosId(userObject.getPosition().getId());
		}
		this.setUpdatedById(userObject.getEmployee().getId());
		this.setUpdatedAt(new Date());
	}

}
  
