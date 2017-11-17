package com.ibm.kstar.entity.order.vo;

import java.util.List;

import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.entity.product.KstarProduct;

/** 
 * ClassName:OrderLinesVO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年1月17日 下午5:50:10 <br/> 
 * @author   liming 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
public class OrderLinesVO {
	
	private String id;
	private OrderLines lines;
	private KstarProduct product;
	
	public OrderLinesVO(List<Object> list) {
		this.lines = (OrderLines)list.get(0);
		this.product = (KstarProduct)list.get(1);
		this.id = product.getId();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public OrderLines getLines() {
		return lines;
	}
	public void setLines(OrderLines lines) {
		this.lines = lines;
	}
	public KstarProduct getProduct() {
		return product;
	}
	public void setProduct(KstarProduct product) {
		this.product = product;
	}
	

	
}
  
