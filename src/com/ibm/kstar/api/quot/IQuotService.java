/** 
 * Project Name:crm 
 * File Name:IQuotService.java 
 * Package Name:com.ibm.kstar.api.quot 
 * Date:Jan 4, 20175:26:20 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.api.quot;
 
 import java.util.ArrayList;
import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.model.HistoryActivityInstance;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;
import com.ibm.kstar.entity.quot.KstarAftSale;
import com.ibm.kstar.entity.quot.KstarAtt;
import com.ibm.kstar.entity.quot.KstarBaseInf;
import com.ibm.kstar.entity.quot.KstarBiddcevl;
import com.ibm.kstar.entity.quot.KstarCntr;
import com.ibm.kstar.entity.quot.KstarIdm;
import com.ibm.kstar.entity.quot.KstarIdu;
import com.ibm.kstar.entity.quot.KstarMemInfo;
import com.ibm.kstar.entity.quot.KstarPgInf;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.entity.quot.KstarSngBty;
import com.ibm.kstar.entity.quot.KstarSngClr;
import com.ibm.kstar.entity.quot.KstarSngElec;
import com.ibm.kstar.entity.quot.KstarSngMnt;
import com.ibm.kstar.entity.quot.KstarSngRck;
import com.ibm.kstar.entity.quot.KstarSngUps;
 
/** 
 * ClassName:IQuotService <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Jan 4, 2017 5:26:20 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public interface IQuotService {

	IPage query(PageCondition condition);

	void saveQuot(KstarQuot quot,UserObject current_user) throws AnneException;

	KstarQuot getKstarQuot(String quotID) throws AnneException;

	void updateQuot(KstarQuot quot) throws AnneException;

	void deleteQuot(String quotId) throws AnneException;

	IPage queryCnt(PageCondition condition);

	void saveCnt(KstarCntr cnt) throws AnneException;

	void updateCnt(KstarCntr cnt) throws AnneException;

	KstarCntr getKstarCntr(String cntID) throws AnneException;

	void deleteCnt(String cntId) throws AnneException;

	IPage queryMem(PageCondition condition);

	void saveMem(KstarMemInfo mem) throws AnneException;

	void updateMem(KstarMemInfo mem) throws AnneException;

	KstarMemInfo getKstarMemInfo(String memID) throws AnneException;

	void deleteMem(String memId) throws AnneException;

	void saveAtt(KstarAtt att) throws AnneException;

	void updateAtt(KstarAtt att) throws AnneException;

	KstarAtt getKstarAtt(String attID) throws AnneException;

	void deleteAtt(String attId) throws AnneException;

	IPage queryAtt(PageCondition condition);

	IPage queryPrjevl(PageCondition condition);

	void savePrjevl(KstarPrjEvl prjevl) throws AnneException;

	void updatePrjEvl(KstarPrjEvl prjevl) throws AnneException;

	KstarPrjEvl getKstarPrjEvl(String prjevlID) throws AnneException;

	void deletePrjevl(String prjevlId) throws AnneException;

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

	IPage queryBiddcevl(PageCondition condition);

	void saveBiddcevl(KstarBiddcevl biddcevl) throws AnneException;

	void updateBiddcevl(KstarBiddcevl biddcevl) throws AnneException;

	KstarBiddcevl getKstarBiddcevl(String biddcevlID) throws AnneException;

	void deleteBiddcevl(String biddcevlId) throws AnneException;

	IPage queryPrjLst(PageCondition condition);

	void updatePrjLst(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void savePrjLst(KstarPrjLst prjLst,String cType) throws AnneException;

	void deletePrjLst(String lvId, String quotCode) throws AnneException;

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

	KstarPrjLst getKstarPrjLst(String qid, String lvId,String ctype) throws AnneException;

	Double getAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	void updateAllAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	ArrayList<String> getevlTyp(String[] _ids);

	void startPreSaleProcess(UserObject user,String id);

	void startAftSaleProcess(UserObject user,String id);

	void updateAllAvgttlByQcode(String quotcode) throws AnneException;

	void startBusinessProcess(UserObject user,String id);

	void startDecisionProcess(UserObject user,String id);

	void startPriceProcess(UserObject user,String id);

	void endQuotPrjAdtProcess(String processId);

	void startBiddcAdtProcess(UserObject user,String qid);

	void startsubmitProcess(UserObject user,String qid);
	
	KstarPrjLst getPrjLst(String quotCode, String orgPrdNm) throws AnneException;

	void deleteKstarPgInf(String Ctype,String relPrd,String quotCode) throws AnneException;

	Long countKstarPgInf(String Ctype, String relPrd, String quotCode) throws AnneException;

	KstarQuot getQuotbyquotcode(String quotcode) throws AnneException;

	void copyPrjdtl(String quotcode,String contractcode,String ifcntr) throws AnneException;

	void copyLovMemberByMemo(String oldMemo, String newMemo);

	void reviseQuot(UserObject user, String quotId) throws AnneException;

	KstarQuot copyKstarQuot(KstarQuot newQuot, KstarQuot oldQuot, UserObject user);

	List<KstarAtt> getAttList(Condition condition) throws AnneException;

	List<KstarMemInfo> getMemList(Condition condition) throws AnneException;

	void copyAttByQuot(KstarQuot newQuot, KstarQuot oldQuot, UserObject user);

	void copyMemByQuot(KstarQuot newQuot, KstarQuot oldQuot, UserObject user);

	List<KstarPgInf> getPginfList(Condition condition) throws AnneException;

	void copyPginfByQuot(KstarQuot newQuot, KstarQuot oldQuot, UserObject user);

	List<HistoryActivityInstance> getContrFinReviewHisLst(String module, String quotId) throws AnneException;

	List<HistoryActivityInstance> getHistoryByBusinessKey(String module, String businessKey);

	KstarQuot getPrequotbyID(String id) throws AnneException;

	void startquotpriceProcess(UserObject user, String qid);

	void updateAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException;

	String getLovememroot(String qid) throws AnneException;

	String getTotalamount(String qid) throws AnneException;

	String CheckttlUnt(String qid) throws AnneException;

	String addlovroot(String qid,String ctype, String groupId) throws AnneException;

	String Checklovroot(String qid) throws AnneException;

	String Checkprjevlstatus(String qid) throws AnneException;

	void updateSprvmrk(UserObject currentUser,KstarQuot quot) throws AnneException;

	ArrayList<String> getPrjevlids(String qid);

	ArrayList<String> getevlTypbyids(ArrayList<String> ids);

	String CheckSprvmrkStatus(String qid) throws AnneException;

	String Checkprjevlststatus(String qid) throws AnneException;

	KstarBiddcevl getBiddcevl(String quotId, String cType) throws AnneException;

	List<HistoryActivityInstance> getBiddcevlReviewHisLst(String processid, String quotId) throws AnneException;

	void startSpProcess(UserObject user, String qid);

	String [] splitlovpath(String lovpath);

	void updateCntSprvmrk(UserObject currentUser,String cntId, String orgid) throws AnneException;

	String CheckQuotsubmitStatus(String qid) throws AnneException;
	
	IPage queryQuot(String qid);
	
	KstarBiddcevl getBiddcevl(String processid) throws AnneException;

	String checkApprovedType(String qid) throws AnneException;

	String checkHighLevel(String qid) throws AnneException;

	String checkcurrentLevel(String qid) throws AnneException;

	ArrayList<String> getevlIds(String[] ids);

	ArrayList<String> getevlTypbyArraylist(ArrayList<String> ids);

	List<KstarPrjLst> getPrjLstPrd(String quotCode) throws AnneException;

	void updatePrjLstwithlineNum(String qid);

	BusinessOpportunity getBizOppbynumber(String number) throws AnneException;

	Double saveWithDouble(Double number) throws AnneException;

	List<KstarBiddcevl> getBiddcevlList(String quotId, String cType) throws AnneException;

	List<KstarProductWorkFlow> getKstarProductWorkFlowList(String quotId, String processName) throws AnneException;

	BusinessOpportunity getBizOppbyId(String Id) throws AnneException;

	void startquottchProcess(UserObject user, String qid);

	void startquotmrProcess(UserObject user, String qid);

	void startquotbrProcess(UserObject user, String qid);

	String CheckContractStatus(String qid) throws AnneException;

	String getContractID(String qid) throws AnneException;

	void updateprjLstSprvmrk(UserObject currentUser, KstarPrjLst prjLst) throws AnneException;

	KstarPrjLst getKstarPrjLst(String qid, String lvId) throws AnneException;

	void updatePrjLst(KstarPrjLst prjLst) throws AnneException;

	void deletePrjLst(String id) throws AnneException;

	String getLineNum(String contrId) throws AnneException;

	void copyKstarPrjLst(String oldQuotId, String newQuotId);

	void copyKstarPrjLstforCont(String oldQuotId, String newQuotId);

	String checkKstarPrjLstwithprdSprc(String qid) throws AnneException;

	void updateprjLstSprvmrkstart(UserObject currentUser, KstarPrjLst prjLst) throws AnneException;

	List<KstarPrjLst> getKstarPrjLsts(String qid) throws AnneException;

	KstarPrjLst getKstarPrjLst(String prjlstID) throws AnneException;

	void saveLinesb(List<KstarPrjLst> lines,String quotID,String typ,UserObject userObject) throws AnneException;
	
}
  
