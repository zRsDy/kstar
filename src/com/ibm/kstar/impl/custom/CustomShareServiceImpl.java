package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
import java.util.List;

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
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomShareService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomShareInfo;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomShareServiceImpl implements ICustomShareService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	protected ITeamService teamService;
	
	@Autowired
	ICustomInfoService customservice;

	@Override
	public IPage queryShare(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(CustomShareInfo.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveShareInfo(CustomShareInfo customShareInfo, UserObject userObject) throws AnneException {
		
		CustomInfo ci = customservice.getCustomInfoByCustCode(customShareInfo.getCustomCode());
		customShareInfo.setShareSale(ci.getCreatedById());
		customShareInfo.setShareDept(ci.getCreatedOrgId());
		
		baseDao.save(customShareInfo);
	}

	@Override
	public CustomShareInfo getShareInfo(String id) throws AnneException {
		return baseDao.get(CustomShareInfo.class, id);
	}

	
	@Override
	public void updateShareInfo(CustomShareInfo customShareInfo ,String flg) throws AnneException {
		CustomShareInfo oldCustomShareInfo = baseDao.get(CustomShareInfo.class, customShareInfo.getId());
		if(oldCustomShareInfo == null){
			throw new AnneException(ICustomShareService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomShareInfo.getId().equals(oldCustomShareInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customShareInfo, oldCustomShareInfo);
		
		baseDao.update(oldCustomShareInfo);
		
		if (!StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_40, flg)) {
			return;
		}
		
		CustomInfo customInfo = customservice.getCustomInfoByCustCode(customShareInfo.getCustomCode());
		
		teamService.addPosition(
				customShareInfo.getApplierPos(),
				customShareInfo.getApplier(), 
				IConstants.CUSTOM_REPORT_PROC,
				customInfo.getId());
	}
	
	
	@Override
	public void deleteShareInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomShareInfo where id = ? ",new Object[]{id});
	}
	
	@Override
	public CustomShareInfo getShareInfoBycode(String code) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(code);
		List<CustomShareInfo> customShareInfos = baseDao.findEntity(" from CustomShareInfo where shareCode = ? ", code);
		
		return customShareInfos.get(0);
	}

}
