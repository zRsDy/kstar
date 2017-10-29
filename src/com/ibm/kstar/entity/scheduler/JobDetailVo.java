package com.ibm.kstar.entity.scheduler;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.quartz.SimpleTrigger;

/**
 * 
 * @author liumq
 * @version 1.0.0 2017-4-7
 */
public class JobDetailVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String repeatCount;
	
	private String repeatInterval;
	
	private String previousFireTime;
	
	private String nextFireTime;
	
	private String startTime;
	
	private String endTime;
	
	private String schedulerState;
	
	private String excuteCount;
	
	public JobDetailVo(){
		repeatCount = "--";
		repeatInterval = "--";
		previousFireTime = "--";
		nextFireTime = "--";
		startTime = "--";
		endTime = "--";
		schedulerState = "-1";
		excuteCount = "--";
	}
	
	public JobDetailVo(SimpleTrigger simpleTrigger){
		if(simpleTrigger == null){
			return;
		}
		if(simpleTrigger.getRepeatCount() < 1){
			repeatCount = "无限";
		}else{
			repeatCount = simpleTrigger.getRepeatCount()+"";
		}
		repeatInterval = simpleTrigger.getRepeatInterval()/1000+"";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(simpleTrigger.getPreviousFireTime() != null){
			previousFireTime = df.format(simpleTrigger.getPreviousFireTime());
		}else{
			previousFireTime = "--";
		}
		if(simpleTrigger.getNextFireTime() != null){
			nextFireTime = df.format(simpleTrigger.getNextFireTime());
		}else{
			nextFireTime = "--";
		}
		if(simpleTrigger.getStartTime() != null){
			startTime = df.format(simpleTrigger.getStartTime());
		}else{
			startTime = "--";
		}
		if(simpleTrigger.getEndTime() != null){
			endTime = df.format(simpleTrigger.getEndTime());
		}else{
			endTime = "--";
		}
		excuteCount = simpleTrigger.getTimesTriggered()+"";
	}

	public String getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(String repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(String repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public String getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(String previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	public String getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(String nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSchedulerState() {
		return schedulerState;
	}

	public void setSchedulerState(String schedulerState) {
		this.schedulerState = schedulerState;
	}

	public String getExcuteCount() {
		return excuteCount;
	}

	public void setExcuteCount(String excuteCount) {
		this.excuteCount = excuteCount;
	}
	
	
}
