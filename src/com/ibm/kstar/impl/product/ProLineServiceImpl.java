package com.ibm.kstar.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.KstarProductLine;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProLineServiceImpl implements IProLineService{
	@Autowired
	BaseDao baseDao;

	@Override
	public KstarProductLine query(String id) {
		return baseDao.findUniqueEntity(" from KstarProductLine where id = ? ", id);
	}

	@Override
	public List<LovMember> prepareForkLov(KstarProduct kp) {
		
		KstarProductLine kl = this.query(kp.getProLineID());
		
		LovMember lm = new LovMember();
		lm.setId(StringUtil.getUUID());
		lm.setName(kl.getCproCategory());
		lm.setCode(lm.getId());
		lm.setLeafFlag("N");
		
		
		LovMember lm1 = new LovMember();
		lm1.setId(StringUtil.getUUID());
		lm1.setName(kl.getProLine());
		lm1.setCode(lm1.getId());
		lm1.setParentId(lm.getCode());
		lm1.setLeafFlag("N");
		
		
		LovMember lm2 = new LovMember();
		lm2.setName(kl.getProSeries());
		lm2.setCode(StringUtil.getUUID());
		lm2.setId(lm2.getCode());
		lm2.setParentId(lm1.getCode());
		lm2.setLeafFlag("N");
		
		LovMember lm3 = new LovMember();
		lm3.setId(StringUtil.getUUID());
		lm3.setName(kp.getProductName());
		lm3.setCode(lm3.getId());
		lm3.setParentId(lm2.getCode());
		lm3.setLeafFlag("Y");
		
		List<LovMember> list = new ArrayList<LovMember>();
		
		list.add(lm);
		list.add(lm1);
		list.add(lm2);
		list.add(lm3);
		return list;
	}

	@Override
	public IPage query(PageCondition condition) throws Exception {
		FilterObject filterObject = condition.getFilterObject(KstarProductLine.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public List<LovMember> selectProSeries(Condition condition) {
		String noInProGroup = condition.getStringCondition("noInProGroup");
		String sql = "select distinct t.c_pro_series from CRM_T_PRODUCT_LINE t";
		if(StringUtil.isNotEmpty(noInProGroup)){
			sql += " where not exists (select d.c_product_series from crm_t_rebate_product_detail d where d.c_product_series = t.c_pro_series)";
		}
		Object findData =(Object) baseDao.findBySql(sql);
		@SuppressWarnings("unchecked")
		List<Object> r = (List<Object>) findData;
		List<LovMember> result = new ArrayList<LovMember>();
		for(Object obj : r){
			if(obj != null){
				String id = (String)obj;
				LovMember lov = new LovMember();
				lov.setId(id);
				lov.setName(id);
				result.add(lov);
			}
		}
		return result;
	}
	
	@Override
	public List<LovMember> selectProLines() {
		Object findData =(Object) baseDao.findBySql("select distinct t.C_PRO_LINE from CRM_T_PRODUCT_LINE t");
		@SuppressWarnings("unchecked")
		List<Object> r = (List<Object>) findData;
		List<LovMember> result = new ArrayList<LovMember>();
		for(Object obj : r){
			if(obj != null){
				String id = (String)obj;
				LovMember lov = new LovMember();
				lov.setId(id);
				lov.setName(id);
				result.add(lov);
			}
		}
		return result;
	}

}


