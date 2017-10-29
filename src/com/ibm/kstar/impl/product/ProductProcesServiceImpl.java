package com.ibm.kstar.impl.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProductProcesServiceImpl implements IProductProcesService {
	
	@Autowired
	BaseDao baseDao;

	@Override
	public void delete(String processId) {
		String hql =  " delete from KstarProductWorkFlow where processID = '" + processId + "'";
		baseDao.executeHQL(hql);
	}

	@Override
	public void save(KstarProductWorkFlow kp,UserObject user) {
		baseDao.saveOrUpdate(kp);
		
	}

	@Override
	public List<KstarProductWorkFlow> getList(String processId) {
		String hql =  " from KstarProductWorkFlow where processID = '" + processId + "'";
		List<KstarProductWorkFlow> reValue = baseDao.findEntity(hql);
		return reValue;
	}

}
