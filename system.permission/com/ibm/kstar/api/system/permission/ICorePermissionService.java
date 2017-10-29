package com.ibm.kstar.api.system.permission;

import java.util.List;
import java.util.Map;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;

public interface ICorePermissionService {

//	UserObject getUserObject(HttpServletRequest request);
//	void putUserObject(HttpServletRequest request,UserObject t);
	
	//获取用户的权限
	List<IPermission> getPermissionList(String appId,String userId);
	
	//获取岗位的权限
	List<IPermission> getPermissionListByPosition(String appId,String positionId);
	
	//获取角色的权限
	List<LovMember> getRolePermissionList(String appId,String roleId);
	
	//获取角色下的用户
	List<Employee> getRoleEmployeeList(String roleId);
	
	List<Employee> getPositionEmployeeList(String positionId);
	
//	//获取用户的权限
//	List<LovMember> getUserPermissionList(String appId,String userId);
//	
//	//获取岗位的权限
//	List<LovMember> getUserPermissionListByPosition(String appId, String positionId);
	
	//获取用户的岗位列表
	List<LovMember> getUserPositionList(String userId);
	
	List<LovMember> getUserList(String positionId);
	
	//系统全部权限
	List<LovMember> getAllPermissionList(String appId);
	
	UserObject login(String appId,String user,String password);
	
	List<IMenu> getSystemMenuList(String appId);
	
	LovMember getOrg(String positionId);

	/**
	 * 判断是否是商务专员
	 * @param employeeId
	 * @return
	 */
    boolean isTradeCommisioner(String employeeId);

    /**
	 * 获取商务专员
	 * @param orgId
	 * @param layerId
	 * @param search
	 * @return
	 */
	List<EmployeeObject> findTradeCommisioner(String orgId, String layerId, String search);

	List<EmployeeObject> findEmployeeObject(String orgId, String layerId, String search);

	/**
	 * 获取职员列表
	 * @param orgId
	 * @param layerId
	 * @param search
	 * @return
	 */
	List<EmployeeObject> findEmployeeList(String orgId, String layerId, String search);
	/**
	 * 获取公司列表
	 * @param orgId
	 * @param layerId
	 * @param search
	 * @return
	 */
	List<EmployeeObject> findOrgList(String orgId, String layerId, String search);

	EmployeeObject findEmployeeObjectById(String id);

	LovMember getPositionByOrgId(String id);
	
	LovMember getOrgByPositionId(String positionId);
	
	LovMember getOrgByApprovePositionId(String approvePositionId);
	
	LovMember getApprovePositionByPositionId(String positionId);
	
	LovMember getPositionByApprovePosition(String approvePositionId);
	
	LovMember getPositionById(String positionId);
	
	List<LovMember> getPositionListByRole(String roleId);
	
	List<LovMember> getRoleListByPosition(String positionId);

	List<Map<String, Object>> downLoadDataBySQL(String exportSql);

	List<EmployeeObject> findEmployeeByPosObject(String posId, String layerId, String search);
	
}
