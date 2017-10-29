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
@Table(name = "XFLOW_PROCESS_INSTANCE")
public class ProcessInstance implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "xflow_process_instance_id_generator", strategy = "assigned")
	@GeneratedValue(generator = "xflow_process_instance_id_generator")
	@Column(name = "ROW_ID")
	String id;
	
	@Column(name = "BUSINESS_KEY")
	String businessKey;
	
	@Column(name = "MODULE")
	String module;
	
	@Column(name = "APPLICATION")
	String application;
	
	@Column(name = "PROCESS_INSTANCE_NAME")
	String name;
	
	@Column(name = "STATUS")
	String status;
	
	@Column(name = "PARENT_ID")
	String parentId;
	
	@Column(name = "START_TIME")
	Date startTime;
	
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

}
