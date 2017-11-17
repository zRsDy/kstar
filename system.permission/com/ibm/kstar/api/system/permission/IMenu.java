package com.ibm.kstar.api.system.permission;

import java.io.Serializable;
import java.util.List;

public interface IMenu extends Serializable{
	
	List<IMenu> getChildMenuList();

	String getId();

	String getPath();

	String getName();
	
	String getIcon();
	
}
