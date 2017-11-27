package com.ibm.kstar.impl.report;

public class ExpenseObj {
	//费用名
	private String name;
	//费用金额
	private Double amount;
	
	public ExpenseObj() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
