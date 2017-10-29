package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IUserPermissionService;
import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.api.system.permission.entity.Role;
import com.ibm.kstar.api.system.permission.entity.UserPermission;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class UserPermissionServiceImpl implements IUserPermissionService {

	@Autowired
	BaseDao baseDao;
	
	@Override
	public List<Position> getPositionList(String userId) {
		return getOrgPositionList(userId);
//		List<Position> result = new ArrayList<Position>();
//		List<LovMember> list = baseDao.findEntity(" select m from LovMember m , UserPermission u where u.userId = ? and u.type = 'P' and m.id = u.memberId ",userId);
//		for(LovMember m : list){
//			Position p = new Position(m);
//			result.add(p);
//		}
//		return result;
	}
	
	public List<Position> getOrgPositionList(String userId) {
		List<Position> result = new ArrayList<Position>();
		List<LovMember> list = baseDao.findEntity(" select o from LovMember m ,LovMember o, UserPermission u where u.userId = ? and u.type = 'P' and m.id = u.memberId and m.optTxt1 = o.id and m.groupId = 'POSITION' and o.groupId = 'ORG' ",userId);
		for(LovMember m : list){
			Position p = new Position(m);
			result.add(p);
		}
		return result;
	}

	@Override
	public List<Role> getRoleList(String userId) {
		List<Role> result = new ArrayList<Role>();
		List<LovMember> list = baseDao.findEntity(" select m from LovMember m , UserPermission u where u.userId = ? and u.type = 'R' and m.id = u.memberId ",userId);
		for(LovMember m : list){
			Role p = new Role(m);
			result.add(p);
		}
		return result;
	}

	@Override
	public List<UserPermission> getUserPermissionList(String userId) {
		String hql = "select u from UserPermission u,Employee e where u.userId = e.id and e.id = ?";
		return baseDao.findEntity(hql, userId);
	}

}
