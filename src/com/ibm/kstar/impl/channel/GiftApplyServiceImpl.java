package com.ibm.kstar.impl.channel;


import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.channel.IGiftApplyService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.channel.GiftApply;
import com.ibm.kstar.entity.channel.GiftApplyDetail;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class GiftApplyServiceImpl extends BaseServiceImpl implements IGiftApplyService {
	
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
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = getQueryApplysSql();
		
		String phql = PermissionUtil.getPermissionSQL(null, "a.c_created_by_id", "a.c_created_pos_id", "a.c_created_org_id", "a.c_pid", user, ProcessConstants.GIFT_APPLY_PROC);
		sql.append(" and ").append(phql).append(" ");
		
		this.addQueryCondition(condition, args, sql, "applyCode", "a.c_apply_code", "like");
		this.addQueryCondition(condition, args, sql, "applyUnitName", "m.lov_name", "like");
		this.addQueryCondition(condition, args, sql, "customName", "c.c_custom_full_name", "like");
		sql.append(" order by a.c_pid desc");
		return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public GiftApply queryApply(String id) {
		GiftApply apply = baseDao.get(GiftApply.class, id);
		apply.setApplierName(employeeService.get(apply.getApplier()).getName());
		String sql ="select nvl(sum(d.c_apply_quantity*g.c_price),0) from crm_t_gift_apply_detail d,crm_t_gift_manage g where d.c_gift_id = g.c_pid";
		sql += " and d.c_apply_id = ?";
		List<Object[]> lst = baseDao.findBySql(sql, id);
		if(lst != null && lst.size() > 0){
			Object obj = (Object) lst.get(0);
			apply.setApplyAmount(new BigDecimal(obj.toString()));
		}
		return apply;
	}

	@Override
	public void addOrEdit(GiftApply giftApply, UserObject user) {
		if(giftApply.getId() != null){
			GiftApply applyData = baseDao.get(GiftApply.class,giftApply.getId());
			BeanUtils.copyPropertiesIgnoreNull(giftApply,applyData);
			applyData.setRecordInfor(true, user);
			baseDao.update(applyData);
		}else{					   
			giftApply.setRecordInfor(false, user);
			baseDao.save(giftApply);
			//加销售团队
			teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.GIFT_APPLY_PROC,giftApply.getId());
		}
	}

	@Override
	public void delete(String id){
		GiftApply apply = baseDao.get(GiftApply.class, id);
		if(apply != null){
			List<GiftApplyDetail> detailList = baseDao.findEntity("from GiftApplyDetail d where d.applyId = ?", apply.getId());
			baseDao.deleteAll(detailList);
			baseDao.delete(apply);
		}
	}
	
	@Override
	public void submit(String id,UserObject user){
		checkSubmit(id);
		dealInventoryQuantity(id,0);
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.GIFT_APPLY_PROC, ProcessConstants.PROCESS_STATUS_02);
        processStatusService.updateProcessStatus("GiftApply", id, "status", lov.getId());
        
        String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.GIFT_APPLY_PROC);
        String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new GiftApply().getLovMember("APPLICATION", model).getName();
		}
		GiftApply apply = baseDao.get(GiftApply.class, id);
        //启动流程
		Map<String,String> varmap = new HashMap<>();
		varmap.put("ORG_TYPE", apply.getDealerCode());
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.GIFT_APPLY_PROC, flowName+"-"+apply.getApplyCode(), id, user, varmap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal getAvailableLimit(String currencyId,Date applyDate) {
		BigDecimal availableLimit = new BigDecimal(0.00);
		//计算人民币的已用额度，当汇率不匹配时，汇率默认为1
		String userdSql = "select nvl(sum(nvl(d.c_apply_quantity*g.c_price*nvl(case when m.lov_code = 'CNY' then 1 else r.conversion_rate end,0),0)),0) \"applyAmount\""
					+" from crm_t_gift_apply t"
					+" left join sys_t_lov_member m on t.c_currency = m.row_id"
					+" left join sys_t_lov_member m1 on t.c_status = m1.row_id"
					+" left join CRM_INT_MV_RATE r on m.lov_code = r.from_currency and r.to_currency = 'CNY' and t.C_APPLY_DATE = r.conversion_date" 
					+" left join crm_t_gift_apply_detail d on d.c_apply_id = t.c_pid"
					+" left join crm_t_gift_manage g on g.c_pid = d.c_gift_id"
					+" where (m1.lov_code = '02' or m1.lov_code = '03') and t.c_apply_date >= to_date((select to_char(sysdate,'yyyy')||'-01-01' from dual),'yyyy-mm-dd')";
		//计算人民币的可用额度，当汇率不匹配时，汇率默认为1
		String availableSql = "select" 
							+" nvl(sum(nvl(t.n_price*t.n_delivery_quantity*nvl(case when oh.c_currency = 'CNY' then 1 else r.conversion_rate end,0),0)),0) \"deliveryAmount\""
							+" from CRM_T_DELIVERY_LINES t"
							+" left join crm_t_order_header oh on t.c_order_code = oh.c_order_code"
							+" left join CRM_INT_MV_RATE r on oh.c_currency = r.from_currency and r.to_currency = 'CNY' and t.C_DELIVERY_DATE = r.conversion_date"
							+" where t.c_create_time >= to_date((select to_char(sysdate,'yyyy')||'-01-01' from dual),'yyyy-mm-dd')";
		String currencyCode = new GiftApply().getLovCode(currencyId);
		BigDecimal rate = new BigDecimal(1);
		if(!"CNY".equals(currencyCode)){
			String rateSql = "select r.conversion_rate from CRM_INT_MV_RATE r where r.from_currency = ? and r.to_currency = 'CNY' and r.conversion_date = ?";
			List<Object[]> rateLst = baseDao.findBySql(rateSql, new Object[]{currencyCode,applyDate});
			if(rateLst != null && rateLst.size() > 0){
				rate = (BigDecimal) ((Object) rateLst.get(0));
			}else{
				throw new AnneException("未配置该货币，请联系管理员！");
			}
		}
		
		IPage availablePage = baseDao.searchBySql4Map(availableSql, 10, 1);
		Map<String,Object> deliveryMap = (Map<String,Object>)availablePage.getList().get(0);
		Object deliveryObj = deliveryMap.get("deliveryAmount");
		Object member = CacheData.getInstance().getMember("SALEUSELIMIT", "01");
		LovMember lov = new LovMember();
		BeanUtils.copyPropertiesIgnoreNull(member, lov);
		if(deliveryObj != null){
			BigDecimal userdAmount = new BigDecimal(0);
			IPage userdPage = baseDao.searchBySql4Map(userdSql, 10, 1);
			Map<String,Object> userdMap = (Map<String,Object>)userdPage.getList().get(0);
			Object userdObj = userdMap.get("applyAmount");
			if(userdObj != null){
				userdAmount = new BigDecimal(userdObj.toString());
			}
			availableLimit = new BigDecimal(deliveryObj.toString())
					.multiply(new BigDecimal(lov.getName().toString()))
					.subtract(userdAmount)
					.divide(rate, 2, RoundingMode.HALF_UP);
		}
		return availableLimit;
	}
	

	@Override
	public void dealInventoryQuantity(String giftApplyId,int type) {
		switch(type){
			case 0:	//礼品申请提交
				String sql = "update crm_t_gift_manage m set m.c_inventory_quantity = m.c_inventory_quantity - "
							+"(select nvl(sum(nvl(d.c_apply_quantity,0)),0) from crm_t_gift_apply_detail d where d.c_gift_id = m.c_pid and d.c_apply_id = ?)";
				baseDao.executeSQL(sql, giftApplyId);
				return;
			case 1://礼品申请驳回
				String sql1 = "update crm_t_gift_manage m set m.c_inventory_quantity = m.c_inventory_quantity + "
						+"(select nvl(sum(nvl(d.c_apply_quantity,0)),0) from crm_t_gift_apply_detail d where d.c_gift_id = m.c_pid and d.c_apply_id = ?)";
				baseDao.executeSQL(sql1, giftApplyId);
				return;
			default:
				return;
		}
	}
	
	private StringBuilder getQueryApplysSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" a.c_pid \"id\",");
		sql.append(" a.c_apply_code \"applyCode\",");
		sql.append(" m.lov_name \"applyUnitName\",");
		sql.append(" c.c_custom_full_name \"custom\",");
		sql.append(" m6.lov_name \"dealerName\",");
		sql.append(" m2.lov_name \"industryTypeName\",");
		sql.append(" a.c_purpose \"purpose\",");
		sql.append(" m3.lov_name \"status\",");
		sql.append(" m4.lov_name \"currencyName\",");
		sql.append(" a.c_available_limit \"availableLimit\",");
		sql.append(" (select nvl(sum(d.c_apply_quantity*g.c_price),0) from crm_t_gift_apply_detail d,crm_t_gift_manage g where d.c_gift_id = g.c_pid and d.c_apply_id = a.c_pid) \"applyAmount\",");
		sql.append(" a.c_apply_date \"applyDate\",");
		sql.append(" a.c_estimated_demand_date \"estimatedDemandDate\",");
		sql.append(" m5.lov_name \"organizationName\",");
		sql.append(" e.name \"applierName\",");
		sql.append(" a.c_applier_phone \"applierPhone\",");
		sql.append(" a.c_explain \"explain\"");
		sql.append(" from crm_t_gift_apply a");
		sql.append(" left join SYS_T_LOV_MEMBER m on a.c_apply_unit = m.row_id");
		sql.append(" left join crm_t_custom_info c on a.c_custom = c.c_pid");
		sql.append(" left join SYS_T_LOV_MEMBER m2 on a.c_industry_type = m2.row_id");
		sql.append(" left join SYS_T_LOV_MEMBER m3 on a.c_status = m3.row_id");
		sql.append(" left join SYS_T_LOV_MEMBER m4 on a.c_currency = m4.row_id");
		sql.append(" left join SYS_T_LOV_MEMBER m5 on a.c_organization = m5.row_id");
		sql.append(" left join SYS_T_PERMISSION_EMPLOYEE e on a.c_applier = e.row_id");
		sql.append(" left join SYS_T_LOV_MEMBER m6 on a.c_dealer = m6.row_id");
		sql.append(" where 1=1");
		return sql;
	}
	
	private void checkSubmit(String id){
		StringBuilder sql = new StringBuilder("select c_gift_code \"giftCode\",restQuantity from(select m.c_gift_code,(m.c_inventory_quantity-");
		sql.append(" (select nvl(sum(nvl(d.c_apply_quantity,0)),0) from crm_t_gift_apply_detail d where d.c_gift_id = m.c_pid and d.c_apply_id = ?))");
		sql.append(" restQuantity from crm_t_gift_manage m) where restQuantity < 0");
		List<Map<String, Object>> lst = baseDao.findBySql4Map(sql.toString(), new Object[]{id});
		if(lst != null && lst.size() > 0){
			StringBuilder warn = new StringBuilder("资料或礼品编号：");
			for(Map<String, Object> map : lst){
				warn.append(map.get("giftCode").toString());
				warn.append("、");
			}
			warn.deleteCharAt(warn.length()-1);
			warn.append(",库存不足！");
			throw new AnneException(warn.toString());
		}
	}
}