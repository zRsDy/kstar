package com.ibm.kstar.action.task.schedule;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.custom.ICustomSatInvestService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.custom.CustomSatInvest;
import com.ibm.kstar.message.service.MessageConfig;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.xsnake.web.dao.BaseDao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CustomSatInvestJob implements Job, StatefulJob{

	MessageConfig messageConfig;
	
	BaseDao baseDao;
	
    ILovMemberService lovMemberService;
	
	ICustomNumberService numberService;
	
	ICustomSatInvestService service;
	
	IEmployeeService employeeService;
	
	UserObject userObject;
	
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		baseDao = (BaseDao)ApplicationContextUtil.getBean("baseDao");
		
		messageConfig = (MessageConfig)ApplicationContextUtil.getBean("messageConfig");
		
	    lovMemberService = (ILovMemberService)ApplicationContextUtil.getBean("lovMemberServiceImpl");
		
		numberService = (ICustomNumberService)ApplicationContextUtil.getBean("customNumberServiceImpl");
		
		service = (ICustomSatInvestService)ApplicationContextUtil.getBean("customSatInvestServiceImpl");
		
		employeeService = (IEmployeeService)ApplicationContextUtil.getBean("employeeServiceImpl");
		
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		userObject = (UserObject)jobDataMap.get("userObject");
		try {
			
			sendCsrEmail();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S D E F w W");
			System.out.println(jobDetail.getName()+"当前时间:"+dateFormat.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void sendCsrEmail(){
		Thread thread = new Thread(){
			@Override
			public void run() {
				Transport transport = null;
				List<LovMember> emails = lovMemberService.getListByGroupCode("CustomSatInvest");
				for(LovMember lovMember : emails){
					try{
						CustomSatInvest invest = createCustomSatInvest(lovMember);
						
						//新建状态下才发调查报告
						if(IConstants.CUSTOM_NORMAL_STATUS_10.equals(invest.getProcStatusCd())){
							Properties properties = new Properties();
					        properties.setProperty("mail.smtp.auth", "true");
					        properties.setProperty("mail.transport.protocol", "smtp");
					        Session session = Session.getInstance(properties);
//						        session.setDebug(true);
					        Message message = new MimeMessage(session);
					        message.setSubject("["+invest.getAreaOrIndustryName()+"]"+invest.getInvestQuarter()+"客户满意度调查");
					        message.setFrom(new InternetAddress("kstarcrm@kstar.com.cn"));
					        
					        LovMember url = lovMemberService.getLovMemberByCode("SYSTEMURLS", "SYSURL");
					        
					        if(url != null){     
					        	 message.setText("您有新的满意度调查报告需要填写,请在["+invest.getEffDate()+"]之前完成,访问地址："+url.getOptTxt1()+"/custom/csr/edit.html?id="+invest.getId()+"\n"
					        	 		+ "\n建议使用Google/IE/火狐浏览器." );
					        }
					        transport = session.getTransport();
							transport.connect(IConstants.MAIL_SERVICE_ADDR, 25, "kstarcrm@kstar.com.cn", "kstar-5");
							transport.sendMessage(message, new Address[]{new InternetAddress(invest.getPrincipalEmail())});
						}
					}catch(Exception e){
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
			}
		};
		fixedThreadPool.submit(thread);
	}
	
	private CustomSatInvest createCustomSatInvest(LovMember lovMember){
		CustomSatInvest customSatInvest = new CustomSatInvest();
		customSatInvest.setId(null);
		customSatInvest.setInvestCode(numberService.getInvestCode());
		
		String quarter = "";
		Calendar effCalendar = Calendar.getInstance();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		int day = Calendar.getInstance().get(Calendar.DATE);
		//第一季度1月21~4月20
		if((month == 1 && day >= 21) || (month > 1 && month < 4) || (month == 4 && day <21)){
			quarter = year+"年第1季度";
			effCalendar.set(Calendar.MONTH,3);
		}
		//第二季度4月21~7月20
		if((month == 4 && day >= 21) || (month > 4 && month < 7) || (month == 7 && day <21)){
			quarter = year+"年第2季度";
			effCalendar.set(Calendar.MONTH,6);
		}
		//第三季度7月21~10月20
		if((month == 7 && day >= 21) || (month > 7 && month < 10) || (month == 10 && day <21)){
			quarter = year+"年第3季度";
			effCalendar.set(Calendar.MONTH,9);
		}
		//第四季度10月21~1月20
		if((month == 10 && day >= 21) || (month > 10 && month <= 12)){
			quarter = year+"年第4季度";
			
			effCalendar.set(Calendar.YEAR,year+1);
			effCalendar.set(Calendar.MONTH,0);
		}
		if(month == 1 && day < 21){
			quarter = (year-1)+"年第4季度";
			effCalendar.set(Calendar.MONTH,0);
		}
		
		effCalendar.set(Calendar.DATE,21);
		effCalendar.set(Calendar.HOUR_OF_DAY,0);
		effCalendar.set(Calendar.MINUTE,0);
		effCalendar.set(Calendar.SECOND,0);
		
		Date effDate = effCalendar.getTime();
		
		customSatInvest.setInvestQuarter(quarter);
		customSatInvest.setProcStatusCd(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		customSatInvest.setCreateDate(sdf.format(new Date()));
		customSatInvest.setEffDate(sdf.format(effDate));
		
		customSatInvest.setCreatorOrgId(userObject.getOrg().getId());
		customSatInvest.setCreatorPosId(userObject.getPosition().getId());
		
		LovMember org = lovMemberService.get(lovMember.getOptTxt1());
		if(org != null){
			customSatInvest.setAreaOrIndustry(org.getId());
		}
		
		Employee employee = employeeService.get(lovMember.getOptTxt2());
		if(employee != null){
			customSatInvest.setPrincipal(employee.getId());
		}
		
		//判断是否存在同样的满意度调查
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("investQuarter", customSatInvest.getInvestQuarter());
		paramsMap.put("areaOrIndustry", customSatInvest.getAreaOrIndustry());
		paramsMap.put("principal", customSatInvest.getPrincipal());
		
		List<CustomSatInvest> list = service.getCustomSatInvestsByParam(paramsMap);
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			service.saveSatInvestInfo(customSatInvest, userObject);
			return customSatInvest;
		}
	}
}
