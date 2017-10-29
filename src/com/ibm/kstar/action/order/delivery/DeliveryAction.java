package com.ibm.kstar.action.order.delivery;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomErpInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.DeliveryHeader;
import com.ibm.kstar.entity.order.DeliveryLines;
import com.ibm.kstar.entity.order.DeliveryLogistics;
import com.ibm.kstar.interceptor.system.permission.NoRight;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/delivery")
public class DeliveryAction extends BaseAction {
	@Autowired
	IDeliveryService deliveryService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IOrderService orderService;
	@Autowired
	ICustomInfoService customInfoService;
	@Autowired
	BaseDao baseDao;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("deliveryCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proposerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("receCustomerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("consignee", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("billCustomerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("businessEntity", "like", "%"+searchKey+"%");
		}
		String deliveryCode = condition.getStringCondition("deliveryCode");
		if(StringUtil.isNotEmpty(deliveryCode)){
			condition.getFilterObject().addCondition("deliveryCode", "like", "%"+deliveryCode+"%");
		}
		String businessEntity = condition.getStringCondition("businessEntity");
		if(StringUtil.isNotEmpty(businessEntity)){
			condition.getFilterObject().addCondition("businessEntity", "eq", businessEntity);
		}
		String receCustomerId = condition.getStringCondition("receCustomerId");
		if(StringUtil.isNotEmpty(receCustomerId)){
			condition.getFilterObject().addCondition("receCustomerId", "eq", receCustomerId);
		}
		String deliveryAddressId = condition.getStringCondition("deliveryAddressId");
		if(StringUtil.isNotEmpty(deliveryAddressId)){
			condition.getFilterObject().addCondition("deliveryAddressId", "eq", deliveryAddressId);
		}
		String consignee = condition.getStringCondition("consignee");
		if(StringUtil.isNotEmpty(consignee)){
			condition.getFilterObject().addCondition("consignee", "like", "%"+consignee+"%");
		}
		String consigneeTel = condition.getStringCondition("consigneeTel");
		if(StringUtil.isNotEmpty(consigneeTel)){
			condition.getFilterObject().addCondition("consigneeTel", "like", "%"+consigneeTel+"%");
		}
		String billCustomerId = condition.getStringCondition("billCustomerId");
		if(StringUtil.isNotEmpty(billCustomerId)){
			condition.getFilterObject().addCondition("billCustomerId", "eq", billCustomerId);
		}
		String proposerId = condition.getStringCondition("proposerId");
		if(StringUtil.isNotEmpty(proposerId)){
			condition.getFilterObject().addCondition("proposerId", "eq", proposerId);
		}
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "eq", status);
		}
		String applyDateStart = condition.getStringCondition("applyDateStart");
		if(StringUtil.isNotEmpty(applyDateStart)){
			condition.getFilterObject().addCondition("applyDate", ">=", applyDateStart);
		}
		String applyDateEnd = condition.getStringCondition("applyDateEnd");
		if(StringUtil.isNotEmpty(applyDateEnd)){
			condition.getFilterObject().addCondition("applyDate", "<=", applyDateEnd);
		}
		
		condition.getFilterObject().addCondition("deleteFlag", "!=", IConstants.YES);
				
		IPage p = deliveryService.queryDeliveryHeaders(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("delivery_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) throws Exception {
		//删除发货单头和行
		deliveryService.deleteDelivery(id ,getUserObject());
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		UserObject userObject = getUserObject();
		LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "DELIVERY");
		String code = "";
		String prefix = "KST-CH-";
		if(lov != null){
			prefix = lov.getName();
		}
		code = orderService.getSequenceCode("gen_delivery_code",prefix);
		
		DeliveryHeader deliveryHeader = new DeliveryHeader();
		deliveryHeader.setDeliveryCode(code);
		deliveryHeader.setProposerId(userObject.getEmployee().getId());
		deliveryHeader.setProposerCode(userObject.getEmployee().getNo());
		deliveryHeader.setProposerName(userObject.getEmployee().getNo() + "|" + userObject.getEmployee().getName());
		deliveryHeader.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		deliveryHeader.setApplyDate(new Date());
		
		String employeeType = "" ;
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
			CustomInfo customInfo  = customInfoService.getCustomInfo(userObject.getOrg().getOptTxt4());
			if(customInfo != null){
				deliveryHeader.setReceCustomerId(customInfo.getId());//收货客户ID
				deliveryHeader.setReceCustomerCode(customInfo.getCustomCode());//收货客户编码
				deliveryHeader.setReceCustomerName(customInfo.getCustomFullName());//收货客户名称
				deliveryHeader.setBillCustomerId(customInfo.getId());//收单客户ID
				deliveryHeader.setBillCustomerName(customInfo.getCustomFullName());//收单客户名称
				CustomErpInfo customErpInfo = customInfoService.getCustomErpInfoByCustomId(customInfo.getId());
				if(customErpInfo != null){
					deliveryHeader.setBusinessEntity(customErpInfo.getId());
				}
				CustomAddressInfo addressInfo =  customInfoService.getAddrInfo1ByCustomId(customInfo.getId());
				if(addressInfo != null){
					deliveryHeader.setDeliveryAddressId(addressInfo.getId());
					deliveryHeader.setDeliveryAddress(addressInfo.getArea());
				}
			}
		}

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		if(this.hasPermission("P06OrderIssueT1ItemPage")){
			tabMain.addTab("货物信息", "/delivery/deliveryInfo.html?deliveryCode="+deliveryHeader.getDeliveryCode());
		}
		if(this.hasPermission("P06OrderIssueT2ItemQuery")){
			tabMain.addTab("物流信息", "/delivery/deliveryLogistics.html?deliveryCode="+deliveryHeader.getDeliveryCode());
		}
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("employeeType",employeeType);
		model.addAttribute("deliveryHeader",deliveryHeader);
		
