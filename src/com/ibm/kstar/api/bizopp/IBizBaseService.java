 package com.ibm.kstar.api.bizopp;

 import com.ibm.kstar.action.ProcessForm;
 import com.ibm.kstar.api.system.permission.UserObject;
 import com.ibm.kstar.entity.bizopp.*;
 import org.xsnake.web.action.PageCondition;
 import org.xsnake.web.page.IPage;
 import org.xsnake.web.page.PageImpl;

 import java.util.List;


 /** 
 * ClassName:IBizBaseService <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Jan 6, 2017 9:27:55 AM <br/> 
 * @author   Wutw 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 public interface IBizBaseService {

     <T> void save(T t);

     <T> void update(T t);

     IPage query(PageCondition condition, Class t) throws Exception;
     
     PageImpl queryByBizName(PageCondition condition, Class t,String bizName,String bizId) throws Exception;

     IPage queryPrototype(PageCondition condition, String systemPid) throws Exception;

     <T> T getEntity(String id, T t);

     void delete(String id, Class t);

     void startApplyProcess(UserObject userObject, String id, String applyNumber);

     void startApplyProcessRebate(UserObject userObject, String id, String applyNumber);

     void startAuthProcess(UserObject userObject, String id, String bidNumber);
     
     void startNewAuthProcess(UserObject userObject, String id, String bidNumber);

     void startModelProcess(UserObject userObject, String id, String orderNumber);

     void saveSpecialPrice(SpecialPrice entity, UserObject userObject);

     void savePrototypeApplyReturn(PrototypeApplyReturn entity, UserObject userObject);

     void saveBid(Bid entity, UserObject userObject);

     void changeStatus(String id, String status);

     IPage querySpecialPriceLine(PageCondition condition, String spId);

     SpecialPriceLine getSpecialPriceLine(String id);

     void editSpecialPriceLine(SpecialPriceLine spl);

     void saveSpecialPriceLine(SpecialPriceLine specialPriceLine);

     void updateSpecialPriceLine(SpecialPriceLine line);

     Rebate getRebate(String id);

     void saveRebate(Rebate entity,UserObject userObject);

     void updateRebate(Rebate entity, ProcessForm form, UserObject userObject,String newProcessType,boolean newProcessTypeUpdateFlag);

     List<RebateLine> getRebateLine(String headerId, String lineId, String productId);
     RebateLine getRebateLine(String lineId);
     
     Double getRebateLineApplyQtyDif(String isBizId);
     
     
     /**
      * 根据特价ID获取最近的特价变更信息
      * @param rebateId
      * @return
      */
     RebateChange getRebateChangeByRebateId(String rebateId);
     
     void updateRebate(RebateChange entity,ProcessForm form,UserObject userObject,String saveOrUpdate,boolean newProcessTypeUpdateFlag);
     
     RebateChange getRebateChangeById(String id);
     
     /**
      * 特价变更通过后更新特价数据
      * @param id
      */
     void updatePriceChangeFlowCallBack(String id);
     
     List<RebateChange> getRebateChanges(String rebateId);

     /**
      * 特价申请审核通过后，备份原单据数据为变更原始数据
      */
	void saveCopyRebatChange(Rebate entity);

	PageImpl queryRebateChangeByBizName(PageCondition condition, Class t, String bizName, String bizId)
			throws Exception;

	void deleteRebateChange(String id, UserObject userObject);

	void destoryRebateChange(String entityName,String businessId,
            String processStatusColumn,Object processStatusValue);

     Rebate getRebateByNo(String rebateNo);
 }
  
