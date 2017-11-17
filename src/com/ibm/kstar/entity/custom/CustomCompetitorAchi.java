package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "crm_t_custom_competitor_achi")
public class CustomCompetitorAchi implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -5925090747411722583L;
    
    /** 主键自增 */
    @Id
    @GeneratedValue(generator = "custom_competitor_achi_generator")
	@GenericGenerator(name="custom_competitor_achi_generator", strategy="uuid")
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    private String id;
    
    /** 报告编号 */
    @Column(name = "c_report_id", nullable = false, length = 32)
    private String reportId;
    
    /** 客户编号 */
    @Column(name = "c_custom_code", nullable = false, length = 32)
    private String customCode;
    
    /** 竞争对手名称 */
    @Column(name = "c_competitor_code", nullable = false, length = 32)
    private String competitorCode;
    
    /** 竞争对手名称 */
    @Column(name = "c_competitor_name", nullable = false, length = 200)
    private String competitorName;
    
    /** 今年完成业绩 */
    @Column(name = "c_comp_year_result", nullable = true, length = 300)
    private String compYearResult;
    
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

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCompetitorCode() {
		return competitorCode;
	}

	public void setCompetitorCode(String competitorCode) {
		this.competitorCode = competitorCode;
	}

	public String getCompetitorName() {
		return competitorName;
	}

	public void setCompetitorName(String competitorName) {
		this.competitorName = competitorName;
	}

	public String getCompYearResult() {
		return compYearResult;
	}

	public void setCompYearResult(String compYearResult) {
		this.compYearResult = compYearResult;
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