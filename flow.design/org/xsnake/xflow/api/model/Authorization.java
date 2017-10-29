package org.xsnake.xflow.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "XFLOW_SUPPORT_AUTH")
public class Authorization implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "xflow_support_auth_id_generator", strategy = "uuid")
	@GeneratedValue(generator = "xflow_support_auth_id_generator")
	@Column(name = "ROW_ID")
	String id;
	
	@Column(name = "OWNER_ID")
	String ownerId;
	
	@Column(name = "OWNER_NAME")
	String ownerName;
	
	@Column(name = "OWNER_TYPE")
	String ownerType;
	
	@Column(name = "ASSIGNEE_ID")
	String assigneeId;
	
	@Column(name = "ASSIGNEE_NAME")
	String assigneeName;
	
	@Column(name = "ASSIGNEE_TYPE")
	String assigneeType;
	
	/**
	 * 类型：
	 * F:按流程
	 * A:按应用
	 */
	@Column(name = "TYPE")
	String type;
	 
	/**
	 * 如果类型是F则是，这里是流程的代码
	 * 如果类型是A则是，这里是应用的代码
	 */
	@Column(name = "MODULE")
	String module;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getAssigneeType() {
		return assigneeType;
	}

	public void setAssigneeType(String assigneeType) {
		this.assigneeType = assigneeType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
}
