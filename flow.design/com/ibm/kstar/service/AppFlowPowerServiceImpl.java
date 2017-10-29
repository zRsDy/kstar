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

import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.entity.AppFlowPower;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class AppFlowPowerServiceImpl implements IAppFlowPowerService{
	
	@Autowired
	BaseDao baseDao;

	@Autowired
	ILovMemberService lovMemberService;
	
	@Override
	public IPage query(PageCondition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(AppFlowPower.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public AppFlowPower get(String id) throws AnneException {
		return baseDao.get(AppFlowPower.class, id);
	}

	@Override
	public void addOrUpdate(AppFlowPower power) throws AnneException {
		if(power.getId() != null){
			AppFlowPower oldPower = baseDao.get(AppFlowPower.class,power.getId());
			BeanUtils.copyPropertiesIgnoreNull(power,oldPower);
			baseDao.update(oldPower);
		}else{
			baseDao.save(power);
		}
	}

	@Override
	public void delete(String id) throws AnneException {
		AppFlowPower power = baseDao.get(AppFlowPower.class, id);
		baseDao.delete(power);
	}

	public List<AppFlowPower> getPowerByFlowCode(String flowCode){
		String appId = lovMemberService.getAppIdByFlowCode(flowCode);
		return baseDao.findEntity("from AppFlowPower p where p.applicationId = ? ",new Object[]{appId});
	}
	
}
