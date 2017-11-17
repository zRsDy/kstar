package com.ibm.kstar.log;



public interface IMethodLogService{
	
	void log(MethodLogger methodLogger); 
	
	MethodLogger getMethodLogger(String from,String orderNumber);
	
	void setFunctionNameAndParameter(MethodLogger methodLogger,String functionName,int i,Object ...objects); 
	
	void setReturnDataNotes(boolean continueOrEnd,MethodLogger methodLogger,Exception exception,int i,Object ...objects);
}
