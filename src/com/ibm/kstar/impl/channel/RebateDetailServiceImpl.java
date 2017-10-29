package com.ibm.kstar.impl.channel;


import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.channel.IRebateDetailService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateDetail;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RebateDetailServiceImpl extends BaseServiceImpl implements IRebateDetailService {
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage queryRebateDetails(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = getDetalPageSql();
		this.addQueryCondition(condition, args, sql, "statusName", "m2.lov_name", "=");
		String not_settleId = condition.getStringCondition("not_settleId");
		if(StringUtil.isNotEmpty(not_settleId)){
			sql.append(" and not exists (select sd.c_pid from crm_t_settle_detail sd where sd.c_rebate_settle_id = ? and sd.c_reabate_detail_id = d.c_pid)");
			args.add(not_settleId);
		}
		this.addQueryCondition(condition, args, sql, "customId", "p.c_custom_id", "=");
		this.addQueryCondition(condition, args, sql, "policyCode", "p.c_policy_code", "like");
		this.addQueryCondition(condition, args, sql, "policyName", "p.c_policy_name", "like");
		this.addQueryCondition(condition, args, sql, "customName", "c.c_custom_full_name", "like");
		sql.append(" order by d.c_pid desc");
		IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String,Object>>) page.getList();
		for(Map<String,Object> map : list){
			if("已关闭".equals(map.get("status").toString())){
				map.put("nonReturnMoney", 0);
			}else{
				BigDecimal nonReturnMoney = new BigDecimal(map.get("checkMoney").toString()).subtract(new BigDecimal(map.get("returnedMoney").toString()));
				map.put("nonReturnMoney",nonReturnMoney);
			}
		}
		return page;
	}

	@Override
	public void checkDetail(String[] ids, UserObject user) {
		LovMember lov = new RebateDetail().getLovMember("REBATEDETAILSTATUS", "03");
		for(String id : ids){
			RebateDetail detail = baseDao.get(RebateDetail.class, id);
			detail.setStatus(lov.getId());
			detail.setRecordInfor(true, user);
			baseDao.update(detail);
		}
	}

	@Override
	public void closeDetail(String[] ids, UserObject user) {
		LovMember lov = new RebateDetail().getLovMember("REBATEDETAILSTATUS", "04");
		for(String id : ids){
			RebateDetail detail = baseDao.get(RebateDetail.class, id);
			detail.setStatus(lov.getId());
			detail.setRecordInfor(true, user);
			baseDao.update(detail);
		}
	}

	@Override
	public void publishDetail(String[] ids, UserObject user) {
		LovMember lov = new RebateDetail().getLovMember("REBATEDETAILSTATUS", "02");
		for(String id : ids){
			RebateDetail detail = baseDao.get(RebateDetail.class, id);
			detail.setStatus(lov.getId());
			detail.setRecordInfor(true, user);
			baseDao.update(detail);
		}
	}
	
	@Override
	public void updateColumnValue(String id, String column, String value, UserObject user) {
		RebateDetail detail = baseDao.get(RebateDetail.class, id);
		if("checkMoney".equals(column)){
			if(StringUtil.isNotEmpty(value)){
				detail.setCheckMoney(new BigDecimal(value));
			}
		}else if("differenceReason".equals(column)){
			if(StringUtil.isNotEmpty(value)){
				detail.setDifferenceReason(value);
			}
		}
		detail.setRecordInfor(true, user);
		baseDao.update(detail);
	}

	@Override
	public void deleteDetails(String[] ids) {
		List<RebateDetail> lst = baseDao.findEntity("from RebateDetail d where d.policyId = (select d1.policyId from RebateDetail d1 where d1.id = ?)",ids[0]);
		if(lst.size() == ids.length){
			for(String id : ids){
				baseDao.deleteById(RebateDetail.class, id);
			}
		}else{
			throw new AnneException("必须同时删除同一个返利政策的所有返利明细行！");
		}
	}

	@Override
	public IPage queryDeductionDetails(PageCondition condition) {
		String rebateDetailId = condition.getStringCondition("rebateDetailId");
		String searchKey = condition.getStringCondition("searchKey");
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = getqueryDeductionDetailsSql();
		sql.append(" and sd.c_reabate_detail_id = ?");
		args.add(rebateDetailId);
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and s.c_deduction_code like ?");
			args.add("%"+searchKey.trim()+"%");
		}
		sql.append(" order by s.c_deduction_code desc");
		return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	private StringBuilder getDetalPageSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" d.c_pid \"id\",");
		sql.append(" p.c_policy_code \"policyCode\",");
		sql.append(" p.c_policy_name \"policyName\",");
		sql.append(" c.c_custom_full_name \"customName\",");
		sql.append(" m1.lov_name \"rebateType\",");
		sql.append(" NVL(d.c_accrued_money,0) \"accruedMoney\",");
		sql.append(" NVL(d.c_check_money,0) \"checkMoney\",");
		sql.append(" d.c_difference_reason \"differenceReason\",");
		sql.append(" NVL((select sum(dl.c_settle_money) from crm_t_settle_detail dl,crm_t_rebate_settle s"
				+ " where dl.c_rebate_settle_id = s.c_pid and s.c_check_date is not null and dl.c_reabate_detail_id = d.c_pid),0) \"returnedMoney\",");
		sql.append(" m2.lov_name \"status\",");
		sql.append(" d.c_create_date \"createDate\",");
		sql.append(" e.name \"creater\",");
		sql.append(" l.C_ORDER_CODE \"salesOrder\",");
		sql.append(" b.c_pro_code \"productCode\",");
		sql.append(" l.N_DELIVERY_QUANTITY \"sendQuantity\",");
		sql.append(" l.N_DELIVERY_AMOUNT \"orderMoney\",");
		sql.append(" l.C_DELIVERY_DATE \"sendDate\"");
		sql.append(" from crm_t_rebate_detail d");
		sql.append(" left join crm_t_rebate_policy p on d.c_policy_id = p.c_pid");
		sql.append(" left join crm_t_custom_info c on p.c_custom_id = c.c_pid");
		sql.append(" left join crm_t_delivery_lines l on d.c_delivery_line_id = l.c_pid");
		sql.append(" left join CRM_T_PRODUCT_BASIC b on l.C_MATERIEL_CODE = b.c_mater_code");
		sql.append(" left join sys_t_lov_member m1 on p.c_rebate_type = m1.row_id");
		sql.append(" left join sys_t_lov_member m2 on d.c_status = m2.row_id");
		sql.append(" left join SYS_T_PERMISSION_EMPLOYEE e on d.c_creater = e.row_id");
		sql.append(" where 1=1");
		return sql;
	}

	private StringBuffer getqueryDeductionDetailsSql(){ 
		StringBuffer sql = new StringBuffer("select");
		sql.append(" s.c_deduction_code \"deductionCode\",");
		sql.append(" s.c_check_date \"deductionDate\",");
		sql.append(" sd.c_settle_money \"deductionMoney\",");
		sql.append(" im.c_invoice_code \"billApplyCode\",");
		sql.append(" im.dt_apply_date \"billApplyDate\"");
		sql.append(" from crm_t_rebate_settle s");
		sql.append(" left join crm_t_settle_detail sd on sd.c_rebate_settle_id = s.c_pid");
		sql.append(" left join crm_t_rebate_detail rd on sd.c_reabate_detail_id = rd.c_pid");
		sql.append(" left join crm_t_invoice_detail cid on cid.c_delivery_line_id = rd.c_delivery_line_id");
		sql.append(" left join crm_t_invoice_master im on cid.c_invoice_code = im.c_invoice_code");
		sql.append(" where s.c_check_date is not null");
		return sql;
	}
}