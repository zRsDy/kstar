package com.ibm.kstar.action.order.IntProdSerie;


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
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.order.IIntProdSerieService;
import com.ibm.kstar.entity.order.IntProdSerie;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/intProdSerie")
public class IntProdSerieAction extends BaseAction {
	@Autowired
	IIntProdSerieService intProdSerieService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("seriesName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("receiveNumber", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("itemMode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("itemNumber", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("itemDesc", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("shipToCustomer", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("externalShipmentNumber", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("intMessage", "like", "%"+searchKey+"%");
		}
		
		String seriesName = condition.getStringCondition("seriesName");
		if(StringUtil.isNotEmpty(seriesName)){
			condition.getFilterObject().addCondition("seriesName", "like", "%"+seriesName+"%");
		}
		String receiveNumber = condition.getStringCondition("receiveNumber");
		if(StringUtil.isNotEmpty(receiveNumber)){
			condition.getFilterObject().addCondition("receiveNumber", "like", "%"+receiveNumber+"%");
		}
		String itemMode = condition.getStringCondition("itemMode");
		if(StringUtil.isNotEmpty(itemMode)){
			condition.getFilterObject().addCondition("itemMode", "like", "%"+itemMode+"%");
		}
		String itemNumber = condition.getStringCondition("itemNumber");
		if(StringUtil.isNotEmpty(itemNumber)){
			condition.getFilterObject().addCondition("itemNumber", "like", "%"+itemNumber+"%");
		}
		String shipToCustomer = condition.getStringCondition("shipToCustomer");
		if(StringUtil.isNotEmpty(shipToCustomer)){
			condition.getFilterObject().addCondition("shipToCustomer", "like", "%"+shipToCustomer+"%");
		}
		String externalShipmentNumber = condition.getStringCondition("externalShipmentNumber");
		if(StringUtil.isNotEmpty(externalShipmentNumber)){
			condition.getFilterObject().addCondition("externalShipmentNumber", "like", "%"+externalShipmentNumber+"%");
		}
		String orderNumber = condition.getStringCondition("orderNumber");
		if(StringUtil.isNotEmpty(orderNumber)){
			condition.getFilterObject().addCondition("orderNumber", "=", orderNumber);
		}
		String anzhuangCompletedDateStart = condition.getStringCondition("anzhuangCompletedDateStart");
		if(StringUtil.isNotEmpty(anzhuangCompletedDateStart)){
			condition.getFilterObject().addCondition("anzhuangCompletedDate", ">=", anzhuangCompletedDateStart);
		}
		String anzhuangCompletedDateEnd = condition.getStringCondition("anzhuangCompletedDateEnd");
		if(StringUtil.isNotEmpty(anzhuangCompletedDateEnd)){
			condition.getFilterObject().addCondition("anzhuangCompletedDate", "<=", anzhuangCompletedDateEnd);
		}
		String receivePrtDateStart = condition.getStringCondition("receivePrtDateStart");
		if(StringUtil.isNotEmpty(receivePrtDateStart)){
			condition.getFilterObject().addCondition("receivePrtDate", ">=", receivePrtDateStart);
		}
		String receivePrtDateEnd = condition.getStringCondition("receivePrtDateEnd");
		if(StringUtil.isNotEmpty(receivePrtDateEnd)){
			condition.getFilterObject().addCondition("receivePrtDate", "<=", receivePrtDateEnd);
		}
		String creationDateStart = condition.getStringCondition("creationDateStart");
		if(StringUtil.isNotEmpty(creationDateStart)){
			condition.getFilterObject().addCondition("creationDate", ">=", creationDateStart);
		}
		String creationDateEnd = condition.getStringCondition("creationDateEnd");
		if(StringUtil.isNotEmpty(creationDateEnd)){
			condition.getFilterObject().addCondition("creationDate", "<=", creationDateEnd);
		}
		
		IPage p = intProdSerieService.queryIntProdSeries(condition,getUserObject());
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("intProdSerie_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		intProdSerieService.deleteIntProdSerie(id);
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return forward("intProdSerie_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(IntProdSerie intProdSerie) {
		intProdSerieService.saveIntProdSerie(intProdSerie);
		return sendSuccessMessage();
	}

	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		IntProdSerie intProdSerie = intProdSerieService.getIntProdSerie(id);
		if (intProdSerie == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("intProdSerie", intProdSerie);
		return forward("intProdSerie_info");
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(IntProdSerie intProdSerie) {
		intProdSerieService.updateIntProdSerie(intProdSerie);
		return sendSuccessMessage();
	}
	
	@NoRight
	@RequestMapping(value = "/exportIntProdSerie")
	public void exportIntProdSerieLists(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(","); 
		List<List<Object>> dataList = intProdSerieService.exportSelectedContrLists(ids);
		ExcelUtil.exportExcel(response, dataList, "产品序列号");
	}
}