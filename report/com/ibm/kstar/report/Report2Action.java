package com.ibm.kstar.report;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.api.report.IReportService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.impl.report.HistoryAnalysisValue;
import com.ibm.kstar.impl.report.ReportGroupMap;
import com.ibm.kstar.impl.report.TypeValue;
import com.ibm.kstar.impl.report.Value;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/report")
public class Report2Action extends BaseAction{

	@Autowired
	IReportService reportService;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	IEmployeeService employeeService;

	@Autowired
	IContractLoanService loanService;
	
	@Autowired
	protected ILovGroupService lovGroupService;
	
	@RequestMapping("/planReceivableDetail")
	public String planReceivableDetail(String reportType, String prType , String orgIdOrEmployeeId, String currency ,String index,Model model){
		List<Map<String, Object>> list = reportService.getPlanReceivableDetail(reportType, prType, orgIdOrEmployeeId, index);
		model.addAttribute("list",list);
		return forward("planReceivableDetail");
	}
	
	@RequestMapping("/reportOrgReachedByProductType")
	public String reportOrgReachedByProductType(String reportType,String year,String orgId,String employeeId,String positionId,String currency,String flag,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(orgId)){
			orgId = getUserObject().getOrg().getId();
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(flag)){
			flag = "0";
		}
		if(StringUtil.isEmpty(positionId)){
			positionId =  getUserObject().getPosition().getId();
		}
		if(StringUtil.isEmpty(reportType)){
			reportType = "EMPLOYEE";
		}
		report(null,reportType,year, orgId,employeeId,positionId, currency, flag,model);
		
