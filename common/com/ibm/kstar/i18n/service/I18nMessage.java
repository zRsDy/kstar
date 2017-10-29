package com.ibm.kstar.i18n.service;

import org.springframework.web.servlet.support.RequestContext;

public class I18nMessage {

	private RequestContext context;
	
	public I18nMessage(RequestContext context){
		this.context = context;
	}
	
	public String get(String code,String args1,String args2,String args3){
		return context.getMessage(code,new Object[]{args1,args2,args3},true);
	}
	
	public String get(String code,String args1,String args2){
		return context.getMessage(code,new Object[]{args1,args2},true);
	}
	
	public String get(String code,String args1){
		return context.getMessage(code,new Object[]{args1},true);
	}
	
	public String get(String code){
		return context.getMessage(code,null,true);
	}
	
}
