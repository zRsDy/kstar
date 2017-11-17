package com.ibm.kstar.action.custom.custominfo.contact;

import java.util.Date;
import java.util.List;

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
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.common.customlov.Custom;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/contact")
public class ContactAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	

	// 公司关系-联系人
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}公司关系-联系人一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("customId", "eq", customCode);
		IPage p = service.queryContact(condition);

		return sendSuccessMessage(p);
	}
	
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击公司关系-联系人添加")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		CustomRelaContact customRelaContact = new CustomRelaContact();
		customRelaContact.setCustomId(id);

		customRelaContact.setContactValid("COMMONYN_Y");//
		model.addAttribute("customRelaContact", customRelaContact);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现公司关系-联系人添加")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomRelaContact customRelaContact,
			HttpServletRequest request) {
		// 功能字段设值
		// 创建字段
		customRelaContact.setCreatedById(getUserObject().getEmployee().getId());
		customRelaContact.setCreatedAt(new Date());
		customRelaContact.setCreatedPosId(getUserObject().getPosition().getId());
		customRelaContact.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customRelaContact.setUpdatedById(getUserObject().getEmployee().getId());
		customRelaContact.setUpdatedAt(new Date());
		service.saveContactInfo(customRelaContact, getUserObject());
		return sendSuccessMessage(customRelaContact.getCustomId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击公司关系-联系人编辑")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomRelaContact customRelaContact = service.getContactInfo(id);
		if (customRelaContact == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customRelaContact", customRelaContact);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击公司关系-联系人添加")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomRelaContact customRelaContact) {

		// 功能字段设值
		// 更新字段
		customRelaContact.setUpdatedById(getUserObject().getEmployee().getId());
		customRelaContact.setUpdatedAt(new Date());
		service.updateContactInfo(customRelaContact);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现公司关系-联系人删除")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteContactInfo(id);
		return sendSuccessMessage();
	}


}
