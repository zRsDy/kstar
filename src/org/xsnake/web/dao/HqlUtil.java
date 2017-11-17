package org.xsnake.web.dao;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xsnake.web.dao.utils.FilerRuler;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.dao.utils.JoinRuler;
import org.xsnake.web.dao.utils.OrderRuler;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;
import com.ibm.kstar.permission.utils.PermissionUtil;

import org.xsnake.web.utils.BeanUtils;

/**
 * 组装SQL 工具类
 * @author wansheng
 *
 */
public class HqlUtil{
	
	public static Map<String, String> SQLCONDITON = new HashMap<String,String>();
	static{
		SQLCONDITON.put("eq", " = ");  			 // 相等
		SQLCONDITON.put("ne", " != ");      	 // 不等于
		SQLCONDITON.put("gt", " > ");     // 大于
		SQLCONDITON.put("ge", " >= ");     // 大于等于
		SQLCONDITON.put("lt", " < ");     // 小于
		SQLCONDITON.put("le", " <= ");     // 小于等于
		SQLCONDITON.put("cn", " like "); //cn 包含
		
		SQLCONDITON.put("=", " = ");  			 // 相等
		SQLCONDITON.put("!=", " != ");      	 // 不等于
		SQLCONDITON.put(">", " > ");     // 大于
		SQLCONDITON.put(">=", " >= ");     // 大于等于
		SQLCONDITON.put("<", " < ");     // 小于
		SQLCONDITON.put("<=", " <= ");     // 小于等于
		SQLCONDITON.put("like", " like "); //cn 包含
	};
	
	public static void main(String[] args) throws Exception {
		FilterObject fb1 = new FilterObject();
		FilterObject fb2 = new FilterObject();
		fb1.addCondition("productName", "=", "aaa").addCondition("proEName", "=", "dddd")
		.addOrderBy("proEName", "desc").addOrderBy("proEName", "desc");
		
		fb1.setClazz(KstarProduct.class);
		HqlObject sql = getHqlObject(fb1);
		
		System.out.println(sql.getHql());
		fb2.addCondition("customCode", "=", "aaa");
		fb2.setClazz(CustomAddressInfo.class);
		fb2.addCondition("customAddress", "=", "bbbb");
		List<FilterObject> list = new ArrayList<FilterObject>();
		List<JoinRuler> jlist = new ArrayList<JoinRuler>();
		JoinRuler r = new JoinRuler(KstarProduct.class,"productName",CustomAddressInfo.class,"customCode");
		jlist.add(r);
		list.add(fb1);
		list.add(fb2);
		sql = getHqlObject(list,jlist);
		System.out.println(sql);
	}
	
