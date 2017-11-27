package com.ibm.kstar.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;

import com.ibm.kstar.api.report.IReportDelivelyDetailService;
import com.ibm.kstar.entity.report.ReportDelivelyDetail;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/report")
public class ReportDelivelyDetailAction extends BaseAction{
	@Autowired
	IReportDelivelyDetailService service;
	
	@RequestMapping("/reportDelivelyDetail")
	public String delivelyDetail(PageCondition condition,HttpServletRequest request){
		return forward("reportDeliveryList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/reportDelivelyDetail/page")
	public String delivelyDetailPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = service.getPage(condition, getUserObject().getOrg().getId());
		return sendSuccessMessage(page);
	}
	
	/**
	 * 
	 * exportReceipts:导出出货详细报表. <br/> 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@RequestMapping("/reportDelivelyDetail/export")
	public void exportReceipts(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
		List<ReportDelivelyDetail> receiptsList = service.getList(condition,getUserObject().getOrg().getId());
		List<List<Object>> dataList  = service.getExcelData(receiptsList);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		ExcelUtil.exportExcel(response, dataList, "出货详细报表_"+sdf.format(new Date()));
	}
}
