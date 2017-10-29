package com.ibm.kstar.api.common.customlov;

import java.util.List;

import com.ibm.kstar.entity.custom.CustomInfo;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.common.customlov.Custom;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.entity.custom.CustomRelation;
import com.ibm.kstar.entity.product.ProductPriceHead;

public interface ICustomLovInfoService {

	List<BusinessOpportunity> queryBizOppByUser(PageCondition condition)
			throws AnneException;

	List<Contract> queryContractByUser(PageCondition condition)
			throws AnneException;

	/**
	 * 
	 * getCustomInfoList:获取客户信息List. <br/>
	 */
	List<Custom> getCustomInfoList(PageCondition condition)
			throws AnneException;
	
	List<CustomInfo> getCustomInfoListWithCondition(PageCondition condition, UserObject user)
			throws AnneException;
	
	List<CustomInfo> getCustomInfoListWithCondition(PageCondition condition)
			throws AnneException;

	List<CustomInfo> getCustomInfoListAuth(PageCondition condition)
			throws AnneException;
	/**
	 * 根据名称查找当前组织及上级符合的价格列表客户
	 * @param condition
	 * @return
	 * @throws AnneException
	 */
	List<ProductPriceHead> quotedPriceCustom(PageCondition condition,UserObject user)
			throws AnneException;

	List<CustomRelation> getCompanyList(PageCondition condition)
			throws AnneException;

	/**
	 * 
	 * getCustomRelaContactList:获取联系人信息List. <br/>
	 */
	List<CustomRelaContact> getCustomRelaContactList(Condition condition)
			throws AnneException;

	/**
	 * 
	 * getCustomAddressInfoList:(获取客户地址信息). <br/>
	 */
	List<CustomAddressInfo> getCustomAddressInfoList(Condition condition)
			throws AnneException;
	
	List<LovMember> getOrgList(Condition condition)
			throws AnneException;

    List<Custom> getCustomInfoListNoAuth(PageCondition condition);

	List<CustomInfo> getCustomInfoListWithConditionAndPos(PageCondition condition,UserObject userObject) throws AnneException;
	
	List<CustomAddressInfo> getCustomAddressInfoAllList(String userType,PageCondition condition)throws AnneException;

	List<CustomInfo> getCustomInfoListWithConditionBySalerCenter(PageCondition condition, LovMember lov)
			throws AnneException;
}
