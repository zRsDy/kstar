package com.ibm.kstar.entity.common.territory;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * (CRM_T_TERRITORY_INFO)
 * 
 * @author bianj
 * @version 1.0.0 2017-04-27
 */
@Entity
@Table(name = "crm_t_territory_info")
public class TerritoryInfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /**  */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "territory_info_pid_generator")
   	@GenericGenerator(name="territory_info_pid_generator", strategy="uuid")
    private String id;
    
    /**  */
    @Column(name = "c_code", nullable = true, length = 32)
    private String code;
    
    @Transient
	private String codeDisp;
    
    public String getCodeDisp() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(code));
		if (lov==null) {
			return null;
		}
		return lov.getCode();
    }
    
    @Transient
	private String name;
    
    public String getName() {
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(code));
		if (lov==null) {
			return null;
		}
		return lov.getName();
    }
    
    /**  */
    @Column(name = "c_lev1", nullable = true, length = 32)
    private String lev1;
    
    /**  */
    @Column(name = "c_lev2", nullable = true, length = 32)
    private String lev2;
    
    /**  */
    @Column(name = "c_lev3", nullable = true, length = 32)
    private String lev3;
    
    /**  */
    @Column(name = "c_lev4", nullable = true, length = 32)
    private String lev4;
    
    /**  */
    @Column(name = "c_lev5", nullable = true, length = 32)
    private String lev5;
    
    /**  */
    @Column(name = "c_lev6", nullable = true, length = 32)
    private String lev6;
    
    @Transient
	private String area;
    
    public String getArea() {
    	if(!StringUtils.isEmpty(lev6)){
    		LovMember lov =  ((LovMember)CacheData.getInstance().get(lev6));
    		if (lov==null) {
    			return null;
    		}
    		return lov.getNamePath();
    	}
    	
    	if(!StringUtils.isEmpty(lev5)){
    		LovMember lov =  ((LovMember)CacheData.getInstance().get(lev5));
    		if (lov==null) {
    			return null;
    		}
    		return lov.getNamePath();
    	}
    	
    	if(!StringUtils.isEmpty(lev4)){
    		LovMember lov =  ((LovMember)CacheData.getInstance().get(lev4));
    		if (lov==null) {
    			return null;
    		}
    		return lov.getNamePath();
    	}
    	
    	if(!StringUtils.isEmpty(lev3)){
    		LovMember lov =  ((LovMember)CacheData.getInstance().get(lev3));
    		if (lov==null) {
    			return null;
    		}
    		return lov.getNamePath();
    	}
    	
    	if(!StringUtils.isEmpty(lev2)){
    		LovMember lov =  ((LovMember)CacheData.getInstance().get(lev2));
    		if (lov==null) {
    			return null;
    		}
    		return lov.getNamePath();
    	}
    	
    	if(!StringUtils.isEmpty(lev1)){
    		LovMember lov =  ((LovMember)CacheData.getInstance().get(lev1));
    		if (lov==null) {
    			return null;
    		}
    		return lov.getNamePath();
    	}
    	
    	return null;
	}
    
    public void setArea(String area) {
		this.area = area;
	}
    
    /**  */
    @Column(name = "c_comment", nullable = true, length = 300)
    private String comment;
    
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLev1() {
		return lev1;
	}

	public void setLev1(String lev1) {
		this.lev1 = lev1;
	}

	public String getLev2() {
		return lev2;
	}

	public void setLev2(String lev2) {
		this.lev2 = lev2;
	}

	public String getLev3() {
		return lev3;
	}

	public void setLev3(String lev3) {
		this.lev3 = lev3;
	}

	public String getLev4() {
		return lev4;
	}

	public void setLev4(String lev4) {
		this.lev4 = lev4;
	}

	public String getLev5() {
		return lev5;
	}

	public void setLev5(String lev5) {
		this.lev5 = lev5;
	}

	public String getLev6() {
		return lev6;
	}

	public void setLev6(String lev6) {
		this.lev6 = lev6;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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