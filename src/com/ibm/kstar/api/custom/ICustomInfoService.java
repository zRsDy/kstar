package com.ibm.kstar.api.custom;

import java.util.List;
import java.util.Map;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomDistrScope;
import com.ibm.kstar.entity.custom.CustomErpInfo;
import com.ibm.kstar.entity.custom.CustomFinancePinfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomInfoChange;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.entity.custom.CustomRelation;
import com.ibm.kstar.entity.custom.CustomRequireInfo;
import com.ibm.kstar.entity.custom.vo.CustomErpAddress;

public interface ICustomInfoService {
	//客户基本信息
	IPage query(PageCondition condition);
	
	void saveCustomInfo(CustomInfo customInfo, UserObject userObject) throws AnneException;
	
	CustomInfo getCustomInfo(String id) throws AnneException;

	void updateCustomInfo(CustomInfo customInfo) throws AnneException;

	void deleteCustomInfo(String id) throws AnneException;
	
	CustomInfo getCustomInfoByCode(String code) throws AnneException;
	
	CustomInfo getCustomInfoByCodeWithAuth(String code, UserObject user) throws AnneException;
	
	void saveCustomInfoChange(CustomInfoChange customInfoChange, UserObject userObject) throws AnneException;
	
	List<CustomInfoChange> getCustomInfoChangeByCustomId(String customId) throws AnneException;
	
	CustomInfoChange getCustomInfoChange(String id) throws AnneException;

	void updateCustomInfoChange(CustomInfoChange customInfoChange, String updateBaseFlg) throws AnneException;

//	void deleteCustomInfoChange(String id) throws AnneException;
	
	// 需求
	IPage queryRequire(PageCondition condition);
	
	void saveRequireInfo(CustomRequireInfo customRequireInfo) throws AnneException;
	
	CustomRequireInfo getRequireInfo(String id) throws AnneException;

	void updateRequireInfo(CustomRequireInfo customRequireInfo) throws AnneException;

	void deleteRequireInfo(String id) throws AnneException;
	
	List<CustomRequireInfo> getRequireInfoBycode(String code) throws AnneException;
	
	// 地址
	IPage queryAddr(PageCondition condition);
	
	void saveAddrInfo(CustomAddressInfo customAddressInfo) throws AnneException;
	
	CustomAddressInfo getAddrInfo(String id) throws AnneException;

	void updateAddrInfo(CustomAddressInfo customAddressInfo) throws AnneException;

	void deleteAddrInfo(String id) throws AnneException;
	
	List<CustomAddressInfo> getAddrInfoBycode(String code) throws AnneException;
	
	// 银行
	IPage queryBank(PageCondition condition);
	
	void saveBankInfo(CustomFinancePinfo customFinancePinfo) throws AnneException;
	
	CustomFinancePinfo getBankInfo(String id) throws AnneException;

	void updateBankInfo(CustomFinancePinfo customFinancePinfo) throws AnneException;

	void deleteBankInfo(String id) throws AnneException;
	
	List<CustomFinancePinfo> getBankInfoBycode(String code) throws AnneException;
	
	public boolean isErpCustom(String customId);
	
	// ERP引入
	IPage queryErp(PageCondition condition, String[] erpStatus);
	
	void saveErpInfo(CustomErpInfo customErpInfo) throws AnneException;
	
	CustomErpAddress getErpInfo(String id) throws AnneException;

	void updateErpInfo(CustomErpInfo customErpInfo, String flg) throws AnneException;
	
	void updateErpInfoAfterProcess(String businessKey, String status) throws AnneException;

	void deleteErpInfo(String id) throws AnneException;
	
	List<CustomErpAddress> getErpInfoByIdStatus(String id, String[] erpStatus) throws AnneException;
	
	// 公司关系
	IPage queryCompany(PageCondition condition);
	
	void saveCompanyInfo(CustomRelation customRelation) throws AnneException;
	
	CustomRelation getCompanyInfo(String id) throws AnneException;

	void updateCompanyInfo(CustomRelation customRelation) throws AnneException;

	void deleteCompanyInfo(String id) throws AnneException;
	
	List<CustomRelation> getCompanyInfoBycode(String code) throws AnneException;
	
	// 公司关系-联系人
	IPage queryContact(PageCondition condition);
	
	void saveContactInfo(CustomRelaContact customRelaContact, UserObject userObject) throws AnneException;
	
	CustomRelaContact getContactInfo(String id) throws AnneException;

	void updateContactInfo(CustomRelaContact customRelaContact) throws AnneException;

	void deleteContactInfo(String id) throws AnneException;
	
	List<CustomRelaContact> getContactInfoBycode(String code) throws AnneException;
	
	// 经销范围
	IPage queryScope(PageCondition condition);
	
	void saveScopeInfo(CustomDistrScope customDistrScope) throws AnneException;
	
	CustomDistrScope getScopeInfo(String id) throws AnneException;

	void updateScopeInfo(CustomDistrScope customDistrScope) throws AnneException;

	void deleteScopeInfo(String id) throws AnneException;
	
	List<CustomDistrScope> getScopeInfoBycode(String code) throws AnneException;
	/**
	 * 
	 * getCustomErpInfoByCustomId:根据客户ID获取客户对应的业务实体. <br/> 
	 * 
	 * @author liming 
	 * @param customId 客户ID
	 * @return 
	 * @since JDK 1.7
	 */
	CustomErpInfo getCustomErpInfoByCustomId(String customId);
	/**
	 * 
	 * getAddrInfo1ByCustomId:根据客户ID获取客户对应的注册地址. <br/> 
	 * 
	 * @author liming 
	 * @param customId
	 * @return 
	 * @since JDK 1.7
	 */
	CustomAddressInfo getAddrInfo1ByCustomId(String customId);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<CustomAddressInfo> getCustomAddressInfoListById(String id);
	
	/**
	 * 
	 * @param module
	 * @param businessKey
	 * @param userObject
	 * @param varmap
	 */
	public void startChangeProcess(String module,String businessKey,UserObject userObject,Map<String,String> varmap);
	
	
	public List<CustomAddressInfo> getCustomAddressInfoBycustomId(String customId);
	
	List<CustomRelaContact> getCustomRelaContactListAuth(String userType);
	


}
