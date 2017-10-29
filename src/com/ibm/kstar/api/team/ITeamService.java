package com.ibm.kstar.api.team;

import com.ibm.kstar.entity.team.Team;

import java.util.List;

public interface ITeamService {

	void config(String positionIds,String employeeIds,String businessType,String businessId);
	
	void addPosition(String positionIds,String employeeIds,String businessType,String businessId);

	void copyPosition(String sourceBusinessId,String sourcrBusinessType,String targetBusinessId,String businessType,String creater);
	
	void copyPositionNoAuth(String sourceBusinessId, String sourceBusinessType, String targetBusinessId, String targetBusinessType,String creater) ;
	
	void deletePosition(String businessId, String businessType);
	
	void deletePositionByEmployee(String businessId, String businessType,String employeeId);
	/**
	 * 
	 * getTeamByBusinessId:(根据业务ID查询销售团队). <br/> 
	 * 
	 * @author liming 
	 * @param businessId 业务ID
	 * @return 
	 * @since JDK 1.7
	 */
	Team getTeamByBusinessId(String businessId);
	List<Team> getAllTeamByBusinessId(String businessId);
}
