package com.ibm.kstar.autocoding;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.utils.StringUtil;

@Controller
@RequestMapping("/")
public class AutoCodingAction extends BaseAction{

	@RequestMapping(value="/autoCoding" ,method = RequestMethod.GET)
	public String autoCoding(){
		return forward("autoCoding");
	}
	
	@RequestMapping(value="/autoCoding" ,method = RequestMethod.POST)
	public String autoCoding(String className,String modelName ,Model model){
		if(StringUtil.isEmpty(className)){
			throw new AnneException("实例名不能为空");
		}
		List<Field> fields = getFieldNames(className);
		model.addAttribute("fields",fields);
		model.addAttribute("className",className);
		model.addAttribute("modelName",modelName);
		return forward("autoCoding2");
	}

	private List<Field> getFieldNames(String name) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new AnneException("您输入的实例名没有找到对应的Class类");
		}
		Field[] fields = clazz.getDeclaredFields();
		List<Field> _fields = new ArrayList<Field>();
		for(Field field : fields){
			Column column = field.getAnnotation(Column.class);
			if(column != null){
				_fields.add(field);
			}
		}
		return _fields;
	}
	
	@RequestMapping(value="/autoCoding2" ,method = RequestMethod.POST)
	public String autoCoding2(BuilderBean builderBean, HttpServletRequest request,Model model){
		Map<String, String> viewNameMap = new HashMap<>();
		Map<String, String> valueSourceMap = new HashMap<>();
		Map<String,String> requiredMap = new HashMap<>();
		Map<String,String> placeholderMap = new HashMap<>();
		Map<String,String> tipMap = new HashMap<>();
		List<Field> fields = getFieldNames(builderBean.getClassName());
		for(Field field : fields){
			String viewName = request.getParameter("viewName_"+field.getName());
			if(StringUtil.isNotEmpty(viewName)){
				viewNameMap.put(field.getName(), viewName);
			}
			String valueSource = request.getParameter("valueSource_"+field.getName());
			if(StringUtil.isNotEmpty(valueSource)){
				valueSourceMap.put(field.getName(), valueSource);
			}
			String required = request.getParameter("required_"+field.getName());
			if(StringUtil.isNotEmpty(required)){
				requiredMap.put(field.getName(), required);
			}
			String placeholder = request.getParameter("placeholder_"+field.getName());
			if(StringUtil.isNotEmpty(placeholder)){
				placeholderMap.put(field.getName(), placeholder);
			}
			String tip = request.getParameter("tip_"+field.getName());
			if(StringUtil.isNotEmpty(tip)){
				tipMap.put(field.getName(), tip);
			}
		}
		builderBean.setFields(fields);
		builderBean.setViewNameMap(viewNameMap);
		builderBean.setValueSourceMap(valueSourceMap);
		builderBean.setRequiredMap(requiredMap);
		builderBean.setPlaceholderMap(placeholderMap);
		builderBean.setTipMap(tipMap);
		setSessionAttribute(request.getSession().getId(),builderBean);
		return forward("autoCoding3");
	}
	
	@RequestMapping(value="/interfaces" ,method = RequestMethod.GET)
	public String interfaces(HttpServletRequest request,Model model){
		BuilderBean builderBean = (BuilderBean) getSessionAttribute(request.getSession().getId());
		if(builderBean == null){
			return forward("error");
		}
		setModelAttribute(model, builderBean);
		return forward("auto_interface");
	}

	public static void main(String[] args) {
		Date d = new Date();
		System.out.println(d.getClass().getName());
	}
	
	private void setModelAttribute(Model model, BuilderBean builderBean) {
		model.addAttribute("fields",builderBean.fields);
		model.addAttribute("className",builderBean.className);
		String shortClassName = builderBean.className.substring(builderBean.className.lastIndexOf(".")+1);
		model.addAttribute("shortClassName",shortClassName);
		model.addAttribute("dShortClassName",shortClassName.substring(0, 1).toLowerCase() + shortClassName.substring(1));
		model.addAttribute("modelName",builderBean.modelName);
		model.addAttribute("viewNameMap",builderBean.viewNameMap);
		model.addAttribute("valueSourceMap",builderBean.valueSourceMap);
		model.addAttribute("requiredMap",builderBean.requiredMap);
		model.addAttribute("placeholderMap",builderBean.placeholderMap);
		model.addAttribute("tipMap",builderBean.tipMap);
		Map<String, String> visableMap = new HashMap<>();
		if(builderBean.visables!=null){
			for(String visable : builderBean.visables){
				visableMap.put(visable, visable);
			}
		}
		model.addAttribute("visableMap",visableMap);
	}
	
	@RequestMapping(value="/interfaceImpl" ,method = RequestMethod.GET)
	public String interfaceImpl(HttpServletRequest request,Model model){
		BuilderBean builderBean = (BuilderBean) getSessionAttribute(request.getSession().getId());
		if(builderBean == null){
			return forward("error");
		}
		setModelAttribute(model, builderBean);
		return forward("auto_interfaceImpl");
	}
	
	@RequestMapping(value="/auto_list" ,method = RequestMethod.GET)
	public String auto_list(HttpServletRequest request,Model model){
		BuilderBean builderBean = (BuilderBean) getSessionAttribute(request.getSession().getId());
		if(builderBean == null){
			return forward("error");
		}
		setModelAttribute(model, builderBean);
		return forward("auto_list");
	}
	
	@RequestMapping(value="/auto_edit" ,method = RequestMethod.GET)
	public String auto_edit(HttpServletRequest request,Model model){
		BuilderBean builderBean = (BuilderBean) getSessionAttribute(request.getSession().getId());
		if(builderBean == null){
			return forward("error");
		}
		setModelAttribute(model, builderBean);
		return forward("auto_edit");
	}
	
	@RequestMapping(value="/auto_action" ,method = RequestMethod.GET)
	public String auto_action(HttpServletRequest request,Model model){
		BuilderBean builderBean = (BuilderBean) getSessionAttribute(request.getSession().getId());
		if(builderBean == null){
			return forward("error");
		}
		setModelAttribute(model, builderBean);
		return forward("auto_action");
	}
	
}
