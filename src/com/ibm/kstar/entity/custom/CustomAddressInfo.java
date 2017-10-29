package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "crm_t_custom_address_info")
public class CustomAddressInfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /**  */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_address_pid_generator")
	@GenericGenerator(name="custom_address_pid_generator", strategy="uuid")
    private String id;
    
    /** 客户基础信息主键 */
    @Column(name = "c_custom_pid", nullable = false, length = 32)
    private String customId;
    
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
    		return sb.toString();
    	} else {
    		sb.append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer2));
    	if (lov==null) {
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer3));
    	if (lov==null) {
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer4));
    	if (lov==null) {
    		return sb.toString();
    	} else {
    		sb.append("/").append(lov.getName());
    	}
    	
//    	if(StringUtil.isNotEmpty(customAddress)){
//    		sb.append("/").append(customAddress);
//    	}
    	
		return sb.toString();
	}
    
    public void setArea(String area) {
		this.area = area;
	}
    
    /** 地址 */
    @Column(name = "c_custom_address", nullable = true, length = 300)
    private String customAddress;
    
    /** 客户明细地址  */
    @Transient
  	private String fullCustomAddress;
    
    public String getFullCustomAddress() {
    	
     	StringBuilder sb = new StringBuilder();
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(layer1));
    	if (lov != null) {
    		sb.append("/").append(lov.getName());
    	} 
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer2));
    	if (lov != null) {
    		sb.append("/").append(lov.getName());
    	} 
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer3));
    	if (lov != null) {
    		sb.append("/").append(lov.getName());
    	} 
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer4));
    	if (lov != null) {
    		sb.append("/").append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer5));
    	if (lov != null) {
    		sb.append("/").append(lov.getName());
    	} 
    	
    	if(StringUtil.isNotEmpty(customAddress)){
    		sb.append("/").append(customAddress);
    	}
    	
		return sb.toString();
	}

	public void setFullCustomAddress(String fullCustomAddress) {
		this.fullCustomAddress = fullCustomAddress;
	}
	
	 @Transient
	 private String allCustomAddress;
	 
	 public String getAllCustomAddress() {
		StringBuilder sb = new StringBuilder();
    	LovMember lov =  ((LovMember)CacheData.getInstance().get(layer1));
    	if (lov != null) {
    		sb.append(lov.getName());
    	} 
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer2));
    	if (lov != null) {
    		sb.append(lov.getName());
    	} 
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer3));
    	if (lov != null) {
    		sb.append(lov.getName());
    	} 
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer4));
    	if (lov != null) {
    		sb.append(lov.getName());
    	}
    	
    	lov =  ((LovMember)CacheData.getInstance().get(layer5));
    	if (lov != null) {
    		sb.append(lov.getName());
    	} 
    	
    	if(StringUtil.isNotEmpty(customAddress)){
    		sb.append(customAddress);
    	}
    	
		return sb.toString();
	}

	public void setAllCustomAddress(String allCustomAddress) {
		this.allCustomAddress = allCustomAddress;
	}

	/** 邮政编码 */
    @Column(name = "c_custom_zip", nullable = true, length = 20)
    private String customZip;
    
    /** 地址-类型 */
    @Column(name = "c_custom_address_type", nullable = true, length = 300)
    private String customAddressType;
    
    @Transient
	private String customAddressTypeGrid;
    
    public Object getCustomAddressTypeGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(customAddressType);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setCustomAddressTypeGrid(String customAddressTypeGrid) {
		this.customAddressTypeGrid = customAddressTypeGrid;
	}
    
    /** 地址-电话 */
    @Column(name = "c_custom_address_phone", nullable = true, length = 300)
    private String customAddressPhone;
    
    /** 地址-传真 */
    @Column(name = "c_custom_address_fax", nullable = true, length = 300)
    private String customAddressFax;
    
    /** 地址-用途 */
    @Column(name = "c_custom_address_use", nullable = true, length = 300)
    private String customAddressUse;
    
    @Transient
	private String customAddressUseGrid;
    
    public Object getCustomAddressUseGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(customAddressUse);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setCustomAddressUseGrid(String customAddressUseGrid) {
		this.customAddressUseGrid = customAddressUseGrid;
	}
    
    /** 地址-有效 */
    @Column(name = "c_custom_address_valid", nullable = true, length = 32)
    private String customAddressValid;
    
    @Transient
	private String customAddressValidGrid;
    
    public Object getCustomAddressValidGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(customAddressValid);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}

		return  lov;
	}
    
    public void setCustomAddressValidGrid(String customAddressValidGrid) {
		this.customAddressValidGrid = customAddressValidGrid;
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

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getCustomAddress() {
		return customAddress;
	}

	public void setCustomAddress(String customAddress) {
		this.customAddress = customAddress;
	}

	public String getCustomAddressType() {
		return customAddressType;
	}

	public void setCustomAddressType(String customAddressType) {
		this.customAddressType = customAddressType;
	}

	public String getCustomAddressPhone() {
		return customAddressPhone;
	}

	public void setCustomAddressPhone(String customAddressPhone) {
		this.customAddressPhone = customAddressPhone;
	}

	public String getCustomAddressFax() {
		return customAddressFax;
	}

	public void setCustomAddressFax(String customAddressFax) {
		this.customAddressFax = customAddressFax;
	}

	public String getCustomAddressUse() {
		return customAddressUse;
	}

	public void setCustomAddressUse(String customAddressUse) {
		this.customAddressUse = customAddressUse;
	}

	public String getCustomAddressValid() {
		return customAddressValid;
	}

	public void setCustomAddressValid(String customAddressValid) {
		this.customAddressValid = customAddressValid;
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

	public String getCustomZip() {
		return customZip;
	}

	public void setCustomZip(String customZip) {
		this.customZip = customZip;
	}

}