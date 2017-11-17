package com.ibm.kstar.action.datatrace;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.custom.IDataTraceService;

/**
 * 
 * ClassName: 任务调度管理 <br/> 
 * @author liumq
 * @version  
 * @since JDK 1.7
 */

@Controller
@RequestMapping("/datatrace")
public class DataTraceAction extends BaseAction {
	
	@Autowired
	IDataTraceService dataTraceService;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		List<Map<String,Object>> list = dataTraceService.getSysClass();
		model.addAttribute("sysClassList",list);
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		IPage p = dataTraceService.queryDatatrace(condition);
		
		System.out.println("==================查询完毕==============");
		
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/traceDetail")
	public String traceDetail(Model model,HttpServletRequest request){
		String pkValue = request.getParameter("pkValue");
		String tableName = request.getParameter("tableName");
		String operateDate = request.getParameter("operateDate");
		
		model.addAttribute("pkValue",pkValue);
		model.addAttribute("tableName",tableName);
		model.addAttribute("operateDate",operateDate);
		return forward("trace_detail");
	}
	
	@ResponseBody
	@RequestMapping("/traceDetailPage")
	public String traceDetailPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		List<TraceDetailVo> dataList = dataTraceService.queryDatatraceDetail(condition);
		
		System.out.println("==================查询完毕==============");
		
		return sendMessage(JSON.toJSONString(dataList));
	}
}
