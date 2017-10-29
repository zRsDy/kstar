package com.ibm.kstar.api.contract;


import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.ContrVeriDetail;
import com.ibm.kstar.entity.contract.ContrVeriDetlLstVO;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.contract.ContractEliminate;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.*;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.model.HistoryActivityInstance;

import java.util.List;

//import com.ibm.kstar.entity.quot.KstarBiddcevl;


/**
 * 产品服务类接口
 * @author wansheng
 *
 */
public interface IContractService {
	
	
	IPage query(PageCondition condition);

	void save(Contract contract,UserObject user) throws AnneException;

	Contract get(String id) throws AnneException;

	void update(Contract contract,ProcessForm form) throws AnneException;

	void delete(String id) throws AnneException;
	
	List<List<Object>> exportContractsHead(PageCondition condition) throws AnneException;
	
	/**
	 * 合同导出
	 * @param condition
	 * @param user
	 * @param ids
	 * @param typ
	 * @return
	 * @throws AnneException
	 */
	List<List<Object>> exportSelectedContrLists(PageCondition condition,UserObject user,String[] ids,String typ) throws AnneException;
	
	/**
	 * 合同申请——工程清单导出
	 * @param 
	 */
	List<List<Object>> exportPrjlstList(PageCondition condition);

	/**
	 * 借货申请——工程清单导出
	 * @param 
	 */
	List<List<Object>> loanPrjlstExport(PageCondition condition);
	
	/**
	 * 合同变更——工程清单导出
	 * @param 
	 */
	List<List<Object>> chgPrjlstExport(PageCondition condition);
	
	/**
	 * PI申请——工程清单导出
	 * @param 
	 */
	List<List<Object>> piPrjlstExport(PageCondition condition);
	
	/**
	 * 报价列表——工程清单导出
	 * @param 
	 */
	List<List<Object>> quotPrjlstExport(List<KstarPrjLst> prjlstList);
	
	IPage queryAtt(PageCondition condition);
	
	IPage queryMem(PageCondition condition);

	void saveMem(KstarMemInfo mem) throws AnneException;
	
	void updateMem(KstarMemInfo mem) throws AnneException;

	KstarMemInfo getKstarMemInfo(String memID) throws AnneException;

	void deleteMem(String memId) throws AnneException;

	void saveAtt(KstarAtt att) throws AnneException;

	void updateAtt(KstarAtt att) throws AnneException;

	KstarAtt getKstarAtt(String attID) throws AnneException;

	void deleteAtt(String attId) throws AnneException;
	
	String getContractNumber() throws AnneException;
		
	
	/**
	 * 
	 * getCustomInfoList:获取客户信息List. <br/> 
	 * TODO(提供公共的查询下拉选择框).<br/>
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<CustomInfo> getCustomInfoList(Condition condition)
			throws AnneException;

	/**
	 * 
	 * getCustomInfoList:获取客户信息List. <br/> 
	 * TODO(提供公共的查询下拉选择框).<br/>
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<BusinessOpportunity> getProjectInfoList(Condition condition)
			throws AnneException;

	/**
	 * 
	 * getCustomInfoList:获取客户信息List. <br/> 
	 * TODO(提供公共的查询下拉选择框).<br/>
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<Contract> getFramenoInfoList(Condition condition)
			throws AnneException;
	
	
	/**
	 * 下拉选择合同，过滤自己有权限的数据
	 * @param condition
	 * @return
	 * @throws AnneException
	 */
	List<Contract> getFramenoInfoListByUser(Condition condition) throws AnneException;
	
	
	/**
	 * 
	 * getCustomInfoList:组织信息List. <br/> 
	 * TODO(提供公共的查询下拉选择框).<br/>
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<LovMember> getOrgInfoList(Condition condition)
			throws AnneException;

	/**
	 * 
	 * getCustomInfoList:价格信息List. <br/> 
	 * TODO(提供公共的查询下拉选择框).<br/>
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<ProductPriceHead> getPriceTableInfoList(Condition condition)
			throws AnneException;
	
	IPage queryPrjevl(PageCondition condition);

	void savePrjevl(KstarPrjEvl prjevl, String typ) throws AnneException;

	void updatePrjEvl(KstarPrjEvl prjevl) throws AnneException;

	KstarPrjEvl getKstarPrjEvl(String prjevlID) throws AnneException;
	
	KstarPrjEvl getKstarPrjevlByProcessId(String contrId,String pi) throws AnneException;
	
	void deletePrjevl(String[] prjevlIds) throws AnneException;
	
	 List<KstarPrjEvl> queryContrPrjevlListByContrId(String contrId) throws AnneException;

//	List<HistoryActivityInstance> getContrFinReviewHisLst(String module, String contrId) throws AnneException;
	
	void startContractReviewProcess(UserObject user,String[] ids,String typ) throws AnneException;

	void completeContrReviewProcess(UserObject user,String contrId) throws AnneException;
	
	void startContractTrialProcess(UserObject user,String contrId, String typ) throws AnneException;

	void startContractFinalProcess(UserObject user,String contrId, String typ) throws AnneException;
	
	 void startContrStdPriceProcess(UserObject user,String contrId) throws AnneException;
	
	Contract reviseContract(UserObject user,String contrId) throws AnneException;

	void signUpContract(UserObject user,String contrId, String typ) throws AnneException;
	
	void updateContractTrialStatus(String id, String status) throws AnneException;
	
	void updateContractFinalStatus(String id, String status) throws AnneException;
	
	void updateContractStatus(String id, String status) throws AnneException;
	
	void updateContractProcessCloseDate(String id) throws AnneException ;
	
	void updateContrLoanCheckStatus(String id, String status) throws AnneException; 
	
	KstarPrjLst getKstarPrjLst(String qid, String lvId) throws AnneException;

	IPage queryPrjLst(PageCondition condition);

	/**
	 * 无合同核销-工程清单
	 * @param condition
	 * @return
	 */
	IPage queryEliminatePrjLst(PageCondition condition);
	
