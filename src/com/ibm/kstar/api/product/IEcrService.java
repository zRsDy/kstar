package com.ibm.kstar.api.product;

import java.util.List;
import java.util.Map;

import com.ibm.kstar.entity.product.EcrChangeLine;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.IEcrReqmEntity;
import com.ibm.kstar.entity.product.KstarEcrBean;

public interface IEcrService {

	IPage query(PageCondition condition,UserObject userObject);
	
	public KstarEcrBean get(String id);
	
	public KstarEcrBean queryEditEcrById(String id);
	
	public void save(KstarEcrBean ecr, UserObject user);
	
	public void delete(String id);
	
	public List<LovMember> queryEcrRefer(String ecrId);

	void startEcrrocess(UserObject userObject, String id);
	
	public void ecrInBound(String id, UserObject user);
	
	public boolean doMove2Int(String id, String userId) throws Exception;
	
	public boolean save(IEcrReqmEntity ecr);
	
	public void move2IntBack(String ecrCode, boolean isSuccess, String backReason);

    IPage queryProductLines(PageCondition condition, UserObject userObject);

	List<Map<String,Object>> getProductList(List<String> productIds);

    void update(KstarEcrBean ecr, UserObject userObject);

	void save(KstarEcrBean ecr, List<EcrChangeLine> changeLines, UserObject userObject);

	void update(KstarEcrBean ecr, List<EcrChangeLine> changeLines, UserObject userObject);
}
