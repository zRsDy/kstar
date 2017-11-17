package com.ibm.kstar.api.system.lov;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.util.List;
import java.util.Map;

public interface ILovMemberService {
	
	void save(LovMember lovMember) throws AnneException;
	
	boolean saveProduct(LovMember lovMember);

	void update(LovMember lovMember) throws AnneException;
	
	void updateTree(LovMember lovMember) throws AnneException;

	IPage query(PageCondition condition) throws AnneException;

	void delete(String lovMemberId) throws AnneException;
	
	void deletes(String[] lovMemberIds) throws AnneException;
	
	void deleteTree(String lovMemberId,String groupId) throws AnneException;
	
	void deleteByMatchID(String matchID) throws AnneException;
	
	LovMember get (String lovMemberId) throws AnneException;
	
//	LovMember getByCode (String lovMemberCode) throws AnneException;
	
	List<LovMember> getList(Condition condition) throws AnneException;
	
	List<LovMember> getList30(Condition condition) throws AnneException;
	
	List<LovMember> getList2(Condition condition,UserObject user) throws AnneException;
	
	List<Map<String, Object>> getSelectList(PageCondition condition,UserObject user) throws AnneException;

	/**
	 * 组织树
	 * @param condition
	 * @param user
	 * @return
	 * @throws AnneException
	 */
	List<Map<String, Object>> getSelectPriceOrgList(PageCondition condition,UserObject user) throws AnneException;
	
	List<LovMember> getListForUpdate(Condition condition,UserObject user) throws AnneException;
	
	List<LovMember> getList(Condition condition,UserObject user) throws AnneException;

	/**
	 * 将节点(dragNodeId)移入到新的父节点下(newParentNodeId)
	 * @param dragNodeId
	 * @param newParentNodeId
	 * @throws AnneException
	 */
	void move(String dragNodeId, String newParentNodeId) throws AnneException;

	List<LovMember> find(Condition condition) throws AnneException;

	List<LovMember> getLovMemberList4Cache(Condition condition) throws AnneException;


	LovMember getLovMemberByCode(String groupCode,String code) throws AnneException;
	
	LovMember getLovMemberByName(String groupCode,String name) throws AnneException;

	/**
	 * 
	 * @param groupCode
	 * @param id
	 * @return
	 * @throws AnneException
	 */
	LovMember getLovMemberByPositionId(String groupCode,String id) throws AnneException;
	
	/**
	 * 获取员工的角色列表
	 * @param userId
	 * @return
	 */
	List<LovMember> getRulesByUserId(String userId);

	/**产品定价对照明细选择职务*/
	List<LovMember> selectLpcLinePosition(Condition condition) throws AnneException;
	
	/**产品定价对照明细选择层级名称*/
	List<LovMember> selectLpcLineName(Condition condition) throws AnneException;
	
	/**产品定价对照选择组织*/
	List<LovMember> selectLpcOrg(Condition condition) throws AnneException;

	void importData(List<List<Object>> list) throws AnneException;
	/**根据应用code返回流程code*/
	String getFlowCodeByAppCode(String appCode) throws AnneException;
	
	String getAppIdByFlowCode(String flowCode) throws AnneException;

	List<LovMember> getSkipList(Condition condition);

	String getSaleCenter(String currentDepId);
	
	List<LovMember> getSaleCenters();

    LovMember getSaleCenterLovmember(String currentDepId);

    Object[] getSaleMethod(String department);


	List<LovMember> getDept(String optTxt3, String type,String search);
	
	List<LovMember> getListByGroupCode(String groupCode);

	void saveList(List<LovMember> lovs);
	
	boolean switchIsOpen(String groupCode, String lovCode);

	boolean isAgentForOrg(String orgId);

	LovMember getByProductModel(String productModel);

    List<List<Object>> getProductModelImportTemplet();

	void importProductModels(List<List<Object>> dataList, UserObject userObject);

	List<List<Object>> getExcelData(List<LovMember> list);
	
	/**
	 * 获取业务部门列表
	 * @return
	 */
	List<LovMember> getBizDeptList();
	
	/**
	 * 获取所有目录树
	 * @return
	 */
	List<LovMember> getAllCatalogList();
	
	/**
	 * 获取所有组织树
	 * @return
	 */
	List<LovMember> getAllOrgList();
}
