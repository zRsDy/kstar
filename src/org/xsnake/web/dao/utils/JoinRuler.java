package org.xsnake.web.dao.utils;

public class JoinRuler {

	public JoinRuler(Class<?> classA,String fieldA,Class<?> classB,String fieldB){
		this.classA = classA;
		this.classB = classB;
		this.fieldA = fieldA;
		this.fieldB = fieldB;
	}
	
	Class<?> classA;
	
	Class<?> classB;
	
	private String fieldA;
	
	private String op = " = ";
	
	private String fieldB;

	public Class<?> getClassA() {
		return classA;
	}

	public void setClassA(Class<?> classA) {
		this.classA = classA;
	}

	public Class<?> getClassB() {
		return classB;
	}

	public void setClassB(Class<?> classB) {
		this.classB = classB;
	}

	public String getFieldA() {
		return fieldA;
	}

	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getFieldB() {
		return fieldB;
	}

	public void setFieldB(String fieldB) {
		this.fieldB = fieldB;
	}
	
}
