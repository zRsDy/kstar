package com.ibm.kstar.api.order;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.DeliveryHeader;
import com.ibm.kstar.entity.order.DeliveryLines;
import com.ibm.kstar.entity.order.DeliveryLogistics;
import com.ibm.kstar.entity.order.DeliveryReceipt;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.util.List;

public interface IDeliveryService {
	
	void saveDelivery(DeliveryHeader deliveryHeader,UserObject userObject) throws Exception;
	/**
	 * 
	 * queryDeliveryHeaders:分页查询发货单头表. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryDeliveryHeaders(PageCondition condition);
	
	IPage queryDeliveryHeadersTtl(PageCondition condition , UserObject user);

	DeliveryHeader getDeliveryHeaderById(String deliveryHeaderId);
	
	IPage queryDeliveryLines(PageCondition condition);
	
	/**
	 * 
	 * getLogisticByDeliveryCode:根据发货单Code查询发货单物流信息. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param deliveryCode
	 * @return 
	 * @since JDK 1.7
	 */
	DeliveryLogistics getLogisticByDeliveryCode(String deliveryCode);

	/**
	 * 
	 * updateDeliveryStatus:更新发货单状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 发货单ID
	 * @param status 状态
	 * @since JDK 1.7
	 */
	void updateDeliveryStatus(String id, String status,UserObject userObject);
	
	/**
	 * 
	 * getDeliveryLinesByHid:根据发货头表ID获取行信息. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeaderId  发货头表ID
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	List<DeliveryLines> getDeliveryLinesByHid(String deliveryHeaderId)
			throws Exception;
	void updateDelivery(DeliveryHeader deliveryHeader, UserObject userObject)
			throws Exception;
	/**
	 * 
	 * updateInvoiceQty:更新发货行开票数量. <br/> 
	 * @author liming 
	 * @param id
	 * @param invoiceQty 开票数量 整数加，负数减
	 * @since JDK 1.7
	 */
	void updateInvoiceQty(String id, double invoiceQty, UserObject userObject);
	/**
	 * HQL查询发货单行，供发货单选择页面使用
	 * getDeliveryLinesByHQL:(这里用一句话描述这个方法的作用). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	IPage getDeliveryLinesByHQL(PageCondition condition) throws Exception;
	/**
	 * 
	 * getDeliveryReceiptByDCode:更新出货单CODE获取出货单的签收单，不带权限. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage getDeliveryReceiptByDCode(PageCondition condition);
	/**
	 * 
	 * deleteDeliveryLines:根据订单头ID删除发货单行. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeaderId 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void deleteDeliveryLinesByHid(String deliveryHeaderId, UserObject userObject)
			throws Exception;
	/**
	 * 
	 * getDeliveryLinesByHCode:通过出货单code获取发货明细行. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryCode 出货单编号
	 * @return 
	 * @since JDK 1.7
	 */
	List<DeliveryLines> getDeliveryLinesByHCode(String deliveryCode);
	
	/**
	 * 
	 * importDeliveryReceipt:导入出货申请签收单. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryCode 出货申请头表编号
	 * @param data 需要导入的Excel数据
	 * @param userObject 
	 * @since JDK 1.7
	 */
	void importDeliveryReceipt(String deliveryCode, List<List<Object>> data,
			UserObject userObject);
	/**
	 * 
	 * getExcelData:获取发货明细行Excel行数据. <br/>  
	 * 
	 * @author liming 
	 * @param deliveryHeaderId
	 * @param deliveryLines
	 * @return 
	 * @since JDK 1.7
	 */
	List<List<Object>> getExcelData(DeliveryHeader deliveryHeader ,
			List<DeliveryLines> deliveryLines);
	/**
	 * 
	 * checkIsErpDelivery:检查订单是否ERP已发货. <br/> 
	 * @author liming 
	 * @param id 
	 * @param op 
	 * @return 
	 * @since JDK 1.7
	 */
	String checkIsErpDelivery(String id, String op);
	
	/**
	 * 即时连接视图查询ERP是否已备货
	 * @param id
	 * @return
	 */
	String checkErpStatus(String id);
	
	String checkHasReceipt(String id,String op);
	/**
	 * 
	 * getDeliveryHeaderByCode:根据出货单编码获取出货单头信息. <br/> 
	 * 
	 * @author liming 
	 * @param code
	 * @return 
	 * @since JDK 1.7
	 */
	DeliveryHeader getDeliveryHeaderByCode(String code);
	/**
	 * 
	 * createContractReceiptDetail:生成合同收款计划明细. <br/> 
	 * @author liming 
	 * @param deliveryHeaderId 发货头表ID
	 * @param userObject  当前登录用户
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void createContractReceiptDetail(String code);
	/**
	 * 
	 * updateDeliveryLineQtyByID:更新发货单产品数量. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @param deliveryQty
	 * @param userObject 
	 * @since JDK 1.7
	 */
	void updateDeliveryLineQtyByID(String id, double deliveryQty,
			UserObject userObject);
	/**
	 * 
	 * deleteDelivery:删除发货单. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeaderId  头ID
	 * @param deliveryCode  头编号
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void deleteDelivery(String deliveryHeaderId,
			UserObject userObject) throws Exception;
	/**
	 * 
	 * getDeliveryLinesByOrderLineId:根据订单行ID找到出货单行. <br/> 
	 * 
	 * @author liming 
	 * @param orderLineId
	 * @return 
	 * @since JDK 1.7
	 */
	List<DeliveryLines> getDeliveryLinesByOrderLineId(String orderLineId);
	/**
	 * 
	 * getDeliveryReceiptListByDCode:发货单code获取签收单行. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryCode
	 * @return 
	 * @since JDK 1.7
	 */
	List<DeliveryReceipt> getDeliveryReceiptListByDCode(String deliveryCode);
	
	/**
	 * 签收单数据导出
	 * @param ids
	 * @return
	 * @throws AnneException
	 */
	List<List<Object>> exportInvoiceLinesFormLists(String[] ids)throws AnneException;

	/**
	 * 根据外部出货单号获取需要生成回款计划的出货单号
	 * @param externalNo
	 * @return
	 */
    List<String> getNeedDetailDeliveryCodeByExternalNo(String externalNo);

	/**
	 * 根据出货单行Id获取出货单行
	 * @param deliveryLineId
	 * @return
	 */
	DeliveryLines getDeliveryLine(String deliveryLineId);
}