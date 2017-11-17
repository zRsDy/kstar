package com.ibm.kstar.action.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.model.Task;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.log.IInterFaceLogService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.log.MethodLogger;


/**
 * 接口日志
 * @author 55425
 *
 */
@Controller
@RequestMapping("/interFaceLog")
public class InterFaceLogAction extends BaseFlowAction {

	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IInterFaceLogService iInterFaceLogService;

    @NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("orderNumber", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("userNum", "like", "%"+searchKey+"%");
		}
		
		String orderNumber = condition.getStringCondition("orderNumber");
		if(StringUtil.isNotEmpty(orderNumber)){
			condition.getFilterObject().addCondition("orderNumber", "like", "%"+orderNumber+"%");
		}
		
		String userNum = condition.getStringCondition("userNum");
		if(StringUtil.isNotEmpty(userNum)){
			condition.getFilterObject().addCondition("userNum", "=", userNum);
		}
		
		String isHaveProcessingStatus = condition.getStringCondition("isHaveProcessingStatus");
		if(StringUtil.isNotEmpty(isHaveProcessingStatus)&&"1".equals(isHaveProcessingStatus)){
			condition.getFilterObject().addCondition("processingStatus", "=", "ERROR");
		}

		String interfaceStartDate_begin = condition.getStringCondition("interfaceStartDate_begin");
		if(StringUtil.isNotEmpty(interfaceStartDate_begin)){
			condition.getFilterObject().addCondition("interfaceStartDate", ">=", interfaceStartDate_begin);
		}
		String interfaceStartDate_end = condition.getStringCondition("interfaceStartDate_end");
		if(StringUtil.isNotEmpty(interfaceStartDate_end)){
			condition.getFilterObject().addCondition("interfaceStartDate", "<=", interfaceStartDate_end);
		}
		
		
		String interfaceReportDate_begin = condition.getStringCondition("interfaceReportDate_begin");
		if(StringUtil.isNotEmpty(interfaceReportDate_begin)){
			condition.getFilterObject().addCondition("interfaceReportDate", ">=", interfaceReportDate_begin);
		}
		String interfaceReportDate_end = condition.getStringCondition("interfaceReportDate_end");
		if(StringUtil.isNotEmpty(interfaceReportDate_end)){
			condition.getFilterObject().addCondition("interfaceReportDate", "<=", interfaceReportDate_end);
		}
		
		String userEmployeeId = condition.getStringCondition("userEmployeeId");
		if(StringUtil.isNotEmpty(userEmployeeId)){
			condition.getFilterObject().addCondition("userEmployeeId", "=", userEmployeeId);
		}
		
		IPage p = iInterFaceLogService.queryMethodLogger(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		return forward("log_list");
	}

	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,String op, Model model,HttpServletRequest request) throws Exception {
		MethodLogger methodLogger = baseDao.get(MethodLogger.class, id);
		model.addAttribute("methodLogger", methodLogger);
		return forward("log_detailed");
	}
	
	
}