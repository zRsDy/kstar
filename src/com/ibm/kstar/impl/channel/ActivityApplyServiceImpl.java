package com.ibm.kstar.impl.channel;


import java.util.ArrayList;
import java.util.Date;
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
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.channel.IActivityApplyService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.channel.ActivityApply;
import com.ibm.kstar.entity.channel.ActivityContent;
import com.ibm.kstar.entity.channel.ActivityExpense;
import com.ibm.kstar.entity.channel.ActivityPerson;
import com.ibm.kstar.entity.channel.ActivitySummary;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ActivityApplyServiceImpl extends BaseServiceImpl implements IActivityApplyService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IProcessService processService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	protected ITeamService teamService;
	
	@Override
	public IPage queryApplys(PageCondition condition, UserObject user) {
		String businessType = condition.getStringCondition("businessType");
		List<Object> args = new ArrayList<Object>();
		StringBuilder sb = getQueryApplysSql();
		sb.append(" where a.c_business_type = ?");
		args.add(businessType);
		
		if("activity".equals(businessType)){
			String phql = PermissionUtil.getPermissionSQL(null, "a.c_created_by_id", "a.c_created_pos_id", "a.c_created_org_id", "a.c_pid", user, ProcessConstants.ACTIVITY_APPLY_PROC);
			sb.append(" and ").append(phql).append(" ");
		}else if("train".equals(businessType)){
			String phql = PermissionUtil.getPermissionSQL(null, "a.c_created_by_id", "a.c_created_pos_id", "a.c_created_org_id", "a.c_pid", user, ProcessConstants.TRAIN_APPLY_PROC);
			sb.append(" and ").append(phql).append(" ");
		}
		
		this.addQueryCondition(condition, args, sb, "applyCode", "a.c_apply_code", "like");
		this.addQueryCondition(condition, args, sb, "activityPurpose", "a.c_activity_purpose", "like");
		this.addQueryCondition(condition, args, sb, "activityType", "a.c_activity_type", "=");
		this.addQueryCondition(condition, args, sb, "industryType", "a.c_industry_type", "=");
		sb.append(" order by a.c_pid desc");
		return baseDao.searchBySql4Map(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public ActivityApply queryApply(String id) {
		ActivityApply apply = baseDao.get(ActivityApply.class, id);
		Employee employee = employeeService.get(apply.getApplier());
		if(employee != null){
			apply.setApplierName(employee.getName());
		}
		return apply;
	}
	
	@Override
	public void addOrEditApply(ActivityApply activityApply, UserObject user) {
		if(activityApply.getId() != null){
			ActivityApply applyData = baseDao.get(ActivityApply.class,activityApply.getId());
			BeanUtils.copyPropertiesIgnoreNull(activityApply,applyData);
			applyData.setRecordInfor(true, user);
			baseDao.update(applyData);
		}else{
			activityApply.setRecordInfor(false, user);
			baseDao.save(activityApply);
			if("activity".equals(activityApply.getBusinessType())){
				teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.ACTIVITY_APPLY_PROC,activityApply.getId());
			}else if("train".equals(activityApply.getBusinessType())){
				teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.TRAIN_APPLY_PROC,activityApply.getId());
			}
		}
	}
	
	@Override
	public ActivitySummary querySummary(String applyId) {
		return baseDao.findUniqueEntity("from ActivitySummary s where s.applyId = ?", applyId);
	}
	
	@Override
	public void checkSummary(String applyId) {
		ActivityApply activityApply = baseDao.get(ActivityApply.class, applyId);
		if(activityApply == null || !"03".equals(activityApply.getStatusCode())){
			throw new AnneException("只有审批通过的申请才能总结！");
		}
		List<ActivityContent> contentLst = baseDao.findEntity("from ActivityContent c where c.status = '未执行' and c.applyId = ?", applyId);
		if(contentLst != null && contentLst.size() > 0){
			throw new AnneException("有未执行的主要活动内容，不能总结！");
		}
	}

	@Override
	public void addOrEditSummary(ActivitySummary activitySummary, UserObject user) {
		ActivitySummary summaryData = this.querySummary(activitySummary.getApplyId());
		if(summaryData != null){
			BeanUtils.copyPropertiesIgnoreNull(activitySummary,summaryData);
			summaryData.setRecordInfor(true, user);
			baseDao.update(summaryData);
		}else{
			activitySummary.setRecordInfor(false, user);
			baseDao.save(activitySummary);
		}
	}
	
	@Override
	public void deleteApply(String id) {
		ActivityApply applyInfo = baseDao.get(ActivityApply.class, id);
		baseDao.delete(applyInfo);
	}
	
	@Override
	public void checkCloseApply(String id) {
		List<ActivitySummary> summaryLst = baseDao.findEntity("from ActivitySummary s where s.applyId = ?", id);
		if(summaryLst == null || summaryLst.size() < 1){
			throw new AnneException("没总结，不能关闭！");
		}
	}
	
	@Override
	public void closeApply(String id, UserObject user) {
		ActivityApply applyInfo = baseDao.get(ActivityApply.class, id);
		applyInfo.setCloseDate(new Date());
		applyInfo.setStatus(applyInfo.getLovMember(ProcessConstants.ACTIVITY_APPLY_PROC, ProcessConstants.PROCESS_STATUS_05).getId());
		applyInfo.setRecordInfor(true, user);
		baseDao.update(applyInfo);
	}

	@Override
	public IPage queryContents(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(ActivityContent.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		IPage page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<ActivityContent> list = (List<ActivityContent>) page.getList();
		for(ActivityContent content : list){
			if(content.getMaker() != null){
				Employee employee = employeeService.get(content.getMaker());
				if(employee != null){
					content.setMakerName(employee.getName());
				}
			}
		}
		return page;
	}

	@Override
	public ActivityContent queryContent(String id) {
		return baseDao.get(ActivityContent.class, id);
	}

	@Override
	public void addOrEditContent(ActivityContent activityContent, UserObject user) {
		if(activityContent.getId() != null){
			ActivityContent contentData = baseDao.get(ActivityContent.class, activityContent.getId());
			BeanUtils.copyPropertiesIgnoreNull(activityContent,contentData);
			contentData.setRecordInfor(true, user);
			baseDao.update(contentData);
		}else{
			activityContent.setStatus("未执行");
			activityContent.setRecordInfor(false, user);
			baseDao.save(activityContent);
		}
	}
	
	@Override
	public void deleteContent(String id) {
		ActivityContent contentInfo = baseDao.get(ActivityContent.class, id);
		baseDao.delete(contentInfo);
	}

	@Override
	public void makeContent(String[] ids,String makerId, UserObject user) {
		for(String id : ids){
			ActivityContent contentData = baseDao.get(ActivityContent.class, id);
			contentData.setMaker(makerId);
			contentData.setActualActDate(new Date());
			contentData.setStatus("已执行");
			contentData.setRecordInfor(true, user);
			baseDao.update(contentData);
		}
	}
	
	@Override
	public IPage queryPersons(PageCondition condition) {
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		String applyId = condition.getStringCondition("applyId");
		String internalPerson = condition.getStringCondition("internalPerson");
		String applyId1 = condition.getStringCondition("applyId1");
		if(applyId == null || "".equals(applyId)){
			applyId = applyId1;
		}
		List<Object> args = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from ActivityPerson p where p.applyId = ? and p.internalPerson = ?");
		args.add(applyId);
		args.add(internalPerson);
		if(StringUtil.isNotEmpty(searchKey)){
			if("yes".equals(internalPerson)){
				hql.append(" and (p.personName like ? or p.department like ?)");
				args.add("%"+searchKey.trim()+"%");
				args.add("%"+searchKey.trim()+"%");
			}else{
				hql.append(" and (p.personName like ? or p.sellerName like ?)");
				args.add("%"+searchKey.trim()+"%");
				args.add("%"+searchKey.trim()+"%");
			}
		}
		hql.append(" order by p.id desc");
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public ActivityPerson queryPerson(String id) {
		 ActivityPerson person = baseDao.get(ActivityPerson.class, id);
		 return person;
	}

	@Override
	public void addOrEditPerson(ActivityPerson activityPerson, UserObject user) {
		if(activityPerson.getId() != null){
			ActivityPerson personData = baseDao.get(ActivityPerson.class,activityPerson.getId());
			BeanUtils.copyPropertiesIgnoreNull(activityPerson,personData);
			personData.setRecordInfor(true, user);
			baseDao.update(personData);
		}else{
			activityPerson.setRecordInfor(false, user);
			baseDao.save(activityPerson);
		}
	}
	
	@Override
	public void deletePerson(String id) {
		ActivityPerson personInfo = baseDao.get(ActivityPerson.class, id);
		baseDao.delete(personInfo);
	}
	
	@Override
	public IPage queryExpenses(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(ActivityExpense.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public ActivityExpense queryExpense(String id) {
		return baseDao.get(ActivityExpense.class, id);
	}

	@Override
	public void addOrEditExpense(ActivityExpense activityExpense, UserObject user) {
		if(activityExpense.getId() != null){
			ActivityExpense expenseData = baseDao.get(ActivityExpense.class, activityExpense.getId());
			BeanUtils.copyPropertiesIgnoreNull(activityExpense,expenseData);
			expenseData.setRecordInfor(true, user);
			baseDao.update(expenseData);
		}else{
			activityExpense.setRecordInfor(false, user);
			baseDao.save(activityExpense);
		}
	}

	@Override
	public void deleteExpense(String id) {
		ActivityExpense expenseInfo = baseDao.get(ActivityExpense.class,id);
		baseDao.delete(expenseInfo);
	}
	
	@Override
	public void checkSubmit(String id) {
		List<ActivityContent> contentLst = baseDao.findEntity("from ActivityContent c where c.applyId = ?", id);
		if(contentLst == null || contentLst.size() < 1){
			throw new AnneException("没有主要活动内容，不能提交！");
		}
	}

	@Override
	public void submitActivity(String id, UserObject user) {
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.ACTIVITY_APPLY_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new ActivityApply().getLovMember("APPLICATION", model).getName();
		}
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.ACTIVITY_APPLY_PROC, ProcessConstants.PROCESS_STATUS_02);
		ActivityApply apply = baseDao.get(ActivityApply.class, id);
		apply.setStatus(lov.getId());
		apply.setRecordInfor(true, user);
		baseDao.update(apply);
		//启动流程
		Map<String,String> varmap = new HashMap<>();
		varmap.put("ORG_TYPE", apply.getDealerCode());
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.ACTIVITY_APPLY_PROC, flowName+"-"+apply.getApplyCode(), id, user, varmap);
	}
	
	@Override
	public void submitTrain(String id, UserObject user) {
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.TRAIN_APPLY_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new ActivityApply().getLovMember("APPLICATION", model).getName();
		}
		//因活动与培训共用一表一字段，所以状态字典用同一个
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.ACTIVITY_APPLY_PROC, ProcessConstants.PROCESS_STATUS_02);
		ActivityApply apply = baseDao.get(ActivityApply.class, id);
		apply.setStatus(lov.getId());
		apply.setRecordInfor(true, user);
		baseDao.update(apply);
		//启动流程
		Map<String,String> varmap = new HashMap<>();
		varmap.put("ORG_TYPE", apply.getDealerCode());
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.TRAIN_APPLY_PROC, flowName+"-"+apply.getApplyCode(), id, user, varmap);
	}
	
	private StringBuilder getQueryApplysSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" a.c_pid \"id\",");
		sql.append(" a.c_created_by_id \"createdById\",");
		sql.append(" a.c_apply_code \"applyCode\",");
		sql.append(" ma.lov_name \"applyUnitName\",");
		sql.append(" md.lov_name \"dealerName\",");
		sql.append(" mt.lov_name \"activityTypeName\",");
		sql.append(" mi.lov_name \"industryTypeName\",");
		sql.append(" a.c_activity_purpose \"activityPurpose\",");
		sql.append(" ms.lov_name \"statusName\",");
		sql.append(" mc.lov_name \"currencyName\",");
		sql.append(" a.c_estimated_expense \"estimatedExpense\",");
		sql.append(" (select nvl(sum(nvl(e.c_actual_expense,0)),0) from crm_t_activity_expense e where e.c_charge_expense = '是' and e.c_apply_id = a.c_pid) \"receivableExpense\",");
		sql.append(" a.c_apply_date \"applyDate\",");
		sql.append(" a.c_estimated_start_date \"estimatedStartDate\",");
		sql.append(" a.c_close_date \"closeDate\",");
		sql.append(" mo.lov_name \"organizationName\",");
		sql.append(" e.name \"applierName\",");
		sql.append(" a.c_applier_phone \"applierPhone\",");
		sql.append(" a.c_explain \"explain\"");
		sql.append(" from crm_t_activity_apply a");
		sql.append(" left join sys_t_lov_member ma on a.c_apply_unit = ma.row_id");
		sql.append(" left join sys_t_lov_member md on a.c_dealer = md.row_id");
		sql.append(" left join sys_t_lov_member mt on a.c_activity_type = mt.row_id");
		sql.append(" left join sys_t_lov_member mi on a.c_industry_type = mi.row_id");
		sql.append(" left join sys_t_lov_member ms on a.c_status = ms.row_id");
		sql.append(" left join sys_t_lov_member mc on a.c_currency = mc.row_id");
		sql.append(" left join sys_t_lov_member mo on a.c_organization = mo.row_id");
		sql.append(" left join SYS_T_PERMISSION_EMPLOYEE e on a.c_applier = e.row_id");
		return sql;
	}

	@Override
	public IPage querySelectPersons(PageCondition condition) {
		String searchKey = condition.getStringCondition("searchKey");
		String isInner = condition.getStringCondition("isInner");
		List<Object> orgs = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select e.row_id \"employeeId\",e.no \"employeeNo\",e.name \"employeeName\",p.lov_name \"positionName\",o.lov_name \"orgName\"");  
		sql.append(" from SYS_T_LOV_MEMBER o,SYS_T_LOV_MEMBER p,SYS_T_PERMISSION_EMPLOYEE e,SYS_T_PERMISSION_USER_REL up");
		sql.append(" where o.GROUP_ID='ORG' and p.GROUP_ID='POSITION' and p.OPT_TXT1=o.ROW_ID and up.USER_ID=e.ROW_ID and up.MEMBER_ID=p.ROW_ID");
		if("0".equals(isInner)){
			sql.append(" and o.opt_txt3 = 'E'");
		}else if("1".equals(isInner)){
			sql.append(" and o.opt_txt3 != 'E'");
		}
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and (e.no like ? or e.name like ? or p.lov_name like ? or o.lov_name like ?)");
			orgs.add("%"+searchKey.trim()+"%");
			orgs.add("%"+searchKey.trim()+"%");
			orgs.add("%"+searchKey.trim()+"%");
			orgs.add("%"+searchKey.trim()+"%");
		}
		sql.append(" order by e.row_id desc");
		return baseDao.searchBySql4Map(sql.toString(), orgs.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void updateColumnValue(String contentId, String column, String value, UserObject user) {
		ActivityContent content = baseDao.get(ActivityContent.class, contentId);
		if("makeExplain".equals(column)){
			content.setMakeExplain(value);
		}
		content.setRecordInfor(true, user);
		baseDao.update(content);
	}

}