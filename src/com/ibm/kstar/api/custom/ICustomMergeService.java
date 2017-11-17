package com.ibm.kstar.api.custom;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.custom.CustomMerge;

public interface ICustomMergeService {
	//合并信息
	IPage queryMerge(PageCondition condition);
	
	void saveMergeInfo(CustomMerge customMerge) throws AnneException;
	
	CustomMerge getMergeInfo(String id) throws AnneException;

	void updateMergeInfo(CustomMerge customMerge) throws AnneException;

	void deleteMergeInfo(String id) throws AnneException;
	
	

}
