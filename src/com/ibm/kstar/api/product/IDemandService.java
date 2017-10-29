package com.ibm.kstar.api.product;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.INonStadProdDemandEntity;
import com.ibm.kstar.entity.product.KstarProductDemand;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import java.util.List;

public interface IDemandService {

	IPage query(PageCondition condition, UserObject user) throws Exception ;
	
	public void save(KstarProductDemand doc, UserObject user,String seriesDemand);
	
	public void submitDemand(String id, UserObject user);
	
	public boolean canApplyDemand(String productId);
	
	public void demandInBound(String id, UserObject user, Boolean wasOne2Many);
	
	public KstarProductDemand queryDemandById(String id,String productId);
	
	public KstarProductDemand queryDemandByCode(String code);
	
	public String findOnerSaleCenter(String orgId);
	
	public void delete(String id);
	
//	public boolean doMove2Int(String id, String userId) throws Exception;
	
	boolean doMove2Int(String id, String userId, Boolean wasOne2Many) throws Exception ;
	
	boolean doMove2IntWithoutSync(String id, String userId, Boolean wasOne2Many) throws Exception;
	
	public INonStadProdDemandEntity save(List<INonStadProdDemandEntity> nspds);
	
	public void move2IntBack(String demandCode, boolean isSuccess, String backReason);
	
	public void demandSelectProducts(String[] ids, String demandId, UserObject user);
	
	public void deleteDemandProducts(String[] ids);
	
	public void submitDemandNumber(String relationId, String column,String value, UserObject user);


	/**
	 * 根据产品Id获取产品需求申请单
	 * @param id
	 * @return
	 */
	List<KstarProductDemand> getProductDemandForProductId(String id);
}