package com.ibm.kstar.service;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.AppFlowPower;

public interface IAppFlowPowerService {
	
	IPage query(PageCondition condition) throws AnneException;
	
	AppFlowPower get(String id) throws AnneException;
	
	void addOrUpdate(AppFlowPower power) throws AnneException;
	
	void delete(String id) throws AnneException;
	
	List<AppFlowPower> getPowerByFlowCode(String flowCode);
	
}
