package com.ibm.kstar.impl.support.docmanagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.support.docmanagement.IDocManagementService;
import com.ibm.kstar.entity.support.docmanagement.SupportTemplate;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class DocManagementServiceImpl implements IDocManagementService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage querySupportTemplate(PageCondition condition,String positionId, String orgId) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" s ");
		hql.append(" from ");
		hql.append(" SupportTemplate s ");
		hql.append(" ,BusinessPermissionRel b ");
		hql.append(" where ");
		hql.append(" s.id = b.businessId ");
		hql.append(" and (b.orgId = ? ");
		hql.append("     or b.orgId = ?) ");
		
		List<Object> args = new ArrayList<Object>();
		args.add(orgId);
		args.add(positionId);
		
		if (!StringUtils.isEmpty(condition.getStringCondition("searchKey"))) {
			hql.append(" and s.docType like ? ");
			args.add("%" + condition.getStringCondition("searchKey") + "%");
		}
		
		hql.append(" order by s.updatedAt desc ");
		
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage querySupportTemplate(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(SupportTemplate.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveSupportTemplateInfo(SupportTemplate supportTemplate)
			throws AnneException {
		baseDao.save(supportTemplate);
		
	}

	@Override
	public SupportTemplate getSupportTemplateInfo(String id) throws AnneException {
		return baseDao.get(SupportTemplate.class, id);
	}

	@Override
	public void updateSupportTemplateInfo(SupportTemplate supportTemplate)
			throws AnneException {
		SupportTemplate oldSupportTemplate = baseDao.get(SupportTemplate.class, supportTemplate.getId());
		if(oldSupportTemplate == null){
			throw new AnneException(IDocManagementService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldSupportTemplate.getId().equals(oldSupportTemplate.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(supportTemplate, oldSupportTemplate);
		
		baseDao.update(oldSupportTemplate);
	}

	@Override
	public void deleteSupportTemplateInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete SupportTemplate where id = ? ",new Object[]{id});
	}
}
