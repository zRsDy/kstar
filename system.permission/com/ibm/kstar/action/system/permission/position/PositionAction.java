package com.ibm.kstar.action.system.permission.position;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IPositionService;
import com.ibm.kstar.api.system.permission.entity.PositionVo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/position")
public class PositionAction extends LovMemberAction{
	
	@Autowired
	protected IPositionService positionService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	
//	${ctx}/position/page.html?groupId=POSITION
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = positionService.query(condition);
		return sendSuccessMessage(page);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/approvePage")
	public String approvePage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = positionService.queryApprovePage(condition);
		return sendSuccessMessage(page);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/employeePage")
	public String employeePage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = positionService.queryEmployeePage(condition);
		return sendSuccessMessage(page);
	}
	
	
	@RequestMapping("/add")
	public String add(String groupId,String parentId,Model model){
		super.add(groupId, parentId, model);
		LovMember positionLovMember = corePermissionService.getOrgByPositionId(parentId);
		if(positionLovMember != null){
			LovMember parentOrgLovmember = lovMemberService.get(positionLovMember.getParentId());
			model.addAttribute("parentOrgLovmember",parentOrgLovmember);
		}
		return forward("position");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(LovMember lovMember,HttpServletRequest request){
		PositionVo positionVo = new PositionVo();
		BeanUtils.copyPropertiesIgnoreNull(lovMember, positionVo);
		positionVo.setParentOrgId(request.getParameter("parentOrgId"));
		positionService.save(positionVo);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		LovMember lovMember = lovMemberService.get(id);
		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
		model.addAttribute("lovGroup",lovGroup);
		model.addAttribute("lovMember",lovMember);
		if(StringUtil.isNotEmpty(lovMember.getParentId())){
			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
			model.addAttribute("parentLovMember",parentLovMember);
		}
		LovMember positionLovMember = corePermissionService.getOrgByPositionId(id);
		LovMember parentOrgLovmember = lovMemberService.get(positionLovMember.getParentId());
		model.addAttribute("parentOrgLovmember",parentOrgLovmember);
		model.addAttribute("roles",JSON.toJSONString(corePermissionService.getRoleListByPosition(lovMember.getOptTxt1())));
		return forward("position");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(LovMember lovMember,HttpServletRequest request){
		PositionVo positionVo = new PositionVo();
		BeanUtils.copyPropertiesIgnoreNull(lovMember, positionVo);
		positionVo.setParentOrgId(request.getParameter("parentOrgId"));
		String roles = request.getParameter("roles");
		positionService.update(positionVo,roles);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/config2")
	public String config2(String id,Model model) {
		model.addAttribute("selectedEmployeeList",JSON.toJSONString(corePermissionService.getPositionEmployeeList(id)));
		model.addAttribute("positionId",id);
		return forward("config2");
	}
	
	@ResponseBody
	@RequestMapping(value="/config2" ,method=RequestMethod.POST)
	public String config2(String positionId,String employees,Model model) {
		positionService.configEmployee(positionId,employees.split(","));
		return sendSuccessMessage();
	}
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return super.list("POSITION", model);
	}
	
	@RequestMapping("/approveList")
	public String approveList(String id,Model model){
		super.list("APPROVE_POSITION_LEVEL", model);
		
		return forward("approveList");
	}
	
	@ResponseBody
	@RequestMapping(value="/updateApprove",method=RequestMethod.POST)
	public String updateApprove(com.ibm.kstar.action.system.permission.position.PositionVo lov){
		positionService.updateApprove(lov);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		positionService.delete(id);
		return sendSuccessMessage();
	}
}
