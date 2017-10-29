package com.ibm.kstar.action.system.permission.employee;

import com.ibm.kstar.api.system.permission.entity.Employee;

public class EmployeeVo extends Employee{
	
	private static final long serialVersionUID = 1L;
	
	private String positions;

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

}
