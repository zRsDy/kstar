package com.ibm.kstar.api.system.permission;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.entity.PositionVo;

public interface IPositionService {

	void save(PositionVo positionVo, String roles);

	void update(PositionVo positionVo,String roles);
	
	void delete(String id);

	IPage query(PageCondition condition);

	IPage queryApprovePage(PageCondition condition);

	IPage queryEmployeePage(PageCondition condition);

	void updateApprove(com.ibm.kstar.action.system.permission.position.PositionVo lov);

	void configEmployee(String positionId, String[] employees);

}
