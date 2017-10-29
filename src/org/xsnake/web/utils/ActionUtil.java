package org.xsnake.web.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.api.system.permission.UserObject;

public class ActionUtil {

	public static boolean isAdmin(HttpServletRequest request){
		return false;
	} 
	
	public static ProcessForm getProcessForm(HttpServletRequest request, UserObject userObject) {
		String newProcessType = request.getParameter("newProcessType");
		if(StringUtil.isEmpty(newProcessType)){
			return null;
		}
		String submitType = request.getParameter("submitType");
		String activityId = request.getParameter("activityId");
		String comment = request.getParameter("comment");
		String taskId = request.getParameter("taskId");
		String appointUserId = request.getParameter("appointUserId");//被指派人ID
		String toActivityId = request.getParameter("toActivityId");
		ProcessForm form = new ProcessForm();
		form.setSubmitType(submitType);
		form.setActivityId(activityId);
		form.setComment(comment);
		form.setTaskId(taskId);
		form.setAppointUserId(appointUserId);
		form.setToActivityId(toActivityId);
		form.setEmployeeId(userObject.getEmployee().getId());
		return form;
	}
	
	public static void requestToCondition(Condition condition, HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String value = request.getParameter(key);
			condition.setCondition(key, value);
		}
		HttpSession session = request.getSession();
		UserObject user = ((UserObject)session.getAttribute(BaseAction.USER));
		condition.setCondition("userObject", user);
	}
	
	public static void requestToCondition4log(Condition condition, HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String value = request.getParameter(key);
			condition.setCondition(key, value);
		}
	}
	
	public static void requestToModel(Model model, HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String value = request.getParameter(key);
			model.addAttribute(key, value);
		}
	}
	
	public static <T> T requestToObject(Class<T> clazz, HttpServletRequest request) throws InstantiationException, IllegalAccessException, SecurityException {
		T obj = clazz.newInstance();
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String value = request.getParameter(key);
			Field field = null;
			try {
				field = clazz.getDeclaredField(key);
				field.setAccessible(true);
				
				Class type = field.getType();
				
				if(type == Date.class){
					
					Date date;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
					} catch (ParseException e) {
						try {
							date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
						} catch (ParseException e1) {
							throw new AnneException("无效的日期格式必须为yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd");
						}
					}
					field.set(obj, date);
				}else{
					if(type == String.class){
						field.set(obj, value);
					}else{
						try {
							Method method = field.getType().getMethod("valueOf", String.class);
							method.setAccessible(true);
							Object objValue = method.invoke(field.getType(), value);
							field.set(obj, objValue);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				}
				
			} catch (NoSuchFieldException e) {
				//忽略不存在的属性
			}
			
		}
		return obj;
	}

	public static void doSearch(PageCondition condition) {
		Map<String, Object> map = condition.getConditionMap();

		if (map.size() > 0) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
//			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				if (entry.getKey().startsWith("pageSearch_") && entry.getValue() != null && !"".equals(entry.getValue())) {
					String str = entry.getKey();
					String propName = str.replaceAll("pageSearch_", "");
					if (entry.getKey().endsWith("_begin")) {
						condition.getFilterObject().addCondition(propName.replaceAll("_begin", ""), ">=", StringUtil.getSearchString(entry.getValue()));
					} else if (entry.getKey().endsWith("_end")) {
						condition.getFilterObject().addCondition(propName.replace("_end", ""), "<=", StringUtil.getSearchString(entry.getValue()));
					} else if (entry.getKey().endsWith("_like")) {
						condition.getFilterObject().addCondition(propName.replace("_like", ""), "like", "%" + StringUtil.getSearchString(entry.getValue()) + "%");
					} else {
						condition.getFilterObject().addCondition(propName, "=", StringUtil.getSearchString(entry.getValue()));
					}
				}
			}
		}
	}
    
    protected static DatagramPacket send(final byte[] bytes, String sRemoteAddr,DatagramSocket ds) throws IOException {
    	DatagramPacket dp = new DatagramPacket(bytes,bytes.length,InetAddress.getByName(sRemoteAddr),137);
     	ds.send(dp);
        return dp;
    }
    
    protected static DatagramPacket receive(DatagramSocket ds) throws Exception {
    	byte[] buffer = new byte[1024];
    	DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
    	ds.receive(dp);
    	return dp;
    }
    
    protected static byte[] GetQueryCmd() throws Exception {
    	byte[] t_ns = new byte[50];
     	t_ns[0] = 0x00;
        t_ns[1] = 0x00;
        t_ns[2] = 0x00;
        t_ns[3] = 0x10;
        t_ns[4] = 0x00;
        t_ns[5] = 0x01;
        t_ns[6] = 0x00;
        t_ns[7] = 0x00;
        t_ns[8] = 0x00;
        t_ns[9] = 0x00;
        t_ns[10] = 0x00;
        t_ns[11] = 0x00;
        t_ns[12] = 0x20;
        t_ns[13] = 0x43;
        t_ns[14] = 0x4B;
        
        for(int i = 15; i < 45; i++)
        {
         t_ns[i] = 0x41;
        }
        
        t_ns[45] = 0x00;
        t_ns[46] = 0x00;
        t_ns[47] = 0x21;
        t_ns[48] = 0x00;
        t_ns[49] = 0x01;
        return t_ns;
    }
    
    protected static String GetMacAddr(byte[] brevdata) throws Exception {
     
    	int i = brevdata[56] * 18 + 56;
        String sAddr="";
        StringBuffer sb = new StringBuffer(17);
                
        for(int j = 1; j < 7;j++)
        {
         sAddr = Integer.toHexString(0xFF & brevdata[i+j]);
            if(sAddr.length() < 2)
            {
             sb.append(0);
            }
            sb.append(sAddr.toUpperCase());
            if(j < 6) sb.append(":");
        }
        return sb.toString();
    }
    
    public static void close(DatagramSocket ds) {
		try{
			ds.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
    }
    
    public static String GetRemoteMacAddr(String ip) throws Exception {
    	byte[] bqcmd = GetQueryCmd();
    	DatagramSocket ds = new DatagramSocket();
     	send(bqcmd,ip,ds);
     	DatagramPacket dp = receive(ds);
        String smac = GetMacAddr(dp.getData());
        close(ds);
        
        return smac;
    }
    
    public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
