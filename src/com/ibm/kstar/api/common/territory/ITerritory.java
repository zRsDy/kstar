package com.ibm.kstar.api.common.territory;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.common.territory.TerritoryConfig;
import com.ibm.kstar.entity.common.territory.TerritoryInfo;

public interface ITerritory {
	IPage queryTerritoryInfo(PageCondition condition);

	void saveTerritoryInfo(TerritoryInfo territoryInfo, UserObject userObject) throws AnneException;

	TerritoryInfo getTerritoryInfo(String id) throws AnneException;

	void updateTerritoryInfo(TerritoryInfo territoryInfo, UserObject userObject) throws AnneException;

	void deleteTerritoryInfo(String id) throws AnneException;
	
	IPage queryTerritoryConfig(PageCondition condition);

	void saveTerritoryConfig(TerritoryConfig territoryConfig, UserObject userObject) throws AnneException;

	TerritoryConfig getTerritoryConfig(String id) throws AnneException;

	void updateTerritoryConfig(TerritoryConfig territoryConfig, UserObject userObject) throws AnneException;

	void deleteTerritoryConfig(String id) throws AnneException;
}
