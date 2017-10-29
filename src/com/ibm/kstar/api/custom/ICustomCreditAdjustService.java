package com.ibm.kstar.api.custom;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomCreditAdjustment;

public interface ICustomCreditAdjustService {
	//信用调整
	IPage queryAdjustment(PageCondition condition);
	
	void saveAdjustmentInfo(CustomCreditAdjustment customCreditAdjustment, UserObject uo) throws AnneException;
	
	CustomCreditAdjustment getAdjustmentInfo(String id) throws AnneException;

	void updateAdjustmentInfo(CustomCreditAdjustment customCreditAdjustment, String completeFlg) throws AnneException;

	void deleteAdjustmentInfo(String id) throws AnneException;
	
	

}
