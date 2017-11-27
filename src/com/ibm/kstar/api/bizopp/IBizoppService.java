 package com.ibm.kstar.api.bizopp;

 import com.ibm.kstar.api.system.permission.UserObject;
 import com.ibm.kstar.entity.bizopp.*;
 import com.ibm.kstar.entity.contract.Contract;
 import com.ibm.kstar.entity.custom.CustomInfo;
 import org.xsnake.web.action.Condition;
 import org.xsnake.web.action.PageCondition;
 import org.xsnake.web.exception.AnneException;
 import org.xsnake.web.page.IPage;

 import java.util.List;
 import java.util.Map;


 /**
 * ClassName:IBizoppService <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Jan 4, 2017 9:56:33 PM <br/> 
 * @author   Wutw 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public interface IBizoppService {
	
	IPage query(PageCondition condition, UserObject user);
	
	IPage query(PageCondition condition);
	
	/**
	 * 订单选择商机报备
	 * @param condition
	 * @param user
	 * @return
	 */
	IPage queryBo(PageCondition condition, UserObject user);
	
	IPage queryProPrice(PageCondition condition);
	
	IPage queryWeekly(PageCondition condition);
	
	IPage queryWeeklyReport(PageCondition condition);
	
	IPage queryConflictBizOpp(PageCondition condition, String bizId,String industry,String industrySub);
	
	/**
	 * 查询PI合同信息
	 * 
	 * @param condition
	 * @return
	 */
	IPage queryPi(PageCondition condition, String weeklyId);
	
	/**
	 * 查询产品详情
	 */
	IPage queryProductDetail(PageCondition condition);
	
	/**
	 * 查询商机基本信息中组织关系
	 */
	IPage queryBizOppOrg(PageCondition condition);
	
	/**
	 * 
	 * queryDecChainContact:查询决策链联系人. <br/> 
	 * 
	 * @author gaoyuliang 
	 * @param condition 
	 * @throws  
	 * @since JDK 1.7
	 */
	IPage queryDecChainContact(PageCondition condition);
	
	/**
	 * 查询商机信息中的竟争关系
	 * 
	 * @param id
	 * @return
	 * @throws AnneException
	 */
	IPage queryBizCompetitor(PageCondition condition);
	
    /**
     * 查询商机售前技术支持
     * 
     */
	IPage querySupportApply(PageCondition condition);
	
	/**
	 * 查询样机申请
	 * 
	 */
	IPage queryPrototypeApplyReturn(PageCondition condition);
	
	/**
	 * 查询投标授权
	 * 
	 */
	IPage queryBid(PageCondition condition);
	
	/**
	 * 查询商机中的产品信息
	 * @param condition
	 * @return
	 */
	IPage queryBizOppProductSelectList(PageCondition condition,UserObject userObject);
	
	/**
	 * 查询 特价明细
	 */
	IPage querySpePrice(PageCondition condition);
		
	/**
	 * 得到商机支持编号
	 * @return
	 */
	String getBizOppSupportNumber();
	
	/**
	 * 得到样机申请编号
	 * 
	 */
	String getBizOppProtoNumber();
	
	/**
	 * 得到投标授权编号
	 * 
	 */
	String getSequenceCode(String functionName);
	
	/**
	 * 得到商机支持编号
	 * @return
	 */
	String getBizOppNumber();
	
	/**
	 * 得到商机冲突编号
	 * @return
	 */
	String getBizOppConflictNumber();
	
	void saveProductDetail(ProductDetail productDetail);

	void save(BusinessOpportunity bizopp,UserObject userObject);
	
	void saveWeekly(InternationWeekly internationWeekly);
	
	/**
	 * 保存商机组织关系
	 * @param productDetail
	 */
	void saveBizOrg(BizOrg bizOrg);
	
	/**
	 * 保存商机信息决策链中联系人
	 * @param id
	 * @return
	 */
	void saveBizContact(BizContact bizContact);
	
	/**
	 * 保存商机信息中的竟争关系
	 * 
	 * @param id
	 * @return
	 */
	void saveBizCompetitor(BizCompetitor bizCompetitor);
	
	/**
	 * 保存商机中的售前支持
	 * 
	 * @param id
	 * @return
	 */
	void saveSupportApply(SupportApply supportApply,UserObject userObject);
	
	BusinessOpportunity getBizOppEntity(String id);
	
	Map<String, Object> getBizOppEntityByNumber(String number);
	
	/**
	 * 得到国际周报entity
	 * @param bizopp
	 */
	InternationWeekly getWeeklyEntity(String id);
	
	/**
	 * 得到决策链中的联系人
	 * 
	 * @param bizopp
	 */
	BizContact getBizContactEntity(String id);
	
	/**
	 * 得到商机信息中的竟争关系
	 * 
	 * @param bizopp
	 */
	BizCompetitor getBizCompetitorEntity(String id);
	
	void update(BusinessOpportunity bizopp,UserObject userObject);
	
	void updateProductDetail(ProductDetail productDetail);

	void delete(String id);
	
	/**
	 * 更新国际周报题头
	 * 
	 * @param id
	 * @return
	 */
    void updateWeekly(InternationWeekly internationWeekly);
	
	/**
	 * updateOrderHeader:更新国际周报进展汇报. <br/> 
	 * 
	 * @author gaoyuliang 
	 * @param internationWeeklyReport 
	 * @throws  
	 * @since JDK 1.7
	 */
	void updateWeeklyReport(InternationWeeklyReport internationWeeklyReport);
	
	/**
	 * updateWeeklyReportLine:更新国际周报进展汇报当前行. <br/> 
	 * 
	 * @author gaoyuliang 
	 * @param internationWeeklyReport 
	 * @throws  
	 * @since JDK 1.7
	 */
	void updateWeeklyReportLine(InternationWeeklyReport internationWeeklyReport);
	
	/**
	 * 更新商机信息中组织关系
	 * 
	 * @param id
	 */
	void updateBizOrg(BizOrg bizOrg);
	
	/**
	 * 更新商机信息中决策链联系人
	 * 
	 * @param id
	 */
	void updateBizContact(BizContact bizContact);
	
	/**
	 * 更新商机信息中的竟争关系
	 * 
	 * @param id
	 */
	void updateBizCompetitor(BizCompetitor bizCompetitor);
	
	/**
	 * 更新售前支持
	 * 
	 * @param id
	 */
    void updateSupportApply(SupportApply supportApply);
    
	void deleteWeeklyReport(String id);
	
	/**
	 * 删除商机对应的产品信息
	 * 
	 */
	void deleteProduceDetail(String id);	
	
	/**
	 * 删除国际周报
	 * 
	 */
	void deleteWeekly(String id);
	
	/**
	 * 删除商机信息中组织关系
	 * 
	 * @param id
	 */
	void deleteBizOrg(String id);
	
	/**
	 * 删除商机信息决策链中的联系人
	 * 
	 */
	void deleteBizContact(String id);
	
	/**
	 * 删除商机信息中的竟争关系
	 * 
	 * @param id
	 * @return
	 * @throws AnneException
	 */
	void deleteBizCompetitor(String id);
	
	/**
	 * 删除售前技术支持信息
	 * 
	 * @param id
	 * @return
	 * @throws AnneException
	 */
	void deleteSupportApply(String id);
	
	//查询国际周报进展
	InternationWeeklyReport getWeeklyReport(String id) throws AnneException;
	//保存国际周报进展
	void savetWeeklyReport(InternationWeeklyReport customEventInfo) throws AnneException;
	
	ProductDetail getProducrDetailEntity(String id);
	
	/**
	 * 查询商机组织关系
	 * 
	 * @param id
	 * @return
	 */
	BizOrg getBizOrgEntity(String id);

	/**
	 * 查询商机信息中售前支持
	 * 
	 */
	SupportApply getSupportApplyEntity(String id);
	
	/**
	 * 商机立项审批生成报价单
	 * 
	 */
	void generateQuot(String bizOppId, String projectType, String isBidProject,String quotName,String isProReview,UserObject userObjece);
	
	/**
	 * 商机立项审批生成框架合同 
	 * 
	 */
	void generateFrameContract(Contract contract, String bizOppId,String contrName);
	
	void updateProjectType(String projectType, String bizOppId,String isBidProject,String quotName,String isProReview,String contrName);

	 void startProcess(UserObject user, String key,String bizOppName,UserObject userObject,String is_10);

	 void submitWeeklyData(String id);

	 String getSpecialPriceApplyNumber();

     void startPrepareProcess(String id, String number, UserObject userObject);

	 void appealSubmit(String id);

	 void confirmConf(String id);

	 Double getTotalAmount(String id);

	 void startApprovedProcess(BusinessOpportunity businessOpportunity,UserObject userObject,String remark);

	 List<BizOppChange> getBizOppChange(String id,String status);

	 void startChangeProcess(String id, String s, UserObject userObject);

	 void startProjectInitProcess(String bizOppId, String quotName,UserObject userObject);

	 void saveChange(BizOppChange boc,String number,UserObject userObject);

     void save(Integrator integrator);

	 Integrator getBizOppIntegrator(String id);

	 void update(Integrator integrator);

	 void deleteIntegrator(String id);

	 IPage queryBizOppIntegrator(PageCondition condition);

	 IPage queryBizOppIntegratorChange(PageCondition condition);
	 
	 void changeBizOpp(String businessKey);

	 String getProductSeries(String productId);

     void save(SupportFeedBack sfb);

	 IPage querySupportFeedBack(PageCondition condition);

	 SupportFeedBack getSupportFeedBack(String id);

	 List<SupportFeedBack> getSupportFeedBackBySid(String supportId);

	 void update(SupportFeedBack supportFeedBack);

     void deleteSfb(String id);

     IPage findBizs(PageCondition condition, String bizIds, String productId);
     
     List<Map<String, Object>> selectBizOpp(Condition condition, UserObject user);

     List<BusinessOpportunity> getBizOppSelectAuth(PageCondition condition,String clientId,String userId, String string);

	 BizOppChange getBizOppChangeById(String id);
	 
	IPage queryBidAuthUnit(PageCondition condition);

	void saveBidAuthUnit(BidAuthUnit bidAuthUnit, UserObject userObject) throws AnneException;

	BidAuthUnit getBidAuthUnit(String id) throws AnneException;

	void updateBidAuthUnit(BidAuthUnit bidAuthUnit, UserObject userObject) throws AnneException;

	void deleteBidAuthUnit(String id) throws AnneException;

     CustomInfo findCustomInfoByName(String customerName);

     Double getOrderQtyByLineId(String lineId);

	 List<ProductDetail> getProducrDetailByBizId(String bizOppId);

	 BusinessOpportunity getBusinessOpportunityByNumber(String number);

     void startBtnxProcess(BusinessOpportunity businessOpportunity, UserObject userObject,String remark);

     void publishMail(List<BusinessOpportunity> bizIds);

	void autoCheckReport();

	Bid getBidEntity(String id);

	 void rejected(BusinessOpportunity businessOpportunity ,String remark);
	 
	 BusinessOpportunity get(String id) throws AnneException;
	 
	 List<List<Object>> exportRebateLineFormLists(String[] ids)throws AnneException;
	 
	 List<List<Object>> export(String[] ids)throws AnneException;
	 
	 List<List<Object>> exportData(String[] ids)throws AnneException;
	 
	 void sendEmail(String id);
	 
	 //报价申请——判断商机数量
	 String checkBiz(String rebateNo, List<RebateLine> rebateLineList, String type, String changeFlag);

	 /**
      * 根据商机获取商机列表中各个产品型号的可转特价数量
      * @param bizId
      * @return
      */
	 Map<String, Integer> getBizProductSurplusSum(String bizId);

	 /**
	  * 查询产品型号可以关联的特价单
	  * 特价单中存在改产品型号的可转特价数量
	  * @param condition
	  * @param userObject
      * @return
	  */
	 List queryUsableBizsWithProduct(PageCondition condition, UserObject userObject);

	 List<Integrator> getBizOppIntegratorList(String id);
	 
	 BusinessOpportunity getBusinessOpportunityByNumberAndOrg(String number,String org);

	void saveBiddingFeedBack(BiddingFeedBack entity, UserObject userObject);

	BiddingFeedBack getBidFeedBack(String bizOppId);

	void startFeedBackProcess(String id, UserObject userObject);

	BiddingFeedBack getBidFeedBackById(String businessKey);

     void updateForConflictAppeal(BusinessOpportunity opportunity);
 }
  
