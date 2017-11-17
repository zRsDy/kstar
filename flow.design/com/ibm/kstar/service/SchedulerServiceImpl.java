package com.ibm.kstar.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.UUIDUtils;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.task.schedule.CRMSchedulerListener;
import com.ibm.kstar.action.task.schedule.CRMTriggerListener;
import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.scheduler.JobDetailVo;
import com.ibm.kstar.entity.scheduler.TaskScheduler;
import com.ibm.kstar.entity.scheduler.TaskSchedulerDetail;


@Service(value="schedulerService")
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class SchedulerServiceImpl implements ISchedulerService{
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	Scheduler scheduler;
	
	@Autowired
	CRMSchedulerListener schedulerListener;
	
	@Override
	public IPage querySchedule(PageCondition condition) {
		StringBuffer hql = new StringBuffer(" from TaskScheduler ");
		return baseDao.search(hql.toString(),condition.getRows(), condition.getPage());
	}
	
	@Override
	public TaskScheduler getTaskScheduler(String id){
		return baseDao.get(TaskScheduler.class,id);
	}
	
	@Override
	public List<TaskScheduler> findTaskSchedulerList(Map<String,Object> paramsMap){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from TaskScheduler where 1=1 ");
		if(paramsMap.get("jobName") != null){
			hql.append(" and jobName=? ");
			params.add(paramsMap.get("jobName"));
		}
		if(paramsMap.get("groupName") != null){
			hql.append(" and groupName=? ");
			params.add(paramsMap.get("groupName"));
		}
		if(paramsMap.get("classPath") != null){
			hql.append(" and classPath=? ");
			params.add(paramsMap.get("classPath"));
		}
		
		List<TaskScheduler> list = baseDao.findEntity(hql.toString(), params.toArray());
		return list;
	}
	
	@Override
	public boolean checkRepeatScheduler(TaskScheduler taskScheduler){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobName", taskScheduler.getJobName());
		map.put("groupName", taskScheduler.getGroupName());
		
		List<TaskScheduler> list = findTaskSchedulerList(map);
		
		return list != null && list.size() > 0;
	}
	
	@Override
	public void saveSchedule(TaskScheduler taskScheduler){
		baseDao.save(taskScheduler);
	}
	
	@Override
	public void updateSchedule(TaskScheduler taskScheduler) {
		// TODO Auto-generated method stub
		TaskScheduler oldTaskScheduler = baseDao.get(TaskScheduler.class, taskScheduler.getId());
		if(oldTaskScheduler == null){
			throw new AnneException(ISchedulerService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldTaskScheduler.getId().equals(oldTaskScheduler.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyProperties(taskScheduler, oldTaskScheduler);
		baseDao.update(oldTaskScheduler);
	}

	@Override
	public void deleteSchedule(TaskScheduler taskScheduler) {
		try {
			//Thread.sleep(5000);
			SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName());
			if(simpleTrigger != null){
				scheduler.deleteJob(taskScheduler.getJobName(), taskScheduler.getGroupName());
			}
			baseDao.delete(taskScheduler);
		} catch (Exception e) {
			throw new AnneException(e.toString());
		}
		
	}
	
	@Override
	public void startSchedule(TaskScheduler taskScheduler) {
		try {
			Class clazz = Class.forName(taskScheduler.getClassPath());
			Date startTime = taskScheduler.getStartTime();
			if(startTime == null){
				startTime = new Date();
			}
			int repeatCount = -1;
			if(taskScheduler.getRepeatCount() != null && taskScheduler.getRepeatCount().intValue() > 0){
				repeatCount = taskScheduler.getRepeatCount().intValue()-1;
			}
			JobDetail jobDetail = new JobDetail(taskScheduler.getJobName(), taskScheduler.getGroupName(), clazz);
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			jobDataMap.put("userObject", ApplicationContextUtil.getUserObject());
			jobDataMap.put("schedulerId", taskScheduler.getId());
			
			if(taskScheduler.getJobParams() != null){
				
				String[] params = taskScheduler.getJobParams().split("\n");
				for(String param : params){
					if(param.contains("=")){
						String[] kv = param.split("=");
						jobDataMap.put(kv[0], kv[1]);
					}
				}
			}
			
			SimpleTrigger simpleTrigger = new SimpleTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName(), 
					taskScheduler.getJobName(), taskScheduler.getGroupName(), startTime, 
					taskScheduler.getEndTime(), repeatCount, (taskScheduler.getRepeatInterval())*1000);

			if(scheduler.getJobListenerNames() != null){
				for(Object name : scheduler.getJobListenerNames()){
					jobDetail.addJobListener(name.toString());
				}
			}
			
			if(scheduler.getTriggerListenerNames() != null){
				for(Object name : scheduler.getTriggerListenerNames()){
					simpleTrigger.addTriggerListener(name.toString());
				}
			}
			
//			simpleTrigger.addTriggerListener(listener.getName());
			
			if(!scheduler.getSchedulerListeners().contains(schedulerListener)){
				scheduler.addSchedulerListener(schedulerListener);
			}
			
			simpleTrigger.getJobDataMap().put("batchNo", UUIDUtils.getUUID());
			simpleTrigger.getJobDataMap().put("schedulerId", taskScheduler.getId());
			scheduler.scheduleJob(jobDetail,simpleTrigger);
			//scheduler.rescheduleJob(jobDetail.getName(), taskScheduler.getGroupName(), simpleTrigger);
			
			//taskScheduler.setStatusCd(IConstants.TASK_SCHEDULER_STATUS_RUNNING);
			
			//updateSchedule(taskScheduler);
		} catch (Exception e) {
			throw new AnneException(e.toString());
		}
	}
	
	@Override
	public void pauseAndResumeSchedule(TaskScheduler taskScheduler) {
		try {
			SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName());
			if(simpleTrigger != null){
				if(0 == getSchedulerState(taskScheduler.getJobName(), taskScheduler.getGroupName())){
					//scheduler.pauseJob(taskScheduler.getJobName(), taskScheduler.getGroupName());
					scheduler.pauseTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName());
					//taskScheduler.setStatusCd(IConstants.TASK_SCHEDULER_STATUS_PAUSE);
					//updateSchedule(taskScheduler);
				}else if(1 == getSchedulerState(taskScheduler.getJobName(), taskScheduler.getGroupName())){
					//scheduler.resumeJob(taskScheduler.getJobName(), taskScheduler.getGroupName());
					scheduler.resumeTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName());
					//taskScheduler.setStatusCd(IConstants.TASK_SCHEDULER_STATUS_RUNNING);
					//updateSchedule(taskScheduler);
				}
			}
//			TaskSchedulerDetail detail = new TaskSchedulerDetail(simpleTrigger, taskScheduler.getId());
//			detail.setState(getSchedulerState(taskScheduler)+"");
//			
//			saveOrUpdateTaskSchedulerDetail(detail);
		} catch (Exception e) {
			throw new AnneException(e.toString());
		}
	}
	
	@Override
	public void remove(TaskScheduler taskScheduler) {
		try {
			//Thread.sleep(5000);
			SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName());
			if(simpleTrigger != null){
				TaskSchedulerDetail detail = new TaskSchedulerDetail((SimpleTrigger)simpleTrigger);
				detail.setState("-101");
				detail.setRemark("任务被强行中断");
				saveOrUpdateTaskSchedulerDetail(detail);
				scheduler.unscheduleJob(taskScheduler.getJobName(), taskScheduler.getGroupName());
			}
		} catch (Exception e) {
			throw new AnneException(e.toString());
		}
		
	}

	public SimpleTrigger getSimpleTrigger(String jobName, String groupName){
		try {
			return (SimpleTrigger)scheduler.getTrigger(jobName, groupName);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public int getSchedulerState(String jobName, String groupName){
		try {
			return scheduler.getTriggerState(jobName, groupName);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public List<JobDetailVo> getJobDetailVo(TaskScheduler taskScheduler){
		String hql = "from TaskSchedulerDetail where 1=1 and schedulerId=? order by startTime desc";
		List<TaskSchedulerDetail> details = baseDao.findEntity(hql, new Object[]{taskScheduler.getId()});
		
		List<JobDetailVo> list = new ArrayList<>();
		
		if(details != null && !details.isEmpty()){
			TaskSchedulerDetail detail = details.get(0);
			JobDetailVo detailVo = new JobDetailVo();
			detailVo.setEndTime(detail.getEndTimeStr());
			detailVo.setExcuteCount(detail.getExcuteCount());
			detailVo.setNextFireTime(detail.getNextFireTimeStr());
			detailVo.setPreviousFireTime(detail.getPreFireTimeStr());
			detailVo.setRepeatCount(detail.getRepeatCount());
			detailVo.setSchedulerState(detail.getState());
			detailVo.setStartTime(detail.getStartTimeStr());
			
			list.add(detailVo);
		}
		return list;
	}
	
	@Override
	public boolean checkTaskOnScheduler(TaskScheduler taskScheduler){
		try {
			SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(taskScheduler.getJobName(), taskScheduler.getGroupName());
			JobDetail jobDetail = scheduler.getJobDetail(taskScheduler.getJobName(), taskScheduler.getGroupName());
			
			if(simpleTrigger == null || jobDetail == null){
				return false;
			}else{
				return true;
			}
		} catch (SchedulerException e) {
			throw new AnneException(e.toString());
		}
	}
	
	@Override
	public TaskSchedulerDetail getTaskSchedulerDetail(Map<String,Object> paramsMap){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from TaskSchedulerDetail where 1=1 ");
		if(paramsMap.get("schedulerId") != null){
			hql.append(" and schedulerId=? ");
			params.add(paramsMap.get("schedulerId"));
		}
		if(paramsMap.get("batchNo") != null){
			hql.append(" and excuteBatchNo=? ");
			params.add(paramsMap.get("batchNo"));
		}
		
		TaskSchedulerDetail taskSchedulerDetail = baseDao.findUniqueEntity(hql.toString(), params.toArray());
		
		return taskSchedulerDetail;
	}
	
	public void saveOrUpdateTaskSchedulerDetail(TaskSchedulerDetail detail){
		
		TaskScheduler taskScheduler = getTaskScheduler(detail.getSchedulerId());
		
		Map<String,Object> paramsMap = new HashMap<>();
		paramsMap.put("schedulerId", detail.getSchedulerId());
		paramsMap.put("batchNo", detail.getExcuteBatchNo());
		//detail.setState(getSchedulerState(taskScheduler)+"");
		
		TaskSchedulerDetail oldDtail = getTaskSchedulerDetail(paramsMap);
		if(oldDtail != null){
			BeanUtils.copyPropertiesIgnoreNull(detail,oldDtail);
//			oldDtail.setRemark(detail.getRemark());
			oldDtail.setNextFireTime(detail.getNextFireTime());
//			oldDtail.setPreFireTime(detail.getPreFireTime());
//			oldDtail.setState(detail.getState());
			
			
			int count = Integer.parseInt(oldDtail.getRepeatCount()) - (Integer.parseInt(detail.getRepeatCount())-Integer.parseInt(detail.getExcuteCount()));
			
			oldDtail.setExcuteCount(count+"");
			
			baseDao.update(oldDtail);
		}else{
			baseDao.save(detail);
		}
		
		//执行过程中报错，强制中断执行
		if("3" == detail.getState()){
			try {
				scheduler.unscheduleJob(taskScheduler.getJobName(), taskScheduler.getGroupName());
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
