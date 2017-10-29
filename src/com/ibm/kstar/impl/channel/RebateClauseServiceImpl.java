package com.ibm.kstar.impl.channel;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.channel.IRebateClauseService;
import com.ibm.kstar.api.channel.IRebateProductService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateClause;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RebateClauseServiceImpl extends BaseServiceImpl implements IRebateClauseService {
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	SerialNumberService serialNumberService;
	
	@Autowired
	IRebateProductService rebateProductService;

	@Override
	public IPage queryClauses(PageCondition condition) {
		String policyId = condition.getStringCondition("policyId");
		List<Object> args = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select new com.ibm.kstar.entity.channel.RebateClause(c,p) from RebateClause c,RebateProduct p where c.productGroup = p.id");
		hql.append(" and c.policyId = ?");
		args.add(policyId);
		this.addQueryCondition(condition, args, hql, "productGroupName", "p.groupName", "like");
		hql.append(" order by c.id desc");
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public RebateClause queryClause(String id) {
		return baseDao.get(RebateClause.class, id);
	}

	@Override
	public void addOrEdit(RebateClause clause, UserObject user) {
		if(clause.getId() != null){
			RebateClause clauseData = baseDao.get(RebateClause.class, clause.getId());
			BeanUtils.copyPropertiesIgnoreNull(clause, clauseData);
			clauseData.setFinishRate2(clause.getFinishRate2());
			clauseData.setRebateRate2(clause.getRebateRate2());
			clauseData.setYearNoGrowth(clause.getYearNoGrowth());
			clauseData.setExcessRebate(clause.getExcessRebate());
			clauseData.setRemark(clause.getRemark());
			clauseData.setRecordInfor(true, user);
			baseDao.update(clauseData);
		}else{
			clause.setRecordInfor(false, user);
			baseDao.save(clause);
		}
	}

	@Override
	public void delete(String id) {
		RebateClause clauseInfo = baseDao.get(RebateClause.class, id);
		baseDao.delete(clauseInfo);
	}

}