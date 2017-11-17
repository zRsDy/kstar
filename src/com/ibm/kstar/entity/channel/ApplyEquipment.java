package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 服务申请设备表(crm_t_apply_equipment)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-13
 */
@Entity
@Table(name = "crm_t_apply_equipment")
public class ApplyEquipment extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 2905804574394674456L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "apply_quipment_c_pid_generator")
   	@GenericGenerator(name="apply_quipment_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 服务单号 */
    @Column(name = "c_apply_id")
    private String applyId;

    /** 设备型号 */
    @Column(name = "c_equip_model")
    private String equipModel;
    
    /** 产品线 */
    @Column(name = "c_product_line")
    private String productLine;

    /** 品牌*/
    @Column(name = "c_brand")
    private String brand;
    
    /** 设备系列号*/
    @Column(name = "c_equip_series")
    private String equipSeries;
    
    /** 配件物料号 */
	@Column(name = "c_equip_materiel")
	private String equipMateriel;
	
    /** 单价 */
    @Column(name = "c_service_price")
    private BigDecimal servicePrice;
    
    /** 数量 */
	@Column(name = "c_service_quantity")
	private Integer serviceQuantity;
    
    /** 备注*/
    @Column(name = "c_remark")
    private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getEquipModel() {
		return equipModel;
	}

	public void setEquipModel(String equipModel) {
		this.equipModel = equipModel;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getEquipSeries() {
		return equipSeries;
	}

	public void setEquipSeries(String equipSeries) {
		this.equipSeries = equipSeries;
	}

	public String getEquipMateriel() {
		return equipMateriel;
	}

	public void setEquipMateriel(String equipMateriel) {
		this.equipMateriel = equipMateriel;
	}

	public BigDecimal getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(BigDecimal servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Integer getServiceQuantity() {
		return serviceQuantity;
	}

	public void setServiceQuantity(Integer serviceQuantity) {
		this.serviceQuantity = serviceQuantity;
	}

	public BigDecimal getServiceMoney() {
		BigDecimal serviceMoney = new BigDecimal(0);
		if(this.servicePrice != null && this.serviceQuantity != null){
			serviceMoney = this.servicePrice.multiply(new BigDecimal(this.serviceQuantity));
		}
		return serviceMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}