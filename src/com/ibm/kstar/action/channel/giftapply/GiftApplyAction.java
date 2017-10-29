package com.ibm.kstar.action.channel.giftapply;


import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.ibm.kstar.api.channel.IGiftApplyDetailService;
import com.ibm.kstar.api.channel.IGiftApplyService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.GiftApply;
import com.ibm.kstar.entity.channel.GiftApplyDetail;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/giftApply")
public class GiftApplyAction extends BaseAction {
	
	@Autowired
	IGiftApplyService giftApplyService ;
	@Autowired
	IGiftApplyDetailService giftApplyDetailService ;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	SerialNumberService serialNumberService;
	
	@RequestMapping("/list")
	public String list() {
		return forward("gift_apply_list");
	}
	
	@NoRight
	@RequestMapping("/selectGift")
	public String selectGift(String pickerId,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("select_gift");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = giftApplyService.queryApplys(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping("/setAvailableLimit")
	public String setAvailableLimit(String currencyId, Date applyDate) {
		BigDecimal availableLimit = giftApplyService.getAvailableLimit(currencyId,applyDate);
		ModelMap map = new ModelMap();
		map.put("availableLimit", availableLimit);
		return sendSuccessMessage(map);
	}

	@NoRight
	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, boolean statusEdit, boolean ableEdit, Model model) {
		if(id != null){
			GiftApply applyInfo = giftApplyService.queryApply(id);
			model.addAttribute("applyInfo",applyInfo);
			CustomInfo customInfo = customService.getCustomInfo(applyInfo.getCustom());
			model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		}else{
			GiftApply applyInfo = new GiftApply();
			applyInfo.setApplyCode(serialNumberService.getSerialNumber3("giftApply"));
			applyInfo.setApplier(this.getUserObject().getEmployee().getId());
			applyInfo.setApplierName(this.getUserObject().getEmployee().getName());
			applyInfo.setApplyUnit(this.getUserObject().getOrg().getId());
			applyInfo.setApplyAmount(new BigDecimal(0));
			applyInfo.setCurrency(applyInfo.getLovMember("CURRENCY", "CNY").getId());
			LovMember lmDealer = null;
			if("E".equals(this.getUserObject().getEmployee().getFlag())){
				lmDealer = lovMemberService.getLovMemberByCode("NY", "1");
			}else{
				lmDealer = lovMemberService.getLovMemberByCode("NY", "0");
			}
			applyInfo.setDealer(lmDealer.getId());
			applyInfo.setStatus(applyInfo.getLovMember(ProcessConstants.GIFT_APPLY_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			applyInfo.setApplyDate(new Date());
			applyInfo.setAvailableLimit(giftApplyService.getAvailableLimit(applyInfo.getCurrency(),applyInfo.getApplyDate()));
			model.addAttribute("applyInfo",applyInfo);
		}
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);

		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		return forward("gift_apply_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(GiftApply giftApply) {
		if(giftApply.getId() == null && giftApply.getEstimatedDemandDate() != null && (new Date()).getTime()-giftApply.getEstimatedDemandDate().getTime()>24*60*60*1000){
			throw new AnneException("预计需求日期不能小于当前日期！");
		}
		giftApplyService.addOrEdit(giftApply,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		giftApplyService.delete(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id) {
		giftApplyService.submit(id,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageDetail")
	public String pageDetail(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String applyId = condition.getStringCondition("applyId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(applyId)){
			condition.getFilterObject().addCondition("applyId", "=", applyId);
			p = giftApplyDetailService.queryDetails(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditDetail")
	public String addOrEditDetail(String applyId, String id, boolean statusEdit, Model model) {
		if(id != null){
			GiftApplyDetail detailInfo = giftApplyDetailService.queryDetail(id);
			model.addAttribute("detailInfo",detailInfo);
		}else{
			GiftApplyDetail detailInfo = new GiftApplyDetail();
			detailInfo.setApplyId(applyId);
			model.addAttribute("detailInfo",detailInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		return forward("gift_apply_detail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditDetail", method = RequestMethod.POST)
	public String addOrEditDetail(GiftApplyDetail detail) {
		giftApplyDetailService.addOrEdit(detail,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDetails", method = RequestMethod.POST)
	public String deleteDetails(@RequestParam(value = "ids[]") String[] ids) {
		giftApplyDetailService.delete(ids);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/giveDetails", method = RequestMethod.POST)
	public String giveDetail(@RequestParam(value = "ids[]") String[] ids) {
		giftApplyDetailService.giveDetails(ids,this.getUserObject());
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/getDetails", method = RequestMethod.POST)
	public String getDetails(@RequestParam(value = "ids[]") String[] ids) {
		giftApplyDetailService.getDetails(ids,this.getUserObject());
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/submitColumnValue", method = RequestMethod.POST)
	public String submitColumnValue(String detailId,String column,String value) {
		giftApplyDetailService.updateColumnValue(detailId,column,value,this.getUserObject());
		return sendSuccessMessage();
	}
}