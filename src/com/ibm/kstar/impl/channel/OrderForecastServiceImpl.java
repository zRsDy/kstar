package com.ibm.kstar.impl.channel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.channel.IOrderForecastService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.channel.DeliveryForecastDetail;
import com.ibm.kstar.entity.channel.OrderForecast;
import com.ibm.kstar.entity.channel.OrderForecastDetail;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class OrderForecastServiceImpl extends BaseServiceImpl implements IOrderForecastService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IProcessService processService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	private ILovMemberService lovMemberService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	protected ITeamService teamService;

	@Override
	public IPage queryOrderForecasts(PageCondition condition, UserObject user) {
		List<Object> args = new ArrayList<>();
		StringBuilder hql = new StringBuilder("select f from OrderForecast f,LovMember v where f.forecastUnit = v.id");
		//数据权限查询
		String phql = PermissionUtil.getPermissionHQL(null, "f.createdById", "f.createdPosId", "f.createdOrgId", "f.id", user, ProcessConstants.ORDER_FORECAST_PROC);
		hql.append(" and ").append(phql).append(" ");
		
		this.addQueryCondition(condition, args, hql, "forecastCode", "f.forecastCode", "like");
		this.addQueryCondition(condition, args, hql, "forecastUnit", "v.name", "like");
		hql.append(" order by f.id desc");
		IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<OrderForecast> list = (List<OrderForecast>) page.getList();
		for(OrderForecast forecast : list){
			forecast.setApplierName(employeeService.get(forecast.getApplier()).getName());
		}
		return page;
	}

	@Override
	public OrderForecast queryOrderForecast(String id) {
		return baseDao.get(OrderForecast.class, id);
	}

	@Override
	public void addOrEditForecast(OrderForecast orderForecast, UserObject user) {
		if(orderForecast.getId() != null){
			OrderForecast forecastData = baseDao.get(OrderForecast.class, orderForecast.getId());
			BeanUtils.copyPropertiesIgnoreNull(orderForecast,forecastData);
			forecastData.setRecordInfor(true, user);
			baseDao.update(forecastData);
		}else{
			orderForecast.setRecordInfor(false, user);
			baseDao.save(orderForecast);
			//加销售团队
			teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.ORDER_FORECAST_PROC,orderForecast.getId());
		}
	}
	
	@Override
	public void deleteForecast(String id) {
		OrderForecast forecastInfo = baseDao.get(OrderForecast.class, id);
		baseDao.delete(forecastInfo);
	}
	
	@Override
	public void submitForecast(UserObject user,String id) {
		//启动流程
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.ORDER_FORECAST_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new OrderForecast().getLovMember("APPLICATION", model).getName();
		}
		OrderForecast apply = baseDao.get(OrderForecast.class, id);
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.ORDER_FORECAST_PROC, ProcessConstants.PROCESS_STATUS_02);
		processStatusService.updateProcessStatus("OrderForecast", id, "status", lov.getId());
		Map<String,String> varmap = new HashMap<>();
		varmap.put("ORG_TYPE", apply.getDealerCode());
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.ORDER_FORECAST_PROC, flowName+"-"+apply.getForecastCode(), id, user, varmap);
	}

	@Override
	public IPage queryForecastDetails(PageCondition condition) {
		List<Object> args = new ArrayList<>();
		StringBuilder hql = new StringBuilder("select d from OrderForecastDetail d,CustomInfo c where d.customer = c.id");
		this.addQueryCondition(condition, args, hql, "forecastId", "d.forecastId", "=");
		this.addQueryCondition(condition, args, hql, "productSeries", "d.productSeries", "like");
		this.addQueryCondition(condition, args, hql, "productKind", "d.productKind", "like");
		this.addQueryCondition(condition, args, hql, "materielCode", "d.materielCode", "like");
		this.addQueryCondition(condition, args, hql, "customerName", "c.customFullName", "like");
		hql.append(" order by d.id desc");
		IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<OrderForecastDetail> list = (List<OrderForecastDetail>) page.getList();
		for(OrderForecastDetail detail : list){
			CustomInfo customInfo = customService.getCustomInfo(detail.getCustomer());
			if(customInfo != null){
				detail.setCustomerName(customInfo.getCustomFullName());
			}
		}
		return page;
	}
	
	@Override
	public OrderForecastDetail queryForecastDetail(String id) {
		return baseDao.get(OrderForecastDetail.class, id);
	}
	
	@Override
	public void addOrEditDetail(OrderForecastDetail forecastDetail, UserObject user) {
		if(forecastDetail.getId() != null){
			OrderForecastDetail detailData = baseDao.get(OrderForecastDetail.class, forecastDetail.getId());
			BeanUtils.copyPropertiesIgnoreNull(forecastDetail,detailData);
			detailData.setRecordInfor(true, user);
			baseDao.update(detailData);
		}else{
			if(StringUtil.isEmpty(forecastDetail.getMaterielCode())){
				List<Object[]> materielCodeLst = baseDao.findBySql("select distinct b.c_mater_code from CRM_T_PRODUCT_BASIC b where b.c_pro_model = ?",forecastDetail.getProductKind());
				if(materielCodeLst != null && materielCodeLst.size() > 0){
					Object materielCode = (Object) materielCodeLst.get(0);
					forecastDetail.setMaterielCode(materielCode.toString());
				}
			}
			forecastDetail.setRecordInfor(false, user);
			baseDao.save(forecastDetail);
		}
	}

	@Override
	public void deleteForecastDetail(String id) {
		OrderForecastDetail detailInfo = baseDao.get(OrderForecastDetail.class, id);
		baseDao.delete(detailInfo);
	}

	@Override
	public IPage queryDeliveryForecastDetails(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(DeliveryForecastDetail.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public DeliveryForecastDetail queryDeliveryForecastDetail(String id) {
		return baseDao.get(DeliveryForecastDetail.class, id);
	}

	@Override
	public void addOrEditDelivery(DeliveryForecastDetail delivery, UserObject user) {
		if(delivery.getId() != null){
			DeliveryForecastDetail deliveryData = baseDao.get(DeliveryForecastDetail.class,delivery.getId());
			BeanUtils.copyPropertiesIgnoreNull(delivery,deliveryData);
			deliveryData.setRecordInfor(true, user);
			baseDao.update(deliveryData);
		}else{
			delivery.setRecordInfor(false, user);
			baseDao.save(delivery);
		}
	}

	@Override
	public void deleteDeliveryForecastDetail(String id) {
		DeliveryForecastDetail deliveryInfo = baseDao.get(DeliveryForecastDetail.class, id);
		baseDao.delete(deliveryInfo);
	}

	@Override
	public IPage queryOrderForecastGathers(PageCondition condition) {
		StringBuilder sql = getQueryGatherSql();
		List<Object> args = new ArrayList<Object>();
		this.addQueryCondition(condition, args, sql, "forecastWeek", "o.c_forecast_week", "=");
		this.addQueryCondition(condition, args, sql, "productSeries", "d.c_product_series", "like");
		this.addQueryCondition(condition, args, sql, "productKind", "d.c_product_kind", "like");
		this.addQueryCondition(condition, args, sql, "materielCode", "d.c_materiel_code", "like");
		sql.append(" group by o.c_forecast_week,d.c_materiel_code,d.c_product_series,d.c_product_kind,m.lov_name");
		return baseDao.searchBySql(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public IPage queryOrderForecastGatherDetails(PageCondition condition) {
		String forecastWeek = condition.getStringCondition("forecastWeek");
		String materielCode = condition.getStringCondition("materielCode");
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = getQueryGatherDetailSql();
		args.add(forecastWeek);
		args.add(materielCode);
		this.addQueryCondition(condition, args, sql, "forecastCode", "o.c_forecast_code", "like");
		this.addQueryCondition(condition, args, sql, "forecastUnit", "m1.lov_name", "like");
		sql.append(" order by o.c_forecast_code");
		return baseDao.searchBySql(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public List<List<Object>> exportGather() {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addGatherHead(lstOut);
		StringBuilder sql = getQueryGatherSql();
		sql.append(" group by o.c_forecast_week,d.c_materiel_code,d.c_product_series,d.c_product_kind,m.lov_name");
		List<Object[]> lst = baseDao.findBySql(sql.toString());
		int index = 0;
		for(Object[] array : lst){
			index = 1;
			List<Object> lstIn = new ArrayList<Object>();
			for(int i=0; i<8; i++){
				lstIn.add(array[index++]);
			}
			lstOut.add(lstIn);
		}
		return lstOut;
	}

	@Override
	public List<List<Object>> exportGatherDetail(String materielCode,String forecastWeek) {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addGatherDetailHead(lstOut);
		StringBuilder sql = getQueryGatherDetailSql();
		List<Object[]> lst = baseDao.findBySql(sql.toString(), new Object[]{forecastWeek,materielCode});
		int index = 0;
		for(Object[] array : lst){
			index = 0;
			List<Object> lstIn = new ArrayList<Object>();
			for(int i=0; i<10; i++){
				lstIn.add(array[index++]);
			}
			lstOut.add(lstIn);
		}
		return lstOut;
	}
	
	private void  addGatherHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("预测起始周次");
		lstHead.add("产品系列");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("第一周数量");
		lstHead.add("第二周数量");
		lstHead.add("第三周数量");
		lstHead.add("第四周数量");
		lstOut.add(lstHead);
	}
	
	private StringBuilder getQueryGatherSql(){
		StringBuilder sql = new StringBuilder("select"
				+" o.c_forecast_week, m.lov_name, d.c_product_series,d.c_product_kind,d.c_materiel_code,sum(d.c_first_week_quantity),sum(d.c_second_week_quantity),sum(d.c_third_week_quantity),sum(d.c_fourth_week_quantity)"
				+" from crm_t_order_forecast o,crm_t_order_forecast_detail d,sys_t_lov_member m where o.c_pid=d.c_forecast_id and o.c_forecast_week = m.row_id");
		return sql;
	}
	
	private void  addGatherDetailHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("预测单号");
		lstHead.add("预测单位");
		lstHead.add("状态");
		lstHead.add("预测日期");
		lstHead.add("提交人");
		lstHead.add("提交人电话");
		lstHead.add("第一周数量");
		lstHead.add("第二周数量");
		lstHead.add("第三周数量");
		lstHead.add("第四周数量");
		lstOut.add(lstHead);
	}
	
	private StringBuilder getQueryGatherDetailSql(){
		StringBuilder sql = new StringBuilder("select o.c_forecast_code,m1.lov_name,m2.lov_name \"status\",o.c_forecast_date,"
				+" e.name,o.c_applier_phone,d.c_first_week_quantity,d.c_second_week_quantity,d.c_third_week_quantity,d.c_fourth_week_quantity"
				+" from crm_t_order_forecast o,crm_t_order_forecast_detail d,SYS_T_LOV_MEMBER m1,SYS_T_LOV_MEMBER m2,SYS_T_PERMISSION_EMPLOYEE e" 
				+" where o.c_pid=d.c_forecast_id and o.c_forecast_unit = m1.row_id and o.c_status=m2.row_id and o.c_applier=e.row_id"
				+" and o.c_forecast_week = ? and d.c_materiel_code = ?");
		return sql;
	}
}