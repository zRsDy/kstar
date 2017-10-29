/** 
 * Project Name:crm 
 * File Name:AnltgtmgtAction.java 
 * Package Name:com.ibm.kstar.action.report 
 * Date:Mar 15, 20173:32:10 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.action.report;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.report.IAnltgtmgtService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.entity.report.KstarAnltgt;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * ClassName:AnltgtmgtAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 15, 2017 3:32:10 PM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/report")
public class AnltgtmgtAction extends BaseAction {

	@Autowired
	IAnltgtmgtService anltgtmgtService;
	
	@Autowired
	protected ILovMemberService lovMemberService;

	@RequestMapping("/anltgtmgt")
	public String anltgtmgt(String id, Model model) {
		return forward("anltgtmgt");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		// 模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if (searchKey != null) {
			condition.getFilterObject().addOrCondition("annual", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("dep", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("emp", "like", "%" + searchKey + "%");
		}
		IPage p = anltgtmgtService.query(condition);

		return sendSuccessMessage(p);
	}

	
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(KstarAnltgt anltgt, HttpServletRequest request) {
		
		//
		//anltgt.setEmpNm(getUserObject().getEmployee().getId());
		//anltgt.setEmp(getUserObject().getEmployee().getName());
		
		String checkduplicate = anltgtmgtService.checkAngtgt(anltgt);
		
		if(checkduplicate.equals("N")){
			throw new AnneException("重复数据不能保存！");
		}
		
		if(anltgt.getCurrency().equals("")){
			anltgt.setCurrency("CURRENCY_CNY");
		}
		
		anltgtmgtService.saveKstarAnltgt(anltgt);
		
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/add")
	public String add(String id,Model model){
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
		//model.addAttribute("empid",JSON.toJSONString(lovMemberService.get(getUserObject().getEmployee().getId())));
				
		return forward("add");
	}
	
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
		//model.addAttribute("empid",JSON.toJSONString(lovMemberService.get(getUserObject().getEmployee().getId())));
		
		KstarAnltgt anltgt = anltgtmgtService.getKstarAnltgt(id);
		
		if(anltgt==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		if(anltgt.getCurrency().equals(null)){
			anltgt.setCurrency("CURRENCY_CNY");
		}
		
		model.addAttribute("anltgtInfo",anltgt);
		
		return forward("add");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(KstarAnltgt anltgt){
		//
		//anltgt.setEmpNm(getUserObject().getEmployee().getId());
		//anltgt.setEmp(getUserObject().getEmployee().getName());
		
		anltgtmgtService.updateKstarAnltgt(anltgt);
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		anltgtmgtService.deleteKstarAnltgt(id);
		return sendSuccessMessage();
	}
	
	
	
}
