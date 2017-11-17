package org.xsnake.xflow.api;

import java.util.List;
import java.util.Map;

import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;

/**
 * 流程处理相关服务接口
 * @author Jerry.Zhao
 *
 */
public interface IProcessService {
	
	
	/**
	 * 
	 * @param module   流程代码
	 * @param application  应用代码
	 * @param title   业务标题
	 * @param businessKey  业务主键
	 * @param participant  流程创建人
	 * @param varmap   业务参数
	 * @return
	 * @throws Exception  一个业务只能同时开启一次对应流程，否则会抛出异常
	 */
	ProcessInstance start(String module,String application,String title,String businessKey,Participant participant,Map<String,String> varmap);
	
	
	ProcessInstance start(String module,String businessKey,Participant participant,Map<String,String> varmap);
	/**
	 * 获取流程实例信息，通过业务ID
	 * @param businessKey
	 * @return
	 */
	List<HistoryProcessInstance> getListByBusinessKey(String application,String businessKey);

	/**
	 * 获得运行中的流程实例
	 * @param module
	 * @param businessKey
	 * @return
	 */
	ProcessInstance getByBusinessKey(String application,String businessKey);

	
	ProcessInstance get(String processInstanceId);
	
	/**
	 * 提前关闭流程实例
	 * @param processInstanceId
	 * @param participant 操作人
	 */
	void close(String processInstanceId,Participant participant,String comment);
	
	void closeByBusinessKey(String application,String businessKey,Participant participant,String comment);
	
	void setBusinessVariable(String application,String businessKey,Participant participant,Map<String,String> varmap);
	
	void setBusinessVariable(String processInstanceId,Participant participant,Map<String,String> varmap);
	
	Map<String,String> getBusinessVariable(String processInstanceId);
}
