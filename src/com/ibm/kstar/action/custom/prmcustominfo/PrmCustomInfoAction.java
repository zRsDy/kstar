package com.ibm.kstar.action.custom.prmcustominfo;

import java.util.Date;

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
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/prm/baseinfo")
public class PrmCustomInfoAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	@Autowired
	protected ILovGroupService lovGroupService;
	@Autowired
	protected ILovMemberService lovMemberService;
	@Autowired
	ICustomNumberService numberService;

	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}打开了客户一览页面")
	@RequestMapping("/list")
	public String list(String id, Model model) {
		return forward("list");
	}

	@NoRight
	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}打开了客户一览页面，进行客户检索")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);

		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customCode", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("customFullName", "like", "%" + searchKey + "%");
		}

		IPage p = service.query(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}打开了客户添加页面")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("mode", IConstants.ACTION_TYPE_ADD);
		
		CustomInfo customInfo = new CustomInfo();
		customInfo.setId(null);
		
		// 新增记录编码为空，待ERP返回
		customInfo.setCustomCode(numberService.getReportNumber());
		customInfo.setErpCode(customInfo.getCustomCode());

		customInfo.setCustomStatus("CUSTOMSTATUS_10");// 客户状态
		customInfo.setCustomControlStatus(IConstants.CONTROLSTATUS_01);//
		customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_10);

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		model.addAttribute("tabMain", tabMain);

		model.addAttribute("customInfo", customInfo);
		return forward("add");
	}

	

	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}点击新增保存功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomInfo customInfo, HttpServletRequest request) {
		customInfo.setCustomType("1");
		customInfo.setPrmFlg(IConstants.PRM_FLAG);
		LovMember lov = lovMemberService.getLovMemberByCode(IConstants.ADDRESSREGION,IConstants.ADDRESSREGION_CN);
		customInfo.setCustomArea(lov.getId());
		
		// main info
		// 功能字段设值
		// 创建字段
		customInfo.setCreatedById(getUserObject().getEmployee().getId());
		customInfo.setCreatedAt(new Date());
		customInfo.setCreatedPosId(getUserObject().getPosition().getId());
		customInfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customInfo.setUpdatedAt(new Date());
		service.saveCustomInfo(customInfo, getUserObject());
		
		return sendSuccessMessage(customInfo.getCustomCode());
	}

	@NoRight
	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}打开了编辑页面")
	@RequestMapping("/edit")
	public String edit(String id, String flg, Model model, PageCondition condition, HttpServletRequest request) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}

		CustomInfo customInfo = service.getCustomInfo(id);
		if (customInfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		

		model.addAttribute("customInfo", customInfo);

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);

		
		model.addAttribute("mode", IConstants.ACTION_TYPE_EDIT);
		model.addAttribute("tabMain", tabMain);
		return forward("add");
	}

	@NoRight
	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}点击编辑保存功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomInfo customInfo) {
		
		// 功能字段设值
		// 更新字段
		customInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customInfo.setUpdatedAt(new Date());
		service.updateCustomInfo(customInfo);
		
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}点击客户删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteCustomInfo(id);
		return sendSuccessMessage();
	}

}
