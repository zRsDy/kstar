package com.ibm.kstar.mobile.enums;

/**
 * 流程操作的枚举值
 * @author jian.xu
 *
 */
public enum OptEnum {
	
	AGREEN("agreen", "同意"),
	DISPATCH("dispatch", "指派"),
	COMMUNICATE("communicate", "沟通"),
	ENTRUST("entrust", "委托"),
	REJECT("reject", "驳回"),
	DESTROY("destroy", "销毁"),
	CHOOSE("choose", "选择");
	
	private final String value;
	
	private final String name;

	private OptEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String value() {
		return this.value;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(String val){
		if(value.equals(val))
			return true;
		return false;
	}
	
	public static OptEnum getOptEnum(String val){
		for(OptEnum e : OptEnum.values()){
			if(e.value().equals(val))
				return e;
		}
		return null;
	}
}
