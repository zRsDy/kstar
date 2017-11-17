package com.ibm.kstar.action.channel.orderforecast;

 
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.channel.IOrderForecastService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.channel.DeliveryForecastDetail;
import com.ibm.kstar.entity.channel.OrderForecast;
import com.ibm.kstar.entity.channel.OrderForecastDetail;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/orderForecast")
public class OrderForecastAction extends BaseAction {
	@Autowired
	IOrderForecastService orderForecastService;
	@Autowired
	IProLineService proLineService;
	@Autowired
	IProductService productService;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	SerialNumberService serialNumberService;
	
	@RequestMapping("/list")
	public String list(Model model) {
		if("E".equals(this.getUserObject().getEmployee().getFlag())){
			model.addAttribute("isDealer","true");
		}else{
			model.addAttribute("isDealer","false");
		}
		return forward("forecast_apply_list");
	}
	
	@RequestMapping("/listGather")
	public String listGather(Model model) {
		if("E".equals(this.getUserObject().getEmployee().getFlag())){
			model.addAttribute("isDealer","true");
		}else{
			model.addAttribute("isDealer","false");
		}
		outQueryCondition(model);
		return forward("forecast_gather_list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = orderForecastService.queryOrderForecasts(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, boolean statusEdit, boolean ableEdit, boolean inFlow, Model model) {
		if(id != null){
			OrderForecast forecastInfo = orderForecastService.queryOrderForecast(id);
			model.addAttribute("forecastInfo",forecastInfo);
		}else{
			OrderForecast forecastInfo = new OrderForecast();
			forecastInfo.setForecastCode(serialNumberService.getSerialNumber4("orderForecast"));
			forecastInfo.setApplier(this.getUserObject().getEmployee().getId());
			forecastInfo.setApplierName(this.getUserObject().getEmployee().getName());
			forecastInfo.setForecastUnit(this.getUserObject().getOrg().getId());
			forecastInfo.setStatus(forecastInfo.getLovMember(ProcessConstants.ORDER_FORECAST_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			forecastInfo.setForecastDate(new Date());
			//TODO:判断是否是经销商的功能尚未完成，因此默认是经销商。
			LovMember lmDealer = null;
			if("E".equals(this.getUserObject().getEmployee().getFlag())){
				lmDealer = forecastInfo.getLovMember("NY", "1");
			}else{
				lmDealer = forecastInfo.getLovMember("NY", "0");
			}
			forecastInfo.setDealer(lmDealer.getId());
			model.addAttribute("forecastInfo",forecastInfo);
		}
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);	//能否编辑
		model.addAttribute("inFlow", inFlow);	//是否正审批
		return forward("forecast_apply_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(OrderForecast orderForecast) {
		orderForecastService.addOrEditForecast(orderForecast,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		orderForecastService.deleteForecast(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id,HttpServletRequest request) {
		orderForecastService.submitForecast(getUserObject(), id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageDetail")
	public String pageDetail(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String forecastId = condition.getStringCondition("forecastId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(forecastId)){
			p = orderForecastService.queryForecastDetails(condition);
		}
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/addOrEditDetail")
	public String addOrEditDetail(String forecastId, String id, boolean statusEdit, Model model) {
		if(id != null){
			OrderForecastDetail detailInfo = orderForecastService.queryForecastDetail(id);
			model.addAttribute("detailInfo",detailInfo);
			CustomInfo customInfo = customService.getCustomInfo(detailInfo.getCustomer());
			model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		}else{
			OrderForecastDetail detailInfo = new OrderForecastDetail();
			detailInfo.setForecastId(forecastId);
			model.addAttribute("detailInfo",detailInfo);
		}
		if("E".equals(this.getUserObject().getEmployee().getFlag())){
			model.addAttribute("isDealer","true");
		}else{
			model.addAttribute("isDealer","false");
		}
		model.addAttribute("statusEdit",statusEdit);
		return forward("forecast_detail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditDetail", method = RequestMethod.POST)
	public String addOrEditDetail(OrderForecastDetail forecastDetail) {
		orderForecastService.addOrEditDetail(forecastDetail,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDetail", method = RequestMethod.POST)
	public String deleteDetail(String id) {
		orderForecastService.deleteForecastDetail(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageDelivery")
	public String pageDelivery(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String orderDetailId = condition.getStringCondition("orderDetailId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(orderDetailId)){
			condition.getFilterObject().addCondition("orderDetailId", "=", orderDetailId);
			p = orderForecastService.queryDeliveryForecastDetails(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/addOrEditDelivery")
	public String addOrEditDelivery(String orderDetailId, String id, boolean statusEdit, Model model) {
		if(id != null){
			DeliveryForecastDetail deliveryInfo = orderForecastService.queryDeliveryForecastDetail(id);
			model.addAttribute("deliveryInfo",deliveryInfo);
		}else{
			if(orderDetailId==null){
				throw new AnneException("请选择下单预测明细！");
			}
			DeliveryForecastDetail deliveryInfo = new DeliveryForecastDetail();
			deliveryInfo.setOrderDetailId(orderDetailId);
			model.addAttribute("deliveryInfo",deliveryInfo);
		}
		model.addAttribute("statusEdit",statusEdit);
		return forward("delivery_detail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEditDelivery", method = RequestMethod.POST)
	public String addOrEditDelivery(DeliveryForecastDetail delivery) {
		orderForecastService.addOrEditDelivery(delivery,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDelivery", method = RequestMethod.POST)
	public String deleteDelivery(String id) {
		orderForecastService.deleteDeliveryForecastDetail(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageGather")
	public String pageGather(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = orderForecastService.queryOrderForecastGathers(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageGatherDetail")
	public String pageGatherDetail(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String forecastWeek = condition.getStringCondition("forecastWeek");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(forecastWeek)){
			p = orderForecastService.queryOrderForecastGatherDetails(condition);
		}
		return sendSuccessMessage(p);
	}
	
	@RequestMapping(value="/exportGather")
	public void exportGather(HttpServletResponse response){
		List<List<Object>> dataList = orderForecastService.exportGather();
		ExcelUtil.exportExcel(response, dataList, "下单预测汇总");
	}
	
	@RequestMapping(value="/exportDetail")
	public void exportDetail(String materielCode,String forecastWeek,HttpServletResponse response){
		if(StringUtil.isEmpty(materielCode)){
			throw new AnneException("请选择下单预测汇总！");
		}
		List<List<Object>> dataList = orderForecastService.exportGatherDetail(materielCode,forecastWeek);
		ExcelUtil.exportExcel(response, dataList, "下单预测明细");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectProSeries")
	public String selectProSeries(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = proLineService.selectProSeries(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectProModel")
	public String selectProModel(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = productService.selectProModel(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectMaterCode")
	public String selectMaterCode(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = productService.selectMaterCode(condition);
		return sendSuccessMessage(p);
	}
	
	private void outQueryCondition(Model model){
		List<LovMember> forecastWeekLst = lovMemberService.getListByGroupCode("FORECASTWEEK");	//预测起始周次
		model.addAttribute("forecastWeekLst",forecastWeekLst);
	}
}