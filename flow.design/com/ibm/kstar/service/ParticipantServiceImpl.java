package com.ibm.kstar.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.xflow.api.Participant;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;

@Service
@Transactional(readOnly = true)
public class ParticipantServiceImpl implements IParticipantService {

	@Autowired
	BaseDao baseDao;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@Override
	public List<Participant> getCreatorDirectLeadership(String id) {
		List<Participant> result = new ArrayList<Participant>();
		LovMember position = corePermissionService.getApprovePositionByPositionId(id);
//		LovMember position = baseDao.get(LovMember.class, id);
		
		if(position == null){
			return result;
		}
		
		if(position.getParentId()!=null){
			LovMember parentPosition = baseDao.get(LovMember.class,position.getParentId());
			parentPosition = corePermissionService.getOrgByApprovePositionId(parentPosition.getId());
			result.add(new Participant(parentPosition.getId(), parentPosition.getName(), "POSITION"));
		}
		return result;
	}

	//获得用户的层级领导
	public List<Participant> getByLevel(String id,int level){
		if(level < 1){
			return new ArrayList<Participant>();
		}
		List<Participant> result = new ArrayList<Participant>();
//		LovMember position = baseDao.get(LovMember.class,id);
		LovMember position = corePermissionService.getApprovePositionByPositionId(id);
		
		if(position == null){
			return result;
		}
		
		int plevel = 0;
		try{
			plevel = Integer.parseInt(position.getOptTxt5());
		}catch(Exception e){
			plevel = position.getLevel();
		}
		
		if(plevel <= level){
			return result;
		}
		
		if(position.getParentId()!=null){
			getParentPosition(level, result, position); 
		}
		return result;
	}

	private void getParentPosition(int level, List<Participant> result, LovMember position) {
		LovMember parentPosition = baseDao.get(LovMember.class,position.getParentId());
		if(parentPosition == null){
			return ;
		}
		int plevel = 0;
		try{
			plevel = Integer.parseInt(parentPosition.getOptTxt5());
			
		}catch(Exception e){
			plevel = parentPosition.getLevel();
		}
		
		if(plevel == level){
			LovMember org = corePermissionService.getOrgByApprovePositionId(parentPosition.getId());
			result.add(new Participant(org.getId(), org.getName(), "POSITION"));
			return;
		}else{
			getParentPosition(level,result,parentPosition);
		}
	}

}
