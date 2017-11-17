package com.ibm.kstar.entity.product;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * KstarEcrBean.java
 * ECR实体类
 * @author Neil Wan  and rights.userId = :userId
 * 2016年12月21日 下午19:12:35
 */

@Entity
@Table(name = "CRM_T_ECR")
public class KstarEcrBean extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4020571759300640908L;

	@Id
	@GeneratedValue(generator = "kstarecr_id_generator")
	@GenericGenerator(name="kstarecr_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;

	// ECR变更单号
	@Column(name = "C_ECR_CODE")
	private String ecrCode;
	
	// ECR最近生效时间
	@Column(name = "DT_EFFECT_TIME")
	private Date nearEffectTime;
	
	// 紧急程度
	@Column(name = "C_ERGENT")
	private String ergent;
	
	// 变更原因类型(外部 A客户需求变更)
	@Column(name = "C_ECR_CHANGE_TYPE")
	private String changeType;
	
	// 变更原因
	@Column(name = "C_ECR_CHANGE_REASON")
	private String changeRoot;
	
	// 变更内容/项目
	@Column(name = "C_ECR_CHANGE_CONTENT")
	private String ecrChangeContent;
	
	// 创建人
	@Column(name = "C_CREATE_BY")
	private String createBy;
	
	// ECR提交时间
	@Column(name = "DT_CREATE_TIME")
	private Date createTime;
	
	// 产品ID
	@Column(name = "C_PRO_ID")
	private String productID;
	
	// 参考已有ECR
	@Column(name = "C_REFER_PID")
	private String referId ;
	
	// 参考已有ECR
	@Column(name = "C_REFER_ECR")
	private String referIdGrid ;
	
	// ECR变更状态
	@Column(name = "C_STATUS")
	private String ecrStatus;
	
	// 申请人
	@Column(name = "C_ECR_APPLY")
	private String applyPerson;
	
	// ECN单号
	@Column(name = "C_ECN_NUMBER")
	private String ecnNumber;
	
	//驳回原因
	@Column(name="C_BACK_REASON")
	private String backReason;

	@Column(name="C_Product_Line")
	private String productLine;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEcrCode() {
		return ecrCode;
	}

	public void setEcrCode(String ecrCode) {
		this.ecrCode = ecrCode;
	}

	public Date getNearEffectTime() {
		return nearEffectTime;
	}

	public void setNearEffectTime(Date nearEffectTime) {
		this.nearEffectTime = nearEffectTime;
	}

	public String getErgent() {
		return ergent;
	}

	public void setErgent(String ergent) {
		this.ergent = ergent;
	}

	public String getUrgentName() {
		return  this.getLovName(ergent);
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeTypeName() {
		return  this.getLovName(changeType);
	}

	public String getChangeRoot() {
		return changeRoot;
	}

	public void setChangeRoot(String changeRoot) {
		this.changeRoot = changeRoot;
	}

	public String getEcrChangeContent() {
		return ecrChangeContent;
	}

	public void setEcrChangeContent(String ecrChangeContent) {
		this.ecrChangeContent = ecrChangeContent;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getReferId() {
		return referId;
	}

	public void setReferId(String referId) {
		this.referId = referId;
	}

	public String getEcrStatus() {
		return ecrStatus;
	}

	public void setEcrStatus(String ecrStatus) {
		this.ecrStatus = ecrStatus;
	}

	public String getEcrStatusName() {
		return this.getLovName(ecrStatus);
	}
	
	public String getApplyPerson() {
		return applyPerson;
	}

	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}

	public String getEcnNumber() {
		return ecnNumber;
	}

	public void setEcnNumber(String ecnNumber) {
		this.ecnNumber = ecnNumber;
	}

	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

	public String getReferIdGrid() {
		return referIdGrid;
	}

	public void setReferIdGrid(String referIdGrid) {
		this.referIdGrid = referIdGrid;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public void fillInit(UserObject userObject) {
		if (this.createdAt == null) {
			this.setCreatedAt(new Date());
		}
		if (this.createdById == null) {
			this.setCreatedById(userObject.getEmployee().getId());
		}
		if (this.getCreatedOrgId() == null) {
			this.setCreatedOrgId(userObject.getOrg().getId());
		}
		if (this.getCreatedPosId() == null) {
			this.setCreatedPosId(userObject.getPosition().getId());
		}
		this.setUpdatedById(userObject.getEmployee().getId());
		this.setUpdatedAt(new Date());
	}
}
