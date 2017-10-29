package org.xsnake.web.action;

public class PageCondition extends Condition{

	private static final long serialVersionUID = 1L;
	
	private int rows = 20;
	
	private int page = 1;
	
	private String reload="N";

	public String getReload() {
		return reload;
	}

	public void setReload(String reload) {
		this.reload = reload;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		if("Y".equals(getReload())){
			page = 1;
			return page;
		}
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
}
