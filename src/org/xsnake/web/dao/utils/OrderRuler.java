package org.xsnake.web.dao.utils;

import java.io.Serializable;

public class OrderRuler implements Serializable{

	private static final long serialVersionUID = 1L;

	private String field;
	
	private String ascOrDesc;

	public String getAscOrDesc() {
		return ascOrDesc;
	}

	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public OrderRuler(String field, String ascOrDesc) {
		this.field = field;
		this.ascOrDesc = ascOrDesc;
	}

	public OrderRuler() {
	}
}
