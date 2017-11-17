package org.xsnake.xflow.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "XFLOW_HISTORY_PROCESS_INSTANCE")
public class HistoryProcessInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	public HistoryProcessInstance(){}
	
	public HistoryProcessInstance(List<Object> objs){
		this.id = (String)objs.get(0);
		this.businessKey = (String)objs.get(1);
		this.creatorId = (String)objs.get(2);
		this.creatorName = (String)objs.get(3);
		this.creatorType = (String)objs.get(4);
		this.endTime = (Date)objs.get(5);
		this.module = (String)objs.get(6);
		this.name =  (String)objs.get(7);
		this.parentId = (String)objs.get(8);
		this.processDefinitionId =  (String)objs.get(9);
		this.processDefinitionName =  (String)objs.get(10);
		this.startTime = (Date)objs.get(11);
		this.status = (String)objs.get(12);
	}
	
	@Id
	@GenericGenerator(name = "xflow_history_process_instance_id_generator", strategy = "assigned")
	@GeneratedValue(generator = "xflow_history_process_instance_id_generator")
	@Column(name = "ROW_ID")
	String id;
	
	@Column(name = "BUSINESS_KEY")
	String businessKey;
	
	@Column(name = "MODULE")
	String module;
	
	@Column(name = "PROCESS_INSTANCE_NAME")
	String name;
	
	@Column(name = "STATUS")
	String status;
	
	@Column(name = "PARENT_ID")
	String parentId;
	
	@Column(name = "START_TIME")
	Date startTime;
	
	@Column(name = "END_TIME")
	Date endTime;
	
	@Column(name = "CREATOR_ID")
	String creatorId;
	
	@Column(name = "CREATOR_NAME")
	String creatorName;
	
	@Column(name = "CREATOR_TYPE")
	String creatorType;
	
	@Column(name = "PROCESS_DEFINITION_ID")
	String processDefinitionId;
	
	@Column(name = "PROCESS_DEFINITION_NAME")
	String processDefinitionName;

	@Column(name = "APPLICATION")
	String application;
	
	@Transient
	Date lastOperateTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
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

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public Date getLastOperateTime() {
		return lastOperateTime;
	}

	public void setLastOperateTime(Date lastOperateTime) {
		this.lastOperateTime = lastOperateTime;
	}

	
}
