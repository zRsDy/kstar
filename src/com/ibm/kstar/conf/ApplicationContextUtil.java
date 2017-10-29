package com.ibm.kstar.conf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.kstar.api.system.permission.UserObject;

public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        ApplicationContextUtil.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }
    
    public static Object getBean(String beanName){
		return ApplicationContextUtil.context.getBean(beanName);
	}
    
    public static UserObject getUserObject(){
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	UserObject userObject = (UserObject)request.getSession().getAttribute("LOGIN_USER");
    	return userObject;
    }
}
