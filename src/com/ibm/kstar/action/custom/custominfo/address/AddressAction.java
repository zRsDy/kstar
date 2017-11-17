package com.ibm.kstar.action.custom.custominfo.address;

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
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/address")
public class AddressAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	
	@Autowired
	protected ILovMemberService lovMemberService;

	// 地址
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}地址一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("customId", "eq", customCode);
		IPage p = service.queryAddr(condition);
		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击地址添加功能")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		CustomAddressInfo customAddressInfo = new CustomAddressInfo();
		customAddressInfo.setCustomId(id);
		
		
		customAddressInfo.setCustomAddressType(lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId());
		customAddressInfo.setCustomAddressUse(lovMemberService.getLovMemberByCode("ADDRESSUSE", "3").getId());
		customAddressInfo.setLayer1(lovMemberService.getLovMemberByCode("ADDRESSREGION", "CN").getId());//
		
		customAddressInfo.setCustomAddressValid("COMMONYN_Y");//
		model.addAttribute("customAddressInfo", customAddressInfo);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现地址添加功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomAddressInfo customAddressInfo, HttpServletRequest request) {
		String area = customAddressInfo.getLayer1();
		LovMember lov = lovMemberService.get(area);
		if (lov != null && StringUtils.equals(lov.getCode(), IConstants.ADDRESSREGION_CN)) {
			if (StringUtils.isEmpty(customAddressInfo.getLayer2()) 
					|| StringUtils.isEmpty(customAddressInfo.getLayer3())) {
				throw new AnneException("当国家为[中国]时，请输入省市!");
			}
		}
		
		
		// 功能字段设值
		// 创建字段
		customAddressInfo.setCreatedById(getUserObject().getEmployee().getId());
		customAddressInfo.setCreatedAt(new Date());
		customAddressInfo
				.setCreatedPosId(getUserObject().getPosition().getId());
		customAddressInfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customAddressInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customAddressInfo.setUpdatedAt(new Date());
		service.saveAddrInfo(customAddressInfo);
		return sendSuccessMessage(customAddressInfo.getCustomId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击地址编辑功能")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomAddressInfo customAddressInfo = service.getAddrInfo(id);
		if (customAddressInfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customAddressInfo", customAddressInfo);
		
		
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现地址编辑功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomAddressInfo customAddressInfo) {
		
		//当前地址是否被ERP引入
		List<CustomAddressInfo> list = service.getCustomAddressInfoListById(customAddressInfo.getId());
		
		if(list.size()>0){
			throw new AnneException("当前地址已被ERP引用，不可修改！");
		}
		
		String area = customAddressInfo.getLayer1();
		LovMember lov = lovMemberService.get(area);
		if (lov != null && StringUtils.equals(lov.getCode(), IConstants.ADDRESSREGION_CN)) {
			if (StringUtils.isEmpty(customAddressInfo.getLayer2()) 
					|| StringUtils.isEmpty(customAddressInfo.getLayer3())) {
				throw new AnneException("当国家为[中国]时，请输入省市!");
			}
		}
		// 功能字段设值
		// 更新字段
		customAddressInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customAddressInfo.setUpdatedAt(new Date());
		service.updateAddrInfo(customAddressInfo);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击地址删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteAddrInfo(id);
		return sendSuccessMessage();
	}

}
