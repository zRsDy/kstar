package com.ibm.kstar.action.flow.transfer;  

import java.util.ArrayList;
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
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.xflow.api.ISupportService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.model.Authorization;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * 
 * ClassName: 流程授权管理 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年3月27日 下午4:28:20 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */

@Controller
@RequestMapping("/flow/auth")
public class AuthorizationAction extends BaseAction {
	@Autowired
	ISupportService supportService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		String type = request.getParameter("type");
		UserObject userObject = getUserObject();
		
		Participant participant = new Participant(userObject.getEmployee().getId(), 
				userObject.getEmployee().getName(), "EMPLOYEE");
		List<Authorization> authorizations = new ArrayList<Authorization>();
		if("out".equals(type)){
			authorizations= supportService.authorizationOut(participant);
		}else if("in".equals(type)) {
			authorizations =  supportService.authorizationIn(participant);
		}
		IPage p = new PageImpl(authorizations, 1, 1, authorizations.size());
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String authList(Model model) {
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		tabMain.addTab("委托他人", "/flow/auth/authOutList.html?type=out");
		tabMain.addTab("他人委托", "/flow/auth/authInList.html?type=in");
		model.addAttribute("tabMain",tabMain);
		return forward("auth_list");
	}
	
	@RequestMapping("/authOutList")
	public String authOutList(HttpServletRequest request,Model model) {
		String type = request.getParameter("type");
		model.addAttribute("type",type);
		return forward("auth_out_list");
	}
	
	@RequestMapping("/authInList")
	public String authInList(HttpServletRequest request,Model model) {
		String type = request.getParameter("type");
		model.addAttribute("type",type);
		return forward("auth_in_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		Authorization authorization = new Authorization();
		authorization.setType("F");
		model.addAttribute("authorization", authorization);
		return forward("auth_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Authorization authorization) {
		UserObject userObject = getUserObject();
		Participant owner = new Participant(userObject.getEmployee().getId(), 
				userObject.getEmployee().getName(), "EMPLOYEE");
		Participant assignee = new Participant(authorization.getAssigneeId(), 
				authorization.getAssigneeName(), "EMPLOYEE");
		
		if("F".equals(authorization.getType())){
			supportService.authorizationByFlow(authorization.getModule(), owner, assignee);
		}else if("A".equals(authorization.getType())){
			supportService.authorizationByApplication(authorization.getModule(), owner, assignee);
		}
		return sendSuccessMessage();
	}

	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		return forward("");
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(Authorization authorization) {
	
		return sendSuccessMessage();
	}

}
  
