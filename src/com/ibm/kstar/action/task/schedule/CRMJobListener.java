package com.ibm.kstar.action.task.schedule;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SimpleTrigger;

import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.scheduler.TaskSchedulerDetail;
import com.ibm.kstar.service.ISchedulerService;

public class CRMJobListener implements JobListener {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "CRMJobListener";
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobToBeExecuted(JobExecutionContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
		
		ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)arg0.getTrigger());
		if(arg1 == null){
			detail.setState("0");
			detail.setRemark("任务正常执行中");
		}else{
			detail.setState("3");
			detail.setRemark("任务执行出错:"+arg1.getCause().getMessage());
		}
		schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
	}

}
