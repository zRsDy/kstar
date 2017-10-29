package com.ibm.kstar.api.system.permission.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_T_PERMISSION_ROLE_P_REL")
public class RolePosition implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sys_t_permission_role_p_rel_id_generator")
	@GenericGenerator(name="sys_t_permission_role_p_rel_id_generator", strategy="uuid")
	@Column(name="ROW_ID")
	private String id;

	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="POSITION_ID")
	private String positionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	
}
