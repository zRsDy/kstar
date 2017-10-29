package com.ibm.kstar.api.support.deposit;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.support.deposit.DepositApply;


public interface IDepositApplyService {

	
	IPage query(PageCondition condition);
	
	void save(DepositApply depositApply) throws AnneException;
	
	DepositApply getDepositApply(String id) throws AnneException;

	void update(DepositApply supportDoc) throws AnneException;

	void delete(String id) throws AnneException;
	

}
