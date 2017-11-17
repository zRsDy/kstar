package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.BaseDaoImpl;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomHandoverService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomHandoverList;
import com.ibm.kstar.entity.custom.CustomHandoverProc;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.HandoverBusinessOpportunity;
import com.ibm.kstar.entity.custom.HandoverContract;
import com.ibm.kstar.entity.custom.HandoverCustomInfo;
import com.ibm.kstar.entity.custom.HandoverOrderHeader;
import com.ibm.kstar.entity.custom.HandoverQuot;
import com.ibm.kstar.entity.order.ContractReceiptDetail;
import com.ibm.kstar.entity.order.DeliveryHeader;
import com.ibm.kstar.entity.order.InvoiceMaster;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.quot.KstarQuot;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomHandoverServiceImpl implements ICustomHandoverService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	protected ITeamService teamService;
	
	@Autowired
	protected IContractService contractService;
	
	@Autowired
	ICorePermissionService permissionService;

	@Override
	public IPage queryHandover(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomHandoverList.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveHandoverInfo(CustomHandoverList customHandoverList, UserObject userObject) throws AnneException {
		// 功能字段设值
		// 创建字段
		customHandoverList.setCreatedById(userObject.getEmployee().getId());
		customHandoverList.setCreatedAt(new Date());
		customHandoverList.setCreatedPosId(userObject.getPosition().getId());
		customHandoverList.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		customHandoverList.setUpdatedById(userObject.getEmployee().getId());
		customHandoverList.setUpdatedAt(new Date());
		baseDao.save(customHandoverList);
		
		List<LovMember> mems = permissionService.getUserPositionList(customHandoverList.getHandoverFrom());
		for (LovMember lovMember : mems) {
			teamService.addPosition(
				lovMember.getId(),
				customHandoverList.getHandoverFrom(), 
				IConstants.CUSTOM_HANDOVER_PROC,
				customHandoverList.getId());
		}
		
		teamService.addPosition(
				userObject.getPosition().getId(),
				userObject.getEmployee().getId(), 
				IConstants.CUSTOM_HANDOVER_PROC,
				customHandoverList.getId());
		
//		mems = permissionService.getUserPositionList(customHandoverList.getHandoverTo());
//		
//		for (LovMember lovMember : mems) {
//			teamService.addPosition(
//				lovMember.getId(),
//				customHandoverList.getHandoverFrom(), 
//				IConstants.CUSTOM_HANDOVER_PROC,
//				customHandoverList.getId());
//		}
	}

	@Override
	public CustomHandoverList getHandoverInfo(String id) throws AnneException {
		return baseDao.get(CustomHandoverList.class, id);
	}

	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	@Autowired
	private ILovMemberService lovMemberService;
	
	@Override
	public void updateHandoverInfo(CustomHandoverList customHandoverList, String processCompleteflg) throws AnneException {
		CustomHandoverList oldCustomHandoverList = baseDao.get(CustomHandoverList.class, customHandoverList.getId());
		if(oldCustomHandoverList == null){
			throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!customHandoverList.getId().equals(oldCustomHandoverList.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customHandoverList, oldCustomHandoverList);
		baseDao.update(oldCustomHandoverList);
		
		List<HandoverCustomInfo> customShareInfos = baseDao.findEntity(" from HandoverCustomInfo where businessId = ? and checkStatus = 'COMMONYN_Y'  ", customHandoverList.getId());
		
		for (HandoverCustomInfo temp : customShareInfos) {
			temp.setBusinessStatus(processCompleteflg);
			baseDao.update(temp);
		}
		
		List<HandoverBusinessOpportunity> handoverBusinessOpportunitys = baseDao.findEntity(" from HandoverBusinessOpportunity where businessId = ? and checkStatus = 'COMMONYN_Y' ", customHandoverList.getId());
		
		for (HandoverBusinessOpportunity temp : handoverBusinessOpportunitys) {
			temp.setBusinessStatus(processCompleteflg);
			baseDao.update(temp);
		}
		
		List<HandoverQuot> handoverQuots = baseDao.findEntity(" from HandoverQuot where businessId = ? and checkStatus = 'COMMONYN_Y' ", customHandoverList.getId());
		
		for (HandoverQuot temp : handoverQuots) {
			temp.setBusinessStatus(processCompleteflg);
			baseDao.update(temp);
		}
		
		List<HandoverContract> handoverContracts = baseDao.findEntity(" from HandoverContract where businessId = ?  and checkStatus = 'COMMONYN_Y' ", customHandoverList.getId());
		
		for (HandoverContract temp : handoverContracts) {
			temp.setBusinessStatus(processCompleteflg);
			baseDao.update(temp);
		}
		
		List<HandoverOrderHeader> handoverOrderHeaders = baseDao.findEntity(" from HandoverOrderHeader where businessId = ?  and checkStatus = 'COMMONYN_Y' ", customHandoverList.getId());
		
		for (HandoverOrderHeader temp : handoverOrderHeaders) {
			temp.setBusinessStatus(processCompleteflg);
			baseDao.update(temp);
		}
		
		if (!StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_40, processCompleteflg)) {
			return;
		}
		
//		List<LovMember> mems = permissionService.getUserPositionList(customHandoverList.getHandoverFrom());
//		for (LovMember lovMember : mems) {
//			teamService.addPosition(
//				lovMember.getId(),
//				customHandoverList.getHandoverFrom(), 
//				IConstants.CUSTOM_HANDOVER_PROC,
//				customHandoverList.getId());
//		}
		
//		List<LovMember> mems = permissionService.getUserPositionList(customHandoverList.getHandoverFrom());
		
//		for (LovMember lovMember : mems) {
//			teamService.addPosition(
//				lovMember.getId(),
//				customHandoverList.getHandoverTo(), 
//				IConstants.CUSTOM_HANDOVER_PROC,
//				customHandoverList.getId());
//		}
		for (HandoverCustomInfo temp : customShareInfos) {
			teamService.deletePositionByEmployee(temp.getBaseId(), IConstants.CUSTOM_REPORT_PROC,customHandoverList.getHandoverFrom());
			teamService.addPosition(
				customHandoverList.getHandoverToPos(),
				customHandoverList.getHandoverTo(), 
				IConstants.CUSTOM_REPORT_PROC,
				temp.getBaseId());
			
		}
		
		for (HandoverBusinessOpportunity temp : handoverBusinessOpportunitys) {
			teamService.deletePositionByEmployee(temp.getBaseId(), "BusinessOpportunity",customHandoverList.getHandoverFrom());
			teamService.addPosition(
				customHandoverList.getHandoverToPos(),
				customHandoverList.getHandoverTo(), 
				"BusinessOpportunity",
				temp.getBaseId());
			
		}
		
		for (HandoverQuot temp : handoverQuots) {
			teamService.deletePositionByEmployee(temp.getBaseId(), "QUOTINF",customHandoverList.getHandoverFrom());
			teamService.addPosition(
				customHandoverList.getHandoverToPos(),
				customHandoverList.getHandoverTo(), 
				"QUOTINF",
				temp.getBaseId());
			
		}
		
		for (HandoverContract temp : handoverContracts) {
			teamService.deletePositionByEmployee(temp.getBaseId(), contractService.getContrBusinessTypeById(temp.getBaseId()),customHandoverList.getHandoverFrom());
			teamService.addPosition(
				customHandoverList.getHandoverToPos(),
				customHandoverList.getHandoverTo(), 
				contractService.getContrBusinessTypeById(temp.getBaseId()),
				temp.getBaseId());
			
		}
		
		for (HandoverOrderHeader temp : handoverOrderHeaders) {
			//teamService.deletePositionByEmployee(temp.getBaseId(), IConstants.PERMISSION_BUSINESS_TYPE_ORDER, customHandoverList.getHandoverFrom());
			teamService.addPosition(
				customHandoverList.getHandoverToPos(),
				customHandoverList.getHandoverTo(), 
				IConstants.PERMISSION_BUSINESS_TYPE_ORDER,
				temp.getBaseId());
			
			//出货申请-销售团队
			List<DeliveryHeader> deliveryHeaders = baseDao.findEntity(" from DeliveryHeader header where header.id in (select line.deliveryId from DeliveryLines line where line.orderCode = ? )", temp.getOrderCode());
			if(deliveryHeaders.size()>0) {
				for(DeliveryHeader header:deliveryHeaders) {
					teamService.deletePositionByEmployee(header.getId(), IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY, customHandoverList.getHandoverFrom());
					teamService.addPosition(
							customHandoverList.getHandoverToPos(),
							customHandoverList.getHandoverTo(), 
							IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY,
							header.getId());
					
					//回款计划-销售团队
					List<ContractReceiptDetail> contractReceiptDetails = baseDao.findEntity(" from ContractReceiptDetail detail where detail.deliveryId = ? or detail.custId = ? ", new String[] {header.getId(),temp.getCustomerId()});
					if(contractReceiptDetails.size()>0) {
						for(ContractReceiptDetail contractReceiptDetail:contractReceiptDetails) {
							teamService.deletePositionByEmployee(contractReceiptDetail.getId(), IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL, customHandoverList.getHandoverFrom());
							teamService.addPosition(
								customHandoverList.getHandoverToPos(),
								customHandoverList.getHandoverTo(), 
								IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL,
								contractReceiptDetail.getId());
						}
					}
				}
			}
			
			//开票申请-销售团队	
			List<InvoiceMaster> invoiceMasters = baseDao.findEntity(" from InvoiceMaster master where  master.id in (select detail.invoiceId from InvoiceDetail detail where detail.orderCode = ? )", temp.getOrderCode());
			if(invoiceMasters.size()>0) {
				for(InvoiceMaster invoiceMaster:invoiceMasters) {
					teamService.deletePositionByEmployee(invoiceMaster.getId(), IConstants.PERMISSION_BUSINESS_TYPE_INVOICE, customHandoverList.getHandoverFrom());
					teamService.addPosition(
						customHandoverList.getHandoverToPos(),
						customHandoverList.getHandoverTo(), 
						IConstants.PERMISSION_BUSINESS_TYPE_INVOICE,
						invoiceMaster.getId());
				}
			}
		}
		
	}
	
	
	@Override
	public void deleteHandoverInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomHandoverList where id = ? ",new Object[]{id});
	}
	
	@Override
	public CustomHandoverList getHandoverInfoBycode(String code) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(code);
		List<CustomHandoverList> customShareInfos = baseDao.findEntity(" from CustomHandoverList where handoverCode = ? ", code);
		
		return customShareInfos.get(0);
	}
	
	private boolean checkHandoverCustomInfo(String id, String con){
		long count = baseDao.findUniqueEntity("select count(*) from HandoverCustomInfo t where t.baseId = ? and t.businessStatus = 'CUSTOM_NORMAL_STATUS_40' " + con, id);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	private boolean checkHandoverBusinessOpportunity(String id, String con){
		long count = baseDao.findUniqueEntity("select count(*) from HandoverBusinessOpportunity t where t.baseId = ? and t.businessStatus = 'CUSTOM_NORMAL_STATUS_40' " + con,id);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	private boolean checkHandoverQuot(String id, String con){
		long count = baseDao.findUniqueEntity("select count(*) from HandoverQuot t where t.baseId = ? and t.businessStatus = 'CUSTOM_NORMAL_STATUS_40' " + con,id);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	private boolean checkHandoverContract(String id, String con){
		long count = baseDao.findUniqueEntity("select count(*) from HandoverContract t where t.baseId = ? and t.businessStatus = 'CUSTOM_NORMAL_STATUS_40' " + con,id);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	private boolean checkHandoverOrderHeader(String id, String con){
		long count = baseDao.findUniqueEntity("select count(*) from HandoverOrderHeader t where t.baseId = ? and t.businessStatus = 'CUSTOM_NORMAL_STATUS_40' " + con,id);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	// 交接单据
	@Override
	public long queryReceiptList(UserObject uo,String id ,String customId ,String customCode, String userId) {
		CustomHandoverList customHandoverList =  getHandoverInfo(id);
		customHandoverList.getHandoverFrom();
		List<CustomHandoverList> customHandoverLists = baseDao.findEntity(" from CustomHandoverList where handoverFrom = ? and handoverStatus = 'CUSTOM_NORMAL_STATUS_40' ", customHandoverList.getHandoverFrom());
		StringBuffer businessIds = new StringBuffer();
		businessIds.append(" and t.businessId in ('");
		for (int i = 0; i < customHandoverLists.size(); i++) {
			if (i != 0) {
				businessIds.append("','");
			}
			
			businessIds.append(customHandoverLists.get(i).getId());
		}
		businessIds.append("') ");
//		customHandoverList.setCustomId(customId);
//		customHandoverList.setUpdatedAt(new Date());
//		customHandoverList.setUpdatedById(uo.getEmployee().getId());
		
		long cnt = 0;
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" from ");
		hql.append(" CustomInfo c ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" c.id = t.businessId ");
		hql.append(" and t.businessType = 'CUSTOM_REPORT_PROC' ");
		hql.append(" and t.masterEmployeeId = ? ");
		
		args.add(userId);
		
		if(!StringUtils.isEmpty(customCode)) {
			hql.append(" and c.customCode = ? ");
			args.add(customCode);
		}
		
		List<CustomInfo> customInfos = baseDao.findEntity(hql.toString(), args.toArray());
		baseDao.executeHQL(" delete HandoverCustomInfo where businessId = ? ",new Object[]{id});
		for (CustomInfo temp : customInfos) {
//			if(checkHandoverCustomInfo(temp.getId(), businessIds.toString())) {
//				continue;
//			}
			HandoverCustomInfo entity = new HandoverCustomInfo();
			BeanUtils.copyPropertiesIgnoreNull(temp, entity);
			entity.setBusinessId(id);
			entity.setBusinessStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
			entity.setCheckStatus("COMMONYN_Y");
			entity.setBaseId(temp.getId());
			// 功能字段设值
			// 创建字段
			entity.setCreatedById(uo.getEmployee().getId());
			entity.setCreatedAt(new Date());
			entity.setCreatedPosId(uo.getPosition().getId());
			entity.setCreatedOrgId(uo.getOrg().getId());
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.save(entity);
		}
		cnt = cnt + customInfos.size();
		
		args = new ArrayList<Object>();
		hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" b ");
		hql.append(" from ");
		hql.append(" BusinessOpportunity b ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" b.id = t.businessId ");
		hql.append(" and t.businessType = 'BusinessOpportunity' ");
		hql.append(" and t.masterEmployeeId = ? ");
		args.add(userId);
		if(!StringUtils.isEmpty(customCode)) {
			hql.append(" and b.clientId = ? ");
			args.add(customId);
		}
		
		List<BusinessOpportunity> businessOpportunitys = baseDao.findEntity(hql.toString(), args.toArray());
		baseDao.executeHQL(" delete HandoverBusinessOpportunity where businessId = ? ",new Object[]{id});
		for (BusinessOpportunity temp : businessOpportunitys) {
			if(checkHandoverBusinessOpportunity(temp.getId(), businessIds.toString())) {
				continue;
			}
			
			HandoverBusinessOpportunity entity = new HandoverBusinessOpportunity();
			BeanUtils.copyPropertiesIgnoreNull(temp, entity);
			
			entity.setBusinessId(id);
			entity.setBusinessStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
			entity.setCheckStatus("COMMONYN_Y");
			entity.setBaseId(temp.getId());
			// 功能字段设值
			// 创建字段
			entity.setCreatedById(uo.getEmployee().getId());
			entity.setCreatedAt(new Date());
			entity.setCreatedPosId(uo.getPosition().getId());
			entity.setCreatedOrgId(uo.getOrg().getId());
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.save(entity);
		}
		cnt = cnt + businessOpportunitys.size();
		
		args = new ArrayList<Object>();
		hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" q ");
		hql.append(" from ");
		hql.append(" KstarQuot q ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" q.id = t.businessId ");
		hql.append(" and t.businessType = 'QUOTINF' ");
		hql.append(" and t.masterEmployeeId = ? ");
		args.add(userId);
		if(!StringUtils.isEmpty(customCode)) {
			hql.append(" and q.customerCode = ? ");
			args.add(customCode);
		}
		
		List<KstarQuot> kstarQuots = baseDao.findEntity(hql.toString(), args.toArray());
		baseDao.executeHQL(" delete HandoverQuot where businessId = ? ",new Object[]{id});
		for (KstarQuot temp : kstarQuots) {
//			if(checkHandoverQuot(temp.getId(), businessIds.toString())) {
//				continue;
//			}
			
			HandoverQuot entity = new HandoverQuot();
			BeanUtils.copyPropertiesIgnoreNull(temp, entity);
			
			entity.setBusinessId(id);
			entity.setBusinessStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
			entity.setCheckStatus("COMMONYN_Y");
			entity.setBaseId(temp.getId());
			// 功能字段设值
			// 创建字段
			entity.setCreatedById(uo.getEmployee().getId());
			entity.setCreatedAt(new Date());
			entity.setCreatedPosId(uo.getPosition().getId());
			entity.setCreatedOrgId(uo.getOrg().getId());
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.save(entity);
		}
		cnt = cnt + kstarQuots.size();
		
		args = new ArrayList<Object>();
		hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" from ");
		hql.append(" Contract c ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" c.id = t.businessId ");
		hql.append(" and (t.businessType = 'CONTR_STAND' ");
		hql.append(" or t.businessType = 'CONTR_LOAN' ");
		hql.append(" or t.businessType = 'CONTR_PI') ");
		hql.append(" and t.masterEmployeeId = ? ");
		args.add(userId);
		
		if(!StringUtils.isEmpty(customCode)) {
			args.add(customId);
			hql.append(" and c.custCode = ? ");
		}
		
		List<Contract> contracts = baseDao.findEntity(hql.toString(), args.toArray());
		baseDao.executeHQL(" delete HandoverContract where businessId = ? ",new Object[]{id});
		for (Contract temp : contracts) {
			/*if(checkHandoverContract(temp.getId(), businessIds.toString())) {
				continue;
			}*/
			HandoverContract entity = new HandoverContract();
			BeanUtils.copyPropertiesIgnoreNull(temp, entity);
			
			entity.setBusinessId(id);
			entity.setBusinessStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
			entity.setCheckStatus("COMMONYN_Y");
			entity.setBaseId(temp.getId());
			// 功能字段设值
			// 创建字段
			entity.setCreatedById(uo.getEmployee().getId());
			entity.setCreatedAt(new Date());
			entity.setCreatedPosId(uo.getPosition().getId());
			entity.setCreatedOrgId(uo.getOrg().getId());
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.save(entity);
		}
		cnt = cnt + contracts.size();
		
		args = new ArrayList<Object>();
		hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" o ");
		hql.append(" from ");
		hql.append(" OrderHeader o ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" o.id = t.businessId ");
		hql.append(" and t.businessType = 'ORDER' ");
		hql.append(" and t.masterEmployeeId = ? ");
		args.add(userId);
		if(!StringUtils.isEmpty(customCode)) {
			hql.append(" and o.customerCode = ? ");
			args.add(customCode);
		}
		
		List<OrderHeader> orderHeaders = baseDao.findEntity(hql.toString(), args.toArray());
		baseDao.executeHQL(" delete HandoverOrderHeader where businessId = ? ",new Object[]{id});
		for (OrderHeader temp : orderHeaders) {
			/*if(checkHandoverOrderHeader(temp.getId(), businessIds.toString())) {
				continue;
			}*/
			HandoverOrderHeader entity = new HandoverOrderHeader();
			BeanUtils.copyPropertiesIgnoreNull(temp, entity);
			
			entity.setBusinessId(id);
			entity.setBusinessStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
			entity.setCheckStatus("COMMONYN_Y");
			entity.setBaseId(temp.getId());
			// 功能字段设值
			// 创建字段
			entity.setCreatedById(uo.getEmployee().getId());
			entity.setCreatedAt(new Date());
			entity.setCreatedPosId(uo.getPosition().getId());
			entity.setCreatedOrgId(uo.getOrg().getId());
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.save(entity);
		}
		cnt = cnt + orderHeaders.size();
		
		return cnt;
	}
	
	
	//未结事务
	@Override
	public IPage queryHandoverProc(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomHandoverProc.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveHandoverProcInfo(CustomHandoverProc customHandoverProc)
			throws AnneException {
		baseDao.save(customHandoverProc);
	}

	@Override
	public CustomHandoverProc getHandoverProcInfo(String id) throws AnneException {
		return baseDao.get(CustomHandoverProc.class, id);
	}

	@Override
	public void updateHandoverProcInfo(CustomHandoverProc customHandoverProc)
			throws AnneException {
		CustomHandoverProc oldCustomHandoverProc = baseDao.get(CustomHandoverProc.class, customHandoverProc.getId());
		if(oldCustomHandoverProc == null){
			throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomHandoverProc.getId().equals(oldCustomHandoverProc.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customHandoverProc, oldCustomHandoverProc);
		baseDao.update(oldCustomHandoverProc);
	}

	@Override
	public void deleteHandoverProcInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomHandoverProc where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomHandoverProc> getHandoverProcInfoBycode(String code)
			throws AnneException {
		return null;
	}

	@Override
	public IPage queryHandoverBusinessOpportunity(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(HandoverBusinessOpportunity.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryHandoverContract(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(HandoverContract.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryHandoverCustomInfo(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(HandoverCustomInfo.class);
		filterObject.addOrderBy("id", "asc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryHandoverOrderHeader(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(HandoverOrderHeader.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryHandoverQuot(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(HandoverQuot.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public void updateHandoverBusinessOpportunity(UserObject uo,String[] ids, String status) throws AnneException {
		for (String id : ids) {
			HandoverBusinessOpportunity entity = baseDao.get(HandoverBusinessOpportunity.class, id);
			if(entity == null){
				throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
			}
			
			if (StringUtils.equals("COMMONYN_Y", status)) {
				if (!(StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, entity.getBusinessStatus())
						|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, entity.getBusinessStatus()))){
					throw new AnneException(" 选择的商机目前状态已变更，请重新确认！商机：" + entity.getOpportunityName());
				}
			}
			
			entity.setCheckStatus(status);
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.update(entity);
		}
	}
	
	@Override
	public void updateHandoverContract(UserObject uo,String[] ids, String status) throws AnneException {
		for (String id : ids) {
			HandoverContract entity = baseDao.get(HandoverContract.class, id);
			if(entity == null){
				throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
			}
			
			if (StringUtils.equals("COMMONYN_Y", status)) {
				if (!(StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, entity.getBusinessStatus())
						|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, entity.getBusinessStatus()))){
					throw new AnneException(" 选择的合同目前状态已变更，请重新确认！合同：" + entity.getContrName());
				}
			}
			
			entity.setCheckStatus(status);
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.update(entity);
			String hql = "select h.id from HandoverOrderHeader h ,HandoverContract c where h.sourceCode = c.contrNo and c.id = ? )";
			List<String> orderList = baseDao.findEntity(hql.toString(),id);
			String[] orderIds = new String[orderList.size()];
			updateHandoverOrderHeader(uo,orderList.toArray(orderIds),status);
			
		}
		
		
		
	}
	
	@Override
	public void updateHandoverCustomInfo(UserObject uo,String[] ids, String status) throws AnneException {
		for (String id : ids) {
			HandoverCustomInfo entity = baseDao.get(HandoverCustomInfo.class, id);
			if(entity == null){
				throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
			}
			
			if (StringUtils.equals("COMMONYN_Y", status)) {
				if (!(StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, entity.getBusinessStatus())
						|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, entity.getBusinessStatus()))){
					throw new AnneException(" 选择的客户目前状态已变更，请重新确认！客户：" + entity.getCustomFullName());
				}
			}
			
//			if (StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, entity.getBusinessStatus())
//					|| StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_40, entity.getBusinessStatus())) {
//				throw new AnneException("审批中或审批完成客户不能重新交接！，客户名称["+ entity.getCustomFullName() + "]");
//			}
			
//			entity.setBusinessStatus(status);
			entity.setCheckStatus(status);
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.update(entity);
		}
	}
	
	@Override
	public void updateHandoverOrderHeader(UserObject uo,String[] ids, String status) throws AnneException {
		for (String id : ids) {
			HandoverOrderHeader entity = baseDao.get(HandoverOrderHeader.class, id);
			if(entity == null){
				throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
			}
			
			if (StringUtils.equals("COMMONYN_Y", status)) {
				if (!(StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, entity.getBusinessStatus())
						|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, entity.getBusinessStatus()))){
					throw new AnneException(" 选择的订单目前状态已变更，请重新确认！订单：" + entity.getOrderCode());
				}
			}
			
			entity.setCheckStatus(status);
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.update(entity);
		}
	}
	
	@Override
	public void updateHandoverQuot(UserObject uo,String[] ids, String status) throws AnneException {
		for (String id : ids) {
			HandoverQuot entity = baseDao.get(HandoverQuot.class, id);
			if(entity == null){
				throw new AnneException(ICustomHandoverService.class.getName() + " 没有找到要更新的数据");
			}
			
			if (StringUtils.equals("COMMONYN_Y", status)) {
				if (!(StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, entity.getBusinessStatus())
						|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, entity.getBusinessStatus()))){
					throw new AnneException(" 选择的报价目前状态已变更，请重新确认！报价：" + entity.getQuotCode());
				}
			}
			
			entity.setCheckStatus(status);
			// 更新字段
			entity.setUpdatedById(uo.getEmployee().getId());
			entity.setUpdatedAt(new Date());
			baseDao.update(entity);
		}
	}

	@Override
	public void startHandoverProcess(String id, UserObject userObject, Map<String, String> varmap,CustomHandoverList customHandoverList, String processCompleteflg) {
		updateHandoverInfo(customHandoverList, processCompleteflg);
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_HANDOVER_PROC);
		xflowProcessServiceWrapper.start(model
				, id
				, userObject
				,varmap);
	}

	@Override
	public void startAndSaveHandoverProcess(UserObject userObject, Map<String, String> varmap,
			CustomHandoverList customHandoverList, String processCompleteflg) {
		 saveHandoverInfo(customHandoverList,userObject);
		 String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_HANDOVER_PROC);
			xflowProcessServiceWrapper.start(model
					, customHandoverList.getId()
					, userObject
					,varmap);
	}
}
