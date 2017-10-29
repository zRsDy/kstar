package org.xsnake.web.exception;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.log.ILogService;
import com.ibm.kstar.log.LogObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.xsnake.web.action.JsonResult;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.utils.ActionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ExceptionResolver extends SimpleMappingExceptionResolver{

    @Autowired
    private ILogService logService;


	@Override
    public ModelAndView doResolveException(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex) {

        if (ex != null) {
            String message = getExceptionMessage(ex);
            if (message != null) {
                try {
					LogObject log = new LogObject();
					UserObject user = getUserObject();

					PageCondition condition = new PageCondition();
					ActionUtil.requestToCondition4log(condition, request);
					log.setArgsJson(JSON.toJSONString(condition));
					log.setDate(new Date());
					log.setIp(request.getRemoteHost());
					log.setMoudle("错误日志");

					log.setUrl(request.getRequestURL()  + (request.getQueryString() == null ? "" : "?"+request.getRequestURL()));

					if (user != null) {
						log.setUserId(user.getEmployee().getId());
						log.setUsername(user.getEmployee().getName());
						log.setPositionId(user.getPosition().getId());
						log.setPositionName(user.getPosition().getName());
						log.setOrgId(user.getOrg().getId());
						log.setOrgName(user.getOrg().getName());
					}

                    log.setDetail(message);
                    logService.log(log);
                } catch (Exception e) {
                    System.out.println("========== 保存错误日志错误 ==========");
                    System.out.println(e.getMessage());
                    System.out.println("====================================");
                }
            }
        }

		if(ex!=null)
			ex.printStackTrace();
		if (request.getHeader("accept").indexOf("application/json") > -1 ||
			(
			request.getContentType()!=null && (
			request.getContentType().indexOf("application/json") > -1  || 
			request.getContentType().indexOf("application/x-www-form-urlencoded") > -1)
			)
			|| "JSON".equalsIgnoreCase(request.getParameter("_ContentType"))
			|| "JSON".equalsIgnoreCase(request.getParameter("_contentType"))
			|| "JSON".equalsIgnoreCase(request.getParameter("_contenttype"))
			|| (request.getContentType() !=null && request.getContentType().indexOf("multipart/form-data") > -1) 
			){
			
			if(ex instanceof AnneException){
				AnneException ve = (AnneException)ex;
				try {
					printErrorMessage(response,ve.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					printErrorMessage(response,ex.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			try {
				String error = "未定义错误";
				if(ex instanceof AnneException){
					AnneException ve = (AnneException)ex;
					error = ve.getMessage();
				}
				response.sendError(500);
				request.setAttribute("error", error);
			} catch (IOException e) {
				e.printStackTrace();
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

	private void printErrorMessage(HttpServletResponse response,String messages)throws IOException {
		PrintWriter writer = null;
		try{
			response.setStatus(500);
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			writer = response.getWriter();
			String message = JsonResult.toErrorJson(messages);
			writer.write(message);
		    writer.flush();
		}finally{
			if(writer !=null){
				writer.close();
			}
		}
	}
}
