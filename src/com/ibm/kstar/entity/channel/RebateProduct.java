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
 * 返利产品组表(crm_t_rebate_product)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-15
 */
@Entity
@Table(name = "crm_t_rebate_product")
public class RebateProduct extends BaseEntity implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = 319113002288030846L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "rebate_product_c_pid_generator")
   	@GenericGenerator(name="rebate_product_c_pid_generator", strategy="uuid")
    private String id;

    /** 产品组名称 */
    @Column(name = "c_group_name")
    private String groupName;

    /** 组织 */
    @Column(name = "c_organization")
    private String organization;
    
    /** 生效日期*/
	@Column(name = "c_start_date")
	private Date startDate;
	
    /** 失效日期*/
	@Column(name = "c_end_date")
	private Date endDate;
    
    /** 创建日期*/
	@Column(name = "c_create_date")
	private Date createDate;
    
    /** 创建人 */
	@Column(name = "c_creater")
	private String creater;
	
	@Transient
	private String createrName;

    /** 说明*/
    @Column(name = "c_explain")
    private String explain;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
    
}