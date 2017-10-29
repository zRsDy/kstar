package org.xsnake.web.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContext;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.i18n.service.I18nMessage;
import com.ibm.kstar.log.ILogService;
import com.ibm.kstar.log.LogObject;

public class ResponseContextHolder extends HandlerInterceptorAdapter{

	private static ThreadLocal<HttpServletResponse> resMap = new ThreadLocal<>();
	
	@Autowired
	CacheData cacheData;
	
	@Autowired
	ILogService logService;
	
	final static String USER = "LOGIN_USER"; 
	
	public UserObject getUserObject() {
		HttpSession session = getSession();
		return ((UserObject)session.getAttribute(USER));
	}
	
	public HttpSession getSession(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//		String jsessionId = request.getParameter("JSESSIONID");
//		if(StringUtil.isNotEmpty(jsessionId)){
//			return SessionManager.getInstance().getSession(jsessionId);
//		}else{
			return request.getSession();
//		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		resMap.set(response);
		request.setAttribute("cache", cacheData);
		request.setAttribute("i18n",new I18nMessage(new RequestContext(request)));
		try{
			UserObject user = getUserObject();
			LogOperate logOperate = ((HandlerMethod)handler).getMethod().getAnnotation(LogOperate.class);
			if(user!=null && logOperate!=null){
				LogObject log = new LogObject();
				PageCondition condition = new PageCondition();
				ActionUtil.requestToCondition4log(condition, request);
				log.setArgsJson(JSON.toJSONString(condition));
				log.setDate(new Date());
				log.setIp(request.getRemoteHost());
				log.setMoudle(logOperate.module());
				log.setNotes(logOperate.notes());
				log.setUrl(request.getRequestURL()  + (request.getQueryString() == null ? "" : "?"+request.getRequestURL()));
				log.setUserId(user.getEmployee().getId());
				log.setUsername(user.getEmployee().getName());
				log.setPositionId(user.getPosition().getId());
				log.setPositionName(user.getPosition().getName());
				log.setOrgId(user.getOrg().getId());
				log.setOrgName(user.getOrg().getName());
				
				logService.log(log);
			}
		}catch(Exception e){}
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		resMap.set(null);
		request.setAttribute("cache", null);
		String jsessionId = request.getParameter("JSESSIONID");
		if(StringUtil.isNotEmpty(jsessionId)){
			request.getSession().invalidate();
		}
		super.afterCompletion(request, response, handler, ex);
	}
	
	public static HttpServletResponse getResponse(){
		return resMap.get();
	}
}
