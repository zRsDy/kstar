package com.ibm.kstar.api.common.outteam;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.common.outteam.SalesTeamOut;

public interface ISalesOutService {
	// 外部团队
	IPage querySalesOut(PageCondition condition);
	
	void saveSalesOutInfo(SalesTeamOut customSalesTeamOut) throws AnneException;
	
	SalesTeamOut getSalesOutInfo(String id) throws AnneException;

	void updateSalesOutInfo(SalesTeamOut customSalesTeamOut) throws AnneException;

	void deleteSalesOutInfo(String id) throws AnneException;
	
	List<SalesTeamOut> getSalesOutInfoBycode(String code) throws AnneException;

}
