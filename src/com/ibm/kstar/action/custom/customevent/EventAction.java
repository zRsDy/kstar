package com.ibm.kstar.action.custom.customevent;

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
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomEventService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.custom.CustomEventInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/event")
public class EventAction extends BaseAction {

	@Autowired
	ICustomEventService service;
	
	@Autowired
	ICustomNumberService numberService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateFeedBack", method = RequestMethod.POST)
	public String reviseQuot(String cid,HttpServletRequest request) throws Exception{		
		
		String eventResult = request.getParameter("eventResult");
		
		CustomEventInfo customEventInfo = service.getEventInfo(cid);
		if(customEventInfo==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		customEventInfo.setEventResult(eventResult);
		
		service.updateEventInfo(customEventInfo);
		
		return sendSuccessMessage();
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户活动一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String eventCode = condition.getStringCondition("eventCode");
		if(StringUtil.isNotEmpty(eventCode)){
			condition.getFilterObject().addCondition("eventCode", "like", "%" + eventCode + "%");
		}
		
		String dateFrom = condition.getStringCondition("dateFrom");
		if(StringUtil.isNotEmpty(dateFrom)){
			condition.getFilterObject().addCondition("createdAt", ">=", dateFrom);
		}
		
		String dateTo = condition.getStringCondition("dateTo");
		if(StringUtil.isNotEmpty(dateTo)){
			condition.getFilterObject().addCondition("createdAt", "<=", dateTo);
		}
		
		String applier = condition.getStringCondition("applier");
		if(StringUtil.isNotEmpty(applier)){
			condition.getFilterObject().addCondition("createdById", "=", applier);
		}
		
		String eventStatus = condition.getStringCondition("eventStatus");
		if(StringUtil.isNotEmpty(eventStatus)){
			condition.getFilterObject().addCondition("eventStatus", "=", eventStatus);
		}
		
		String eventBearDeaprtment = condition.getStringCondition("eventBearDeaprtment");
		if(StringUtil.isNotEmpty(eventBearDeaprtment)){
			condition.getFilterObject().addCondition("eventBearDeaprtment", "=", eventBearDeaprtment);
		}
		
		IPage p = service.queryEvent(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户活动添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		model.addAttribute("id",id);
		CustomEventInfo customEventInfo = new CustomEventInfo();
		customEventInfo.setId(null);
		
		customEventInfo.setEventCode(numberService.getEventNumber());
		
		
		customEventInfo.setApplierName(getUserObject().getEmployee().getNo().concat("/").concat(getUserObject().getEmployee().getName()));
		customEventInfo.setApplier(getUserObject().getEmployee().getId());
		
		customEventInfo.setApplierOrg(getUserObject().getOrg().getId());
		customEventInfo.setApplierPos(getUserObject().getPosition().getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customEventInfo.setEventCreateDate(sdf.format(new Date()));
		
		customEventInfo.setEventStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		customEventInfo.setIs1("0");
		customEventInfo.setIs2("0");
		customEventInfo.setIs3("0");
		customEventInfo.setIs4("0");
		customEventInfo.setIs5("0");
		customEventInfo.setIs6("0");
		customEventInfo.setIs7("0");
		
		customEventInfo.setIs11("0");
		customEventInfo.setIs12("0");
		customEventInfo.setIs13("0");
		customEventInfo.setIs14("0");
		customEventInfo.setIs15("0");
		customEventInfo.setIs16("0");
		customEventInfo.setIs17("0");
		customEventInfo.setIs18("0");
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		model.addAttribute("mode", IConstants.ACTION_TYPE_ADD);
		
		LovMember lov = lovMemberService.getLovMemberByCode("COMMONYN","Y");
		model.addAttribute("isExistValue", lov.getId());
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("personCnt","0");
		model.addAttribute("customEventInfo",customEventInfo);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户活动添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String eventAdd(CustomEventInfo customEventInfo, HttpServletRequest request){
		if ((StringUtils.equals("0", customEventInfo.getIs1()) || StringUtils.isEmpty(customEventInfo.getIs1()))
				&& (StringUtils.equals("0", customEventInfo.getIs2()) || StringUtils.isEmpty(customEventInfo.getIs2()))
				&& (StringUtils.equals("0", customEventInfo.getIs3()) || StringUtils.isEmpty(customEventInfo.getIs3()))
				&& (StringUtils.equals("0", customEventInfo.getIs4()) || StringUtils.isEmpty(customEventInfo.getIs4()))
				&& (StringUtils.equals("0", customEventInfo.getIs5()) || StringUtils.isEmpty(customEventInfo.getIs5()))
				&& (StringUtils.equals("0", customEventInfo.getIs6()) || StringUtils.isEmpty(customEventInfo.getIs6()))
				&& (StringUtils.equals("0", customEventInfo.getIs7()) || StringUtils.isEmpty(customEventInfo.getIs7()))) {
			throw new AnneException("请至少选怎一个客户关注点!");
		}
		
		if ((StringUtils.equals("0", customEventInfo.getIs11()) || StringUtils.isEmpty(customEventInfo.getIs11()))
				&& (StringUtils.equals("0", customEventInfo.getIs12()) || StringUtils.isEmpty(customEventInfo.getIs12()))
				&& (StringUtils.equals("0", customEventInfo.getIs13()) || StringUtils.isEmpty(customEventInfo.getIs13()))
				&& (StringUtils.equals("0", customEventInfo.getIs14()) || StringUtils.isEmpty(customEventInfo.getIs14()))
				&& (StringUtils.equals("0", customEventInfo.getIs15()) || StringUtils.isEmpty(customEventInfo.getIs15()))
				&& (StringUtils.equals("0", customEventInfo.getIs16()) || StringUtils.isEmpty(customEventInfo.getIs16()))
				&& (StringUtils.equals("0", customEventInfo.getIs17()) || StringUtils.isEmpty(customEventInfo.getIs17()))
				&& (StringUtils.equals("0", customEventInfo.getIs18()) || StringUtils.isEmpty(customEventInfo.getIs18()))) {
			throw new AnneException("请至少选怎一个协助事项!");
		}
		
		service.saveEventInfo(customEventInfo, getUserObject());
		return sendSuccessMessage(customEventInfo.getCustomCode());
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户活动编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomEventInfo customEventInfo = service.getEventInfo(id);
		if(customEventInfo==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if(hasPermission("P02ReceptionT1PersonPage")) {
			tabMain.addTab("人员名单", "/custom/event/persons.html?id=" + customEventInfo.getId());
		}
		if(hasPermission("P02ReceptionT2ActionPage")) {
			tabMain.addTab("协助事项", "/custom/event/items.html?id=" + customEventInfo.getId());
		}
		if(hasPermission("P02ReceptionT3FilePage")) {
			tabMain.addTab("附件","/common/attachment/attachment.html?businessType=ACCOUNT_PARTNER_FILE&docGroupCode=CONTRACTDOCTYPE&businessId="+customEventInfo.getId());
		}
		if(hasPermission("P02ReceptionT4TeamMemberQuery")) {
			tabMain.addTab("销售团队", "/team/list.html?businessType="+ IConstants.CUSTOM_EVENT_PROC  +"&businessId="+customEventInfo.getId());
		}
		if(hasPermission("P02ReceptionT5ProReviewHistory")) {
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+customEventInfo.getId());
		}
		
		if (!StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, customEventInfo.getEventStatus())) {
			if(!(hasPermission("P02ReceptionSaveInProcess") || hasPermission("P02ReceptionSaveApproved"))) {
				model.addAttribute("saveDisabled","disabled");
			}
		}
		model.addAttribute("mode", IConstants.ACTION_TYPE_EDIT);
		
		if(StringUtils.isEmpty(customEventInfo.getCustomCode())) {
			model.addAttribute("isExistValue", "COMMONYN_N");
		} else {
			model.addAttribute("isExistValue", "COMMONYN_Y");
		}
		
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("personCnt","0");
		model.addAttribute("customEventInfo",customEventInfo);
		return forward("add");
	}
	
	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}点击人员名单列表")
	@RequestMapping("/persons")
	public String persons(String id, Model model) {
		CustomEventInfo customEventInfo = new CustomEventInfo();
		if (!StringUtils.isEmpty(id)) {
			customEventInfo = service.getEventInfo(id);
		}

		model.addAttribute("customEventInfo", customEventInfo);
		return forward("persons");
	}
	
	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}点击协助事项列表")
	@RequestMapping("/items")
	public String items(String id, Model model) {
		CustomEventInfo customEventInfo = new CustomEventInfo();
		if (!StringUtils.isEmpty(id)) {
			customEventInfo = service.getEventInfo(id);
		}

		model.addAttribute("customEventInfo", customEventInfo);
		return forward("items");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户活动编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomEventInfo customEventInfo){
		if ((StringUtils.equals("0", customEventInfo.getIs1()) || StringUtils.isEmpty(customEventInfo.getIs1()))
				&& (StringUtils.equals("0", customEventInfo.getIs2()) || StringUtils.isEmpty(customEventInfo.getIs2()))
				&& (StringUtils.equals("0", customEventInfo.getIs3()) || StringUtils.isEmpty(customEventInfo.getIs3()))
				&& (StringUtils.equals("0", customEventInfo.getIs4()) || StringUtils.isEmpty(customEventInfo.getIs4()))
				&& (StringUtils.equals("0", customEventInfo.getIs5()) || StringUtils.isEmpty(customEventInfo.getIs5()))
				&& (StringUtils.equals("0", customEventInfo.getIs6()) || StringUtils.isEmpty(customEventInfo.getIs6()))
				&& (StringUtils.equals("0", customEventInfo.getIs7()) || StringUtils.isEmpty(customEventInfo.getIs7()))) {
			throw new AnneException("请至少选怎一个客户关注点!");
		}
		
		if ((StringUtils.equals("0", customEventInfo.getIs11()) || StringUtils.isEmpty(customEventInfo.getIs11()))
				&& (StringUtils.equals("0", customEventInfo.getIs12()) || StringUtils.isEmpty(customEventInfo.getIs12()))
				&& (StringUtils.equals("0", customEventInfo.getIs13()) || StringUtils.isEmpty(customEventInfo.getIs13()))
				&& (StringUtils.equals("0", customEventInfo.getIs14()) || StringUtils.isEmpty(customEventInfo.getIs14()))
				&& (StringUtils.equals("0", customEventInfo.getIs15()) || StringUtils.isEmpty(customEventInfo.getIs15()))
				&& (StringUtils.equals("0", customEventInfo.getIs16()) || StringUtils.isEmpty(customEventInfo.getIs16()))
				&& (StringUtils.equals("0", customEventInfo.getIs17()) || StringUtils.isEmpty(customEventInfo.getIs17()))
				&& (StringUtils.equals("0", customEventInfo.getIs18()) || StringUtils.isEmpty(customEventInfo.getIs18()))) {
			throw new AnneException("请至少选怎一个协助事项!");
		}
		
		// 功能字段设值
		// 更新字段
		customEventInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customEventInfo.setUpdatedAt(new Date());
		
		service.updateEventInfo(customEventInfo);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户反馈保存")
	@ResponseBody
	@RequestMapping(value="/feedBack",method=RequestMethod.POST)
	public String feedBack(CustomEventInfo customEventInfo){
		CustomEventInfo entity = service.getEventInfo(customEventInfo.getId());
		// 功能字段设值
		// 更新字段
		entity.setEventResult(customEventInfo.getEventResult());
		entity.setUpdatedById(getUserObject().getEmployee().getId());
		entity.setUpdatedAt(new Date());
		
		service.updateEventInfo(entity);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：客户活动",notes="${user}页面：客户活动删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		CustomEventInfo entity = service.getEventInfo(id);
		if(!entity.getEventStatus().equals("CUSTOM_NORMAL_STATUS_10")){
			throw new AnneException("活动一览状态不为新建状态下无法删除！");
		}
		service.deleteEventInfo(id);
		return sendSuccessMessage();
	}

}
