package org.xsnake.web.ui;

import java.util.ArrayList;
import java.util.List;

import org.xsnake.web.utils.StringUtil;

public class TabMain {
	public TabMain(){
		this.id = StringUtil.getUUID();
		this.style = Style.top;
	}
	public TabMain(Style style){
		this.id = StringUtil.getUUID();
		this.style = style;
	}
	String id;
	Style style;
	boolean initAll = false;
	List<Tab> list = new ArrayList<>();
	public enum Style{
		top("top"),left("left");
		private Style(String style){
			this.style = style;
		}
		String style;
		public String getStyle() {
			return style;
		}
	}
	public String getStyle() {
		return style.getStyle();
	}
	
	public TabMain addTab(String title,String url){
		Tab tab = new Tab(title,url);
		list.add(tab);
		return this;
	}
	public List<Tab> getList() {
		return list;
	}
	public String getId() {
		return id;
	}
	public boolean isInitAll() {
		return initAll;
	}
	public void setInitAll(boolean initAll) {
		this.initAll = initAll;
	}
	
}
