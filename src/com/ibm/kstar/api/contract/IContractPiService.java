package com.ibm.kstar.api.contract;


import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.*;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.util.List;

//import com.ibm.kstar.entity.quot.KstarBiddcevl;


/**
 * 产品服务类接口
 * @author wansheng
 *
 */
public interface IContractPiService {
	
	
	IPage query(PageCondition condition);

	void save(Contract contract,UserObject userObject) throws AnneException;

	Contract get(String id) throws AnneException;

	void update(Contract contract) throws AnneException;

	void delete(String id) throws AnneException;
	
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
	
	String getContractPiNumber(String dep) throws AnneException;
		
	
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

	void savePrjevl(KstarPrjEvl prjevl) throws AnneException;

	void updatePrjEvl(KstarPrjEvl prjevl) throws AnneException;

	KstarPrjEvl getKstarPrjEvl(String prjevlID) throws AnneException;

	void deletePrjevl(String prjevlId) throws AnneException;

	void startContractTrialProcess(UserObject user,String contrId,String typ) throws AnneException;
	
	void reviseContract(UserObject user,String contrId) throws AnneException;

	void signUpContract(UserObject user,String contrId) throws AnneException;
	
	KstarPrjLst getKstarPrjLst(String qid, String lvId) throws AnneException;

	IPage queryPrjLst(PageCondition condition);
	
	void updatePrjLst(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void savePrjLst(KstarPrjLst prjLst,String cType) throws AnneException;

	void deletePrjLst(String lvId, String quotCode) throws AnneException;

	Double getAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void updateAllAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void updateAllAvgttlByQcode(String quotcode) throws AnneException;
	
	IPage queryPginf(PageCondition condition);

	void savePgInf(KstarPgInf pginf,String cType) throws AnneException;

	void updatePgInf(KstarPgInf pginf) throws AnneException;

	KstarPgInf getKstarPgInf(String pginfID) throws AnneException;

	void deletePgInf(String pginfId) throws AnneException;

	KstarBaseInf getKstarBaseInf(String baseinfID,String cType) throws AnneException;

	void updateBaseInf(KstarBaseInf baseinf,String cType) throws AnneException;

	KstarIdu getKstarIdu(String iduID,String cType) throws AnneException;

	void updateIdu(KstarIdu idu,String cType) throws AnneException;

	void saveBaseinf(KstarBaseInf baseinf,String qid,String cType) throws AnneException;

	void saveIdu(KstarIdu idu, String qid ,String cType) throws AnneException;

	KstarSngBty getKstarSngBty(String sngbtyID,String cType) throws AnneException;

	void saveSngbty(KstarSngBty sngbty, String qid,String cType) throws AnneException;

	void updateSngbty(KstarSngBty sngbty,String cType) throws AnneException;

	KstarAftSale getKstarAftSale(String aftsaleID,String cType) throws AnneException;

	void saveAftsale(KstarAftSale aftsale, String qid,String cType) throws AnneException;

	void updateAftsale(KstarAftSale aftsale,String cType) throws AnneException;

	KstarIdm getKstarIdm(String idmID,String cType) throws AnneException;

	void updateIdm(KstarIdm idm,String cType) throws AnneException;

	void saveIdm(KstarIdm idm, String qid,String cType) throws AnneException;

	KstarSngUps getKstarSngUps(String sngupsID,String cType) throws AnneException;

	void saveSngUps(KstarSngUps sngups, String qid,String cType) throws AnneException;

	void updateSngups(KstarSngUps sngups,String cType) throws AnneException;

	KstarSngElec getKstarSngElec(String sngelecID,String cType) throws AnneException;

	void saveSngelec(KstarSngElec sngelec, String qid,String cType) throws AnneException;

	void updateSngelec(KstarSngElec sngelec,String cType) throws AnneException;

	KstarSngClr getKstarSngClr(String sngclrID,String cType) throws AnneException;

	void saveSngclr(KstarSngClr sngclr, String qid,String cType) throws AnneException;

	void updateSngclr(KstarSngClr sngclr,String cType) throws AnneException;

	KstarSngRck getKstarSngRck(String sngrckID,String cType) throws AnneException;

	void saveSngrck(KstarSngRck sngrck, String qid,String cType) throws AnneException;

	void updateSngRck(KstarSngRck sngrck,String cType) throws AnneException;

	KstarSngMnt getKstarSngMnt(String sngmntID,String cType) throws AnneException;

	void saveSngmnt(KstarSngMnt sngmnt, String qid,String cType) throws AnneException;

	void updateSngMnt(KstarSngMnt sngmnt,String cType) throws AnneException;

	List<KstarAtt> getAttList(Condition condition) throws AnneException;
	
	List<KstarMemInfo> getMemList(Condition condition) throws AnneException;
	
	List<ContrAddr> getAddrList(Condition condition) throws AnneException;
	
	List<List<Object>> exportContractsHead(PageCondition condition,String typ,String[] ids);

    void updateHighRiskFlag(String id, String isHighRisk);
}
