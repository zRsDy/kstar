package com.ibm.kstar.impl.channel;


import java.math.BigDecimal;
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
import org.xsnake.web.utils.DateUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.channel.IRebatePolicyService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateClause;
import com.ibm.kstar.entity.channel.RebateDetail;
import com.ibm.kstar.entity.channel.RebatePolicy;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RebatePolicyServiceImpl extends BaseServiceImpl implements IRebatePolicyService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	IProcessService processService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;

	@Override
	public IPage queryPolicys(PageCondition condition) {
		StringBuilder hql = new StringBuilder("select new com.ibm.kstar.entity.channel.RebatePolicy(p,c) from RebatePolicy p,CustomInfo c where p.customId = c.id");
		List<Object> args = new ArrayList<>();
		this.addQueryCondition(condition, args, hql, "policyCode", "p.policyCode", "like");
		this.addQueryCondition(condition, args, hql, "policyName", "p.policyName", "like");
		this.addQueryCondition(condition, args, hql, "customName", "c.customFullName", "like");
		hql.append(" order by p.id desc");
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public RebatePolicy queryPolicy(String id) {
		return baseDao.get(RebatePolicy.class, id);
	}

	@Override
	public void addOrEdit(RebatePolicy policy, UserObject user) {
		if(policy.getId() != null){
			RebatePolicy policyData = baseDao.get(RebatePolicy.class, policy.getId());
			BeanUtils.copyPropertiesIgnoreNull(policy,policyData);
			policyData.setRecordInfor(true, user);
			baseDao.update(policyData);
		}else{
			policy.setRecordInfor(false, user);
			baseDao.save(policy);
		}
	}
	
	@Override
	public void delete(String id){
		RebatePolicy policyInfo = baseDao.get(RebatePolicy.class, id);
		List<RebateClause> clauseList = baseDao.findEntity("from RebateClause c where c.policyCode = ?", policyInfo.getPolicyCode());
		baseDao.deleteAll(clauseList);
		baseDao.delete(policyInfo);
	}
	
	@Override
	public void submit(String id, UserObject user){
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.REBATE_POLICY_PROC, ProcessConstants.PROCESS_STATUS_02);
		processStatusService.updateProcessStatus("RebatePolicy", id, "status", lov.getId());
		//启动流程
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.REBATE_POLICY_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new RebatePolicy().getLovMember("APPLICATION", model).getName();
		}
		RebatePolicy policy = baseDao.get(RebatePolicy.class, id);
		Map<String,String> varmap = new HashMap<>();
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.REBATE_POLICY_PROC, flowName+"-"+policy.getPolicyCode(), id, user, varmap);
	}

	@Override
	public void compute(String id, UserObject user) {
		List<RebateDetail> detailLst = baseDao.findEntity("from RebateDetail d where d.policyId = ?", id);
		if(detailLst != null && detailLst.size() > 0){
			throw new AnneException("该返利政策已计算！");
		}
		//返利政策
		RebatePolicy policy = baseDao.get(RebatePolicy.class, id);
		Map<String,BigDecimal> rateMap = new HashMap<>();	//产品组返利比例map
		String rebateTypeCode = policy.getRebateTypeCode();	//返利类型code	01-年度返利；02-项目返利
		String referenceTypeCode = policy.getReferenceTypeCode();	//基准类型code 01-销售金额；02-回款金额；03-合同金额；04-固定值
		if("01".equals(rebateTypeCode)){
			//获取前一年销售额
			Date startDateBefore = DateUtil.getDateAfterYear(policy.getStartDate(), -1);
			Date endDateBefore = DateUtil.getDateAfterYear(policy.getEndDate(), -1);
			List<Object[]> objBeforeLst = baseDao.findBySql(getReturnRateSql().toString(), 
					new Object[]{startDateBefore,endDateBefore,policy.getCustomId(),policy.getCustomId(),policy.getId()});
			Map<String,Long> amountBeforeMap = new HashMap<>();
			if(objBeforeLst != null){
				for(Object[] obj : objBeforeLst){
					amountBeforeMap.put(obj[0].toString(), Long.parseLong(obj[2].toString()));	//前一年销售额
				}
			}
			//获取返利比例
			List<Object[]> objLst = baseDao.findBySql(getReturnRateSql().toString(), 
					new Object[]{policy.getStartDate(),policy.getEndDate(),policy.getCustomId(),policy.getCustomId(),policy.getId()});
			if(objLst != null){
				for(Object[] obj : objLst){
					BigDecimal returnBate = new BigDecimal(obj[1].toString());
					if(amountBeforeMap.get(obj[0].toString()) != null && obj[3] != null){
						if((Long.parseLong(obj[1].toString())*100)/amountBeforeMap.get(obj[0].toString()) > Long.parseLong(obj[3].toString())){
							returnBate.add(new BigDecimal(obj[4].toString()));
						}
					}
					rateMap.put(obj[0].toString(), returnBate);
				}
			}
		}
		StringBuilder sql = getComputeSql();
		sql.append(" where rpl.c_pid = ?");
		List<Object[]> lst = baseDao.findBySql(sql.toString(), id);
		LovMember lov = new RebateDetail().getLovMember("REBATEDETAILSTATUS", "01");
		double returnRate = 0.1;	//回款率
		for(Object[] objA : lst){
			if(Double.parseDouble(objA[5].toString()) > returnRate){
				RebateDetail detail = new RebateDetail();
				detail.setPolicyId(objA[0].toString());
				detail.setDeliveryLineId(objA[1].toString());
				detail.setCreater(user.getEmployee().getId());
				detail.setCreateDate(new Date());
				detail.setStatus(lov.getId());
				detail.setAccruedMoney(new BigDecimal(0));
				if("01".equals(rebateTypeCode)){	//年度返利
					BigDecimal accruedMoney = new BigDecimal(objA[2].toString())	//发货金额
							.multiply(rateMap.get(objA[3].toString()))	//返利比例
							.multiply(new BigDecimal(objA[4].toString()))	//返利系数
							.divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP);	//保留2位小数，四舍五入（比例%+系数%）
					detail.setAccruedMoney(accruedMoney);
				}else if("04".equals(referenceTypeCode)){	//项目返利  固定值
					detail.setAccruedMoney(new BigDecimal(objA[4].toString()));//返利系数
				}
				baseDao.save(detail);
			}
		}
	}

	/**
	 * 计算返利明细
	 * @return
	 */
	private StringBuilder getComputeSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" rpl.c_pid,dl.c_pid deliveryLineId,dl.N_DELIVERY_AMOUNT,rc.c_product_group");
		sql.append(" ,rc.c_rebate_retio,rc.c_least_return_rate");
		sql.append(" from crm_t_rebate_policy rpl");
		sql.append(" inner join crm_t_rebate_clause rc on rc.c_policy_id = rpl.c_pid");
		sql.append(" inner join crm_t_rebate_product rp on rc.c_product_group = rp.c_pid");
		sql.append(" inner join crm_t_rebate_product_detail rpd on rpd.c_product_group_id = rp.c_pid");
		sql.append(" inner join CRM_T_PRODUCT_LINE prl on rpd.c_product_series = prl.c_pro_series");
		sql.append(" inner join CRM_T_PRODUCT_BASIC pb on pb.c_pro_line_id = prl.c_pid");
		sql.append(" inner join crm_t_delivery_lines dl on pb.c_mater_code = dl.c_materiel_code"); 
		sql.append(" and dl.C_DELIVERY_DATE >= rpl.c_start_date and dl.C_DELIVERY_DATE <= rpl.c_end_date");
		sql.append(" inner join crm_t_delivery_header dh on dl.C_DELIVERY_CODE = dh.c_delivery_code and dh.c_rece_customer_id = rpl.c_custom_id"); 
		return sql;
	}
	
	/**
	 * 获取产品组的返利比例
	 * @return
	 */
	private StringBuilder getReturnRateSql(){
		StringBuilder sql = new StringBuilder("select");
		sql.append(" rc.c_product_group proGroup,"); 
		sql.append(" case"); 
		sql.append(" when rc.c_finish_rate2 is not null and amt.amount >= rc.c_reference_quantity*rc.c_finish_rate2 then rc.c_rebate_rate2"); 
		sql.append(" when amt.amount >= rc.c_reference_quantity*rc.c_finish_rate then rc.c_rebate_rate"); 
		sql.append(" else 0 end return_rate,"); 
		sql.append(" amt.amount,rc.c_year_no_growth,rc.c_excess_rebate"); 
		sql.append(" from crm_t_rebate_clause rc"); 
		sql.append(" inner join"); 
		sql.append(" (select rp.c_pid c_product_group,"); 
		sql.append(" (nvl(sum(nvl(dl.N_DELIVERY_AMOUNT,0)),0)+nvl(sum(nvl(im.c_import_amount,0)),0)) amount"); 
		sql.append(" from crm_t_rebate_product rp"); 
		sql.append(" left join crm_t_rebate_product_detail rpd on rpd.c_product_group_id = rp.c_pid"); 
		sql.append(" left join CRM_T_PRODUCT_LINE prl on rpd.c_product_series = prl.c_pro_series"); 
		sql.append(" left join CRM_T_PRODUCT_BASIC pb on pb.c_pro_line_id = prl.c_pid"); 
		sql.append(" left join crm_t_delivery_lines dl on pb.c_mater_code = dl.c_materiel_code"); 
		sql.append(" and dl.C_DELIVERY_DATE >= ? and dl.C_DELIVERY_DATE <= ?"); 
		sql.append(" left join crm_t_delivery_header dh on dl.C_DELIVERY_CODE = dh.c_delivery_code"); 
		sql.append(" and dh.c_rece_customer_id = ?");  
		sql.append(" left outer join ("); 
		sql.append(" select sa.c_import_unit,sad.c_product_series,sad.c_import_amount"); 
		sql.append(" from crm_t_import_sale_apply sa"); 
		sql.append(" left join crm_t_import_sale_apply_detail sad on sad.c_apply_id = sa.c_pid"); 
		sql.append(" left join sys_t_lov_member m on sa.c_status = m.row_id and m.lov_code = '03') im"); 
		sql.append(" on im.c_import_unit = ? and im.c_product_series = rpd.c_product_series");     
		sql.append(" group by rp.c_pid) amt on rc.c_product_group = amt.c_product_group"); 
		sql.append(" where rc.c_policy_id = ?"); 
		return sql;
	}
	
	
}