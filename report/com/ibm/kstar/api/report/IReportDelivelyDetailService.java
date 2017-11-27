package com.ibm.kstar.api.report;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.report.ReportDelivelyDetail;


public interface IReportDelivelyDetailService {
	
	//出货详细页面查询
	IPage getPage(PageCondition condition,String orgId) throws AnneException;
	
	//出货详细List查询
	List<ReportDelivelyDetail> getList(PageCondition condition, String orgId)throws AnneException;
	
	//出货详细导出
	List<List<Object>> getExcelData(List<ReportDelivelyDetail> delivelyDetailList)throws AnneException;
}
