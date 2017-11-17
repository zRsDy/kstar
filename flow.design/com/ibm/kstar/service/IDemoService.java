package com.ibm.kstar.service;

import java.util.List;

import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;

public interface IDemoService {
	
//	void complete(String taskId, String comment, String submitType, String activityId,UserObject user);
	
	void doCallback(String processInstanceId) throws AnneException;
	
	IPage taskList(UserObject user,List<LovMember> positions,int size,int pageno);
	
	String getProcessJSON(String code)throws Exception;
	
	String getProcessJSONByProcessDefinitionId(String code)throws Exception;
	
//	void chooseNext(String taskId,String transitionId,Participant participant,String comment,Map<String,String> varmap);
	
	void todoProcess(ProcessForm form);
	
	IPage pdmTaskList(UserObject user,int size,int pageno);
}
