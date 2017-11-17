package com.ibm.kstar.entity.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.cache.CacheUtils;
/**
 * 
 * ClassName: 产品序列号库  <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年4月13日 下午2:05:50 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Entity
@Table(name = "crm_int_prod_series")
public class IntProdSerie implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** primary key */
    @Id
    @Column(name = "id", unique = true)
	@GeneratedValue(generator = "int_prod_series_generator")
	@GenericGenerator(name = "int_prod_series_generator", strategy = "uuid")
    private String id;
    
    /** 序列号 */
    @Column(name = "series_name")
    private String seriesName;
    
    /** 最后更新时间 */
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    
    /** 签收单号 */
    @Column(name = "receive_number")
    private String receiveNumber;
    
    /** 实际出货日期 */
    @Column(name = "anzhuang_completed_date")
    private Date anzhuangCompletedDate;
    
    /** 产品型号 */
    @Column(name = "item_mode")
    private String itemMode;
    
    /** 物料编号 */
    @Column(name = "item_number")
    private String itemNumber;
    
    /** 产品描述 */
    @Column(name = "item_desc")
    private String itemDesc;
    
    /** 单位 */
    @Column(name = "uom")
    private String uom;
    
    /** 数量 */
    @Column(name = "receive_quantity")
    private Integer receiveQuantity;
    
    /** 已录数量 */
    @Column(name = "series_count")
    private Integer seriesCount;
    
    /** 件数 */
    @Column(name = "zongjianshu")
    private Integer zongjianshu;
    
    /** 备注 */
    @Column(name = "comments")
    private String comments;
    
    /** 签收单打印日期 */
    @Column(name = "receive_prt_date")
    private Date receivePrtDate;
    
    /** 客户名称 */
    @Column(name = "ship_to_customer")
    private String shipToCustomer;
    
    /** 出货单号 */
    @Column(name = "external_shipment_number")
    private String externalShipmentNumber;
    
    /** 订单编号 */
    @Column(name = "order_number")
    private Integer orderNumber;
    
    /** 创建日期 */
    @Column(name = "creation_date")
    private Date creationDate;
    
    /** 接口信息 */
    @Column(name = "int_message")
    private String intMessage;
    
    /** 接口数据状态(S:接收处理成功 E:接收处理失败) */
    @Column(name = "int_status")
    private String intStatus;

    @Transient
    private String consignee;
    
    /*收货人*/
    @Transient
    private String receAddress;
    
    /*收货地址*/
    @Transient
    private DeliveryReceipt receipt;
    
    public IntProdSerie(){}

    public IntProdSerie(IntProdSerie intProdSerie,String consignee,String receAddress){
    	this.itemNumber = intProdSerie.getItemNumber();
    	this.consignee = consignee;
    	this.receAddress = receAddress;
    }
     
    public DeliveryReceipt getDeliveryReceipt(){
    	if(receipt != null){
    		return receipt;
    	}
    	this.receipt = CacheUtils.getDeliveryReceipt(this.receiveNumber);
    	if(this.receipt == null){
    		this.receipt = new DeliveryReceipt();
    	}
    	return receipt;
    }
    
	public String getConsignee() {
		return getDeliveryReceipt().getConsignee();
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getReceAddress() {
		return getDeliveryReceipt().getReceAddress();
	}

	public void setReceAddress(String receAddress) {
		this.receAddress = receAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getReceiveNumber() {
		return receiveNumber;
	}

	public void setReceiveNumber(String receiveNumber) {
		this.receiveNumber = receiveNumber;
	}

	public Date getAnzhuangCompletedDate() {
		return anzhuangCompletedDate;
	}

	public void setAnzhuangCompletedDate(Date anzhuangCompletedDate) {
		this.anzhuangCompletedDate = anzhuangCompletedDate;
	}

	public String getItemMode() {
		return itemMode;
	}

	public void setItemMode(String itemMode) {
		this.itemMode = itemMode;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Integer getReceiveQuantity() {
		return receiveQuantity;
	}

	public void setReceiveQuantity(Integer receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
	}

	public Integer getSeriesCount() {
		return seriesCount;
	}

	public void setSeriesCount(Integer seriesCount) {
		this.seriesCount = seriesCount;
	}

	public Integer getZongjianshu() {
		return zongjianshu;
	}

	public void setZongjianshu(Integer zongjianshu) {
		this.zongjianshu = zongjianshu;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getReceivePrtDate() {
		return receivePrtDate;
	}

	public void setReceivePrtDate(Date receivePrtDate) {
		this.receivePrtDate = receivePrtDate;
	}

	public String getShipToCustomer() {
		return shipToCustomer;
	}

	public void setShipToCustomer(String shipToCustomer) {
		this.shipToCustomer = shipToCustomer;
	}

	public String getExternalShipmentNumber() {
		return externalShipmentNumber;
	}

	public void setExternalShipmentNumber(String externalShipmentNumber) {
		this.externalShipmentNumber = externalShipmentNumber;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getIntMessage() {
		return intMessage;
	}

	public void setIntMessage(String intMessage) {
		this.intMessage = intMessage;
	}

	public String getIntStatus() {
		return intStatus;
	}

	public void setIntStatus(String intStatus) {
		this.intStatus = intStatus;
	}


   
}