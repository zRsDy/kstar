package com.ibm.kstar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

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
public class BaseEntity {
	
	 /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    @PermissionUserId
    protected String createdById;
	
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    protected Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    @PermissionPositionId
    protected String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    @PermissionOrgId
    protected String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    protected String updatedById;
	
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    protected Date updatedAt;

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
	
	public String getCreatorName(){
		Employee e = (Employee)CacheData.getInstance().get(createdById);
		if(e!=null){
			return e.getName();
		}
		return null;
	}
	
	public String getCreatorNo(){
		Employee e = (Employee)CacheData.getInstance().get(createdById);
		if(e!=null){
			return e.getNo();
		}
		return null;
	}
	
	public String getCreatedPositionName() {
		LovMember position = (LovMember)CacheData.getInstance().get(createdPosId);
		if(position==null){
			return createdPosId;
		}
		return position.getName();
	}
	
	public String getCreatedOrgName() {
		LovMember org = (LovMember)CacheData.getInstance().get(createdOrgId);
		if(org==null){
			return null;
		}
		return org.getName();
	}

	public String getUpdatedByName() {
		Employee emp = (Employee)CacheData.getInstance().get(updatedById);
		if(emp==null){
			return updatedById;
		}
		return emp.getName();
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
	
	public LovMember getLovMember(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}
	
	public String getLovCode(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getCode();
	}
	
	public String getLovName(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
	
	public LovMember getLovMember(String groupCode,String code){
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember(groupCode, code);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}
	
	public void setRecordInfor(Boolean isUpdated, UserObject user) {
		if (!isUpdated) {
			// 创建字段
			this.setCreatedById(user.getEmployee().getId());
			this.setCreatedAt(new Date());
			this.setCreatedPosId(user.getPosition().getId());
			this.setCreatedOrgId(user.getOrg().getId());
		}
		// 更新字段
		this.setUpdatedById(user.getEmployee().getId());
		this.setUpdatedAt(new Date());
	}
	
}
  
