package com.ibm.kstar.impl.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.product.IClientService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductClient;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ClientServiceImpl implements IClientService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage query(PageCondition condition) throws Exception  {
		FilterObject filterObject = condition.getFilterObject(KstarProductClient.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public KstarProductClient queryClientById(String id) {
		KstarProductClient kc = baseDao.get(KstarProductClient.class, id);
		return kc;
	}

	@Override
	public void save(KstarProductClient kc, UserObject user) {
		if(kc.getId() != null){
			kc.setRecordInfor(true, user);
			baseDao.update(kc);
		}else{
			kc.setRecordInfor(false, user);
			baseDao.save(kc);
		}
	}

	@Override
	public void delete(String id) {
		baseDao.deleteById(KstarProductClient.class, id);
	}
}