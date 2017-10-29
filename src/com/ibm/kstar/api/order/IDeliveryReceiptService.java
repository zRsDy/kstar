package com.ibm.kstar.api.order;


import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.DeliveryReceipt;

public interface IDeliveryReceiptService {
	
	void saveDeliveryReceipt(DeliveryReceipt deliveryReceipt,UserObject userObject)
			throws AnneException;

	void deleteDeliveryReceipt(String deliveryReceiptId) throws AnneException;

	DeliveryReceipt getDeliveryReceipt(String deliveryReceiptId)
			throws AnneException;

	/**
	 * 
	 * updateStatus:修改签收单状态. <br/> 
	 * @author liming 
	 * @param id
	 * @param status 
	 * @since JDK 1.7
	 */
	void updateStatus(String id, String status);
	/**
	 * 
	 * updateDeliveryReceipt:出货单签收. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryReceipt
	 * @param userObject 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void updateDeliveryReceipt(DeliveryReceipt deliveryReceipt,UserObject userObject) throws Exception;
	/**
	 * 
	 * queryDeliveryReceipts:获取发货单对应签收信息. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	IPage queryDeliveryReceipts(PageCondition condition, UserObject userObject)
			throws AnneException;

	List<DeliveryReceipt> getDeliveryReceipts(PageCondition condition,
			UserObject userObject) throws AnneException;

	List<List<Object>> getExcelData(List<DeliveryReceipt> deliveryReceipts);
	/**
	 * 
	 * setSearchCondition:设置查询条件. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param userObject 
	 * @since JDK 1.7
	 */
	void setSearchCondition(PageCondition condition, UserObject userObject);
}