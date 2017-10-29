package com.ibm.kstar.action.channel.gift;


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
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.channel.IGiftService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.GiftManage;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/gift")
public class GiftAction extends BaseAction {
	@Autowired
	IGiftService giftService ;
	@Autowired
	ILovMemberService lovMemberService;
	
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		outQueryCondition(model);
		return forward("gift_list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = giftService.queryGifts(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, Model model) {
		if(id != null){
			GiftManage giftInfo = giftService.queryGift(id);
			model.addAttribute("giftInfo",giftInfo);
		}else{
			GiftManage giftInfo = new GiftManage();
			giftInfo.setManager(this.getUserObject().getEmployee().getName());
			giftInfo.setStartDate(new Date());
			giftInfo.setCurrency(giftInfo.getLovMember("CURRENCY", "CNY").getId());
			model.addAttribute("giftInfo",giftInfo);
		}
		return forward("gift_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(GiftManage giftManage)  {
		if(giftManage.getId() == null && (new Date()).getTime()-giftManage.getStartDate().getTime()>24*60*60*1000){
			throw new AnneException("生效日期不能小于当前日期！");
		}
		if(giftManage.getEndDate() != null){
			if(giftManage.getEndDate().before(giftManage.getStartDate())){
				throw new AnneException("失效日期不能小于生效日期！");
			}
		}
		giftService.saveOrUpdateGift(giftManage,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		GiftManage giftInfo = giftService.queryGift(id);
		giftService.delete(giftInfo);
		return sendSuccessMessage();
	}
	
	private void outQueryCondition(Model model){
		List<LovMember> giftTypeLst = lovMemberService.getListByGroupCode("GIFTTYPE");	//行业类型
		model.addAttribute("giftTypeLst",giftTypeLst);
	}
}