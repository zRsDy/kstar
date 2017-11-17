package com.ibm.kstar.api.system.permission;

import java.util.List;

import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.api.system.permission.entity.Role;
import com.ibm.kstar.api.system.permission.entity.UserPermission;

public interface IUserPermissionService {

	List<Position> getPositionList(String userId); 
	
	List<Role> getRoleList(String userId);
	
	List<UserPermission> getUserPermissionList(String userId);
	
}
