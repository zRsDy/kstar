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
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.channel.IServiceApplyService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.channel.ApplyEquipment;
import com.ibm.kstar.entity.channel.ServiceApply;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ServiceApplyServiceImpl extends BaseServiceImpl implements IServiceApplyService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	protected ITeamService teamService;

	@Override
	public IPage queryServiceApplys(PageCondition condition, UserObject user) {
		StringBuilder sql = getqueryServiceApplysSql();
		List<Object> args = new ArrayList<>();
		//数据权限查询
		String phql = PermissionUtil.getPermissionSQL(null, "a.c_created_by_id", "a.c_created_pos_id", "a.c_created_org_id", "a.c_pid", user, ProcessConstants.SERVICE_APPLY_PROC);
		sql.append(" and ").append(phql).append(" ");
		
		String searchKey = condition.getStringCondition("searchKey");
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and (a.c_apply_code like ? or m1.lov_name like ?)");
			args.add("%"+searchKey.trim()+"%");
			args.add("%"+searchKey.trim()+"%");
		}
		sql.append(" order by a.c_pid desc");
		return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveOrUpdateServiceApply(ServiceApply serviceApply, UserObject user) {
		if(serviceApply.getId() != null){
			ServiceApply applyData = baseDao.get(ServiceApply.class, serviceApply.getId());
			BeanUtils.copyPropertiesIgnoreNull(serviceApply,applyData);
			applyData.setRecordInfor(true, user);
			baseDao.update(applyData);
		}else{
			serviceApply.setRecordInfor(false, user);
			baseDao.save(serviceApply);
			//加销售团队
			teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.SERVICE_APPLY_PROC,serviceApply.getId());
		}
	}

	@Override
	public ServiceApply queryServiceApply(String id) {
		 ServiceApply apply = baseDao.get(ServiceApply.class, id);
		 Employee employee = employeeService.get(apply.getApplier());
		 if(employee != null){
			 apply.setApplierName(employee.getName());
		 }
		 return apply;
	}

	@Override
	public void deleteServiceApply(ServiceApply serviceApply) {
		baseDao.delete(serviceApply);
	}
	@Override
	public IPage queryApplyEquipments(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(ApplyEquipment.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveOrUpdateApplyEquipment(ApplyEquipment applyEquipment, UserObject user) {
		if(applyEquipment.getId() != null){
			ApplyEquipment equipData = baseDao.get(ApplyEquipment.class, applyEquipment.getId());
			BeanUtils.copyPropertiesIgnoreNull(applyEquipment,equipData);
			equipData.setRecordInfor(true, user);
			baseDao.update(equipData);
		}else{
			applyEquipment.setRecordInfor(false, user);
			baseDao.save(applyEquipment);
		}
	}

	@Override
	public ApplyEquipment queryApplyEquipment(String id) {
		return baseDao.get(ApplyEquipment.class, id);
	}

	@Override
	public void deleteApplyEquipment(ApplyEquipment applyEquipment) {
		baseDao.delete(applyEquipment);
	}

	private StringBuilder getqueryServiceApplysSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" a.c_pid \"id\",");
		sql.append(" a.c_created_by_id \"createdById\",");
		sql.append(" a.c_apply_code \"applyCode\",");
		sql.append(" m1.lov_name \"applyUnitName\",");
		sql.append(" m2.lov_name \"dealerName\",");
		sql.append(" m3.lov_name \"serviceTypeName\",");
		sql.append(" m4.lov_name  \"statusName\",");
		sql.append(" m5.lov_name  \"currencyName\",");
		sql.append(" (select nvl(sum(nvl(eq.c_service_price*eq.c_service_quantity,0)),0) from crm_t_apply_equipment eq where eq.c_apply_id = a.c_pid) \"serviceExpense\",");
		sql.append(" a.c_apply_date \"applyDate\",");
		sql.append(" a.c_demand_finish_date \"demandFinishDate\",");
		sql.append(" e.name \"applierName\",");
		sql.append(" a.c_applier_phone \"applierPhone\",");
		sql.append(" a.c_explain \"explain\"");
		sql.append(" from crm_t_service_apply a");
		sql.append(" left join sys_t_lov_member m1 on a.c_apply_unit = m1.row_id");
		sql.append(" left join sys_t_lov_member m2 on a.c_dealer = m2.row_id");
		sql.append(" left join sys_t_lov_member m3 on a.c_service_type = m3.row_id");
		sql.append(" left join sys_t_lov_member m4 on a.c_status = m4.row_id");
		sql.append(" left join sys_t_lov_member m5 on a.c_currency = m5.row_id");
		sql.append(" left join SYS_T_PERMISSION_EMPLOYEE e on a.c_applier = e.row_id");
		sql.append(" where 1=1");
		return sql;
	}

	@Override
	public void submit(String id, UserObject user) {
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.SERVICE_APPLY_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new ServiceApply().getLovMember("APPLICATION", model).getName();
		}
		ServiceApply apply = baseDao.get(ServiceApply.class, id);
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.SERVICE_APPLY_PROC, ProcessConstants.PROCESS_STATUS_02);
		processStatusService.updateProcessStatus("ServiceApply", id, "status", lov.getId());
		//启动流程
		Map<String,String> varmap = new HashMap<>();
		varmap.put("ORG_TYPE", apply.getDealerCode());
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.SERVICE_APPLY_PROC, flowName+"-"+apply.getApplyCode(), id, user, varmap);
	}
}