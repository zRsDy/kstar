package com.ibm.kstar.api.custom;

import java.util.List;
import java.util.Map;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomSatInvest;

public interface ICustomSatInvestService {
	
	/**
	 * 分页查询客户满意度调查信息
	 * @param condition
	 * @return
	 */
	public IPage querySatInvest(PageCondition condition);
	
	/**
	 * 修改客户满意度调查信息
	 * @param customSatInvest
	 * @throws AnneException
	 */
	public void updateSatInvestInfo(CustomSatInvest customSatInvest) throws AnneException;
	
	/**
	 * 新增客户满意度调查信息
	 * @param customSatInvest
	 * @throws AnneException
	 */
	public void saveSatInvestInfo(CustomSatInvest customSatInvest, UserObject userObject) throws AnneException;
	
	/**
	 * 删除客户满意度调查信息
	 * @param customSatInvest
	 * @throws AnneException
	 */
	public void deleteSatInvestInfo(String id) throws AnneException;
	
	/**
	 * 根据id获取客户满意度调查信息
	 * @param id
	 * @return
	 * @throws AnneException
	 */
	public CustomSatInvest getSatInvest(String id) throws AnneException;
	
	/**
	 * 导出客户满意度调查信息
	 * @param condition
	 * @return
	 */
	public List<List<Object>> mappageExport(PageCondition condition);
	
	/**
	 * 按条件查询客户满意度调查记录
	 * @param paramsMap
	 * @return
	 */
	public List<CustomSatInvest> getCustomSatInvestsByParam(Map<String,Object> paramsMap);
	
}
