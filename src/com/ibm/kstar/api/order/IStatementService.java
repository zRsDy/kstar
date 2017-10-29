package com.ibm.kstar.api.order;


import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.StatementMaster;

public interface IStatementService {
	
	void saveStatementMaster(StatementMaster statementMaster,UserObject userObject);

	void updateStatementMaster(StatementMaster statementMaster);

	IPage queryStatementMasters(PageCondition condition);

	void deleteStatementMaster(String statementMasterId);

	StatementMaster getStatementMaster(String id);
	/**
	 * 
	 * queryStatementInvoiceMaster:获取开票申请单头表. <br/> 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	IPage queryStatementInvoiceMaster(PageCondition condition) throws Exception;
	/**
	 * 
	 * queryStatementInvoiceDetail:获取开票申请行表. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	IPage queryStatementInvoiceDetail(PageCondition condition) throws Exception;
	/**
	 * 
	 * queryStatementDeliveryLines:获取发货行列表. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	IPage queryStatementDeliveryLines(PageCondition condition) throws Exception;
	/**
	 * 
	 * queryStatementReceipts:获取对账单收款. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	IPage queryStatementReceipts(PageCondition condition) throws Exception;
	/**
	 * 
	 * publish:发布对账单. <br/>  
	 * 
	 * @author liming 
	 * @param id
	 * @param userObject
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void publish(String id, UserObject userObject) throws Exception;
	/**
	 * 
	 * updateStatus:更新对账单状态
	 * 
	 * @author liming 
	 * @param id
	 * @param status 
	 * @since JDK 1.7
	 */
	void updateStatus(String id, String status, UserObject userObject);
	
}