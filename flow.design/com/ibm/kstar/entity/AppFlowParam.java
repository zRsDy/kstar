package com.ibm.kstar.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sys_t_app_flow_param")
public class AppFlowParam implements Serializable{

	private static final long serialVersionUID = 1l;

	@Id
	@GeneratedValue(generator = "sys_t_sys_t_app_flow_power")
	@GenericGenerator(name="sys_t_sys_t_app_flow_power", strategy="uuid")
	@Column(name="c_pid")
	private String id;
	
	@Column(name="c_application_id")
	private String applicationId;
	
	@Column(name="c_param_name")
	private String paramName;
	
	@Column(name="c_related_form")
	private String relatedForm;
	
	@Column(name="c_related_table_field")
	private String relatedTableField;
	
	@Column(name="c_remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getRelatedForm() {
		return relatedForm;
	}

	public void setRelatedForm(String relatedForm) {
		this.relatedForm = relatedForm;
	}

	public String getRelatedTableField() {
		return relatedTableField;
	}

	public void setRelatedTableField(String relatedTableField) {
		this.relatedTableField = relatedTableField;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
