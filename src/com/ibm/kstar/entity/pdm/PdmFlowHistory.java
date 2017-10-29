package com.ibm.kstar.entity.pdm;

import javax.persistence.*;
import javax.persistence.Transient;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年09月21日 18:06
 * @LastModifier 黄奇
 */
@Entity
@Table(name = "V_WF_FOR_CRM",schema = "CPCBASE")
public class PdmFlowHistory {

    /** 流程ID  */
    private Long id;

    /** 流程名称  */
    private String name;

    /** 流程模板ID  */
    private Long tempId;

    /** 节点ID  */
    private Long procId;

    /** 节点名称  */
    private String procName;

    /** 节点状态(1=计划,4=启动,5=驳回,6=暂停,7=完成)  */
    private Long status;

    /** 状态名字  */
    private String statusName;
    
    /** 节点开始时间  */
    private String startTime;

    /** 节点结束时间  */
    private String endTime;

    /** 节点用户  */
    private String proUsers;

    /** 节点意见  */
    private String opinions;

    /** 是否需要确认(1=不需要,2=需要)  */
    private Long needconfirm;

    /** 表单编码  */
    private String formNo;
    
    
    @Transient
    public String getStatusName() {
    	if(this.getStatus() == 1){
    		this.statusName = "计划";
    	}else if(this.getStatus() == 4){
    		this.statusName = "启动";
		}else if(this.getStatus() == 5){
			this.statusName = "驳回";
		}else if(this.getStatus() == 6){
			this.statusName = "暂停";
		}else if(this.getStatus() == 7){
			this.statusName = "完成";
		}else {
			this.statusName = "";
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Id
    @Column(name = "WFID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WFNAME")
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "WFTEMPID")
    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    @Column(name = "PROCID")
    public Long getProcId() {
        return procId;
    }

    public void setProcId(Long procId) {
        this.procId = procId;
    }

    @Column(name = "PROCNAME")
    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    @Column(name = "STAT")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "ASTATIME")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Column(name = "AENDTIME")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Column(name = "PROUSERS")
    public String getProUsers() {
        return proUsers;
    }

    public void setProUsers(String proUsers) {
        this.proUsers = proUsers;
    }

    @Column(name = "OPINIONS")
    public String getOpinions() {
        return opinions;
    }

    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    @Column(name = "NEEDCONFIRM")
    public Long getNeedconfirm() {
        return needconfirm;
    }

    public void setNeedconfirm(Long needconfirm) {
        this.needconfirm = needconfirm;
    }

    @Column(name = "BUSINESSID")
    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
}
