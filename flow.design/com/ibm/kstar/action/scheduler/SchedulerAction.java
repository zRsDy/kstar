package com.ibm.kstar.action.scheduler;  

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.entity.scheduler.JobDetailVo;
import com.ibm.kstar.entity.scheduler.TaskScheduler;
import com.ibm.kstar.service.ISchedulerService;

/**
 * 
 * ClassName: 任务调度管理 <br/> 
 * @author liumq
 * @version  
 * @since JDK 1.7
 */

@Controller
@RequestMapping("/task/schedule")
public class SchedulerAction extends BaseAction {
	@Autowired
	ISchedulerService schedulerService;
	
	@Autowired
	Scheduler scheduler;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@LogOperate(module="流程管理：任务调度",notes="${user}页面：任务调度")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		IPage p = schedulerService.querySchedule(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="流程管理：任务调度",notes="${user}页面：任务调度新增")
	@RequestMapping("/add")
	public String add(String id, Model model){

		return forward("add");
	}
	
	@LogOperate(module="流程管理：任务调度",notes="${user}页面：任务调度新增保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(TaskScheduler taskScheduler, HttpServletRequest request){
		try {
			Class.forName(taskScheduler.getClassPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AnneException("任务类名对应的Java类不存在.\n"
					+e.toString());
		}
		
		
		if(schedulerService.checkRepeatScheduler(taskScheduler)){
			throw new AnneException("已经存在相同的任务："+taskScheduler.getJobName());
		}
		schedulerService.saveSchedule(taskScheduler);
		return sendSuccessMessage(taskScheduler.getId());
	}
	
	@LogOperate(module="流程管理：任务调度",notes="${user}页面：任务调度修改")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		TaskScheduler taskScheduler = schedulerService.getTaskScheduler(id);
		
		if(schedulerService.checkTaskOnScheduler(taskScheduler)){
			throw new AnneException("任务已经配置运行,请移除任务再修改!");
		}
		
		model.addAttribute("taskScheduler", taskScheduler);
		return forward("add");
	}
	
	@LogOperate(module="流程管理：任务调度",notes="${user}页面：任务调度修改保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(TaskScheduler taskScheduler){
		schedulerService.updateSchedule(taskScheduler);
		return sendSuccessMessage(taskScheduler.getId());
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		if(id==null){
			throw new AnneException("没有找到需要删除的任务调度信息");
		}
		TaskScheduler taskScheduler = schedulerService.getTaskScheduler(id);
		if(schedulerService.checkTaskOnScheduler(taskScheduler)){
			throw new AnneException("任务已经配置运行,请移除任务再删除!");
		}
		schedulerService.deleteSchedule(taskScheduler);
		return sendSuccessMessage(id);
	}
	
	@ResponseBody
	@RequestMapping(value="/start",method=RequestMethod.POST)
	public String start(String id){
		if(id==null){
			throw new AnneException("没有找到需要启动的任务调度");
		}
		TaskScheduler taskScheduler = schedulerService.getTaskScheduler(id);
		if(schedulerService.checkTaskOnScheduler(taskScheduler)){
			throw new AnneException("任务已经配置运行,不能重复添加!");
		}
		schedulerService.startSchedule(taskScheduler);
		return sendSuccessMessage(id);
	}
	
	@ResponseBody
	@RequestMapping(value="/pauseAndResume",method=RequestMethod.POST)
	public String pauseAndResume(String id){
		if(id==null){
			throw new AnneException("没有找到需要暂停或重启的任务调度!");
		}
		TaskScheduler taskScheduler = schedulerService.getTaskScheduler(id);
		if(!schedulerService.checkTaskOnScheduler(taskScheduler)){
			throw new AnneException("没有找到需要暂停或重启的任务调度!");
		}
		schedulerService.pauseAndResumeSchedule(taskScheduler);
		return sendSuccessMessage(id);
	}
	
	@ResponseBody
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public String remove(String id){
		if(id==null){
			throw new AnneException("没有找到需要停止的任务调度!");
		}
		TaskScheduler taskScheduler = schedulerService.getTaskScheduler(id);
		if(!schedulerService.checkTaskOnScheduler(taskScheduler)){
			throw new AnneException("没有找到需要停止的任务调度!");
		}
		schedulerService.remove(taskScheduler);
		return sendSuccessMessage(id);
	}
	
	@LogOperate(module="流程管理：任务调度",notes="${user}页面：任务调度执行详情")
	@ResponseBody
	@RequestMapping("/queryJodDetailVo")
	public String queryJodDetailVo(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String taskSchedulerId = (String)condition.getCondition("taskSchedulerId");
		if(taskSchedulerId == null || taskSchedulerId.isEmpty()){
			return sendSuccessMessage(new PageImpl(new ArrayList<JobDetailVo>(), condition.getPage(), condition.getRows(), 0));
		}
		TaskScheduler taskScheduler = schedulerService.getTaskScheduler(taskSchedulerId);
		
		if(taskScheduler == null){
			return sendSuccessMessage(new PageImpl(new ArrayList<JobDetailVo>(), condition.getPage(), condition.getRows(), 0));
		}
		
		List<JobDetailVo> list = schedulerService.getJobDetailVo(taskScheduler);
		
		IPage page = new PageImpl(list, condition.getPage(), condition.getRows(), list.size());
		return sendSuccessMessage(page);
	}
}
  
