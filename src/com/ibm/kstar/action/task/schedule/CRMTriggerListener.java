package com.ibm.kstar.action.task.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.scheduler.TaskSchedulerDetail;
import com.ibm.kstar.service.ISchedulerService;


public class CRMTriggerListener implements TriggerListener {

	@Override
	public String getName() {
		return "CRMTriggerListener";
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext arg1, int arg2) {
		System.out.println("triggerComplete");
	}

	@Override
	public void triggerFired(Trigger arg0, JobExecutionContext arg1) {
		//System.out.println("triggerFired");
	}

	@Override
	public void triggerMisfired(Trigger arg0) {
		System.out.println("triggerMisfired");
	}

	@Override
	public boolean vetoJobExecution(Trigger arg0, JobExecutionContext arg1) {
		//System.out.println("vetoJobExecution");
		return false;
	}

}
