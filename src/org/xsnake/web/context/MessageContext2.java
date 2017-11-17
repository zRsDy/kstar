package org.xsnake.web.context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.utils.ReflectionUtil;

import com.google.common.io.Files;


public class MessageContext2 implements ApplicationContextAware {
	
	private static ApplicationContext context = null;
	
	Map<String,RequestMappingAction> map = new HashMap<String,RequestMappingAction>();
	
	Map<String,RequestMappingAction> pathMap = new HashMap<String,RequestMappingAction>();
	
	@Override
	public void setApplicationContext(ApplicationContext context)throws BeansException {
		MessageContext2.context = context;
		
		String[] names = context.getBeanDefinitionNames();
		for (String name : names) {
			Object obj = context.getBean(name);
			Object target = ReflectionUtil.getTarget(obj);
			RequestMapping rm = target.getClass().getAnnotation(RequestMapping.class);
			RequestMappingAction rma = null;
			if (rm != null) {
				rma = map.get(rm.value()[0]);
				if(rma == null){
					rma = new RequestMappingAction();
					rma.setValue(rm.value()[0]);
					rma.setFile(target.getClass().getName());
					map.put(rm.value()[0], rma);
				}
			}
			if(rma != null){
				Method[] methods = target.getClass().getMethods();
				for(Method method : methods){
					rm = method.getAnnotation(RequestMapping.class);
					if (rm != null) {
						rma.add(rm.value()[0]);
					}
				}
			}
		}
		StringBuffer s = new StringBuffer();
		Collection<RequestMappingAction> l = map.values();
		for(RequestMappingAction rma : l){
			for(String path : rma.getChildList()){
				System.out.println(rma.getValue()+path +"      "+ rma.getFile());
				s.append(rma.getValue()+path +"      "+ rma.getFile()+"\n");
				
//				pathMap.put(rma.getValue()+path, rma);
			}
		}
		
		try {
			Files.write(s.toString().getBytes(), new File("c:\\a.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getMessage(String code,String... args){
		return MessageContext2.context.getMessage(code, args, null);
	}
	
	public static Object getBean(String beanName){
		
		return MessageContext2.context.getBean(beanName);
		
	}
	
	public String getFile(String path){
		RequestMappingAction rma = pathMap.get(path);
		if(rma == null){
			return null;
		}
		return rma.getFile();
	}

}
