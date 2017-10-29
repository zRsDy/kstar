package com.ibm.kstar.action.order.delivery.Logistics;

import java.text.SimpleDateFormat;
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
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IDeliveryReceiptService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.DeliveryReceipt;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/logistics")
public class LogisticsAction extends BaseAction {
	@Autowired
	IDeliveryReceiptService deliveryReceiptService ;
	@Autowired 
	BaseDao baseDao ;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		UserObject userObject = getUserObject();
		
		deliveryReceiptService.setSearchCondition(condition, userObject);
		
		IPage p = deliveryReceiptService.queryDeliveryReceipts(condition,userObject);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("receipt_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		deliveryReceiptService.deleteDeliveryReceipt(id);
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return forward("receipt_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeliveryReceipt deliveryReceipt) {
		deliveryReceiptService.saveDeliveryReceipt(deliveryReceipt,getUserObject());
		return sendSuccessMessage();
	}

	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		UserObject userObject = getUserObject();
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		DeliveryReceipt deliveryReceipt = deliveryReceiptService
				.getDeliveryReceipt(id);
		if (deliveryReceipt == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		String employeeType = "" ;
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
		}
		model.addAttribute("employeeType", employeeType);
		model.addAttribute("deliveryReceipt", deliveryReceipt);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		if(this.hasPermission("P06SignT1DetailPage")){
			tabMain.addTab("签收单明细行", "/logistics/deliveryReceiptLinesList.html?receiptCode="
					+deliveryReceipt.getReceiptCode());
		}
		if(this.hasPermission("P06SignT2FilePage")){
			String unableAdd = "true";
			String unableDelete = "true";
			if(this.hasPermission("P06SignT2FileSave")){
				unableAdd = "false";
			}
			if(this.hasPermission("P06SignT2FileDelete")){
				unableDelete = "false";
			}
			tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+deliveryReceipt.getId() 
				+ "&businessType=DELIVERY_RECEIPT&docGroupCode=FileTypeCode&unableAdd="+unableAdd+"&unableDelete="+unableDelete);
		}
		model.addAttribute("tabMain", tabMain);
		return forward("receipt_info");
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeliveryReceipt deliveryReceipt) throws Exception {
		deliveryReceiptService.updateDeliveryReceipt(deliveryReceipt,getUserObject());
		return sendSuccessMessage();
	}
	
	@RequestMapping("/deliveryReceiptLinesList")
	public String deliveryReceiptLines(String receiptCode, Model model) {
		UserObject userObject = getUserObject();
		String employeeType = "" ;
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
		}
		model.addAttribute("employeeType", employeeType);
		
		model.addAttribute("receiptCode", receiptCode);
		return forward("receipt_lines");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/queryDeliveryReceiptLines")
	public String queryDeliveryReceiptLines(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(StringUtil.isNotEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("orderCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("deliveryCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("logisticsNo", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("consignee", "like", "%"+searchKey+"%");
		}
		String receiptCode = condition.getStringCondition("receiptCode");
		condition.getFilterObject().addCondition("receiptCode", "eq", receiptCode);
		condition.getFilterObject().addCondition("deleteFlag", "!=", IConstants.YES);
		
		IPage p = deliveryReceiptService.queryDeliveryReceipts(condition,getUserObject());
		return sendSuccessMessage(p);
	}
	
	/**
	 * 
	 * exportOrder:导出订单. <br/> 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@RequestMapping("/exportReceiptList")
	public void exportReceiptList(HttpServletRequest request,HttpServletResponse response){
		UserObject userObject = getUserObject();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			PageCondition condition = new PageCondition();
			ActionUtil.requestToCondition(condition, request);
			//deliveryReceiptService.setSearchCondition(condition, userObject);
			String id = condition.getStringCondition("id");
			DeliveryReceipt deliveryReceipt = baseDao.get(DeliveryReceipt.class, id);
			if(deliveryReceipt != null){
				condition.getFilterObject().addCondition("receiptCode", "eq", deliveryReceipt.getReceiptCode());
				condition.getFilterObject().addCondition("deleteFlag", "!=", IConstants.YES);
				List<DeliveryReceipt> deliveryReceipts = deliveryReceiptService.getDeliveryReceipts(condition, userObject);
				List<List<Object>> dataList  = deliveryReceiptService.getExcelData(deliveryReceipts);
				ExcelUtil.exportExcel(response, dataList, "CRM签收单列表导出 _"+sdf.format(new Date()));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new AnneException("导出失败");
		}
	}

}