package com.ibm.kstar.impl.common.customlov;


import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.common.customlov.ICustomLovInfoService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.common.customlov.Custom;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.entity.custom.CustomRelation;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.permission.utils.PermissionUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomLovInfoServiceImpl implements ICustomLovInfoService{
	@Autowired
	BaseDao baseDao;
	@Autowired
	ICustomInfoService customService;

	@Override
	public List<BusinessOpportunity> queryBizOppByUser(PageCondition condition) throws AnneException {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" b ");
		hql.append(" from ");
		hql.append(" BusinessOpportunity b ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" b.id = t.businessId ");
		hql.append(" and t.businessType = 'BusinessOpportunity' ");
		hql.append(" and (b.number like ? ");
		hql.append(" or b.opportunityName like ?) ");
		
		List<Object> args = new ArrayList<Object>();
		args.add("%"+condition.getStringCondition("search")+"%");
		args.add("%"+condition.getStringCondition("search")+"%");
		
		if (!StringUtils.isEmpty(condition.getStringCondition("clientId"))) {
			hql.append(" and b.clientId = ? ");
			args.add(condition.getStringCondition("clientId"));
		}
		
		if (!StringUtils.isEmpty(condition.getStringCondition("userId"))) {
			hql.append(" and b.createdById = ? ");
			args.add(condition.getStringCondition("userId"));
		}
		
		return baseDao.findEntity(hql.toString(), args.toArray());
	}
	
	@Override
	public List<Contract> queryContractByUser(PageCondition condition) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" from ");
		hql.append(" Contract c ");
		hql.append(" ,Team t ");
		hql.append(" where ");
		hql.append(" c.id = t.businessId ");
		hql.append(" and (t.businessType = 'CONTR_STAND' ");
		hql.append(" or t.businessType = 'CONTR_PI' ");
		hql.append(" or t.businessType = 'CONTR_LOAN') ");
		hql.append(" and (c.contrNo like ? ");
		hql.append(" or c.contrName like ?) ");
		
		List<Object> args = new ArrayList<Object>();
		args.add("%"+condition.getStringCondition("search")+"%");
		args.add("%"+condition.getStringCondition("search")+"%");
		
		if (!StringUtils.isEmpty(condition.getStringCondition("userId"))) {
			hql.append(" and c.createdById = ? ");
			args.add(condition.getStringCondition("userId"));
		}
		
		return baseDao.findEntity(hql.toString(), args.toArray());
	}
	
	@Override
	public List<CustomRelation> getCompanyList(PageCondition condition) throws AnneException {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" from ");
		hql.append(" CustomRelation c ");
		hql.append(" ,CustomInfo i ");
		hql.append(" where ");
		hql.append(" c.customCode = ? ");
		hql.append(" and c.customCompCode = i.customCode ");
		hql.append(" and c.customRelationLvl = 'levcompiter' ");
		hql.append(" and (i.customFullName like ? ");
		hql.append("     or i.customCode like ? ");
		hql.append("     or i.customAliasName like ?) ");

		List<Object> args = new ArrayList<Object>();
		args.add(condition.getStringCondition("customCode"));
		args.add("%"+condition.getStringCondition("search")+"%");
		args.add("%"+condition.getStringCondition("search")+"%");
		args.add("%"+condition.getStringCondition("search")+"%");


		List<CustomRelation> list = baseDao.findEntity(hql.toString(), args.toArray());
		
		hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" from ");
		hql.append(" CustomRelation c ");
		hql.append(" ,CustomInfo i ");
		hql.append(" where ");
		hql.append(" c.customCompCode = ? ");
		hql.append(" and c.customCode = i.customCode ");
		
		hql.append(" and c.customRelationLvl = 'levcompiter' ");
		hql.append(" and (i.customFullName like ? ");
		hql.append("     or i.customCode like ? ");
		hql.append("     or i.customAliasName like ?) ");
		List<CustomRelation> listComp = baseDao.findEntity(hql.toString(), args.toArray());
		
		for (CustomRelation customRelation : listComp) {
			list.add(customRelation);
		}
		
		return list;
	}
	
	@Override
	public List<Custom> getCustomInfoList(PageCondition condition) throws AnneException {
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" ,t ");
		hql.append(" ,o ");
		hql.append(" from ");
		hql.append(" CustomInfo c ");
		hql.append(" ,Team t ");
		hql.append(" ,Orgs o ");
		
		hql.append(" where ");
		
		hql.append(" c.id = t.businessId ");
		hql.append(" and t.businessType = 'CUSTOM_REPORT_PROC' ");
		
		hql.append(" and c.id = o.businessId ");
		hql.append(" and o.businessType = 'CUSTOM_REPORT_PROC' ");
		hql.append(" and o.primaryOrgFlag = 'Y' ");
		

		String search = condition.getStringCondition("search");
		String customType = condition.getStringCondition("customType");
		List<Object> args = new ArrayList<Object>();
		if (!StringUtils.isEmpty(search)) {
			hql.append(" and (c.customCode like ? or c.customFullName like ? or c.customAliasName like ?)");
			args.add("%"+search+"%");
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if (!StringUtils.isEmpty(customType)) {
			hql.append(" and c.customType = ? ");
			args.add(customType);
		}
		
		hql.append(" order by c.updatedAt desc ");
		
		List<Object[]> list = baseDao.findEntity(hql.toString(), args.toArray());
		return BeanUtils.castList(Custom.class,list);		
	}
	
	@Override
	public List<CustomInfo> getCustomInfoListWithCondition(PageCondition condition, UserObject user) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from ");
		hql.append(" CustomInfo ");
		hql.append(" where ");
		hql.append(" 1 = 1 ");
		String search = condition.getStringCondition("search");
		if (StringUtil.isNotEmpty(search)) {
			hql.append(" and ( ");
			hql.append("     customFullName like ? ");
			hql.append("     or customAliasName like ? ");
			hql.append("     or customCode like ? ");
			hql.append("     or erpCode like ? ");
			hql.append(" ) ");
			
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		String perHql= "";
		perHql= PermissionUtil.getPermissionHQL(null, "createdById", "createdPosId", "createdOrgId", "id", user, IConstants.CUSTOM_REPORT_PROC);
		perHql = " and " + perHql;
		
		hql.append(perHql);
		
		String customType = condition.getStringCondition("customType");
		if (StringUtil.isNotEmpty(customType)) {
			hql.append(" customType = ? ");
			args.add(customType);
		}
		
		hql.append(" and rownum <= 20 ");
		
		hql.append(" order by updatedAt desc ");
		
		return baseDao.findEntity(hql.toString() , args.toArray());
		
	}
	
	@Override
	public List<CustomInfo> getCustomInfoListWithCondition(PageCondition condition) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from ");
		hql.append(" CustomInfo ");
		hql.append(" where ");
		hql.append(" 1 = 1 ");
		String search = condition.getStringCondition("search");
		if (StringUtil.isNotEmpty(search)) {
			hql.append(" and ( ");
			hql.append("     customFullName like ? ");
			hql.append("     or customAliasName like ? ");
			hql.append("     or customCode like ? ");
			hql.append(" ) ");
			
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		
		String customType = condition.getStringCondition("customType");
		if (StringUtil.isNotEmpty(customType)) {
			hql.append(" customType = ? ");
			args.add(customType);
		}
		
		hql.append(" and rownum <= 50 ");
		
		hql.append(" order by updatedAt desc ");
		
		return baseDao.findEntity(hql.toString() , args.toArray());
		
	}
	
	@Override
	public List<CustomInfo> getCustomInfoListWithConditionAndPos(PageCondition condition,UserObject userObject) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from ");
		hql.append(" CustomInfo c ");
		hql.append(" where ");
		hql.append(" 1 = 1 ");
		hql.append(" and c.id in ( ");
		hql.append(" select b.businessId from LovMember a,Team b where ");
		hql.append(" a.id = b.positionId ");
		hql.append(" and a.groupCode = 'POSITION' ");
		if(userObject != null){
			hql.append(" and a.id = ?  " );
			args.add(userObject.getPosition().getId());
		}
		hql.append(" ) " );
		//hql.append(" and b.businessType ='CUSTOM_REPORT_PROC' ) ");
		
		String search = condition.getStringCondition("search");
		if (StringUtil.isNotEmpty(search)) {
			hql.append(" and ( ");
			hql.append("     c.customFullName like ? ");
			hql.append("     or c.customAliasName like ? ");
			hql.append("     or c.customCode like ? ");
			hql.append(" ) ");
			
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		
		String customType = condition.getStringCondition("customType");
		if (StringUtil.isNotEmpty(customType)) {
			hql.append(" c.customType = ? ");
			args.add(customType);
		}
		
		hql.append(" order by c.updatedAt desc ");
		
		return baseDao.findEntity(hql.toString() , args.toArray());
		
	}
	
	@Override
	public List<CustomInfo> getCustomInfoListWithConditionBySalerCenter(PageCondition condition,LovMember lov) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from ");
		hql.append(" CustomInfo c ");
		hql.append(" where ");
		hql.append(" 1 = 1 ");
		hql.append(" and c.id in ( ");
		hql.append(" select b.businessId from LovMember a,Team b where ");
		hql.append(" a.id = b.positionId ");
		hql.append(" and a.groupCode = 'POSITION' ");
		if(lov != null){
			hql.append(" and a.optTxt1 in (  " );
			hql.append("  select o.id from LovMember o where o.path like ? ");
			hql.append(" ) " );
			args.add("%" +lov.getId()+ "%");
		}
		hql.append(" ) " );
		//hql.append(" and b.businessType ='CUSTOM_REPORT_PROC' ) ");
		
		String search = condition.getStringCondition("search");
		if (StringUtil.isNotEmpty(search)) {
			hql.append(" and ( ");
			hql.append("     c.customFullName like ? ");
			hql.append("     or c.customAliasName like ? ");
			hql.append("     or c.customCode like ? ");
			hql.append(" ) ");
			
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		
		String customType = condition.getStringCondition("customType");
		if (StringUtil.isNotEmpty(customType)) {
			hql.append(" c.customType = ? ");
			args.add(customType);
		}
		
		hql.append(" order by c.updatedAt desc ");
		
		return baseDao.findEntity(hql.toString() , args.toArray());
		
	}

	@Override
	public List<Custom> getCustomInfoListNoAuth(PageCondition condition) {
		String search = "%" + condition.getStringCondition("search") + "%";
		return baseDao.findEntity("from CustomInfo where customFullName like ? or customAliasName like ? or customCode like ? order by customFullName asc",
				new Object[]{search,search,search},0,20);
	}

	@Override
	public List<CustomInfo> getCustomInfoListAuth(PageCondition condition) throws AnneException {


		String search = condition.getStringCondition("search");
		if (StringUtil.isNotEmpty(search)) {
			condition.getFilterObject().addOrCondition("customFullName", "like", "%" + search + "%");
			condition.getFilterObject().addOrCondition("customAliasName", "like", "%" + search + "%");
			condition.getFilterObject().addOrCondition("customCode", "like", "%" + search + "%");
		}
		String customType = condition.getStringCondition("customType");
		if (StringUtil.isNotEmpty(customType)) {
			condition.getFilterObject().addCondition("customType", "=", search);
		}
		String belongIndustrySub = condition.getStringCondition("belongIndustrySub");
		if(StringUtil.isNotEmpty(belongIndustrySub)){
			condition.getFilterObject().addCondition("customCategorySub", "=", belongIndustrySub);
		}
		FilterObject filterObject = condition.getFilterObject(CustomInfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);

		return baseDao.findEntity(hqlObject.getHql(), hqlObject.getArgs());
	}
	
	@Override
	public List<CustomRelaContact> getCustomRelaContactList(Condition condition) throws AnneException{
		
		FilterObject filterObject = condition.getFilterObject(CustomRelaContact.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}
	
	@Override
	public List<CustomAddressInfo> getCustomAddressInfoList(Condition condition) throws AnneException{
		String search = condition.getStringCondition("search");
		if (StringUtil.isNotEmpty(search)) {
			condition.getFilterObject().addOrCondition("customAddress", "like", "%" + search + "%");
		}
		FilterObject filterObject = condition.getFilterObject(CustomAddressInfo.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}
	
	@Override
	public List<LovMember> getOrgList(Condition condition)
			throws AnneException{
		String search = condition.getStringCondition("search");
		
		String hql = " select o from LovMember o  where o.groupId='ORG' and o.leafFlag='N' and o.deleteFlag = 'N' and o.name like ? ";
		return baseDao.findEntity(hql,new Object[]{"%" + search + "%"});
	}

	@Override
	public List<ProductPriceHead> quotedPriceCustom(PageCondition condition,UserObject user) throws AnneException {
		String search = condition.getStringCondition("search");
		List<Object> args = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select  p from ProductPriceHead p,LovMember m"
				+ " where p.organization = m.id and p.clientPrice = 1 "
				+" 	and (select c.customFullName from CustomInfo c where c.id = p.clientId) like ? and");
		args.add("%"+search+"%");
		LovMember lov = user.getOrg();
		ArrayList<String> parentId = new ArrayList();
		parentId.add(lov.getId());
		if(lov.getParentId()!=null) {
			getParentId(lov, parentId);
		}
		Object[] ids = parentId.toArray();
		StringBuilder sbId = new StringBuilder();
		for(int i = 0;i<ids.length;i++) {
			if((i+1)!=ids.length) {
				sbId.append("'"+ids[i]+"',");
			}else {
				sbId.append("'"+ids[i]+"'");
			}
		}
		sbId.append(" )");
		sb.append(" ( p.organization in ( ");
		sb.append(sbId.toString());
		if(ids.length>0) {
			sb.append(" or ( ");
			for(int i = 0;i<ids.length;i++) {
				if((i+1)!=ids.length) {
					sb.append("instr(p.shareCreateOrgId,'"+ids[i]+"') > 0 or ");
				}else {
					sb.append("instr(p.shareCreateOrgId,'"+ids[i]+"') > 0 ))");
				}
			}	
		}
		
		IPage page = baseDao.search(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
		ArrayList<ProductPriceHead> productPriceHeadList = (ArrayList<ProductPriceHead>) page.getList();
		for(ProductPriceHead head : productPriceHeadList){
			String client = head.getClientId();
			if(StringUtil.isNotEmpty(client)){
				CustomInfo custom = customService.getCustomInfo(client);
				if(custom != null){
					head.setClientName(custom.getCustomFullName());
					head.setCustomCategory(custom.getCustomCategory());
					head.setCustomCategoryName(custom.getCustomCategoryName());
					head.setCustomCategorySub(custom.getCustomCategorySub());
					head.setCustomCategorySubName(custom.getCustomCategorySubName());
				}
			}
		}
		return productPriceHeadList;
	}

	private void getParentId(LovMember lov,ArrayList parentId) {
		LovMember parentFieldMember = new LovMember();
		parentFieldMember = baseDao.get(LovMember.class, lov.getParentId());
		if(parentFieldMember!= null) {
			parentId.add(parentFieldMember.getId());
			/*if(parentFieldMember.getParentId()!=null) {
				getParentId(parentFieldMember,parentId);
			}*/
		}
	}

	@Override
	public List<CustomAddressInfo> getCustomAddressInfoAllList(String userType,PageCondition condition)
			throws AnneException {
	
		String search= condition.getStringCondition("search");
		
		List<Object> args = new ArrayList<>();
		String hql=" select c from CustomAddressInfo c where customAddressType = (select m.id from LovMember m where name = '收货地址') ";
		if(StringUtil.isNotEmpty(userType)){
			hql += " and c.customId = ?" ;
			args.add(userType);
		}
		if(StringUtil.isNotEmpty(search)){
			hql += " and c.customAddress like ?";
			args.add("%"+ search + "%");
		}
		return baseDao.findEntity(hql,args.toArray());
	}
}
