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
import org.springframework.util.StringUtils;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IAccountsService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.order.AccountsDetail;
import com.ibm.kstar.entity.order.AccountsMaster;
@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class AccountsServiceImpl implements IAccountsService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	private ITeamService teamService;
	@Autowired
	IXflowProcessServiceWrapper processService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IEmployeeService employeeService;
	
	@Override
	public void saveAccounts(AccountsMaster accountsMaster,UserObject userObject)
			throws Exception {
		// 创建字段
		accountsMaster.setCreatedById(userObject.getEmployee().getId());
		accountsMaster.setCreatedAt(new Date());
		accountsMaster.setCreatedPosId(userObject.getPosition().getId());
		accountsMaster.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		accountsMaster.setUpdatedById(userObject.getEmployee().getId());
		accountsMaster.setUpdatedAt(new Date());
				
		baseDao.save(accountsMaster);
		this.saveOrUpdateAccountsDetail(accountsMaster,userObject);
		
		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
				IConstants.PERMISSION_BUSINESS_TYPE_STATEMENT,accountsMaster.getId());
	}

	@Override
	public void updateAccounts(AccountsMaster accountsMaster,UserObject userObject)
			throws Exception {
		AccountsMaster oldAccountsMaster = baseDao.get(AccountsMaster.class,
				accountsMaster.getId());
		if (oldAccountsMaster == null) {
			throw new AnneException(IAccountsService.class.getName()
					+ " saveAccountsMaster : 没有找到要更新的数据");
		}
		BeanUtils.copyPropertiesIgnoreNull(accountsMaster, oldAccountsMaster);
		oldAccountsMaster.setUpdatedAt(new Date());
		oldAccountsMaster.setUpdatedById(userObject.getEmployee().getId());
		baseDao.update(oldAccountsMaster);
		
		this.saveOrUpdateAccountsDetail(accountsMaster,userObject);
	}

	@Override
	public IPage queryAccountsMasters(PageCondition condition)
			throws AnneException {

		FilterObject filterObject = condition
				.getFilterObject(AccountsMaster.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(),
				condition.getRows(), condition.getPage());
	}

	@Override
	public void deleteAccountsMaster(String accountsMasterId)
			throws AnneException {
		baseDao.deleteById(AccountsMaster.class, accountsMasterId);
	}

	@Override
	public AccountsMaster getAccountsMaster(String accountsMasterId)
			throws AnneException {
		return baseDao.get(AccountsMaster.class, accountsMasterId);
	}

	@Override
	public IPage queryAccountsDetail(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(AccountsDetail.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(),
				condition.getRows(), condition.getPage());
	}
	
	/**
	 * 
	 * saveOrUpdateAccountsDetail:(保存/更新账期申请明细). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param orderHeader
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	public void saveOrUpdateAccountsDetail(AccountsMaster  accountsMaster,UserObject userObject) throws Exception  {
		
		List<Map<Object, Object>> detailList = accountsMaster.getDetailList();
		List<Object> list = new ArrayList<Object>();
		if(detailList != null){
			for(Map<Object, Object> map  :  detailList){
				list.add(map.get("id"));
			}
		}
		String hql = " from AccountsDetail d where d.accountsId = ? ";
	    Object[] args = {accountsMaster.getId()};
		List<AccountsDetail> details = baseDao.findEntity(hql, args);
		//行数据不在页面返回的集合中，则将其删除
		for(AccountsDetail accountsDetail : details){
			if(list == null || list.size() <= 0 || !list.contains(accountsDetail.getId())){
				baseDao.deleteById(AccountsDetail.class, accountsDetail.getId());
			}
		}
		
		if( detailList != null){
			for (Map<Object, Object> map : detailList) {
				//将Map集合转成对象
				AccountsDetail accountsDetail = (AccountsDetail) BeanUtils.convertMap(AccountsDetail.class, map);
				if(accountsDetail != null){
					accountsDetail.setAccountsId(accountsMaster.getId());
					accountsDetail.setAccountsCode(accountsMaster.getAccountsCode());
					
					AccountsDetail oldAccountsDetail;
					
					if(StringUtils.isEmpty(accountsDetail.getId())){
						oldAccountsDetail = accountsDetail;
					}else{
						oldAccountsDetail  = baseDao.get(AccountsDetail.class,accountsDetail.getId());
						if (oldAccountsDetail == null) {
							accountsDetail.setId(null);
							oldAccountsDetail = accountsDetail;
						}else{
							//将 orderLines的属性复制到 oldOrderLines
							BeanUtils.copyPropertiesIgnoreNull(accountsDetail, oldAccountsDetail);
						}
					}
					oldAccountsDetail.setUpdatedAt(new Date());
					oldAccountsDetail.setUpdatedById(userObject.getEmployee().getId());
					baseDao.saveOrUpdate(oldAccountsDetail);
				}
			}
		}
		
	}
	
	/**
	 * updateStatus:修改开票申请状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 开票申请ID
	 * @param status 状态
	 * @since JDK 1.7
	 */
	@Override
	public void updateStatus(String id , String status , UserObject userObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		AccountsMaster accountsMaster = baseDao.get(AccountsMaster.class,id);
		if (accountsMaster == null) {
			throw new AnneException(IAccountsService.class.getName()
					+ " updateStatus : 没有找到要更新的数据");
		}
		if(IConstants.ACCOUNTS_STATUS_20.equals(status) 
				&& IConstants.ORDER_CONTROL_STATUS_30.equals(accountsMaster.getControlStatus()) 
				&& userObject != null  ){
			//申请延期时，启动账期延期审核工作流
			Map<String, String> varmap = new HashMap<String, String>();
			varmap.put("title", "账期申请延期- " + accountsMaster.getAccountsCode()+"|"+userObject.getEmployee().getName()+"|"+ sdf.format(new Date()));
			varmap.put("app",IConstants.ORDER_AUDIT_FLOW_APP_ACCOUNTS_DELAY);
			
			String salesCenter = lovMemberService.getSaleCenter(accountsMaster.getCreatedOrgId());
			varmap.put("SalesCenter",salesCenter);
			String employeeType ="";
			if(userObject != null && userObject.getOrg() != null){
				employeeType = userObject.getOrg().getOptTxt3();
			}
			varmap.put("EmployeeType", employeeType);
			
			String accounts_delay_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_ACCOUNTS_DELAY);
			processService.start(accounts_delay_app, id, userObject, varmap);
			accountsMaster.setStatus(IConstants.ACCOUNTS_STATUS_20);
			accountsMaster.setControlStatus(IConstants.ORDER_CONTROL_STATUS_20);
		}
		
		accountsMaster.setUpdatedAt(new Date());
		if(userObject != null){
			accountsMaster.setUpdatedById(userObject.getEmployee().getId());
		}
		baseDao.update(accountsMaster);
	}
	
	/**
	 * updateControlStatus:修改开票申请控制状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 开票申请ID
	 * @param status 控制状态
	 * @since JDK 1.7
	 */
	@Override
	public void updateControlStatus(String id,String status,UserObject userObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		AccountsMaster accountsMaster = baseDao.get(AccountsMaster.class,id);
		if (accountsMaster == null) {
			throw new AnneException(IAccountsService.class.getName()
					+ " updateStatus : 没有找到要更新的数据");
		}
		accountsMaster.setControlStatus(status);
		accountsMaster.setUpdatedAt(new Date());
		if(userObject != null){
			accountsMaster.setUpdatedById(userObject.getEmployee().getId());
		}
		baseDao.update(accountsMaster);
		
		if(IConstants.ACCOUNTS_STATUS_10.equals(accountsMaster.getStatus())&& 
				IConstants.ORDER_CONTROL_STATUS_20.equals(status) && userObject != null){
			//如果是账期申请状态是未延期，且status是审核中-20 ，则 启动账期审核 工作流
			Map<String, String> varmap = new HashMap<String, String>();
			varmap.put("title", "账期申请提交 - " + accountsMaster.getAccountsCode()+"|"+userObject.getEmployee().getName()+"|"+ sdf.format(new Date()));
			varmap.put("app", IConstants.ORDER_AUDIT_FLOW_APP_ACCOUNTS_AUDIT);
			
			String salesCenter = lovMemberService.getSaleCenter(accountsMaster.getCreatedOrgId());
			varmap.put("SalesCenter",salesCenter);
			String employeeType ="";
			if(userObject != null && userObject.getOrg() != null){
				employeeType = userObject.getOrg().getOptTxt3();
			}
			varmap.put("EmployeeType", employeeType);
			
			String accounts_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_ACCOUNTS_AUDIT);
			processService.start(accounts_audit_app, id, userObject, varmap);
		}
	}

}