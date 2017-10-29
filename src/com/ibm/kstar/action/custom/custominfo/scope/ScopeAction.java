package com.ibm.kstar.action.custom.custominfo.scope;

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
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.custom.CustomDistrScope;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/scope")
public class ScopeAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	
	// 经销范围
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}经销范围一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {

		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("customId", "eq", customCode);
		IPage p = service.queryScope(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击经销范围添加")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		CustomDistrScope customDistrScope = new CustomDistrScope();
		customDistrScope.setCustomId(id);
		model.addAttribute("customDistrScope", customDistrScope);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现经销范围添加")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomDistrScope customDistrScope,
			HttpServletRequest request) {

		// 功能字段设值
		// 创建字段
		customDistrScope.setCreatedById(getUserObject().getEmployee().getId());
		customDistrScope.setCreatedAt(new Date());
		customDistrScope.setCreatedPosId(getUserObject().getPosition().getId());
		customDistrScope.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customDistrScope.setUpdatedById(getUserObject().getEmployee().getId());
		customDistrScope.setUpdatedAt(new Date());
		service.saveScopeInfo(customDistrScope);
		return sendSuccessMessage(customDistrScope.getCustomId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击经销范围编辑")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomDistrScope customDistrScope = service.getScopeInfo(id);
		if (customDistrScope == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customDistrScope", customDistrScope);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现经销范围编辑")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomDistrScope customDistrScope) {

		// 功能字段设值
		// 更新字段
		customDistrScope.setUpdatedById(getUserObject().getEmployee().getId());
		customDistrScope.setUpdatedAt(new Date());
		service.updateScopeInfo(customDistrScope);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现经销范围删除")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String scopeDelete(String id) {
		service.deleteScopeInfo(id);
		return sendSuccessMessage();
	}

	

}
