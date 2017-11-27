package com.ibm.kstar.api.cost;


import java.util.List;


import com.ibm.kstar.api.system.permission.UserObject;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;



public interface ICostService {
	
	
	/**
	 * 查询费用列表
	 * @param condition
	 * @return
	 */
	IPage queryCost(PageCondition condition);
	/**
	 * 根据报表参数查询费用列表
	 * @param condition
	 * @param orgIdOrEmployeeId
	 * @param reportType
	 * @param name
	 * @param year
	 * @param month
	 * @return
	 */
	IPage queryCostByReportParameter(PageCondition condition,String orgIdOrEmployeeId,String reportType,String name,String year,String month);


	List<List<Object>> getOaExpensesClaimTemplet();

	void importOaExpensesClaimList(List<List<Object>> dataList, UserObject userObject);

	/**
	 * 导出费用查询列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	List<List<Object>> exportCostList(String[] ids) throws Exception;
}
