package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "crm_t_custom_comp_achi_deep")
public class CustomCompAchiDeep implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 6518304175925292969L;
    
    /** 主键自增 */
    @Id
    @GeneratedValue(generator = "comp_achi_deep_generator")
	@GenericGenerator(name="comp_achi_deep_generator", strategy="uuid")
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    private String id;
    
    /** 业绩信息主键 */
    @Column(name = "c_achi_pid", nullable = false, length = 32)
    private String achiPid;
    
    /** 竞争对手提报ID */
    @Column(name = "c_report_pid", nullable = false, length = 32)
    private String reportPid;
    
    /** 产品名称 */
    @Column(name = "c_product_code", nullable = false, length = 32)
    private String productCode;
    
    /** 竞争对手主推产品系列 */
    @Column(name = "c_comp_pruduct_set", nullable = true, length = 300)
    private String compPruductSet;
    
    /** 竞争对手产品卖点 */
    @Column(name = "c_comp_pruduct_usp", nullable = true, length = 300)
    private String compPruductUsp;
    
    /** 对应科士达产品系列 */
    @Column(name = "c_comp_pruduct_equ", nullable = true, length = 300)
    private String compPruductEqu;
    
    /** 科士达对应产品改善建议 */
    @Column(name = "c_comp_pruduct_sugg", nullable = true, length = 300)
    private String compPruductSugg;
    
    /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAchiPid() {
		return achiPid;
	}

	public void setAchiPid(String achiPid) {
		this.achiPid = achiPid;
	}

	public String getReportPid() {
		return reportPid;
	}

	public void setReportPid(String reportPid) {
		this.reportPid = reportPid;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCompPruductSet() {
		return compPruductSet;
	}

	public void setCompPruductSet(String compPruductSet) {
		this.compPruductSet = compPruductSet;
	}

	public String getCompPruductUsp() {
		return compPruductUsp;
	}

	public void setCompPruductUsp(String compPruductUsp) {
		this.compPruductUsp = compPruductUsp;
	}

	public String getCompPruductEqu() {
		return compPruductEqu;
	}

	public void setCompPruductEqu(String compPruductEqu) {
		this.compPruductEqu = compPruductEqu;
	}

	public String getCompPruductSugg() {
		return compPruductSugg;
	}

	public void setCompPruductSugg(String compPruductSugg) {
		this.compPruductSugg = compPruductSugg;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedPosId() {
		return createdPosId;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


}