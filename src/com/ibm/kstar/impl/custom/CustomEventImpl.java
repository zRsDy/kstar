package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
import java.util.Date;
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
import com.ibm.kstar.api.custom.ICustomEventService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.custom.CustomEventContact;
import com.ibm.kstar.entity.custom.CustomEventInfo;
import com.ibm.kstar.entity.custom.CustomEventItems;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomEventImpl implements ICustomEventService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	protected ITeamService teamService;

	@Override
	public IPage queryEvent(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(CustomEventInfo.class);
		filterObject.addOrderBy("createdAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		
	}

	@Override
	public void saveEventInfo(CustomEventInfo customEventInfo,UserObject userObject) throws AnneException {
		// 功能字段设值
		// 创建字段
		customEventInfo.setCreatedById(userObject.getEmployee().getId());
		customEventInfo.setCreatedAt(new Date());
		customEventInfo.setCreatedPosId(userObject.getPosition().getId());
		customEventInfo.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		customEventInfo.setUpdatedById(userObject.getEmployee().getId());
				customEventInfo.setUpdatedAt(new Date());
		baseDao.save(customEventInfo);
		
		teamService.addPosition(
				userObject.getPosition().getId(),
				userObject.getEmployee().getId(), 
				IConstants.CUSTOM_EVENT_PROC,
				customEventInfo.getId());
	}

	@Override
	public CustomEventInfo getEventInfo(String id) throws AnneException {
		
		return baseDao.get(CustomEventInfo.class, id);
	}

	
	@Override
	public void updateEventInfo(CustomEventInfo customEventInfo) throws AnneException {
		CustomEventInfo oldCustomEventInfo = baseDao.get(CustomEventInfo.class, customEventInfo.getId());
		if(oldCustomEventInfo == null){
			throw new AnneException(ICustomEventService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomEventInfo.getId().equals(oldCustomEventInfo.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customEventInfo, oldCustomEventInfo);
		
		if(StringUtils.isEmpty(customEventInfo.getIs1())) {
			oldCustomEventInfo.setIs1("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs2())) {
			oldCustomEventInfo.setIs2("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs3())) {
			oldCustomEventInfo.setIs3("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs4())) {
			oldCustomEventInfo.setIs4("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs5())) {
			oldCustomEventInfo.setIs5("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs6())) {
			oldCustomEventInfo.setIs6("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs7())) {
			oldCustomEventInfo.setIs7("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs11())) {
			oldCustomEventInfo.setIs11("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs12())) {
			oldCustomEventInfo.setIs12("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs13())) {
			oldCustomEventInfo.setIs13("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs14())) {
			oldCustomEventInfo.setIs14("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs15())) {
			oldCustomEventInfo.setIs15("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs16())) {
			oldCustomEventInfo.setIs16("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs17())) {
			oldCustomEventInfo.setIs17("");
		}
		
		if(StringUtils.isEmpty(customEventInfo.getIs18())) {
			oldCustomEventInfo.setIs18("");
		}
		
		baseDao.update(oldCustomEventInfo);
	}
	
	
	@Override
	public void deleteEventInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomEventInfo where id = ? ",new Object[]{id});
	}
	
	@Override
	public CustomEventInfo getEventInfoBycode(String code) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(code);
		List<CustomEventInfo> customEventInfos = baseDao.findEntity(" from CustomEventInfo where eventCode = ? ", code);
		
		if (customEventInfos == null || customEventInfos.size() == 0) {
			return new CustomEventInfo();
		} else if (customEventInfos.size() > 1) {
			throw new AnneException(" TODO ＥＲＲＯＲ");
		}
		return customEventInfos.get(0);
	}
	
	@Override
	public IPage queryCustomEventContact(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(CustomEventContact.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		
	}

	@Override
	public void saveCustomEventContact(CustomEventContact customEventContact,UserObject userObject) throws AnneException {
		
		baseDao.save(customEventContact);
	}

	@Override
	public CustomEventContact getCustomEventContact(String id) throws AnneException {
		
		return baseDao.get(CustomEventContact.class, id);
	}

	
	@Override
	public void updateCustomEventContact(CustomEventContact customEventContact) throws AnneException {
		CustomEventContact oldCustomEventContact = baseDao.get(CustomEventContact.class, customEventContact.getId());
		if(oldCustomEventContact == null){
			throw new AnneException(ICustomEventService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomEventContact.getId().equals(oldCustomEventContact.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customEventContact, oldCustomEventContact);
		baseDao.update(oldCustomEventContact);
	}
	
	
	@Override
	public void deleteCustomEventContact(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomEventContact where id = ? ",new Object[]{id});
	}

	@Override
	public IPage queryCustomEventItems(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(CustomEventItems.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		
	}

	@Override
	public void saveCustomEventItems(CustomEventItems customEventItems,UserObject userObject) throws AnneException {
		
		baseDao.save(customEventItems);
	}

	@Override
	public CustomEventItems getCustomEventItems(String id) throws AnneException {
		
		return baseDao.get(CustomEventItems.class, id);
	}

	
	@Override
	public void updateCustomEventItems(CustomEventItems customEventItems) throws AnneException {
		CustomEventItems oldCustomEventItems = baseDao.get(CustomEventItems.class, customEventItems.getId());
		if(oldCustomEventItems == null){
			throw new AnneException(ICustomEventService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomEventItems.getId().equals(oldCustomEventItems.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customEventItems, oldCustomEventItems);
		baseDao.update(oldCustomEventItems);
	}
	
	
	@Override
	public void deleteCustomEventItems(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomEventItems where id = ? ",new Object[]{id});
	}
}
