package com.ibm.kstar.action.custom.zxbInt;

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
import com.ibm.kstar.api.custom.ICustomerZXBInfoService;
import com.ibm.kstar.entity.custom.CustomZXBInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/customZXB")
public class CustomInfoZXBAction extends BaseAction {
	
	@Autowired
	ICustomerZXBInfoService service;

	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}打开了中信保客户一览页面")
	@RequestMapping("/customInfo")
	public String index(String id, Model model) {
		return forward("customZXBList");
	}
	
	@NoRight
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}打开了中信保客户一览页面，进行客户检索")
	@ResponseBody
	@RequestMapping("/customListPage")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if (searchKey != null) {
			condition.getFilterObject().addOrCondition("corpSerialNo", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("chnName", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("engName", "like", "%" + searchKey + "%");
		}
		IPage p = service.query(condition);
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}打开了中信保客户添加页面")
	@RequestMapping("/add")
	public String customMainAdd(String id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("mode", IConstants.ACTION_TYPE_ADD);
		TabMain tabMain = new TabMain();
		model.addAttribute("tabMain", tabMain);
		CustomZXBInfo customInfo = service.createCustomInfor();
		model.addAttribute("customInfo", customInfo);
		return forward("customZXBMain");
	}
	
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}点击新增保存功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String customMainAdd(CustomZXBInfo customInfo, HttpServletRequest request) {
		customInfo.setRecordInfor(false, super.getUserObject());
		service.saveCustomInfo(customInfo);
		return sendSuccessMessage("创建成功：" + customInfo.getCorpSerialNo());
	}
	
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}打开了编辑页面")
	@RequestMapping("/edit")
	public String edit(String id, Model model, PageCondition condition, HttpServletRequest request) {
		if (null == id) {
			throw new AnneException("没有找到对应ID所需要修改的数据");
		}
		CustomZXBInfo customInfo = service.getCustomInfoById(id);
		if (customInfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customInfo", customInfo);
		TabMain tabMain = new TabMain();
		model.addAttribute("tabMain", tabMain);
		return forward("customZXBMain");
	}

	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}点击编辑保存功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomZXBInfo customInfo) {
		customInfo.setRecordInfor(true, super.getUserObject());
		service.updateCustomInfo(customInfo);
		return sendSuccessMessage("更新成功：" + customInfo.getCorpSerialNo());
	}
	
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}点击客户删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteCustomInfo(id);
		return sendSuccessMessage("删除成功" );
	}
	
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}点击客户复制功能")
	@ResponseBody
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	public String copy(String id) {
		CustomZXBInfo customInfo = new CustomZXBInfo();
		customInfo.setId(id);
		customInfo.setRecordInfor(false, super.getUserObject());
		String corpSerialNo = service.copyCustomInfo(customInfo);
		return sendSuccessMessage("复制生成新单据流水号:" + corpSerialNo);
	}
	
	@LogOperate(module = "客户管理模块：中信保买家代码申请", notes = "${user}点击客户提交申请功能")
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id) {
		CustomZXBInfo customInfo = new CustomZXBInfo();
		customInfo.setId(id);
		customInfo.setRecordInfor(true, super.getUserObject());
		String corpSerialNo = service.doBuyerCodeApply(customInfo);
		return sendSuccessMessage("提交申请成功: "+ corpSerialNo);
	}
	
}
