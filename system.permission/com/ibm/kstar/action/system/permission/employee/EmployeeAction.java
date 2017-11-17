package com.ibm.kstar.action.system.permission.employee;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IUserPermissionService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/employee")
public class EmployeeAction extends BaseAction{
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	IUserPermissionService userPermissionService;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		IPage p = query(condition, request);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request){
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		employeeService.delete(id);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/add")
	public String add(String partition,Model model){
		return forward("employee");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(EmployeeVo employeeVo){
		employeeService.save(employeeVo);
		return sendSuccessMessage();
	}
	
	@Autowired
	ICustomInfoService customInfoService;
	
	
	@ResponseBody
	@RequestMapping(value="/getCustomer",method=RequestMethod.POST)
	public String getCustomerName(String id){
		CustomInfo info = customInfoService.getCustomInfo(id);
		return sendSuccessMessage(info);
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		Employee employee = employeeService.get(id);
		model.addAttribute("employee",employee);
		List<Position> positionList = userPermissionService.getPositionList(id);
		model.addAttribute("epositions",JSON.toJSONString(positionList));
		model.addAttribute("epositionList",positionList);
		return forward("employee");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(EmployeeVo employeeVo){
		employeeService.update(employeeVo);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/findList",method=RequestMethod.POST)
	public String findList(PageCondition condition,HttpServletRequest request){
		IPage p = query(condition, request);
		return sendSuccessMessage(p.getList());
	}

	private IPage query(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		condition.getFilterObject().addCondition("startDate", "<", sdf.format(new Date()));
//		condition.getFilterObject().addCondition("endDate", ">", sdf.format(new Date()));
		String searchKey = condition.getStringCondition("search");
		String userFlag = condition.getStringCondition("userFlag");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("no", "like", "%"+searchKey+"%");
		}
		if(userFlag !=null){
			condition.getFilterObject().addCondition("userFlag", "=", userFlag);
		}
		IPage p = employeeService.query(condition);
		return p;
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/selectOrg",method=RequestMethod.POST)
	public String findOrgByEmployeeNameOrNo(PageCondition condition, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		List<LovMember> positions = employeeService.findOrgByEmployeeNameOrNo(searchKey);
		return sendSuccessMessage(positions);
	}
	
}
