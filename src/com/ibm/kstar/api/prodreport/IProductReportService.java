package com.ibm.kstar.api.prodreport;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

public interface IProductReportService {
	IPage query(PageCondition condition) throws AnneException;
	
	List<List<Object>>  proReportYanshenExport(PageCondition condition) throws AnneException;
}
