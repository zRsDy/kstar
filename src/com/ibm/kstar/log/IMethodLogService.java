package com.ibm.kstar.log;



public interface IMethodLogService{
	
	void log(MethodLogger methodLogger); 
	
	MethodLogger getMethodLogger();
	
	void setFunctionNameAndParameter(MethodLogger methodLogger,String functionName,int i,Object ...objects); 
	
	void exceptionLog(MethodLogger methodLogger,Exception exception);
	
	void setReturnData(MethodLogger methodLogger,int i,Object ...objects);
}
