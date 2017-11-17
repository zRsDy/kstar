package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
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

import com.ibm.kstar.api.custom.ICustomCompReportService;
import com.ibm.kstar.entity.custom.CustomCompAchiBusiness;
import com.ibm.kstar.entity.custom.CustomCompAchiDeep;
import com.ibm.kstar.entity.custom.CustomCompAchiProduct;
import com.ibm.kstar.entity.custom.CustomCompetitorAchi;
import com.ibm.kstar.entity.custom.CustomCompetitorReport;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomCompReportImpl implements ICustomCompReportService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage queryReport(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomCompetitorReport.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryReportByCustomCode(PageCondition condition) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" e ");
		hql.append(" from ");
		hql.append(" CustomCompetitorReport e ");
		hql.append(" ,CustomCompetitorAchi a ");
		hql.append(" where ");
		hql.append(" e.id = a.reportId ");
		hql.append(" and a.competitorCode = ? ");
		hql.append(" order by e.updatedAt desc ");
		List<Object> args = new ArrayList<Object>();
		args.add(condition.getStringCondition("customCode"));
		
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveReportInfo(CustomCompetitorReport customCompetitorReport) throws AnneException {
		baseDao.save(customCompetitorReport);
	}

	@Override
	public CustomCompetitorReport getReportInfo(String id) throws AnneException {
		return baseDao.get(CustomCompetitorReport.class, id);
	}

	
	@Override
	public void updateReportInfo(CustomCompetitorReport customCompetitorReport) throws AnneException {
		CustomCompetitorReport oldCustomCompetitorReport = baseDao.get(CustomCompetitorReport.class, customCompetitorReport.getId());
		if(oldCustomCompetitorReport == null){
			throw new AnneException(ICustomCompReportService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomCompetitorReport.getId().equals(oldCustomCompetitorReport.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customCompetitorReport, oldCustomCompetitorReport);
		baseDao.update(oldCustomCompetitorReport);
	}
	
	
	@Override
	public void deleteReportInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomCompetitorReport where id = ? ",new Object[]{id});
	}
	
	//业绩
	@Override
	public IPage queryAchievement(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomCompetitorAchi.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveAchievementInfo(CustomCompetitorAchi customCompetitorAchi)
			throws AnneException {
		baseDao.save(customCompetitorAchi);
		
	}

	@Override
	public CustomCompetitorAchi getAchievementInfo(String id) throws AnneException {
		return baseDao.get(CustomCompetitorAchi.class, id);
	}

	@Override
	public void updateAchievementInfo(CustomCompetitorAchi customCompetitorAchi)
			throws AnneException {
		CustomCompetitorAchi oldCustomCompetitorAchi = baseDao.get(CustomCompetitorAchi.class, customCompetitorAchi.getId());
		if(oldCustomCompetitorAchi == null){
			throw new AnneException(ICustomCompReportService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomCompetitorAchi.getId().equals(oldCustomCompetitorAchi.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customCompetitorAchi, oldCustomCompetitorAchi);
		baseDao.update(oldCustomCompetitorAchi);
		
	}

	@Override
	public void deleteAchievementInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomCompetitorAchi where id = ? ",new Object[]{id});
	}

	@Override
	public List<CustomCompetitorAchi> getAchievementInfoBycode(String code)
			throws AnneException {
		return null;
	}
	
	//业绩分解-产品
	@Override
	public IPage queryAnalyseProduct(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomCompAchiProduct.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	

	@Override
	public void saveAnalyseProduct(CustomCompAchiProduct customCompAchiProduct)
			throws AnneException {
		baseDao.save(customCompAchiProduct);
		
	}

	@Override
	public CustomCompAchiProduct getAnalyseProductInfo(String id) throws AnneException {
		return baseDao.get(CustomCompAchiProduct.class, id);
	}

	@Override
	public void updateAnalyseProductInfo(CustomCompAchiProduct customCompAchiProduct)
			throws AnneException {
		CustomCompAchiProduct oldCustomCompAchiProduct = baseDao.get(CustomCompAchiProduct.class, customCompAchiProduct.getId());
		if(oldCustomCompAchiProduct == null){
			throw new AnneException(ICustomCompReportService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomCompAchiProduct.getId().equals(oldCustomCompAchiProduct.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customCompAchiProduct, oldCustomCompAchiProduct);
		baseDao.update(oldCustomCompAchiProduct);
		
	}

	@Override
	public void deleteAnalyseProductInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomCompAchiProduct where id = ? ",new Object[]{id});
	}

	//业绩分解-行业
	@Override
	public IPage queryAnalyseBusiness(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomCompAchiBusiness.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	

	@Override
	public void saveAnalyseBusiness(CustomCompAchiBusiness customCompAchiBusiness)
			throws AnneException {
		baseDao.save(customCompAchiBusiness);
		
	}

	@Override
	public CustomCompAchiBusiness getAnalyseBusinessInfo(String id) throws AnneException {
		return baseDao.get(CustomCompAchiBusiness.class, id);
	}

	@Override
	public void updateAnalyseBusinessInfo(CustomCompAchiBusiness customCompAchiBusiness)
			throws AnneException {
		CustomCompAchiBusiness oldCustomCompAchiBusiness = baseDao.get(CustomCompAchiBusiness.class, customCompAchiBusiness.getId());
		if(oldCustomCompAchiBusiness == null){
			throw new AnneException(ICustomCompReportService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomCompAchiBusiness.getId().equals(oldCustomCompAchiBusiness.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customCompAchiBusiness, oldCustomCompAchiBusiness);
		baseDao.update(oldCustomCompAchiBusiness);
		
	}

	@Override
	public void deleteAnalyseBusinessInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomCompAchiBusiness where id = ? ",new Object[]{id});
	}

	//业绩分解-产品建议分析
	@Override
	public IPage queryAnalyseDeep(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomCompAchiDeep.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	

	@Override
	public void saveAnalyseDeep(CustomCompAchiDeep customCompAchiDeep)
			throws AnneException {
		baseDao.save(customCompAchiDeep);
		
	}

	@Override
	public CustomCompAchiDeep getAnalyseDeepInfo(String id) throws AnneException {
		return baseDao.get(CustomCompAchiDeep.class, id);
	}

	@Override
	public void updateAnalyseDeepInfo(CustomCompAchiDeep customCompAchiDeep)
			throws AnneException {
		CustomCompAchiDeep oldCustomCompAchiDeep = baseDao.get(CustomCompAchiDeep.class, customCompAchiDeep.getId());
		if(oldCustomCompAchiDeep == null){
			throw new AnneException(ICustomCompReportService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomCompAchiDeep.getId().equals(oldCustomCompAchiDeep.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customCompAchiDeep, oldCustomCompAchiDeep);
		baseDao.update(oldCustomCompAchiDeep);
		
	}

	@Override
	public void deleteAnalyseDeepInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomCompAchiDeep where id = ? ",new Object[]{id});
	}

}
