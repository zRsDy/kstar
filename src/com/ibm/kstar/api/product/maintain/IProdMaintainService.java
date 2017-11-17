package com.ibm.kstar.api.product.maintain;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.maintain.ProdAttrModLine;
import com.ibm.kstar.entity.product.maintain.ProdInfoMaintain;

public interface IProdMaintainService {
	
	/**
	 * 获取产品维护单编码
	 * @return
	 */
	String getMaintainCode();
	
	/**
	 * 分页查询维护单编码
	 * @param condition
	 * @return
	 */
	IPage getProdMaintainPage(PageCondition condition);
	
	/**
	 * 分页查询产品属性变更明细
	 * @param condition
	 * @return
	 */
	IPage getProdAttrModLinePage(PageCondition condition);
	
	/**
	 * 分页查询产品目录变更明细
	 * @param condition
	 * @return
	 */
	IPage getProdCatalogModLinePage(PageCondition condition);
	
	/**
	 * 保存产品维护单
	 * @param maintain
	 * @throws Exception 
	 */
	void saveProdMaintain(ProdInfoMaintain maintain,UserObject userObject) throws Exception;
	
	/**
	 * 根据主键获取产品维护单
	 * @param maintainId
	 * @return
	 */
	ProdInfoMaintain getProdInfoMaintain(String maintainId);
	
	/**
	 * 更新产品维护单
	 * @param prodInfoMaintain
	 * @param userObject
	 * @throws Exception 
	 */
	void updateProdMaintain(ProdInfoMaintain prodInfoMaintain, UserObject userObject) throws Exception;
	
	/**
	 * 分页查询产品销售状态变更明细
	 * @param condition
	 * @return
	 */
	IPage getSaleStatusModLinePage(PageCondition condition);
	
	/**
	 * 更新产品维护单的流程状态
	 * @param businessKey
	 * @param prodInfoMaintainProcStutas40
	 */
	void updateProcessStatus(String businessKey, String processStatus);
	
	/**
	 * 启动产品维护单审批流程
	 * @param maintain
	 * @param userObject
	 * @throws Exception 
	 */
	void startMaintainProcess(ProdInfoMaintain maintain, UserObject userObject) throws Exception;
	
	/**
	 * 产品信息维护删除(逻辑删除)
	 * @param id
	 */
	void deleteProdInfoMaintain(String id, UserObject userObject);
	
	/**
	 * 查询产品属性变更明细
	 * @param maintainId
	 * @return
	 */
	List<ProdAttrModLine> getProdAttrModLineList(String maintainId);
}
