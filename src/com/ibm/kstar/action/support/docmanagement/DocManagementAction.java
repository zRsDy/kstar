package com.ibm.kstar.action.support.docmanagement;

import java.util.Date;

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
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.support.docmanagement.IDocManagementService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.support.docmanagement.SupportTemplate;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/docManagement")
public class DocManagementAction extends BaseAction {
	@Autowired
	IDocManagementService service;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}跳转文档管理页面")
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}跳转文档管理一览查询")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addCondition("docType", "like", "%"+searchKey+"%");
		}
		IPage p = service.querySupportTemplate(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}跳转文档追加页面")
	@RequestMapping("/add")
	public String add(String id,Model model){
		
		
		TabMain tabMain = new TabMain();
		model.addAttribute("tabMain",tabMain);
		return forward("add");
	}
	
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}跳转文档追加实现")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(SupportTemplate supportTemplate, HttpServletRequest request){
		
		// 功能字段设值
		// 创建字段
		supportTemplate.setCreatedById(getUserObject().getEmployee().getId());
		supportTemplate.setCreatedAt(new Date());
		supportTemplate.setCreatedPosId(getUserObject().getPosition().getId());
		supportTemplate.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		supportTemplate.setUpdatedById(getUserObject().getEmployee().getId());
		supportTemplate.setUpdatedAt(new Date());
		
		service.saveSupportTemplateInfo(supportTemplate);
		
		// lov
		LovMember lovMember = new LovMember();
		lovMember.setCode(supportTemplate.getId());
		lovMember.setOptTxt1("Y");
		lovMember.setGroupId("DOC_MANAGE_FILE");
		lovMember.setName(supportTemplate.getDocType());
		lovMember.setLeafFlag("N");

		lovMemberService.save(lovMember);
		return sendSuccessMessage(supportTemplate.getId());
	}
	
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}跳转文档编辑页面")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		model.addAttribute("mode", IConstants.ACTION_TYPE_EDIT);
		
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		SupportTemplate supportTemplate = service.getSupportTemplateInfo(id);
		if(supportTemplate==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		TabMain tabMain = new TabMain();
		if(hasPermission("P08DocAdminTab")) {
			tabMain.addTab("文档维护","/common/attachment/attachment.html?businessType=DOC_MANAGE_FILE&docGroupCode=ATTACHMENTTYPEGROUP&businessId="+supportTemplate.getId());
		}
		if(hasPermission("P08DocAdminT2Tab")) {
			tabMain.addTab("访问组", "/common/permission/tab.html?groupId=DOC_MANAGE_FILE&businessId=" + supportTemplate.getId());
		}
		
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("supportTemplate",supportTemplate);
		return forward("add");
	}
	
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}跳转文档编辑页面")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(SupportTemplate supportTemplate){
		
		// 更新字段
		supportTemplate.setUpdatedById(getUserObject().getEmployee().getId());
		supportTemplate.setUpdatedAt(new Date());
		service.updateSupportTemplateInfo(supportTemplate);
		
		LovMember lovMember = 
				(LovMember) CacheData.getInstance().getMember("DOC_MANAGE_FILE", supportTemplate.getId());
		
		lovMember.setName(supportTemplate.getDocType());

		lovMemberService.update(lovMember);
		return sendSuccessMessage();
	}
	
	@LogOperate(module = "支持管理模块：文档管理", notes = "${user}实现文档删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteSupportTemplateInfo(id);
		return sendSuccessMessage();
	}
}
