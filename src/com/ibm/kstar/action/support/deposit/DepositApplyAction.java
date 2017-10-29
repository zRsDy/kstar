package com.ibm.kstar.action.support.deposit;

import java.text.SimpleDateFormat;
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

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.support.deposit.IDepositApplyService;
import com.ibm.kstar.entity.support.deposit.DepositApply;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/depositApply")
public class DepositApplyAction extends BaseAction {
	@Autowired
	IDepositApplyService service;
	
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请一览页面")
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请一览页面查询")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addCondition("applyCode", "like", "%"+searchKey+"%");
		}
		IPage p = service.query(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请记录追加页面")
	@RequestMapping("/add")
	public String add(String id,Model model){

		DepositApply depositApply = new DepositApply();
		depositApply.setId(null);
		
		depositApply.setApplier(getUserObject().getEmployee().getId());
		depositApply.setApplierNo(getUserObject().getEmployee().getNo());
		depositApply.setApplierName(getUserObject().getEmployee().getName());
		depositApply.setApplierOrg(getUserObject().getOrg().getId());
		depositApply.setApplierOrgName(getUserObject().getOrg().getName());
		depositApply.setApplierPos(getUserObject().getPosition().getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		depositApply.setApplyDate(sdf.format(new Date()));
		
		sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		
		depositApply.setApplyCode(sdf.format(new Date()));
		
		
		
		depositApply.setStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		model.addAttribute("depositApply", depositApply);
		return forward("add");
	}
	
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请记录追加实现")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(DepositApply depositApply , HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		
		// 功能字段设值
		// 创建字段
		depositApply.setCreatedById(getUserObject().getEmployee().getId());
		depositApply.setCreatedAt(new Date());
		depositApply.setCreatedPosId(getUserObject().getPosition().getId());
		depositApply.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		depositApply.setUpdatedById(getUserObject().getEmployee().getId());
		depositApply.setUpdatedAt(new Date());
		
		service.save(depositApply);
		return sendSuccessMessage(depositApply.getId());
	}
	
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请记录编辑追加页面")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		DepositApply depositApply = service.getDepositApply(id);
		if(depositApply==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("depositApply",depositApply);
		return forward("add");
	}
	
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请记录保存页面")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(DepositApply depositApply){
		
		// 更新字段
		depositApply.setUpdatedById(getUserObject().getEmployee().getId());
		depositApply.setUpdatedAt(new Date());
		service.update(depositApply);
		return sendSuccessMessage();
	}
	
	@LogOperate(module = "支持管理模块：投标保证金申请", notes = "${user}跳转投标保证金申请记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.delete(id);
		return sendSuccessMessage();
	}
}
