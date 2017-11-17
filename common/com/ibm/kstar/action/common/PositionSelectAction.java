package com.ibm.kstar.action.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.permission.EmployeeObject;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common")
public class PositionSelectAction extends BaseAction{

	@Autowired
	ICorePermissionService corePermissionService;
	
	@Autowired
	IEmployeeService employeeService;
	@NoRight
	@ResponseBody
	@RequestMapping("/selectPositionList")
	public String selectProductList(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String orgId = condition.getStringCondition("orgId");
		String layerId = condition.getStringCondition("layerId");
		List<EmployeeObject> list = corePermissionService.findEmployeeObject(orgId,layerId,search);
		return sendSuccessMessage(list);
	}
	
	/**
	 * 通过岗位层级过滤选择人
	 * @param condition
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/selectByPositionList")
	public String selectByPositionList(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String posId = condition.getStringCondition("posId");
		String layerId = condition.getStringCondition("layerId");
		List<EmployeeObject> list = corePermissionService.findEmployeeByPosObject(posId,layerId,search);
		return sendSuccessMessage(list);
	}

	/**
	 * 选择商务专员
	 * @param condition
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/selectTradeCommisioner")
	public String selectTradeCommisioner(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String orgId = condition.getStringCondition("orgId");
		String layerId = condition.getStringCondition("layerId");
		List<EmployeeObject> list = corePermissionService.findTradeCommisioner(orgId,layerId,search);
		return sendSuccessMessage(list);
	}
	
	/**
	 * 选择员工
	 * @param condition
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/selectEmployeeList")
	public String selectEmployeeList(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String orgId = condition.getStringCondition("orgId");
		String layerId = condition.getStringCondition("layerId");
		List<EmployeeObject> list = corePermissionService.findEmployeeList(orgId,layerId,search);
		return sendSuccessMessage(list);
	}
	
	/**
	 * 选择公司
	 * @param condition
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/selectOrgList")
	public String selectOrgList(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String orgId = condition.getStringCondition("orgId");
		String layerId = condition.getStringCondition("layerId");
		if(StringUtil.isEmpty(orgId)) {
			orgId = getUserObject().getOrg().getId();
		}
		List<EmployeeObject> list = corePermissionService.findOrgList(orgId,layerId,search);
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/queryEmployee")
	public String queryEmployee(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = employeeService.findEmployeeByPositionId(condition);
		return sendSuccessMessage(page);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/queryPosition")
	public String queryPosition(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = employeeService.queryTeamByBusinessId(condition);
		return sendSuccessMessage(page);
	}
	
}
