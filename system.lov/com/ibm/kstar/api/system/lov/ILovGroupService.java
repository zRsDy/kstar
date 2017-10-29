package com.ibm.kstar.api.system.lov;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovGroup;

public interface ILovGroupService {

	void save(LovGroup lovGroup)throws AnneException;
	
	void update(LovGroup lovGroup)throws AnneException;
	
	IPage query(PageCondition condition) throws AnneException;
	
	void delete(String lovGroupId) throws AnneException;
	
	LovGroup get(String lovGroupId) throws AnneException;
	
	LovGroup getByCode(String lovGroupCode) throws AnneException;
	
}
