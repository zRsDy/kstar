package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * @author gaoyuliang  2017-2-26
 * 
 */
@Entity
@Table(name = "crm_t_biz_org")
public class BizOrg implements Serializable {
	
	/** 版本号 */
    private static final long serialVersionUID = 8767255541212446323L;
	
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "bizorg_pid_generator")
	@GenericGenerator(name="bizorg_pid_generator", strategy="uuid")
    private String id;
    
    /** 外键 关联商机编号 */
    @Column(name = "c_fk_bizopp_id", nullable = false, length = 2)
    private String bizOppId;
    
    /** 干系方名称 */
    @Column(name = "c_enterprise_name", nullable = true, length = 80)
    private String enterpriseName;
    
    /** 业务关系 */
    @Column(name = "c_biz_relationship", nullable = true, length = 30)
    private String bizRelationship;

    @Transient
    private String bizRelationshipLable;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getBizRelationship() {
		return bizRelationship;
	}

	public void setBizRelationship(String bizRelationship) {
		this.bizRelationship = bizRelationship;
	}

	public Object getBizRelationshipLable() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(bizRelationship);
		if(obj != null ){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}

	public void setBizRelationshipLable(String bizRelationshipLable) {
		this.bizRelationshipLable = bizRelationshipLable;
	}
}
