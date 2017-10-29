package com.ibm.kstar.api.report;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.report.RankHeader;
import com.ibm.kstar.entity.report.RankLine;

public interface IReportParamService {

	IPage query(PageCondition condition);
	
	List<RankHeader> rankList();
	
	RankHeader get(String headerId);
	
	void save(RankHeader rh);
	
	void delete(String headerId);
	
	void update(RankHeader rh);
	
	List<RankLine> getLineList(String headerId);

	void config(String headerId, String[] orgs);

	List<RankHeader> rankListByOrgId(String orgId);
}
