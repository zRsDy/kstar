package com.ibm.kstar.api.product;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductAuTest;

public interface IAuTestService {

	IPage query(PageCondition condition) throws Exception ;
	
	public void save(KstarProductAuTest ka, UserObject user);
	
	public KstarProductAuTest queryAuTestById(String id);

	public void delete(String id);
	
	public void startAuTestApplyrocess(UserObject userObject, String id);

	void update(KstarProductAuTest kc, UserObject user);
}