package com.ibm.kstar.impl.custom;

import org.apache.commons.lang.StringUtils;
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

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomCreditAdjustService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.custom.CustomCreditAdjustment;
import com.ibm.kstar.entity.custom.CustomInfo;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomCreditAdjustImpl implements ICustomCreditAdjustService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	ICustomInfoService customService;
	
	@Autowired
	protected ITeamService teamService;

	@Override
	public IPage queryAdjustment(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomCreditAdjustment.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveAdjustmentInfo(CustomCreditAdjustment customCreditAdjustment, 
			UserObject userObject) throws AnneException {
		baseDao.save(customCreditAdjustment);
		
		teamService.addPosition(
				userObject.getPosition().getId(),
				userObject.getEmployee().getId(), 
				IConstants.CUSTOM_ADJUST_PROC,
				customCreditAdjustment.getId());
	}

	@Override
	public CustomCreditAdjustment getAdjustmentInfo(String id) throws AnneException {
		return baseDao.get(CustomCreditAdjustment.class, id);
	}

	
	@Override
	public void updateAdjustmentInfo(CustomCreditAdjustment customCreditAdjustment, 
			String flg) throws AnneException {
		CustomCreditAdjustment oldCustomCreditAdjustment = baseDao.get(CustomCreditAdjustment.class, customCreditAdjustment.getId());
		if(oldCustomCreditAdjustment == null){
			throw new AnneException(ICustomCreditAdjustService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomCreditAdjustment.getId().equals(oldCustomCreditAdjustment.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customCreditAdjustment, oldCustomCreditAdjustment);
		baseDao.update(oldCustomCreditAdjustment);
		
		if (!StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_40, flg)) {
			return;
		}
		
		// process complete
		CustomInfo customInfo = customService.getCustomInfoByCode(customCreditAdjustment.getCustomCode());
		customInfo.setCreditId(customCreditAdjustment.getId());
		customInfo.setCorpPayDays(customCreditAdjustment.getPayDaysChangeto());
		customInfo.setCustomGrade(customCreditAdjustment.getGradeChangeto());
		
		customInfo.setLimitCurrency(customCreditAdjustment.getLimitCurrency());
		customInfo.setLimitAmount(customCreditAdjustment.getLimitAmount());
		customInfo.setLimitRate(customCreditAdjustment.getLimitRate());
		customInfo.setLimitUnit(customCreditAdjustment.getLimitUnit());
		// TODO 
		customInfo.setLimitComment("");
		customInfo.setLimitValidTo(customCreditAdjustment.getLimitValidTo());
		
		customInfo.setTempCurrency(customCreditAdjustment.getTempCurrency());
		customInfo.setTempAmount(customCreditAdjustment.getTempAmount());
		customInfo.setTempRate(customCreditAdjustment.getTempRate());
		customInfo.setTempUnit(customCreditAdjustment.getTempUnit());
		// TODO 
		customInfo.setTempComment("");
		customInfo.setTempValidTo(customCreditAdjustment.getTempValidTo());
		
		// 功能字段设值
		// 更新字段
		customService.updateCustomInfo(customInfo);
	}
	
	
	@Override
	public void deleteAdjustmentInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomCreditAdjustment where id = ? ",new Object[]{id});
	}

}
