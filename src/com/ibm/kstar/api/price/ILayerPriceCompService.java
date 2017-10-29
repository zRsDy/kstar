package com.ibm.kstar.api.price;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.PriceLayCompareHeader;

public interface ILayerPriceCompService {

	IPage query(PageCondition condition, UserObject user) throws Exception ;
	
	public void save(PriceLayCompareHeader doc, UserObject user);
	
	public PriceLayCompareHeader queryLpcById(String id);
	
	public PriceLayCompareHeader queryLpcHeadByOrg(String organization);
	
	public void delete(String id);

}
