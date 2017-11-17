package com.ibm.kstar.action.system.lov.group;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/lov/group")
public class LovGroupAction extends BaseAction{
	
	@Autowired
	ILovGroupService lovGroupService;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
		}
		IPage p = lovGroupService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request){
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		lovGroupService.delete(id);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/add")
	public String add(String partition,Model model){
		return forward("lovGroup");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(@Valid LovGroup lovGroup,BindingResult bindingResult){
		lovGroupService.save(lovGroup);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		LovGroup lovGroup = lovGroupService.get(id);
		model.addAttribute("lovGroup",lovGroup);
		return forward("lovGroup");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(LovGroup lovGroup){
		lovGroupService.update(lovGroup);
		return sendSuccessMessage();
	}
}
