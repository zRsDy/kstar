package com.ibm.kstar.api.channel;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.ImportSaleApply;
import com.ibm.kstar.entity.channel.ImportSaleApplyDetail;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import java.util.List;
import java.util.Map;

public interface IImportSaleService {
	
	IPage queryApplys(PageCondition condition);
	
	ImportSaleApply queryApply(String id);
	
	void addOrEditApply(ImportSaleApply importApply, UserObject user);

	void deleteApply(String id);
	
	void submitApply(UserObject user, String id);
	
	IPage queryDetails(PageCondition condition);
	
	ImportSaleApplyDetail queryDetail(String id);

	void addOrEditDetail(ImportSaleApplyDetail detailInfo, UserObject user);
	
	void deleteDetail(String id);

	List<Map<String,Object>> selectOrgOrEmployee(PageCondition condition);

    List<Map<String, Object>> getEmployeeLovForOrg(String id);
}
