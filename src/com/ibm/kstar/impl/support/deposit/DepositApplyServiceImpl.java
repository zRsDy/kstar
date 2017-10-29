package com.ibm.kstar.impl.support.deposit;

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

import com.ibm.kstar.api.support.deposit.IDepositApplyService;
import com.ibm.kstar.entity.support.deposit.DepositApply;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class DepositApplyServiceImpl implements IDepositApplyService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage query(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(DepositApply.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void save(DepositApply depositApply)
			throws AnneException {
		baseDao.save(depositApply);
	}

	@Override
	public DepositApply getDepositApply(String id) throws AnneException {
		return baseDao.get(DepositApply.class, id);
	}

	@Override
	public void update(DepositApply depositApply) throws AnneException {
		DepositApply oldDepositApply = baseDao.get(DepositApply.class, depositApply.getId());
		if(oldDepositApply == null){
			throw new AnneException(IDepositApplyService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldDepositApply.getId().equals(oldDepositApply.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(depositApply, oldDepositApply);
		
		baseDao.update(oldDepositApply);
	}

	@Override
	public void delete(String id) throws AnneException {
		baseDao.executeHQL(" delete DepositApply where id = ? ",new Object[]{id});
	}
}


