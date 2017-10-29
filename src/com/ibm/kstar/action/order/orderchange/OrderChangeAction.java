package com.ibm.kstar.action.order.orderchange;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderHeaderChange;
import com.ibm.kstar.entity.order.OrderLines;
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
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.Task;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.RebateChange;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderHeaderChange;
import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.RebateChange;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderHeaderChange;
import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import com.ibm.kstar.service.IDemoService;

@Controller
@RequestMapping("/order/change")
public class OrderChangeAction extends BaseFlowAction {
	@Autowired
	BaseDao baseDao;
	@Autowired
	ITaskService taskService;
	@Autowired
	IOrderService orderService;
	@Autowired 
	ILovMemberService lovMemberService;
	@Autowired
	ICustomInfoService customInservice;
	@Autowired
    IDemoService demoService;
	
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
		
		condition.getFilterObject().addCondition("eventStatus", "!=", IConstants.ORDER_CONTROL_STATUS_70);
		IPage p = orderService.queryOrderHeaderChanges(condition);
		return sendSuccessMessage(p);
	}


	//因为审批需要打开该页面
	@NoRight
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String change(String id , String flg, Model model, HttpServletRequest request) throws Exception {
		UserObject userObject = getUserObject();
		
		if (id == null) {
			throw new AnneException("没有找到需要变更的数据");
		}
		OrderHeader orderHeader = orderService.queryOrderHeaderById(id);
		if (orderHeader == null) {
			throw new AnneException("没有找到需要变更的数据");
		}
	
		OrderHeaderChange orderHeaderChange = new OrderHeaderChange();
		BeanUtils.copyPropertiesIgnoreNull(orderHeader, orderHeaderChange);	
		orderHeaderChange.setEventStatus(IConstants.ORDER_CONTROL_STATUS_10);
		
		model.addAttribute("orderHeader", orderHeaderChange);
		String employeeType = "" ;
		String addUrl = "order_add_internal_change" ; //内部用户
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
			addUrl = "order_add_external_change"; 
		}
		
		CustomInfo customInfo = customInservice.getCustomInfo(orderHeaderChange.getCustomerId());
		LovMember orgLovMember = lovMemberService.getLovMemberByName("ORG",customInfo.getCustomFullName());
		String optTx3 =  orgLovMember.getOptTxt3();
		model.addAttribute("optTx3", optTx3);
		model.addAttribute("employeeType", employeeType);
		
		String orderTypeTxt2 = "";
		LovMember orderTypeLovMember= lovMemberService.getLovMemberByCode("OPERATION_UNIT", orderHeader.getOrderType());
		if(orderTypeLovMember != null){
			orderTypeTxt2 = orderTypeLovMember.getOptTxt2();
		}
		model.addAttribute("orderTypeTxt2", orderTypeTxt2);
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		tabMain.addTab("订单行列表", "/order/change/orderLineList.html?op=add&orderId=" + orderHeader.getId() 
				+"&sourceType="+orderHeader.getSourceType() 
				+"&sourceId="+orderHeader.getSourceId()
				+"&employeeType="+employeeType
				+"&optTx3="+optTx3
				+"&delete=Y"
				+"&orderTypeTxt2="+orderTypeTxt2);

        tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+orderHeader.getId()
                + "&businessType=ORDER_CHANGE&docGroupCode=FileTypeCode");

		model.addAttribute("tabMain", tabMain);
		List<OrderLines> orderLines = new ArrayList<OrderLines>();
		model.addAttribute("orderLines", JSON.toJSONString(orderLines));
		
		return forward(addUrl);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(OrderHeader orderHeader,HttpServletRequest request) throws Exception {
		String orderHeaderChangeId = request.getParameter("orderHeaderChangeId");
		orderHeader.setId(orderHeaderChangeId);
		OrderHeader oldOrderHeader = baseDao.get(OrderHeader.class, orderHeader.getId());
		if (!IConstants.ORDER_CONTROL_STATUS_30.equals(oldOrderHeader.getControlStatus())) {
			throw new AnneException("订单状态状态不允许变更！");
		}
		if (!IConstants.ORDER_EXECUTE_STATUS_BOOKED.equals(oldOrderHeader.getExecuteStatus()) 
				&& !IConstants.ORDER_EXECUTE_STATUS_ENTERED.equals(oldOrderHeader.getExecuteStatus())) {
			throw new AnneException("订单状态状态不允许变更！");
		}
		UserObject userObject = getUserObject();
		orderService.saveOrderChange(orderHeader, userObject);
		return sendSuccessMessage("订单变更启动成功");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(OrderHeaderChange orderHeaderChange,String orderChangeFlag, HttpServletRequest request) throws Exception {
		UserObject userObject = getUserObject();
		ProcessForm form = ActionUtil.getProcessForm(request, getUserObject());
		String orderHeaderChangeId = request.getParameter("orderHeaderChangeId");
		orderHeaderChange.setId(orderHeaderChangeId);
		OrderHeaderChange oldOrderHeaderChange = baseDao.get(OrderHeaderChange.class, orderHeaderChangeId);
		if("Y".equals(orderChangeFlag)) {
			orderService.updateOrderChange(orderHeaderChange, userObject,orderChangeFlag);
        }
		demoService.todoProcess(form);
		return sendSuccessMessage();
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id , String flg, Model model, HttpServletRequest request) throws Exception {
		OrderHeaderChange orderHeaderChange = baseDao.get(OrderHeaderChange.class, id);
		
		UserObject userObject = getUserObject();
		String newProcessType = request.getParameter("newProcessType");
		String orderChangeFlag = "N";
		if(!StringUtil.isNullOrEmpty(newProcessType)) {
			//被驳回且是创建人时
			if("40".equals(orderHeaderChange.getEventStatus())) {
				if(userObject.getEmployee().getId().equals(orderHeaderChange.getCreatedById())) {
					orderChangeFlag = "Y";
				}
			}else if("20".equals(orderHeaderChange.getEventStatus())&&hasPermission("P06OrderChangeEdit")) {
				orderChangeFlag = "Y";
			}
		}
		model.addAttribute("orderChangeFlag", orderChangeFlag);
		if (id == null) {
			throw new AnneException("没有找到数据");
		}
		
		if (orderHeaderChange == null) {
			throw new AnneException("没有找到数据");
		}
		
		model.addAttribute("orderHeader", orderHeaderChange);
		String employeeType = "" ;
		String addUrl = "order_add_internal_change" ; //内部用户
		//如果是外部用户
		if(userObject != null && !userObject.isInner()){
			employeeType = "E";
			addUrl = "order_add_external_change"; 
		}
		
		model.addAttribute("employeeType", employeeType);
		
		String orderTypeTxt2 = "";
		LovMember orderTypeLovMember= lovMemberService.getLovMemberByCode("OPERATION_UNIT", orderHeaderChange.getOrderType());
		if(orderTypeLovMember != null){
			orderTypeTxt2 = orderTypeLovMember.getOptTxt2();
		}
		model.addAttribute("orderTypeTxt2", orderTypeTxt2);
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		tabMain.addTab("订单行列表", "/order/change/orderLineList.html?op=edit&orderId=" + orderHeaderChange.getId()
				+"&sourceType="+orderHeaderChange.getSourceType()
				+"&sourceId="+orderHeaderChange.getSourceId()
				+"&employeeType="+employeeType
				+"&executeStatus="+orderHeaderChange.getExecuteStatus()
				+"&orderTypeTxt2="+orderTypeTxt2);
		tabMain.addTab("审批历史", "/standard/history.html?contrId="+orderHeaderChange.getId());

        String businessId = orderHeaderChange.getFromId();

		if (IConstants.ORDER_CONTROL_STATUS_10.equals(orderHeaderChange.getEventStatus()) || IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeaderChange.getEventStatus())||(IConstants.ORDER_CONTROL_STATUS_20.equals(orderHeaderChange.getEventStatus())&&hasPermission("P06OrderChangeEdit"))) {
			String unableChange = "false";
			if (IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeaderChange.getEventStatus())) {
				unableChange = (orderChangeFlag.equals("Y") ? "false" : "true");
			}
			
			tabMain.addTab("附件", "/common/attachment/attachment.html?businessId=" + businessId + "&businessType=ORDER_CHANGE&docGroupCode=FileTypeCode&unableAdd=" + unableChange + "&unableDelete=" + unableChange);
		} else {
			tabMain.addTab("附件", "/common/attachment/attachment.html?businessId=" + businessId + "&businessType=ORDER_CHANGE&docGroupCode=FileTypeCode&unableAdd=true&unableDelete=true");
		}

		model.addAttribute("tabMain", tabMain);
		List<OrderLines> orderLines = new ArrayList<OrderLines>();
		model.addAttribute("orderLines", JSON.toJSONString(orderLines));
		
		model.addAttribute("newProcessType", request.getParameter("newProcessType"));

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
	@RequestMapping("/list")
	public String list(Model model) throws Exception {
		return forward("order_list_change");
	}
	
	@NoRight
	@RequestMapping("/orderLineList")
	public String orderLineList(String orderId,String sourceType,String sourceId,String op,String employeeType,
			String orderTypeTxt2,String optTx3,String executeStatus,String delete,Model model) throws Exception {
		
		List<OrderLines> orderLines = new ArrayList<OrderLines>();
		if("add".equals(op)){
			orderLines = orderService.getOrderLinesOrderId(orderId);
		}
		model.addAttribute("orderId", orderId);
		model.addAttribute("executeStatus", executeStatus);
		model.addAttribute("sourceType",sourceType);
		model.addAttribute("orderTypeTxt2", orderTypeTxt2);
		model.addAttribute("optTx3", optTx3);
		model.addAttribute("delete", delete);
		model.addAttribute("employeeType", employeeType);
		model.addAttribute("orderLines",JSON.toJSONString(orderLines));
	
		return forward("order_line_list_change");
	}
	
	@NoRight
	@RequestMapping(value = "/changePage")
	public String pageGet(String orderId, Model model) {
		model.addAttribute("orderId",orderId);
		return forward("changePage");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/orderChangeList")
	public String pagePost(String orderId, HttpServletRequest request) {
		List<OrderHeaderChange> list = orderService.getOrderChangeByOrderId(orderId);
		return sendSuccessMessage(new PageImpl(list, 1, 1, 20));
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pagelines")
	public String page_lines(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String orderChangeId = condition.getStringCondition("orderId");
		condition.getFilterObject().addCondition("orderChangeId", "eq",orderChangeId);
		IPage p = orderService.queryOrderLinesChange(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		orderService.deleteOrderChange(id,getUserObject());
		return sendSuccessMessage();
	}
}