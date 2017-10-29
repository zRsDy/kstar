package com.ibm.kstar.log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.message.service.MessageAdapter;
@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class MethodLogServiceImpl  extends MessageAdapter<MethodLogger> implements IMethodLogService {

	@Autowired
	BaseDao baseDao;
	
	/**
	 * 队列的名称
	 */
	@Override
	public String getQueueName() {
		return "ErpMethodLog";
	}
	
	/**
	 * 消费者
	 */
	@Override
	public void consume(MethodLogger methodLogger) {
		baseDao.saveOrUpdate(methodLogger);
	}
	
	/**
	 * 生产者
	 */
	@Override
	public void log(MethodLogger methodLogger) {
		produce(methodLogger);
	}

	@Override
	public boolean isProducer() {
		return true;
	}

	@Override
	public boolean isConsumer() {
		return true;
	}

	@Override
	public MethodLogger getMethodLogger() {
		MethodLogger methodLogger = new MethodLogger();
		UserObject user = getUserObject();
		methodLogger.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
		methodLogger.setInterfaceStartDate(new Date());
		methodLogger.setUserOrgId(user.getOrg().getId());
		methodLogger.setUserOrgName(user.getOrg().getName());
		methodLogger.setUserNum(user.getEmployeeNo());
		methodLogger.setUserName(user.getEmployeeName());
		methodLogger.setUserEmployeeId(user.getEmployee().getId());
		methodLogger.setUserPosition(user.getPosition().getName());
		methodLogger.setUserPositionId(user.getPosition().getId());
		return methodLogger;
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

	/**
	 * 设置接口名称与接口参数
	 * MethodLogger日志对象
	 * functionName方法名
	 * i设置参数1/2/3/4/5
	 * objects参数不固定数组
	 */
	@Override
	public void setFunctionNameAndParameter(MethodLogger methodLogger,String functionName,int i,Object ...objects) {
		switch(i) {
			case 1:
				methodLogger.setInterfaceParameter1(JSON.toJSONString(objects));
				methodLogger.setInterfaceName1(functionName);
				break;
			case 2:
				methodLogger.setInterfaceParameter2(JSON.toJSONString(objects));
				methodLogger.setInterfaceName2(functionName);
				break;
			case 3:
				methodLogger.setInterfaceParameter3(JSON.toJSONString(objects));
				methodLogger.setInterfaceName3(functionName);
				break;
			case 4:
				methodLogger.setInterfaceParameter4(JSON.toJSONString(objects));
				methodLogger.setInterfaceName4(functionName);
				break;
			case 5:
				methodLogger.setInterfaceParameter5(JSON.toJSONString(objects));
				methodLogger.setInterfaceName5(functionName);
				break;
		}
	}

	/**
	 * 设置接口名称与接口参数
	 * MethodLogger日志对象
	 * i设置参数1/2/3/4/5
	 * objects参数不固定数组
	 */
	@Override
	public void setReturnData(MethodLogger methodLogger,int i,Object ...objects) {
		if(objects.length>0) {
			switch(i) {
				case 1:
					methodLogger.setInterfaceReturnData1(JSON.toJSONString(objects));
					break;
				case 2:
					methodLogger.setInterfaceReturnData2(JSON.toJSONString(objects));
					break;
				case 3:
					methodLogger.setInterfaceReturnData3(JSON.toJSONString(objects));
					break;
				case 4:
					methodLogger.setInterfaceReturnData4(JSON.toJSONString(objects));
					break;
				case 5:
					methodLogger.setInterfaceReturnData5(JSON.toJSONString(objects));
					break;
			}
		}
	}
	
	@Override
	public void exceptionLog(MethodLogger methodLogger, Exception exception) {
		if(StringUtil.isNullOrEmpty(exception.getMessage())) {
			log(methodLogger);
			methodLogger.setProcessingStatus("SUCCESS");
			methodLogger.setInterfaceReportDate(new Date());
		}else {
			methodLogger.setProcessingStatus("ERROR");
			methodLogger.setProcessingReport(getExceptionMessage(exception));
			methodLogger.setInterfaceReportDate(new Date());
			log(methodLogger);
		}
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
}
