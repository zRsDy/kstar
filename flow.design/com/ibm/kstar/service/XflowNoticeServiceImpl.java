package com.ibm.kstar.service;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.message.service.MessageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.StringUtil;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class XflowNoticeServiceImpl {
	@Autowired
	protected MessageConfig messageConfig;
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	private final int batchNum = 100;
	
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
	@Scheduled(cron = " 0 0/5 * * * ? ")
	private void sendFlowEmail() {
		try {
			Thread thread = new Thread(){
				@Override
				public void run() {
					
					String sql = "select a.row_id, a.participant_id,a.participant_type,a.notice_type,b.process_instance_name,b.process_definition_name,b.status,b.creator_name "
							+ " from xflow_notice a, xflow_history_process_instance b where a.process_instance_id = b.row_id and a.status = 'N' and rownum <= ? ";
					List<Map<String,Object>> noticeList = baseDao.findBySql4Map(sql, new Object[]{batchNum});
					if(noticeList != null && noticeList.size() > 0){
						for(Map<String,Object> notice : noticeList){
							String subject = "待办任务提醒："+notice.get("PROCESS_DEFINITION_NAME");
							Address[] emailAddress = getEmailAddress((String)notice.get("PARTICIPANT_ID"),(String)notice.get("PARTICIPANT_TYPE"));
							String msg = "";
							
							if(emailAddress == null || emailAddress.length == 0){
								continue;
							}
							
							if("CREATE_TASK".equals(notice.get("NOTICE_TYPE"))){
								if("running".equals(notice.get("STATUS"))){
									msg = "请审批["+notice.get("CREATOR_NAME")+"]提交的任务["+notice.get("PROCESS_INSTANCE_NAME")+"]";
								}else if("end".equals(notice.get("STATUS"))){ 
									msg = "["+notice.get("CREATOR_NAME")+"]提交的任务["+notice.get("PROCESS_INSTANCE_NAME")+"]已经审批通过";
								}
							}else if("NOTICE".equals(notice.get("NOTICE_TYPE"))){
								if("running".equals(notice.get("STATUS"))){
									msg = "["+notice.get("CREATOR_NAME")+"]提交 的任务["+notice.get("PROCESS_INSTANCE_NAME")+"]已经在审批中";
								}else if("end".equals(notice.get("STATUS"))){ 
									msg = "["+notice.get("CREATOR_NAME")+"]提交 的任务["+notice.get("PROCESS_INSTANCE_NAME")+"]已经审批通过";
								}
							}
							if(msg != ""){
								try {
									//if("PROD".equalsIgnoreCase(messageConfig.getEnv())){
									if(lovMemberService.switchIsOpen(IConstants.JAVA_CODE_LOGIC_SWITCH, "flowNoticeMail")){
									  sendEmail(subject, msg, emailAddress);
									  updateNotice((String)notice.get("ROW_ID"));
									}
									//}
								} catch (Exception e) {
									// TODO: handle exception
									
								}
							} 
						}
					}
				}
			};
			fixedThreadPool.submit(thread);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateNotice(String noticeId){
		String sql = "update xflow_notice a set a.status = 'Y' where a.row_id = ?";
		baseDao.executeSQL(sql, noticeId);
	}
	
	private Address[] getEmailAddress(String participantId, String participantType) {
		List<InternetAddress> addresses = new ArrayList<InternetAddress>();
		
		if(participantId == null || participantType == null){
			return null;
		}
		try {
			if("EMPLOYEE".equals(participantType)){
				Employee employee = employeeService.get(participantId);
				if(employee != null){
					addresses.add(new InternetAddress(employee.getEmail()));
				}
			}else if("POSITION".equals(participantType)){
				String sql = "select e.* from sys_t_permission_user_rel r, sys_t_lov_member          m, sys_t_permission_employee e where r.type = 'P' "
						+ " and e.row_id = r.user_id and r.member_id = m.row_id "
						+ " and (m.opt_txt1 = ? or m.opt_txt2 = ? or m.row_id = ?)";
				
				List<Map<String,Object>> employeeList = baseDao.findBySql4Map(sql, new String[]{participantId,participantId,participantId});
				if(employeeList != null && employeeList.size() > 0){
					for(Map<String,Object> employee : employeeList){
						addresses.add(new InternetAddress((String)employee.get("EMAIL")));
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
		return addresses.toArray(new InternetAddress[addresses.size()]);
	}
	
	private void sendEmail(String subject, String msg, Address[] emailAddress) throws Exception{
		try{
		 	Properties properties = new Properties();
	        properties.setProperty("mail.smtp.auth", "true");
	        properties.setProperty("mail.transport.protocol", "smtp");
	        Session session = Session.getInstance(properties);
	        session.setDebug(true);
	        Message message = new MimeMessage(session);
	        message.setSubject(subject); 
	        message.setFrom(new InternetAddress("kstarcrm@kstar.com.cn"));
	        message.setText(msg);
	        Transport transport = session.getTransport();
			transport.connect(IConstants.MAIL_SERVICE_ADDR, 25, "kstarcrm@kstar.com.cn", "kstar-5");
			transport.sendMessage(message, emailAddress);
			transport.close();
		}catch(Exception e){
			throw e;
		}
	}
	
}
