package com.ibm.kstar.api.common.permission;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public interface ICommonPermissionService {

	List<LovMember> getPermissionList(String id, String groupId);

	List<LovMember> getAllPermissionList();

	void configPermission(String roleId, String[] permissions);

	IPage getPermissionPage(PageCondition condition);

}
