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
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;

@Entity
@Table(name = "crm_t_custom_rela_contact")
@Permission(businessType = "CUSTOM_RELA_CONTACT")
public class CustomRelaContact implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_rela_contact_pid_generator")
	@GenericGenerator(name="custom_rela_contact_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_pid", nullable = false, length = 32)
    private String customId;
    
    /** 名称 */
    @Column(name = "c_contact_name", nullable = true, length = 32)
    private String contactName;
    
    /** 性别 */
    @Column(name = "c_contact_sex", nullable = true, length = 32)
    private String contactSex;
    
	@Transient
	private String contactSexGrid;

	public Object getContactSexGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(contactSex);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public void setContactSexGrid(String contactSexGrid) {
		this.contactSexGrid = contactSexGrid;
	}
	
	/** 年龄*/
    @Column(name = "c_contact_age", nullable = true, length = 3)
    private String age;
    /** 决策权重 */
    @Column(name = "c_contact_weight", nullable = true, length = 200)
    private String weight;
    /** 利益诉求 */
    @Column(name = "c_contact_appeal", nullable = true, length = 200)
    private String appeal;
    /** 兴趣爱好 */
    @Column(name = "c_contact_interest", nullable = true, length = 200)
    private String interest;
    
    /** 部门 */
    @Column(name = "c_contact_dept", nullable = true, length = 100)
    private String contactDept;
    
    /** 职务 */
    @Column(name = "c_contact_business", nullable = true, length = 100)
    private String contactBusiness;
    
    /** 角色 */
    @Column(name = "c_contact_role", nullable = true, length = 100)
    private String contactRole;
    
	@Transient
	private Object contactRoleGrid;

	public Object getContactRoleGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(contactRole);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public void setContactRoleGrid(Object contactRoleGrid) {
		this.contactRoleGrid = contactRoleGrid;
	}
    
    /** 主管领域 */
    @Column(name = "c_contact_competent", nullable = true, length = 100)
    private String contactCompetent;
    
    /** 电话 */
    @Column(name = "c_contact_tel", nullable = true, length = 100)
    private String contactTel;
    
    /** 传真 */
    @Column(name = "c_contact_fax", nullable = true, length = 100)
    private String contactFax;
    
    /** E-mail */
    @Column(name = "c_contact_mail", nullable = true, length = 100)
    private String contactMail;
    
    /** 有效 */
    @Column(name = "c_contact_valid", nullable = true, length = 32)
    private String contactValid;
    
    @Transient
   	private String contactValidGrid;
       
       public Object getContactValidGrid() {
   		
   		LovMember lov = new LovMember();
   		Object obj = CacheData.getInstance().get(contactValid);
   		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
   		return  lov;
   	}
       
    public void setContactValidGrid(String contactValidGrid) {
   		this.contactValidGrid = contactValidGrid;
   	}
    
    /** 创建人 */
    @Column(name = "c_created_by_id")
    @PermissionUserId
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at")
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id")
    @PermissionPositionId
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id")
    @PermissionOrgId
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id")
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at")
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactSex() {
		return contactSex;
	}

	public void setContactSex(String contactSex) {
		this.contactSex = contactSex;
	}

	public String getContactDept() {
		return contactDept;
	}

	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}

	public String getContactBusiness() {
		return contactBusiness;
	}

	public void setContactBusiness(String contactBusiness) {
		this.contactBusiness = contactBusiness;
	}

	public String getContactRole() {
		return contactRole;
	}

	public void setContactRole(String contactRole) {
		this.contactRole = contactRole;
	}

	public String getContactCompetent() {
		return contactCompetent;
	}

	public void setContactCompetent(String contactCompetent) {
		this.contactCompetent = contactCompetent;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactMail() {
		return contactMail;
	}

	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

	public String getContactValid() {
		return contactValid;
	}

	public void setContactValid(String contactValid) {
		this.contactValid = contactValid;
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAppeal() {
		return appeal;
	}

	public void setAppeal(String appeal) {
		this.appeal = appeal;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

}