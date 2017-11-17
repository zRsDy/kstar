package com.ibm.kstar.impl.bizopp;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.api.bizopp.IBizOppLovInfoService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class BizOppLovInfoServiceImpl implements IBizOppLovInfoService{
	@Autowired
	BaseDao baseDao;

	
	@Override
	public List<BusinessOpportunity> getBizOppNameInfoList(PageCondition condition, UserObject user) throws AnneException {
		
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" c ");
		hql.append(" from ");
		hql.append(" BusinessOpportunity c ");
		hql.append(" where ");
		hql.append(" 1 = 1 ");
		hql.append(" and (c.id like ? ");
		hql.append(" or c.opportunityName like ? ) ");
		String perHql= PermissionUtil.getPermissionHQL(null, "c.createdById", "c.createdPosId", "c.createdOrgId", "c.id", user, "BusinessOpportunity");
		hql.append(" and ");
		hql.append(perHql);
		List<Object> args = new ArrayList<Object>();
		args.add("%"+condition.getStringCondition("search")+"%");
		args.add("%"+condition.getStringCondition("search")+"%");
		
		List<BusinessOpportunity> list = baseDao.findEntity(hql.toString(), args.toArray());
		return list;
	}
}
