package com.ibm.kstar.api.product;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProSub;

public interface IProSubService {

	IPage query(PageCondition condition) throws Exception ;
	
	public void save(KstarProSub doc, UserObject user);
	
	public void update(KstarProSub kc, UserObject user);
	
	List<KstarProSub>  querySubList(String productID);
	
	public KstarProSub queryById(String id);
	
	public IPage queryDup(PageCondition condition)  throws Exception;
	public void delete(String id);
}
