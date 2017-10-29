package com.ibm.kstar.impl.common.outteam;


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

import com.ibm.kstar.api.common.outteam.ISalesOutService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.common.outteam.SalesTeamOut;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class SalesOutServiceImpl implements ISalesOutService{
	@Autowired
	BaseDao baseDao;

	
	// 外部团队
	@Override
	public IPage querySalesOut(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(SalesTeamOut.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveSalesOutInfo(SalesTeamOut customSalesTeamOut)
			throws AnneException {
		baseDao.save(customSalesTeamOut);
		
	}

	@Override
	public SalesTeamOut getSalesOutInfo(String id) throws AnneException {
		return baseDao.get(SalesTeamOut.class, id);
	}

	@Override
	public void updateSalesOutInfo(SalesTeamOut customSalesTeamOut)
			throws AnneException {
		SalesTeamOut oldCustomSalesTeamOut = baseDao.get(SalesTeamOut.class, customSalesTeamOut.getId());
		if(oldCustomSalesTeamOut == null){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomSalesTeamOut.getId().equals(oldCustomSalesTeamOut.getId())){
			throw new AnneException("报价单ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customSalesTeamOut, oldCustomSalesTeamOut);
//									oldQuot.setCreateTime(new Date());
		baseDao.update(oldCustomSalesTeamOut);
		Long count = baseDao.findUniqueEntity("select count(*) from SalesTeamOut where id = ? ",customSalesTeamOut.getId());
		if(count > 1){
			throw new AnneException("报价单ID已经存在!"); 
		}
	}

	@Override
	public void deleteSalesOutInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete SalesTeamOut where id = ? ",new Object[]{id});
		
	}

	@Override
	public List<SalesTeamOut> getSalesOutInfoBycode(String code)
			throws AnneException {
		// TODO Auto-generated method stub
		return null;
	}

}
