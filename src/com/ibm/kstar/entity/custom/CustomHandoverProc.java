package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "crm_t_custom_handover_proc")
public class CustomHandoverProc implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_handover_proc_pid_generator")
	@GenericGenerator(name="custom_handover_proc_pid_generator", strategy="uuid")
    private String id;
    
    /** 交接编号 */
    @Column(name = "c_handover_code", nullable = false, length = 32)
    private String handoverCode;
    
    /** 商机编码 */
    @Column(name = "c_business_oppo_code", nullable = true, length = 32)
    private String businessOppoCode;
    
    /** 商机名称 */
    @Column(name = "c_bizopp_name", nullable = true, length = 100)
    private String bizoppName;
    
    /** 合同编号 */
    @Column(name = "c_contract_code", nullable = true, length = 32)
    private String contractCode;
    
    /** 合同名称 */
    @Column(name = "c_contract_name", nullable = true, length = 100)
    private String contractName;
    
    /** 未尽事项 */
    @Column(name = "c_goingon_proc", nullable = true, length = 100)
    private String goingonProc;
    
    /** 详细描述 */
    @Column(name = "c_comment", nullable = true, length = 100)
    private String comment;
    
    /** 后续工作及对接人 */
    @Column(name = "c_continue_by", nullable = true, length = 100)
    private String continueBy;
    
    /** 已完成 */
    @Column(name = "c_finish_flg", nullable = true, length = 32)
    private String finishFlg;
    
    @Transient
	private String finishFlgName;
    
    public String getFinishFlgName() {
		
		LovMember lov = (LovMember)CacheData.getInstance().get(finishFlg);
		return  lov == null ? null: lov.getName();
	}
    
    
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

	public String getHandoverCode() {
		return handoverCode;
	}

	public void setHandoverCode(String handoverCode) {
		this.handoverCode = handoverCode;
	}

	public String getBusinessOppoCode() {
		return businessOppoCode;
	}

	public void setBusinessOppoCode(String businessOppoCode) {
		this.businessOppoCode = businessOppoCode;
	}

	public String getBizoppName() {
		return bizoppName;
	}

	public void setBizoppName(String bizoppName) {
		this.bizoppName = bizoppName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getGoingonProc() {
		return goingonProc;
	}

	public void setGoingonProc(String goingonProc) {
		this.goingonProc = goingonProc;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContinueBy() {
		return continueBy;
	}

	public void setContinueBy(String continueBy) {
		this.continueBy = continueBy;
	}

	public String getFinishFlg() {
		return finishFlg;
	}

	public void setFinishFlg(String finishFlg) {
		this.finishFlg = finishFlg;
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

	public void setFinishFlgName(String finishFlgName) {
		this.finishFlgName = finishFlgName;
	}


}