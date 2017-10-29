package com.ibm.kstar.entity.bizopp;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 商机国际业绩周报进展汇报表(CRM_T_BIZ_WEEKLY_REPORT)
 * 
 * @author bianj
 * @version 1.0.0 2017-02-13
 */
@Entity
@Table(name = "crm_t_biz_weekly_report")
public class InternationWeeklyReport implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -1912141458143927630L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "weeklyreport_pid_generator")
	@GenericGenerator(name="weeklyreport_pid_generator", strategy="uuid")
    private String id;
    
    /** 周报题头主表ID */
    @Column(name = "c_fk_weekly_id", nullable = true, length = 32)
    private String fkWeeklyId;
    
    /** 客户ID */
    @Column(name = "c_fk_client_id", nullable = true, length = 32)
    private String fkClientId;
    
    /** 客户名称 */
    @Column(name = "n_client_name", nullable = true, length = 30)
    private String clientName;
    
    /** 客户类别 新客户 */
    @Column(name = "n_is_new_client", nullable = true, length = 22)
    private Integer isNewClient;
    
    /** 客户类别 老客户 */
    @Column(name = "n_is_old_client", nullable = true, length = 22)
    private Integer isOldClient;
    
    /** 后备机 */
    @Column(name = "n_backup_machine", nullable = true, length = 22)
    private Integer isBackupMachine;
    
    /** 家用逆变器 */
    @Column(name = "n_home_inverter", nullable = true, length = 22)
    private Integer isHomeInverter;
    
    /** 工频机 */
    @Column(name = "n_power_frequency_machine", nullable = true, length = 22)
    private Integer isPowerFrequencyMachine;
    
    /** 电池 */
    @Column(name = "n_battery", nullable = true, length = 22)
    private Integer isBattery;
    
    /** >20K高频 */
    @Column(name = "n_high_frequency", nullable = true, length = 22)
    private Integer isHighFrequency;
    
    /** IDU/IDM */
    @Column(name = "n_idu_idm", nullable = true, length = 22)
    private Integer isIduIdm;
    
    /** 单相光伏逆变器 */
    @Column(name = "n_single_phase_inverter", nullable = true, length = 22)
    private Integer isSinglePhaseInverter;
    
    /** 三相光伏逆变器 */
    @Column(name = "n_three_phase_inverter", nullable = true, length = 22)
    private Integer isThreePhaseInverter;
    
    /** 集中型逆变器 */
    @Column(name = "n_central_inverter", nullable = true, length = 22)
    private Integer isCentralInverter;
    
    /** MW房 */
    @Column(name = "n_mw_house", nullable = true, length = 22)
    private Integer isMwHouse;
    
    /** 储能 */
    @Column(name = "n_stored_energy", nullable = true, length = 22)
    private Integer isStoredEnergy;
    
    /** 空调 */
    @Column(name = "n_air_condition", nullable = true, length = 22)
    private Integer isAirCondition;
    
    /** 未出货订单金额（USD） 本月 */
    @Column(name = "n_not_shipped_this_month", nullable = true)
    private BigDecimal notShippedThisMonth;
    
    /** 未出货订单金额（USD） 下月 */
    @Column(name = "n_not_shipped_next_month", nullable = true)
    private BigDecimal notShippedNextMonth;
    
    /** 未出货订单金额（USD） 下下月 */
    @Column(name = "n_not_shipped_n_n_month", nullable = true)
    private BigDecimal notShippedNNMonth;
    
    /** 出货订单金额（USD） 本月 */
    @Column(name = "n_shipped_this_month", nullable = true)
    private BigDecimal shippedThisMonth;
    
    /** 出货订单金额（USD） 下月 */
    @Column(name = "n_shipped_next_month", nullable = true)
    private BigDecimal shippedNextMonth;
    
    /** 出货订单金额（USD） 下下月 */
    @Column(name = "n_shipped_n_n_month", nullable = true)
    private BigDecimal shippedNNMonth;
    
    /** 客户沟通维护方式 邮件 */
    @Column(name = "n_contact_method_mail", nullable = true, length = 22)
    private Integer isContactMethodMail;
    
    /** 客户沟通维护方式 电话 */
    @Column(name = "n_contact_method_phone", nullable = true, length = 22)
    private Integer isContactMethodPhone;
    
    /** 客户沟通维护方式 拜访 */
    @Column(name = "n_contact_method_visit", nullable = true, length = 22)
    private Integer isContactMethodVisit;
    
    /** 客户沟通维护方式 来访 */
    @Column(name = "n_contact_come_visit", nullable = true, length = 22)
    private Integer isContactComeVisit;
    
    /** 存在问题和处理方式 */
    @Column(name = "c_problem_method", nullable = true, length = 300)
    private String problemMethod;
    
    /** 备注 */
    @Column(name = "c_remark", nullable = true, length = 300)
    private String remark;

    /** 客户类别*/
    @Transient
    private String clientType;
    
    /** 客户类别*/
    @Transient
    private String productNeed;
    
    /** 联系方式*/
    @Transient
    private String contactWay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFkWeeklyId() {
		return fkWeeklyId;
	}

	public void setFkWeeklyId(String fkWeeklyId) {
		this.fkWeeklyId = fkWeeklyId;
	}
	
	public String getFkClientId() {
		return fkClientId;
	}

	public void setFkClientId(String fkClientId) {
		this.fkClientId = fkClientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getIsNewClient() {
		return isNewClient;
	}

	public void setIsNewClient(Integer isNewClient) {
		this.isNewClient = isNewClient;
	}

	public Integer getIsOldClient() {
		return isOldClient;
	}

	public void setIsOldClient(Integer isOldClient) {
		this.isOldClient = isOldClient;
	}

	public Integer getIsBackupMachine() {
		return isBackupMachine;
	}

	public void setIsBackupMachine(Integer isBackupMachine) {
		this.isBackupMachine = isBackupMachine;
	}

	public Integer getIsHomeInverter() {
		return isHomeInverter;
	}

	public void setIsHomeInverter(Integer isHomeInverter) {
		this.isHomeInverter = isHomeInverter;
	}

	public Integer getIsPowerFrequencyMachine() {
		return isPowerFrequencyMachine;
	}

	public void setIsPowerFrequencyMachine(Integer isPowerFrequencyMachine) {
		this.isPowerFrequencyMachine = isPowerFrequencyMachine;
	}

	public Integer getIsBattery() {
		return isBattery;
	}

	public void setIsBattery(Integer isBattery) {
		this.isBattery = isBattery;
	}

	public Integer getIsHighFrequency() {
		return isHighFrequency;
	}

	public void setIsHighFrequency(Integer isHighFrequency) {
		this.isHighFrequency = isHighFrequency;
	}

	public Integer getIsIduIdm() {
		return isIduIdm;
	}

	public void setIsIduIdm(Integer isIduIdm) {
		this.isIduIdm = isIduIdm;
	}

	public Integer getIsSinglePhaseInverter() {
		return isSinglePhaseInverter;
	}

	public void setIsSinglePhaseInverter(Integer isSinglePhaseInverter) {
		this.isSinglePhaseInverter = isSinglePhaseInverter;
	}

	public Integer getIsThreePhaseInverter() {
		return isThreePhaseInverter;
	}

	public void setIsThreePhaseInverter(Integer isThreePhaseInverter) {
		this.isThreePhaseInverter = isThreePhaseInverter;
	}

	public Integer getIsCentralInverter() {
		return isCentralInverter;
	}

	public void setIsCentralInverter(Integer isCentralInverter) {
		this.isCentralInverter = isCentralInverter;
	}

	public Integer getIsMwHouse() {
		return isMwHouse;
	}

	public void setIsMwHouse(Integer isMwHouse) {
		this.isMwHouse = isMwHouse;
	}

	public Integer getIsStoredEnergy() {
		return isStoredEnergy;
	}

	public void setIsStoredEnergy(Integer isStoredEnergy) {
		this.isStoredEnergy = isStoredEnergy;
	}

	public Integer getIsAirCondition() {
		return isAirCondition;
	}

	public void setIsAirCondition(Integer isAirCondition) {
		this.isAirCondition = isAirCondition;
	}

	public BigDecimal getNotShippedThisMonth() {
		return notShippedThisMonth;
	}

	public void setNotShippedThisMonth(BigDecimal notShippedThisMonth) {
		this.notShippedThisMonth = notShippedThisMonth;
	}

	public BigDecimal getNotShippedNextMonth() {
		return notShippedNextMonth;
	}

	public void setNotShippedNextMonth(BigDecimal notShippedNextMonth) {
		this.notShippedNextMonth = notShippedNextMonth;
	}

	public BigDecimal getNotShippedNNMonth() {
		return notShippedNNMonth;
	}

	public void setNotShippedNNMonth(BigDecimal notShippedNNMonth) {
		this.notShippedNNMonth = notShippedNNMonth;
	}
	
	public BigDecimal getShippedThisMonth() {
		return shippedThisMonth;
	}

	public void setShippedThisMonth(BigDecimal shippedThisMonth) {
		this.shippedThisMonth = shippedThisMonth;
	}

	public BigDecimal getShippedNextMonth() {
		return shippedNextMonth;
	}

	public void setShippedNextMonth(BigDecimal shippedNextMonth) {
		this.shippedNextMonth = shippedNextMonth;
	}

	public BigDecimal getShippedNNMonth() {
		return shippedNNMonth;
	}

	public void setShippedNNMonth(BigDecimal shippedNNMonth) {
		this.shippedNNMonth = shippedNNMonth;
	}

	public Integer getIsContactMethodMail() {
		return isContactMethodMail;
	}

	public void setIsContactMethodMail(Integer isContactMethodMail) {
		this.isContactMethodMail = isContactMethodMail;
	}

	public Integer getIsContactMethodPhone() {
		return isContactMethodPhone;
	}

	public void setIsContactMethodPhone(Integer isContactMethodPhone) {
		this.isContactMethodPhone = isContactMethodPhone;
	}

	public Integer getIsContactMethodVisit() {
		return isContactMethodVisit;
	}

	public void setIsContactMethodVisit(Integer isContactMethodVisit) {
		this.isContactMethodVisit = isContactMethodVisit;
	}

	public Integer getIsContactComeVisit() {
		return isContactComeVisit;
	}

	public void setIsContactComeVisit(Integer isContactComeVisit) {
		this.isContactComeVisit = isContactComeVisit;
	}

	public String getProblemMethod() {
		return problemMethod;
	}

	public void setProblemMethod(String problemMethod) {
		this.problemMethod = problemMethod;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getProductNeed() {
		return productNeed;
	}

	public void setProductNeed(String productNeed) {
		this.productNeed = productNeed;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}
}