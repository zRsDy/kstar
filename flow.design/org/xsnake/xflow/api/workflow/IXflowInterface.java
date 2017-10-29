package org.xsnake.xflow.api.workflow;

import org.xsnake.xflow.api.Participant;

public abstract class IXflowInterface {
	
	/**
	 * 流程审批结束回调方法
	 * @param businessKey
	 * @param flowModule
	 * @param processInstanceId
	 */
	public abstract void onComplete(String businessKey, String flowModule, String processInstanceId, String comment);
	
	/**
	 * 流程异常关闭，销毁回调方法
	 * @param businessKey
	 * @param flowModule
	 * @param processInstanceId
	 */
	public abstract void onDestory(String businessKey,String flowModule,String processInstanceId,String comment);
	
	public abstract void onReject(String businessKey,String flowModule,String processInstanceId,String comment);
	
	public abstract void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment);
	
	/**
	 * 流程处理地址
	 * @return
	 */
	public abstract String processPath();
	
	public abstract String viewPath();
	
	public abstract String mobileView(String id);

	public abstract void onEvent(String event,String businessKey,String flowModule,String processInstanceId,String comment);
	
	public boolean newType(){
		return false;
	}
	
}
