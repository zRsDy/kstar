package com.ibm.kstar.action.channel.serviceapply;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.channel.IServiceApplyService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.ApplyEquipment;
import com.ibm.kstar.entity.channel.ServiceApply;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/serviceApply")
public class ServiceApplyAction extends BaseAction {
	@Autowired
	IServiceApplyService serviceApplyService ;
	@Autowired
	IProLineService proLineService;
	@Autowired
	IProductService productService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IBizoppService bizoppService;
	@Autowired
	IContractService contractService;
	@Autowired
	SerialNumberService serialNumberService;
	
	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());
		return forward("service_apply_list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = serviceApplyService.queryServiceApplys(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}

	@NoRight
	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, boolean statusEdit, boolean ableEdit, Model model) {
		if(id != null){
			ServiceApply applyInfo = serviceApplyService.queryServiceApply(id);
			Map<String, Object> bizOpp = bizoppService.getBizOppEntityByNumber(applyInfo.getBusiChanceCode());
			Contract contract = contractService.getContractByNo(applyInfo.getContractCode());
			if(bizOpp != null){
				model.addAttribute("bizOpp",JSON.toJSONString(bizOpp));
			}
			if(contract != null){
				model.addAttribute("contract",JSON.toJSONString(contract));
			}
			model.addAttribute("applyInfo",applyInfo);
		}else{
			ServiceApply applyInfo = new ServiceApply();
			applyInfo.setApplyCode(serialNumberService.getSerialNumber3("serviceApply"));
			applyInfo.setApplier(this.getUserObject().getEmployee().getId());
			applyInfo.setApplierName(this.getUserObject().getEmployee().getName());
			applyInfo.setApplyUnit(this.getUserObject().getOrg().getId());
			applyInfo.setApplyDate(new Date());
			applyInfo.setStatus(applyInfo.getLovMember(ProcessConstants.SERVICE_APPLY_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			applyInfo.setCurrency(applyInfo.getLovMember("CURRENCY", "CNY").getId());
			//TODO:判断是否是经销商的功能尚未完成，因此默认是经销商。
			LovMember lmDealer = null;
			if("E".equals(this.getUserObject().getEmployee().getFlag())){
				lmDealer = applyInfo.getLovMember("NY", "1");
			}else{
				lmDealer = applyInfo.getLovMember("NY", "0");
			}
			applyInfo.setDealer(lmDealer.getId());
			model.addAttribute("applyInfo",applyInfo);
		}
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("isDealer","E".equals(this.getUserObject().getEmployee().getFlag()));
		return forward("service_apply_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(ServiceApply serviceApply) {
		if("01".equals(serviceApply.getServiceTypeCode()) && StringUtil.isEmpty(serviceApply.getBusiChanceCode())){
			throw new AnneException("服务类型为现场勘察时，商机编号必填！");
		}
		serviceApplyService.saveOrUpdateServiceApply(serviceApply,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		ServiceApply applyInfo = serviceApplyService.queryServiceApply(id);
		serviceApplyService.deleteServiceApply(applyInfo);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id) {
		serviceApplyService.submit(id, this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageEquip")
	public String pageEquip(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String applyId = condition.getStringCondition("applyId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(applyId)){
			condition.getFilterObject().addCondition("applyId", "=", applyId);
			p = serviceApplyService.queryApplyEquipments(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditEquip")
	public String addOrEditEquip(String applyId, String id, String serviceTypeName, boolean statusEdit, Model model) {
		if(id != null){
			 ApplyEquipment equipInfo = serviceApplyService.queryApplyEquipment(id);
			model.addAttribute("equipInfo",equipInfo);
		}else{
			ApplyEquipment equipInfo = new ApplyEquipment();
			equipInfo.setApplyId(applyId);
			model.addAttribute("equipInfo",equipInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		model.addAttribute("serviceTypeName",serviceTypeName);
		return forward("service_apply_detail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditEquip", method = RequestMethod.POST)
	public String addOrEditEquip(ApplyEquipment equipInfo) {
		serviceApplyService.saveOrUpdateApplyEquipment(equipInfo,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteEquip", method = RequestMethod.POST)
	public String deleteEquip(String id) {
		ApplyEquipment equipInfo = serviceApplyService.queryApplyEquipment(id);
		serviceApplyService.deleteApplyEquipment(equipInfo);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectMaterCode")
	public String selectMaterCode(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = productService.selectMaterCode(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectProLines")
	public String selectProLines(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = proLineService.selectProLines();
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectProModel")
	public String selectProModel(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = productService.selectProModel(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectProBrand")
	public String selectProBrand(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = productService.selectProBrand(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectBizOpp")
	public String selectBizOpp(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<Map<String, Object>> p = bizoppService.selectBizOpp(condition, this.getUserObject());
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectContract")
	public String selectContract(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<Contract> p = contractService.selectContract(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@RequestMapping("/selectContact")
	public String selectContact(String pickerId, String customId, Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("customId",customId);
		return forward("select_contact");
	}
}