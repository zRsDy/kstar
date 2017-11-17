package com.ibm.kstar.impl.common.permission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.common.permission.ICommonPermissionService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.common.permission.BusinessPermissionRel;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CommonPermissionServiceImpl implements ICommonPermissionService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public List<LovMember> getPermissionList(String id, String groupId) {
		String hql = " select o from BusinessPermissionRel r , LovMember o ,LovMember p where r.businessId = p.code and r.orgId = o.id and p.groupId = ? and o.groupId='ORG' and o.deleteFlag = 'N' and p.code = ? ";
		return baseDao.findEntity(hql,new Object[]{groupId, id});
	}
	
	@Override
	public IPage getPermissionPage(PageCondition condition) {
		if(StringUtil.isEmpty(condition.getStringCondition("businessId"))){
			return new PageImpl(new ArrayList<>(), 20, 1, 0);
		}
		String hql = " select o from BusinessPermissionRel r , LovMember o ,LovMember p where r.businessId = p.code and r.orgId = o.id and p.groupId = ? and o.groupId='ORG' and o.deleteFlag = 'N' and p.code = ? ";
		return baseDao.search(hql ,new Object[]{condition.getStringCondition("groupId"), condition.getStringCondition("businessId")},condition.getRows(), condition.getPage());
	}

	@Override
	public List<LovMember> getAllPermissionList() {
		List<LovMember> list = baseDao.findEntity(" select o from LovMember o where o.groupId = 'ORG' and o.deleteFlag = 'N' ");
		return list;
	}

	@Override
	public void configPermission(String businessId, String[] permissions) {
		baseDao.executeHQL(" delete from BusinessPermissionRel rp where rp.businessId = ? ",new Object[]{businessId});
		for(String permissionId : permissions){
			if(StringUtil.isEmpty(permissionId)){
				continue;
			}
			BusinessPermissionRel rp = new BusinessPermissionRel();
			rp.setBusinessId(businessId);
			rp.setOrgId(permissionId);
			baseDao.save(rp);
		}
	}

}
