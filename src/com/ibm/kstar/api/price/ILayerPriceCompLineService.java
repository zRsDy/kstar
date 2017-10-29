package com.ibm.kstar.api.price;

import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.PriceLayCompareLine;

public interface ILayerPriceCompLineService {

	IPage query(PageCondition condition) throws Exception ;
	
	public void save(PriceLayCompareLine doc, UserObject user);
	
	public PriceLayCompareLine queryLpcById(String id);
	
	public List<PriceLayCompareLine> queryLpcByHeadId(String headerId);
	
	public void delete(String id);
	
	List<LovMember> selectApproveLayLine(Condition condition);
}
