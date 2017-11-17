package com.ibm.kstar.impl.channel;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.channel.IRebateProductDetailService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateProductDetail;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RebateProductDetailServiceImpl extends BaseServiceImpl implements IRebateProductDetailService {
	@Autowired
	BaseDao baseDao;

	@Autowired
	IEmployeeService employeeService;
	
	@Override
	public IPage queryDetails(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(RebateProductDetail.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		IPage page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<RebateProductDetail> list = (List<RebateProductDetail>) page.getList();
		for(int i=0; i<list.size(); i++){
			list.get(i).setRowNumber(i+1);
		}
		return page;
	}

	@Override
	public RebateProductDetail queryDetail(String id) {
		return baseDao.get(RebateProductDetail.class, id);
	}
	
	@Override
	public void addOrEdit(RebateProductDetail detail, UserObject user) {
		if(detail.getId() != null){
			RebateProductDetail detailData = baseDao.get(RebateProductDetail.class, detail.getId());
			BeanUtils.copyPropertiesIgnoreNull(detail,detailData);
			detailData.setRecordInfor(true, user);
			baseDao.update(detailData);
		}else{
			detail.setRecordInfor(false, user);
			baseDao.save(detail);
		}
	}

	@Override
	public void delete(String id) {
		RebateProductDetail detailInfo = baseDao.get(RebateProductDetail.class, id);
		baseDao.delete(detailInfo);
	}

	
}