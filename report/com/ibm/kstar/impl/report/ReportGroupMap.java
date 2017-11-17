package com.ibm.kstar.impl.report;

import java.util.HashMap;

public class ReportGroupMap<K,V> extends HashMap<K, V>{

	private static final long serialVersionUID = 1L;
	
	//分组的头只需要被读取一次后清空
	@Override
	public V get(Object key) {
		V result = super.get(key);
		this.remove(key);
		return result;
	}
}
