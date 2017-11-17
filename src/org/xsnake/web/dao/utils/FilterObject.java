package org.xsnake.web.dao.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.kstar.api.system.permission.UserObject;


/**
 * 过滤条件对象
 * @author wansheng
 *
 */
public class FilterObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<FilerRuler> rules = new ArrayList<FilerRuler>();
	
	private List<FilerRuler> orRules = new ArrayList<FilerRuler>();
	
	private List<OrderRuler> orders = new ArrayList<OrderRuler>();
	
	private Class<?> clazz;
	
	private UserObject user;
	
	public FilterObject(){}
	
	public FilterObject(Class<?> clazz){
		this.clazz = clazz;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public FilterObject setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

	public List<OrderRuler> getOrders() {
		return orders;
	}

	
	
	public FilterObject addCondition(String field,String op,String data){
		if(data == null){
			return this;
		}
		FilerRuler fr = new FilerRuler();
		fr.setField(field);
		fr.setOp(op);
		fr.setData(data);
		rules.add(fr);
		return this;
	}
	
	public FilterObject addOrCondition(String field,String op,String data){
//		if(data == null){
//			return this;
//		}
		FilerRuler fr = new FilerRuler();
		fr.setField(field);
		fr.setOp(op);
		fr.setData(data);
		orRules.add(fr);
		return this;
	}
	
	public FilterObject addOrderBy(String field,String ascOrDesc){
		OrderRuler or = new OrderRuler();
		or.setField(field);
		or.setAscOrDesc(ascOrDesc);
		orders.add(or);
		return this;
	}
	
	public List<FilerRuler> getOrRules() {
		return orRules;
	}

	public void setOrRules(List<FilerRuler> orRules) {
		this.orRules = orRules;
	}
	
	public void setOrders(List<OrderRuler> orders) {
		this.orders = orders;
	}

	public List<FilerRuler> getRules() {
		return rules;
	}

	public void setRules(List<FilerRuler> rules) {
		this.rules = rules;
	}

	public UserObject getUser() {
		return user;
	}

	public void setUser(UserObject user) {
		this.user = user;
	}
	
}
