package com.ibm.kstar.action.custom.custominfo.bank;

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
import com.ibm.kstar.entity.custom.CustomFinancePinfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/bank")
public class BankAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	

	// 银行
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}银行一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("customId", "eq", customCode);
		IPage p = service.queryBank(condition);
		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击银行添加功能")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		CustomFinancePinfo customFinancePinfo = new CustomFinancePinfo();
		customFinancePinfo.setCustomId(id);
		customFinancePinfo.setCorpInvoiceBankSta("COMMONYN_Y");//
		model.addAttribute("customFinancePinfo", customFinancePinfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现银行添加功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomFinancePinfo customFinancePinfo, HttpServletRequest request) {

		// 功能字段设值
		// 创建字段
		customFinancePinfo.setCreatedById(getUserObject().getEmployee().getId());
		customFinancePinfo.setCreatedAt(new Date());
		customFinancePinfo.setCreatedPosId(getUserObject().getPosition().getId());
		customFinancePinfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customFinancePinfo.setUpdatedById(getUserObject().getEmployee().getId());
		customFinancePinfo.setUpdatedAt(new Date());
		service.saveBankInfo(customFinancePinfo);
		return sendSuccessMessage(customFinancePinfo.getCustomId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击银行编辑功能")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomFinancePinfo customFinancePinfo = service.getBankInfo(id);
		if (customFinancePinfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customFinancePinfo", customFinancePinfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现银行编辑功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomFinancePinfo customFinancePinfo) {

		// 功能字段设值
		// 更新字段
		customFinancePinfo.setUpdatedById(getUserObject().getEmployee().getId());
		customFinancePinfo.setUpdatedAt(new Date());
		service.updateBankInfo(customFinancePinfo);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现银行删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteBankInfo(id);
		return sendSuccessMessage();
	}

}
