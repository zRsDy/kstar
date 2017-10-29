package com.ibm.kstar.action.order.invoice;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.NumberToCN;
import org.xsnake.web.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IInvoiceService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.InvoiceDetail;
import com.ibm.kstar.entity.order.InvoiceGoldenTax;
import com.ibm.kstar.entity.order.InvoiceMaster;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/invoice")
public class InvoiceAction extends BaseAction {
	@Autowired
	IInvoiceService invoiceService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IOrderService orderService;
	
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("invoiceCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proposerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("expressNo", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("consignee", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("consigneeTel", "like", "%"+searchKey+"%");
		}
		
		String invoiceCode = condition.getStringCondition("invoiceCode");
		if(StringUtil.isNotEmpty(invoiceCode)){
			condition.getFilterObject().addCondition("invoiceCode", "like", "%"+invoiceCode+"%");
		}
		String proposerName = condition.getStringCondition("proposerName");
		if(StringUtil.isNotEmpty(proposerName)){
			condition.getFilterObject().addCondition("proposerName", "like", "%"+proposerName+"%");
		}
		String consignee = condition.getStringCondition("consignee");
		if(StringUtil.isNotEmpty(consignee)){
			condition.getFilterObject().addCondition("consignee", "like", "%"+consignee+"%");
		}
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "eq", status);
		}
		String customerId = condition.getStringCondition("customerId");
		if(StringUtil.isNotEmpty(customerId)){
			condition.getFilterObject().addCondition("customerId", "eq", customerId);
		}
		String consigneeTel = condition.getStringCondition("consigneeTel");
		if(StringUtil.isNotEmpty(consigneeTel)){
			condition.getFilterObject().addCondition("consigneeTel", "like", "%"+consigneeTel+"%");
		}
		String invoiceType = condition.getStringCondition("invoiceType");
		if(StringUtil.isNotEmpty(invoiceType)){
			condition.getFilterObject().addCondition("invoiceType", "eq", invoiceType);
		}
		String expressNo = condition.getStringCondition("expressNo");
		if(StringUtil.isNotEmpty(expressNo)){
			condition.getFilterObject().addCondition("expressNo", "like",  "%"+expressNo+"%");
		}
		String taxNo = condition.getStringCondition("taxNo");
		if(StringUtil.isNotEmpty(taxNo)){
			condition.getFilterObject().addCondition("taxNo", "like",  "%"+taxNo+"%");
		}
		String contractCode = condition.getStringCondition("contractCode");
		if(StringUtil.isNotEmpty(contractCode)){
			condition.getFilterObject().addCondition("contractCode", "like",  "%"+contractCode+"%");
		}
		String applyDateStart = condition.getStringCondition("applyDateStart");
		if(StringUtil.isNotEmpty(applyDateStart)){
			condition.getFilterObject().addCondition("applyDate", ">=",  applyDateStart);
		}
		String applyDateEnd = condition.getStringCondition("applyDateEnd");
		if(StringUtil.isNotEmpty(applyDateEnd)){
			condition.getFilterObject().addCondition("applyDate", "<=",  applyDateEnd);
		}
		String invoiceDateStart = condition.getStringCondition("invoiceDateStart");
		if(StringUtil.isNotEmpty(invoiceDateStart)){
			condition.getFilterObject().addCondition("invoiceDate", ">=",  invoiceDateStart);
		}
		String invoiceDateEnd = condition.getStringCondition("invoiceDateEnd");
		if(StringUtil.isNotEmpty(invoiceDateEnd)){
			condition.getFilterObject().addCondition("invoiceDate", "<=",  invoiceDateEnd);
		}
		String businessEntity = condition.getStringCondition("businessEntity");
		if(StringUtil.isNotEmpty(businessEntity)){
			condition.getFilterObject().addCondition("businessEntity", "=", businessEntity);
		}
		IPage p = invoiceService.queryInvoiceMasters(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("invoice_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) throws Exception {
		//删除发票头和行表
		invoiceService.deleteInvoice(id,getUserObject());
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		UserObject userObject = getUserObject();
		InvoiceMaster invoiceMaster =  new  InvoiceMaster(); 
		
		UserObject user = getUserObject();
		LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "INVOICE");
		String code = "";
		String prefix = "KST-KP-";
		if(lov != null){
			prefix = lov.getName();
		}
		code = orderService.getSequenceCode("gen_invoice_code",prefix);
		invoiceMaster.setInvoiceCode(code);
		invoiceMaster.setInvoiceType("02");
		invoiceMaster.setProposerId( user.getEmployee().getId());
		invoiceMaster.setProposerName(user.getEmployee().getNo() + "|" + user.getEmployee().getName());
		invoiceMaster.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		invoiceMaster.setApplyDate(new Date());
		//invoiceMaster.setInvoiceDate(new Date());
		model.addAttribute("invoiceMaster",invoiceMaster);
		
		//获取当前登录人员销售中心
		String center =lovMemberService.getSaleCenter(userObject.getOrg().getId());
		if("P_GJORG_B1_0001".equals(center)){
			model.addAttribute("center","int");//国际
		}
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		if(this.hasPermission("P06InvoiceT2IssuedPage")){
			tabMain.addTab("已发货未开票明细", "/invoice/invoiceDetailDeliveryList.html?invoiceCode=");
		}
		if(this.hasPermission("P06InvoiceT1AdvancePage")){
			tabMain.addTab("提前开票明细", "/invoice/invoiceDetailOrderList.html?invoiceCode=");
		}
		if(this.hasPermission("P06InvoiceT6InvoicePage")){
			tabMain.addTab("金税/形式发票明细", "/invoice/goldenTaxList.html?invoiceCode=");
		}
		model.addAttribute("tabMain",tabMain);
		
		return forward("invoice_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(InvoiceMaster invoiceMaster) throws Exception {
		invoiceMaster.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		invoiceMaster.setApplyDate(new Date());
		//invoiceMaster.setInvoiceDate(new Date());
		invoiceService.saveInvoice(invoiceMaster,getUserObject());
		return sendSuccessMessage(invoiceMaster.getId());
	}

	@NoRight
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		InvoiceMaster invoiceMaster = invoiceService.getInvoiceMaster(id);
		if (invoiceMaster == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		
		//获取当前登录人员销售中心
		String center =lovMemberService.getSaleCenter(invoiceMaster.getCreatedOrgId());
		if("P_GJORG_B1_0001".equals(center)){
			model.addAttribute("center","int");//国际
		}
				
		model.addAttribute("invoiceMaster", invoiceMaster);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		if(this.hasPermission("P06InvoiceT2IssuedPage")){
			tabMain.addTab("已发货未开票明细", "/invoice/invoiceDetailDeliveryList.html?invoiceCode="+invoiceMaster.getInvoiceCode());
		}
		if(this.hasPermission("P06InvoiceT1AdvancePage")){
			tabMain.addTab("提前开票明细", "/invoice/invoiceDetailOrderList.html?invoiceCode="+invoiceMaster.getInvoiceCode());
		}
		if(this.hasPermission("P06InvoiceT6InvoicePage")){
			tabMain.addTab("金税/形式发票明细", "/invoice/goldenTaxList.html?invoiceCode="+invoiceMaster.getInvoiceCode());
		}
		
		if(this.hasPermission("P06InvoiceT3TeamPosPage")){
			tabMain.addTab("销售团队", "/team/list.html?businessType="+IConstants.PERMISSION_BUSINESS_TYPE_INVOICE
				+"&businessId=" + invoiceMaster.getId());
		}
		if(this.hasPermission("P06InvoiceT5ProReviewHistoryPage")){
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+invoiceMaster.getId());
		}
		if(this.hasPermission("P06InvoiceT4FilePage")){
			tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+invoiceMaster.getId() 
				+ "&businessType=ORDER_INVOICE&docGroupCode=FileTypeCode");
		}
		model.addAttribute("tabMain",tabMain);
		
		return forward("invoice_info");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(InvoiceMaster invoiceMaster) throws Exception {
		invoiceService.updateInvoice(invoiceMaster,getUserObject());
		return sendSuccessMessage(invoiceMaster.getId());
	}
	
	@RequestMapping("/invoiceDetailOrderList")
	public String  invoiceDetailOrderList(String invoiceCode, Model model) {
		model.addAttribute("invoiceCode", invoiceCode);
		model.addAttribute("invoiceType", "01");
		return forward("invoice_detail_order_list");
	}
	
	@RequestMapping("/invoiceDetailDeliveryList")
	public String  invoiceDetailDeliveryList(String invoiceCode, Model model) {
		model.addAttribute("invoiceCode", invoiceCode);
		model.addAttribute("invoiceType", "02");
		return forward("invoice_detail_delivery_list");
	}
	
	@RequestMapping("/goldenTaxList")
	public String  goldenTaxList(String invoiceCode, Model model) {
		model.addAttribute("invoiceCode", invoiceCode);
		return forward("invoice_golden_tax_list");
	}
	
	/**
	 * 
	 * getInvoiceLines:查询发货单明细行. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/invoiceLines")
	public String getInvoiceLines(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String invoiceCode = condition.getStringCondition("invoiceCode");
		String invoiceType = condition.getStringCondition("invoiceType");
		condition.getFilterObject().addCondition("invoiceCode", "eq", invoiceCode);
		condition.getFilterObject().addCondition("invoiceType", "eq", invoiceType);
		IPage p = invoiceService.queryInvoiceDetail(condition);
		return sendSuccessMessage(p);
	}

	@ResponseBody
	@RequestMapping("/getInvoiceDetailList")
	public String getInvoiceDetailList(String invoiceCode,Model model) throws Exception {
		List<InvoiceDetail>  invoiceDetails =  invoiceService.getInvoiceDetailListByMCode(invoiceCode);
		return sendSuccessMessage(JSON.toJSONString(invoiceDetails));
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/queryInvoiceGoldenTaxList")
	public String queryInvoiceGoldenTaxList(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String invoiceCode = condition.getStringCondition("invoiceCode");
		condition.getFilterObject().addCondition("invoiceCode", "eq", invoiceCode);
		IPage p = invoiceService.queryInvoiceGoldenTax(condition);
		return sendSuccessMessage(p);
	}
	
	
	/**
	 * 
	 * exportReceipts:导出开票申请. <br/> 
	 * @param request
	 * @param response
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/exportTaxlists")
	public void exportReceipts(HttpServletRequest request,HttpServletResponse response){
		
		try{		
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

			String invoiceMasterId = request.getParameter("id");
			
			InvoiceMaster invoiceMaster;
			String invoiceCode;
			
			if(!StringUtil.isEmpty(invoiceMasterId)){
				invoiceMaster = invoiceService.getInvoiceMaster(invoiceMasterId);
				if(invoiceMaster!=null){
					invoiceCode = invoiceMaster.getInvoiceCode();
					List<InvoiceGoldenTax> taxlists = invoiceService.getInvoiceGoldenTaxListByMCode(invoiceCode);
					List<List<Object>> dataList  = invoiceService.getTaxListExcelData(taxlists);
					ExcelUtil.exportExcel(response, dataList, "CRM开票明细_"+sdf.format(new Date()));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new AnneException("导出失败");
		}
		
	}
	
	/**
	 * 已发货未开票明细Excel导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping(value = "/invoiceExport")
	public void exportStdContractLists(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(","); 
		List<List<Object>> dataList = invoiceService.exportSelectedContrLists(ids);
		ExcelUtil.exportExcel(response, dataList, "已发货未开票明细");
	}
	
	/**
	 * 提前开票明细Excel导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping(value = "/invoiceLinesFormExport")
	public void exportInvoiceLinesFormLists(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(",");
		List<List<Object>> dataList = invoiceService.exportInvoiceLinesFormLists(ids);
		ExcelUtil.exportExcel(response, dataList, "提前开票明细");
	}
	
	@NoRight
	@RequestMapping(value = "/printInvoiceExcel")
	public String printInvoiceExcel(String invoiceCode, String op, Model model,HttpServletResponse response) throws UnsupportedEncodingException {
		String name = new String("发票打印".getBytes("GBK"),"ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+name+".xls");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/msexcel;charset=utf-8");
		printInvoice(invoiceCode,  op,  model);
		return forward("print_invoice_excel");
	}
	/**
	 * 
	 * printInvoice:发票打印. <br/> 
	 * @author liming 
	 * @param invoiceCode
	 * @param op
	 * @param model
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@RequestMapping(value = "/printInvoice")
	public String printInvoice(String invoiceCode, String op, Model model) {
		if (invoiceCode == null) {
			throw new AnneException("没有找到数据");
		}
		InvoiceMaster invoiceMaster = invoiceService.getInvoiceMasterByCode(invoiceCode);
		if (invoiceMaster == null) {
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("invoiceMaster", invoiceMaster);

		BigDecimal totalAmount = new BigDecimal(0.00);
		if (invoiceMaster.getInvoiceAmount() != null) {
			totalAmount = invoiceMaster.getInvoiceAmount();
		}
		List<InvoiceGoldenTax> list = invoiceService.getInvoiceGoldenTaxListByMCode(invoiceCode);
		model.addAttribute("list",list);
		model.addAttribute("toUpper",NumberToCN.number2CNMontrayUnit(totalAmount));
		return forward("print_invoice");
	}
	
	@NoRight
	@RequestMapping(value = "/printPackingListExcel")
	public String printPackingListExcel(String invoiceCode, String op, Model model,HttpServletResponse response) throws UnsupportedEncodingException {
		String name = new String("装箱单打印".getBytes("GBK"),"ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+name+".xls");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/msexcel;charset=utf-8");
		printPackingList(invoiceCode,  op,  model);
		return forward("print_packing_list_excel");
	}
	
	@NoRight
	@RequestMapping(value = "/printPackingList")
	public String printPackingList(String invoiceCode, String op, Model model) {
		if (invoiceCode == null) {
			throw new AnneException("没有找到数据");
		}
		InvoiceMaster invoiceMaster = invoiceService.getInvoiceMasterByCode(invoiceCode);
		if (invoiceMaster == null) {
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("invoiceMaster", invoiceMaster);
		double proQty = 0;
		List<InvoiceGoldenTax> list = invoiceService.getInvoiceGoldenTaxListByMCode(invoiceCode);
		if(list != null){
			for(InvoiceGoldenTax tax : list){
				proQty += tax.getInvoiceQty();
			}
		}
		model.addAttribute("list",list);
		model.addAttribute("proQty",proQty);
		return forward("print_packing_list");
	}
	
	@NoRight
	@RequestMapping(value = "/printSalesContractExcel")
	public String printSalesContractExcel(String invoiceCode, String op, Model model,HttpServletResponse response) throws UnsupportedEncodingException {
		String name = new String("销售合同打印".getBytes("GBK"),"ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+name+".xls");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/msexcel;charset=utf-8");
		printSalesContract(invoiceCode,  op,  model);
		return forward("print_sales_contract_excel");
	}
	
	@NoRight
	@RequestMapping(value = "/printSalesContract")
	public String printSalesContract(String invoiceCode, String op, Model model) {
		if (invoiceCode == null) {
			throw new AnneException("没有找到数据");
		}
		InvoiceMaster invoiceMaster = invoiceService.getInvoiceMasterByCode(invoiceCode);
		if (invoiceMaster == null) {
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("invoiceMaster", invoiceMaster);

		BigDecimal totalAmount = new BigDecimal(0.00);
		if (invoiceMaster.getInvoiceAmount() != null) {
			totalAmount = invoiceMaster.getInvoiceAmount();
		}
		List<InvoiceGoldenTax> list = invoiceService.getInvoiceGoldenTaxListByMCode(invoiceCode);
		model.addAttribute("list",list);
		model.addAttribute("toUpper",NumberToCN.number2CNMontrayUnit(totalAmount));
		return forward("print_sales_contract");
	}
}