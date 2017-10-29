package com.ibm.kstar.action.price.batch;


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
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.price.IBatchProductPriceService;
import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.entity.price.BatchProductPrice;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/batchProductPrice")
public class BatchProductPriceAction extends BaseAction {
	
	@Autowired
	IBatchProductPriceService batchProductPriceService ;
	
	@Autowired
	IProLineService lineService;
	
	@RequestMapping("/list")
	public String list() {
		return forward("batch_product_price_list");
	}
	
	@NoRight
	@RequestMapping("/selectPrice")
	public String selectPrice(String pickerId,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("select_price_table");
	}
	
	@NoRight
	@RequestMapping("/selectProCategory")
	public String selectProCategory(String pickerId,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("select_product_category");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = batchProductPriceService.queryBatchProductPrices(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, boolean statusEdit, Model model) {
		if(id != null){
			BatchProductPrice batchInfo = batchProductPriceService.query(id);
			model.addAttribute("batchInfo",batchInfo);
		}else{
			BatchProductPrice batchInfo = new BatchProductPrice();
			batchInfo.setCreateOrg(this.getUserObject().getOrg().getId());
			batchInfo.setCreater(this.getUserObject().getEmployee().getId());
			batchInfo.setCreaterName(this.getUserObject().getEmployee().getName());
			batchInfo.setCreateDate(new Date());
			batchInfo.setAdjustStatus("新建");
			batchInfo.setAdjustType(batchInfo.getLovMember("PRICEADJUST", "01").getId());
			model.addAttribute("batchInfo",batchInfo); 
		}
		model.addAttribute("statusEdit",statusEdit);
		return forward("batch_product_price_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(BatchProductPrice batchProductPrice) {
		if(batchProductPrice.getId() != null){
			BatchProductPrice batchData = batchProductPriceService.queryBatchProductPrice(batchProductPrice.getId());
			BeanUtils.copyPropertiesIgnoreNull(batchProductPrice,batchData);
			batchProductPriceService.saveOrUpdateBatchProductPrice(batchData,this.getUserObject());
		}else{
			batchProductPrice.setAdjustStatus("新建");
			batchProductPriceService.saveOrUpdateBatchProductPrice(batchProductPrice,this.getUserObject());
		}
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		BatchProductPrice batchInfo = batchProductPriceService.queryBatchProductPrice(id);
		batchProductPriceService.deleteBatchProductPrice(batchInfo);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id) {
		batchProductPriceService.submitBatchProductPrice(this.getUserObject(),id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/proCategoryPage")
	public String proCategoryPage(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("cproCategory", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("cproType", "like", "%"+searchKey+"%");
		}
		IPage p = lineService.query(condition);
		return sendSuccessMessage(p);
	}
}