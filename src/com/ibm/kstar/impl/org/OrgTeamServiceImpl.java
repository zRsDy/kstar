package com.ibm.kstar.impl.org;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.team.Orgs;
import com.ibm.kstar.entity.team.Team;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class OrgTeamServiceImpl implements IOrgTeamService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public LovMember getPrimaryOrg(String businessId, String businessType) {
		String hql = " select o from Orgs r , LovMember o  where r.orgId = o.id and r.businessType = ? and o.groupId='ORG' and o.leafFlag='N' and o.deleteFlag = 'N' and r.businessId = ? and r.primaryOrgId is not null and r.primaryOrgFlag = 'Y'";
		 List<LovMember> l = baseDao.findEntity(hql,new Object[]{businessType, businessId});
		 
		 if(l != null && l.size() > 0 ) {
			 return l.get(0);
		 }
		 
		 return new LovMember();
	}
	
	@Override
	public List<LovMember> getOrgList(String businessId, String businessType) {
		String hql = " select o from Orgs r , LovMember o  where r.orgId = o.id and r.businessType = ? and o.groupId='ORG' and o.leafFlag='N' and o.deleteFlag = 'N' and r.businessId = ? ";
		return baseDao.findEntity(hql,new Object[]{businessType, businessId});
	}
	
	@Override
	public IPage page(PageCondition condition) {
		if(StringUtil.isEmpty(condition.getStringCondition("businessId"))){
			return new PageImpl(new ArrayList<>(), 20, 1, 0);
		}
		String hql = " select o from Orgs r , LovMember o  where r.orgId = o.id and r.businessType = ? and o.groupId='ORG' and o.leafFlag='N' and o.deleteFlag = 'N' and r.businessId = ? ";
		return baseDao.search(hql ,new Object[]{condition.getStringCondition("businessType"), condition.getStringCondition("businessId")},condition.getRows(), condition.getPage());
	}

	@Override
	public List<LovMember> getAllOrgList() {
		List<LovMember> list = baseDao.findEntity(" select o from LovMember o where o.groupId = 'ORG' and o.leafFlag='N' and o.deleteFlag = 'N' ");
		return list;
	}

	@Override
	public void config(String businessId, String businessType, String[] permissions, String primaryOrgId) {
		baseDao.executeHQL(" delete from Orgs r where r.businessId = ? and r.primaryOrgId is null and r.primaryOrgFlag is null ",new Object[]{businessId});
		for(String permissionId : permissions){
			if(StringUtil.isEmpty(permissionId) || StringUtils.equals(primaryOrgId, permissionId)){
				continue;
			}
			Orgs orgs = new Orgs();
			orgs.setBusinessId(businessId);
			orgs.setBusinessType(businessType);
			orgs.setOrgId(permissionId);
			baseDao.save(orgs);
		}
	}
	
	@Override
	public void configPrimaryOrg(String businessId, String businessType, String permissionId) {
		Orgs orgs = new Orgs();
		orgs.setBusinessId(businessId);
		orgs.setBusinessType(businessType);
		orgs.setOrgId(permissionId);
		orgs.setPrimaryOrgId(permissionId);
		orgs.setPrimaryOrgFlag("Y");
		baseDao.save(orgs);
	}

	@Override
	public void copyPrimaryOrg(String sourceBusinessId, String sourceBusinessType, String targetBusinessId, String targetBusinessType,String creater){
		String hql= "select t from Orgs t where t.businessType = ? and t.businessId = ?" ;
//				" and exists ( select 1 from UserPermission p where p.type = 'P' and p.memberId = t.positionId " + "and p.userId = ? ) ";
		List<Orgs> orgs = baseDao.findEntity(hql, new Object[]{sourceBusinessType,sourceBusinessId});
		for(Orgs org: orgs){
			Orgs o = new Orgs();
			o.setBusinessId(targetBusinessId);
			o.setBusinessType(targetBusinessType);
			o.setOrgId(org.getOrgId());
			o.setPrimaryOrgId(org.getPrimaryOrgId());
			o.setPrimaryOrgFlag(org.getPrimaryOrgFlag());
			baseDao.save(o);
		}
		
	}
	
	/**
	 * 根据businessId, businessType 删除Org
	 * @param businessId 被删除业务单据的id
	 * @param businessType 被删除业务单据的业务类型 ：
	 */
	@Override
	public void deleteOrg(String businessId, String businessType) {
		baseDao.executeHQL("delete Orgs t where t.businessType = ? and t.businessId = ?" ,
				new Object[]{businessType,businessId});
	}

}
