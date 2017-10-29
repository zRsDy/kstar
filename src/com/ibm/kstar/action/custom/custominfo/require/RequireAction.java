package com.ibm.kstar.action.custom.custominfo.require;

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
import com.ibm.kstar.entity.custom.CustomRequireInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/require")
public class RequireAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	

	// 需求
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}展示需求产品信息")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {

		ActionUtil.requestToCondition(condition, request);

		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("customId", "eq", customCode);
		IPage p = service.queryRequire(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击需求产品添加页面")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		CustomRequireInfo customRequireInfo = new CustomRequireInfo();
		customRequireInfo.setCustomId(id);
		model.addAttribute("customRequireInfo", customRequireInfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现需求产品添加功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomRequireInfo customRequireInfo, HttpServletRequest request) {
		// 功能字段设值
		// 创建字段
		customRequireInfo.setCreatedById(getUserObject().getEmployee().getId());
		customRequireInfo.setCreatedAt(new Date());
		customRequireInfo.setCreatedPosId(getUserObject().getPosition().getId());
		customRequireInfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customRequireInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customRequireInfo.setUpdatedAt(new Date());
		service.saveRequireInfo(customRequireInfo);
		return sendSuccessMessage(customRequireInfo.getCustomId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击需求产品编辑页面")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomRequireInfo customRequireInfo = service.getRequireInfo(id);
		if (customRequireInfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customRequireInfo", customRequireInfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现需求产品编辑功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomRequireInfo customRequireInfo) {

		// 功能字段设值
		// 更新字段
		customRequireInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customRequireInfo.setUpdatedAt(new Date());
		service.updateRequireInfo(customRequireInfo);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现需求产品删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteRequireInfo(id);
		return sendSuccessMessage();
	}

}
