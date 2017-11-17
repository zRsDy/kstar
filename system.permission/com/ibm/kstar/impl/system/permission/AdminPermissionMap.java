package com.ibm.kstar.impl.system.permission;

import java.util.HashMap;

public class AdminPermissionMap  extends HashMap<String, String>{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String get(Object key) {
		return (String)key;
	}

}
