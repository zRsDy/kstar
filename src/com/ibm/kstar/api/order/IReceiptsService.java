package com.ibm.kstar.api.order;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.ContractReceiptDetail;
import com.ibm.kstar.entity.order.ContractReceiptProduct;
import com.ibm.kstar.entity.order.Receipts;
import com.ibm.kstar.entity.order.vo.ReceiptsListVO;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface IReceiptsService {
	void saveReceipts(Receipts receipts ,UserObject userObject) throws AnneException;

	IPage queryReceipts(PageCondition condition, UserObject userObject) throws AnneException;

	void deleteReceipts(String receiptsId) throws AnneException;

	Receipts getReceipts(String receiptsId) throws AnneException;
	/**
	 * 
	 * queryVerification:(查询收款核销明细). <br/> 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	IPage queryVerification(PageCondition condition) throws AnneException;
	
	/**
	 * 
	 * getReceiptsList:条件获取收款记录. <br/>  
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<Receipts> getReceiptsList(Condition condition) throws AnneException;
	/**
	 * 
	 * getExcelData:创建收款记录的Excel数据. <br/> 
	 * 
	 * @author liming 
	 * @param receipts
	 * @return 
	 * @since JDK 1.7
	 */
	List<List<Object>> getExcelData(List<Receipts> receiptsList);
	/**
	 * 收款核销
	 * receiptsVerification:(这里用一句话描述这个方法的作用). <br/> 
	 * @author liming 
	 * @param id
	 * @param receiptsList
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void receiptsVerification(String id, ReceiptsListVO receiptsList,
			UserObject userObject) throws Exception;
	//查询合同收款计划明细
	IPage queryContractReceiptDetail(PageCondition condition)
			throws AnneException;

	/**
	 * 
	 * publishReceipts:发布收款单. <br/> 
	 * 
	 * @author liming 
	 * @param ids
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void publishReceipts(String id, UserObject userObject) throws Exception;
	/**
	 * 
	 * importReceiptsList:(导入收款记录). <br/> 
	 * 
	 * @author liming 
	 * @param data 
	 * @throws ParseException 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void importReceiptsList(List<List<Object>> data, UserObject userObject)
			throws Exception;

	void saveOrUpdateReceiptsList(ReceiptsListVO receiptsList,
			UserObject userObject) throws Exception;

	void updateReceipts(Receipts receipts, UserObject userObject)
			throws AnneException;
	/**
	 * 
	 * getReceiptsImportTemplet:获取收款导入模板. <br/> 
	 * @author liming 
	 * @return 
	 * @since JDK 1.7
	 */
	List<List<Object>> getReceiptsImportTemplet();
	/**
	 * 
	 * deleteVerification:删除核销明细. <br/> 
	 * @author liming 
	 * @param verificationId
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	void deleteVerification(String verificationId, UserObject userObject)
			throws AnneException;
	/**
	 * 
	 * returnReceipt:退回收款. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	void returnReceipt(String id, UserObject userObject) throws AnneException;

    LovMember getSaleCenter(String currentDepId);

    LovMember getBizDept(String oid, String parentId);

    /**
	 * 
	 * importReceiptPlanList:(这里用一句话描述这个方法的作用). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param data
	 * @param userObject
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	void importReceiptPlanList(List<List<Object>> data, UserObject userObject)
			throws Exception;

	List<List<Object>> getReceiptPlanImportTemplet();
	/**
	 * 
	 * cancelReceipts:(取消收款单). <br/> 
	 * 
	 * @author liming 
	 * @param receiptsId
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	void cancelReceipts(String receiptsId, UserObject userObject)
			throws AnneException;
	/**
	 *
	 * splitReceiptsSave: 收款拆分保存. <br/> 
	 * 
	 * @author liming 
	 * @param id 收款单ID
	 * @param splitAmount 拆分金额
	 * @param splitContractCode  拆分合同号
	 * @since JDK 1.7
	 */
	void splitReceiptsSave(String id, BigDecimal splitAmount,
			String splitContractCode,UserObject userObject);

	
	/**
	 * 根据核销ID获取核销明细
	 * @param detailId
	 * @return
	 */
	List<ContractReceiptProduct> getContractReceiptProduct(String detailId);
	
	/**
	 * 保存更新核销明细
	 * @param detail
	 */
	void saveOrUpdateContractReceiptProduct(ContractReceiptDetail detail,String receiptsId);
	/**
	 * 
	 * receiptsVerificationAutoService:(后天自动核销收款定时任务调用服务). <br/> 
	 * 
	 * @author liming  
	 * @since JDK 1.7
	 */
	void receiptsVerificationAutoService();

	/**
	 * 按客户进进行收款核销
	 * @param receipts
	 */
    void receiptsVerificationByCustomInfo(Receipts receipts);

	List<List<Object>> exportSelectedReceiptLists(PageCondition condition,UserObject user,String[] ids,String typ) throws AnneException;

	List<ContractReceiptDetail> checkedReceiptsPlan(String contrNo, String deliveryCode);
}