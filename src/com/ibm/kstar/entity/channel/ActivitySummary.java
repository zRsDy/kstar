package com.ibm.kstar.entity.channel;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 活动总结表(crm_t_activity_summary)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-06
 */
@Entity
@Table(name = "crm_t_activity_summary")
public class ActivitySummary extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 1424170285704997986L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "activity_summary_c_pid_generator")
   	@GenericGenerator(name="activity_summary_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_id")
    private String applyId;
	
    /** 活动开始时间 */
    @Column(name = "c_start_date")
    private Date startDate;
	
    /** 活动结束时间 */
    @Column(name = "c_end_date")
    private Date endDate;
    
    /** 活动效果评估 */
    @Column(name = "c_result_assess")
    private String resultAssess;
    
    /** 活动效果说明 */
    @Column(name = "c_result_explain")
    private String resultExplain;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getResultAssess() {
		return resultAssess;
	}

	public void setResultAssess(String resultAssess) {
		this.resultAssess = resultAssess;
	}

	public String getResultAssessName() {
		return this.getLovName(resultAssess);
	}
	
	public String getResultExplain() {
		return resultExplain;
	}

	public void setResultExplain(String resultExplain) {
		this.resultExplain = resultExplain;
	}

}