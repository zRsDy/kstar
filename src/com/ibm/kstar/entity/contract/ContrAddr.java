package com.ibm.kstar.entity.contract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;





/**
 * KstarContract.java 产品实体类
 * 
 * @author Neil Wan and rights.userId = :userId 2016年12月14日 下午1:12:35
 */
@Entity
@Table(name = "CRM_T_CONTR_ADDR")

public class ContrAddr implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658308573164369043L;


	@Id
	@GeneratedValue(generator = "crm_t_contr_addr_id_generator")
	@GenericGenerator(name="crm_t_contr_addr_id_generator", strategy="uuid")
	@Column(name = "C_ID")
	private String id;
	
		
	@Column(name = "C_CONTR_NO")
	private String contrId;
	
	@Column(name = "C_CONTR_NAME")
	private String contrName;
	
	@Column(name = "C_CONTR_TYPE")
	private String contrType;
	
	@Column(name = "C_DELIV_ADDR")
	private String delivAddr;
	
	@Column(name = "C_RECEIVER")
	private String receiver;	
	
	@Column(name = "C_PHONE")
	private String phone;
	
	@Column(name = "C_REMARK")
	private String remark;

    /** 业务类型CONTR_STAND/CHANGE/LOAN/PI */
    @Column(name = "C_TYPE", nullable = true, length = 32)
    private String cType;
	
	 /** 区域大分类 */
    @Column(name = "c_area_lov1", nullable = true, length = 32)
    private String layer1;
    
    /** 区域中分类1 */
    @Column(name = "c_area_lov2", nullable = true, length = 32)
    private String layer2;
    
    /** 区域中分类2 */
    @Column(name = "c_area_lov3", nullable = true, length = 32)
    private String layer3;
    
    /** 区域小分类1 */
    @Column(name = "c_area_lov4", nullable = true, length = 32)
    private String layer4;
    
    /** 区域小分类2 */
    @Column(name = "c_area_lov5", nullable = true, length = 32)
    private String layer5;
    
    @Transient
	private String area;
    
    public String getArea() {
    	StringBuilder sb = new StringBuilder();
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(layer1));
    	if (lov==null) {
    		if (!StringUtils.isEmpty(delivAddr)) {
        		sb.append(" ").append(delivAddr);
        	}
    		
    		return sb.toString();
    	} else {
    		sb.append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer2));
    	if (lov==null) {
    		if (!StringUtils.isEmpty(delivAddr)) {
        		sb.append(" ").append(delivAddr);
        	}
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer3));
    	if (lov==null) {
    		if (!StringUtils.isEmpty(delivAddr)) {
        		sb.append(" ").append(delivAddr);
        	}
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer4));
    	if (lov==null) {
    		if (!StringUtils.isEmpty(delivAddr)) {
        		sb.append(" ").append(delivAddr);
        	}
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer5));
    	if (lov==null) {
    		if (!StringUtils.isEmpty(delivAddr)) {
        		sb.append(" ").append(delivAddr);
        	}
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
    	if (!StringUtils.isEmpty(delivAddr)) {
    		sb.append(" ").append(delivAddr);
    	}
    	
		return sb.toString();
	}
    
    public void setArea(String area) {
		this.area = area;
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

	public String getContrId() {
		return contrId;
	}

	public void setContrId(String contrId) {
		this.contrId = contrId;
	}

	public String getContrName() {
		return contrName;
	}

	public void setContrName(String contrName) {
		this.contrName = contrName;
	}

	public String getContrType() {
		return contrType;
	}

	public void setContrType(String contrType) {
		this.contrType = contrType;
	}

	public String getDelivAddr() {
		return delivAddr;
	}

	public void setDelivAddr(String delivAddr) {
		this.delivAddr = delivAddr;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCType() {
		return cType;
	}

	public void setCType(String cType) {
		this.cType = cType;
	}

	public String getLayer1() {
		return layer1;
	}

	public void setLayer1(String layer1) {
		this.layer1 = layer1;
	}

	public String getLayer2() {
		return layer2;
	}

	public void setLayer2(String layer2) {
		this.layer2 = layer2;
	}

	public String getLayer3() {
		return layer3;
	}

	public void setLayer3(String layer3) {
		this.layer3 = layer3;
	}

	public String getLayer4() {
		return layer4;
	}

	public void setLayer4(String layer4) {
		this.layer4 = layer4;
	}

	public String getLayer5() {
		return layer5;
	}

	public void setLayer5(String layer5) {
		this.layer5 = layer5;
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
