package com.ibm.kstar.impl.contract;

import java.sql.Time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ibm.kstar.api.contract.IContrAddrService;

import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.ContrPay;


@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrAddrServiceImpl implements IContrAddrService {

	@Autowired
	BaseDao baseDao;
	
	private static final Logger log = LoggerFactory.getLogger(ContrAddrServiceImpl.class);

	@Override
	public IPage query(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(ContrAddr.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());

//		StringBuffer hql = new StringBuffer(" from ContrAddr ");
//		return baseDao.search(hql.toString(),condition.getRows(), condition.getPage());
	}

	@Override
	public void save(ContrAddr contrAddr) throws AnneException {
		baseDao.save(contrAddr);		
	}

	@Override
	public ContrAddr get(String id) throws AnneException {
		if(id==null){
			return null;					
		}
		return baseDao.get(ContrAddr.class, id);
	}

	@Override
	public void update(ContrAddr contrAddr) throws AnneException {
		ContrAddr old = baseDao.get(ContrAddr.class, contrAddr.getId());
		if(old == null){
			throw new AnneException(IContrAddrService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}
//		
//		if(!contrAddr.getId().equals(old.getId())){
//			throw new AnneException("合同单ID不能被修改");
//		}
		
		BeanUtils.copyPropertiesIgnoreNull(contrAddr, old);
		//old.setCreateTime(new Time(0));
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from ContrAddr where c_id = ? ",contrAddr.getId());
		if(count > 1){
			throw new AnneException("地址ID已经存在!"); 
		}
		
	}
	
	@Override
	public void delete(String id) throws AnneException {
		baseDao.executeHQL(" delete ContrAddr  where c_id = ? ",new Object[]{id});
		
	}

	

}
