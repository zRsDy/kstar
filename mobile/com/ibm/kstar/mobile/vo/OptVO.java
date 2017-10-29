package com.ibm.kstar.mobile.vo;

import java.io.Serializable;

public class OptVO implements Serializable {

	private static final long serialVersionUID = 8429179539501608020L;
	/**
	 * 操作类型
	 */
	private String optType;
	/**
	 * 操作名称
	 */
	private String optName;
	/**
	 * 是否需要选择下一步参与人
	 */
	private boolean selectParticpant;
	/**
	 * 参与人类型
	 */
	private String particpantType;
	/**
	 * 参与人是否可以多选
	 */
	private boolean particpantMultiple;
	
	public OptVO(){}
	
	public OptVO(String optType,String optName,boolean selectParticpant,String particpantType,boolean particpantMultiple){
		this.optType = optType;
		this.optName = optName;
		this.selectParticpant = selectParticpant;
		this.particpantType = particpantType;
		this.particpantMultiple = particpantMultiple;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public boolean isSelectParticpant() {
		return selectParticpant;
	}

	public void setSelectParticpant(boolean selectParticpant) {
		this.selectParticpant = selectParticpant;
	}

	public String getParticpantType() {
		return particpantType;
	}

	public void setParticpantType(String particpantType) {
		this.particpantType = particpantType;
	}

	public boolean isParticpantMultiple() {
		return particpantMultiple;
	}

	public void setParticpantMultiple(boolean particpantMultiple) {
		this.particpantMultiple = particpantMultiple;
	}
	
	
}
