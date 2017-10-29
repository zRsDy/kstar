package com.ibm.kstar.impl.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.team.Team;

import java.util.List;


@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class TeamServiceImpl implements ITeamService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public void config(String positionIds, String employeeIds, String businessType, String businessId) {
		baseDao.executeHQL(" delete from Team t where t.businessType = ? and t.businessId = ? " , new Object[]{businessType,businessId});
		String[] pIds = positionIds.split(",");
		String[] eIds = employeeIds.split(",");
		for(int i=0;i< pIds.length;i++){
			String positionId = pIds[i];
			String employeeId = eIds[i];
			if(StringUtil.isNotEmpty(positionId)){
				Team team = new Team();
				team.setBusinessId(businessId);
				team.setBusinessType(businessType);
				team.setPositionId(positionId);
				team.setMasterEmployeeId(employeeId);
				baseDao.save(team);
			}
		}
	}

	@Override
	public void addPosition(String positionId, String employeeId, String businessType, String businessId) {
		String hql = "from Team t where t.positionId = ? and t.masterEmployeeId = ? and t.businessType= ? and t.businessId= ?";
		List<Team> teams = baseDao.findEntity(hql, new Object[]{positionId,employeeId,businessType,businessId});
		if(teams != null && teams.size() > 0 ){
			return;
		}
		Team team = new Team();
		team.setBusinessId(businessId);
		team.setBusinessType(businessType);
		team.setPositionId(positionId);
		team.setMasterEmployeeId(employeeId);
		baseDao.save(team);
	}

	/**
	 *
	 * @param sourceBusinessId 被复制业务单据的id
	 * @param sourceBusinessType 被复制业务单据的业务类型
	 * @param targetBusinessId 要复制为：
	 * @param targetBusinessType 要复制为：
	 */
	@Override
	public void copyPosition(String sourceBusinessId, String sourceBusinessType, String targetBusinessId, String targetBusinessType,String creater) {
		List<Team> teams = baseDao.findEntity("select t from Team t where t.businessType = ? and t.businessId = ?" +
				" and masterEmployeeId <> ? ",
				new Object[]{sourceBusinessType,sourceBusinessId,creater});

//		"select t from Team t where t.businessType = ? and t.businessId = ?" +
//				" and masterEmployeeId <> ? " +
//				" and exists ( select 1 from UserPermission p where p.type = 'P' and p.memberId = t.positionId " +
//				"and p.userId = ? ) "

		for (Team t: teams) {
			Team team = new Team();
			team.setBusinessId(targetBusinessId);
			team.setBusinessType(targetBusinessType);
			team.setPositionId(t.getPositionId());
			team.setMasterEmployeeId(t.getMasterEmployeeId());
			baseDao.save(team);
		}
	}
	
	/**
	 * 复制Position不带权限
	 * @param sourceBusinessId 被复制业务单据的id
	 * @param sourceBusinessType 被复制业务单据的业务类型
	 * @param targetBusinessId 要复制为：
	 * @param targetBusinessType 要复制为：
	 */
	@Override
	public void copyPositionNoAuth(String sourceBusinessId, String sourceBusinessType, String targetBusinessId, String targetBusinessType,String creater) {
		List<Team> teams = baseDao.findEntity("select t from Team t where t.businessType = ? and t.businessId = ?" ,
				new Object[]{sourceBusinessType,sourceBusinessId});
 
		for (Team t: teams) {
			Team team = new Team();
			team.setBusinessId(targetBusinessId);
			team.setBusinessType(targetBusinessType);
			team.setPositionId(t.getPositionId());
			team.setMasterEmployeeId(t.getMasterEmployeeId());
			baseDao.save(team);
		}
	}
	
	/**
	 * 根据businessId, businessType 删除Position
	 * @param businessId 被删除业务单据的id
	 * @param businessType 被删除业务单据的业务类型 ：
	 */
	@Override
	public void deletePosition(String businessId, String businessType) {
		baseDao.executeHQL("delete Team t where t.businessType = ? and t.businessId = ?" ,
				new Object[]{businessType,businessId});
	}
	
	public void deletePositionByEmployee(String businessId, String businessType,String employeeId) {
		baseDao.executeHQL("delete Team t where t.businessType = ? and t.businessId = ? and t.masterEmployeeId = ? " ,
				new Object[]{businessType,businessId,employeeId});
	}
	/**
	 * 
	 * getTeamByBusinessId:(根据业务ID查询销售团队). <br/> 
	 * 
	 * @author liming 
	 * @param businessId 业务ID
	 * @return 
	 * @since JDK 1.7
	 */
	@Override
	public Team getTeamByBusinessId(String businessId) {
		List<Team> teams = baseDao.findEntity("select t from Team t where t.businessId = ? order by t.id asc" ,
				new Object[]{businessId});
		if(teams == null || teams.size() <= 0){
			return null;
		}
		return teams.get(0);
	}

	@Override
	public List<Team> getAllTeamByBusinessId(String businessId) {
		List<Team> teams = baseDao.findEntity("select t from Team t where t.businessId = ? order by t.id asc" ,
				new Object[]{businessId});
		if(teams == null || teams.size() <= 0){
			return null;
		}
		return teams;
	}
}
