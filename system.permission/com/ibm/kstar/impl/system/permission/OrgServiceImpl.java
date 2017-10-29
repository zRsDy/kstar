package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IOrgService;
import com.ibm.kstar.api.system.permission.IPositionService;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class OrgServiceImpl implements IOrgService {

	@Autowired
	BaseDao baseDao;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	public IPage getOrgPage(PageCondition condition) {
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		hql.append( " select new com.ibm.kstar.api.system.permission.OrgPositionVo(o,p) from LovMember o ,LovMember p where o.id = p.optTxt1 and o.groupId = 'ORG' and p.groupId = 'POSITION' ");
		hql.append(" and o.deleteFlag = 'N'  ");
		if (StringUtil.isNotEmpty(parentId)) {
			hql.append(" and o.path like ? ");
			args.add("%" + parentId + "%");
		}
		if (StringUtil.isNotEmpty(searchKey)) {
			hql.append(" and ( o.name like ? or o.code like ? ) ");
			args.add("%" + searchKey + "%");
			args.add("%" + searchKey + "%");
		}
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Autowired
	IPositionService positionService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@Override
	public void delete(String id) {
		LovMember org = baseDao.get(LovMember.class, id);
		if("Y".equals(org.getLeafFlag())){
			LovMember position = corePermissionService.getPositionByOrgId(id);
			positionService.delete(position.getId());
		}else{
			Long count = baseDao.findUniqueEntity("select count(*) from LovMember m where m.parentId = ? ",id);
			if(count > 0){
				throw new AnneException("部门下包含有子部门或者岗位，不能删除");
			}
			baseDao.delete(org);
		}
	}
	
	@Override
	public void settingLeader(String id) {
		LovMember org = baseDao.get(LovMember.class, id);
		if(!"Y".equals(org.getLeafFlag())){
			throw new AnneException("无法设置一个部门为领导岗位");
		}
		
		if("Y".equals(org.getOptTxt2())){
			org.setOptTxt2("N");
		}else{
			org.setOptTxt2("Y");
		}
		baseDao.update(org);
//		baseDao.executeHQL(" update LovMember p set p.optTxt2 = 'N' where p.groupId = 'ORG' and p.leafFlag = 'Y' and p.parentId = ? ",new Object[]{org.getParentId()});
//		LovMember parentOrg = baseDao.get(LovMember.class, org.getParentId());
//		//设置领导
//		if(!"Y".equals(org.getOptTxt2())){
//			baseDao.executeHQL(" update LovMember p set p.optTxt2 = 'Y' where p.id = ? ",new Object[]{org.getId()});
//			if(parentOrg != null){
//				parentOrg.setOptTxt2(org.getId());
//			}
//			baseDao.update(parentOrg);
//		}else{
//			baseDao.executeHQL(" update LovMember p set p.optTxt2 = 'N' where p.id = ? ",new Object[]{org.getId()});
//			if(parentOrg != null){
//				parentOrg.setOptTxt2(null);
//			}
//			baseDao.update(parentOrg);
//		}
	}
	
	public void update(LovMember org){
		lovMemberService.update(org);
//		if(org.getParentId()!=null && !"ROOT".equals(org.getParentId())){
//			LovMember parentOrg = baseDao.get(LovMember.class, org.getParentId());
//			org.setOptTxt3(parentOrg.getOptTxt3());
//			org.setOptTxt4(parentOrg.getOptTxt4());
//			baseDao.update(org);
//		}
	}
	
	public void save(LovMember org){
		lovMemberService.save(org);
	}

	@Override
	public IPage queryEmployeePage(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select e from Employee e,UserPermission up,LovMember p ,LovMember po where po.id = p.optTxt1  and p.id = up.memberId and e.id = up.userId and up.type = 'P' ");
		String orgId = condition.getStringCondition("orgId");
		if(orgId !=null){
			hql.append(" and po.path like ?  ");
//			args.add(positionId);
			args.add("%" + orgId + "%");
		}
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public List<LovMember> getOrgGroupList(Condition condition) {
		String sql = "select  * from apps.cux_sales_dept_v@crm_erp a";
		List<Object[]> list = baseDao.findBySql(sql);
		List<LovMember> lovMembers = new ArrayList<LovMember>();
		
		for(Object[] obj : list){
			LovMember lovMember = new LovMember();
			
			lovMember.setId(StringUtil.strnull(obj[1]));
			lovMember.setCode(StringUtil.strnull(obj[1]));
			lovMember.setName(StringUtil.strnull("["+obj[1]+"]"+obj[0]));
			
			lovMembers.add(lovMember);
		}
		
		return lovMembers;
	}

}
