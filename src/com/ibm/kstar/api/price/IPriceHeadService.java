package com.ibm.kstar.api.price;

import java.util.List;
import java.util.Map;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.ProductPriceDiscount;
import com.ibm.kstar.entity.product.ProductPriceHead;

public interface IPriceHeadService {

	IPage query(PageCondition condition,UserObject user) throws Exception ;
	
	public void save(ProductPriceHead doc,UserObject user);
	
	public ProductPriceHead queryLpcById(String id);
	
	public void delete(String id);
	
	List<Map<String,Object>> queryPrice(String customId, String orgId, String searchKey);

	/**
	 * 获取默认的价格表
	 * @param orgId
	 * @return
	 */
	ProductPriceHead getDefaultPriceHead(String orgId);
	ProductPriceHead getCustomerPriceHead(String clientId);

    Double getCustomerProductPrice(String belongOperator, String productId);
    
    /**
     * 根据价格表头ID获取批发价格信息
     * @param headId
     * @return
     */
    List<ProductPriceDiscount> getProductPriceDiscount(String headId);
    
}
