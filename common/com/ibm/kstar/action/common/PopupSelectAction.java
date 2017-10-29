package com.ibm.kstar.action.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common")
public class PopupSelectAction extends BaseAction{
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	ILovGroupService lovGroupService;
	
	@NoRight
	@RequestMapping("/selectLovList")
	public String selectLovList(String leafFlag,String pickerId,String groupId,Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("groupId",groupId);
		model.addAttribute("leafFlag",leafFlag);
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup",lovGroup);
		return forward("lovList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/lovPage")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String searchKey = condition.getStringCondition("searchKey");
		String parentId = condition.getStringCondition("parentId");
		String leafFlag = condition.getStringCondition("leafFlag");

		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		condition.getFilterObject().addCondition("groupId", "=", groupId);
		if(StringUtil.isNotEmpty(leafFlag)){
			condition.getFilterObject().addCondition("leafFlag", "=", leafFlag);
		}
		if(StringUtil.isNotEmpty(parentId)){
			condition.getFilterObject().addCondition("path", "like", "%"+parentId+"%");
		}
		
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
		}
		IPage p = lovMemberService.query(condition);
		return sendSuccessMessage(p);
	}
	
	/*************************************************************************************/
	@NoRight
	@RequestMapping("/selectLovTree")
	public String selectLovTree(String pickerId,String groupId,String leafFlag,String rootId,String opType,Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("groupId",groupId);
		model.addAttribute("leafFlag",leafFlag);
		model.addAttribute("rootId",rootId);
		model.addAttribute("opType",opType);
		return forward("lovTree");
	}
	
}
