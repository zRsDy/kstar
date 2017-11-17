package com.ibm.kstar.action.task.schedule;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.conf.ApplicationContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.utils.MD5Util;
import org.xsnake.web.utils.StringUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchResetPwdJob implements Job, StatefulJob{
	
	BaseDao baseDao;
	
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		baseDao = (BaseDao)ApplicationContextUtil.getBean("baseDao");

		String sql = "select * from TEMP_FOR_RESET_PWD a";
		
		List<Map<String, Object>> list = baseDao.findBySql4Map(sql, null);
		
		for(Map<String, Object> map : list){
			resetPasswordById(StringUtil.strnull(map.get("ROW_ID")));
		}
	}
	
	private void resetPasswordById(String id) throws AnneException{
		Employee employee = baseDao.findUniqueEntity(" select e from Employee e, User u where e.id = u.id and e.id = ?  " , new Object[]{id});
		if(employee == null){
			throw new AnneException("无效的邮箱");
		}
		User user = baseDao.get(User.class, employee.getId());
		String password = getRandomPassword();
		user.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
		baseDao.update(user);
		sendPasswordNotice(employee.getEmail(),employee.getName(),employee.getNo(),password);
	}
	
	private void sendPasswordNotice(final String email,final String name,final String no,final String password){
		sendMail(no,email, "新CRM系统用户初始密码","新CRM系统已正式上线，访问地址：http://crm.kstar.com:8888\n您可以使用工号或者邮箱登录.\n"
				+ name+"["+no+"]的初始密码为：" + password + ".\n请妥善保管您的密码。"
				+ "\n\n建议使用Google浏览器下载地址 http://sw.bos.baidu.com/sw-search-sp/software/7164c4c6bc6e0/ChromeStandalone_58.0.3029.110_Setup.exe" );
	}
	
	private void sendMail(final String no,final String email, final String title,final String content) {
		Thread thread = new Thread(){
			@Override
			public void run() {
				Transport transport = null;
				try{
					System.out.println("--------------------sendMain------------------------start");
					System.out.println("mail:"+email + "  title:" + title);
				 	Properties properties = new Properties();
			        properties.setProperty("mail.smtp.auth", "true");
			        properties.setProperty("mail.transport.protocol", "smtp");
			        Session session = Session.getInstance(properties);
			        Message message = new MimeMessage(session);
			        message.setSubject(title);
			        message.setFrom(new InternetAddress("kstarcrm@kstar.com.cn"));
			        message.setText(content);
			        transport = session.getTransport();
					transport.connect(IConstants.MAIL_SERVICE_ADDR, 25, "kstarcrm@kstar.com.cn", "kstar-5");
					transport.sendMessage(message, new Address[]{new InternetAddress(email.trim())});
					System.out.println("--------------------sendMain------------------------end");
				}catch(Exception e){
					String sql="update TEMP_FOR_RESET_PWD a set a.status='False',a.reson='"+e.getMessage()+"' where a.no='"+no+"'";
					baseDao.executeSQL(sql);
					e.printStackTrace();
				}finally{
					if(transport !=null){
						try {
							transport.close();
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		fixedThreadPool.submit(thread);
	}
	
	private static String getRandomPassword(){
		String words = "1234567890";
		int passwordLenght = 6;
		String password = "";
		Random r = new Random();
		for(int i=0;i<passwordLenght;i++){
			int p = r.nextInt(words.length());
			password = password + words.substring(p, p+1);
		}
		return password;
	}

}
