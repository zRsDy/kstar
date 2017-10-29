package com.ibm.kstar.action.custom.custominfo.erp;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.custom.CustomErpInfo;
import com.ibm.kstar.entity.custom.vo.CustomErpAddress;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/erp")
public class ErpAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	

	// ERP引入
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}ERP引入功能一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {

		ActionUtil.requestToCondition(condition, request);
		IPage p = service.queryErp(condition,new String[]{});
		return sendSuccessMessage(p);
	}

	@RequestMapping("/add")
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现ERP引入添加功能")
	public String add(String id, Model model) {
		CustomErpInfo customErpInfo = new CustomErpInfo();
		customErpInfo.setCustomId(id);

		customErpInfo.setCorpErpStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		model.addAttribute("customErpInfo", customErpInfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现ERP引入添加功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomErpInfo customErpInfo, HttpServletRequest request) {

		String[] status = new String[]{IConstants.CUSTOM_NORMAL_STATUS_10
				, IConstants.CUSTOM_NORMAL_STATUS_20
				, IConstants.CUSTOM_NORMAL_STATUS_30};
		
		List<CustomErpAddress> customErpAddress =  service.getErpInfoByIdStatus(customErpInfo.getCustomId(), status);
		
		if(customErpAddress != null && customErpAddress.size() > 0) {
			throw new AnneException("存在未审批完成的ERP引入，不能继续新建！");
		}
		
		// 功能字段设值
		// 创建字段
		customErpInfo.setCreatedById(getUserObject().getEmployee().getId());
		customErpInfo.setCreatedAt(new Date());
		customErpInfo.setCreatedPosId(getUserObject().getPosition().getId());
		customErpInfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customErpInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customErpInfo.setUpdatedAt(new Date());
		service.saveErpInfo(customErpInfo);
		return sendSuccessMessage(customErpInfo.getCustomId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击ERP引入编辑功能")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomErpAddress customErpInfo = service.getErpInfo(id);
		if (customErpInfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		
		if(StringUtils.equals(customErpInfo.getCorpErpStatus(), IConstants.CUSTOM_NORMAL_STATUS_40) 
				|| StringUtils.equals(customErpInfo.getCorpErpStatus(), IConstants.CUSTOM_NORMAL_STATUS_20)) {
			throw new AnneException("审批中或审批完成的引入不能被修改!");
		}
		
		model.addAttribute("customErpInfo", customErpInfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现ERP引入编辑功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomErpInfo customErpInfo) {

		// 功能字段设值
		// 更新字段
		customErpInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customErpInfo.setUpdatedAt(new Date());
		service.updateErpInfo(customErpInfo, customErpInfo.getCorpErpStatus());
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现ERP引入删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		
		CustomErpAddress customErpInfo = service.getErpInfo(id);
		if(StringUtils.equals(customErpInfo.getCorpErpStatus(), IConstants.CUSTOM_NORMAL_STATUS_40) 
				|| StringUtils.equals(customErpInfo.getCorpErpStatus(), IConstants.CUSTOM_NORMAL_STATUS_20)) {
			throw new AnneException("审批中或审批完成的引入不能被删除!");
		}
		
		service.deleteErpInfo(id);
		return sendSuccessMessage();
	}

}
