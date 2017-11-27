package com.ibm.kstar.impl.report;

import java.util.List;

public class ExpenseVO {
	/** 招待费**/
	private List<ExpenseObj> hosExp; 
	/** 差旅费**/
	private List<ExpenseObj> traExp;
	/** 通讯费**/
	private List<ExpenseObj> comExp; 
	/** 交通费**/
	private List<ExpenseObj> carExp; 
	/** 办公用品**/
	private List<ExpenseObj> offExp; 
	/** 其他**/
	private List<ExpenseObj> otherExp;
	
	public List<ExpenseObj> getHosExp() {
		return hosExp;
	}
	public void setHosExp(List<ExpenseObj> hosExp) {
		this.hosExp = hosExp;
	}
	public List<ExpenseObj> getTraExp() {
		return traExp;
	}
	public void setTraExp(List<ExpenseObj> traExp) {
		this.traExp = traExp;
	}
	public List<ExpenseObj> getComExp() {
		return comExp;
	}
	public void setComExp(List<ExpenseObj> comExp) {
		this.comExp = comExp;
	}
	public List<ExpenseObj> getCarExp() {
		return carExp;
	}
	public void setCarExp(List<ExpenseObj> carExp) {
		this.carExp = carExp;
	}
	public List<ExpenseObj> getOffExp() {
		return offExp;
	}
	public void setOffExp(List<ExpenseObj> offExp) {
		this.offExp = offExp;
	}
	public List<ExpenseObj> getOtherExp() {
		return otherExp;
	}
	public void setOtherExp(List<ExpenseObj> otherExp) {
		this.otherExp = otherExp;
	}
	
	
}
