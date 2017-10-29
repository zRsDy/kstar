package com.ibm.kstar.i18n.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.xsnake.web.action.BaseAction;

import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/language")
public class I18nAction extends BaseAction{

	@NoRight
	@ResponseBody
	@RequestMapping("/change")
	public String change(String language,HttpServletRequest request){
		if(language == null){
			Locale locale = new Locale("zh", "CN"); 
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
			return sendSuccessMessage();
		}
		if(language.equals("zh")){
	        Locale locale = new Locale("zh", "CN"); 
	        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
	        return sendSuccessMessage();
	    }
	    else if(language.equals("en")){
	        Locale locale = new Locale("en", "US"); 
	        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
	        return sendSuccessMessage();
	    }
		Locale locale = new Locale("zh", "CN"); 
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
		return sendSuccessMessage();
	}
	
}
