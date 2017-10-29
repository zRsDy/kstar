package com.ibm.kstar.interceptor.system.permission;

import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IMenu;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.cache.CacheData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.xsnake.web.action.JsonResult;
import org.xsnake.web.session.SessionManager;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	ICorePermissionService corePermissionService;
	
	private String[] excludeUrls;
	
	private String loginPage;
	
	private String noRightPage;
	
	/**
	 * 拦截器
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		ServletContext application = request.getSession().getServletContext();

        String jsessionId = request.getParameter("JSESSIONID");
        HttpSession session = request.getSession();
        if(StringUtil.isNotEmpty(jsessionId)){
            SessionManager.getInstance().getMappingSession(jsessionId,session);
        }

		if (application.getAttribute("menuList") == null) {
            synchronized (application) {
				if (application.getAttribute("menuList") == null) {
					List<IMenu> menuList = corePermissionService.getSystemMenuList("CRM");
					application.setAttribute("menuList", menuList);
				}
			}
		}
		application.setAttribute("cacheData",CacheData.getInstance());
		return validate(request, response, handler);
	}


	/**
	 * 验证权限
	 */
	private boolean validate(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception, IOException {

		String jsessionId = request.getParameter("JSESSIONID");
		HttpSession session = null;
//		if(StringUtil.isNotEmpty(jsessionId)){
//			session = SessionManager.getInstance().getSession(jsessionId);
//			if(session == null){
//				session = request.getSession();
//			}
//		}else{
			session = request.getSession();
//		}
		if(StringUtil.isNotEmpty(jsessionId)){
			SessionManager.getInstance().getMappingSession(jsessionId, session);
		}
		
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (request.getServletPath().indexOf(exc)>-1) {
					return super.preHandle(request, response, handler);
				}
			}
		}
		if(request.getServletPath().indexOf(loginPage) > -1){
			return super.preHandle(request, response, handler);
		}
		if(request.getServletPath().indexOf(noRightPage) > -1){
			return super.preHandle(request, response, handler);
		}
		//判断有没有登录
		UserObject userObject = (UserObject)session.getAttribute("LOGIN_USER");
		//已经登录判断是否有权限
		if(userObject != null){
			if(handler instanceof HandlerMethod){
				NoRight noRight = ((HandlerMethod)handler).getMethod().getAnnotation(NoRight.class);
				if(noRight!=null){
					return super.preHandle(request, response, handler);
				}
			}
			String path = request.getServletPath();
			boolean hasPermission = userObject.hasPermissionPath(path);
			if(hasPermission){
				return super.preHandle(request, response, handler);
			}
		}
		noRight(userObject,request, response,request.getRequestURI());
		return false;
	}

	private void noRight(UserObject userObject,HttpServletRequest request, HttpServletResponse response,String uri) throws IOException {
		if (isAjaxRequest(request)){
			try {
				PrintWriter writer = null;
				try{
					response.setContentType("application/json");
					response.setCharacterEncoding("utf-8");
					writer = response.getWriter();
					String message = null;
					if(userObject == null){
						response.setStatus(401);
						message = JsonResult.toErrorJson(loginPage);
					}else{
						response.setStatus(403);
						message = JsonResult.toErrorJson("无权访问" + uri);
					}
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
			if(userObject == null){
				response.sendRedirect(request.getContextPath()+loginPage+"?login=session");
			}else{
				response.sendRedirect(request.getContextPath()+noRightPage+"?uri="+uri);
			}
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return request.getHeader("accept").indexOf("application/json") > -1 
				|| (request.getContentType()!=null && (
					request.getContentType().indexOf("application/json") > -1  || 
					request.getContentType().indexOf("application/x-www-form-urlencoded") > -1)
				)
				|| "JSON".equalsIgnoreCase(request.getParameter("_ContentType"))
				|| "JSON".equalsIgnoreCase(request.getParameter("_contentType"))
				|| "JSON".equalsIgnoreCase(request.getParameter("_contenttype"));
	}

	public String[] getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getNoRightPage() {
		return noRightPage;
	}

	public void setNoRightPage(String noRightPage) {
		this.noRightPage = noRightPage;
	}
	
}
