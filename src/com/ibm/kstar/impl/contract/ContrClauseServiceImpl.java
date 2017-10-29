package com.ibm.kstar.impl.contract;


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
import org.xsnake.web.utils.StringUtil;


import com.ibm.kstar.api.contract.IContrClauseService;

import com.ibm.kstar.entity.contract.ContrClause;
import com.ibm.kstar.entity.contract.ContrPay;



@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrClauseServiceImpl implements IContrClauseService {

	@Autowired
	BaseDao baseDao;

	@Override
	public IPage query(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(ContrClause.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void save(ContrClause contrClause) throws AnneException {
		if(StringUtil.isEmpty(contrClause.getContrNo())){
			throw new AnneException("合同单编号不能为空");
		}
		
		
		baseDao.save(contrClause);
		
	}

	@Override
	public ContrClause get(String id) throws AnneException {
		if(id==null){
			return null;
		}
		return baseDao.get(ContrClause.class, id);
	}

	@Override
	public void update(ContrClause contrClause) throws AnneException {
		ContrClause old = baseDao.get(ContrClause.class, contrClause.getId());
		if(old == null){
			throw new AnneException(IContrClauseService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}
		
		if(!contrClause.getId().equals(old.getId())){
			throw new AnneException("合同单ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(contrClause, old);
		//old.setCreateTime(new Time(0));
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from ContrClause where id = ? ",contrClause.getId());
		if(count > 1){
			throw new AnneException("合同单ID已经存在!"); 
		}
		
	}

	public void delete(String id) throws AnneException {
		baseDao.executeHQL(" delete ContrClause contr where contr.id = ? ",new Object[]{id});
		
	}

}
