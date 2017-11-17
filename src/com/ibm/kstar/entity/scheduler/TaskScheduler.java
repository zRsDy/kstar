package com.ibm.kstar.entity.scheduler;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * (CRM_T_SCHEDULER)
 * 
 * @author liumq
 * @version 1.0.0 2017-4-7
 */
@Entity
@Table(name = "crm_t_scheduler")
@Permission(businessType = "crm_t_scheduler")
public class TaskScheduler implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "crm_t_scheduler_pid_generator")
	@GenericGenerator(name="crm_t_scheduler_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    @Column(name = "c_job_name", nullable = false, length = 100)
    private String jobName;
    
    @Column(name = "c_group_name", nullable = false, length = 100)
    private String groupName;
    
    @Column(name = "c_class_path", nullable = false, length = 100)
    private String classPath;
    
    @Column(name = "c_start_time", nullable = true)
    private Date startTime;
    
    @Column(name = "c_end_time", nullable = true)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
    private Date endTime;
    
    @Column(name = "c_repeat_count", nullable = true, length = 10)
    private Long repeatCount;
    
    @Column(name = "c_repeat_interval", nullable = true, length = 10)
    private Long repeatInterval;
    
    @Column(name = "c_status_cd", nullable = true, length = 40)
    private String statusCd;
    
    @Column(name = "c_job_params", nullable = true, length = 1000)
    private String jobParams;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
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

	public Long getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Long repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public String getJobParams() {
		return jobParams;
	}

	public void setJobParams(String jobParams) {
		this.jobParams = jobParams;
	}
	
	@Transient
	private String statusCdName;
	
	public String getStatusCdName() {
		return this.getLovName(statusCd);
	}
	
	public String getLovName(String lovId) {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(lovId);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
}
