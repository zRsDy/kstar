package com.ibm.kstar.impl.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.report.IReportParamService;
import com.ibm.kstar.entity.report.RankHeader;
import com.ibm.kstar.entity.report.RankLine;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ReportParamServiceImpl implements IReportParamService {

	@Autowired
	BaseDao baseDao;
	
	@Override
	public IPage query(PageCondition condition) {
		return baseDao.search(" from RankHeader ", condition.getRows(), condition.getPage());
	}

	@Override
	public RankHeader get(String headerId) {
		return baseDao.get(RankHeader.class, headerId);
	}

	@Override
	public void save(RankHeader rh) {
		baseDao.save(rh);
	}

	@Override
	public void delete(String headerId) {
		baseDao.deleteById(RankHeader.class, headerId);
	}

	@Override
	public void update(RankHeader rh) {
		baseDao.update(rh);
	}

	@Override
	public List<RankLine> getLineList(String headerId) {
		return baseDao.findEntity(" from RankLine where headerId = ? ",headerId);
	}

	@Override
	public void config(String headerId, String[] orgs) {
		baseDao.executeHQL(" delete RankLine where headerId = ? " ,new Object[]{headerId});
		if(orgs == null || orgs.length == 0){
			return;
		}
		for(String orgId : orgs){
			RankLine rl = new RankLine();
			rl.setHeaderId(headerId);
			rl.setOrgId(orgId);
			baseDao.save(rl);
		}
	}

	@Override
	public List<RankHeader> rankList() {
		return baseDao.findEntity(" from RankHeader ");
	}
	
	
	@Override
	public List<RankHeader> rankListByOrgId(String orgId) {
		String hql = " select t from RankHeader t,RankLine a where 1=1 ";
			hql += " and t.id = a.headerId ";
			hql += " and a.orgId = ? ";
		return baseDao.findEntity(hql,new Object[]{orgId});
	}

}
