package com.ibm.kstar.impl.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.report.IReportService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.report.BizoopReportScaleVO;
import com.ibm.kstar.entity.report.KstarAnltgt;


@Service
@Remote
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ReportServiceImpl implements IReportService{

	@Autowired
	BaseDao baseDao;

	@Autowired
	IUserService userService;
	
	@Override
	public Double getOrgTarget(String year, String orgId,String currency) {
		KstarAnltgt anltgt = baseDao.findUniqueEntity(" select a from KstarAnltgt a,LovMember b where a.ctype = b.id and b.code = '10' and a.depid = ? and a.annual = ? ",new Object[]{orgId,year});
		if(anltgt == null){
			return 0d;
		}
		return anltgt.getAnlTrg();
	}

	@Override
	public Value getOrgActual(String year, String orgId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount),sum(v.usd_amount),v.year,v.month")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_org_path like ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+orgId+"%"});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	/**
	 * 合同回款计划金额获取
	 */
	@Override
	public Value getOrgContact(String year, String orgId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.n_plan_amount) as rmb_amount,sum(v.n_usd_plan_amount) as usd_amount ,v.year,v.month")
				.append(" from kstar_verification_v_1 v where v.year = ? ") 
				.append(" and v.C_SALESMAN_ORG_path like ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+orgId+"%"});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	/**
	 * 合同回款被核销金额获取
	 */
	@Override
	public Value getOrgContactVeri(String year, String orgId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.n_veri_amount) as rmb_amount,sum(v.n_usd_veri_amount) as usd_amount ,v.year,v.month")
				.append(" from kstar_verification_v_1 v where v.year = ? ") 
				.append(" and v.C_SALESMAN_ORG_path like ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+orgId+"%"});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	@Override
	public Value getOrgDelivly(String year, String orgId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount ,v.year,v.month")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_org_path like ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.year,v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+orgId+"%"});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	/**
	 * 合同回款计划金额获取
	 */
	@Override
	public Value getEmpContact(String year, String employeeId,String positionId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.n_plan_amount) as rmb_amount,sum(v.n_usd_plan_amount) as usd_amount ,v.year,v.month")
				.append(" from kstar_verification_v_1 v where v.year = ? ") 
				.append(" and v.c_salesman_id like ? ")
				.append(" and v.C_SALESMAN_POSITION = ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+employeeId+"%",positionId});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	/**
	 * 合同回款被核销金额获取
	 */
	@Override
	public Value getEmpContactVeri(String year, String employeeId,String positionId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.n_veri_amount) as rmb_amount,sum(v.n_usd_veri_amount) as usd_amount ,v.year,v.month")
				.append(" from kstar_verification_v_1 v where v.year = ? ") 
				.append(" and v.c_salesman_id like ? ")
				.append(" and v.C_SALESMAN_POSITION = ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+employeeId+"%",positionId});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	@Override
	public Value getEmpDelivly(String year, String employeeId,String positionId,String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount ,v.year,v.month")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_id like ? ")
				.append(" and v.salesman_position_id = ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.year,v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+employeeId+"%",positionId});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}
	
	//group by customType
	public List<TypeValue> getOrgActualByCustomType(String year, String orgId,String currency,String flag) {
		StringBuffer sql = null;
		if("0".equals(flag)){
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.custom_class ,'无','无' ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.month ,v.custom_class order by v.custom_class  ");
		}else if("1".equals(flag)){
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.custom_class ,v.custom_type ,'无' ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.month ,v.custom_class ,v.custom_type  order by v.custom_class ,v.custom_type  ");
		}else{
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.custom_class ,v.custom_type ,v.custom_type_sub ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.month ,v.custom_class ,v.custom_type ,v.custom_type_sub order by v.custom_class ,v.custom_type ,v.custom_type_sub ");
		}
		
		return getReportData(sql.toString(),year, "%"+orgId+"%",null, currency,"ORG");
	}
	
	public List<TypeValue> getOrgActualByCustomType4Charts(String year, String orgId,String currency) {
		
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, '无', v.custom_class,'无' ,'无' ")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_org_path like ? ")
				.append(" group by v.year ,v.custom_class  order by v.custom_class  ");
		
		return getReportData(sql.toString(),year, "%"+orgId+"%",null, currency,"ORG");
	}

	private List<TypeValue> getReportData(String sql,String year, String orgId,String positionId, String currency,String reportType) {
		List<Object[]> list = null;
		if("ORG".equals(reportType)){
			list = baseDao.findBySql(sql.toString(),new Object[]{year,orgId});
		}else {			
			list = baseDao.findBySql(sql.toString(),new Object[]{year,orgId,positionId});
		}
		Map<String,TypeValue> map = new LinkedHashMap<String, TypeValue>();
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  String.valueOf(obj[3]);
			Double amount = 0d;
			String customClass = String.valueOf(obj[4]);
			String customType = String.valueOf(obj[5]);
			String customTypeSub = String.valueOf(obj[6]);
			TypeValue value = map.get(customClass+"_"+customType+"_"+customTypeSub);
			if(value == null){
				value = new TypeValue(customClass,customType,customTypeSub);
				map.put(customClass+"_"+customType+"_"+customTypeSub, value);
			}
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			if(month == null || "无".equals(month)){
				createValue(value.getValue(), amount);
			}else{
				createValue(value.getValue(), month, amount);
			}
		}
		return new ArrayList<TypeValue>(map.values());
	}

	private List<TypeValue> getProReportData(String sql,String year, String orgId,String positionId, String currency,String reportType) {
		List<Object[]> list = null;
		if("ORG".equals(reportType)){
			list = baseDao.findBySql(sql.toString(),new Object[]{year,orgId});
		}else {			
			list = baseDao.findBySql(sql.toString(),new Object[]{year,orgId,positionId});
		}
		//List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,orgId});
		Map<String,TypeValue> map = new HashMap<String, TypeValue>();
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  String.valueOf(obj[3]);
			Double amount = 0d;
			String customType = String.valueOf(obj[4]);
			String customTypeSub = String.valueOf(obj[5]);
			TypeValue value = map.get(customType+"_"+customTypeSub);
			if(value == null){
				value = new TypeValue(customType,customTypeSub);
				map.put(customType+"_"+customTypeSub, value);
			}
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			if(month == null || "无".equals(month)){
				createValue(value.getValue(), amount);
			}else{
				createValue(value.getValue(), month, amount);
			}
		}
		return new ArrayList<TypeValue>(map.values());
	}
	
	private void createValue(Value actual, Double amount){
		actual.setM01(amount);
	}
	
	private void createValue(Value actual, String month, Double amount) {
		if("01".equals(month)){
			actual.setM01(amount);
		}
		if("02".equals(month)){
			actual.setM02(amount);
		}
		if("03".equals(month)){
			actual.setM03(amount);
		}
		if("04".equals(month)){
			actual.setM04(amount);
		}
		if("05".equals(month)){
			actual.setM05(amount);
		}
		if("06".equals(month)){
			actual.setM06(amount);
		}
		if("07".equals(month)){
			actual.setM07(amount);
		}
		if("08".equals(month)){
			actual.setM08(amount);
		}
		if("09".equals(month)){
			actual.setM09(amount);
		}
		if("10".equals(month)){
			actual.setM10(amount);
		}
		if("11".equals(month)){
			actual.setM11(amount);
		}
		if("12".equals(month)){
			actual.setM12(amount);
		}
	}

	@Override
	public List<TypeValue> getOrgActualByProductType(String year, String orgId, String currency,String flag) {
		StringBuffer sql = null;
		if("0".equals(flag)){
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.pro_category,'无' ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.month ,v.pro_category  order by v.pro_category  ");
		}else{
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.pro_category,v.pro_series ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.month ,v.pro_category,v.pro_series order by v.pro_category,v.pro_series ");
		}
		return getProReportData(sql.toString(),year, "%"+orgId+"%",null, currency,"ORG");
	}
	
	public List<TypeValue> getOrgActualByProductType4Charts(String year, String orgId, String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year,'无', v.pro_category ,'无' ")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_org_path like ? ")
				.append(" group by v.year ,v.pro_category order by v.pro_category  ");
		
		return getProReportData(sql.toString(),year, "%"+orgId+"%",null, currency,"ORG");
	}

	@Override
	public Double getEmployeeTarget(String year, String employeeId, String currency) {
		KstarAnltgt anltgt = baseDao.findUniqueEntity(" select a from KstarAnltgt a,LovMember b where a.ctype = b.id and b.code = '20' and a.empId = ? and a.annual = ? ",new Object[]{employeeId,year});
		if(anltgt == null){
			return 0d;
		}
		return anltgt.getAnlTrg();
	}

	@Override
	public Value getEmployeeActual(String year, String employeeId,String positionId, String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount),sum(v.usd_amount),v.year,v.month")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ")
				.append(" and v.salesman_id = ? ")
				.append(" and v.salesman_position_id = ? ")
				.append(" group by v.year,v.month ")
				.append(" order by v.month ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,employeeId,positionId});
		list = fixData(list,year);
		Value actual = new Value();
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
			}
			count++;
		}
		actual.setTotle(counts);
		return actual;
	}

	@Override
	public List<TypeValue> getEmployeeActualByCustomType(String year, String employeeId,String positionId, String currency, String flag) {
		StringBuffer sql = null;
		if("0".equals(flag)){
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.custom_class ,'无','无' ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_id = ? ")
					.append(" and v.salesman_position_id = ? ")
					.append(" group by v.year,v.month ,v.custom_class order by v.custom_class  ");
		}else if("1".equals(flag)){
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.custom_class ,v.custom_type ,'无' ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_id = ? ")
					.append(" and v.salesman_position_id = ? ")
					.append(" group by v.year,v.month ,v.custom_class ,v.custom_type  order by v.custom_class ,v.custom_type  ");
		}else{
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.custom_class ,v.custom_type ,v.custom_type_sub ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_id = ? ")
					.append(" and v.salesman_position_id = ? ")
					.append(" group by v.year,v.month ,v.custom_class ,v.custom_type ,v.custom_type_sub order by v.custom_class ,v.custom_type ,v.custom_type_sub ");
		}
		
		return getReportData(sql.toString(),year, employeeId,positionId, currency,"Employee");
	}

	@Override
	public List<TypeValue> getEmployeeActualByCustomType4Charts(String year, String employeeId,String positionId, String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, '无', v.custom_class ,'无','无' ")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_id = ? ")
				.append(" and v.salesman_position_id = ? ")
				.append(" group by v.year ,v.custom_class  order by v.custom_class  ");
		
		return getReportData(sql.toString(),year, employeeId,positionId, currency,"Employee");
	}

	@Override
	public List<TypeValue> getEmployeeActualByProductType(String year, String employeeId,String positionId, String currency, String flag) {
		StringBuffer sql = null;
		if("0".equals(flag)){
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.pro_category,'无' ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_id like ? ")
					.append(" and v.salesman_position_id = ? ")
					.append(" group by v.year,v.month ,v.pro_category  order by v.pro_category  ");
		}else{
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.month, v.pro_category,v.pro_series ")
					.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
					.append(" and v.salesman_id like ? ")
					.append(" and v.salesman_position_id = ? ")
					.append(" group by v.year,v.month ,v.pro_category,v.pro_series order by v.pro_category,v.pro_series ");
		}
		return getProReportData(sql.toString(),year, employeeId,positionId, currency,"Employee");
	}

	@Override
	public List<TypeValue> getEmployeeActualByProductType4Charts(String year, String employeeId,String positionId, String currency) {
		StringBuffer sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year,'无', v.pro_category ,'无' ")
				.append(" from kstat_sale_performace_v_1 v where v.year = ? ") 
				.append(" and v.salesman_id = ? ")
				.append(" and v.salesman_position_id = ? ")
				.append(" group by v.year ,v.pro_category order by v.pro_category  ");
		
		return getProReportData(sql.toString(),year, employeeId,positionId, currency,"Employee");
	}
	
	@Override
	public List<TotalValue> getRankReport(String year, String rankHeaderId, String currency) {
		StringBuffer sql = new StringBuffer(" select nvl(sum(v.rmb_amount),0) as  rmb_amount, nvl(sum(v.usd_amount),0) as usd_amount, v.year, m.lov_name ")
		.append(" from rpt_t_rank_line r left join kstat_sale_performace_v_1 v on r.org_id = v.salesman_org_id and v.year = ? left join  sys_t_lov_member m on r.org_id = m.row_id  ")
		.append("  where r.header_id = ?  group by v.year ,m.lov_name order by rmb_amount ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,rankHeaderId});
		List<TotalValue> result = new ArrayList<TotalValue>();
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String orgName = String.valueOf(obj[3]);
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			TotalValue tv = new TotalValue();
			tv.setName(orgName);
			tv.setTotal(amount);
			result.add(tv);
		}
		return result;
	}

	/**
	 * 
	 * @param reportType  ORG 或者 EMPLOYEE
	 * @param prType  1:  出货  2 合同
	 * @param orgIdOrEmployeeId
	 * @param index 第几个条件，不同条件时间区间不同
	 * @return
	 */
	public List<Map<String,Object>> getPlanReceivableDetail(String reportType, String prType , String orgIdOrEmployeeId,String index /* ,String currency */){
		Integer start = 0;
		Integer end = null;
		if("0".equals(index)){
			start = 0;
			end = 90;
		}else if("1".equals(index)){
			start = 90;
			end = 180;
		}else if("2".equals(index)){
			start = 180;
			end = 365;
		}else if("3".equals(index)){
			start = 365;
			end = 730;
		}else{
			start = 730;
		}
		String dateTime = "dt_print_time";
		if("2".equals(prType)){
			dateTime = "dt_payment_date";
		}
		List<Object> args = new ArrayList<Object>();
		String sql = " select  * from KSTAR_RECEIPTES_V_1 v where (sysdate - v."+dateTime+") > ? ";
		args.add(start);
		if(end !=null){
			sql = sql + " and (sysdate - v."+dateTime+") <= ? ";
			args.add(end);
		}if("ORG".equalsIgnoreCase(reportType)){
			sql = sql + " and v.C_SALESMAN_ORG_ID like ? ";
			args.add("%"+orgIdOrEmployeeId+"%");
		}else{
			sql = sql + " and v.c_salesman_id = ? ";
			args.add(orgIdOrEmployeeId);
		}
		sql = sql + " order by v."+dateTime; 
		List<Map<String,Object>> list = baseDao.findBySql4Map(sql.toString(),args.toArray());
		return list;
	}

	
	//以出货计算的应收歀
	@Override
	public List<Double> getOrgPlanReceivable(String orgId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 0 and (sysdate - v.dt_print_time) <= 90 and v.C_BIZ_DEPT_PATH like ? union all ") 
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 90 and (sysdate - v.dt_print_time) <= 180 and v.C_BIZ_DEPT_PATH like ? union all ") 
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 180 and (sysdate - v.dt_print_time) <= 365 and v.C_BIZ_DEPT_PATH like ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 365 and (sysdate - v.dt_print_time) <= 730 and v.C_BIZ_DEPT_PATH like ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 730 and v.C_BIZ_DEPT_PATH like ?  ");
		return getResult("%"+orgId+"%", currency, sql);
	}
	
	//以出货计算的应收歀
	@Override
	public List<Double> getEmployeePlanReceivable(String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 0 and (sysdate - v.dt_print_time) <= 90 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 90 and (sysdate - v.dt_print_time) <= 180 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 180 and (sysdate - v.dt_print_time) <= 365 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 365 and (sysdate - v.dt_print_time) <= 730 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_usd_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_print_time) > 730 and v.group_id = 'POSITION' and v.c_salesman_id = ?  ");
		return getResult(employeeId, currency, sql);
	}
	
	//以合同计算的应收歀
	@Override
	public List<Double> getOrgPlanReceivable2(String orgId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 365  and v.C_BIZ_DEPT_PATH like ? union all ") 
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > -90 and (sysdate - v.dt_payment_date) < 0 and (sysdate - v.dt_print_time) <= 90 and v.C_BIZ_DEPT_PATH like ? union all  ") 
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 0 and (sysdate - v.dt_print_time) <= 90 and v.C_BIZ_DEPT_PATH like ? union all  ") 
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 90 and (sysdate - v.dt_print_time) <= 180 and v.C_BIZ_DEPT_PATH like ? union all ") 
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 180 and (sysdate - v.dt_print_time) <= 365 and v.C_BIZ_DEPT_PATH like ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 365 and (sysdate - v.dt_print_time) <= 730 and v.C_BIZ_DEPT_PATH like ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount ,  nvl( sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 730 and v.C_BIZ_DEPT_PATH like ?  ");
		return getPlanReceivable2Result("%"+orgId+"%", currency, sql);
	}

	//以合同计算的应收歀
	@Override
	public List<Double> getEmployeePlanReceivable2(String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 365  and v.c_salesman_id = ? and v.group_id = 'POSITION' union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > -90 and (sysdate - v.dt_payment_date) < 0 and (sysdate - v.dt_print_time) <= 90 and v.group_id = 'POSITION' and v.c_salesman_id like ? union all  ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 0 and (sysdate - v.dt_print_time) <= 90 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 90 and (sysdate - v.dt_print_time) <= 180 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 180 and (sysdate - v.dt_print_time) <= 365 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 365 and (sysdate - v.dt_print_time) <= 730 and v.group_id = 'POSITION' and v.c_salesman_id = ? union all ")
			.append(" select  nvl(sum(v.n_plan_amount-v.n_veri_amount)/10000,0) as plan_amount , nvl(sum(v.n_usd_plan_amount-v.n_veri_amount)/10000,0) as usd_plan_amount from KSTAR_RECEIPTES_V_1 v where (sysdate - v.dt_payment_date) > 730 and v.group_id = 'POSITION' and v.c_salesman_id = ?  ");
		return getPlanReceivable2Result(employeeId, currency, sql);
	}
	
	private List<Double> getResult(String orgId, String currency, StringBuffer sql) {
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{orgId,orgId,orgId,orgId,orgId});
		List<Double> result = new ArrayList<>();
		for(Object[] obj : list){
			BigDecimal planAmount = (BigDecimal)obj[0];
			BigDecimal usdPlanAmount = (BigDecimal)obj[1];
			if("RMB".equals(currency)){
				result.add(planAmount.doubleValue());
			}else{
				result.add(usdPlanAmount.doubleValue());
			}
		}
		return result;
	}

	private List<Double> getPlanReceivable2Result(String orgId, String currency, StringBuffer sql) {
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{orgId,orgId,orgId,orgId,orgId,orgId,orgId});
		List<Double> result = new ArrayList<>();
		for(Object[] obj : list){
			BigDecimal planAmount = (BigDecimal)obj[0];
			BigDecimal usdPlanAmount = (BigDecimal)obj[1];
			if("USD".equals(currency)){
				result.add(usdPlanAmount.doubleValue());
			}else{
				result.add(planAmount.doubleValue());
			}
		}
		return result;
	}
	
	@Override
	public Value getOrgReceivable(String year, String orgId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(v.n_veri_amount), 0) as veri_amount, ")
			.append("nvl(sum(v.n_usd_veri_amount), 0) as usd_veri_amount ,to_char(v.dt_payment_date, 'yyyy'),to_char(v.dt_payment_date, 'mm') ")
			.append("from KSTAR_RECEIPTES_V_1 v ")
			.append("where to_char(v.dt_payment_date, 'yyyy') = ? and v.C_SALESMAN_ORG_ID like ? ")
			.append(" group by to_char(v.dt_payment_date, 'yyyy'), ")
			.append("   to_char(v.dt_payment_date, 'mm') ")
			.append(" order by to_number( to_char(v.dt_payment_date, 'mm')) ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+orgId+"%"});
		list = fixData(list,year);
		Value actual = new Value();
		
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			counts += amount;
			if(count<=newMonth) {
				createValue(actual, month, counts);
			}else {
				createValue(actual, month, 0d);
			}
			count++;
		}
		return actual;
	}

	@Override
	public Value getEmployeeReceivable(String year, String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(v.n_veri_amount), 0) as veri_amount, ")
			.append("nvl(sum(v.n_usd_veri_amount), 0) as usd_veri_amount ,to_char(v.dt_payment_date, 'yyyy'),to_char(v.dt_payment_date, 'mm') ")
			.append("from KSTAR_RECEIPTES_V_1 v ")
			.append("where to_char(v.dt_payment_date, 'yyyy') = ? and v.c_salesman_id = ? ")
			.append(" group by to_char(v.dt_payment_date, 'yyyy'), ")
			.append("   to_char(v.dt_payment_date, 'mm') ")
			.append(" order by to_number( to_char(v.dt_payment_date, 'mm')) ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,employeeId});
		Value actual = new Value();
		int i = 1;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[3];
			Double amount = 0d;
			if("USD".equals(currency)){
				amount = usdAmount.doubleValue();
			}else{
				amount = rmbAmount.doubleValue();
			}
			
			createValue(actual, month, amount);
			i++;
		}
		return actual;
	}

	@Override
	public Double getOrgTotalPlanReceivable(String year, String orgId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(v.n_plan_amount), 0) as plan_amount, ")
			.append("nvl(sum(v.n_usd_plan_amount), 0) as usd_plan_amount ")
			.append("from KSTAR_RECEIPTES_V_1 v ")
			.append("where to_char(v.dt_payment_date, 'yyyy') = ? and v.C_SALESMAN_ORG_ID like ? ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,"%"+orgId+"%"});
		if(list.size() == 0){
			return 0d;
		}
		Object[] obj = list.get(0);
		BigDecimal rmbAmount = (BigDecimal)obj[0];
		BigDecimal usdAmount = (BigDecimal)obj[1];
		if("USD".equals(currency)){
			return usdAmount.doubleValue();
		}else{
			return rmbAmount.doubleValue();
		}
	}

	@Override
	public Double getEmployeeTotalPlanReceivable(String year, String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(v.n_plan_amount), 0) as plan_amount, ")
			.append("nvl(sum(v.n_usd_plan_amount), 0) as usd_plan_amount ")
			.append("from KSTAR_RECEIPTES_V_1 v ")
			.append("where to_char(v.dt_payment_date, 'yyyy') = ? and v.c_salesman_id = ? ");
		
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,employeeId});
		if(list.size() == 0){
			return 0d;
		}
		Object[] obj = list.get(0);
		BigDecimal rmbAmount = (BigDecimal)obj[0];
		BigDecimal usdAmount = (BigDecimal)obj[1];
		if("USD".equals(currency)){
			return usdAmount.doubleValue();
		}else{
			return rmbAmount.doubleValue();
		}
	}

	private List<Object[]> fixData(List<Object[]> list,String year){
		List<Object[]> newList = new ArrayList();
		if(list.size()<13&&list.size()>0) {
			int count = 1; 
			for(int i = 1;i<13;i++) {
				Object[] newObject = new Object[4];
				BigDecimal rmbAmount = new BigDecimal(0);
				BigDecimal usdAmount = new BigDecimal(0);
				newObject[0] = (Object)rmbAmount;
				newObject[1] = (Object)usdAmount;
				newObject[2] = (Object)year;
				if(i<10) {
					newObject[3] = "0"+i;
				}else {
					newObject[3] = String.valueOf(i);
				}
				newList.add(newObject);
			}
			for(Object[] objNew : newList){
				for(Object[] obj : list){
					Object[] newObject = new Object[3];
					String month =  (String)obj[3];
					if(objNew[3].equals(month)) {
						BigDecimal rmbAmount = (BigDecimal)obj[0];
						BigDecimal usdAmount = (BigDecimal)obj[1];
						objNew[0] = (Object)rmbAmount;
						objNew[1] = (Object)usdAmount;
					}
				}
			}
		}
		return newList;
	}

	//统计年度的费用金额（万元）——按员工
	@Override
	public Value getEmployeeInvoiceNo(String year, String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(cux.INVOICE_AMOUNT), 0) as amount,to_char(cux.INVOICE_DATE, 'yyyy') as year,to_char(cux.INVOICE_DATE, 'mm') ")
			.append(" from CUX_OA_EXPENSES_CLAIM_DATA cux,SYS_T_PERMISSION_EMPLOYEE employee where to_char(cux.INVOICE_DATE, 'yyyy') = ?  ")
			.append(" and  employee.no = cux.EMPLOYEE_NO  and employee.row_id = ? ")
			.append(" group by to_char(cux.INVOICE_DATE, 'yyyy'), ")
			.append(" to_char(cux.INVOICE_DATE, 'mm') ")
			.append(" order by to_number( to_char(cux.INVOICE_DATE, 'mm')) ");
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,employeeId});
		Value actual = new Value();
		Calendar cal=Calendar.getInstance();//使用日历类
		int newYear=cal.get(Calendar.YEAR);//得到年
		int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		int count = 1;
		Double counts = 0d;
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			//BigDecimal usdAmount = (BigDecimal)obj[1];
			String month =  (String)obj[2];
			Double amount = 0d;
			amount = rmbAmount.doubleValue();
			counts = amount;
			if(String.valueOf(newYear).equals(year)) {
				if(count<=newMonth) {
					createValue(actual, month, counts);
					actual.setTotle(amount+=amount);
				}else {
					createValue(actual, month, 0d);
				}
			}else {
				createValue(actual, month, counts);
				actual.setTotle(amount+=amount);
			}
			count++;
		}
		return actual;
	}

	//统计年度的费用金额（万元）——按员工岗位与部门
		@Override
		public Value getOrgInvoiceNo(String year, String orgId, String currency,UserObject  user) {
			StringBuffer sql = new StringBuffer();
			sql.append(" select nvl(sum(cux.INVOICE_AMOUNT), 0) as amount,to_char(cux.INVOICE_DATE, 'yyyy') as year,to_char(cux.INVOICE_DATE, 'mm') ")
				.append("  from CUX_OA_EXPENSES_CLAIM_DATA cux ")
				.append("  where to_char(cux.INVOICE_DATE, 'yyyy') = ? ")
				.append("  and cux.crm_position_id = ? and cux.crm_org_id = ? ")
				.append(" group by to_char(cux.INVOICE_DATE, 'yyyy'), ")
				.append(" to_char(cux.INVOICE_DATE, 'mm') ")
				.append(" order by to_number( to_char(cux.INVOICE_DATE, 'mm')) ");
			List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,user.getPosition().getId(),orgId});
			Value actual = new Value();
			Calendar cal=Calendar.getInstance();//使用日历类
			int newYear=cal.get(Calendar.YEAR);//得到年
			int newMonth=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
			int count = 1;
			Double counts = 0d;
			for(Object[] obj : list){
				BigDecimal rmbAmount = (BigDecimal)obj[0];
				//BigDecimal usdAmount = (BigDecimal)obj[1];
				String month =  (String)obj[2];
				Double amount = 0d;
				amount = rmbAmount.doubleValue();
				counts = amount;
				if(String.valueOf(newYear).equals(year)) {
					if(count<=newMonth) {
						createValue(actual, month, counts);
						actual.setTotle(amount+=amount);
					}else {
						createValue(actual, month, 0d);
					}
				}else {
					createValue(actual, month, counts);
					actual.setTotle(amount+=amount);
				}
				count++;
			}
			return actual;
		}
	
	/**
	 * 组织——财务应收款——已开票，未开票
	 */
	@Override
	public List<Double> getOrgInvoicedAndUnbilled(String year, String orgId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(n_billing_quantity_rmb)/10000,0) ,  nvl(sum(n_delivery_quantity_rmb)/10000,0),nvl(sum(n_billing_quantity_usd)/10000,0) ,  nvl(sum(n_delivery_quantity_usd)/10000,0) from kstat_sale_performace_v_new_1 v   ") 
		.append(" where v.salesman_org_path like ? ");
		return getInvoicedAndUnbilledResult("%"+orgId+"%", year, sql,currency);
	}

	/**
	 * 员工——财务应收款——已开票，未开票
	 */
	@Override
	public List<Double> getEmployeeInvoicedAndUnbilled(String year, String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(n_billing_quantity_rmb)/10000,0) ,  nvl(sum(n_delivery_quantity_rmb)/10000,0),nvl(sum(n_billing_quantity_usd)/10000,0),  nvl(sum(n_delivery_quantity_usd)/10000,0) from kstat_sale_performace_v_new_1 v   ") 
		.append(" where v.salesman_id like ? ");
		return getInvoicedAndUnbilledResult(employeeId, year, sql,currency);
	}
	
	private List<Double> getInvoicedAndUnbilledResult(String orgId, String year, StringBuffer sql,String currency) {
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{"%"+orgId+"%"});
		List<Double> result = new ArrayList<>();
		for(Object[] obj : list){
			BigDecimal n_billing_quantity_rmb = (BigDecimal)obj[0];
			BigDecimal n_delivery_quantity_rmb = (BigDecimal)obj[1];
			BigDecimal n_billing_quantity_usd = (BigDecimal)obj[2];
			BigDecimal n_delivery_quantity_usd = (BigDecimal)obj[3];
			if("USD".equals(currency)){
				result.add(n_billing_quantity_usd.doubleValue());
				result.add(n_delivery_quantity_usd.doubleValue());
			}else {
				result.add(n_billing_quantity_rmb.doubleValue());
				result.add(n_delivery_quantity_rmb.doubleValue());
			}
			
		}
		return result;
	}

	private List<Double> getInvoicedAndUnbilledResult2(String orgId, String year, StringBuffer sql,String currency) {
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{"%"+orgId+"%",year});
		List<Double> result = new ArrayList<>();
		for(Object[] obj : list){
			BigDecimal n_billing_quantity = (BigDecimal)obj[0];
			BigDecimal n_delivery_quantity_rmb = (BigDecimal)obj[1];
			BigDecimal n_delivery_quantity_usd = (BigDecimal)obj[2];
			result.add(n_billing_quantity.doubleValue());
			if("USD".equals(currency)){
				result.add(n_delivery_quantity_usd.doubleValue());
			}else {
				result.add(n_delivery_quantity_rmb.doubleValue());
			}
			
		}
		return result;
	}
	/**
	 * 组织——预收歀（万元）——笔数、预收款金额
	 */
	@Override
	public List<Double> getOrgAdvancesReceived(String year, String orgId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select nvl(sum(sum_amount_count),0),nvl(sum(notVeriAmount_rmb)/10000,0),nvl(sum(notVeriAmount_usd)/10000,0) from crm_int_mv_receipts_report v   ") 
		.append(" where v.c_biz_dept_path like ? and to_char(v.dt_receipts_date, 'yyyy') = ?");
		return getInvoicedAndUnbilledResult2("%"+orgId+"%", year, sql,currency);
	}

	/**
	 * 岗位——预收歀（万元）——笔数、预收款金额
	 */
	@Override
	public List<Double> getEmployeeAdvancesReceived(String year, String employeeId, String currency) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select  nvl(sum(sum_amount_count),0),nvl(sum(notVeriAmount_rmb)/10000,0),nvl(sum(notVeriAmount_usd)/10000,0)   from crm_int_mv_receipts_report v   ") 
		.append(" where v.c_salesman_id like ? and to_char(v.dt_receipts_date, 'yyyy') = ?");
		return getInvoicedAndUnbilledResult2(employeeId, year, sql,currency);
	}
	
	//历史分析——组织
	public List<HistoryAnalysisValue> getOrgActualHistory(String year,String historyYear, String orgId,String currency,String flag) {
		StringBuffer sql = null;
		if("0".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.custom_class ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.custom_class order by v.custom_class  ");
		}else if("1".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.custom_type ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.custom_type order by v.custom_type  ");
		}else if("2".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.pro_category ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.pro_category order by v.pro_category  ");
		}else if("3".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.pro_series ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_org_path like ? ")
					.append(" group by v.year,v.pro_series order by v.pro_series  ");
		}
		return getHistoryReportData(sql.toString(),year,historyYear, "%"+orgId+"%", currency);
	}
	
	private List<HistoryAnalysisValue> getHistoryReportData(String sql,String year,String historyYear, String orgId, String currency) {
		List<Object[]> list = baseDao.findBySql(sql.toString(),new Object[]{year,historyYear,orgId});
		Map<String,HistoryAnalysisValue> map = new LinkedHashMap<String, HistoryAnalysisValue>();
		for(Object[] obj : list){
			BigDecimal rmbAmount = (BigDecimal)obj[0];
			BigDecimal usdAmount = (BigDecimal)obj[1];
			String dataYear =  String.valueOf(obj[2]);
			String type = String.valueOf(obj[3]);
			
			HistoryAnalysisValue value = map.get(type);
			if(value == null){
				value = new HistoryAnalysisValue();
				value.setType(type);
				setAccomplishAndContrast(value,dataYear,rmbAmount);
				map.put(type, value);
			}else if(value!=null){
				setAccomplishAndContrast(value,dataYear,rmbAmount);
				map.put(type, value);
			}
		}
		ArrayList<HistoryAnalysisValue> historyAnalysisValues = new ArrayList<HistoryAnalysisValue>(map.values());
		CalculationContrast(historyAnalysisValues);
		return historyAnalysisValues;
	}
	
	private void setAccomplishAndContrast(HistoryAnalysisValue value,String dataYear,BigDecimal rmbAmount) {
		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
		int cutYear = nowYear - Integer.valueOf(dataYear);
		if(cutYear==0) {
			value.setNewYearsAgoAccomplish(rmbAmount);
		}else if(cutYear==1) {
			value.setOneYearsAgoAccomplish(rmbAmount);
		}else if(cutYear==2) {
			value.setTwoYearsAgoAccomplish(rmbAmount);
		}else if(cutYear==3) {
			value.setThreeYearsAgoAccomplish(rmbAmount);
		}else if(cutYear==4) {
			value.setFourYearsAgoAccomplish(rmbAmount);
		}else if(cutYear==5) {
			value.setFiveYearsAgoAccomplish(rmbAmount);
		}else if(cutYear==6) {
			value.setSixYearsAgoAccomplish(rmbAmount);
		}
	}
	
	private void CalculationContrast(ArrayList<HistoryAnalysisValue> historyAnalysisValues) {
		if(historyAnalysisValues.size()>0) {
			for(HistoryAnalysisValue historyAnalysisValue:historyAnalysisValues) {
				//同比公式——（今年销量-去年销量）除与去年销量
				if(historyAnalysisValue.getFiveYearsAgoAccomplish().compareTo(new BigDecimal(0))==1
						&&historyAnalysisValue.getSixYearsAgoAccomplish().compareTo(new BigDecimal(0))==1) {
					BigDecimal contrast = (historyAnalysisValue.getFiveYearsAgoAccomplish().subtract(historyAnalysisValue.getSixYearsAgoAccomplish())).divide(historyAnalysisValue.getSixYearsAgoAccomplish(), 2, BigDecimal.ROUND_HALF_UP);
					historyAnalysisValue.setFiveYearsAgoContrast(contrast.doubleValue());
				}
				if(historyAnalysisValue.getFourYearsAgoAccomplish().compareTo(new BigDecimal(0))==1
						&&historyAnalysisValue.getFiveYearsAgoAccomplish().compareTo(new BigDecimal(0))==1) {
					BigDecimal contrast = (historyAnalysisValue.getFourYearsAgoAccomplish().subtract(historyAnalysisValue.getFiveYearsAgoAccomplish())).divide(historyAnalysisValue.getFiveYearsAgoAccomplish(), 2, BigDecimal.ROUND_HALF_UP);
					historyAnalysisValue.setFourYearsAgoContrast(contrast.doubleValue());
				}
				if(historyAnalysisValue.getThreeYearsAgoAccomplish().compareTo(new BigDecimal(0))==1
						&&historyAnalysisValue.getFourYearsAgoAccomplish().compareTo(new BigDecimal(0))==1) {
					BigDecimal contrast = (historyAnalysisValue.getThreeYearsAgoAccomplish().subtract(historyAnalysisValue.getFourYearsAgoAccomplish())).divide(historyAnalysisValue.getFourYearsAgoAccomplish(), 2, BigDecimal.ROUND_HALF_UP);
					historyAnalysisValue.setThreeYearsAgoContrast(contrast.doubleValue());				
				}
				if(historyAnalysisValue.getTwoYearsAgoAccomplish().compareTo(new BigDecimal(0))==1
						&&historyAnalysisValue.getThreeYearsAgoAccomplish().compareTo(new BigDecimal(0))==1) {
					BigDecimal contrast = (historyAnalysisValue.getTwoYearsAgoAccomplish().subtract(historyAnalysisValue.getThreeYearsAgoAccomplish())).divide(historyAnalysisValue.getThreeYearsAgoAccomplish(), 2, BigDecimal.ROUND_HALF_UP);
					historyAnalysisValue.setTwoYearsAgoContrast(contrast.doubleValue());
				}
				if(historyAnalysisValue.getOneYearsAgoAccomplish().compareTo(new BigDecimal(0))==1
						&&historyAnalysisValue.getTwoYearsAgoAccomplish().compareTo(new BigDecimal(0))==1) {
					BigDecimal contrast = (historyAnalysisValue.getOneYearsAgoAccomplish().subtract(historyAnalysisValue.getTwoYearsAgoAccomplish())).divide(historyAnalysisValue.getTwoYearsAgoAccomplish(), 2, BigDecimal.ROUND_HALF_UP);
					historyAnalysisValue.setOneYearsAgoContrast(contrast.doubleValue());
				}
				if(historyAnalysisValue.getNewYearsAgoAccomplish().compareTo(new BigDecimal(0))==1
						&&historyAnalysisValue.getOneYearsAgoAccomplish().compareTo(new BigDecimal(0))==1) {
					BigDecimal contrast = (historyAnalysisValue.getNewYearsAgoAccomplish().subtract(historyAnalysisValue.getOneYearsAgoAccomplish())).divide(historyAnalysisValue.getOneYearsAgoAccomplish(),2,BigDecimal.ROUND_HALF_UP);
					historyAnalysisValue.setNewYearsAgoContrast(contrast.doubleValue());
				}
				
			}
		}
	}
	
	//历史分析——组织
	public List<HistoryAnalysisValue> getEmployeeActualHistory(String year,String historyYear, String employeeId,String currency,String flag) {
		StringBuffer sql = null;
		if("0".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.custom_class ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_id like ? ")
					.append(" group by v.year,v.custom_class order by v.custom_class  ");
		}else if("1".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.custom_type ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_id like ? ")
					.append(" group by v.year,v.custom_type order by v.custom_type  ");
		}else if("2".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.pro_category ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_id like ? ")
					.append(" group by v.year,v.pro_category order by v.pro_category  ");
		}else if("3".equals(flag)) {
			sql = new StringBuffer(" select sum(v.rmb_amount) as rmb_amount,sum(v.usd_amount) as usd_amount, v.year, v.pro_series ")
					.append(" from kstat_sale_performace_v_1 v where v.year <= ? and v.year >= ?") 
					.append(" and v.salesman_id like ? ")
					.append(" group by v.year,v.pro_series order by v.pro_series  ");
		}
		return getHistoryReportData(sql.toString(),year,historyYear, "%"+employeeId+"%", currency);
	}

	
	/**
	 * 逾期未发货明细
	 */
	@Override
	public IPage getReportOverdue(PageCondition condition, String reportType, String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		IPage page = null;
		if("ORG".equals(reportType)){
			String hql = getReportOverdueORGHql(condition,orgIdOrEmployeeId);
			page = baseDao.search(hql, condition.getRows(), condition.getPage());
		}else{
			String hql = getReportOverdueEMPHql(condition,orgIdOrEmployeeId);
			page = baseDao.search(hql, condition.getRows(), condition.getPage());
		}
		return page;
	}

	private String getReportOverdueORGHql(PageCondition condition, String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		String hql = " from ReportOverdueVO where 1=1 ";
			hql += " and dueId in ( ";
			hql += "   select t.businessId from Team t , LovMember p  where ";
			hql += " p.groupId = 'POSITION' and p.path like '%"+orgIdOrEmployeeId+"%'  ";
			hql += " and p.id = t.positionId and t.businessType = 'ORDER' ";
			hql += " ) ";
		return hql;
	}

	private String getReportOverdueEMPHql(PageCondition condition, String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		String hql = " from ReportOverdueVO where 1=1 ";
			hql += " and salesmanId = '"+orgIdOrEmployeeId+"' ";
		return hql;
	}

	
	/**
	 * 逾期未发货总金额
	 */
	@Override
	public Double getOverdueSum(String type, String id) {
		// TODO Auto-generated method stub
		Double sum = 0.0d;
		if("ORG".equals(type)){
			sum = getOverdueSumORG(id);
		}else{
			sum = getOverdueSumEMP(id);
		}
		return sum;
	}

	private Double getOverdueSumEMP(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(v.n_amount), 0) as sum ")
			.append(" from kstart_order_report_v v ")
			.append(" where 1=1 ")
			.append(" and c_salesman_id = ? ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString(),new Object[]{id});
		return sum.doubleValue();
	}

	private Double getOverdueSumORG(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select nvl(sum(v.n_amount), 0) as sum ")
			.append(" from kstart_order_report_v v ")  
			.append(" where 1=1 ")
			.append(" and c_pid in (  ")
			.append(" 	select t.BUSINESS_Id from CRM_T_TEAM t , SYS_T_LOV_MEMBER p  where ")
			.append(" 	p.GROUP_ID = 'POSITION' and p.LOV_PATH like ? ")
			.append(" 	and p.ROW_ID = t.POSITION_ID and t.BUSINESS_TYPE = 'ORDER' ")
			.append(" ) ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString(),new Object[]{"%"+id+"%"});
		return sum.doubleValue();
	}

	//@Scheduled(cron = "0 0 0 * * ?")
	public void updatePositionId() {
		Exception a = new Exception();
		BigDecimal count = new BigDecimal(0);
		try {
			
			StringBuffer countSql = new StringBuffer();
			countSql.append(" select count(0)   ")
				.append(" 	  from SYS_T_PERMISSION_EMPLOYEE em, ")
				.append(" 	       CUX_OA_EXPENSES_CLAIM_DATA oa, ")
				.append(" 	       SYS_T_LOV_MEMBER lov, ")
				.append(" 	       SYS_T_PERMISSION_USER_REL up, ")
				.append(" 	       SYS_T_LOV_MEMBER t2, ")
				.append(" 	       SYS_T_LOV_MEMBER t3 ")
				.append(" 	  where oa.employee_no=em.no  ")
				.append(" 	    and lov.group_code='POSITION' ")
				.append(" 	    and lov.row_id = up.MEMBER_ID ")
				.append(" 	    and em.row_id = up.USER_ID  ")
				.append(" 	    and up.type = 'P'  ")
				.append(" 	    and lov.opt_txt1 = t2.row_id  ")
				.append(" 	    and t3.opt_txt6 like  '%'||oa.ACCOUNT_DEPT||'%'  ")
				.append(" 	    and t3.row_id = t2.parent_id  ")
				.append(" 	    and oa.crm_position_id is null ")
				.append(" 	    and oa.CRM_POSITION_NAME is null ")
				.append(" 	    and oa.CRM_ORG_ID is null ")
				.append(" 	    and oa.CRM_ORG_NAME is null  ");
			count =	baseDao.findUniqueBySql(countSql.toString());
			
			StringBuffer sql = new StringBuffer();
			sql.append(" update CUX_OA_EXPENSES_CLAIM_DATA  ")
				.append(" set (CRM_POSITION_ID,CRM_POSITION_NAME,CRM_ORG_ID,CRM_ORG_NAME) = ( ")
				.append("    select lov.row_id,lov.lov_name, ")
				.append("           t3.row_id,t3.lov_name ")
				.append(" 	  from SYS_T_PERMISSION_EMPLOYEE em, ")
				.append(" 	       CUX_OA_EXPENSES_CLAIM_DATA oa, ")
				.append(" 	       SYS_T_LOV_MEMBER lov, ")
				.append(" 	       SYS_T_PERMISSION_USER_REL up, ")
				.append(" 	       SYS_T_LOV_MEMBER t2, ")
				.append(" 	       SYS_T_LOV_MEMBER t3 ")
				.append(" 	  where oa.employee_no=em.no  ")
				.append(" 	    and lov.group_code='POSITION' ")
				.append(" 	    and lov.row_id = up.MEMBER_ID ")
				.append(" 	    and em.row_id = up.USER_ID  ")
				.append(" 	    and up.type = 'P'  ")
				.append(" 	    and lov.opt_txt1 = t2.row_id  ")
				.append(" 	    and t3.opt_txt6 like  '%'||oa.ACCOUNT_DEPT||'%'  ")
				.append(" 	    and t3.row_id = t2.parent_id  ")
				.append(" 	    and oa.crm_position_id is null ")
				.append(" 	    and oa.CRM_POSITION_NAME is null ")
				.append(" 	    and oa.CRM_ORG_ID is null ")
				.append(" 	    and oa.CRM_ORG_NAME is null ) ");
				baseDao.executeSQL(sql.toString());
		}catch(Exception e) {
			e.printStackTrace();
			a=e;
		}finally {
			if(StringUtil.isNullOrEmpty(a.getMessage())) {
				userService.sendMail("13430185107@163.com", "OA", String.valueOf(count.intValue()));
			}else {				userService.sendMail("13430185107@163.com", "OA", "总数："+count.intValue()+"|"+a.getMessage());
	   		}
		}
	}
	/**
	 * 商机报备授权比例
	 */
	@Override
	public int getBizoppReportScale(String type, String id) {
		// TODO Auto-generated method stub
		int scale = 0;
		float bizoppSum = 0f;
		float reportSum = 0f;
		if("ORG".equals(type)){
			reportSum = getReportORG(id);
			bizoppSum = getBizoppORG(id);
			if(bizoppSum>0) {
				BigDecimal sBigDecimal = new BigDecimal(String.valueOf((reportSum / bizoppSum)*100)).setScale(0, BigDecimal.ROUND_HALF_UP);
				scale = sBigDecimal.intValue();
			}
		}else {
			reportSum = getReportEMP(id);
			bizoppSum = getBizoppEMP(id);
			if(bizoppSum>0) {
				BigDecimal sBigDecimal = new BigDecimal(String.valueOf((reportSum / bizoppSum)*100)).setScale(0, BigDecimal.ROUND_HALF_UP);
				scale = sBigDecimal.intValue();
			}
		}
		return scale;
	}

	/**
	 * 报备落单率比例
	 */
	@Override
	public int getBizoppOrderReportScale(String type, String id) {
		// TODO Auto-generated method stub
		int scale = 0;
		float bizoppSum = 0f;
		float reportSum = 0f;
		if("ORG".equals(type)){
			reportSum = getOrderReportORG(id);
			bizoppSum = getBizoppORG(id);
		}else {
			reportSum = getOrderReportEMP(id);
			bizoppSum = getBizoppEMP(id);
		}
		if(bizoppSum>0) {
			BigDecimal sBigDecimal = new BigDecimal(String.valueOf((reportSum / bizoppSum)*100)).setScale(0, BigDecimal.ROUND_HALF_UP);
			scale = sBigDecimal.intValue();
		}
		return scale;
	}
	
	/**
	 * 通过员工id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过所有商机总数
	 * @param id
	 * @return
	 */
	private float getBizoppEMP(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0) " )
			.append("	from crm_t_business_opportunity b ")
			.append(" where 1=1 ")
			.append(" and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      b.c_status = '100'or  b.c_status = '110') ")
			.append(" and b.created_by_id = '").append(id).append("' ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}

	/**
	 * 通过员工id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过并且在用章申请已授权所有授权总数所有商机总数
	 * @param id
	 * @return
	 */
	private float getReportEMP(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0)")
			.append(" 	from crm_t_business_opportunity b,CRM_T_BID t")
			.append(" where 1=1")
			.append(" 	and t.c_business_opp_id = b.c_pid ")
			.append(" 	and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      	  b.c_status = '100'or  b.c_status = '110') ")
			.append(" 	and b.created_by_id = '").append(id).append("' ")
			.append("   and t.c_status = 'Completed'");;
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}

	/**
	 * 通过组织id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过所有商机总数
	 * @param id
	 * @return
	 */
	private float getBizoppORG(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0) " )
			.append("	from crm_t_business_opportunity b, SYS_T_LOV_MEMBER l ")
			.append(" where 1=1 ")
			.append(" and b.c_status = l.LOV_CODE ")
			.append(" and l.GROUP_CODE = 'OPPORTUNITY_STATUS'")
			.append(" and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      b.c_status = '100'or  b.c_status = '110') ")
			.append(" and b.c_pid in")
			.append("	(select t.BUSINESS_Id")
			.append("		from CRM_T_TEAM t, SYS_T_LOV_MEMBER p")
			.append("    where p.GROUP_ID = 'POSITION'")
			.append("		and p.LOV_PATH like '%").append(id).append("%'")
			.append(" 		and p.ROW_ID = t.POSITION_ID")
			.append(" 		and t.BUSINESS_TYPE = 'BusinessOpportunity')");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}

	/**
	 * 通过组织id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过并且在用章申请已授权所有授权总数
	 * @param id
	 * @return
	 */
	private float getReportORG(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0)")
			.append(" 	from crm_t_business_opportunity b,CRM_T_BID t")
			.append(" where 1=1")
			.append(" 	and t.c_business_opp_id = b.c_pid ")
			.append(" 	and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      	  b.c_status = '100'or  b.c_status = '110') ")
			.append(" 	and b.c_pid in")
			.append("	    (select t.BUSINESS_Id")
			.append("			from CRM_T_TEAM t, SYS_T_LOV_MEMBER p")
			.append("    	 where p.GROUP_ID = 'POSITION'")
			.append("		 	 and p.LOV_PATH like '%").append(id).append("%'")
			.append(" 		 	 and p.ROW_ID = t.POSITION_ID")
			.append(" 			 and t.BUSINESS_TYPE = 'BusinessOpportunity')")
			.append(" and t.c_status = 'Completed'");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}

	
	/**
	 * 报备授权后台查询组装Page
	 * @param condition
	 * @param reportType
	 * @param orgIdOrEmployeeId
	 * @return
	 */
	@Override
	public IPage getBizoopReportScale(PageCondition condition, String reportType, String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		IPage page = null;
		if("ORG".equals(reportType)){
			page = getBizoopReportORG(condition,orgIdOrEmployeeId);
		}else {
			page = getBizoopReportEMP(condition,orgIdOrEmployeeId);
		}
		
		return page;
	}

	private IPage getBizoopReportEMP(PageCondition condition,String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql .append(" select * ")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append("   and v.created_by_id = '").append(orgIdOrEmployeeId).append("' ")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("Completed".equals((String)objects[11])?"是":"否");
				bScaleVO.setIsOrder("");
				bScaleVO.setIsReportFeedback("");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}

	private IPage getBizoopReportORG(PageCondition condition,String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql .append(" select * ")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append("   and v.c_pid in  (")
			.append(" 		select t.BUSINESS_Id ")
			.append(" 		  from CRM_T_TEAM t,SYS_T_LOV_MEMBER p ")
			.append("		where ")
			.append("          p.GROUP_ID = 'POSITION' ")
			.append("          and p.LOV_PATH like '%").append(orgIdOrEmployeeId).append("%' ")
			.append("          and p.ROW_ID = t.POSITION_ID ")
			.append("          and t.BUSINESS_TYPE = 'BusinessOpportunity' ")
			.append(" )")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("Completed".equals((String)objects[11])?"是":"否");
				bScaleVO.setIsOrder("");
				bScaleVO.setIsReportFeedback("");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}

	
	/**
	 * 商机报备已授权落单比例
	 */
	@Override
	public int getReportOrderScale(String type, String id) {
		int scale = 0;
		float orderSum = 0f;
		float reportSum = 0f;
		if("ORG".equals(type)){
			reportSum = getReportORG(id);
			orderSum = getOrderORG(id);
			if(reportSum>0) {
				BigDecimal sBigDecimal = new BigDecimal(String.valueOf((orderSum / reportSum)*100)).setScale(0, BigDecimal.ROUND_HALF_UP);
				scale = sBigDecimal.intValue();
			}
		}else {
			reportSum = getReportEMP(id);
			orderSum = getOrderEMP(id);
			if(reportSum>0) {
				BigDecimal sBigDecimal = new BigDecimal(String.valueOf((orderSum / reportSum)*100)).setScale(0, BigDecimal.ROUND_HALF_UP);
				scale = sBigDecimal.intValue();
			}
		}
		return scale;
	}

	private float getOrderEMP(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0)")
			.append(" 	from crm_t_business_opportunity b,CRM_T_BID t")
			.append(" where 1=1")
			.append(" 	and t.c_business_opp_id = b.c_pid ")
			.append(" 	and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      	  b.c_status = '100'or  b.c_status = '110') ")
			.append(" 	and b.created_by_id = '").append(id).append("' ")
			.append(" 	and b.c_pid in")
			.append("	    (select a.c_source_id from CRM_T_ORDER_LINES a)")
			.append(" and t.c_status = 'Completed'");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}

	private float getOrderORG(String id) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0)")
			.append(" 	from crm_t_business_opportunity b,CRM_T_BID t")
			.append(" where 1=1")
			.append(" 	and t.c_business_opp_id = b.c_pid ")
			.append(" 	and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      	  b.c_status = '100'or  b.c_status = '110') ")
			.append(" 	and b.c_pid in")
			.append("	    (select t.BUSINESS_Id")
			.append("			from CRM_T_TEAM t, SYS_T_LOV_MEMBER p")
			.append("    	 where p.GROUP_ID = 'POSITION'")
			.append("		 	 and p.LOV_PATH like '%").append(id).append("%'")
			.append(" 		 	 and p.ROW_ID = t.POSITION_ID")
			.append(" 			 and t.BUSINESS_TYPE = 'BusinessOpportunity')")
			.append(" 	and b.c_pid in")
			.append("	    (select a.c_source_id from CRM_T_ORDER_LINES a)")
			.append(" and t.c_status = 'Completed'");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}

	
	/**
	 * 通过组织id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过,并且在订单中下单的所有商机总数
	 * @param id
	 * @return
	 */
	private float getOrderReportORG(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0) " )
			.append("	from crm_t_business_opportunity b ")
			.append(" where 1=1 ")
			.append(" and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      b.c_status = '100'or  b.c_status = '110') ")
			.append(" and b.c_pid in")
			.append("	(select t.BUSINESS_Id")
			.append("		from CRM_T_TEAM t, SYS_T_LOV_MEMBER p")
			.append("    where p.GROUP_ID = 'POSITION'")
			.append("		and p.LOV_PATH like '%").append(id).append("%'")
			.append(" 		and p.ROW_ID = t.POSITION_ID")
			.append(" 		and t.BUSINESS_TYPE = 'BusinessOpportunity')")
			.append(" and b.c_pid in ( ")
			.append(" select order_line.c_source_id from CRM_T_ORDER_LINES order_line ")
			.append(" where order_line.c_source_id is not null ) ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}
	
	
	/**
	 * 通过员工id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过,并且在订单中下单的所有商机总数
	 * @param id
	 * @return
	 */
	private float getOrderReportEMP(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0) " )
			.append("	from crm_t_business_opportunity b ")
			.append(" where 1=1 ")
			.append(" and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      b.c_status = '100'or  b.c_status = '110') ")
			.append(" and b.created_by_id = '").append(id).append("' ")
			.append(" and b.c_pid in ( ")
			.append(" select order_line.c_source_id from CRM_T_ORDER_LINES order_line ")
			.append(" where order_line.c_source_id is not null ) ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}
	
	/**
	 * 报备落单率后台查询组装Page
	 * @param condition
	 * @param reportType
	 * @param orgIdOrEmployeeId
	 * @return
	 */
	@Override
	public IPage getBizoopOrderReportScale(PageCondition condition, String reportType, String orgIdOrEmployeeId) {
		IPage page = null;
		if("ORG".equals(reportType)){
			page = getBizoopOrderReportORG(condition,orgIdOrEmployeeId);
		}else {
			page = getBizoopOrderReportEMP(condition,orgIdOrEmployeeId);
		}
		
		return page;
	}
	
	private IPage getBizoopOrderReportORG(PageCondition condition,String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql .append(" select v.*, ")
			.append(" (select order_line.c_source_id from CRM_T_ORDER_LINES order_line ")
			.append(" where order_line.c_source_id is not null and v.c_pid = order_line.c_source_id  and rownum = 1 )  as orderLineId")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append("   and v.c_pid in  (")
			.append(" 		select t.BUSINESS_Id ")
			.append(" 		  from CRM_T_TEAM t,SYS_T_LOV_MEMBER p ")
			.append("		where ")
			.append("          p.GROUP_ID = 'POSITION' ")
			.append("          and p.LOV_PATH like '%").append(orgIdOrEmployeeId).append("%' ")
			.append("          and p.ROW_ID = t.POSITION_ID ")
			.append("          and t.BUSINESS_TYPE = 'BusinessOpportunity' ")
			.append(" )")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("");
				bScaleVO.setIsOrder(StringUtil.isNullOrEmpty((String)objects[13])?"否":"是");
				bScaleVO.setIsReportFeedback("");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}
	
	private IPage getBizoopOrderReportEMP(PageCondition condition,String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql .append(" select v.*, ")
			.append(" (select order_line.c_source_id from CRM_T_ORDER_LINES order_line ")
			.append(" where order_line.c_source_id is not null and v.c_pid = order_line.c_source_id and rownum = 1 )  as orderLineId")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append("   and v.created_by_id = '").append(orgIdOrEmployeeId).append("' ")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("");
				bScaleVO.setIsOrder(StringUtil.isNullOrEmpty((String)objects[13])?"否":"是");
				bScaleVO.setIsReportFeedback("");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}

	/**
	 * 已授权落单后台查询组装Page
	 * @param condition
	 * @param reportType
	 * @param orgIdOrEmployeeId
	 * @return
	 */
	@Override
	public IPage getReportOrder(PageCondition condition, String reportType, String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
		IPage page = null;
		if("ORG".equals(reportType)){
			page = getReportOrderORG(condition,orgIdOrEmployeeId);
		}else {
			page = getReportOrderEMP(condition,orgIdOrEmployeeId);
		}
		
		return page;
	}

	private IPage getReportOrderEMP(PageCondition condition, String orgIdOrEmployeeId) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select v.*, ")
			.append(" (select order_line.c_source_id from CRM_T_ORDER_LINES order_line ")
			.append(" where order_line.c_source_id is not null and v.c_pid = order_line.c_source_id and rownum = 1 )  as orderLineId")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append(" 	and v.c_pid in")
			.append("	    (select a.c_source_id from CRM_T_ORDER_LINES a)")
			.append("   and v.created_by_id = '").append(orgIdOrEmployeeId).append("' ")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("");
				bScaleVO.setIsOrder(StringUtils.isNotEmpty((String)objects[13])?"否":"是");
				bScaleVO.setIsReportFeedback("");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}

	
	private IPage getReportOrderORG(PageCondition condition, String orgIdOrEmployeeId) {
		// TODO Auto-generated method stub
				StringBuffer sql = new StringBuffer();
				sql .append(" select v.*, ")
					.append(" (select order_line.c_source_id from CRM_T_ORDER_LINES order_line ")
					.append(" where order_line.c_source_id is not null and v.c_pid = order_line.c_source_id and rownum = 1 )  as orderLineId")
					.append(" 	from kstat_bizopp_report_scale_v v  ")
					.append(" where 1=1 ")
					.append(" 	and v.c_pid in")
					.append("	    (select a.c_source_id from CRM_T_ORDER_LINES a)")
					.append("   and v.c_pid in  (")
					.append(" 		select t.BUSINESS_Id ")
					.append(" 		  from CRM_T_TEAM t,SYS_T_LOV_MEMBER p ")
					.append("		where ")
					.append("          p.GROUP_ID = 'POSITION' ")
					.append("          and p.LOV_PATH like '%").append(orgIdOrEmployeeId).append("%' ")
					.append("          and p.ROW_ID = t.POSITION_ID ")
					.append("          and t.BUSINESS_TYPE = 'BusinessOpportunity' ")
					.append(" )")
					.append(" order by v.created_at desc");
				List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
				IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
				List<Object[]> list = (List<Object[]>) p.getList();
				
				if(list != null && list.size() > 0){
					for(Object[] objects : list){
						BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
						bScaleVO.setOpportunityName((String)objects[0]);
						bScaleVO.setBizOppAddressName((String)objects[1]);
						bScaleVO.setClientName((String)objects[2]);
						bScaleVO.setCreatedById((String)objects[3]);
						bScaleVO.setCreatedOrgId((String)objects[4]);
						bScaleVO.setCreatedAt((Date)objects[5]);
						bScaleVO.setStatus((String)objects[6]);
						bScaleVO.setConflictStatus((String)objects[7]);
						bScaleVO.setSaleStage((String)objects[8]);
						bScaleVO.setSuccessDate((Date)objects[9]);
						bScaleVO.setEndDate((Date)objects[10]);
						bScaleVO.setIsReport("");
						bScaleVO.setIsOrder(StringUtils.isNotEmpty((String)objects[13])?"否":"是");
						bScaleVO.setIsReportFeedback("");
						
						bizoopReportScaleVOs.add(bScaleVO);
					}
				}
				IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
				return page;
	}


	/**
	 * 授权反馈率
	 */
	@Override
	public int getBidReportScale(String type, String id) {
		int scale = 0;
		float bizoppSum = 0f;
		float reportSum = 0f;
		if("ORG".equals(type)){
			reportSum = getBidReportORG(id);
			bizoppSum = getBizoppORG(id);
		}else {
			reportSum = getBidReportEMP(id);
			bizoppSum = getBizoppEMP(id);
		}
		if(bizoppSum>0) {
			BigDecimal sBigDecimal = new BigDecimal(String.valueOf((reportSum / bizoppSum)*100)).setScale(0, BigDecimal.ROUND_HALF_UP);
			scale = sBigDecimal.intValue();
		}
		return scale;
	}

	/**
	 * 通过组织id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过,并且在“业务用章申请”中状态为已审批，
	 * 并且“投标结果”不为空的所有商机总数
	 * @param id
	 * @return
	 */
	private float getBidReportORG(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0) " )
			.append("	from crm_t_business_opportunity b,crm_t_bid t ")
			.append(" where 1=1 ")
			.append(" and t.c_business_opp_id = b.c_pid ")
			.append(" and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      b.c_status = '100'or  b.c_status = '110') ")
			.append(" and b.c_pid in")
			.append("	(select t.BUSINESS_Id")
			.append("		from CRM_T_TEAM t, SYS_T_LOV_MEMBER p")
			.append("    where p.GROUP_ID = 'POSITION'")
			.append("		and p.LOV_PATH like '%").append(id).append("%'")
			.append(" 		and p.ROW_ID = t.POSITION_ID")
			.append(" 		and t.BUSINESS_TYPE = 'BusinessOpportunity')")
			.append(" and t.c_status = 'Completed'")
			.append(" and t.c_bid_result is not null ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}
	
	/**
	 * 通过员工id 获取商机在处理状态为报备已通过，立项已通过
	 * 立项审批中，立项未通过,并且在“业务用章申请”中状态为已审批，
	 * 并且“投标结果”不为空的所有商机总数
	 * @param id
	 * @return
	 */
	private float getBidReportEMP(String id) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select count(0) " )
			.append("	from crm_t_business_opportunity b,crm_t_bid t ")
			.append(" where 1=1 ")
			.append(" and t.c_business_opp_id = b.c_pid ")
			.append(" and (b.c_status = '20' or  b.c_status = '120' or  ")
			.append("      b.c_status = '100'or  b.c_status = '110') ")
			.append(" and b.created_by_id = '").append(id).append("' ")
			.append(" and t.c_status = 'Completed' ")
			.append(" and t.c_bid_result is not null ");
		BigDecimal sum = baseDao.findUniqueBySql(sql.toString());
		return sum.floatValue();
	}
	
	/**
	 * 授权反馈率后台查询组装Page
	 * @param condition
	 * @param reportType
	 * @param orgIdOrEmployeeId
	 * @return
	 */
	@Override
	public IPage getBidReportScale(PageCondition condition, String reportType, String orgIdOrEmployeeId) {
		IPage page = null;
		if("ORG".equals(reportType)){
			page = getBidReportORG(condition,orgIdOrEmployeeId);
		}else {
			page = getBidReportEMP(condition,orgIdOrEmployeeId);
		}
		
		return page;
	}
	
	private IPage getBidReportORG(PageCondition condition, String orgIdOrEmployeeId) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select v.*, ")
			.append(" (select t.c_bid_result from crm_t_bid t ")
			.append(" where t.c_status = 'Completed' and v.c_pid = t.c_business_opp_id and rownum = 1 )  as c_bid_result")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append(" 	and v.c_pid in")
			.append("	    (select a.c_source_id from CRM_T_ORDER_LINES a)")
			.append("   and v.created_by_id = '").append(orgIdOrEmployeeId).append("' ")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("");
				bScaleVO.setIsOrder("");
				bScaleVO.setIsReportFeedback(StringUtils.isNotEmpty((String)objects[13])?"否":"是");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}
	
	private IPage getBidReportEMP(PageCondition condition,String orgIdOrEmployeeId) {
		StringBuffer sql = new StringBuffer();
		sql .append(" select v.*, ")
			.append(" (select t.c_bid_result from crm_t_bid t ")
			.append(" where t.c_status = 'Completed' and v.c_pid = t.c_business_opp_id and rownum = 1 )  as c_bid_result")
			.append(" 	from kstat_bizopp_report_scale_v v  ")
			.append(" where 1=1 ")
			.append("   and v.created_by_id = '").append(orgIdOrEmployeeId).append("' ")
			.append(" order by v.created_at desc");
		List<BizoopReportScaleVO> bizoopReportScaleVOs = new ArrayList<BizoopReportScaleVO>();
		IPage p = baseDao.searchBySql(sql.toString(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) p.getList();
		
		if(list != null && list.size() > 0){
			for(Object[] objects : list){
				BizoopReportScaleVO bScaleVO = new BizoopReportScaleVO();
				bScaleVO.setOpportunityName((String)objects[0]);
				bScaleVO.setBizOppAddressName((String)objects[1]);
				bScaleVO.setClientName((String)objects[2]);
				bScaleVO.setCreatedById((String)objects[3]);
				bScaleVO.setCreatedOrgId((String)objects[4]);
				bScaleVO.setCreatedAt((Date)objects[5]);
				bScaleVO.setStatus((String)objects[6]);
				bScaleVO.setConflictStatus((String)objects[7]);
				bScaleVO.setSaleStage((String)objects[8]);
				bScaleVO.setSuccessDate((Date)objects[9]);
				bScaleVO.setEndDate((Date)objects[10]);
				bScaleVO.setIsReport("");
				bScaleVO.setIsOrder(StringUtil.isNullOrEmpty((String)objects[13])?"否":"是");
				bScaleVO.setIsReportFeedback("");
				
				bizoopReportScaleVOs.add(bScaleVO);
			}
		}
		IPage page = new PageImpl(bizoopReportScaleVOs,condition.getPage(), condition.getRows(),p.getCount());
		return page;
	}
}
