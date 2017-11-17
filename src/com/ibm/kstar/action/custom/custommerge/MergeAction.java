package com.ibm.kstar.action.custom.custommerge;

import java.text.SimpleDateFormat;
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
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomMergeService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.entity.custom.CustomMerge;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/merge")
public class MergeAction extends BaseAction {

	@Autowired
	ICustomMergeService service;
	
	@Autowired
	ICustomNumberService numberService;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：客户合并",notes="${user}页面：客户合并一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customCodeFrm", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customNameFrm", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customCodeTo", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customNameTo", "like", "%"+searchKey+"%");
		}
		
		IPage p = service.queryMerge(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：客户合并",notes="${user}页面：客户合并一览记录添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		model.addAttribute("id",id);
		CustomMerge customMerge = new CustomMerge();
		customMerge.setId(null);
		
		customMerge.setMergeCode(numberService.getMergeNumber());
		
		customMerge.setApplierName(getUserObject().getEmployee().getNo().concat("/").concat(getUserObject().getEmployee().getName()));
		customMerge.setApplier(getUserObject().getEmployee().getId());
		
		customMerge.setApplierOrg(getUserObject().getOrg().getId());
		customMerge.setApplierPos(getUserObject().getPosition().getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customMerge.setMergeDate(sdf.format(new Date()));
		
		customMerge.setMergeStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		model.addAttribute("customMerge",customMerge);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：客户合并",notes="${user}页面：客户合并一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String mergeAdd(CustomMerge customMerge, HttpServletRequest request){
		
		if (StringUtils.equals(customMerge.getCustomCodeFrm(), customMerge.getCustomCodeTo())) {
			throw new AnneException("合并用户相同，不能合并!");
		}
		
		// 功能字段设值
		// 创建字段
		customMerge.setCreatedById(getUserObject().getEmployee().getId());
		customMerge.setCreatedAt(new Date());
		customMerge.setCreatedPosId(getUserObject().getPosition().getId());
		customMerge.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customMerge.setUpdatedById(getUserObject().getEmployee().getId());
		customMerge.setUpdatedAt(new Date());
		service.saveMergeInfo(customMerge);
		return sendSuccessMessage(customMerge.getMergeCode());
	}
	
	@LogOperate(module="客户管理模块：客户合并",notes="${user}页面：客户合并一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomMerge customMerge = service.getMergeInfo(id);
		if(customMerge==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customMerge",customMerge);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：客户合并",notes="${user}页面：客户合并一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomMerge customMerge){
		
		if (StringUtils.equals(customMerge.getCustomCodeFrm(), customMerge.getCustomCodeTo())) {
			throw new AnneException("合并用户相同，不能合并!");
		}
		
		// 功能字段设值
		// 更新字段
		customMerge.setUpdatedById(getUserObject().getEmployee().getId());
		customMerge.setUpdatedAt(new Date());
		service.updateMergeInfo(customMerge);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：客户合并",notes="${user}页面：客户合并一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteMergeInfo(id);
		return sendSuccessMessage();
	}
	
}
