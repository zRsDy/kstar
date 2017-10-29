package com.ibm.kstar.api.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderHeaderChange;
import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.entity.order.OrderLinesChange;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.entity.order.vo.OrderVO;
import com.ibm.kstar.entity.quot.KstarPrjLst;

/**
 * ClassName:IOrderService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年12月16日 上午11:09:12 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 * @see
 */
public interface IOrderService {
	
	/**
	 * 
	 * queryOrderHeaders:查询订单头. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryOrderHeaders(PageCondition condition);
	/**
	 * 
	 * queryOrderLines:查询订单行表. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryOrderLines(PageCondition condition);
	/**
	 * 获取所有列表
	 * getOrderLinesByOrderId:(这里用一句话描述这个方法的作用). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param orderId
	 * @return 
	 * @since JDK 1.7
	 */
	List<Object[]> getKstarProductByOrderId(String orderId);
	
	/**
	 * 
	 * saveOrUpdateOrderLines:(更新订单行). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param orderLines 
	 * @since JDK 1.7
	 */
	List<OrderLines> getOrderLinesOrderId(String orderId);
	
	List<OrderLinesChange> getOrderLinesChangeOrderId(String orderId);
	/**
	 * 
	 * updateOrder:更新订单头和行. <br/> 
	 * 
	 * @author liming 
	 * @param orderHeader
	 * @param userObject
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void updateOrder(OrderHeader orderHeader,UserObject userObject) throws Exception;
	
	void updateOrderChangeStatus(String orderId, String status,UserObject userObject);
	
	List<OrderHeaderChange> getOrderChangeByOrderId(String orderId) throws AnneException;
	
	IPage queryOrderLinesChange(PageCondition condition);
	/**
	 * 
	 * updateOrderControlStatus:更新控制状态. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param orderId
	 * @param status 
	 * @since JDK 1.7
	 */
	void updateOrderControlStatus(String orderId, String status,UserObject userObject);
	/**
	 * 
	 * updateOrderExecuteStatus:更新订单执行状态. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param orderId
	 * @param status 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void updateOrderExecuteStatus(String orderId, String status,UserObject userObject) throws Exception;
	
	/**
	 * 
	 * queryOrderSelectList:产品选择列表. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryOrderSelectList(PageCondition condition);
	
	/**
	 * 
	 * deleteOrder:删除订单头和订单行. <br/> 
	 * 
	 * @author liming 
	 * @param orderHeaderId 
	 * @since JDK 1.7
	 */
	void deleteOrder(String orderHeaderId, UserObject userObject);
	
	/**
	 * 
	 * queryOrderHeaderByCode:根据订单编号获取订单头信息. <br/> 
	 * 
	 * @author liming 
	 * @param orderCode
	 * @return 
	 * @since JDK 1.7
	 */
	OrderHeader queryOrderHeaderByCode(String orderCode);
	
	/**
	 * 
	 * saveOrder:保存订单. <br/> 
	 * 
	 * @author liming 
	 * @param orderHeader 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void saveOrder(OrderHeader orderHeader, UserObject userObject)
			throws Exception;
	/**
	 * 
	 * splitLine:订单行拆分. <br/> 
	 * 
	 * @author liming 
	 * @param orderLineId  订单行ID
	 * @param deliveryLineId  订单行ID
	 * @param quantity 拆分数量
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void splitLine(String op, String orderLineId, String deliveryLineId,
			double quantity, UserObject userObject) throws Exception;
	/**
	 * 
	 * updateOrderLinesStatus:更新订单行状态. <br/> 
	 * @author liming 
	 * @param id
	 * @param status
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void updateOrderLinesStatus(String id, String status, UserObject userObject)
			throws Exception;
	
	/**
	 * 
	 * updateOrderLinesInvoiceStatus:更新订单行开票状态. <br/> 
	 * @author liming 
	 * @param id 订单行ID
	 * @param status 1 是、0 否
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void updateOrderLinesInvoiceStatus(String id, String status,
			UserObject userObject) throws Exception;
	
	/**
	 * 
	 * updateOrderLinesStatusByHeaderId:(这里用一句话描述这个方法的作用). <br/> 
	 * 
	 * @author liming 
	 * @param orderHeaderId
	 * @param status
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void updateOrderLinesStatusByHeaderId(String orderHeaderId, String status,
			UserObject userObject) throws Exception;
	/**
	 * 
	 * getContractOrderQty:根据订单编号和合同行号获取订单发货、开票、取消数量. <br/> 
	 * 
	 * @author liming 
	 * @param sourceCode 
	 * 			来源编号(合同、商机编号)
	 * @param sourceId 
	 * 			来源ID合同行号、商机ID)
	 * @return com.ibm.kstar.entity.order.vo.OrderQuantityVo 
	 * 			订单相关数量
	 * @since JDK 1.7
	 */
	OrderQuantityVo getContractOrderQty(String sourceCode, String sourceId);
	
