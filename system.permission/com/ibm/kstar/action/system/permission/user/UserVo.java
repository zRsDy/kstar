package com.ibm.kstar.action.system.permission.user;

import com.ibm.kstar.api.system.permission.entity.User;

public class UserVo extends User{
		
	private static final long serialVersionUID = 1L;

	private String roles;
	
	private String employeeId;

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
}
