package org.xsnake.xflow.api;

import java.util.List;

import org.xsnake.xflow.api.model.Authorization;

public interface ISupportService {

	/**
	 * 按流程授权给他人，可以将该流程类型的任务全部授权给他人
	 * @param module
	 * @param owner
	 * @param assignee
	 */
	void authorizationByFlow(String module,Participant owner,Participant assignee);
	
	/**
	 * 按应用授权给他人
	 * @param module
	 * @param owner
	 * @param assignee
	 */
	void authorizationByApplication(String module,Participant owner,Participant assignee);
	
	/**
	 * 查询是否有授权，未授权返回null，反之返回授权人
	 * @param type
	 * @param module
	 * @param owner
	 * @return
	 */
	Participant getAuthorizationAssignee(String type,String module,Participant owner);
	
	/**
	 * 查询用户被授权的项
	 * @param participant
	 * @return
	 */
	List<Authorization> authorizationIn(Participant participant);
	
	/**
	 * 查询用户授权给他人的项
	 * @param participant
	 * @return
	 */
	List<Authorization> authorizationOut(Participant participant);
	
}
