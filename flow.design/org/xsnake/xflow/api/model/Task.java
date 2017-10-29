package org.xsnake.xflow.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "XFLOW_TASK")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Task(){}
	
	public Task(List<Object> objs){
		this.id = (String)objs.get(0);
		this.processInstanceId = (String)objs.get(1);
		this.title = (String)objs.get(2);
		this.assigneeName = (String)objs.get(3);
		this.assigneeId = (String)objs.get(4);
		this.assigneeType = (String)objs.get(5);
		this.status = (String)objs.get(6);
		this.startTime = (Date)objs.get(7);
		this.activityId = (String)objs.get(8);
		this.activityName = (String)objs.get(9);
		this.activityType = (String)objs.get(10);
		this.businessKey = (String)objs.get(11);
		this.srcTransitionId= (String)objs.get(12);
		this.srcTransitionName= (String)objs.get(13);
		this.srcActivityId= (String)objs.get(14);
		this.srcActivityName= (String)objs.get(15);
		this.runtimeAssigneFlag = (String)objs.get(16);
		this.module = (String)objs.get(17);
	}
	
	/**
	 * 任务ID
	 */
	@Id
	@GenericGenerator(name = "xflow_task_id_generator", strategy = "assigned")
	@GeneratedValue(generator = "xflow_task_id_generator")
	@Column(name = "ROW_ID")
	String id;
	/**
	 * 流程实例ID
	 */
	@Column(name = "PROCESS_INSTANCE_ID")
	String processInstanceId;
	/**
	 * 任务显示的标题
	 */
	@Column(name = "TITLE")
	String title;
	/**
	 * 任务接收者名称
	 */
	@Column(name = "ASSIGNEE_NAME")
	String assigneeName;
	/**
	 * 任务接受者ID
	 */
	@Column(name = "ASSIGNEE_ID")
	String assigneeId;
	/**
	 * 任务接受者类型
	 */
	@Column(name = "ASSIGNEE_TYPE")
	String assigneeType;

	/**
	 * 任务状态
	 */
	@Column(name = "STATUS")
	String status;
	/**
	 * 任务开始时间
	 */
	@Column(name = "START_TIME")
	Date startTime;
	/**
	 * 任务所属环节
	 */
	@Column(name = "ACTIVITY_ID")
	String activityId;
	/**
	 * 任务所属环节名称
	 */
	@Column(name = "ACTIVITY_NAME")
	String activityName;
	
	@Column(name = "ACTIVITY_TYPE")
	String activityType;
	
	/**
	 * 任务地址
	 */
	@Column(name = "formURL")
	String formURL;
	/**
	 * 批次号（多人任务专用）
	 */
	@Column(name = "BATCH_NO")
	String batchNo;
	/**
	 * 来源流转ID
	 */
	@Column(name = "SRC_TRANSITION_ID")
	String srcTransitionId;
	
	@Column(name = "SRC_TRANSITION_NAME")
	String srcTransitionName;
	
	@Column(name = "SRC_ACTIVITY_ID")
	String srcActivityId;
	
	@Column(name = "SRC_ACTIVITY_NAME")
	String srcActivityName;
	/**
	 * 非流程定义内产生的任务,如加签,转办,通知
	 */
	@Column(name = "FOR_TASK_ID")
	String forTaskId;
	/**
	 * 非流程定义内的任务为N,否则为Y
	 */
	@Column(name = "IS_NORMAL_TASK")
	String isNormalTask;
	
	/**
	 * 流程定义ID
	 */
	@Column(name = "PROCESS_DEFINITION_ID")
	String processDefinitionId;
	/**
	 * 流程定义名称
	 */
	@Column(name = "PROCESS_DEFINITION_NAME")
	String processDefinitionName;
	/**
	 * 业务类型
	 */
	@Column(name = "MODULE")
	String module;
	/**
	 *业务主键 
	 */
	@Column(name = "BUSINESS_KEY")
	String businessKey;
	/**
	 * 任务执行顺序号（多人任务顺序执行专用）
	 */
	@Column(name = "ORDER_NO")
	int orderNo;
	/**
	 * 查看地址
	 */
	@Column(name = "VIEW_URL")
	String viewURL;

	@Column(name = "OPERATOR_ID")
	String operatorId;
	
	@Column(name = "OPERATOR_NAME")
	String operatorName;
	
	/**
	 * 如果该字段为Y则表示需要用户处理时指定下一步办理人
	 */
	@Column(name = "RUNTIME_ASSIGNE_FLAG")
	String runtimeAssigneFlag;

	@Column(name="MESSAGE_FLAG")
	String messageFlag;
	
	@Column(name="CLOSE_FLAG")
	String closeFlag;
	
	@Column(name="NON_AUTO_FLAG")
	String nonautoFlag;
	
	@Column(name="REJECT_FLAG")
	String rejectFlag;
	
	@Column(name="TO_IDS")
	String toIds;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getAssigneeType() {
		return assigneeType;
	}

	public void setAssigneeType(String assigneeType) {
		this.assigneeType = assigneeType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getFormURL() {
		return formURL;
	}

	public void setFormURL(String formURL) {
		this.formURL = formURL;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSrcTransitionId() {
		return srcTransitionId;
	}

	public void setSrcTransitionId(String srcTransitionId) {
		this.srcTransitionId = srcTransitionId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getSrcTransitionName() {
		return srcTransitionName;
	}

	public void setSrcTransitionName(String srcTransitionName) {
		this.srcTransitionName = srcTransitionName;
	}

	public String getViewURL() {
		return viewURL;
	}

	public void setViewURL(String viewURL) {
		this.viewURL = viewURL;
	}

	public String getForTaskId() {
		return forTaskId;
	}

	public void setForTaskId(String forTaskId) {
		this.forTaskId = forTaskId;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getSrcActivityId() {
		return srcActivityId;
	}

	public void setSrcActivityId(String srcActivityId) {
		this.srcActivityId = srcActivityId;
	}

	public String getSrcActivityName() {
		return srcActivityName;
	}

	public void setSrcActivityName(String srcActivityName) {
		this.srcActivityName = srcActivityName;
	}

	public String getIsNormalTask() {
		return isNormalTask;
	}

	public void setIsNormalTask(String isNormalTask) {
		this.isNormalTask = isNormalTask;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRuntimeAssigneFlag() {
		return runtimeAssigneFlag;
	}

	public void setRuntimeAssigneFlag(String runtimeAssigneFlag) {
		this.runtimeAssigneFlag = runtimeAssigneFlag;
	}

	public String getMessageFlag() {
		return messageFlag;
	}

	public void setMessageFlag(String messageFlag) {
		this.messageFlag = messageFlag;
	}
	
	public String getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}

	public String getNonAutoFlag() {
		return nonautoFlag;
	}

	public void setNonAutoFlag(String nonautoFlag) {
		this.nonautoFlag = nonautoFlag;
	}

	public String getRejectFlag() {
		return rejectFlag;
	}

	public void setRejectFlag(String rejectFlag) {
		this.rejectFlag = rejectFlag;
	}
	
	public String getToIds() {
		return toIds;
	}

	public void setToIds(String toIds) {
		this.toIds = toIds;
	}

	public String getStayTime(){
		
		long current = System.currentTimeMillis();
		
		long start = startTime.getTime();
		
		long stay = current - start;
		
		if(stay < (60*1000*5)){
			
			return "刚刚";
			
		}
		
		long hour = stay/(60*60*1000);
		
		long min = (stay % (60*60*1000)) / (60*1000);
		
		long day = 0;
		
		if(hour >= 24){
			
			day = hour/24;
			
			hour = hour%24;
			
		}
		
		if(day > 0){
			
			return day+"天"+hour+"小时"+min+"分";
			
		}
		
		return hour+"小时"+min+"分";
	}
}
