package com.ibm.kstar.action.task.schedule;

import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.SimpleTrigger;

import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.scheduler.TaskSchedulerDetail;
import com.ibm.kstar.service.ISchedulerService;

public class CRMSchedulerListener implements SchedulerListener {

	/**
	 * 任务被部署时被执行
	 */
	@Override
	public void jobScheduled(Trigger trigger) {
		/*ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
		detail.setState("0");
		detail.setRemark("任务被部署");
		schedulerService.saveOrUpdateTaskSchedulerDetail(detail);*/
		System.out.println("任务被部署时被执行");
	}
	
	/**
	 * 任务被卸载时被执行
	 */
	@Override
	public void jobUnscheduled(String jobName, String groupName) {
		/*ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		SimpleTrigger trigger = schedulerService.getSimpleTrigger(jobName, groupName);
		if(trigger != null){
			TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
			detail.setState("-101");
			detail.setRemark("任务被强行中断");
			schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
		}*/
		System.out.println("任务被卸载时被执行");
	}
	
	/**
	 * 暂停时被执行
	 */
	@Override
	public void jobsPaused(String jobName, String groupName) {
		ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		SimpleTrigger trigger = schedulerService.getSimpleTrigger(jobName, groupName);
		if(trigger != null){
			TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
			detail.setState("1");
			detail.setRemark("任务被暂停");
			schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
		}
		System.out.println("暂停时被执行");
	}
	
	/**
	 * 恢复时被执行
	 */
	@Override
	public void jobsResumed(String jobName, String groupName) {
		ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		SimpleTrigger trigger = schedulerService.getSimpleTrigger(jobName, groupName);
		if(trigger != null){
			TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
			detail.setState("0");
			detail.setRemark("任务恢复运行");
			schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
		}
		System.out.println("恢复时被执行");
	}

	/**
	 * 任务执行出错时执行
	 */
	@Override
	public void schedulerError(String arg0, SchedulerException arg1) {
		//ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		System.out.println(arg0+"任务执行出错时执行");
	}
	
	/**
	 * scheduler被关闭时执行
	 */
	@Override
	public void schedulerShutdown() {
		System.out.println("scheduler被关闭时执 行");
	}
	
	/**
	 * 任务完成了它的使命，光荣退休时被执行
	 */
	@Override
	public void triggerFinalized(Trigger trigger) {
		ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
		detail.setState("-100");
		detail.setRemark("任务正常执行完毕");
		schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
		System.out.println("任务完成了它的使命，光荣退休时被执行");
	}
	
	/**
	 * 所在组的全部触发器被停止时被执行
	 */
	@Override
	public void triggersPaused(String jobName, String groupName) {
		ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		SimpleTrigger trigger = schedulerService.getSimpleTrigger(jobName, groupName);
		if(trigger != null){
			TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
			detail.setState("1");
			detail.setRemark("任务被暂停");
			schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
		}
		System.out.println(groupName+"-"+jobName+":所在组的全部触发器被停止时被执行");
	}
	
	/**
	 * 所在组的全部触发器被回复时被执行
	 */
	@Override
	public void triggersResumed(String jobName, String groupName) {
		ISchedulerService schedulerService = (ISchedulerService)ApplicationContextUtil.getBean("schedulerService");
		SimpleTrigger trigger = schedulerService.getSimpleTrigger(jobName, groupName);
		if(trigger != null){
			TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)trigger);
			detail.setState("0");
			detail.setRemark("任务恢复运行");
			schedulerService.saveOrUpdateTaskSchedulerDetail(detail);
		}
		System.out.println(groupName+"-"+jobName+":所在组的全部触发器被回复时被执行");
	}
}
