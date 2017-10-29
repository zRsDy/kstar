package com.ibm.kstar.log;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SYS_T_LOG")
public class LogObject implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ROW_ID", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "SYS_T_LOG_ID_GENERATOR")
	@GenericGenerator(name = "SYS_T_LOG_ID_GENERATOR", strategy = "uuid")
	String id;
	
	@Column(name="CREATE_DATE")
	Date date;

	@Column(name="user_Id")
	String userId;
	
	@Column(name="USER_NAME")
	String username;
	
	@Column(name="IP")
	String ip;
	
	@Column(name="MOUDLE")
	String moudle;
	
	@Column(name="NOTES")
	String notes;
	
	@Column(name="ARGSJSON")
	String argsJson;

	@Column(name="URL")
	String url;
	
	@Column(name="POSITION_ID")
	String positionId;
	
	@Column(name="POSITION_NAME")
	String positionName;
	
	@Column(name="ORG_ID")
	String orgId;
	
	@Column(name="ORG_NAME")
	String orgName;

	@Lob
	@Column(name = "DETAIL")
	String detail;

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMoudle() {
		return moudle;
	}

	public void setMoudle(String moudle) {
		this.moudle = moudle;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getArgsJson() {
		return argsJson;
	}

	public void setArgsJson(String argsJson) {
		this.argsJson = argsJson;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
