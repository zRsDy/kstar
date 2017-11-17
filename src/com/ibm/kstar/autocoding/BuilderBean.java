package com.ibm.kstar.autocoding;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class BuilderBean {

	List<Field> fields;
	
	String className;
	
	String modelName;
	
	Map<String, String> viewNameMap;
	
	Map<String, String> valueSourceMap;
	
	Map<String, String> requiredMap;
	
	Map<String, String> placeholderMap;
	
	Map<String, String> tipMap;
	
	String[] visables;
	
	String[] requireds;
	
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Map<String, String> getViewNameMap() {
		return viewNameMap;
	}

	public void setViewNameMap(Map<String, String> viewNameMap) {
		this.viewNameMap = viewNameMap;
	}
	
	public Map<String, String> getValueSourceMap() {
		return valueSourceMap;
	}

	public void setValueSourceMap(Map<String, String> valueSourceMap) {
		this.valueSourceMap = valueSourceMap;
	}

	public String[] getVisables() {
		return visables;
	}

	public void setVisables(String[] visables) {
		this.visables = visables;
	}

	public String[] getRequireds() {
		return requireds;
	}

	public void setRequireds(String[] requireds) {
		this.requireds = requireds;
	}

	public Map<String, String> getRequiredMap() {
		return requiredMap;
	}

	public void setRequiredMap(Map<String, String> requiredMap) {
		this.requiredMap = requiredMap;
	}

	public Map<String, String> getPlaceholderMap() {
		return placeholderMap;
	}

	public void setPlaceholderMap(Map<String, String> placeholderMap) {
		this.placeholderMap = placeholderMap;
	}

	public Map<String, String> getTipMap() {
		return tipMap;
	}

	public void setTipMap(Map<String, String> tipMap) {
		this.tipMap = tipMap;
	}
	
}