	/**
	 * 无合同核销-删除工程清单
	 * @param condition
	 * @return
	 */
	void deleteEliminatePrjLst(String id);
	
	void updatePrjLst( KstarPrjLst prjLst,String typ, String contrId) throws AnneException;

	void savePrjLst(KstarPrjLst prjLst,String cType) throws AnneException;

	String getLineNum(String contrId) throws AnneException;
	
	void deletePrjLst( String quotCode) throws AnneException;

	Double getAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;
	
	String getLovememroot(String qid) throws AnneException;

	Double getTotalamount(String qid) throws AnneException;

	String CheckttlUnt(String qid) throws AnneException;

	String addlovroot(String qid,String ctype, String groupId) throws AnneException;
	
	String Checklovroot(String qid) throws AnneException;

	String Checkprjevlstatus(String qid) throws AnneException;
	
	void updateAvgttl(LovMember lovMember,KstarPrjLst prjLst) throws AnneException;
	
	void updateAllAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void updateAllAvgttlByQcode(String quotcode) throws AnneException;
	
	IPage queryPginf(PageCondition condition);

	void savePgInf(KstarPgInf pginf,String cType) throws AnneException;

	void updatePgInf(KstarPgInf pginf) throws AnneException;

	KstarPgInf getKstarPgInf(String pginfID) throws AnneException;

	void deletePgInf(String pginfId) throws AnneException;

	KstarBaseInf getKstarBaseInf(String baseinfID, String typ) throws AnneException;

	void updateBaseInf(KstarBaseInf baseinf) throws AnneException;

	KstarIdu getKstarIdu(String iduID, String typ) throws AnneException;

	void updateIdu(KstarIdu idu) throws AnneException;

	void saveBaseinf(KstarBaseInf baseinf,String qid,String cType) throws AnneException;

	void saveIdu(KstarIdu idu, String qid ,String cType) throws AnneException;

	KstarSngBty getKstarSngBty(String sngbtyID, String typ) throws AnneException;

	void saveSngbty(KstarSngBty sngbty, String qid,String cType) throws AnneException;

	void updateSngbty(KstarSngBty sngbty) throws AnneException;

	KstarAftSale getKstarAftSale(String aftsaleID, String typ) throws AnneException;

	void saveAftsale(KstarAftSale aftsale, String qid,String cType) throws AnneException;

	void updateAftsale(KstarAftSale aftsale) throws AnneException;

	KstarIdm getKstarIdm(String idmID, String typ) throws AnneException;

	void updateIdm(KstarIdm idm) throws AnneException;

	void saveIdm(KstarIdm idm, String qid,String cType) throws AnneException;

	KstarSngUps getKstarSngUps(String sngupsID,String typ) throws AnneException;

