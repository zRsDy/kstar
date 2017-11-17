package com.ibm.kstar.action.bizopp.bid.auth;

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
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.Bid;
import com.ibm.kstar.entity.bizopp.BidAuthUnit;
import com.ibm.kstar.interceptor.system.permission.NoRight;


@Controller
@RequestMapping("/bid/auth")
public class BizBidAuthAction extends BaseAction {

	@Autowired
	IBizoppService service;
	
	@Autowired
	IBizBaseService bizService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位一览")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bidId = condition.getStringCondition("bidId");
		if (!StringUtils.isEmpty(bidId)) {
			condition.getFilterObject().addCondition("bidId", "=", bidId);
		}
		IPage p = service.queryBidAuthUnit(condition);
		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位新增")
	public String add(String bidId, Model model){
		BidAuthUnit entity = new BidAuthUnit();
		Bid bid = bizService.getEntity(bidId, new Bid());
		entity.setBidId(bidId);
		entity.setBizOppId(bid.getBizOppId());
		model.addAttribute("entity", entity);
		return forward("add");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位新增")
	public String add(BidAuthUnit bidAuthUnit) {
		service.saveBidAuthUnit(bidAuthUnit, getUserObject());
		return sendSuccessMessage(bidAuthUnit.getId());
	}
	
	@NoRight
	@RequestMapping("/edit")
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位修改")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		BidAuthUnit entity = service.getBidAuthUnit(id);
		
		model.addAttribute("entity",entity);
		return forward("add");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位修改")
	public String edit(BidAuthUnit bidAuthUnit){

		service.updateBidAuthUnit(bidAuthUnit, getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位删除")
	public String delete(String id){
		service.deleteBidAuthUnit(id);
		return sendSuccessMessage();
	}
}