		List<TypeValue> list = null; 
		
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			list = reportService.getOrgActualByProductType(year, orgId,currency,flag);
		}else{
			list = reportService.getEmployeeActualByProductType(year, employeeId,positionId,currency,flag);
		}
		Map<String,Integer> customTypeMap = new ReportGroupMap<String,Integer>();
		for(TypeValue ctv : list){
			Integer count = customTypeMap.get(ctv.getCustomType());
			if(count == null){
				count = new Integer(1);
				customTypeMap.put(ctv.getCustomType(), count);
			}else{
				customTypeMap.put(ctv.getCustomType(), ++count);
			}
		}
		
		List<TypeValue> list2 = null;
		
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			list2 = reportService.getOrgActualByProductType4Charts(year, orgId,currency);
		}else{
			list2 = reportService.getEmployeeActualByProductType4Charts(year, employeeId,positionId,currency);
		}
		
		model.addAttribute("customTypeMap",customTypeMap);
		Collections.sort(list);
		model.addAttribute("list",list);
		
		//图表
		List<String> typeList = new ArrayList<String>();
		List<Map<String,Object>> valueList = new ArrayList<Map<String,Object>>();
		for(TypeValue tv : list2){
			typeList.add(tv.getCustomType());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", tv.getTotle());
			map.put("name", tv.getCustomType());
			valueList.add(map);
		}
		model.addAttribute("typeList",JSON.toJSONString(typeList));
		model.addAttribute("valueList",JSON.toJSONString(valueList));
		
		return forward("reportOrgReachedByProType");
	}
	
	@RequestMapping("/reportOrgReachedByCustomType")
	public String reportOrgReachedByCustomType(String reportType,String year,String orgId,String employeeId,String positionId,String currency,String flag,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(orgId)){
			orgId = getUserObject().getOrg().getId();
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(flag)){
			flag = "0";
		}
		if(StringUtil.isEmpty(employeeId)){
			employeeId = getUserObject().getEmployee().getId();
		}
		if(StringUtil.isEmpty(positionId)){
			positionId =  getUserObject().getPosition().getId();
		}
		if(StringUtil.isEmpty(reportType)){
			reportType = "EMPLOYEE";
		}
		report(null,reportType,year, orgId,employeeId,positionId, currency, flag,model);
		List<TypeValue> list = null;
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			list = reportService.getOrgActualByCustomType(year, orgId,currency,flag);
		}else{
			list = reportService.getEmployeeActualByCustomType(year, employeeId,positionId,currency,flag);
		}
		
		Map<String,Integer> customTypeMap = new ReportGroupMap<String,Integer>();
		Map<String,Map<String,Integer>> customChildMap = new HashMap<String,Map<String,Integer>>();
		for(TypeValue ctv : list){
			Integer count = customTypeMap.get(ctv.getCustomClass());
//			
			if(count == null){
				count = new Integer(1);
				customTypeMap.put(ctv.getCustomClass(), count);
			}else{
				customTypeMap.put(ctv.getCustomClass(), ++count);
			}
			
			Map<String,Integer> customChlMap = customChildMap.get(ctv.getCustomClass());
			Integer count1 = null;
			if(customChlMap == null){
				customChlMap = new ReportGroupMap<String,Integer>();
				count1 = customChlMap.get(ctv.getCustomType());
				
				if(count1 == null){
					count1 = new Integer(1);
					customChlMap.put(ctv.getCustomType(), count1);
				}
				else{
					customChlMap.put(ctv.getCustomType(), ++count1);
				}
				customChildMap.put(ctv.getCustomClass(), customChlMap);
			}else {
				count1 = customChlMap.get(ctv.getCustomType());
				if(count1 == null){
					count1 = new Integer(1);
					customChlMap.put(ctv.getCustomType(), count1);
				}
				else{
					customChlMap.put(ctv.getCustomType(), ++count1);
				}
				customChildMap.put(ctv.getCustomClass(), customChlMap);
			}
		}
		
		List<TypeValue> list2 = null;
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			list2 = reportService.getOrgActualByCustomType4Charts(year, orgId,currency);
		}else{
			list2 = reportService.getEmployeeActualByCustomType4Charts(year, employeeId,positionId,currency);
		}
		
		model.addAttribute("customTypeMap",customTypeMap);
		model.addAttribute("customChildMap",customChildMap);
		//Collections.sort(list);
		model.addAttribute("list",list);
		
		List<String> typeList = new ArrayList<String>();
		List<Map<String,Object>> valueList = new ArrayList<Map<String,Object>>();
		for(TypeValue tv : list2){
			typeList.add(tv.getCustomClass());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", tv.getTotle());
			map.put("name", tv.getCustomClass());
			valueList.add(map);
		}
		model.addAttribute("typeList",JSON.toJSONString(typeList));
		model.addAttribute("valueList",JSON.toJSONString(valueList));
		
		return forward("reportOrgReachedByCustomType");
	}
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@RequestMapping("/reportOrgReached")
	public String reportOrgReached(String treeOrgId,String reportType,String year,String orgId,String positionId,String employeeId,String currency,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(orgId)){
			orgId = getUserObject().getOrg().getId();
		}
		
		
		if(StringUtil.isEmpty(employeeId)){
			employeeId = getUserObject().getEmployee().getId();
		}
		if(StringUtil.isEmpty(positionId)){
			positionId = getUserObject().getPosition().getId();
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(reportType)){
			reportType = "EMPLOYEE";
		}
		
		report(treeOrgId,reportType,year, orgId,employeeId,positionId, currency,null, model);
		if(!StringUtil.isEmpty(treeOrgId)){
			orgId = treeOrgId;
		}
		Value contactVeri = null;//回款被核销金额
		Value contact = null;//回款计划金额
		Value delively = null;//出货金额
		Value actual = null;//
		Value last = null;
		Value invoiceNo = null;
		Double target = null;
		Value rate = null;
		Value rateContact = null;
		Value rateDelively = null;
		Value invoiceNoContact = null;//出货费用率
		Value invoiceNoDelively = null;//回款费用率
		Value yoy = null; //同比（今年和去年同比）
		Double planReceivable = null;
		Value receivable = null;
		List<Double> receivableList = null;
		List<Double> receivableList2 = null;
		List<Double> invoicedAndUnbilled = null;//财务应收歀（万元）
		List<Double> advancesReceived = null;//预收歀（万元）
		Double overDueSum = null;//逾期未发货总计金额
		Double rebateReportSum = null;//特价逾期未下单
		Double invoicingSum = null;//提前开票未出货总计金额
		Double rebateSum = null;//特价逾期未出货总金额
		Double expireSum = null;//特价逾期未出货总金额
		int bizoppReportScale = 0;//商机报备授权比例
		int bizoppOrderReportScale = 0;//报备落单率
		int reportOrderScale = 0;//已授权落单比例
		int bidReportScale = 0;//授权反馈率
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			contact = reportService.getOrgContact(year, orgId,currency);//获取当年回款的计划金额
			contactVeri = reportService.getOrgContactVeri(year, orgId,currency);//获取当年回款计划被核销金额
			delively = reportService.getOrgDelivly(year, orgId,currency);//获取出货的出货金额
			last = reportService.getOrgActual(String.valueOf((Integer.parseInt(year) - 1)), orgId, currency);
			actual = reportService.getOrgActual(year, orgId, currency);
			target = reportService.getOrgTarget(year, orgId, currency);
			receivableList = reportService.getOrgPlanReceivable(orgId, currency);
			receivableList2 = reportService.getOrgPlanReceivable2(orgId, currency);
			receivable = reportService.getOrgReceivable(year,orgId, currency);
			planReceivable = reportService.getOrgTotalPlanReceivable(year,orgId, currency);
			invoiceNo = reportService.getOrgInvoiceNo(year,orgId, currency,getUserObject());
			invoicedAndUnbilled = reportService.getOrgInvoicedAndUnbilled(year,orgId, currency);
			advancesReceived = reportService.getOrgAdvancesReceived(year,orgId, currency);
			overDueSum = reportService.getOverdueSum("ORG",orgId,currency,year);
			rebateReportSum = reportService.getRebateReportSum("ORG",orgId,currency,year);
			invoicingSum = reportService.getInvoicingSum("ORG",orgId,currency,year);
			rebateSum = reportService.getRebateSum("ORG",orgId,currency,year);
			expireSum = reportService.getExpireSum("ORG",orgId,currency,year);
			bizoppReportScale = reportService.getBizoppReportScale("ORG",getUserObject().getPosition().getId());
			bizoppOrderReportScale = reportService.getBizoppOrderReportScale("ORG",getUserObject().getPosition().getId());
			reportOrderScale = reportService.getReportOrderScale("ORG",getUserObject().getPosition().getId());
			bidReportScale = reportService.getBidReportScale("ORG",getUserObject().getPosition().getId());
		}else{
			contact = reportService.getEmpContact(year, employeeId,positionId,currency);//获取当年回款的计划金额
			contactVeri = reportService.getEmpContactVeri(year, employeeId,positionId,currency);//获取当年回款计划被核销金额
			delively = reportService.getEmpDelivly(year, employeeId,positionId,currency);//获取出货的出货金额
			last = reportService.getEmployeeActual(String.valueOf((Integer.parseInt(year) - 1)), employeeId,positionId,currency);
			actual = reportService.getEmployeeActual(year, employeeId,positionId, currency);
			target = reportService.getEmployeeTarget(year, positionId, currency);
			receivableList = reportService.getEmployeePlanReceivable(positionId, currency);
			receivableList2 = reportService.getEmployeePlanReceivable2(positionId, currency);
			receivable = reportService.getEmployeeReceivable(year,positionId, currency);
			invoiceNo = reportService.getEmployeeInvoiceNo(year,positionId, currency);
			planReceivable = reportService.getEmployeeTotalPlanReceivable(year,positionId, currency);
			invoicedAndUnbilled = reportService.getEmployeeInvoicedAndUnbilled(year,positionId, currency);
			advancesReceived = reportService.getEmployeeAdvancesReceived(year,positionId, currency);
			overDueSum = reportService.getOverdueSum("Employee",positionId,currency,year);
			rebateReportSum = reportService.getRebateReportSum("Employee",positionId,currency,year);
			invoicingSum = reportService.getInvoicingSum("Employee",positionId,currency,year);
			rebateSum = reportService.getRebateSum("Employee",positionId,currency,year);
			expireSum = reportService.getExpireSum("Employee",positionId,currency,year);
			bizoppReportScale = reportService.getBizoppReportScale("Employee",positionId);
			bizoppOrderReportScale = reportService.getBizoppOrderReportScale("Employee",positionId);
			reportOrderScale = reportService.getReportOrderScale("Employee",positionId);
			bidReportScale = reportService.getBidReportScale("Employee",positionId);
		}
		rate = getRate(target,actual);
		rateContact = getRateContact(contact, contactVeri);//合同回款率(%)
		rateDelively = getRateDelively(delively, contactVeri);//出货回款率(%)
		yoy = getYoy(actual,last);//同比数据
		
		
		invoiceNoContact = getInvoiceNoContact(contact,invoiceNo);//回款费用率
		invoiceNoDelively = getInvoiceNoContact(delively,invoiceNo);//出货费用率

		//String positionId = getUserObject().getPosition().getId();
		UserObject user = new UserObject(orgId, positionId, employeeId);
		Employee employee = new Employee();
		employee.setId(employeeId);
		user.setEmployee(employee);

		LovMember position = new LovMember();
		position.setId(positionId);
		user.setPosition(position);

		LovMember org = new LovMember();
		org.setId(orgId);
		user.setOrg(org);

		//借货未核销
		Condition condition = new Condition();
		condition.setCondition("currency", currency);
		condition.setCondition("reportType", reportType);
		condition.getFilterObject().setUser(user);

		BigDecimal unverificationLoan = loanService.unverificationSum(condition);
		model.addAttribute("unverificationLoan",unverificationLoan);

		model.addAttribute("lastList",JSON.toJSONString(last.toList()));
		model.addAttribute("contactList",JSON.toJSONString(rateContact.toListOne()));
		model.addAttribute("actualListOne",JSON.toJSONString(actual.toListOne()));
		model.addAttribute("actualList",JSON.toJSONString(actual.toList()));
		model.addAttribute("delivelyList",JSON.toJSONString(rateDelively.toListOne()));
		model.addAttribute("target",target);
		model.addAttribute("rateList",JSON.toJSONString(rate.toListOne()));
		model.addAttribute("yoyList",JSON.toJSONString(yoy.toList()));//获取同比比例
		model.addAttribute("receivableList",receivableList);
		model.addAttribute("receivableList2",receivableList2);
		model.addAttribute("receivable",receivable);
		model.addAttribute("rebateReportSum",rebateReportSum);
		model.addAttribute("invoiceNo",invoiceNo);
		model.addAttribute("invoiceNoContact",invoiceNoContact);
		model.addAttribute("invoiceNoDelively",invoiceNoDelively);
		model.addAttribute("invoicedAndUnbilled",invoicedAndUnbilled);
		model.addAttribute("advancesReceived",advancesReceived);
		model.addAttribute("planReceivable",planReceivable);
		model.addAttribute("overDueSum", overDueSum);
		model.addAttribute("invoicingSum", invoicingSum);
		model.addAttribute("rebateSum", rebateSum);
		model.addAttribute("expireSum", expireSum);
		model.addAttribute("bizoppReportScale", bizoppReportScale);
		model.addAttribute("bizoppOrderReportScale", bizoppOrderReportScale);
		model.addAttribute("reportOrderScale", reportOrderScale);		
		model.addAttribute("bidReportScale", bidReportScale);		
		return forward("reportOrgReached");
	}

	/**
	 * 今去年同比计算
	 * @param actual
	 * @param last
	 * @return
	 */
	private Value getYoy(Value actual, Value last) {
		Value yoy = new Value();
		yoy.setM01((last.getM01() > 0 ? (actual.getM01()-last.getM01()) / last.getM01() : 0)* 100);
		yoy.setM02((last.getM02() > 0 ? (actual.getM02()-last.getM02()) / last.getM02() : 0)* 100);
		yoy.setM03((last.getM03() > 0 ? (actual.getM03()-last.getM03()) / last.getM03() : 0)* 100);
		yoy.setM04((last.getM04() > 0 ? (actual.getM04()-last.getM04()) / last.getM04() : 0)* 100);
		yoy.setM05((last.getM05() > 0 ? (actual.getM05()-last.getM05()) / last.getM05() : 0)* 100);
		yoy.setM06((last.getM06() > 0 ? (actual.getM06()-last.getM06()) / last.getM06() : 0)* 100);
		yoy.setM07((last.getM07() > 0 ? (actual.getM07()-last.getM07()) / last.getM07() : 0)* 100);
		yoy.setM08((last.getM08() > 0 ? (actual.getM08()-last.getM08()) / last.getM08() : 0)* 100);
		yoy.setM09((last.getM09() > 0 ? (actual.getM09()-last.getM09()) / last.getM09() : 0)* 100);
		yoy.setM10((last.getM10() > 0 ? (actual.getM10()-last.getM10()) / last.getM10() : 0)* 100);
		yoy.setM11((last.getM11() > 0 ? (actual.getM11()-last.getM11()) / last.getM11() : 0)* 100);
		yoy.setM12((last.getM12() > 0 ? (actual.getM12()-last.getM12()) / last.getM12() : 0)* 100);
		return yoy;
	}
	
	private void report(String treeOrgId,String reportType,String year,String orgId,String employeeId,String positionId,String currency,String flag,Model model){

		List<Integer> years = new ArrayList<Integer>();
		int startYear = 2016;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for(int i=startYear;i<=currentYear;i++){
			years.add(i);
		}
		
		
		model.addAttribute("reportType",reportType);
		model.addAttribute("years",years);
		model.addAttribute("flag",flag);
		model.addAttribute("currentYear",currentYear);
		model.addAttribute("currency",currency);
		model.addAttribute("year",year);
		LovMember org = lovMemberService.get(orgId);
		Employee employee = employeeService.get(employeeId);
		model.addAttribute("employee",employee);
		model.addAttribute("employeeId",employeeId);
		model.addAttribute("org",org);
		model.addAttribute("orgId",orgId);
		if(!StringUtil.isNullOrEmpty(treeOrgId)) {
			LovMember treeOrg = lovMemberService.get(treeOrgId);
			model.addAttribute("treeOrgId",treeOrg.getId());
			model.addAttribute("treeOrgName",treeOrg.getName());
		}else {
			model.addAttribute("treeOrgId",orgId);
			model.addAttribute("treeOrgName",org.getName());
		}
		LovMember position = lovMemberService.get(positionId);
		model.addAttribute("position",position);
		model.addAttribute("positionId", positionId);
		model.addAttribute("posId", getUserObject().getPosition().getId());
		
		LovMember positionInOrg = corePermissionService.getOrgByPositionId(getUserObject().getPosition().getId());
		if("Y".equals(positionInOrg.getOptTxt2())){
			model.addAttribute("rootId", getUserObject().getOrg().getId());
		}else{
			model.addAttribute("rootId","XXXX");//没有权限
		}
	}
	
	private Value getRate(Double target, Value actual) {
		Value rate = new Value();
		rate.setM01((target > 0 ? actual.getM01() / target : 0)* 100);
		rate.setM02((target > 0 ? actual.getM02() / target : 0)* 100);
		rate.setM03((target > 0 ? actual.getM03() / target : 0)* 100);
		rate.setM04((target > 0 ? actual.getM04() / target : 0)* 100);
		rate.setM05((target > 0 ? actual.getM05() / target : 0)* 100);
		rate.setM06((target > 0 ? actual.getM06() / target : 0)* 100);
		rate.setM07((target > 0 ? actual.getM07() / target : 0)* 100);
		rate.setM08((target > 0 ? actual.getM08() / target : 0)* 100);
		rate.setM09((target > 0 ? actual.getM09() / target : 0)* 100);
		rate.setM10((target > 0 ? actual.getM10() / target : 0)* 100);
		rate.setM11((target > 0 ? actual.getM11() / target : 0)* 100);
		rate.setM12((target > 0 ? actual.getM12() / target : 0)* 100);
		return rate;
	}
	
	private Value getRateContact(Value actualContact, Value actual) {
		Value rate = new Value();
		rate.setM01((actualContact.getM01() > 0 ? actual.getM01() / actualContact.getM01() : 0)* 100);
		rate.setM02((actualContact.getM02() > 0 ? actual.getM02() / actualContact.getM02() : 0)* 100);
		rate.setM03((actualContact.getM03() > 0 ? actual.getM03() / actualContact.getM03() : 0)* 100);
		rate.setM04((actualContact.getM04() > 0 ? actual.getM04() / actualContact.getM04() : 0)* 100);
		rate.setM05((actualContact.getM05() > 0 ? actual.getM05() / actualContact.getM05() : 0)* 100);
		rate.setM06((actualContact.getM06() > 0 ? actual.getM06() / actualContact.getM06() : 0)* 100);
		rate.setM07((actualContact.getM07() > 0 ? actual.getM07() / actualContact.getM07() : 0)* 100);
		rate.setM08((actualContact.getM08() > 0 ? actual.getM08() / actualContact.getM08() : 0)* 100);
		rate.setM09((actualContact.getM09() > 0 ? actual.getM09() / actualContact.getM09() : 0)* 100);
		rate.setM10((actualContact.getM10() > 0 ? actual.getM10() / actualContact.getM10() : 0)* 100);
		rate.setM11((actualContact.getM11() > 0 ? actual.getM11() / actualContact.getM11() : 0)* 100);
		rate.setM12((actualContact.getM12() > 0 ? actual.getM12() / actualContact.getM12() : 0)* 100);
		rate.setTotle((actualContact.getTotle() > 0 ? actual.getTotle() / actualContact.getTotle() : 0)* 100);
		return rate;
	}
	

	private Value getRateDelively(Value delively, Value actual) {
		Value rate = new Value();
		rate.setM01((delively.getM01() > 0 ? actual.getM01() / delively.getM01() : 0)* 100);
		rate.setM02((delively.getM02() > 0 ? actual.getM02() / delively.getM02() : 0)* 100);
		rate.setM03((delively.getM03() > 0 ? actual.getM03() / delively.getM03() : 0)* 100);
		rate.setM04((delively.getM04() > 0 ? actual.getM04() / delively.getM04() : 0)* 100);
		rate.setM05((delively.getM05() > 0 ? actual.getM05() / delively.getM05() : 0)* 100);
		rate.setM06((delively.getM06() > 0 ? actual.getM06() / delively.getM06() : 0)* 100);
		rate.setM07((delively.getM07() > 0 ? actual.getM07() / delively.getM07() : 0)* 100);
		rate.setM08((delively.getM08() > 0 ? actual.getM08() / delively.getM08() : 0)* 100);
		rate.setM09((delively.getM09() > 0 ? actual.getM09() / delively.getM09() : 0)* 100);
		rate.setM10((delively.getM10() > 0 ? actual.getM10() / delively.getM10() : 0)* 100);
		rate.setM11((delively.getM11() > 0 ? actual.getM11() / delively.getM11() : 0)* 100);
		rate.setM12((delively.getM12() > 0 ? actual.getM12() / delively.getM12() : 0)* 100);
		rate.setTotle((delively.getTotle() > 0 ? actual.getTotle() / delively.getTotle() : 0)* 100);
		return rate;
	}
	
	@RequestMapping("/reportHistoryAnalysis")
	public String reportHistoryAnalysis(String reportType,String year,String orgId,String employeeId,String currency,String flag,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(orgId)){
			orgId = getUserObject().getOrg().getId();
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(flag)){
			flag = "0";
		}
		if(StringUtil.isEmpty(reportType)){
			reportType = "EMPLOYEE";
		}
		
		List<Integer> years = new ArrayList<Integer>();
		int startYear = 2016;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		String historyYear = String.valueOf(currentYear-5);
		model.addAttribute("reportType",reportType);
		model.addAttribute("years",years);
		model.addAttribute("flag",flag);
		model.addAttribute("currentYear",currentYear);
		model.addAttribute("currency",currency);
		model.addAttribute("year",year);
		LovMember org = lovMemberService.get(orgId);
		Employee employee = employeeService.get(employeeId);
		model.addAttribute("employee",employee);
		model.addAttribute("employeeId",employeeId);
		model.addAttribute("org",org);
		model.addAttribute("orgId",orgId);
		
		LovMember positionInOrg = corePermissionService.getOrgByPositionId(getUserObject().getPosition().getId());
		if("Y".equals(positionInOrg.getOptTxt2())){
			model.addAttribute("rootId", getUserObject().getOrg().getId());
		}else{
			model.addAttribute("rootId","XXXX");//没有权限
		}
		
		List<HistoryAnalysisValue> list = null;
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			list = reportService.getOrgActualHistory(year,historyYear,orgId,currency,flag);
		}else{
			list = reportService.getEmployeeActualHistory(year,historyYear,employeeId,currency,flag);
		}
		
		model.addAttribute("list",list);
		model.addAttribute("year",Integer.parseInt(year));
		return forward("reportHistoryAnalysis");
	}

	/**
	 * 逾期未发货明细
	 */
	@NoRight
	@RequestMapping("/reportOverdueList")
	public String reportOverdueList(String reportType, String orgIdOrEmployeeId,String currency,String year,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		model.addAttribute("currency", currency);
		model.addAttribute("year", year);
		return forward("reportOverdue");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/reportOverdue")
	public String reportOverdue(PageCondition condition,String reportType, String orgIdOrEmployeeId,String currency,String year, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getReportOverdue(condition,reportType,orgIdOrEmployeeId,currency,year);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 提前开票未出货明细
	 */
	@NoRight
	@RequestMapping("/reportInvoicingList")
	public String reportInvoicingList(String reportType, String orgIdOrEmployeeId,String currency,String year,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		model.addAttribute("currency", currency);
		model.addAttribute("year", year);
		return forward("reportInvoicing");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/reportInvoicing")
	public String reportInvoicing(PageCondition condition,String reportType, String orgIdOrEmployeeId,String currency,String year, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getReportInvoicing(condition,reportType,orgIdOrEmployeeId,currency,year);
		return sendSuccessMessage(p);
	}

	/**
	 * 特价逾期未出货明细
	 */
	@NoRight
	@RequestMapping("/reportRebateList")
	public String reportRebateList(String reportType, String orgIdOrEmployeeId,String currency,String year,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		model.addAttribute("currency", currency);
		model.addAttribute("year", year);
		return forward("reportRebate");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/reportRebate")
	public String reportRebate(PageCondition condition,String reportType, String orgIdOrEmployeeId,String currency,String year, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getReportRebate(condition,reportType,orgIdOrEmployeeId,currency,year);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 7天到期订单
	 */
	@NoRight
	@RequestMapping("/reportExpireList")
	public String reportExpireList(String reportType, String orgIdOrEmployeeId,String currency,String year,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		model.addAttribute("currency", currency);
		model.addAttribute("year", year);
		return forward("reportExpire");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/reportExpire")
	public String reportExpire(PageCondition condition,String reportType, String orgIdOrEmployeeId,String currency,String year, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getReportExpire(condition,reportType,orgIdOrEmployeeId,currency,year);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 借货未核销页面
	 */
	@NoRight
	@RequestMapping("/unverificationLoanList")
	public String unverificationLoanList(String reportType, String orgId,String positionId,String employeeId,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgId", orgId);
		model.addAttribute("positionId", positionId);
		model.addAttribute("employeeId", employeeId);
		return forward("unverificationLoan");
	}

    /**
     * 借货未核销分页数据
     * @param condition
     * @param request
     * @return
     */
	@NoRight
	@ResponseBody
	@RequestMapping("/unverificationLoanPage")
	public String unverificationLoan(PageCondition condition, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String orgId = condition.getStringCondition("orgId");
		String positionId = condition.getStringCondition("positionId");
		String employeeId = condition.getStringCondition("employeeId");

		UserObject user = new UserObject(orgId, positionId, employeeId);
		Employee employee = new Employee();
		employee.setId(employeeId);
		user.setEmployee(employee);

		LovMember position = new LovMember();
		position.setId(positionId);
		user.setPosition(position);

		LovMember org = new LovMember();
		org.setId(orgId);
		user.setOrg(org);

        condition.getFilterObject().setUser(user);
        IPage page = loanService.unverificationLoanPage(condition);
		return sendSuccessMessage(page);
	}

	
	private Value getInvoiceNoContact(Value actualContact, Value invoiceNo) {
		Value rate = new Value();
		DecimalFormat df = new DecimalFormat("#.00");  
		if(actualContact.getM01()>0&&invoiceNo.getM01()>0) {
			rate.setM01(formatDouble4((invoiceNo.getM01() / ((actualContact.getM01()-0)/1.17))));
		}else {
			rate.setM01(0d);
		}
		if(actualContact.getM02()>0&&invoiceNo.getM02()>0) {
			rate.setM02(formatDouble4((invoiceNo.getM02() / ((actualContact.getM02()-0)/1.17))));
		}else {
			rate.setM02(0d);
		}
		if(actualContact.getM03()>0&&invoiceNo.getM03()>0) {
			rate.setM03(formatDouble4((invoiceNo.getM03() / ((actualContact.getM03()-0)/1.17))));
		}else {
			rate.setM03(0d);
		}
		if(actualContact.getM04()>0&&invoiceNo.getM04()>0) {
			rate.setM04(formatDouble4((invoiceNo.getM04() / ((actualContact.getM04()-0)/1.17))));
		}else {
			rate.setM04(0d);
		}
		if(actualContact.getM05()>0&&invoiceNo.getM05()>0) {
			rate.setM05(formatDouble4((invoiceNo.getM05() / ((actualContact.getM05()-0)/1.17))));
		}else {
			rate.setM05(0d);
		}
		if(actualContact.getM06()>0&&invoiceNo.getM06()>0) {
			rate.setM06(formatDouble4((invoiceNo.getM06() / ((actualContact.getM06()-0)/1.17))));
		}else {
			rate.setM06(0d);
		}
		if(actualContact.getM01()>0&&invoiceNo.getM01()>0) {
			rate.setM07(formatDouble4((invoiceNo.getM07() / ((actualContact.getM07()-0)/1.17))));
		}else {
			rate.setM07(0d);
		}
		if(actualContact.getM08()>0&&invoiceNo.getM08()>0) {
			rate.setM08(formatDouble4((invoiceNo.getM08() / ((actualContact.getM08()-0)/1.17))));
		}else {
			rate.setM08(0d);
		}
		if(actualContact.getM09()>0&&invoiceNo.getM09()>0) {
			rate.setM09(formatDouble4((invoiceNo.getM09() / ((actualContact.getM09()-0)/1.17))));
		}else {
			rate.setM09(0d);
		}
		if(actualContact.getM10()>0&&invoiceNo.getM10()>0) {
			rate.setM10(formatDouble4((invoiceNo.getM10() / ((actualContact.getM10()-0)/1.17))));
		}else {
			rate.setM10(0d);
		}
		if(actualContact.getM11()>0&&invoiceNo.getM11()>0) {
			rate.setM11(formatDouble4((invoiceNo.getM11() / ((actualContact.getM11()-0)/1.17))));
		}else {
			rate.setM11(0d);
		}
		if(actualContact.getM12()>0&&invoiceNo.getM12()>0) {
			rate.setM12(formatDouble4((invoiceNo.getM12() / ((actualContact.getM12()-0)/1.17))));
		}else {
			rate.setM12(0d);
		}
		if(actualContact.getTotle()>0&&invoiceNo.getTotle()>0) {
			rate.setTotle(formatDouble4((invoiceNo.getTotle() / ((actualContact.getTotle()-0)/1.17))));
		}else {
			rate.setTotle(0d);
		}
		return rate;
	}
	
	private  double formatDouble4(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(d));
    }

	/**
	 * 报备授权
	 */
	@NoRight
	@RequestMapping("/bizoppReportScaleList")
	public String bizoppReportScaleList(String reportType, String orgIdOrEmployeeId,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		return forward("bizoopReportScale");
	}

	/**
	 * 报备授权明细
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/bizoppReportScale")
	public String bizoppReportScale(PageCondition condition,String reportType, String orgIdOrEmployeeId, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getBizoopReportScale(condition,reportType,orgIdOrEmployeeId);
		return sendSuccessMessage(p);
	}

	
	/**
	 * 报备落单率
	 */
	@NoRight
	@RequestMapping("/bizoppOrderReportScaleList")
	public String bizoppOrderReportScaleList(String reportType, String orgIdOrEmployeeId,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		return forward("bizoppOrderReportScale");
	}
	
	/**
	 * 报备落单率明细
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/bizoppOrderReportScale")
	public String bizoppOrderReportScale(PageCondition condition,String reportType, String orgIdOrEmployeeId, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getBizoopOrderReportScale(condition,reportType,orgIdOrEmployeeId);
		return sendSuccessMessage(p);
	}
	
	
	/**
	 * 已授权落单
	 */
	@NoRight
	@RequestMapping("/reportOrderList")
	public String reportOrderList(String reportType, String orgIdOrEmployeeId,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		return forward("reportOrderList");
	}
	
	/**
	 * 已授权明细
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/reportOrder")
	public String reportOrder(PageCondition condition,String reportType, String orgIdOrEmployeeId, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getReportOrder(condition,reportType,orgIdOrEmployeeId);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 授权反馈率
	 */
	@NoRight
	@RequestMapping("/bidReportScaleList")
	public String bidReportScaleList(String reportType, String orgIdOrEmployeeId,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		return forward("bidReportScale");
	}
	
	/**
	 * 授权反馈率明细
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/bidReportScale")
	public String bidReportScale(PageCondition condition,String reportType, String orgIdOrEmployeeId, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getBidReportScale(condition,reportType,orgIdOrEmployeeId);
		return sendSuccessMessage(p);
	}

	/**
	 * 特价逾期未下单
	 */
	@NoRight
	@RequestMapping("/reportRebateOverOrderList")
	public String reportRebateOverOrderList(String reportType, String orgIdOrEmployeeId,Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("orgIdOrEmployeeId", orgIdOrEmployeeId);
		return forward("reportRebateOverOrder");
	}

	/**
	 * 特价逾期未下单明细
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/reportRebateOverOrder")
	public String reportRebateOverOrder(PageCondition condition,String reportType, String orgIdOrEmployeeId, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = reportService.getReportRebateOverOrder(condition,reportType,orgIdOrEmployeeId);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 占比分析
	 */
	@NoRight
	@RequestMapping("/proportionAnalysis")
	public String proportionAnalysis(String reportType,String year,String orgId,String employeeId,String positionId,String currency,String flag,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(orgId)){
			orgId = getUserObject().getOrg().getId();
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(flag)){
			flag = "0";
		}
		if(StringUtil.isEmpty(employeeId)){
			employeeId = getUserObject().getEmployee().getId();
		}
		if(StringUtil.isEmpty(positionId)){
			positionId =  getUserObject().getPosition().getId();
		}
		if(StringUtil.isEmpty(reportType)){
			reportType = "EMPLOYEE";
		}
		report(null,reportType,year, orgId,employeeId,positionId, currency, flag,model);
		
		List<TypeValue>	orgList = null;
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			orgList = reportService.getOrgActualByCustomType4Charts(year, orgId,currency);
		}else{
			orgList = reportService.getEmployeeActualByCustomType4Charts(year, employeeId,positionId,currency);
		}
		
		List<TypeValue> employeeList = null;
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			employeeList = reportService.getOrgActualByProductType(year, orgId,currency,flag);
		}else{
			employeeList = reportService.getEmployeeActualByProductType(year, employeeId,positionId,currency,flag);
		}
		
		List<String> orgTypeList = new ArrayList<String>();
		List<Map<String,Object>> orgValueList = new ArrayList<Map<String,Object>>();
		
		List<String> employeeTypeList = new ArrayList<String>();
		List<Map<String,Object>> employeeValueList = new ArrayList<Map<String,Object>>();
		
		for(TypeValue tv : orgList){
			orgTypeList.add(tv.getCustomClass());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", tv.getTotle());
			map.put("name", tv.getCustomClass());
			orgValueList.add(map);
		}
		
		for(TypeValue tv : employeeList){
			employeeTypeList.add(tv.getCustomType());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", tv.getTotle());
			map.put("name", tv.getCustomType());
			employeeValueList.add(map);
		}
		
		model.addAttribute("orgTypeList",JSON.toJSONString(orgTypeList));
		model.addAttribute("orgValueList",JSON.toJSONString(orgValueList));
		model.addAttribute("employeeTypeList",JSON.toJSONString(employeeTypeList));
		model.addAttribute("employeeValueList",JSON.toJSONString(employeeValueList));
		
		return forward("proportionAnalysis");
	}
	
	/**
	 * 接单量
	 * @param treeOrgId
	 * @param reportType
	 * @param year
	 * @param orgId
	 * @param positionId
	 * @param employeeId
	 * @param currency
	 * @param model
	 * @return
	 */
	@NoRight
	@RequestMapping("/orderQuantity")
	public String orderQuantity(String reportType,String year,String orgId,String positionId,String employeeId,String currency,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(orgId)){
			orgId = getUserObject().getOrg().getId();
		}
		
		
		if(StringUtil.isEmpty(employeeId)){
			employeeId = getUserObject().getEmployee().getId();
		}
		if(StringUtil.isEmpty(positionId)){
			positionId = getUserObject().getPosition().getId();
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(reportType)){
			reportType = "EMPLOYEE";
		}
		
		Value orderQuantity = null;//接单量
		Value delivery = null;//已出货
		Value notDelivery = null;//未出货
		if(reportType != null && "ORG".equals(reportType.toUpperCase())){
			orderQuantity = reportService.getOrderQuantityByOrg(year, orgId, currency);
			delivery = reportService.getDeliveryByOrg(year, orgId, currency);
			notDelivery = reportService.getNotDelivery(orderQuantity,delivery);
		}else{
			orderQuantity = reportService.getOrderQuantityByEmployee(year, positionId,currency);
			delivery = reportService.getDeliveryByEmployee(year, positionId,currency);
			notDelivery = reportService.getNotDelivery(orderQuantity,delivery);
		}

		model.addAttribute("orderQuantity",JSON.toJSONString(orderQuantity.toList()));
		model.addAttribute("delivery",JSON.toJSONString(delivery.toList()));
		model.addAttribute("notDelivery",JSON.toJSONString(notDelivery.toList()));
		model.addAttribute("currency",currency);
		return forward("orderQuantity");
	}
	
	@NoRight
	@RequestMapping("/selectLovTree")
	public String selectLovTree(String pickerId,String groupId,String leafFlag,String rootId,String opType,Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("groupId",groupId);
		model.addAttribute("leafFlag",leafFlag);
		model.addAttribute("rootId",rootId);
		model.addAttribute("opType",opType);
		return forward("lovTree");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/tree")
	public String tree(Condition condition, HttpServletRequest request) {
		return contructTree(condition,request,2);
	}
	
	private String contructTree(Condition condition, HttpServletRequest request,int optType){
		ActionUtil.requestToCondition(condition, request);
		String groupCode = condition.getStringCondition("groupCode");
		String groupId = condition.getStringCondition("groupId");
		String leafFlag = condition.getStringCondition("leafFlag");
		if (StringUtil.isEmpty(groupId) && StringUtil.isEmpty(groupCode)) {
			throw new AnneException("无效的参数访问");
		}
		if (StringUtil.isEmpty(groupId)) {
			LovGroup group = lovGroupService.getByCode(groupCode);
			if (group == null) {
				throw new AnneException("无效的参数访问");
			}
			groupId = group.getId();
		}
		String parentId = condition.getStringCondition("id");
		String rootId = condition.getStringCondition("rootId");

		List<LovMember> list = null;
		
		list = reportService.getLovList(rootId,groupId,leafFlag,parentId);
		return sendSuccessMessage(list);
	}
}
