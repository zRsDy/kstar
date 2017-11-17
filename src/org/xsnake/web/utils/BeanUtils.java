package org.xsnake.web.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.xsnake.web.exception.AnneException;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class BeanUtils extends org.springframework.beans.BeanUtils{

	public static void copyPropertiesIgnoreNull(Object source,Object target){
		copyPropertiesIgnoreNull(source, target, null, (String[]) null);
	}
	
	public static <T> List<T> castList( Class<T> cls , List<Object[]> list) throws AnneException{
		List<T> result = new ArrayList<>(); 
		try{
			for(Object[] objs : list){
				List<?> l = Arrays.asList(objs);
				Constructor<T> c = cls.getConstructor(List.class);
				c.setAccessible(true);
				T obj = c.newInstance(l);
				result.add(obj);
			}
		}catch(Exception e){
			throw new AnneException(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 源码见：org.springframework.beans.BeanUtils
	 * 如果为复制属性时，其中值为空，则忽略不复制
	 * @param source
	 * @param target
	 * @param editable
	 * @param ignoreProperties
	 * @throws BeansException
	 */
	private static void copyPropertiesIgnoreNull(Object source, Object target, Class<?> editable, String... ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null &&
							ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							if(value != null){
								if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
									writeMethod.setAccessible(true);
								}
								writeMethod.invoke(target, value);
							}
						}
						catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map<? extends Object, Object> map) throws Exception{
		return convertMap(type, map, false);
	}
	
	/**
	 * 将一个 Map 对象转化为一个 JavaBean 使用时最好保证value值不为null 不支持枚举类型和 用户自定义类
	 * 
	 * @param type
	 *            要转化的javabean
	 * @param map
	 *            包含属性值的map map的key值为javabean属性 key值小写
	 * @return 转化出来的 JavaBean 对象
	 * @throws Exception
	 *             Exception
	 */

	@SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map<? extends Object, Object> map, boolean isUpperCase)
			throws Exception {
		// 创建 JavaBean 对象
		Object obj = null;

		// 获取类属性
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		obj = type.newInstance();
		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = null;
			if(isUpperCase){
				propertyName = descriptor.getName().toUpperCase();
			} else {
				propertyName = descriptor.getName();
			}
			
			if (map.containsKey(propertyName)) {
				String value = ConvertUtils.convert(map.get(propertyName));
				Object[] args = new Object[1];
				if(!StringUtils.isEmpty(value)){
					
					if(descriptor.getPropertyType() == Date.class ){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						try {
							args[0] = sdf.parse(value);
						} catch (Exception e) {
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							args[0] = sdf1.parse(value);
						}
						
					}else{
						args[0] = ConvertUtils.convert(value,descriptor.getPropertyType());
					}
					
				}else{
					if(descriptor.getPropertyType() == int.class 
							|| descriptor.getPropertyType() == double.class 
							|| descriptor.getPropertyType() == float.class){
						args[0] = 0;
					}else{
						args[0] = null;
					}
				}
				
				if(descriptor.getWriteMethod()!=null){
					descriptor.getWriteMethod().invoke(obj, args);
				}
				
			}
		}
		return obj;
	}

    /**
     * 判断某字段属性是否在当前类或父类中。如果当前类和直接父类中都没有就返回false
     *
     * @return
     * @paramclazz
     * @paramfieldName
     */
    public static boolean hasField(Class clazz, String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                Class<?> clazzSuper = Class.forName(clazz.getSuperclass().getName());
                clazzSuper.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                return false;
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }
    
    /**
	 * get all the property names and Its values
	 * @param object
	 * @param methodName
	 */
	public static String printBean(Object object){
		StringBuffer sbf = new StringBuffer();
		Method[] mds = object.getClass().getDeclaredMethods();
		mds = object.getClass().getMethods();
		for(Method m:mds){
			String sMethod = m.getName();
			if(sMethod.startsWith("get")){
				try {
					sbf.append("  ");
					sbf.append(sMethod.substring(3));
					sbf.append("-->");
					sbf.append(m.invoke(object));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return sbf.toString();
	}
	
	/**
	 * check null value property if exist
	 * @param nspd
	 * @return
	 */
	public static StringBuilder hasNullValue(Object obj) {
		StringBuilder sbf = null;
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator val = vf.getValidator();
		Set<ConstraintViolation<Object>> set = val.validate(obj);
		if (set.size() > 0) {
			sbf = new StringBuilder();
			for (ConstraintViolation<Object> v : set) {
				sbf.append(v.getMessage());
			}
		}
		return sbf;
	}
}
