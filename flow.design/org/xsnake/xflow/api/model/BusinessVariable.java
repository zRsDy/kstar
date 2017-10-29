package org.xsnake.xflow.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "XFLOW_VARIABLE")
public class BusinessVariable implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "xflow_variable_id_generator", strategy = "uuid")
	@GeneratedValue(generator = "xflow_variable_id_generator")
	@Column(name = "ROW_ID")
	String id;
	
	@Column(name = "VARIABLE_KEY")
	String key;
	
	@Column(name = "STRING_VALUE")
	String value;
	
	@Column(name = "PROCESS_INSTANCE_ID")
	String processInstanceId;

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


}