	/**
	 * 
	 * getContractOrderQty:根据特价编号和特价行ID获取订单发货、开票、取消数量. <br/> 
	 * 
	 * @author liming 
	 * @param spCode 
	 * 			特价编号
	 * @param spLineId 
	 * 			特价行ID
	 * @return com.ibm.kstar.entity.order.vo.OrderQuantityVo 
	 * 			订单相关数量
	 * @since JDK 1.7
	 */
	OrderQuantityVo getRebateOrderQty(String spCode,String spLineId);
	
	
	/**
	 * 
	 * updateContractOrderLinePending:将订单行改为暂挂. <br/> 
	 * 
	 * @author liming 
	 * @param contractId 合同ID
	 * @param contractLineNo 合同行号
	 * @param userObject  当前登录用户
	 * @since JDK 1.7
	 */
	void updateContractOrderLinePending(String contractId,
			String contractLineNo, UserObject userObject);
	/**
	 * 
	 * createOrderLinesByContract:合同创建订单时调用此方法生成订单行. <br/> 
	 * 
	 * @author liming 
	 * @param contractId
	 * @return 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	List<OrderLines> createOrderLinesByContract(String contractId,UserObject userObject) throws Exception;
	/**
	 * 
	 * createOrderHeaderByContract:合同创建订单时调用此方法生成订单头. <br/> 
	 * 
	 * @author liming 
	 * @param contractId
	 * @return 
	 * @since JDK 1.7
	 */
	OrderHeader createOrderHeaderByContract(OrderHeader orderHeader,
			String contractId);
	
	/**
	 * 
	 * updateOrderLinesByContractChange:合同变更、更新订单信息. <br/> 
	 * @author liming 
	 * @param contractId 合同ID
	 * @param kstarPrjLsts 合同变更后的工程清单列表（只需要传变更过的产品行）
	 * @param userObject 当前登录用户
	 * @since JDK 1.7
	 */
	void updateOrderLinesByContractChange(String contractId,
			List<KstarPrjLst> kstarPrjLsts, UserObject userObject,String changeType);
	/**
	 * 
	 * createOrderHeaderByBizopp:商机生成订单. <br/> 
	 * 
	 * @author liming 
	 * @param bizappId  商机ID
	 * @param userObject 操作用户
	 * @return 
	 * @since JDK 1.7
	 */
	OrderHeader createOrderHeaderByBizopp(OrderHeader orderHeader,
			String bizappId, UserObject userObject);
	/**
	 * 
	 * createOrderLinesByBizopp:根据商机生成订单行. <br/> 
	 * 
	 * @author liming 
	 * @param bizoppId 商机ID
	 * @param userObject 操作用户
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	List<OrderLines> createOrderLinesByBizopp(String bizoppId,
			UserObject userObject) throws Exception;
	/**
	 * 
	 * getSequenceCode:(这里用一句话描述这个方法的作用). <br/> 
	 * 
	 * @author liming 
	 * @param docType 类型
	 * @param prefix 前缀
	 * @return 
	 * @since JDK 1.7
	 */
	String getSequenceCode(String docType, String prefix);
	/**
	 * 
	 * updateOrderInvoiceQty:更新订单行行开票数量. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @param invoiceQty 发货数量
	 * @since JDK 1.7
	 */
	void updateOrderInvoiceQty(String id, double invoiceQty, UserObject userObject);
	/**
	 * 
	 * cancelOrderLine:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author liming 
	 * @param lineId 订单行ID
	 * @since JDK 1.7
	 */
	void cancelOrderLine(String lineId, UserObject userObject);
	/**
	 * 
	 * cancelOrderLine:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author liming 
	 * @param lineId 订单行ID
	 * @since JDK 1.7
	 */
	void cancelAdvanceBilling(String lineId, UserObject userObject);
	/**
	 * 
	 * salesmanChange:销售人员变更后销售中心和部门联动. <br/> 
	 * 
	 * @author liming 
	 * @param oid 组织ID
	 * @return 
	 * @since JDK 1.7
	 */
	Map<String, String> salesmanChange(String orgId);
	/**
	 * 
	 * hasValidOrder:根据合同ID 查询 是否存在有效状态的订单. <br/> 
	 * 
	 * @author liming 
	 * @param contractId 合同ID
	 * @return true 存在有效订单， false 不存在有效订单
	 * @since JDK 1.7
	 */
	boolean hasValidOrder(String contractId);
	OrderHeader queryOrderHeaderById(String orderHeaderId);
	/**
	 * 
	 * getOrderLinesOrderCode:根据订单Code获取订单行信息. <br/> 
	 * 
	 * @author liming 
	 * @param orderCode 订单编号
	 * @return 
	 * @since JDK 1.7
	 */
	List<OrderLines> getOrderLinesOrderCode(String orderCode);
	/**
	 * 
	 * calculateSparePrice:0价格订单计算备件金额. <br/> 
	 * 
	 * @author liming 
	 * @param erpOrderCode ERP订单编号
	 * @return 
	 * @since JDK 1.7
	 */
	BigDecimal calculateSparePrice(String erpOrderCode, UserObject userObject);
	/**
	 * 
	 * getOrderLinesErpOrderCode:根据ERP订单编号获取所有订单行. <br/> 
	 * @author liming 
	 * @param erpOrderCode
	 * @return 
	 * @since JDK 1.7
	 */
	List<OrderLines> getOrderLinesErpOrderCode(String erpOrderCode);
	/**
	 * 
	 * queryOrderHeaderBySourceId:根据来源类型ID查询 最新的订单. <br/> 
	 * @author liming 
	 * @param sourceId
	 * @return 
	 * @since JDK 1.7
	 */
	OrderHeader queryOrderHeaderBySourceId(String sourceId);
	
