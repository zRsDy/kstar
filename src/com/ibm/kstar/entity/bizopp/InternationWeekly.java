package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.*;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 商机国际业务周报(CRM_T_BIZ_PERFORMANCE_WEEKLY)
 * 
 * @author Gaoyuliang  2017-2-15
 * 
 */
@Entity
@Table(name = "crm_t_biz_performance_weekly")
@Permission(businessType = "InternationWeekly")
public class InternationWeekly extends ComEntity implements Serializable {
	/** 版本号 */
    private static final long serialVersionUID = -6082951837327684765L;
    
    /** primary key */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "weekly_pid_generator")
    @GenericGenerator(name="weekly_pid_generator", strategy="uuid")
	@PermissionBusinessId
    private String id;
    
    /** 销售人员 */
    @Column(name = "c_salesman", nullable = true, length = 20)
    private String salesman;
    
    /** 销售人员ID */
    @Column(name = "c_salesid", nullable = true, length = 32)
    private String salesid;
    
    /** 所属部门 */
    @Column(name = "c_department", nullable = true, length = 20)
    private String department;
    
    /** 年度 */
    @Column(name = "dt_year", nullable = true, length = 32)
    private String year;
    
    /** 周数 */
    @Column(name = "n_weeks", nullable = true, length = 32)
    private String weeks;
    
    /** 状态 */
    @Column(name = "c_status", nullable = true, length = 10)
    private String status;
    
    /** 本月新联系（个） */
    @Column(name = "n_new_client", nullable = true, length = 22)
    private Integer newClient;
    
    /** 预计成交（个） */
    @Column(name = "n_plan_deal", nullable = true, length = 22)
    private Integer planDeal;
    
    /** 客户数量(个) */
    @Column(name = "n_client_count", nullable = true, length = 22)
    private Integer clientCount;
    
    /** 本周下单客户（个） */
    @Column(name = "n_place_order_c", nullable = true, length = 22)
    private Integer placeOrderC;

	@Transient
    private String yearLabel;
    
    @Transient
    private String weeksLabel;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNewClient() {
		return newClient;
	}

	public void setNewClient(Integer newClient) {
		this.newClient = newClient;
	}

	public Integer getPlanDeal() {
		return planDeal;
	}

	public void setPlanDeal(Integer planDeal) {
		this.planDeal = planDeal;
	}

	public Integer getClientCount() {
		return clientCount;
	}

	public void setClientCount(Integer clientCount) {
		this.clientCount = clientCount;
	}

	public Integer getPlaceOrderC() {
		return placeOrderC;
	}

	public void setPlaceOrderC(Integer placeOrderC) {
		this.placeOrderC = placeOrderC;
	}

	public String getSalesid() {
		return salesid;
	}

	public void setSalesid(String salesid) {
		this.salesid = salesid;
	}

	public Object getYearLabel() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(year);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setYearLabel(String yearLabel) {
		this.yearLabel = yearLabel;
	}

	public Object getWeeksLabel() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(weeks);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setWeeksLabel(String weeksLabel) {
		this.weeksLabel = weeksLabel;
	}
}
