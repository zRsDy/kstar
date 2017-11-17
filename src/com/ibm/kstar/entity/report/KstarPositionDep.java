/** 
 * Project Name:crm 
 * File Name:KstarAnltgt.java 
 * Package Name:com.ibm.kstar.entity.report 
 * Date:Mar 15, 20173:14:51 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.entity.report;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "CRM_T_POSITION_DEP")
public class KstarPositionDep implements Serializable {
	
	private static final long serialVersionUID = 2005407449905894312L;

	@Id
	@GeneratedValue(generator = "kstarposdep_id_generator")
	@GenericGenerator(name="kstarposdep_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	/**
	 * 岗位id
	 */
	@Column(name = "C_POS_ID")
	private String posId;
	
	/**
	 * 部门id
	 */
	@Column(name = "C_DEP_ID")
	private String depId;
	
	public KstarPositionDep(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	
}
