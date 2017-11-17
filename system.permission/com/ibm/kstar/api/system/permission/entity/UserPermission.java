package com.ibm.kstar.api.system.permission.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

//关联用户岗位,用户角色的中间表
@Entity
@Table(name = "SYS_T_PERMISSION_USER_REL")
public class UserPermission implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sys_t_permission_rel_id_generator")
	@GenericGenerator(name="sys_t_permission_rel_id_generator", strategy="uuid")
	@Column(name="ROW_ID")
	private String id;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="TYPE")
	private String type;//岗位 P，角色R
	
	@Column(name="MEMBER_ID")
	private String memberId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}
