package org.xsnake.xflow.api;

import java.util.List;
import java.util.Map;

import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.model.Task;

public interface ITaskService {

	/**
	 * 完成任务,该方法支持一个任务有多个流转路径，调用前需要获取路径列表
	 * @param taskId
	 * @param transitionId
	 * @param participant
	 * @param comment
	 * @param varmap
	 * @return
	 */
	boolean complete(String taskId,String transitionId,Participant participant,String comment,Map<String,String> varmap);
	
	boolean complete(String taskId,Participant participant,String comment,Map<String,String> varmap);
	
	boolean complete(String taskId,Participant participant,String comment);
	
	boolean complete(String taskId,Participant participant,String comment,Participant assignee);
	
	void reject(String taskId, String toActivityId,Participant operator, String comment);
	
	List<RejectPath> getRejectPathList(String taskId);
	
	/**
	 * 转交任务
	 * @param taskId
	 * @param participant
	 */
	void transfer(String taskId,Participant operator, Participant assignee,String comment);
	
	/**
	 * 复制任务
	 * @param taskId
	 * @param participantList
	 */
	void support(String taskId,Participant operator, List<Participant> participantList,String comment);
	
	IPage myTask(Participant p,int size,int page);
	
	public IPage myTask(List<Participant> plist,int size,int pageno);
	
	public IPage allTask(Map<String,String> condition,int size,int pageno);
	
	Task getTask(String taskId);
	
	List<Task> getTaskByProcessInstanceId(String processInstanceId);
	
}
