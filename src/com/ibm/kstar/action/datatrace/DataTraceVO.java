package com.ibm.kstar.action.datatrace;

import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;

public class DataTraceVO{
	
	private String pkValue;
	
	private String tableName;
	
	private String tableComment;
	
	private String keyColComment;
	
	private String keyColValue;
	
	private String operator;
	
	private String operateDate;
	
	private String operateType;

	public String getPkValue() {
		return pkValue;
	}

	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public String getKeyColComment() {
		return keyColComment;
	}

	public void setKeyColComment(String keyColComment) {
		this.keyColComment = keyColComment;
	}

	public String getKeyColValue() {
		return keyColValue;
	}

	public void setKeyColValue(String keyColValue) {
		this.keyColValue = keyColValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	public String getOperatorName(){
		Employee emp = (Employee)CacheData.getInstance().get(operator);
		if(emp==null){
			return operator;
		}
		return emp.getName();
	}
}
