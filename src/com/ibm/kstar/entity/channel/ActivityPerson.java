package com.ibm.kstar.entity.channel;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 参加活动人员表(crm_t_activity_person)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-06
 */
@Entity
@Table(name = "crm_t_activity_person")
public class ActivityPerson extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 5806319533740891078L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "activity_person_c_pid_generator")
   	@GenericGenerator(name="activity_person_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_id")
    private String applyId;
    
    /** 是否内部人员 */
    @Column(name = "c_internal_person")
    private String internalPerson;
    
    /** 经销商名称 */
    @Column(name = "c_seller_name")
    private String sellerName;
    
    @Column(name = "c_person_Name")
    private String personName;
    
    /** 部门 */
    @Column(name = "c_department")
    private String department;
    
	/** 职务 */
    @Column(name = "c_position")
    private String position;
    
    /** 联系电话 */
    @Column(name = "c_phone")
    private String phone;
    
    /** 角色 */
    @Column(name = "c_role")
    private String role;
    
    /** email */
    @Column(name = "c_email")
    private String email;
    
    /** 权限*/
    @Column(name = "c_power")
    private String power;
    
    /** 生效日期 */
    @Column(name = "c_start_date")
    private Date startDate;
    
    /** 失效日期 */
    @Column(name = "c_end_date")
    private Date endDate;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getInternalPerson() {
		return internalPerson;
	}

	public void setInternalPerson(String internalPerson) {
		this.internalPerson = internalPerson;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartmentName() {
		return this.getLovName(department);
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleName() {
		return this.getLovName(role);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getPowerName() {
		return this.getLovName(power);
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}