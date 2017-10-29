package com.ibm.kstar.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.log.MethodLogger;
import com.ibm.kstar.log.IMethodLogService;

public class ErpMethodInterceptor implements MethodInterceptor  {

	@Autowired
    private IMethodLogService MethodLogService;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		MethodLogger MethodLogger;
		ErpLogOperate erpLogOperate;
		
		if(method.isAnnotationPresent(ErpLogOperate.class)){//加了@ErpLogOperate注解，被拦截  
			erpLogOperate= method.getAnnotation(ErpLogOperate.class);
			MethodLogger = initStartErpMethodLog(erpLogOperate,invocation);
			Object rval = new Object();
			Exception exception = new Exception();
			
			try {
				rval = invocation.proceed();
				return rval;
			}catch(Exception e) {
				exception = e;
			}finally {
				if(StringUtil.isNullOrEmpty(exception.getMessage())) {
					MethodLogger = initEndErpMethodLog(MethodLogger,invocation);
					MethodLogService.log(MethodLogger);
				}else {
					MethodLogger = exceptionErpMethodLog(exception,MethodLogger,invocation);
					MethodLogService.log(MethodLogger);
				}
			}
		}else {
			return invocation.proceed();
		}
		return invocation.proceed();
	}
	
	private MethodLogger initStartErpMethodLog(ErpLogOperate erpLogOperate,MethodInvocation invocation) {
		MethodLogger MethodLogger = new MethodLogger();
		/*MethodLogger.setInterfaceName(invocation.getMethod().getDeclaringClass().getName()+invocation.getMethod().getName());
		MethodLogger.setInterfaceParameter(JSON.toJSONString(invocation.getArguments()));
		MethodLogger.setInterfaceStartDate(new Date());
		MethodLogger.setLogType(erpLogOperate.logType());
		UserObject user = getUserObject();
		MethodLogger.setInterfaceType(erpLogOperate.interfaceType());
		MethodLogger.setUserOrgId(user.getOrg().getId());
		MethodLogger.setUserOrgName(user.getOrg().getName());
		MethodLogger.setUserNum(user.getEmployeeNo());
		MethodLogger.setUserName(user.getEmployeeName());
		MethodLogger.setUserEmployeeId(user.getEmployee().getId());
		MethodLogger.setUserPosition(user.getPosition().getName());
		MethodLogger.setUserPositionId(user.getPosition().getId());
		*/
		return MethodLogger;
	}
	
	private MethodLogger initEndErpMethodLog(MethodLogger MethodLogger,MethodInvocation invocation) {
		Method method = invocation.getMethod();
		//MethodLogger.setInterfaceReportDate(new Date());
		method.getReturnType();
		method.getExceptionTypes();
		return MethodLogger;
	}
	
	private MethodLogger exceptionErpMethodLog(Exception exception,MethodLogger MethodLogger,MethodInvocation invocation) {
		Method method = invocation.getMethod();
		if(StringUtil.isNullOrEmpty(exception.getMessage())) {
			//MethodLogger.setProcessingStatus("SUCCESS");
			//MethodLogger.setProcessingReport(method.getReturnType().getName());
		}else {
			//MethodLogger.setProcessingStatus("ERROR");
			//MethodLogger.setProcessingReport(getExceptionMessage(exception));
		}
		///MethodLogger.setInterfaceReportDate(new Date());
		//MethodLogger.setInterfaceReportData(method.getReturnType().getName());
		method.getExceptionTypes();
		return MethodLogger;
	}
	
	 private String getExceptionMessage(Exception ex) {
	        ByteArrayOutputStream buf = null;
	        try {
	            buf = new java.io.ByteArrayOutputStream();
	            ex.printStackTrace(new java.io.PrintWriter(buf, true));
	            return buf.toString();
	        } catch (Exception ignored) {

	        } finally {
	            if (buf != null) {
	                try {
	                    buf.close();
	                } catch (IOException ignored) {

	                }
	            }
	        }
	        return null;
	}
	 
	final static String USER = "LOGIN_USER";
	public UserObject getUserObject() {
		HttpSession session = getSession();
		return ((UserObject)session.getAttribute(USER));
	} 
	
	public HttpSession getSession(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession();
	}
}
