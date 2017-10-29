package com.ibm.kstar.impl.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilerRuler;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.product.IProSubService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProSub;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProSubServiceImpl extends BaseServiceImpl implements IProSubService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage query(PageCondition condition) throws Exception {
		List<Object> args = new ArrayList<>();
		StringBuilder hql = new StringBuilder("select new com.ibm.kstar.entity.product.KstarProSub(s,p) from KstarProSub s,KstarProduct p where s.proSubID = p.id and s.productID = ?");
		args.add(condition.getStringCondition("productID"));
		this.addQueryCondition(condition, args, hql, "productCode", "p.productCode", "like");
		this.addQueryCondition(condition, args, hql, "productName", "p.productName", "like");
		hql.append(" order by s.id desc");
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public IPage queryDup(PageCondition condition) throws Exception{
		String hql = "from KstarProSub ps where 1=1 ";
		List<Object> args = new ArrayList<Object>();
		List<FilerRuler> list = condition.getFilterObject().getRules();
		for(FilerRuler fr : list){
			hql = hql + " and ps." + fr.getField() + HqlUtil.SQLCONDITON.get(fr.getOp()) + " ? ";
			args.add(HqlUtil.getObjectValue(KstarProSub.class, fr.getField(), fr.getData()));
		}
		return baseDao.search(hql,args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public void save(KstarProSub kc, UserObject user) {
		kc.setRecordInfor(false, user);
		baseDao.save(kc);
	}
	
	@Override
	public void update(KstarProSub kc, UserObject user){
		KstarProSub old = baseDao.get(KstarProSub.class, kc.getId());
		BeanUtils.copyPropertiesIgnoreNull(kc, old);
		old.setRecordInfor(true, user);
		baseDao.update(old);
	}

	@Override
	public List<KstarProSub> querySubList(String productID) {
		String hql = " from KstarProSub where productID = '" + productID + "' ";
		List<KstarProSub> reVlaue  = baseDao.findEntity(hql);		
		return reVlaue;
	}

	@Override
	public KstarProSub queryById(String id) {
		KstarProSub kd = baseDao.get(KstarProSub.class, id);
		return kd;
	}
	
	@Override
	public void delete(String id) {
		baseDao.deleteById(KstarProSub.class, id);
	}
}