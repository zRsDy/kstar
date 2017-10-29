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
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.channel.IRebateProductService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateProduct;
import com.ibm.kstar.entity.channel.RebateProductDetail;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RebateProductServiceImpl extends BaseServiceImpl implements IRebateProductService {
	@Autowired
	BaseDao baseDao;

	@Autowired
	IEmployeeService employeeService;
	
	@Override
	public IPage queryProducts(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(RebateProduct.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		IPage page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<RebateProduct> list = (List<RebateProduct>) page.getList();
		for(RebateProduct product : list){
			product.setCreaterName(employeeService.get(product.getCreater()).getName());
		}
		return page;
	}

	@Override
	public RebateProduct queryProduct(String id) {
		RebateProduct product = baseDao.get(RebateProduct.class, id);
		product.setCreaterName(employeeService.get(product.getCreater()).getName());
		return product;
	}
	
	@Override
	public void addOrEdit(RebateProduct product, UserObject user) {
		List<RebateProduct> lst = baseDao.findEntity("from RebateProduct p where p.groupName = ?",product.getGroupName());
		int lstSize = lst.size();
		if(product.getId() != null){
			if(lstSize > 0){
				if(product.getId().equals(lst.get(0).getId())){
					RebateProduct productData = lst.get(0);
					BeanUtils.copyPropertiesIgnoreNull(product,productData);
					productData.setRecordInfor(true, user);
					baseDao.update(productData);
				}else{
					throw new AnneException("该产品组名称已被用！");
				}
			}else{
				product.setRecordInfor(true, user);
				baseDao.update(product);
			}
		}else{
			if(lstSize > 0){
				throw new AnneException("该产品组名称已被用！");
			}
			product.setRecordInfor(false, user);
			baseDao.save(product);
		}
	}

	@Override
	public void delete(String id){
		RebateProduct product = baseDao.get(RebateProduct.class, id);
		if(product != null){
			List<RebateProductDetail> detailList = baseDao.findEntity("from RebateProductDetail d where d.productGroupId = ?", product.getId());
			baseDao.deleteAll(detailList);
			baseDao.delete(product);
		}
	}

	@Override
	public List<RebateProduct> getProducts() {
		List<RebateProduct> lst = baseDao.findEntity("from RebateProduct order by groupName");
		return lst;
	}
	
}