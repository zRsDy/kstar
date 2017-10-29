package com.ibm.kstar.api.product;

import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.KstarProductLine;

public interface IProLineService {
	
	IPage query(PageCondition condition) throws Exception;

	public KstarProductLine query(String id);
	
	public List<LovMember> prepareForkLov(KstarProduct kp);
	
	List<LovMember> selectProSeries(Condition condition);
	
	List<LovMember> selectProLines();
}
