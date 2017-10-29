package com.ibm.kstar.service;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.AppFlowParam;

public interface IAppFlowParamService {
	
	IPage query(PageCondition condition) throws AnneException;
	
	AppFlowParam get(String id) throws AnneException;
	
	void addOrUpdate(AppFlowParam param) throws AnneException;
	
	void delete(String id) throws AnneException;
	
	List<LovMember> getList(String optTxt1) throws AnneException;
}
