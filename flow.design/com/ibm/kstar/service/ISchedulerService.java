package com.ibm.kstar.service;

import java.util.List;
import java.util.Map;

import org.quartz.SimpleTrigger;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.scheduler.JobDetailVo;
import com.ibm.kstar.entity.scheduler.TaskScheduler;
import com.ibm.kstar.entity.scheduler.TaskSchedulerDetail;

public interface ISchedulerService {
	
	/**
	 * 分页查询任务调度配置
	 * @param condition
	 * @return
	 */
	IPage querySchedule(PageCondition condition);
	
	/**
	 * 根据主键查询任务调度配置
	 * @param id
	 * @return
	 */
	TaskScheduler getTaskScheduler(String id);
	
	/**
	 * 按条件查询任务调度配置(目前已经支持根据jobName/groupName/classPath查询,若查询条件变更,请更新注释)
	 * @param paramsMap
	 * @return
	 */
	List<TaskScheduler> findTaskSchedulerList(Map<String,Object> paramsMap);
	
	/**
	 * 校验任务调度配置是否重复(jobName、groupName一致则为重复数据)
	 * @param taskScheduler
	 * @return
	 */
	boolean checkRepeatScheduler(TaskScheduler taskScheduler);
	
	/**
	 * 新增任务调度配置
	 * @param taskScheduler
	 */
	void saveSchedule(TaskScheduler taskScheduler);
	
	/**
	 * 修改任务调度配置
	 * @param taskScheduler
	 */
	void updateSchedule(TaskScheduler taskScheduler);
	
	/**
	 * 删除任务调度配置
	 * @param taskScheduler
	 */
	void deleteSchedule(TaskScheduler taskScheduler);
	
	/**
	 * 启动任务调度配置
	 * @param taskScheduler
	 */
	void startSchedule(TaskScheduler taskScheduler);
	
	/**
	 * 暂停/重启任务调度配置
	 * @param taskScheduler
	 */
	void pauseAndResumeSchedule(TaskScheduler taskScheduler);
	
	/**
	 * 停止任务调度配置
	 * @param taskScheduler
	 */
	void remove(TaskScheduler taskScheduler);
	
	/**
	 * 根据组名和任务名获取触发器
	 * @return
	 */
	public SimpleTrigger getSimpleTrigger(String jobName, String groupName);
    
    /**
     * 获取任务状态
     * @param taskScheduler
     * @return
     */
    public int getSchedulerState(String jobName, String groupName);
    
    /**
     * 获取任务详情
     * @param taskScheduler
     * @return
     */
    public List<JobDetailVo> getJobDetailVo(TaskScheduler taskScheduler);
    
    /**
     * 判断任务调度是否已经添加到Scheduler
     * @param taskScheduler
     * @return
     */
	boolean checkTaskOnScheduler(TaskScheduler taskScheduler);
	
	/**
	 * 获取任务执行详情
	 * @param paramsMap
	 * @return
	 */
	public TaskSchedulerDetail getTaskSchedulerDetail(Map<String,Object> paramsMap);
	
	/**
	 * 保存或更新
	 * @param detail
	 */
	public void saveOrUpdateTaskSchedulerDetail(TaskSchedulerDetail detail);
}
