package org.xsnake.xflow.api;

import org.xsnake.xflow.api.model.ProcessDefinition;

/**
 * 流程定义相关服务接口
 * @author Jerry.Zhao
 *
 */
public interface IDefinitionService {

	String create(String xml);

	void delete(String processDefinitionId);

	String getXML(String processDefinitionId);

	String getLastVersionXML(String module);
	
	ProcessDefinition getLastVersion(String module);
	
	ProcessDefinition getDefinition(String processDefinitionId);
	
}