	void saveSngUps(KstarSngUps sngups, String qid,String cType) throws AnneException;

	void updateSngups(KstarSngUps sngups) throws AnneException;

	KstarSngElec getKstarSngElec(String sngelecID, String typ) throws AnneException;

	void saveSngelec(KstarSngElec sngelec, String qid,String cType) throws AnneException;

	void updateSngelec(KstarSngElec sngelec) throws AnneException;

	KstarSngClr getKstarSngClr(String sngclrID, String typ) throws AnneException;

	void saveSngclr(KstarSngClr sngclr, String qid,String cType) throws AnneException;

	void updateSngclr(KstarSngClr sngclr) throws AnneException;

	KstarSngRck getKstarSngRck(String sngrckID, String typ) throws AnneException;

	void saveSngrck(KstarSngRck sngrck, String qid,String cType) throws AnneException;

	void updateSngRck(KstarSngRck sngrck) throws AnneException;

	KstarSngMnt getKstarSngMnt(String sngmntID, String typ) throws AnneException;

	void saveSngmnt(KstarSngMnt sngmnt, String qid,String cType) throws AnneException;

	void updateSngMnt(KstarSngMnt sngmnt) throws AnneException;

	List<KstarAtt> getAttList(Condition condition) throws AnneException;
	
	List<KstarMemInfo> getMemList(Condition condition) throws AnneException;
	
	List<ContrAddr> getAddrList(Condition condition) throws AnneException;
	
	ContrVeriDetail getContrVeriDtl(String id) throws AnneException;
	
	KstarPrjLst getKstarPrjLstById(String id) throws AnneException; 
	
	IPage queryVeriDtl(PageCondition condition) throws AnneException;
	
	void saveVeriDtlLst(String contrId,String oriPrjlstId,ContrVeriDetlLstVO loanVeriDtlList) throws AnneException, Exception ;
	
	void deleteVeriDtlLst(String contrVeriDetlId) throws AnneException ;
	
	void confirm(String id) throws AnneException;
	
	Contract copyContract(Contract contract, Contract old,UserObject user) throws AnneException;
	
	void copyLovMemberByMemo(String oldMemo,String newMemo) throws AnneException;
	
	void copyPrjlstById(String oId,String nId,String nTyp) throws AnneException;
	
	int buildRandom(int length) throws AnneException;
		
	void copyAttachmentById(String nId, String oId, String typ) throws AnneException;
	
	void copyMemById(String oldId, String newId, String typ) throws AnneException;
	
	void copyAddrById(String oId, String nId, String typ) throws AnneException;
	
	void copyPayPlanById(String oId,String nId) throws AnneException;
	
	void copyPrjdtl(String quotcode,String contractcode, String typ) throws AnneException ;
	
	 List<KstarPrjLst> getPrjlstList(Condition condition) throws AnneException ;
	 List<KstarPrjLst> getPrjlstListByContrId(String id) throws AnneException;
	 /**
	  * 
	  * updatePrjLstOrderNoByContractID:更新功能清单的订单编号. <br/> 
	  * 
	  * @author liming 
	  * @param contractId
	  * @param op
	  * @param orderCode
	  * @throws AnneException 
	  * @since JDK 1.7
	  */
//	void updatePrjLstOrderNoByContractID(String contractId, String op,
//			String orderCode) throws AnneException;

	void updatePrjLstOrderNoByContractID(String contractId, String lineNum, String op, String orderCode)
			throws AnneException;
	
	String getAppnameByType(String tp, String bizTp) throws AnneException ;
	
	String getTitleByAppName(String appName) throws AnneException ;
	
	 /**
	  * 根据合同ID更新回款信息 
	  * @param contrId			合同ID
	  * @param tolRecdAmt 		总已收金额
	  * @param notTolRecAmt 	总未收金额
	  */
	void updatePayAmtByContrID(String contrId,Double  tolRecdAmt,Double notTolRecAmt) throws AnneException;

	 /**
	  * 根据合同ID更新订单信息 
	  * @param contrId			合同ID
	  * @param orderNo 		ERP订单编号	
	  * @param orderedAmt 	已下单金额
	  */
	void updateOrderInfoByContrID(String contrId,String orderNo , Double orderedAmt) throws AnneException;

