package com.ibm.kstar.action.task.schedule;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.conf.ApplicationContextUtil;

public class DataSyncFromOAJob implements Job, StatefulJob {
	
	private String defaultDriverName = "net.sourceforge.jtds.jdbc.Driver";
	private String defaultUrl = "jdbc:jtds:sqlserver://10.2.1.17:1433/KEKP";
	private String defaultUserName = "crm_user";
	private String defaultPassword = "crm_user";
	
	BaseDao baseDao;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		
		String driverName = (String)jobDataMap.get("driverName");
		String url = (String)jobDataMap.get("url");
		String userName = (String)jobDataMap.get("userName");
		String password = (String)jobDataMap.get("password");
		
		Driver driver = null;
		try {
			if(!StringUtil.isNullOrEmpty(driverName)){
				driver = (Driver)Class.forName(driverName).newInstance();
			}else{
				driver = (Driver)Class.forName(defaultDriverName).newInstance();
				url = defaultUrl;
				userName = defaultUserName;
				password = defaultPassword;
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new JobExecutionException(e.getMessage());
		}
		
		if(StringUtil.isNullOrEmpty(url) || StringUtil.isNullOrEmpty(userName) || StringUtil.isNullOrEmpty(password)){
			throw new JobExecutionException("参数url/userName/password不能为空!");
		}
		
		baseDao = (BaseDao)ApplicationContextUtil.getBean("baseDao");
		
		Statement stm = null;
	    ResultSet rs = null;
		Connection conn=null; 
		try {
			DriverManager.registerDriver(driver);
			conn = DriverManager.getConnection(url, userName, password);
			if(conn != null){
				stm = conn.createStatement();
				rs = stm.executeQuery("select * from v_for_crm");
				
				final List<Temp> data = new ArrayList<Temp>();
		        final RowMapper<Temp> rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(Temp.class);
		        
		        int currentRow = 0;
            	while (rs.next()) {
            		data.add(rowMapper.mapRow(rs, currentRow));
                    currentRow++;
                }
            	
            	syncToCRM(data);
            	System.out.println("success");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(stm != null){
					stm.close();
				}
				if(conn != null){
					conn.close();
				}
				DriverManager.deregisterDriver(driver);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void syncToCRM(List<Temp> data) {
		for(Temp temp : data){
			try {
				TempForDataSync target = null;
				
				String hql = "select a from TempForDataSync a where a.fdId=?";
				target = baseDao.findUniqueEntity(hql, new String[]{temp.getFdId()});
				
				if(target != null){
					BeanUtils.copyProperties(temp, target,new String[]{"id","status"});
					if("30".equals(temp.getDocStatus())){
						target.setStatus("已回款");
					}else{
						if("N9".equals(temp.getFdFactNodeId())){
							target.setStatus("已放款");
						}else{
							target.setStatus("未放款");
						}
					}
					baseDao.update(target);
				}else{
					target = new TempForDataSync();
					BeanUtils.copyProperties(temp, target,new String[]{"id","status"});
					if("30".equals(temp.getDocStatus())){
						target.setStatus("已回款");
					}else{
						if("N9".equals(temp.getFdFactNodeId())){
							target.setStatus("已放款");
						}else{
							target.setStatus("未放款");
						}
					}
					baseDao.save(target);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public static class Temp{
		String 	fdId;
		String 	fdPerson;		//员工id
		String 	fdDeput;    	//部门id
		String 	fdEmpNo;   		//员工工号
		Date 	fdRequestDate; 	//申请日期
		String 	fdProject;     	//项目名称
		String 	fdType;        	//项目类型 1.投标保证金 2.质量保证金 3.履约保证金 4.其他
		Date 	fdPayDate;   	//拟款日期
		Date 	fdBackDate;  	//拟回款日期
		String 	fdReceiptName; 	//收款单位名称 
		String 	fdBankName;  	//收款单位银行名称 
		String 	fdBankNo; 		//收款单位银行账号 
		Double 	fdAmount;  		//金额（元）
		String 	fdMemo;  		//备注
		String 	fdNumber;  		//流程编号
		String 	docStatus; 		//编号状态 30为流程结束
		Date 	fdLastModifiedTime; //最后更新时间
		String 	docSubject; 		//表单标题
		String 	fdFactNodeId;
		
		public Temp(){
			
		}
		
		public String getFdId() {
			return fdId;
		}
		public void setFdId(String fdId) {
			this.fdId = fdId;
		}
		public String getFdPerson() {
			return fdPerson;
		}
		public void setFdPerson(String fdPerson) {
			this.fdPerson = fdPerson;
		}
		public String getFdDeput() {
			return fdDeput;
		}
		public void setFdDeput(String fdDeput) {
			this.fdDeput = fdDeput;
		}
		public String getFdEmpNo() {
			return fdEmpNo;
		}
		public void setFdEmpNo(String fdEmpNo) {
			this.fdEmpNo = fdEmpNo;
		}
		public Date getFdRequestDate() {
			return fdRequestDate;
		}
		public void setFdRequestDate(Date fdRequestDate) {
			this.fdRequestDate = fdRequestDate;
		}
		public String getFdProject() {
			return fdProject;
		}
		public void setFdProject(String fdProject) {
			this.fdProject = fdProject;
		}
		public String getFdType() {
			return fdType;
		}
		public void setFdType(String fdType) {
			this.fdType = fdType;
		}
		public Date getFdPayDate() {
			return fdPayDate;
		}
		public void setFdPayDate(Date fdPayDate) {
			this.fdPayDate = fdPayDate;
		}
		public Date getFdBackDate() {
			return fdBackDate;
		}
		public void setFdBackDate(Date fdBackDate) {
			this.fdBackDate = fdBackDate;
		}
		public String getFdReceiptName() {
			return fdReceiptName;
		}
		public void setFdReceiptName(String fdReceiptName) {
			this.fdReceiptName = fdReceiptName;
		}
		public String getFdBankName() {
			return fdBankName;
		}
		public void setFdBankName(String fdBankName) {
			this.fdBankName = fdBankName;
		}
		public String getFdBankNo() {
			return fdBankNo;
		}
		public void setFdBankNo(String fdBankNo) {
			this.fdBankNo = fdBankNo;
		}
		public Double getFdAmount() {
			return fdAmount;
		}
		public void setFdAmount(Double fdAmount) {
			this.fdAmount = fdAmount;
		}
		public String getFdMemo() {
			return fdMemo;
		}
		public void setFdMemo(String fdMemo) {
			this.fdMemo = fdMemo;
		}
		public String getFdNumber() {
			return fdNumber;
		}
		public void setFdNumber(String fdNumber) {
			this.fdNumber = fdNumber;
		}
		public String getDocStatus() {
			return docStatus;
		}
		public void setDocStatus(String docStatus) {
			this.docStatus = docStatus;
		}
		public Date getFdLastModifiedTime() {
			return fdLastModifiedTime;
		}
		public void setFdLastModifiedTime(Date fdLastModifiedTime) {
			this.fdLastModifiedTime = fdLastModifiedTime;
		}
		public String getDocSubject() {
			return docSubject;
		}
		public void setDocSubject(String docSubject) {
			this.docSubject = docSubject;
		}
		public String getFdFactNodeId() {
			return fdFactNodeId;
		}
		public void setFdFactNodeId(String fdFactNodeId) {
			this.fdFactNodeId = fdFactNodeId;
		}
	}
}
