package com.ibm.kstar.entity.scheduler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.quartz.SimpleTrigger;

@Entity
@Table(name = "CRM_T_SCHEDULER_DETAIL")
public class TaskSchedulerDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 主键自增 */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "crm_t_scheduler_detail_id_generator")
	@GenericGenerator(name="crm_t_scheduler_detail_id_generator", strategy="uuid")
    private String id;
    
    @Column(name = "SCHEDULER_ID", nullable = false, length = 100)
    private String schedulerId;
    
    @Column(name = "STRAT_TIME", nullable = true)
    private Date startTime;
    
    @Transient
    private String startTimeStr;
    
    @Column(name = "END_TIME", nullable = true)
    private Date endTime;
    
    @Transient
    private String endTimeStr;
    
    @Column(name = "PRE_FIRE_TIME", nullable = true)
    private Date preFireTime;
    
    @Transient
    private String preFireTimeStr;
    
    @Column(name = "NEXT_FIRE_TIME", nullable = true)
    private Date nextFireTime;
    
    @Transient
    private String nextFireTimeStr;

    @Column(name = "REPEAT_COUNT", nullable = false, length = 100)
    private String repeatCount;
    
    @Column(name = "EXCUTE_COUNT", nullable = false, length = 100)
    private String excuteCount;
    
    @Column(name = "STATE", nullable = false, length = 100)
    private String state;
    
    @Column(name = "REMARK", nullable = false, length = 500)
    private String remark;
    
    @Column(name = "EXCUTE_BATCH_NO", nullable = false, length = 100)
    private String excuteBatchNo;
    
    public TaskSchedulerDetail(){
    	
    }
    
    public TaskSchedulerDetail(SimpleTrigger simpleTrigger){
    	if(simpleTrigger == null){
			return;
		}
		if(simpleTrigger.getRepeatCount() < 1){
			this.repeatCount = "无限";
		}else{
			this.repeatCount = simpleTrigger.getRepeatCount()+1+"";
		}
		//repeatInterval = simpleTrigger.getRepeatInterval()/1000+"";
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(simpleTrigger.getPreviousFireTime() != null){
			this.preFireTime = simpleTrigger.getPreviousFireTime();
		}
		if(simpleTrigger.getNextFireTime() != null){
			this.nextFireTime = simpleTrigger.getNextFireTime();
		}
		if(simpleTrigger.getStartTime() != null){
			this.startTime = simpleTrigger.getStartTime();
		}
		if(simpleTrigger.getEndTime() != null){
			this.endTime = simpleTrigger.getEndTime();
		}
		this.excuteCount = simpleTrigger.getTimesTriggered()+"";
		this.excuteBatchNo = simpleTrigger.getJobDataMap().getString("batchNo");
		this.schedulerId = simpleTrigger.getJobDataMap().getString("schedulerId");
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getPreFireTime() {
		return preFireTime;
	}

	public void setPreFireTime(Date preFireTime) {
		this.preFireTime = preFireTime;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(String repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getExcuteCount() {
		return excuteCount;
	}

	public void setExcuteCount(String excuteCount) {
		this.excuteCount = excuteCount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExcuteBatchNo() {
		return excuteBatchNo;
	}

	public void setExcuteBatchNo(String excuteBatchNo) {
		this.excuteBatchNo = excuteBatchNo;
	}

	public String getStartTimeStr() {
		if(startTime !=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(startTime);
		}else{
			return "--";
		}
	}

	public String getEndTimeStr() {
		if(endTime !=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(endTime);
		}else{
			return "--";
		}
	}

	public String getPreFireTimeStr() {
		if(preFireTime !=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(preFireTime);
		}else{
			return "--";
		}
	}

	public String getNextFireTimeStr() {
		if(nextFireTime !=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(nextFireTime);
		}else{
			return "--";
		}
	}
}
