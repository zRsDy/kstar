package com.ibm.kstar.mobile.vo;

import java.io.Serializable;

public class ProcessVO implements Serializable {

	private static final long serialVersionUID = 3755022050234678444L;
	/**
	 * 任务id 必填
	 */
	private String taskId;
	/**
	 * 操作名称必填
	 */
	private String optName;
	/**
	 * 操作类型必填
	 */
	private String optType;
	/**
	 * 选择的参与人或者活动id,如果particpantMultiple=true，可以多选，非必填
	 */
	private String selectIds;
	/**
	 * 备注，非必填
	 */
	private String comment;
	
	public ProcessVO(){}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
}
