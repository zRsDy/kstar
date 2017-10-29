package com.ibm.kstar.action.channel.rebate.product;


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
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.channel.IRebateProductDetailService;
import com.ibm.kstar.api.channel.IRebateProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.RebateProduct;
import com.ibm.kstar.entity.channel.RebateProductDetail;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/rebateProduct")
public class RebateProductAction extends BaseAction {
	
	@Autowired
	IRebateProductService rebateProductService ;
	
	@Autowired
	IRebateProductDetailService rebateProductDetailService ;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@RequestMapping("/list")
	public String list() {
		return forward("rebate_product_list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String groupName = condition.getStringCondition("groupName");
		if(StringUtil.isNotEmpty(groupName)){
			condition.getFilterObject().addOrCondition("groupName", "like", "%"+groupName+"%");
		}
		IPage p = rebateProductService.queryProducts(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, Model model) {
		if(id != null){
			RebateProduct productInfo = rebateProductService.queryProduct(id);
			model.addAttribute("productInfo",productInfo);
			LovMember lovMember = lovMemberService.get(productInfo.getOrganization());
			model.addAttribute("lovMember", lovMember==null?null : JSON.toJSONString(lovMember));
		}else{
			RebateProduct productInfo = new RebateProduct();
			productInfo.setCreater(this.getUserObject().getEmployee().getId());
			productInfo.setCreaterName(this.getUserObject().getEmployee().getName());
			productInfo.setOrganization(this.getUserObject().getOrg().getId());
			productInfo.setCreateDate(new Date());
			productInfo.setStartDate(new Date());
			model.addAttribute("productInfo",productInfo);
		}
		return forward("rebate_product_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(RebateProduct product) {
		rebateProductService.addOrEdit(product,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		rebateProductService.delete(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageDetail")
	public String pageDetail(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String productGroupId = condition.getStringCondition("productGroupId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(productGroupId)){
			condition.getFilterObject().addCondition("productGroupId", "=", productGroupId);
			String productSeries = condition.getStringCondition("productSeries");
			if(StringUtil.isNotEmpty(productSeries)){
				condition.getFilterObject().addOrCondition("productSeries", "like", "%"+productSeries+"%");
			}
			p = rebateProductDetailService.queryDetails(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditDetail")
	public String addOrEditDetail(String policyCode, String id, Model model) {
		if(id != null){
			 RebateProductDetail detailInfo = rebateProductDetailService.queryDetail(id);
			model.addAttribute("detailInfo",detailInfo);
		}
		return forward("rebate_product_detail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditDetail", method = RequestMethod.POST)
	public String addOrEditDetail(RebateProductDetail detail) {
		rebateProductDetailService.addOrEdit(detail,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDetail", method = RequestMethod.POST)
	public String deleteDetail(String id) {
		rebateProductDetailService.delete(id);
		return sendSuccessMessage();
	}
}