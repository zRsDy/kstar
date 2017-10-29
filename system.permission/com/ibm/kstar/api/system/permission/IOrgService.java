package com.ibm.kstar.api.system.permission;

import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public interface IOrgService {

	IPage getOrgPage(PageCondition condition);
	
	void delete(String id);
	
	void settingLeader(String id);
	
	void update(LovMember org);
	
	void save(LovMember org);

	IPage queryEmployeePage(PageCondition condition);

	List<LovMember> getOrgGroupList(Condition condition); 
	
}
