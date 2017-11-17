package com.ibm.kstar.action.custom.customevent.persons;

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

import com.ibm.kstar.api.custom.ICustomEventService;
import com.ibm.kstar.entity.custom.CustomEventContact;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/event/person")
public class PersonAction extends BaseAction {

	@Autowired
	ICustomEventService service;
	

	@NoRight
	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}人员名单一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		
		String eventPid = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("eventPid", "eq", eventPid);
		
		IPage p = service.queryCustomEventContact(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}点击人员名单添加")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		CustomEventContact customEventContact = new CustomEventContact();
		customEventContact.setEventPid(id);

		model.addAttribute("customEventContact", customEventContact);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}实现人员名单添加")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomEventContact customEventContact,
			HttpServletRequest request) {
		// 功能字段设值
		// 创建字段
		customEventContact.setCreatedById(getUserObject().getEmployee().getId());
		customEventContact.setCreatedAt(new Date());
		customEventContact.setCreatedPosId(getUserObject().getPosition().getId());
		customEventContact.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customEventContact.setUpdatedById(getUserObject().getEmployee().getId());
		customEventContact.setUpdatedAt(new Date());
		service.saveCustomEventContact(customEventContact, getUserObject());
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}点击人员名单编辑")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomEventContact customEventContact = service.getCustomEventContact(id);
		if (customEventContact == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customEventContact", customEventContact);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}实现人员名单编辑")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomEventContact customEventContact) {

		// 功能字段设值
		// 更新字段
		customEventContact.setUpdatedById(getUserObject().getEmployee().getId());
		customEventContact.setUpdatedAt(new Date());
		service.updateCustomEventContact(customEventContact);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}实现人员名单删除")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteCustomEventContact(id);
		return sendSuccessMessage();
	}


}
