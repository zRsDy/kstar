package com.ibm.kstar.api.system.permission;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;

public class EmployeeObject {
	
	String orgName;
	
	String positionName;
	
	String positionId;
	
	String id;
	
	String employeeName;
	
	String employeeNo;
	
	String employeeId;
	
	String orgId;
	
	public EmployeeObject(LovMember o , LovMember p , Employee e){
		
		id = p.getId();
		
		positionName = p.getName();
		
		orgName = o.getNamePath();
		
		employeeName = e.getName();
		
		employeeNo = e.getNo();
		
		employeeId = e.getId();
		
		orgId = o.getId();
		
		positionId = p.getId();
		
	}

	public String getOrgName() {
		return orgName;
	}

	public String getPositionName() {
		return positionName;
	}

	public String getId() {
		return id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public String getEmployeeId() {
		return employeeId;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public String getPositionId() {
		return positionId;
	}
	
}
