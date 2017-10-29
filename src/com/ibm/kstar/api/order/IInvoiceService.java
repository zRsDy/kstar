package com.ibm.kstar.api.order;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.Participant;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.InvoiceDetail;
import com.ibm.kstar.entity.order.InvoiceGoldenTax;
import com.ibm.kstar.entity.order.InvoiceMaster;

public interface IInvoiceService {

	IPage queryInvoiceMasters(PageCondition condition);

	InvoiceMaster getInvoiceMaster(String invoiceMasterId);
	
	InvoiceMaster getInvoiceMasterByCode(String invoiceCode);
	
	/**
	 * 
	 * queryInvoiceDetail:(获取发货单对应的明细行). <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryInvoiceDetail(PageCondition condition);

	void saveInvoice(InvoiceMaster invoiceMaster,UserObject userObject) throws Exception;
	/**
	 * 
	 * updateStatus:更新开票申请状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 开票申请头ID
	 * @param status 状态
	 * @param userObject 用户
	 * @since JDK 1.7
	 */
	void updateStatus(String id, String status, UserObject userObject);

	void updateInvoice(InvoiceMaster invoiceMaster, UserObject userObject)
			throws Exception;

	void deleteInvoiceDetailByHid(String invoiceMasterId, UserObject userObject)
			throws Exception;

	void deleteInvoice(String invoiceMasterId, UserObject userObject)
			throws Exception;
	/**
	 * 
	 * getInvoiceDetailListByMId:根据主表ID获取所有开票明细行. <br/> 
	 * 
	 * @author liming 
	 * @param invoiceMasterId
	 * @return 
	 * @since JDK 1.7
	 */
	List<InvoiceDetail> getInvoiceDetailListByMId(String invoiceMasterId,String invoiceType);
	/**
	 * 
	 * getInvoiceDetailListByMCode:根据主表CODE获取所有开票明细行. <br/> 
	 * 
	 * @author liming 
	 * @param invoiceMasterCode
	 * @return 
	 * @since JDK 1.7
	 */
	List<InvoiceDetail> getInvoiceDetailListByMCode(String invoiceMasterCode);
	/**
	 * 
	 * queryInvoiceGoldenTax:查询金税明细行. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryInvoiceGoldenTax(PageCondition condition);

	List<InvoiceGoldenTax> getInvoiceGoldenTaxListByMCode(String invoiceCode);

	List<List<Object>> getTaxListExcelData(List<InvoiceGoldenTax> taxList);
	/**
	 * 
	 * getInvoiceDetailListByOrderLineId:根据订单行ID查询开票行（未发货开票）. <br/> 
	 * 
	 * @author liming 
	 * @param orderLineId 订单行ID
	 * @return 
	 * @since JDK 1.7
	 */
	List<InvoiceDetail> getInvoiceDetailListByOrderLineId(String orderLineId);
	/**
	 * 
	 * checkInvoiceStatus:检查开票申请是否已经审核通过. <br/> 
	 * 
	 * @author liming 
	 * @param orderLineId
	 * @return 
	 * @since JDK 1.7
	 */
	boolean checkInvoiceStatus(String orderLineId);
	
	/**
	 * 已发货未开票明细Excel导出
	 * @param ids
	 * @return
	 * @throws AnneException
	 */
	List<List<Object>> exportSelectedContrLists(String[] ids) throws AnneException;
	
	/**
	 * 提前开票明细Excel导出
	 * @param ids
	 * @return
	 * @throws AnneException
	 */
	List<List<Object>> exportInvoiceLinesFormLists(String[] ids)throws AnneException;
	

	//设置流程参数
	public void setBusinessVariable(String businessKey,String processInstanceId,Participant participant );
	

}