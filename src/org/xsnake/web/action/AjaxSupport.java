package org.xsnake.web.action;

public interface AjaxSupport {

	String sendSuccessMessage();
	
	String sendSuccessMessage(Object result);
	
	String sendErrorMessage();
	
	String sendErrorMessage(Object result);
	
}
