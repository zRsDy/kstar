package com.ibm.kstar.api.custom;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomEventContact;
import com.ibm.kstar.entity.custom.CustomEventInfo;
import com.ibm.kstar.entity.custom.CustomEventItems;


public interface ICustomEventService {
	//活动信息
	IPage queryEvent(PageCondition condition);
	
	void saveEventInfo(CustomEventInfo customEventInfo,UserObject userObject) throws AnneException;
	
	CustomEventInfo getEventInfo(String id) throws AnneException;

	void updateEventInfo(CustomEventInfo customEventInfo) throws AnneException;

	void deleteEventInfo(String id) throws AnneException;
	
	CustomEventInfo getEventInfoBycode(String code) throws AnneException;
	
	//活动信息 人员名单
	IPage queryCustomEventContact(PageCondition condition);
	
	void saveCustomEventContact(CustomEventContact customEventContact,UserObject userObject) throws AnneException;
	
	CustomEventContact getCustomEventContact(String id) throws AnneException;

	void updateCustomEventContact(CustomEventContact customEventContact) throws AnneException;

	void deleteCustomEventContact(String id) throws AnneException;
	
	//活动信息 协助事项
	IPage queryCustomEventItems(PageCondition condition);
	
	void saveCustomEventItems(CustomEventItems customEventItems,UserObject userObject) throws AnneException;
	
	CustomEventItems getCustomEventItems(String id) throws AnneException;

	void updateCustomEventItems(CustomEventItems customEventItems) throws AnneException;

	void deleteCustomEventItems(String id) throws AnneException;
}
