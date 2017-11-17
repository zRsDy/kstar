package org.xsnake.web.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.cache.CacheData;

public class SessionManager implements HttpSessionListener{
	
	static SessionManager instance;
	
	public SessionManager(){
		instance = this;
	}
	
	Map<String,Map<String,Object>> jsessionMap = new HashMap<String,Map<String,Object>>();
	
	static Map<String,HttpSession> userMap = new HashMap<String,HttpSession>();
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		userMap.put(session.getId(), session);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		userMap.remove(session.getId());
	}

	public static SessionManager getInstance() {
		return instance;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setMappingSession(String jsessionId,HttpSession session){
		Map<String,Object> sessionMap = (Map<String,Object>)CacheData.getInstance().get(jsessionId+"_SESSION");
		if(sessionMap == null){
			sessionMap = new HashMap<String,Object>();
		}
		Enumeration es = session.getAttributeNames();
		while(es.hasMoreElements()){
			String key = String.valueOf(es.nextElement());
			sessionMap.put(key, session.getAttribute(key));
		}
		CacheData.getInstance().set(jsessionId+"_SESSION", sessionMap);
	}
	
	@SuppressWarnings({ "unchecked" })
	public void getMappingSession(String jsessionId,HttpSession session){
		Map<String,Object> sessionMap = (Map<String,Object>)CacheData.getInstance().get(jsessionId+"_SESSION");
		if(sessionMap !=null){
		Set<String> keys = sessionMap.keySet();
			for(String key : keys){
				session.setAttribute(key, sessionMap.get(key));
			}
		}
	}
	
	public static List<HttpSession> getOnlineSession(){
		Collection<HttpSession> sessionList = userMap.values();
		List<HttpSession> list = new ArrayList<HttpSession>(sessionList);
		return list;
	}
	
	public static List<UserObject> getOnlineUser(){
		Collection<HttpSession> sessionList = userMap.values();
		List<UserObject> list = new ArrayList<UserObject>();
		for(HttpSession session : sessionList){
			Object user = session.getAttribute("LOGIN_USER");
			if(user != null){
				UserObject u = (UserObject)user;
				list.add(u);
			}
		}
		return list;
	}
}
