package com.ibm.kstar.action.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common")
public class Select4FlowAction extends BaseAction{

	@Autowired
	ILovGroupService lovGroupService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@NoRight
	@RequestMapping("/select")
	public String select(String pickerId,String groupId,Model model){
		model.addAttribute("pickerId",pickerId);
		TabMain tabMain = new TabMain();
		tabMain.addTab("按员工", "/common/selectUser.html?pickerId="+pickerId);
		tabMain.addTab("按岗位", "/common/selectPosition.html?pickerId="+pickerId+"&groupId=POSITION");
		model.addAttribute("tabMain",tabMain);
		return forward("select");
	}
	
	@NoRight
	@RequestMapping("/selectUser")
	public String selectLovList(String pickerId,String groupId,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("selectUser");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/userPage")
	public String userPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		condition.getFilterObject().addCondition("startDate", "<", sdf.format(new Date()));
		condition.getFilterObject().addCondition("endDate", ">", sdf.format(new Date()));
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
		}
		IPage p = userService.queryWithPosition(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@RequestMapping("/selectPosition")
	public String selectPosition(String pickerId,String groupId,Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("groupId",groupId);
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup",lovGroup);
		return forward("lovList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/positionPage")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String searchKey = condition.getStringCondition("searchKey");
		String parentId = condition.getStringCondition("parentId");

		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		condition.getFilterObject().addCondition("groupId", "=", groupId);
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
	
}
