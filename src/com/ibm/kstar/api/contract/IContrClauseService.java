package com.ibm.kstar.api.contract;



import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.contract.ContrClause;

/**
 * 产品服务类接口
 * @author wansheng
 *
 */
public interface IContrClauseService {
	
	IPage query(PageCondition condition);

	void save(ContrClause contrClause) throws AnneException;

	ContrClause get(String id) throws AnneException;

	void update(ContrClause contrClause) throws AnneException;

	void delete(String id) throws AnneException;
	
}
