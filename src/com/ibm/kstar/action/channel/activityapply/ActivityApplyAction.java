package com.ibm.kstar.action.channel.activityapply;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.channel.IActivityApplyService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.ActivityApply;
import com.ibm.kstar.entity.channel.ActivityContent;
import com.ibm.kstar.entity.channel.ActivityExpense;
import com.ibm.kstar.entity.channel.ActivityPerson;
import com.ibm.kstar.entity.channel.ActivitySummary;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/activityApply")
public class ActivityApplyAction extends BaseAction {
	@Autowired
	IActivityApplyService activityApplyService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	SerialNumberService serialNumberService;
	
	@RequestMapping("/activity")
	public String activity(Model model) {
		this.outQueryCondition(model);
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());
		
		TabMain tabs = new TabMain();
		tabs.setInitAll(true);
		tabs.addTab("主要活动内容", "/activityApply/content.html?businessType=activity");
		tabs.addTab("参加活动人员", "/activityApply/person.html?businessType=activity");
		tabs.addTab("总结", "/activityApply/summary.html?businessType=activity");
		model.addAttribute("tabMain", tabs);
		
		return forward("activity_apply_list");
	}
	
	@RequestMapping("/train")
	public String train(Model model) {
		this.outQueryCondition(model);
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());
		
		TabMain tabs = new TabMain();
		tabs.setInitAll(true);
		tabs.addTab("培训内容", "/activityApply/content.html");
		tabs.addTab("参加活动人员", "/activityApply/person.html");
		tabs.addTab("总结", "/activityApply/summary.html");
		model.addAttribute("tabMain", tabs);
		
		return forward("train_apply_list");
	}
	
	@NoRight
	@RequestMapping("/selectInnerPerson")
	public String selectInnerPerson(String pickerId, Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("isInner",1);
		return forward("select_person");
	}
	
	@NoRight
	@RequestMapping("/selectOuterPerson")
	public String selectOuterPerson(String pickerId, Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("isInner",0);
		return forward("select_person");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/querySelectPersons")
	public String querySelectPersons(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = activityApplyService.querySelectPersons(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/content")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了主要活动内容页面")
	public String content(String applyId, String businessType, Model model) {
		model.addAttribute("applyId",applyId);
		if("activity".equals(businessType)){
			return forward("activity_content");
		}else{
			return forward("train_content");
		}
	}
	
	@RequestMapping("/person")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了参加活动成员页面")
	public String person(String applyId, String businessType, Model model) {
		model.addAttribute("applyId",applyId);
		if("activity".equals(businessType)){
			return forward("activity_person");
		}else{
			return forward("train_person");
		}
	}
	
	@RequestMapping("/summary")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了总结页面")
	public String summary(String applyId, String businessType, Model model) {
		model.addAttribute("applyId",applyId);
		if("activity".equals(businessType)){
			return forward("activity_summary");//活动申请
		}else{
			return forward("train_summary");//培训申请
		}
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	@LogOperate(module="市场活动或培训模块",notes="${user}查询了申请单号数据")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = activityApplyService.queryApplys(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}

	@NoRight
	@RequestMapping("/addOrEditActivity")
	@LogOperate(module="市场活动模块",notes="${user}打开了申请单号编辑页面")
	public String addOrEditActivity(String id, boolean statusEdit, boolean ableEdit, Model model) {
		this.addOrEdit(id, model);
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		return forward("activity_apply_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addOrEditActivity", method = RequestMethod.POST)
	@LogOperate(module="市场活动模块",notes="${user}提交了申请单号数据")
	public String addOrEditActivity(ActivityApply activityApply) {
		activityApplyService.addOrEditApply(activityApply,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@RequestMapping("/addOrEditTrain")
	@LogOperate(module="培训模块",notes="${user}打开了申请单号编辑页面")
	public String addOrEditTrain(String id, boolean statusEdit, boolean ableEdit, Model model) {
		this.addOrEdit(id, model);
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		return forward("train_apply_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addOrEditTrain", method = RequestMethod.POST)
	@LogOperate(module="培训模块",notes="${user}提交了申请单号数据")
	public String addOrEditTrain(ActivityApply activityApply) {
		activityApplyService.addOrEditApply(activityApply,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/checkSummary")
	public String checkSummary(String applyId) {
		activityApplyService.checkSummary(applyId);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/addOrEditSummary")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了执行编辑页面")
	public String addOrEditSummary(String applyId, String businessType, boolean statusEdit, Model model) {
		ActivitySummary summaryInfo = activityApplyService.querySummary(applyId);
		model.addAttribute("statusEdit",statusEdit);
		model.addAttribute("summaryInfo",summaryInfo);
		if("activity".equals(businessType)){
			return forward("activity_summary_add");
		}else{
			return forward("train_summary_add");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditSummary", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}提交了执行数据")
	public String addOrEditSummary(ActivitySummary activitySummary) {
		activityApplyService.addOrEditSummary(activitySummary,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}删除了申请单号数据")
	public String delete(String id) {
		activityApplyService.deleteApply(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/checkSubmit", method = RequestMethod.POST)
	public String checkSubmit(String id) {
		activityApplyService.checkSubmit(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submitActivity", method = RequestMethod.POST)
	@LogOperate(module="市场活动模块",notes="${user}提价了申请单号数据")
	public String submitActivity(String id) {
		activityApplyService.submitActivity(id,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submitTrain", method = RequestMethod.POST)
	@LogOperate(module="培训模块",notes="${user}提价了申请单号数据")
	public String submitTrain(String id) {
		activityApplyService.submitTrain(id,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/checkClose", method = RequestMethod.POST)
	public String checkClose(String id) {
		activityApplyService.checkCloseApply(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}关闭了申请单号数据")
	public String close(String id) {
		activityApplyService.closeApply(id,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageContent")
	@LogOperate(module="市场活动或培训模块",notes="${user}查询了主要活动内容")
	public String pageContent(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		String applyId = condition.getStringCondition("applyId");
		String applyId1 = condition.getStringCondition("applyId1");
		if(applyId == null || "".equals(applyId)){
			applyId = applyId1;
		}
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(applyId)){
			condition.getFilterObject().addCondition("applyId", "=", applyId);
			if(searchKey != null && !"".equals(searchKey)){
				condition.getFilterObject().addOrCondition("activityPlace", "like", "%"+searchKey+"%");
				condition.getFilterObject().addOrCondition("contentDesc", "like", "%"+searchKey+"%");
				condition.getFilterObject().addOrCondition("trainProject", "like", "%"+searchKey+"%");
			}
			p = activityApplyService.queryContents(condition);
		}
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/addOrEditContent")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了主要活动内容编辑页面")
	public String addOrEditContent(String applyId, String businessType, String id, boolean statusEdit, Model model) {
		if(id != null){
			ActivityContent contentInfo = activityApplyService.queryContent(id);
			model.addAttribute("contentInfo",contentInfo);
		}else{
			if(applyId==null){
				throw new AnneException("没有找到需要的申请单");
			}
			ActivityContent contentInfo = new ActivityContent();
			contentInfo.setApplyId(applyId);
			contentInfo.setStatus("未执行");
			model.addAttribute("contentInfo",contentInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		if("activity".equals(businessType)){
			return forward("activity_content_add");
		}else{
			return forward("train_content_add");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditContent", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}提交了主要活动内容数据")
	public String addOrEditContent(ActivityContent activityContent) {
		activityApplyService.addOrEditContent(activityContent,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteContent", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}删除了主要活动内容数据")
	public String deleteContent(String id) {
		activityApplyService.deleteContent(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/makeContent", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}提交了主要活动内容执行数据")
	public String makeContent(@RequestParam(value="ids[]") String[] ids) {
		activityApplyService.makeContent(ids, this.getUserObject().getEmployee().getId(),this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pagePerson")
	@LogOperate(module="市场活动或培训模块",notes="${user}查询了参加活动人员")
	public String pagePerson(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String applyId = condition.getStringCondition("applyId");
		String applyId1 = condition.getStringCondition("applyId1");
		if(applyId == null || "".equals(applyId)){
			applyId = applyId1;
		}
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(applyId)){
			p = activityApplyService.queryPersons(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditPerson")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了参加活动人员编辑页面")
	public String addOrEditPerson(String applyId, String businessType, String internalPerson, String id, boolean statusEdit, Model model) {
		if(id != null){
			ActivityPerson personInfo = activityApplyService.queryPerson(id);
			model.addAttribute("personInfo",personInfo);
			CustomInfo customInfo = customService.getCustomInfo(personInfo.getSellerName());
			model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		}else{
			if(applyId==null){
				throw new AnneException("没有找到需要的申请单");
			}
			ActivityPerson personInfo = new ActivityPerson();
			personInfo.setApplyId(applyId);
			personInfo.setStartDate(new Date());
			personInfo.setInternalPerson(internalPerson);
			model.addAttribute("personInfo",personInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		if("activity".equals(businessType)){
			return forward("activity_person_add");
		}else{
			return forward("train_person_add");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditPerson", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}提交了参加活动人员数据")
	public String addOrEditPerson(ActivityPerson activityPerson) {
		activityApplyService.addOrEditPerson(activityPerson,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deletePerson", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}删除了参加活动人员数据")
	public String deletePerson(String id) {
		activityApplyService.deletePerson(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageExpense")
	@LogOperate(module="市场活动或培训模块",notes="${user}查询了费用信息")
	public String pageExpense(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		String applyId = condition.getStringCondition("applyId");
		String applyId1 = condition.getStringCondition("applyId1");
		if(applyId == null || "".equals(applyId)){
			applyId = applyId1;
		}
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(applyId)){
			condition.getFilterObject().addCondition("applyId", "=", applyId);
			if(searchKey != null && !"".equals(searchKey)){
				condition.getFilterObject().addOrCondition("expenseProject", "like", "%"+searchKey+"%");
				condition.getFilterObject().addOrCondition("organizer", "like", "%"+searchKey+"%");
			}
			p = activityApplyService.queryExpenses(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditExpense")
	@LogOperate(module="市场活动或培训模块",notes="${user}打开了费用信息编辑页面")
	public String addOrEditExpense(String applyId, String businessType, String id, boolean statusEdit, Model model) {
		if(id != null){
			ActivityExpense expenseInfo = activityApplyService.queryExpense(id);
			model.addAttribute("expenseInfo",expenseInfo);
			LovMember org = lovMemberService.get(expenseInfo.getOrganizer());
			model.addAttribute("org", JSON.toJSONString(org));
		}else{
			if(applyId==null){
				throw new AnneException("没有找到需要的申请单");
			}
			ActivityExpense expenseInfo = new ActivityExpense();
			expenseInfo.setApplyId(applyId);
			model.addAttribute("expenseInfo",expenseInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		if("activity".equals(businessType)){
			return forward("activity_expense_add");
		}else{
			return forward("train_expense_add");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditExpense", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}提交了费用信息数据")
	public String addOrEditExpense(ActivityExpense activityExpense) {
		activityApplyService.addOrEditExpense(activityExpense,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteExpense", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}删除费用信息数据")
	public String deleteExpense(String id) {
		activityApplyService.deleteExpense(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/querySummary", method=RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}查询了申请单号执行情况")
	public String querySummary(String applyId) {
		if(applyId != null){
			ActivitySummary summaryInfo = activityApplyService.querySummary(applyId);
			return sendSuccessMessage(summaryInfo);
		}
		return null;
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/submitColumnValue", method = RequestMethod.POST)
	public String submitColumnValue(String contentId,String column,String value) {
		activityApplyService.updateColumnValue(contentId,column,value,this.getUserObject());
		return sendSuccessMessage();
	}
	
	private void addOrEdit(String id, Model model) {
		if(id != null){
			ActivityApply applyInfo = activityApplyService.queryApply(id);
			model.addAttribute("applyInfo",applyInfo);
			LovMember org = lovMemberService.get(applyInfo.getOrganization());
			model.addAttribute("org", JSON.toJSONString(org));
		}else{
			ActivityApply applyInfo = new ActivityApply();
			applyInfo.setApplyCode(serialNumberService.getSerialNumber3("activityApply"));
			applyInfo.setApplier(this.getUserObject().getEmployee().getId());
			applyInfo.setApplierName(this.getUserObject().getEmployee().getName());
			applyInfo.setApplyUnit(this.getUserObject().getOrg().getId());
			applyInfo.setReceivableExpense(new BigDecimal(0));
			applyInfo.setCurrency(applyInfo.getLovMember("CURRENCY", "CNY").getId());
			applyInfo.setStatus(applyInfo.getLovMember(ProcessConstants.ACTIVITY_APPLY_PROC, ProcessConstants.PROCESS_STATUS_01).getId());

			LovMember lmDealer = null;
			if("E".equals(this.getUserObject().getEmployee().getFlag())){
				lmDealer = lovMemberService.getLovMemberByCode("NY", "1");
			}else{
				lmDealer = lovMemberService.getLovMemberByCode("NY", "0");
			}
			applyInfo.setDealer(lmDealer.getId());
			applyInfo.setApplyDate(new Date());
			model.addAttribute("applyInfo",applyInfo);
		}
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);
	}
	
	private void outQueryCondition(Model model){
		List<LovMember> busTypeLst = lovMemberService.getListByGroupCode("BUSTYPE");	//行业类型
		model.addAttribute("busTypeLst",busTypeLst);
		List<LovMember> activityTypeLst = lovMemberService.getListByGroupCode("MKACTYPY");	//市场活动类型
		model.addAttribute("activityTypeLst",activityTypeLst);
	}
}