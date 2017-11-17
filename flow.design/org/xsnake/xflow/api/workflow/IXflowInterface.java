package org.xsnake.xflow.api.workflow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.Participant;

public abstract class IXflowInterface {
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	ITaskService taskService; 
	
	public void onCompletex(String businessKey, String flowModule, String processInstanceId, String comment){
		try{
			onComplete(businessKey, flowModule, processInstanceId, comment);
			baseDao.executeSQLX("update XFLOW_CALLBACK set STATUS = 'Y' where process_Instance_Id = ? " , new Object[]{processInstanceId});
		}catch(Exception e){
			PrintStream ps = null;
			try{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ps = new PrintStream(out);
				e.printStackTrace(ps);
				String errorStackTrace = new String(out.toByteArray());
				errorStackTrace = errorStackTrace.substring(0,3500);
				try{
					baseDao.executeSQLX("update XFLOW_CALLBACK set ERROR_MESSAGE = ? , STATUS = 'E' where process_Instance_Id = ? " , new Object[]{errorStackTrace,processInstanceId});
				}catch(Exception ex){
					baseDao.executeSQLX("update XFLOW_CALLBACK set ERROR_MESSAGE = ? , STATUS = 'E' where process_Instance_Id = ? " , new Object[]{e.getMessage(),processInstanceId});
				}
				taskService.rollback(processInstanceId);
				throw e;
			}finally{
				ps.close();
			}
		}
	}
	
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
