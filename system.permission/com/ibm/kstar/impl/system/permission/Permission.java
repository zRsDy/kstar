package com.ibm.kstar.impl.system.permission;

import java.io.Serializable;

import com.ibm.kstar.api.system.permission.IPermission;

public class Permission implements Serializable ,IPermission {

	private static final long serialVersionUID = 1L;

	String code;
	
	String codePath;
	
	String path;
	
	public Permission(String code,String codePath,String path){
		this.code = code;
		this.path = path;
		this.codePath = codePath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}
	
}
