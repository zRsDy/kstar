package com.ibm.kstar.api.product;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductClient;

public interface IClientService {

	IPage query(PageCondition condition) throws Exception ;
	
	public KstarProductClient queryClientById(String id);
	
	public void save(KstarProductClient kc, UserObject user);
	
	public void delete(String id);
}
