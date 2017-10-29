package com.ibm.kstar.action.bizopp.weeklyReport;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.InternationWeekly;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * ClassName:BizweeklyAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 6, 2017 10:07:56 AM <br/>
 * 
 * @author Wutw
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/weekly")
public class BizWeeklyAction extends BaseAction {

	@Autowired
	IBizBaseService bizService;
	
	@Autowired 
	IBizoppService bizoppService;

	@RequestMapping("/list")
	public String index(String id, Model model) {
		return forward("list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		IPage p = bizoppService.queryWeekly(condition);

		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/pipage")
	public String piPage(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String weeklyId = request.getParameter("weeklyId");
		IPage p = bizoppService.queryPi(condition, weeklyId);
		return sendSuccessMessage(p);
	}
	

	@RequestMapping("/add")
	public String add(Model model){
		InternationWeekly entity = new InternationWeekly();
		UserObject userObject = getUserObject();
		String userId = userObject.getEmployee().getId();
		String userName = userObject.getEmployee().getName();
		String orgName = userObject.getOrg().getName();
		entity.setSalesid(userId);
		entity.setSalesman(userName);
		entity.setDepartment(orgName);
		entity.setStatus("NEW");
		entity.fillInit(getUserObject());
		model.addAttribute("entity", entity);
		return forward("weeklyAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(InternationWeekly entity, HttpServletRequest request) {

		entity.fillInit(getUserObject());
		bizoppService.saveWeekly(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		InternationWeekly entity = bizoppService.getWeeklyEntity(id);
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("weeklyAdd");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(InternationWeekly bizopp){
		bizoppService.updateWeekly(bizopp);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizoppService.deleteWeekly(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/submitData",method = RequestMethod.POST)
	public String submitData(String id){
		bizoppService.submitWeeklyData(id);
		return sendSuccessMessage();
	}
}
