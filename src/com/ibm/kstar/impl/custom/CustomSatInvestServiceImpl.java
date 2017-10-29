package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.custom.ICustomSatInvestService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.custom.CustomSatInvest;


@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomSatInvestServiceImpl implements ICustomSatInvestService {
	@Autowired
	BaseDao baseDao;
	
	@Override
	public IPage querySatInvest(PageCondition condition) {
		StringBuffer hql = new StringBuffer(" from CustomSatInvest a where 1=1 and (a.statusCd <>'1100' or a.statusCd is null) ");
		List<Object> params = new ArrayList<Object>();
		if(condition.getCondition("investQuarter") != null && condition.getCondition("investQuarter") != ""){
			hql.append(" and a.investQuarter = ? ");
			params.add(condition.getCondition("investQuarter"));
		}
		if(condition.getCondition("orgId") != null && condition.getCondition("orgId") != ""){
			hql.append(" and a.areaOrIndustry = ? ");
			params.add(condition.getCondition("orgId"));
		}
		if(condition.getCondition("employeeId") != null && condition.getCondition("employeeId") != ""){
			hql.append(" and a.principal = ? ");
			params.add(condition.getCondition("employeeId"));
		}
		return baseDao.search(hql.toString(),params.toArray(),condition.getRows(), condition.getPage());
	}
	
	@Override
	public void updateSatInvestInfo(CustomSatInvest customSatInvest) throws AnneException {
		CustomSatInvest oldCustomSatInvest = baseDao.get(CustomSatInvest.class, customSatInvest.getId());
		if(oldCustomSatInvest == null){
			throw new AnneException(ICustomSatInvestService.class.getName() + " 没有找到要更新的数据");
		}
		
		if(!oldCustomSatInvest.getId().equals(oldCustomSatInvest.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customSatInvest, oldCustomSatInvest);
		baseDao.update(oldCustomSatInvest);
	}
	
	@Override
	public void saveSatInvestInfo(CustomSatInvest customSatInvest, UserObject userObject) throws AnneException{
		customSatInvest.setCreateEmployee(userObject.getEmployee().getId());
		customSatInvest.setCreatorOrgId(userObject.getOrg().getId());
		customSatInvest.setCreatorPosId(userObject.getPosition().getId());
		baseDao.save(customSatInvest);
	}
	
	@Override
	public void deleteSatInvestInfo(String id) throws AnneException{
		CustomSatInvest customSatInvest = baseDao.get(CustomSatInvest.class, id);
		customSatInvest.setStatusCd("1100");
		baseDao.update(customSatInvest);
	}
	
	@Override
	public CustomSatInvest getSatInvest(String id) throws AnneException{
		return baseDao.get(CustomSatInvest.class, id);
	}

	@Override
	public List<List<Object>> mappageExport(PageCondition condition) {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		
		addHead(lstOut);
		
		StringBuffer hql = new StringBuffer(" from CustomSatInvest a where 1=1 and (a.statusCd <>'1100' or a.statusCd is null) ");
		List<CustomSatInvest> list = baseDao.findEntity(hql.toString());
		
		if(list != null && list.size() > 0){
			for(CustomSatInvest invest : list){
				List<Object> lstIn = new ArrayList<Object>();
				lstIn.add(invest.getInvestQuarter());
				lstIn.add(invest.getInvestCode());
				lstIn.add(invest.getCreateDate());
				lstIn.add(invest.getPrincipalName());
				lstIn.add(invest.getAreaOrIndustryPathName());
				lstIn.add(invest.getServiceAttitudeName());
				lstIn.add(invest.getServiceAttitudeImprove());
				lstIn.add(invest.getBusinessAbilityName());
				lstIn.add(invest.getBusinessAbilityImprove());
				lstIn.add(invest.getCooperateAttitudeName());
				lstIn.add(invest.getCooperateAttitudeImprove());
				lstIn.add(invest.getSalesmanAdvice());
				
				lstOut.add(lstIn);
			}
		}
		return lstOut;
	}
	
	private void addHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("季度");
		lstHead.add("调查编号");
		lstHead.add("调查发起时间");
		lstHead.add("负责人");
		lstHead.add("办事处区域/行业");
		lstHead.add("服务态度");
		lstHead.add("服务态度改进");
		lstHead.add("业务能力");
		lstHead.add("业务能力改进");
		lstHead.add("工作的配合程度");
		lstHead.add("工作的配合程度改进");
		lstHead.add("销售对客服的合理化建议");
		lstOut.add(lstHead);
	}

	@Override
	public List<CustomSatInvest> getCustomSatInvestsByParam(Map<String, Object> paramsMap) {
		StringBuffer hql = new StringBuffer(" from CustomSatInvest a where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(!StringUtil.isNullOrEmpty(paramsMap.get("investQuarter"))){
			hql.append(" and a.investQuarter = ? ");
			params.add(paramsMap.get("investQuarter"));
		}
		if(!StringUtil.isNullOrEmpty(paramsMap.get("areaOrIndustry"))){
			hql.append(" and a.areaOrIndustry = ? ");
			params.add(paramsMap.get("areaOrIndustry"));
		}
		if(!StringUtil.isNullOrEmpty(paramsMap.get("principal"))){
			hql.append(" and a.principal = ? ");
			params.add(paramsMap.get("principal"));
		}
		return baseDao.findEntity(hql.toString(), params.toArray());
	}
}
