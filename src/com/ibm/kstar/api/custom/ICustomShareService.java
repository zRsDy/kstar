package com.ibm.kstar.api.custom;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomShareInfo;

public interface ICustomShareService {
	//共享信息
	IPage queryShare(PageCondition condition);
	
	void saveShareInfo(CustomShareInfo customShareInfo, UserObject userObject) throws AnneException;
	
	CustomShareInfo getShareInfo(String id) throws AnneException;

	void updateShareInfo(CustomShareInfo customShareInfo, String flg) throws AnneException;

	void deleteShareInfo(String id) throws AnneException;
	
	CustomShareInfo getShareInfoBycode(String code) throws AnneException;
	
}