	public static Object getObjectValue(Class<?> cls,String field,String data) throws Exception{
		Field f = cls.getDeclaredField(field);
		Class<?> clazz = f.getType();
		if(clazz == Date.class){
			Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
			} catch (ParseException e) {
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(data);
				} catch (ParseException e1) {
					throw new AnneException("无效的日期格式必须为yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd");
				}
			}
			return date;
		}else if(clazz == Double.class || clazz == double.class){
			return Double.parseDouble(data);
		}else if(clazz == Integer.class || clazz == int.class){
			return Integer.parseInt(data);
		}else if(clazz == Long.class || clazz == long.class){
			return Long.parseLong(data);
		}else{
			return data;
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	public static HqlObject getHqlObject(List<FilterObject> fbs,List<JoinRuler> jos) throws AnneException{
		if(fbs == null || fbs.size()==0){
			throw new AnneException("未设置查询对象");
		}
		if(fbs.size()>1 && (jos == null || jos.size() < 0)){
			throw new AnneException("未设置查询对象Join"); 
		}
		List<Object> args = new ArrayList<>();
		StringBuffer hql = new StringBuffer("from ");
		for(int i=0;i<fbs.size();i++){
			FilterObject fb = fbs.get(i);
			Class cls = fb.getClazz();
			if(cls == null ){
				throw new AnneException("未设置查询对象");	
			}
			String as = cls.getSimpleName().toLowerCase();
			hql.append(" ");
			hql.append(cls.getName());
			hql.append(" ");
			hql.append(as);
			if(i < fbs.size()-1){
				hql.append(",");	
			}
		}
		hql.append(" where 1=1 ");
		
		if(jos != null){
			for(int k=0;k<jos.size();k++){
				hql.append(" and ");
				JoinRuler jo = jos.get(k);
				Class clsA = jo.getClassA();
				Class clsB = jo.getClassB();
				String asA = clsA.getSimpleName().toLowerCase();
				String asB = clsB.getSimpleName().toLowerCase();
				hql.append(asA);
				hql.append(".");
				hql.append(jo.getFieldA());
				hql.append(SQLCONDITON.get(jo.getOp()));
				hql.append(asB);
				hql.append(".");
				hql.append(jo.getFieldB());
			}
		}
		
		for(int j=0;j<fbs.size();j++){
			FilterObject fb = fbs.get(j);
			Class cls = fb.getClazz();
			String as = cls.getSimpleName().toLowerCase();
			List<FilerRuler> filterRules = fb.getRules();
			for(int i=0;i<filterRules.size();i++){
				hql.append(" and ");
				FilerRuler fr = filterRules.get(i);
				Field field = null;
				try{
					field = cls.getDeclaredField(fr.getField());
				}catch(Exception e){
					try {
						if(cls.getSuperclass()!=null){
							field = cls.getSuperclass().getDeclaredField(fr.getField());
						}
					} catch (Exception e1) {
						throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
					} 
				}
				hql.append(as);
				hql.append(".");
				hql.append(field.getName());
				hql.append(" ");
				hql.append(SQLCONDITON.get(fr.getOp()));
				hql.append(" ? ");
				args.add(fr.getData());
			}
		}
		
		
		for(int j=0;j<fbs.size();j++){
			FilterObject fb = fbs.get(j);
			Class cls = fb.getClazz();
			String as = cls.getSimpleName().toLowerCase();
			List<FilerRuler> filterRules = fb.getOrRules();
			for(int i=0;i<filterRules.size();i++){
				if(i==0){
					hql.append(" and (");
				}else{
					hql.append(" or ");
				}
				FilerRuler fr = filterRules.get(i);
				Field field = null;
				try{
					field = cls.getDeclaredField(fr.getField());
				}catch(Exception e){
					try {
						if(cls.getSuperclass()!=null){
							field = cls.getSuperclass().getDeclaredField(fr.getField());
						}
					} catch (Exception e1) {
						throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
					} 
				}
				hql.append(as);
				hql.append(".");
				hql.append(field.getName());
				hql.append(" ");
				hql.append(SQLCONDITON.get(fr.getOp()));
				hql.append(" ? ");
				args.add(fr.getData());
				
				if((filterRules.size()-1)== i){
					hql.append(" ) ");
				}
			}
		}
		
		
		return new HqlObject(hql.toString(), args);
	}
	
	@SuppressWarnings("rawtypes")
	public static HqlObject getHqlWhere(String as ,FilterObject fb)throws AnneException {
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		List<FilerRuler> filterRules = fb.getRules();
		List<FilerRuler> filterOrRules = fb.getOrRules();
		Class cls = fb.getClazz();
		if(cls == null){
			throw new AnneException("未设置查询对象");
		}
		for(int i=0;i<filterRules.size();i++){
			hql.append(" and ");
			FilerRuler fr = filterRules.get(i);
			Field field = null;
			try{
				field = cls.getDeclaredField(fr.getField());
			}catch(Exception e){
				try {
					if(cls.getSuperclass()!=null){
						field = cls.getSuperclass().getDeclaredField(fr.getField());
					}
				} catch (Exception e1) {
					throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
				} 
			}
			hql.append(as);
			hql.append(".");
			hql.append(field.getName());
			hql.append(" ");
			hql.append(SQLCONDITON.get(fr.getOp()));
			hql.append(" ? ");
			Class<?> clazz = field.getType();
			if(clazz == Date.class){
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fr.getData());
				} catch (ParseException e) {
					try {
						date = new SimpleDateFormat("yyyy-MM-dd").parse(fr.getData());
					} catch (ParseException e1) {
						throw new AnneException("无效的日期格式必须为yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd");
					}
				}
				args.add(date);
			}else if(clazz == Double.class || clazz == double.class){
				args.add(Double.parseDouble(fr.getData()));
			}else if(clazz == Integer.class || clazz == int.class){
				args.add(Integer.parseInt(fr.getData()));
			}else if(clazz == Long.class || clazz == long.class){
				args.add(Long.parseLong(fr.getData()));
			}else{
				args.add(fr.getData());
			}
		}
		
		for(int i=0;i<filterOrRules.size();i++){
			if(i==0){
				hql.append(" and (");
			}else{
				hql.append(" or ");
			}
			FilerRuler fr = filterOrRules.get(i);
			Field field = null;
			try{
				field = cls.getDeclaredField(fr.getField());
			}catch(Exception e){
				try {
					if(cls.getSuperclass()!=null){
						field = cls.getSuperclass().getDeclaredField(fr.getField());
					}
				} catch (Exception e1) {
					throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
				} 
			}
			hql.append(as);
			hql.append(".");
			hql.append(field.getName());
			hql.append(" ");
			hql.append(SQLCONDITON.get(fr.getOp()));
			hql.append(" ? ");
			args.add(fr.getData());
			if((filterOrRules.size()-1)== i){
				hql.append(" ) ");
			}
		}
		
		
		for(int i=0;i<fb.getOrders().size();i++){
			if(i==0){
				hql.append(" order by ");
			}
			OrderRuler r = fb.getOrders().get(i);
			hql.append(as);
			hql.append(".");
			hql.append(r.getField());
			hql.append(" ");
			hql.append(r.getAscOrDesc());
			if(i<fb.getOrders().size()-1){
				hql.append(" , ");
			}
		}
		
		return new HqlObject(hql.toString(), args);
		
	}
	
	public static HqlObject getHqlObject(FilterObject fb) throws AnneException {
		Class<?> cls = fb.getClazz();
		if(fb.getOrders().size() == 0){
			if(BeanUtils.hasField(cls,"createdAt")) {
				OrderRuler orderRuler = new OrderRuler("createdAt", "desc");
				fb.getOrders().add(orderRuler);
			}
		}

		if(cls == null){
			throw new AnneException("未设置查询对象");
		}
		StringBuffer hql = new StringBuffer("from ");
		String as = cls.getSimpleName().toLowerCase();
		hql.append(cls.getName());
		hql.append(" ");
		hql.append(as);
		hql.append(" where 1=1 ");
		
		List<Object> args = new ArrayList<>();
		List<FilerRuler> filterRules = fb.getRules();
		List<FilerRuler> filterOrRules = fb.getOrRules();

		for(int i=0;i<filterRules.size();i++){
			hql.append(" and ");
			FilerRuler fr = filterRules.get(i);
			Field field = null;
			try{
				field = cls.getDeclaredField(fr.getField());
			}catch(Exception e){
				try {
					if(cls.getSuperclass()!=null){
						field = cls.getSuperclass().getDeclaredField(fr.getField());
					}
				} catch (Exception e1) {
					throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
				} 
			}
			
			//新增忽略大小写查询
			if("like".equals(fr.getOp())){
				hql.append("lower(");
			}
			hql.append(as);
			hql.append(".");
			hql.append(field.getName());
			if("like".equals(fr.getOp())){
				hql.append(")");
			}
			
			if(fr.getData() != null){
				hql.append(SQLCONDITON.get(fr.getOp()));
				hql.append(" ? ");
				Class<?> clazz = field.getType();
				if(clazz == Date.class){
					Date date;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fr.getData());
					} catch (ParseException e) {
						try {
							date = new SimpleDateFormat("yyyy-MM-dd").parse(fr.getData());
						} catch (ParseException e1) {
							throw new AnneException("无效的日期格式必须为yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd");
						}
					}
					args.add(date);
				}else if(clazz == Double.class || clazz == double.class){
					args.add(Double.parseDouble(fr.getData()));
				}else if(clazz == Integer.class || clazz == int.class){
					args.add(Integer.parseInt(fr.getData()));
				}else if(clazz == Long.class || clazz == long.class){
					args.add(Long.parseLong(fr.getData()));
				}else{
					if("like".equals(fr.getOp())){
						if(fr.getData() !=null){
							args.add(fr.getData().toLowerCase());
						}else{
							args.add(fr.getData());
						}
					}else{
						args.add(fr.getData());
					}
				}
			}else{
				hql.append(" = null ");
			}
			
		}
		
		for(int i=0;i<filterOrRules.size();i++){
			if(i==0){
				hql.append(" and (");
			}else{
				hql.append(" or ");
			}
			FilerRuler fr = filterOrRules.get(i);
			Field field = null;
			try{
				field = cls.getDeclaredField(fr.getField());
			}catch(Exception e){
				try {
					if(cls.getSuperclass()!=null){
						field = cls.getSuperclass().getDeclaredField(fr.getField());
					}
				} catch (Exception e1) {
					throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
				} 
			}
			hql.append(as);
			hql.append(".");
			hql.append(field.getName());
			hql.append(" ");
			if(fr.getData() != null){
				hql.append(SQLCONDITON.get(fr.getOp()));
				hql.append(" ? ");
				args.add(fr.getData());
			}else{
				hql.append(" = null ");
			}
			if((filterOrRules.size()-1)== i){
				hql.append(" ) ");
			}
		}
		
		Permission permission = cls.getAnnotation(Permission.class);
		if(permission!=null){
			String businessField = null;
			String userIdField = null;
			String positionIdField = null;
			String orgIdField = null;
			Field[] fs = cls.getDeclaredFields();
			for(Field f : fs){
				PermissionBusinessId businessId = f.getAnnotation(PermissionBusinessId.class);
				if(businessId !=null){
					businessField = f.getName();
				}
				
				PermissionUserId userId = f.getAnnotation(PermissionUserId.class);
				if(userId !=null){
					userIdField = f.getName();
				}
				
				PermissionPositionId positionId = f.getAnnotation(PermissionPositionId.class);
				if(positionId !=null){
					positionIdField = f.getName();
				}
				
				PermissionOrgId orgId = f.getAnnotation(PermissionOrgId.class);
				if(orgId !=null){
					orgIdField = f.getName();
				}
			}
			
			fs = cls.getSuperclass().getDeclaredFields();
			for(Field f : fs){
				PermissionBusinessId businessId = f.getAnnotation(PermissionBusinessId.class);
				if(businessId !=null){
					businessField = f.getName();
				}
				
				PermissionUserId userId = f.getAnnotation(PermissionUserId.class);
				if(userId !=null){
					userIdField = f.getName();
				}
				
				PermissionPositionId positionId = f.getAnnotation(PermissionPositionId.class);
				if(positionId !=null){
					positionIdField = f.getName();
				}
				
				PermissionOrgId orgId = f.getAnnotation(PermissionOrgId.class);
				if(orgId !=null){
					orgIdField = f.getName();
				}
			}
			
			if(userIdField == null){
				throw new AnneException("PermissionUserId 没有添加注解");
			}
			
			if(positionIdField == null){
				throw new AnneException("PermissionPositionId 没有添加注解");
			}
			
			if(orgIdField == null){
				throw new AnneException("PermissionOrgId 没有添加注解");
			}
			
			if(businessField == null){
				throw new AnneException("PermissionBusinessId 没有添加注解");
			}
			
			
			String phql = PermissionUtil.getPermissionHQL(null, as+"."+userIdField, as+"."+positionIdField, as+"."+orgIdField, as+"."+businessField, fb.getUser(), permission.businessType());
			hql.append(" and ").append(phql).append(" ");
		}
		
		for(int i=0;i<fb.getOrders().size();i++){
			if(i==0){
				hql.append(" order by ");
			}
			OrderRuler r = fb.getOrders().get(i);
			hql.append(as);
			hql.append(".");
			hql.append(r.getField());
			hql.append(" ");
			hql.append(r.getAscOrDesc());
			if(i<fb.getOrders().size()-1){
				hql.append(" , ");
			}
		}
		
		return new HqlObject(hql.toString(), args);
	}
	/**
	 * 不带权限
	 * getHqlObjectNoPermission:(这里用一句话描述这个方法的作用). <br/> 
	 * 
	 * @author liming 
	 * @param fb
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	public static HqlObject getHqlObjectNoPermission(FilterObject fb) throws AnneException {
		Class<?> cls = fb.getClazz();
		if(fb.getOrders().size() == 0){
			if(BeanUtils.hasField(cls,"createdAt")) {
				OrderRuler orderRuler = new OrderRuler("createdAt", "desc");
				fb.getOrders().add(orderRuler);
			}
		}

		if(cls == null){
			throw new AnneException("未设置查询对象");
		}
		StringBuffer hql = new StringBuffer("from ");
		String as = cls.getSimpleName().toLowerCase();
		hql.append(cls.getName());
		hql.append(" ");
		hql.append(as);
		hql.append(" where 1=1 ");
		
		List<Object> args = new ArrayList<>();
		List<FilerRuler> filterRules = fb.getRules();
		List<FilerRuler> filterOrRules = fb.getOrRules();

		for(int i=0;i<filterRules.size();i++){
			hql.append(" and ");
			FilerRuler fr = filterRules.get(i);
			Field field = null;
			try{
				field = cls.getDeclaredField(fr.getField());
			}catch(Exception e){
				try {
					if(cls.getSuperclass()!=null){
						field = cls.getSuperclass().getDeclaredField(fr.getField());
					}
				} catch (Exception e1) {
					throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
				} 
			}
			hql.append(as);
			hql.append(".");
			hql.append(field.getName());
			hql.append(" ");
			hql.append(SQLCONDITON.get(fr.getOp()));
			hql.append(" ? ");
			Class<?> clazz = field.getType();
			if(clazz == Date.class){
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fr.getData());
				} catch (ParseException e) {
					try {
						date = new SimpleDateFormat("yyyy-MM-dd").parse(fr.getData());
					} catch (ParseException e1) {
						throw new AnneException("无效的日期格式必须为yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd");
					}
				}
				args.add(date);
			}else if(clazz == Double.class || clazz == double.class){
				args.add(Double.parseDouble(fr.getData()));
			}else if(clazz == Integer.class || clazz == int.class){
				args.add(Integer.parseInt(fr.getData()));
			}else if(clazz == Long.class || clazz == long.class){
				args.add(Long.parseLong(fr.getData()));
			}else{
				args.add(fr.getData());
			}
		}
		
		for(int i=0;i<filterOrRules.size();i++){
			if(i==0){
				hql.append(" and (");
			}else{
				hql.append(" or ");
			}
			FilerRuler fr = filterOrRules.get(i);
			Field field = null;
			try{
				field = cls.getDeclaredField(fr.getField());
			}catch(Exception e){
				try {
					if(cls.getSuperclass()!=null){
						field = cls.getSuperclass().getDeclaredField(fr.getField());
					}
				} catch (Exception e1) {
					throw new AnneException("没有找到需要查找的字段 : "+fr.getField());
				} 
			}
			hql.append(as);
			hql.append(".");
			hql.append(field.getName());
			hql.append(" ");
			hql.append(SQLCONDITON.get(fr.getOp()));
			hql.append(" ? ");
			args.add(fr.getData());
			if((filterOrRules.size()-1)== i){
				hql.append(" ) ");
			}
		}
	
		for(int i=0;i<fb.getOrders().size();i++){
			if(i==0){
				hql.append(" order by ");
			}
			OrderRuler r = fb.getOrders().get(i);
			hql.append(as);
			hql.append(".");
			hql.append(r.getField());
			hql.append(" ");
			hql.append(r.getAscOrDesc());
			if(i<fb.getOrders().size()-1){
				hql.append(" , ");
			}
		}
		
		return new HqlObject(hql.toString(), args);
	}
	
}
