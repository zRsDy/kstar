package com.ibm.kstar.api.channel;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateProduct;

public interface IRebateProductService {
	
	IPage queryProducts(PageCondition condition);
	
	RebateProduct queryProduct(String id);

	void addOrEdit(RebateProduct product, UserObject user);

	void delete(String id);
	
	List<RebateProduct> getProducts();
}
