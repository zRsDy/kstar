package com.ibm.kstar.api.custom;

import java.util.List;
import java.util.Map;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomHandoverList;
import com.ibm.kstar.entity.custom.CustomHandoverProc;

public interface ICustomHandoverService {
	//交接信息
	IPage queryHandover(PageCondition condition);
	
	void saveHandoverInfo(CustomHandoverList customHandoverList, UserObject userObject) throws AnneException;
	
	CustomHandoverList getHandoverInfo(String id) throws AnneException;

	void updateHandoverInfo(CustomHandoverList customHandoverList, String processCompleteflg) throws AnneException;
	
	void startHandoverProcess(String id,UserObject userObject,Map<String,String> varmap,CustomHandoverList customHandoverList, String processCompleteflg);

	void startAndSaveHandoverProcess(UserObject userObject,Map<String,String> varmap,CustomHandoverList customHandoverList, String processCompleteflg);

	void deleteHandoverInfo(String id) throws AnneException;
	
	CustomHandoverList getHandoverInfoBycode(String code) throws AnneException;
	
	//未结事务
	IPage queryHandoverProc(PageCondition condition);
	
	void saveHandoverProcInfo(CustomHandoverProc customHandoverProc) throws AnneException;
	
	CustomHandoverProc getHandoverProcInfo(String id) throws AnneException;

	void updateHandoverProcInfo(CustomHandoverProc customHandoverProc) throws AnneException;

	void deleteHandoverProcInfo(String id) throws AnneException;
	
	List<CustomHandoverProc> getHandoverProcInfoBycode(String code) throws AnneException;
	
	
	long queryReceiptList(UserObject uo, String id ,String customId , String customCode, String userId);
	
	IPage queryHandoverBusinessOpportunity(PageCondition condition);
	
	IPage queryHandoverContract(PageCondition condition);
	
	IPage queryHandoverCustomInfo(PageCondition condition);
	
	IPage queryHandoverOrderHeader(PageCondition condition);
	
	IPage queryHandoverQuot(PageCondition condition);
	
	void updateHandoverBusinessOpportunity(UserObject uo,String[] ids, String status) throws AnneException;
	
	void updateHandoverContract(UserObject uo,String[] ids, String status) throws AnneException;
	
	void updateHandoverCustomInfo(UserObject uo,String[] ids, String status) throws AnneException;
	
	void updateHandoverOrderHeader(UserObject uo,String[] ids, String status) throws AnneException;
	
	void updateHandoverQuot(UserObject uo,String[] ids, String status) throws AnneException;

}
