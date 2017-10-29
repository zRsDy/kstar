package com.ibm.kstar.action.support.docView;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.support.docmanagement.IDocManagementService;
import com.ibm.kstar.entity.support.docmanagement.SupportTemplate;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/docView")
public class DocViewAction extends BaseAction {
	@Autowired
	IDocManagementService service;
	
	@LogOperate(module = "支持管理模块：文档查看", notes = "${user}跳转文档一览页面")
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module = "支持管理模块：文档查看", notes = "${user}实现文档一览查询")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		IPage p = service.querySupportTemplate(condition, getUserObject().getPosition().getId(), getUserObject().getOrg().getId());
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module = "支持管理模块：文档查看", notes = "${user}跳转文档编辑页面")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		SupportTemplate supportTemplate = service.getSupportTemplateInfo(id);
		if(supportTemplate==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		TabMain tabMain = new TabMain();
		if(hasPermission("P08DocViewTabsPage")) {
			tabMain.addTab("文档列表","/common/attachment/attachment.html?businessType=DOC_MANAGE_FILE&docGroupCode=ATTACHMENTTYPEGROUP&businessId="+supportTemplate.getId());	
		}
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("supportTemplate",supportTemplate);
		return forward("add");
	}
	
}
