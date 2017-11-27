package com.ibm.kstar.action.cost;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.api.cost.ICostService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/cost")
public class CostAction extends BaseFlowAction {

	@Autowired
	ICostService costService; 
	@Autowired
	BaseDao baseDao;
	
	@NoRight
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		String orgIdOrEmployeeId = request.getParameter("orgIdOrEmployeeId");
		String reportType = request.getParameter("reportType");
		String name = request.getParameter("name");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		model.addAttribute("orgIdOrEmployeeId",orgIdOrEmployeeId);
		model.addAttribute("reportType",reportType);
		model.addAttribute("name",name);
		model.addAttribute("year",year);
		model.addAttribute("month",month);
		return forward("cost_list");
	}
	
	@NoRight
	@RequestMapping(value = "/exportCost")
	public void exportRebateLineFormLists(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(",");
		List<List<Object>> dataList = costService.exportCostList(ids);
		ExcelUtil.exportExcel(response, dataList, "费用列表");
	}
	
	@NoRight
	@LogOperate(module="费用查询模块",notes="${user}页面：费用列表")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String orgIdOrEmployeeId = request.getParameter("orgIdOrEmployeeId");
		String reportType = request.getParameter("reportType");
		String name = request.getParameter("name");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		IPage p = null;
		
		if(!StringUtil.isNullOrEmpty(orgIdOrEmployeeId)) {
			 p = costService.queryCostByReportParameter(condition,orgIdOrEmployeeId,reportType,name,year,month);
		}else {
			 p = costService.queryCost(condition);
		}
		return sendSuccessMessage(p);
	}

	@ResponseBody
	@RequestMapping("/importOaExpensesClaim")
	public String importReceipts(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<List<Object>> dataList = this.getExcelData(request);
		costService.importOaExpensesClaimList(dataList,getUserObject());
		return sendSuccessMessage("导入成功");
	}

	@NoRight
	@RequestMapping("/importTemplet")
	public void importReceiptsTemplet(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<List<Object>> dataList  = costService.getOaExpensesClaimTemplet();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ExcelUtil.exportExcel(response, dataList, "OA费用导入模板_"+sdf.format(new Date()));
	}
}