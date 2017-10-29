package com.ibm.kstar.impl.system.lov;

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

import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class LovGroupServiceImpl implements ILovGroupService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public void save(LovGroup lovGroup) throws AnneException {
		checkCode(lovGroup);
		baseDao.save(lovGroup);
	}

	@Override
	public void update(LovGroup lovGroup) throws AnneException {
		checkCode(lovGroup);
		LovGroup oldLovGroup = baseDao.get(LovGroup.class, lovGroup.getId());
		BeanUtils.copyPropertiesIgnoreNull(lovGroup, oldLovGroup);
		try{
			baseDao.update(oldLovGroup);
		}catch(Exception e){
			throw new AnneException("已经存在相同的代码："+lovGroup.getCode());
		}
	}
	
	/**
	 * 检查Code字段是否有重复
	 * @param lovGroup
	 */
	private void checkCode(LovGroup lovGroup){
		Long count = 0L;
		if(lovGroup.getId() == null){
			count = baseDao.findUniqueEntity("select count(*) from LovGroup g where g.code = ? ",lovGroup.getCode());
		}else{
			count = baseDao.findUniqueEntity("select count(*) from LovGroup g where g.code = ? and g.id != ? ",new Object[]{lovGroup.getCode(),lovGroup.getId()});
		}
		if(count > 0){
			throw new AnneException("已经存在相同的代码："+lovGroup.getCode());
		}
	}
	
	@Override
	public IPage query(PageCondition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(LovGroup.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void delete(String lovGroupId) throws AnneException {
		baseDao.deleteById(LovGroup.class, lovGroupId);
	}

	@Override
	public LovGroup get(String lovGroupId) throws AnneException {
		return baseDao.get(LovGroup.class, lovGroupId);
	}
	
	@Override
	public LovGroup getByCode(String lovGroupCode) throws AnneException{
		return baseDao.findUniqueEntity(" from LovGroup m where m.code = ? " , lovGroupCode);
	}
	
}
