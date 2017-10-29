package com.ibm.kstar.api.contract;



import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;



import com.ibm.kstar.entity.contract.ContrPay;

/**
 * 产品服务类接口
 * @author wansheng
 *
 */
public interface IContrPayService {
	
	IPage query(PageCondition condition);

	void save(ContrPay contrPay, String contrNo) throws AnneException;

	ContrPay get(String id) throws AnneException;

	void update(ContrPay contrPay) throws AnneException;

	void delete(String id) throws AnneException;
	/**
	 * 
	 * getContrPayByContrId:根据合同ID查询合同收款计划. <br/> 
	 * 
	 * @author liming 
	 * @param contrId 合同ID
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<ContrPay> getContrPayByContrId(String contrId) throws AnneException;
	
	void copyPayPlanByContrId(String contrId) throws AnneException;
	
	void saveOrUpdateContrPayList(List<ContrPay> lines,String contrId) throws AnneException;
	
	void saveContrPayForLoan(String contrId) throws AnneException ;
	
	/**
	 * 根据合同ID和金额更新收款明细
	 * @param contrId
	 * @param amt
	 */
	void updateContrPayAmt(String contrId ,Double amt);
	
}
