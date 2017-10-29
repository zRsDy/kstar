package com.ibm.kstar.impl.order;

import java.text.SimpleDateFormat;
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
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IStatementService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.order.StatementMaster;
import com.ibm.kstar.permission.utils.PermissionUtil;
@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class StatementServiceImpl implements IStatementService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	private ITeamService teamService;
	@Autowired 
	ILovMemberService lovMemberService;
	@Autowired
	IXflowProcessServiceWrapper processService;
	
	@Override
	public void saveStatementMaster(StatementMaster statementMaster,UserObject userObject){
		// 创建字段
		statementMaster.setCreatedById(userObject.getEmployee().getId());
		statementMaster.setCreatedAt(new Date());
		statementMaster.setCreatedPosId(userObject.getPosition().getId());
		statementMaster.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		statementMaster.setUpdatedById(userObject.getEmployee().getId());
		statementMaster.setUpdatedAt(new Date());
		statementMaster.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		statementMaster.setApplyDate(new Date());
		statementMaster.setIsPublish(IConstants.NO_No);
		
		baseDao.save(statementMaster);
		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
				IConstants.PERMISSION_BUSINESS_TYPE_STATEMENT,statementMaster.getId());
	}

	@Override
	public void updateStatementMaster(StatementMaster statementMaster)
			throws AnneException {
		StatementMaster oldStatementMaster = baseDao.get(StatementMaster.class,
				statementMaster.getId());
		if (oldStatementMaster == null) {
			throw new AnneException(IStatementService.class.getName()
					+ " saveStatementMaster : 没有找到要更新的数据");
		}
		BeanUtils.copyPropertiesIgnoreNull(statementMaster, oldStatementMaster);
		baseDao.update(oldStatementMaster);
	}

	@Override
	public IPage queryStatementMasters(PageCondition condition)
			throws AnneException {
		FilterObject filterObject = condition.getFilterObject(StatementMaster.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void deleteStatementMaster(String statementMasterId)
			throws AnneException {
		baseDao.deleteById(StatementMaster.class, statementMasterId);
	}

	@Override
	public StatementMaster getStatementMaster(String statementMasterId)
			throws AnneException {
		return baseDao.get(StatementMaster.class, statementMasterId);
	}

	/**
	 * updateStatus:修改对账单状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 对账单ID
	 * @param status 状态
	 * @since JDK 1.7
	 */
	@Override
	public void updateStatus(String id,String status,UserObject userObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		StatementMaster statementMaster = baseDao.get(StatementMaster.class,id);
		if (statementMaster == null) {
			throw new AnneException(IStatementService.class.getName()
					+ " updateStatus : 没有找到要更新的数据");
		}
		
		statementMaster.setStatus(status);
		statementMaster.setUpdatedAt(new Date());
		if(userObject != null){
			statementMaster.setUpdatedById(userObject.getEmployee().getId());
		}
		baseDao.update(statementMaster);
		
		//如果是已录入状态，审核通过，启动订单提交工作流
		if(userObject != null && IConstants.ORDER_CONTROL_STATUS_20.equals(status)){
			Map<String, String> varmap = new HashMap<String, String>();
			varmap.put("title", "对账单提交审核 - " + statementMaster.getStatementCode()+"|"+userObject.getEmployee().getName()+"|"+sdf.format(new Date()));
			varmap.put("app", IConstants.ORDER_AUDIT_FLOW_APP_STATEMENT_AUDIT);
			String order_audit_app =  lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_STATEMENT_AUDIT);
			processService.start(order_audit_app, id, userObject, varmap);
		}
	}
	
	@Override
	public IPage queryStatementInvoiceMaster(PageCondition condition) throws Exception {
		
		StringBuffer hql = new StringBuffer("select distinct m from InvoiceMaster m ,InvoiceDetail d , DeliveryLines l ");
		hql.append(" where m.id = d.invoiceId and d.deliveryLineId=l.id ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String statementDateBegin = condition.getStringCondition("statementDateBegin");
		String statementDateEnd = condition.getStringCondition("statementDateEnd");
		String customerId = condition.getStringCondition("customerId");
		String isPostAccount = condition.getStringCondition("isPostAccount");
		
		List<Object> args = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(statementDateBegin)){
			hql.append(" and l.printTime > ?");
			Date date = sdf.parse(statementDateBegin+" 00:00:00");
			args.add(date);
		}
		if(StringUtil.isNotEmpty(statementDateEnd)){
			hql.append(" and l.printTime <= ?");
			Date date = sdf.parse(statementDateEnd+" 23:59:59");
			args.add(date);
		} 
		if(StringUtil.isEmpty(customerId)){
			customerId ="";
		} 
		hql.append(" and m.customerId = ?");
		args.add(customerId);
		
		if(StringUtil.isNotEmpty(isPostAccount)){
			hql.append(" and l.isPostAccount = ?");
			args.add(isPostAccount);
		} 
		String perHql= PermissionUtil.getPermissionHQL(null, "m.createdById", "m.createdPosId", "m.createdOrgId", "m.id", 
				(UserObject)condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_INVOICE);
		hql.append(" and " +perHql);
		hql.append(" order by m.invoiceCode desc , m.updatedAt desc ");
		
		return baseDao.search(hql.toString(),args.toArray(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryStatementInvoiceDetail(PageCondition condition) throws Exception {
		
		StringBuffer hql = new StringBuffer("select distinct d from InvoiceMaster m ,InvoiceDetail d , DeliveryLines l ");
		hql.append(" where m.id = d.invoiceId and d.deliveryLineId=l.id ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String statementDateBegin = condition.getStringCondition("statementDateBegin");
		String statementDateEnd = condition.getStringCondition("statementDateEnd");
		String customerId = condition.getStringCondition("customerId");
		String isPostAccount = condition.getStringCondition("isPostAccount");
		
		List<Object> args = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(statementDateBegin)){
			hql.append(" and l.printTime > ?");
			Date date = sdf.parse(statementDateBegin+" 00:00:00");
			args.add(date);
		}
		if(StringUtil.isNotEmpty(statementDateEnd)){
			hql.append(" and l.printTime <= ?");
			Date date = sdf.parse(statementDateEnd+" 23:59:59");
			args.add(date);
		} 
		if(StringUtil.isEmpty(customerId)){
			customerId ="";
		} 
		hql.append(" and m.customerId = ?");
		args.add(customerId);
		
		if(StringUtil.isNotEmpty(isPostAccount)){
			hql.append(" and l.isPostAccount = ?");
			args.add(isPostAccount);
		} 
		String perHql= PermissionUtil.getPermissionHQL(null, "m.createdById", "m.createdPosId", "m.createdOrgId", "m.id", 
				(UserObject)condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_INVOICE);
		
		hql.append(" and " +perHql);
		hql.append(" order by d.invoiceCode desc , d.updatedAt desc ");
		
		return baseDao.search(hql.toString(),args.toArray(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryStatementDeliveryLines(PageCondition condition) throws Exception {
		
		StringBuffer hql = new StringBuffer("select l from  DeliveryHeader h, DeliveryLines l ");
		hql.append(" where h.id = l.deliveryId ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String statementDateBegin = condition.getStringCondition("statementDateBegin");
		String statementDateEnd = condition.getStringCondition("statementDateEnd");
		String customerId = condition.getStringCondition("customerId");
		String isPostAccount = condition.getStringCondition("isPostAccount");
		
		List<Object> args = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(statementDateBegin)){
			hql.append(" and l.printTime > ?");
			Date date = sdf.parse(statementDateBegin+" 00:00:00");
			args.add(date);
		}
		if(StringUtil.isNotEmpty(statementDateEnd)){
			hql.append(" and l.printTime <= ?");
			Date date = sdf.parse(statementDateEnd+" 23:59:59");
			args.add(date);
		} 
		if(StringUtil.isNotEmpty(customerId)){
			customerId = "";
		} 
		hql.append(" and l.singleCustId = ?");
		args.add(customerId);
		
		if(StringUtil.isNotEmpty(isPostAccount)){
			hql.append(" and l.isPostAccount = ?");
			args.add(isPostAccount);
		} 
		String perHql= PermissionUtil.getPermissionHQL(null, "h.createdById", "h.createdPosId", "h.createdOrgId", "h.id", 
				(UserObject)condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY);
		hql.append(" and " +perHql);
		hql.append(" order by l.deliveryCode desc , l.updatedAt desc ");
		
		return baseDao.search(hql.toString(),args.toArray(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryStatementReceipts(PageCondition condition) throws Exception {
		
		StringBuffer hql = new StringBuffer("from  Receipts r where ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String statementDateBegin = condition.getStringCondition("statementDateBegin");
		String statementDateEnd = condition.getStringCondition("statementDateEnd");
		String customerId = condition.getStringCondition("customerId");
		//String isPostAccount = condition.getStringCondition("isPostAccount");
		
		List<Object> args = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(statementDateBegin)){
			hql.append("  r.receiptsDate > ?");
			Date date = sdf.parse(statementDateBegin+" 00:00:00");
			args.add(date);
		}
		if(StringUtil.isNotEmpty(statementDateEnd)){
			hql.append(" and r.receiptsDate <= ?");
			Date date = sdf.parse(statementDateEnd+" 23:59:59");
			args.add(date);
		} 
		if(StringUtil.isNotEmpty(customerId)){
			hql.append(" and ((r.erpCust = '1' and r.paymentCustomerId = ? ) or (r.erpCust = '0' and r.correctCustId = ?) )");
			args.add(customerId);
			args.add(customerId);
		} 
		
		String perHql= PermissionUtil.getPermissionHQL(null, "r.createdById", "r.createdPosId", "r.createdOrgId", "r.id", 
				(UserObject)condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_RECEIPTS);
		hql.append(" and " +perHql);
		hql.append(" order by r.receiptsCode desc , r.updatedAt desc ");
		
		return baseDao.search(hql.toString(),args.toArray(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public void publish(String id,UserObject userObject) throws Exception {
		StatementMaster sm = baseDao.get(StatementMaster.class, id);
		if (sm == null) {
			throw new AnneException(IStatementService.class.getName()
					+ " publish : 没有找到要更新的数据");
		}
		sm.setIsPublish(IConstants.YES_Yes);
		sm.setUpdatedAt(new Date());
		if(userObject != null){
			sm.setUpdatedById(userObject.getEmployee().getId());
		}
		baseDao.update(sm);
	}
	
}