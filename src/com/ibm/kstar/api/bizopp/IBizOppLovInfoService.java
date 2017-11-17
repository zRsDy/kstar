package com.ibm.kstar.api.bizopp;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;

public interface IBizOppLovInfoService {
	

	/**
	 * 
	 * getBizOppNameInfoList:获取商机名称信息List. <br/> 
	 * TODO(提供公共的查询下拉选择框).<br/>
	 * 
	 * @author gaoyuliang 
	 * @param condition	
	 * @return
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	List<BusinessOpportunity> getBizOppNameInfoList(PageCondition condition, UserObject userObject)
			throws AnneException;

}
