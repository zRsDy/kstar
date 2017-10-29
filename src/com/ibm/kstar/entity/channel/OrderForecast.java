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
 * 下单预测表(crm_t_order_forecast)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-09
 */
@Entity
@Table(name = "crm_t_order_forecast")
public class OrderForecast extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 2350528795418811518L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "order_forecast_c_pid_generator")
   	@GenericGenerator(name="order_forecast_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 预测单号 */
    @Column(name = "c_forecast_code")
    private String forecastCode;
    
    /** 预测单位 */
    @Column(name = "c_forecast_unit")
    private String forecastUnit;

    /** 预测起始周次 */
    @Column(name = "c_forecast_week")
    private String forecastWeek;
    
    /** 状态*/
    @Column(name = "c_status")
    private String status;

	/** 预测日期 */
    @Column(name = "c_forecast_date")
    private Date forecastDate;
    
    /** 提交人*/
    @Column(name = "c_applier")
    private String applier;
    
    @Transient
    private String applierName;
    
    /** 提交人电话 */
    @Column(name = "c_applier_phone")
    private String applierPhone;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;
    
    /** 是否经销商*/
    @Column(name = "c_dealer")
    private String dealer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForecastCode() {
		return forecastCode;
	}

	public void setForecastCode(String forecastCode) {
		this.forecastCode = forecastCode;
	}

	public String getForecastUnit() {
		return forecastUnit;
	}

	public void setForecastUnit(String forecastUnit) {
		this.forecastUnit = forecastUnit;
	}

	public String getForecastUnitName() {
		return this.getLovName(forecastUnit);
	}

	public String getForecastWeek() {
		return forecastWeek;
	}

	public void setForecastWeek(String forecastWeek) {
		this.forecastWeek = forecastWeek;
	}

	public String getForecastWeekName() {
		return this.getLovName(forecastWeek);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    public String getStatusName() {
		return this.getLovName(status);
	}
	
	public Date getForecastDate() {
		return forecastDate;
	}

	public void setForecastDate(Date forecastDate) {
		this.forecastDate = forecastDate;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApplierPhone() {
		return applierPhone;
	}

	public void setApplierPhone(String applierPhone) {
		this.applierPhone = applierPhone;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	
	public String getDealerCode() {
		return this.getLovCode(dealer);
	}
	
	public String getDealerName() {
		return this.getLovName(dealer);
	}
}