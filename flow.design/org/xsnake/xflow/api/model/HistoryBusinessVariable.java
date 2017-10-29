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
@Table(name = "XFLOW_HISTORY_VARIABLE")
public class HistoryBusinessVariable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "xflow_history_variable_id_generator", strategy = "uuid")
	@GeneratedValue(generator = "xflow_history_variable_id_generator")
	@Column(name = "ROW_ID")
	String id;
	
	@Column(name = "VARIABLE_KEY")
	String key;
	
	@Column(name = "STRING_VALUE")
	String value;
	
	@Column(name = "PROCESS_INSTANCE_ID")
	String processInstanceId;
	
	@Column(name = "CREATE_TIME")
	Date createTime;
	
	@Column(name = "CREATOR_ID")
	String creatorId;
	
	@Column(name = "CREATOR_NAME")
	String creatorName;
	
	@Column(name = "CREATOR_TYPE")
	String creatorType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

}
