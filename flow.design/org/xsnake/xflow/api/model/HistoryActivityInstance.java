package org.xsnake.xflow.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "XFLOW_HISTORY")
public class HistoryActivityInstance implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "xflow_history_id_generator", strategy = "assigned")
	@GeneratedValue(generator = "xflow_history_id_generator")
	@Column(name = "ROW_ID")
	String id;
	
	@Column(name = "PROCESS_INSTANCE_ID")
	String processInstanceId;
	
	@Column(name = "ACTIVITY_TYPE")
	String activityType;
	
	@Column(name = "ACTIVITY_ID")
	String activityId;
	
	@Column(name = "ACTIVITY_NAME")
	String activityName;
	
	@Column(name = "START_TIME")
	Date startTime;
	
	@Column(name = "END_TIME")
	Date endTime;
	
	@Column(name = "TO_TRANSITION_ID")
	String toTransitionId;
	
	@Column(name = "TO_TRANSITION_NAME")
	String toTransitionName;

	@Column(name = "TO_ACTIVITY_ID")
	String toActivityId;
	
	@Column(name = "TO_ACTIVITY_NAME")
	String toActivityName;
	
	@Column(name = "ASSIGNEE_NAME")
	String assigneeName;
	
	@Column(name = "ASSIGNEE_ID")
	String assigneeId;
	
	@Column(name = "ASSIGNEE_TYPE")
	String assigneeType;
	
	@Column(name = "OPERATOR_ID")
	String operatorId;
	
	@Column(name = "OPERATOR_NAME")
	String operatorName;
	
	@Column(name = "BATCH_NO")
	String batchNo;
	
	@Column(name = "ORDER_NO")
	int orderNo;
	
	@Column(name = "STATUS")
	String status;
	
	@Column(name = "VIEW_URL")
	String viewURL;
	
	@Column(name = "TASK_ID")
	String taskId;
	
	@Column(name = "CCOMMENT")
	String comment;
	
	@Column(name = "PROCESS_DEFINITION_ID")
	String processDefinitionId;
	
	@Column(name = "PROCESS_DEFINITION_NAME")
	String processDefinitionName;
	
	@Column(name = "MODULE")
	String module;
	
	@Column(name = "BUSINESS_KEY")
	String businessKey;

	@Column(name = "FOR_TASK_ID")
	String forTaskId;
	
	@Column(name = "IS_NORMAL_TASK")
	String isNormalTask;
	
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

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getToTransitionId() {
		return toTransitionId;
	}

	public void setToTransitionId(String toTransitionId) {
		this.toTransitionId = toTransitionId;
	}

	public String getToTransitionName() {
		return toTransitionName;
	}

	public void setToTransitionName(String toTransitionName) {
		this.toTransitionName = toTransitionName;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getViewURL() {
		return viewURL;
	}

	public void setViewURL(String viewURL) {
		this.viewURL = viewURL;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getForTaskId() {
		return forTaskId;
	}

	public void setForTaskId(String forTaskId) {
		this.forTaskId = forTaskId;
	}

	public String getIsNormalTask() {
		return isNormalTask;
	}

	public void setIsNormalTask(String isNormalTask) {
		this.isNormalTask = isNormalTask;
	}

	public String getToActivityId() {
		return toActivityId;
	}

	public void setToActivityId(String toActivityId) {
		this.toActivityId = toActivityId;
	}

	public String getToActivityName() {
		return toActivityName;
	}

	public void setToActivityName(String toActivityName) {
		this.toActivityName = toActivityName;
	}
	
}

