package com.ibm.kstar.action.order;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.common.customlov.ICustomLovInfoService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderHeaderChange;
import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.entity.order.vo.OrderVO;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.log.IMethodLogService;
import com.ibm.kstar.log.MethodLogger;
import com.ibm.kstar.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.NumberToCN;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.Task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderAction extends BaseFlowAction {
	
	@Autowired
	ITaskService taskService;
	@Autowired
	BaseDao baseDao;
	@Autowired
	ICustomLovInfoService customLovInfoService;
	@Autowired
	IOrderService orderService;
	@Autowired 
	ILovMemberService lovMemberService;
	@Autowired
	ICustomInfoService customInfoService;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IPriceHeadService priceHeadService;
	@Autowired
	ICustomInfoService service;
	@Autowired
    IDemoService demoService;
	@Autowired
    IMethodLogService methodLogService;

    @NoRight
	@LogOperate(module="订单管理模块",notes="${user}页面：订单列表")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("orderCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customerCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("custAttnCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("custAttnName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customerPo", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("businessEntity", "like", "%"+searchKey+"%");
		}
		
		String businessEntity = condition.getStringCondition("businessEntity");
		if(StringUtil.isNotEmpty(businessEntity)){
			condition.getFilterObject().addCondition("businessEntity", "=", businessEntity);
		}
		
		String salesCenter = condition.getStringCondition("salesmanCenter");
		if(StringUtil.isNotEmpty(salesCenter)){
			condition.getFilterObject().addCondition("salesmanCenter", "eq", salesCenter);
		}
		
		String bizDept = condition.getStringCondition("salesmanDep");
		if(StringUtil.isNotEmpty(bizDept)){
			condition.getFilterObject().addCondition("salesmanDep", "eq", bizDept);
		}
		
		String orderCode = condition.getStringCondition("orderCode");
		if(StringUtil.isNotEmpty(orderCode)){
			condition.getFilterObject().addCondition("orderCode", "like", "%"+orderCode+"%");
		}
		
		String erpOrderCode = condition.getStringCondition("erpOrderCode");
		if(StringUtil.isNotEmpty(erpOrderCode)){
			condition.getFilterObject().addCondition("erpOrderCode", "like", "%"+erpOrderCode+"%");
		}
		
		String orderType = condition.getStringCondition("orderType");
		if(StringUtil.isNotEmpty(orderType)){
			condition.getFilterObject().addCondition("orderType", "=", orderType);
		}
		
		String salesmanId = condition.getStringCondition("salesmanId");
		if(StringUtil.isNotEmpty(salesmanId)){
			condition.getFilterObject().addCondition("salesmanId", "=", salesmanId);
		}
		
		String customerId = condition.getStringCondition("customerId");
		if(StringUtil.isNotEmpty(customerId)){
			condition.getFilterObject().addCondition("customerId", "=", customerId);
		}
		
		String customerPo = condition.getStringCondition("customerPo");
		if(StringUtil.isNotEmpty(customerPo)){
			condition.getFilterObject().addCondition("customerPo", "=", customerPo);
		}
		
		String executeStatus = condition.getStringCondition("executeStatus");
		if(StringUtil.isNotEmpty(executeStatus)){
			condition.getFilterObject().addCondition("executeStatus", "=", executeStatus);
		}
		
		String sourceType = condition.getStringCondition("sourceType");
		if(StringUtil.isNotEmpty(sourceType)){
			condition.getFilterObject().addCondition("sourceType", "=", sourceType);
		}

		String controlStatus = condition.getStringCondition("controlStatus");
		if(StringUtil.isNotEmpty(controlStatus)){
			condition.getFilterObject().addCondition("controlStatus", "=", controlStatus);
		}
		
		String sourceCode = condition.getStringCondition("sourceCode");
		if(StringUtil.isNotEmpty(sourceCode)){
			condition.getFilterObject().addCondition("sourceCode", "like", "%"+sourceCode+"%");
		}
		
		String createdById = condition.getStringCondition("createdById");
		if(StringUtil.isNotEmpty(createdById)){
			condition.getFilterObject().addCondition("createdById", "=", createdById);
		}
		
		String orderDate_begin = condition.getStringCondition("orderDate_begin");
		if(StringUtil.isNotEmpty(orderDate_begin)){
			condition.getFilterObject().addCondition("orderDate", ">=", orderDate_begin);
		}
		String orderDate_end = condition.getStringCondition("orderDate_end");
		if(StringUtil.isNotEmpty(orderDate_end)){
			condition.getFilterObject().addCondition("orderDate", "<=", orderDate_end);
		}
		
		
		String contrId = condition.getStringCondition("contrId");
		if(StringUtil.isNotEmpty(contrId)){
			condition.getFilterObject().addCondition("sourceId", "=", contrId);
		}
		
		IPage p = orderService.queryOrderHeaders(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		String contrId = request.getParameter("contrId");
		model.addAttribute("contrId", contrId);
		return forward("order_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		orderService.deleteOrder(id,getUserObject());
		return sendSuccessMessage();
	}

	/**
	 * 
	 * add:新建订单. <br/> 
	 * @author liming 
	 * @param model
	 * @param sourceType 来源类型
	 * @param sourceId   来源ID
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/add")
	public String add(Model model,HttpServletRequest request) {
		
		UserObject userObject = getUserObject();
		String sourceType = request.getParameter("source");
		String sourceId = request.getParameter("srcId");
		
		LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "ORDER");
		String code = "";
		String prefix = "KST-SO-";
		if(lov != null){
			prefix = lov.getName();
		}
		
		code = orderService.getSequenceCode("gen_order_code",prefix);
	
		
		OrderHeader orderHeader = new OrderHeader();
		String employeeType = "" ;
		String addUrl = "order_add_internal" ;
		orderHeader.setOrderCode(code);
		
		//订单初始版本1
		orderHeader.setVersion(IConstants.ORDER_VERSION);
		
		if(IConstants.ORDER_SOURCE_CONTRACT.equals(sourceType)){
			//来源类型为合同
			if(StringUtil.isNotEmpty(sourceId)){
				orderHeader = orderService.createOrderHeaderByContract(orderHeader,sourceId);
			}
		}else if(IConstants.ORDER_SOURCE_BIZ.equals(sourceType)) {
			//来源类型为商机
			if(StringUtil.isNotEmpty(sourceId)){
				orderHeader = orderService.createOrderHeaderByBizopp(orderHeader,sourceId, userObject);
			}
		}else{
			//手工创建
			//orderHeader.setRequestDate(new Date());//请求日期
			orderHeader.setSourceType(IConstants.ORDER_SOURCE_CREATE);
			orderHeader.setSalesmanId(userObject.getEmployee().getId());//销售人员ID
			orderHeader.setSalesmanCode(userObject.getEmployee().getNo());//销售人员编码
			orderHeader.setSalesmanName(userObject.getEmployee().getName());//销售人员名称
			//获取当前登录人员销售中心和销售部门
			Map<String, String> salesman = orderService.salesmanChange(userObject.getOrg().getId());
			orderHeader.setSalesmanCenter(salesman.get("salesmanCenter"));//营销中心
			orderHeader.setSalesmanDep(salesman.get("salesmanDep"));//销售部门
			ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
			if (productPriceHead != null) {
				orderHeader.setPriceTableId( productPriceHead.getId());
				orderHeader.setPriceTableName(productPriceHead.getPriceTableName());
			}
			orderHeader.setIsAm(IConstants.NO);
			orderHeader.setIsInstall(IConstants.NO);
			orderHeader.setIsHomeDelivery(IConstants.NO);
			orderHeader.setIsDestinationDelivery(IConstants.NO);
			LovMember lovShipType = lovMemberService.getLovMemberByCode("TRANSPORT_METHOD", "10");
			if(lovShipType != null){
				orderHeader.setShipType(lovShipType.getId());
			}
		}
		
		if(StringUtil.isEmpty(orderHeader.getBusinessEntity())){
			LovMember lovBusinessEntity = lovMemberService.getLovMemberByCode("OPERATION_UNIT", "101");
			if(lovBusinessEntity != null){
				orderHeader.setBusinessEntity(lovBusinessEntity.getId());//业务实体
			}
			if(StringUtil.isEmpty(orderHeader.getOrderType())){
				orderHeader.setOrderType("10110");//订单类型
			}
		}
		
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
			addUrl = "order_add_external"; 
			if(StringUtil.isEmpty(orderHeader.getCustomerId()) && userObject.getOrg() != null){
				CustomInfo customInfo  = customInfoService.getCustomInfo(userObject.getOrg().getOptTxt4());
				if(customInfo != null){
					orderHeader.setCustomerId(customInfo.getId());
					orderHeader.setCustomerCode(customInfo.getCustomCode());//客户编码
					orderHeader.setCustomerName(customInfo.getCustomFullName());//客户名称
					orderHeader.setPaymentCustId(customInfo.getId());//付款客户ID
					orderHeader.setPaymentCustName(customInfo.getCustomFullName());//付款客户名称
					orderHeader.setCustomerErpCode(customInfo.getErpCode());//客户ERP编码
//					CustomAddressInfo customAddressInfo =  customInfoService.getAddrInfo1ByCustomId(customInfo.getId());
//					if(customAddressInfo != null){
//						orderHeader.setDeliveryAddressId(customAddressInfo.getId());
//						orderHeader.setDeliveryAddress(customAddressInfo.getFullCustomAddress());
//					}
				}
			}
			orderHeader.setSourceType(IConstants.ORDER_SOURCE_BIZ);
		}
		
		if(StringUtil.isEmpty(orderHeader.getCurrency())){
			orderHeader.setCurrency("CNY");
		}
		
		PageCondition condition = new PageCondition();
		if(userObject.getOrg().getOptTxt4()!=null){
			List<CustomAddressInfo>  customAddressInfoList = customLovInfoService.getCustomAddressInfoAllList(userObject.getOrg().getOptTxt4(),condition);
			if(customAddressInfoList.size()>0){
				orderHeader.setDeliveryAddress(customAddressInfoList.get(0).getAllCustomAddress());
				orderHeader.setCustAttnTel(customAddressInfoList.get(0).getCustomAddressPhone());
				model.addAttribute("isHasCustomAddress","1");
			}else{
				model.addAttribute("isHasCustomAddress","2");
			}
			List<CustomRelaContact> customRelaContactList = service.getCustomRelaContactListAuth(userObject.getOrg().getOptTxt4());
			if(customRelaContactList.size()>0){
				orderHeader.setCustAttnName(customRelaContactList.get(0).getContactName());
			}
		}
		
		orderHeader.setTermPayment("IMMEDIATE");//立即付款
		orderHeader.setOrderDate(new Date());//订购日期
		orderHeader.setExecuteStatus(IConstants.ORDER_EXECUTE_STATUS_ENTERED);//执行状态
		orderHeader.setControlStatus(IConstants.ORDER_CONTROL_STATUS_10);//控制状态
		orderHeader.setConvertType(IConstants.ORDER_EXCHANGE_RATE_10);//折换类型
		orderHeader.setConvertDate(new Date());//折换日期
		model.addAttribute("orderHeader",orderHeader);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		tabMain.addTab("订单行列表", "/order/orderLineList.html?op=add&orderHeaderId=" + (orderHeader.getId() == null ? "" : orderHeader.getId())
				+ "&orderHeaderNo=" + (orderHeader.getOrderCode() == null ? "" : orderHeader.getOrderCode())
				+ "&sourceType=" + orderHeader.getSourceType()
				+"&sourceId="+orderHeader.getSourceId()
				+"&employeeType="+employeeType);
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("userType",getUserObject().getEmployee().getId());
		return forward(addUrl);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/autocomplete_customAddress")
	public String autoCompleteCustom(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		if(getUserObject().getOrg().getOptTxt4()!=null){
			List<CustomAddressInfo>  customAddressInfoList = customLovInfoService.getCustomAddressInfoAllList(
					getUserObject().getOrg().getOptTxt4(),condition);
			return sendSuccessMessage(customAddressInfoList);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(OrderHeader orderHeader) throws Exception {
		UserObject userObject = getUserObject();
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			if(StringUtil.isNotEmpty(orderHeader.getBusinessEntity())){
				LovMember lovBusinessEntity = lovMemberService.get(orderHeader.getBusinessEntity());
				if(lovBusinessEntity != null){
					if("101".equals(lovBusinessEntity.getCode())){
						orderHeader.setOrderType("10110");//订单类型
					}else if("102".equals(lovBusinessEntity.getCode())){
						orderHeader.setOrderType("10210");//订单类型
					}else if("103".equals(lovBusinessEntity.getCode())){
						orderHeader.setOrderType("10310");//订单类型
					}
				}
			}
			CustomInfo customInfo  = customInfoService.getCustomInfo(userObject.getOrg().getOptTxt4());
			if(customInfo != null){
				orderHeader.setCustomerId(customInfo.getId());
				orderHeader.setCustomerCode(customInfo.getCustomCode());//客户编码
				orderHeader.setCustomerName(customInfo.getCustomFullName());//客户名称
				orderHeader.setPaymentCustId(customInfo.getId());//付款客户ID
				orderHeader.setPaymentCustName(customInfo.getCustomFullName());//付款客户名称
				orderHeader.setCustomerErpCode(customInfo.getErpCode());//客户ERP编码
				//销售人员
				orderHeader.setSalesmanId(customInfo.getCreatedById());
				orderHeader.setSalesmanName(customInfo.getCreatorName());
				orderHeader.setSalesmanCode(customInfo.getCreatorNo());
				//获取销售人员销售中心和销售部门
				Map<String, String> salesman = orderService.salesmanChange(customInfo.getCreatedOrgId());
				if(salesman != null){
					orderHeader.setSalesmanCenter(salesman.get("salesmanCenter"));//营销中心
					orderHeader.setSalesmanDep(salesman.get("salesmanDep"));//销售部门
				}
			}
			orderHeader.setSourceCode("签约代理商");
			orderHeader.setCurrency("CNY");
			orderHeader.setOrderType("10110");//国内销售不成套
			orderHeader.setTermPayment("IMMEDIATE");//立即付款
			orderHeader.setOrderDate(new Date());//订购日期
			orderHeader.setConvertType(IConstants.ORDER_EXCHANGE_RATE_10);//折换类型
			orderHeader.setConvertDate(new Date());//折换日期
			orderHeader.setSourceType(IConstants.ORDER_SOURCE_BIZ);//订单来源-代理商
			
			orderHeader.setIsAm(IConstants.NO);
			orderHeader.setIsInstall(IConstants.NO);
			orderHeader.setIsHomeDelivery(IConstants.NO);
			orderHeader.setIsDestinationDelivery(IConstants.NO);
			LovMember lov = lovMemberService.getLovMemberByCode("TRANSPORT_METHOD", "10");
			if(lov != null){
				orderHeader.setShipType(lov.getId());
			}
		}
		orderHeader.setExecuteStatus(IConstants.ORDER_EXECUTE_STATUS_ENTERED);//执行状态
		orderHeader.setControlStatus(IConstants.ORDER_CONTROL_STATUS_10);//控制状态
		
		orderService.saveOrder(orderHeader,getUserObject());
		return sendSuccessMessage(orderHeader);
	}

	//因为审批需要打开该页面
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,String op, Model model,HttpServletRequest request) throws Exception {
		UserObject userObject = getUserObject();
		
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		OrderHeader orderHeader = orderService.queryOrderHeaderById(id);
		if (orderHeader == null) {
			throw new AnneException("没有找到需要修改的数据");
		}

		//黄奇 2017-08-10
		//修复:不允许商务操作代理商订单“提交”：对于代理商订单，增加判断:当订单保存后，只有当前操作岗位同订单创建岗位时，才显示“提交”按钮,其他岗位不显示
        boolean canSubmit = true;
        Employee employee = employeeService.get(orderHeader.getCreatedById());
        LovMember positon = lovMemberService.get(orderHeader.getCreatedPosId());
        LovMember org = lovMemberService.get(orderHeader.getCreatedOrgId());
        UserObject createor = new UserObject(employee, null);
        createor.setPosition(positon);
        createor.setOrg(org);
        
        if(createor != null && employee != null ){
        	if("E".equals(employee.getFlag())){
        		if ((!Objects.equals(createor.getPosition().getId(), userObject.getPosition().getId()))||!"10".equals(orderHeader.getControlStatus())) {
        				 canSubmit = false;
    			}
        	}
        }
//        if (createor != null && "E".equals(employee.getFlag())) {
//			if (!Objects.equals(createor.getPosition().getId(), userObject.getPosition().getId())) {
//                canSubmit = false;
//			}
//		}
        
        model.addAttribute("canSubmit", canSubmit);
		model.addAttribute("orderHeader", orderHeader);
		model.addAttribute("isHasCustomAddress","2");
		String employeeType = "" ;
		String addUrl = "order_add_internal" ; //内部用户
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
			addUrl = "order_add_external"; 
		}
		 //如果存在在途的变更单
		String checkInApproval= "Y";
        if (this.checkOrderHeaderChangeInApproval(orderHeader.getOrderCode())) {
        	checkInApproval = "N";
        }
        model.addAttribute("checkInApproval", checkInApproval);
		model.addAttribute("employeeType", employeeType);
		
		String orderTypeTxt2 = "";
		LovMember orderTypeLovMember= lovMemberService.getLovMemberByCode("OPERATION_UNIT", orderHeader.getOrderType());
		if(orderTypeLovMember != null){
			orderTypeTxt2 = orderTypeLovMember.getOptTxt2();
		}
		model.addAttribute("orderTypeTxt2", orderTypeTxt2);
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		tabMain.addTab("订单行列表", "/order/orderLineList.html?op=edit&orderHeaderId=" + (orderHeader.getId() == null ? "" : orderHeader.getId())
				+ "&orderHeaderNo=" + (orderHeader.getOrderCode() == null ? "" : orderHeader.getOrderCode())
				+"&sourceType="+orderHeader.getSourceType() 
				+"&sourceId="+orderHeader.getSourceId()
				+"&employeeType="+employeeType
				+"&orderTypeTxt2="+orderTypeTxt2);
		
		tabMain.addTab("销售团队", "/team/list.html?businessType="+IConstants.PERMISSION_BUSINESS_TYPE_ORDER
				+"&businessId=" + orderHeader.getId());
		if(this.hasPermission("P06OrderT3ProReviewHistory")){
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+orderHeader.getId());
		}
		String businessId = orderHeader.getId();
		tabMain.addTab("附件", "/common/attachment/attachment.html?businessId=" + businessId + "&businessType=ORDER_CHANGE&docGroupCode=FileTypeCode");
		tabMain.addTab("订单变更", "/order/change/changePage.html?orderId=" + businessId);
		
		model.addAttribute("tabMain", tabMain);
		List<OrderLines> orderLines = new ArrayList<OrderLines>();
		model.addAttribute("orderLines", JSON.toJSONString(orderLines));
		
		
		model.addAttribute("newProcessType", request.getParameter("newProcessType"));
		model.addAttribute("showPrintBtn", request.getParameter("showPrintBtn"));

		String taskId = request.getParameter("taskId");
		if (StringUtil.isNotEmpty(taskId)) {
			Task task = taskService.getTask(taskId);
			if (task == null) {
				throw new AnneException("任务已经不存在");
			}
			model.addAttribute("task", task);
			model.addAttribute("taskId", task.getId());
			try {
				getHistory(taskId, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return forward(addUrl);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(OrderHeader orderHeader,HttpServletRequest request) throws Exception {
		String newProcessType = request.getParameter("newProcessType");
		if(StringUtil.isNullOrEmpty(newProcessType)) {
			orderService.updateOrder(orderHeader,getUserObject());
			return sendSuccessMessage(orderHeader);
		}else if("Y".equals(newProcessType)) {
			ProcessForm form = ActionUtil.getProcessForm(request, getUserObject());
			String controlStatus = orderHeader.getControlStatus();
			if((hasPermission("P06OrderSave")||("10".equals(controlStatus)||"40".equals(controlStatus)))
					||(hasPermission("P06OrderSave20")&&"20".equals(controlStatus))
					||(hasPermission("P06OrderSave30")&&"30".equals(controlStatus))) {
				orderService.updateOrder(orderHeader,getUserObject());
			}
			demoService.todoProcess(form);
		}
		return sendSuccessMessage(orderHeader);
		
	}
	
	@NoRight
	@RequestMapping("/orderLineList")
	public String orderLineList(String orderHeaderId, String orderHeaderNo, String sourceType, String sourceId, String op, String employeeType,
								String orderTypeTxt2, Model model) throws Exception {
		
		List<OrderLines> orderLines = new ArrayList<OrderLines>();
		if(IConstants.ORDER_SOURCE_CONTRACT.equals(sourceType) && "add".equals(op)){
			orderLines = orderService.createOrderLinesByContract(sourceId,getUserObject());
		}else if(IConstants.ORDER_SOURCE_BIZ.equals(sourceType) && "add".equals(op)){
			orderLines = orderService.createOrderLinesByBizopp(sourceId, getUserObject());
		}
		model.addAttribute("orderHeaderId", orderHeaderId);
		model.addAttribute("orderHeaderNo", orderHeaderNo);
		model.addAttribute("sourceType",sourceType);
		model.addAttribute("orderTypeTxt2", orderTypeTxt2);
		model.addAttribute("employeeType", employeeType);
		model.addAttribute("orderLines",JSON.toJSONString(orderLines));
	
		return forward("order_line_list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pagelines")
	public String page_lines(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("orderCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proModel", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("materielCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("itemDescription", "like", "%"+searchKey+"%");
		}
		
		String orderId = condition.getStringCondition("orderId");
		if(orderId == null){
			orderId ="";
		}
		condition.getFilterObject().addCondition("orderId", "eq", orderId);
		IPage p = orderService.queryOrderLines(condition);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 
	 * updateStatus:订单状态更新 <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/updateStatus")
	public String updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String orderId = request.getParameter("id");
		String operate = request.getParameter("operate");
		String status = request.getParameter("status");
		UserObject userObject = getUserObject();
		
		if(("register").equals(operate)){
			//订单登记
			orderService.updateOrderExecuteStatus(orderId, status,userObject);
		}else if("submit".equals(operate)){
			//订单提交-更新订单控制状态
			orderService.updateOrderControlStatus(orderId, status,userObject);
		}
		return sendSuccessMessage("操作成功");
	}
	/**
	 * 
	 * splitLineCheck:拆行前校验ERP. <br/> 
	 * 
	 * @author liming 
	 * @param op
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/splitLineCheck")
	public String splitLineCheck(String op ,String id,Model model) throws Exception {
		OrderLines orderLine = baseDao.get(OrderLines.class, id);
		OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderLine.getOrderId());
		String ret = "S";
		if(orderLine != null ){
			MethodLogger methodLogger = methodLogService.getMethodLogger("com.ibm.kstar.action.order.OrderAction.splitLineCheck",orderHeader.getOrderCode());
			Exception exception = new Exception();
			methodLogService.setFunctionNameAndParameter(methodLogger, "orderService.checkOrderSplitLine(orderLine.getOrderCode(), orderLine.getLineNo(),orderLine.getProQty())", 1, orderLine.getOrderCode(), orderLine.getLineNo(),orderLine.getProQty());
			try {
				Map<String, String> retMap =  orderService.checkOrderSplitLine(orderLine.getOrderCode(), orderLine.getLineNo(),orderLine.getProQty());
				ret = retMap.get("status");
				if(!"S".equals(ret)){
					ret = retMap.get("msg");
				}
			}catch(Exception e) {
	    		e.printStackTrace();
	    		exception = e;
	    		throw e;
	    	}finally {
	    		methodLogService.setReturnDataNotes(true,methodLogger,exception,1,ret);
	    	}	
		}
		return sendSuccessMessage(ret);
	}
	/**
	 * 
	 * splitLine:拆分订单行. <br/> 
	 * 
	 * @author liming 
	 * @param op
	 * @param id
	 * @param deliveryLineId
	 * @param residualQty
	 * @param model
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@RequestMapping("/splitLine")
	public String splitLine(String op ,String id, String deliveryLineId, Double residualQty, Model model) throws Exception {
		OrderLines orderLine = baseDao.get(OrderLines.class, id);
		model.addAttribute("op", op);
		if(residualQty == null || residualQty == 0 ){
			residualQty = orderLine.getProQty() - 1 ;
		}
		double validQty = orderLine.getProQty() - orderLine.getCancelQty();
		if( validQty < residualQty ){
			residualQty = validQty;
		}
		model.addAttribute("deliveryLineId",deliveryLineId);
		model.addAttribute("residualQty",residualQty);
		model.addAttribute("orderLine", orderLine);
		return forward("order_line_split");
	}
	/**
	 * 
	 * splitLineSave:订单行分保存. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/splitLineSave", method = RequestMethod.POST)
	public String splitLineSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String orderLineId = request.getParameter("id");
		String op = request.getParameter("op");
		String deliveryLineId = request.getParameter("deliveryLineId");
		double quantity = request.getParameter("quantity") == null ? 0 : Double.parseDouble(request.getParameter("quantity"));
		//拆分订单
		orderService.splitLine(op,orderLineId, deliveryLineId, quantity, getUserObject());
		return sendSuccessMessage("订单拆分成功");
	}
	
	/**
	 * 
	 * splitLineSave:订单行分保存. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelLine", method = RequestMethod.POST)
	public String cancelLine(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String msg ="订单行取消成功";
		String lineId = request.getParameter("id");
		orderService.cancelOrderLine(lineId, getUserObject());
		return sendSuccessMessage(msg);
	}
	/**
	 * 
	 * salesmanChange:销售人员变更后销售中心和部门联动. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/salesmanChange")
	public String salesmanChange(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String orgId = request.getParameter("orgId");
		Map<String, String> retMap = orderService.salesmanChange(orgId);
		return sendSuccessMessage(retMap);
	}
	
	/**
	 * 
	 * salesmanChangeG:回款计划销售人员变更后销售中心和部门联动. <br/> 
	 * 
	 * @author 张钧鑫 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/salesmanChangeG")
	public String salesmanChangeG(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String orgId = request.getParameter("orgId");
		Map<String, String> retMap = orderService.salesmanChangeG(orgId);
		return sendSuccessMessage(retMap);
	}
	
	/**
	 * 
	 * calculateSparePrice:计算备件价格. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/calculateSparePrice")
	public String calculateSparePrice(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String spareOrderNo = request.getParameter("spareOrderNo");
		BigDecimal spareAmount  = StringUtil.isNotEmpty(request.getParameter("spareAmount")) 
				? new BigDecimal(request.getParameter("spareAmount")) : new BigDecimal(0);
		BigDecimal amount = new BigDecimal(0);
		if(spareAmount.doubleValue() == 0  ){
			amount =  orderService.calculateSparePrice(spareOrderNo,getUserObject());
		}else{
			amount = spareAmount;
		}
		return sendSuccessMessage(amount);
	}
	
	/**
	 * 订单列表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping(value = "/orderExport")
	public void exportStdContractLists(   HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(","); 
		List<List<Object>> dataList = orderService.exportSelectedContrLists(ids);
		ExcelUtil.exportExcel(response, dataList, "订单列表");
	}
	
	
	
	@RequestMapping(value = "/print")
	public String print(String id, String op, Model model) {

		UserObject userObject = getUserObject();

		if (id == null) {
			throw new AnneException("没有找到数据");
		}

		OrderHeader orderHeader = orderService.queryOrderHeaderById(id);
		if (orderHeader == null) {
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("orderHeader", orderHeader);

		String employeeType = "";
		// 如果是外部用户
		if (userObject != null && userObject.isInner()) {
			employeeType = "A";
		}
		model.addAttribute("employeeType", employeeType);

		BigDecimal totalAmount = new BigDecimal(0.00);
		if (orderHeader.getAmount() != null) {
			totalAmount = orderHeader.getAmount();
		}
		
		model.addAttribute("list",orderService.getOrderLinesOrderId(id));
		model.addAttribute("toUpper",NumberToCN.number2CNMontrayUnit(totalAmount));
		
		return forward("print");
	}
	
	/**
	 * 
	 * calculateWholesalePrice:计算产品批发、折扣价格. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/calculateDiscountPrice", method = RequestMethod.POST)
	public String calculateDiscountPrice(OrderHeader orderHeader) throws Exception{
		String priceTableId =  orderHeader.getPriceTableId();
		String customerCode =  orderHeader.getCustomerCode();
		List<Map<Object,Object>> lines = orderHeader.getLinesList();
	    if(lines != null){
	    	orderService.calculateProPrice(customerCode,priceTableId, lines);
	    }
		return sendSuccessMessage(lines);
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
	@RequestMapping("/exportOrder")
	public void exportOrder(HttpServletRequest request,HttpServletResponse response){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			Condition condition = new Condition();
			ActionUtil.requestToCondition(condition, request);
			List<OrderVO> orders = orderService.queryOrderListByCondition(condition);
			List<List<Object>> dataList  = orderService.getExcelData(orders);
			ExcelUtil.exportExcel(response, dataList, "CRM订单列表导出 _"+sdf.format(new Date()));
		}catch(Exception e){
			e.printStackTrace();
			throw new AnneException("导出失败");
		}
	}
	
	/**
     * queryOrderHeaderChangeInApproval:查询在途的变更单. <br/>
     *
     * @param orderCode 订单编号
     * @return
     * @author liming
     * @since JDK 1.7
     */
	private boolean checkOrderHeaderChangeInApproval(String orderCode) {
        StringBuffer hql = new StringBuffer("from OrderHeaderChange o where o.orderCode = ? ");
        hql.append(" and ( o.eventStatus = ? or o.eventStatus = ? or o.eventStatus = ? ) ");
        List<Object> args = new ArrayList<Object>();
        args.add(orderCode);
        args.add(IConstants.ORDER_CONTROL_STATUS_10);
        args.add(IConstants.ORDER_CONTROL_STATUS_20);
        args.add(IConstants.ORDER_CONTROL_STATUS_40);
        List<OrderHeaderChange> orderHeaderChanges = baseDao.findEntity(hql.toString(), args.toArray());
        if (orderHeaderChanges != null && orderHeaderChanges.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * 
	 * cancelAdvanceBilling:取消提前开票
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelAdvanceBilling", method = RequestMethod.POST)
	public String cancelAdvanceBilling(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String msg ="订单行提前开票取消成功";
		String lineId = request.getParameter("id");
		orderService.cancelAdvanceBilling(lineId, getUserObject());
		return sendSuccessMessage(msg);
	}
}