package com.ibm.kstar.api.system.permission;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public interface IRoleService {

	//给角色配置权限
	void configPermission(String roleId, String[] split);
	
	void configEmployee(String roleId,String[] employees);
	
	void configPosition(String roleId,String[] positions);

	void save(LovMember lovMember);

	IPage queryEmployeePage(PageCondition condition);

	void delete(String id); 
	
}
