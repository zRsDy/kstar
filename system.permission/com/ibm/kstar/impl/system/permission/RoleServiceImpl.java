package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IRoleService;
import com.ibm.kstar.api.system.permission.entity.RolePermission;
import com.ibm.kstar.api.system.permission.entity.RolePosition;
import com.ibm.kstar.api.system.permission.entity.UserPermission;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RoleServiceImpl implements IRoleService {

	@Autowired
	BaseDao baseDao;

	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Override
	public void configPermission(String roleId, String[] permissions) {
		baseDao.executeHQL(" delete from RolePermission rp where rp.roleId = ? ",new Object[]{roleId});
		Set<String> set = new HashSet<String>(Arrays.asList(permissions));
		for(String permissionId : set){
			if(StringUtil.isEmpty(permissionId)){
				continue;
			}
			RolePermission rp = new RolePermission();
			rp.setRoleId(roleId);
			rp.setPermissionId(permissionId);
			baseDao.save(rp);
		}
	}
	
	public void configEmployee(String roleId,String[] employees){
		baseDao.executeHQL(" delete from UserPermission up where up.memberId = ? and up.type = 'R' ",new Object[]{roleId});
		Set<String> set = new HashSet<String>(Arrays.asList(employees));
		for(String employeeId : set){
			if(StringUtil.isEmpty(employeeId)){
				continue;
			}
			UserPermission up = new UserPermission();
			up.setMemberId(roleId);
			up.setUserId(employeeId);
			up.setType("R");
			baseDao.save(up);
		}
	}
	
	public void configPosition(String roleId,String[] positions){
		baseDao.executeHQL(" delete from RolePosition rp where rp.roleId = ? ",new Object[]{roleId});
		Set<String> set = new HashSet<String>(Arrays.asList(positions));
		for(String positionId : set){
			if(StringUtil.isEmpty(positionId)){
				continue;
			}
			RolePosition up = new RolePosition();
			up.setRoleId(roleId);
			up.setPositionId(positionId);
			baseDao.save(up);
		}
	}

	@Override
	public void save(LovMember lovMember) {
		if(lovMember.getParentId()!=null){
			LovMember parentRole = baseDao.get(LovMember.class, lovMember.getParentId());
			if(parentRole !=null && "Y".equals(parentRole.getLeafFlag())){
				throw new AnneException("不能再角色下创建");
			}
		}
		lovMemberService.save(lovMember);
	}

	@Override
	public IPage queryEmployeePage(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select e from Employee e,UserPermission up where e.id = up.userId and up.type = 'R' ");
		String roleId = condition.getStringCondition("roleId");
		if(roleId !=null){
			hql.append(" and up.memberId = ?  ");
			args.add(roleId);
		}
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());

	}

	@Override
	public void delete(String id) {
		List<UserPermission> list = baseDao.findEntity(" from UserPermission up where up.memberId = ? and up.type = 'R' ",new Object[]{id});
		if(list.size() > 0 ){
			throw new AnneException("该角色下还有用户，不能被删除");
		}
		
		List<LovMember> list2 =baseDao.findEntity(" from LovMember l where l.path like ? and l.id != ? " ,new Object[]{"%"+id+"%",id});
		if( list2.size() > 0 ){
			throw new AnneException("该目录下还有角色，不能被删除");
		}
		
		lovMemberService.delete(id);
	}

}
