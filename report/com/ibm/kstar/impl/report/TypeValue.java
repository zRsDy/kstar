package com.ibm.kstar.impl.report;

public class TypeValue implements Comparable<TypeValue> {
	String customClass;
	String customType;
	String customTypeSub;
	Value value ;

	public TypeValue(String customType,String customTypeSub) {
		this.customTypeSub = customTypeSub;
		this.customType = customType;
		
		if(this.customType == null){
			this.customType = "未分类";
		}
		
		if(this.customTypeSub == null){
			this.customTypeSub = "未分类";
		}
		
		value= new Value();
	}
	
	public TypeValue(String customClass,String customType,String customTypeSub) {
		this.customTypeSub = customTypeSub;
		this.customType = customType;
		this.customClass = customClass;
		
		if(this.customClass == null){
			this.customClass = "未分类";
		}
		if(this.customType == null){
			this.customType = "未分类";
		}
		
		if(this.customTypeSub == null){
			this.customTypeSub = "未分类";
		}
		
		value= new Value();
	}
	
	
	public Double getTotle() {
		return value.getM01() +
		value.getM02() +
		value.getM03() +
		value.getM04() +
		value.getM05() +
		value.getM06() +
		value.getM07() +
		value.getM08() +
		value.getM09() +
		value.getM10() +
		value.getM11() +
		value.getM12() ;
	}

	
	public String getCustomClass() {
		return customClass;
	}

	public void setCustomClass(String customClass) {
		this.customClass = customClass;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public String getCustomTypeSub() {
		return customTypeSub;
	}

	public void setCustomTypeSub(String customTypeSub) {
		this.customTypeSub = customTypeSub;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	@Override
	public int compareTo(TypeValue o) {
		if(o.getCustomType() == null){
			return 0;
		}
		if(customType == null){
			return 1;
		}
		return customType.hashCode()  - o.getCustomType().hashCode();
	}
}
