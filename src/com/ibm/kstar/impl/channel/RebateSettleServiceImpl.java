package com.ibm.kstar.impl.channel;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.ibm.kstar.api.channel.IRebateSettleService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateSettle;
import com.ibm.kstar.entity.channel.SettleDetail;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RebateSettleServiceImpl extends BaseServiceImpl implements IRebateSettleService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	ICustomInfoService customService;
	
	@Override
	public IPage queryRebateSettles(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = getSettlePageSql();
		this.addQueryCondition(condition, args, sql, "deductionCode", "rs.c_deduction_code", "like");
		this.addQueryCondition(condition, args, sql, "customName", "ci.c_custom_full_name", "like");
		sql.append(" order by rs.c_pid desc");
		return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public RebateSettle queryRebateSettle(String id) {
		return baseDao.get(RebateSettle.class, id);
	}

	@Override
	public void checkSettle(String id, UserObject user) {
		RebateSettle settle = baseDao.get(RebateSettle.class, id);
		LovMember lovMember = settle.getLovMember("REBATESETTLESTATUS", "02");
		settle.setStatus(lovMember.getId());
		settle.setCheckDate(new Date());
		settle.setRecordInfor(true, user);
		baseDao.update(settle);
	}

	@Override
	public void addOrEditSettle(RebateSettle settle, UserObject user) {
		if(settle.getId() != null){
			RebateSettle oldSettle = baseDao.get(RebateSettle.class, settle.getId());
			BeanUtils.copyPropertiesIgnoreNull(settle,oldSettle);
			oldSettle.setRecordInfor(true, user);
			baseDao.update(oldSettle);
		}else{
			settle.setRecordInfor(false, user);
			baseDao.save(settle);
		}
	}
	
	@Override
	public IPage queryRebateSettleDetails(PageCondition condition) {
		String rebateSettleId = condition.getStringCondition("rebateSettleId");
		String searchKey = condition.getStringCondition("searchKey");
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = getDetalPageSql();
		sql.append(" where sd.c_rebate_settle_id = ?");
		args.add(rebateSettleId);
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" where p.c_policy_code like ? or c.c_custom_full_name like ?");
			args.add("%"+searchKey.trim()+"%");
			args.add("%"+searchKey.trim()+"%");
		}
		sql.append(" order by sd.c_pid desc");
		IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String,Object>>) page.getList();
		for(int i=0; i<list.size(); i++){
			list.get(i).put("rowNumber", (i+1));
		}
		return page;
	}

	@Override
	public void selectDetails(String[] ids, String settleId) {
		for(String id : ids){
			SettleDetail detail = new SettleDetail();
			detail.setRebateSettleId(settleId);
			detail.setRebateDetailId(id);
			detail.setSettleMoney(new BigDecimal(0));
			baseDao.save(detail);
		}
	}

	@Override
	public void updateSettleMoney(String detailId, String money, UserObject user) {
		BigDecimal settleMoney = null;
		try{
			settleMoney = new BigDecimal(money);
		}catch(Exception e){
			throw new AnneException("只能输入数字！");
		}
		SettleDetail detail = baseDao.get(SettleDetail.class, detailId);
		detail.setSettleMoney(settleMoney);
		detail.setRecordInfor(true, user);
		baseDao.update(detail);
	}

	private StringBuffer getDetalPageSql(){
		StringBuffer sql = new StringBuffer("select");
		sql.append(" sd.c_pid \"id\",");
		sql.append(" p.c_policy_code \"policyCode\",");
		sql.append(" c.c_custom_full_name \"customName\",");
		sql.append(" d.c_check_money \"checkMoney\",");
		sql.append(" sd.c_settle_money \"settleMoney\",");
		sql.append(" l.C_ORDER_CODE \"salesOrder\",");
		sql.append(" b.c_pro_code \"productCode\",");
		sql.append(" l.N_DELIVERY_QUANTITY \"sendQuantity\",");
		sql.append(" l.N_DELIVERY_AMOUNT \"orderMoney\",");
		sql.append(" l.C_DELIVERY_DATE \"sendDate\"");
		sql.append(" from crm_t_settle_detail sd");
		sql.append(" left join crm_t_rebate_detail d on sd.c_reabate_detail_id = d.c_pid");
		sql.append(" left join crm_t_rebate_policy p on d.c_policy_id = p.c_pid");
		sql.append(" left join crm_t_custom_info c on p.c_custom_id = c.c_pid");
		sql.append(" left join crm_t_delivery_lines l on d.c_delivery_line_id = l.c_pid");
		sql.append(" left join CRM_T_PRODUCT_BASIC b on l.C_MATERIEL_CODE = b.c_mater_code");
		sql.append(" left join sys_t_lov_member m1 on p.c_rebate_type = m1.row_id");
		sql.append(" left join sys_t_lov_member m2 on d.c_status = m2.row_id");
		sql.append(" left join SYS_T_PERMISSION_EMPLOYEE e on d.c_creater = e.row_id");
		return sql;
	}
	
	private StringBuilder getSettlePageSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" rs.c_pid \"id\",");
		sql.append(" rs.c_deduction_code \"deductionCode\",");
		sql.append(" ci.c_pid \"customId\",");
		sql.append(" ci.c_custom_full_name \"customName\",");
		sql.append(" (select nvl(sum(nvl(sd.c_settle_money,0)),0) from crm_t_settle_detail sd where sd.c_rebate_settle_id = rs.c_pid) \"deductionMoney\",");
		sql.append(" m1.lov_name \"rebateModeName\",");
		sql.append(" m2.lov_name \"statusName\",");
		sql.append(" m3.lov_name \"currencyName\",");
		sql.append(" rs.c_create_date \"createDate\",");
		sql.append(" rs.c_check_date \"checkDate\",");
		sql.append(" m4.lov_name \"organizationName\",");
		sql.append(" rs.c_explain \"explain\"");
		sql.append(" from crm_t_rebate_settle rs");
		sql.append(" left join crm_t_custom_info ci on rs.c_custom_id = ci.c_pid");
		sql.append(" left join sys_t_lov_member m1 on rs.c_rebate_mode = m1.row_id");
		sql.append(" left join sys_t_lov_member m2 on rs.c_status = m2.row_id");
		sql.append(" left join sys_t_lov_member m3 on rs.c_currency = m3.row_id");
		sql.append(" left join sys_t_lov_member m4 on rs.c_organization = m4.row_id");
		sql.append(" where 1=1");
		return sql;
	}

	@Override
	public void deleteDetails(String[] ids) {
		for(String id : ids){
			baseDao.deleteById(SettleDetail.class, id);
		}
	}
}