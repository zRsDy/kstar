package com.ibm.kstar.api.order;



import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.IntProdSerie;

public interface IIntProdSerieService {
	void saveIntProdSerie(IntProdSerie intProdSerie) throws AnneException;

	void updateIntProdSerie(IntProdSerie intProdSerie) throws AnneException;

	IPage queryIntProdSeries(PageCondition condition,UserObject userObject) throws AnneException;

	void deleteIntProdSerie(String intProdSerieId) throws AnneException;

	IntProdSerie getIntProdSerie(String intProdSerieId) throws AnneException;
	
	List<List<Object>> exportSelectedContrLists(String[] ids) throws AnneException;
}