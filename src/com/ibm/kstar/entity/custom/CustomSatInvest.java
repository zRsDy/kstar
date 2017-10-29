package com.ibm.kstar.entity.custom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;
import com.ibm.kstar.permission.utils.PermissionOrgId;
import com.ibm.kstar.permission.utils.PermissionPositionId;
import com.ibm.kstar.permission.utils.PermissionUserId;
/**
 * (CRM_T_CUSTOM_SAT_INVEST)
 * 
 * @author liumq
 * @version 1.0.0 2017-4-7
 */
@Entity
@Table(name = "crm_t_custom_sat_invest")
@Permission(businessType = "crm_t_custom_sat_invest")
public class CustomSatInvest implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_sat_invest_pid_generator")
	@GenericGenerator(name="custom_sat_invest_pid_generator", strategy="uuid")
    @PermissionBusinessId
    private String id;
    
    /** 调查编号 */
    @Column(name = "c_invest_code", nullable = false, length = 32)
    private String investCode;
    
    /** 负责人 **/
    @Column(name = "c_principal", nullable = false, length = 32)
    private String principal;
    
    /**办事处区域/行业 **/
    @Column(name = "c_area_or_industry", nullable = false, length = 32)
    private String areaOrIndustry;
    
    /** 服务态度 **/
    @Column(name = "c_service_attitude", nullable = true, length = 40)
    private String serviceAttitude;
    
    /** 服务态度改进 **/
    @Column(name = "c_service_attitude_improve", nullable = true, length = 300)
    private String serviceAttitudeImprove;
    
    /** 业务能力 **/
    @Column(name = "c_business_ability", nullable = true, length = 40)
    private String businessAbility;
    
    /** 业务能力改进 **/
    @Column(name = "c_business_ability_improve", nullable = true, length = 300)
    private String businessAbilityImprove;
    
	/** 工作的配合程度 **/
    @Column(name = "c_cooperate_attitude", nullable = true, length = 40)
    private String cooperateAttitude;
    
    /** 工作的配合程度改进 **/
    @Column(name = "c_cooperate_attitude_improve", nullable = true, length = 300)
    private String cooperateAttitudeImprove;
    
    /** 销售对客服的合理化建议**/
    @Column(name = "c_salesman_advice", nullable = true, length = 250)
    private String salesmanAdvice;
    
    /** 调查发起人 */
    @Column(name = "c_create_employee", nullable = false, length = 100)
    @PermissionUserId
    private String createEmployee;
    
    /** 调查发起时间 */
    @Column(name = "c_create_date", nullable = false)
    private String createDate;
    
    /** 发起人岗位 */
    @Column(name = "c_creator_pos_id", nullable = false, length = 100)
    @PermissionPositionId
    private String creatorPosId;
    
    /** 发起人组织 */
    @Column(name = "c_creator_org_id", nullable = false, length = 100)
    @PermissionOrgId
    private String creatorOrgId;
    
    /** 修改人 */
    @Column(name = "c_update_employee", nullable = true, length = 100)
    @PermissionUserId
    private String updateEmployee;
    
    /** 修改时间 */
    @Column(name = "c_update_date", nullable = true)
    private String updateDate;
    
    /**状态**/
    @Column(name = "c_status_cd", nullable = true, length = 32)
    private String statusCd;
    
    /**流程状态**/
    @Column(name = "c_proc_status_cd", nullable = false, length = 32)
    private String procStatusCd;
    
    /**季度**/
    @Column(name = "c_invest_quarter", nullable = false, length = 32)
    private String investQuarter;
    
    /** 有效时间 */
    @Column(name = "eff_date", nullable = false)
    private String effDate;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getInvestCode() {
		return investCode;
	}

	public void setInvestCode(String investCode) {
		this.investCode = investCode;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getAreaOrIndustry() {
		return areaOrIndustry;
	}

	public void setAreaOrIndustry(String areaOrIndustry) {
		this.areaOrIndustry = areaOrIndustry;
	}

	public String getServiceAttitude() {
		return serviceAttitude;
	}

	public void setServiceAttitude(String serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	public String getServiceAttitudeImprove() {
		return serviceAttitudeImprove;
	}

	public void setServiceAttitudeImprove(String serviceAttitudeImprove) {
		this.serviceAttitudeImprove = serviceAttitudeImprove;
	}

	public String getBusinessAbility() {
		return businessAbility;
	}

	public void setBusinessAbility(String businessAbility) {
		this.businessAbility = businessAbility;
	}

	public String getBusinessAbilityImprove() {
		return businessAbilityImprove;
	}

	public void setBusinessAbilityImprove(String businessAbilityImprove) {
		this.businessAbilityImprove = businessAbilityImprove;
	}

	public String getSalesmanAdvice() {
		return salesmanAdvice;
	}

	public void setSalesmanAdvice(String salesmanAdvice) {
		this.salesmanAdvice = salesmanAdvice;
	}

	public String getCooperateAttitude() {
		return cooperateAttitude;
	}

	public void setCooperateAttitude(String cooperateAttitude) {
		this.cooperateAttitude = cooperateAttitude;
	}

	public String getCooperateAttitudeImprove() {
		return cooperateAttitudeImprove;
	}

	public void setCooperateAttitudeImprove(String cooperateAttitudeImprove) {
		this.cooperateAttitudeImprove = cooperateAttitudeImprove;
	}

	public String getCreateEmployee() {
		return createEmployee;
	}

	public void setCreateEmployee(String createEmployee) {
		this.createEmployee = createEmployee;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreatorPosId() {
		return creatorPosId;
	}

	public void setCreatorPosId(String creatorPosId) {
		this.creatorPosId = creatorPosId;
	}

	public String getCreatorOrgId() {
		return creatorOrgId;
	}

	public void setCreatorOrgId(String creatorOrgId) {
		this.creatorOrgId = creatorOrgId;
	}

	public String getUpdateEmployee() {
		return updateEmployee;
	}

	public void setUpdateEmployee(String updateEmployee) {
		this.updateEmployee = updateEmployee;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public String getProcStatusCd() {
		return procStatusCd;
	}

	public void setProcStatusCd(String procStatusCd) {
		this.procStatusCd = procStatusCd;
	}

	public String getInvestQuarter() {
		return investQuarter;
	}

	public void setInvestQuarter(String investQuarter) {
		this.investQuarter = investQuarter;
	}
	
	public String getEffDate() {
		return effDate;
	}

	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	@Transient
	private String principalName;
	
	public String getPrincipalName() {
		Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(principal);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getName();
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	@Transient
	private String principalNo;
	
	public String getPrincipalNo() {
		Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(principal);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getNo();
	}

	public void setPrincipalNo(String principalNo) {
		this.principalNo = principalNo;
	}
	
	@Transient
	private String principalEmail;
	
	public String getPrincipalEmail() {
		Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(principal);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getEmail();
	}
	
	@Transient
	private String createEmployeeName;

	public String getCreateEmployeeName() {
		Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(createEmployee);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getName();
	}

	public void setCreateEmployeeName(String createEmployeeName) {
		this.createEmployeeName = createEmployeeName;
	}
	
	@Transient
	private String updateEmployeeName;

	public String getUpdateEmployeeName() {
		Employee employee = new Employee();
		Object obj = CacheData.getInstance().get(updateEmployee);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, employee);
		}
		return employee.getName();
	}

	public void setUpdateEmployeeName(String updateEmployeeName) {
		this.updateEmployeeName = updateEmployeeName;
	}
	
	@Transient
	private String procStatusCdName;

	public String getProcStatusCdName() {

		LovMember lov =  ((LovMember)CacheData.getInstance().get(procStatusCd));
		return lov==null? null : lov.getName();
	}

	public void setProcStatusCdName(String procStatusCdName) {
		this.procStatusCdName = procStatusCdName;
	}
	
	public String getServiceAttitudeName() {
		return this.getLovName(serviceAttitude);
	}
	
	public String getBusinessAbilityName() {
		return this.getLovName(businessAbility);
	}
	
	public String getCooperateAttitudeName() {
		return this.getLovName(cooperateAttitude);
	}
	
	public String getAreaOrIndustryName() {
		return this.getLovName(areaOrIndustry);
	}
	
	public String getAreaOrIndustryPathName() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(areaOrIndustry);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getNamePath();
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