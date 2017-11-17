package com.ibm.kstar.api.custom;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.custom.CustomZXBInfo;

public interface ICustomerZXBInfoService {
	
	IPage query(PageCondition condition);
	
	CustomZXBInfo createCustomInfor();
	
	void saveCustomInfo(CustomZXBInfo customInfo) throws AnneException;
	
	void updateCustomInfo(CustomZXBInfo customInfo) throws AnneException;
	
	void deleteCustomInfo(String id) throws AnneException;
	
	CustomZXBInfo getCustomInfoById(String id) throws AnneException;
	
	List<CustomZXBInfo> getCustomInfoByCode(String code) throws AnneException;
	
	String copyCustomInfo(CustomZXBInfo customInfo) throws AnneException;
	
	String doBuyerCodeApply(CustomZXBInfo customInfo) throws AnneException;
}
