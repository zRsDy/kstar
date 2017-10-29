package com.ibm.kstar.api.report;

import java.util.List;
import java.util.Map;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.impl.report.HistoryAnalysisValue;
import com.ibm.kstar.impl.report.TotalValue;
import com.ibm.kstar.impl.report.TypeValue;
import com.ibm.kstar.impl.report.Value;

public interface IReportService {
	
	Double getOrgTarget(String year,String orgId,String currency);
	
	Value getOrgActual(String year,String orgId,String currency);
	
	Double getEmployeeTarget(String year,String employeeId,String currency);
	
	Value getEmployeeActual(String year,String employeeId,String positionId,String currency);
	
	//////////////////////
	
	List<TypeValue> getEmployeeActualByCustomType(String year, String employeeId,String positionId,String currency,String flag);
	
	List<TypeValue> getOrgActualByCustomType(String year, String orgId,String currency,String flag);

	//////////////////////
	
	List<TypeValue> getEmployeeActualByCustomType4Charts(String year, String employeeId,String positionId,String currency);
	
	List<TypeValue> getOrgActualByCustomType4Charts(String year, String orgId,String currency);
	
	//////////////////////
	List<TypeValue> getEmployeeActualByProductType(String year, String employeeId,String positionId,String currency,String flag);
	
	List<TypeValue> getOrgActualByProductType(String year, String orgId,String currency,String flag);
	
	/////////////////////
	List<TypeValue> getEmployeeActualByProductType4Charts(String year, String employeeId,String positionId,String currency);
	
	List<TypeValue> getOrgActualByProductType4Charts(String year, String orgId,String currency);
	
	///////////////////////////////////////////
	
	List<TotalValue> getRankReport(String year,String rankHeaderId,String currency);

	
	///////////////////////////////////
	List<Double> getOrgPlanReceivable(String orgId,String currency);

	List<Double> getEmployeePlanReceivable(String employeeId, String currency);
	
	
	List<Double> getOrgPlanReceivable2(String orgId, String currency);
	
	List<Double> getEmployeePlanReceivable2(String employeeId, String currency);
	///////////////////////////////////
	
	List<Map<String,Object>> getPlanReceivableDetail(String reportType, String prType , String orgIdOrEmployeeId,String index /* ,String currency */);
	
	
	Value getOrgReceivable(String year, String orgId, String currency);
	
	Value getEmployeeReceivable(String year, String employeeId, String currency);

	Double getOrgTotalPlanReceivable(String year, String orgId, String currency);

	Double getEmployeeTotalPlanReceivable(String year, String employeeId, String currency);

	Value getOrgDelivly(String year, String orgId, String currency);

	Value getOrgContact(String year, String orgId, String currency);
	
	Value getEmployeeInvoiceNo(String year, String employeeId, String currency);
	
	Value getOrgInvoiceNo(String year, String orgId, String currency,UserObject  user);
	
	List<Double> getOrgInvoicedAndUnbilled(String year, String orgId, String currency);

	List<Double> getEmployeeInvoicedAndUnbilled(String year, String employeeId, String currency);
	
	List<Double> getOrgAdvancesReceived(String year, String orgId, String currency);

	List<Double> getEmployeeAdvancesReceived(String year, String employeeId, String currency);

	Value getEmpDelivly(String year, String orgId,String positionId, String currency);

	Value getEmpContact(String year, String orgId,String positionId, String currency);
	
	List<HistoryAnalysisValue> getOrgActualHistory(String year, String historyYear,String orgId,String currency,String flag);

	List<HistoryAnalysisValue> getEmployeeActualHistory(String year, String historyYear,String employeeId,String currency,String flag);

	IPage getReportOverdue(PageCondition condition, String reportType, String orgIdOrEmployeeId);

	Double getOverdueSum(String type,String id);

	int getBizoppReportScale(String string, String id);

	IPage getBizoopReportScale(PageCondition condition, String reportType, String orgIdOrEmployeeId);

	IPage getBizoopOrderReportScale(PageCondition condition, String reportType, String orgIdOrEmployeeId);

	int getReportOrderScale(String string, String id);

	int getBizoppOrderReportScale(String type, String id);

	IPage getReportOrder(PageCondition condition, String reportType, String orgIdOrEmployeeId);
	
	int getBidReportScale(String type, String id);
	
	IPage getBidReportScale(PageCondition condition, String reportType, String orgIdOrEmployeeId);
	
	void updatePositionId();

	Value getOrgContactVeri(String year, String orgId, String currency);

	Value getEmpContactVeri(String year, String employeeId, String positionId, String currency); 
}
