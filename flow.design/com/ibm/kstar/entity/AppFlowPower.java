package com.ibm.kstar.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "sys_t_app_flow_power")
public class AppFlowPower implements Serializable{

	private static final long serialVersionUID = 1l;

	@Id
	@GeneratedValue(generator = "sys_t_sys_t_app_flow_power")
	@GenericGenerator(name="sys_t_sys_t_app_flow_power", strategy="uuid")
	@Column(name="c_pid")
	private String id;
	
	@Column(name="c_application_id")
	private String applicationId;
	
	@Column(name="c_area_name")
	private String areaName;
	
	@Column(name="c_power")
	private String power;
	
	@Column(name="c_remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getPowerName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(power);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