	 /**
	  * 根据合同ID更新订单信息 
	  * @param contrId			合同ID
	  * @param deliveredAmt 	已发货金额	
	  */
	void updateDeliverAmtByContrID(String contrId,Double deliveredAmt) throws AnneException;

	 /**
	  * 根据合同ID更新订单信息 
	  * @param contrId			合同ID
	  * @param invoicedAmt 		已开票金额	
	  */
	void updateInvoicedAmtByContrID(String contrId,Double invoicedAmt) throws AnneException;

	 /**
	  * 生成订单后根据合同ID更新合同状态   ---08	已签订商务已下单
	  * @param contrId			合同ID
	  * 
	  */
	void updateContrStaForOrderByContrID(String contrId) throws AnneException;
	
	String getContrBusinessTypeById(String contrId) throws AnneException;
	
	List<Contract> selectContract(PageCondition condition);
	
	Contract getContractByNo(String contrNo);
	
	String checkContrBusiEntityForOrder(String custId, String businessEntityId) throws AnneException;
	
	String checkGenOrdLinesByContract(String contrId) throws AnneException;
	
	void updatePrjlstTypeByContr(String buzId, String cType, String buzCode)  throws AnneException;
	
	Boolean checkPayPlanListNull(String buzId)  throws AnneException ;
	
	void discardContr(String contrId,String typ,UserObject  user) throws AnneException;
	/**
	 * 
	 * getPrjLstByContractID:根据合同ID和工程清单行号获取工程清单信息. <br/> 
	 * 
	 * @author liming 
	 * @param contractId 合同ID
	 * @param lineNum 行号
	 * @return 
	 * @since JDK 1.7
	 */
	KstarPrjLst getPrjLstByContractID(String contractId, String lineNum);
	
	void createPrjevlForContr(String contrId) throws AnneException;
	
	String getAllRelationCustByCustId(String custId) throws AnneException;
	
	Long getContractNumtByQutoNo(String qutoNo) throws AnneException;
	
	void startContrSumReviewFlow(String contrId, String typ) throws AnneException;
	
	/*
	 * 根据产品ID 更新 ， 合同、报价 工程清单行的 料号 和 产品描述 
	 * proId :　产品ＩＤ　
	 * 
	 */
	void updateContrPrjlstMaterCodeByPrdId(String proId) throws AnneException ;
	/**
	 * 
	 * getContractListForAutocomp:合同搜索录入框. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<Contract> getContractListForAutocomp(Condition condition)
			throws AnneException;
	
	/**
	 * 根据订单明细数量和合同明细数量判断合同是否已全部下单
	 * @param orderId
	 * @param contractId
	 * @throws AnneException
	 */
	void updateContractStatByAmt(String orderId,String contractId) throws AnneException;
	
	
	void saveLines(String listStr, String contrId,String typ) throws AnneException;

	/**
	 * 无合同核销——工程清单保存
	 * @param listStr
	 * @param contrId
	 * @param typ
	 * @throws AnneException
	 */
	void saveEliminateLines(String listStr, String contrId,String eliminateid,ContractEliminate contractEliminate) throws AnneException;
	
	/**
	 * 根据合同ID获取全部的审批历史明细
	 * @param contrId
	 * @return
	 */
	List<HistoryActivityInstance> getHistoryActivityInstanceAllByContrId(String contrId);
	
	/**
	 * 0单价价格平摊
	 * @param contrId
	 */
	void share0Price(String contrId);
	/**
	 * 
	 * getContractByNoForContrVer:获取最大版本的号的合同. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param contrNo
	 * @return 
	 * @since JDK 1.7
	 */
	Contract getContractByNoForContrVer(String contrNo);
	
	/**
	 * 
	 */
	String checkContrStat(String id,UserObject user);
	
	/**
	 * 验证核销数量
	 * @return
	 */
	String checkVeriedNum(String listStr, String contrId,String eliminateid)throws AnneException;
	
	void updateContrIsPassStatus(String id) throws AnneException;

	/**
	 * 判断工程清单行是否可以进行核销
	 * 新需求：待借货核销列表，过滤掉创建时间大于该合同创建时间的借货单
	 * @param prjlstId
	 * @return
	 */
    double canVerificationNum(String prjlstId);

	List<List<Object>> reportExportContrLists(PageCondition condition, UserObject user, String[] ids, String typ)
			throws AnneException;

    void updateHighRiskFlag(String id, String isHighRisk);
}
