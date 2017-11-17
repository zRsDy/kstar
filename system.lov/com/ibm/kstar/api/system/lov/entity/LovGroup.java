package com.ibm.kstar.api.system.lov.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "SYS_T_LOV_GROUP")
public class LovGroup implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sys_t_lov_group_id_generator")
	@GenericGenerator(name="sys_t_lov_group_id_generator", strategy="uuid")
	@Column(name="ROW_ID")
	private String id;
	
	@NotEmpty
	@Column(name="GROUP_CODE")
	private String code;
	
	@NotEmpty
	@Column(name="GROUP_NAME")
	private String name;
	
	@NotEmpty
	@Column(name="MEMO")
	private String memo;
	
	@Column(name="TREE_FLAG")
	private String treeFlag;
	
	@Column(name="DELETE_FLAG")
	private String deleteFlag = "N";
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="CREATION_BY")
	private String creationBy;
	
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="OPT_TXT1")
	private String optTxt1;
	
	@Column(name="OPT_TXT2")
	private String optTxt2;
	
	@Column(name="OPT_TXT3")
	private String optTxt3;
	
	@Column(name="OPT_TXT4")
	private String optTxt4;
	
	@Column(name="OPT_TXT5")
	private String optTxt5;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="REPEAT_FLAG")
	private String repeatFlag;
	
	/**
	 * 需要在业务端维护的数据，需要锁定在通用LOV处维护的功能
	 */
	@Column(name="LOCKED_FLAG")
	private String lockedFlag;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTreeFlag() {
		return treeFlag;
	}

	public void setTreeFlag(String treeFlag) {
		this.treeFlag = treeFlag;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationBy() {
		return creationBy;
	}

	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getOptTxt1() {
		return optTxt1;
	}

	public void setOptTxt1(String optTxt1) {
		this.optTxt1 = optTxt1;
	}

	public String getOptTxt2() {
		return optTxt2;
	}

	public void setOptTxt2(String optTxt2) {
		this.optTxt2 = optTxt2;
	}

	public String getOptTxt3() {
		return optTxt3;
	}

	public void setOptTxt3(String optTxt3) {
		this.optTxt3 = optTxt3;
	}

	public String getOptTxt4() {
		return optTxt4;
	}

	public void setOptTxt4(String optTxt4) {
		this.optTxt4 = optTxt4;
	}

	public String getOptTxt5() {
		return optTxt5;
	}

	public void setOptTxt5(String optTxt5) {
		this.optTxt5 = optTxt5;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getLockedFlag() {
		return lockedFlag;
	}

	public void setLockedFlag(String lockedFlag) {
		this.lockedFlag = lockedFlag;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}
	
}
