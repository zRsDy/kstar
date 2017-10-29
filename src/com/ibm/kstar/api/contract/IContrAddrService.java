package com.ibm.kstar.api.contract;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;


import com.ibm.kstar.entity.contract.ContrAddr;


/**
 * 产品服务类接口
 * @author wansheng
 *
 */
public interface IContrAddrService {
	
	IPage query(PageCondition condition);

	void save(ContrAddr contrAddr) throws AnneException;

	ContrAddr get(String id) throws AnneException;

	void update(ContrAddr contrAddr) throws AnneException;

	void delete(String id) throws AnneException;
	
}
