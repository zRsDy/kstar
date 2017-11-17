package com.ibm.kstar.impl.custom;

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

import com.ibm.kstar.api.custom.ICustomMergeService;
import com.ibm.kstar.entity.custom.CustomMerge;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomMergeImpl implements ICustomMergeService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage queryMerge(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomMerge.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveMergeInfo(CustomMerge customMerge) throws AnneException {
		
		baseDao.save(customMerge);
		
	}

	@Override
	public CustomMerge getMergeInfo(String id) throws AnneException {
		return baseDao.get(CustomMerge.class, id);
	}

	
	@Override
	public void updateMergeInfo(CustomMerge customMerge) throws AnneException {
		CustomMerge oldCustomMerge = baseDao.get(CustomMerge.class, customMerge.getId());
		if(oldCustomMerge == null){
			throw new AnneException(ICustomMergeService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomMerge.getId().equals(oldCustomMerge.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customMerge, oldCustomMerge);
		baseDao.update(oldCustomMerge);
	}
	
	
	@Override
	public void deleteMergeInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomMerge where id = ? ",new Object[]{id});
	}
	
	

}