	/**
	 * 订单excel导出
	 * @param condition
	 * @return
	 * @throws AnneException
	 */
	List<List<Object>> exportSelectedContrLists(String[] ids) throws AnneException;
	
	LovMember getOrderSalesmanCenter(String oid);
	LovMember getOrderSalesmanDep(String oid, String parentId);
	/**
	 * 
	 * checkOrderBook:订单登记前校验. <br/> 
	 * 
	 * @author liming 
	 * @param orderCode 订单编号
	 * @return 
	 * @since JDK 1.7
	 */
	Map<String,String> checkOrderBook(String orderCode);
	Map<String,String> checkOrderSplitLine(String orderCode, String lineNum,double proQty);
	/**
	 * 
	 * checkOrderSplitLineSave:拆行保存后调用ERP接口. <br/> 
	 * 
	 * @author liming 
	 * @param orderCode 订单code
	 * @param sourceLineNum 被拆订单行的行号
	 * @param newLineNum 拆分订单行的行号
	 * @param proQty 拆分订单行的数量
	 * @return 
	 * @since JDK 1.7
	 */
	Map<String,String> checkOrderSplitLineSave(String orderCode,String sourceLineNum,String newLineNum,double proQty);
	
	/**
	 * 
	 * calculateProPrice:计算产品价格. <br/> 
	 * 
	 * @author liming 
	 * @param customerCode 客户编号
	 * @param priceTableId 标准价格表ID
	 * @param lines 订单列表
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	List<Map<Object, Object>> calculateProPrice(String customerCode,
			String priceTableId, List<Map<Object, Object>> lines)
			throws Exception;
	/**
	 * 
	 * updateOrderLinePendingBySP:特价变更，将对应的订单行改为暂挂状态. <br/> 
	 * @author liming 
	 * @param spCode 特价申请编号 (必填)
	 * @param spLineId 特价申请行行ID (选填，为空时会影响特价申请所有行对应的订单行)
	 * @param op 操作类型： Yes-将订单设置为暂挂， No-将订单行暂挂取消
	 * @param userObject 
	 * @since JDK 1.7
	 */
	void updateOrderLinePendingBySP(String spCode, String spLineId, String op,
			UserObject userObject);
	/**
	 * 
	 * getOrderLinesChangeByChangeHID:根据订单变更单头ID查询变更行. <br/> 
	 * 
	 * @author liming 
	 * @param changeId
	 * @return 
	 * @since JDK 1.7
	 */
	List<OrderLinesChange> getOrderLinesChangeByChangeHID(String changeId);
	/**
	 * 
	 * queryOrderHeaderChanges:查询变更列表. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryOrderHeaderChanges(PageCondition condition);
	
	void saveOrderChange(OrderHeader orderHeader, UserObject userObject)
			throws Exception;
	void updateOrderChange(OrderHeaderChange orderHeader, UserObject userObject,String updateOrderChange)
			throws Exception;
	List<OrderVO> queryOrderListByCondition(Condition condition);
	List<List<Object>> getExcelData(List<OrderVO> orders);
	
	/**
	 * 
	 * deleteOrderChange:删除变更订单头和变更订单行. <br/> 
	 * 
	 * @author 张钧鑫
	 * @param orderHeaderChangeId 
	 * @since JDK 1.7
	 */
	void deleteOrderChange(String id, UserObject userObject);
	
	/**
	 * 已审批通过的订单元数据 保存到订单变更做为初始元数据
	 * @param businessKey
	 */
	void saveCopyOrderChange(String businessKey);
	
	int countByChange(String id);
	
	
	LovMember getOrderSalesmanCenterG(String oid);
	
	LovMember getOrderSalesmanDepG(String oid, String parentId);
	/**
	 * 
	 * salesmanChangeG:回款计划销售人员变更后销售中心和部门联动. <br/> 
	 * 
	 * @author 张钧鑫 
	 * @param oid 组织ID
	 * @return 
	 * @since JDK 1.7
	 */
	Map<String, String> salesmanChangeG(String orgId);
	
}
