package com.ibm.kstar.api.custom;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.custom.CustomCompAchiBusiness;
import com.ibm.kstar.entity.custom.CustomCompAchiDeep;
import com.ibm.kstar.entity.custom.CustomCompAchiProduct;
import com.ibm.kstar.entity.custom.CustomCompetitorAchi;
import com.ibm.kstar.entity.custom.CustomCompetitorReport;

public interface ICustomCompReportService {
	//竞争对手提报
	IPage queryReport(PageCondition condition);
	
	void saveReportInfo(CustomCompetitorReport customCompetitorReport) throws AnneException;
	
	CustomCompetitorReport getReportInfo(String id) throws AnneException;

	void updateReportInfo(CustomCompetitorReport customCompetitorReport) throws AnneException;

	void deleteReportInfo(String id) throws AnneException;
	
	IPage queryReportByCustomCode(PageCondition condition);
	
	// 对手业绩
	IPage queryAchievement(PageCondition condition);
	
	void saveAchievementInfo(CustomCompetitorAchi customCompetitorAchi) throws AnneException;
	
	CustomCompetitorAchi getAchievementInfo(String id) throws AnneException;

	void updateAchievementInfo(CustomCompetitorAchi customCompetitorAchi) throws AnneException;

	void deleteAchievementInfo(String id) throws AnneException;
	
	List<CustomCompetitorAchi> getAchievementInfoBycode(String code) throws AnneException;
	
	// 对手业绩分解-产品
	IPage queryAnalyseProduct(PageCondition condition);
	
	void saveAnalyseProduct(CustomCompAchiProduct customCompAchiProduct) throws AnneException;
	
	CustomCompAchiProduct getAnalyseProductInfo(String id) throws AnneException;

	void updateAnalyseProductInfo(CustomCompAchiProduct customCompAchiProduct) throws AnneException;

	void deleteAnalyseProductInfo(String id) throws AnneException;
	
	// 对手业绩分解-行业
	IPage queryAnalyseBusiness(PageCondition condition);
	
	void saveAnalyseBusiness(CustomCompAchiBusiness customCompAchiBusiness) throws AnneException;
	
	CustomCompAchiBusiness getAnalyseBusinessInfo(String id) throws AnneException;

	void updateAnalyseBusinessInfo(CustomCompAchiBusiness customCompAchiBusiness) throws AnneException;

	void deleteAnalyseBusinessInfo(String id) throws AnneException;
	
	// 对手业绩分解-产品建议分析
	IPage queryAnalyseDeep(PageCondition condition);
	
	void saveAnalyseDeep(CustomCompAchiDeep customCompAchiDeep) throws AnneException;
	
	CustomCompAchiDeep getAnalyseDeepInfo(String id) throws AnneException;

	void updateAnalyseDeepInfo(CustomCompAchiDeep customCompAchiDeep) throws AnneException;

	void deleteAnalyseDeepInfo(String id) throws AnneException;

}
