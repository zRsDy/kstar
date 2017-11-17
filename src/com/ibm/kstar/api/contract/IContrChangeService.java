package com.ibm.kstar.api.contract;


import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;

/**
 * 产品服务类接口
 * @author wansheng
 *
 */
public interface IContrChangeService {
	
	IPage query(PageCondition condition);

	void save(ContrChange contrChange, UserObject userObject) throws AnneException;

	ContrChange get(String id) throws AnneException;

	void update(ContrChange contrChange) throws AnneException;

	void delete(String id) throws AnneException;

	String getContracChangetNumber() throws AnneException;	

	void signUpContract(UserObject user,String changeId) throws AnneException;
	
	void startContrChgTrialProcess(UserObject user,String contrId,String typ) throws AnneException;

	IPage queryPrjLst(PageCondition condition);

	void updatePrjLst( KstarPrjLst prjLst) throws AnneException;

	void savePrjLst(KstarPrjLst prjLst,String cType) throws AnneException;

	void deletePrjLst(String quotCode) throws AnneException;	

	void updateAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void updateAllAvgttlByQcode(String quotcode) throws AnneException;
	
	KstarPrjLst getKstarPrjLst(String qid, String lvId,String ctype) throws AnneException;
	
	KstarPrjLst getKstarPrjLst(String qid, String lvId) throws AnneException ;
	
	KstarPrjLst getKstarPrjLstById(String id) throws AnneException ;

	Double getTotalamount(String qid) throws AnneException;
	
	Double getNewTotalamount(String qid) throws AnneException ;

	KstarQuot getKstarQuot(String quotID) throws AnneException;
	
	void updateQuot(KstarQuot quot) throws AnneException;

	String addlovroot(String qid,String ctype, String groupId) throws AnneException;
	
	String Checklovroot(String qid) throws AnneException;
	
	void updateContrChgTrialStatus(String id, String status) throws AnneException;
	
	void updateContrChgReviewStatus(String id, String status) throws AnneException;
	 
	void updateContrChgStatus(String id, String status) throws AnneException;
	
	List<ContrChange> queryChangeListByContrId(String contrId) throws AnneException;
	
	void cancelChange(String contrId,String typ,UserObject  user) throws AnneException;
	
	void validataNewAmt(ContrChange contrChange) throws AnneException;

	/**
	 * 保存合同清单，锁定订单行
	 * @param listStr
	 * @param contrId
	 * @param typ
	 * @param userObject
	 * @throws AnneException
	 */
	void saveLines(String listStr, String contrId,String typ,UserObject userObject) throws AnneException;
	
	List<List<Object>> exportContractsHead(PageCondition condition,String typ,String[] ids);
	
}
