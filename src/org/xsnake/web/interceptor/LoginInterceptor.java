package org.xsnake.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.xsnake.web.action.JsonResult;
import org.xsnake.web.session.SessionManager;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private String[] excludeUrls;
	
	private String loginPage;
	
    Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
	
    @Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (request.getRequestURI().indexOf(exc)>-1) {
					return super.preHandle(request, response, handler);
				}
			}
		}
		
		if(request.getRequestURI().indexOf(loginPage) > -1){
			return super.preHandle(request, response, handler);
		}
		
		String jsessionId = request.getParameter("JSESSIONID");
		HttpSession session = null;
//		if(StringUtil.isNotEmpty(jsessionId)){
//			session = SessionManager.getInstance().getSession(jsessionId);
//		}else{
			session = request.getSession();
//		}
		SessionManager.getInstance().getMappingSession(jsessionId, session);
		
		if(session.getAttribute("USER")==null){
			
			if (request.getHeader("accept").indexOf("application/json") > -1 ||
					(
					request.getContentType()!=null && (
					request.getContentType().indexOf("application/json") > -1  || 
					request.getContentType().indexOf("application/x-www-form-urlencoded") > -1)
					)
					|| "JSON".equalsIgnoreCase(request.getParameter("_ContentType"))
					|| "JSON".equalsIgnoreCase(request.getParameter("_contentType"))
					|| "JSON".equalsIgnoreCase(request.getParameter("_contenttype"))
					){
				
					try {
						PrintWriter writer = null;
						try{
							response.setStatus(401);
							response.setContentType("application/json");
							response.setCharacterEncoding("utf-8");
							writer = response.getWriter();
							String message = JsonResult.toErrorJson(loginPage);
							writer.write(message);
						    writer.flush();
						}finally{
							if(writer !=null){
								writer.close();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else{
			
					response.sendRedirect(request.getContextPath()+loginPage);
			}
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
	
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}
}
