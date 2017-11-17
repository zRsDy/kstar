package com.ibm.kstar.entity.channel;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 活动主要内容表(crm_t_activity_content)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-06
 */
@Entity
@Table(name = "crm_t_activity_content")
public class ActivityContent extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = -309697367160421983L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "activity_content_c_pid_generator")
   	@GenericGenerator(name="activity_content_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_id")
    private String applyId;
    
    /** 培训项目 */
    @Column(name = "c_train_project")
    private String trainProject;
	
	/** 预计活动时间 */
    @Column(name = "c_estimated_activity_date")
    private Date estimatedActDate;
	
	/** 实际活动时间 */
    @Column(name = "c_actual_activity_date")
    private Date actualActDate;

	/** 活动地点 */
    @Column(name = "c_activity_place")
    private String activityPlace;

	/** 内容描述 */
    @Column(name = "c_content_desc")
    private String contentDesc;
    
    /** 负责人员 */
    @Column(name = "c_responsible_person")
    private String responsiblePerson;
    
    /** 执行人员 */
    @Column(name = "c_maker")
    private String maker;
    
    @Transient
    private String makerName;
    
    /** 状态 */
	@Column(name = "c_status")
	private String status;

	/** 执行说明 */
    @Column(name = "c_make_explain")
    private String makeExplain;
    
    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getTrainProject() {
		return trainProject;
	}

	public void setTrainProject(String trainProject) {
		this.trainProject = trainProject;
	}

	public Date getEstimatedActDate() {
		return estimatedActDate;
	}

	public void setEstimatedActDate(Date estimatedActDate) {
		this.estimatedActDate = estimatedActDate;
	}

	public Date getActualActDate() {
		return actualActDate;
	}

	public void setActualActDate(Date actualActDate) {
		this.actualActDate = actualActDate;
	}

	public String getActivityPlace() {
		return activityPlace;
	}

	public void setActivityPlace(String activityPlace) {
		this.activityPlace = activityPlace;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getMakerName() {
		return makerName;
	}

	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMakeExplain() {
		return makeExplain;
	}

	public void setMakeExplain(String makeExplain) {
		this.makeExplain = makeExplain;
	}

}