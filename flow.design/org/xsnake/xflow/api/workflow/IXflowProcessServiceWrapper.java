package org.xsnake.xflow.api.workflow;

import java.util.Map;

import org.xsnake.xflow.api.model.ProcessInstance;

import com.ibm.kstar.api.system.permission.UserObject;

public interface IXflowProcessServiceWrapper {
	
	/**
	 * 
	 * @param module	流程编码
	 * @param application	应用编码
	 * @param title	流程标题
	 * @param businessKey	表单主键
	 * @param userObject	操作人信息
	 * @param varmap	流程参数
	 * @return
	 */
	ProcessInstance start(String module,String application,String title,String businessKey,UserObject userObject,Map<String,String> varmap);
	
	/**
	 * 
	 * @param module	流程编码
	 * @param businessKey	表单主键
	 * @param userObject	操作人信息
	 * @param varmap	流程参数
	 * @return
	 */
	ProcessInstance start(String module,String businessKey,UserObject userObject,Map<String,String> varmap);

	
}
