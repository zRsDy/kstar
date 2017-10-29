package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import com.ibm.kstar.permission.utils.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Wutw  2017-1-9
 * 
 */
@Entity
@Table(name = "CRM_T_BIZ_SPE_PRICE_APP")
@Permission(businessType = "SpecialPrice")
public class SpecialPrice extends ComEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "crm_t_biz_spe_price_app_id_generator")
	@GenericGenerator(name="crm_t_biz_spe_price_app_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	@PermissionBusinessId
	private String id;
	
	/** 申请人 */
    @Column(name = "c_applicant", nullable = true, length = 32)
    private String applicant;

	@Transient
	private String applicantName;

	/** 申请编号 */
    @Column(name = "c_apply_number", nullable = true, length = 32)
    private String applyNumber;
    
    /** 申请状态 */
    @Column(name = "c_apply_status", nullable = true, length = 32)
    private String applyStatus;

    @Transient
	private String applyStatusName;

	public String getApplyStatusName() {
		LovMember lov = (LovMember) CacheData.getInstance().get("PROCESS_STATUS_" + applyStatus);
		return lov == null ? "" : lov.getName();
	}

	public void setApplyStatusName(String applyStatusName) {
		this.applyStatusName = applyStatusName;
	}

	/** 申请单位 */
    @Column(name = "c_apply_unit", nullable = true, length = 32)
    private String applyUnit;

	@Transient
	private String applyUnitName;

    /** 折扣 */
    @Column(name = "c_discount", nullable = true, length = 32)
    private String discount;
    
    /** 申请日期 */
    @Column(name = "dt_apply_date", nullable = true)
    private Date applyDate;
    
    /** 价目表总金额 */
    @Column(name = "c_total_price", nullable = true, length = 32)
    private String totalPrice;
    
    /** 折扣后总金额 */
    @Column(name = "c_discount_total_price", nullable = true, length = 32)
    private String discountTotalPrice;
    
    /** 特价有效期至 */
    @Column(name = "dt_valid_date", nullable = true)
    private Date validDate;
    
    /** 说明 */
    @Column(name = "c_explanation", nullable = true, length = 300)
    private String explanation;


	@Transient
	private String selectedList;

	public String getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(String selectedList) {
		this.selectedList = selectedList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDiscountTotalPrice() {
		return discountTotalPrice;
	}

	public void setDiscountTotalPrice(String discountTotalPrice) {
		this.discountTotalPrice = discountTotalPrice;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getApplicantName() {
		Employee e = (Employee) CacheData.getInstance().get(this.applicant);
		if (e != null) {
			return e.getName();
		}
		return "";
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplyUnitName() {
		LovMember l = (LovMember) CacheData.getInstance().get(this.applyUnit);
		if (l != null) {
			return l.getName();
		}
		return "";
	}

	public void setApplyUnitName(String applyUnitName) {
		this.applyUnitName = applyUnitName;
	}
}