		return forward("delivery_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeliveryHeader deliveryHeader) throws Exception {
		UserObject userObject = getUserObject();
		deliveryHeader.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		deliveryHeader.setApplyDate(new Date());
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			CustomInfo customInfo  = customInfoService.getCustomInfo(userObject.getOrg().getOptTxt4());
			if(customInfo != null){
				deliveryHeader.setReceCustomerId(customInfo.getId());//收货客户ID
				deliveryHeader.setReceCustomerCode(customInfo.getCustomCode());//收货客户编码
				deliveryHeader.setReceCustomerName(customInfo.getCustomFullName());//收货客户名称
				deliveryHeader.setBillCustomerId(customInfo.getId());//收单客户ID
				deliveryHeader.setBillCustomerName(customInfo.getCustomFullName());//收单客户名称
			}
		}
		deliveryService.saveDelivery(deliveryHeader,getUserObject());
		return sendSuccessMessage(deliveryHeader.getId());
	}

	@NoRight
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		UserObject userObject = getUserObject();
		
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		DeliveryHeader deliveryHeader = deliveryService.getDeliveryHeaderById(id);
		if (deliveryHeader == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		
		String employeeType = "" ;
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
			
		}
		model.addAttribute("employeeType", employeeType);
		model.addAttribute("deliveryHeader", deliveryHeader);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		if(this.hasPermission("P06OrderIssueT1ItemPage")){
			tabMain.addTab("货物信息", "/delivery/deliveryInfo.html?deliveryCode="+deliveryHeader.getDeliveryCode());
		}
		if(this.hasPermission("P06OrderIssueT2ItemQuery")){
			tabMain.addTab("物流信息", "/delivery/deliveryLogistics.html?deliveryCode="+deliveryHeader.getDeliveryCode());
		}
		if(this.hasPermission("P06OrderIssueT3TeamSetPage")){
			tabMain.addTab("销售团队", "/team/list.html?businessType="+IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY
				+"&businessId=" + deliveryHeader.getId());
		}
		if(this.hasPermission("P06OrderIssueT4ReviewHistoryPage")){
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+deliveryHeader.getId());
		}
		if(this.hasPermission("P06OrderIssueT5FilePage")){
			tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+deliveryHeader.getId() 
					+ "&businessType=DELIVERY&docGroupCode=FileTypeCode");
		}
		
		model.addAttribute("tabMain",tabMain);
		
		return forward("delivery_add");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeliveryHeader deliveryHeader) throws Exception {
		// 更新字段
		deliveryHeader.setUpdatedById(getUserObject().getEmployee().getId());
		deliveryHeader.setUpdatedAt(new Date());
		deliveryService.updateDelivery(deliveryHeader,getUserObject());
		return sendSuccessMessage(deliveryHeader.getId());
	}
	
	/**
	 * 
	 * deliveryInfo:跳转到发货单明细信息. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryId
	 * @param model
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/deliveryInfo")
	public String deliveryInfo(String deliveryCode, Model model) {
		DeliveryHeader deliveryHeader = deliveryService.getDeliveryHeaderByCode(deliveryCode);
		if(deliveryHeader == null){
			deliveryHeader = new DeliveryHeader();
			deliveryHeader.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		}
		model.addAttribute("deliveryHeader", deliveryHeader);
		model.addAttribute("deliveryCode", deliveryCode);
		return forward("delivery_info");
	}
	
	@RequestMapping("/deliveryLogistics")
	public String getDeliveryLogistics(String deliveryCode, Model model) {
		DeliveryLogistics  logistics = deliveryService.getLogisticByDeliveryCode(deliveryCode);
		model.addAttribute("logistics", logistics);
		return forward("delivery_logistics");
	}
	
	/**
	 * 
	 * page_lines:获取发货单行. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/deliveryLines")
	public String getDeliveryLines(PageCondition condition, HttpServletRequest request) {
		
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("deliveryCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proposer", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("receCustome", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("consignee", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("receBillCustomer", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("businessEntity", "like", "%"+searchKey+"%");
		}
		
		String deliveryCode = condition.getStringCondition("deliveryCode");
		condition.getFilterObject().addCondition("deliveryCode", "eq", deliveryCode);
		condition.getFilterObject().addCondition("deleteFlag", "!=", IConstants.YES);
		
		IPage p = deliveryService.queryDeliveryLines(condition);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 
	 * getDeliveryReceipts:获取发货单对应的签收单. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/receipts")
	public String getDeliveryReceipts(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = deliveryService.getDeliveryReceiptByDCode(condition);
		return sendSuccessMessage(p);
	}
	/**
	 * 
	 * createReceiptsPlan:生成回款计划. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/createReceiptsPlan")
	public String createReceiptsPlan(HttpServletRequest request, HttpServletResponse response){
		String deliveryCode = request.getParameter("code");
		String msg = "操作成功";
		deliveryService.createContractReceiptDetail(deliveryCode);
		return sendSuccessMessage(msg);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/createReceiptsPlanAll")
	public String createReceiptsPlanAll(HttpServletRequest request, HttpServletResponse response){
		String msg = "操作成功";
		List<DeliveryHeader> deliveryHeades = baseDao.findEntity(
				"select distinct h from DeliveryHeader h, DeliveryLines l " +
						"where h.deliveryCode = l.deliveryCode and l.deleteFlag != '1' " +
						"and not exists (select 1 from DeliveryHeader dh,DeliveryLines dl " +
						"where dh.deliveryCode = dl.deliveryCode and dl.printTime is null and dh.id=h.id) " +
						"and h.deleteFlag != '1' and not exists (select 1 from ContractReceiptDetail rd where rd.deliveryId=h.id)");
		
		if(deliveryHeades != null ){
			for(DeliveryHeader deliveryHeader: deliveryHeades){
				synchronized (this) {
					deliveryService.createContractReceiptDetail(deliveryHeader.getDeliveryCode());
				}
			}
		}
		return sendSuccessMessage(msg);
	}
	
	/**
	 * 
	 * exportDeliveryLines:导出出货申请明细行. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response 
	 * @since JDK 1.7
	 */
	@NoRight
	@RequestMapping("/exportDeliveryLines")
	public void exportDeliveryLines(HttpServletRequest request,HttpServletResponse response){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String id = request.getParameter("id");
			String code = request.getParameter("code");
			List<DeliveryLines> deliveryLines = null;
			DeliveryHeader deliveryHeader = null;
			if(StringUtil.isNotEmpty(id)){
				deliveryLines = deliveryService.getDeliveryLinesByHid(id);
				deliveryHeader  = deliveryService.getDeliveryHeaderById(id);
			}else if(StringUtil.isNotEmpty(code)){
				deliveryLines = deliveryService.getDeliveryLinesByHCode(code);
				deliveryHeader  = deliveryService.getDeliveryHeaderByCode(code);
			}
			if(deliveryLines == null){
				deliveryLines = new ArrayList<DeliveryLines>();
			}
			List<List<Object>> dataList  = deliveryService.getExcelData(deliveryHeader,deliveryLines);
			ExcelUtil.exportExcel(response, dataList, "CRM出货明细表_"+sdf.format(new Date()));

		}catch(Exception e){
			e.printStackTrace();
			throw new AnneException("导出失败");
		}
	}
	/**
	 * 
	 * importDeliveryReceipt:导入出货签收单. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/importDeliveryReceipt")
	public String importDeliveryReceipt(HttpServletRequest request,HttpServletResponse response){
		String deliveryCode = request.getParameter("deliveryCode");
		List<List<Object>> dataList = this.getExcelData(request);
		deliveryService.importDeliveryReceipt(deliveryCode,dataList,getUserObject());
		return sendSuccessMessage("导入成功");
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/checkStatus")
	public String checkCanDelete(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String op = request.getParameter("op");
		String ret = "No" ;
		if("Yes".equals(deliveryService.checkIsErpDelivery(id,op))||"Yes".equals(deliveryService.checkHasReceipt(id,op))){
			ret = "Yes";
		}
		return sendSuccessMessage(ret);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/checkIsErpDelivery")
	public String checkIsErpDelivery(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String op = request.getParameter("op");
		String ret =  deliveryService.checkIsErpDelivery(id,op);
		return sendSuccessMessage(ret);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/checkHasReceipt")
	public String checkHasReceipt(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String op = request.getParameter("op");
		String ret =  deliveryService.checkHasReceipt(id,op);
		return sendSuccessMessage(ret);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/customerChange")
	public String customerChange(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String customId = request.getParameter("customId");
		CustomAddressInfo addrInfo = customInfoService.getAddrInfo1ByCustomId(customId);
		CustomErpInfo erpInfo = customInfoService.getCustomErpInfoByCustomId(customId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("addrInfo", addrInfo);
		retMap.put("erpInfo", erpInfo);
		return sendSuccessMessage(retMap);
	}
	
	/**
	 * 签收单数据导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping(value = "/exportDeliveryReceipt")
	public void exportStdContractLists(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(","); 
		List<List<Object>> dataList = deliveryService.exportInvoiceLinesFormLists(ids);
		ExcelUtil.exportExcel(response, dataList, "签收单");
	}
}