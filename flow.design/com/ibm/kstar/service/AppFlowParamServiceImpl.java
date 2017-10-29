package com.ibm.kstar.service;

import java.util.List;

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

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.AppFlowParam;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class AppFlowParamServiceImpl implements IAppFlowParamService{

	@Autowired
	BaseDao baseDao;

	@Override
	public IPage query(PageCondition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(AppFlowParam.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public AppFlowParam get(String id) throws AnneException {
		return baseDao.get(AppFlowParam.class, id);
	}

	@Override
	public void addOrUpdate(AppFlowParam param) throws AnneException {
		if(param.getId() != null){
			AppFlowParam oldParam = baseDao.get(AppFlowParam.class,param.getId());
			BeanUtils.copyPropertiesIgnoreNull(param,oldParam);
			baseDao.update(oldParam);
		}else{
			baseDao.save(param);
		}
	}

	@Override
	public void delete(String id) throws AnneException {
		AppFlowParam param = baseDao.get(AppFlowParam.class, id);
		baseDao.delete(param);
	}

	@Override
	public List<LovMember> getList(String optTxt1) throws AnneException {
		String hql = "from LovMember m where m.groupId='APPLICATION' and m.deleteFlag='N'";
		return baseDao.findEntity(hql);
	}
}
