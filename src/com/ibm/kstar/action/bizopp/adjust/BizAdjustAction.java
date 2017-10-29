package com.ibm.kstar.action.bizopp.adjust;

import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.bizopp.InternationWeekly;
import com.ibm.kstar.interceptor.system.permission.NoRight;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/bizadjust")
public class BizAdjustAction extends BaseAction {

	@Autowired 
	IBizoppService bizoppService;

	@Autowired
	ProcessStatusService processStatusService;

	@RequestMapping("/list")
	public String index(Model model) {
		return forward("list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("opportunityName", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("number", "like", "%" + searchKey + "%");
		}

		ActionUtil.doSearch(condition);
		IPage p = bizoppService.query(condition, getUserObject());
		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/pageConflict")
	public String pageConflict(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizId = request.getParameter("bizId");
		BusinessOpportunity businessOpportunity = bizoppService.getBizOppEntity(bizId);

		IPage p = null;
		if (StringUtil.isEmpty(bizId)) {
			p = new PageImpl(null, 1, 20, 1);
			return sendSuccessMessage(p);
		}
		p = bizoppService.queryConflictBizOpp(condition, bizId,businessOpportunity.getIndustry(),businessOpportunity.getIndustrySub());
		return sendSuccessMessage(p );
	}

	@RequestMapping("/add")
	public String add(Model model){
		String userId = getUserObject().getEmployee().getId();
		String userName = getUserObject().getEmployee().getName();
		InternationWeekly entity = new InternationWeekly();
		entity.setSalesid(userId);
		entity.setSalesman(userName);
		model.addAttribute("entity", entity);
		
		return forward("weeklyAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(InternationWeekly entity, HttpServletRequest request) {

		bizoppService.saveWeekly(entity);
		return sendSuccessMessage(entity.getId());
	}

	@ResponseBody
	@RequestMapping(value = "/approved", method = RequestMethod.POST)
	public String approved(String id,String remark) {
		BusinessOpportunity businessOpportunity = bizoppService.getBizOppEntity(id);
		if(!"15".equals(businessOpportunity.getStatus())){
			throw new AnneException("区域已审核状态的商机才可进行批准操作！可通过查询条件对商机列表进行筛选后操作。");
		}
		//启动流程
		bizoppService.startApprovedProcess(businessOpportunity, getUserObject(),remark);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/rejected", method = RequestMethod.POST)
	public String rejected(String id) {
		BusinessOpportunity businessOpportunity = bizoppService.getBizOppEntity(id);
		if(!"15".equals(businessOpportunity.getStatus())){
			throw new AnneException("区域已审核状态的商机才可进行驳回操作！可通过查询条件对商机列表进行筛选后操作。");
		}
		processStatusService.updateProcessStatus("BusinessOpportunity", id, "status", "30");
		processStatusService.updateProcessStatus("BusinessOpportunity", id, "conflictStatus", "20");
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public String publish(String id,
						  String bizOpp1_,
						  String bizOpp2_,
						  String bizOpp3_,
						  String bizOpp4_,
						  String bizOpp5_,
						  String bizOpp6_,
						  String bizOpp7_,
						  String bizOpp8_,
						  String bizOpp9_,
						  String bizOpp10_,
						  String bizOpp11_,
						  String bizOpp12_,
						  String bizOpp13_,
						  String bizOpp14_,
						  String bizOpp15_,
						  String bizOpp16_,
						  String bizOpp17_,
						  String bizOpp18_,
						  String bizOpp19_,
						  String bizOpp20_
	) {
		// 发布冲突生成冲突编号
		String conflictNumber = "";
		boolean isExist = false;
		
		if (StringUtil.isNotEmpty(bizOpp1_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp1_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp2_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp2_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp3_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp3_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp4_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp4_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp5_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp5_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp6_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp6_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp7_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp7_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp8_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp8_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp9_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp9_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp10_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp10_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp11_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp11_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp12_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp12_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp13_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp13_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp14_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp14_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp15_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp15_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp16_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp16_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp17_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp17_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp18_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp18_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp19_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp19_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if (StringUtil.isNotEmpty(bizOpp20_) && !isExist) {
			BusinessOpportunity entity = bizoppService.getBizOppEntity(bizOpp20_);
			if (!StringUtils.isEmpty(entity.getConflictNumber())) {
				isExist = true;
				conflictNumber = entity.getConflictNumber();
			}
		}
		
		if(!isExist) {
			conflictNumber = bizoppService.getBizOppConflictNumber();
		}
		//-------------------------------------------------------------------------------------

		if (StringUtil.isNotEmpty(bizOpp1_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp1_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp1_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp2_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp2_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp2_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp3_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp3_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp3_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp4_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp4_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp4_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp5_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp5_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp5_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp6_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp6_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp6_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp7_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp7_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp7_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp8_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp8_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp8_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp9_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp9_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp9_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp10_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp10_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp10_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp11_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp11_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp11_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp12_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp12_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp12_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp13_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp13_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp13_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp14_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp14_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp14_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp15_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp15_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp15_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp16_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp16_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp16_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp17_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp17_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp17_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp18_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp18_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp18_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp19_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp19_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp19_, "conflictNumber", conflictNumber);
		}
		if (StringUtil.isNotEmpty(bizOpp20_)) {
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp20_, "conflictStatus", "30");
			processStatusService.updateProcessStatus("BusinessOpportunity", bizOpp20_, "conflictNumber", conflictNumber);
		}

		return sendSuccessMessage();
	}
}
