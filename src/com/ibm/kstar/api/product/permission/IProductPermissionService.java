package com.ibm.kstar.api.product.permission;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.permission.PermissionOptRel;

public interface IProductPermissionService {

	/**
	 * 
	 * @param id 目录ID
	 * @return
	 */
	List<LovMember> getPermissionList(String id);
	
	List<LovMember> getOptPermissionList(String id);
	
	List<LovMember> getAllPermissionList();

	void configPermission(String[] roleIds, String[] permissions);
	
	void configOptPermission(String[] roleIds, String[] permissions);

	IPage getPermissionPage(PageCondition condition);

	IPage getOptPermissionPage(PageCondition condition);
	
	List<LovMember> getJobSelectPermissionList(String id); 
	
	List<LovMember> getReportSelectPermissionList(String id); 

	void updateProductPermission(String rightzTreeId,String leftzTreeId)throws AnneException;

	void updateReportPermission(String rightzTreeId,String leftzTreeId)throws AnneException;
}
