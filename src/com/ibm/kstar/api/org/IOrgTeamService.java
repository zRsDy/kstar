package com.ibm.kstar.api.org;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public interface IOrgTeamService {

	List<LovMember> getOrgList(String businessId, String businessType);

	List<LovMember> getAllOrgList();

	void config(String businessId, String businessType, String[] permissions, String primaryOrgId);

	IPage page(PageCondition condition);
	
	LovMember getPrimaryOrg(String businessId, String businessType);
	
	void configPrimaryOrg(String businessId, String businessType, String permission);

	void copyPrimaryOrg(String sourceBusinessId, String sourceBusinessType, String targetBusinessId, String targetBusinessType,String creater);

	void deleteOrg(String businessId, String businessType);

}
