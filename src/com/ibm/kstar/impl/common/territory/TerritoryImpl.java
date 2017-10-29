package com.ibm.kstar.impl.common.territory;

import java.util.Date;

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

import com.ibm.kstar.api.common.territory.ITerritory;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.common.territory.TerritoryConfig;
import com.ibm.kstar.entity.common.territory.TerritoryInfo;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class TerritoryImpl implements ITerritory{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage queryTerritoryInfo(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(TerritoryInfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveTerritoryInfo(TerritoryInfo territoryInfo, UserObject userObject) throws AnneException {
		// 创建字段
		territoryInfo.setCreatedById(userObject.getEmployee().getId());
		territoryInfo.setCreatedAt(new Date());
		territoryInfo.setCreatedPosId(userObject.getPosition().getId());
		territoryInfo.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		territoryInfo.setUpdatedById(userObject.getEmployee().getId());
		territoryInfo.setUpdatedAt(new Date());
		baseDao.save(territoryInfo);
		
	}

	@Override
	public TerritoryInfo getTerritoryInfo(String id) throws AnneException {
		return baseDao.get(TerritoryInfo.class, id);
	}

	
	@Override
	public void updateTerritoryInfo(TerritoryInfo territoryInfo, UserObject userObject) throws AnneException {
		TerritoryInfo oldTerritoryInfo = baseDao.get(TerritoryInfo.class, territoryInfo.getId());
		if(oldTerritoryInfo == null){
			throw new AnneException(ITerritory.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldTerritoryInfo.getId().equals(oldTerritoryInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(territoryInfo, oldTerritoryInfo);
		
		// 更新字段
		territoryInfo.setUpdatedById(userObject.getEmployee().getId());
		territoryInfo.setUpdatedAt(new Date());
		baseDao.update(oldTerritoryInfo);
	}
	
	
	@Override
	public void deleteTerritoryInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete TerritoryInfo where id = ? ",new Object[]{id});
	}

	@Override
	public IPage queryTerritoryConfig(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(TerritoryConfig.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveTerritoryConfig(TerritoryConfig territoryConfig, UserObject userObject) throws AnneException {
		
		// 创建字段
		territoryConfig.setCreatedById(userObject.getEmployee().getId());
		territoryConfig.setCreatedAt(new Date());
		territoryConfig.setCreatedPosId(userObject.getPosition().getId());
		territoryConfig.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		territoryConfig.setUpdatedById(userObject.getEmployee().getId());
		territoryConfig.setUpdatedAt(new Date());
		baseDao.save(territoryConfig);
		
	}

	@Override
	public TerritoryConfig getTerritoryConfig(String id) throws AnneException {
		return baseDao.get(TerritoryConfig.class, id);
	}

	
	@Override
	public void updateTerritoryConfig(TerritoryConfig territoryConfig, UserObject userObject) throws AnneException {
		TerritoryConfig oldTerritoryConfig = baseDao.get(TerritoryConfig.class, territoryConfig.getId());
		if(oldTerritoryConfig == null){
			throw new AnneException(ITerritory.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldTerritoryConfig.getId().equals(oldTerritoryConfig.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(territoryConfig, oldTerritoryConfig);
		
		// 更新字段
		territoryConfig.setUpdatedById(userObject.getEmployee().getId());
		territoryConfig.setUpdatedAt(new Date());
				
		baseDao.update(oldTerritoryConfig);
	}
	
	
	@Override
	public void deleteTerritoryConfig(String id) throws AnneException {
		baseDao.executeHQL(" delete TerritoryConfig where id = ? ",new Object[]{id});
	}
}
