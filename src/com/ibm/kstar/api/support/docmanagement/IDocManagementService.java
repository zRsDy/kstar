package com.ibm.kstar.api.support.docmanagement;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.support.docmanagement.SupportTemplate;


public interface IDocManagementService {

	IPage querySupportTemplate(PageCondition condition);
	
	IPage querySupportTemplate(PageCondition condition,String positionId, String orgId);
	
	void saveSupportTemplateInfo(SupportTemplate supportTemplate) throws AnneException;
	
	SupportTemplate getSupportTemplateInfo(String id) throws AnneException;

	void updateSupportTemplateInfo(SupportTemplate supportTemplate) throws AnneException;

	void deleteSupportTemplateInfo(String id) throws AnneException;
	

}
