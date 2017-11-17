package com.ibm.kstar.entity.bizopp;

import java.beans.ConstructorProperties;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Wutw  2017-1-12
 * 
 */
@Entity
@Table(name = "CRM_T_PROTOTYPE_APPLY")
public class Prototype extends ComEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "crm_t_prototype_apply_id_generator")
	@GenericGenerator(name="crm_t_prototype_apply_id_generator", strategy="uuid")
	@Column(name = "C_PID")
	private String id;
	
	/** 系统ID */
    @Column(name = "c_system_pid",  length = 32)
    private String systemPid;
    
    /** VCode */
    @Column(name = "c_vcode",  length = 32)
    private String code;
    
    /** 产品型号 */
    @Column(name = "c_product_model",  length = 32)
    private String productModel;
    
    /** 产品料号 */
    @Column(name = "c_material_number",  length = 32)
    private String materialNumber;
    
    /** 产品描述 */
    @Column(name = "c_product_desc",  length = 32)
    private String productDesc;

	/** 标准产品 */
	@Column(name = "c_is_standard",  length = 32)
	private String isStandard;

	/** 申请数量 */
    @Column(name = "c_apply_count",  length = 32)
    private String applyCount;
    
    /** 类型 */
    @Column(name = "c_type",  length = 32)
    private String type;
    
    /** 备注 */
    @Column(name = "c_remark",  length = 80)
    private String remark;


    @Column(name = "c_product_id")
	private String productId;

    @Column(name = "product_Price")
	private String productPrice;

	@Column(name = "apply_amount")
	private String applyAmount;

	public String getApplyAmount() {
		applyAmount = String.valueOf(Double.valueOf(applyCount)*Double.valueOf(productPrice));
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getId() {
		return id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemPid() {
		return systemPid;
	}

	public void setSystemPid(String systemPid) {
		this.systemPid = systemPid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(String applyCount) {
		this.applyCount = applyCount;
	}

	public String getType() {
		return type;
	}

	public String getTypeName() {
		LovMember lov = (LovMember) CacheData.getInstance().getMember("SAMPLETYPE",this.type);
		if(lov == null){
			return "";
		}
		return lov.getName();
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

}
