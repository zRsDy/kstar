package com.ibm.kstar.impl.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAnalysisValue {

	String type;
	BigDecimal sixYearsAgoAccomplish = new BigDecimal(0);//达成
	Double sixYearsAgoContrast = 0d;//同比
	
	BigDecimal fiveYearsAgoAccomplish = new BigDecimal(0);//达成
	Double fiveYearsAgoContrast = 0d;//同比
	
	BigDecimal fourYearsAgoAccomplish = new BigDecimal(0);
	Double fourYearsAgoContrast = 0d;
	
	BigDecimal threeYearsAgoAccomplish = new BigDecimal(0);
	Double threeYearsAgoContrast = 0d;
	
	BigDecimal twoYearsAgoAccomplish = new BigDecimal(0);
	Double twoYearsAgoContrast = 0d;
	
	BigDecimal oneYearsAgoAccomplish = new BigDecimal(0);
	Double oneYearsAgoContrast = 0d;
	
	BigDecimal newYearsAgoAccomplish = new BigDecimal(0);
	Double newYearsAgoContrast = 0d;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getFiveYearsAgoAccomplish() {
		return fiveYearsAgoAccomplish;
	}
	public void setFiveYearsAgoAccomplish(BigDecimal fiveYearsAgoAccomplish) {
		this.fiveYearsAgoAccomplish = fiveYearsAgoAccomplish;
	}
	public Double getFiveYearsAgoContrast() {
		return fiveYearsAgoContrast;
	}
	public void setFiveYearsAgoContrast(Double fiveYearsAgoContrast) {
		this.fiveYearsAgoContrast = fiveYearsAgoContrast;
	}
	public BigDecimal getFourYearsAgoAccomplish() {
		return fourYearsAgoAccomplish;
	}
	public void setFourYearsAgoAccomplish(BigDecimal fourYearsAgoAccomplish) {
		this.fourYearsAgoAccomplish = fourYearsAgoAccomplish;
	}
	public Double getFourYearsAgoContrast() {
		return fourYearsAgoContrast;
	}
	public void setFourYearsAgoContrast(Double fourYearsAgoContrast) {
		this.fourYearsAgoContrast = fourYearsAgoContrast;
	}
	public BigDecimal getThreeYearsAgoAccomplish() {
		return threeYearsAgoAccomplish;
	}
	public void setThreeYearsAgoAccomplish(BigDecimal threeYearsAgoAccomplish) {
		this.threeYearsAgoAccomplish = threeYearsAgoAccomplish;
	}
	public Double getThreeYearsAgoContrast() {
		return threeYearsAgoContrast;
	}
	public void setThreeYearsAgoContrast(Double threeYearsAgoContrast) {
		this.threeYearsAgoContrast = threeYearsAgoContrast;
	}
	public BigDecimal getTwoYearsAgoAccomplish() {
		return twoYearsAgoAccomplish;
	}
	public void setTwoYearsAgoAccomplish(BigDecimal twoYearsAgoAccomplish) {
		this.twoYearsAgoAccomplish = twoYearsAgoAccomplish;
	}
	public Double getTwoYearsAgoContrast() {
		return twoYearsAgoContrast;
	}
	public void setTwoYearsAgoContrast(Double twoYearsAgoContrast) {
		this.twoYearsAgoContrast = twoYearsAgoContrast;
	}
	public BigDecimal getOneYearsAgoAccomplish() {
		return oneYearsAgoAccomplish;
	}
	public void setOneYearsAgoAccomplish(BigDecimal oneYearsAgoAccomplish) {
		this.oneYearsAgoAccomplish = oneYearsAgoAccomplish;
	}
	public Double getOneYearsAgoContrast() {
		return oneYearsAgoContrast;
	}
	public void setOneYearsAgoContrast(Double oneYearsAgoContrast) {
		this.oneYearsAgoContrast = oneYearsAgoContrast;
	}
	public BigDecimal getNewYearsAgoAccomplish() {
		return newYearsAgoAccomplish;
	}
	public void setNewYearsAgoAccomplish(BigDecimal newYearsAgoAccomplish) {
		this.newYearsAgoAccomplish = newYearsAgoAccomplish;
	}
	public Double getNewYearsAgoContrast() {
		return newYearsAgoContrast;
	}
	public void setNewYearsAgoContrast(Double newYearsAgoContrast) {
		this.newYearsAgoContrast = newYearsAgoContrast;
	}
	public BigDecimal getSixYearsAgoAccomplish() {
		return sixYearsAgoAccomplish;
	}
	public void setSixYearsAgoAccomplish(BigDecimal sixYearsAgoAccomplish) {
		this.sixYearsAgoAccomplish = sixYearsAgoAccomplish;
	}
	public Double getSixYearsAgoContrast() {
		return sixYearsAgoContrast;
	}
	public void setSixYearsAgoContrast(Double sixYearsAgoContrast) {
		this.sixYearsAgoContrast = sixYearsAgoContrast;
	}
	
	
}
