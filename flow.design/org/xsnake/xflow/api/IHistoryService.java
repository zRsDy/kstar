package org.xsnake.xflow.api;

import java.util.List;
import java.util.Map;

import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;

public interface IHistoryService {

	List<HistoryActivityInstance> findHistoryActivityInstance(String processInstanceId);
	
	List<HistoryProcessInstance> findHistoryProcessInstanceList(String module,String businessKey);

	IPage findProcessInstanceByList(List<String> businessKeyList, int size, int pageno);

	HistoryProcessInstance findLastProcessInstance(String module,String businessKey);
	
	/** 查询所有运行中的流程
	 * 查询键值：module，application
	 * @param condition  
	 * @param size
	 * @param pageno
	 * @return
	 */
	IPage findProcessInstance(Map<String,String> condition,int size,int pageno);

	/**
	 * 历史参与过的流程
	 * @param p
	 * @param size
	 * @param page
	 * @return
	 */
	IPage history(Participant p, Map<String,Object> condition,int size, int pageno);
	
	HistoryProcessInstance get(String processInstanceId);
}
