package org.xsnake.web.action;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.permission.UserObject;

public class Condition implements Serializable {

	private static final long serialVersionUID = 1L;

	Map<String, Object> conditionMap = new HashMap<String, Object>();
	
	FilterObject filterObject = null;

	public Map<String, Object> getConditionMap() {
		return conditionMap;
	}

	public void setConditionMap(Map<String, Object> conditionMap) {
		this.conditionMap = conditionMap;
	}

	public FilterObject getFilterObject(Class<?> clazz) {
		if(filterObject !=null){
			filterObject.setClazz(clazz);
			return filterObject;
		}else{
			filterObject = new FilterObject();
		}
		String filter = (String)conditionMap.get("filters");
		if(filter != null){
			FilterObject _filterObject = JsonResult.toObject(filter,FilterObject.class);
			filterObject.setRules(_filterObject.getRules());
			filterObject.setOrRules(_filterObject.getOrRules());
		}
		filterObject.setClazz(clazz);
		filterObject.setUser((UserObject)this.getCondition("userObject"));
		
		String orderField = (String)conditionMap.get("sidx");
		String orderType = (String)conditionMap.get("sord");
		if(StringUtil.isNotEmpty(orderField)){
			filterObject.addOrderBy(orderField, StringUtil.isNotEmpty(orderType)?orderType : "asc");
		}
		
		return filterObject;
	}

	public FilterObject getFilterObject() {
		return getFilterObject(null);
	}

	public void setFilterObject(FilterObject filterObject) {
		this.filterObject = filterObject;
	}

	public Condition setCondition(String name, Object condition) {
		conditionMap.put(name, condition);
		return this;
	}

	public Object getCondition(String name) {
		return conditionMap.get(name);
	}

	public Date getDateCondition(String name) {
		Object value = conditionMap.get(name);
		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return (Date) value;
		}
		throw new RuntimeException("请注意条件类型");
	}

	public String getStringCondition(String name) {
		Object value = conditionMap.get(name);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		throw new RuntimeException("请注意条件类型");
	}

	public Map<String,String> toMap(){
		Map<String,String> map = new HashMap<String,String>();
		for(String key : conditionMap.keySet()){
			map.put(key, conditionMap.get(key)!=null?conditionMap.get(key).toString(): null);
		}
		return map;
	}
}
