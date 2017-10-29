package org.xsnake.web.action;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.upload.UploadUtils;
import org.xsnake.web.utils.ExcelUtil;

import com.ibm.kstar.action.common.utils.CrmCustomDateEditor;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.i18n.service.I18nMessage;

/**
 * 这里封装了一些特色的方法
 *
 * 1、和session有关的操作请使用父级的setSessionAttribute,getSessionAttribute,sessionInvalidate 来操作，
 * 这样在集群环境中我们可以通过复写这些操作来达成切换session的存储方式，而不以至于面临大量的修改代码
 *
 * 2、sendSuccessMessage,sendErrorMessage封装为给客户端AJAX方法返回的结果，
 * 使用该返回值的方法需要增加注解@ResponseBody;
 *
 * 3、forward，redirect提供给action更加优雅的跳转。
 * forward,让我们的页面与action同在一个目录下，这样在维护与开发中会更加轻松
 * @author Administrator
 *
 */
public abstract class BaseAction implements SessionSupport ,AjaxSupport{

	public static final String SUFFIX = ".html";

	@InitBinder
	protected void initBaseBinder(WebDataBinder binder){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CrmCustomDateEditor(sdf,true));
	}

	private String getPackagePath() {
		String className = this.getClass().getPackage().getName();
		String packagePath = className.replaceAll("\\.", "/");
		return  "/WEB-INF/classes/" + packagePath + "/";
	}

	protected String forward(String path) {
		return getPackagePath() + path;
	}

	protected String redirect(String path){
		if(path.indexOf(SUFFIX) > -1){
			return "redirect:" + path;
		}
		return "redirect:" + path + SUFFIX;
	}

	public void setSessionAttribute(String key , Object obj){
		getSession().setAttribute(key, obj);
	}

	public void sessionInvalidate(){
		getSession().invalidate();
	}

	public Object getSessionAttribute(String key){
		return getSession().getAttribute(key);
	}

	public void removeAttribute(String key){
		getSession().removeAttribute(key);
	}


	public String sendSuccessMessage(Object result){
		return sendMessage(JsonResult.toSuccessJson(result));
	}

	public String sendErrorMessage(Object result){
		return sendMessage(JsonResult.toErrorJson(result));
	}

	public String sendSuccessMessage(){
		return sendMessage(JsonResult.toSuccessJson(null));
	}

	public String sendErrorMessage(){
		return sendMessage(JsonResult.toErrorJson(null));
	}

	public String sendMessage(String result){
//		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		HttpServletResponse response = ResponseContextHolder.getResponse();
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		Writer witer = null;
		try {
			witer = response.getWriter();
			witer.write(result);
			witer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(witer !=null){
				try {
					witer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public List<List<Object>> readImportFile(HttpServletRequest request) throws Exception {
		List<IUploadFile> fileList = UploadUtils.uploadFile(request, "importFile");
		IUploadFile file = null;
		if(fileList.size() > 0){
			file = fileList.get(0);
		}
		if(file == null){
			throw new Exception("没有找到上传文件");
		}

		List<List<Object>> list = null;
		try {
			list = ExcelUtil.readExcelPOI(file.getRealPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(list == null || list.size() == 0){
			throw new Exception("文件解析异常，请确认上传的文件是Excel类型的(xls或者xlsx为后缀的),或检查数据格式是否正确！");
		}
		return list;
	}

	public final static String USER = "LOGIN_USER";

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

	public List<List<Object>> getExcelData(HttpServletRequest request) {
		List<IUploadFile> fileList = UploadUtils.uploadFile(request, "importFile");
		IUploadFile file = null;
		if (fileList.size() > 0) {
			file = fileList.get(0);
		}
		if (file == null) {
			throw new AnneException("没有找到上传文件");
		}
		List<List<Object>> list = null;
		try {
			list = ExcelUtil.readExcelPOI(file.getRealPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (list == null || list.size() == 0) {
			throw new AnneException("文件解析异常，请确认上传的文件是Excel类型的(xls或者xlsx为后缀的),或检查数据格式是否正确！");
		}
		for(IUploadFile uf :fileList){
			File f = new File(uf.getRealPath());
			f.delete();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public boolean hasPermission(String code){
		Map<String,String> permissionMap = (Map<String,String>)getSession().getAttribute("permission");
		if(permissionMap.get(code)!=null){
			return true;
		}
		return false;
	}
	
	public I18nMessage getI18n(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		I18nMessage i18n = (I18nMessage)request.getAttribute("i18n");
		return i18n;
	}
	
	public String getI18nString(String key){
		return getI18n().get(key);
	}
}
