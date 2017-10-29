package com.ibm.kstar.api.product;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductDoc;

public interface IDocService {

	IPage query(PageCondition condition) throws Exception ;
	
	public void save(KstarProductDoc doc, UserObject user);
	
	public KstarProductDoc queryDocByID(String docId);
	
	public void checkDocEditByID(String docId);
	
	public void delete(String id);
	
	public void startDocApplyrocess(UserObject user, String id);

	void update(KstarProductDoc doc, UserObject user);

}
