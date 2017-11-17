package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.ActivityApply;
import com.ibm.kstar.entity.channel.ActivityContent;
import com.ibm.kstar.entity.channel.ActivityExpense;
import com.ibm.kstar.entity.channel.ActivityPerson;
import com.ibm.kstar.entity.channel.ActivitySummary;

public interface IActivityApplyService {
	
	IPage queryApplys(PageCondition condition, UserObject user);
	
	ActivityApply queryApply(String id);
	
	void addOrEditApply(ActivityApply activityApply, UserObject user);
	
	void checkSummary(String applyId);
	
	ActivitySummary querySummary(String applyId);
	
	void updateColumnValue(String contentId,String column,String value, UserObject user);
	
	void addOrEditSummary(ActivitySummary activitySummary, UserObject user);
	
	void deleteApply(String id);
	
	void checkSubmit(String id);
	
	void submitActivity(String id, UserObject user);
	
	void submitTrain(String id, UserObject user);
	
	void checkCloseApply(String id);
	
	void closeApply(String id, UserObject user);
	
	IPage queryContents(PageCondition condition);
	
	ActivityContent queryContent(String id);
	
	void addOrEditContent(ActivityContent activityContent, UserObject user);
	
	void deleteContent(String id);
	
	void makeContent(String[] ids,String makerId, UserObject user);
	
	IPage queryPersons(PageCondition condition);
	
	ActivityPerson queryPerson(String id);
	
	void addOrEditPerson(ActivityPerson activityPerson, UserObject user);
	
	void deletePerson(String id);
	
	IPage queryExpenses(PageCondition condition);
	
	ActivityExpense queryExpense(String id);
	
	void addOrEditExpense(ActivityExpense activityExpense, UserObject user);
	
	void deleteExpense(String id);
	
	IPage querySelectPersons(PageCondition condition);
}
