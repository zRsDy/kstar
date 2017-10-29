package com.ibm.kstar.api.price;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.ProductPriceDiscountVO;
import com.ibm.kstar.entity.product.ProductPriceLine;

public interface IPriceLineService {

	IPage query(PageCondition condition) throws Exception ;
	
	public void save(ProductPriceLine doc, UserObject user);
	
	public ProductPriceLine queryLpcById(String id);
	
	List<ProductPriceLine> queryPriceLines(String priceHeadId, String productSortId, String productType);
	
	public void delete(String id);
	
	public void importPriceLine(List<List<Object>> data, UserObject user);
	
	public List<List<Object>> exportPriceLine(String headId);
	
	public List<List<Object>> exportPriceLineTemplet(String headId);
	
	void saveProductPriceLines(ProductPriceDiscountVO discountVO);
}
