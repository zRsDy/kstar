package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 资料与礼品库管理表(crm_t_gift_manage)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-04
 */
@Entity
@Table(name = "crm_t_gift_manage")
public class GiftManage extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 8813157975419057911L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "gift_manage_c_pid_generator")
   	@GenericGenerator(name="gift_manage_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 礼品编号 */
    @Column(name = "c_gift_code")
    private String giftCode;
    
    /** 描述*/
    @Column(name = "c_description")
    private String giftDesc;
    
    /** 类型 */
    @Column(name = "c_type")
    private String giftType;
    
    /** 入库数量 */
   	@Column(name = "c_inbound_quantity")
   	private Integer inboundQuantity;
   	
    /** 库存数量 */
	@Column(name = "c_inventory_quantity")
	private Integer inventoryQuantity;
    
	/** 单位 */
	@Column(name = "c_unit")
	private String giftUnit;
	
	/** 货币 */
	@Column(name = "c_currency")
	private String currency;
	
	/** 单价 */
    @Column(name = "c_price")
    private BigDecimal giftPrice;
    
    /** 生效日期 */
    @Column(name = "c_start_date")
    private Date startDate;
    
    /** 失效日期 */
    @Column(name = "c_end_date")
    private Date endDate;
    
    /** 管理人员 */
    @Column(name = "c_manager")
    private String manager;

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGiftCode() {
		return giftCode;
	}

	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}

	public String getGiftDesc() {
		return giftDesc;
	}

	public void setGiftDesc(String giftDesc) {
		this.giftDesc = giftDesc;
	}

	public String getGiftType() {
		return giftType;
	}

	public void setGiftType(String giftType) {
		this.giftType = giftType;
	}
	
	public String getGiftTypeName() {
		return getLovName(giftType);
	}

	public Integer getInboundQuantity() {
		return inboundQuantity;
	}

	public void setInboundQuantity(Integer inboundQuantity) {
		this.inboundQuantity = inboundQuantity;
	}

	public Integer getInventoryQuantity() {
		return inventoryQuantity;
	}

	public void setInventoryQuantity(Integer inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}

	//已用库存数
	public Integer getUsedQuantity() {
		if(this.inboundQuantity != null && this.inventoryQuantity != null){
			return this.inboundQuantity - this.inventoryQuantity;
		}
		return 0;
	}
	
	public String getGiftUnit() {
		return giftUnit;
	}

	public void setGiftUnit(String giftUnit) {
		this.giftUnit = giftUnit;
	}
	
	public String getGiftUnitName() {
		return this.getLovName(giftUnit);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyName() {
		return this.getLovName(currency);
	}

	public BigDecimal getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(BigDecimal giftPrice) {
		this.giftPrice = giftPrice;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

}