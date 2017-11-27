package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IOrgService;
import com.ibm.kstar.api.system.permission.IPositionService;
import com.ibm.kstar.api.system.permission.entity.PositionVo;
import com.ibm.kstar.api.system.permission.entity.RolePosition;
import com.ibm.kstar.api.system.permission.entity.UserPermission;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class PositionServiceImpl implements IPositionService{

	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IOrgService orgService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@Override
	public void save(PositionVo positionVo,String roles) {
		LovMember position = new LovMember();
		LovMember org = new LovMember();
		BeanUtils.copyPropertiesIgnoreNull(positionVo, position);
		BeanUtils.copyPropertiesIgnoreNull(positionVo, org);
		if(StringUtil.isEmpty(positionVo.getParentOrgId())){
			throw new AnneException("岗位必须归属一个部门");
		}
		LovMember parentOrg = baseDao.get(LovMember.class, positionVo.getParentOrgId());
		org.setLeafFlag("Y");
		org.setParentId(positionVo.getParentOrgId());
		org.setGroupId("ORG");
		org.setOptTxt3(parentOrg.getOptTxt3());
		org.setOptTxt4(parentOrg.getOptTxt4());
		lovMemberService.save(org);
		
		LovMember approve = new LovMember();
		approve.setName(positionVo.getName());
		approve.setCode(positionVo.getCode());
		approve.setMemo(positionVo.getMemo());
		approve.setGroupId("APPROVE_POSITION_LEVEL");
		approve.setLeafFlag("N");
		
		LovMember parentPosition = baseDao.get(LovMember.class, position.getParentId());
		if(parentPosition!=null){
			approve.setParentId(parentPosition.getOptTxt2());
		}
		lovMemberService.save(approve);
		
		position.setParentId(positionVo.getParentId());
		position.setGroupId("POSITION");
		position.setOptTxt1(org.getId());
		position.setOptTxt2(approve.getId());
		lovMemberService.save(position);
		
		//更新角色
		if(StringUtils.isNotEmpty(roles)){
			baseDao.executeHQL(" delete from RolePosition rp where rp.positionId = ? ",new Object[]{org.getId()});
			Set<String> set = new HashSet<String>(Arrays.asList(roles.split(",")));
			for(String roleId : set){
				if(StringUtil.isEmpty(roleId)){
					continue;
				}
				RolePosition up = new RolePosition();
				up.setRoleId(roleId);
				up.setPositionId(org.getId());
				baseDao.save(up);
			}
		}
	}
	
	public void update(PositionVo positionVo,String roles) {
		LovMember org;
		LovMember position;
		LovMember approve;
		position = baseDao.get(LovMember.class, positionVo.getId());
		org =  corePermissionService.getOrgByPositionId(position.getId());
		
		approve = corePermissionService.getApprovePositionByPositionId(position.getId());
		
//		if(positionVo.getCode()!=null && !(positionVo.getCode().equals(position.getCode()))){
//			throw new AnneException("岗位编码不能被修改");
//		}
		String positionId = position.getId(); 
		String orgId = org.getId();
		String approveId= approve.getId();
		
		//更换部门，撤销领导
		if(!org.getParentId().equals(positionVo.getParentOrgId()) ){
			if("Y".equals(org.getOptTxt2())){
				orgService.settingLeader(org.getId());
			}
		}
		
		BeanUtils.copyPropertiesIgnoreNull(positionVo, org);
		BeanUtils.copyPropertiesIgnoreNull(positionVo, position);
		BeanUtils.copyPropertiesIgnoreNull(positionVo, approve);
		
		LovMember parentOrg = baseDao.get(LovMember.class, positionVo.getParentOrgId());
		
//		if( ("E".equals(parentOrg.getOptTxt3()) && !"E".equals(org.getOptTxt3()))  || 
//			(!"E".equals(parentOrg.getOptTxt3()) && "E".equals(org.getOptTxt3())) ){
//			throw new AnneException("请区分内部和外部组织");
//		}
		
		position.setId(positionId);
		org.setId(orgId);
		
		org.setParentId(positionVo.getParentOrgId());
		org.setGroupId("ORG");
		org.setLeafFlag("Y");
		org.setOptTxt3(parentOrg.getOptTxt3());
		org.setOptTxt4(parentOrg.getOptTxt4());
		//因为事务原因之前在撤销领导时候把领导标示已经设置为N，但是这里的岗位对象是在撤销领导之前所加载，并没有被影响到该数据，所以这里又做一次判断重新设置
		if(!org.getParentId().equals(positionVo.getParentOrgId()) ){
			org.setOptTxt2("N");
		}
		
		approve.setId(approveId);
		
		lovMemberService.update(org);
		position.setGroupId("POSITION");
		position.setOptTxt1(org.getId());
		lovMemberService.update(position);
		
//		LovMember approve = baseDao.get(LovMember.class, position.getOptTxt2());
//		approve.setName(position.getName());
//		approve.setCode(position.getCode());
//		approve.setMemo(position.getMemo());
		baseDao.update(approve);
		
		//更新角色
		baseDao.executeHQL(" delete from RolePosition rp where rp.positionId = ? ",new Object[]{org.getId()});
		Set<String> set = new HashSet<String>(Arrays.asList(roles.split(",")));
		for(String roleId : set){
			if(StringUtil.isEmpty(roleId)){
				continue;
			}
			RolePosition up = new RolePosition();
			up.setRoleId(roleId);
			up.setPositionId(org.getId());
			baseDao.save(up);
		}
		
	}

	@Override
	public void delete(String id) {
		LovMember lov = baseDao.get(LovMember.class, id);
		if(lov ==null){
			return ;
		}
		
		Long count = baseDao.findUniqueEntity("select count(*) from LovMember m where m.parentId = ? ",id);
		if(count > 0){
			throw new AnneException("岗位层级下包含子岗位不能删除");
		}
		
		//optTxt1;组织层级中岗位为叶子节点，所以不加判断。
		
		count = baseDao.findUniqueEntity("select count(*) from LovMember m where m.parentId = ? ",lov.getOptTxt2());
		if(count > 0){
			throw new AnneException("审批层级下包含有子岗位，不能删除");
		}
		
		baseDao.deleteById(LovMember.class, lov.getOptTxt1());
		baseDao.deleteById(LovMember.class, lov.getOptTxt2());
		baseDao.deleteById(LovMember.class,id);
		baseDao.executeHQL("delete from UserPermission where type = 'P' and memberId = ? ", new Object[]{id});  //删除关联数据
	}
	
	@Override
	public IPage query(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select new com.ibm.kstar.action.system.permission.position.PositionVo(position,org,approve) from LovMember position , LovMember porg, LovMember org , LovMember approve where position.optTxt1=porg.id and position.optTxt2 = approve.id and porg.parentId = org.id ");
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			hql.append(" and (position.name like ? or position.code like ? ) ");
			args.add("%"+searchKey+"%");
			args.add("%"+searchKey+"%");
		}
		
		String parentId = condition.getStringCondition("parentId");
		if(searchKey !=parentId){
			hql.append(" and position.path like ? ");
			args.add("%"+parentId+"%");
		}
		
		hql.append("order by position.level");
		
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public IPage queryApprovePage(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select new com.ibm.kstar.action.system.permission.position.PositionVo(position,org,approve) from LovMember position ,LovMember approve, LovMember porg, LovMember org where position.optTxt1=porg.id and porg.parentId = org.id and position.optTxt2 = approve.id ");
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			hql.append(" and (position.name like ? or position.code like ? ) ");
			args.add("%"+searchKey+"%");
			args.add("%"+searchKey+"%");
		}
		
		String parentId = condition.getStringCondition("parentId");
		if(parentId !=null){
			hql.append(" and approve.path like ? ");
			args.add("%"+parentId+"%");
		}
		
		hql.append("order by approve.level");
		
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public IPage queryEmployeePage(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select e from Employee e,UserPermission up where e.id = up.userId and up.type = 'P' ");
		String positionId = condition.getStringCondition("positionId");
		if(positionId !=null){
			hql.append(" and up.memberId = ?  ");
			args.add(positionId);
		}
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public void updateApprove(com.ibm.kstar.action.system.permission.position.PositionVo lov) {
		lovMemberService.update(lov.getApprove());
	}

	@Override
	public void configEmployee(String positionId,String[] employees){
		baseDao.executeHQL(" delete from UserPermission up where up.memberId = ? and up.type = 'P' ",new Object[]{positionId});
		Set<String> set = new HashSet<String>(Arrays.asList(employees));
		for(String employeeId : set){
			if(StringUtil.isEmpty(employeeId)){
				continue;
			}
			UserPermission up = new UserPermission();
			up.setMemberId(positionId);
			up.setUserId(employeeId);
			up.setType("P");
			baseDao.save(up);
		}
	}
	
}
