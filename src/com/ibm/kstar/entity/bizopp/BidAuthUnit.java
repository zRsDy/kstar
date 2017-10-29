package com.ibm.kstar.entity.bizopp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * (CRM_T_BID_AUTH_UNIT)
 * 
 * @author bianj
 * @version 1.0.0 2017-05-01
 */
@Entity
@Table(name = "crm_t_bid_auth_unit")
public class BidAuthUnit implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = 1L;

	/**  */
	@Id
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "crm_t_bid_auth_unit_id_generator")
	@GenericGenerator(name = "crm_t_bid_auth_unit_id_generator", strategy = "uuid")
	private String id;

	/**  */
	@Column(name = "c_bid_id", nullable = true, length = 32)
	private String bidId;
	
	/** 商机ID */
    @Column(name = "c_business_opp_id")
    private String bizOppId;

	/**  */
	@Column(name = "c_from_id", nullable = true, length = 32)
	private String fromId;

	/**  */
	@Column(name = "c_integrator", nullable = true, length = 200)
	private String integrator;

	/**  */
	@Column(name = "c_address", nullable = true, length = 200)
	private String address;

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

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getIntegrator() {
		return integrator;
	}

	public void setIntegrator(String integrator) {
		this.integrator = integrator;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

}