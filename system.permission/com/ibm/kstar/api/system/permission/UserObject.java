package com.ibm.kstar.api.system.permission;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xsnake.xflow.api.Participant;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.impl.system.permission.AdminPermissionMap;

public class UserObject implements Serializable{

	private static final long serialVersionUID = 1L;

	private String eid;

	private String pid;

	private String oid;

	private Employee employee;
	
	private Position position;
	
	private LovMember org;
	
	private String loginIp;
	
	private String clientMac;
	
	private String loginDate;
	
	private String sessionId;
	
	private Map<String,String> permissionCodeMap = new HashMap<String,String>();
	private Map<String,String> permissionCodePathMap = new HashMap<String,String>();
	private Map<String,String> permissionPathMap = new HashMap<String,String>();

	public UserObject(String eid, String pid,String oid) {
		this.eid = eid;
		this.pid = pid;
		this.oid = oid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getClientMac() {
		return clientMac;
	}

	public void setClientMac(String clientMac) {
		this.clientMac = clientMac;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public UserObject(Employee employee, List<IPermission> permissionList){
		this.employee = employee;
		if(permissionList == null){
			permissionCodeMap = new AdminPermissionMap();
			permissionPathMap = new AdminPermissionMap();
			permissionCodePathMap = new AdminPermissionMap();
		}else{
			for(IPermission permission : permissionList){
				permissionCodeMap.put(permission.getCode(),permission.getCode());
				permissionPathMap.put(permission.getPath(), permission.getPath());
				permissionCodePathMap.put(permission.getCodePath(), permission.getCodePath());
			}
		}
	}
	
	public void initPermission(List<IPermission> permissionList){
		if(permissionList !=null){
			permissionCodeMap = new HashMap<String,String>();
			permissionPathMap = new HashMap<String,String>();
			permissionCodePathMap = new HashMap<String,String>();
			for(IPermission permission : permissionList){
				permissionCodeMap.put(permission.getCode(),permission.getCode());
				permissionPathMap.put(permission.getPath(), permission.getPath());
				permissionCodePathMap.put(permission.getCodePath(), permission.getCodePath());
			}
		}else{
			permissionCodeMap = new AdminPermissionMap();
			permissionPathMap = new AdminPermissionMap();
			permissionCodePathMap = new AdminPermissionMap();
		}
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public String getEmployeeName(){
		return this.getEmployee().getName();
	}
	
	public String getEmployeeNo(){
		return this.getEmployee().getNo();
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(LovMember lovMember) {
		position = new Position(lovMember);
	}

	public boolean hasPermission(String code){
		return permissionCodeMap.get(code) == null ? false : true;
	}
	
	public boolean hasPermissionPath(String path){
		return permissionPathMap.get(path) == null ? false : true;
	}

	public Map<String, String> getPermissionPathMap() {
		return permissionPathMap;
	}

	public void setPermissionPathMap(Map<String, String> permissionPathMap) {
		this.permissionPathMap = permissionPathMap;
	}

	public LovMember getOrg() {
		return org;
	}

	public void setOrg(LovMember org) {
		this.org = org;
	}
	
	public Participant getParticipant(){
		return new Participant(getEmployee().getId(),getEmployee().getName(),"EMPLOYEE");
	}

	public Map<String, String> getPermissionCodeMap() {
		return permissionCodeMap;
	}

	public Map<String, String> getPermissionCodePathMap() {
		return permissionCodePathMap;
	}
	
	public boolean isInner(){
		if(getOrg().getOptTxt3() != null){
			return !"E".equals(getOrg().getOptTxt3());
		}
		return false;
		
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
