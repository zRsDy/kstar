package com.ibm.kstar.impl.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Value {

	Double m01 = 0d;
	Double m02 = 0d;
	Double m03 = 0d;
	Double m04 = 0d;
	Double m05 = 0d;
	Double m06 = 0d;
	Double m07 = 0d;
	Double m08 = 0d;
	Double m09 = 0d;
	Double m10 = 0d;
	Double m11 = 0d;
	Double m12 = 0d;
	Double totle = 0d;
	
	public List<String> toList(){
		List<String> list = new ArrayList<String>();
		DecimalFormat    df   = new DecimalFormat("#.##"); 
		list.add(m01==null ? "0":df.format(m01));
		list.add(m02==null ? "0":df.format(m02));
		list.add(m03==null ? "0":df.format(m03));
		list.add(m04==null ? "0":df.format(m04));
		list.add(m05==null ? "0":df.format(m05));
		list.add(m06==null ? "0":df.format(m06));
		list.add(m07==null ? "0":df.format(m07));
		list.add(m08==null ? "0":df.format(m08));
		list.add(m09==null ? "0":df.format(m09));
		list.add(m10==null ? "0":df.format(m10));
		list.add(m11==null ? "0":df.format(m11));
		list.add(m12==null ? "0":df.format(m12));
		return list;
	}
	
	public List<String> toListOne(){
		List<String> list = new ArrayList<String>();
		DecimalFormat    df   = new DecimalFormat("#.##"); 
		list.add(m01==null ? "0":df.format(m01));
		list.add(m02==null ? "0":df.format(m02));
		list.add(m03==null ? "0":df.format(m03));
		list.add(m04==null ? "0":df.format(m04));
		list.add(m05==null ? "0":df.format(m05));
		list.add(m06==null ? "0":df.format(m06));
		list.add(m07==null ? "0":df.format(m07));
		list.add(m08==null ? "0":df.format(m08));
		list.add(m09==null ? "0":df.format(m09));
		list.add(m10==null ? "0":df.format(m10));
		list.add(m11==null ? "0":df.format(m11));
		list.add(m12==null ? "0":df.format(m12));
		list.add(totle==null?"0":df.format(totle));
		return list;
	}

	public Double getTotle() {
		return totle;
	}

	public void setTotle(Double totle) {
		this.totle = totle;
	}

	public Double getM01() {
		return m01;
	}

	public void setM01(Double m01) {
		this.m01 = m01;
	}

	public Double getM02() {
		return m02;
	}

	public void setM02(Double m02) {
		this.m02 = m02;
	}

	public Double getM03() {
		return m03;
	}

	public void setM03(Double m03) {
		this.m03 = m03;
	}

	public Double getM04() {
		return m04;
	}

	public void setM04(Double m04) {
		this.m04 = m04;
	}

	public Double getM05() {
		return m05;
	}

	public void setM05(Double m05) {
		this.m05 = m05;
	}

	public Double getM06() {
		return m06;
	}

	public void setM06(Double m06) {
		this.m06 = m06;
	}

	public Double getM07() {
		return m07;
	}

	public void setM07(Double m07) {
		this.m07 = m07;
	}

	public Double getM08() {
		return m08;
	}

	public void setM08(Double m08) {
		this.m08 = m08;
	}

	public Double getM09() {
		return m09;
	}

	public void setM09(Double m09) {
		this.m09 = m09;
	}

	public Double getM10() {
		return m10;
	}

	public void setM10(Double m10) {
		this.m10 = m10;
	}

	public Double getM11() {
		return m11;
	}

	public void setM11(Double m11) {
		this.m11 = m11;
	}

	public Double getM12() {
		return m12;
	}

	public void setM12(Double m12) {
		this.m12 = m12;
	}

	public Double getTotalM01() {
		return m01;
	}
	
	public Double getTotalM02() {
		return getTotalM01() + m02;
	}
	
	public Double getTotalM03() {
		return getTotalM02() + m03;
	}
	
	public Double getTotalM04() {
		return getTotalM03() + m04;
	}
	
	public Double getTotalM05() {
		return getTotalM04() + m05;
	}
	
	public Double getTotalM06() {
		return getTotalM05() + m06;
	}

	public Double getTotalM07() {
		return getTotalM06() + m07;
	}
	
	public Double getTotalM08() {
		return getTotalM07() + m08;
	}
	
	public Double getTotalM09() {
		return getTotalM08() + m09;
	}
	
	public Double getTotalM10() {
		return getTotalM09() + m10;
	}
	
	public Double getTotalM11() {
		return getTotalM10() + m11;
	}
	
	public Double getTotalM12() {
		return getTotalM11() + m12;
	}
}
