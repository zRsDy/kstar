package com.ibm.kstar.api.system.permission.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "SYS_T_PERMISSION_EMPLOYEE")
public class Employee implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sys_t_permission_employee_id_generator")
	@GenericGenerator(name="sys_t_permission_employee_id_generator", strategy="uuid")
	@Column(name="ROW_ID")
	private String id;
	
	@Column(name="NO")
	private String no;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="FLAG")
	private String flag;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="CONTACT_ID")
	private String contactId;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="USER_FLAG")
	private String userFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}
	
	public String getOutDate(){
		Date date = new Date();
		if(endDate.after(date)){
			return "否";
		}
		return "是";
	}
	
}
