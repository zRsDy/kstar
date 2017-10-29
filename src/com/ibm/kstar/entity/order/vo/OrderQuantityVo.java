/** 
 * Project Name:crm 
 * File Name:ContractOrderQtyVo.java 
 * Package Name:com.ibm.kstar.entity.order.vo 
 * Date:2017年4月7日上午10:57:06 
 * Copyright (c) 2017, liming All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.entity.order.vo;  



/** 
 * ClassName:合同和订单产品数量相关 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年4月7日 上午10:57:06 <br/> 
 * @author   liming 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public class OrderQuantityVo {
	/**
	 * 产品数量
	 */
	private double proQty;
	
	/** 发货数量 */
	private double deliveryQty;

	/** 开票数量 */
	private double invoiceQty;
	
	/** 取消数量 */
	private double cancelQty;
	
	public OrderQuantityVo(){}

	public OrderQuantityVo(Object proQty, Object deliveryQty, Object invoiceQty,
			Object cancelQty) {
		this.proQty = proQty == null ? 0 : Double.parseDouble(proQty.toString());
		this.deliveryQty =  deliveryQty == null ? 0 : Double.parseDouble(deliveryQty.toString());
		this.invoiceQty =  invoiceQty == null ? 0 : Double.parseDouble(invoiceQty.toString());
		this.cancelQty =   cancelQty == null ? 0 : Double.parseDouble(cancelQty.toString());
	}

	
	public double getProQty() {
		return proQty;
	}

	public void setProQty(double proQty) {
		this.proQty = proQty;
	}

	public double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public double getInvoiceQty() {
		return invoiceQty;
	}

	public void setInvoiceQty(double invoiceQty) {
		this.invoiceQty = invoiceQty;
	}

	public double getCancelQty() {
		return cancelQty;
	}

	public void setCancelQty(double cancelQty) {
		this.cancelQty = cancelQty;
	}

	
}
  
