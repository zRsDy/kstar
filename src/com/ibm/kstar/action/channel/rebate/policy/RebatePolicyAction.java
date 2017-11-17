package com.ibm.kstar.action.channel.rebate.policy;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.channel.IRebateClauseService;
import com.ibm.kstar.api.channel.IRebatePolicyService;
import com.ibm.kstar.api.channel.IRebateProductService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.RebateClause;
import com.ibm.kstar.entity.channel.RebatePolicy;
import com.ibm.kstar.entity.channel.RebateProduct;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/rebatePolicy")
public class RebatePolicyAction extends BaseAction {
	
	@Autowired
	IRebatePolicyService rebatePolicyService;
	@Autowired
	IRebateClauseService rebateClauseService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	IRebateProductService rebateProductService ;
	@Autowired
	SerialNumberService serialNumberService;
	
	@RequestMapping("/list")
	public String list() {
		return forward("rebate_policy_list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = rebatePolicyService.queryPolicys(condition);
		return sendSuccessMessage(p);
	}

	@NoRight
	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, boolean statusEdit, boolean ableEdit, Model model) {
		if(id != null){
			RebatePolicy policyInfo = rebatePolicyService.queryPolicy(id);
			model.addAttribute("policyInfo",policyInfo);
			LovMember lovMember = lovMemberService.get(policyInfo.getOrganization());
			model.addAttribute("lovMember", lovMember==null?null : JSON.toJSONString(lovMember));
			CustomInfo customInfo = customService.getCustomInfo(policyInfo.getCustomName());
			model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		}else{
			RebatePolicy policyInfo = new RebatePolicy();
			policyInfo.setPolicyCode(serialNumberService.getSerialNumber3("rebatePolicy"));
			policyInfo.setOrganization(this.getUserObject().getOrg().getId());
			policyInfo.setCurrency(policyInfo.getLovMember("CURRENCY", "CNY").getId());
			policyInfo.setStatus(policyInfo.getLovMember(ProcessConstants.REBATE_POLICY_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			model.addAttribute("policyInfo",policyInfo);
		}
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		return forward("rebate_policy_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(RebatePolicy policy) {
		if(policy.getEndDate().after(new Date())){
			throw new AnneException("结束日期不能大于当前日期！");
		}
		if(policy.getStartDate().after(policy.getEndDate())){
			throw new AnneException("结束日期不能小于开始日期！");
		}
		rebatePolicyService.addOrEdit(policy,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		rebatePolicyService.delete(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id) {
		rebatePolicyService.submit(id,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/compute", method = RequestMethod.POST)
	public String compute(String id) {
		rebatePolicyService.compute(id,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageClause")
	public String pageClause(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String policyId = condition.getStringCondition("policyId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(policyId)){
			p = rebateClauseService.queryClauses(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditClause")
	public String addOrEditClause(String policyId, String id, String typeName, boolean statusEdit, Model model) {
		if(id != null){
			RebateClause clauseInfo = rebateClauseService.queryClause(id);
			model.addAttribute("clauseInfo",clauseInfo);
		}else{
			RebateClause clauseInfo = new RebateClause();
			clauseInfo.setPolicyId(policyId);
			model.addAttribute("clauseInfo",clauseInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		model.addAttribute("typeName", typeName);
		return forward("rebate_clause_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditClause", method = RequestMethod.POST)
	public String addOrEditClause(RebateClause clause) {
		checkNumericalRange(clause.getLeastReturnRate(),0,1,"至少回款率");
		checkNumericalRange(clause.getFinishRate(),0,1,"完成率");
		checkNumericalRange(clause.getRebateRate(),0,100,"返利比例");
		checkNumericalRange(clause.getFinishRate2(),0,1,"完成率2");
		checkNumericalRange(clause.getRebateRate2(),0,100,"返利比例2");
		checkNumericalRange(clause.getRebateRatio(),0,100,"返利系数");
		checkNumericalRange(clause.getExcessRebate(),0,100,"超额返利");
		if((clause.getFinishRate2() == null && clause.getRebateRate2() != null)
			|| (clause.getFinishRate2() != null && clause.getRebateRate2() == null)){
			throw new AnneException("完成率2、返利比例2必须同有同无！");
		}
		if((clause.getYearNoGrowth() == null && clause.getExcessRebate() != null)
			|| (clause.getYearNoGrowth() != null && clause.getExcessRebate() == null)){
			throw new AnneException("同比增长率、超额返利必须同有同无！");
		}
		rebateClauseService.addOrEdit(clause,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteClause", method = RequestMethod.POST)
	public String deleteClause(String id) {
		rebateClauseService.delete(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getProducts")
	public String getProducts() {
		List<RebateProduct> products = rebateProductService.getProducts();
		return sendSuccessMessage(products);
	}
	
	private void checkNumericalRange(BigDecimal b, int min, int max, String warn){
		if(b != null){
			if(b.compareTo(new BigDecimal(max)) > 0 || b.compareTo(new BigDecimal(min)) < 0){
				throw new AnneException(warn+"只能填"+min+"-"+max+"之间的数字！");
			}
		}
	}
}