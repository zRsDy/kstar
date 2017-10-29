package com.ibm.kstar.entity.channel;


import com.ibm.kstar.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 引入销量申请表(crm_t_import_sale_apply)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-10
 */
@Entity
@Table(name = "crm_t_import_sale_apply")
public class ImportSaleApply extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 1617002260379094225L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "import_sale_apply_c_pid_generator")
   	@GenericGenerator(name="import_sale_apply_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_code")
    private String applyCode;
    
    /** 申请单位 */
    @Column(name = "c_apply_unit")
    private String applyUnit;

    /** 申请类型 */
    @Column(name = "c_apply_type")
    private String applyType;
    
    /** 销量转入单位组织*/
    @Column(name = "c_import_unit")
    private String importUnit;

	/** 销量转入员工*/
	@Column(name = "c_import_positionId")
	private String importPositionId;

	/** 销量转入单位，如果为空则为组织类型*/
	@Column(name = "c_import_employeeId")
	private String importEmployeeId;

	/** 销量转入比例*/
    @Column(name = "c_import_ratio")
    private BigDecimal importRatio;
    
    /** 销量转出单位组织 */
    @Column(name = "c_export_unit")
    private String exportUnit;

	/** 销量转出职位 */
	@Column(name = "c_export_positionId")
	private String exportPositionId;

	/** 销量转出员工，如果为空则为组织类型*/
	@Column(name = "c_export_employeeId")
	private String exportEmployeeId;

	/** 销量转出比例 */
    @Column(name = "c_export_ratio")
    private BigDecimal exportRatio;
    
    /** 状态*/
    @Column(name = "c_status")
    private String status;

	/** 货币 */
	@Column(name = "c_currency")
	private String currency;
    
    /** 申请金额 */
    @Column(name = "c_apply_amount")
    private BigDecimal applyAmount;
    
    /** 申请日期 */
    @Column(name = "c_apply_date")
    private Date applyDate;
    
    /** 组织 */
    @Column(name = "c_organization")
    private String organization;

	/** 申请人*/
    @Column(name = "c_applier")
    private String applier;
    
    @Transient
    private String applierName;
    
    /** 申请人电话 */
    @Column(name = "c_applier_phone")
    private String applierPhone;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;

	@Transient
	private String importName;

	@Transient
	private String exportName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public String getApplyUnit() {
		return applyUnit;
	}

	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}

	public String getApplyUnitName() {
		return this.getLovName(applyUnit);
	}
	
	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getApplyTypeName() {
		return this.getLovName(applyType);
	}
	
	public String getImportUnit() {
		return importUnit;
	}

	public void setImportUnit(String importUnit) {
		this.importUnit = importUnit;
	}
    
    public String getImportUnitName() {
    	return this.getLovName(importUnit);
	}
    
	public BigDecimal getImportRatio() {
		return importRatio;
	}

	public void setImportRatio(BigDecimal importRatio) {
		this.importRatio = importRatio;
	}

	public String getExportUnit() {
		return exportUnit;
	}

	public void setExportUnit(String exportUnit) {
		this.exportUnit = exportUnit;
	}
    
    public String getExportUnitName() {
    	return this.getLovName(exportUnit);
	}
    
	public BigDecimal getExportRatio() {
		return exportRatio;
	}

	public void setExportRatio(BigDecimal exportRatio) {
		this.exportRatio = exportRatio;
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
    
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrencyName() {
		return this.getLovName(currency);
	}
	
	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationName() {
		return this.getLovName(organization);
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

	public String getImportPositionId() {
		return importPositionId;
	}

	public void setImportPositionId(String importPositionId) {
		this.importPositionId = importPositionId;
	}

	public String getImportEmployeeId() {
		return importEmployeeId;
	}

	public void setImportEmployeeId(String importEmployeeId) {
		this.importEmployeeId = importEmployeeId;
	}

	public String getExportPositionId() {
		return exportPositionId;
	}

	public void setExportPositionId(String exportPositionId) {
		this.exportPositionId = exportPositionId;
	}

	public String getExportEmployeeId() {
		return exportEmployeeId;
	}

	public void setExportEmployeeId(String exportEmployeeId) {
		this.exportEmployeeId = exportEmployeeId;
	}

	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
}