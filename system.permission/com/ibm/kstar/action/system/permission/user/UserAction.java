package com.ibm.kstar.action.system.permission.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IUserPermissionService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.Role;
import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction{
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	IUserPermissionService userPermissionService;
	
	@RequestMapping("/add")
	public String add(String partition,Model model){
		return forward("user");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(UserVo userVo){
		userService.save(userVo);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		Employee employee = employeeService.get(id);
		model.addAttribute("employee",employee);
		
		User user = userService.get(id);
		model.addAttribute("user",user);
		
		List<Role> positionList = userPermissionService.getRoleList(id);
		model.addAttribute("roles",JSON.toJSONString(positionList));
		model.addAttribute("roleList",positionList);
		return forward("user");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(UserVo UserVo){
		userService.update(UserVo);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/list")
	public String list(Model model){
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		userService.delete(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		condition.getFilterObject().addCondition("startDate", "<", sdf.format(new Date()));
//		condition.getFilterObject().addCondition("endDate", ">", sdf.format(new Date()));
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
		}
		IPage p = userService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping(value="/resetPassword",method = RequestMethod.POST)
	public String resetPassword(String id){
		userService.resetPasswordById(id);
		return sendSuccessMessage();
	}
	
}
