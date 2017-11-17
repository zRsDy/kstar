package org.xsnake.web.context;

import java.util.ArrayList;
import java.util.List;

public class RequestMappingAction {

	String value;
	
	String file;
	
	List<String> childList = new ArrayList<>();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getChildList() {
		return childList;
	}

	public void setChildList(List<String> childList) {
		this.childList = childList;
	}
	
	public void add(String path){
		childList.add(path);
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
}
