package com.ibm.kstar.action.system.permission.org;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IOrgService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/org")
public class OrgAction extends LovMemberAction{
	
	@Autowired
	IOrgService orgService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@Autowired
	ICustomInfoService customInfoService;
	
	@RequestMapping("/add")
	public String add(String groupId,String parentId,Model model){
		super.add(groupId, parentId, model);
		LovMember parentMember = lovMemberService.get(parentId);
		if(parentMember!=null){
			CustomInfo customInfo = customInfoService.getCustomInfo(parentMember.getOptTxt4());
			model.addAttribute("customInfo",customInfo);
		}
		return forward("org");
	}

	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(LovMember lovMember,HttpServletRequest request){
		LovMember parentMember = lovMemberService.get(lovMember.getParentId());
		if(parentMember != null && "Y".equals(parentMember.getOptTxt1())){
			throw new AnneException("岗位下不能建组织！");
		}
		orgService.save(lovMember);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/employeePage")
	public String employeePage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = orgService.queryEmployeePage(condition);
		return sendSuccessMessage(page);
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		LovMember lovMember = lovMemberService.get(id);
		if("Y".equals(lovMember.getLeafFlag())){
			LovMember position = corePermissionService.getPositionByOrgId(id);
			return redirect("/position/edit.html?id="+position.getId());
		}
		
		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
		model.addAttribute("lovGroup",lovGroup);
		model.addAttribute("lovMember",lovMember);
		if(StringUtil.isNotEmpty(lovMember.getParentId())){
			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
			model.addAttribute("parentLovMember",parentLovMember);
		}
		
		if(lovMember.getOptTxt4() != null){
			CustomInfo customInfo = customInfoService.getCustomInfo(lovMember.getOptTxt4());
			model.addAttribute("customInfo",customInfo);
		}
		return forward("org");
	}
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		model.addAttribute("type","ORG");
		return super.list("ORG", model);
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		orgService.delete(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/settingLeader",method=RequestMethod.POST)
	public String settingLeader(String id){
		orgService.settingLeader(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/orgGroupList")
	public String orgGroupList(Condition condition, HttpServletRequest request) {
		List<LovMember> list = orgService.getOrgGroupList(condition);
		return sendSuccessMessage(list);
	}

	
}
