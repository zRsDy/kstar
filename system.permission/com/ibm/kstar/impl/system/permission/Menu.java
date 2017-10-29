package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.List;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IMenu;

public class Menu implements IMenu{

	private static final long serialVersionUID = 1L;

	public Menu(LovMember lovMember){
		this.id = lovMember.getId();
		this.name = lovMember.getName();
		this.path = lovMember.getOptTxt1();
		this.icon = lovMember.getOptTxt2();
		this.code = lovMember.getCode();
	}
	
	List<IMenu> childMenuList = new ArrayList<IMenu>();
	
	String id;
	
	String name;
	
	String path;
	
	String icon;
	
	String code;

	public List<IMenu> getChildMenuList() {
		return childMenuList;
	}

	public String getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public void setChildMenuList(List<IMenu> childMenuList) {
		this.childMenuList = childMenuList;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
