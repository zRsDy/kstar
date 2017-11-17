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
@Table(name = "XFLOW_DEFINITION")
public class ProcessDefinition implements Serializable{

	public static final String STATUS_AVAILABLE = "available";
	
	public static final String STATUS_UNAVAILABLE = "unavailable";
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "xflow_definition_id_generator", strategy = "assigned")
	@GeneratedValue(generator = "xflow_definition_id_generator")
	@Column(name = "ROW_ID")
	protected String id;
	
	/**
	 * 流程定义的名称
	 */
	@Column(name = "PROCESS_NAME")
	protected String name;
	
	/**
	 * 流程描述
	 */
	@Column(name = "DESCRIPTION")
	protected String description;

	/**
	 * 流程为哪一类业务定义
	 */
	@Column(name = "MODULE")
	protected String module;
	
	@Column(name = "CREATE_DATE")
	protected Date createDate;
	
	@Column(name = "PROCESS_VERSION")
	protected int version;
	
	@Column(name = "status")
	protected String status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
