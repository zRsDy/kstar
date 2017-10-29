package com.ibm.kstar.api.order;



import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.AccountsMaster;

public interface IAccountsService {

	IPage queryAccountsMasters(PageCondition condition) throws AnneException;

	void deleteAccountsMaster(String accountsMasterId) throws AnneException;

	AccountsMaster getAccountsMaster(String accountsMasterId)throws AnneException;
	
	IPage queryAccountsDetail(PageCondition condition) throws AnneException;

	void saveAccounts(AccountsMaster accountsMaster,UserObject userObject) throws Exception;

	/**
	 * 
	 * updateStatus:更新账期申请状态. <br/> 
	 * @author liming 
	 * @param id 账期申请头ID
	 * @param status 状态
	 * @param userObject 
	 * @since JDK 1.7
	 */
	void updateStatus(String id, String status, UserObject userObject);
	/**
	 * 
	 * updateControlStatus:更新账期申请控制状态. <br/> 
	 * @author liming 
	 * @param id 账期申请头ID
	 * @param status 控制状态
	 * @param userObject 
	 * @since JDK 1.7
	 */
	void updateControlStatus(String id, String status, UserObject userObject);

	void updateAccounts(AccountsMaster accountsMaster, UserObject userObject)
			throws Exception;
}