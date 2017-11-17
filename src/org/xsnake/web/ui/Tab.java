package org.xsnake.web.ui;

import org.xsnake.web.utils.StringUtil;

public class Tab {
	String title;
	String url;
	String styleClass;
	String id;
	public Tab(String title,String url,String styleClass){
		id=StringUtil.getUUID();
		this.title = title;
		this.url = url;
		this.styleClass = styleClass;
	}
	
	public Tab(String title,String url){
		id=StringUtil.getUUID();
		this.title = title;
		this.url = url;
	}
	
	public String getTitle() {
		return title;
	}
	public String getUrl() {
		return url;
	}
	public String getStyleClass() {
		return styleClass;
	}

	public String getId() {
		return id;
	}
	
}
