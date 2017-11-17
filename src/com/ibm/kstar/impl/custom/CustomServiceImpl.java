package com.ibm.kstar.impl.custom;


import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.custom.*;
import com.ibm.kstar.entity.custom.vo.CustomErpAddress;
import com.ibm.kstar.permission.utils.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.util.*;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomServiceImpl implements ICustomInfoService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	protected ITeamService teamService;
	
	@Autowired
	protected IOrgTeamService orgTeamService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Autowired
	ProcessStatusService processStatusService;
	
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;

	@Override
	public IPage query(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomInfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveCustomInfo(CustomInfo customInfo, UserObject userObject) throws AnneException {
		if(!StringUtils.equals(IConstants.PRM_FLAG, customInfo.getPrmFlg())) {
			checkCustom(customInfo.getCustomFullName());
		}
		
		baseDao.save(customInfo);
		
		if(StringUtils.equals(IConstants.PRM_FLAG, customInfo.getPrmFlg())) {
			CustomRelaContact customRelaContact = new CustomRelaContact();
			
			customRelaContact.setCustomId(customInfo.getId());
			
			
			customRelaContact.setContactName(customInfo.getContactName());
			customRelaContact.setContactDept(customInfo.getContactDept());
			customRelaContact.setContactBusiness(customInfo.getContactBusiness());
			customRelaContact.setContactTel(customInfo.getContactTel());
			customRelaContact.setContactRole(customInfo.getContactRole());
			
			baseDao.save(customRelaContact);
		}
		
		CustomAddressInfo customAddressInfo = new CustomAddressInfo();
		customAddressInfo.setCustomId(customInfo.getId());
		
		customAddressInfo.setLayer1(customInfo.getCustomArea());
		customAddressInfo.setLayer2(customInfo.getCustomAreaSub1());
		customAddressInfo.setLayer3(customInfo.getCustomAreaSub2());
		customAddressInfo.setLayer4(customInfo.getCustomAreaSub3());
		
		customAddressInfo.setCustomAddress(customInfo.getCorpRegAddress());
		customAddressInfo.setCustomAddressType(lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId());
		customAddressInfo.setCustomAddressValid("COMMONYN_Y");//
		customAddressInfo.setCreatedById(userObject.getEmployee().getId());
		customAddressInfo.setCreatedAt(new Date());
		customAddressInfo.setCreatedPosId(userObject.getPosition().getId());
		customAddressInfo.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		customAddressInfo.setUpdatedById(userObject.getEmployee().getId());
		customAddressInfo.setUpdatedAt(new Date());
		baseDao.save(customAddressInfo);
		
		teamService.addPosition(
				userObject.getPosition().getId(),
				userObject.getEmployee().getId(), 
				IConstants.CUSTOM_REPORT_PROC,
				customInfo.getId());
		
		orgTeamService.configPrimaryOrg(customInfo.getId(), IConstants.CUSTOM_REPORT_PROC, userObject.getOrg().getId());

		// lov
		LovMember lovMember = new LovMember();
		lovMember.setCode(customInfo.getCustomCode());
		lovMember.setGroupId(IConstants.CUSTOM_ORG_TREE);
		lovMember.setName(customInfo.getCustomFullName());
		lovMember.setLeafFlag("N");

		lovMemberService.save(lovMember);
		
	}
	
	/**
	 * 检查客户名称是否有重复
	 * @param name 客户名称
	 */
	private void checkCustom(String name){
		if (!StringUtils.isEmpty(name)) {
			
			String tempName = StringUtils.replace(name, " ", "");
			tempName = StringUtils.replace(tempName, "　", "");
			
			List<CustomInfo> customInfos = baseDao.findEntity(" from CustomInfo ", new String[]{});
			
			for (CustomInfo customInfo : customInfos) {
				if(StringUtils.equals(tempName, StringUtils.replace(StringUtils.replace(customInfo.getCustomFullName(), "　", ""), " ", ""))){
					throw new AnneException("已经存在相同的客户："+name);
				}
			}
		}
	}

	@Override
	public CustomInfo getCustomInfo(String id) throws AnneException {
		return baseDao.get(CustomInfo.class, id);
	}

	
	@Override
	public void updateCustomInfo(CustomInfo customInfo) throws AnneException {
		CustomInfo oldCustomInfo = baseDao.get(CustomInfo.class, customInfo.getId());
		if(oldCustomInfo == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomInfo.getId().equals(oldCustomInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customInfo, oldCustomInfo);
		baseDao.update(oldCustomInfo);
		
		if(StringUtils.equals(IConstants.PRM_FLAG, customInfo.getPrmFlg())) {
			
			List<CustomRelaContact> customRelaContacts = baseDao.findEntity(" from CustomRelaContact where customId = ? ", new String[]{customInfo.getId()});
			if (customRelaContacts == null || customRelaContacts.size() == 0) {
				throw new AnneException(" 系统错误，联系人信息不存在，请联系管理员。");
			} 
			
			for (CustomRelaContact entity : customRelaContacts) {
				entity.setContactName(customInfo.getContactName());
				entity.setContactDept(customInfo.getContactDept());
				entity.setContactBusiness(customInfo.getContactBusiness());
				entity.setContactTel(customInfo.getContactTel());
				entity.setContactRole(customInfo.getContactRole());
				
				baseDao.update(entity);	
			}
		}
		
		
		LovMember lovMember = lovMemberService.getLovMemberByCode(IConstants.CUSTOM_ORG_TREE, customInfo.getCustomCode());
		if (lovMember == null) {
			return;
		}
		lovMember.setName(customInfo.getCustomFullName());
		lovMemberService.update(lovMember);
	}
	
	
	@Override
	public void deleteCustomInfo(String id) throws AnneException {
		CustomInfo customInfo = getCustomInfo(id);
		LovMember lovMember = lovMemberService.getLovMemberByCode(IConstants.CUSTOM_ORG_TREE, customInfo.getCustomCode());
		if (lovMember == null) {
			return;
		}
		
		lovMemberService.delete(lovMember.getId());
		
		//baseDao.executeHQL(" delete CustomInfo where id = ? ",new Object[]{id});
		baseDao.delete(customInfo);
	}
	
	@Override
	public CustomInfo getCustomInfoByCode(String code) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(code);
		List<CustomInfo> customInfos = baseDao.findEntity(" from CustomInfo where erpCode = ? ", args.toArray());
		if (customInfos == null || customInfos.size() == 0) {
			return new CustomInfo();
		} else if (customInfos.size() > 1) {
			throw new AnneException(" 系统错误，客户编码重复，请联系管理员。");
		}
		return customInfos.get(0);
	}
	
	@Override
	public CustomInfo getCustomInfoByCodeWithAuth(String code, UserObject user) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(code);
		String perHql= PermissionUtil.getPermissionHQL(null, "createdById", "createdPosId", "createdOrgId", "id", user, IConstants.CUSTOM_REPORT_PROC);
		
		List<CustomInfo> customInfos = baseDao.findEntity(" from CustomInfo where customCode = ? and " +perHql , args.toArray());
		if (customInfos == null || customInfos.size() == 0) {
			throw new AnneException(" 没有权限查看该用户。");
		} else if (customInfos.size() > 1) {
			throw new AnneException(" 系统错误，客户编码重复，请联系管理员。");
		}
		return customInfos.get(0);
	}
	
	//客户基本信息变更
	@Override
	public void saveCustomInfoChange(CustomInfoChange customInfoChange, UserObject userObject) throws AnneException {
		
		if (!StringUtils.equals(customInfoChange.getCustomFullName(), customInfoChange.getTempFullName())) {
			checkCustom(customInfoChange.getTempFullName());
		}
		
		// 功能字段设值
		// 创建字段
		customInfoChange.setCreatedById(userObject.getEmployee().getId());
		customInfoChange.setCreatedAt(new Date());
		customInfoChange.setCreatedPosId(userObject.getPosition().getId());
		customInfoChange.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		customInfoChange.setUpdatedById(userObject.getEmployee().getId());
		customInfoChange.setUpdatedAt(new Date());
				
		baseDao.save(customInfoChange);

	}
	
	@Override
	public List<CustomInfoChange> getCustomInfoChangeByCustomId(String id) throws AnneException {
		
		return baseDao.findEntity(" from CustomInfoChange where customId = ? order by createDate desc ", new String[]{id});
	}
	
	@Override
	public CustomInfoChange getCustomInfoChange(String id) throws AnneException {
		return baseDao.get(CustomInfoChange.class, id);
	}
	
	@Override
	public void updateCustomInfoChange(CustomInfoChange customInfoChange, String flg) throws AnneException {
		CustomInfoChange oldCustomInfoChange = baseDao.get(CustomInfoChange.class, customInfoChange.getId());
		if(oldCustomInfoChange == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomInfoChange.getId().equals(oldCustomInfoChange.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customInfoChange, oldCustomInfoChange);
		baseDao.update(oldCustomInfoChange);
		
		if (!StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_40, flg)) {
			return;
		}
		CustomInfo customInfo = baseDao.get(CustomInfo.class, customInfoChange.getCustomId());
		tempCopy(customInfoChange, customInfo);
		
		customInfo.setUpdatedById(customInfoChange.getCreatedById());
		customInfo.setUpdatedAt(new Date());
		updateCustomInfo(customInfo);
		
		if (StringUtils.isEmpty(customInfoChange.getAddressId())) {
			return;
		}
		
		CustomAddressInfo customAddressInfo = baseDao.get(CustomAddressInfo.class, customInfoChange.getAddressId());
		customAddressInfo.setLayer1(customInfoChange.getTempArea());
		customAddressInfo.setLayer2(customInfoChange.getTempAreaSub1());
		customAddressInfo.setLayer3(customInfoChange.getTempAreaSub2());
		customAddressInfo.setLayer4(customInfoChange.getTempAreaSub3());
		customAddressInfo.setCustomAddress(customInfoChange.getTempRegAddress());
		
		customAddressInfo.setUpdatedById(customInfoChange.getCreatedById());
		customAddressInfo.setUpdatedAt(new Date());
		
		updateAddrInfo(customAddressInfo);
	}
	
	private void tempCopy(CustomInfoChange from, CustomInfo to) {
		/** 客户全称 */
		to.setCustomFullName(from.getTempFullName());

		/** 客户别名 */
		to.setCustomAliasName(from.getTempAliasName());

		/** 公司网址 */
		to.setCustomWebAddress(from.getTempWebAddress());

		/** OEM 品牌 */
		to.setCustomOem(from.getTempOem());

		/** 所属区域 */
		to.setCustomArea(from.getTempArea());
		to.setCustomAreaSub1(from.getTempAreaSub1());
		to.setCustomAreaSub2(from.getTempAreaSub2());
		to.setCustomAreaSub3(from.getTempAreaSub3());

		/** 收入规模 */
		to.setCustomIncomeScale(from.getTempIncomeScale());

		/** 客户行业 */
		to.setCustomCategory(from.getTempCategory());
		to.setCustomCategorySub(from.getTempCategorySub());

		/** 客户概况 */
		to.setCustomProfile(from.getTempProfile());

		/** 法定代表人 */
		to.setCorpRepresentative(from.getTempRepresentative());

		/** 纳税登记号 */
		to.setCorpTrn(from.getTempTrn());

		/** 开票名称 */
		to.setCorpInvoiceName(from.getTempInvoiceName());

		/** 开票注意事项 */
		to.setCorpInvoiceComment(from.getTempInvoiceComment());

		/** 注册地址 */
		to.setCorpRegAddress(from.getTempRegAddress());
		
		/** 一般纳税人资格 */
		to.setCorpOrdinaryFlg(from.getTempOrdinaryFlg());
		/** 开票地址 */
		to.setAccountAddress(from.getTempAccountAddress());
		/** 开户行 */
		to.setAccountBank(from.getTempAccountBank());
		/** 开票电话 */
		to.setAccountTel(from.getTempAccountTel());
		/** 开户账号 */
		to.setAccountNo(from.getTempAccountNo());
	}

	//需求
	@Override
	public IPage queryRequire(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomRequireInfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveRequireInfo(CustomRequireInfo customRequireInfo)
			throws AnneException {
		baseDao.save(customRequireInfo);
		
	}

	@Override
	public CustomRequireInfo getRequireInfo(String id) throws AnneException {
		return baseDao.get(CustomRequireInfo.class, id);
	}

	@Override
	public void updateRequireInfo(CustomRequireInfo customRequireInfo)
			throws AnneException {
		CustomRequireInfo oldCustomRequireInfo = baseDao.get(CustomRequireInfo.class, customRequireInfo.getId());
		if(oldCustomRequireInfo == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomRequireInfo.getId().equals(oldCustomRequireInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customRequireInfo, oldCustomRequireInfo);
		baseDao.update(oldCustomRequireInfo);
		
	}

	@Override
	public void deleteRequireInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomRequireInfo where id = ? ",new Object[]{id});
	}

	@Override
	public List<CustomRequireInfo> getRequireInfoBycode(String code)
			throws AnneException {
		return null;
	}

	//地址
	@Override
	public IPage queryAddr(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomAddressInfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveAddrInfo(CustomAddressInfo customAddressInfo)
			throws AnneException {
		existRegAdress(customAddressInfo);
		
		baseDao.save(customAddressInfo);
		
	}
	
	private boolean existRegAdress(CustomAddressInfo customAddressInfo){
		StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" count(*) ");
		sb.append(" from ");
		sb.append(" CustomAddressInfo ");
		sb.append(" where ");
		
		if (StringUtils.isEmpty(customAddressInfo.getId())) {
			sb.append(" customId = ? ");
			String[] args = new String[]{customAddressInfo.getCustomId(), lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId()};
			sb.append(" and customAddressType = ? ");
			sb.append(" and customAddressValid = 'COMMONYN_Y' ");
			
			long count = baseDao.findUniqueEntity(sb.toString(), args);

			if(count > 0){
				throw new AnneException("已经存在有效注册地址！不能继续添加！");
			}
		} else {
			
			if (StringUtils.equals(lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId(), customAddressInfo.getCustomAddressType())) {
				
				
				sb.append(" id <> ? ");
				sb.append(" and customId = ? ");
				String[] args = new String[]{customAddressInfo.getId(), customAddressInfo.getCustomId(), lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId()};
				sb.append(" and customAddressType = ? ");
				sb.append(" and customAddressValid = 'COMMONYN_Y' ");
				
				long count = baseDao.findUniqueEntity(sb.toString(), args);

				if(count > 0){
					throw new AnneException("已经存在有效注册地址！不能继续添加！");
				}
			} else {
				sb.append(" id <> ? ");
				sb.append(" and customId = ? ");
				String[] args = new String[]{customAddressInfo.getId(), customAddressInfo.getCustomId(), lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId()};
				sb.append(" and customAddressType = ? ");
				sb.append(" and customAddressValid = 'COMMONYN_Y' ");
				
				long count = baseDao.findUniqueEntity(sb.toString(), args);

				if(count == 0){
					//throw new AnneException("注册地址不能被删除！");
				}
				
			}
			
			
		}
		
		
		return false;
	}

	@Override
	public CustomAddressInfo getAddrInfo(String id) throws AnneException {
		return baseDao.get(CustomAddressInfo.class, id);
	}

	@Override
	public void updateAddrInfo(CustomAddressInfo customAddressInfo)
			throws AnneException {
		existRegAdress(customAddressInfo);
		
		CustomAddressInfo oldCustomAddressInfo = baseDao.get(CustomAddressInfo.class, customAddressInfo.getId());
		if(oldCustomAddressInfo == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomAddressInfo.getId().equals(oldCustomAddressInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customAddressInfo, oldCustomAddressInfo);
		baseDao.update(oldCustomAddressInfo);
	}
	
	

	@Override
	public void deleteAddrInfo(String id) throws AnneException {
		CustomAddressInfo customAddressInfo = baseDao.get(CustomAddressInfo.class, id);
		
		if (StringUtils.equals(lovMemberService.getLovMemberByCode("ADDRESSTYPE", "1").getId(), customAddressInfo.getCustomAddressType())) {
			throw new AnneException("注册地址不能被删除！");
		}
		
		baseDao.executeHQL(" delete CustomAddressInfo where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomAddressInfo> getAddrInfoBycode(String code)
			throws AnneException {
		return baseDao.findEntity(" from CustomAddressInfo where customId = ? ", new String[]{code});
	}
	
	// 银行
	@Override
	public IPage queryBank(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomFinancePinfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveBankInfo(CustomFinancePinfo customFinancePinfo)
			throws AnneException {
		baseDao.save(customFinancePinfo);
		
	}

	@Override
	public CustomFinancePinfo getBankInfo(String id) throws AnneException {
		return baseDao.get(CustomFinancePinfo.class, id);
	}

	@Override
	public void updateBankInfo(CustomFinancePinfo customFinancePinfo)
			throws AnneException {
		CustomFinancePinfo oldCustomFinancePinfo = baseDao.get(CustomFinancePinfo.class, customFinancePinfo.getId());
		if(oldCustomFinancePinfo == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomFinancePinfo.getId().equals(oldCustomFinancePinfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customFinancePinfo, oldCustomFinancePinfo);
		baseDao.update(oldCustomFinancePinfo);
	}

	@Override
	public void deleteBankInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomFinancePinfo where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomFinancePinfo> getBankInfoBycode(String code)
			throws AnneException {
		return null;
	}
	
	public boolean isErpCustom(String customId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" e ");
		hql.append(" from ");
		hql.append(" CustomErpInfo e ");
		hql.append(" ,CustomAddressInfo a ");
		hql.append(" where ");
		hql.append(" e.corpLeadedAddress = a.id ");
		hql.append(" and e.customId = ? and e.corpErpStatus = 'CUSTOM_NORMAL_STATUS_40' ");
		
		List<Object> args = new ArrayList<Object>();
		args.add(customId);
		
		List<Object> list = baseDao.findEntity(hql.toString(),args.toArray());
		if(list.size() > 0){
			return true;
		}
		return false;
	}
	
	// ERP引入
	@Override
	public IPage queryErp(PageCondition condition, String[] erpStatus) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" e ");
		hql.append(" ,a ");
		hql.append(" from ");
		hql.append(" CustomErpInfo e ");
		hql.append(" ,CustomAddressInfo a ");
		hql.append(" where ");
		hql.append(" e.corpLeadedAddress = a.id ");
		hql.append(" and e.customId = ? ");
		
		List<Object> args = new ArrayList<Object>();
		args.add(condition.getStringCondition("id"));
		
		if (erpStatus.length > 0 ) {
			hql.append(" and e.corpErpStatus in ( ");
			for (int i = 0; i < erpStatus.length; i++) {
				if (i != 0) {
					hql.append(" , ");
				}
				hql.append(" ? ");
				args.add(erpStatus[i]);
			}
				
			hql.append(" )");
		}
		hql.append(" order by e.updatedAt desc ");
		
		IPage page = baseDao.search(hql.toString(),args.toArray(), condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>)page.getList();
		List<CustomErpAddress> l = BeanUtils.castList(CustomErpAddress.class,list);
		((PageImpl)page).setList(l);
		return page;
	}

	@Override
	public void saveErpInfo(CustomErpInfo customErpInfo)
			throws AnneException {
		
		if (erpCheck(customErpInfo)) {
			throw new AnneException("该业务实体已存在，请确认后添加！");
		}
		
		baseDao.save(customErpInfo);
		
		CustomInfo customInfo = getCustomInfo(customErpInfo.getCustomId());
		customInfo.setErpStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		baseDao.update(customInfo);
	}
	
	private boolean erpCheck(CustomErpInfo customErpInfo){
		List<CustomErpInfo> customErpInfos = baseDao.findEntity(" from CustomErpInfo where customId = ? and corpErpUnit = ? ", new String[]{customErpInfo.getCustomId(), customErpInfo.getCorpErpUnit()});
		
		if (customErpInfos == null || customErpInfos.size() ==0) {
			return false;
		}
		return true;
	}

	@Override
	public CustomErpAddress getErpInfo(String id) throws AnneException {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" e ");
		hql.append(" ,a ");
		hql.append(" from ");
		hql.append(" CustomErpInfo e ");
		hql.append(" ,CustomAddressInfo a ");
		hql.append(" where ");
		hql.append(" e.corpLeadedAddress = a.id ");
		hql.append(" and e.id = ? ");

		
		List<Object> args = new ArrayList<Object>();
		args.add(id);

		List<Object[]> list = baseDao.findEntity(hql.toString(), args.toArray());
		List<CustomErpAddress> l = BeanUtils.castList(CustomErpAddress.class,list);
		
		
		return l.get(0);
	}

	@Override
	public void updateErpInfo(CustomErpInfo customErpInfo, String flg)
			throws AnneException {
		Logger logger = Logger.getLogger(CustomErpInfo.class);
		
		CustomErpInfo oldCustomErpInfo = baseDao.get(CustomErpInfo.class, customErpInfo.getId());
		if(oldCustomErpInfo == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomErpInfo.getId().equals(oldCustomErpInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customErpInfo, oldCustomErpInfo);
		baseDao.update(oldCustomErpInfo);
		logger.info("-----------------------------ERP引入流程完成后当前状态："+oldCustomErpInfo.getCorpErpStatus()+"------------------------");
		
		CustomInfo customInfo = getCustomInfo(customErpInfo.getCustomId());
		customInfo.setErpStatus(flg);
		
		if(StringUtils.equals(flg, IConstants.CUSTOM_NORMAL_STATUS_40)) {
			customInfo.setCustomStatus("CUSTOMSTATUS_40");
		}
		
		updateCustomInfo(customInfo);
	}
	
	@Override
	public void updateErpInfoAfterProcess(String businessKey, String status)
			throws AnneException {
		String[] statuses = new String[]{IConstants.CUSTOM_NORMAL_STATUS_20,IConstants.CUSTOM_NORMAL_STATUS_30};
		
		List<CustomErpAddress> customErpAddress =  getErpInfoByIdStatus(businessKey, statuses);
		
		for (CustomErpAddress temp : customErpAddress) {
			CustomErpInfo entity = temp.getCustomErpInfo();
			entity.setCorpErpStatus(status);
			entity.setUpdatedAt(new Date());
			updateErpInfo(entity, status);
		}
	}

	@Override
	public void deleteErpInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomErpInfo where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomErpAddress> getErpInfoByIdStatus(String id, String[] erpStatus)
			throws AnneException {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" e ");
		hql.append(" ,a ");
		hql.append(" from ");
		hql.append(" CustomErpInfo e ");
		hql.append(" ,CustomAddressInfo a ");
		hql.append(" where ");
		hql.append(" e.corpLeadedAddress = a.id ");
		hql.append(" and e.customId = ? ");
		
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		
		if (erpStatus.length > 0 ) {
			hql.append(" and e.corpErpStatus in ( ");
			for (int i = 0; i < erpStatus.length; i++) {
				if (i != 0) {
					hql.append(" , ");
				}
				hql.append(" ? ");
				args.add(erpStatus[i]);
			}
				
			hql.append(" )");
		}
		
		List<Object[]> list = baseDao.findEntity(hql.toString(), args.toArray());
		List<CustomErpAddress> l = BeanUtils.castList(CustomErpAddress.class,list);
		return l;
	}
	
	// 公司关系
	@Override
	public IPage queryCompany(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(CustomRelation.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveCompanyInfo(CustomRelation customRelation)
			throws AnneException {
		if (existSameCustomCodes(customRelation.getCustomCode(), customRelation.getCustomCompCode())) {
			throw new AnneException( " 公司关系已存在，请确认!");
		}
		String lovCode = "";
		String parentCode = "";
		String lvl = customRelation.getRelationLvl();
		
		if (StringUtils.equals(IConstants.LEV_UP, lvl)) {
			lovCode = customRelation.getCustomCode();
			if (checkLevUp(lovCode)){
				throw new AnneException( lovCode + " 该公司已经存在上级公司，已经不能继续添加上级公司，请确认!");
			}
			
			parentCode = customRelation.getCustomCompCode();
			treeRefrsh(parentCode, lovCode);
		} else  if (StringUtils.equals(IConstants.LEV_DOWN, lvl)) {
			lovCode = customRelation.getCustomCompCode();
			if (checkLevUp(lovCode)){
				throw new AnneException( lovCode + " 该公司已经存在上级公司，已经不能继续添加上级公司，请确认!");
			}
			
			
			parentCode = customRelation.getCustomCode();
			treeRefrsh(parentCode, lovCode);
		}
		
		baseDao.save(customRelation);
	}
	
	/**
	 * 存在性check
	 * @param customCode 
	 */
	private boolean existSameCustomCodes(String customCode, String customCompCode) {
		Long count = baseDao.findUniqueEntity(" select count(*) from CustomRelation r where r.customCode = ? and r.customCompCode = ? ",new String[]{customCode, customCompCode});
		if(count > 0){
			return true;
		}
		
		count = baseDao.findUniqueEntity(" select count(*) from CustomRelation r where r.customCompCode = ? and r.customCode = ? ",new String[]{customCode, customCompCode});
		if(count > 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 上级存在性check
	 * @param customCode 
	 */
	private boolean checkLevUp(String customCode) {
		Long count = baseDao.findUniqueEntity(" select count(*) from CustomRelation r where r.customCode = ? and r.customRelationLvl = 'levup' ",customCode);
		if(count > 0){
			return true;
		}
		
		count = baseDao.findUniqueEntity(" select count(*) from CustomRelation r where r.customCompCode = ? and r.customRelationLvl = 'levdown' ",customCode);
		if(count > 0){
			return true;
		}
		
		return false;
	}

	
	@Override
	public CustomRelation getCompanyInfo(String id) throws AnneException {
		return baseDao.get(CustomRelation.class, id);
	}

	@Override
	public void updateCompanyInfo(CustomRelation customRelation)
			throws AnneException {
		CustomRelation oldCustomRelation = baseDao.get(CustomRelation.class, customRelation.getId());
		if(oldCustomRelation == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomRelation.getId().equals(oldCustomRelation.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customRelation, oldCustomRelation);
		
		baseDao.update(oldCustomRelation);
		
	}
	
	/**
	 * 子节点存在性check
	 * @param lovMemberId 
	 */
	private boolean hasChild(String lovMemberId) throws AnneException {
		Long count = baseDao.findUniqueEntity(" select count(*) from LovMember m where m.parentId = ? and m.deleteFlag = 'N' ",lovMemberId);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 客户维度树重新构筑
	 * @param parentCode 父节点Code
	 * @param subCode 子节点Code
	 */
	private void treeReBuild(String parentCode, String subCode, String lev){
		LovMember lovParent = lovMemberService.getLovMemberByCode(IConstants.CUSTOM_ORG_TREE, parentCode);
		LovMember lovSub = lovMemberService.getLovMemberByCode(IConstants.CUSTOM_ORG_TREE, subCode);
		
		if(lovSub != null){
			lovSub.setParentId("ROOT");
			if (!hasChild(lovSub.getId())) {
				lovSub.setOptTxt1("");
			}
			lovMemberService.update(lovSub);
		}
		
		if(lovParent != null){
			if (!hasChild(lovParent.getId()) && StringUtils.equals("ROOT", lovParent.getParentId())) {
				lovParent.setOptTxt1("");
				lovMemberService.update(lovParent);
			}
		}
		
			
	}
	
	/**
	 * 客户维度树重新构筑
	 * @param parentCode 父节点Code
	 * @param subCode 子节点Code
	 */
	private void treeRefrsh(String parentCode, String subCode){
		LovMember lovParent = lovMemberService.getLovMemberByCode(IConstants.CUSTOM_ORG_TREE, parentCode);
		LovMember lovSub = lovMemberService.getLovMemberByCode(IConstants.CUSTOM_ORG_TREE, subCode);
		
		if(lovParent != null){
			if (StringUtils.isEmpty(lovParent.getOptTxt1())) {
				lovParent.setOptTxt1(IConstants.CUSTOM_ORG_TREE);
				lovMemberService.update(lovParent);
			}
		}
		
		if(lovParent != null && lovSub != null){
			lovSub.setParentId(lovParent.getId());
			lovSub.setOptTxt1(IConstants.CUSTOM_ORG_TREE);
			lovMemberService.update(lovSub);
		}
		
		
		
		
		
		
		
		
		
		
//		String finalSubPath = lovParent.getId().concat("/");
//		
//		int posP = lovParent.getPath().indexOf(parentCode);
//		int posS = lovSub.getPath().indexOf(subCode);
//		
//		String tempParentId = "ROOT";
//		String subPath = "";
//		if (posP > 0) {
//			subPath = lovParent.getPath().substring(0, posP);
//		}
//		subPath = subPath + finalSubPath;
//		String[] livePath = new String[]{};
//		if (posS > 0) {
//			subPath = subPath + lovSub.getPath().substring(posS);
//			livePath = lovSub.getPath().substring(posS).split("/");
//		} else {
//			subPath = subPath + lovSub.getPath();
//		}
//		
//		for (String temp : livePath) {
//			if (StringUtils.isEmpty(temp)) {
//				continue;
//			}
//			
//			LovMember lovMember = lovMemberService.get( temp);
//			lovMember.setParentId(tempParentId);
//			lovMemberService.update(lovMember);
//		}
//		
//		livePath = subPath.split("/");
//		
//		for (String temp : livePath) {
//			if (StringUtils.isEmpty(temp)) {
//				continue;
//			}
//			LovMember lovMember = lovMemberService.get( temp);
//			lovMember.setParentId(tempParentId);
//			lovMember.setOptTxt1(optTxt1);
//			lovMemberService.update(lovMember);
//			tempParentId = temp;
//		}
		
	}
	
	
	private boolean hasReport(String customCode, String customCompCode) throws AnneException {
		Long count = baseDao.findUniqueEntity(" select count(*) from CustomCompetitorAchi c where (c.customCode = ? and  competitorCode = ?) or (customCode = ? and competitorCode = ?) ",new Object[]{customCode, customCompCode, customCompCode, customCode});
		if(count > 0){
			return true;
		}
		return false;
	}

	@Override
	public void deleteCompanyInfo(String id) throws AnneException {
		CustomRelation customRelation = baseDao.get(CustomRelation.class, id);
		
		String lovCode = "";
		String parentCode = "";
		String lvl = customRelation.getRelationLvl();

		if (StringUtils.equals(IConstants.LEV_DOWN, lvl)) {
			lovCode = customRelation.getCustomCompCode();
			parentCode = customRelation.getCustomCode();
			treeReBuild(parentCode, lovCode, IConstants.LEV_DOWN);
		} else if (StringUtils.equals(IConstants.LEV_UP, lvl)) {
			lovCode = customRelation.getCustomCode();
			parentCode = customRelation.getCustomCompCode();
			treeReBuild(parentCode, lovCode, IConstants.LEV_UP);
		} else if (StringUtils.equals(IConstants.LEV_COMPITER, lvl)) {
			if (hasReport(customRelation.getCustomCode(), customRelation.getCustomCompCode())) {
				throw new AnneException("已存在的互为竞争对手的提报，该数据不能被删除!");
			}
		}
		
		
		baseDao.executeHQL(" delete CustomRelation where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomRelation> getCompanyInfoBycode(String code)
			throws AnneException {
		return null;
	}
	
	// 公司关系-联系人
	@Override
	public IPage queryContact(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(CustomRelaContact.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveContactInfo(CustomRelaContact customRelaContact, UserObject userObject)
			throws AnneException {
		
		CustomInfo customInfo = getCustomInfo(customRelaContact.getCustomId());
		LovMember lov = lovMemberService.getLovMemberByCode(IConstants.ADDRESSREGION,IConstants.ADDRESSREGION_CN);
		if (!StringUtils.equals(customInfo.getCustomArea(), lov.getId()) && StringUtils.isEmpty(customRelaContact.getContactMail())) {
			throw new AnneException("海外客户E-mail不能为空!");
		}
		
		baseDao.save(customRelaContact);
		
		teamService.addPosition(
				userObject.getPosition().getId(),
				userObject.getEmployee().getId(), 
				IConstants.CUSTOM_RELA_CONTACT,
				customRelaContact.getId());
	}

	@Override
	public CustomRelaContact getContactInfo(String id) throws AnneException {
		return baseDao.get(CustomRelaContact.class, id);
	}

	@Override
	public void updateContactInfo(CustomRelaContact customRelaContact)
			throws AnneException {
		CustomRelaContact oldCustomRelaContact = baseDao.get(CustomRelaContact.class, customRelaContact.getId());
		if(oldCustomRelaContact == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomRelaContact.getId().equals(oldCustomRelaContact.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customRelaContact, oldCustomRelaContact);
		baseDao.update(oldCustomRelaContact);
	}

	@Override
	public void deleteContactInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomRelaContact where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomRelaContact> getContactInfoBycode(String code)
			throws AnneException {
		return baseDao.findEntity(" from CustomRelaContact where customId = ? ", new String[]{code});
	}
	
	// 经销范围
	@Override
	public IPage queryScope(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(CustomDistrScope.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveScopeInfo(CustomDistrScope customDistrScope)
			throws AnneException {
		baseDao.save(customDistrScope);
		
	}

	@Override
	public CustomDistrScope getScopeInfo(String id) throws AnneException {
		return baseDao.get(CustomDistrScope.class, id);
	}

	@Override
	public void updateScopeInfo(CustomDistrScope customDistrScope)
			throws AnneException {
		CustomDistrScope oldCustomDistrScope = baseDao.get(CustomDistrScope.class, customDistrScope.getId());
		if(oldCustomDistrScope == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomDistrScope.getId().equals(oldCustomDistrScope.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customDistrScope, oldCustomDistrScope);
		baseDao.update(oldCustomDistrScope);
	}

	@Override
	public void deleteScopeInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomDistrScope where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<CustomDistrScope> getScopeInfoBycode(String code)
			throws AnneException {
		return null;
	}
	
	@Override
	public CustomErpInfo getCustomErpInfoByCustomId(String customId) {
		if(StringUtil.isEmpty(customId)){
			return null;
		}
		String hql = "select erp from CustomErpInfo erp where erp.customId = ? and erp.corpErpStatus = ? ";
		
		List<CustomErpInfo> customErpInfos = baseDao.findEntity(hql,new Object[]{customId,IConstants.CUSTOM_NORMAL_STATUS_40});
		if(customErpInfos == null ||  customErpInfos.size() <= 0 ){
			return null;
		}
		return customErpInfos.get(0) ;
	}
	
	@Override
	public CustomAddressInfo getAddrInfo1ByCustomId(String customId){
		if(StringUtil.isEmpty(customId)){
			return null;
		}
		String hql = "select addr from CustomAddressInfo addr , LovMember lov "
				+ " where addr.customAddressType = lov.id "
				+ " and addr.customAddressValid = ? "
				+ " and addr.customId = ? "
				+ " and lov.code = ? ";
		LovMember lovMember = lovMemberService.getLovMemberByCode("COMMONYN", "Y");
		String yn = lovMember == null? "":lovMember.getId();
		List<CustomAddressInfo> customAddressInfos = baseDao.findEntity(hql,new Object[]{yn,customId,"1"});
		if(customAddressInfos == null ||  customAddressInfos.size() <= 0 ){
			return null;
		}
		return customAddressInfos.get(0);
	}

	@Override
	public List<CustomAddressInfo> getCustomAddressInfoListById(String id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" e ");
		hql.append(" ,a ");
		hql.append(" from ");
		hql.append(" CustomErpInfo e ");
		hql.append(" ,CustomAddressInfo a ");
		hql.append(" where ");
		hql.append(" e.corpLeadedAddress = a.id ");
		hql.append(" and a.id = ? ");
		return baseDao.findEntity(hql.toString(),new String[]{id});
	}

	@Override
	public void startChangeProcess(String module, String businessKey, UserObject userObject,
			Map<String, String> varmap) {
		String title = varmap.get("title");
		String application = varmap.get("app");
		Map<String, String> vmap = new HashMap<>();
		String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
		vmap.put("SalesCenter", salesCenter);
		xflowProcessServiceWrapper.start(module, application, title, businessKey, userObject, varmap);	
		processStatusService.updateProcessStatus("CustomInfoChange", businessKey, "status", IConstants.CUSTOM_NORMAL_STATUS_20);
		
	}

	@Override
	public List<CustomAddressInfo> getCustomAddressInfoBycustomId(String customId) {
		return baseDao.findEntity(" select c from CustomAddressInfo c where c.customId = ? ",new Object[]{customId});
	}

	@Override
	public List<CustomRelaContact> getCustomRelaContactListAuth(String userType) {
		String hql = " select c from CustomRelaContact c where 1 = 1 ";
		if(StringUtil.isNotEmpty(userType)){
			hql+=" and c.customId = ?";
		}
		return baseDao.findEntity(hql,new String[]{userType});
	}

}
