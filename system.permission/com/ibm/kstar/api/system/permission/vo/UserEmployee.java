package com.ibm.kstar.api.system.permission.vo;

import java.io.Serializable;
import java.util.List;

import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.User;

public class UserEmployee implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private User user;
	private Employee employee;
	
	public UserEmployee(List<Object> list){
		this.user = (User)list.get(0);
		this.employee = (Employee)list.get(1);
		this.id = employee.getId();
		this.name = employee.getName();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
