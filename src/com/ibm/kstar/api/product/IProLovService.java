package com.ibm.kstar.api.product;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;

public interface IProLovService {

	void saveCatelog(LovMember lovMember) throws AnneException;
	
	void updateBaobei(String lovId,String baobei) throws AnneException;
	
	List<LovMember> getFatherList(String lovCode) throws AnneException;
	
	IPage mappage(PageCondition condition, UserObject user) throws AnneException;
	
	IPage demandSelectProduct(PageCondition condition, UserObject user) throws AnneException;
	
	List<List<Object>> mappageExport(PageCondition condition, UserObject user) throws AnneException;
}
