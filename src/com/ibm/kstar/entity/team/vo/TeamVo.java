package com.ibm.kstar.entity.team.vo;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.team.Team;

public class TeamVo {

	String positionName;
	
	String employeeName;
	
	String orgName;
	
	String positionId;
	
	String id;
	
	String employeeId;
	
	public TeamVo(){}
	
	public TeamVo(Team team,Employee employee,LovMember position,LovMember org){
		orgName = org.getName();
		employeeName = employee.getName();
		positionName = position.getName();
		positionId = position.getId();
		employeeId = employee.getId();
		id = position.getId();
	}

	public String getPositionName() {
		return positionName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getPositionId() {
		return positionId;
	}

	public String getId() {
		return id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
}
