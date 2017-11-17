package com.ibm.kstar.api.system.permission;

import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.entity.team.vo.TeamVo;

public interface IEmployeeService {

	void save(Employee employee)throws AnneException;
	
	void update(Employee employee)throws AnneException;
	
	IPage query(PageCondition condition) throws AnneException;
	
	void delete(String employeeId) throws AnneException;
	
	Employee get(String employeeId) throws AnneException;
	
	IPage findEmployeeByPositionId(PageCondition condition);
	
	IPage queryTeamByBusinessId(PageCondition condition);
	
	List<TeamVo> findTeamByBusinessId(Condition condition);
	
	List<Employee> getAllEmployee();

	Employee getEmployeeByNo(String employeeNo) throws AnneException;
	
	List<Employee> findEmployeeByOrgId(String orgId);
	
	List<LovMember> findOrgByEmployeeNameOrNo(String searchKey);
}
